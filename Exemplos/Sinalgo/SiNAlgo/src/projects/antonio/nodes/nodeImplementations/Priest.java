package projects.antonio.nodes.nodeImplementations;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;
import java.util.Vector;

import projects.antonio.Ballot;
import projects.antonio.CustomGlobal;
import projects.antonio.Tabela;
import projects.antonio.nodes.messages.Accept;
import projects.antonio.nodes.messages.Ack;
import projects.antonio.nodes.messages.Decided;
import projects.antonio.nodes.messages.GeneralMessage;
import projects.antonio.nodes.messages.Prepare;
import projects.antonio.nodes.messages.Promise;
import projects.antonio.nodes.messages.RetransmitAnswer;
import projects.antonio.nodes.messages.RetransmitRequest;
import sinalgo.configuration.Configuration;
import sinalgo.configuration.CorruptConfigurationEntryException;
import sinalgo.configuration.WrongConfigurationException;
import sinalgo.gui.transformation.PositionTransformation;
import sinalgo.nodes.Node;
import sinalgo.nodes.edges.Edge;
import sinalgo.nodes.messages.Inbox;
import sinalgo.runtime.Global;
// import sinalgo.nodes.messages.Message;
import sinalgo.tools.Tools;

public class Priest extends Node {

	static public final int STATE_IDLE = 0;				
	static public final int STATE_TRYING = 1;
	static public final int STATE_POLLING = 2;

	int leaderId = this.ID;				// Initial state as leader.
	
	/** IDLE, TRYING, POLLING */
	int state = Priest.STATE_IDLE;
	boolean firstTime = true;
	int myEpoch = CustomGlobal.epoch;
	int  nextIndex = this.ID;
	private int qtdProposed = 0;		// number of items proposed
	private int proposeRate;			// propose rate - must be configured at Config.xml

	int lastTriedEpoch = -1;			// Leader prepare trial
	int lastTriedIndex = -1;
	
	int lastPromiseEpoch = -1;			// Acceptor promised
	int lastPromiseIndex = -1;
	
	boolean hasAccept = false;
	int lastAccept = -1;				// Acceptor last Accepted (ACK) 
	

	int numPromise = 0;									// Control Promises received
	Vector<String> cmdPromise = new Vector<String>();
		
	int numAcks = 0;						// Control Acks received
	String acceptedCmd = "None";
	
	boolean TOPrepare = false;				// Lider Time Outs.		
	boolean TOAck = false;
	
	int initTOPrepare = -1;
	int initTOAck = -1;
	
	int timePrepare = 0;
	int timeAck = 0;
	int retriesAck = 0;
	Accept accept;	

	int timeFol = 0;						// Follower Time Outs
	int initTOFol = -1;	
	double initTime = 0;

	Vector<Ballot> Ledger = new Vector<Ballot>();
	
	private Tabela tabela = new Tabela();
	int tsAccept = 0;
	int tsAck = 0;
	int tsDecided = 0;
	int tsPrepare = 0;
	int tsPromise = 0;
	int tsRetAnswer = 0;
	int tsRetRequest = 0;

	/**
	 * Priest Constructor
	 */
	public Priest() {
		super();
		try {
			proposeRate = Configuration.getIntegerParameter("proposerate/rate");
		} catch (CorruptConfigurationEntryException e) {
			proposeRate = 100;
			e.printStackTrace();
		}
	}
		
