package com.bolsaempleo.model;

import jakarta.persistence.*;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "postulaciones")
public class Postulacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "candidato_id", nullable = false)
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	private Usuario candidato;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "oferta_id", nullable = false)
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	private Oferta oferta;

    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Usuario getCandidato() {
		return candidato;
	}

	public void setCandidato(Usuario candidato) {
		this.candidato = candidato;
	}

	public Oferta getOferta() {
		return oferta;
	}

	public void setOferta(Oferta oferta) {
		this.oferta = oferta;
	}

	public LocalDate getFechaPostulacion() {
		return fechaPostulacion;
	}

	public void setFechaPostulacion(LocalDate fechaPostulacion) {
		this.fechaPostulacion = fechaPostulacion;
	}


    @Column(name = "fecha_postulacion", nullable = false)
    private LocalDate fechaPostulacion = LocalDate.now();

    // Getters y Setters (generar automáticamente)
}