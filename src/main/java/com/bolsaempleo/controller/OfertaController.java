package com.bolsaempleo.controller;

import com.bolsaempleo.model.Oferta;
import com.bolsaempleo.model.Postulacion;
import com.bolsaempleo.model.TipoUsuario;
import com.bolsaempleo.repository.OfertaRepository;
import com.bolsaempleo.repository.PostulacionRepository;
import com.bolsaempleo.repository.UsuarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ofertas")
@CrossOrigin(origins = "*")
public class OfertaController {

    @Autowired
    private OfertaRepository ofertaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PostulacionRepository postulacionRepository;

    // Obtener todas las ofertas
    @GetMapping
    public List<Oferta> listarOfertas() {
        return ofertaRepository.findAll();
    }

    // Crear nueva oferta
    @PostMapping
    public ResponseEntity<?> crearOferta(@RequestParam Long usuarioId, @RequestBody Oferta nuevaOferta) {
        var usuarioOpt = usuarioRepository.findById(usuarioId);

        if (usuarioOpt.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Usuario no encontrado"));
        }

        var usuario = usuarioOpt.get();

        if (usuario.getTipo() != TipoUsuario.EMPRESA) {
            return ResponseEntity.status(403)
                    .body(Map.of("success", false, "message", "Solo las empresas pueden crear ofertas"));
        }

        nuevaOferta.setFechaPublicacion(LocalDate.now().toString());
        nuevaOferta.setEmpresa(usuario);

        return ResponseEntity.ok(ofertaRepository.save(nuevaOferta));
    }

    // Obtener una oferta por ID
    @GetMapping("/{id}")
    public ResponseEntity<Oferta> obtenerOferta(@PathVariable Long id) {
        return ofertaRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Actualizar oferta
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarOferta(@PathVariable Long id, @RequestBody Oferta ofertaActualizada,
                                              @RequestParam Long usuarioId) {
        var usuarioOpt = usuarioRepository.findById(usuarioId);
        if (usuarioOpt.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Usuario no encontrado"));
        }

        var usuario = usuarioOpt.get();

        if (usuario.getTipo() != TipoUsuario.EMPRESA) {
            return ResponseEntity.status(403)
                    .body(Map.of("success", false, "message", "Solo las empresas pueden editar ofertas"));
        }

        return ofertaRepository.findById(id).map(oferta -> {
            if (!oferta.getEmpresa().getId().equals(usuario.getId())) {
                return ResponseEntity.status(403)
                        .body(Map.of("success", false, "message", "No tienes permiso para editar esta oferta"));
            }

            oferta.setTitulo(ofertaActualizada.getTitulo());
            oferta.setUbicacion(ofertaActualizada.getUbicacion());
            oferta.setVacantes(ofertaActualizada.getVacantes());
            oferta.setFunciones(ofertaActualizada.getFunciones());
            oferta.setRequisitos(ofertaActualizada.getRequisitos());
            oferta.setTipoContrato(ofertaActualizada.getTipoContrato());
            oferta.setSueldo(ofertaActualizada.getSueldo());
            oferta.setModalidad(ofertaActualizada.getModalidad());

            return ResponseEntity.ok(Map.of("success", true, "oferta", ofertaRepository.save(oferta)));
        }).orElse(ResponseEntity.status(404).body(Map.of("success", false, "message", "La oferta no existe")));
    }

    // Eliminar una oferta
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarOferta(@PathVariable Long id, @RequestParam Long usuarioId) {
        var usuarioOpt = usuarioRepository.findById(usuarioId);
        if (usuarioOpt.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Usuario no encontrado"));
        }

        var usuario = usuarioOpt.get();

        if (usuario.getTipo() != TipoUsuario.EMPRESA) {
            return ResponseEntity.status(403)
                    .body(Map.of("success", false, "message", "Solo las empresas pueden eliminar ofertas"));
        }

        return ofertaRepository.findById(id).map(oferta -> {
            if (!oferta.getEmpresa().getId().equals(usuario.getId())) {
                return ResponseEntity.status(403)
                        .body(Map.of("success", false, "message", "No puedes eliminar una oferta que no es tuya"));
            }

            // ✅ Eliminar postulaciones asociadas antes de eliminar la oferta
            List<Postulacion> postulaciones = postulacionRepository.findByOferta(oferta);
            postulacionRepository.deleteAll(postulaciones);

            ofertaRepository.delete(oferta);
            return ResponseEntity.ok(Map.of("success", true, "message", "Oferta eliminada correctamente"));
        }).orElse(ResponseEntity.status(404).body(Map.of("success", false, "message", "La oferta no existe")));
    }

    // Obtener todas las ofertas creadas por una empresa
    @GetMapping("/empresa/{empresaId}")
    public ResponseEntity<?> listarOfertasPorEmpresa(@PathVariable Long empresaId) {
        var usuarioOpt = usuarioRepository.findById(empresaId);

        if (usuarioOpt.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Empresa no encontrada"));
        }

        var ofertas = ofertaRepository.findByEmpresaId(empresaId);
        return ResponseEntity.ok(ofertas);
    }
}
