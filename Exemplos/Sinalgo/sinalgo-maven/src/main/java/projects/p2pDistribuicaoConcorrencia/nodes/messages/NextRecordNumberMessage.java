package p2pDistribuicaoConcorrencia.nodes.messages;

import sinalgo.nodes.messages.Message;

/**
 * Message to ask for more records based on the last record received by the node
<<<<<<< HEAD
=======
 * 
 * @author meslin
>>>>>>> main
 */
public class NextRecordNumberMessage extends Message {
    private int nodeId;
    private int nextRecordNumber;

    /**
     * Constructor
<<<<<<< HEAD
=======
     * 
>>>>>>> main
     * @param nodeId node id
     * @param nextRecordNumber next record number
     */
    public NextRecordNumberMessage(int nodeId, int nextRecordNumber) {
        this.nodeId = nodeId;
        this.nextRecordNumber = nextRecordNumber;
    }

<<<<<<< HEAD
=======
    /**
     * Get node ID
     * 
     * @return node ID
     */
>>>>>>> main
    public int getNodeId() {
        return this.nodeId;
    }

<<<<<<< HEAD
=======
    /**
     * Get next record number
     * 
     * @return next record number
     */
>>>>>>> main
    public int getNextRecordNumber() {
        return this.nextRecordNumber;
    }

<<<<<<< HEAD
=======
    /**
     * Clone the message
     * 
     * @return cloned message
     */
>>>>>>> main
    @Override
    public Message clone() {
        return new NextRecordNumberMessage(this.nodeId, this.nextRecordNumber);
    }
}
