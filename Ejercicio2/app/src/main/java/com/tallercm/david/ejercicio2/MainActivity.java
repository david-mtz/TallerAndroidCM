package com.tallercm.david.ejercicio2;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ExamplePListParser mPListParser = null;
    int ancho, altura, columnas = 2;
    Context contextActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        contextActivity = this;
        // Calculo de espacio
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        ancho = size.x;
        altura = size.y;

        CargarAPI cargarElementos = new CargarAPI();
        cargarElementos.execute();


    }

    public void setGridView(String respuestaAPI) {
        InputStream is2 = new ByteArrayInputStream(respuestaAPI.getBytes());
        mPListParser = new ExamplePListParser(is2);
        final ArrayList<ArrayList<Object>> productos = new ArrayList<ArrayList<Object>>();

        int i = 1;

        while (mPListParser.getProducto(i) != null) {
            productos.add((ArrayList<Object>) mPListParser.getProducto(i));
            i++;
        }

        GridView gridView = findViewById(R.id.gVProductos);
        gridView.setNumColumns(this.columnas);
        ImageAdapter adapter = new ImageAdapter(this.contextActivity);
        for (int j = 0; j < productos.size(); j++) {
            adapter.setThumb(productos.get(j).get(0).toString());
        }
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ArrayList<Object> producto = (ArrayList<Object>) productos.get(position);
                Intent intent = new Intent(MainActivity.this, Main2Activity.class);
                intent.putExtra("id", producto.get(0).toString());
                intent.putExtra("nombre", producto.get(1).toString());
                intent.putExtra("descripcion", producto.get(2).toString());
                startActivity(intent);
            }
        });

    }

    public class ImageAdapter extends BaseAdapter {

        Context context;

        private ArrayList<String> thumbs = new ArrayList<String>();

        public void setThumb(String thumb) {
            this.thumbs.add(thumb);
        }

        public ImageAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return thumbs.size();
        }

        @Override
        public String getItem(int position) {
            return this.thumbs.get(position);
        }

        public long getItemId(int position) {
            return Long.parseLong(this.thumbs.get(position));
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView = new ImageView(context);
            imageView.setLayoutParams(new GridView.LayoutParams(ancho/columnas-20,ancho/columnas-20));
            imageView.setPadding(15,15,15,15);
            Picasso.get()
                    .load("https://www.serverbpw.com/cm/2019-1/" + this.thumbs.get(position) + ".png")
                    .into(imageView);
            return imageView;
        }
    }

    private class CargarAPI extends AsyncTask<String, String, String> {

        protected String respuesta = "";

        protected String doInBackground(String... params) {

            try {
                URL servicioLista = new URL("https://www.serverbpw.com/cm/2019-1/products.php");
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
                Log.d("ERROR", "No se cargo el servicio.");
            }

            return respuesta;
        }

        @Override
        protected void onPostExecute(String respuesta) {
            setGridView(respuesta);
        }

    }


}
