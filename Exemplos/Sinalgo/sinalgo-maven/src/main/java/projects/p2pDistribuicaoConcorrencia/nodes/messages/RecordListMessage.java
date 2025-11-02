package p2pDistribuicaoConcorrencia.nodes.messages;

import java.util.List;

import projects.p2pDistribuicaoConcorrencia.RecordEntry;
import sinalgo.nodes.messages.Message;

<<<<<<< HEAD
<<<<<<< HEAD
public class RecordListMessage extends Message {
    private final int nodeId;
    private final List<RecordEntry> recordList;

=======
=======
>>>>>>> main
/**
 * Message to send a list of records
 * 
 * @author meslin
 */
public class RecordListMessage extends Message {
    /** Node ID */
    private final int nodeId;
    /** List of records */
    private final List<RecordEntry> recordList;

    /**
     * Constructor
     * 
     * @param nodeId node ID
     * @param recordList list of records
     */
<<<<<<< HEAD
>>>>>>> main
=======
>>>>>>> main
    public RecordListMessage(int nodeId, List<RecordEntry> recordList) {
        this.nodeId = nodeId;
        this.recordList = recordList;
    }

<<<<<<< HEAD
<<<<<<< HEAD
=======
=======
>>>>>>> main
    /**
     * Get record list
     * 
     * @return list of records
     */
<<<<<<< HEAD
>>>>>>> main
=======
>>>>>>> main
    public List<RecordEntry> getRecordList() {
        return this.recordList;
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
        return new RecordListMessage(this.nodeId, this.recordList);
    }
}
