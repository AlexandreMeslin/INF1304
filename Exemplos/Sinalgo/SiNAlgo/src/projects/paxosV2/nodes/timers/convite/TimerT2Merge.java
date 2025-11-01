/**
 * 
 */
package projects.paxosV2.nodes.timers.convite;

import projects.paxosV2.nodes.nodeImplementations.NodeV2;
import sinalgo.nodes.timers.Timer;
import sinalgo.tools.Tools;

/**
 * @author meslin
 *
 */
public class TimerT2Merge extends Timer {
	private NodeV2 node;
	private boolean valido;

	/**
	 * timer 2 do merge: espera pela resposta de todos os componentes do grupo ao aviso de ready<br>
	 * Lugar de uso conhecido: TimerT1Merge.fire()<br>
	 */
	public TimerT2Merge(NodeV2 node) {
		this.node = node;
		this.valido = true;
	}

	/* (non-Javadoc)
	 * @see sinalgo.nodes.timers.Timer#fire()
	 */
	@Override
	public void fire() {
		if(this.valido) node.fireT2Merge();
	}

	/**
	 * Retorna o timer default
	 * @return timer default
	 */
	public double getDefault() {
		int timerDefault = 2 * (Tools.getNodeList().size() +1);
		return timerDefault;
	}

	/**
	 * 
	 * @param timer
	 * @param node
	 */
	public void start(double timer, NodeV2 node) {
		this.valido = true;
		super.startRelative(timer, node);
	}
	
	public void stop() {
		this.valido = false;
	}
}
