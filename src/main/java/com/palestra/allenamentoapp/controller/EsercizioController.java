package com.palestra.allenamentoapp.controller;

import com.palestra.allenamentoapp.model.Esercizio;
import com.palestra.allenamentoapp.service.EsercizioService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/esercizi")
@RequiredArgsConstructor // Lombok: genera il costruttore con i campi final (iniezione via costruttore)
public class EsercizioController {

    @Autowired
    private EsercizioService esercizioService;

    // Get di tutti gli esercizi
    @GetMapping
    public List<Esercizio> getAllEsercizi() {
        return esercizioService.getAllEsercizi();
    }

    // Get di un esercizio dato il suo id
    @GetMapping("/{id}")
    public Esercizio getEsercizioById(@PathVariable Long id) {
        return esercizioService.getEsercizioById(id);
    }

    // Inserimento esercizio
    @PostMapping()
    public Esercizio addEsercizio(@RequestBody Esercizio esercizio) {
        return esercizioService.addEsercizio(esercizio.getAllenamento().getId(), esercizio);
    }

    // Eliminazione esercizio
    @DeleteMapping("/{id}")
    public void deleteEsercizio(@PathVariable Long id) {
        esercizioService.deleteEsercizio(id);
    }

    // Aggiornamento lo stato di completamento di un esercizio
    @PutMapping("/{id}")
    public Esercizio updateEsercizio(@PathVariable Long id, @RequestBody Esercizio esercizio) {
        return esercizioService.updateEsercizio(id, esercizio);
    }

    // Get di tutti gli esercizi di un allenamento
    @GetMapping("/allenamento/{allenamentoId}")
    public List<Esercizio> getEserciziByAllenamento(@PathVariable Long allenamentoId) {
        return esercizioService.getEserciziByAllenamentoId(allenamentoId);
    }
}
