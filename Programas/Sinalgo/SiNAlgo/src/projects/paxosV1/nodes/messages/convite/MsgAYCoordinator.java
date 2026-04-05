/**
 * 
 */
package projects.paxosV1.nodes.messages.convite;

import projects.paxosV1.nodes.messages.MsgGenerica;
import projects.paxosV1.nodes.nodeImplementations.NodeV1;
import projects.paxosV1.nodes.nodeImplementations.NodeV1.EnumState;
import sinalgo.nodes.Node;
import sinalgo.nodes.messages.Message;

/**
 * @author meslin
 *
 */
public class MsgAYCoordinator extends MsgGenerica {
	/** id do coordenador que está enviando a mensagem */
	private int id;
	
	/**
	 * 
	 * @param noOrigem
	 * @param noDestino
	 * @param id
	 * @param sequencial
	 */
	public MsgAYCoordinator(Node noOrigem, Node noDestino, int id, int sequencial)
	{
		this(noOrigem, noDestino, id);
		this.sequencial = sequencial;
	}
	public MsgAYCoordinator(Node noOrigem, Node noDestino, int id) {
		this.noOrigem = noOrigem;
		this.noDestino = noDestino;
		this.id = id;
	}

	@Override
	public Message clone()
	{
		// TODO Auto-generated method stub
		return new MsgAYCoordinator(this.noOrigem, this.noDestino, this.id, this.sequencial);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	@Override
	public String toString() {
		return "porque eu, " + this.noOrigem + ", quero saber";
	}

	/**
	 * AYCoordinator
	 */
	@Override
	public void acao(NodeV1 node) {
		MsgAYCoordinatorAnswer msgAYCoordinatorAnswer = new MsgAYCoordinatorAnswer(node, getNoOrigem(), true);
		msgAYCoordinatorAnswer.setSequencial(node.sequencial.valor(msgAYCoordinatorAnswer));
		if(node.state == EnumState.NORMAL && node.coordinator.ID == node.ID) {
			node.fase = getClass().getSimpleName() + " true";
			msgAYCoordinatorAnswer.setCoordinator(true);
		}
		else {
			msgAYCoordinatorAnswer.setCoordinator(false);
		}
		node.broadcast(msgAYCoordinatorAnswer);
	}
}
