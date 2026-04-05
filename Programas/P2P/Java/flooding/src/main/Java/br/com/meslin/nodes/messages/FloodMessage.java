package br.com.meslin.nodes.messages;

import sinalgo.nodes.messages.Message;

public class FloodMessage extends Message {
    public int messageId;

    public FloodMessage(int id) {
        this.messageId = id;
    }

    @Override
    public Message clone() {
        return new FloodMessage(this.messageId);
    }
}
