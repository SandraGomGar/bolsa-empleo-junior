package com.bolsaempleo.repository;

import com.bolsaempleo.model.Oferta;
import com.bolsaempleo.model.Postulacion;
import com.bolsaempleo.model.Usuario;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface PostulacionRepository extends JpaRepository<Postulacion, Long> {
	
    // Buscar postulaciones por candidato (usuario)
    List<Postulacion> findByCandidato(Usuario candidato);

    // Buscar postulaciones por oferta
    List<Postulacion> findByOferta(Oferta oferta);

    // Evitar duplicados: comprobar si ya existe una postulación para un usuario y oferta
    boolean existsByCandidatoAndOferta(Usuario candidato, Oferta oferta);
    
    List<Postulacion> findByCandidatoEmail(String email);
	List<Postulacion> findByOfertaId(Long ofertaId);
	

}
