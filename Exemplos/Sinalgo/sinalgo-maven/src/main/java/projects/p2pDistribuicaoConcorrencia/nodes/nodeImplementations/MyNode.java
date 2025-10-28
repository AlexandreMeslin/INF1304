/**
 * @author Meslin
 */
package projects.p2pDistribuicaoConcorrencia.nodes.nodeImplementations;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;

import p2pDistribuicaoConcorrencia.nodes.messages.IdMessage;
import p2pDistribuicaoConcorrencia.nodes.messages.LastSavedRecordMessage;
import p2pDistribuicaoConcorrencia.nodes.messages.NextRecordNumberMessage;
import p2pDistribuicaoConcorrencia.nodes.messages.RecordListMessage;
import projects.p2pDistribuicaoConcorrencia.CustomGlobal;
import projects.p2pDistribuicaoConcorrencia.RecordCollectionManager;
import projects.p2pDistribuicaoConcorrencia.RecordEntry;

import sinalgo.configuration.Configuration;
import sinalgo.exception.WrongConfigurationException;
import sinalgo.gui.transformation.PositionTransformation;
import sinalgo.nodes.Node;
import sinalgo.nodes.edges.Edge;
import sinalgo.nodes.messages.Inbox;
import sinalgo.nodes.messages.Message;
import sinalgo.runtime.Global;
import sinalgo.tools.Tools;

/**
 * @author Meslin
 * Implementation of a node for P2P data distribution with concurrency control
 */
public class MyNode extends Node {
	/** Animation frames for the node */
	private char[] animacao = {'|', '/', '-', '\\'};
	/** Current position in the animation frames */
	private int posAnimacao = 0;
	/** this node previous neighbors */
	private Set<Node> previousNeighbors;
	/** A list of all node data (from this and other nodes) */
	private List<RecordCollectionManager> collectionManager;


	/**
	 * Initializes the node, setting up data structures and starting the record creation thread.
	 * Overrides the init method from the Node class.
	 * Sets up the previousNeighbors set and initializes the collectionManager list.
	 * Starts a new thread for the RecordCreator task to generate records periodically.
	 * @see sinalgo.nodes.Node#init()
	 */
	@Override
	public void init() {
		CustomGlobal.consoleln(Global.currentTime + " nó " + this + " em init()");
		this.previousNeighbors = new HashSet<>();
		this.collectionManager = new ArrayList<>();
        RecordCollectionManager collection = new RecordCollectionManager(ID);
        this.collectionManager.add(collection);

		RecordCreator recordCreatorTask = new RecordCreator();
		Thread recordCreatorThread = new Thread(recordCreatorTask);
		recordCreatorThread.start();
	}

