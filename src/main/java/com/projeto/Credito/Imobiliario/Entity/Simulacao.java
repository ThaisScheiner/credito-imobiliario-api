package com.projeto.Credito.Imobiliario.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "simulacoes")
@Getter
@Setter
public class Simulacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, updatable = false)
    private UUID idEvento;

    @Column(nullable = false, updatable = false)
    private LocalDateTime dataSimulacao;

    // Dados da requisição
    @Column(precision = 15, scale = 2)
    private BigDecimal valorImovel;

    @Column(precision = 15, scale = 2)
    private BigDecimal valorEntrada;

    @Column(precision = 15, scale = 2)
    private BigDecimal rendaMensal;

    private int prazoAnos;

    // Dados da resposta
    @Column(precision = 15, scale = 2)
    private BigDecimal valorParcela;

    @Column(precision = 5, scale = 2)
    private BigDecimal taxaJurosAnual;

    @Column(precision = 5, scale = 2)
    private BigDecimal custoEfetivoTotalAnual;
}
