package com.example.myrssreaderapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myrssreaderapp.R;

public class SettingActivity extends AppCompatActivity {

    private ImageView imgAvatar;
    private TextView txtEmail;
    private Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        imgAvatar = findViewById(R.id.imgAvatar);
        txtEmail = findViewById(R.id.txtUsername);
        btnLogout = findViewById(R.id.btnLogout);

        // Lấy email từ Intent
        String email = getIntent().getStringExtra("email");
        if (email == null) email = "user@example.com"; // mặc định
        txtEmail.setText(email);

        // Logout: quay về LoginActivity
        btnLogout.setOnClickListener(v -> {
            Intent intent = new Intent(SettingActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }
}
