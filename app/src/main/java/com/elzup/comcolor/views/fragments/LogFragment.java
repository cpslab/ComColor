package com.elzup.comcolor.views.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.elzup.comcolor.R;

public class LogFragment extends Fragment {
    public static final String TAG = "CanvasFragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_log, null);
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}

