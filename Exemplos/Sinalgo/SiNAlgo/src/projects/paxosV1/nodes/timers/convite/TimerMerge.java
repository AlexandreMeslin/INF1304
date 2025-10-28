/**
 * @author meslin
 *
 */
package projects.paxosV1.nodes.timers.convite;

import projects.paxosV1.nodes.nodeImplementations.NodeV1;
import sinalgo.nodes.timers.Timer;
import sinalgo.tools.Tools;

/**
 * timer para realizar o merge (inversamente proporcional ao ID)<br>
 * Chamada conhecida: TimerCheckCoordinator<br>
 */
public class TimerMerge extends Timer {
	// This wait time should be large as compared to the time between calls to procedure Check.
	private NodeV1 node;
	private boolean valido;

	/**
	 * 
	 */
	public TimerMerge(NodeV1 nodeV1) {
		this.node = nodeV1;
		this.valido = true;
	}

	/* (non-Javadoc)
	 * @see sinalgo.nodes.timers.Timer#fire()
	 */
	@Override
	public void fire() {
		if(this.valido) {
			// checkMembers
			this.node.merge(this.node.others);
		}
	}

	/**
	 * "wait time proportional to p-i"
	 * 
	 * @return tempo default do timer
	 */
	public double getDefault() {
		double timerDefault = Tools.getNodeList().size() * Tools.getNodeList().size() - Tools.getNodeList().size() * this.node.ID +1;
		return timerDefault;
	}

	/**
	 * Inicia o timer
	 * @param timer
	 * @param node
	 */
	public void start(double timer, NodeV1 node) {
		this.valido = true;
		super.startRelative(timer, node);
	}

	/**
	 * Cancela o timer
	 */
	public void stop() {
		this.valido = false;
	}
}
