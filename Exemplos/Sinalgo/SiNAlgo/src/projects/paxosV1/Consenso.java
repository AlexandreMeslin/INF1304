/**
 * @author Meslin
 *
 */
package projects.paxosV1;

import projects.paxosV1.nodes.messages.paxos.MsgConsenso;
import projects.paxosV1.nodes.nodeImplementations.NodeV1;
import sinalgo.nodes.Node;
import sinalgo.runtime.Global;
import sinalgo.tools.Tools;
import sinalgo.tools.statistics.Distribution;

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
	private NodeV1 node;
	/** número de acceptors no consenso (global para todos os nós) */
	public static int nAcceptors;
	
	// estado do nó em relação ao consenso de Paxos
	// se não for proposer ou acceptor, vai ser learner
	/** indica se esse nó é um proposer */
	public boolean isProposer;
	/** indica se esse nó é um acceptor */
	public boolean isAcceptor;
	/** estágio da máquina de estados do consenso */
	public Stage stage;
	
	// proposer
	/** número da proposta preparada pelo proposer */
	public int prepareRequestProposalNumber;
	/** valor proposto pelo proposer */
	public int prepareRequestProposalValue;
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
	public boolean isAcceptorProposed;
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

	public Consenso(NodeV1 node) {
		this.node = node;
	}
	
	/**
	 * método para iniciar um novo consenso
	 * @param rodada
	 */
	public void preparaConsenso(int rodada) {
		// calcula o papel desse nó no consenso
		// o nó pode ser learner, acceptor ou proposer (ou acceptor e proposer)
		if(Distribution.getRandom().nextDouble() < CustomGlobal.porcentagemLearners) {
			// é um leaner
			this.isAcceptor = false;
			this.isProposer = false;
		}
		else if(Distribution.getRandom().nextDouble() < CustomGlobal.porcentagemAcceptors) {
			// é um acceptor
			this.isAcceptor = true;
			this.isProposer = false;
		}
		else if(Distribution.getRandom().nextDouble() < CustomGlobal.porcentagemProposers) {
			// é um proposer
			this.isAcceptor = false;
			this.isProposer = true;
		}
		else {
			// é um acceptor e proposer
			this.isAcceptor = true;
			this.isProposer = true;
		}
		
		// se é o coordenador, estamos iniciando um novo consenso, logo vamos reiniciar os contadores
		if(node.ID == node.coordinator.ID) nAcceptors = 0;
		if(isAcceptor) nAcceptors++;

		reinicia(rodada);
	}
	
	/**
	 * Inicia o consenso<br/>
	 * @param node 
	 */
	public void preInicia(NodeV1 node) {
		// o consenso somente pode ser iniciado se o grupo coordenador por esse nó contiver a maioria dos nos
		if(node.ID == node.coordinator.ID && node.up.size() > Tools.getNodeList().size()/2) {
			// inicia o consenso...
			Consenso.nAcceptors = 0;
			Tools.appendToOutput(Global.currentTime + " nó " + node + " consenso: préiniciando com " + node.up.size() + " nos\n");

			// envia mensagem para cada um dos coordenados informado que um consenso vai ser iniciado
			// cada coordenado deve escolher os seus papeis
			// a mensagem foi enviada com sendDirect apenas para agilizar o início do consenso
			for(Node coordenado : node.up) {
				MsgConsenso msgConsenso = new MsgConsenso(node, coordenado);
				msgConsenso.setSequencial(node.sequencial.valor(msgConsenso));
				node.sendDirect(msgConsenso, coordenado);
			}
			node.consenso.reset();
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
		isProposer = false;
		isAcceptor = false;
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
		
		if(isProposer) {
			// prepara a proposta (número e valor)
			this.prepareRequestProposalNumber = rodada * Tools.getNodeList().size() + node.ID;
			this.prepareRequestProposalValue = rodada * Tools.getNodeList().size() + node.ID;
			this.nPrepareResponses = 0;
			proposerWinnerProposal = -1;		// negativo para sinalizar que ainda não existe proposta ganhadora
			CustomGlobal.consoleln(Global.currentTime + " nó " + node + " consenso: é proposer");
		}
		
		if(isAcceptor) {
			this.isAcceptorProposed = false;	// inicialmente ainda não recebeu nenhuma proposta
			CustomGlobal.consoleln(Global.currentTime + " nó " + node + " consenso: é acceptor");
		}
		
		// learner
		learnerWinner = false;
	}
}
