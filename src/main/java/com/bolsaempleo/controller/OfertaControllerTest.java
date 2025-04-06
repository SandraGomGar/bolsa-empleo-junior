package com.bolsaempleo.controller;

import com.bolsaempleo.model.Oferta;
import com.bolsaempleo.repository.OfertaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
public class OfertaControllerTest {

    @Autowired
    private OfertaRepository ofertaRepository;

    @GetMapping("/test-ofertas")
    public List<Oferta> listarTodasOfertas() {
        return ofertaRepository.findAll();
    }
}