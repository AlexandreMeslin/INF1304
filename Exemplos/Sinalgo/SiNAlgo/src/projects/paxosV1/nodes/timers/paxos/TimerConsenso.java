/**
 * @author Meslin
 *
 */
package projects.paxosV1.nodes.timers.paxos;

import projects.paxosV1.CustomGlobal;
import projects.paxosV1.nodes.nodeImplementations.NodeV1;
import sinalgo.nodes.timers.Timer;
import sinalgo.runtime.Global;
import sinalgo.tools.Tools;

public class TimerConsenso extends Timer {
	private NodeV1 node;
	private boolean valido;

	/**
	 * Cria um timer<br>
	 * Esse timer é ativado quando os nós não chegarem a um consenso
	 * @param node
	 */
	public TimerConsenso(NodeV1 node) {
		this.node = node;
	}

	/* (non-Javadoc)
	 * @see sinalgo.nodes.timers.Timer#fire()
	 */
	@Override
	public void fire() {
		if(valido) {
			CustomGlobal.consoleln(Global.currentTime + " nó " + node + " consenso: vou tentar de novo");
			node.consenso.reinicia(++node.round);
			node.timerStage1.start(node);
		}
	}
	
	/**
	 * Retorna o timer default
	 * @return timer default
	 */
	public double getDefault() {
		return 2 * (Tools.getNodeList().size() +1);
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
