/**
 * @author Meslin
 *
 */
package projects.paxosV1.nodes.messages.paxos.proposer;

import projects.paxosV1.nodes.messages.MsgGenerica;
import projects.paxosV1.nodes.nodeImplementations.NodeV1;
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
	 * @see projects.PaxosV1.nodes.messages.MsgGenerica#acao(projects.PaxosV1.nodes.nodeImplementations.NodeV1)
	 */
	@Override
	public void acao(NodeV1 node) {
		node.recebeuMsgPrepareRequest(this);
	}
	
	@Override
	public String toString() {
		return "proposta " + proposalNumber + " com valor " + proposalValue;
	}
}
