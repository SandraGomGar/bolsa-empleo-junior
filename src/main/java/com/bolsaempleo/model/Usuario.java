package com.bolsaempleo.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.persistence.*;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity 
@Table(name = "usuarios") 
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email debe tener un formato válido")
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoUsuario tipo;
    
    @NotBlank(message = "La contraseña es obligatoria")
    @Column(nullable = false)
    private String contraseña;


    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public TipoUsuario getTipo() {
		return tipo;
	}

	public void setTipo(TipoUsuario tipo) {
		this.tipo = tipo;
	}

	public List<Oferta> getOfertasPublicadas() {
		return ofertasPublicadas;
	}

	public void setOfertasPublicadas(List<Oferta> ofertasPublicadas) {
		this.ofertasPublicadas = ofertasPublicadas;
	}
	
	public String getContraseña() {
	    return contraseña;
	}

	public void setContraseña(String contraseña) {
	    this.contraseña = contraseña;
	}


	@OneToMany(mappedBy = "empresa", cascade = CascadeType.ALL)
	@JsonIgnore
    private List<Oferta> ofertasPublicadas;
	
	@OneToMany(mappedBy = "candidato", cascade = CascadeType.ALL)
	@JsonIgnore
	private List<Postulacion> postulaciones;


  
}