package projects.paxosV1.nodes.messages.convite;

import projects.paxosV1.nodes.messages.MsgGenerica;
import projects.paxosV1.nodes.nodeImplementations.NodeV1;
import projects.paxosV1.nodes.nodeImplementations.NodeV1.EnumState;
import sinalgo.nodes.Node;
import sinalgo.nodes.messages.Message;

public class MsgReady extends MsgGenerica {
	/** id do grupo (id do coordenador + contador) */
	private long group;
	/** definição do estado da aplicação fictícia */
	private int definition;

	public MsgReady(Node noOrigem, Node noDestino, int sequencial, long group, int definition)
	{
		this.noOrigem = noOrigem;
		this.noDestino = noDestino;
		this.sequencial = sequencial;
		this.group = group;
		this.definition = definition;
	}

	@Override
	public Message clone()
	{
		return new MsgReady(this.noOrigem, this.noDestino, this.sequencial, this.group, this.definition);
	}

	public long getGroup() {
		return group;
	}

	public void setGroup(long group) {
		this.group = group;
	}

	public int getDefinition() {
		return definition;
	}

	public void setDefinition(int definition) {
		this.definition = definition;
	}

	@Override
	public String toString() {
		return "para o grupo " + this.getGroup();
	}

	/**
	 * Ready
	 */
	@Override
	public void acao(NodeV1 node) {
		MsgReadyAnswer msgReadyAnswer = new MsgReadyAnswer(node, node.coordinator, true, node.group);
		msgReadyAnswer.setSequencial(node.sequencial.valor(msgReadyAnswer));
		if(node.group == getGroup() && node.state == EnumState.REORGANIZING) {
			node.fase = getClass().getSimpleName() + " true";
			node.definition++;		// only if in Reorganizing
			node.state = EnumState.NORMAL;
			msgReadyAnswer.setInGroup(true);
		}
		else {
			msgReadyAnswer.setInGroup(false);
		}
		node.broadcast(msgReadyAnswer);
	}
}
