/**
 * @author meslin
 *
 */
package projects.paxosV1.nodes.timers.convite;

import projects.paxosV1.CustomGlobal;
import projects.paxosV1.nodes.nodeImplementations.NodeV1;
import sinalgo.nodes.Node;
import sinalgo.nodes.timers.Timer;
import sinalgo.tools.Tools;

/**
 * Timer esperando que venha uma resposta ao pedido de aceitação
 */
public class TimerAcceptAnswer extends Timer {
	private NodeV1 node;
	private boolean valido;

	/**
	 * 
	 * @param node
	 */
	public TimerAcceptAnswer(NodeV1 node) {
		this.node = node;
		this.valido = true;
	}

	/* (non-Javadoc)
	 * @see sinalgo.nodes.timers.Timer#fire()
	 */
	@Override
	public void fire() {
		if(this.valido) {
			CustomGlobal.consoleln("nó " + this.node.ID + " recebeu timeout da mensagem AcceptAnswer");
			this.node.recovery();
			CustomGlobal.estatistica(this.node.ID, "recovery1");
		}
	}

	/**
	 * Inicia o timer
	 * @param relativeTime
	 * @param n
	 */
	public void start(double relativeTime, Node n) {
		this.valido = true;
		super.startRelative(relativeTime, n);
	}
	
	/**
	 * Cancela o timer
	 */
	public void stop() {
		this.valido = false;
	}

	/**
	 * Retorna o timer default
	 * @return timer default
	 */
	public double getDefault() {
		int timerDefault = 2 * (Tools.getNodeList().size() +1);
		return timerDefault;
	}
}
