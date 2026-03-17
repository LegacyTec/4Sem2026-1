# Módulo 2 — Manutenções

[← Voltar ao README](../README.md)

## Responsabilidade

Coração do sistema. Gerencia o ciclo de vida completo de cada manutenção — da criação automática ou manual até o fechamento com histórico registrado — e aplica a lógica de priorização por criticidade, prazo e tipo.

---

## Tipos de manutenção e suas origens

| Tipo | Origem | Quem cria |
|------|--------|-----------|
| **Preventiva** | Regra do contrato atingida (prazo ou horas de uso) | Sistema automaticamente via `DBMS_SCHEDULER` |
| **Corretiva** | Falha ou anomalia detectada fora do ciclo previsto | Operador manualmente |

Após criadas, ambas percorrem o mesmo ciclo de vida.

---

## Ciclo de vida

```
[Preventiva gerada automaticamente]  [Corretiva registrada manualmente]
                  \                          /
                   ▼                        ▼
              ┌──────────┐           prazo excedido → [Vencida] ──→ alerta crítico
              │ Pendente │─────────────────────────────────────────────────▲
              └────┬─────┘
                   │ técnico alocado
                   ▼
              ┌──────────┐           conflito / cancelamento
              │  Alocada │──────────────────────────────────→ [Cancelada]
              └────┬─────┘
                   │ técnico confirma presença no local
                   ▼
           ┌──────────────┐
           │ Em execução  │──→ alerta disparado se prazo crítico
           └──────┬───────┘
                  │ checklist concluído + observações registradas
                  ▼
           ┌───────────┐
           │ Concluída │──→ histórico salvo + próxima preventiva agendada
           └───────────┘
```

**Status possíveis:** `pendente` · `alocada` · `em_execucao` · `concluida` · `cancelada` · `vencida`

---

## Geração automática de preventivas (PL/SQL)

Uma procedure agendada via `DBMS_SCHEDULER` roda diariamente. Para cada ativo com contrato vigente e status `operacional`, verifica as condições da regra de manutenção:

```
SE (SYSDATE - dt_ultima_manutencao) >= periodicidade_dias
   OU horas_uso_acumuladas >= limite_horas_uso
ENTÃO
   INSERT INTO manutencao (status = 'pendente', gerado_automaticamente = 'S')
   INSERT INTO alerta (tipo = 'manut_proxima')
```

A condição que disparar primeiro gera o registro. O sistema não cria duplicatas: antes de inserir, verifica se já existe manutenção pendente ou em andamento para aquele ativo.

---

## Lógica de priorização

A fila de manutenções pendentes é ordenada por um score composto calculado em view materializada, cruzando três fatores:

| Fator | Peso | Lógica |
|-------|------|--------|
| Criticidade do contrato | Alto | `alta = 3` · `media = 2` · `baixa = 1` |
| Proximidade do prazo | Alto | Quanto menos dias restam, maior o fator |
| Tipo de manutenção | Médio | Corretiva > Preventiva (falha já existente) |

```sql
score = (fator_criticidade * 3) + (fator_prazo * 3) + (fator_tipo * 2)
```

---

## Modelo de dados

### `manutencao`

| Coluna | Tipo | Descrição |
|--------|------|-----------|
| `id` | NUMBER PK | Identificador |
| `ativo_id` | NUMBER FK | Ativo a ser mantido |
| `tecnico_id` | NUMBER FK | Técnico líder (após alocação) |
| `tipo` | VARCHAR2(20) | `preventiva` / `corretiva` |
| `status` | VARCHAR2(20) | Ver estados acima |
| `criticidade` | VARCHAR2(10) | `alta` / `media` / `baixa` |
| `dt_prevista` | DATE | Data planejada de execução |
| `dt_limite` | DATE | Prazo máximo — ultrapassado → `vencida` |
| `dt_inicio_execucao` | TIMESTAMP | Quando técnico confirmou início |
| `dt_conclusao` | TIMESTAMP | Quando foi encerrada |
| `horas_uso_no_momento` | NUMBER | Snapshot das horas do ativo na criação |
| `observacoes` | CLOB | Anotações do técnico |
| `gerado_automaticamente` | CHAR(1) | `S` se criada pelo scheduler |

### `equipe_manutencao`

| Coluna | Tipo | Descrição |
|--------|------|-----------|
| `manutencao_id` | NUMBER FK | Manutenção |
| `tecnico_id` | NUMBER FK | Técnico da equipe |
| `papel` | VARCHAR2(20) | `lider` / `auxiliar` |
| `dt_alocacao` | DATE | Data da alocação |

> Uma manutenção pode ter mais de um técnico. `manutencao.tecnico_id` aponta para o líder; a equipe completa fica aqui.

### `historico_status`

| Coluna | Tipo | Descrição |
|--------|------|-----------|
| `id` | NUMBER PK | Identificador |
| `manutencao_id` | NUMBER FK | Manutenção |
| `status_anterior` | VARCHAR2(20) | Estado anterior |
| `status_novo` | VARCHAR2(20) | Novo estado |
| `dt_transicao` | TIMESTAMP | Momento da mudança |
| `usuario_id` | NUMBER FK | Quem realizou a transição |
| `motivo` | VARCHAR2(500) | Justificativa (obrigatória para cancelamentos) |

### `alerta_manutencao`

| Coluna | Tipo | Descrição |
|--------|------|-----------|
| `id` | NUMBER PK | Identificador |
| `manutencao_id` | NUMBER FK | Manutenção relacionada |
| `tipo_alerta` | VARCHAR2(30) | `preventivo` / `vencimento` / `atraso` |
| `dt_envio` | TIMESTAMP | Quando foi disparado |
| `destinatario` | VARCHAR2(200) | E-mail do destinatário |
| `enviado` | CHAR(1) | `S` / `N` |

### `score_prioridade` *(view materializada)*

| Coluna | Tipo | Descrição |
|--------|------|-----------|
| `manutencao_id` | NUMBER FK | Manutenção |
| `score_calculado` | NUMBER | Score final para ordenação |
| `fator_criticidade` | NUMBER | Contribuição do fator criticidade |
| `fator_prazo` | NUMBER | Contribuição do fator prazo |
| `fator_tipo` | NUMBER | Contribuição do tipo de manutenção |
| `dt_calculo` | TIMESTAMP | Última atualização do score |

---

## Notas técnicas

- A view `score_prioridade` é materializada e atualizada via `DBMS_MVIEW.REFRESH` ao final de cada rodada do scheduler, evitando recalcular a cada abertura de tela
- Trigger `trg_manutencao_concluida` atualiza `ativo.horas_uso_acumuladas` e `ativo.status` ao mudar para `concluida`
- Trigger `trg_manutencao_execucao` muda `ativo.status` para `em_manutencao` ao entrar em `em_execucao`
- O campo `dt_limite` é calculado no momento da criação com base em `regra_manutencao.antecedencia_alerta` e na criticidade do contrato
