package com.algaworks.algafood.api.controller;

import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.repository.EstadoRepository;
import com.algaworks.algafood.domain.model.Estado;
import com.algaworks.algafood.domain.service.CadastroEstadoService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@ResponseBody
@RestController
@RequestMapping(value = "/estados", produces = MediaType.APPLICATION_JSON_VALUE)
public class EstadoController {

    @Autowired
    private EstadoRepository estadoRepository;

    @Autowired
    private CadastroEstadoService cadastroEstado;

    @GetMapping
    public List<Estado> listar(){
        return estadoRepository.listar();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @GetMapping("/{estadoId}")
    public ResponseEntity<Estado> buscar(@PathVariable("estadoId") Long id) {
        Estado estado = estadoRepository.buscar(id);
            if (estado != null) {
                return ResponseEntity.ok(estado);
            }

            return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<?> adicionar(@RequestBody Estado estado) {
        try {
            estado = cadastroEstado.salvar(estado);

            return ResponseEntity.status(HttpStatus.CREATED).body(estado);
        } catch (EntidadeNaoEncontradaException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{estadoId}")
    public ResponseEntity<?> atualizar(@PathVariable Long estadoId, @RequestBody Estado estado) {
        Estado estadoAtual = estadoRepository.buscar(estadoId);

        try {
            if (estadoAtual != null) {
                BeanUtils.copyProperties(estado, estadoAtual, "id");
                cadastroEstado.salvar(estadoAtual);

                return ResponseEntity.ok(estadoAtual);
            }
        } catch (EntidadeNaoEncontradaException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{estadoId}")
    public ResponseEntity<?> remover(@PathVariable Long estadoId) {
        try {
            cadastroEstado.excluir(estadoId);

            return ResponseEntity.noContent().build();

        } catch (EntidadeNaoEncontradaException e){
            return ResponseEntity.notFound().build();

        } catch (EntidadeEmUsoException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

}
