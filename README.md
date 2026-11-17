# Banking Account — Plataforma Bancária com Java 17 e Spring Boot

Projeto de estudo e prática de desenvolvimento backend Java, simulando operações bancárias e aplicando conceitos de arquitetura hexagonal, APIs REST, segurança, mensageria, observabilidade, contêineres, Kubernetes e CI/CD.

O objetivo é desenvolver uma aplicação próxima dos padrões utilizados em ambientes corporativos, especialmente no setor financeiro, sem implementar toda a complexidade de um sistema bancário real.

## 1. Visão geral

O **Banking Account** é uma API REST para gerenciamento de contas bancárias e movimentações financeiras.

A aplicação foi desenvolvida com Java 17 e Spring Boot, utilizando PostgreSQL para persistência e uma arquitetura organizada em camadas de domínio, aplicação e adaptadores.

O projeto também explora tecnologias utilizadas em arquiteturas de microserviços, como Apache Kafka, Docker, Kubernetes e ferramentas de observabilidade.

### Objetivos técnicos

- Aplicar princípios de arquitetura hexagonal.
- Desenvolver APIs REST com Spring Boot.
- Implementar operações bancárias com validações.
- Garantir consistência de dados e transações.
- Implementar autenticação e autorização com JWT.
- Criar testes unitários e de integração.
- Trabalhar com mensageria e eventos assíncronos.
- Utilizar Docker e Kubernetes para execução da aplicação.
- Implementar observabilidade e métricas.
- Preparar automação de CI/CD com GitHub Actions.

## 2. Funcionalidades bancárias

O sistema contempla operações de gerenciamento de contas e movimentações financeiras.

| Funcionalidade | Descrição |
|---|---|
| Criação de contas | Cadastro de contas bancárias |
| Consulta de contas | Recuperação dos dados de uma conta |
| Listagem de contas | Consulta das contas cadastradas |
| Consulta de saldo | Visualização do saldo disponível |
| Depósitos | Adição de valores ao saldo |
| Saques | Retirada de valores com validação |
| Transferências | Movimentação de valores entre contas |
| Extrato bancário | Histórico de movimentações |
| Validações | Verificação de dados e regras de negócio |
| Tratamento de exceções | Respostas padronizadas para erros |

As operações financeiras foram desenvolvidas com foco em consistência transacional, incluindo testes para situações de falha e rollback.

O sistema é uma simulação educacional e não realiza operações financeiras reais.

## 3. Tecnologias utilizadas

### Backend

| Tecnologia | Finalidade |
|---|---|
| Java 17 | Linguagem de programação |
| Spring Boot 4.1.2 | Framework principal |
| Spring Web | Desenvolvimento de APIs REST |
| Spring Data JPA | Persistência de dados |
| Hibernate | Mapeamento objeto-relacional |
| PostgreSQL 16 | Banco de dados relacional |
| Maven | Gerenciamento de dependências e build |
| Spring Security | Segurança da aplicação |
| JWT | Autenticação baseada em tokens |
| Swagger / OpenAPI | Documentação da API |

### Testes e qualidade

| Tecnologia | Finalidade |
|---|---|
| JUnit 5 | Testes automatizados |
| Mockito | Simulação de dependências |
| Testcontainers | Testes de integração com contêineres |
| Maven Verify | Validação do ciclo de build |

### Mensageria

| Tecnologia | Finalidade |
|---|---|
| Apache Kafka | Comunicação orientada a eventos |
| Kafka Streams | Processamento de fluxos de eventos |
| Transactional Outbox | Preparação para publicação confiável de eventos |
| Idempotência | Controle de processamento duplicado |

### Infraestrutura e observabilidade

| Tecnologia | Finalidade |
|---|---|
| Docker | Empacotamento da aplicação |
| Docker Compose | Gerenciamento da infraestrutura local |
| Kubernetes | Orquestração de contêineres |
| Minikube | Cluster Kubernetes local |
| Spring Boot Actuator | Monitoramento da aplicação |
| Micrometer | Instrumentação de métricas |
| Prometheus | Coleta de métricas |
| Grafana | Visualização de métricas |
| GitHub Actions | Automação de CI/CD |

## 4. Arquitetura do projeto

A aplicação utiliza princípios de **arquitetura hexagonal (Ports and Adapters)**, buscando separar as regras de negócio dos detalhes de infraestrutura.

### Domain

Responsável pelos modelos de domínio, regras de negócio e contratos utilizados pela aplicação.

### Application

Contém os serviços e casos de uso responsáveis por executar as operações bancárias.

### Adapters

Responsáveis pela comunicação com sistemas externos, incluindo:

- Controllers REST.
- Persistência com Spring Data JPA.
- Integrações com Apache Kafka.
- Conversão entre entidades e objetos de transferência de dados.

### Config

Contém configurações de segurança, infraestrutura e componentes Spring.

Essa organização favorece a manutenção, a testabilidade e a evolução da aplicação.

## 5. Banco de dados e persistência

O projeto utiliza PostgreSQL 16 para armazenamento das informações bancárias.

