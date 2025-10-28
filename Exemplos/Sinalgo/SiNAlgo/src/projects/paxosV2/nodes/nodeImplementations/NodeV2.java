/**
 * @author Meslin 
 *
 */
package projects.paxosV2.nodes.nodeImplementations;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import projects.paxosV2.Consenso;
import projects.paxosV2.Consenso.Stage;
import projects.paxosV2.CustomGlobal;
import projects.paxosV2.Sequencial;
import projects.paxosV2.Tabela;
import projects.paxosV2.nodes.messages.MsgGenerica;
import projects.paxosV2.nodes.messages.convite.MsgAYCoordinator;
import projects.paxosV2.nodes.messages.convite.MsgAYThere;
import projects.paxosV2.nodes.messages.convite.MsgInvitation;
import projects.paxosV2.nodes.messages.convite.MsgReady;
import projects.paxosV2.nodes.messages.paxos.acceptor.MsgAccepted;
import projects.paxosV2.nodes.messages.paxos.acceptor.MsgPrepareResponse;
import projects.paxosV2.nodes.messages.paxos.proposer.MsgAcceptRequest;
import projects.paxosV2.nodes.messages.paxos.proposer.MsgPrepareRequest;
import projects.paxosV2.nodes.timers.convite.TimerAcceptAnswer;
import projects.paxosV2.nodes.timers.convite.TimerCheckCoordinator;
import projects.paxosV2.nodes.timers.convite.TimerCheckMembers;
import projects.paxosV2.nodes.timers.convite.TimerMerge;
import projects.paxosV2.nodes.timers.convite.TimerPeriodico;
import projects.paxosV2.nodes.timers.convite.TimerT1Merge;
import projects.paxosV2.nodes.timers.convite.TimerT2Merge;
import projects.paxosV2.nodes.timers.paxos.TimerConsenso;
import projects.paxosV2.nodes.timers.paxos.TimerStage1;
import sinalgo.configuration.Configuration;
import sinalgo.configuration.CorruptConfigurationEntryException;
import sinalgo.configuration.WrongConfigurationException;
import sinalgo.gui.transformation.PositionTransformation;
import sinalgo.nodes.Node;
import sinalgo.nodes.messages.Inbox;
import sinalgo.runtime.Global;
import sinalgo.tools.Tools;

public abstract class NodeV2 extends Node
{
	/** depuração: fase que o nó se encontra */
	public String fase;
	/** estado da eleição */
	public enum EnumState {NORMAL, ELECTION, REORGANIZING}
	/** papeis do Paxos */
	public enum EnumPaxosRole {LEARNER, ACCEPTOR, PROPOSER, ACCEPTOR_PROPOSER}
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
	public NodeV2(EnumPaxosRole role) {
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
		consenso = new Consenso(this, role);
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
			else if(this.consenso.isProposer() && this.consenso.isAcceptor() && this.consenso.isAcceptorProposed()) {
				texto = String.format("%02d", this.consenso.acceptorAcceptedNumber) + "/" + String.format("%02d", this.consenso.acceptorAcceptedValue);
			}
			else if(this.consenso.isProposer()) {
				texto = String.format("%02d", this.consenso.getPrepareRequestProposalNumber()) + "/" + String.format("%02d", this.consenso.getPrepareRequestProposalValue());
			}
			else if(this.consenso.isAcceptor()) {
				if(this.consenso.isAcceptorProposed()) texto = String.format("%02d", this.consenso.acceptorAcceptedNumber) + "/" + String.format("%02d", this.consenso.acceptorAcceptedValue);
				else texto = "--/--";
			}
			else if(this.consenso.learnerWinner) texto = String.format("%02d", this.consenso.learnerWinnerNumber) + "/" + String.format("%02d", this.consenso.learnerWinnerValue);
			else texto = "??/??";
			if(this.consenso.isProposer()) r = 255;
			if(this.consenso.isAcceptor()) g = 255;
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
			//CustomGlobal.estatistica(this.ID, "merge");
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
		// verifica se deve repassar a mensagem para os outros nós
		// a mensagem será roteada se:
		// - (1) não tiver sido originada nesse nó (verificar noOrigem da msg)
		// - (2) não tiver sido recebida anteriormente por esse nó (verificar sequência e tipo da msg)
		// - (3) o destino não for unicamente esse nó (verificar noDestino da msg)
		if(msg.getNoOrigem().ID != this.ID &&	// (1)
		   msg.getSequencial() > tabela.getSequencia(msg.getNoOrigem().ID, msg.getClass().getSimpleName())) {	// (2)
			// mensagem nova de outro nó
			// acrescenta na lista de msg já recebidas
			tabela.setSequencia(msg.getNoOrigem().ID, msg.getClass().getSimpleName(), msg.getSequencial());
			if(msg.getNoDestino() == null) {
				// mensagem em broadcast
				// repasssa a mensagem e avisa que deve ser tratada localmente
				this.broadcast(msg);
				return true;
			}
			else {
				// mensagem em unicast
				// verifica se é para esse nó
				if(msg.getNoDestino().ID == this.ID) {
					return true;
				}
				else {
					// mensagem em unicast para outro nó
					// deve ser repassada
					// não deve ser tratada localmente
					this.broadcast(msg);
					return false;
				}
			}
		}
		else {
			// mensagem velha
			// já está na tabela de mensagens
			// não deve ser repassada
			// não deve ser tratada localmente
			return false;
		}
	}
	
