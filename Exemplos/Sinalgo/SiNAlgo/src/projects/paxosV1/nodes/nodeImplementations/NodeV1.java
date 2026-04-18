/**
 * @author Meslin
 *
 */
package projects.paxosV1.nodes.nodeImplementations;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import projects.paxosV1.Consenso;
import projects.paxosV1.Consenso.Stage;
import projects.paxosV1.CustomGlobal;
import projects.paxosV1.Sequencial;
import projects.paxosV1.Tabela;
import projects.paxosV1.nodes.messages.MsgGenerica;
import projects.paxosV1.nodes.messages.convite.MsgAYCoordinator;
import projects.paxosV1.nodes.messages.convite.MsgAYThere;
import projects.paxosV1.nodes.messages.convite.MsgInvitation;
import projects.paxosV1.nodes.messages.paxos.acceptor.MsgAccepted;
import projects.paxosV1.nodes.messages.paxos.acceptor.MsgPrepareResponse;
import projects.paxosV1.nodes.messages.paxos.proposer.MsgAcceptRequest;
import projects.paxosV1.nodes.messages.paxos.proposer.MsgPrepareRequest;
import projects.paxosV1.nodes.timers.convite.TimerAcceptAnswer;
import projects.paxosV1.nodes.timers.convite.TimerCheckCoordinator;
import projects.paxosV1.nodes.timers.convite.TimerCheckMembers;
import projects.paxosV1.nodes.timers.convite.TimerMerge;
import projects.paxosV1.nodes.timers.convite.TimerPeriodico;
import projects.paxosV1.nodes.timers.convite.TimerT1Merge;
import projects.paxosV1.nodes.timers.convite.TimerT2Merge;
import projects.paxosV1.nodes.timers.paxos.TimerConsenso;
import projects.paxosV1.nodes.timers.paxos.TimerStage1;
import sinalgo.configuration.Configuration;
import sinalgo.configuration.CorruptConfigurationEntryException;
import sinalgo.configuration.WrongConfigurationException;
import sinalgo.gui.transformation.PositionTransformation;
import sinalgo.nodes.Node;
import sinalgo.nodes.messages.Inbox;
import sinalgo.runtime.Global;
import sinalgo.tools.Tools;

public class NodeV1 extends Node
{
	/** depuração: fase que o nó se encontra */
	public String fase;
	/** estado da eleição */
	public enum EnumState {NORMAL, ELECTION, REORGANIZING}
	/** estado do nó */
	public EnumState state;
	/** conjunto dos membros do próprio grupo */
	public List<Node> upSet;	
	/** conjunto dos membros da união dos grupos */
	public List<Node> up; 
	/** identificação do grupo formado por (CoordId <<16 + count) */
	public Long group; 
	/** conjunto de outros coordenadores descobertos */
	public List<Node> others;
	/** coordenador desse nó (0 sem coordenador) */
	public Node coordinator;
	/** contador de mudancas de coordenacao */
	public int counter;
	/** indicador de mensagens recebidas e reenviadas */
	private Tabela tabela;
	/** número de respostas ao convite de merge */
	public int numberAnswers;
	/** estado atual da aplicação */
	public int definition;

	/*
	 * Timers do convite
	 */
	public TimerMerge timerMerge;
	public TimerPeriodico timerPeriodico;
	public TimerCheckCoordinator timerCheckCoordinator;
	public TimerT1Merge timerT1Merge;
	public TimerT2Merge timerT2Merge;
	public TimerAcceptAnswer timerAcceptAnswer;
	public TimerCheckMembers timerCheckMembers;

	
	/*
	 * timers do Paxos
	 */
	public TimerStage1 timerStage1;
	public TimerConsenso timerConsenso;

	
	/** contador sequencial de todos os tipos de mensagens enviadas por esse nó */
	public Sequencial sequencial;

	// informações relativas ao consenso de Paxos
	public Consenso consenso;
	public int round;

	
	
