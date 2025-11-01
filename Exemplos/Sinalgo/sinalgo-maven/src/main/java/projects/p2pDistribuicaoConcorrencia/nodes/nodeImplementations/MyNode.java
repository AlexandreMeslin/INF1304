/**
 * @author Meslin
 */
package projects.p2pDistribuicaoConcorrencia.nodes.nodeImplementations;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;

import p2pDistribuicaoConcorrencia.nodes.messages.IdMessage;
import p2pDistribuicaoConcorrencia.nodes.messages.LastSavedRecordMessage;
import p2pDistribuicaoConcorrencia.nodes.messages.NextRecordNumberMessage;
import p2pDistribuicaoConcorrencia.nodes.messages.RecordListMessage;
import projects.p2pDistribuicaoConcorrencia.CustomGlobal;
import projects.p2pDistribuicaoConcorrencia.RecordCollectionManager;
import projects.p2pDistribuicaoConcorrencia.RecordEntry;

import sinalgo.configuration.Configuration;
import sinalgo.exception.WrongConfigurationException;
import sinalgo.gui.transformation.PositionTransformation;
import sinalgo.nodes.Node;
import sinalgo.nodes.edges.Edge;
import sinalgo.nodes.messages.Inbox;
import sinalgo.nodes.messages.Message;
import sinalgo.runtime.Global;
import sinalgo.tools.Tools;

public class MyNode extends Node {
	private char[] animacao = {'|', '/', '-', '\\'};
	private int posAnimacao = 0;
	/** this node previous neighbors */
	private Set<Node> previousNeighbors;
	/** all node data (this and other nodes) */
	private List<RecordCollectionManager> collectionManager;

	/* (non-Javadoc)
	 * @see sinalgo.nodes.Node#init()
	 */
	@Override
	public void init() {
		CustomGlobal.consoleln(Global.currentTime + " nó " + this + " em init()");
		this.previousNeighbors = new HashSet<>();
		collectionManager = new ArrayList<>();
        RecordCollectionManager collection = new RecordCollectionManager(ID);
        collectionManager.add(collection);

		RecordCreator recordCreatorTask = new RecordCreator();
		Thread recordCreatorThread = new Thread(recordCreatorTask);
		recordCreatorThread.start();
	}

	/* (non-Javadoc)
	 * @see sinalgo.nodes.Node#preStep()
	 */
	@Override
	public void preStep() {
	}

	
	/* (non-Javadoc)
	 * @see sinalgo.nodes.Node#neighborhoodChange()
	 */
	@Override
	public void neighborhoodChange() {
		List<RecordCollectionManager> collectionManagerCloned;
		Set<Node> currentNeighbors = new HashSet<>();

		// Itera sobre as arestas de saída para encontrar os vizinhos atuais
		for (Edge edge : this.outgoingConnections) {
			currentNeighbors.add(edge.endNode);
		}

		// Detecta novos vizinhos
		for (Node neighbor : currentNeighbors) {
			if (!previousNeighbors.contains(neighbor)) {
				// Envia mensagem se apresentado para o novo vizinho
				synchronized(collectionManager) {
					collectionManagerCloned = new ArrayList<>(collectionManager);
				}
				for(RecordCollectionManager collection : collectionManagerCloned) {
					IdMessage idMessage = new IdMessage(collection.getNodeId());
					this.send(idMessage, neighbor);
				}
				collectionManagerCloned = null;
			}
		}

		// Atualiza o conjunto de vizinhos anteriores
		previousNeighbors = currentNeighbors;
	}

	
	// Timers são executados nesse ponto
	// entre o preStep() e o handlerMessages()
	
	
	/* (non-Javadoc)
	 * @see sinalgo.nodes.Node#handleMessages(sinalgo.nodes.messages.Inbox)
	 */
	@Override
	public void handleMessages(Inbox inbox) {
		while(inbox.hasNext()) {
			Message msg = inbox.next();
			Node sender = inbox.getSender();
			MessageProcessor messageProcessor = new MessageProcessor();
			messageProcessor.processMessage(sender, msg);
		}
	}

	/* (non-Javadoc)
	 * @see sinalgo.nodes.Node#postStep()
	 */					

	@Override
	public void postStep() {
	}

	/* (non-Javadoc)
	 * @see sinalgo.nodes.Node#checkRequirements()
	 */
	@Override
	public void checkRequirements() throws WrongConfigurationException {
	}
	
	/* (non-Javadoc)
	 * @see sinalgo.nodes.Node#draw(java.awt.Graphics, sinalgo.gui.transformation.PositionTransformation, boolean)
	 */
	public void draw(Graphics graphics, PositionTransformation pt, boolean highlight) {
		int nodeSize = 10;
        // set the color of this node
		if (highlight) {
            this.setColor(Color.RED);
        } else {
            this.setColor(Color.BLUE);
        }	

		//this.setColor(new Color((float) 0.5, (float) 0.5, (float) 1.0));
        String text = this.ID + Character.toString(this.animacao[this.posAnimacao]) + collectionManager.get(0).getLastSavedRecord();
		this.posAnimacao = (this.posAnimacao+1) % this.animacao.length;
        // draw the node as a circle with the text inside
        super.drawNodeAsDiskWithText(graphics, pt, highlight, text, 3, Color.YELLOW);
	}

	private class MessageProcessor {
		private Map<Class<? extends Message>, BiConsumer<? extends Node, ? extends Message>> handlers = new HashMap<>();

