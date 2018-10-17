package com.tallercm.david.ejercicio1.models;

import java.util.regex.Pattern;

/**
 * Created by David Arturo on 15/10/2018.
 */

public class Persona {

    private String nombre;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String correoElectronico;
    private Integer edad;
    private String sexo;

    public Persona(String nombre, String apellidoPaterno, String apellidoMaterno, String correoElectronico, Integer edad, String sexo) {
        this.nombre = nombre;
        this.apellidoPaterno = apellidoPaterno;
        this.apellidoMaterno = apellidoMaterno;
        this.correoElectronico = correoElectronico;
        this.edad = edad;
        this.sexo = sexo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidoPaterno() {
        return apellidoPaterno;
    }

    public void setApellidoPaterno(String apellidoPaterno) {
        this.apellidoPaterno = apellidoPaterno;
    }

    public String getApellidoMaterno() {
        return apellidoMaterno;
    }

    public void setApellidoMaterno(String apellidoMaterno) {
        this.apellidoMaterno = apellidoMaterno;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    public Integer getEdad() {
        return edad;
    }

    public void setEdad(Integer edad) {
        this.edad = edad;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public static boolean validarEdad(int edad) {
        return edad > 0 && edad < 120;
    }

    public static boolean validarCorreo(String correo) {
        return Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE).matcher(correo).find();

    }

    public String toString() {
        return "Nombre: " + getNombre() +
                "\nApellido Paterno:" + getApellidoPaterno() +
                "\nApellido Materno:" + getApellidoMaterno() +
                "\nCorreo electronico: " + getCorreoElectronico() +
                "\nEdad: " + getEdad() +
                "\nSexo: " + getSexo();
    }

}
