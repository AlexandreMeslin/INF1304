package p2pDistribuicaoConcorrencia.nodes.messages;

import java.util.List;

import projects.p2pDistribuicaoConcorrencia.RecordEntry;
import sinalgo.nodes.messages.Message;

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
    public RecordListMessage(int nodeId, List<RecordEntry> recordList) {
        this.nodeId = nodeId;
        this.recordList = recordList;
    }

    /**
     * Get record list
     * 
     * @return list of records
     */
    public List<RecordEntry> getRecordList() {
        return this.recordList;
    }

    /**
     * Get node ID
     * 
     * @return node ID
     */
    public int getNodeId() {
        return this.nodeId;
    }

    /**
     * Clone the message
     * 
     * @return cloned message
     */
    @Override
    public Message clone() {
        return new RecordListMessage(this.nodeId, this.recordList);
    }
}