	@Override
	public void preStep() {

		if(firstTime) {
			nextIndex = this.ID;
			if(this.ID == CustomGlobal.chosenLeader) {
				myEpoch++;
			}
			
			timePrepare = Tools.getNodeList().size() * 2;
			timeAck = Tools.getNodeList().size() * 2;
			timeFol = Tools.getNodeList().size() * 2;

			firstTime = false;
			leaderId = this.ID;			
		}
			
		// Only send a prepare message if I am leader, we are trying a new 
		// command and my Epoch and Index are greater than whatever I had promised.
		// Note that if the leader had failed God will increase Epoch and randomly
		// select some node to be the new leader.
		// If the old leader return within this meantime he/she will notice the 
		// new epoch eventually.
		if(this.ID == leaderId && state == Priest.STATE_IDLE && qtdProposed <= (int)(Global.currentTime/proposeRate) && 
				((myEpoch > lastTriedEpoch) || 
				((myEpoch == lastTriedEpoch) && (nextIndex > lastTriedIndex)))) {
			
			// TODO Talvez colocar um intervalo rand�mico para iniciar uma nova proposta.
			qtdProposed++;
			
			// Verificar a possibilidade de pular o prepare e fazer o accept diretamente.
						
			Prepare msg = new Prepare(myEpoch, nextIndex, this.ID, ++tsPrepare);
			lastTriedEpoch = myEpoch;
			lastTriedIndex = nextIndex;
			TOPrepare = true;
			
			CustomGlobal.estatistica(this.ID, "m1: Criando proposta ," + myEpoch + "," + nextIndex);			// TODO início do consenso
			
			initTOPrepare = (int) Tools.getGlobalTime();
			
			// Since the broadcast does not send the message to myself.
			numPromise = 1;
			lastPromiseEpoch = myEpoch;
			lastPromiseIndex = nextIndex;
			
			broadcast(msg);
			CustomGlobal.cPrepare++;
			CustomGlobal.cPromise++;			

			nextIndex += CustomGlobal.n;			// n: number of nodes
			state = Priest.STATE_TRYING;
			setColor(Color.magenta);
			
			initTime = Tools.getGlobalTime();
		}
	}
	
