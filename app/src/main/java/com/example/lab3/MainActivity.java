package com.example.lab3;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.example.lab3.ui.login.LoginActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.provider.MediaStore;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Kliknieto przycisk FAB", Toast.LENGTH_SHORT).show();
            }
        });
    }

    static final int REQUEST_IMAGE_CAPTURE = 1;

    public void kliknij(View view) {
        Toast.makeText(getApplicationContext(), "Kliknieto przycisk Button 1", Toast.LENGTH_SHORT).show();
        Intent intencja = new Intent(this, LoginActivity.class);
        startActivity(intencja);
    }

    public void kliknij2(View view) {
        Toast.makeText(getApplicationContext(), "Kliknieto przycisk Button 2", Toast.LENGTH_SHORT).show();
        Intent intencja = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intencja, REQUEST_IMAGE_CAPTURE);
    }

    public void kliknij3(View view) {
        Random random = new Random();
        int x = random.nextInt(5 - 1) + 1;

        Button btn = (Button) findViewById(R.id.button3);

        switch (x){
            case 1:
                btn.setBackgroundResource(android.R.drawable.arrow_up_float);
                break;
            case 2:
                btn.setBackgroundResource(android.R.drawable.arrow_down_float);
                break;
            case 3:
                btn.setBackgroundResource(android.R.drawable.alert_dark_frame);
                break;
            case 4:
                btn.setBackgroundResource(R.drawable.obrazek1);
                break;
            case 5:
                btn.setBackgroundResource(R.drawable.obrazek2);
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK ) {
                Toast.makeText(getApplicationContext(), "jest git", Toast.LENGTH_SHORT).show();
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                ConstraintLayout lay = (ConstraintLayout) findViewById(R.id.cont);
                lay.setBackground(new BitmapDrawable(getResources(), imageBitmap));
            }
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), "Coś nie działa", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        switch (id) {
            case R.id.action_settings:
                Toast.makeText(getApplicationContext(), "Kliknieto settings", Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_settings2:
                Toast.makeText(getApplicationContext(), "Kliknieto action", Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_settings3:
                Toast.makeText(getApplicationContext(), "Kliknieto info", Toast.LENGTH_SHORT).show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}