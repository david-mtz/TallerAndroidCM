package com.tallercm.david.ejercicio2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class Main2Activity extends AppCompatActivity {

    TextView textVTitulo;
    TextView textVDescripcion;
    ImageView imgVProducto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Bundle bundle = new Bundle();
        bundle = getIntent().getExtras();
        String nombre = (String) bundle.getString("nombre");
        String descripcion = (String) bundle.getString("descripcion");
        String id = (String) bundle.getString("id");

        textVTitulo = (TextView)findViewById(R.id.textVTitulo);
        textVDescripcion = (TextView)findViewById(R.id.textVDescripcion);
        imgVProducto = (ImageView)findViewById(R.id.imgVProducto);

        textVTitulo.setText(nombre);
        textVDescripcion.setText(descripcion);

        Picasso.get()
                .load("https://www.serverbpw.com/cm/2019-1/" + id + ".png")
                .into(imgVProducto);

    }
}
