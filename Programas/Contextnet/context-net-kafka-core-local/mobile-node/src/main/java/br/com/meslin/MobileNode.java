package br.com.meslin;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import ckafka.data.SwapData;
import lac.cnclib.net.NodeConnection;
import lac.cnclib.sddl.message.ApplicationMessage;;
import lac.cnclib.sddl.message.Message;
import main.java.ckafka.mobile.CKMobileNode;
import main.java.ckafka.mobile.tasks.SendLocationTask;

public class MobileNode extends CKMobileNode {
    // Opções válidas de entrada do usuário
    private static final String OPTION_GROUPCAST = "G";
    private static final String OPTION_UNICAST = "I";
    private static final String OPTION_PN = "P";
    private static final String OPTION_EXIT = "Z";

    // a variável não pode ser local porque está sendo usada em uma função lambda
    // Controle do loop eterno até que ele termine
    private boolean fim = false;


    /**
     * main
     * <br>
     * @param args command line arguments
     */
    public static void main(String[] args) {
        Scanner keyboard = new Scanner(System.in);
        MobileNode mn = new MobileNode();
        mn.runMN(keyboard);

        // Calls close() to properly close MN method after shut down
        Runtime.getRuntime().addShutdownHook(new Thread( () -> { close();}));
    }

	/**
	 * Executa o Mobile Node.
     * <br>
	 * Read user option from keyboard (unicast or groupcast message)<br>
	 * Read destination receipt from keyboard (UUID or Group)<br>
	 * Read message from keyboard<br>
	 * Send message<br>
	 */
    private void runMN(Scanner keyboard) {
        Map<String, Consumer<Scanner>> optionsMap = new HashMap<>();

        // Mapeia as opções para as funções correspondentes
//        optionsMap.put(OPTION_GROUPCAST, this::sendGroupcastMessage);
        optionsMap.put(OPTION_UNICAST, this::sendUnicastMessage);
        optionsMap.put(OPTION_PN, this::sendMessageToPN);
        optionsMap.put(OPTION_EXIT, scanner -> fim = true);

        while(!fim) {
            System.out.print("Mensagem para (G)rupo ou (I)ndivíduo (P)rocessing Node (Z para terminar)? ");
            String linha = keyboard.nextLine().trim().toUpperCase();
            System.out.printf("Sua opção foi %s.", linha);
            if(optionsMap.containsKey(linha)) optionsMap.get(linha).accept(keyboard);
            else System.out.printf("Opção %s inválida.\n", linha);
        }
        keyboard.close();
        System.out.println("FIM!");
        System.exit(0);
    }

    /**
     * Quando estiver conectado, enviar localização a cada instante
     */
    @Override
    public void connected(NodeConnection nodeConnection) {
        try {
            logger.debug("Connected");
            final SendLocationTask sendlocationtask = new SendLocationTask(this);
            this.scheduledFutureLocationTask = this.threadPool.scheduleWithFixedDelay(
                sendlocationtask, 5000, 60000, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            logger.error("Error scheduling SendLocationTask", e);
        }
    }

    /**
     * Lê a mensagem via linha de comando do usuário.
     * <br>
     * Envia uma mensagem em unicast
     */
    private void sendUnicastMessage(Scanner keyboard) {
        System.out.println("Mensagem unicast. Entre com o UUID do indivíduo: ");
        String uuid = keyboard.nextLine();
        System.out.print("Entre com a mensagem: ");
        String messageText = keyboard.nextLine();
        System.out.println(String.format("Mensagem de |%s| para %s.", messageText, uuid));
        // Create and send the message
        SwapData privateData = new SwapData();
        privateData.setMessage(messageText.getBytes(StandardCharsets.UTF_8));
        privateData.setTopic("PrivateMessageTopic");
        privateData.setRecipient(uuid);
        ApplicationMessage message = createDefaultApplicationMessage();
        message.setContentObject(privateData);
        sendMessageToGateway(message);
    }

    /**
     * Recebe mensagens
     */
    @Override
    public void newMessageReceived(NodeConnection nodeConnection, Message message) {
        try {
            SwapData swp = fromMessageToSwapData(message);
            if(swp.getTopic().equals("Ping")) {
                    message.setSenderID(this.mnID);
                    sendMessageToGateway(message);
            } else {
                    String str = new String(swp.getMessage(), StandardCharsets.UTF_8);
                logger.info("Message: " + str);
            }
        } catch (Exception e) {
            logger.error("Error reading new message received");
        }
    }

    /**
     * Envia mensagem para o Processing Node estacionário
     */
    private void sendMessageToPN(Scanner keyboard) {
        System.out.print("Entre com a mensagem: ");
        String messageText = keyboard.nextLine();
    
        ApplicationMessage message = createDefaultApplicationMessage();
        SwapData data = new SwapData();
        data.setMessage(messageText.getBytes(StandardCharsets.UTF_8));
        data.setTopic("AppModel");
        message.setContentObject(data);
        sendMessageToGateway(message);
    }

    @Override
    public void internalException(NodeConnection arg0, Exception arg1) {
        // TODO Auto-generated method stub
    }
    @Override
    public void unsentMessages(NodeConnection arg0, List<Message> arg1) {
        // TODO Auto-generated method stub
    }
    @Override
    public void disconnected(NodeConnection arg0) {
        // TODO Auto-generated method stub
    }

    /**
     * Send groupcast message
     * @param keyboard
     */
    private void sendGroupcastMessage(Scanner keyboard) {
        String group;
        System.out.print("Mensagem groupcast. Entre com o número do grupo: ");
        group = keyboard.nextLine();
        System.out.print("Entre com a mensagem: ");
        String messageText = keyboard.nextLine();
        System.out.println(String.format("Mensagem de |%s| para o grupo %s.", messageText, group));
        // Create and send the message
        SwapData groupData = new SwapData();
        groupData.setMessage(messageText.getBytes(StandardCharsets.UTF_8));
        groupData.setTopic("GroupMessageTopic");
        groupData.setRecipient(group);
        ApplicationMessage message = createDefaultApplicationMessage();
        message.setContentObject(groupData);
        sendMessageToGateway(message);
    }    
}
