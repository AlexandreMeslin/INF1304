package projects.v1.nodes.messages;

import sinalgo.nodes.Node;
import sinalgo.nodes.messages.Message;

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
		this.noOrigem = noOrigem;
		this.noDestino = noDestino;
		this.sequencial = sequencial;
		this.isInGroup = isInGroup;
	}

	@Override
	public Message clone()
	{
		// TODO Auto-generated method stub
		return new MsgAcceptAnswer(this.noOrigem, this.noDestino, this.sequencial, isInGroup);
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
	
	@Override
	public String toString() {
		return Boolean.toString(this.isInGroup());
	}
}
