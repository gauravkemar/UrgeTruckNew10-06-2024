package com.android.urgetruck.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.android.urgetruck.R;

public class SecurityInspectionActivity extends AppCompatActivity {

    View layout_toolbar;
    TextView toolbarText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        layout_toolbar = findViewById(R.id.layout_toolbar);
        initToolbar();

        FragmentTransaction fragmentTransaction =getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.securityfragment_container,new ScanrfidFragment());
        fragmentTransaction.commit();
    }

    private void initToolbar() {
        toolbarText = layout_toolbar.findViewById(R.id.toolbarText);
        toolbarText.setText(getString(R.string.security_inspection_activity));

        ImageView ivLogo =layout_toolbar.findViewById(R.id.ivLogoLeftToolbar);
        ivLogo.setVisibility(View.VISIBLE);
        ivLogo.setImageResource(R.drawable.ut_logo_with_outline);
        ivLogo.setOnClickListener(view -> {
            startActivity(new Intent(SecurityInspectionActivity.this,HomeActivity.class));
            finishAffinity();
        });
    }
}
