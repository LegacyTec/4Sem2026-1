# Sistema de Gestão de Manutenções - Altave

> 🚀 **LegacyTech** | 4º Semestre - FATEC SJC | API - Sprint 1 🔄 EM ANDAMENTO

[Sobre](#-sobre-o-projeto) |
[Estrutura](#-estrutura-do-projeto) |
[Como Rodar](#-como-rodar) |
[Backlog](#-product-backlog) |
[Sprints](#-sprints) |
[Padrões](#-padrões-e-convenções) |
[Equipe](#-equipe)

---

## 🎯 Sobre o Projeto

**Problema:** A Altave opera cerca de 100 sistemas distribuídos mundialmente, cada um exigindo manutenções periódicas conforme suas horas de uso. O controle dessas manutenções — que variam em duração e criticidade conforme a localização dos sistemas (de um dia para intervenções próximas até vários dias para regiões como a Ásia) — é feito de forma manual ou descentralizada, sem visibilidade adequada do histórico, agendamentos, criticidade e logística envolvida.

**Solução:** Desenvolvimento de um sistema web para gestão de manutenções preventivas e corretivas, permitindo o cadastro e acompanhamento de equipamentos, controle de técnicos e suas competências, agendamento de visitas com checklist de execução, controle de ferramentas e materiais para viagem, e visualização geográfica da localização dos sistemas e técnicos.

# 🚀 [Link do Deploy - quando disponível]

---

## 📁 Estrutura do Projeto

```
/
├── backend/              # 📦 Código-fonte do backend (Spring Boot)
│   ├── src/
│   └── README.md
├── frontend/             # 📦 Código-fonte do frontend (Vue.js)
│   ├── src/
│   └── README.md
├── Documentacao/         # Modelagem de dados, diagramas
├── Manuais/              # Manuais técnicos e de usuário
├── Manual de Instalação.md
├── manual do usuario.md
├── Manual de Dados.md
├── SPRINT_1.md
├── SPRINT_2.md
├── SPRINT_3.md
└── SPRINT_4.md
```

### 📦 Repositórios

| Componente | Link Original | Cópia Local | Deploy |
| --- | --- | --- | --- |
| **Backend** | [🔗 Repositório Original](#) | [`/backend`](./backend) | [Plataforma de deploy] |
| **Frontend** | [🔗 Repositório Original](#) | [`/frontend`](./frontend) | [Plataforma de deploy] |
| **Boards/Cards** | [Board do Projeto](#) | - | - |

### 🛠️ Tecnologias

| Tecnologia | Uso |
| --- | --- |
| **Spring Boot** | Backend — criação de APIs RESTful, injeção de dependências, segurança |
| **Spring Data JPA / Hibernate** | Persistência e mapeamento objeto-relacional com Oracle |
| **Oracle Database** | Banco de dados relacional principal; PL/SQL para programação backend |
| **Vue.js** | Frontend — componentização, gerenciamento de estado, renderização condicional |
| **Axios** | Integração do frontend com as APIs RESTful |
| **Leaflet / Vue Chart.js** | Visualização de mapas (GIS) e gráficos dinâmicos |
| **Spring Security** | Autenticação e autorização de usuários |

---

## ▶️ Como Rodar

Consulte o [Manual de Instalação](./Manual%20de%20Instalação.md) para instruções detalhadas de setup local.

**Resumo:**

* **Backend:** `./mvnw spring-boot:run`
* **Frontend:** `npm install && npm run dev`

---

## 📚 Documentação Técnica

### 📝 Manuais de Uso

| Documento | Descrição | Link |
| --- | --- | --- |
| **Manual de Instalação** | Guia completo de setup do ambiente | [📄 Ver manual](./Manual%20de%20Instalação.md) |
| **Manual de Dados** | Estrutura do banco de dados, DER, dicionário | [📄 Ver manual](./Manual%20de%20Dados.md) |
| **Manual do Usuário** | Guia de uso da plataforma | [📄 Ver manual](./manual%20do%20usuario.md) |

### 💻 Guias de Desenvolvimento

| Componente | Descrição | Link |
| --- | --- | --- |
| **Backend** | Arquitetura, endpoints da API, configurações, padrões de código | [📄 Ver guia](./Manuais/BackEnd.md) |
| **Frontend** | Estrutura de componentes, padrões, guia para devs | [📄 Ver guia](./Manuais/FrontEnd.md) |

---

## 📝 Requisitos

**Requisitos Funcionais**

* [RF01] - Agendar manutenções (preventivas e corretivas) respeitando intervalo contratual e criticidade
* [RF02] - Registrar e acompanhar a execução de manutenções por meio de checklist
* [RF03] - Controlar ferramentas e equipamentos necessários para cada viagem de manutenção (formulário de retirada)
* [RF04] - Visualizar localização geográfica dos sistemas e técnicos em mapa (GIS)
* [RF05] - Consultar histórico completo de manutenções por equipamento
* [RF06] - Calcular e exibir previsão de chegada dos técnicos aos locais de manutenção
* [RF07] - Avaliar impacto de manutenções urgentes sobre o planejamento existente *(desejável)*
* [RF08] - Telas de cadastro de equipamentos, técnicos e contratos *(desejável — Sprint 3 se houver tempo)*

**Requisitos Não Funcionais**

* [RNF01] - Manual de Instalação disponível no repositório Git (requisito Fatec – obrigatório)
* [RNF02] - Manual do Usuário disponível (requisito Fatec – obrigatório)
* [RNF03] - Documentação da API (Swagger / OpenAPI)
* [RNF04] - Modelagem de Banco de Dados com DER e dicionário de dados
* [RNF05] - Sistema operacional em cloud (Oracle Cloud)
* [RNF06] - Backend desenvolvido com Spring Boot (Java) + Spring Security + Spring Data JPA com Oracle
* [RNF07] - Frontend desenvolvido com Vue.js + Axios + Leaflet/Vue Chart.js

---

## ⭐ Product Backlog

| Rank | Prioridade | User Story | Estimativa (SP) | Sprint |
| --- | --- | --- | --- | --- |
| 1 | Alta | Como **gestor**, quero **agendar manutenções preventivas e corretivas** para que **o intervalo contratual seja respeitado e as prioridades sejam atendidas**. | 13 | 1 |
| 2 | Alta | Como **técnico**, quero **acessar e preencher o checklist de manutenção** para que **a execução seja padronizada e registrada corretamente**. | 8 | 1 |
| 3 | Alta | Como **gestor**, quero **controlar a retirada de ferramentas e equipamentos para a viagem** para que **extravios sejam evitados e a logística seja rastreável**. | 8 | 2 |
| 4 | Alta | Como **gestor**, quero **visualizar a localização dos sistemas e técnicos em um mapa** para que **eu possa tomar decisões logísticas com base na posição geográfica**. | 13 | 2 |
| 5 | Média | Como **gestor**, quero **consultar o histórico de manutenções por equipamento** para que **eu tenha rastreabilidade completa das intervenções realizadas**. | 5 | 2 |
| 6 | Média | Como **gestor**, quero **visualizar a previsão de chegada dos técnicos** para que **o planejamento das manutenções seja mais preciso**. | 8 | 3 |
| 7 | Média | Como **gestor**, quero **avaliar o impacto de uma manutenção urgente no planejamento vigente** para que **eu possa reordenar prioridades sem comprometer outros atendimentos**. | 8 | 3 |
| 8 | Baixa | Como **gestor**, quero **cadastrar equipamentos, técnicos e contratos** para que **os dados possam ser mantidos diretamente pelo sistema quando necessário**. | 13 | 3 *(se houver tempo)* |

---

## 📆 Sprints

| SPRINTS | PERÍODOS | DESCRIÇÃO |
| --- | --- | --- |
| Sprint 1 | [data início] à [data fim] | Agendamento de manutenções com criticidade e intervalos contratuais; checklist de execução; setup base do projeto (Spring Boot + Vue.js + Oracle) |
| Sprint 2 | [data início] à [data fim] | Controle logístico de ferramentas e viagem; visualização GIS com Leaflet; histórico de manutenções por equipamento |
| Sprint 3 | [data início] à [data fim] | Previsão de chegada; avaliação de impacto de urgências; telas de cadastro (se houver tempo); deploy em cloud e ajustes finais |

---

## 📋 Sprint Backlog e Alocação

### 🏃‍♂️ Sprint 1 (🔄 Em andamento)

**Período:** [data início] – [data fim]

📄 **[Ver alocação detalhada e burndown → SPRINT_1.md](./SPRINT_1.md)**

**User Stories:**

* 🔄 **US 1:** Agendamento de manutenções preventivas e corretivas (13 SP)
* 🔄 **US 2:** Checklist de execução de manutenção (8 SP)

**Progress:** 21 SP total | ✅ 0 concluídas | 🔄 2 em progresso | 📋 0 a fazer

#### 📉 Burndown — Sprint 1

| Dia | SP Restantes (Ideal) | SP Restantes (Real) |
| --- | --- | --- |
| Dia 1 | 21 | 21 |
| Dia 2 | 18 | - |
| Dia 3 | 15 | - |
| Dia 4 | 12 | - |
| Dia 5 | 9 | - |
| Dia 6 | 6 | - |
| Dia 7 | 3 | - |
| Dia 8 | 0 | - |

> 📌 Preencher a coluna **Real** ao final de cada dia de trabalho.

---

### 🏃‍♂️ Sprint 2 (📋 Aguardando)

**Período:** [data início] – [data fim]

📄 **[Ver alocação detalhada e burndown → SPRINT_2.md](./SPRINT_2.md)**

---

### 🏃‍♂️ Sprint 3 (📋 Aguardando)

**Período:** [data início] – [data fim]

📄 **[Ver alocação detalhada e burndown → SPRINT_3.md](./SPRINT_3.md)**

---

## 👥 Equipe

| FUNÇÃO | NOME | REDES SOCIAIS |
| --- | --- | --- |
| Product Owner | [Nome] | [LinkedIn](#) [GitHub](#) |
| Scrum Master | [Nome] | [LinkedIn](#) [GitHub](#) |
| Developer | [Nome] | [LinkedIn](#) [GitHub](#) |
| Developer | [Nome] | [LinkedIn](#) [GitHub](#) |
| DBA | [Nome] | [LinkedIn](#) [GitHub](#) |
| DBA | [Nome] | [LinkedIn](#) [GitHub](#) |

---

## 📜 Padrões e Convenções

### Estratégia de Branches (GitFlow Simplificado)

```
main            → Código estável/produção
sprint-N        → Branch de cada sprint (ex: sprint-1, sprint-2)
feature/nome    → Features individuais (ex: feature/cadastro-equipamentos)
```

**Fluxo:**

1. Criar branch `feature/nome` a partir de `main`
2. Desenvolver e commitar seguindo o padrão
3. Abrir PR para `main`
4. Após aprovação, merge para `main`

### Padrão de Commits (Conventional Commits)

```
tipo(escopo): descrição curta

[corpo opcional]

[rodapé opcional]
```

**Tipos:**

* `feat`: Nova funcionalidade
* `fix`: Correção de bug
* `docs`: Documentação
* `style`: Formatação, lint
* `refactor`: Refatoração de código
* `test`: Testes
* `chore`: Tarefas de build, configs

**Exemplos:**

```
feat(backend): adicionar endpoint de agendamento de manutenção
fix(frontend): corrigir exibição do mapa GIS
docs(readme): atualizar instruções de instalação
refactor(backend): extrair lógica de cálculo de prioridade para serviço dedicado
```

---

🔥 **#GoLegacyTech** 🚀
