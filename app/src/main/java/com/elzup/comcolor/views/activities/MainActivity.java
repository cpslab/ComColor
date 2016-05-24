package com.elzup.comcolor.views.activities;

import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;

import com.elzup.comcolor.R;
import com.elzup.comcolor.util.ColorUtil;
import com.elzup.comcolor.views.fragments.CanvasFragment;

import java.nio.charset.Charset;

public class MainActivity extends AppCompatActivity implements NfcAdapter.CreateNdefMessageCallback {
    public static final String TAG = "MainActivity";

    NfcAdapter mNfcAdapter;
    CanvasFragment canvasFragment;
    static final String NDEF_TYPE_FELLOW_PREFIX = "push:";

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
            // Beam 受信
            Parcelable[] raws = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            NdefMessage msg = (NdefMessage) raws[0];
            byte[] payload = msg.getRecords()[0].getPayload();
            String colorStr = new String(payload);
            int newCol = 0x00000000;
            if (colorStr.length() >= 5 && colorStr.substring(0, 5).equals(NDEF_TYPE_FELLOW_PREFIX)) {
                // 端末同士
                newCol = (int) Long.parseLong(colorStr.substring(5), 16);
            } else {
                // カードタグ
                byte status = payload[0];
                int languageCodeLength = status & 0x3F;
                //                    boolean encodeUtf8 = ((status & 0x80) == 0);
                //                    String textEncoding = encodeUtf8 ;
                String hexColStr = new String(payload, languageCodeLength + 1, payload.length - languageCodeLength - 1);
                newCol = (int) Long.parseLong(hexColStr, 16);
            }
            canvasFragment.updateColor(ColorUtil.blend(canvasFragment.mColor, newCol, 0.5f));
        }
    }

    @Override
    public NdefMessage createNdefMessage(NfcEvent event) {
        // mColor (String) の送信
        byte[] bytes1 = (NDEF_TYPE_FELLOW_PREFIX + Integer.toHexString(canvasFragment.mColor)).getBytes(Charset.forName("US-ASCII"));
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
