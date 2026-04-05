// From https://pamodaaw.medium.com/handson-introduction-to-grpc-with-java-1195870027fb

package org.sample.park.server;

import io.grpc.stub.StreamObserver;
import org.sample.park.CarParkServiceGrpc;
import org.sample.park.ParkRequest;
import org.sample.park.ParkResponse;
import org.sample.park.ParkResponseManyTimes;

public class CarParkServiceImpl extends CarParkServiceGrpc.CarParkServiceImplBase {

    @Override
    public void parkVehicle(ParkRequest request, StreamObserver<ParkResponse> responseObserver) {

        String vehicleNo = request.getVehicle().getVehicleNumber();
        String vehicleType = request.getVehicle().getVehicleType();

        System.out.println("The vehicle of number " + vehicleNo + " and type " + vehicleType + " entered the park.");
        String resultMsg = "The vehicle of number " + vehicleNo + " and type " + vehicleType + " is parked.";

        ParkResponse parkResponse = ParkResponse
                .newBuilder()
                .setResult(resultMsg)   // The response is sent when we call responseObserver’s onNext() method.
                .build();               // gRPC uses builders for creating objects.

        // Send the response to the client.
        responseObserver.onNext(parkResponse);  // The response is sent when we call responseObserver’s onNext() method.

        // Notifies the customer that the call is completed.
        responseObserver.onCompleted();     // onCompleted() specifies that RPC is finished, else the connection will be hung, and the client will just wait for more information to come in.
        System.out.println(resultMsg);

    }

    @Override
    public void parkVehicleManyTimes(ParkRequest request, StreamObserver<ParkResponseManyTimes> responseObserver) {

        String vehicleNo = request.getVehicle().getVehicleNumber();
        String vehicleType = request.getVehicle().getVehicleType();

        // Build and send the first response.
        String response1 = "The vehicle of number " + vehicleNo + " and type " + vehicleType + " entered the park.";
        System.out.println(response1);
        ParkResponseManyTimes parkResponse1 = ParkResponseManyTimes
                .newBuilder()           
                .setResult(response1)   
                .build();               
        responseObserver.onNext(parkResponse1);

        // Hold the thread for 5s before sending the second response.
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Build and send the second response.
        String response2 = "The ground floor is full.";
        ParkResponseManyTimes parkResponse2 = ParkResponseManyTimes
                .newBuilder()
                .setResult(response2)
                .build();
        responseObserver.onNext(parkResponse2);

        // Hold the thread for 5s before sending the third response.
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Build and send the third response.
        String response3 = "The vehicle of number " + vehicleNo + " and type " + vehicleType + " is parked in the " +
                "first floor";
        ParkResponseManyTimes parkResponse3 = ParkResponseManyTimes
                .newBuilder()
                .setResult(response3)
                .build();
        responseObserver.onNext(parkResponse3);

        // Complete the communication.
        responseObserver.onCompleted();

        System.out.println(response3);
    }
}