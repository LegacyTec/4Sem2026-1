# Módulo 3 — Técnicos e Competências

[← Voltar ao README](../README.md)

## Responsabilidade

Gerencia o perfil completo dos técnicos de campo — certificações, localização atual, disponibilidade e histórico — e fornece a lógica de filtragem e sugestão de alocação para cada manutenção.

---

## Os três eixos de avaliação

Para qualquer alocação, cada técnico é avaliado simultaneamente em:

| Eixo | Pergunta | Fonte de dados |
|------|----------|----------------|
| **Competências** | Possui todas as certificações exigidas pelo contrato, com validade em dia? | `tecnico_certificacao` × `contrato_certificacao` |
| **Disponibilidade** | Está livre no período necessário (incluindo tempo de viagem)? | `disponibilidade_tecnico` |
| **Proximidade** | Qual a distância até o ativo? | `SDO_GEOM.SDO_DISTANCE` entre `tecnico.coordenada_atual` e `localizacao.coordenadas` |

---

## Fluxo de sugestão de técnico

```
Manutenção pendente sem técnico
         │
         ▼
Filtro 1 — Certificações
  Possui todas as NRs obrigatórias do contrato com validade em dia?
  NÃO → Eliminado
  SIM ↓
         ▼
Filtro 2 — Disponibilidade
  Sem conflito de agenda no período da viagem + execução?
  NÃO → Eliminado
  SIM ↓
         ▼
Filtro 3 — Proximidade GIS
  Calcula SDO_GEOM.SDO_DISTANCE entre técnico e ativo
         ↓
Lista ranqueada por distância (+ criticidade como desempate)
         ↓
Planejador confirma alocação → ordem de serviço gerada (Módulo 4)
```

---

## Localização em campo

O técnico tem dois registros de localização no sistema:

- **`localizacao_base_id`** — cidade/base de origem (permanente)
- **`localizacao_atual_id` + `coordenada_atual`** — posição atual, atualizada manualmente pelo técnico ao embarcar, desembarcar ou iniciar viagem

O status do técnico reflete sua situação operacional:

| Status | Significado |
|--------|-------------|
| `disponivel` | Na base, sem alocação ativa |
| `em_campo` | Executando uma manutenção |
| `embarcado` | No mar (ciclo de ~15 dias) |
| `de_folga` | Bloqueado por férias ou folga |

---

## Controle de certificações e vencimentos

Cada registro em `tecnico_certificacao` tem `dt_vencimento`. A coluna virtual `valida` é calculada automaticamente:

```sql
valida AS (CASE WHEN dt_vencimento >= SYSDATE THEN 'S' ELSE 'N' END) VIRTUAL
```

Isso simplifica o filtro de alocação — a query usa `WHERE valida = 'S'` sem precisar recalcular em cada chamada.

Quando `dt_vencimento - SYSDATE <= 30 dias`, o módulo de Alertas dispara notificação para o técnico e para o gestor (ver [Módulo 5](./modulo-alertas-notificacoes.md)).

---

## Modelo de dados

### `tecnico`

| Coluna | Tipo | Descrição |
|--------|------|-----------|
| `id` | NUMBER PK | Identificador |
| `nome` | VARCHAR2(200) | Nome completo |
| `email` | VARCHAR2(200) | E-mail corporativo |
| `nivel` | VARCHAR2(10) | `junior` / `pleno` / `senior` |
| `status` | VARCHAR2(20) | `disponivel` / `em_campo` / `embarcado` / `de_folga` |
| `localizacao_base_id` | NUMBER FK | Base / cidade de origem |
| `localizacao_atual_id` | NUMBER FK | Localização atual |
| `coordenada_atual` | SDO_GEOMETRY | Ponto geográfico atual |
| `dt_ultimo_embarque` | DATE | Data do último embarque registrado |
| `dt_previsto_retorno` | DATE | Retorno previsto da missão atual |

### `tecnico_certificacao`

| Coluna | Tipo | Descrição |
|--------|------|-----------|
| `tecnico_id` | NUMBER FK | Técnico |
| `certificacao_id` | NUMBER FK | Certificação |
| `dt_obtencao` | DATE | Data de obtenção |
| `dt_vencimento` | DATE | Data de vencimento |
| `documento_ref` | VARCHAR2(200) | Número ou referência do documento |
| `valida` | CHAR(1) VIRTUAL | `S` se `dt_vencimento >= SYSDATE` |

### `disponibilidade_tecnico`

| Coluna | Tipo | Descrição |
|--------|------|-----------|
| `id` | NUMBER PK | Identificador |
| `tecnico_id` | NUMBER FK | Técnico |
| `dt_inicio_bloqueio` | DATE | Início do período indisponível |
| `dt_fim_bloqueio` | DATE | Fim do período indisponível |
| `motivo` | VARCHAR2(20) | `ferias` / `embarque` / `manutencao` |
| `manutencao_id` | NUMBER FK | Referência à manutenção (quando aplicável, nullable) |

> Quando o bloqueio vem de uma alocação do sistema, `manutencao_id` aponta para ela. Férias e folgas deixam o campo nulo.

### `alerta_certificacao`

| Coluna | Tipo | Descrição |
|--------|------|-----------|
| `id` | NUMBER PK | Identificador |
| `tecnico_id` | NUMBER FK | Técnico afetado |
| `certificacao_id` | NUMBER FK | Certificação próxima do vencimento |
| `dias_para_vencer` | NUMBER | Dias restantes no momento do disparo |
| `dt_alerta_enviado` | TIMESTAMP | Momento do envio |
| `enviado` | CHAR(1) | `S` / `N` |

### `historico_localizacao`

| Coluna | Tipo | Descrição |
|--------|------|-----------|
| `id` | NUMBER PK | Identificador |
| `tecnico_id` | NUMBER FK | Técnico |
| `localizacao_id` | NUMBER FK | Localização registrada |
| `tipo_movimento` | VARCHAR2(20) | `embarque` / `desembarque` / `viagem` |
| `dt_registro` | TIMESTAMP | Momento do registro |

---

## Notas técnicas

- O cálculo de distância usa `SDO_GEOM.SDO_DISTANCE(t.coordenada_atual, l.coordenadas, 0.005, 'unit=KM')` para obter o resultado em quilômetros diretamente na query de sugestão
- A coluna `valida` como virtual column elimina a necessidade de joins adicionais ou cálculos no Java — o ORM já expõe o campo como atributo da entidade
- O bloqueio automático de disponibilidade quando uma manutenção é alocada é feito por trigger em `manutencao` ao mudar status para `alocada`
