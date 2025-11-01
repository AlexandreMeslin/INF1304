package projects.paxosV1.nodes.messages.paxos.acceptor;

import projects.paxosV1.nodes.messages.MsgGenerica;
import projects.paxosV1.nodes.nodeImplementations.NodeV1;
import sinalgo.nodes.Node;
import sinalgo.nodes.messages.Message;

public class MsgAccepted extends MsgGenerica {
	private int proposalNumber;
	private int proposalValue;
	
	public MsgAccepted(Node noOrigem, Node noDestino, int sequencial, int numero, int valor) {
		this(noOrigem, noDestino, valor);
		this.sequencial = sequencial;
		this.setProposalNumber(numero);
	}

	public MsgAccepted(Node noOrigem, Node noDestino, int valor) {
		this.noOrigem = noOrigem;
		this.noDestino = noDestino;
		this.setProposalValue(valor);
	}

	@Override
	public void acao(NodeV1 node) {
		node.recebeuMsgAccepted(this);
	}

	@Override
	public Message clone() {
		return new MsgAccepted(noOrigem, noDestino, sequencial, getProposalNumber(), getProposalValue());
	}

	@Override
	public String toString() {
		return "proposta " + getProposalNumber() + " com valor " + getProposalValue();
	}

	public int getProposalValue() {
		return proposalValue;
	}

	public void setProposalValue(int proposalValue) {
		this.proposalValue = proposalValue;
	}

	public int getProposalNumber() {
		return proposalNumber;
	}

	public void setProposalNumber(int proposalNumber) {
		this.proposalNumber = proposalNumber;
	}
}
