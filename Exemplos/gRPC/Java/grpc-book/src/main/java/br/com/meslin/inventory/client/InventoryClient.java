package br.com.meslin.inventory.client;

//import br.com.meslin.inventory.InventoryProto.*;
import br.com.meslin.inventory.*;
import br.com.meslin.inventory.BookInventoryServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class InventoryClient {
    final static int INVENTORY_PORT = 50051;    // Match server port
    final static String INVENTORY_HOST = "inventory-server";    // Docker service name
    final static int TERMINATION_TIMEOUT = 5; // seconds

    private final ManagedChannel channel;   // Communication channel
    private final BookInventoryServiceGrpc.BookInventoryServiceBlockingStub blockingStub;   // Synchronous stub

    /**
     * Constructor to create a client connecting to the gRPC server at specified host and port.
     * 
     * @param host The hostname of the gRPC server.
     * @param port The port number of the gRPC server.
     */
    public InventoryClient(String host, int port) {
        System.err.println("Connecting to " + host + ":" + port);
        this.channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .build();
        this.blockingStub = BookInventoryServiceGrpc.newBlockingStub(this.channel);
    }

    /**
     * Main method to run the inventory client.
     * 
     * @param args Command line arguments. Not used.
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        InventoryClient client = new InventoryClient(INVENTORY_HOST, INVENTORY_PORT);
        try {
            client.run();
        } finally {
            client.shutdown();
        }
    }

    /**
     * Run client operations: add books, get a book, and search books by author.
     * 
     * @throws InterruptedException
     */
    public void run() {
        // Add some books
        Book[] books = {
                Book.newBuilder().setIsbn("978-0001").setTitle("Distributed Systems").setAuthor("Author A").setYear(2010).setStockCount(3).build(),
                Book.newBuilder().setIsbn("978-0002").setTitle("Intro to gRPC").setAuthor("Author B").setYear(2018).setStockCount(5).build(),
                Book.newBuilder().setIsbn("978-0003").setTitle("Docker in Action").setAuthor("Author A").setYear(2016).setStockCount(2).build(),
                Book.newBuilder().setIsbn("978-0004").setTitle("Cloud Patterns").setAuthor("Author C").setYear(2020).setStockCount(4).build(),
                Book.newBuilder().setIsbn("978-0005").setTitle("Advanced RPC").setAuthor("Author D").setYear(2021).setStockCount(1).build()
        };

        // AddBook
        // Send each book to the server
        // Print the status message for each book
        for (Book b : books) {
            Status status = this.blockingStub.addBook(b);
            System.out.println("AddBook: " + status.getMessage());
        }

        // GetBook
        // Get the book with ISBN "978-0001"
        // Create a request
        // Call the getBook method and print the result
        BookRequest req = BookRequest.newBuilder().setIsbn("978-0001").build();
        Book book = this.blockingStub.getBook(req);
        System.out.println("GetBook: " + book);

        // SearchBooks
        // Search for books by "Author A"
        // Print each book found
        System.out.println("SearchBooks for Author A:");
        this.blockingStub.searchBooks(SearchRequest.newBuilder().setAuthor("Author A").build()).forEachRemaining(System.out::println);
    }

    /**
     * Shut down the client channel.
     * 
     * @throws InterruptedException
     */
    public void shutdown() throws InterruptedException {
        this.channel.shutdown().awaitTermination(TERMINATION_TIMEOUT, TimeUnit.SECONDS);
    }
}