	/**
	 * Realiza o roteamento das mensagens<br>
	 * @param msg mensagem a ser roteada
	 * @return verdadeiro se a mensagem (tamb�m) deve ser tratada localmente
	 */
	private boolean roteamento(GeneralMessage msg) {
		// verifica se deve repassar a mensagem para os outros n�s
		// a mensagem ser� roteada se:
		// - (1) n�o tiver sido originada nesse n� (verificar noOrigem da msg)
		// - (2) n�o tiver sido recebida anteriormente por esse n� (verificar sequ�ncia e tipo da msg)
		// - (3) o destino n�o for unicamente esse n� (verificar noDestino da msg)
		if(msg.get_sender() != this.ID &&	// (1)
		   msg.getSequencial() > tabela.getSequencia(msg.get_sender(), msg.getClass().getSimpleName())) { // (2)
			// mensagem nova de outro n�
			// acrescenta na lista de msg j� recebidas
			tabela.setSequencia(msg.get_sender(), msg.getClass().getSimpleName(), msg.getSequencial());
			if(msg.get_destination() < 0) {
				// mensagem em broadcast
				// repasssa a mensagem e avisa que deve ser tratada localmente
				this.broadcast(msg);
				return true;
			}
			else {
				// mensagem em unicast
				// verifica se � para esse n�
				if(msg.get_destination() == this.ID) {
					return true;
				}
				else {
					// mensagem em unicast para outro n�
					// deve ser repassada
					// n�o deve ser tratada localmente
					this.broadcast(msg);
					return false;
				}
			}
		}
		else {
			// mensagem velha
			// j� est� na tabela de mensagens
			// n�o deve ser repassada
			// n�o deve ser tratada localmente
			return false;
		}
	}

	
	@Override
	public void handleMessages(Inbox inbox) {
		int sender = 0;
		int dest = -1;
		int recEpoch = -1;
		int recIndex = -1;
		int ackEpoch = -1;
		int ackIndex = -1;
		boolean prepare = false;
		
		// Used to store temporarily answers to a prepare request.
		Vector<Integer> receivedPrepareEpoch = new Vector<Integer>();
		Vector<Integer> receivedPrepareIndex = new Vector<Integer>();
		Vector<Integer> receivedPrepareLeader = new Vector<Integer>();

		while(inbox.hasNext()) {
			GeneralMessage msg = (GeneralMessage) inbox.next();
		
			// Routing routine implemented by Alexandre Meslin. 
			// This code was adapted to support its method of following a sequential number per message type.
			if(!roteamento(msg)) continue;
		
			sender = msg.get_sender();						// Gets ID of original sender
			dest = msg.get_destination();
			recEpoch = msg.get_epoch();
			recIndex = msg.get_index();
			
			// If I am a follower, receiving a message from Leader, reset timer. 
			if(leaderId != this.ID && sender == leaderId) {
				initTOFol = (int) Tools.getGlobalTime();
			}
						
			// pedido de atualização de log
			if(msg instanceof RetransmitRequest) {
				// Leader received a request to retransmit an old Ledger entry that is missing in sender's Ledger.	
				boolean exist = false;
				RetransmitRequest request = (RetransmitRequest) msg;

				CustomGlobal.myLog.logln(true, this.ID + " Ret Request: " +  request.missingIndex + " from " + sender);
				
				// find the requested entry and send it to sender.
				for(Ballot b: Ledger) {
					if(b.get_index() == request.missingIndex) {
						// TODO: envia um item
						CustomGlobal.estatistica(this.ID, "Item enviado para atualizar log");
						RetransmitAnswer answer = new RetransmitAnswer(sender, this.ID, request.missingIndex, b.epoch, b.cmd, b.leader, ++tsRetAnswer);
						broadcast(answer);
						exist = true;
						break;
					}
				}
				
				// If it's not in Leader's ledger, Leader broadcast a request.
				if(! exist) {
					if(this.ID == leaderId) {
						CustomGlobal.myLog.logln(true, this.ID + " NOT FOUND in Leader's LEDGER : " +  request.missingIndex + " from " + sender);					
						RetransmitRequest req = new RetransmitRequest(request.missingIndex, this.ID, -1, ++tsRetRequest);
						broadcast(req);			
					}
				}
				
			} else
			if(msg instanceof RetransmitAnswer) {
				// Had requested this ballot to leader and receive it now.
				// Then, store in its local Ledger.
				// I guess, there is a bug in the retransmission protocol because messages
				// can arrive out of order or even be lost. Then, it is necessary to implement a control
				// to assure log's order.
				boolean exist = false;
				RetransmitAnswer answer = (RetransmitAnswer) msg;
				Ballot reqBallot = answer.getBallot();
				
				for(Ballot b: Ledger) {
					if(b.get_index() == reqBallot.index) {
						exist = true;
						break;
					}
				}

				if(!exist) {
					Ledger.add(answer.getBallot());				
				}

				CustomGlobal.myLog.logln(true, this.ID + " Ret Answer: " +  answer.get_index() + " from " + sender);

				
			// Everybody has to vote.
			} else
			if(msg instanceof Prepare) {
				CustomGlobal.myLog.logln(true, this.ID + " Prepare: E " + recEpoch + ":" + recIndex + " from " + sender);

				if(recEpoch > lastPromiseEpoch || (recEpoch == lastPromiseEpoch && recIndex > lastPromiseIndex)) {
					prepare = true;
					receivedPrepareEpoch.add(recEpoch);
					receivedPrepareIndex.add(recIndex);
					receivedPrepareLeader.add(sender);
					state = Priest.STATE_TRYING;
				}
			// The Leader receive a vote for tried value.
			} else if(msg instanceof Promise) {
				if(leaderId == this.ID && state == Priest.STATE_TRYING && dest == this.ID
						&& recEpoch == lastTriedEpoch && recIndex == lastTriedIndex) {
					String cmd = ((Promise) msg).get_cmd();
					numPromise++;
					cmdPromise.add(cmd);
					CustomGlobal.myLog.logln(true, this.ID + " Promise " + recEpoch + ":" + recIndex + " Qtde: " + numPromise + " cmd " + cmd  + " from " + sender);
				} else {
					continue;
				}
				
			// Acceptor
			} else if(msg instanceof Accept) {
				Ack ack = new Ack(sender, recEpoch, recIndex, this.ID, ++tsAck);
				String cmd = ((Accept) msg).get_cmd();

				CustomGlobal.myLog.logln(true, Tools.getGlobalTime() + " - " + this.ID + " Accept: " + recEpoch + ":" + recIndex + " lastPromised " + lastPromiseEpoch + ":" + lastPromiseIndex + " cmd " + cmd + " from " + sender);

				if(! Ledger.isEmpty()) {
				// We are going to check if there are gaps in the Ledger, but we will ignore
				// if the accept will create a new gap. Because it is only an accept.
					int idxPrev = 0;
					int idxCurr = 0;
					
					// Find gaps in the ledger
					for(Ballot b: Ledger) {
						idxCurr = Integer.parseInt(b.cmd.split("-")[1]);
						
						if(idxCurr > idxPrev + 1 && idxPrev > 0) {
							while(idxPrev < idxCurr - 1) {
								// envia pedido para para atualizar o log
								RetransmitRequest req = new RetransmitRequest(++idxPrev, this.ID, sender, ++tsRetRequest);
								broadcast(req);													
							}
						}
						idxPrev = idxCurr;						
					}					
				}

				if(recEpoch > lastPromiseEpoch || (recEpoch == lastPromiseEpoch && recIndex >= lastPromiseIndex)) {
					if(state != Priest.STATE_IDLE) {
						// TODO: descartou um item
						CustomGlobal.estatistica(this.ID, "m2: descartou um item");
					}
					leaderId = sender;
					TOPrepare = false;
					TOAck = false;
					myEpoch = recEpoch;
					initTOFol = (int) Tools.getGlobalTime();					
					hasAccept = true;
					acceptedCmd = cmd;

					ack.set_cmd(acceptedCmd);
					ack.set_flag(true);
					state = Priest.STATE_POLLING;
					this.setColor(Color.GREEN);
				} else {
					// I had promised to another lider with a greater epoch:index
					ack.set_flag(false);
					ack.setNewEpoch(lastPromiseEpoch);
					ack.setNewEpoch(leaderId);
				}
								
				CustomGlobal.myLog.logln(true, Tools.getGlobalTime() + " - " +  this.ID + " Sending ACK " + recEpoch + ":" + recIndex + " to " + sender);
				sendMsg((GeneralMessage) ack, Tools.getNodeByID(sender));
				CustomGlobal.cAck++;
				
			// Proposer	
			} else if(msg instanceof Ack) {				
				if(dest != this.ID) {
					CustomGlobal.myLog.logln(false, Tools.getGlobalTime() + " - " + this.ID + " Forward Ack: E " + recEpoch + ":" + recIndex + " from " + sender + " to " + dest);
					continue;
				}
				
				Ack ack = (Ack) msg;
				CustomGlobal.myLog.logln(true, Tools.getGlobalTime() + " - " +  this.ID + " Ack: E " + recEpoch + ":" + recIndex + " sender: " + sender);

				// It is only important if I am leader and meet my accept message.
				if(leaderId == this.ID && recEpoch == lastTriedEpoch && recIndex == lastTriedIndex) {
					if(ack.get_flag() ) {
						numAcks++;
						ackEpoch = ack.get_epoch();
						ackIndex = ack.get_index();
					} else {
						if(state != Priest.STATE_IDLE) {
							// TODO: descartou um item
							CustomGlobal.estatistica(this.ID, "m2: descartou um item");
						}
						myEpoch = ack.getNewEpoch();
						leaderId = ack.getNewLeader();
						this.setColor(Color.orange);
						TOPrepare = false;
						TOAck = false;
						initTOFol = (int) Tools.getGlobalTime();
						state = Priest.STATE_TRYING;
						CustomGlobal.myLog.logln(true, this.ID + " NACK E " + recEpoch + ":" + recIndex + " sender: " + sender);
					}
				}
				
			// ALL
			} else if(msg instanceof Decided) {
				if (state == Priest.STATE_POLLING) {
					Decided dec = (Decided) msg;
					boolean exists = false;
					
					CustomGlobal.myLog.logln(true, Tools.getGlobalTime() + " - " + this.ID + " Decided: " + "E " + recEpoch + ":" + recIndex + " - " + dec.get_cmd() + " sender: " + sender);

					for(Ballot b: Ledger) {
						if(b.get_priestId() == this.ID && b.get_epoch() == dec.get_epoch() 
								&& b.get_index() == dec.get_index()) {
							exists = true;
							break;
						}	
					}
				
					if(! exists) {
						Ballot newBallot = new Ballot(this.ID, dec.get_epoch(), dec.get_index(), dec.get_cmd(), leaderId);
						Ledger.add(newBallot);
					}
				
					state = Priest.STATE_IDLE;
					hasAccept = false;
				}
			}
			
		}  // End of WHILE
		
		//
		// Prepare
		//
		if(prepare) {
			int pEpoch = -1;
			int pIndex = -1;
			int pSender = -1;
			int i, l;
			
			prepare = false;
			
			for(int rEpoch: receivedPrepareEpoch) {
				i = receivedPrepareIndex.remove(0);
				l = receivedPrepareLeader.remove(0);
				
				if(rEpoch > pEpoch || (rEpoch == pEpoch && i > pIndex)) {
					pEpoch = rEpoch;
					pIndex = i;
					pSender = l;
				}
			}
			
			receivedPrepareEpoch.clear();
			receivedPrepareIndex.clear();
			receivedPrepareLeader.clear();
			
			
			if(pEpoch > myEpoch || (pEpoch == myEpoch && pIndex > lastTriedIndex)) {
				if(state != Priest.STATE_IDLE) {
					// TODO: descartou um item
					CustomGlobal.estatistica(this.ID, "m2: descartou um item");
				}
				myEpoch = pEpoch;
				leaderId = sender;
				TOPrepare = false;
				TOAck = false;
				initTOFol = (int) Tools.getGlobalTime();					

				state = Priest.STATE_TRYING;
				this.setColor(Color.GREEN);
			}						
			
			lastPromiseEpoch = pEpoch;
			lastPromiseIndex = pIndex;
			
			Promise msg_prom = new Promise(pEpoch, pIndex, pSender, this.ID, ++tsPromise);
			
			if(hasAccept) {
				msg_prom.set_cmd(acceptedCmd);
			} else {
				msg_prom.set_cmd("None");
			}
			
			leaderId = pSender;
			CustomGlobal.myLog.logln(true, this.ID + " Sending promise to: " + pSender);		
			
			CustomGlobal.cPromise++;
			sendMsg((GeneralMessage) msg_prom, Tools.getNodeByID(pSender));
		}
		
		
		//
		// PROPOSER RECEIVED MAJORITY OF PROMISES
		//
		if(numPromise > CustomGlobal.n/2 && state == Priest.STATE_TRYING) {
			int id = -1;
			int last_id = 0;
			boolean first = true;
			String selCmd = "None";
						
			TOPrepare = false;						// Disable TimeOut Prepare
								
			for(String cmd: cmdPromise) {
				if(cmd != "None" && cmd.length() >=1) {		
					for(String part: cmd.split(":")) {
						if(first && part != null && part.length() >= 1) {
							id = Integer.parseInt(part);
							first = false;
						}
					}
					
					if(id > last_id) {
						last_id = id;
						selCmd = cmd;
					}
				}
			}
			
			if(selCmd == "None") {
				selCmd = Integer.toString(lastTriedEpoch) + Integer.toString(lastTriedIndex) +  ":decree-" + CustomGlobal.decree;
			} 
						
			state = Priest.STATE_POLLING;
			cmdPromise.clear();
			numPromise = 0;
			accept = new Accept(recEpoch, recIndex, selCmd, this.ID, ++tsAccept);
			CustomGlobal.cAccept++;
			broadcast(accept);
			TOAck = true;
			initTOAck = (int) Tools.getGlobalTime();		
			
			CustomGlobal.myLog.logln(true, this.ID + " Sending Accept cmd: " + selCmd);			
			// Broadcast not send to myself. Then, I give my vote now.
			numAcks = 1;
			hasAccept = true;
			acceptedCmd = selCmd;
		}
		
		
		//
		// ACK
		//
		if(numAcks > CustomGlobal.n/2) {
			Double interval = Tools.getGlobalTime() - initTime;			
			CustomGlobal.TimeDecide.add(interval);
			
			TOAck = false;								// Disable TimeOut Acks.

			Decided decided = new Decided(ackEpoch, ackIndex, acceptedCmd, this.ID, ++tsDecided);
			broadcast(decided);

			CustomGlobal.estatistica(this.ID, "m1: Decided ," + ackEpoch + "," + ackIndex);	// TODO: fim consenso

			CustomGlobal.cDecided++;
			Ballot newBallot = new Ballot(this.ID, ackEpoch, ackIndex, acceptedCmd, leaderId);
			
			Ledger.add(newBallot);
			
			// Laws is a general repository just to know what was decided, not part of the protocol and
			// used in another part.
			CustomGlobal.Laws.add(newBallot);

			CustomGlobal.myLog.logln(true, this.ID + " Sent Decided: " + acceptedCmd);			
			
			acceptedCmd = "";
			CustomGlobal.decree++;
			numAcks = 0;
			state = Priest.STATE_IDLE;
		}
	}

