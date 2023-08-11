package com.android.urgetruck.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.urgetruck.R;
import com.android.urgetruck.UI.Utils.Utils;

public class HomeActivity extends AppCompatActivity {

    //HomeRecyclerViewAdapter adapter;
    TextView toolbarText;
    View layout_toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home1);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        if (Utils.getSharedPreferences(this, "isadmin").equals("true")) {
            findViewById(R.id.cardview_admin).setVisibility(View.VISIBLE);
        }

        layout_toolbar = findViewById(R.id.layout_toolbar);
        initToolbar();
        toolbarText = layout_toolbar.findViewById(R.id.toolbarText);
        toolbarText.setText(Utils.getSharedPreferences(this, "username"));


        findViewById(R.id.cardview_scanrfid).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, VehicleDetectionActivity.class));
            }
        });

        findViewById(R.id.cardview_tagfid).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, VehicleRfidMappingActivity.class));
            }
        });

        findViewById(R.id.cardview_securitycheck).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, SecurityInspectionActivity.class));
            }
        });
        findViewById(R.id.cardview_physicalcheck).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, ExitClearanceActivity.class));
            }
        });
        findViewById(R.id.cardview_track).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, TrackVehicleActivity.class));
            }
        });

        /*findViewById(R.id.cardview_logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.postsharedPreferences(HomeActivity.this, "token", "");
                Utils.postsharedPreferences(HomeActivity.this, "username", "");
                Utils.postsharedPreferences(HomeActivity.this, "isadmin", "");
                startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                finish();
            }
        });*/

        findViewById(R.id.cardview_admin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, AdminNewActivity.class));
            }
        });

    }

    private void initToolbar() {
        /*ImageView ivBack = layout_toolbar.findViewById(R.id.ivLeftToolbar);
        ivBack.setImageResource(R.drawable.back);
        ivBack.setOnClickListener(view -> {
            onBackPressed();
        });*/
        ImageView ivLogo =layout_toolbar.findViewById(R.id.ivLogoLeftToolbar);
        ivLogo.setVisibility(View.VISIBLE);
        ivLogo.setImageResource(R.drawable.ut_logo_with_outline);
        ImageView ivLogout = layout_toolbar.findViewById(R.id.ivRightToolbar);
        ivLogout.setVisibility(View.VISIBLE);
        ivLogout.setImageResource(R.drawable.logout_white);
        ivLogout.setOnClickListener(view -> {
            showLogoutPopup();
        });

    }

    public void showLogoutPopup() {
        AlertDialog.Builder builder;
        AlertDialog alert;
        builder = new AlertDialog.Builder(HomeActivity.this);
        builder.setMessage("Are you sure you want to logout?")
                .setCancelable(false)
                .setPositiveButton("Yes", (dialog, id) -> {
                    Utils.postsharedPreferences(HomeActivity.this, "token", "");
                    Utils.postsharedPreferences(HomeActivity.this, "username", "");
                    Utils.postsharedPreferences(HomeActivity.this, "isadmin", "");
                    startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                    finish();
                })
                .setNegativeButton("Cancel", (dialog, id) -> dialog.cancel());
        alert = builder.create();
        alert.show();
    }
}

