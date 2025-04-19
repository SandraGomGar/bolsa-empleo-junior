package com.bolsaempleo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "experiencia_laboral")
public class ExperienciaLaboral {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String empresa;
    private String puesto;

    @Column(length = 2000)
    private String descripcion;

    private String habilidades; // texto separado por comas

    private String fechaInicioMes;
    private String fechaInicioAnio;
    private String fechaFin; // NUEVO: puedes guardar como "MM/yyyy" o "yyyy" según lo ingreses

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuario candidato;

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getEmpresa() { return empresa; }
    public void setEmpresa(String empresa) { this.empresa = empresa; }

    public String getPuesto() { return puesto; }
    public void setPuesto(String puesto) { this.puesto = puesto; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getHabilidades() { return habilidades; }
    public void setHabilidades(String habilidades) { this.habilidades = habilidades; }

    public String getFechaInicioMes() { return fechaInicioMes; }
    public void setFechaInicioMes(String fechaInicioMes) { this.fechaInicioMes = fechaInicioMes; }

    public String getFechaInicioAnio() { return fechaInicioAnio; }
    public void setFechaInicioAnio(String fechaInicioAnio) { this.fechaInicioAnio = fechaInicioAnio; }

    public String getFechaFin() { return fechaFin; }
    public void setFechaFin(String fechaFin) { this.fechaFin = fechaFin; }

    public Usuario getCandidato() { return candidato; }
    public void setCandidato(Usuario candidato) { this.candidato = candidato; }
}
