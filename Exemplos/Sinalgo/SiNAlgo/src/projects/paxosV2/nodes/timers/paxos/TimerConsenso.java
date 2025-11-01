/**
 * @author Meslin
 *
 */
package projects.paxosV2.nodes.timers.paxos;

import projects.paxosV2.nodes.nodeImplementations.NodeV2;
import sinalgo.nodes.timers.Timer;
import sinalgo.tools.Tools;

public class TimerConsenso extends Timer {
	private NodeV2 node;
	private boolean valido;

	/**
	 * Cria um timer<br>
	 * Esse timer é ativado quando os nós não chegarem a um consenso
	 * @param node
	 */
	public TimerConsenso(NodeV2 node) {
		this.node = node;
	}

	/* (non-Javadoc)
	 * @see sinalgo.nodes.timers.Timer#fire()
	 */
	@Override
	public void fire() {
		if(valido) node.fireConsenso();
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
	public void start(double timer, NodeV2 node) {
		this.valido = true;
		super.startRelative(timer, node);
	}
	
	/**
	 * Inicia um timer com o tempo default
	 * @param node
	 */
	public void start(NodeV2 node) {
		start(getDefault(), node);
	}
	
	/**
	 * Cancela o timer
	 */
	public void stop() {
		this.valido = false;
	}
}
