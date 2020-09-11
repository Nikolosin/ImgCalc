package com.example.imgcalc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class SetActivity extends AppCompatActivity {

    public static final int REQUEST_CODE_PERMISSION_READ_STORAGE = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);
        Button btnOk = findViewById(R.id.button);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int permissionStatus = ContextCompat.checkSelfPermission(SetActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE);

                if (permissionStatus == PackageManager.PERMISSION_GRANTED) {

                    LoadImg();
                } else {
                    ActivityCompat.requestPermissions(SetActivity.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            REQUEST_CODE_PERMISSION_READ_STORAGE);
                }

            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_PERMISSION_READ_STORAGE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    LoadImg();
                }
                return;
        }
    }

    private void LoadImg()
    {

        ImageView view = (ImageView) findViewById(R.id.imageView);
        final EditText inputName = findViewById(R.id.nameImg);
        if (isExternalStorageWritable()) {

            File logFile = new File(getApplicationContext().getExternalFilesDir(null),"log.txt");
            try {
                FileWriter logWriter = new FileWriter(logFile);
                logWriter.append("App loaded");
                logWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                    inputName.getText().toString());

            Bitmap b = BitmapFactory.decodeFile(file.getAbsolutePath());
            view.setImageBitmap(b);
            Toast.makeText(this, file.getAbsolutePath(), Toast.LENGTH_LONG).show();
            Intent intent = new Intent();
            intent.putExtra("name", inputName.getText().toString());
            setResult(RESULT_OK, intent);
            finish();
        } else {
            Toast.makeText(this, "File Error", Toast.LENGTH_LONG).show();
        }
    }

    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }
}