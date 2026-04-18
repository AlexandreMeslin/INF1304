package projects.v1.nodes.messages;

import sinalgo.nodes.Node;
import sinalgo.nodes.messages.Message;

public class MsgAYThereAnswer extends MsgGenerica {
	/** informa se o coordenador está ativo ou não */
	private boolean isCoordinator;
	private long groupLocal;
	private long groupRemote;

	/**
	 * 
	 * @param noOrigem
	 * @param noDestino
	 * @param sequencial
	 * @param isCoordinator
	 * @param groupLocal
	 * @param groupRemote
	 */
	public MsgAYThereAnswer(Node noOrigem, Node noDestino, int sequencial, boolean isCoordinator, long groupLocal, long groupRemote)
	{
		this.noOrigem = noOrigem;
		this.noDestino = noDestino;
		this.sequencial = sequencial;
		this.isCoordinator = isCoordinator;
		this.groupLocal = groupLocal;
		this.groupRemote = groupRemote;
	}

	@Override
	public Message clone()
	{
		return new MsgAYThereAnswer(this.noOrigem, this.noDestino, this.sequencial, this.isCoordinator, this.groupLocal, this.groupRemote);
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

	public int getSequencial()
	{
		return sequencial;
	}

	public void setSequencial(int sequencial)
	{
		this.sequencial = sequencial;
	}

	public boolean isCoordinator()
	{
		return isCoordinator;
	}

	public void setCoordinator(boolean isCoordinator)
	{
		this.isCoordinator = isCoordinator;
	}

	public long getGroupLocal() {
		return groupLocal;
	}

	public void setGroupLocal(long groupLocal) {
		this.groupLocal = groupLocal;
	}

	public long getGroupRemote() {
		return groupRemote;
	}

	public void setGroupRemote(long groupRemote) {
		this.groupRemote = groupRemote;
	}

	@Override
	public String toString() {
		return Boolean.toString(this.isCoordinator()) + " grupo local " + getGroupLocal() + ", grupo remoto " + getGroupRemote();
	}
}
