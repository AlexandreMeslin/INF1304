/**
 * @author Meslin
 *
 */
package projects.paxosV1.nodes.timers.convite;

import projects.paxosV1.CustomGlobal;
import projects.paxosV1.nodes.nodeImplementations.NodeV1;
import sinalgo.nodes.Node;
import sinalgo.nodes.timers.Timer;
import sinalgo.runtime.Global;
import sinalgo.tools.Tools;

/**
 * timer para esperar a resposta do coordenador à mensagem AYThere
 */
public class TimerCheckCoordinator extends Timer {
	private NodeV1 node;
	private boolean valido;

	/**
	 * Espera pela resposta de um coordenador que pode estar faltoso<br>
	 * É enviada a mensagem AYThere<br>
	 * Se a mensagem AYTAnswer = TRUE não vier dentro do tempo, executa a recuperação<br>
	 */
	public TimerCheckCoordinator(NodeV1 nodeV1) {
		this.node = nodeV1;
		this.valido = true;
	}

	/* (non-Javadoc)
	 * @see sinalgo.nodes.timers.Timer#fire()
	 */
	@Override
	public void fire() {
		if(this.valido) {
			CustomGlobal.consoleln(Global.currentTime + " nó " + this.node + " detectou que o coordenador " + this.node.coordinator.ID + " falhou");
			this.node.recovery();		// coordenator has crashed
			CustomGlobal.estatistica(this.node.ID, "recovery2");
		}
	}

	/**
	 * 
	 * @see sinalgo.nodes.timers.Timer#startRelative()
	 * @param relativeTime tempo relativo para timeout
	 * @param n nó
	 */
	public void start(double relativeTime, Node n) {
		this.valido = true;
		super.startRelative(relativeTime, n);
	}

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
