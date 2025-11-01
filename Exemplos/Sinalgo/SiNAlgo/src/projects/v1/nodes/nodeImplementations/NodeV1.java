/**
 * 
 */
package projects.v1.nodes.nodeImplementations;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import projects.v1.CustomGlobal;
import projects.v1.Sequencial;
import projects.v1.Tabela;
import projects.v1.nodes.messages.MsgAYCoordinator;
import projects.v1.nodes.messages.MsgAYCoordinatorAnswer;
import projects.v1.nodes.messages.MsgAYThere;
import projects.v1.nodes.messages.MsgAYThereAnswer;
import projects.v1.nodes.messages.MsgAccept;
import projects.v1.nodes.messages.MsgAcceptAnswer;
import projects.v1.nodes.messages.MsgGenerica;
import projects.v1.nodes.messages.MsgInvitation;
import projects.v1.nodes.messages.MsgReady;
import projects.v1.nodes.messages.MsgReadyAnswer;
import projects.v1.nodes.timers.TimerAcceptAnswer;
import projects.v1.nodes.timers.TimerCheckCoordinator;
import projects.v1.nodes.timers.TimerCheckMembers;
import projects.v1.nodes.timers.TimerMerge;
import projects.v1.nodes.timers.TimerPeriodico;
import projects.v1.nodes.timers.TimerT1Merge;
import projects.v1.nodes.timers.TimerT2Merge;
import sinalgo.configuration.Configuration;
import sinalgo.configuration.CorruptConfigurationEntryException;
import sinalgo.configuration.WrongConfigurationException;
import sinalgo.gui.transformation.PositionTransformation;
import sinalgo.nodes.Node;
import sinalgo.nodes.messages.Inbox;
import sinalgo.runtime.Global;
import sinalgo.tools.Tools;

/**
 * @author Meslin
 *
 */
public class NodeV1 extends Node
{
	// depuração
	public String fase;
	
	public enum EnumState {NORMAL, ELECTION, REORGANIZING}
	/** estado do nó */
	public EnumState state;
	/** conjunto dos membros do próprio grupo */
	private List<Node> upSet;	
	/** conjunto dos membros da união dos grupos */
	public List<Node> up; 
	/** identificação do grupo formado por (CoordId <<16 + count) */
	public Long group; 
	/** conjunto de outros coordenadores descobertos */
	public List<Node> others;
	/** coordenador desse nó (0 sem coordenador) */
	public Node coordinator;
	/** contador de mudancas de coordenacao */
	private int counter;
	/** indicador de mensagens recebidas e reenviadas */
	private Tabela tabela;
	/** número de respostas ao convite de merge */
	public int numberAnswers;
	/** estado atual da aplicação */
	public int definition;

	/*
	 * Timers
	 */
	public TimerMerge timerMerge;
	public TimerPeriodico timerPeriodico;
	public TimerCheckCoordinator timerCheckCoordinator;
	public TimerT1Merge timerT1Merge;
	public TimerT2Merge timerT2Merge;
	public TimerAcceptAnswer timerAcceptAnswer;
	public TimerCheckMembers timerCheckMembers;
	
	/** contador sequencial de todos os tipos de mensagens enviadas por esse nó */
	public Sequencial sequencial;

	/**
	 * Contrutor do nó
	 */
	public NodeV1()
	{
		// cria os timers
		this.timerMerge = new TimerMerge(this);
		this.timerPeriodico = new TimerPeriodico(this);
		this.timerCheckCoordinator = new TimerCheckCoordinator(this);
		this.timerT1Merge = new TimerT1Merge(this);
		this.timerT2Merge = new TimerT2Merge(this);
		this.timerAcceptAnswer = new TimerAcceptAnswer(this);
		this.timerCheckMembers = new TimerCheckMembers(this);
		
		// cria os objetos
		this.up = new ArrayList<Node>();
		this.sequencial = new Sequencial();
		this.tabela = new Tabela();
		this.counter = 1;
		this.numberAnswers = 0;
		this.definition = 0;
		this.group = CustomGlobal.makeGroup(this.ID, this.counter);
		
		// inicializa variáveis do algoritmo
		this.state = EnumState.NORMAL;
		this.coordinator = this;
	}