		public MessageProcessor() {
			handlers.put(IdMessage.class,               (sender, msg) -> handleIdMessage              ((Node)sender, (IdMessage)msg));
			handlers.put(LastSavedRecordMessage.class,  (sender, msg) -> handleLastSavedRecordMessage ((Node)sender, (LastSavedRecordMessage) msg));
			handlers.put(NextRecordNumberMessage.class, (sender, msg) -> handleNextRecordNumberMessage((Node)sender, (NextRecordNumberMessage) msg));
			handlers.put(RecordListMessage.class,       (sender, msg) -> handleRecordListMessage      ((Node)sender, (RecordListMessage) msg));
		}

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
		 * @param msg last saved record number message (inclusive)
		 */
		private void handleLastSavedRecordMessage(Node sender, LastSavedRecordMessage msg) {
			int nodeId = msg.getNodeId();
			int lastSavedRecord = msg.getLastSavedRecordNumber();
			RecordCollectionManager collectionManager =null;

			synchronized(MyNode.this.collectionManager) {
				for(RecordCollectionManager collection : MyNode.this.collectionManager) {
					if(collection.getNodeId() == nodeId) {
						collectionManager = collection;
						break;
					}					
				}
			}

			if(collectionManager != null) {
				List<RecordEntry> recordEntries = collectionManager.getRecordList();
				synchronized(recordEntries) {
					recordEntries.removeIf(entry -> entry.getRecordNumber() <= msg.getLastSavedRecordNumber());
				}
				if(lastSavedRecord > collectionManager.getLastSavedRecord()) collectionManager.setLastSavedRecord(lastSavedRecord);
				//CustomGlobal.consoleln("Nó " + MyNode.this.ID + " atualizando last saved record de " + msg.getNodeId() + " com " + lastSavedRecord);
			}
		}

		/**
		 * Send all new records to the new neighbor
		 * @param msg next record number message
		 */
		private void handleNextRecordNumberMessage(Node sender, NextRecordNumberMessage msg) {
			int nodeId = msg.getNodeId();
			int nextMessage = msg.getNextRecordNumber();
			RecordCollectionManager manager =null; 
			List<RecordEntry> outgoingRecords = new ArrayList<>();

			synchronized(MyNode.this.collectionManager) {
				for(RecordCollectionManager collection : MyNode.this.collectionManager) {
					if(collection.getNodeId() == nodeId) {
						manager = collection;
						break;
					}
				}
			}

			// antes, verifica se encontrou algum registro 
			if(manager != null) {
				synchronized(collectionManager) {
					for(RecordEntry entry : manager.getRecordList()) {
						if(entry.getRecordNumber() >= nextMessage) {
							outgoingRecords.add(entry);
						}
					}
				}

				if(outgoingRecords.size() > 0) {
					RecordListMessage recordList = new RecordListMessage(ID, outgoingRecords);
					send(recordList, sender);
				} 
			}
		}

		private void handleRecordListMessage(Node sender, RecordListMessage msg) {
			boolean found = false;
			RecordCollectionManager collectionManager = null;

			for(RecordCollectionManager manager : MyNode.this.collectionManager) {
				if(manager.getNodeId() == msg.getNodeId()) {
					found = true;
					collectionManager = manager;
					break;
				}
			}
			if(!found) {
				collectionManager = new RecordCollectionManager(msg.getNodeId());
				MyNode.this.collectionManager.add(collectionManager);
			}
			if(msg.getRecordList().size() > 0) {
				collectionManager.getRecordList().addAll(msg.getRecordList());
				collectionManager.setNextRecord(msg.getRecordList().get(msg.getRecordList().size()-1).getRecordNumber());
			}
	}

		protected void sendLastSavedRecordNumber(Node sender, int nodeId) {
			int lastSavedRecord = -1;
			boolean found = false;

			synchronized(MyNode.this.collectionManager) {
				for(RecordCollectionManager colletion : MyNode.this.collectionManager) {
					if(colletion.getNodeId() == nodeId) {
						lastSavedRecord = colletion.getLastSavedRecord();
						found = true;
						break;
					}
				}
			}
			LastSavedRecordMessage message = new LastSavedRecordMessage(nodeId, lastSavedRecord);
			MyNode.this.send(message, sender);
		}

		protected void sendNextRecordNumber(Node sender, int nodeId) {
			int nextRecordNumber = -1;
			boolean found = false;

			synchronized(MyNode.this.collectionManager) {
				for(RecordCollectionManager colletion : MyNode.this.collectionManager) {
					if(colletion.getNodeId() == nodeId) {
						nextRecordNumber = colletion.getNextRecord();
						found = true;
						break;
					}
				}
			}
			NextRecordNumberMessage message = new NextRecordNumberMessage(nodeId, nextRecordNumber);
			MyNode.this.send(message, sender);
		}

		/**
		 * Handle (unlikely) unknown message
		 * @param msg
		 */
		private void handleUnknownMessageType(Message msg) {
			// Tratar mensagens desconhecidas
			// TODO
		}
	}

	private class RecordCreator implements Runnable {
		private int counter;
	
		public RecordCreator() {
			this.counter = 0;
		}

		@Override
		public void run() {
			try {
				while(true) {
					RecordEntry record = new RecordEntry(this.counter, "Data: " + this.counter++);
					collectionManager.get(0).setNextRecord(this.counter);
					synchronized(collectionManager.get(0)) {
						collectionManager.get(0).getRecordList().add(record);
					}
					Thread.sleep(10*1000 + (int)(Math.random()*10*1000));
				}
			} catch (Exception e) {
				CustomGlobal.consoleln("***** ERROR ***** " + e.getStackTrace());
			}
		}
	}
}
