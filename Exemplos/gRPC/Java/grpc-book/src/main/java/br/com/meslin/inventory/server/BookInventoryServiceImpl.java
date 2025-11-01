package br.com.meslin.inventory.server;

import br.com.meslin.inventory.*;
import br.com.meslin.inventory.BookInventoryServiceGrpc;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


class BookInventoryServiceImpl extends BookInventoryServiceGrpc.BookInventoryServiceImplBase {
    private final Map<String, Book> books = new ConcurrentHashMap<>();  // Thread-safe storage for books by ISBN

    /**
     * Handle the GetBook RPC call.
     * 
     * @param request The request containing the ISBN of the book to retrieve.
     * @param responseObserver The observer to send the response to.
     */
    @Override
    public void getBook(BookRequest request, StreamObserver<Book> responseObserver) {
        // Retrieve the book by ISBN
        Book book = books.get(request.getIsbn());
        if (book == null) {
            // Book not found, return an error
            responseObserver.onError(new RuntimeException("Book not found: " + request.getIsbn()));
        } else {
            // Book found, return it
            responseObserver.onNext(book);  // Send the book back to the client
            responseObserver.onCompleted(); // Complete the RPC call
        }
    }

    /**
     * Handle the AddBook RPC call.
     * 
     * @param request The book to add.
     * @param responseObserver The observer to send the response to.
     */
    @Override
    public void addBook(Book request, StreamObserver<Status> responseObserver) {
        // Add the book to the inventory if the ISBN does not already exist
        if (books.containsKey(request.getIsbn())) {
            // ISBN already exists, return failure status
            responseObserver.onNext(Status.newBuilder().setSuccess(false).setMessage("ISBN already exists").build());
        } else {
            // Add the new book
            books.put(request.getIsbn(), request);  // Store the book by its ISBN
            responseObserver.onNext(Status.newBuilder().setSuccess(true).setMessage("Book added").build()); // Return success status
        }
        responseObserver.onCompleted(); // Complete the RPC call
    }

    /**
     * Handle the SearchBooks RPC call.
     * Streams back all books by the specified author.
     * 
     * @param request The search request containing the author to search for.
     * @param responseObserver The observer to send the response to.
     */
    @Override
    public void searchBooks(SearchRequest request, StreamObserver<Book> responseObserver) {
        // Stream back all books by the specified author
        books.values().stream()
                .filter(b -> b.getAuthor().equals(request.getAuthor()))
                .forEach(responseObserver::onNext);
        responseObserver.onCompleted(); // Complete the RPC call
    }
}
