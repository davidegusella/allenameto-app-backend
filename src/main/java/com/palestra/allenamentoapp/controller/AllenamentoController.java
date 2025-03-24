package com.palestra.allenamentoapp.controller;

import com.palestra.allenamentoapp.model.Allenamento;
import com.palestra.allenamentoapp.service.AllenamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/allenamenti")
public class AllenamentoController {

    @Autowired
    private AllenamentoService allenamentoService;

    // Get di tutti gli allenamenti
    @GetMapping
    public List<Allenamento> getAllAllenamenti() {
        return allenamentoService.getAllAllenamenti();
    }

    // Get di un allenamento dato il suo id
    @GetMapping("/{id}")
    public Allenamento getAllenamentoById(@PathVariable Long id) {
        return allenamentoService.getAllenamentoById(id);
    }

    // Inserimento allenamento
    @PostMapping
    public ResponseEntity<Allenamento> addAllenamento(@RequestBody Allenamento allenamento) {
        return allenamentoService.addAllenamento(allenamento);
    }

    // Eliminaizone allenamento
    @DeleteMapping("/{id}")
    public void deleteAllenamento(@PathVariable Long id) {
        allenamentoService.deleteAllenamento(id);
    }

    // Aggiornamento stato completamento allenamento
    @PutMapping("/{id}")
    public ResponseEntity updateAllenamento(@PathVariable Long id) {
        return allenamentoService.updateAllenamento(id);
    }

    // Aggiornamneto nome allenamento
    @PutMapping("/modificaNome/{id}")
    public ResponseEntity<Allenamento> updateNomeAllenamento(@PathVariable Long id, @RequestBody Map<String, String> payload) {
        String nuovoNome = payload.get("nomeGiorno");

        if (nuovoNome == null || nuovoNome.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }

        return allenamentoService.updateNomeAllenamento(id, nuovoNome);
    }

}
