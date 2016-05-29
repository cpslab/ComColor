package com.elzup.comcolor.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.elzup.comcolor.R;
import com.elzup.comcolor.models.NfcModel;
import com.elzup.comcolor.views.fragments.CanvasFragment;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";

    CanvasFragment canvasFragment;
    NfcModel nfc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        this.nfc = new NfcModel(this);
        this.nfc.ndefIntentCheck();

        canvasFragment = (CanvasFragment) getFragmentManager().findFragmentById(R.id.canvas_fragment);
    }

    @Override
    public void onResume() {
        super.onResume();
        this.nfc.setEnable();
    }

    @Override
    public void onPause() {
        super.onPause();
        this.nfc.setDisable();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        this.nfc.ndefIntentCheck();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }


}
