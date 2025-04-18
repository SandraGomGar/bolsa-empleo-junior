package com.bolsaempleo.controller;

import com.bolsaempleo.dto.RegistroCandidatoRequest;
import com.bolsaempleo.dto.RegistroEmpresaRequest;
import com.bolsaempleo.model.TipoUsuario;
import com.bolsaempleo.model.Usuario;
import com.bolsaempleo.repository.UsuarioRepository;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/auth")

public class AuthController {

    private static final String UPLOAD_DIR = "uploads/";

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @PostMapping("/registro")
    public ResponseEntity<?> registrarUsuario(@RequestBody Usuario usuario) {
        if (usuarioRepository.findByEmail(usuario.getEmail()) != null) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "El email ya está registrado"));
        }

        usuario.setContraseña(passwordEncoder.encode(usuario.getContraseña()));
        Usuario nuevoUsuario = usuarioRepository.save(usuario);
        return ResponseEntity.ok(Map.of("success", true, "usuario", nuevoUsuario));
    }

    @PostMapping("/registro-candidato")
    public ResponseEntity<?> registrarCandidato(@Valid @ModelAttribute RegistroCandidatoRequest request, BindingResult result) {
        // Validaciones manuales
        if (request.getContraseña() == null || request.getContraseña().length() < 6) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "La contraseña debe tener al menos 6 caracteres"));
        }

        if (usuarioRepository.findByEmail(request.getEmail()) != null) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "El email ya está registrado"));
        }

        // Validar archivo
        MultipartFile cvFile = request.getCv();
        String cvFilename = null;
        if (cvFile != null && !cvFile.isEmpty()) {
            String fileType = cvFile.getContentType();
            if (!fileType.equals("application/pdf") && !fileType.equals("application/msword")
                    && !fileType.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document")) {
                return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Formato de archivo no permitido. Solo PDF, DOC o DOCX"));
            }

            if (cvFile.getSize() > 5 * 1024 * 1024) {
                return ResponseEntity.badRequest().body(Map.of("success", false, "message", "El archivo no debe superar los 5MB"));
            }

            try {
                String uniqueFilename = UUID.randomUUID() + "_" + cvFile.getOriginalFilename();
                File uploadFolder = new File(UPLOAD_DIR);
                if (!uploadFolder.exists()) uploadFolder.mkdirs();
                File dest = new File(uploadFolder, uniqueFilename);
                cvFile.transferTo(dest);
                cvFilename = uniqueFilename;
            } catch (IOException e) {
                return ResponseEntity.status(500).body(Map.of("success", false, "message", "Error al guardar el archivo CV"));
            }
        }

        // Crear el candidato
        Usuario candidato = new Usuario();
        candidato.setNombre(request.getNombre() + " " + request.getApellido());
        candidato.setEmail(request.getEmail());
        candidato.setContraseña(passwordEncoder.encode(request.getContraseña()));
        candidato.setTipo(TipoUsuario.CANDIDATO);
        candidato.setCvFilename(cvFilename);

        Usuario nuevoCandidato = usuarioRepository.save(candidato);
        return ResponseEntity.ok(Map.of("success", true, "usuario", nuevoCandidato));
    }

    @PostMapping("/registro-empresa")
    public ResponseEntity<?> registrarEmpresa(@Valid @RequestBody RegistroEmpresaRequest request, BindingResult result) {
        if (result.hasErrors()) {
            List<String> errores = result.getFieldErrors().stream()
                    .map(error -> error.getField() + ": " + error.getDefaultMessage())
                    .toList();
            return ResponseEntity.badRequest().body(Map.of("success", false, "errors", errores));
        }

        if (usuarioRepository.findByEmail(request.getEmail()) != null) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "El email ya está registrado"));
        }

        Usuario empresa = new Usuario();
        empresa.setNombre(request.getNombre());
        empresa.setEmail(request.getEmail());
        empresa.setContraseña(passwordEncoder.encode(request.getContraseña()));
        empresa.setTipo(TipoUsuario.EMPRESA);

        Usuario nuevaEmpresa = usuarioRepository.save(empresa);
        return ResponseEntity.ok(Map.of("success", true, "usuario", nuevaEmpresa));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginData) {
        String email = loginData.get("email");
        String contraseña = loginData.get("contraseña");

        if (email == null || contraseña == null || email.isBlank() || contraseña.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Email y contraseña son obligatorios"));
        }

        Usuario usuario = usuarioRepository.findByEmail(email);
        if (usuario == null || !passwordEncoder.matches(contraseña, usuario.getContraseña())) {
            return ResponseEntity.status(401).body(Map.of("success", false, "message", "Credenciales incorrectas"));
        }

        return ResponseEntity.ok(Map.of("success", true, "usuario", usuario));
    }
}
