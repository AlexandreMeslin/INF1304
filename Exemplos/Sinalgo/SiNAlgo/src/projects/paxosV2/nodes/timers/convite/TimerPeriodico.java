/**
 * @author Meslin
 *
 */
package projects.paxosV2.nodes.timers.convite;

import projects.paxosV2.nodes.nodeImplementations.NodeV2;
import sinalgo.nodes.timers.Timer;
import sinalgo.tools.Tools;

/**
 * Timer para verificar periodicamente por outros coordenadores
 */
public class TimerPeriodico extends Timer {
	private NodeV2 node;

	/**
	 * Cria um timer
	 * @param node implementação particular do nó derivado da classe Node
	 */
	public TimerPeriodico(NodeV2 node) {
		this.node = node;
	}

	/* (non-Javadoc)
	 * @see sinalgo.nodes.timers.Timer#fire()
	 */
	@Override
	public void fire() {
		node.firePeriodico();
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
