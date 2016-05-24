package com.elzup.comcolor.views.activities;

import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.elzup.comcolor.R;
import com.elzup.comcolor.manager.NFCManager;
import com.elzup.comcolor.util.ColorUtil;
import com.elzup.comcolor.views.fragments.CanvasFragment;

import java.nio.charset.Charset;

public class MainActivity extends AppCompatActivity implements NfcAdapter.CreateNdefMessageCallback {
    public static final String TAG = "MainActivity";

    NfcAdapter mNfcAdapter;
    CanvasFragment canvasFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        mNfcAdapter.setNdefPushMessageCallback(this, this);
        setContentView(R.layout.activity_main);
        canvasFragment = (CanvasFragment) getFragmentManager().findFragmentById(R.id.canvas_fragment);
    }

    @Override
    public synchronized void onResume() {
        super.onResume();
        if (canvasFragment == null) {
            return;
        }
        Intent intent = getIntent();
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
            int recievedColor = NFCManager.parseColor(intent);
            canvasFragment.pushColor(recievedColor);
        }
    }

    @Override
    public NdefMessage createNdefMessage(NfcEvent event) {
        // mColor (String) の送信
        byte[] bytes1 = NFCManager.createPayload(canvasFragment.mColor);
        NdefMessage msg = new NdefMessage(new NdefRecord[]{
                createMimeRecord("application/com.example.beamtest", bytes1)
        });
        return msg;
    }

    public NdefRecord createMimeRecord(String mimeType, byte[] payload) {
        byte[] mimeBytes = mimeType.getBytes(Charset.forName("US-ASCII"));
        NdefRecord mimeRecord = new NdefRecord(
                NdefRecord.TNF_MIME_MEDIA, mimeBytes, new byte[0], payload);
        return mimeRecord;
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }


}
