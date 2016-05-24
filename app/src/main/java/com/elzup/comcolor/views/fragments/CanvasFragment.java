package com.elzup.comcolor.views.fragments;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.elzup.comcolor.R;
import com.elzup.comcolor.models.StateService;

public class CanvasFragment extends Fragment {
    public static final String TAG = "CanvasFragment";

    public int mColor;
    StateService service;

    TextView colorText;
    Button resetButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.service = new StateService(this.getActivity().getApplicationContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_canvas, null);
        colorText = (TextView) v.findViewById(R.id.color_text);
        resetButton = (Button) v.findViewById(R.id.reset_button);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateColor(Color.WHITE);
            }
        });
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        resyncColor();

    }

    public void resyncColor() {
        this.updateColor(this.service.getColor());
    }

    /**
     * mColor の値を set してから呼び出すと textView に反映する
     */
    public void updateColor(int color) {
        this.mColor = color;
        this.service.setColor(color);
        colorText.setText(String.valueOf(this.mColor));
        getView().setBackgroundColor(this.mColor);
    }
}
