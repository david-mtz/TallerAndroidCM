package com.tallercm.david.ejercicio1.core;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.Arrays;
import java.util.List;

public class Validador extends AppCompatActivity {

    private static Context contexto;

    public static boolean campoNoVacio(ViewGroup layout, String error, String[] excluir) {

        boolean vacios = false;

        List elemetnosExcluidos = Arrays.asList(excluir);

        int campos = layout.getChildCount();
        for(int i=0; i < campos; i++) {
            View vista = layout.getChildAt(i);
            if( vista instanceof ViewGroup) {
                campoNoVacio((ViewGroup) vista, error, excluir);
            } else if( vista instanceof EditText ) {
                EditText campoTexto = (EditText) vista;
                if(! elemetnosExcluidos.contains( campoTexto.getId() ) ) {
                    if( campoTexto.getText().toString().trim().equals("") ) {
                        campoTexto.setError(error);
                        vacios = true;
                    }
                }
            }
        }

        return vacios;
    }

    public static boolean campoNoVacio(ViewGroup vistaGeneral, String error) {
        String vacio[] = {};
        return campoNoVacio(vistaGeneral, error, vacio);
    }

    public static boolean radioNoVacio(RadioGroup layout, String error) {
        boolean vacio = true;
        int radios = layout.getChildCount();

        for(int i=0; i < radios; i++) {
            View vista = layout.getChildAt(i);
            if( vista instanceof RadioButton ) {
                RadioButton radioBtn = (RadioButton) vista;
                if( radioBtn.isChecked() ) {
                    vacio = false;
                    break;
                }
            }
        }

        RadioButton radioBtn = (RadioButton) layout.getChildAt(radios-1);

        if( vacio ) {
            radioBtn.setError(error);
        } else {
            radioBtn.setError(null);
        }

        return vacio;
    }

}
