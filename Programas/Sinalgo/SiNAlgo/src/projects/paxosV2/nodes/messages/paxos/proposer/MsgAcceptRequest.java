package projects.paxosV2.nodes.messages.paxos.proposer;

import projects.paxosV2.nodes.messages.MsgGenerica;
import projects.paxosV2.nodes.nodeImplementations.NodeV2;
import sinalgo.nodes.Node;
import sinalgo.nodes.messages.Message;

public class MsgAcceptRequest extends MsgGenerica {

	public int proposalNumber;
	public int proposalValue;

	/**
	 * 
	 * @param noOrigem
	 * @param noDestino
	 * @param sequencial
	 * @param proposta
	 * @param valor
	 */
	public MsgAcceptRequest(Node noOrigem, Node noDestino, int sequencial, int proposta, int valor) {
		this(noOrigem, noDestino, proposta, valor);
		this.sequencial = sequencial;
	}

	public MsgAcceptRequest(Node noOrigem, Node noDestino, int proposta, int valor) {
		this.noOrigem = noOrigem;
		this.noDestino = noDestino;
		this.proposalNumber = proposta;
		this.proposalValue = valor;
	}

	@Override
	public void acao(NodeV2 node) {
		node.recebeuMsgAcceptRequest(this);
	}

	@Override
	public Message clone() {
		return new MsgAcceptRequest(noOrigem, noDestino, proposalNumber, proposalNumber, proposalValue);
	}

	@Override
	public String toString() {
		return "proposta " + proposalNumber + " com valor " + proposalValue;
	}
}
