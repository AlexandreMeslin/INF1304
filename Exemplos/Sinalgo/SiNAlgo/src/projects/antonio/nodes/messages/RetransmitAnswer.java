package projects.antonio.nodes.messages;

import projects.antonio.Ballot;
import sinalgo.nodes.messages.Message;

// Answer accepting the invitation to merge.
public class RetransmitAnswer extends GeneralMessage {
	private int leaderId;
	private String cmd;
	
	public RetransmitAnswer(int dest, int senderId, int requestedIndex, int epoch, String cmd, int leaderId, int seq) {
		this.dest = dest;
		this.sender = senderId;
		this.epoch = epoch;
		this.index = requestedIndex;
		this.leaderId = leaderId;
	    this.cmd = cmd;	
	    this.sequential = seq;
	}
	
	public Ballot getBallot() {
		Ballot ballot = new Ballot(dest, epoch, index, cmd, leaderId);		
		return ballot;
	}
						
	@Override
	public Message clone() {
		// TODO Auto-generated method stub
		return this;
	}

}
