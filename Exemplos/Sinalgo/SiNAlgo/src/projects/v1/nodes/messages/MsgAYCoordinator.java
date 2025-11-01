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
		this.noOrigem = noOrigem;
		this.noDestino = noDestino;
		this.id = id;
		this.sequencial = sequencial;
	}

	@Override
	public Message clone()
	{
		// TODO Auto-generated method stub
		return new MsgAYCoordinator(this.noOrigem, this.noDestino, this.id, this.sequencial);
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

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getSequencial() {
		return sequencial;
	}

	public void setSequencial(int sequencial) {
		this.sequencial = sequencial;
	}
	
	@Override
	public String toString() {
		return "porque eu, " + this.noOrigem + ", quero saber";
	}
}
