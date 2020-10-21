package com.example.smartageliketool;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.smartageliketool.data.util.PrefManager;

public class BaseActivity extends Activity {


    public static final String TAG = "xeagle69_BaseActivity >>";
    public static String Version;
    public static PrefManager prefManager;
    private Dialog progressDialog;
    SharedPreferences prefs;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            Version = getPackageManager().getPackageInfo(getBaseContext().getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        prefManager = new PrefManager(prefs);


    }


    //********************************************************************************************
    @SuppressLint("LongLogTag")
    public void progressDialogShow(final boolean show) {
        try {
            if (progressDialog == null) {
                progressDialog = new Dialog(this, android.R.style.Theme_Black);
                View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.progress_dialog, null);

                progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                progressDialog.setContentView(view);
            }


            if (!BaseActivity.this.isFinishing()) {
                if (show) {
                    progressDialog.show();
                    Log.d(TAG, "showProgress");
                } else {
                    progressDialog.dismiss();
                    Log.d(TAG, "hideProgress");
                }
            }


        } catch (Exception e) {
            Log.i("progress ex", e.getMessage());
        }
    }
    //********************************************************************************************


    //********************************************************************************************
    @SuppressLint("LongLogTag")
    private boolean hasNetConnection() {

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);

        NetworkInfo mobileInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        boolean mobileConnected = mobileInfo.getState() == NetworkInfo.State.CONNECTED;

        NetworkInfo wifiInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        boolean wifiConnected = wifiInfo.getState() == NetworkInfo.State.CONNECTED;

        if (mobileConnected || wifiConnected) {
            Log.d(TAG, "Internet is available");
            return true;
        } else {
            Log.d(TAG, "Internet is NOT available");
            return false;
        }

    }
    //********************************************************************************************


}
