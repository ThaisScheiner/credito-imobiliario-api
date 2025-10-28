package com.projeto.Credito.Imobiliario.Repository;

import com.projeto.Credito.Imobiliario.Entity.Simulacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SimulacaoRepository extends JpaRepository<Simulacao, Long> {
}
