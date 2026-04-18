package p2pDistribuicaoConcorrencia.nodes.messages;

import java.util.List;

import projects.p2pDistribuicaoConcorrencia.RecordEntry;
import sinalgo.nodes.messages.Message;

public class RecordListMessage extends Message {
    private final int nodeId;
    private final List<RecordEntry> recordList;

    public RecordListMessage(int nodeId, List<RecordEntry> recordList) {
        this.nodeId = nodeId;
        this.recordList = recordList;
    }

    public List<RecordEntry> getRecordList() {
        return this.recordList;
    }

    public int getNodeId() {
        return this.nodeId;
    }

    @Override
    public Message clone() {
        return new RecordListMessage(this.nodeId, this.recordList);
    }
}
