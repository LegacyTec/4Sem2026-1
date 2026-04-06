# Sistema de Gestão de Manutenções — Altave

> 🚀 **LegacyTech** | 4º Semestre - FATEC SJC | API — Sprint 1 🔄 CONCLUÍDA

<p align="center">
    <a href="#-sobre-o-projeto">Sobre</a> |
    <a href="#-estrutura-do-projeto">Estrutura</a> |
    <a href="#-como-rodar">Como Rodar</a> |
    <a href="#-product-backlog">Backlog</a> |
    <a href="#-sprints">Sprints</a> |
    <a href="#-padrões-e-convenções">Padrões</a> |
    <a href="#-equipe">Equipe</a>
</p>

---

## 🎯 Sobre o Projeto

Sistema web para gestão de manutenções preventivas e corretivas da Altave. O foco é o planejamento, alocação de técnicos por competência e rastreio do ciclo completo de manutenção através de checklists dinâmicos.

**Problema:** Controle manual de ~100 sistemas mundiais, gerando falta de visibilidade sobre histórico, agendamentos e logística complexa.

**Solução:** Centralização da gestão de ativos, automação de cronogramas de manutenção e suporte logístico para equipes de campo.

<h1 align="center"> 🚀 <a href="https://front-altave.vercel.app/">https://front-altave.vercel.app/</a> </h1>

---

## 📁 Estrutura do Projeto

```
/
├── altave-backend/                 # 📦 API Spring Boot (Java 17)
│   ├── src/main/java/              # Lógica de negócio
│   ├── src/main/resources/         # Configurações e SQL
│   └── pom.xml                     # Dependências Maven
├── front-altave-main/              # 📦 Frontend Next.js 15 (TypeScript)
│   ├── app/                        # App Router (React 19)
│   ├── public/                     # Ativos estáticos
│   └── package.json                # Dependências NPM
├── Documentacao/                   # Requisitos e especificações
├── modelo_de_dados/                # DER e Dicionário de Dados
├── SPRINT_1.md                     # Relatório detalhado da Sprint 1
└── README.md                       # Documentação Principal
```

### 📦 Repositórios e Deploy

