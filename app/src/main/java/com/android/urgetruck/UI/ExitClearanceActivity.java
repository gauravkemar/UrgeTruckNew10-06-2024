package com.android.urgetruck.UI;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;

import com.android.urgetruck.R;
import com.symbol.emdk.EMDKManager;
import com.symbol.emdk.barcode.Scanner;

import java.util.ArrayList;
import java.util.List;

import com.symbol.emdk.EMDKBase;
import com.symbol.emdk.EMDKManager;
import com.symbol.emdk.ProfileManager;
import com.symbol.emdk.barcode.BarcodeManager;

import com.symbol.emdk.EMDKManager;
import com.symbol.emdk.EMDKManager.EMDKListener;
import com.symbol.emdk.EMDKResults;
import com.symbol.emdk.barcode.BarcodeManager;
import com.symbol.emdk.barcode.ScanDataCollection;
import com.symbol.emdk.barcode.ScanDataCollection.ScanData;
import com.symbol.emdk.barcode.Scanner;
import com.symbol.emdk.barcode.Scanner.DataListener;
import com.symbol.emdk.barcode.Scanner.StatusListener;
import com.symbol.emdk.barcode.Scanner.TriggerType;
import com.symbol.emdk.barcode.ScannerConfig;
import com.symbol.emdk.barcode.ScannerException;
import com.symbol.emdk.barcode.ScannerResults;
import com.symbol.emdk.barcode.StatusData;
import com.symbol.emdk.barcode.StatusData.ScannerStates;


public class ExitClearanceActivity extends AppCompatActivity  {

    View layout_toolbar;
    TextView toolbarText;
    public static String result="";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_physicalcheck);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        layout_toolbar = findViewById(R.id.layout_toolbar);
        initToolbar();

        FragmentTransaction fragmentTransaction =getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.physicalcheckfragment_container,new ScanrfidFragment());
        fragmentTransaction.commit();
        Log.d("ExitClearance","This is ExitClearance Activity");
    }

    private void initToolbar() {
        toolbarText = layout_toolbar.findViewById(R.id.toolbarText);
        toolbarText.setText(getString(R.string.exit_clearance_activity));

        ImageView ivLogo =layout_toolbar.findViewById(R.id.ivLogoLeftToolbar);
        ivLogo.setVisibility(View.VISIBLE);
        ivLogo.setImageResource(R.drawable.ut_logo_with_outline);
        ivLogo.setOnClickListener(view -> {
            startActivity(new Intent(ExitClearanceActivity.this,HomeActivity.class));
            finishAffinity();
        });
    }


}
