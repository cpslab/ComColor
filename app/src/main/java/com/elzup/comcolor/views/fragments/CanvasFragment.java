package com.elzup.comcolor.views.fragments;

import android.animation.Animator;
import android.annotation.TargetApi;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.elzup.comcolor.R;
import com.elzup.comcolor.models.ColorLogObject;
import com.elzup.comcolor.models.ColorLogService;
import com.elzup.comcolor.util.ColorUtil;

public class CanvasFragment extends Fragment {
    public static final String TAG = "CanvasFragment";
    private ColorLogObject color;
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
        color = this.service.getColor();

        View v = inflater.inflate(R.layout.fragment_canvas, null);
        colorText = (TextView) v.findViewById(R.id.color_text);
        resetButton = (Button) v.findViewById(R.id.reset_button);

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateColor(Color.WHITE);
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            v.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                    v.removeOnLayoutChangeListener(this);
                    float finalRadius = (float) Math.hypot(v.getWidth(), v.getHeight());
                    Animator anim = ViewAnimationUtils.createCircularReveal(v, v.getWidth(), v.getHeight(), 0, finalRadius);
                    v.setBackgroundColor(color.getColor());
                    anim.start();
                }
            });
        }
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
        this.color = this.service.getColor();
        colorText.setText(String.valueOf(ColorUtil.toHexRGBText(color.getMergedColor())));
    }

    public void updateColor(int color, boolean isLog) {
        color = color | ColorUtil.CLEAR_COLOR_FILTER;
        if (isLog) {
            this.service.setColor(color, color);
        }
        colorText.setText(String.valueOf(ColorUtil.toHexRGBText(color)));
        getView().setBackgroundColor(color);
    }

    public void updateColor(int color) {
        this.updateColor(color, true);
    }

}