A persistência é realizada com Spring Data JPA e Hibernate.

As operações financeiras utilizam gerenciamento transacional para preservar a consistência dos dados.

O PostgreSQL também pode ser executado em contêiner Docker, facilitando a preparação do ambiente de desenvolvimento.

## 6. Segurança — Spring Security e JWT

A API utiliza Spring Security e autenticação baseada em JSON Web Token (JWT).

A implementação contempla:

- Autenticação por token.
- Proteção de endpoints.
- Configuração stateless.
- Validação de tokens JWT.
- Tratamento de requisições não autorizadas.

A chave JWT é configurada por variável de ambiente, evitando seu armazenamento diretamente no código-fonte.

## 7. Documentação — Swagger / OpenAPI

O projeto utiliza Swagger/OpenAPI para documentar e explorar os endpoints REST.

A documentação permite consultar as operações disponíveis, os parâmetros esperados e os formatos das respostas.

O acesso à interface Swagger depende da aplicação em execução e das configurações de segurança.

## 8. Apache Kafka e arquitetura orientada a eventos

O Apache Kafka foi incorporado para estudar comunicação assíncrona e processamento de eventos bancários.

O projeto explora:

- Producers e Consumers.
- Publicação e consumo de eventos.
- Serialização de mensagens.
- Grupos de consumidores.
- Processamento com Kafka Streams.
- Conceitos de idempotência.
- Padrão Transactional Outbox.

O objetivo é compreender como eventos podem desacoplar operações e integrações em sistemas distribuídos.

A implementação e os testes locais não representam, por si só, uma validação completa de confiabilidade de mensageria em produção.

## 9. Docker e Docker Compose

O projeto utiliza Docker para construir a imagem executável do serviço Spring Boot.

O Docker Compose organiza os componentes de infraestrutura utilizados no desenvolvimento local.

### Serviços configurados no Docker Compose

| Serviço | Função | Porta local |
|---|---|---|
| PostgreSQL | Banco de dados | 5435 |
| Prometheus | Coleta de métricas | 9090 |
| Grafana | Visualização de métricas | 3001 |

O Kafka também foi utilizado no ambiente de desenvolvimento e configurado no cluster Kubernetes.

As senhas locais do PostgreSQL e do Grafana são fornecidas por variáveis de ambiente.

### Variáveis de ambiente

Exemplo de configuração local:

```dotenv
DB_PASSWORD=defina_sua_senha_local
GRAFANA_PASSWORD=defina_uma_senha_forte
JWT_SECRET=defina_uma_chave_segura
```

O arquivo `.env` está incluído no `.gitignore` e não deve ser publicado.

## 10. Kubernetes — Minikube

A aplicação foi implantada em um cluster Kubernetes local utilizando Minikube.

Foram utilizados os seguintes recursos:

| Recurso | Finalidade |
|---|---|
| Deployment | Gerenciamento dos Pods |
| Service | Comunicação entre serviços |
| ConfigMap | Configurações da aplicação |
| Secret | Armazenamento de informações sensíveis |
| StatefulSet | Execução do PostgreSQL |
| PersistentVolumeClaim | Persistência de dados |
| Health Probes | Verificação de saúde da aplicação |

### Experimentos realizados

- Implantação do Spring Boot no Kubernetes.
- Execução de PostgreSQL e Kafka no cluster.
- Comunicação entre os serviços.
- Verificação do endpoint `/actuator/health`.
- Escalabilidade horizontal de duas para três réplicas.
- Teste de Self-Healing com recriação automática de Pod.
- Configuração e acionamento de Rolling Update.

O ambiente local foi utilizado para estudar conceitos de orquestração sem necessidade de contratar infraestrutura em nuvem.

## 11. Observabilidade — Actuator, Prometheus e Grafana

O projeto incorpora ferramentas de observabilidade para acompanhar o funcionamento da aplicação.

### Spring Boot Actuator

Disponibiliza endpoints de monitoramento, incluindo:

```text
/actuator/health
/actuator/metrics
/actuator/prometheus
```

### Micrometer

Responsável pela instrumentação e exposição de métricas.

### Prometheus

Utilizado para coleta e consulta de métricas.

### Grafana

Disponibiliza uma interface para visualização e análise das métricas coletadas.

Esses recursos ajudam a compreender aspectos de monitoramento de aplicações em ambientes distribuídos.

## 12. AWS EKS e OpenShift

O projeto também incluiu o estudo conceitual de duas plataformas Kubernetes corporativas.

### Amazon EKS

Serviço gerenciado da AWS para execução de aplicações Kubernetes.

Foram estudados conceitos como:

- Clusters gerenciados.
- Deployments e Services.
- Integração com serviços AWS.
- Escalabilidade.
- Publicação de imagens Docker.

### Red Hat OpenShift

Plataforma empresarial baseada em Kubernetes.

Foram estudadas suas características de implantação, segurança, gerenciamento e automação.

**Escopo:** os estudos de EKS e OpenShift foram conceituais. O deploy prático foi realizado no Minikube, sem provisionamento de clusters nessas plataformas.

