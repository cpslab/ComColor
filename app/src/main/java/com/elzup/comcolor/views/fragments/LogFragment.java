package com.elzup.comcolor.views.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.elzup.comcolor.R;
import com.elzup.comcolor.adapters.ColorLogAdapter;
import com.elzup.comcolor.models.ColorLogService;

public class LogFragment extends Fragment {
    public static final String TAG = "CanvasFragment";

    private RecyclerView recyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_log, null);
        recyclerView = (RecyclerView) v.findViewById(R.id.logs_recycler_view);
        ColorLogService service = new ColorLogService(this.getContext());
        ColorLogAdapter adapter = new ColorLogAdapter(service.getColorListPretty());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}

