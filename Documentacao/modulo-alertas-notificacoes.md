# Módulo 5 — Alertas e Notificações

[← Voltar ao README](../README.md)

## Responsabilidade

Sistema nervoso do AltaveTrack. Observa silenciosamente todos os outros módulos e age quando uma condição crítica é detectada — sem intervenção manual. Não gera dados primários: apenas reage ao estado dos outros módulos.

---

## Princípio central

> Alertas são **consequência**, não função. Cada um nasce de uma condição detectada automaticamente nos módulos de Manutenções, Técnicos, Ativos ou Logística.

---

## Catálogo de alertas

### Origem: Manutenções

| Alerta | Condição | Severidade | Canal | Destinatários |
|--------|----------|-----------|-------|---------------|
| Manutenção se aproximando | `dt_limite - SYSDATE <= antecedencia_alerta` | Média | E-mail + in-app | Planejador, Gestor |
| Manutenção vencida sem atendimento | `dt_limite < SYSDATE` e `status != 'concluida'` | Alta | E-mail + in-app | Planejador, Gestor, Cliente |
| Pendente sem técnico há N dias | `status = 'pendente'` por mais de N dias | Média | In-app | Planejador |
| Corretiva crítica aberta | `tipo = 'corretiva'` e `criticidade = 'alta'` | Alta | E-mail imediato | Gestor, Planejador |

### Origem: Técnicos

| Alerta | Condição | Severidade | Canal | Destinatários |
|--------|----------|-----------|-------|---------------|
| Certificação vencendo em breve | `dt_vencimento - SYSDATE <= 30 dias` | Média | E-mail | Técnico, Gestor RH |
| Certificação vencida — técnico bloqueado | `dt_vencimento < SYSDATE` com ordens abertas | Alta | E-mail + in-app | Gestor RH, Planejador |

### Origem: Ativos e Logística

| Alerta | Condição | Severidade | Canal | Destinatários |
|--------|----------|-----------|-------|---------------|
| Contrato próximo do vencimento | `dt_fim - SYSDATE <= 60 dias` | Baixa | E-mail | Gestor comercial |
| Item de estoque não devolvido | `qtd_devolvida < qtd_retirada` após `dt_retorno` | Média | E-mail + in-app | Técnico, Gestor logística |

---

## Como o motor funciona

### Detecção — PL/SQL (`DBMS_SCHEDULER`)

Procedure agendada, roda diariamente de madrugada. Para cada tipo de alerta, verifica a condição e insere em `alerta` com `enviado_email = 'N'` — **se não existir alerta idêntico já enviado hoje para o mesmo registro de origem** (evita spam).

```
DBMS_SCHEDULER job → procedure prc_verifica_alertas()
  └── Para cada condição:
        IF condição atingida AND não existe alerta hoje para origem_id
        THEN INSERT INTO alerta (...)
             INSERT INTO destinatario_alerta (...)
```

### Disparo — Spring Boot (`@Scheduled`)

Componente Java roda a cada 5 minutos, consulta alertas com `enviado_email = 'N'`, envia via JavaMail e atualiza `enviado_email = 'S'`. Falhas são registradas em `log_envio_email` com `status_envio = 'falha'` para reprocessamento.

### Notificação in-app — WebSocket

Para alertas marcados com canal `in-app`, o Spring envia via WebSocket ao usuário logado em tempo real, sem precisar recarregar a tela.

```
PL/SQL detecta → INSERT alerta → Spring @Scheduled lê →
  ├── JavaMail → e-mail
  └── WebSocket → notificação in-app (Vue.js)
        └── Registro em log_envio_email (auditoria)
```

### Por que separar detecção (PL/SQL) de envio (Java)?

O banco cuida das **regras de negócio** — quando e para quem alertar. O Java cuida da **integração com serviços externos** — SMTP, WebSocket. Essa separação facilita testes, manutenção e substituição do canal de envio sem tocar nas regras.

---

## Modelo de dados

