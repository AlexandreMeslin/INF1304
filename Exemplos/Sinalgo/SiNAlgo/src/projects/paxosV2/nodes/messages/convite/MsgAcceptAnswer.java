package projects.paxosV2.nodes.messages.convite;

import projects.paxosV2.CustomGlobal;
import projects.paxosV2.nodes.messages.MsgGenerica;
import projects.paxosV2.nodes.nodeImplementations.NodeV2;
import projects.paxosV2.nodes.nodeImplementations.NodeV2.EnumState;
import sinalgo.nodes.Node;
import sinalgo.nodes.messages.Message;
import sinalgo.runtime.Global;

public class MsgAcceptAnswer extends MsgGenerica {
	/** flag indicando se aceitou finalmente ficar no grupo */
	private boolean isInGroup;

	/**
	 * 
	 * @param noOrigem
	 * @param noDestino
	 * @param sequencial
	 * @param isInGroup
	 */
	public MsgAcceptAnswer(Node noOrigem, Node noDestino, int sequencial, boolean isInGroup)
	{
		this(noOrigem, noDestino, isInGroup);
		this.sequencial = sequencial;
	}

	public MsgAcceptAnswer(Node noOrigem, Node noDestino, boolean isInGroup) {
		this.noOrigem = noOrigem;
		this.noDestino = noDestino;
		this.isInGroup = isInGroup;
	}

	@Override
	public Message clone()
	{
		// TODO Auto-generated method stub
		return new MsgAcceptAnswer(this.noOrigem, this.noDestino, this.sequencial, isInGroup);
	}

	public boolean isInGroup() {
		return isInGroup;
	}

	public void setInGroup(boolean isInGroup) {
		this.isInGroup = isInGroup;
	}
	
	@Override
	public String toString() {
		return Boolean.toString(this.isInGroup());
	}

	/**
	 * AcceptAnswer
	 */
	@Override
	public void acao(NodeV2 node) {
		if(isInGroup()) {
			node.fase = getClass().getSimpleName();
			CustomGlobal.consoleln(Global.currentTime + " nó " + node + " indo para o estado reorganizing porque recebeu acceptanswer");
			node.state = EnumState.REORGANIZING;
			node.timerAcceptAnswer.stop();
		}
	}
}
