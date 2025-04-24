package com.bolsaempleo.controller;

import com.bolsaempleo.model.*;
import com.bolsaempleo.repository.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/usuarios")
@CrossOrigin(origins = "*")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ExperienciaLaboralRepository experienciaLaboralRepository;

    @Autowired
    private EstudiosRepository estudiosRepository;

    @Autowired
    private IdiomaRepository idiomaRepository;

    // ✅ Obtener perfil completo de un candidato
    @GetMapping("/candidato/{id}")
    public ResponseEntity<?> obtenerPerfilCandidato(@PathVariable Long id) {
        Optional<Usuario> optionalUsuario = usuarioRepository.findById(id);
        if (optionalUsuario.isEmpty()) return ResponseEntity.notFound().build();

        Usuario usuario = optionalUsuario.get();

        if (usuario.getTipo() == TipoUsuario.CANDIDATO) {
            if (usuario.getExperiencias() != null) usuario.getExperiencias().size();
            if (usuario.getEstudios() != null) usuario.getEstudios().size();
            if (usuario.getIdiomas() != null) usuario.getIdiomas().size();
            return ResponseEntity.ok(usuario);
        } else {
            return ResponseEntity.badRequest().body("El usuario no es un candidato");
        }
    }

    // ✅ Actualizar perfil del candidato
    @PutMapping("/actualizar/{id}")
    public ResponseEntity<?> actualizarPerfilCandidato(@PathVariable Long id, @RequestBody Usuario nuevosDatos) {
        Optional<Usuario> optionalUsuario = usuarioRepository.findById(id);
        if (optionalUsuario.isEmpty()) return ResponseEntity.notFound().build();

        Usuario usuario = optionalUsuario.get();
        if (usuario.getTipo() != TipoUsuario.CANDIDATO)
            return ResponseEntity.badRequest().body("El usuario no es un candidato");

        usuario.setNombre(nuevosDatos.getNombre());
        usuario.setTelefono(nuevosDatos.getTelefono());
        usuario.setProvincia(nuevosDatos.getProvincia());
        usuario.setPoblacion(nuevosDatos.getPoblacion());
        usuario.setViveEnEspaña(nuevosDatos.getViveEnEspaña());
        usuario.setPais(nuevosDatos.getPais());
        usuario.setOtrosDatos(nuevosDatos.getOtrosDatos());

        if (usuario.getIdiomas() != null && !usuario.getIdiomas().isEmpty()) {
            idiomaRepository.deleteAll(usuario.getIdiomas());
        }

        if (nuevosDatos.getIdiomas() != null) {
            for (Idioma idioma : nuevosDatos.getIdiomas()) {
                idioma.setCandidato(usuario);
                idiomaRepository.save(idioma);
            }
        }

        usuarioRepository.save(usuario);
        return ResponseEntity.ok(usuario);
    }

    // 🔹 Añadir nueva experiencia
    @PostMapping("/{id}/experiencia")
    public ResponseEntity<?> añadirExperiencia(@PathVariable Long id, @RequestBody ExperienciaLaboral experiencia) {
        Optional<Usuario> optionalUsuario = usuarioRepository.findById(id);
        if (optionalUsuario.isEmpty()) return ResponseEntity.notFound().build();

        Usuario usuario = optionalUsuario.get();
        if (usuario.getTipo() != TipoUsuario.CANDIDATO)
            return ResponseEntity.badRequest().body("El usuario no es un candidato");

        experiencia.setCandidato(usuario);
        experienciaLaboralRepository.save(experiencia);
        return ResponseEntity.ok(experiencia);
    }

    // 🔹 Actualizar experiencia existente
    @PutMapping("/experiencia/{expId}")
    public ResponseEntity<?> actualizarExperiencia(@PathVariable Long expId, @RequestBody ExperienciaLaboral experienciaActualizada) {
        Optional<ExperienciaLaboral> optionalExp = experienciaLaboralRepository.findById(expId);
        if (optionalExp.isEmpty()) return ResponseEntity.notFound().build();

        ExperienciaLaboral experiencia = optionalExp.get();
        experiencia.setEmpresa(experienciaActualizada.getEmpresa());
        experiencia.setPuesto(experienciaActualizada.getPuesto());
        experiencia.setDescripcion(experienciaActualizada.getDescripcion());
        experiencia.setHabilidades(experienciaActualizada.getHabilidades());
        experiencia.setFechaInicioMes(experienciaActualizada.getFechaInicioMes());
        experiencia.setFechaInicioAnio(experienciaActualizada.getFechaInicioAnio());
        experiencia.setFechaFin(experienciaActualizada.getFechaFin());

        experienciaLaboralRepository.save(experiencia);
        return ResponseEntity.ok(experiencia);
    }

    // 🔹 Eliminar experiencia
    @DeleteMapping("/experiencia/{expId}")
    public ResponseEntity<?> eliminarExperiencia(@PathVariable Long expId) {
        if (!experienciaLaboralRepository.existsById(expId))
            return ResponseEntity.notFound().build();

        experienciaLaboralRepository.deleteById(expId);
        return ResponseEntity.ok().build();
    }

    // 🔹 Añadir nuevo estudio
    @PostMapping("/{id}/estudio")
    public ResponseEntity<?> añadirEstudio(@PathVariable Long id, @RequestBody Estudios estudio) {
        Optional<Usuario> optionalUsuario = usuarioRepository.findById(id);
        if (optionalUsuario.isEmpty()) return ResponseEntity.notFound().build();

        Usuario usuario = optionalUsuario.get();
        if (usuario.getTipo() != TipoUsuario.CANDIDATO)
            return ResponseEntity.badRequest().body("El usuario no es un candidato");

        estudio.setCandidato(usuario);
        estudiosRepository.save(estudio);
        return ResponseEntity.ok(estudio);
    }

    // 🔹 Actualizar estudio existente
    @PutMapping("/estudio/{estudioId}")
    public ResponseEntity<?> actualizarEstudio(@PathVariable Long estudioId, @RequestBody Estudios estudioActualizado) {
        Optional<Estudios> optionalEst = estudiosRepository.findById(estudioId);
        if (optionalEst.isEmpty()) return ResponseEntity.notFound().build();

        Estudios estudio = optionalEst.get();
        estudio.setNivel(estudioActualizado.getNivel());
        estudio.setCentro(estudioActualizado.getCentro());
        estudio.setFechaInicioMes(estudioActualizado.getFechaInicioMes());
        estudio.setFechaInicioAnio(estudioActualizado.getFechaInicioAnio());
        estudio.setFechaFinMes(estudioActualizado.getFechaFinMes());
        estudio.setFechaFinAnio(estudioActualizado.getFechaFinAnio());
        estudio.setCursandoActualmente(estudioActualizado.isCursandoActualmente());
        estudiosRepository.save(estudio);
        return ResponseEntity.ok(estudio);
    }

    // 🔹 Eliminar estudio
    @DeleteMapping("/estudio/{estudioId}")
    public ResponseEntity<?> eliminarEstudio(@PathVariable Long estudioId) {
        if (!estudiosRepository.existsById(estudioId))
            return ResponseEntity.notFound().build();

        estudiosRepository.deleteById(estudioId);
        return ResponseEntity.ok().build();
    }
}
