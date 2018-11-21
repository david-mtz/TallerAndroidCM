package com.tallercm.david.ejercicio3;

import android.os.*;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.transition.Visibility;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class GameActivity extends AppCompatActivity {

    protected String palabra;
    protected String categoria;
    protected List<String> palabraResolucion = new ArrayList<String>();
    protected int intentos;
    protected int errores = 0;
    protected int aciertos = 0;
    ExamplePListParser mPListParser = null;
    protected Context contextActivity;
    TextView txtVCategoria,
            txtGuiones,
            txtRespuesta,
            txtIntentos;
    ImageView imgAhorcado;
    ProgressBar progressBar;
    Chronometer tiempoJuego;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        contextActivity = getApplicationContext();
        txtVCategoria = findViewById(R.id.txtVCategoria);
        imgAhorcado = findViewById(R.id.imgAhorcado);
        progressBar = findViewById(R.id.progressBar);
        txtRespuesta = findViewById(R.id.txtRespuesta);
        txtIntentos = findViewById(R.id.txtIntentos);
        tiempoJuego = findViewById(R.id.tiempoJuego);

        Picasso.with(contextActivity).load(R.drawable.e0).into(imgAhorcado);

        CargarAPI cargarElementos = new CargarAPI();
        // Establecer los elementos del juego
        cargarElementos.execute();


    }

    public void parseAPI(String plist) {
        InputStream is2 = new ByteArrayInputStream(plist.getBytes());
        mPListParser = new ExamplePListParser(is2);
        if( mPListParser.getConfigurationObject("PALABRA") != null ) {
            ArrayList<Object> configuracion = (ArrayList<Object>) mPListParser.getConfigurationObject("PALABRA");
            setIntentos(Integer.parseInt(configuracion.get(2).toString()));
            setPalabra(configuracion.get(1).toString());
            setCategoria(configuracion.get(0).toString());
        }
        iniciarJuego();
    }

    public void iniciarJuego() {

        AlphaAnimation fadeOut;
        fadeOut = new AlphaAnimation(1,0); //fade out animation from 1 (fully visible) to 0 (transparent)
        fadeOut.setDuration(1000); //set duration in mill seconds
        fadeOut.setFillAfter(true);
        progressBar.startAnimation(fadeOut);
        progressBar.setVisibility(View.GONE);

        tiempoJuego.start();
        tiempoJuego.setFormat("Tiempo: %s");
        txtVCategoria.setText("Categoría: " + getCategoria());
        for(int i=0; i < palabra.length(); i++) {
            palabraResolucion.add("_");
        }

        dibujarRespuesta();
        dibujarIntentos();
    }

    public void onClickLetra(View btn) {
        Button btnLetra = (Button) btn;

        if(errores >= intentos)
            return;

        if( btnLetra.getTag() == "selected" ) {
            Toast.makeText(contextActivity, R.string.btnSelected, Toast.LENGTH_SHORT).show();
            return;
        }

        btnLetra.setBackground(ContextCompat.getDrawable(contextActivity, R.drawable.buttonshapeblack));
        btnLetra.setTag("selected");

        char letraSelec = btnLetra.getText().toString().toLowerCase().charAt(0);
        String palabraLower = getPalabra().toLowerCase();
        boolean cambio = false;

        for(int i=0; i<palabraLower.length(); i++) {
            if( palabraLower.charAt(i) == letraSelec ) {
                palabraResolucion.set(i, btnLetra.getText().toString().toUpperCase());
                aciertos++;
                cambio = true;
            }
        }

        if( cambio ) {
            dibujarRespuesta();
        } else {
            errores++;
            Toast.makeText(contextActivity, R.string.error, Toast.LENGTH_LONG).show();
            dibujarIntentos();
            cambiarImagen(errores);
            if(errores >= intentos) {
                tiempoJuego.stop();
                Toast.makeText(contextActivity, R.string.perdedor, Toast.LENGTH_LONG).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent inicio = new Intent(contextActivity, MainActivity.class);
                        startActivity(inicio);
                        finish();
                    }
                }, 2500);
            }
        }

        if( aciertos == getPalabra().length() ) {
            // Agregar al marcador
            tiempoJuego.stop();
            Toast.makeText(contextActivity, R.string.ganador, Toast.LENGTH_LONG).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent inicio = new Intent(contextActivity, MainActivity.class);
                    startActivity(inicio);
                    finish();
                }
            }, 2500);
        }

    }

    public void dibujarRespuesta() {
        String txtRespuestaString = "";
        for(int i = 0; i < palabraResolucion.size(); i++) {
            txtRespuestaString += palabraResolucion.get(i);
            if(i+1 < palabraResolucion.size())
                txtRespuestaString += " ";
        }
        txtRespuesta.setText(txtRespuestaString);
    }

    public void dibujarIntentos() {
        String erroresRestantes = "Intentos restantes: " + (intentos-errores) + "";
        txtIntentos.setText(erroresRestantes);
    }

    public void cambiarImagen(int error) {
        int img;
        switch(error) {
            case 1: img = R.drawable.e1;
                break;
            case 2: img = R.drawable.e2;
                break;
            case 3: img = R.drawable.e3;
                break;
            case 4: img = R.drawable.e4;
                break;
            case 5: img = R.drawable.e5;
                break;
            default: img = R.drawable.e4;
                break;
        }

        Picasso.with(contextActivity).load(img).into(imgAhorcado);

    }

    public String getPalabra() {
        return palabra;
    }

    public void setPalabra(String palabra) {
        this.palabra = palabra.trim();
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public int getIntentos() {
        return intentos;
    }

    public void setIntentos(int intentos) {
        this.intentos = intentos;
    }


    public void onBackPressed() {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
    }


    private class CargarAPI extends AsyncTask<String, String, String> {

        protected String respuesta = "";

        protected String doInBackground(String... params) {

            try {
                URL servicioLista = new URL("https://www.serverbpw.com/cm/2019-1/hangman.php");
                InputStream is = servicioLista.openStream();
                if (is != null) {
                    InputStreamReader isr = new InputStreamReader(is);
                    BufferedReader br = new BufferedReader(isr);
                    String line;

                    while ((line = br.readLine()) != null) {
                        respuesta += line;
                    }

                }
            } catch (Exception e) {
                Log.d("ERRORAPI", "No se cargo el servicio.");
                Log.d("ERRORAPI", e.getMessage());
            }

            return respuesta;
        }

        @Override
        protected void onPostExecute(String respuesta) {
            parseAPI(respuesta);
        }

    }

}
