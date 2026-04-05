package projects.v1.nodes.messages;

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
}
