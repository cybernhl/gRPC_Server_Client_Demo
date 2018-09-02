package io.grpc.examples.helloworld;


import java.io.IOException;
import java.util.logging.Logger;

import demo.grpc.proto.GreeterGrpc;
import demo.grpc.proto.HelloRequest;
import demo.grpc.proto.HelloResponse;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;


/**
 * A simple GRPC server implementation which understands the protocol defined in hello.proto.
 *
 * Inspired by
 * <a href="https://github.com/grpc/grpc-java/blob/master/interop-testing/src/main/java/io/grpc/testing/integration/TestServiceServer.java">grpc-java's TestServiceServer</a>,
 * <a href="https://github.com/grpc/grpc-java/blob/master/examples/src/main/java/io/grpc/examples/helloworld/HelloWorldServer.java">grpc-java's TestServiceServer</a>,
 * but radically simplified.
 *
 * When changing the proto file you also have to adapt the {@link #start()} method. For more complex
 * implementations extracting the service implementation in an own class is advised.
 */
public class HelloWorldBasicServer {
    private static final Logger logger = Logger.getLogger(HelloWorldBasicServer.class.getName());

    private Server server;
    /**
     * Main launches the server from the command line.
     */
    public static void main(String[] args) throws IOException, InterruptedException {
        final HelloWorldBasicServer server = new HelloWorldBasicServer();
        server.start();
        server.blockUntilShutdown();
    }

    private void start() throws IOException {
        /* The port on which the server should run */
        int port = 8080;
        server = ServerBuilder.forPort(port)
                .addService(new GreeterImpl())
                .build()
                .start();
        logger.info("Server started, listening on " + port);
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                // Use stderr here since the logger may have been reset by its JVM shutdown hook.
                System.err.println("*** shutting down gRPC server since JVM is shutting down");
                HelloWorldBasicServer.this.stop();
                System.err.println("*** server shut down");
            }
        });
    }

    private void stop() {
        if (server != null) {
            server.shutdown();
        }
    }

    /**
     * Await termination on the main thread since the grpc library uses daemon threads.
     */
    private void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }

    static class GreeterImpl extends GreeterGrpc.GreeterImplBase {

        @Override
        public void sayHello(HelloRequest req, StreamObserver<HelloResponse> responseObserver) {
            HelloResponse response = HelloResponse.newBuilder().setMessage("Say : Hello to " + req.getName()).build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }
    }
}
