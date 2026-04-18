package projects.v1.nodes.messages;

import sinalgo.nodes.Node;
import sinalgo.nodes.messages.Message;

public class MsgReadyAnswer extends MsgGenerica {
	/** informa se está no grupo ou não */
	private boolean isInGroup;
	/** id do novo grupo */
	private long group;

	public MsgReadyAnswer(Node noOrigem, Node noDestino, int sequencial, boolean isInGroup, long group)
	{
		this.noOrigem = noOrigem;
		this.noDestino = noDestino;
		this.sequencial = sequencial;
		this.isInGroup = isInGroup;
		this.group = group;
	}

	@Override
	public Message clone()
	{
		return new MsgReadyAnswer(this.noOrigem, this.noDestino, this.sequencial, this.isInGroup, this.group);
	}

	public Node getNoOrigem() {
		return noOrigem;
	}

	public void setNoOrigem(Node noOrigem) {
		this.noOrigem = noOrigem;
	}

	public Node getNoDestino() {
		return noDestino;
	}

	public void setNoDestino(Node noDestino) {
		this.noDestino = noDestino;
	}

	public int getSequencial() {
		return sequencial;
	}

	public void setSequencial(int sequencial) {
		this.sequencial = sequencial;
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
}
