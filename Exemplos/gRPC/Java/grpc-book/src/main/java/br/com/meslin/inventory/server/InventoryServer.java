package br.com.meslin.inventory.server;

//import br.com.meslin.inventory.InventoryProto.*;
import br.com.meslin.inventory.*;
import br.com.meslin.inventory.BookInventoryServiceGrpc;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InventoryServer {
    private final int port = 50051; // Match client port
    private final Server server;    // gRPC server instance

    /**
     * Constructor to create and configure the gRPC server.
     * The server listens on the specified port and adds the BookInventoryService.
     */
    public InventoryServer() {
        this.server = ServerBuilder.forPort(port)
                .addService(new BookInventoryServiceImpl())
                .build();
    }

    /**
     * Main method to start the server.
     * 
     * @param args  // Command line arguments. Not used.
     * @throws IOException
     * @throws InterruptedException
     */
    public static void main(String[] args) throws IOException, InterruptedException {
        InventoryServer server = new InventoryServer();
        server.start();
        server.blockUntilShutdown();
    }

    /**
     * Start the gRPC server and add a shutdown hook.
     * 
     * @throws IOException
     */
    public void start() throws IOException {
        this.server.start();
        System.out.println("Server started on port " + port);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.err.println("Shutting down gRPC server");
            InventoryServer.this.stop();
        }));
    }

    /**
     * Stop the gRPC server.
     */
    public void stop() {
        if (this.server != null) {
            this.server.shutdown();
        }
    }

    /**
     * Block the main thread until the server is terminated.
     * 
     * @throws InterruptedException
     */
    public void blockUntilShutdown() throws InterruptedException {
        if (this.server != null) {
            this.server.awaitTermination();
        }
    }
}
