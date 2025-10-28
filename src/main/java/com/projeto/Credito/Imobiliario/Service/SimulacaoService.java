package com.projeto.Credito.Imobiliario.Service;

import com.projeto.Credito.Imobiliario.Config.KafkaAppProperties;
import com.projeto.Credito.Imobiliario.DTO.SimulacaoRealizadaEvent;
import com.projeto.Credito.Imobiliario.DTO.SimulacaoRequest;
import com.projeto.Credito.Imobiliario.DTO.SimulacaoResponse;
import com.projeto.Credito.Imobiliario.Entity.Simulacao;
import com.projeto.Credito.Imobiliario.Repository.SimulacaoRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SimulacaoService {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final SimulacaoRepository simulacaoRepository;
    private final KafkaAppProperties kafkaAppProperties; // CORRIGIDO: Injetando a classe de propriedades

    public SimulacaoService(KafkaTemplate<String, Object> kafkaTemplate,
                            SimulacaoRepository simulacaoRepository,
                            KafkaAppProperties kafkaAppProperties) { // CORRIGIDO
        this.kafkaTemplate = kafkaTemplate;
        this.simulacaoRepository = simulacaoRepository;
        this.kafkaAppProperties = kafkaAppProperties;
    }

    @Transactional // Garante que a operação de salvar e enviar para o Kafka seja atômica
    public SimulacaoResponse calcularESalvarSimulacao(SimulacaoRequest request) {
        log.info("Iniciando cálculo da simulação para o valor: {}", request.valorImovel());

        // LÓGICA DE SIMULAÇÃO (sem alterações)
        BigDecimal taxaJurosAnual = new BigDecimal("9.5");
        BigDecimal taxaJurosMensal = taxaJurosAnual.divide(new BigDecimal("1200"), 10, RoundingMode.HALF_UP);
        BigDecimal valorFinanciado = request.valorImovel().subtract(request.valorEntrada());
        int numeroMeses = request.prazoAnos() * 12;
        BigDecimal umMaisI = BigDecimal.ONE.add(taxaJurosMensal);
        BigDecimal umMaisIelevadoN = umMaisI.pow(numeroMeses);
        BigDecimal valorParcela = valorFinanciado.multiply(taxaJurosMensal).multiply(umMaisIelevadoN)
                .divide(umMaisIelevadoN.subtract(BigDecimal.ONE), 2, RoundingMode.HALF_UP);

        SimulacaoResponse response = new SimulacaoResponse(
                valorParcela,
                taxaJurosAnual,
                taxaJurosAnual.add(new BigDecimal("1.5"))
        );

        // CORRIGIDO: O método salvar agora retorna a entidade salva com o ID do evento
        Simulacao simulacaoSalva = salvar(request, response);

        log.info("Simulação calculada e salva com sucesso. ID do evento: {}", simulacaoSalva.getIdEvento());

        // CORRIGIDO: Passando o ID do evento correto para o método publicarEvento
        publicarEvento(request, response, simulacaoSalva.getIdEvento());

        return response;
    }

    // CORRIGIDO: O readOnly=true está correto, o problema era provavelmente a falta do import
    @Transactional(readOnly = true)
    public List<SimulacaoResponse> buscarTodas() {
        return simulacaoRepository.findAll().stream()
                .map(simulacao -> new SimulacaoResponse(
                        simulacao.getValorParcela(),
                        simulacao.getTaxaJurosAnual(),
                        simulacao.getCustoEfetivoTotalAnual()))
                .collect(Collectors.toList());
    }

    // CORRIGIDO: Este método agora retorna a entidade Simulacao para que possamos obter o UUID
    private Simulacao salvar(SimulacaoRequest request, SimulacaoResponse response) {
        Simulacao simulacaoEntity = new Simulacao();
        simulacaoEntity.setIdEvento(UUID.randomUUID());
        simulacaoEntity.setDataSimulacao(LocalDateTime.now());
        simulacaoEntity.setValorImovel(request.valorImovel());
        simulacaoEntity.setValorEntrada(request.valorEntrada());
        simulacaoEntity.setRendaMensal(request.rendaMensal());
        simulacaoEntity.setPrazoAnos(request.prazoAnos());
        simulacaoEntity.setValorParcela(response.valorParcela());
        simulacaoEntity.setTaxaJurosAnual(response.taxaJurosAnual());
        simulacaoEntity.setCustoEfetivoTotalAnual(response.custoEfetivoTotalAnual());

        return simulacaoRepository.save(simulacaoEntity);
    }

    // CORRIGIDO: A assinatura do método agora recebe o UUID para garantir consistência
    private void publicarEvento(SimulacaoRequest request, SimulacaoResponse response, UUID eventoId) {
        SimulacaoRealizadaEvent event = new SimulacaoRealizadaEvent(
                eventoId,
                LocalDateTime.now(),
                request,
                response
        );
        try {
            // CORRIGIDO: Usando a propriedade da classe de configuração
            String topicName = kafkaAppProperties.getTopicName();
            kafkaTemplate.send(topicName, event);
            log.info("Evento de simulação {} enviado com sucesso para o tópico Kafka: {}", eventoId, topicName);
        } catch (Exception e) {
            log.error("Erro ao enviar evento {} para o Kafka", eventoId, e);
            // Em um cenário real, você poderia ter uma lógica de retry ou salvar o evento em uma "dead letter queue"
        }
    }
}