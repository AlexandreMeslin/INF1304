// From https://pamodaaw.medium.com/handson-introduction-to-grpc-with-java-1195870027fb

package org.sample.park.client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.sample.park.CarParkServiceGrpc;
import org.sample.park.ParkRequest;
import org.sample.park.ParkResponse;
import org.sample.park.Vehicle;

public class CarParkClient {

    public static void main(String[] args) {
        System.out.println("Starting client...");

        // Channel is used by the client to communicate with the server using the domain localhost and port 5003.
        // This is the port where our server is starting.
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 5003)
                .usePlaintext()
                .build();

        // Auto generated stub class with the constructor wrapping the channel.
        CarParkServiceGrpc.CarParkServiceBlockingStub stub = CarParkServiceGrpc.newBlockingStub(channel);

        // Start calling the `parkVehicle` method
        ParkRequest parkRequest = ParkRequest.newBuilder()
                .setVehicle(Vehicle.newBuilder()
                        .setVehicleNumber("NA-1324")
                        .setVehicleType("BUS")
                        .build())
                .build();

        ParkResponse parkResponse = stub.parkVehicle(parkRequest);
        System.out.println("Response for the first call: " + parkResponse.getResult());

        // Call to the `parkVehicle` method is successfully completed. Now calling the `parkVehicleManyTimes` method.
        // Hold the thread for 10s before calling the other method.
        System.out.println("### Initiating the second request ###");
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ParkRequest parkRequest2 = ParkRequest.newBuilder()
                .setVehicle(Vehicle.newBuilder()
                        .setVehicleNumber("PE-6723")
                        .setVehicleType("VAN")
                        .build())
                .build();

        System.out.println("Response for the second call: ");
        stub.parkVehicleManyTimes(parkRequest2).forEachRemaining(parkResponseManyTimes -> {
            System.out.println(parkResponseManyTimes.getResult());
        });
        // Call to the `parkVehicleManyTimes` method is successfully completed.
    }
}