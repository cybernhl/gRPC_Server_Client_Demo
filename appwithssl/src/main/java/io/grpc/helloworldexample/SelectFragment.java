package io.grpc.helloworldexample;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class SelectFragment extends Fragment {
    private static final String TAG = "SelectFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_select, container, false);
        Button normal = root.findViewById(R.id.normal);
        normal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().addToBackStack(TAG).replace(R.id.content, new MainActivityFragment()).commitAllowingStateLoss();
            }
        });
        Button withstream = root.findViewById(R.id.withstream);
        withstream.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().addToBackStack(TAG).replace(R.id.content, new MainActivityFragment()).commitAllowingStateLoss();
            }
        });

        return root;
    }
}
