/**
 * 
 */
package projects.paxosV1.nodes.timers.paxos;

import projects.paxosV1.Consenso;
import projects.paxosV1.CustomGlobal;
import projects.paxosV1.nodes.messages.paxos.proposer.MsgPrepareRequest;
import projects.paxosV1.nodes.nodeImplementations.NodeV1;
import sinalgo.nodes.timers.Timer;
import sinalgo.runtime.Global;
import sinalgo.tools.Tools;
import sinalgo.tools.statistics.Distribution;

/**
 * @author Meslin
 * 
 * Espera por um tempo aleatório antes de começar a fase 1 do algoritmo de Paxos
 *
 */
public class TimerStage1 extends Timer {
	private NodeV1 node;
	private boolean valido;

	
	
	/**
	 * 
	 * @param nodeV1
	 */
	public TimerStage1(NodeV1 nodeV1) {
		this.node = nodeV1;
		this.valido = true;
	}

	
	
	/* (non-Javadoc)
	 * @see sinalgo.nodes.timers.Timer#fire()
	 */
	@Override
	public void fire() {
		if(this.valido) {
			node.timerConsenso.start(node);		// começa a contar o tempo para terminar o consenso
			// se for um proposer, prepara um pedido
			if(this.node.consenso.isProposer) {
				// prepara um pedido de consenso
				// como o nó não sabe quem é acceptor, envia a mensagem em broadcast, quem não for acceptor apenas ignora
				MsgPrepareRequest msgPrepareRequest = new MsgPrepareRequest(
						this.node, 
						null, 
						this.node.consenso.prepareRequestProposalNumber, 
						this.node.consenso.prepareRequestProposalValue
				);
				msgPrepareRequest.setSequencial(node.sequencial.valor(msgPrepareRequest));
				this.node.broadcast(msgPrepareRequest);
				CustomGlobal.consoleln(Global.currentTime + " nó " + node + " consenso: proposer propondo " + this.node.consenso.prepareRequestProposalNumber + " com valor " + this.node.consenso.prepareRequestProposalValue);
				if(this.node.consenso.isAcceptor && !this.node.consenso.isAcceptorProposed) {
					this.node.consenso.nPrepareResponses++;
					CustomGlobal.consoleln(Global.currentTime + " nó " + node + " consenso: (acceptor)proposer agora com " + this.node.consenso.nPrepareResponses + " de " + Consenso.nAcceptors);
					this.node.consenso.isAcceptorProposed = true;
					this.node.consenso.acceptorAcceptedNumber        = this.node.consenso.prepareRequestProposalNumber;
					this.node.consenso.acceptorAcceptedValue         = this.node.consenso.prepareRequestProposalValue;
					this.node.consenso.acceptorGreaterProposalNumber = this.node.consenso.prepareRequestProposalNumber;
				}
			}
		}
	}

	/**
	 * Retorna o timer default
	 * @return timer default
	 */
	public double getDefault() {
		double timerDefault = Distribution.getRandom().nextDouble() * Tools.getNodeList().size() +1;
		return timerDefault;
	}

	/**
	 * Inicia um timer especificando o tempo
	 * @param timer
	 * @param node
	 */
	public void start(double timer, NodeV1 node) {
		this.valido = true;
		super.startRelative(timer, node);
	}

	/**
	 * Inicia um timer com o tempo default
	 * @param node
	 */
	public void start(NodeV1 node) {
		start(getDefault(), node);
	}

	/**
	 * Cancela o timer
	 */
	public void stop() {
		this.valido = false;
	}

}