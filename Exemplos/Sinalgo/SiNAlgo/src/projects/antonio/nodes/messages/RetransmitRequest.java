package projects.antonio.nodes.messages;

import sinalgo.nodes.messages.Message;

// Answer accepting the invitation to merge.
public class RetransmitRequest extends GeneralMessage {
	public int missingIndex;
	
	public RetransmitRequest(int requestedIndex, int senderId, int dest, int seq) {
		this.missingIndex = requestedIndex;
		this.sender = senderId;
		this.dest = dest;
		this.sequential = seq;
	}
						
	@Override
	public Message clone() {
		// TODO Auto-generated method stub
		return this;
	}

}
