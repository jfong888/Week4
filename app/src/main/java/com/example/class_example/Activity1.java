package com.example.class_example;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.class_example.databinding.Activity1Binding;

public class Activity1 extends AppCompatActivity {
    private Activity1Binding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = Activity1Binding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        Log.w("Activity1","In onCreate() - Loading Widgets");

        SharedPreferences prefs = getSharedPreferences("Mydata", Context.MODE_PRIVATE);
        String initiallogin = prefs.getString("email", "");

        binding.editTextEmailAddress.setText(initiallogin);
        binding.login.setOnClickListener(click -> {
            String logintyped = binding.editTextEmailAddress.getText().toString();
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("email", logintyped);
            editor.commit();

            Intent np = new Intent(Activity1.this, Activity2.class);
            np.putExtra("email", logintyped);
            startActivity(np);
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.w("Activity1","In onStart() - The application is now visible on screen");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.w("Activity1","In onResume() - The application is now responding to user input");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.w("Activity1","In onPause() - The application no longer responds to user input");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.w("Activity1","In onDestroy() - Any memory used by the application is freed");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.w("Activity1","In onStop() - The application is no longer visible");
    }

}