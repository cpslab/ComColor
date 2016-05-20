package com.elzup.comcolor;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.nio.charset.Charset;

import icepick.Icepick;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, NfcAdapter.CreateNdefMessageCallback {

    // 保持する色, 今は String
    String mColor;
    NfcAdapter mNfcAdapter;

    static final String PREFERENCE_KEY_COLOR = "color";
    static final String PREFERENCE_NAME = "DataSave";

    static final String NDEF_TYPE_FELLOW_PREFIX = "push:";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences data = getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        mColor = data.getString(PREFERENCE_KEY_COLOR, "none");
        Icepick.restoreInstanceState(this, savedInstanceState);
        setContentView(R.layout.activity_main);

        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        mNfcAdapter.setNdefPushMessageCallback(this, this);
        this.updateColorText();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.reset_button:
                String resetColor = "white";
                mColor = resetColor;
                this.updateColorText();
                break;
        }
    }

    @Override
    public NdefMessage createNdefMessage(NfcEvent event) {
        // mColor (String) の送信
        byte[] bytes1 = (NDEF_TYPE_FELLOW_PREFIX + mColor).getBytes(Charset.forName("US-ASCII"));
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
    public synchronized void onResume() {
        super.onResume();
        Intent intent = getIntent();
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
            // Beam 受信
            Parcelable[] raws = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            NdefMessage msg = (NdefMessage) raws[0];
            byte[] payload = msg.getRecords()[0].getPayload();

            String colorStr = new String(payload);
            if (colorStr.length() >= 5 && colorStr.substring(0, 5).equals(NDEF_TYPE_FELLOW_PREFIX)) {
                // 端末同士
                mColor += colorStr.substring(5);
            } else {
                // カードタグ
                byte status = payload[0];
                int languageCodeLength = status & 0x3F;
//                    boolean encodeUtf8 = ((status & 0x80) == 0);
//                    String textEncoding = encodeUtf8 ;
                mColor += new String(payload, languageCodeLength + 1, payload.length - languageCodeLength - 1);
            }
            this.updateColorText();
        }
    }

    /**
     * mColor の値を set してから呼び出すと textView に反映する
     */
    private void updateColorText() {
        SharedPreferences data = getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = data.edit();
        editor.putString(PREFERENCE_KEY_COLOR, mColor);
        editor.apply();
        TextView colorText = (TextView) findViewById(R.id.color_text);
        colorText.setText(mColor);
    }


}
