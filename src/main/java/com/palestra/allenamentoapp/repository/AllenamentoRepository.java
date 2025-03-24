package com.palestra.allenamentoapp.repository;

import com.palestra.allenamentoapp.model.Allenamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AllenamentoRepository extends JpaRepository<Allenamento, Long> {

    // Unica query per ottenere tutti gli allenamenti con relativi esercizi
    @Query("SELECT a FROM Allenamento a LEFT JOIN FETCH a.esercizi")
    List<Allenamento> findAllWithEsercizi();

}
