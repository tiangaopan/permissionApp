package com.example.administrator.permissionapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.permissionapp.permission.PermissionFail;
import com.example.administrator.permissionapp.permission.PermissionHelper;
import com.example.administrator.permissionapp.permission.PermissionSuccess;
import com.example.administrator.permissionapp.permission.PermissionUtils;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView call = findViewById(R.id.callphone);
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PermissionHelper.with(MainActivity.this).requestCode(100)
                        .requestPermissions(Manifest.permission.CALL_PHONE)
                        .request();
            }
        });
    }

    @PermissionSuccess(requestCode = 100)
    private void callPhone() {
        Intent intent = new Intent(Intent.ACTION_CALL);
        Uri parse = Uri.parse("tel:18211042972");
        intent.setData(parse);
        startActivity(intent);
    }

    @PermissionFail(requestCode = 100)
    private void failPhone() {
        Toast.makeText(this, "失败了", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionHelper.requestPermissionsResult(this, requestCode, permissions);
    }
}
