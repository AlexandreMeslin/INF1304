package p2pDistribuicaoConcorrencia.nodes.messages;

import sinalgo.nodes.messages.Message;

/**
 * Message to ask for more records based on the last record received by the node
<<<<<<< HEAD
<<<<<<< HEAD
=======
 * 
 * @author meslin
>>>>>>> main
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
<<<<<<< HEAD
=======
     * 
>>>>>>> main
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
<<<<<<< HEAD
=======
=======
>>>>>>> main
    /**
     * Get node ID
     * 
     * @return node ID
     */
<<<<<<< HEAD
>>>>>>> main
=======
>>>>>>> main
    public int getNodeId() {
        return this.nodeId;
    }

<<<<<<< HEAD
<<<<<<< HEAD
=======
=======
>>>>>>> main
    /**
     * Get next record number
     * 
     * @return next record number
     */
<<<<<<< HEAD
>>>>>>> main
=======
>>>>>>> main
    public int getNextRecordNumber() {
        return this.nextRecordNumber;
    }

<<<<<<< HEAD
<<<<<<< HEAD
=======
=======
>>>>>>> main
    /**
     * Clone the message
     * 
     * @return cloned message
     */
<<<<<<< HEAD
>>>>>>> main
=======
>>>>>>> main
    @Override
    public Message clone() {
        return new NextRecordNumberMessage(this.nodeId, this.nextRecordNumber);
    }
}
