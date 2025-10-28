package com.projeto.Credito.Imobiliario.DTO;
import java.math.BigDecimal;

public record SimulacaoResponse(
        BigDecimal valorParcela,
        BigDecimal taxaJurosAnual,
        BigDecimal custoEfetivoTotalAnual
) { }
