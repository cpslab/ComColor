package com.elzup.comcolor.views.activities;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.elzup.comcolor.R;
import com.elzup.comcolor.manager.NFCManager;
import com.elzup.comcolor.models.StateService;
import com.elzup.comcolor.util.ColorUtil;
import com.elzup.comcolor.views.fragments.CanvasFragment;

import java.nio.charset.Charset;

public class MainActivity extends AppCompatActivity implements NfcAdapter.CreateNdefMessageCallback {
    public static final String TAG = "MainActivity";

    NfcAdapter mNfcAdapter;
    CanvasFragment canvasFragment;
    private PendingIntent mPendingIntent;
    private IntentFilter[] mIntentFilters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mNfcAdapter = NfcAdapter.getDefaultAdapter(getApplicationContext());
        mNfcAdapter.setNdefPushMessageCallback(this, this);

        Intent intent = new Intent(getApplicationContext(), getClass());
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        mPendingIntent = createPendingIntent();

        IntentFilter tagIntentFilter = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        IntentFilter ndefIntentFilter = IntentFilter.create(NfcAdapter.ACTION_NDEF_DISCOVERED, "*/*");
        mIntentFilters = new IntentFilter[]{ ndefIntentFilter, tagIntentFilter };

        setContentView(R.layout.activity_main);
        this.ndefIntentCheck();
        canvasFragment = (CanvasFragment) getFragmentManager().findFragmentById(R.id.canvas_fragment);
    }

    @Override
    public void onResume() {
        super.onResume();
        mNfcAdapter.enableForegroundDispatch(this, mPendingIntent, mIntentFilters, null);
    }

    @Override
    public void onPause() {
        super.onPause();

        //foregrandDispathch無効
        mNfcAdapter.disableForegroundDispatch(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        this.ndefIntentCheck();
    }

    protected void ndefIntentCheck() {
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
            int recievedColor = NFCManager.parseColor(getIntent());
            new StateService(this.getApplicationContext()).addColor(recievedColor);
            getFragmentManager().beginTransaction().replace(R.id.canvas_fragment, new CanvasFragment());
        }
    }


    @Override
    public NdefMessage createNdefMessage(NfcEvent event) {
        // mColor (String) の送信
        byte[] bytes1 = NFCManager.createPayload(canvasFragment.mColor);
        NdefMessage msg = new NdefMessage(new NdefRecord[]{
                createMimeRecord("application/com.elzup.comcolor", bytes1)

        });
        mNfcAdapter.enableForegroundDispatch(this, mPendingIntent, mIntentFilters, null);
        return msg;
    }

    public NdefRecord createMimeRecord(String mimeType, byte[] payload) {
        byte[] mimeBytes = mimeType.getBytes(Charset.forName("US-ASCII"));
        NdefRecord mimeRecord = new NdefRecord(
                NdefRecord.TNF_MIME_MEDIA, mimeBytes, new byte[0], payload);
        return mimeRecord;
    }

    private PendingIntent createPendingIntent() {
        Intent i = new Intent(this, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
                | Intent.FLAG_ACTIVITY_NEW_TASK); // 【5】
        return PendingIntent.getActivity(this, 0, i, 0);
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }


}
