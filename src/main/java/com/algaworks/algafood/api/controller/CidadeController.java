package com.algaworks.algafood.api.controller;

import java.util.List;

import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.model.Cidade;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.repository.CidadeRepository;
import com.algaworks.algafood.domain.service.CadastroCidadeService;
import org.apache.coyote.Response;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@Controller
@ResponseBody
@RequestMapping(value = "/cidades", produces = MediaType.APPLICATION_JSON_VALUE)
public class CidadeController {

    @Autowired
    private CidadeRepository cidadeRepository;

    @Autowired
    private CadastroCidadeService cadastroCidadeService;

    @GetMapping
    public List<Cidade> listar() {
        return cidadeRepository.listar();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @GetMapping("/{cidadeId}")
    public ResponseEntity<Cidade> buscar(@PathVariable("cidadeId") Long id) {
        Cidade cidade = cidadeRepository.buscar(id);
            if (cidade != null) {
                return ResponseEntity.ok(cidade);
            }

            return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<?> adicionar(@RequestBody Cidade cidade) {
        try{
            cidade = cadastroCidadeService.salvar(cidade);

            return ResponseEntity.status(HttpStatus.CREATED).body(cidade);
        } catch (EntidadeNaoEncontradaException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @PutMapping("/{cidadeId}")
    public ResponseEntity<?> atualizar(@PathVariable Long cidadeId, @RequestBody Cidade cidade){
        Cidade cidadeAtual = cidadeRepository.buscar(cidadeId);
        try{
            if (cidadeAtual != null) {
                BeanUtils.copyProperties(cidade, cidadeAtual, "id");
                cidadeRepository.salvar(cidadeAtual);

                return ResponseEntity.ok(cidadeAtual);
            }
        } catch (EntidadeNaoEncontradaException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{cidadeId}")
    public ResponseEntity<?> remover(@PathVariable Long cidadeId) {
        try {
            cadastroCidadeService.excluir(cidadeId);

            return ResponseEntity.noContent().build();

        } catch (EntidadeNaoEncontradaException e){
            return ResponseEntity.notFound().build();

        } catch (EntidadeEmUsoException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }
}
