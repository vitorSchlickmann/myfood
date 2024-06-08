package com.algaworks.algafood.api.controller;


import java.util.List;

import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.repository.RestauranteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RestController
@ResponseBody
@RequestMapping(value = "/restaurantes", produces = MediaType.APPLICATION_JSON_VALUE)
public class RestauranteController {

    @Autowired
    private RestauranteRepository restauranteRepository;

    @GetMapping()
    public List<Restaurante> listar() {
        return restauranteRepository.listar();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @GetMapping("/{restaraunteId}")
    public ResponseEntity<Restaurante> buscar(@PathVariable("restauranteId") Long id) {
        Restaurante restaurante = restauranteRepository.buscar(id);
            if (restaurante != null) {
                return ResponseEntity.ok(restaurante);
            }

            return ResponseEntity. notFound().build();
    }
}
