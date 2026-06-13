# Sprint 3 — KPIs, Dashboard e Gestão de Ordens de Manutenção (Supervisor)

## User Story  — Dashboard de KPIs, Gráfico, Alertas e Ordens

| Rank | Prioridade | User Story | SP | Sprint |
|------|-----------|------------|----|--------|
| 10 | 🟡 Média | Como supervisor, quero visualizar KPIs, gráficos, ativos em alerta e ordens em andamento, para acompanhar a situação operacional em tempo real. | 5 | 3 |


### Critérios de Aceitação

#### KPIs do Dashboard

- O dashboard deve exibir cards com os principais indicadores da operação.
- Os KPIs obrigatórios são:
  - Ativos operando;
  - Ativos em manutenção;
  - MTTR médio (baseado em ordens corretivas concluídas);
  - MTBF médio (tempo médio entre falhas por ativo);
  - Ordens atrasadas;
  - Ativo com maior número de manutenções.
- Os dados devem ser claros, objetivos e visualmente destacados.
- O card de ordens atrasadas deve indicar alerta visual quando houver pendências.

#### Gráfico Preventiva x Corretiva

- Deve existir um gráfico comparando manutenções preventivas e corretivas.
- O gráfico deve considerar os últimos 6 meses.
- Deve possuir legenda clara para cada tipo de manutenção.
- As cores devem diferenciar preventivas e corretivas.
- O eixo horizontal deve representar os meses.
- Os dados devem ser atualizados conforme registros de ordens.
- Deve tratar ausência de dados (estado vazio).

#### Ativos em Alerta

- A lista deve exibir ativos com situação crítica ou manutenção atrasada.
- Cada item deve conter:
  - Nome do ativo;
  - Tipo de manutenção;
  - Data do último PM;
  - Status.
- Ativos atrasados devem ter destaque visual.
- Deve existir contador de ativos em alerta.
- Deve exibir estado vazio quando não houver alertas.

#### Ordens em Andamento

- O sistema deve exibir tabela com ordens de manutenção.
- A tabela deve conter:
  - Número da ordem;
  - Tipo de manutenção;
  - Ativo;
  - Técnico responsável;
  - Início previsto;
  - Prazo;
  - Status.
- Status devem ter destaque visual (Concluída / Pendente / Atrasada).
- A tabela deve ser clara e responsiva.


#### Filtros da Tabela

- Deve existir filtro por tipo de manutenção:
  - Todos;
  - Preventiva;
  - Corretiva.
- Deve existir filtro por status:
  - Todas;
  - Concluída;
  - Pendente;
  - Atrasada.
- Os filtros devem funcionar combinados.
- A atualização deve ocorrer sem reload da página.
- Deve existir estado vazio quando não houver resultados.

### Tarefas Técnicas

#### KPIs

- Criar componente de cards de KPIs.
- Integrar dados ao dashboard.
- Calcular ativos operando.
- Calcular ativos em manutenção.
- Calcular MTTR médio.
- Calcular MTBF médio.
- Calcular ordens atrasadas.
- Identificar ativo com mais manutenções.
- Implementar estados: loading, empty e error.

#### Gráfico

- Criar componente de gráfico Preventiva x Corretiva.
- Buscar ordens dos últimos 6 meses.
- Agrupar dados por mês.
- Separar por tipo de manutenção.
- Definir cores distintas.
- Implementar legenda.
- Tratar ausência de dados.
- Garantir responsividade.

#### Ativos em Alerta

- Criar componente de listagem de ativos em alerta.
- Criar regra de identificação de ativos críticos.
- Exibir nome, tipo, último PM e status.
- Implementar ordenação por criticidade.
- Criar estado vazio.

#### Ordens em Andamento

- Criar tabela de ordens.
- Integrar dados de ordens do sistema.
- Exibir todos os campos definidos.
- Implementar badges de status.
- Garantir responsividade.

#### Filtros

- Criar filtro por tipo de manutenção.
- Criar filtro por status.
- Aplicar filtros combinados.
- Atualizar tabela dinamicamente.
- Criar estado vazio para filtros.