package br.com.meslin.nodes.nodeImplementations;

import sinalgo.nodes.Node;
import sinalgo.nodes.messages.Inbox;
import sinalgo.nodes.messages.Message;
import sinalgo.nodes.messages.NackBox;
import sinalgo.nodes.timers.Timer;
import sinalgo.tools.Tools;
import sinalgo.nodes.edges.Edge;
import br.com.meslin.nodes.messages.FloodMessage;

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
                    Tools.appendToOutput("Node " + this.getID() + " received message " + floodMsg.messageId + "\n");

                    // Forward the message to all neighbors
                    for (Edge edge : this.getOutgoingConnections()) {
                        Node neighbor = edge.getEndNode();
                        send(new FloodMessage(floodMsg.messageId), neighbor);
                    }
                }
            }
        }
    }

    @Override
    public void preStep() {}

    @Override
    public void init() {
        // O nó 1 inicia o flooding
        if (this.getID() == 1) {
            startFlooding();
        }
    }

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
            Node sender = getTargetNode();
            sender.broadcast(new FloodMessage(messageCounter++));
            Tools.appendToOutput("Node " + sender.getID() + " started flooding with message " + messageCounter + "\n");
        }
    }

    public void startFlooding() {
        StartFloodingTimer timer = new StartFloodingTimer();
        timer.startRelative(1, this);
    }
}
