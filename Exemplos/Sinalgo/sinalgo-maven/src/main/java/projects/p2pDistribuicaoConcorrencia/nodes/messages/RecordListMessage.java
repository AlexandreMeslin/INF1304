package p2pDistribuicaoConcorrencia.nodes.messages;

import java.util.List;

import projects.p2pDistribuicaoConcorrencia.RecordEntry;
import sinalgo.nodes.messages.Message;

<<<<<<< HEAD
public class RecordListMessage extends Message {
    private final int nodeId;
    private final List<RecordEntry> recordList;

=======
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
>>>>>>> main
    public RecordListMessage(int nodeId, List<RecordEntry> recordList) {
        this.nodeId = nodeId;
        this.recordList = recordList;
    }

<<<<<<< HEAD
=======
    /**
     * Get record list
     * 
     * @return list of records
     */
>>>>>>> main
    public List<RecordEntry> getRecordList() {
        return this.recordList;
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
     * Clone the message
     * 
     * @return cloned message
     */
>>>>>>> main
    @Override
    public Message clone() {
        return new RecordListMessage(this.nodeId, this.recordList);
    }
}