	/* (non-Javadoc)
	 * @see sinalgo.nodes.Node#preStep()
	 */
	@Override
	public void preStep() {
	}

	
	/**
	 * Handles changes in the node's neighborhood.
	 * Detects new neighbors and sends them an IdMessage containing this node's ID.
	 * Updates the previousNeighbors set to reflect the current neighbors.
	 * Overrides the neighborhoodChange method from the Node class.
	 * @see sinalgo.nodes.Node#neighborhoodChange()
	 */
	@Override
	public void neighborhoodChange() {
		List<RecordCollectionManager> collectionManagerCloned;
		Set<Node> currentNeighbors = new HashSet<>();

		// Itera sobre as arestas de saída para encontrar os vizinhos atuais
		// outgoingConnections é uma lista de arestas conectadas a este nó
		// Cada aresta tem um nó de destino (endNode)
		// Adiciona cada nó de destino ao conjunto de vizinhos atuais
		// Isso cria um conjunto dos nós atualmente conectados a este nó
		// outgoingConnections é atualizado automaticamente pelo Sinalgo e herdado de Node
		for (Edge edge : this.outgoingConnections) {
			currentNeighbors.add(edge.endNode);
		}

		// Detecta novos vizinhos
		for (Node neighbor : currentNeighbors) {
			if (!previousNeighbors.contains(neighbor)) {
				// Se chegamos aqui, é porque 'neighbor' é um novo vizinho
				// Envia mensagem se apresentando para o novo vizinho
				synchronized(collectionManager) {
					collectionManagerCloned = new ArrayList<>(collectionManager);
				}
				// Envia mensagem de ID para o novo vizinho
				// Para cada coleção de registros, envia uma mensagem de ID
				// Isso informa ao novo vizinho sobre os IDs dos nós cujos registros este nó possui
				// O novo vizinho pode então solicitar registros com base nesses IDs
				for(RecordCollectionManager collection : collectionManagerCloned) {
					IdMessage idMessage = new IdMessage(collection.getNodeId());
					this.send(idMessage, neighbor);
				}
				collectionManagerCloned = null;
			}
		}

		// Atualiza o conjunto de vizinhos anteriores
		previousNeighbors = currentNeighbors;
	}

	
	// Timers são executados nesse ponto
	// entre o preStep() e o handlerMessages()
	
	
	/**
	 * Handles incoming messages for the node.
	 * Processes each message in the inbox using the MessageProcessor class.
	 * Overrides the handleMessages method from the Node class.
	 * 
	 * @param inbox The inbox containing incoming messages.
	 * @see sinalgo.nodes.Node#handleMessages(sinalgo.nodes.messages.Inbox)
	 */
	@Override
	public void handleMessages(Inbox inbox) {
		while(inbox.hasNext()) {
			Message msg = inbox.next();
			Node sender = inbox.getSender();
			MessageProcessor messageProcessor = new MessageProcessor();
			messageProcessor.processMessage(sender, msg);
		}
	}

	/* (non-Javadoc)
	 * @see sinalgo.nodes.Node#postStep()
	 */					
	@Override
	public void postStep() {
	}

	/* (non-Javadoc)
	 * @see sinalgo.nodes.Node#checkRequirements()
	 */
	@Override
	public void checkRequirements() throws WrongConfigurationException {
	}
	

	/**
	 * Draws the node with animation and highlights.
	 * Changes color based on highlight status and displays node ID with animation frame.
	 * Overrides the draw method from the Node class.
	 * 
	 * @param graphics The graphics context to draw on.
	 * @param pt The position transformation for drawing.
	 * @param highlight Whether the node should be highlighted.
	 * @see sinalgo.nodes.Node#draw(java.awt.Graphics, sinalgo.gui.transformation.PositionTransformation, boolean)
	 */
	public void draw(Graphics graphics, PositionTransformation pt, boolean highlight) {
		int nodeSize = 10;
        // set the color of this node
		if (highlight) {
            this.setColor(Color.RED);
        } else {
            this.setColor(Color.BLUE);
        }	

		//this.setColor(new Color((float) 0.5, (float) 0.5, (float) 1.0));
        String text = this.ID + Character.toString(this.animacao[this.posAnimacao/100]) + collectionManager.get(0).getLastSavedRecord();
		this.posAnimacao = (this.posAnimacao+1) % (this.animacao.length * 100);
        // draw the node as a circle with the text inside
        super.drawNodeAsDiskWithText(graphics, pt, highlight, text, 3, Color.YELLOW);
	}


	/**
	 * Processes incoming messages and delegates handling based on message type.
	 * Uses a map of message types to handler functions for extensibility.
	 * @author Meslin
	 */
	private class MessageProcessor {
		/** Map of message types to handler functions */
		private Map<Class<? extends Message>, BiConsumer<? extends Node, ? extends Message>> handlers = new HashMap<>();

