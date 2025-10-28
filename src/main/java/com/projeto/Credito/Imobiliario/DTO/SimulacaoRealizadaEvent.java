package com.projeto.Credito.Imobiliario.DTO;

import java.time.LocalDateTime;
import java.util.UUID;

public record SimulacaoRealizadaEvent(
        UUID idEvento,
        LocalDateTime dataHoraEvento,
        SimulacaoRequest request,
        SimulacaoResponse response
) { }
