package com.palestra.allenamentoapp.controller;

import com.palestra.allenamentoapp.service.AllenamentoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reset")
@RequiredArgsConstructor
public class ResetController {

    private final AllenamentoService allenamentoService;

    // Reset flag completamento settimanale
    @PostMapping("/weekly")
    public String resetWeeklyFlags() {
        allenamentoService.resetCompletamentoSettimanale();
        return "Completamento settimanale resettato con successo.";
    }
}
