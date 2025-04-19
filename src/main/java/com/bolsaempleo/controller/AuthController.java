package com.bolsaempleo.controller;

import com.bolsaempleo.dto.RegistroCandidatoRequest;
import com.bolsaempleo.model.*;
import com.bolsaempleo.repository.*;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private static final String UPLOAD_DIR = "uploads/";

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ExperienciaLaboralRepository experienciaLaboralRepository;

    @Autowired
    private EstudiosRepository estudiosRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    // ✅ REGISTRO CANDIDATO
    @PostMapping("/registro-candidato")
    public ResponseEntity<?> registrarCandidato(@Valid @RequestBody RegistroCandidatoRequest request, BindingResult result) {
        if (usuarioRepository.findByEmail(request.getEmail()) != null) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "El email ya está registrado"));
        }

 
        Usuario candidato = new Usuario();
        candidato.setNombre(request.getNombre() + " " + request.getApellido());
        candidato.setEmail(request.getEmail());
        candidato.setContraseña(passwordEncoder.encode(request.getContraseña()));
        candidato.setTipo(TipoUsuario.CANDIDATO);
        candidato.setTelefono(request.getTelefono());
        candidato.setFechaNacimiento(request.getFechaNacimiento());
        candidato.setGenero(request.getGenero());
        candidato.setViveEnEspaña(request.getViveEnEspaña());
        candidato.setCodigoPostal(request.getCodigoPostal());
        candidato.setProvincia(request.getProvincia());
        candidato.setPoblacion(request.getPoblacion());
        candidato.setPais(request.getPais());

        Usuario nuevoCandidato = usuarioRepository.save(candidato);

        // ✅ Guardar experiencia
        if ("sí".equalsIgnoreCase(request.getTieneExperiencia()) && request.getEmpresa() != null) {
            ExperienciaLaboral experiencia = new ExperienciaLaboral();
            experiencia.setEmpresa(request.getEmpresa());
            experiencia.setPuesto(request.getPuesto());
            experiencia.setFechaInicioMes(request.getFechaInicioMes());
            experiencia.setFechaInicioAnio(request.getFechaInicioAnio());
            experiencia.setFechaFin(request.getFechaFin());
            experiencia.setDescripcion(request.getDescripcionExperiencia());
            experiencia.setHabilidades(request.getHabilidades());
            experiencia.setCandidato(nuevoCandidato);
            experienciaLaboralRepository.save(experiencia);
        }

        // ✅ Guardar estudios
        if ("sí".equalsIgnoreCase(request.getTieneEstudios()) && request.getEstudiosNivel() != null) {
            Estudios estudios = new Estudios();
            estudios.setNivel(request.getEstudiosNivel());
            estudios.setCentro(request.getEstudiosCentro());
            estudios.setFechaInicioMes(request.getEstudiosFechaInicioMes());
            estudios.setFechaInicioAnio(request.getEstudiosFechaInicioAnio());
            estudios.setFechaFinMes(request.getEstudiosFechaFinMes());
            estudios.setFechaFinAnio(request.getEstudiosFechaFinAnio());
            estudios.setCursandoActualmente(request.isEstudiosCursandoActualmente());
            estudios.setCandidato(nuevoCandidato);
            estudiosRepository.save(estudios);
        }

        return ResponseEntity.ok(Map.of("success", true, "usuario", nuevoCandidato));
    }

    // ✅ LOGIN USUARIO
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginData) {
        String email = loginData.get("email");
        String contraseña = loginData.get("contraseña");

        Usuario usuario = usuarioRepository.findByEmail(email);
        if (usuario == null || !passwordEncoder.matches(contraseña, usuario.getContraseña())) {
            return ResponseEntity.status(401).body(Map.of("success", false, "message", "Credenciales incorrectas"));
        }

        return ResponseEntity.ok(Map.of("success", true, "usuario", usuario));
    }

    // ✅ PERFIL CANDIDATO
    @GetMapping("/candidato/perfil/{id}")
    public ResponseEntity<?> obtenerPerfilCandidato(@PathVariable Long id) {
        Optional<Usuario> candidatoOpt = usuarioRepository.findById(id);

        if (candidatoOpt.isEmpty()) {
            return ResponseEntity.status(404).body(Map.of("success", false, "message", "Candidato no encontrado"));
        }

        Usuario candidato = candidatoOpt.get();

        if (candidato.getTipo() != TipoUsuario.CANDIDATO) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Este usuario no es un candidato"));
        }

        // Forzar carga de relaciones
        candidato.getExperiencias().size();
        candidato.getEstudios().size();

        return ResponseEntity.ok(Map.of("success", true, "perfil", candidato));
    }
}
