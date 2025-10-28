package projects.paxosV2.nodes.messages.convite;

import projects.paxosV2.CustomGlobal;
import projects.paxosV2.nodes.messages.MsgGenerica;
import projects.paxosV2.nodes.nodeImplementations.NodeV2;
import sinalgo.nodes.Node;
import sinalgo.nodes.messages.Message;

public class MsgAYThere extends MsgGenerica {
	/** id do grupo */
	private long group;

	/**
	 * 
	 * @param noOrigem
	 * @param noDestino	coordenador
	 * @param sequencial
	 * @param group
	 */
	public MsgAYThere(Node noOrigem, Node noDestino, int sequencial, long group)
	{
		this(noOrigem, noDestino, group);
		this.sequencial = sequencial;
	}

	public MsgAYThere(Node noOrigem, Node noDestino, long group) {
		this.noOrigem = noOrigem;
		this.noDestino = noDestino;
		this.group = group;
	}

	@Override
	public Message clone()
	{
		return new MsgAYThere(this.noOrigem, this.noDestino, this.sequencial, this.group);
	}

	public long getGroup() {
		return group;
	}

	public void setGroup(long group) {
		this.group = group;
	}
	
	@Override
	public String toString() {
		return "no grupo " + this.getGroup() + " estando no grupo " + this.group;
	}

	/**
	 * Are You There? 
	 * mensagem de nó para coordenador
	 */
	@Override
	public void acao(NodeV2 node) {
		MsgAYThereAnswer msgAYThereAnswer = new MsgAYThereAnswer(node, getNoOrigem(), true, CustomGlobal.makeGroup(node.ID, node.counter), getGroup());
		msgAYThereAnswer.setSequencial(node.sequencial.valor(msgAYThereAnswer));
		if(node.group == getGroup() && node.coordinator.ID == node.ID) {
			node.fase = getClass().getSimpleName() + " true";
			msgAYThereAnswer.setCoordinator(true);
		}
		else {
			node.fase = getClass().getSimpleName() + " false";
			msgAYThereAnswer.setCoordinator(false);
		}
		node.broadcast(msgAYThereAnswer);
	}
}
