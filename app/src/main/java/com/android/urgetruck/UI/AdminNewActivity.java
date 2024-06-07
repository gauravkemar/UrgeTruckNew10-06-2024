package com.android.urgetruck.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.urgetruck.R;
import com.android.urgetruck.UI.Utils.Utils;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class AdminNewActivity extends AppCompatActivity {

    private TextInputLayout textinputantenna;
    private TextInputEditText tvantenna, edServerIp;
    View layout_toolbar;
    TextView toolbarText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_new);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        layout_toolbar = findViewById(R.id.layout_toolbar);
        initToolbar();
        textinputantenna = findViewById(R.id.textinputantenna);
        edServerIp = findViewById(R.id.edServerIp);
        tvantenna = findViewById(R.id.tvantenna);

        edServerIp.setText(Utils.getSharedPreferences(this, "apiurl"));
        tvantenna.setText(Utils.getSharedPreferences(this, "antennapower"));


        findViewById(R.id.btSubmit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkinputUrl();
            }
        });
        findViewById(R.id.btAntenna).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkinputAntenna();
            }
        });
    }

    private void initToolbar() {
        toolbarText = layout_toolbar.findViewById(R.id.toolbarText);
        toolbarText.setText(getString(R.string.admin));

        ImageView ivLogo = layout_toolbar.findViewById(R.id.ivLogoLeftToolbar);
        ivLogo.setVisibility(View.VISIBLE);
        ivLogo.setImageResource(R.drawable.ut_logo_with_outline);
        ivLogo.setOnClickListener(view -> {
            startActivity(new Intent(AdminNewActivity.this, HomeActivity.class));
            finishAffinity();
        });
    }

    private void checkinputAntenna() {
        String antenna = textinputantenna.getEditText().getText().toString().trim();
        if (antenna.isEmpty()) {
            textinputantenna.setError("Please enter the Antenna Power");
        } else if (Integer.parseInt(antenna) > 300) {
            textinputantenna.setError("Entered Antenna Power Should be less than 300");
        } else {
            Utils.postsharedPreferences(AdminNewActivity.this, "antennapower", antenna);
            Utils.showCustomDialogFinish(AdminNewActivity.this, "Antenna Power Successfully Updated");

        }
    }

    private void checkinputUrl() {

        String url = edServerIp.getText().toString().trim();
        if (url.equals("")) {
            edServerIp.setError("Please enter ip address");
        } else {
            Utils.postsharedPreferences(AdminNewActivity.this, "token", "");
            Utils.postsharedPreferences(AdminNewActivity.this, "username", "");
            Utils.postsharedPreferences(AdminNewActivity.this, "isadmin", "");
            Utils.postsharedPreferences(AdminNewActivity.this, "apiurl", url);

            showCustomDialogFinish(AdminNewActivity.this, "Base Url Updated. Changes will take place after Re-Login");
        }
    }

    public void showCustomDialogFinish(Context context, String message) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                        finishAffinity();
                    }
                })
                .show();

    }
}
