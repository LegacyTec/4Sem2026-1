## \# Sistema de Gestão de Manutenções — Altave

## 

## 🚀 \*\*LegacyTech\*\* | 4º Semestre - FATEC SJC | API — Sprint 1 🔄 EM ANDAMENTO

## 

## \[Sobre](#-sobre-o-projeto) |

## \[Módulos](#-módulos-do-sistema) |

## \[Estrutura](#-estrutura-do-projeto) |

## \[Como Rodar](#️-como-rodar) |

## \[Backlog](#-product-backlog) |

## \[Sprints](#-sprints) |

## \[Padrões](#-padrões-e-convenções) |

## \[Equipe](#-equipe)

## 

## \---

## 

## \## 🎯 Sobre o Projeto

## 

## \*\*Problema:\*\* A Altave opera cerca de 100 sistemas distribuídos mundialmente, cada um exigindo manutenções periódicas conforme suas horas de uso. O controle dessas manutenções — que variam em duração e criticidade conforme a localização dos sistemas (de um dia para intervenções próximas até vários dias para regiões como a Ásia) — é feito de forma manual ou descentralizada, sem visibilidade adequada do histórico, agendamentos, criticidade e logística envolvida.

## 

## \*\*Solução:\*\* Sistema web para gestão de manutenções preventivas e corretivas, com cadastro e acompanhamento de equipamentos, controle de técnicos e suas competências, agendamento de visitas com checklist de execução, controle de ferramentas e materiais para viagem, e visualização geográfica da localização dos sistemas e técnicos.

## 

## ⚠️ O sistema \*\*não\*\* cobre abertura de chamados. O foco é planejamento, alocação e rastreio do ciclo completo de manutenção.

## 

## 🚀 \[Link do Deploy — quando disponível]

## 

## \---

## 

## \## 🧩 Módulos do Sistema

## 

## O sistema é composto por cinco módulos funcionais. Cada um possui documentação detalhada com modelo de dados, regras de negócio e fluxos.

## 

## | # | Módulo | Responsabilidade | Documentação |

## |---|--------|-----------------|--------------|

## | 1 | \*\*Ativos e Contratos\*\* | Clientes, contratos, equipamentos, localização GIS | \[📄 Ver doc](./Documentacao/modulo-ativos-contratos.md) |

## | 2 | \*\*Manutenções\*\* | Ciclo de vida, geração automática de preventivas, priorização | \[📄 Ver doc](./Documentacao/modulo-manutencoes.md) |

## | 3 | \*\*Técnicos e Competências\*\* | Certificações (NR35, NR10...), disponibilidade, alocação por GIS | \[📄 Ver doc](./Documentacao/modulo-tecnicos-competencias.md) |

## | 4 | \*\*Logística e Checklist\*\* | Planejamento de viagem, retirada de ferramentas, checklist técnico | \[📄 Ver doc](./Documentacao/modulo-logistica-checklist.md) |

## | 5 | \*\*Alertas e Notificações\*\* | E-mails automáticos, notificações in-app, motor de regras | \[📄 Ver doc](./Documentacao/modulo-alertas-notificacoes.md) |

## 

## \### Arquitetura geral

## 

## ┌─────────────────────────────────────────────────────────────┐

## │                   Frontend — Next.js / React                │

## │  Dashboard · Mapa GIS · Alertas · Checklist · Calendário    │

## └───────────────────────┬─────────────────────────────────────┘

## &#x20;                       │ REST + WebSocket

## ┌───────────────────────▼─────────────────────────────────────┐

## │           API RESTful + WebSocket — Spring Boot             │

## │           Spring Security · JWT · JPA / Hibernate           │

## └──────┬──────────┬──────────┬──────────┬──────────┬──────────┘

## &#x20;      │          │          │          │          │

## &#x20; Ativos \&   Manutenções  Técnicos   Logística  Alertas

## &#x20; Contratos                          Checklist

## &#x20;      │          │          │          │          │

## ┌──────▼──────────▼──────────▼──────────▼──────────▼──────────┐

## │                 Oracle Database (OCI)                       │

## │          PL/SQL · DBMS\_SCHEDULER · SDO\_GEOMETRY             │

## └─────────────────────────────────────────────────────────────┘

## 

## \---

## 

## \## 📁 Estrutura do Projeto

## 

## /

## ├── backend/                        # Código-fonte Spring Boot

## │   ├── src/

## │   └── README.md

## ├── frontend/                       # Código-fonte Next.js (App Router)

