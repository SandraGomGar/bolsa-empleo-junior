package com.bolsaempleo.controller;

import com.bolsaempleo.model.Usuario;
import com.bolsaempleo.repository.UsuarioRepository;

import jakarta.validation.Valid;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@PostMapping("/registro")
	public ResponseEntity<?> registrarUsuario(@Valid @RequestBody Usuario usuario, BindingResult result) {

		if (result.hasErrors()) {
			// Recoger los mensajes de error y devolverlos en la respuesta
			List<String> errores = result.getFieldErrors().stream()
					.map(error -> error.getField() + ": " + error.getDefaultMessage()).toList();

			return ResponseEntity.badRequest().body(Map.of("success", false, "errors", errores));
		}

		// Validar si el email ya existe
		if (usuarioRepository.findByEmail(usuario.getEmail()) != null) {
			return ResponseEntity.badRequest().body(Map.of("success", false, "message", "El email ya está registrado"));
		}

		// Cifrar contraseña
		String passCifrada = passwordEncoder.encode(usuario.getContraseña());
		usuario.setContraseña(passCifrada);

		Usuario nuevoUsuario = usuarioRepository.save(usuario);
		return ResponseEntity.ok(Map.of("success", true, "usuario", nuevoUsuario));
	} 

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody Map<String, String> loginData) {
		String email = loginData.get("email");
		String contraseña = loginData.get("contraseña");

		if (email == null || contraseña == null || email.isBlank() || contraseña.isBlank()) {
			return ResponseEntity.badRequest()
					.body(Map.of("success", false, "message", "Email y contraseña son obligatorios"));
		}

		Usuario usuario = usuarioRepository.findByEmail(email);

		if (usuario == null || !passwordEncoder.matches(contraseña, usuario.getContraseña())) {
			return ResponseEntity.status(401).body(Map.of("success", false, "message", "Credenciales incorrectas"));
		}

		return ResponseEntity.ok(Map.of("success", true, "usuario", usuario));
	}

}