| Componente | Link Original | Cópia Local | Deploy |
|------------|---------------|-------------|--------|
| **Backend** | [🔗 Repositório](https://github.com/cleberkirch86/4Sem2026-1/tree/main/altave-backend) | [`/altave-backend`](./altave-backend) | Railway |
| **Frontend** | [🔗 Repositório](https://github.com/cleberkirch86/4Sem2026-1/tree/main/front-altave-main) | [`/front-altave-main`](./front-altave-main) | Vercel |

### 🛠️ Tecnologias

| Tecnologia | Uso |
|------------|-----|
| **Java 17 + Spring Boot 3** | API REST, JPA/Hibernate |
| **MySQL 8** | Banco de dados relacional |
| **Next.js 15 + React 19** | Interface do usuário (App Router) |
| **Tailwind CSS v4** | Estilização utilitária |
| **Lucide React** | Iconografia |

---

## ▶️ Como Rodar

Consulte o [Manual de Instalação](./Manual%20de%20Instalação.md) para instruções completas.

**Resumo:**
- **Backend:** `cd altave-backend && ./mvnw spring-boot:run`
- **Frontend:** `cd front-altave-main && npm install && npm run dev`

---

## 📚 Documentação Técnica

### 📝 Manuais de Uso

| Documento | Descrição | Link |
|-----------|-------------|------|
| **Manual de Instalação** | Guia completo de setup do ambiente | [📄 Ver manual](./Manual%20de%20Instalação.md) |
| **Manual de Dados** | Estrutura do banco de dados, DER, dicionário | [📄 Ver manual](./Manual%20de%20Dados.md) |
| **Manual do Usuário** | Guia de uso da plataforma | [📄 Ver manual](./manual%20do%20usuario.md) |

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

**Requisitos Não Funcionais**
| ID | Descrição | Sprint |
|----|-----------|--------|
| [RNF01] | Manual de Instalação disponível no repositório Git (requisito Fatec – obrigatório)
| [RNF02] | Manual do Usuário disponível (requisito Fatec – obrigatório)
|[RNF03] | Documentação da API (Swagger / OpenAPI)
| [RNF04] | Modelagem de Banco de Dados com DER e dicionário de dados
| [RNF05] | Sistema operacional em cloud (Oracle Cloud)
| [RNF06] | Backend desenvolvido com Spring Boot (Java) + Spring Security + Spring Data JPA com Oracle
| [RNF07] | Frontend desenvolvido com Vue.js + Axios + Leaflet/Vue Chart.js

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

---

## 🏃 Sprints e Progresso

### Sprint 1 — Concluída ✅
**Período:** 24/03/2026 a 05/04/2026

Para acessar o detalhamento técnico, as User Stories executadas e o **Gráfico de Burndown**, consulte:

📄 [**Relatório Completo da Sprint 1 → SPRINT_1.md**](./SPRINT_1.md)


### Sprint 2 
Controle logístico de ferramentas e viagem; visualização GIS com Leaflet; histórico de manutenções por equipamento 

### Sprint 3
Previsão de chegada; avaliação de impacto de urgências; telas de cadastro (se houver tempo); deploy em cloud e ajustes finais

---

## 👥 Equipe

| FUNÇÃO | NOME | REDES SOCIAIS | FOTO |
| --- | --- | --- | --- |
| Product Owner | Pedro H. Mattos | [![Linkedin](https://img.shields.io/badge/Linkedin-blue?style=flat-square&logo=Linkedin&logoColor=white)]() [![GitHub](https://img.shields.io/badge/GitHub-111217?style=flat-square&logo=github&logoColor=white)]() |<p align="center"><img src="https://github.com/user-attachments/assets/8108bdb8-c9d3-473e-9800-da1286cc91e5" alt="Pedro" style="width:60px;height:60px;">
| Scrum Master| Cleber Kirch | [![Linkedin](https://img.shields.io/badge/Linkedin-blue?style=flat-square&logo=Linkedin&logoColor=white)](https://www.linkedin.com/in/cleberkirch/) [![GitHub](https://img.shields.io/badge/GitHub-111217?style=flat-square&logo=github&logoColor=white)](https://github.com/cleberkirch86/) |<p align="center"><img src="https://github.com/user-attachments/assets/9683e19f-7d59-4273-8a08-8cbddadcf2c8" alt="Cleber" style="width:60px;height:60px;">
| Developer | Ed Wilson | [![Linkedin](https://img.shields.io/badge/Linkedin-blue?style=flat-square&logo=Linkedin&logoColor=white)]() [![GitHub](https://img.shields.io/badge/GitHub-111217?style=flat-square&logo=github&logoColor=white)]() |<p align="center"><img src="https://github.com/user-attachments/assets/32812572-8636-43e6-bc31-88844c1fe8c3" alt="Ed" style="width:60px;height:60px;">
| DBA | Diego Vitvicki | [![Linkedin](https://img.shields.io/badge/Linkedin-blue?style=flat-square&logo=Linkedin&logoColor=white)](https://www.linkedin.com/in/diegovitvicki/) [![GitHub](https://img.shields.io/badge/GitHub-111217?style=flat-square&logo=github&logoColor=white)](https://github.com/dievit) |<p align="center"><img src="https://github.com/user-attachments/assets/947ab778-da15-455e-b3b7-bd90b4758dfc" alt="Diego" style="width:60px;height:60px;">
| Developer | Johnatan | [![Linkedin](https://img.shields.io/badge/Linkedin-blue?style=flat-square&logo=Linkedin&logoColor=white)]() [![GitHub](https://img.shields.io/badge/GitHub-111217?style=flat-square&logo=github&logoColor=white)]() |<p align="center"><img src="https://via.placeholder.com/60" alt="Johnatan" style="width:60px;height:60px;">

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
- `refactor`: Refatoração de código
- `chore`: Configs de build/dependências

---

🔥 **#GoLegacyTech** 🚀
