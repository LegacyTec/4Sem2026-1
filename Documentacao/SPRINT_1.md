# Sprint 1 — Setup & Core Business Logic

> **Período:** 24/03/2026 a 05/04/2026
> **Status:** ✅ CONCLUÍDA
> **Story Points Totais:** 21 SP

---

## 🎯 Objetivos da Sprint
1. **Ambiente:** Configuração do ecossistema de desenvolvimento (Backend Java/Frontend Next.js).
2. **Persistência:** Modelagem ER e implementação física em MySQL (Local e Railway).
3. **API REST:** Desenvolvimento do Core de Agendamento e Execução de Checklist.
4. **Interface:** Prototipação funcional e estruturação do Layout base no Frontend.

---

## 📋 Entregas Técnicas

### 🏗️ Backend (`altave-backend/`)
- **Arquitetura:** Spring Boot 3.2.5 com Java 17.
- **Mapeamento JPA:** Entidades `Ativo`, `Manutencao`, `ChecklistExecucao`, `Tecnico` e `Certificacao` mapeadas via Hibernate.
- **Business Logic:** Implementação de serviços para priorização automática de manutenções com base em criticidade.
- **Endpoints:** 
  - `GET /ativos`: Listagem de ativos e contratos.
  - `POST /manutencoes`: Agendamento de intervenções.
  - `POST /checklists/executar`: Registro de execução técnica.
- **Documentação:** Swagger UI integrado (`/swagger-ui/index.html`).

### 🎨 Frontend (`front-altave-main/`)
- **Framework:** Next.js 15 (App Router) + React 19.
- **UI/UX:** Tailwind CSS 4 para estilização atômica e Lucide React para iconografia.
- **Integração:** Client Axios configurado com interceptors para tratamento de erros.
- **Componentes:** Sidebar, Dashboard de Ativos e Formulário de Checklist (dados dinâmicos).

---

## 📈 Gráfico de Burndown (Sprint 1)

O Burndown reflete a conclusão integral dos **21 Story Points** planejados.

| Dia | SP Restantes (Ideal) | SP Restantes (Real) | Notas |
|-----|----------------------|----------------------|-------|
| 0 | 21 | 21 | Início do Setup |
| 1 | 18.4 | 21 | Configuração de Ambiente |
| 2 | 15.8 | 19 | Modelagem de Banco |
| 3 | 13.1 | 19 | Setup JPA concluído |
| 4 | 10.5 | 13 | CRUD de Ativos finalizado |
| 5 | 7.9 | 10 | Lógica de Manutenção iniciada |
| 6 | 5.3 | 5 | Checklist API concluída |
| 7 | 2.6 | 2 | Ajustes de Layout Frontend |
| 8 | 0 | 0 | **Entrega Final** |

### Visualização do Progresso
```text
SP (Story Points)
21 | * (Início)
18 |  \
15 |   \
12 |    \   * (Realizado)
 9 |     \  |
 6 |      \ |
 3 |       \|
 0 |________\* (Finalizado)
   0 1 2 3 4 5 6 7 8 (Dias)
```

---

## 🛠️ Stack Tecnológica Validada
- **Linguagem:** Java 17, TypeScript 5.
- **Frameworks:** Spring Boot 3.2, Next.js 15.
- **Banco de Dados:** MySQL 8.
- **Infraestrutura:** Git (Controle de Versão), Railway (Database).

---
🔥 **#GoLegacyTech** 🚀