## 13. CI/CD — GitHub Actions

Foi criado o arquivo:

```text
.github/workflows/ci.yml
```

A pipeline foi configurada para executar as seguintes atividades:

1. Obter o código-fonte do repositório.
2. Configurar Java 17.
3. Compilar o projeto com Maven.
4. Executar os testes automatizados.
5. Construir a imagem Docker da aplicação.

### Comando Maven

```bash
mvn clean verify
```

O comando foi executado localmente com resultado:

```text
BUILD SUCCESS
```

A execução da pipeline no ambiente do GitHub Actions ainda depende de validação após a publicação, incluindo a configuração dos segredos necessários.

O projeto não realiza deploy automático em produção.

## 14. Como executar o projeto

### Pré-requisitos

- Java 17.
- Maven.
- Docker e Docker Compose.
- Git.
- PostgreSQL e demais dependências de infraestrutura configuradas.

### Iniciar a infraestrutura local

Na raiz do projeto, com as variáveis de ambiente configuradas:

```bash
docker compose up -d
```

### Compilar e testar

```bash
mvn clean verify
```

### Executar a aplicação

Com as variáveis de ambiente necessárias configuradas:

```bash
mvn spring-boot:run
```

A API utiliza a porta local `8081` na configuração de desenvolvimento.

### Verificar a saúde

```bash
curl http://localhost:8081/actuator/health
```

O endpoint de saúde permite verificar o estado da aplicação.

A execução completa depende de PostgreSQL, Kafka e das configurações correspondentes.

## 15. Testes automatizados

O projeto contempla estudos e implementações de testes unitários e de integração.

Entre os cenários trabalhados estão:

- Operações bancárias.
- Validação de dados.
- Tratamento de exceções.
- Consistência transacional.
- Rollback de operações.
- Integração com banco de dados.

Foram utilizadas ferramentas como JUnit 5, Mockito e Testcontainers.

O build Maven local foi concluído com sucesso.

## 16. Evolução para microserviços

A arquitetura do projeto foi planejada considerando uma possível separação futura em serviços independentes.

| Microsserviço | Responsabilidade prevista |
|---|---|
| account-service | Gerenciamento de contas e operações bancárias |
| transaction-service | Processamento de movimentações financeiras |
| notification-service | Consumo de eventos e notificações |
| API Gateway | Ponto de entrada para APIs |

O **account-service** é o principal serviço desenvolvido neste repositório.

A separação completa em `transaction-service`, `notification-service` e API Gateway permanece como possibilidade de evolução arquitetural, não como funcionalidade concluída.

## 17. Etapas do desenvolvimento

| Etapas | Implementação | Situação |
|---|---|---|
| 1–6 | Configuração, arquitetura e persistência | Concluídas |
| 7–13 | Operações bancárias e validações | Concluídas |
| 14–16 | Testes, rollback e extrato | Concluídas |
| 17 | Spring Security e JWT | Concluída |
| 18 | Swagger / OpenAPI | Concluída |
| 19 | Docker Compose e Testcontainers | Concluída |
| 20 | Conceitos de arquitetura de microserviços | Estudados |
| 21 | Apache Kafka | Implementado |
| 22 | Kafka Streams | Implementado e testado localmente |
| 23 | Actuator, Micrometer, Prometheus e Grafana | Concluída |
| 24 | Kubernetes com Minikube | Prática concluída |
| 25 | AWS EKS e OpenShift | Estudo conceitual concluído |
| 26 | CI/CD com GitHub Actions | Configurada; validação remota pendente |

## 18. Aprendizados

O desenvolvimento do Banking Account permitiu praticar conceitos importantes do ecossistema Java corporativo:

- Desenvolvimento backend com Java 17 e Spring Boot.
- Modelagem e persistência de dados.
- Arquitetura hexagonal.
- Segurança de APIs REST.
- Transações e consistência de dados.
- Testes automatizados.
- Comunicação orientada a eventos.
- Processamento com Kafka Streams.
- Empacotamento com Docker.
- Orquestração com Kubernetes.
- Observabilidade e métricas.
- Fundamentos de cloud e CI/CD.

## 19. Limitações do projeto

Este projeto foi desenvolvido para fins educacionais e demonstração de conhecimentos técnicos.

Não constitui um sistema bancário pronto para produção e não implementa funcionalidades como liquidação financeira real, integração com PIX, contabilidade bancária completa, mecanismos antifraude ou requisitos regulatórios de uma instituição financeira.

## 20. Considerações finais

O **Banking Account** reúne tecnologias e conceitos frequentemente utilizados no desenvolvimento de sistemas Java corporativos.

A proposta foi evoluir uma API bancária desde suas funcionalidades básicas até aspectos de arquitetura, segurança, mensageria, observabilidade, contêineres, Kubernetes e automação de build.

O projeto também serve como material de estudo, prática e portfólio profissional para desenvolvimento backend Java.

---

**Autor:** [edufulldev](https://github.com/edufulldev)

**Finalidade:** Estudos, prática de engenharia de software e portfólio profissional.
