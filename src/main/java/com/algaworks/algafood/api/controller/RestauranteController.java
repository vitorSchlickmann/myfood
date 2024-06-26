package com.algaworks.algafood.api.controller;


import java.nio.file.Path;
import java.util.List;

import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.repository.RestauranteRepository;
import com.algaworks.algafood.domain.service.CadastroRestauranteService;
import org.springframework.beans.BeanUtils;
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

    @Autowired
    private CadastroRestauranteService cadastroRestaurante;

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

            return ResponseEntity.notFound().build();
    }


    @PostMapping
    public ResponseEntity<?> adicionar(@RequestBody Restaurante restaurante) {
        try {
            restaurante = cadastroRestaurante.salvar(restaurante);

            return ResponseEntity.status(HttpStatus.CREATED).body(restaurante);
        } catch (EntidadeNaoEncontradaException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{restauranteId}")
    public ResponseEntity<?> atualizar(@PathVariable Long restauranteId, @RequestBody Restaurante restaurante) {
        Restaurante restauranteAtual = restauranteRepository.buscar(restauranteId);
           try {
               if (restauranteAtual != null) {
                   BeanUtils.copyProperties(restaurante, restauranteAtual, "id");
                   cadastroRestaurante.salvar(restauranteAtual);

                   return ResponseEntity.ok(restauranteAtual);
               }

           } catch (EntidadeNaoEncontradaException e) {
               return ResponseEntity.badRequest().body(e.getMessage());
           }
            return ResponseEntity.notFound().build();
    }
}
