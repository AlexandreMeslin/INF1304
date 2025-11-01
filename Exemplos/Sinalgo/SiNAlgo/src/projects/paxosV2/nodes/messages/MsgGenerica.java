/**
 * @author Meslin
 *
 */
package projects.paxosV2.nodes.messages;

import projects.paxosV2.nodes.nodeImplementations.NodeV2;
import sinalgo.nodes.Node;
import sinalgo.nodes.messages.Message;

public abstract class MsgGenerica extends Message {
	/** origem da mensagem */
	protected Node noOrigem;
	/** destino da mensagem */
	protected Node noDestino;
	/** contador de mensagens, utilizado para que os nós não repassem mensagens repetidas */
	protected int sequencial;

	/**
	 * 
	 */
	public MsgGenerica() {
	}
	
	/**
	 * Reage à mensagem
	 * @param node
	 */
	public abstract void acao (NodeV2 node);

	public final Node getNoOrigem() {
		return this.noOrigem;
	}

	public final void setNoOrigem(Node noOrigem) {
		this.noOrigem = noOrigem;
	}

	public final Node getNoDestino() {
		return this.noDestino;
	}

	public final void setNoDestino(Node noDestino) {
		this.noDestino = noDestino;
	}

	public final int getSequencial() {
		return this.sequencial;
	}

	public final void setSequencial(int sequencial) {
		this.sequencial = sequencial;
	}
	
	@Override
	public String toString() {
		return " nó origem: " + this.getNoOrigem() + ", nó destino: " + this.getNoDestino() + ", seq: " + this.getSequencial(); 
	}
}