## │   ├── app/

## │   ├── dev\_resources/

## │   └── README.md

## ├── Documentacao/                   # Modelagem, diagramas, docs de módulos

## │   ├── modulo-ativos-contratos.md

## │   ├── modulo-manutencoes.md

## │   ├── modulo-tecnicos-competencias.md

## │   ├── modulo-logistica-checklist.md

## │   ├── modulo-alertas-notificacoes.md

## │   └── DER.md

## ├── Manuais/

## │   ├── BackEnd.md

## │   └── FrontEnd.md

## ├── Manual de Instalação.md

## ├── Manual do Usuario.md

## ├── Manual de Dados.md

## ├── SPRINT\_1.md

## ├── SPRINT\_2.md

## ├── SPRINT\_3.md

## └── SPRINT\_4.md

## 

## \### Repositórios

## 

## | Componente | Repositório | Deploy |

## |-----------|-------------|--------|

## | \*\*Backend\*\* | \[🔗 Repositório](#) | \[Plataforma de deploy] |

## | \*\*Frontend\*\* | \[🔗 Repositório](#) | \[Plataforma de deploy] |

## | \*\*Boards\*\* | \[Board do Projeto](#) | — |

## 

## \### Tecnologias

## 

## | Tecnologia | Uso |

## |-----------|-----|

## | \*\*Spring Boot\*\* | Backend — APIs RESTful, injeção de dependências, segurança |

## | \*\*Spring Data JPA / Hibernate\*\* | Persistência e mapeamento objeto-relacional com Oracle |

## | \*\*Oracle Database (OCI)\*\* | Banco relacional principal; PL/SQL + DBMS\_SCHEDULER para automações |

## | \*\*Next.js 16 / React 19\*\* | Frontend — framework principal, App Router, componentização |

## | \*\*Tailwind CSS v4\*\* | Estilização utilitária |

## | \*\*Zustand\*\* | Gerenciamento de estado global no React |

## | \*\*React Hook Form + Zod\*\* | Gerenciamento e validação de formulários |

## | \*\*Axios\*\* | Integração frontend ↔ API REST |

## 

## \---

## 

## \## ▶️ Como Rodar

## 

## Consulte o \[Manual de Instalação](./Manual%20de%20Instalação.md) para instruções completas.

## 

## \*\*Resumo:\*\*

## 

## \# Backend

## ./mvnw spring-boot:run

## 

## \# Frontend (na pasta front-altave-main)

## npm install \&\& npm run dev

## 

## \---

## 

## \## 📚 Documentação Técnica

## 

## \### Manuais

## 

## | Documento | Descrição | Link |

## |-----------|-----------|------|

## | \*\*Manual de Instalação\*\* | Setup completo do ambiente local e cloud | \[📄 Ver](./Manual%20de%20Instalação.md) |

## | \*\*Manual de Dados\*\* | DER, dicionário de dados, modelo lógico | \[📄 Ver](./Manual%20de%20Dados.md) |

## | \*\*Manual do Usuário\*\* | Guia de uso da plataforma | \[📄 Ver](./Manual%20do%20Usuario.md) |

## 

## \### Guias de Desenvolvimento

## 

## | Componente | Link |

## |-----------|------|

## | \*\*Backend\*\* | \[📄 Ver guia](./Manuais/BackEnd.md) |

## | \*\*Frontend\*\* | \[📄 Ver guia](./front-altave-main/dev\_resources/README.md) |

## 

## \---

## 

## \## 📝 Requisitos

## 

## \### Funcionais

## 

## | ID | Descrição | Sprint |

## |----|-----------|--------|

## | RF01 | Agendar manutenções preventivas e corretivas respeitando intervalo contratual e criticidade | 1 |

## | RF02 | Registrar e acompanhar execução de manutenções por meio de checklist | 1 |

## | RF03 | Controlar retirada e devolução de ferramentas para cada viagem (formulário de retirada) | 2 |

## | RF04 | Visualizar localização geográfica de sistemas e técnicos em mapa GIS | 2 |

## | RF05 | Consultar histórico completo de manutenções por equipamento | 2 |

## | RF06 | Calcular e exibir previsão de chegada dos técnicos ao local de manutenção | 3 |

## | RF07 | Avaliar impacto de manutenções urgentes sobre o planejamento vigente \*(desejável)\* | 3 |

## | RF08 | Telas de cadastro de equipamentos, técnicos e contratos \*(desejável)\* | 3 |

## 

