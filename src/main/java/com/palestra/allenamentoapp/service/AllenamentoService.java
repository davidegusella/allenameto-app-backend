package com.palestra.allenamentoapp.service;

import com.palestra.allenamentoapp.model.Allenamento;
import com.palestra.allenamentoapp.model.Esercizio;
import com.palestra.allenamentoapp.repository.AllenamentoRepository;
import com.palestra.allenamentoapp.repository.EsercizioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AllenamentoService {

    @Autowired
    private final AllenamentoRepository allenamentoRepository;

    @Autowired
    private final EsercizioRepository esercizioRepository;

    // Eseguito ogni lunedì a mezzanotte per resettare lo stato di completamento di allenamenti ed esercizi.
    // Metodo che azzera i flag di completamento ogni settimana
    @Scheduled(cron = "0 0 0 ? * MON")  // Esegue ogni lunedì alle 00:00
    public void resetCompletamentoSettimanale() {

        // Reset dei flag degli allenamenti
        List<Allenamento> allenamenti = allenamentoRepository.findAll();
        for (Allenamento allenamento : allenamenti) {
            allenamento.setCompletato(false);
            allenamentoRepository.save(allenamento);  // Salva ogni allenamento con il flag resettato
        }

        // Reset dei flag degli esercizi
        List<Esercizio> esercizi = esercizioRepository.findAll();
        for (Esercizio esercizio : esercizi) {
            esercizio.setCompletato(false);
            esercizioRepository.save(esercizio);  // Salva ogni esercizio con il flag resettato
        }
    }

    // Get di tutti gli allenamenti
    public List<Allenamento> getAllAllenamenti() {
        return allenamentoRepository.findAllWithEsercizi();
    }

    // Get di un allenamento dato il suo id
    public Allenamento getAllenamentoById(Long id) {
        return allenamentoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Allenamento con id " + id + " non trovato."
                ));
    }

    // Aggiorna un allenamento esistente.
    // Consente l'aggiornamento solo se tutti gli esercizi associati sono completati.
    public ResponseEntity<Allenamento> updateAllenamento(Long id) {
        Allenamento existingAllenamento = allenamentoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Allenamento non trovato con id " + id
                ));

        // Verifica se tutti gli esercizi sono completati
        List<Esercizio> eserciziAssociati = esercizioRepository.findByAllenamentoId(id);
        boolean tuttiCompletati = eserciziAssociati.stream().allMatch(Esercizio::isCompletato);

        if (!tuttiCompletati) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Non tutti gli esercizi sono completati. Impossibile aggiornare l'allenamento.");
        }

        // Se tutti gli esercizi sono completati, imposta l'allenamento come completato
        existingAllenamento.setCompletato(true);

        // Salva l'aggiornamento
        Allenamento updatedAllenamento = allenamentoRepository.save(existingAllenamento);

        return ResponseEntity.ok(updatedAllenamento);
    }

    // Salva un nuovo allenamento
    public ResponseEntity<Allenamento> addAllenamento(Allenamento nuovoAllenamento) {
        // Blocca caratteri non desiderati
        if (!nuovoAllenamento.getNomeGiorno().matches("^[a-zA-Z0-9 ]+$")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Input non valido!");
        }

        Allenamento allenamentoSalvato = allenamentoRepository.save(nuovoAllenamento);
        return ResponseEntity.status(HttpStatus.CREATED).body(allenamentoSalvato);
    }

    // Aggiorna il nome di un allenamento esistente
    public ResponseEntity<Allenamento> updateNomeAllenamento(Long id, String nuovoNome) {
        Allenamento existingAllenamento = allenamentoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Allenamento non trovato con id " + id
                ));

        // Verifica input valido per evitare SQL Injection
        if (!nuovoNome.matches("^[a-zA-Z0-9 ]+$")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Input non valido!");
        }

        // Aggiorna il nome del giorno dell'allenamento
        existingAllenamento.setNomeGiorno(nuovoNome);
        allenamentoRepository.save(existingAllenamento);

        return ResponseEntity.ok(existingAllenamento);
    }


    // Elimina un allenamento dato il suo id
    public void deleteAllenamento(Long id) {
        allenamentoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Allenamento non trovato con id " + id
                ));

        allenamentoRepository.deleteById(id);
    }
}
