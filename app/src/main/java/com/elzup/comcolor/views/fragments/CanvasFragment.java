package com.elzup.comcolor.views.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.SurfaceView;
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
    WaveAnimationSurfaceView wa;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.service = new ColorLogService(this.getActivity().getApplicationContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_canvas, null);
        wa = new WaveAnimationSurfaceView(getActivity());
        colorText = (TextView) v.findViewById(R.id.color_text);
        resetButton = (Button) v.findViewById(R.id.reset_button);
        //        wa = (WaveAnimationSurfaceView) v.findViewById(R.id.wave_surfaceview);
        mColor = this.service.getColor();

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                updateColor(Color.WHITE, Color.GRAY);
            }
        });
        return wa;
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
        this.mColor = this.service.getColor();
        colorText.setText(String.valueOf(ColorUtil.toHexRGBText(mColor)));
    }
}
