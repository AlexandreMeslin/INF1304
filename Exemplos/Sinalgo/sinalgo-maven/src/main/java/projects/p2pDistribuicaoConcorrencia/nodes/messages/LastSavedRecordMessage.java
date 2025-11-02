package p2pDistribuicaoConcorrencia.nodes.messages;

import sinalgo.nodes.messages.Message;

/**
 * Message to inform the last saved record number known by the other node
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
public class LastSavedRecordMessage extends Message {
    private final int nodeId;
    private final int lastSavedRecordNumber;

<<<<<<< HEAD
<<<<<<< HEAD
=======
=======
>>>>>>> main
    /**
     * Constructor
     * 
     * @param nodeId node ID
     * @param lastSavedRecordNumber last saved record number
     */
<<<<<<< HEAD
>>>>>>> main
=======
>>>>>>> main
    public LastSavedRecordMessage(int nodeId, int lastSavedRecordNumber) {
        this.nodeId = nodeId;
        this.lastSavedRecordNumber = lastSavedRecordNumber;
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
     * Get last saved record number
     * 
     * @return last saved record number
     */
<<<<<<< HEAD
>>>>>>> main
=======
>>>>>>> main
    public int getLastSavedRecordNumber() {
        return this.lastSavedRecordNumber;
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
        return new LastSavedRecordMessage(this.nodeId, this.lastSavedRecordNumber);
    }
}
