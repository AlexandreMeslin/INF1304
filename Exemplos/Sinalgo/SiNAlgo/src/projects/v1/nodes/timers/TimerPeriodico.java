/**
 * @author Meslin
 *
 */
package projects.v1.nodes.timers;

import projects.v1.CustomGlobal;
import projects.v1.nodes.nodeImplementations.NodeV1;
import sinalgo.nodes.timers.Timer;
import sinalgo.runtime.Global;
import sinalgo.tools.Tools;

/**
 * Timer para verificar periodicamente por outros coordenadores
 */
public class TimerPeriodico extends Timer {
	private NodeV1 node;

	/**
	 * Cria um timer
	 * @param node implementação particular do nó derivado da classe Node
	 */
	public TimerPeriodico(NodeV1 node) {
		this.node = node;
	}

	/* (non-Javadoc)
	 * @see sinalgo.nodes.timers.Timer#fire()
	 */
	@Override
	public void fire() {
		CustomGlobal.consoleln(Global.currentTime + " nó " + this.node + " em TimerPeriodico.fire()");
		this.node.checkMembers();
		this.node.checkCoordinator();
		
		this.node.timerPeriodico.startRelative(this.node.timerPeriodico.getDefault(), this.node);
	}

	/**
	 * Retorna o timer default
	 * @return timer default
	 */
	public double getDefault() {
		int timerDefault = Math.max(Tools.getNodeList().size() * Tools.getNodeList().size(), 10 * Tools.getNodeList().size());
		return timerDefault;
	}
}
