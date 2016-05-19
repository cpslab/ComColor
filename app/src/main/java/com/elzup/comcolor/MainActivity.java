package com.elzup.comcolor;

import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

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
}
