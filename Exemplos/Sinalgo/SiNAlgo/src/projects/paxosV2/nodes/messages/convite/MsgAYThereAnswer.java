package projects.paxosV2.nodes.messages.convite;

import projects.paxosV2.CustomGlobal;
import projects.paxosV2.nodes.messages.MsgGenerica;
import projects.paxosV2.nodes.nodeImplementations.NodeV2;
import sinalgo.nodes.Node;
import sinalgo.nodes.messages.Message;
import sinalgo.runtime.Global;

public class MsgAYThereAnswer extends MsgGenerica {
	/** informa se o coordenador está ativo ou não */
	private boolean isCoordinator;
	private long groupLocal;
	private long groupRemote;

	/**
	 * 
	 * @param noOrigem
	 * @param noDestino
	 * @param sequencial
	 * @param isCoordinator
	 * @param groupLocal
	 * @param groupRemote
	 */
	public MsgAYThereAnswer(Node noOrigem, Node noDestino, int sequencial, boolean isCoordinator, long groupLocal, long groupRemote)
	{
		this(noOrigem, noDestino, isCoordinator, groupLocal, groupRemote);
		this.sequencial = sequencial;
	}

	public MsgAYThereAnswer(Node noOrigem, Node noDestino, boolean isCoordinator, long groupLocal, long groupRemote) {
		this.noOrigem = noOrigem;
		this.noDestino = noDestino;
		this.isCoordinator = isCoordinator;
		this.groupLocal = groupLocal;
		this.groupRemote = groupRemote;
	}

	@Override
	public Message clone()
	{
		return new MsgAYThereAnswer(this.noOrigem, this.noDestino, this.sequencial, this.isCoordinator, this.groupLocal, this.groupRemote);
	}

	public boolean isCoordinator()
	{
		return isCoordinator;
	}

	public void setCoordinator(boolean isCoordinator)
	{
		this.isCoordinator = isCoordinator;
	}

	public long getGroupLocal() {
		return groupLocal;
	}

	public void setGroupLocal(long groupLocal) {
		this.groupLocal = groupLocal;
	}

	public long getGroupRemote() {
		return groupRemote;
	}

	public void setGroupRemote(long groupRemote) {
		this.groupRemote = groupRemote;
	}

	@Override
	public String toString() {
		return Boolean.toString(this.isCoordinator()) + " grupo local " + getGroupLocal() + ", grupo remoto " + getGroupRemote();
	}

	/**
	 * Quando um Membro suspeita de falha do Coordenador (continuação)
	 * checkCoordinator
	 *
	 * AYThereAnswer
	 */
	@Override
	public void acao(NodeV2 node) {
		node.timerCheckCoordinator.stop();
		if(isCoordinator()) {
			node.fase = getClass().getSimpleName() + " true";
		}
		else {
			node.fase = getClass().getSimpleName() + " false";
			CustomGlobal.consoleln(Global.currentTime + " nó " + node + " em recovery porque o coordenador " + getNoOrigem() + " respondeu não: " + CustomGlobal.formataGrupo(getGroupLocal()) + "/" + CustomGlobal.formataGrupo(getGroupRemote()));
			node.recovery();
			//CustomGlobal.estatistica(node.ID, "recovery4");
		}
	}
}
