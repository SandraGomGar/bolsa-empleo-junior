package com.bolsaempleo.controller;

import com.bolsaempleo.model.Postulacion;
import com.bolsaempleo.model.TipoUsuario;
import com.bolsaempleo.repository.PostulacionRepository;
import com.bolsaempleo.repository.UsuarioRepository;
import com.bolsaempleo.repository.OfertaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/postulaciones")
@CrossOrigin(origins = "*")
public class PostulacionController {

    @Autowired
    private PostulacionRepository postulacionRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private OfertaRepository ofertaRepository;

    // 1. Crear una nueva postulación
    @PostMapping
    public ResponseEntity<?> crearPostulacion(@RequestParam Long candidatoId, @RequestParam Long ofertaId) {
        var candidatoOpt = usuarioRepository.findById(candidatoId);
        var ofertaOpt = ofertaRepository.findById(ofertaId);

        if (candidatoOpt.isEmpty() || ofertaOpt.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Usuario o oferta no encontrados"));
        }

        var candidato = candidatoOpt.get();
        var oferta = ofertaOpt.get();

        // 🔒 Validar tipo de usuario
        if (candidato.getTipo() != TipoUsuario.CANDIDATO) {
            return ResponseEntity.status(403).body(Map.of("success", false, "message", "Solo los candidatos pueden postularse"));
        }

        // 🛑 Validación: ya existe postulación
        if (postulacionRepository.existsByCandidatoAndOferta(candidato, oferta)) {
            return ResponseEntity.badRequest().body(
                Map.of("success", false, "message", "Ya te has postulado a esta oferta")
            );
        }

        // ✅ Crear la postulación
        Postulacion nueva = new Postulacion();
        nueva.setCandidato(candidato);
        nueva.setOferta(oferta);

        return ResponseEntity.ok(postulacionRepository.save(nueva));
    }

    // 2. Listar todas las postulaciones (opcional)
    @GetMapping
    public List<Postulacion> listarTodas() {
        return postulacionRepository.findAll();
    }

    // 3. Obtener postulaciones por ID de candidato
    @GetMapping("/candidato/{id}")
    public List<Postulacion> porCandidato(@PathVariable Long id) {
        return postulacionRepository.findAll().stream()
                .filter(p -> p.getCandidato().getId().equals(id))
                .toList();
    }

    // 4. Obtener postulaciones por ID de oferta
    @GetMapping("/oferta/{id}")
    public List<Postulacion> porOferta(@PathVariable Long id) {
        return postulacionRepository.findAll().stream()
                .filter(p -> p.getOferta().getId().equals(id))
                .toList();
    }

    @GetMapping("/candidato/email/{email}")
    public List<Postulacion> porEmailCandidato(@PathVariable String email) {
        return postulacionRepository.findByCandidatoEmail(email);
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<?> obtenerPostulacionesUsuario(@PathVariable Long usuarioId) {
        var usuarioOpt = usuarioRepository.findById(usuarioId);

        if (usuarioOpt.isEmpty()) {
            return ResponseEntity.status(404).body(Map.of("success", false, "message", "Usuario no encontrado"));
        }

        var usuario = usuarioOpt.get();

        if (usuario.getTipo() == TipoUsuario.CANDIDATO) {
            List<Postulacion> postulaciones = postulacionRepository.findByCandidato(usuario);
            return ResponseEntity.ok(postulaciones);
        } else if (usuario.getTipo() == TipoUsuario.EMPRESA) {
            List<Postulacion> postulaciones = postulacionRepository.findAll().stream()
                .filter(p -> p.getOferta().getEmpresa().equals(usuario.getNombre()))
                .toList();
            return ResponseEntity.ok(postulaciones);
        } else {
            return ResponseEntity.status(403).body(Map.of("success", false, "message", "Tipo de usuario no autorizado"));
        }
    }

    // ✅ PATCH para actualizar estado de postulación
    @PatchMapping("/{id}/estado")
    public ResponseEntity<?> actualizarEstadoPostulacion(
            @PathVariable Long id,
            @RequestParam("estado") String estado) {

        Optional<Postulacion> optional = postulacionRepository.findById(id);
        if (optional.isEmpty()) {
            return ResponseEntity.status(404).body(Map.of("success", false, "message", "Postulación no encontrada"));
        }

        Postulacion postulacion = optional.get();
        try {
            postulacion.setEstado(Enum.valueOf(Postulacion.EstadoPostulacion.class, estado.toUpperCase()));
            postulacionRepository.save(postulacion);
            return ResponseEntity.ok(Map.of("success", true, "message", "Estado actualizado"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Estado inválido"));
        }
    }
}
