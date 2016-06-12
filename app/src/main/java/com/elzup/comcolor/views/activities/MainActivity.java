package com.elzup.comcolor.views.activities;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.nfc.NfcAdapter;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.graphics.ColorUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.elzup.comcolor.R;
import com.elzup.comcolor.models.ColorLogObject;
import com.elzup.comcolor.models.ColorLogService;
import com.elzup.comcolor.models.NfcModel;
import com.elzup.comcolor.views.fragments.CanvasFragment;
import com.elzup.comcolor.views.fragments.LogFragment;

public class MainActivity extends AppCompatActivity implements FragmentManager.OnBackStackChangedListener {
    public static final String TAG = "MainActivity";
    
    NfcModel nfc;
    MenuItem historyMenuItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().getDecorView().setBackgroundColor(new ColorLogService(this).getPreColor().getColor());

        FragmentManager fm = getSupportFragmentManager();
        fm.addOnBackStackChangedListener(this);
        fm.beginTransaction().replace(R.id.fragment_container, new CanvasFragment()).commit();

        this.nfc = new NfcModel(this);
        this.colorSync();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        historyMenuItem = menu.findItem(R.id.action_hisotry);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_hisotry:
                FragmentManager fm = getSupportFragmentManager();
                fm.beginTransaction().replace(R.id.fragment_container, new LogFragment()).addToBackStack(null).commit();
                return true;
        }
        return super.onOptionsItemSelected(item);
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
        FragmentManager fm = getSupportFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            // トップに戻る
            fm.popBackStackImmediate();
        }
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
            this.nfc.ndefIntentColorUpdate(intent);
            this.colorSync();
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    void colorSync() {
        ColorLogObject color = new ColorLogService(this).getColor();
        // getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(color));
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(ColorUtils.blendARGB(color.getColor(), 0xff000000, 0.5f)));
        getWindow().setStatusBarColor(ColorUtils.blendARGB(color.getColor(), 0xff000000, 0.6f));
        getWindow().getDecorView().setBackgroundColor(new ColorLogService(this).getPreColor().getColor());
    }

    @SuppressWarnings("deprecation")
    public Drawable getDrawableResource(int id) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return getDrawable(id);
        } else {
            return getResources().getDrawable(id);
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }


    @Override
    public void onBackStackChanged() {
        if (this.historyMenuItem == null) {
            return;
        }
        switch (getSupportFragmentManager().getBackStackEntryCount()) {
            case 0:
                historyMenuItem.setVisible(true);
                this.invalidateOptionsMenu();
            case 1:
                // log 画面ではアイテム非表示
                historyMenuItem.setVisible(false);
        }
    }
}