### `alerta`

| Coluna | Tipo | Descrição |
|--------|------|-----------|
| `id` | NUMBER PK | Identificador |
| `tipo_alerta` | VARCHAR2(40) | Ex: `manut_vencida`, `cert_vencendo`, `item_nao_devolvido` |
| `severidade` | VARCHAR2(10) | `alta` / `media` / `baixa` |
| `origem_modulo` | VARCHAR2(20) | `manutencao` / `tecnico` / `ativo` / `logistica` |
| `origem_id` | NUMBER | ID do registro que originou o alerta |
| `mensagem` | VARCHAR2(1000) | Texto descritivo gerado automaticamente |
| `dt_deteccao` | TIMESTAMP | Quando a condição foi detectada |
| `enviado_email` | CHAR(1) | `S` / `N` |
| `dt_envio_email` | TIMESTAMP | Momento do envio |
| `lido_app` | CHAR(1) | `S` / `N` — marcado pelo usuário na interface |

### `destinatario_alerta`

| Coluna | Tipo | Descrição |
|--------|------|-----------|
| `alerta_id` | NUMBER FK | Alerta |
| `usuario_id` | NUMBER FK | Usuário destinatário |
| `email` | VARCHAR2(200) | E-mail de envio |
| `canal` | VARCHAR2(10) | `email` / `in-app` / `ambos` |
| `enviado` | CHAR(1) | `S` / `N` |
| `dt_leitura` | TIMESTAMP | Quando o usuário leu na interface |

### `regra_alerta`

| Coluna | Tipo | Descrição |
|--------|------|-----------|
| `id` | NUMBER PK | Identificador |
| `tipo_alerta` | VARCHAR2(40) | Chave do tipo |
| `antecedencia_dias` | NUMBER | Dias de antecedência para disparar |
| `severidade_padrao` | VARCHAR2(10) | Severidade padrão |
| `canal_padrao` | VARCHAR2(10) | Canal padrão |
| `ativo` | CHAR(1) | `S` / `N` — permite desativar um tipo de alerta |

> Configuração persistida em banco: alterar a antecedência ou desativar um tipo não requer redeploy.

### `log_envio_email`

| Coluna | Tipo | Descrição |
|--------|------|-----------|
| `id` | NUMBER PK | Identificador |
| `alerta_id` | NUMBER FK | Alerta relacionado |
| `destinatario_email` | VARCHAR2(200) | E-mail de destino |
| `assunto` | VARCHAR2(300) | Assunto do e-mail |
| `status_envio` | VARCHAR2(10) | `sucesso` / `falha` / `pendente` |
| `dt_tentativa` | TIMESTAMP | Momento da tentativa |
| `erro_msg` | CLOB | Mensagem de erro (preenchida se falha) |
| `tentativas` | NUMBER | Contador de retentativas |

### `preferencia_notificacao`

| Coluna | Tipo | Descrição |
|--------|------|-----------|
| `usuario_id` | NUMBER FK | Usuário |
| `tipo_alerta` | VARCHAR2(40) | Tipo de alerta |
| `receber_email` | CHAR(1) | `S` / `N` |
| `receber_app` | CHAR(1) | `S` / `N` |
| `antecedencia_dias` | NUMBER | Override pessoal da antecedência padrão |

---

## Notas técnicas

- O campo `tentativas` em `log_envio_email` implementa retry automático: o `@Scheduled` reprocessa registros com `status_envio = 'falha'` e `tentativas < 3`, evitando perda de alertas críticos por instabilidade momentânea do SMTP
- A deduplicação de alertas usa: `WHERE tipo_alerta = :tipo AND origem_id = :id AND TRUNC(dt_deteccao) = TRUNC(SYSDATE)` — um alerta do mesmo tipo para o mesmo registro não é criado mais de uma vez por dia
- `preferencia_notificacao` sobrescreve `regra_alerta` por usuário, permitindo granularidade individual sem alterar a regra global
