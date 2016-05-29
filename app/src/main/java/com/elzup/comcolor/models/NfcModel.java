package com.elzup.comcolor.models;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;

import com.elzup.comcolor.R;
import com.elzup.comcolor.manager.NFCManager;
import com.elzup.comcolor.views.activities.MainActivity;
import com.elzup.comcolor.views.fragments.CanvasFragment;

import java.nio.charset.Charset;

public class NfcModel implements NfcAdapter.CreateNdefMessageCallback {
    private PendingIntent mPendingIntent;
    private IntentFilter[] mIntentFilters;
    NfcAdapter mNfcAdapter;
    private MainActivity mainAcitivty;

    public NfcModel(MainActivity mainActivity) {
        this.mainAcitivty = mainActivity;

        mNfcAdapter = NfcAdapter.getDefaultAdapter(mainActivity);
        mNfcAdapter.setNdefPushMessageCallback(this, mainActivity);

        Intent intent = new Intent(mainActivity, getClass());
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        mPendingIntent = createPendingIntent();

        IntentFilter tagIntentFilter = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        IntentFilter ndefIntentFilter = IntentFilter.create(NfcAdapter.ACTION_NDEF_DISCOVERED, "*/*");
        mIntentFilters = new IntentFilter[]{ ndefIntentFilter, tagIntentFilter };
    }

    public void setEnable() {
        mNfcAdapter.enableForegroundDispatch(mainAcitivty, mPendingIntent, mIntentFilters, null);
    }

    public void setDisable() {
        mNfcAdapter.disableForegroundDispatch(mainAcitivty);
    }

    private PendingIntent createPendingIntent() {
        Intent i = new Intent(mainAcitivty, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
                | Intent.FLAG_ACTIVITY_NEW_TASK);
        return PendingIntent.getActivity(mainAcitivty, 0, i, 0);
    }

    public void ndefIntentCheck() {
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(mainAcitivty.getIntent().getAction())) {
            int recievedColor = NFCManager.parseColor(mainAcitivty.getIntent());
            new StateService(mainAcitivty).addColor(recievedColor);
            mainAcitivty.getFragmentManager().beginTransaction().replace(R.id.canvas_fragment, new CanvasFragment());
        }
    }


    @Override
    public NdefMessage createNdefMessage(NfcEvent event) {
        // mColor (String) の送信
        byte[] bytes1 = NFCManager.createPayload(mainAcitivty.getColor());
        NdefMessage msg = new NdefMessage(new NdefRecord[]{
                createMimeRecord("application/com.elzup.comcolor", bytes1)

        });
        mNfcAdapter.enableForegroundDispatch(mainAcitivty, mPendingIntent, mIntentFilters, null);
        return msg;
    }

    public NdefRecord createMimeRecord(String mimeType, byte[] payload) {
        byte[] mimeBytes = mimeType.getBytes(Charset.forName("US-ASCII"));
        NdefRecord mimeRecord = new NdefRecord(
                NdefRecord.TNF_MIME_MEDIA, mimeBytes, new byte[0], payload);
        return mimeRecord;
    }


}