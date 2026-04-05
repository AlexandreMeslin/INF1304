package projects.v1.nodes.messages;

import sinalgo.nodes.Node;
import sinalgo.nodes.messages.Message;

public class MsgAYCoordinatorAnswer extends MsgGenerica {
	/** estado do processo informando se é ou não coordenador */
	private boolean isCoordinator;
	
	/**
	 * 
	 * @param noOrigem
	 * @param noDestino
	 * @param isCoordinator
	 * @param sequencial
	 */
	public MsgAYCoordinatorAnswer(Node noOrigem, Node noDestino, boolean isCoordinator, int sequencial)
	{
		this.noOrigem = noOrigem;
		this.noDestino = noDestino;
		this.isCoordinator = isCoordinator;
		this.sequencial = sequencial;
	}

	@Override
	public Message clone()
	{
		// TODO Auto-generated method stub
		return new MsgAYCoordinatorAnswer(this.noOrigem, this.noDestino, this.isCoordinator, this.sequencial);
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

	public boolean isCoordinator()
	{
		return isCoordinator;
	}

	public void setCoordinator(boolean isCoordinator)
	{
		this.isCoordinator = isCoordinator;
	}

	@Override
	public String toString() {
		return Boolean.toString(this.isCoordinator());
	}
}
