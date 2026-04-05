package projects.paxosV1.nodes.messages.paxos.acceptor;

import projects.paxosV1.nodes.messages.MsgGenerica;
import projects.paxosV1.nodes.nodeImplementations.NodeV1;
import sinalgo.nodes.Node;
import sinalgo.nodes.messages.Message;

public class MsgPrepareResponse extends MsgGenerica {
	public boolean hasPrevious;
	public int proposalNumber;
	public int proposalValue;

	/**
	 * 
	 * @param noOrigem
	 * @param noDestino
	 * @param sequencial
	 * @param previous
	 * @param proposalNumber
	 * @param proposalValue
	 */
	public MsgPrepareResponse(Node noOrigem, Node noDestino, int sequencial, boolean previous, int proposalNumber, int proposalValue) {
		this(noOrigem, noDestino, previous, proposalNumber, proposalValue);
		this.sequencial = sequencial;
	}

	/**
	 * 
	 * @param noOrigem
	 * @param noDestino
	 * @param previous
	 * @param proposalNumber
	 * @param proposalValue
	 */
	public MsgPrepareResponse(Node noOrigem, Node noDestino, boolean previous, int proposalNumber, int proposalValue) {
		this.noOrigem = noOrigem;
		this.noDestino = noDestino;
		this.hasPrevious = previous;
		this.proposalNumber = proposalNumber;
		this.proposalValue = proposalNumber;
	}

	@Override
	public Message clone() {
		return new MsgPrepareResponse(noOrigem, noDestino, sequencial, hasPrevious, proposalNumber, proposalValue);
	}

	/**
	 * Proposer recebe a mensagem PrepareResponse
	 */
	@Override
	public void acao(NodeV1 node) {
		node.recebeuMsgPrepareResponse(this);
	}

	@Override
	public String toString() {
		return "proposta " + proposalNumber + " com valor " + proposalValue + (hasPrevious? "" : " não ") + "tem proposta prévia";
	}
}
