package com.bolsaempleo.dto;

import jakarta.validation.constraints.*;

public class RegistroCandidatoRequest {

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    private String apellido;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email debe tener un formato válido")
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    private String contraseña;

    @NotBlank(message = "La fecha de nacimiento es obligatoria")
    private String fechaNacimiento;

    private String genero;

    @NotBlank(message = "El teléfono es obligatorio")
    @Pattern(regexp = "^\\d{9}$", message = "El teléfono debe tener 9 dígitos")
    private String telefono;

    @NotBlank(message = "Debes indicar si vives en España")
    private String viveEnEspaña;

    private String codigoPostal;
    private String provincia;
    private String poblacion;
    private String pais;

    // ---------- EXPERIENCIA ----------
    private String tieneExperiencia;
    private String empresa;
    private String puesto;
    private String descripcionExperiencia;
    private String habilidades;
    private String fechaInicioMes;
    private String fechaInicioAnio;
    private String fechaFin;

    // ---------- ESTUDIOS ----------
    private String tieneEstudios;
    private String estudiosNivel;
    private String estudiosCentro;
    private String estudiosFechaInicioMes;
    private String estudiosFechaInicioAnio;
    private String estudiosFechaFinMes;
    private String estudiosFechaFinAnio;
    private boolean estudiosCursandoActualmente;

    private String tipo;

    // ---------- GETTERS Y SETTERS ----------

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getContraseña() { return contraseña; }
    public void setContraseña(String contraseña) { this.contraseña = contraseña; }

    public String getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(String fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }

    public String getGenero() { return genero; }
    public void setGenero(String genero) { this.genero = genero; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getViveEnEspaña() { return viveEnEspaña; }
    public void setViveEnEspaña(String viveEnEspaña) { this.viveEnEspaña = viveEnEspaña; }

    public String getCodigoPostal() { return codigoPostal; }
    public void setCodigoPostal(String codigoPostal) { this.codigoPostal = codigoPostal; }

    public String getProvincia() { return provincia; }
    public void setProvincia(String provincia) { this.provincia = provincia; }

    public String getPoblacion() { return poblacion; }
    public void setPoblacion(String poblacion) { this.poblacion = poblacion; }

    public String getPais() { return pais; }
    public void setPais(String pais) { this.pais = pais; }

    public String getTieneExperiencia() { return tieneExperiencia; }
    public void setTieneExperiencia(String tieneExperiencia) { this.tieneExperiencia = tieneExperiencia; }

    public String getEmpresa() { return empresa; }
    public void setEmpresa(String empresa) { this.empresa = empresa; }

    public String getPuesto() { return puesto; }
    public void setPuesto(String puesto) { this.puesto = puesto; }

    public String getDescripcionExperiencia() { return descripcionExperiencia; }
    public void setDescripcionExperiencia(String descripcionExperiencia) { this.descripcionExperiencia = descripcionExperiencia; }

    public String getHabilidades() { return habilidades; }
    public void setHabilidades(String habilidades) { this.habilidades = habilidades; }

    public String getFechaInicioMes() { return fechaInicioMes; }
    public void setFechaInicioMes(String fechaInicioMes) { this.fechaInicioMes = fechaInicioMes; }

    public String getFechaInicioAnio() { return fechaInicioAnio; }
    public void setFechaInicioAnio(String fechaInicioAnio) { this.fechaInicioAnio = fechaInicioAnio; }

    public String getFechaFin() { return fechaFin; }
    public void setFechaFin(String fechaFin) { this.fechaFin = fechaFin; }

    public String getTieneEstudios() { return tieneEstudios; }
    public void setTieneEstudios(String tieneEstudios) { this.tieneEstudios = tieneEstudios; }

    public String getEstudiosNivel() { return estudiosNivel; }
    public void setEstudiosNivel(String estudiosNivel) { this.estudiosNivel = estudiosNivel; }

    public String getEstudiosCentro() { return estudiosCentro; }
    public void setEstudiosCentro(String estudiosCentro) { this.estudiosCentro = estudiosCentro; }

    public String getEstudiosFechaInicioMes() { return estudiosFechaInicioMes; }
    public void setEstudiosFechaInicioMes(String estudiosFechaInicioMes) { this.estudiosFechaInicioMes = estudiosFechaInicioMes; }

    public String getEstudiosFechaInicioAnio() { return estudiosFechaInicioAnio; }
    public void setEstudiosFechaInicioAnio(String estudiosFechaInicioAnio) { this.estudiosFechaInicioAnio = estudiosFechaInicioAnio; }

    public String getEstudiosFechaFinMes() { return estudiosFechaFinMes; }
    public void setEstudiosFechaFinMes(String estudiosFechaFinMes) { this.estudiosFechaFinMes = estudiosFechaFinMes; }

    public String getEstudiosFechaFinAnio() { return estudiosFechaFinAnio; }
    public void setEstudiosFechaFinAnio(String estudiosFechaFinAnio) { this.estudiosFechaFinAnio = estudiosFechaFinAnio; }

    public boolean isEstudiosCursandoActualmente() { return estudiosCursandoActualmente; }
    public void setEstudiosCursandoActualmente(boolean estudiosCursandoActualmente) { this.estudiosCursandoActualmente = estudiosCursandoActualmente; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
} 
