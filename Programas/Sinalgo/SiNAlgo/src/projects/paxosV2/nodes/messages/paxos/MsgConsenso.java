/**
 * @author Meslin
 *
 */
package projects.paxosV2.nodes.messages.paxos;

import projects.paxosV2.Consenso.Stage;
import projects.paxosV2.CustomGlobal;
import projects.paxosV2.nodes.messages.MsgGenerica;
import projects.paxosV2.nodes.nodeImplementations.NodeV2;
import sinalgo.nodes.Node;
import sinalgo.nodes.messages.Message;
import sinalgo.runtime.Global;

public class MsgConsenso extends MsgGenerica {
	/**
	 * 
	 * @param noOrigem
	 * @param noDestino
	 * @param sequencial
	 */
	public MsgConsenso(Node noOrigem, Node noDestino, int sequencial) {
		this(noOrigem, noDestino);
		this.sequencial = sequencial;
	}

	/**
	 * 
	 * @param noOrigem
	 * @param noDestino
	 */
	public MsgConsenso(Node noOrigem, Node noDestino) {
		this.noOrigem = noOrigem;
		this.noDestino = noDestino;
	}

	@Override
	public Message clone() {
		return new MsgConsenso(this.noOrigem, this.noDestino, this.sequencial);
	}

	/**
	 * Recebeu uma mensagem informando que o consenso vai começar<br>
	 */
	@Override
	public void acao(NodeV2 node) {
		CustomGlobal.consoleln(Global.currentTime + " nó " + node + " consenso: vai criar consenso");
		node.round++;
		node.consenso.preparaConsenso(node.round);
		node.consenso.stage = Stage.INICIADO;
		node.timerStage1.start(node);
	}
}