	/**
	 * 
	 */
	@NodePopupMethod(menuText="Estado do nó")
	public void estadoDoNo() {
		Tools.appendToOutput("Estado do nó " + this.ID + ": " + this.state + " fase: " + fase);
	}


	/**
	 * Paxos:<br>
	 * Tratamento de mensagens Accept Request
	 * @param msgAcceptRequest	mensagem
	 */
	public void recebeuMsgAcceptRequest(MsgAcceptRequest msgAcceptRequest) {
		if(consenso.isAcceptor()) {
			if(msgAcceptRequest.proposalNumber >= consenso.acceptorGreaterProposalNumber) {
				CustomGlobal.consoleln(Global.currentTime + " nó " + this + " consenso: acceptor aceitou proposta ganhadora #" + msgAcceptRequest.proposalNumber + " com valor " + msgAcceptRequest.proposalValue);
				consenso.stage = Stage.FIM;
				MsgAccepted msgAccepted = new MsgAccepted(this, null, msgAcceptRequest.proposalValue);
				msgAccepted.setSequencial(sequencial.valor(msgAccepted));
				this.broadcast(msgAccepted);
			}
			else {
				CustomGlobal.consoleln(Global.currentTime + " nó " + this + " consenso: acceptor rejeitou proposta ganhadora #" + msgAcceptRequest.proposalNumber + " com valor " + msgAcceptRequest.proposalValue + " porque tinha #" + consenso.acceptorGreaterProposalNumber);
			}
		}
	}


