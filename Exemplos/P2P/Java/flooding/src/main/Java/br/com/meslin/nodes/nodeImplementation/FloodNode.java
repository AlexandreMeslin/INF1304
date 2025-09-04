package br.com.meslin.nodes.nodeImplementations;

import sinalgo.nodes.Node;
import sinalgo.nodes.messages.Inbox;
import sinalgo.nodes.messages.Message;
import sinalgo.nodes.messages.NackBox;
import sinalgo.nodes.timers.Timer;
import nodes.messages.FloodMessage;
import sinalgo.tools.Tools;

public class FloodNode extends Node {
    private boolean received = false;
    private static int messageCounter = 0;

    @Override
    public void handleMessages(Inbox inbox) {
        while (inbox.hasNext()) {
            Message msg = inbox.next();

            if (msg instanceof FloodMessage) {
                FloodMessage floodMsg = (FloodMessage) msg;

                if (!received) {
                    received = true;
                    Tools.appendToOutput("Node " + this.ID + " received message " + floodMsg.messageId + "\n");

                    // Forward the message to all neighbors
                    for (Node neighbor : this.outgoingConnections) {
                        send(new FloodMessage(floodMsg.messageId), neighbor);
                    }
                }
            }
        }
    }

    @Override
    public void preStep() {}

    @Override
    public void init() {}

    @Override
    public void neighborhoodChange() {}

    @Override
    public void postStep() {}

    @Override
    public void checkRequirements() {}

    // Timer to start flooding
    private static class StartFloodingTimer extends Timer {
        @Override
        public void fire() {
            Node sender = this.node;
            sender.broadcast(new FloodMessage(messageCounter++));
            Tools.appendToOutput("Node " + sender.ID + " started flooding with message " + messageCounter + "\n");
        }
    }

    public void startFlooding() {
        StartFloodingTimer timer = new StartFloodingTimer();
        timer.startRelative(1, this);
    }
}
