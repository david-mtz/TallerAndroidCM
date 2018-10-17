package com.tallercm.david.ejercicio1;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.tallercm.david.ejercicio1.core.Validador;
import com.tallercm.david.ejercicio1.models.Persona;

public class MainActivity extends AppCompatActivity {

    Persona registro_persona;
    EditText txtNombre, txtApellidoPaterno, txtApellidoMaterno, txtCorreoElectronico, txtEdad;
    RadioGroup sexo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtNombre = findViewById(R.id.txtNombre);
        txtApellidoPaterno = findViewById(R.id.txtApellidoPaterno);
        txtApellidoMaterno = findViewById(R.id.txtApellidoMaterno);
        txtCorreoElectronico = findViewById(R.id.txtCorreoElectronico);
        txtEdad = findViewById(R.id.txtEdad);
        Button btnEnviar = findViewById(R.id.btnEnviar);
        String sexo;

        btnEnviar.setOnClickListener(new View.OnClickListener(){

           public void onClick(View v) {
               boolean datosNoValidos = false;
               LinearLayout vistaGeneral = (LinearLayout) findViewById(R.id.layoutMainActivity);
               datosNoValidos = Validador.campoNoVacio(vistaGeneral, getString(R.string.error_campo_vacio));

               RadioGroup radioGroupSexo = findViewById(R.id.radioGroupSexo);
               datosNoValidos = Validador.radioNoVacio(radioGroupSexo, getString(R.string.error_campo_vacio)) || datosNoValidos;

               if(datosNoValidos) {
                   Toast.makeText(getApplicationContext(), R.string.error_formulario, Toast.LENGTH_SHORT).show();
               } else {
                   boolean errores = false;

                   if(! Persona.validarEdad(Integer.parseInt(txtEdad.getText().toString())) ) {
                       txtEdad.setError("No es una edad valida");
                       errores = true;
                   }

                   if(! Persona.validarCorreo(txtCorreoElectronico.getText().toString()) ) {
                       txtCorreoElectronico.setError("No es un correo valido");
                       errores = true;
                   }

                   if(!errores) {
                       Persona persona1 = new Persona(txtNombre.getText().toString(), txtApellidoPaterno.getText().toString(), txtApellidoMaterno.getText().toString(), txtCorreoElectronico.getText().toString(), Integer.parseInt(txtEdad.getText().toString()), (String)((RadioButton)findViewById(radioGroupSexo.getCheckedRadioButtonId())).getText().toString() );
                       Toast.makeText(getApplicationContext(), persona1.toString(), Toast.LENGTH_SHORT).show();
                       Log.i(getString(R.string.debug_info), persona1.toString());
                   }
               }
           }
        });

    }
}
