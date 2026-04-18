/**
 * @author Meslin
 *
 */
package projects.paxosV2;

import projects.paxosV2.nodes.messages.paxos.MsgConsenso;
import projects.paxosV2.nodes.nodeImplementations.NodeV2;
import projects.paxosV2.nodes.nodeImplementations.NodeV2.EnumPaxosRole;
import sinalgo.runtime.Global;
import sinalgo.tools.Tools;

public class Consenso {
	public enum Stage {
		/** se consenso em andamento */
		NOVO,
		/** consenso iniciado */
		INICIADO,
		/** consenso no estado 1, enviando prepare request */
		REQUEST,
		/** consenso terminado para esse nó */
		FIM
	}
	
	/** representa esse nó */
	private NodeV2 node;
	/** número de acceptors no consenso (global para todos os nós) */
	private static int nAcceptors;
	
	// estado do nó em relação ao consenso de Paxos
	// se não for proposer ou acceptor, vai ser learner
	/** indica se esse nó é um proposer */
	private boolean proposer;
	/** indica se esse nó é um acceptor */
	private boolean acceptor;
	/** estágio da máquina de estados do consenso */
	public Stage stage;
	
	// proposer
	/** número da proposta preparada pelo proposer */
	private int prepareRequestProposalNumber;
	/** valor proposto pelo proposer */
	private int prepareRequestProposalValue;
	/** quantidade de respostas ao prepare request recebidas por um proposer */
	public int nPrepareResponses;
	/** número da proposta que está ganhando até agora */
	public int proposerWinnerProposal;
	/** valor da proposta que está ganhando até agora */
	public int proposerWinnerValue;
	
	// acceptor
	/** número da maior proposta preparada recebida por um acceptor */
	public int acceptorGreaterProposalNumber;	
	/** indica se o accpetor já recebeu uma proposta */
	private boolean acceptorProposed;
	/** número da preparacação da proposta aceita pelo acceptor */
	public int acceptorAcceptedNumber;
	/** valor da preparação aceita pelo acceptor */
	public int acceptorAcceptedValue;
	
	// learner
	/** learner recebeu proposta ganhadora */
	public boolean learnerWinner;
	/** número da proposta ganhadora do learner */
	public int learnerWinnerNumber;
	/** valor da proposta ganhadora do learner */
	public int learnerWinnerValue;

	public Consenso(NodeV2 node, EnumPaxosRole role) {
		this.node = node;
		// calcula o papel desse nó no consenso
		// o nó pode ser learner, acceptor ou proposer (ou acceptor e proposer)
		if(role == EnumPaxosRole.LEARNER) {
			// é um leaner
			setAcceptor(false);
			setProposer(false);
		}
		else if(role == EnumPaxosRole.ACCEPTOR) {
			// é um acceptor
			setAcceptor(true);
			setProposer(false);
		}
		else if(role == EnumPaxosRole.PROPOSER) {
			// é um proposer
			setAcceptor(false);
			setProposer(true);
		}
		else {
			// é um acceptor e proposer
			setAcceptor(true);
			setProposer(true);
		}
		if(isAcceptor()) setNAcceptors(getNAcceptors() + 1);
	}
	
	/**
	 * método para iniciar um novo consenso
	 * @param rodada
	 */
	public void preparaConsenso(int rodada) {
		reinicia(rodada);
	}
	
	/**
	 * Inicia o consenso<br/>
	 * @param node 
	 */
	public void preInicia(NodeV2 node) {
		// o consenso somente pode ser iniciado se o grupo coordenador por esse nó contiver a maioria dos nos
		if(node.ID == node.coordinator.ID && node.up.size() > Tools.getNodeList().size()/2) {
			// inicia o consenso...
			Tools.appendToOutput(Global.currentTime + " nó " + node + " consenso: préiniciando com " + node.up.size() + " nos\n");
			CustomGlobal.estatistica(node.ID, "iniciando consenso");
			CustomGlobal.enviouFimConsenso = false;
			// envia mensagem para cada um dos coordenados informado que um consenso vai ser iniciado
			MsgConsenso msgConsenso = new MsgConsenso(node, null);
			msgConsenso.setSequencial(node.sequencial.valor(msgConsenso));
			node.broadcast(msgConsenso);
			node.consenso.reset();
			node.round++;
			node.consenso.preparaConsenso(node.round);
			stage = Stage.INICIADO;
			node.timerStage1.start(node);
		}
	}

	/**
	 * Termina o consenso de forma abrupta e coloca todas as suas variáveis no estado inicial
	 */
	public void reset() {
		CustomGlobal.consoleln(Global.currentTime + " nó " + node + " consenso: reset");
		stage = Stage.NOVO;
		learnerWinner = false;
	}

	/**
	 * Reinicia um consenso<br>
	 * (Provavelmente porque o anterior não chegou a um consenso)
	 * @param node
	 */
	public void reinicia(int rodada) {
		// iniciando um novo consenso
		this.stage = Stage.NOVO;
		
		if(isProposer()) {
			// prepara a proposta (número e valor)
			this.setPrepareRequestProposalNumber(rodada * Tools.getNodeList().size() + node.ID);
			this.setPrepareRequestProposalValue(rodada * Tools.getNodeList().size() + node.ID);
			this.nPrepareResponses = 0;
			proposerWinnerProposal = -1;		// negativo para sinalizar que ainda não existe proposta ganhadora
			CustomGlobal.consoleln(Global.currentTime + " nó " + node + " consenso: é proposer");
		}
		
		if(isAcceptor()) {
			this.setAcceptorProposed(false);	// inicialmente ainda não recebeu nenhuma proposta
			CustomGlobal.consoleln(Global.currentTime + " nó " + node + " consenso: é acceptor");
		}
		
		// learner
		learnerWinner = false;
	}

	public boolean isProposer() {
		return proposer;
	}

	public void setProposer(boolean proposer) {
		this.proposer = proposer;
	}

	public boolean isAcceptor() {
		return acceptor;
	}

	public void setAcceptor(boolean acceptor) {
		this.acceptor = acceptor;
	}

	public boolean isAcceptorProposed() {
		return acceptorProposed;
	}

	public void setAcceptorProposed(boolean acceptorProposed) {
		this.acceptorProposed = acceptorProposed;
	}

	public int getPrepareRequestProposalNumber() {
		return prepareRequestProposalNumber;
	}

	public void setPrepareRequestProposalNumber(int prepareRequestProposalNumber) {
		this.prepareRequestProposalNumber = prepareRequestProposalNumber;
	}

	public int getPrepareRequestProposalValue() {
		return prepareRequestProposalValue;
	}

	public void setPrepareRequestProposalValue(int prepareRequestProposalValue) {
		this.prepareRequestProposalValue = prepareRequestProposalValue;
	}

	public static int getNAcceptors() {
		return nAcceptors;
	}

	public static void setNAcceptors(int nAcceptors) {
		Consenso.nAcceptors = nAcceptors;
	}
}
