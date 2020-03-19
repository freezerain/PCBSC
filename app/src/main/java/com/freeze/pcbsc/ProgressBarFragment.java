package com.freeze.pcbsc;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import javax.xml.datatype.Duration;

public class ProgressBarFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.progress_bar, container, false);
        return v;
    }

    public void somethig(){
        Toast.makeText(getContext(), "ProgressBar", Toast.LENGTH_SHORT).show();

    }
}
