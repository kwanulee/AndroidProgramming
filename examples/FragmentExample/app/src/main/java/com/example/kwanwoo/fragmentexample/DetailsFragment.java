package com.example.kwanwoo.fragmentexample;



import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailsFragment extends Fragment {
    static int index=-1;

    public DetailsFragment() {
        // Required empty public constructor
    }

    public void setSelection(int i) { index = i; }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_details, container, false);
        TextView tv = (TextView)view.findViewById(R.id.textview);

        if (index >=0)
            tv.setText(Shakespeare.DIALOGUE[index]);

        return view;
    }
}
