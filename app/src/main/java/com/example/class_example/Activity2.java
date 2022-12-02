package com.example.class_example;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.class_example.databinding.Activity2Binding;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class Activity2 extends AppCompatActivity {
    private Activity2Binding binding;
    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    ImageView photoImg2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = Activity2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        File myImage = new File("/data/data/com.example.class_example/profile.png");
        if(myImage.exists()){ //load your profile picture
            Bitmap theImage = BitmapFactory.decodeFile("/data/data/com.example.class_example/profile.png");
            binding.imageView.setImageBitmap( theImage);
        }

        binding.button2.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
                } else {
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                }
            }
        });

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        String emailAddress = intent.getStringExtra("email");

        // Capture the layout's TextView and set the string as its text
        binding.textView3.setText("Welcome " + emailAddress);

        SharedPreferences prefs = getSharedPreferences("Mydata", Context.MODE_PRIVATE);
        String initialnumber = prefs.getString("Phone", "");

        binding.editTextPhone.setText(initialnumber);
        binding.PHONE.setOnClickListener(click -> {
            String phonenumber = binding.editTextPhone.getText().toString();
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("Phone", phonenumber);
            editor.commit();

            Intent call = new Intent(Intent.ACTION_DIAL);
            call.setData(Uri.parse("tel:" + phonenumber));
            startActivity(call);
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ImageView photoImg = (ImageView) findViewById(R.id.imageView);
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap)data.getExtras().get("data");
            photoImg.setImageBitmap(photo);
            photoImg.setDrawingCacheEnabled(true);
            photoImg.buildDrawingCache();
            Bitmap bm = photoImg.getDrawingCache();

            OutputStream fOut = null;
            Uri outputFileUri;
            try {
                File root = new File("/data/data/com.example.class_example");
                root.mkdirs();
                File sdImageMainDirectory = new File(root, "profile.png");
                outputFileUri = Uri.fromFile(sdImageMainDirectory);
                fOut = new FileOutputStream(sdImageMainDirectory);
            } catch (Exception e) {
                Toast.makeText(this, "Error occured. Please try again later.",
                        Toast.LENGTH_SHORT).show();
            }

            try {
                bm.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                fOut.flush();
                fOut.close();
            } catch (Exception e){

            }
        }

    }
}