	/* (non-Javadoc)
	 * @see sinalgo.nodes.Node#init()
	 */
	@Override
	public void init() {
		CustomGlobal.consoleln(Global.currentTime + " nó " + this + " em init()");
		timerPeriodico.startRelative(1, this);	// inicia verificando se existem outros coordenadores
	}

	/* (non-Javadoc)
	 * @see sinalgo.nodes.Node#preStep()
	 */
	@Override
	public void preStep() {
	}

	/* (non-Javadoc)
	 * @see sinalgo.nodes.Node#neighborhoodChange()
	 */
	@Override
	public void neighborhoodChange()
	{
	}

	// Timers são executados nesse ponto
	
	/* (non-Javadoc)
	 * @see sinalgo.nodes.Node#handleMessages(sinalgo.nodes.messages.Inbox)
	 */
	@Override
	public void handleMessages(Inbox inbox)
	{
		while(inbox.hasNext()) {
			MsgGenerica msg = (MsgGenerica) inbox.next();
			if(!roteamento(msg)) continue;

			CustomGlobal.consoleln(Global.currentTime + " nó " + this + " recebeu mensagem " + msg.getSequencial() + " " + msg.getClass().getSimpleName() + " de " + msg.getNoOrigem() + " dizendo: " + msg.toString());

			/*
			 * reação às mensagens recebidas
			 */
			/*
			 * AYCoordinatorAnswer
			 * checkMembers (continuação)
			 */
			if(msg instanceof MsgAYCoordinatorAnswer) { // resposta de uma mensagem sobre coordenação
				MsgAYCoordinatorAnswer msgAYCoordinatorAnswer = (MsgAYCoordinatorAnswer) msg;
				if(msgAYCoordinatorAnswer.isCoordinator()) {
					fase = msg.getClass().getSimpleName();
					this.others.add(msgAYCoordinatorAnswer.getNoOrigem());
				}
			}
			
			// Quando um Membro suspeita de falha do Coordenador (continuação)
			// checkCoordinator
			/*
			 * AYThereAnswer
			 */
			if(msg instanceof MsgAYThereAnswer) {
				MsgAYThereAnswer msgAYThereAnswer = (MsgAYThereAnswer) msg;
				this.timerCheckCoordinator.stop();
				if(msgAYThereAnswer.isCoordinator()) {
					fase = msg.getClass().getSimpleName() + " true";
				}
				else {
					fase = msg.getClass().getSimpleName() + " false";
					CustomGlobal.consoleln(Global.currentTime + " nó " + this + " em recovery porque o coordenador " + msgAYThereAnswer.getNoOrigem() + " respondeu não: " + CustomGlobal.formataGrupo(msgAYThereAnswer.getGroupLocal()) + "/" + CustomGlobal.formataGrupo(msgAYThereAnswer.getGroupRemote()));
					recovery();
					CustomGlobal.estatistica(this.ID, "recovery4");
				}
			}

			/*
			 * ReadyAnswer
			 * merge (continuação)
			 */
			if(msg instanceof MsgReadyAnswer) {
				MsgReadyAnswer msgReadyAnswer = (MsgReadyAnswer) msg;
				if(msgReadyAnswer.isInGroup() && msgReadyAnswer.getGroup() == this.group) {
					fase = msg.getClass().getSimpleName();
					this.numberAnswers++;
				}
			}
			
			// Thread para tratar um Convite
			/*
			 * Invitation
			 */
			if(msg instanceof MsgInvitation) {
				this.timerMerge.stop();
				MsgInvitation msgInvitation = (MsgInvitation) msg;
				if(this.state == EnumState.NORMAL) {
					fase = msg.getClass().getSimpleName();
					this.stopProcessing();
					Node oldCoordinator = this.coordinator;
					this.upSet = this.up;
					this.state = EnumState.ELECTION;
					this.coordinator = msgInvitation.getCoordenador();
					this.group = msgInvitation.getGroup();
					if(oldCoordinator.ID == this.ID) {
						for(Node noDestino: this.upSet) {
							msgInvitation.setSequencial(++this.sequencial.sequencialMsgInvitation);
							msgInvitation.setNoOrigem(this);
							msgInvitation.setNoDestino(noDestino);
							this.broadcast(msgInvitation);
						}
					}
					MsgAccept msgAccept = new MsgAccept(this, this.coordinator, ++this.sequencial.sequencialMsgAccept, this.group);
					this.broadcast(msgAccept);
					this.timerAcceptAnswer.start(this.timerAcceptAnswer.getDefault(), this);
				}
				else {
					CustomGlobal.estatistica(this.ID, "recusado", Integer.toString(msgInvitation.getNoOrigem().ID));
				}
			}
			/*
			 * AcceptAnswer
			 */
			if(msg instanceof MsgAcceptAnswer) {
				MsgAcceptAnswer msgAcceptAnswer = (MsgAcceptAnswer) msg;
				if(msgAcceptAnswer.isInGroup()) {
					fase = msg.getClass().getSimpleName();
					CustomGlobal.consoleln(Global.currentTime + " nó " + this + " indo para o estado reorganizing porque recebeu acceptanswer");
					this.state = EnumState.REORGANIZING;
					this.timerAcceptAnswer.stop();
				}
			}
			
			// Thread Monitor para responder às mensagens
			/*
			 * Ready
			 */
			if(msg instanceof MsgReady) {		// receiving a message from process k
				MsgReady msgReady = (MsgReady) msg;
				MsgReadyAnswer msgReadyAnswer = new MsgReadyAnswer(this, this.coordinator, ++this.sequencial.sequencialMsgReadyAnswer, true, this.group);
				if(this.group == msgReady.getGroup() && this.state == EnumState.REORGANIZING) {
					fase = msg.getClass().getSimpleName() + " true";
					this.definition++;		// only if in Reorganizing
					this.state = EnumState.NORMAL;
					msgReadyAnswer.setInGroup(true);
				}
				else {
					msgReadyAnswer.setInGroup(false);
				}
				this.broadcast(msgReadyAnswer);
			}
			
			/*
			 * AYCoordinator
			 */
			if(msg instanceof MsgAYCoordinator) {
				MsgAYCoordinator msgAYCoordinator = (MsgAYCoordinator) msg;
				MsgAYCoordinatorAnswer msgAYCoordinatorAnswer = new MsgAYCoordinatorAnswer(this, msgAYCoordinator.getNoOrigem(), true, ++this.sequencial.sequencialMsgAYCoordinatorAnswer);
				if(this.state == EnumState.NORMAL && this.coordinator.ID == this.ID) {
					fase = msg.getClass().getSimpleName() + " true";
					msgAYCoordinatorAnswer.setCoordinator(true);
				}
				else {
					msgAYCoordinatorAnswer.setCoordinator(false);
				}
				this.broadcast(msgAYCoordinatorAnswer);
			}
			
			/*
			 * Are You There? 
			 * mensagem de nó para coordenador
			 */
			if(msg instanceof MsgAYThere) {
				MsgAYThere msgAYThere = (MsgAYThere) msg;
				MsgAYThereAnswer msgAYThereAnswer = new MsgAYThereAnswer(this, msgAYThere.getNoOrigem(), ++this.sequencial.sequencialMsgAYTAnswer, true, CustomGlobal.makeGroup(this.ID, this.counter), msgAYThere.getGroup());
				if(this.group == msgAYThere.getGroup() && this.coordinator.ID == this.ID) {
					fase = msg.getClass().getSimpleName() + " true";
					msgAYThereAnswer.setCoordinator(true);
				}
				else {
					fase = msg.getClass().getSimpleName() + " false";
					msgAYThereAnswer.setCoordinator(false);
				}
				this.broadcast(msgAYThereAnswer);
			}
			
			/*
			 * Accept
			 */
			if(msg instanceof MsgAccept) {
				MsgAccept msgAccept = (MsgAccept) msg;
				MsgAcceptAnswer msgAcceptAnswer = new MsgAcceptAnswer(this, msgAccept.getNoOrigem(), ++this.sequencial.sequencialMsgAcceptAnswer, true);
				if(this.state == EnumState.ELECTION && this.coordinator.ID == this.ID && this.group == msgAccept.getGroup()) {
					fase = msg.getClass().getSimpleName() + " true";
					CustomGlobal.consoleln(Global.currentTime + " nó " + this + " aceitou " + msgAccept.getNoOrigem().ID + " no grupo");
					// only if in Election and for new Group
					this.up.add(msgAccept.getNoOrigem());		// Up is used by Merge()
					msgAcceptAnswer.setInGroup(true);
				}
				else {
					fase = msg.getClass().getSimpleName() + " false";
					msgAcceptAnswer.setInGroup(false);
				}
				this.broadcast(msgAcceptAnswer);
			}
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
	
	/* (non-Javadoc)
	 * @see sinalgo.nodes.Node#draw(java.awt.Graphics, sinalgo.gui.transformation.PositionTransformation, boolean)
	 */
	public void draw(Graphics g, PositionTransformation pt, boolean highlight) {
		switch (this.state) {
		case NORMAL:
			this.setColor(Color.BLUE);
			break;

		case ELECTION:
			this.setColor(Color.GREEN);
			break;

		case REORGANIZING:
			this.setColor(Color.RED);
			break;

		default:
			break;
		}
		this.drawingSizeInPixels = 2;

		int x = pt.guiX;
		int y = pt.guiY;
		int raio = drawingSizeInPixels;
		try {
			raio = (int) (Configuration.getDoubleParameter("UDG/rMax") * pt.getZoomFactor());
		} catch (CorruptConfigurationEntryException e) {
			e.printStackTrace();
		}
		if(CustomGlobal.showRadio) {
			g.setColor(Color.GRAY);
			g.drawArc(x -raio, y -raio, 2*raio, 2*raio, 0, 360);
//			CustomGlobal.consoleln("Nó " + this + " Posicao = " + x + ", " + y + " com raio = " + raio);
		}
		this.drawNodeAsDiskWithText(g, pt, highlight, String.format("%02d", this.ID) + "/" + String.format("%02d", this.coordinator.ID), 1, Color.YELLOW);
	}

	/**
	 * Coordenador procura por outros coordenadores<br>
	 * Periodicamente, cada coordenador verifica se consegue se comunicar com outro coordenador 
	 * (e coleta na variável Others os PIDs correspondentes)<br>
	 * Chamado periodicamente pelo TimerPeriodico<br>
	 */
	public void checkMembers() {
		if(state == EnumState.NORMAL && coordinator.ID == this.ID) {
			CustomGlobal.consoleln(Global.currentTime + " nó " + this + " verificando membros do grupo " + CustomGlobal.formataGrupo(this.group));
			this.others = new ArrayList<Node>();
			MsgAYCoordinator msgAYCoordinator = new MsgAYCoordinator(this, null, this.ID, ++this.sequencial.sequencialMsgAYCoordinator);
			this.broadcast(msgAYCoordinator);	// a mensagem tem que ser enviada em broadcast porque o nó não sabe exatamente quem são os seus vizinhos
			this.timerCheckMembers.start(this.timerCheckMembers.getDefault(), this);
		}
	}

	/**
	 * Verifica se o coordenador está ativo ou falhou.<br>
	 * Esse método é chamado periodicamente pelo TimerPeriodico<br>
	 */
	public void checkCoordinator() {
		if(this.coordinator.ID == this.ID) return;
		MsgAYThere msgAYThere = new MsgAYThere(this, this.coordinator, ++this.sequencial.sequencialMsgAYThere, this.group);
		this.broadcast(msgAYThere);
		this.timerCheckCoordinator.start(this.timerCheckCoordinator.getDefault(), this);
	}

	/**
	 * Recupera o coordenador quando o antigo coordenador falha<br>
	 * Membro simplesmente cria um novo grupo contendo somente ele próprio.<br>
	 */
	public void recovery()
	{
		CustomGlobal.consoleln(Global.currentTime + " nó " + this + " em recovery");
		this.state = EnumState.ELECTION;
		stopProcessing();
		this.counter++;
		this.group = CustomGlobal.makeGroup(this.ID, this.counter);
		this.coordinator = this;
		this.up = new ArrayList<Node>();
		this.state = EnumState.REORGANIZING;
		this.definition ++;		// ((A single node task description is computed and placed in S(i)d.))
		this.state = EnumState.NORMAL;
	}

	/**
	 * Realiza o merge de grupos baseados nos coordenadores<br>
	 * @param others (aka coordSet) lista de coordenadores encontrados
	 */
	public void merge(List<Node> others)
	{
		CustomGlobal.consoleln(Global.currentTime + " Nó " + this + " em merge com status " + this.state);
		if(this.coordinator.ID == this.ID && this.state == EnumState.NORMAL) {
			CustomGlobal.estatistica(this.ID, "merge");
			this.state = EnumState.ELECTION;
			stopProcessing();
			this.counter++;
			this.group = CustomGlobal.makeGroup(this.ID, this.counter);	// creates a new group
			this.upSet = this.up;	// upSet: members of previous group
			CustomGlobal.consoleln(Global.currentTime + " nó " + this + " tem " + up.size() + " vizinhos up e sabe de " + others.size() + " coordenadores");
			this.up = new ArrayList<Node>();
			this.timerT1Merge.startRelative(timerT1Merge.getDefault(), this);
			MsgInvitation msgInvitation = new MsgInvitation(this, null, this, 0, this.group);
			for(Node destino: this.upSet) {
				msgInvitation.setNoDestino(destino);
				msgInvitation.setSequencial(++this.sequencial.sequencialMsgInvitation);
				this.broadcast(msgInvitation);
			}
			for(Node destino: others) {
				msgInvitation.setNoDestino(destino);
				msgInvitation.setSequencial(++this.sequencial.sequencialMsgInvitation);
				this.broadcast(msgInvitation);
			}
		}
	}

	/**
	 * Para o processamento da aplicação<br>
	 */
	private void stopProcessing() {
		// TODO
	}
	
	/**
	 * Realiza o roteamento das mensagens<br>
	 * @param msg mensagem a ser roteada
	 * @return verdadeiro se a mensagem (também) deve ser tratada localmente
	 */
	private boolean roteamento(MsgGenerica msg) {
		// verifica se deve repassar a mensagem para os outros nós
		if(msg.getNoOrigem().ID != this.ID &&
		   msg.getSequencial() > tabela.getSequencia(msg.getNoOrigem().ID, msg.getClass().getSimpleName())) {
			tabela.setSequencia(msg.getNoOrigem().ID, msg.getClass().getSimpleName(), msg.getSequencial());
			this.broadcast(msg);
		}
		else {
			// se a mensagem já foi tratada, ela pode ser descartada e vamos pegar a próxima mensagem
			// se a origem da mensagem foi esse nó, ela também pode ser descartada
			return false;
		}
		// somente mensagem broadcast ou uniscast para esse destino devem ser tratadas localmente
		// mensagens unicast para outro destino não devem ser tratadas localmente
		if(msg.getNoDestino() != null && msg.getNoDestino().ID != this.ID) {
			return false;
		}
		return true;
	}
	
	@NodePopupMethod(menuText="Estado do nó")
	public void estadoDoNo() {
		Tools.appendToOutput("Estado do nó " + this.ID + ": " + this.state + " fase: " + fase);
	}
}
