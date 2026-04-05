package projects.paxosV2.nodes.messages.convite;

import projects.paxosV2.CustomGlobal;
import projects.paxosV2.nodes.messages.MsgGenerica;
import projects.paxosV2.nodes.nodeImplementations.NodeV2;
import projects.paxosV2.nodes.nodeImplementations.NodeV2.EnumState;
import sinalgo.nodes.Node;
import sinalgo.nodes.messages.Message;
import sinalgo.runtime.Global;

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
		this(noOrigem, noDestino, group);
		this.sequencial = sequencial;
	}

	public MsgAccept(Node noOrigem, Node noDestino, long group) {
		this.noOrigem = noOrigem;
		this.noDestino = noDestino;
		this.group = group;
	}

	@Override
	public Message clone()
	{
		return new MsgAccept(this.noOrigem, this.noDestino, this.sequencial, this.group);
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

	@Override
	public void acao(NodeV2 node) {
		/*
		 * Accept
		 */
		MsgAcceptAnswer msgAcceptAnswer = new MsgAcceptAnswer(node, getNoOrigem(), true);
		msgAcceptAnswer.setSequencial(node.sequencial.valor(msgAcceptAnswer));
		if(node.state == EnumState.ELECTION && node.coordinator.ID == node.ID && node.group == getGroup()) {
			node.fase = getClass().getSimpleName() + " true";
			CustomGlobal.consoleln(Global.currentTime + " nó " + node + " aceitou " + getNoOrigem().ID + " no grupo");
			// only if in Election and for new Group
			node.up.add(getNoOrigem());		// Up is used by Merge()
			msgAcceptAnswer.setInGroup(true);
		}
		else {
			node.fase = getClass().getSimpleName() + " false";
			msgAcceptAnswer.setInGroup(false);
		}
		node.broadcast(msgAcceptAnswer);
	}
}
