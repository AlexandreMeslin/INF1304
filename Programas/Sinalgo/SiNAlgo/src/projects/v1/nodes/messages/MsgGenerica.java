/**
 * 
 */
package projects.v1.nodes.messages;

import sinalgo.nodes.Node;
import sinalgo.nodes.messages.Message;

/**
 * @author meslin
 *
 */
public class MsgGenerica extends Message {
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
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see sinalgo.nodes.messages.Message#clone()
	 */
	@Override
	public Message clone() {
		// TODO Auto-generated method stub
		return null;
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
	
	@Override
	public String toString() {
		return " nó origem: " + this.getNoOrigem() + ", nó destino: " + this.getNoDestino() + ", seq: " + this.getSequencial(); 
	}
}
