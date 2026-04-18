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
public class MsgInvitation extends MsgGenerica {
	/** id do coordenador */
	private Node coordenador;
	/** identificador do grupo composto pelo id do coordenador e contador */
	private long group;

	/**
	 * Constroe uma mensagem do tipo INVITE
	 */
	public MsgInvitation(Node noOrigem, Node noDestino, Node coordenador, int sequencial, long group)
	{
		this.noOrigem = noOrigem;
		this.noDestino = noDestino;
		this.coordenador = coordenador;
		this.sequencial = sequencial;
		this.group = group;
	}

	/* (non-Javadoc)
	 * @see sinalgo.nodes.messages.Message#clone()
	 */
	@Override
	public Message clone()
	{
		return new MsgInvitation(this.noOrigem, this.noDestino, this.coordenador, this.sequencial, this.group);
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

	public Node getCoordenador()
	{
		return coordenador;
	}

	public void setCoordenador(Node coordenador)
	{
		this.coordenador = coordenador;
	}

	public int getSequencial()
	{
		return sequencial;
	}

	public void setSequencial(int sequencial)
	{
		this.sequencial = sequencial;
	}

	public long getGroup() {
		return group;
	}

	public void setGroup(long group) {
		this.group = group;
	}
	
	@Override
	public String toString() {
		return "convite para o grupo " + this.getGroup() + " coordenado por " + this.getCoordenador();
	}
}