	@Override
	public void init() {}

	@Override
	public void neighborhoodChange() {}

	@Override
	public void postStep() {
		// Check TimeOuts
		int currTime = (int) Tools.getGlobalTime();
		Random rand = new Random();
				
		if(leaderId == this.ID) {
			if(TOPrepare) {
				if(currTime > (initTOPrepare + timePrepare)) {					
					if(rand.nextInt(CustomGlobal.n) < this.ID) {
						state = Priest.STATE_IDLE; 			// it will send prepare again.
						TOPrepare = false;
						CustomGlobal.myLog.logln(true, Tools.getGlobalTime() + ":   " + this.ID + " TimeOut Prepare " + lastTriedEpoch + ":" + lastTriedIndex);
						timePrepare = Tools.getNodeList().size() * 2;
					} else {
						timePrepare += (this.ID + rand.nextInt(CustomGlobal.n));
					}
				}				
			} else if (TOAck) {
				if(currTime > (initTOAck + timeAck) && state == Priest.STATE_POLLING) {
					if(rand.nextInt(CustomGlobal.n) < this.ID) {

						timeAck = Tools.getNodeList().size() * 2;

						if(retriesAck < 2) {
							CustomGlobal.myLog.logln(true, Tools.getGlobalTime() + ":   " + this.ID + " TimeOut ACK");
							broadcast(accept);
							retriesAck++;
							initTOAck = (int) Tools.getGlobalTime();
						} else {
							CustomGlobal.myLog.logln(true, Tools.getGlobalTime() + ":   " + this.ID + " TimeOut ACK - Starting prepare again");
							retriesAck = 0;
							TOAck = false;
							state = Priest.STATE_IDLE;
						}
					}
					else {
						timeAck += this.ID;
					}
				}				
			}									
		} else {
			if(currTime > (initTOFol + timeFol)) {
				if((this.outgoingConnections.size() > 0) &&
				  (rand.nextInt(CustomGlobal.n) <= this.ID)) {
					myEpoch++;
					CustomGlobal.myLog.logln(true, Tools.getGlobalTime() + ":   " + this.ID + " TimeOut Follower " + initTOFol + " my leader was " + leaderId);
					leaderId = this.ID;	
					state = Priest.STATE_IDLE;
					timeFol = Tools.getNodeList().size() * 2;
				} else {
					timeFol += (this.ID + rand.nextInt(CustomGlobal.n));
				}
			}
		}
	}

	@Override
	public void checkRequirements() throws WrongConfigurationException {}
	
	
	private boolean checkConnection(int id) {
		
		for(Edge e: this.outgoingConnections) {
			if(e.endNode.ID == id) {
				return true;
			}
		}
		
		return false;
	}


	private void sendMsg(GeneralMessage msg, Node dest) {
		if(dest == null || msg == null) {
			CustomGlobal.myLog.logln(true, "Null in send");
			return;
		}
		
		if(checkConnection(dest.ID)) {
			this.send(msg, dest);
		} else {
			this.broadcast(msg);
		}
	}

	
	public void draw(Graphics g, PositionTransformation pt, boolean highlight) {
		String text = Integer.toString(this.ID) + "/" + Integer.toString(leaderId);
		this.defaultDrawingSizeInPixels = 12;
		super.drawNodeAsDiskWithText(g, pt, false, text, 6, Color.BLACK);
		//super.drawNodeAsSquareWithText(g, pt, highlight, text, 10, Color.YELLOW);
	}
}
