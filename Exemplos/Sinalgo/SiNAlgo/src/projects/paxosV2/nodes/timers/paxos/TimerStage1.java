/**
 * 
 */
package projects.paxosV2.nodes.timers.paxos;

import projects.paxosV2.nodes.nodeImplementations.NodeV2;
import sinalgo.nodes.timers.Timer;
import sinalgo.tools.Tools;
import sinalgo.tools.statistics.Distribution;

/**
 * @author Meslin
 * 
 * Espera por um tempo aleatório antes de começar a fase 1 do algoritmo de Paxos
 *
 */
public class TimerStage1 extends Timer {
	private NodeV2 node;
	private boolean valido;

	
	
	/**
	 * 
	 * @param NodeV2
	 */
	public TimerStage1(NodeV2 NodeV2) {
		this.node = NodeV2;
		this.valido = true;
	}

	
	
	/* (non-Javadoc)
	 * @see sinalgo.nodes.timers.Timer#fire()
	 */
	@Override
	public void fire() {
		if(this.valido) node.fireStage1();
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