# Sistema de Gestão de Manutenções — Altave

> 🚀 **LegacyTech** | 4º Semestre - FATEC SJC | API — Sprint 1 🔄 EM ANDAMENTO

[Sobre](#-sobre-o-projeto) |
[Módulos](#-módulos-do-sistema) |
[Estrutura](#-estrutura-do-projeto) |
[Como Rodar](#️-como-rodar) |
[Backlog](#-product-backlog) |
[Sprints](#-sprints) |
[Padrões](#-padrões-e-convenções) |
[Equipe](#-equipe)

---

## 🎯 Sobre o Projeto

**Problema:** A Altave opera cerca de 100 sistemas distribuídos mundialmente, cada um exigindo manutenções periódicas conforme suas horas de uso. O controle dessas manutenções — que variam em duração e criticidade conforme a localização dos sistemas (de um dia para intervenções próximas até vários dias para regiões como a Ásia) — é feito de forma manual ou descentralizada, sem visibilidade adequada do histórico, agendamentos, criticidade e logística envolvida.

**Solução:** Sistema web para gestão de manutenções preventivas e corretivas, com cadastro e acompanhamento de equipamentos, controle de técnicos e suas competências, agendamento de visitas com checklist de execução, controle de ferramentas e materiais para viagem, e visualização geográfica da localização dos sistemas e técnicos.

> ⚠️ O sistema **não** cobre abertura de chamados. O foco é planejamento, alocação e rastreio do ciclo completo de manutenção.

🚀 [Link do Deploy — quando disponível]

---

## 🧩 Módulos do Sistema

O sistema é composto por cinco módulos funcionais. Cada um possui documentação detalhada com modelo de dados, regras de negócio e fluxos.

| # | Módulo | Responsabilidade | Documentação |
|---|--------|-----------------|--------------|
| 1 | **Ativos e Contratos** | Clientes, contratos, equipamentos, localização GIS | [📄 Ver doc](./Documentacao/modulo-ativos-contratos.md) |
| 2 | **Manutenções** | Ciclo de vida, geração automática de preventivas, priorização | [📄 Ver doc](./Documentacao/modulo-manutencoes.md) |
| 3 | **Técnicos e Competências** | Certificações (NR35, NR10...), disponibilidade, alocação por GIS | [📄 Ver doc](./Documentacao/modulo-tecnicos-competencias.md) |
| 4 | **Logística e Checklist** | Planejamento de viagem, retirada de ferramentas, checklist técnico | [📄 Ver doc](./Documentacao/modulo-logistica-checklist.md) |
| 5 | **Alertas e Notificações** | E-mails automáticos, notificações in-app, motor de regras | [📄 Ver doc](./Documentacao/modulo-alertas-notificacoes.md) |

### Arquitetura geral

```
┌─────────────────────────────────────────────────────────────┐
│                   Frontend — Vue.js                         │
│  Dashboard · Mapa GIS · Alertas · Checklist · Calendário    │
└───────────────────────┬─────────────────────────────────────┘
                        │ REST + WebSocket
┌───────────────────────▼─────────────────────────────────────┐
│           API RESTful + WebSocket — Spring Boot             │
│           Spring Security · JWT · JPA / Hibernate           │
└──────┬──────────┬──────────┬──────────┬──────────┬──────────┘
       │          │          │          │          │
  Ativos &   Manutenções  Técnicos   Logística  Alertas
  Contratos                          Checklist
       │          │          │          │          │
┌──────▼──────────▼──────────▼──────────▼──────────▼──────────┐
│                 Oracle Database (OCI)                       │
│          PL/SQL · DBMS_SCHEDULER · SDO_GEOMETRY             │
└─────────────────────────────────────────────────────────────┘
```

---

## 📁 Estrutura do Projeto

```
/
├── backend/                        # Código-fonte Spring Boot
│   ├── src/
│   └── README.md
├── frontend/                       # Código-fonte Vue.js
│   ├── src/
│   └── README.md
├── Documentacao/                   # Modelagem, diagramas, docs de módulos
│   ├── modulo-ativos-contratos.md
│   ├── modulo-manutencoes.md
│   ├── modulo-tecnicos-competencias.md
│   ├── modulo-logistica-checklist.md
│   ├── modulo-alertas-notificacoes.md
│   └── DER.md
├── Manuais/
│   ├── BackEnd.md
│   └── FrontEnd.md
├── Manual de Instalação.md
├── Manual do Usuario.md
├── Manual de Dados.md
├── SPRINT_1.md
├── SPRINT_2.md
├── SPRINT_3.md
└── SPRINT_4.md
```

### Repositórios

| Componente | Repositório | Deploy |
|-----------|-------------|--------|
| **Backend** | [🔗 Repositório](#) | [Plataforma de deploy] |
| **Frontend** | [🔗 Repositório](#) | [Plataforma de deploy] |
| **Boards** | [Board do Projeto](#) | — |

### Tecnologias

| Tecnologia | Uso |
|-----------|-----|
| **Spring Boot** | Backend — APIs RESTful, injeção de dependências, segurança |
| **Spring Data JPA / Hibernate** | Persistência e mapeamento objeto-relacional com Oracle |
| **Oracle Database (OCI)** | Banco relacional principal; PL/SQL + DBMS_SCHEDULER para automações |
| **Oracle Spatial** | Coordenadas GIS com `SDO_GEOMETRY`; cálculo de distância entre técnicos e ativos |
| **Vue.js** | Frontend — componentização, estado global, renderização condicional |
| **Axios** | Integração frontend ↔ API REST |
| **Leaflet** | Mapas interativos com localização de técnicos e ativos |
| **Vue Chart.js** | Gráficos e dashboards dinâmicos |
| **Spring Security + JWT** | Autenticação e autorização |
| **JavaMail / SMTP** | Disparo de e-mails de alerta automático |
| **Tailwind** | Biblioteca front end |

---

## ▶️ Como Rodar

Consulte o [Manual de Instalação](./Manual%20de%20Instalação.md) para instruções completas.

**Resumo:**

```bash
# Backend
./mvnw spring-boot:run

# Frontend
npm install && npm run dev
```

---

## 📚 Documentação Técnica

### Manuais

| Documento | Descrição | Link |
|-----------|-----------|------|
| **Manual de Instalação** | Setup completo do ambiente local e cloud | [📄 Ver](./Manual%20de%20Instalação.md) |
| **Manual de Dados** | DER, dicionário de dados, modelo lógico | [📄 Ver](./Manual%20de%20Dados.md) |
| **Manual do Usuário** | Guia de uso da plataforma | [📄 Ver](./Manual%20do%20Usuario.md) |

### Guias de Desenvolvimento

| Componente | Link |
|-----------|------|
| **Backend** | [📄 Ver guia](./Manuais/BackEnd.md) |
| **Frontend** | [📄 Ver guia](./Manuais/FrontEnd.md) |

---

## 📝 Requisitos

### Funcionais

| ID | Descrição | Sprint |
|----|-----------|--------|
| RF01 | Agendar manutenções preventivas e corretivas respeitando intervalo contratual e criticidade | 1 |
| RF02 | Registrar e acompanhar execução de manutenções por meio de checklist | 1 |
| RF03 | Controlar retirada e devolução de ferramentas para cada viagem (formulário de retirada) | 2 |
| RF04 | Visualizar localização geográfica de sistemas e técnicos em mapa GIS | 2 |
| RF05 | Consultar histórico completo de manutenções por equipamento | 2 |
| RF06 | Calcular e exibir previsão de chegada dos técnicos ao local de manutenção | 3 |
| RF07 | Avaliar impacto de manutenções urgentes sobre o planejamento vigente *(desejável)* | 3 |
| RF08 | Telas de cadastro de equipamentos, técnicos e contratos *(desejável)* | 3 |

### Não Funcionais

| ID | Descrição |
|----|-----------|
| RNF01 | Manual de Instalação no repositório Git (requisito Fatec — obrigatório) |
| RNF02 | Manual do Usuário disponível (requisito Fatec — obrigatório) |
| RNF03 | Documentação da API via Swagger / OpenAPI |
| RNF04 | Modelagem de banco de dados com DER e dicionário de dados |
| RNF05 | Sistema operacional em cloud (Oracle Cloud Infrastructure) |
| RNF06 | Backend: Spring Boot + Spring Security + Spring Data JPA + Oracle |
| RNF07 | Frontend: Vue.js + Axios + Leaflet + Vue Chart.js |

---

## ⭐ Product Backlog

| Rank | Prioridade | User Story | SP | Sprint |
|------|-----------|------------|----|--------|
| 1 | 🔴 Alta | Como **gestor**, quero **agendar manutenções preventivas e corretivas** para que **o intervalo contratual seja respeitado e as prioridades sejam atendidas**. | 13 | 1 |
| 2 | 🔴 Alta | Como **técnico**, quero **acessar e preencher o checklist de manutenção** para que **a execução seja padronizada e registrada corretamente**. | 8 | 1 |
| 3 | 🔴 Alta | Como **gestor**, quero **controlar a retirada de ferramentas e equipamentos para a viagem** para que **extravios sejam evitados e a logística seja rastreável**. | 8 | 2 |
| 4 | 🔴 Alta | Como **gestor**, quero **visualizar a localização dos sistemas e técnicos em um mapa** para que **eu possa tomar decisões logísticas com base na posição geográfica**. | 13 | 2 |
| 5 | 🟡 Média | Como **gestor**, quero **consultar o histórico de manutenções por equipamento** para que **eu tenha rastreabilidade completa das intervenções realizadas**. | 5 | 2 |
| 6 | 🟡 Média | Como **gestor**, quero **visualizar a previsão de chegada dos técnicos** para que **o planejamento das manutenções seja mais preciso**. | 8 | 3 |
| 7 | 🟡 Média | Como **gestor**, quero **avaliar o impacto de uma manutenção urgente no planejamento vigente** para que **eu possa reordenar prioridades sem comprometer outros atendimentos**. | 8 | 3 |
| 8 | 🟢 Baixa | Como **gestor**, quero **cadastrar equipamentos, técnicos e contratos** para que **os dados possam ser mantidos diretamente pelo sistema quando necessário**. | 13 | 3 *(se houver tempo)* |

**Total:** 76 SP

---

## 📆 Sprints

| Sprint | Período | Foco | SP |
|--------|---------|------|----|
| **Sprint 1** | [início] → [fim] | Agendamento de manutenções com criticidade e intervalos contratuais; checklist de execução; setup base (Spring Boot + Vue.js + Oracle) | 21 |
| **Sprint 2** | [início] → [fim] | Controle logístico de ferramentas; mapa GIS com Leaflet; histórico de manutenções por equipamento | 26 |
| **Sprint 3** | [início] → [fim] | Previsão de chegada; avaliação de impacto de urgências; cadastros; deploy cloud; ajustes finais | 29 |

---

## 📋 Sprint Backlog e Alocação

### 🏃 Sprint 1 — 🔄 Em andamento

**Período:** [data início] – [data fim]

📄 [Ver alocação detalhada e burndown → SPRINT_1.md](./SPRINT_1.md)

**User Stories desta sprint:**

- 🔄 **US01** — Agendamento de manutenções preventivas e corretivas (13 SP)
- 🔄 **US02** — Checklist de execução de manutenção (8 SP)

**Progress:** 21 SP total | ✅ 0 concluídas | 🔄 2 em progresso | 📋 0 a fazer

#### Burndown — Sprint 1

| Dia | SP Restantes (Ideal) | SP Restantes (Real) |
|-----|---------------------|---------------------|
| Dia 1 | 21 | 21 |
| Dia 2 | 18 | — |
| Dia 3 | 15 | — |
| Dia 4 | 12 | — |
| Dia 5 | 9 | — |
| Dia 6 | 6 | — |
| Dia 7 | 3 | — |
| Dia 8 | 0 | — |

> 📌 Preencher a coluna **Real** ao final de cada dia de trabalho.

---

### 🏃 Sprint 2 — 📋 Aguardando

**Período:** [data início] – [data fim]

📄 [Ver alocação detalhada e burndown → SPRINT_2.md](./SPRINT_2.md)

---

### 🏃 Sprint 3 — 📋 Aguardando

**Período:** [data início] – [data fim]

📄 [Ver alocação detalhada e burndown → SPRINT_3.md](./SPRINT_3.md)

---

## 👥 Equipe

| Função | Nome | Redes |
|--------|------|-------|
| Product Owner | [Nome] | [LinkedIn](#) · [GitHub](#) |
| Scrum Master | [Nome] | [LinkedIn](#) · [GitHub](#) |
| Developer | [Nome] | [LinkedIn](#) · [GitHub](#) |
| Developer | [Nome] | [LinkedIn](#) · [GitHub](#) |
| DBA | [Nome] | [LinkedIn](#) · [GitHub](#) |
| DBA | [Nome] | [LinkedIn](#) · [GitHub](#) |

---

## 📜 Padrões e Convenções

### Estratégia de Branches (GitFlow Simplificado)

```
main            → Código estável / produção
sprint-N        → Branch de cada sprint (ex: sprint-1, sprint-2)
feature/nome    → Features individuais (ex: feature/agendamento-manutencao)
```

**Fluxo:**

1. Criar branch `feature/nome` a partir de `main`
2. Desenvolver e commitar seguindo o padrão abaixo
3. Abrir PR para `main`
4. Após aprovação, merge para `main`

### Padrão de Commits (Conventional Commits)

```
tipo(escopo): descrição curta em minúsculas

[corpo opcional — contexto e motivação da mudança]

[rodapé opcional — ex: closes #12]
```

**Tipos:**

| Tipo | Uso |
|------|-----|
| `feat` | Nova funcionalidade |
| `fix` | Correção de bug |
| `docs` | Documentação |
| `style` | Formatação, lint (sem mudança de lógica) |
| `refactor` | Refatoração sem nova feature nem bugfix |
| `test` | Adição ou ajuste de testes |
| `chore` | Configs de build, dependências |

**Exemplos:**

```bash
feat(backend): adicionar endpoint de agendamento de manutenção
fix(frontend): corrigir exibição do mapa GIS no mobile
docs(modulo-manutencoes): adicionar fluxo de geração automática de preventivas
refactor(backend): extrair lógica de priorização para serviço dedicado
chore(ci): configurar deploy automático na OCI
```

---

🔥 **#GoLegacyTech** 🚀
