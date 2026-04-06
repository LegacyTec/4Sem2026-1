# Sistema de Gestão de Manutenções — Altave

> 🚀 **LegacyTech** | 4º Semestre - FATEC SJC | API — Sprint 1 🔄 CONCLUÍDA

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

**Problema:** A Altave opera cerca de 100 sistemas distribuídos mundialmente, exigindo manutenções periódicas. O controle manual gera falta de visibilidade sobre histórico, agendamentos e logística.

**Solução:** Sistema web para gestão de manutenções preventivas e corretivas, com acompanhamento de ativos, competências técnicas, checklists de execução e visualização geográfica.

🚀 [Link do Deploy — Railway/Vercel]

---

## 🧩 Módulos do Sistema

| # | Módulo | Responsabilidade | Documentação |
|---|--------|-----------------|--------------|
| 1 | **Ativos e Contratos** | Clientes, equipamentos, localização GIS | [📄 Ver doc](./Documentacao/modulo-ativos-contratos.md) |
| 2 | **Manutenções** | Ciclo de vida, geração automática, priorização | [📄 Ver doc](./Documentacao/modulo-manutencoes.md) |
| 3 | **Técnicos e Competências** | Certificações, disponibilidade, alocação | [📄 Ver doc](./Documentacao/modulo-tecnicos-competencias.md) |
| 4 | **Logística e Checklist** | Planejamento de viagem, checklist técnico | [📄 Ver doc](./Documentacao/modulo-logistica-checklist.md) |
| 5 | **Alertas e Notificações** | E-mails automáticos, notificações in-app | [📄 Ver doc](./Documentacao/modulo-alertas-notificacoes.md) |

### Arquitetura Atualizada

```
┌─────────────────────────────────────────────────────────────┐
│                 Frontend — Next.js 15                       │
│  React 19 · Tailwind 4 · Shadcn/UI · Lucide · Axios         │
└───────────────────────┬─────────────────────────────────────┘
                        │ REST API
┌───────────────────────▼─────────────────────────────────────┐
│           Backend — Spring Boot 3.2 (Java 17)               │
│      Spring Data JPA · Hibernate · Lombok · OpenAPI         │
└──────┬──────────┬──────────┬──────────┬──────────┬──────────┘
       │          │          │          │          │
  Ativos &   Manutenções  Técnicos   Logística  Alertas
       │          │          │          │          │
┌──────▼──────────▼──────────▼──────────▼──────────▼──────────┐
│                 MySQL Database (Railway)                    │
│           Persistência Relacional · Flyway (em breve)       │
└─────────────────────────────────────────────────────────────┘
```

---

## 📁 Estrutura do Projeto

```
/
├── altave-backend/                 # API Spring Boot
├── front-altave-main/              # Frontend Next.js
├── Documentacao/                   # Requisitos e especificações
├── modelo_de_dados/                # DER e Dicionário de Dados
├── SPRINT_1.md                     # Relatório da Sprint Atual
└── README.md                       # Documentação Principal
```

---

## ▶️ Como Rodar

Consulte o [Manual de Instalação](./Manual%20de%20Instalação.md) para instruções completas.

**Resumo:**

```bash
# Backend
cd altave-backend
./mvnw spring-boot:run

# Frontend
cd front-altave-main
npm install && npm run dev
```

---

## 📝 Requisitos

### Funcionais

| ID | Descrição | Sprint |
|----|-----------|--------|
| RF01 | Agendar manutenções preventivas e corretivas respeitando intervalo contratual e criticidade | 1 |
| RF02 | Registrar e acompanhar execução de manutenções por meio de checklist | 1 |
| RF03 | Controlar retirada e devolução de ferramentas para cada viagem | 2 |
| RF04 | Visualizar localização geográfica de sistemas e técnicos em mapa GIS | 2 |
| RF05 | Consultar histórico completo de manutenções por equipamento | 2 |
| RF06 | Calcular e exibir previsão de chegada dos técnicos ao local de manutenção | 3 |
| RF07 | Avaliar impacto de manutenções urgentes sobre o planejamento vigente | 3 |
| RF08 | Telas de cadastro de equipamentos, técnicos e contratos | 3 |

### Não Funcionais

| ID | Descrição |
|----|-----------|
| RNF01 | Manual de Instalação no repositório Git |
| RNF02 | Manual do Usuário disponível |
| RNF03 | Documentação da API via Swagger / OpenAPI |
| RNF04 | Modelagem de banco de dados com DER e dicionário de dados |
| RNF05 | Backend: Spring Boot + JPA + MySQL (Railway) |
| RNF06 | Frontend: Next.js + Tailwind + Axios |

---

## ⭐ Product Backlog

| Rank | Prioridade | User Story | SP | Sprint |
|------|-----------|------------|----|--------|
| 1 | 🔴 Alta | Agendamento de manutenções preventivas e corretivas | 13 | 1 |
| 2 | 🔴 Alta | Checklist de execução de manutenção | 8 | 1 |
| 3 | 🔴 Alta | Controle de retirada de ferramentas | 8 | 2 |
| 4 | 🔴 Alta | Localização de sistemas e técnicos em mapa | 13 | 2 |
| 5 | 🟡 Média | Histórico de manutenções por equipamento | 5 | 2 |
| 6 | 🟡 Média | Previsão de chegada dos técnicos | 8 | 3 |
| 7 | 🟡 Média | Impacto de manutenção urgente no planejamento | 8 | 3 |
| 8 | 🟢 Baixa | Cadastro de equipamentos, técnicos e contratos | 13 | 3 |

---

## 🏃 Sprints e Progresso

### Sprint 1 — Concluída ✅
Para acessar o detalhamento técnico, as User Stories executadas e o **Gráfico de Burndown**, consulte o documento dedicado:

📄 [**Relatório Completo da Sprint 1 → SPRINT_1.md**](./SPRINT_1.md)

---

## 👥 Equipe

| Função | Nome | Redes |
|--------|------|-------|
| Product Owner | [Nome] | [LinkedIn](#) · [GitHub](#) |
| Scrum Master | [Nome] | [LinkedIn](#) · [GitHub](#) |
| Developer | Cleber | [LinkedIn](#) · [GitHub](#) |

---

## 📜 Padrões e Convenções

### Estratégia de Branches (GitFlow Simplificado)
```
main            → Código estável / produção
sprint-N        → Branch de cada sprint
feature/nome    → Features individuais
```

### Padrão de Commits (Conventional Commits)
- `feat`: Nova funcionalidade
- `fix`: Correção de bug
- `docs`: Documentação
- `style`: Formatação, lint
- `refactor`: Refatoração

---

🔥 **#GoLegacyTech** 🚀
