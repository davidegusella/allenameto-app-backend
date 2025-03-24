package com.palestra.allenamentoapp.repository;

import com.palestra.allenamentoapp.model.Esercizio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EsercizioRepository extends JpaRepository<Esercizio, Long> {
    List<Esercizio> findByAllenamentoId(Long allenamentoId);
}
