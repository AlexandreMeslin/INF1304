package projects.p2pDistribuicaoConcorrencia.nodes.nodeImplementations;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

import p2pDistribuicaoConcorrencia.nodes.messages.IdMessage;
import p2pDistribuicaoConcorrencia.nodes.messages.LastSavedRecordMessage;
import p2pDistribuicaoConcorrencia.nodes.messages.NextRecordNumberMessage;
import p2pDistribuicaoConcorrencia.nodes.messages.RecordListMessage;
import projects.p2pDistribuicaoConcorrencia.CustomGlobal;
import projects.p2pDistribuicaoConcorrencia.RecordCollectionManager;
import sinalgo.exception.WrongConfigurationException;
import sinalgo.gui.transformation.PositionTransformation;
import sinalgo.nodes.Node;
import sinalgo.nodes.messages.Inbox;
import sinalgo.nodes.messages.Message;
import sinalgo.runtime.Global;

<<<<<<< HEAD
=======
/**
 * Implementation of a stationary node that handles messages and manages record collections.
 * Extends the Node class from the Sinalgo framework.
 * 
 * @author Meslin
 */
>>>>>>> main
public class MyStationaryNode extends Node {
    /** all node data (this and other nodes) */
    private List<RecordCollectionManager> collectionManager;

<<<<<<< HEAD
=======
	/**
	 * Initializes the stationary node.
	 * Sets up the collection manager for handling records.
	 * Overrides the init method from the Node class.
	 * 
	 * @see sinalgo.nodes.Node#init()
	 */
>>>>>>> main
    @Override
    public void init() {
        CustomGlobal.consoleln(Global.currentTime + " stationary " + this + " em init()");
        collectionManager = new ArrayList<>();
        RecordCollectionManager collection = new RecordCollectionManager(ID);
        collectionManager.add(collection);
    }

<<<<<<< HEAD
=======
	/**
	 * Handles incoming messages for the stationary node.
	 * Processes each message using the MessageProcessor class.
	 * Overrides the handleMessages method from the Node class.
	 * 
	 * @param inbox The inbox containing incoming messages.
	 */
>>>>>>> main
    @Override
    public void handleMessages(Inbox inbox) {
        while(inbox.hasNext()) {
            Message msg = inbox.next();		// precisa obter a próxima mensagem antes de tudo
            Node sender = inbox.getSender();
			MessageProcessor messageProcessor = new MessageProcessor();
			messageProcessor.processMessage(sender, msg);
        }        
    }

<<<<<<< HEAD
=======
	/**
	 * Draws the stationary node on the graphical interface.
	 * Sets the color and appearance of the node based on its state.
	 * Overrides the draw method from the Node class.
	 * 
	 * @param graphics The graphics context for drawing.
	 * @param pt The position transformation for coordinate conversion.
	 * @param highlight Indicates whether the node should be highlighted.
	 */
>>>>>>> main
    @Override
    public void draw(Graphics graphics, PositionTransformation pt, boolean highlight) {
		int nodeSize = 10;
        // set the color of this node
		if (highlight) {
            graphics.setColor(Color.RED);
        } else {
            graphics.setColor(Color.BLUE);
        }	

<<<<<<< HEAD
		this.setColor(Color.GREEN);
=======
		// set the node color to green
		this.setColor(Color.GREEN);
		// prepare the text to be drawn inside the node with its ID
>>>>>>> main
        String text = Integer.toString(this.ID);
        // draw the node as a circle with the text inside
        super.drawNodeAsDiskWithText(graphics, pt, highlight, text, 3, Color.YELLOW);
    }

    // TODO Auto-generated method stub
    @Override
    public void checkRequirements() throws WrongConfigurationException {    }

    // TODO Auto-generated method stub
    @Override
    public void preStep() {    }

    // TODO Auto-generated method stub
    @Override
    public void postStep() {    }

    // TODO Auto-generated method stub
    @Override
    public void neighborhoodChange() {    }

<<<<<<< HEAD
	private class MessageProcessor {
		private Map<Class<? extends Message>, BiConsumer<? extends Node, ? extends Message>> handlers = new HashMap<>();

=======
	/**
	 * Class responsible for processing different types of messages received by the stationary node.
	 * Uses a map to associate message types with their corresponding handler methods.
	 * Implements message handling for ID messages, last saved record messages, next record number messages, and record list messages.
	 * 
	 * @author Meslin
	 */
	private class MessageProcessor {
		private Map<Class<? extends Message>, BiConsumer<? extends Node, ? extends Message>> handlers = new HashMap<>();

		/**
		 * Constructor that initializes the message handlers.
		 * Maps each message type to its corresponding handler method.
		 * 
		 * @see p2pDistribuicaoConcorrencia.nodes.messages.IdMessage
		 * @see p2pDistribuicaoConcorrencia.nodes.messages.LastSavedRecordMessage
		 * @see p2pDistribuicaoConcorrencia.nodes.messages.NextRecordNumberMessage
		 * @see p2pDistribuicaoConcorrencia.nodes.messages.RecordListMessage
		 */
>>>>>>> main
		public MessageProcessor() {
			handlers.put(IdMessage.class,               (sender, msg) -> handleIdMessage              ((Node)sender, (IdMessage)msg));
			handlers.put(LastSavedRecordMessage.class,  (sender, msg) -> handleLastSavedRecordMessage ((Node)sender, (LastSavedRecordMessage) msg));
			handlers.put(NextRecordNumberMessage.class, (sender, msg) -> handleNextRecordNumberMessage((Node)sender, (NextRecordNumberMessage) msg));
			handlers.put(RecordListMessage.class,       (sender, msg) -> handleRecordListMessage      ((Node)sender, (RecordListMessage) msg));
		}

<<<<<<< HEAD
=======
		/**
		 * Processes a received message by invoking the appropriate handler based on the message type.
		 * If no handler is found for the message type, it calls the handleUnknownMessageType method.
		 * 
		 * @param sender who sent the message
		 * @param msg the message
		 */
>>>>>>> main
		public void processMessage(Node sender, Message msg) {
			BiConsumer<Node, Message> handler = (BiConsumer<Node, Message>) handlers.get(msg.getClass());
			if (handler != null) {
				handler.accept(sender, msg);
			} else {
				handleUnknownMessageType(msg);
			}
		}

