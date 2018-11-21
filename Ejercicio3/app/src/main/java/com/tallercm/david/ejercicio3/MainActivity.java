package com.tallercm.david.ejercicio3;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    ImageView imgVLogo;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = getApplicationContext();

        imgVLogo = findViewById(R.id.imgVLogo);

        Picasso.with(context).load(R.drawable.logo).into(imgVLogo);

        Button btnIniciarJuego = (Button) findViewById(R.id.btnIniciarJuego);

        btnIniciarJuego.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(context, GameActivity.class);
                startActivity(intent);
                finish();
            }
        });



    }
}