	/**
	 * Paxos:<br>
	 * Tratamento de mensagens Prepare Request<br>
	 * Acceptor recebe a mensagem
	 * @param msgPrepareRequest
	 */
	public void recebeuMsgPrepareRequest(MsgPrepareRequest msgPrepareRequest) {
		// somente vai tomar alguma providência se for um ACCEPTOR
		if(consenso.isAcceptor()) {
			timerStage1.stop();	// aceito a proposta, logo não pode mais propor nada, ou seja, fim do estágio 1 para esse nó
			if(!consenso.isAcceptorProposed()) {
				CustomGlobal.consoleln(Global.currentTime + " nó " + this + " consenso: acceptor recebeu de " + msgPrepareRequest.getNoOrigem() + " primeira proposta #" + msgPrepareRequest.proposalNumber + " com valor " + msgPrepareRequest.proposalValue);
				consenso.setAcceptorProposed(true);
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
				consenso.acceptorGreaterProposalNumber = consenso.acceptorAcceptedNumber;
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
	 * Paxos:<br>
	 * Tratamento de mensagens Prepare Response
	 * @param msgPrepareResponse
	 */
	public void recebeuMsgPrepareResponse(MsgPrepareResponse msgPrepareResponse) {
		if(!msgPrepareResponse.hasPrevious) {
			CustomGlobal.consoleln(Global.currentTime + " nó " + this + " consenso: proposer recebeu de " + msgPrepareResponse.getNoOrigem() + " proposta melhor " + msgPrepareResponse.proposalNumber + ">" + consenso.proposerWinnerProposal + " com valor " + msgPrepareResponse.proposalValue + " e agora está com " + consenso.nPrepareResponses + " de " + Consenso.getNAcceptors());
			consenso.nPrepareResponses++;
			CustomGlobal.consoleln(Global.currentTime + " nó " + this + " consenso: agora com " + consenso.nPrepareResponses + " de " + Consenso.getNAcceptors());
			if(msgPrepareResponse.proposalNumber > consenso.proposerWinnerProposal) {
				consenso.proposerWinnerProposal = msgPrepareResponse.proposalNumber;
				consenso.proposerWinnerValue = msgPrepareResponse.proposalValue;
			}
			if(consenso.nPrepareResponses > Consenso.getNAcceptors()/2) {
				CustomGlobal.consoleln(Global.currentTime + " nó " + this + " consenso: proposer ganhou o consenso # " + msgPrepareResponse.proposalNumber + " com valor " + msgPrepareResponse.proposalValue);
				MsgAcceptRequest msgAcceptRequest = new MsgAcceptRequest(this, null, consenso.getPrepareRequestProposalNumber(), consenso.proposerWinnerValue);
				msgAcceptRequest.setSequencial(sequencial.valor(msgAcceptRequest));
				broadcast(msgAcceptRequest);
			}
		}
	}


	/**
	 * Paxos:<br>
	 * Tratamento de mensagens do tipo Accepted
	 * @param msgAccepted
	 */
	public void recebeuMsgAccepted(MsgAccepted msgAccepted) {
		if(!CustomGlobal.enviouFimConsenso) {
			CustomGlobal.estatistica(this.ID, "sucesso no consenso");
			CustomGlobal.enviouFimConsenso = true;
		}
		CustomGlobal.consoleln(Global.currentTime + " nó " + this + " consenso: alguém recebeu proposta ganhadora com valor " + msgAccepted.getProposalValue());
		// nós chegaram a um consenso
		timerConsenso.stop();
		timerStage1.stop();
		consenso.stage = Stage.FIM;	// chegou ao final do consenso
		timerConsenso.stop();			// evita dar timeout na tentativa de consenso
		if(!consenso.isProposer() && !consenso.isAcceptor()) {
			CustomGlobal.consoleln(Global.currentTime + " nó " + this + " consenso: learner recebeu proposta ganhadora valor " + msgAccepted.getProposalValue());
			// esse nó é apenas um learner
			consenso.learnerWinner = true;
		}
		// todos usam esses valores depois que o consenso termina
		consenso.learnerWinnerNumber = msgAccepted.getProposalNumber();
		consenso.learnerWinnerValue = msgAccepted.getProposalValue();
	}

	
	/**
	 * Convite:<br>
	 * Timeout no final do Merge<br>
	 */
	public void fireT2Merge() {
		// somente o lider executa essa atividade
		if(this.numberAnswers < this.up.size()) {
			CustomGlobal.consoleln(Global.currentTime + " nó " + this + " em recovery porque deu timeout em T2Merge porque respostas = " + this.numberAnswers + " e up= " + this.up.size());
			this.recovery();
			//CustomGlobal.estatistica(this.ID, "recovery3");
		}
		else {
			// Fim com sucesso da eleicao
			//CustomGlobal.estatistica(this.ID, "fim", Integer.toString(this.others.size() + this.up.size()));
			this.state = EnumState.NORMAL;
			
			// tenta iniciar um consenso
			this.consenso.preInicia(this);	// o método verifica se deve iniciar realemente
		}
	}


	/**
	 * Paxos:<br>
	 * Timeout para começar o estágio 1
	 */
	public void fireStage1() {
		this.timerConsenso.start(this);		// começa a contar o tempo para terminar o consenso
		// se for um proposer, prepara um pedido
		if(this.consenso.isProposer()) {
			// prepara um pedido de consenso
			// como o nó não sabe quem é acceptor, envia a mensagem em broadcast, quem não for acceptor apenas ignora
			MsgPrepareRequest msgPrepareRequest = new MsgPrepareRequest(
					this, 
					null, 
					this.consenso.getPrepareRequestProposalNumber(), 
					this.consenso.getPrepareRequestProposalValue()
			);
			msgPrepareRequest.setSequencial(sequencial.valor(msgPrepareRequest));
			this.broadcast(msgPrepareRequest);
			CustomGlobal.consoleln(Global.currentTime + " nó " + this + " consenso: proposer propondo " + this.consenso.getPrepareRequestProposalNumber() + " com valor " + this.consenso.getPrepareRequestProposalValue());
			if(this.consenso.isAcceptor() && !this.consenso.isAcceptorProposed()) {
				this.consenso.nPrepareResponses++;
				CustomGlobal.consoleln(Global.currentTime + " nó " + this + " consenso: (acceptor)proposer agora com " + this.consenso.nPrepareResponses + " de " + Consenso.getNAcceptors());
				this.consenso.setAcceptorProposed(true);
				this.consenso.acceptorAcceptedNumber        = this.consenso.getPrepareRequestProposalNumber();
				this.consenso.acceptorAcceptedValue         = this.consenso.getPrepareRequestProposalValue();
				this.consenso.acceptorGreaterProposalNumber = this.consenso.getPrepareRequestProposalNumber();
			}
		}
	}


	/**
	 * Paxos:<br>
	 * Timeout para chegar a um consenso
	 */
	public void fireConsenso() {
		CustomGlobal.consoleln(Global.currentTime + " nó " + this + " consenso: vou tentar de novo");
		if(this.ID == this.coordinator.ID && !CustomGlobal.enviouFimConsenso) CustomGlobal.estatistica(this.ID, "falha no consenso");
		CustomGlobal.enviouFimConsenso = true;
		consenso.reinicia(++round);
		timerStage1.start(this);
	}


	/**
	 * Convite:<br>
	 * Timeout para esperar pela resposta e começar a enviar convites
	 */
	public void fireT1Merge() {
		this.state = EnumState.REORGANIZING;
		this.numberAnswers = 0;
		this.timerT2Merge.start(this.timerT2Merge.getDefault(), this);
		MsgReady msgReady = new MsgReady(this, null, 0, this.group, this.definition);
		for(Node destino: this.up) {
			msgReady.setNoDestino(destino);
			msgReady.setSequencial(sequencial.valor(msgReady));
			this.broadcast(msgReady);
		}
	}


	/**
	 * Convite:<br>
	 * Timeout para verificar periodicamente o lider e os liderados
	 */
	public void firePeriodico() {
		CustomGlobal.consoleln(Global.currentTime + " nó " + this + " em TimerPeriodico.fire()");
		this.checkMembers();
		this.checkCoordinator();		
		this.timerPeriodico.startRelative(this.timerPeriodico.getDefault(), this);
	}
}
