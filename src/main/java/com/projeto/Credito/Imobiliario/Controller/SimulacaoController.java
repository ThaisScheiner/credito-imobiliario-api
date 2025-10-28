package com.projeto.Credito.Imobiliario.Controller;

import com.projeto.Credito.Imobiliario.DTO.SimulacaoRequest;
import com.projeto.Credito.Imobiliario.DTO.SimulacaoResponse;
import com.projeto.Credito.Imobiliario.Service.SimulacaoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/simulacoes")
@CrossOrigin(origins = "http://localhost:4200")
public class SimulacaoController {

    private final SimulacaoService simulacaoService;

    public SimulacaoController(SimulacaoService simulacaoService) {
        this.simulacaoService = simulacaoService;
    }

    @PostMapping
    public ResponseEntity<SimulacaoResponse> simular(@RequestBody SimulacaoRequest request) {
        SimulacaoResponse response = simulacaoService.calcularESalvarSimulacao(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<SimulacaoResponse>> listarSimulacoes() {
        List<SimulacaoResponse> simulacoes = simulacaoService.buscarTodas();
        return ResponseEntity.ok(simulacoes);
    }
}
