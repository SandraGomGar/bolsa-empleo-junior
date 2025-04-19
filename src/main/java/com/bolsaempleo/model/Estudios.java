package com.bolsaempleo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "estudios")
public class Estudios {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nivel;
    private String centro;

    private String fechaInicioMes;
    private String fechaInicioAnio;

    private String fechaFinMes;
    private String fechaFinAnio;

    private boolean cursandoActualmente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")  // 👈 Este nombre es correcto según tu base de datos
    @JsonIgnore
    private Usuario candidato;

    // ----- Getters y Setters -----

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNivel() { return nivel; }
    public void setNivel(String nivel) { this.nivel = nivel; }

    public String getCentro() { return centro; }
    public void setCentro(String centro) { this.centro = centro; }

    public String getFechaInicioMes() { return fechaInicioMes; }
    public void setFechaInicioMes(String fechaInicioMes) { this.fechaInicioMes = fechaInicioMes; }

    public String getFechaInicioAnio() { return fechaInicioAnio; }
    public void setFechaInicioAnio(String fechaInicioAnio) { this.fechaInicioAnio = fechaInicioAnio; }

    public String getFechaFinMes() { return fechaFinMes; }
    public void setFechaFinMes(String fechaFinMes) { this.fechaFinMes = fechaFinMes; }

    public String getFechaFinAnio() { return fechaFinAnio; }
    public void setFechaFinAnio(String fechaFinAnio) { this.fechaFinAnio = fechaFinAnio; }

    public boolean isCursandoActualmente() { return cursandoActualmente; }
    public void setCursandoActualmente(boolean cursandoActualmente) { this.cursandoActualmente = cursandoActualmente; }

    public Usuario getCandidato() { return candidato; }
    public void setCandidato(Usuario candidato) { this.candidato = candidato; }
}
