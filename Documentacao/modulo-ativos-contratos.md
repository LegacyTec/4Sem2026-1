# Módulo 1 — Ativos e Contratos

[← Voltar ao README](../README.md)

## Responsabilidade

Fundação do sistema. Define o que existe (ativos/equipamentos), para quem (clientes), sob quais regras (contratos) e onde está (localização com GIS). Sem este módulo, nenhum outro sabe o que monitorar, para quem ou com qual frequência.

---

## Hierarquia de dados

```
Cliente
  └── Contrato (periodicidade, criticidade, SLA, certificações exigidas)
        ├── Localização (país, coordenadas SDO_GEOMETRY, tipo: terra / embarcação)
        ├── Tipo de equipamento (modelo, fabricante, NRs aplicáveis)
        ├── Regra de manutenção (prazo em dias OU horas de uso acumuladas)
        └── Ativo — equipamento instalado
              ├── Número de série
              ├── Horas de uso acumuladas
              ├── Status: operacional / em_manutenção / inativo
              └── Gera manutenções automaticamente (ver Módulo 2)
```

---

## Regras de negócio

- Um ativo **não muda de localização** após instalado
- A criticidade do **contrato** prevalece sobre a criticidade do cliente quando há conflito de prioridade
- A regra de manutenção pode usar **prazo em dias** ou **limite de horas de uso** — o sistema verifica a condição que disparar primeiro
- Certificações exigidas ficam no contrato e são herdadas por todos os ativos vinculados
- Contrato vencido bloqueia geração de novas manutenções preventivas para os ativos vinculados

---

## Modelo de dados

### `cliente`

| Coluna | Tipo | Descrição |
|--------|------|-----------|
| `id` | NUMBER PK | Identificador |
| `razao_social` | VARCHAR2(200) | Nome da empresa |
| `pais` | VARCHAR2(100) | País sede |
| `criticidade` | VARCHAR2(10) | `alta` / `media` / `baixa` |
| `ativo` | CHAR(1) | `S` / `N` |

### `contrato`

| Coluna | Tipo | Descrição |
|--------|------|-----------|
| `id` | NUMBER PK | Identificador |
| `cliente_id` | NUMBER FK | Referência ao cliente |
| `numero` | VARCHAR2(50) | Número do contrato |
| `criticidade` | VARCHAR2(10) | `alta` / `media` / `baixa` |
| `sla_dias` | NUMBER | Prazo máximo de atendimento em dias |
| `dt_inicio` | DATE | Início da vigência |
| `dt_fim` | DATE | Fim da vigência |

### `contrato_certificacao`

| Coluna | Tipo | Descrição |
|--------|------|-----------|
| `contrato_id` | NUMBER FK | Contrato |
| `certificacao_id` | NUMBER FK | Certificação exigida |
| `obrigatoria` | CHAR(1) | `S` — eliminatória na alocação de técnico |

### `localizacao`

| Coluna | Tipo | Descrição |
|--------|------|-----------|
| `id` | NUMBER PK | Identificador |
| `nome` | VARCHAR2(200) | Nome do local |
| `pais` | VARCHAR2(100) | País |
| `tipo` | VARCHAR2(20) | `terra` / `embarcacao` |
| `coordenadas` | SDO_GEOMETRY | Coordenadas geográficas (Oracle Spatial) |

### `tipo_equipamento`

| Coluna | Tipo | Descrição |
|--------|------|-----------|
| `id` | NUMBER PK | Identificador |
| `nome` | VARCHAR2(200) | Descrição do tipo |
| `fabricante` | VARCHAR2(200) | Fabricante |
| `modelo` | VARCHAR2(200) | Modelo |

### `regra_manutencao`

| Coluna | Tipo | Descrição |
|--------|------|-----------|
| `id` | NUMBER PK | Identificador |
| `contrato_id` | NUMBER FK | Contrato ao qual a regra pertence |
| `tipo_manutencao` | VARCHAR2(20) | `preventiva` / `corretiva` |
| `periodicidade_dias` | NUMBER | Intervalo em dias (opcional) |
| `limite_horas_uso` | NUMBER | Limite de horas acumuladas (opcional) |
| `antecedencia_alerta` | NUMBER | Dias de antecedência para gerar alerta |

### `ativo`

| Coluna | Tipo | Descrição |
|--------|------|-----------|
| `id` | NUMBER PK | Identificador |
| `contrato_id` | NUMBER FK | Contrato vinculado |
| `tipo_equipamento_id` | NUMBER FK | Tipo do equipamento |
| `localizacao_id` | NUMBER FK | Localização fixa do ativo |
| `numero_serie` | VARCHAR2(100) | Número de série único |
| `dt_instalacao` | DATE | Data de instalação |
| `horas_uso_acumuladas` | NUMBER | Contador atualizado após cada manutenção |
| `status` | VARCHAR2(20) | `operacional` / `em_manutencao` / `inativo` |

### `certificacao`

| Coluna | Tipo | Descrição |
|--------|------|-----------|
| `id` | NUMBER PK | Identificador |
| `nome` | VARCHAR2(50) | Ex: `NR35`, `NR10` |
| `descricao` | VARCHAR2(500) | Descrição completa |
| `validade_meses` | NUMBER | Validade típica em meses |

---

## Notas técnicas

- `SDO_GEOMETRY` no campo `coordenadas` da tabela `localizacao` permite usar `SDO_GEOM.SDO_DISTANCE` nas queries do módulo de Técnicos para calcular a distância entre técnico e ativo sem lógica trigonométrica manual no Java
- A coluna `horas_uso_acumuladas` em `ativo` é atualizada por uma trigger após conclusão de cada manutenção
- O Oracle Spatial requer criação do índice espacial: `CREATE INDEX idx_loc_coords ON localizacao(coordenadas) INDEXTYPE IS MDSYS.SPATIAL_INDEX`
