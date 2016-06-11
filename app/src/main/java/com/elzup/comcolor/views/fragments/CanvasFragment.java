package com.elzup.comcolor.views.fragments;

import android.animation.Animator;
import android.annotation.TargetApi;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.TextView;

import com.elzup.comcolor.R;
import com.elzup.comcolor.models.ColorLogObject;
import com.elzup.comcolor.models.ColorLogService;
import com.elzup.comcolor.util.ColorUtil;

public class CanvasFragment extends Fragment {
    public static final String TAG = "CanvasFragment";
    private ColorLogObject mColor, pColor;
    ColorLogService service;
    TextView colorText;
    Button resetButton;
    int count = 2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.service = new ColorLogService(this.getActivity().getApplicationContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mColor = this.service.getColor();
        pColor = this.service.getPreColor();


        View v = inflater.inflate(R.layout.fragment_canvas, null);
        colorText = (TextView) v.findViewById(R.id.color_text);
        resetButton = (Button) v.findViewById(R.id.reset_button);

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setBackgroundColor(Color.WHITE);
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
                    v.setBackgroundColor(mColor.getColor());
                    anim.start();
                }
            });
        }


//        if (count < 2) {
//            return wa;
//        } else {
//            v.setBackgroundColor(mColor.getColor());
        return v;
//        }

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
        colorText.setText(String.valueOf(ColorUtil.toHexRGBText(mColor.getMergedColor())));
    }

}
