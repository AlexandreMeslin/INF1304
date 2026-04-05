/**
 * timer esperando resposta de AYCoordinator
 */
package projects.paxosV1.nodes.timers.convite;

import projects.paxosV1.CustomGlobal;
import projects.paxosV1.nodes.nodeImplementations.NodeV1;
import sinalgo.nodes.timers.Timer;
import sinalgo.runtime.Global;
import sinalgo.tools.Tools;

/**
 * @author Meslin
 *
 */
public class TimerCheckMembers extends Timer {
	private NodeV1 node;
	private boolean valido;

	/**
	 * Espera que todos os nós conectados respondam dizendo se são ou não coordenadores<br>
	 */
	public TimerCheckMembers(NodeV1 node) {
		this.node = node;
		this.valido = true;
	}

	/* (non-Javadoc)
	 * @see sinalgo.nodes.timers.Timer#fire()
	 */
	@Override
	public void fire() {
		if(this.valido) {
			// checkMembers()
			if(this.node.others.isEmpty()) return;
			CustomGlobal.estatistica(this.node.ID, "inicio");
			CustomGlobal.consoleln(Global.currentTime + " nó " + this.node + " vai fazer merge daqui a pouco");
			this.node.timerMerge.start(this.node.timerMerge.getDefault(), this.node);
		}
	}

	/**
	 * Retorna o timer default
	 * O tempo dessse timer deve ser igual ao RTT ou seja, 2*|n|
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
	public void start(double timer, NodeV1 node) {
		this.valido = true;
		super.startRelative(timer, node);
	}
	
	public void stop() {
		this.valido = false;
	}
}
