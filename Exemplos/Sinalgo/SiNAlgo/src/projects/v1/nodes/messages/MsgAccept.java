package projects.v1.nodes.messages;

import sinalgo.nodes.Node;
import sinalgo.nodes.messages.Message;

public class MsgAccept extends MsgGenerica {
	private long group;
	
	/**
	 * 
	 * @param noOrigem
	 * @param noDestino
	 * @param sequencial
	 * @param group
	 */
	public MsgAccept(Node noOrigem, Node noDestino, int sequencial, long group)
	{
		this.noOrigem = noOrigem;
		this.noDestino = noDestino;
		this.sequencial = sequencial;
		this.group = group;
	}

	@Override
	public Message clone()
	{
		// TODO Auto-generated method stub
		return new MsgAccept(this.noOrigem, this.noDestino, this.sequencial, this.group);
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

	public long getGroup() {
		return group;
	}

	public void setGroup(long group) {
		this.group = group;
	}
	
	@Override
	public String toString() {
		return "entar no grupo " + this.group;
	}
}
