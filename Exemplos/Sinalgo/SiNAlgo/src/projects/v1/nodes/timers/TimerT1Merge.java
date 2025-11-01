/**
 * @author meslin
 *
 */
package projects.v1.nodes.timers;

import projects.v1.nodes.messages.MsgReady;
import projects.v1.nodes.nodeImplementations.NodeV1;
import projects.v1.nodes.nodeImplementations.NodeV1.EnumState;
import sinalgo.nodes.Node;
import sinalgo.nodes.timers.Timer;
import sinalgo.tools.Tools;

/**
 * timer 1 do merge: espera pela resposta de todos os coordenadores ao convite
 */
public class TimerT1Merge extends Timer {
	private NodeV1 node;

	/**
	 * 
	 */
	public TimerT1Merge(NodeV1 node) {
		this.node = node;
	}

	/* (non-Javadoc)
	 * @see sinalgo.nodes.timers.Timer#fire()
	 */
	@Override
	public void fire() {
		this.node.state = EnumState.REORGANIZING;
		this.node.numberAnswers = 0;
		this.node.timerT2Merge.start(this.node.timerT2Merge.getDefault(), this.node);
		MsgReady msgReady = new MsgReady(this.node, null, 0, this.node.group, this.node.definition);
		for(Node destino: this.node.up) {
			msgReady.setNoDestino(destino);
			msgReady.setSequencial(++this.node.sequencial.sequencialMsgReady);
			this.node.broadcast(msgReady);
		}
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