	/**
	 * Contrutor do nó
	 */
	public NodeV1() {
		// cria os timers do convite
		this.timerMerge = new TimerMerge(this);
		this.timerPeriodico = new TimerPeriodico(this);
		this.timerCheckCoordinator = new TimerCheckCoordinator(this);
		this.timerT1Merge = new TimerT1Merge(this);
		this.timerT2Merge = new TimerT2Merge(this);
		this.timerAcceptAnswer = new TimerAcceptAnswer(this);
		this.timerCheckMembers = new TimerCheckMembers(this);
		
		// cria os timers do Paxos
		this.timerStage1 = new TimerStage1(this);
		this.timerConsenso = new TimerConsenso(this);
		
		// cria os objetos
		this.up = new ArrayList<Node>();
		this.sequencial = new Sequencial();
		this.tabela = new Tabela();
		this.counter = 1;
		this.numberAnswers = 0;
		this.definition = 0;
		this.group = CustomGlobal.makeGroup(this.ID, this.counter);
		
		// inicializa variáveis do algoritmo do convite
		this.state = EnumState.NORMAL;
		this.coordinator = this;
		
		// inicia as variáveis do algoritmo do Paxos
		round = 0;			// número do round corrente (0 = ainda não começou)
		consenso = new Consenso(this);
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
	public void neighborhoodChange() {
	}

	
	// Timers são executados nesse ponto
	// entre o preStep() e o handlerMessages()
	
	
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
			msg.acao(this);		// reação às mensagens recebidas
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
	public void draw(Graphics graphics, PositionTransformation pt, boolean highlight) {
		String texto;
		Color textColor;
		int r=0, g=0, b=0;
		
		if(CustomGlobal.paxos) {
			if(consenso.stage == Stage.FIM) {
				texto = String.format("%02d", this.consenso.learnerWinnerNumber) + "/" + String.format("%02d", this.consenso.learnerWinnerValue);
			}
			else if(this.consenso.isProposer && this.consenso.isAcceptor && this.consenso.isAcceptorProposed) {
				texto = String.format("%02d", this.consenso.acceptorAcceptedNumber) + "/" + String.format("%02d", this.consenso.acceptorAcceptedValue);
			}
			else if(this.consenso.isProposer) {
				texto = String.format("%02d", this.consenso.prepareRequestProposalNumber) + "/" + String.format("%02d", this.consenso.prepareRequestProposalValue);
			}
			else if(this.consenso.isAcceptor) {
				if(this.consenso.isAcceptorProposed) texto = String.format("%02d", this.consenso.acceptorAcceptedNumber) + "/" + String.format("%02d", this.consenso.acceptorAcceptedValue);
				else texto = "--/--";
			}
			else if(this.consenso.learnerWinner) texto = String.format("%02d", this.consenso.learnerWinnerNumber) + "/" + String.format("%02d", this.consenso.learnerWinnerValue);
			else texto = "??/??";
			if(this.consenso.isProposer) r = 255;
			if(this.consenso.isAcceptor) g = 255;
			this.setColor(new Color(r, g, b));
			
			textColor = new Color(255-r, 255-g, 255-b);
		}
		else {
			texto = String.format("%02d", this.ID) + "/" + String.format("%02d", this.coordinator.ID);
			textColor = Color.YELLOW;
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
			if(this.ID > this.coordinator.ID) {
				this.setColor(Color.BLACK);
			}
		}

		this.drawingSizeInPixels = 2;

		// desenha o raio do alcance do rádio
		// não sei porque o último raio está errado
		int x = pt.guiX;
		int y = pt.guiY;
		int raio = drawingSizeInPixels;
		try {
			raio = (int) (Configuration.getDoubleParameter("UDG/rMax") * pt.getZoomFactor());
		} catch (CorruptConfigurationEntryException e) {
			e.printStackTrace();
		}
		if(CustomGlobal.showRadio) {
			graphics.setColor(Color.GRAY);
			graphics.drawArc(x -raio, y -raio, 2*raio, 2*raio, 0, 360);
		}
		
		this.drawNodeAsDiskWithText(graphics, pt, highlight, texto, 1, textColor);
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
			MsgAYCoordinator msgAYCoordinator = new MsgAYCoordinator(this, null, this.ID);
			msgAYCoordinator.setSequencial(sequencial.valor(msgAYCoordinator));
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
		MsgAYThere msgAYThere = new MsgAYThere(this, this.coordinator, this.group);
		msgAYThere.setSequencial(sequencial.valor(msgAYThere));
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
		
		consenso.reset();
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
			this.up = new ArrayList<Node>();
			CustomGlobal.consoleln(Global.currentTime + " nó " + this + " tem " + upSet.size() + " vizinhos up e sabe de " + others.size() + " coordenadores");
			this.timerT1Merge.start(timerT1Merge.getDefault(), this);
			MsgInvitation msgInvitation = new MsgInvitation(this, null, this, 0, this.group);
			for(Node destino: this.upSet) {
				msgInvitation.setNoDestino(destino);
				msgInvitation.setSequencial(this.sequencial.valor(msgInvitation));
				CustomGlobal.consoleln(Global.currentTime + " no " + this + " enviando convite para " + msgInvitation.getNoDestino());
				this.broadcast(msgInvitation);
			}
			for(Node destino: others) {
				msgInvitation.setNoDestino(destino);
				msgInvitation.setSequencial(this.sequencial.valor(msgInvitation));
				CustomGlobal.consoleln(Global.currentTime + " no " + this + " enviando convite coordenador para " + msgInvitation.getNoDestino());
				this.broadcast(msgInvitation);
			}
		}
	}

