/**
 * 
 */
package projects.paxosV2.nodes.messages.convite;

import projects.paxosV2.CustomGlobal;
import projects.paxosV2.nodes.messages.MsgGenerica;
import projects.paxosV2.nodes.nodeImplementations.NodeV2;
import projects.paxosV2.nodes.nodeImplementations.NodeV2.EnumState;
import sinalgo.nodes.Node;
import sinalgo.nodes.messages.Message;
import sinalgo.runtime.Global;

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

	public Node getCoordenador()
	{
		return coordenador;
	}

	public void setCoordenador(Node coordenador)
	{
		this.coordenador = coordenador;
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

	/**
	 * Thread para tratar um Convite
	 * Invitation
	 */
	@Override
	public void acao(NodeV2 node) {
		node.timerMerge.stop();
		if(node.state == EnumState.NORMAL) {
			CustomGlobal.consoleln(Global.currentTime + " nó " + node + " recebeu convite de " + getNoOrigem());
			node.fase = getClass().getSimpleName();
			node.stopProcessing();
			Node oldCoordinator = node.coordinator;
			node.upSet = node.up;
			node.state = EnumState.ELECTION;
			node.coordinator = getCoordenador();
			node.group = getGroup();
			if(oldCoordinator.ID == node.ID) {
				for(Node noDestino: node.upSet) {
					setSequencial(node.sequencial.valor(this));
					setNoOrigem(node);
					setNoDestino(noDestino);
					node.broadcast(this);
				}
			}
			MsgAccept msgAccept = new MsgAccept(node, node.coordinator, node.group);
			msgAccept.setSequencial(node.sequencial.valor(msgAccept));
			node.broadcast(msgAccept);
			node.timerAcceptAnswer.start(node.timerAcceptAnswer.getDefault(), node);
		}
		else {
			//CustomGlobal.estatistica(node.ID, "recusado", Integer.toString(getNoOrigem().ID));
		}
	}
}
