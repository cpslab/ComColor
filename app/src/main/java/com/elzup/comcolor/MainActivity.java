package com.elzup.comcolor;

import android.content.Intent;
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

public class MainActivity extends AppCompatActivity implements View.OnClickListener, NfcAdapter.CreateNdefMessageCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView text = (TextView)findViewById(R.id.nfcTextView);
        //インテントの取得
        Intent intent = getIntent();
        //NDEF対応カードの検出か確認
        String action = intent.getAction();

        if(NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)){
            Parcelable[] raws = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            NdefMessage[] msgs = new NdefMessage[raws.length];

            String message = "";
            for(int i=0;i<raws.length;i++){
                msgs[i] = (NdefMessage)raws[i];
                for(NdefRecord record : msgs[i].getRecords()) {
                    byte[] payload = record.getPayload();
                    byte status = payload[0];
                    int languageCodeLength = status & 0x3F;
//                    boolean encodeUtf8 = ((status & 0x80) == 0);
//                    String textEncoding = encodeUtf8 ;
                    message = new String(payload,languageCodeLength + 1,payload.length - languageCodeLength -1);
                }
            }text.setText(message);
        }
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        mNfcAdapter.setNdefPushMessageCallback(this, this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.reset_button:
                TextView colorText = (TextView) findViewById(R.id.color_text);
                colorText.setText("白");
                break;
        }
    }

    NfcAdapter mNfcAdapter;

    @Override
    public NdefMessage createNdefMessage(NfcEvent event) {
        String string1 = "test1";
        String string2 = "test2";
        byte[] bytes1 = string1.getBytes(Charset.forName("US-ASCII"));
        byte[] bytes2 = string2.getBytes(Charset.forName("US-ASCII"));
        NdefMessage msg = new NdefMessage(new NdefRecord[]{
                createMimeRecord("application/com.example.beamtest", bytes1),
                createMimeRecord("application/com.example.beamtest", bytes2),
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
            Parcelable[] rawMsgs = intent.getParcelableArrayExtra( NfcAdapter.EXTRA_NDEF_MESSAGES);
            NdefMessage msg = (NdefMessage) rawMsgs[0];
            String string1 = new String(msg.getRecords()[0].getPayload());
            String string2 = new String(msg.getRecords()[1].getPayload());
        }
    }


}
