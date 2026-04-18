/**
 * @author Meslin
 *
 */
package projects.paxosV2.nodes.messages.paxos.proposer;

import projects.paxosV2.nodes.messages.MsgGenerica;
import projects.paxosV2.nodes.nodeImplementations.NodeV2;
import sinalgo.nodes.Node;
import sinalgo.nodes.messages.Message;

public class MsgPrepareRequest extends MsgGenerica {

	public int proposalNumber;
	public int proposalValue;

	/**
	 * 
	 * @param noOrigem
	 * @param noDestino
	 * @param sequencial
	 * @param proposalNumber
	 * @param proposalValue
	 */
	public MsgPrepareRequest(Node noOrigem, Node noDestino, int sequencial, int proposalNumber, int proposalValue) {
		this(noOrigem, noDestino, proposalNumber, proposalValue);
		this.sequencial = sequencial;
	}

	/**
	 * 
	 * @param noOrigem
	 * @param noDestino
	 * @param proposalNumber
	 * @param proposalValue
	 */
	public MsgPrepareRequest(Node noOrigem, Node noDestino, int proposalNumber, int proposalValue) {
		this.noOrigem = noOrigem;
		this.noDestino = noDestino;
		this.proposalNumber = proposalNumber;
		this.proposalValue = proposalValue;
	}


	/* (non-Javadoc)
	 * @see sinalgo.nodes.messages.Message#clone()
	 */
	@Override
	public Message clone() {
		return new MsgPrepareRequest(this.noOrigem, this.noDestino, this.sequencial, this.proposalNumber, this.proposalValue);
	}

	
	/* (non-Javadoc)
	 * @see projects.paxosV2.nodes.messages.MsgGenerica#acao(projects.paxosV2.nodes.nodeImplementations.NodeV2)
	 */
	@Override
	public void acao(NodeV2 node) {
		node.recebeuMsgPrepareRequest(this);
	}
	
	@Override
	public String toString() {
		return "proposta " + proposalNumber + " com valor " + proposalValue;
	}
}