## \### Não Funcionais

## 

## | ID | Descrição |

## |----|-----------|

## | RNF01 | Manual de Instalação no repositório Git (requisito Fatec — obrigatório) |

## | RNF02 | Manual do Usuário disponível (requisito Fatec — obrigatório) |

## | RNF03 | Documentação da API via Swagger / OpenAPI |

## | RNF04 | Modelagem de banco de dados com DER e dicionário de dados |

## | RNF05 | Sistema operacional em cloud (Oracle Cloud Infrastructure) |

## | RNF06 | Backend: Spring Boot + Spring Data JPA + Oracle |

## | RNF07 | Frontend: Next.js + Tailwind CSS + Axios |

## 

## \---

## 

## \## ⭐ Product Backlog

## 

## | Rank | Prioridade | User Story | SP | Sprint |

## |------|-----------|------------|----|--------|

## | 1 | 🔴 Alta | Como \*\*gestor\*\*, quero \*\*agendar manutenções preventivas e corretivas\*\* para que \*\*o intervalo contratual seja respeitado e as prioridades sejam atendidas\*\*. | 13 | 1 |

## | 2 | 🔴 Alta | Como \*\*técnico\*\*, quero \*\*acessar e preencher o checklist de manutenção\*\* para que \*\*a execução seja padronizada e registrada corretamente\*\*. | 8 | 1 |

## | 3 | 🔴 Alta | Como \*\*gestor\*\*, quero \*\*controlar a retirada de ferramentas e equipamentos para a viagem\*\* para que \*\*extravios sejam evitados e a logística seja rastreável\*\*. | 8 | 2 |

## | 4 | 🔴 Alta | Como \*\*gestor\*\*, quero \*\*visualizar a localização dos sistemas e técnicos em um mapa\*\* para que \*\*eu possa tomar decisões logísticas com base na posição geográfica\*\*. | 13 | 2 |

## | 5 | 🟡 Média | Como \*\*gestor\*\*, quero \*\*consultar o histórico de manutenções por equipamento\*\* para que \*\*eu tenha rastreabilidade completa das intervenções realizadas\*\*. | 5 | 2 |

## | 6 | 🟡 Média | Como \*\*gestor\*\*, quero \*\*visualizar a previsão de chegada dos técnicos\*\* para que \*\*o planejamento das manutenções seja mais preciso\*\*. | 8 | 3 |

## | 7 | 🟡 Média | Como \*\*gestor\*\*, quero \*\*avaliar o impacto de uma manutenção urgente no planejamento vigente\*\* para que \*\*eu possa reordenar prioridades sem comprometer outros atendimentos\*\*. | 8 | 3 |

## | 8 | 🟢 Baixa | Como \*\*gestor\*\*, quero \*\*cadastrar equipamentos, técnicos e contratos\*\* para que \*\*os dados possam ser mantidos diretamente pelo sistema quando necessário\*\*. | 13 | 3 \*(se houver tempo)\* |

## 

## \*\*Total:\*\* 76 SP

## 

## \---

## 

## \## 📆 Sprints

## 

## | Sprint | Período | Foco | SP |

## |--------|---------|------|----|

## | \*\*Sprint 1\*\* | 24/03/2026 → 05/04/2026 | Agendamento de manutenções com criticidade; checklist de execução; setup base (Spring Boot + Next.js + Oracle) | 21 |

## | \*\*Sprint 2\*\* | \[início] → \[fim] | Controle logístico de ferramentas; mapa GIS; histórico de manutenções por equipamento | 26 |

## | \*\*Sprint 3\*\* | \[início] → \[fim] | Previsão de chegada; avaliação de impacto de urgências; cadastros; deploy cloud; ajustes finais | 29 |

## 

## \---

## 

## \## 👥 Equipe

## 

## | Função | Nome | Redes |

## |--------|------|-------|

## | Product Owner | \[Nome] | \[LinkedIn](#) · \[GitHub](#) |

## | Scrum Master | \[Nome] | \[LinkedIn](#) · \[GitHub](#) |

## | Developer | \[Nome] | \[LinkedIn](#) · \[GitHub](#) |

## | Developer | \[Nome] | \[LinkedIn](#) · \[GitHub](#) |

## | DBA | \[Nome] | \[LinkedIn](#) · \[GitHub](#) |

## | DBA | \[Nome] | \[LinkedIn](#) · \[GitHub](#) |

## 

## \---

## 

## 🔥 \*\*#GoLegacyTech\*\* 🚀

