package com.palestra.allenamentoapp.service;

import com.palestra.allenamentoapp.model.Allenamento;
import com.palestra.allenamentoapp.model.Esercizio;
import com.palestra.allenamentoapp.repository.AllenamentoRepository;
import com.palestra.allenamentoapp.repository.EsercizioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EsercizioService {

    @Autowired
    private EsercizioRepository esercizioRepository;

    @Autowired
    private AllenamentoRepository allenamentoRepository;

    // Get di tutti gli esercizi
    public List<Esercizio> getAllEsercizi() {
        return esercizioRepository.findAll();
    }

    // Get di un esercizio dato il suo id
    public Esercizio getEsercizioById(Long id) {
        return esercizioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Esercizio con id " + id + " non trovato."
                ));
    }

    // Salva un nuovo esercizio, richiede che l'esercizio sia associato ad un allenamento valido.
    public Esercizio addEsercizio(Long allenamentoId, Esercizio esercizio) {
        // Verifica che l'allenamento associato sia valido
        if (esercizio.getAllenamento() == null || esercizio.getAllenamento().getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Allenamento associato non valido.");
        }

        // Verifica se l'allenamento esiste nel database
        if (!allenamentoRepository.existsById(esercizio.getAllenamento().getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Allenamento associato non valido.");
        }

        // Controllo che le serie e le ripetizioni siano maggiori di zero
        if (esercizio.getSerie() <= 0 || esercizio.getRipetizioni() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Il numero di serie e ripetizioni deve essere maggiore di zero.");
        }

        // Salva il nuovo esercizio
        esercizio.setAllenamento(allenamentoRepository.findById(allenamentoId).get());
        esercizioRepository.save(esercizio);

        // Se l'esercizio non è completato, segna l'allenamento come non completato
        if (!esercizio.isCompletato()) {
            Allenamento allenamento = esercizio.getAllenamento();
            allenamento.setCompletato(false); // Imposta l'allenamento a non completato
            allenamentoRepository.save(allenamento); // Salva l'allenamento con il flag aggiornato
        }

        // Restituisce l'esercizio appena salvato
        return esercizio;
    }


    // Elimina un esercizio, aggiornando lo stato dell'allenamento associato
    public void deleteEsercizio(Long id) {
        Esercizio esercizio = esercizioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Esercizio con id " + id + " non trovato."
                ));

        Long allenamentoId = esercizio.getAllenamento().getId(); // 🔹 Ottieni l'ID dell'allenamento associato

        esercizioRepository.deleteById(id); // 🔥 Elimina l'esercizio

        // ✅ Dopo l'eliminazione, aggiorna lo stato dell'allenamento
        List<Esercizio> eserciziRimasti = esercizioRepository.findByAllenamentoId(allenamentoId);
        boolean tuttiCompletati = eserciziRimasti.stream().allMatch(Esercizio::isCompletato);

        Allenamento allenamento = allenamentoRepository.findById(allenamentoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Allenamento con id " + allenamentoId + " non trovato."));

        allenamento.setCompletato(tuttiCompletati); // ✅ Imposta il flag in base agli esercizi rimanenti
        allenamentoRepository.save(allenamento); // 🔄 Salva l'aggiornamento nel database
    }


    // Aggiorna lo stato di completamento di un esercizio
    public Esercizio updateEsercizio(Long id, Esercizio esercizio) {
        Esercizio existingEsercizio = esercizioRepository.findById(id).orElseThrow(() -> new RuntimeException("Esercizio non trovato"));

        // Se il contatore delle serie arriva a zero, imposta il flag "completato" a true
        if (esercizio.getSerie() == 0) {
            existingEsercizio.setCompletato(true); // Imposta completato a true
        }

        // Salva l'esercizio con il nuovo flag e nuove informazioni
        return esercizioRepository.save(existingEsercizio);
    }


    public List<Esercizio> getEserciziByAllenamentoId(Long allenamentoId) {
        return esercizioRepository.findByAllenamentoId(allenamentoId);
    }
}