		/**
		 * Constructor that initializes the message handlers.
		 * Sets up handlers for different message types.
		 * @author Meslin
		 */
		public MessageProcessor() {
			handlers.put(IdMessage.class,               (sender, msg) -> handleIdMessage              ((Node)sender, (IdMessage)msg));
			handlers.put(LastSavedRecordMessage.class,  (sender, msg) -> handleLastSavedRecordMessage ((Node)sender, (LastSavedRecordMessage) msg));
			handlers.put(NextRecordNumberMessage.class, (sender, msg) -> handleNextRecordNumberMessage((Node)sender, (NextRecordNumberMessage) msg));
			handlers.put(RecordListMessage.class,       (sender, msg) -> handleRecordListMessage      ((Node)sender, (RecordListMessage) msg));
		}


		/**
		 * Processes a message by delegating to the appropriate handler based on message type.
		 * 
		 * @param sender
		 * @param msg
		 */
		public void processMessage(Node sender, Message msg) {
			BiConsumer<Node, Message> handler = (BiConsumer<Node, Message>) handlers.get(msg.getClass());
			if (handler != null) {
				handler.accept(sender, msg);
			} else {
				handleUnknownMessageType(msg);
			}
		}


		/**
		 * Update new neighbor about already saved messages
		 * Send request for next record
		 * 
		 * @param msg ID message
		 */
		private void handleIdMessage(Node sender, IdMessage msg) {
			// Tratar Message
			// Enviar lastSavedRecordNumber
			sendLastSavedRecordNumber(sender, msg.getNodeId());
			// Enviar nextRecordNumber
			sendNextRecordNumber(sender, msg.getNodeId());
		}

		/**
		 * Delete messages that were reported to be already at the stationary server
		 * 
		 * @param sender The node that sent the message
		 * @param msg last saved record number message (inclusive)
		 */
		private void handleLastSavedRecordMessage(Node sender, LastSavedRecordMessage msg) {
			int nodeId = msg.getNodeId();
			int lastSavedRecord = msg.getLastSavedRecordNumber();
			RecordCollectionManager collectionManager =null;

			synchronized(MyNode.this.collectionManager) {
				for(RecordCollectionManager collection : MyNode.this.collectionManager) {
					if(collection.getNodeId() == nodeId) {
						collectionManager = collection;
						break;
					}					
				}
			}

			if(collectionManager != null) {
				List<RecordEntry> recordEntries = collectionManager.getRecordList();
				synchronized(recordEntries) {
					recordEntries.removeIf(entry -> entry.getRecordNumber() <= msg.getLastSavedRecordNumber());
				}
				if(lastSavedRecord > collectionManager.getLastSavedRecord()) collectionManager.setLastSavedRecord(lastSavedRecord);
				//CustomGlobal.consoleln("Nó " + MyNode.this.ID + " atualizando last saved record de " + msg.getNodeId() + " com " + lastSavedRecord);
			}
		}


		/**
		 * Send all new records to the new neighbor
		 * 
		 * @param sender The node that sent the message
		 * @param msg next record number message
		 */
		private void handleNextRecordNumberMessage(Node sender, NextRecordNumberMessage msg) {
			int nodeId = msg.getNodeId();
			int nextMessage = msg.getNextRecordNumber();
			RecordCollectionManager manager =null; 
			List<RecordEntry> outgoingRecords = new ArrayList<>();

			synchronized(MyNode.this.collectionManager) {
				for(RecordCollectionManager collection : MyNode.this.collectionManager) {
					if(collection.getNodeId() == nodeId) {
						manager = collection;
						break;
					}
				}
			}

			// antes, verifica se encontrou algum registro 
			if(manager != null) {
				synchronized(collectionManager) {
					for(RecordEntry entry : manager.getRecordList()) {
						if(entry.getRecordNumber() >= nextMessage) {
							outgoingRecords.add(entry);
						}
					}
				}

				if(outgoingRecords.size() > 0) {
					RecordListMessage recordList = new RecordListMessage(ID, outgoingRecords);
					send(recordList, sender);
				} 
			}
		}


