package com.bolsaempleo.controller;

import com.bolsaempleo.model.Oferta;
import com.bolsaempleo.model.TipoUsuario;
import com.bolsaempleo.repository.OfertaRepository;
import com.bolsaempleo.repository.UsuarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;

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

	// Endpoint GET: Obtener todas las ofertas
	@GetMapping
	public List<Oferta> listarOfertas() {
		System.out.println(" GET Listando ofertas");
		return ofertaRepository.findAll();
	}

	// Endpoint POST: Crear una nueva oferta
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

	@GetMapping("/{id}")
	public ResponseEntity<Oferta> obtenerOferta(@PathVariable Long id) {
		return ofertaRepository.findById(id).map(oferta -> ResponseEntity.ok(oferta))
				.orElse(ResponseEntity.notFound().build());
	}

	// Actualizar una oferta
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
			// Verificamos que el usuario sea el creador original
			if (!oferta.getEmpresa().getId().equals(usuario.getId())) {
				return ResponseEntity.status(403)
						.body(Map.of("success", false, "message", "No tienes permiso para editar esta oferta"));
			}

			oferta.setTitulo(ofertaActualizada.getTitulo());
			oferta.setDescripcion(ofertaActualizada.getDescripcion());
			oferta.setUbicacion(ofertaActualizada.getUbicacion());
			// La fecha no se actualiza, mantenemos la original
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

			ofertaRepository.delete(oferta);
			return ResponseEntity.ok().body(Map.of("success", true, "message", "Oferta eliminada correctamente"));
		}).orElse(ResponseEntity.status(404).body(Map.of("success", false, "message", "La oferta no existe")));
	}

}