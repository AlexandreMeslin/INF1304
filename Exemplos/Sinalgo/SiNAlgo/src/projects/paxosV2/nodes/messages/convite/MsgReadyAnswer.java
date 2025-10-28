package projects.paxosV2.nodes.messages.convite;

import projects.paxosV2.nodes.messages.MsgGenerica;
import projects.paxosV2.nodes.nodeImplementations.NodeV2;
import sinalgo.nodes.Node;
import sinalgo.nodes.messages.Message;

public class MsgReadyAnswer extends MsgGenerica {
	/** informa se está no grupo ou não */
	private boolean isInGroup;
	/** id do novo grupo */
	private long group;

	public MsgReadyAnswer(Node noOrigem, Node noDestino, int sequencial, boolean isInGroup, long group) {
		this(noOrigem, noDestino, isInGroup, group);
		this.sequencial = sequencial;
	}

	public MsgReadyAnswer(Node noOrigem, Node noDestino, boolean isInGroup, long group) {
		this.noOrigem = noOrigem;
		this.noDestino = noDestino;
		this.isInGroup = isInGroup;
		this.group = group;
	}

	@Override
	public Message clone()
	{
		return new MsgReadyAnswer(this.noOrigem, this.noDestino, this.sequencial, this.isInGroup, this.group);
	}

	public boolean isInGroup() {
		return isInGroup;
	}

	public void setInGroup(boolean isInGroup) {
		this.isInGroup = isInGroup;
	}

	public long getGroup() {
		return group;
	}

	public void setGroup(long group) {
		this.group = group;
	}
	
	@Override
	public String toString() {
		return Boolean.toString(this.isInGroup()) + " para o grupo " + this.getGroup();
	}

	/**
	 * ReadyAnswer
	 * merge (continuação)
	 */
	@Override
	public void acao(NodeV2 node) {
		if(isInGroup() && getGroup() == node.group) {
			node.fase = getClass().getSimpleName();
			node.numberAnswers++;
		}
	}
}