		/**
		 * Update local record list with received records
		 * 
		 * @param sender The node that sent the message
		 * @param msg record list message
		 */
		private void handleRecordListMessage(Node sender, RecordListMessage msg) {
			boolean found = false;
			RecordCollectionManager collectionManager = null;

			for(RecordCollectionManager manager : MyNode.this.collectionManager) {
				if(manager.getNodeId() == msg.getNodeId()) {
					found = true;
					collectionManager = manager;
					break;
				}
			}
			if(!found) {
				collectionManager = new RecordCollectionManager(msg.getNodeId());
				MyNode.this.collectionManager.add(collectionManager);
			}
			if(msg.getRecordList().size() > 0) {
				collectionManager.getRecordList().addAll(msg.getRecordList());
				collectionManager.setNextRecord(msg.getRecordList().get(msg.getRecordList().size()-1).getRecordNumber());
			}
		}

		/**
		 * Send last saved record number to the requester
		 * 
		 * @param sender The node that sent the message
		 * @param nodeId The node ID for which to send the last saved record number
		 */
		protected void sendLastSavedRecordNumber(Node sender, int nodeId) {
			int lastSavedRecord = -1;
			boolean found = false;

			synchronized(MyNode.this.collectionManager) {
				for(RecordCollectionManager colletion : MyNode.this.collectionManager) {
					if(colletion.getNodeId() == nodeId) {
						lastSavedRecord = colletion.getLastSavedRecord();
						found = true;
						break;
					}
				}
			}
			LastSavedRecordMessage message = new LastSavedRecordMessage(nodeId, lastSavedRecord);
			MyNode.this.send(message, sender);
		}

		/**
		 * Send next record number to the requester
		 * 
		 * @param sender The node that sent the message
		 * @param nodeId The node ID for which to send the next record number
		 */
		protected void sendNextRecordNumber(Node sender, int nodeId) {
			int nextRecordNumber = -1;
			boolean found = false;

			synchronized(MyNode.this.collectionManager) {
				for(RecordCollectionManager colletion : MyNode.this.collectionManager) {
					if(colletion.getNodeId() == nodeId) {
						nextRecordNumber = colletion.getNextRecord();
						found = true;
						break;
					}
				}
			}
			NextRecordNumberMessage message = new NextRecordNumberMessage(nodeId, nextRecordNumber);
			MyNode.this.send(message, sender);
		}

		/**
		 * Handle (unlikely) unknown message
		 * 
		 * @param msg The unknown message
		 */
		private void handleUnknownMessageType(Message msg) {
			// Tratar mensagens desconhecidas
			// TODO: Implementar tratamento para mensagens desconhecidas
		}
	}

	/**
	 * Thread that periodically creates new records for the node.
	 * Implements Runnable to allow execution in a separate thread.
	 * Generates new records with incrementing counter values and random data.
	 * Sleeps for a random interval between 10 to 20 seconds between record creations.
	 * @author Meslin
	 */
	private class RecordCreator implements Runnable {
		/** Record counter */
		private int counter;
	
		/**
		 * Constructor that initializes the record counter.
		 * Sets the initial counter value to zero.
		 */
		public RecordCreator() {
			this.counter = 0;
		}

		/**
		 * Runs the record creation loop.
		 * Continuously creates new records and adds them to the collection manager.
		 * Sleeps for a random interval between 10 to 20 seconds between record creations.
		 * Overrides the run method from the Runnable interface.
		 * 
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run() {
			try {
				while(true) {
					RecordEntry record = new RecordEntry(this.counter, "Data: " + this.counter++);
					collectionManager.get(0).setNextRecord(this.counter);
					synchronized(collectionManager.get(0)) {
						collectionManager.get(0).getRecordList().add(record);
					}
					Thread.sleep(10*1000 + (int)(Math.random()*10*1000));
				}
			} catch (Exception e) {
				CustomGlobal.consoleln("***** ERROR ***** " + e.getStackTrace());
			}
		}
	}
}
