package io.grpc.helloworldexample;

import android.app.Fragment;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;

import demo.grpc.proto.GreeterGrpc;
import demo.grpc.transmission.composition.CompositionRequest;
import demo.grpc.transmission.composition.CompositionResponse;
import io.grpc.Server;
import io.grpc.netty.NettyServerBuilder;
import io.grpc.stub.StreamObserver;

public class MainActivityFragment extends Fragment {
    //Ref : https://hk.saowen.com/a/9d55edb280c562eff884c815b60b6e70cf60fe0eec96ec8389802cd5d3854baa
    private final static String TAG = "MainActivityFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        TextView mServerHostEditText = view.findViewById(R.id.main_edit_server_host);
        final EditText mServerPortEditText = view.findViewById(R.id.main_edit_server_port);
        final TextView logtext = view.findViewById(R.id.main_text_log);
        /*
        makes log text view scroll automatically as new lines are added, just works in combination
        with gravity:bottom
        */
        logtext.setMovementMethod(new ScrollingMovementMethod());
        view.findViewById(R.id.main_button_send_request).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String portstring = mServerPortEditText.getText().toString();
                if (!TextUtils.isEmpty(portstring) && TextUtils.isDigitsOnly(portstring)) {
                    startServer(Integer.valueOf(portstring));
                }
            }
        });


        return view;
    }

    private void startServer(int port) {
        try {
            Server server = NettyServerBuilder.forPort(port)
                    .addService(new GreeterImpl())
                    .build()
                    .start();
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, e.getMessage());
        }
    }

    private final static class GreeterImpl extends GreeterGrpc.GreeterImplBase {

        @Override
        public void sayHello(CompositionRequest.HelloRequest req, StreamObserver<CompositionResponse.HelloResponse> responseObserver) {
            CompositionResponse.HelloResponse response = CompositionResponse.HelloResponse.newBuilder().setMessage("Say : Hello to " + req.getName()).build();
//            mLogText.append("\n" + req.getName());
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }
    }
}
