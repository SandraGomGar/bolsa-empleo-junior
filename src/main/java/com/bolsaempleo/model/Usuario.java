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

    private String telefono;
    private String identificacionFiscal;

    @Column(length = 2000)
    private String descripcion;

    private String fechaNacimiento;
    private String genero;
    private String viveEnEspaña;
    private String codigoPostal;
    private String provincia;
    private String poblacion;
    private String pais;

    @Column(length = 2000)
    private String otrosDatos;

    @OneToMany(mappedBy = "empresa", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Oferta> ofertasPublicadas;

    @OneToMany(mappedBy = "candidato", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Postulacion> postulaciones;

    @OneToMany(mappedBy = "candidato", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("candidato")
    private List<ExperienciaLaboral> experiencias;

    @OneToMany(mappedBy = "candidato", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("candidato")
    private List<Estudios> estudios;

    @OneToMany(mappedBy = "candidato", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("candidato")
    private List<Idioma> idiomas;

    // Getters y Setters

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public TipoUsuario getTipo() { return tipo; }
    public void setTipo(TipoUsuario tipo) { this.tipo = tipo; }

    public String getContraseña() { return contraseña; }
    public void setContraseña(String contraseña) { this.contraseña = contraseña; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getIdentificacionFiscal() { return identificacionFiscal; }
    public void setIdentificacionFiscal(String identificacionFiscal) { this.identificacionFiscal = identificacionFiscal; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(String fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }

    public String getGenero() { return genero; }
    public void setGenero(String genero) { this.genero = genero; }

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

    public String getOtrosDatos() { return otrosDatos; }
    public void setOtrosDatos(String otrosDatos) { this.otrosDatos = otrosDatos; }

    public List<Oferta> getOfertasPublicadas() { return ofertasPublicadas; }
    public void setOfertasPublicadas(List<Oferta> ofertasPublicadas) { this.ofertasPublicadas = ofertasPublicadas; }

    public List<Postulacion> getPostulaciones() { return postulaciones; }
    public void setPostulaciones(List<Postulacion> postulaciones) { this.postulaciones = postulaciones; }

    public List<ExperienciaLaboral> getExperiencias() { return experiencias; }
    public void setExperiencias(List<ExperienciaLaboral> experiencias) { this.experiencias = experiencias; }

    public List<Estudios> getEstudios() { return estudios; }
    public void setEstudios(List<Estudios> estudios) { this.estudios = estudios; }

    public List<Idioma> getIdiomas() { return idiomas; }
    public void setIdiomas(List<Idioma> idiomas) { this.idiomas = idiomas; }
}
