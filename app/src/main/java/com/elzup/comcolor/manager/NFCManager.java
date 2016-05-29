package com.elzup.comcolor.manager;

import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.os.Parcelable;
import android.util.Log;

import java.nio.charset.Charset;

public class NFCManager {

    static final String NDEF_TYPE_FELLOW_PREFIX = "push:";

    public static int parseColor(Intent intent) {
        Parcelable[] raws = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
        NdefMessage msg = (NdefMessage) raws[0];
        byte[] payload = msg.getRecords()[0].getPayload();
        String dataStr = new String(payload);
        if (fromAndroidMessage(dataStr)) {
            // ndef, Android Beam
            return (int) Long.parseLong(dataStr.substring(NDEF_TYPE_FELLOW_PREFIX.length()), 16);
        }
        // カードタグ
        byte status = payload[0];
        int languageCodeLength = status & 0x3F;
        String hexColStr = new String(payload, languageCodeLength + 1, payload.length - languageCodeLength - 1);
        return (int) Long.parseLong(hexColStr, 16);
    }

    public static byte[] createPayload(int color) {
        return (NDEF_TYPE_FELLOW_PREFIX + Integer.toHexString(color)).getBytes(Charset.forName("US-ASCII"));
    }

    public static boolean fromAndroidMessage(String dataStr) {
        return dataStr.length() >= 5 && dataStr.substring(0, 5).equals(NDEF_TYPE_FELLOW_PREFIX);
    }

}