		/**
		 * Update new neighbor about already saved messages
		 * Send request for next record
<<<<<<< HEAD
=======
		 * 
		 * @param sender who sent the message
>>>>>>> main
		 * @param msg ID message
		 */
		private void handleIdMessage(Node sender, IdMessage msg) {
			// Tratar Message
			// Enviar lastSavedRecordNumber
			sendLastSavedRecordNumber(sender, msg.getNodeId());
			// Enviar nextRecordNumber
			sendNextRecordNumber(sender, msg.getNodeId());
		}

		/**
		 * Delete messages that were reported to be already at the stationary server
<<<<<<< HEAD
=======
		 * 
		 * @param sender who sent the message
>>>>>>> main
		 * @param msg last saved record number message (inclusive)
		 */
		private void handleLastSavedRecordMessage(Node sender, LastSavedRecordMessage msg) {
            // will not receive
		}

		/**
		 * Send all new records to the new neighbor
<<<<<<< HEAD
=======
		 * 
		 * @param sender who sent the message
>>>>>>> main
		 * @param msg next record number message
		 */
		private void handleNextRecordNumberMessage(Node sender, NextRecordNumberMessage msg) {
            // will not receive
		}

		/**
<<<<<<< HEAD
		 * Receive a message record list 
=======
		 * Receive a message record list
		 * 
>>>>>>> main
		 * @param sender who sent the message
		 * @param msg the message
		 */
		private void handleRecordListMessage(Node sender, RecordListMessage msg) {
			boolean found = false;
			RecordCollectionManager collectionManager = null;

			// search the message collection list for a previous message from the same producer (not the same sender)
			for(RecordCollectionManager manager : MyStationaryNode.this.collectionManager) {
				if(manager.getNodeId() == msg.getNodeId()) {
					found = true;
					collectionManager = manager;
					break;
				}
			}

			// if this is the very first message, create a new entry in the record collection for this producer
			if(!found) {
				collectionManager = new RecordCollectionManager(msg.getNodeId());
				MyStationaryNode.this.collectionManager.add(collectionManager);
			}

			// check for a non-empty received message list 
			if(msg.getRecordList().size() > 0) {
                // not save the record list, just the last record number
				collectionManager.setNextRecord(msg.getRecordList().get(msg.getRecordList().size()-1).getRecordNumber());
				// also update last saved record
				collectionManager.setLastSavedRecord(msg.getRecordList().get(msg.getRecordList().size()-1).getRecordNumber());
			}
	    }

<<<<<<< HEAD
=======
		/**
		 * Send last saved record number to the sender node
		 * 
		 * @param sender who sent the message
		 * @param nodeId the node ID
		 */
>>>>>>> main
		protected void sendLastSavedRecordNumber(Node sender, int nodeId) {
			int lastSavedRecord = -1;
			boolean found = false;

<<<<<<< HEAD
=======
			// search for the record collection of the given node ID
>>>>>>> main
			synchronized(MyStationaryNode.this.collectionManager) {
				for(RecordCollectionManager colletion : MyStationaryNode.this.collectionManager) {
					if(colletion.getNodeId() == nodeId) {
						lastSavedRecord = colletion.getLastSavedRecord();
						found = true;
						break;
					}
				}
			}
			LastSavedRecordMessage message = new LastSavedRecordMessage(nodeId, lastSavedRecord);
			MyStationaryNode.this.send(message, sender);
		}

<<<<<<< HEAD
=======
		/**
		 * Send next record number to the sender node
		 * @param sender who sent the message
		 * @param nodeId the node ID
		 */
>>>>>>> main
		protected void sendNextRecordNumber(Node sender, int nodeId) {
			int nextRecordNumber = -1;
			boolean found = false;

<<<<<<< HEAD
=======
			// search for the record collection of the given node ID
			// synchronized to avoid concurrent modification exceptions
			// when another thread is adding new record collections
>>>>>>> main
			synchronized(MyStationaryNode.this.collectionManager) {
				for(RecordCollectionManager colletion : MyStationaryNode.this.collectionManager) {
					if(colletion.getNodeId() == nodeId) {
						nextRecordNumber = colletion.getNextRecord();
						found = true;
						break;
					}
				}
			}
<<<<<<< HEAD
			NextRecordNumberMessage message = new NextRecordNumberMessage(nodeId, nextRecordNumber);
=======
			// send the next record number message back to the requester
			NextRecordNumberMessage message = new NextRecordNumberMessage(nodeId, nextRecordNumber);
			// send the message
>>>>>>> main
			MyStationaryNode.this.send(message, sender);
		}

		/**
		 * Handle (unlikely) unknown message
<<<<<<< HEAD
		 * @param msg
		 */
		private void handleUnknownMessageType(Message msg) {
			// Tratar mensagens desconhecidas
			// TODO
=======
		 * 
		 * @param msg The unknown message
		 */
		private void handleUnknownMessageType(Message msg) {
			// Tratar mensagens desconhecidas
			// TODO: Implementar tratamento para mensagens desconhecidas
>>>>>>> main
		}
	}
}
