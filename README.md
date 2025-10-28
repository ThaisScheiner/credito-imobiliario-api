# API de Simulação de Crédito Imobiliário

![Java](https://img.shields.io/badge/Java-21-blue?logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.3.1-brightgreen?logo=spring&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-4.0.0-red?logo=apache-maven&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-8.0-orange?logo=mysql&logoColor=white)
![Kafka](https://img.shields.io/badge/Apache_Kafka-blue?logo=apache-kafka&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-blue?logo=docker&logoColor=white)

Este projeto consiste em uma API REST para simulação de crédito imobiliário, desenvolvida com Java 21 e Spring Boot 3. A aplicação permite que os usuários calculem o valor das parcelas de um financiamento, salva o histórico de simulações e publica um evento em um tópico do Apache Kafka para cada nova simulação realizada.

***

## 📋 Funcionalidades Principais

* **Simulação de Financiamento**: Calcula o valor da parcela mensal com base no valor do imóvel, entrada, renda mensal e prazo em anos.
* **Persistência de Dados**: Armazena todas as simulações realizadas em um banco de dados MySQL.
* **Listagem de Simulações**: Oferece um endpoint para consultar o histórico de todas as simulações já feitas.
* **Arquitetura Orientada a Eventos**: Publica um evento no Kafka a cada simulação bem-sucedida, permitindo que outros microsserviços (como análise de crédito, BI, etc.) possam consumir essa informação de forma desacoplada.
* **Performance Otimizada**: Utiliza Virtual Threads do Java 21 para melhorar a concorrência e o desempenho da aplicação sob alta carga.

***

## 🛠️ Tecnologias Utilizadas

### Backend
* **Java 21**: Versão mais recente da linguagem, com suporte a Virtual Threads.
* **Spring Boot 3.3.1**: Framework principal para criação da API REST.
* **Spring Web**: Para a construção dos endpoints REST.
* **Spring Data JPA**: Para a persistência de dados e comunicação com o banco de dados.
* **Hibernate**: Implementação da especificação JPA.
* **Spring Kafka**: Para integração e publicação de mensagens no Apache Kafka.
* **Lombok**: Para reduzir o código boilerplate (getters, setters, construtores).
* **Maven**: Gerenciador de dependências do projeto.

### Banco de Dados
* **MySQL 8.0**: Banco de dados relacional para armazenar os dados das simulações.

### Mensageria
* **Apache Kafka**: Plataforma de streaming de eventos para comunicação assíncrona.
* **Zookeeper**: Utilizado para o gerenciamento do cluster Kafka.

### Infraestrutura
* **Docker & Docker Compose**: Para containerização da aplicação e dos serviços de infraestrutura (MySQL e Kafka), facilitando a configuração do ambiente de desenvolvimento.

***

## 📡 API Endpoints

A API expõe os seguintes endpoints:

| Método HTTP | Rota              | Descrição                                 |
| :---------- | :---------------- | :---------------------------------------- |
| `POST`      | `/api/simulacoes` | Realiza uma nova simulação de crédito.    |
| `GET`       | `/api/simulacoes` | Lista todas as simulações já realizadas. |

### Exemplo de Requisição (`POST /api/simulacoes`)

**Request Body:**
```json
{
  "valorImovel": 300000.00,
  "valorEntrada": 60000.00,
  "rendaMensal": 8000.00,
  "prazoAnos": 30
}
Success Response (200 OK):

JSON

{
  "valorParcela": 2028.61,
  "taxaJurosAnual": 9.50,
  "custoEfetivoTotalAnual": 11.00
}
📨 Evento Kafka
Após cada simulação bem-sucedida, um evento do tipo SimulacaoRealizadaEvent é publicado no tópico credito-imobiliario.simulacoes.realizadas.

Estrutura do Evento:

JSON

{
  "idEvento": "a1b2c3d4-e5f6-7890-1234-567890abcdef",
  "dataHoraEvento": "2025-10-28T18:30:00.123456",
  "request": {
    "valorImovel": 300000.00,
    "valorEntrada": 60000.00,
    "rendaMensal": 8000.00,
    "prazoAnos": 30
  },
  "response": {
    "valorParcela": 2028.61,
    "taxaJurosAnual": 9.50,
    "custoEfetivoTotalAnual": 11.00
  }
}
🚀 Como Executar o Projeto
Pré-requisitos
JDK 21 ou superior

Apache Maven 3.8+

Docker e Docker Compose

Passos
Clone o repositório:

Bash

git clone [https://github.com/ThaisScheiner/credito-imobiliario-api.git](https://github.com/ThaisScheiner/credito-imobiliario-api.git)
cd credito-imobiliario-api
Inicie a infraestrutura (MySQL e Kafka): Execute os seguintes comandos na raiz do projeto para iniciar os contêineres Docker.

Bash

# Iniciar o banco de dados MySQL
docker-compose -f docker-compose-db.yml up -d

# Iniciar Zookeeper, Kafka e Kafka-UI
docker-compose -f docker-compose-kafka.yml up -d
O MySQL estará acessível em localhost:3306.

O Kafka estará acessível em localhost:29092.

O Kafka-UI (interface web para gerenciar o Kafka) estará acessível em http://localhost:8088.

Execute a aplicação Spring Boot: Você pode executar a aplicação usando o Maven Wrapper incluído:

Bash

./mvnw spring-boot:run
A API estará disponível em http://localhost:8080.