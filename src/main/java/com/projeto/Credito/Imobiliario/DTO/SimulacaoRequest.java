package com.projeto.Credito.Imobiliario.DTO;

import java.math.BigDecimal;

public record SimulacaoRequest(
        BigDecimal valorImovel,
        BigDecimal valorEntrada,
        BigDecimal rendaMensal,
        int prazoAnos
) { }
