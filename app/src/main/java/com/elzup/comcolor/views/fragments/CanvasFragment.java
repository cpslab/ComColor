package com.elzup.comcolor.views.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.elzup.comcolor.R;
import com.elzup.comcolor.models.ColorLogService;
import com.elzup.comcolor.util.ColorUtil;

public class CanvasFragment extends Fragment {
    public static final String TAG = "CanvasFragment";

    private int mColor;
    ColorLogService service;

    TextView colorText;
    Button resetButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.service = new ColorLogService(this.getActivity().getApplicationContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_canvas, null);
        colorText = (TextView) v.findViewById(R.id.color_text);
        resetButton = (Button) v.findViewById(R.id.reset_button);
        mColor = this.service.getColor();
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

    /**
     * UserDefault に保存されている色に設定
     */
    public void resyncColor() {
        this.updateColor(this.service.getColor(), false);
    }

    /**
     * mColor の値を set してから呼び出すと textView に反映する
     */
    public void updateColor(int color, boolean isLog) {
        this.mColor = color | 0xff000000;
        if (isLog) {
            this.service.setColor(mColor);
        }
        colorText.setText(String.valueOf(ColorUtil.toHexRGBText(mColor)));
        getView().setBackgroundColor(mColor);
    }

    public void updateColor(int color) {
        this.updateColor(color, true);
    }
}