	/**
	 * Para o processamento da aplicação<br>
	 */
	public void stopProcessing() {
		// TODO
	}
	
	
	
	/**
	 * Realiza o roteamento das mensagens<br>
	 * @param msg mensagem a ser roteada
	 * @return verdadeiro se a mensagem (também) deve ser tratada localmente
	 */
	private boolean roteamento(MsgGenerica msg) {
		//CustomGlobal.consoleln(Global.currentTime + " nó " + this + " recebeu mensagem " + msg.getSequencial() + " " + msg.getClass().getSimpleName() + " de " + msg.getNoOrigem() + " dizendo: " + msg.toString());
		// verifica se deve repassar a mensagem para os outros nós
		if(msg.getNoOrigem().ID != this.ID &&
		   msg.getSequencial() > tabela.getSequencia(msg.getNoOrigem().ID, msg.getClass().getSimpleName())) {
			tabela.setSequencia(msg.getNoOrigem().ID, msg.getClass().getSimpleName(), msg.getSequencial());
			this.broadcast(msg);
		}
		else {
			//CustomGlobal.consoleln(Global.currentTime + " nó " + this + " descartou mensagem já tratada " + msg.getSequencial() + " " + msg.getClass().getSimpleName() + " de " + msg.getNoOrigem() + " dizendo: " + msg.toString());
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
	
	/**
	 * 
	 */
	@NodePopupMethod(menuText="Estado do nó")
	public void estadoDoNo() {
		Tools.appendToOutput("Estado do nó " + this.ID + ": " + this.state + " fase: " + fase);
	}


	/**
	 * Tratamento de mensagens Accept Request
	 * @param msgAcceptRequest	mensagem
	 */
	public void recebeuMsgAcceptRequest(MsgAcceptRequest msgAcceptRequest) {
		if(consenso.isAcceptor) {
			if(msgAcceptRequest.proposalNumber >= consenso.acceptorGreaterProposalNumber) {
				CustomGlobal.consoleln(Global.currentTime + " nó " + this + " consenso: acceptor aceitou proposta ganhadora # " + msgAcceptRequest.proposalNumber + " com valor " + msgAcceptRequest.proposalValue);
				consenso.stage = Stage.FIM;
				MsgAccepted msgAccepted = new MsgAccepted(this, null, msgAcceptRequest.proposalValue);
				msgAccepted.setSequencial(sequencial.valor(msgAccepted));
				this.broadcast(msgAccepted);
			}
		}
	}


	/**
	 * Tratamento de mensagens Prepare Request
	 * @param msgPrepareRequest
	 */
	public void recebeuMsgPrepareRequest(MsgPrepareRequest msgPrepareRequest) {
		// somente vai tomar alguma providência se for um ACCEPTOR
		if(consenso.isAcceptor) {
			timerStage1.stop();	// aceito a proposta, logo não pode mais propor nada, ou seja, fim do estágio 1 para esse nó
			if(!consenso.isAcceptorProposed) {
				CustomGlobal.consoleln(Global.currentTime + " nó " + this + " consenso: acceptor recebeu de " + msgPrepareRequest.getNoOrigem() + " primeira proposta #" + msgPrepareRequest.proposalNumber + " com valor " + msgPrepareRequest.proposalValue);
				consenso.isAcceptorProposed = true;
				MsgPrepareResponse msgPrepareResponse = new MsgPrepareResponse(
						this, 											// origem é esse nó
						msgPrepareRequest.getNoOrigem(),				// o destino é a origem da mensagem prepare
						false,											// ainda não recebeu proposta				
						msgPrepareRequest.proposalNumber,				// número do valor aceito 	
						msgPrepareRequest.proposalValue					// valor aceito
				);
				msgPrepareResponse.setSequencial(sequencial.valor(msgPrepareResponse));
				broadcast(msgPrepareResponse);
				consenso.acceptorAcceptedNumber = msgPrepareRequest.proposalNumber;
				consenso.acceptorAcceptedValue = msgPrepareRequest.proposalValue;
				consenso.acceptorGreaterProposalNumber = msgPrepareRequest.proposalNumber;
			}
			else if(msgPrepareRequest.proposalNumber > consenso.acceptorGreaterProposalNumber && consenso.stage != Consenso.Stage.FIM) {
				CustomGlobal.consoleln(Global.currentTime + " nó " + this + " consenso: acceptor recebeu de " + msgPrepareRequest.getNoOrigem() + " proposta maior #" + msgPrepareRequest.proposalNumber + ">" + consenso.acceptorGreaterProposalNumber + " com valor " + msgPrepareRequest.proposalValue);
				consenso.acceptorGreaterProposalNumber = msgPrepareRequest.proposalNumber;
				MsgPrepareResponse msgPrepareResponse = new MsgPrepareResponse(
						this, 
						msgPrepareRequest.getNoOrigem(), 
						true,
						consenso.acceptorAcceptedNumber,
						consenso.acceptorAcceptedValue
				);
				msgPrepareResponse.setSequencial(sequencial.valor(msgPrepareResponse));
				broadcast(msgPrepareResponse);
				consenso.acceptorAcceptedNumber = msgPrepareRequest.proposalNumber;	// apenas o número muda, o valor continua sendo o primeiro a chegar
			}
		}
	}


	/**
	 * Tratamento de mensagens Prepare Response
	 * @param msgPrepareResponse
	 */
	public void recebeuMsgPrepareResponse(MsgPrepareResponse msgPrepareResponse) {
		if(!msgPrepareResponse.hasPrevious) {
			CustomGlobal.consoleln(Global.currentTime + " nó " + this + " consenso: proposer recebeu de " + msgPrepareResponse.getNoOrigem() + " proposta melhor " + msgPrepareResponse.proposalNumber + ">" + consenso.proposerWinnerProposal + " com valor " + msgPrepareResponse.proposalValue + " e agora está com " + consenso.nPrepareResponses + " de " + Consenso.nAcceptors);
			consenso.nPrepareResponses++;
			CustomGlobal.consoleln(Global.currentTime + " nó " + this + " consenso: agora com " + consenso.nPrepareResponses + " de " + Consenso.nAcceptors);
			if(msgPrepareResponse.proposalNumber > consenso.proposerWinnerProposal) {
				consenso.proposerWinnerProposal = msgPrepareResponse.proposalNumber;
				consenso.proposerWinnerValue = msgPrepareResponse.proposalValue;
			}
			if(consenso.nPrepareResponses > Consenso.nAcceptors/2) {
				CustomGlobal.consoleln(Global.currentTime + " nó " + this + " consenso: proposer ganhou o consenso # " + msgPrepareResponse.proposalNumber + " com valor " + msgPrepareResponse.proposalValue);
				MsgAcceptRequest msgAcceptRequest = new MsgAcceptRequest(this, null, consenso.prepareRequestProposalNumber, consenso.proposerWinnerValue);
				msgAcceptRequest.setSequencial(sequencial.valor(msgAcceptRequest));
				broadcast(msgAcceptRequest);
			}
		}
	}


	/**
	 * Tratamento de mensagens do tipo Accepted
	 * @param msgAccepted
	 */
	public void recebeuMsgAccepted(MsgAccepted msgAccepted) {
		CustomGlobal.consoleln(Global.currentTime + " nó " + this + " consenso: alguém recebeu proposta ganhadora com valor " + msgAccepted.getProposalValue());
		// nós chegaram a um consenso
		timerConsenso.stop();
		timerStage1.stop();
		consenso.stage = Stage.FIM;	// chegou ao final do consenso
		timerConsenso.stop();			// evita dar timeout na tentativa de consenso
		if(!consenso.isProposer && !consenso.isAcceptor) {
			CustomGlobal.consoleln(Global.currentTime + " nó " + this + " consenso: learner recebeu proposta ganhadora valor " + msgAccepted.getProposalValue());
			// esse nó é apenas um learner
			consenso.learnerWinner = true;
		}
		// todos usam esses valores depois que o consenso termina
		consenso.learnerWinnerNumber = msgAccepted.getProposalNumber();
		consenso.learnerWinnerValue = msgAccepted.getProposalValue();
	}
}
