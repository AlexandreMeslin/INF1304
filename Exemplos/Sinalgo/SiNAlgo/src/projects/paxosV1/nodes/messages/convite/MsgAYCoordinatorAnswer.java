package projects.paxosV1.nodes.messages.convite;

import projects.paxosV1.nodes.messages.MsgGenerica;
import projects.paxosV1.nodes.nodeImplementations.NodeV1;
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
		this(noOrigem, noDestino, isCoordinator);
		this.sequencial = sequencial;
	}

	public MsgAYCoordinatorAnswer(Node noOrigem, Node noDestino, boolean isCoordinator) {
		this.noOrigem = noOrigem;
		this.noDestino = noDestino;
		this.isCoordinator = isCoordinator;
	}

	@Override
	public Message clone()
	{
		// TODO Auto-generated method stub
		return new MsgAYCoordinatorAnswer(this.noOrigem, this.noDestino, this.isCoordinator, this.sequencial);
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

	/**
	 * AYCoordinatorAnswer
	 * checkMembers (continuação)
	 */
	@Override
	public void acao(NodeV1 node) {
		if(isCoordinator()) {
			node.fase = getClass().getSimpleName();
			node.others.add(getNoOrigem());
		}
	}
}
