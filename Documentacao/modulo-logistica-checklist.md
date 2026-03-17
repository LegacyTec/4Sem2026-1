# Módulo 4 — Logística e Checklist

[← Voltar ao README](../README.md)

## Responsabilidade

Garante que o técnico certo saia preparado para o lugar certo, com os equipamentos corretos, dentro da janela de tempo viável. Controla fisicamente o que sai do estoque e o que retorna, e registra a execução técnica da manutenção via checklist.

---

## Duas responsabilidades distintas

| Responsabilidade | O que resolve |
|-----------------|---------------|
| **Planejamento e logística** | Viabilidade da viagem, tempo de deslocamento, retirada e devolução de ferramentas |
| **Checklist de execução** | Padronização e auditoria do que foi feito no campo |

---

## Fluxo completo

```
Manutenção alocada (técnico definido)
         │
         ├─── Planejamento de viagem
         │       Destino, tipo (terra / embarcação), duração estimada com deslocamento
         │       Alerta se dt_limite inviável dado o tempo de viagem
         │
         ├─── Checklist de ferramentas
         │       Lista de itens necessários gerada a partir do tipo_equipamento
         │
         └─── Formulário de retirada
                 Técnico confirma itens retirados do estoque → registro formal
                          │
                          ▼
              Execução em campo
                 Técnico preenche checklist_execucao passo a passo
                          │
                          ▼
              Devolução e fechamento
                 Itens conferidos (qtd_devolvida vs qtd_retirada)
                 Ordem de serviço encerrada → histórico registrado no ativo
```

---

## Planejamento de viagem

Para ativos em localizações distantes (embarcações, outros países), o sistema calcula a janela de tempo necessária incluindo deslocamento. Considera:

- Se o ativo é do tipo `embarcacao`, verifica se o técnico precisará embarcar e qual o ciclo atual (15 dias)
- Compara `dt_limite` da manutenção com `dt_prevista + tempo_viagem_h`
- Se a janela for inviável, gera alerta para o planejador antes de confirmar a alocação

---

## Checklist

O checklist funciona em dois níveis:

**Template** (`checklist_template` + `checklist_item`) — definido por tipo de equipamento, reutilizado em todas as manutenções daquele tipo. Cada item pode ser marcado como obrigatório.

**Execução** (`checklist_execucao`) — instância do template para uma ordem de serviço específica. O técnico marca cada item como concluído e pode adicionar observações por item.

---

## Controle de estoque

O formulário de retirada registra o que saiu (`qtd_retirada`) e o que voltou (`qtd_devolvida`). A comparação entre os dois valores, após `dt_retorno` da ordem de serviço, aciona alertas de item não devolvido (ver [Módulo 5](./modulo-alertas-notificacoes.md)).

O campo `qtd_disponivel` em `item_estoque` é decrementado por trigger na retirada e incrementado na devolução.

---

## Modelo de dados

### `ordem_servico`

| Coluna | Tipo | Descrição |
|--------|------|-----------|
| `id` | NUMBER PK | Identificador |
| `manutencao_id` | NUMBER FK | Manutenção vinculada |
| `tecnico_id` | NUMBER FK | Técnico responsável |
| `status` | VARCHAR2(20) | `planejada` / `em_campo` / `encerrada` |
| `dt_saida` | DATE | Data de saída do técnico |
| `dt_retorno` | DATE | Data de retorno previsto |
| `tempo_viagem_h` | NUMBER | Horas estimadas de deslocamento |

### `item_estoque`

| Coluna | Tipo | Descrição |
|--------|------|-----------|
| `id` | NUMBER PK | Identificador |
| `nome` | VARCHAR2(200) | Nome do item |
| `categoria` | VARCHAR2(20) | `ferramenta` / `EPI` / `peca` |
| `qtd_disponivel` | NUMBER | Quantidade atual em estoque |

### `retirada_item`

| Coluna | Tipo | Descrição |
|--------|------|-----------|
| `id` | NUMBER PK | Identificador |
| `ordem_servico_id` | NUMBER FK | Ordem de serviço |
| `item_estoque_id` | NUMBER FK | Item retirado |
| `qtd_retirada` | NUMBER | Quantidade retirada |
| `qtd_devolvida` | NUMBER | Quantidade devolvida (preenchido no retorno) |
| `dt_retirada` | DATE | Data de saída |

### `checklist_template`

| Coluna | Tipo | Descrição |
|--------|------|-----------|
| `id` | NUMBER PK | Identificador |
| `tipo_equipamento_id` | NUMBER FK | Tipo de equipamento ao qual pertence |
| `nome` | VARCHAR2(200) | Nome do template (ex: "Revisão semestral modelo X") |

### `checklist_item`

| Coluna | Tipo | Descrição |
|--------|------|-----------|
| `id` | NUMBER PK | Identificador |
| `template_id` | NUMBER FK | Template pai |
| `descricao` | VARCHAR2(500) | Descrição do passo |
| `obrigatorio` | CHAR(1) | `S` — bloqueia conclusão se não marcado |

### `checklist_execucao`

| Coluna | Tipo | Descrição |
|--------|------|-----------|
| `id` | NUMBER PK | Identificador |
| `ordem_servico_id` | NUMBER FK | Ordem de serviço |
| `item_id` | NUMBER FK | Item do template |
| `concluido` | CHAR(1) | `S` / `N` |
| `observacao` | CLOB | Anotação livre do técnico |
| `dt_registro` | TIMESTAMP | Momento do registro |

---

## Notas técnicas

- Trigger `trg_retirada_estoque` decrementa `item_estoque.qtd_disponivel` ao inserir em `retirada_item` e lança exceção se `qtd_disponivel = 0`
- Trigger `trg_devolucao_estoque` incrementa `qtd_disponivel` ao preencher `qtd_devolvida`
- A validação de viabilidade de prazo (tempo de viagem vs. `dt_limite`) pode ser implementada como function PL/SQL chamada pela API antes de confirmar a alocação: `fn_viagem_viavel(manutencao_id, tecnico_id) RETURN BOOLEAN`
- Itens obrigatórios do checklist bloqueiam a mudança de status da manutenção para `concluida` — verificado por constraint via trigger ou validação no service Java
