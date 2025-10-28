package projects.antonio.nodes.messages;

import sinalgo.nodes.messages.Message;

// During the merge process, the Coordinator with the highest p_id send an invitation
// to other coordinators to merge under its coordination.
public class Prepare extends GeneralMessage {	

	public Prepare(int epoch, int index, int senderId, int seq) {
		this.epoch = epoch;
		this.index = index;
		this.sender = senderId;
		this.sequential = seq;
	}
	
	@Override
	public Message clone() {
		return this;
	}

}
