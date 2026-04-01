# 📚 Dicionário de Dados 

## 📌 Visão Geral
Sistema de gestão de ativos, contratos, manutenções e técnicos, incluindo logística, planejamento e controle de checklists.

## Diagrama Entidade-Relacionamento (DER)

<img src="modelo_bd.png">

## 🧱 Módulo 1 - Ativos e Contratos

### 🤵‍♂️ Tabela Cliente

Armazena informações dos clientes da Altave

| Campo | Tipo | Obrigatório | Descrição | Observação |
|-------|------|-------------|-----------|------------|
| id    | NUMBER |  Sim      | Identificador do cliente | PK |
| razao_social | VARCHAR2(200) | Sim | Nome da empresa | |
| pais | VARCHAR2(200) | Sim | País do cliente | | 
| criticidade | VARCHAR2(200) | Sim | Nível de criticidade | CHECK |
| ativo | CHAR(1) | Sim | Cliente ativo (S/N) | DEFAULT S | 

### 📄 Tabela Contrato

Representa os contratos firmados com o cliente

| Campo | Tipo | Obrigatório | Descrição | Observação |
|-------|------|-------------|-----------|------------|
| id    | NUMBER | Sim | Identificador do contrato | PK |
| cliente_id | NUMBER | Sim | Cliente vinculado | FK -> cliente.id |
| numero | VARCHAR2(50) | Sim | Número do contrato | UNIQUE | 
| criticidade | VARCHAR2(10) | Sim | Nível de criticidade | CHECK |
| sla_dias | NUMBER | Sim | SLA em dias | | 
| dt_inicio | DATE | Sim | Data de início do contrato | |
| dt_fim | DATE | Sim | Data de fim do contrato | |

### ⭐ Tabela certificacao

Representa as certificações que a empresa exige e também as certificações que o técnico possui.

| Campo | Tipo | Obrigatório | Descrição | Observação |
|-------|------|-------------|-----------|------------|
| id | NUMBER | Sim | Identificador da certificação | PK |
| nome | VARCHAR2(50) | Sim | Nome da certificação | | 
| descricao | VARCHAR2(500) | Não | Descrição da certificação | |  
| validade_meses | NUMBER | Sim | Validade da certificação | |

### 🔗 Tabela contrato_certificacao

| Campo | Tipo | Obrigatório | Descrição | Observação |
|-------|------|-------------|-----------|------------|
| contrato_id |	NUMBER | Sim |	Contrato |	PK/FK     |
| certificacao_id |	NUMBER | Sim |	Certificação |	PK/FK | 
| obrigatoria |	CHAR(1) | Sim	| Se é obrigatória (S/N) |	DEFAULT S |

### 📍 Tabela localização

Representa a localização dos ativos e dos técnicos.

| Campo | Tipo | Obrigatório | Descrição | Observação |
|-------|------|-------------|-----------|------------|
| id	| NUMBER | Sim | Identificador da localização | PK | 
| nome	| VARCHAR2(200) | Sim |	Nome do local |	|
| pais	| VARCHAR2(100) | Sim | País | |	
| tipo	| VARCHAR2(20) | Sim | Tipo (terra/embarcacao) | CHECK |
| lat	| NUMBER(10, 7) | Não | Latitude |	|
| lng	| NUMBER(10, 7) | Não | Longitude | | 

### ⚙️ Tabela tipo_equipamento 

Representa o equipamento que está ativo na empresa (cliente).

| Campo | Tipo | Obrigatório | Descrição | Observação |
|-------|------|-------------|-----------|------------|
| id | NUMBER | Sim | Identificador do tipo de equipamento | PK |
| nome | VARCHAR2(200) | Sim | Nome do equipamento | |
| fabricante | VARCHAR2(200) | Não | Nome do fabricante | | 
| modelo | VARCHAR2(200) | Não | Modelo do equipamento | |

### 📃 Tabela regra_manutencao

Representa as regras de manutenção que a empresa altave deve seguir de acordo com o contrato.

| Campo | Tipo | Obrigatório | Descrição | Observação |
|-------|------|-------------|-----------|------------|
| id | NUMBER | Sim | Identificador da regra de manutenção | PK |
| contrato_id | NUMBER | Sim | Contrato | FK -> contrato.id |
| tipo_manutencao | VARCHAR2(20) | Sim | Tipo de manutenção | CHECK |
| periodicidade_dias | NUMBER | Não | Período de dias | |
| limites_horas_uso | NUMBER | Não | Limite de horas de uso | |
| antecedencia_alerta | NUMBER | Sim | Antecedência de dias de alerta | DEFAULT 7 | 

### ⚡ Tabela ativo

Representa os equipamentos ativados dentro da empresa (Cliente).

| Campo | Tipo | Obrigatório | Descrição | Observação |
|-------|------|-------------|-----------|------------|
| id | NUMBER | Sim | Identificador do ativo | PK |
| contrato_id | NUMBER | Sim | Contrato | FK -> contrato.id |
| tipo_equipamento_id | NUMBER | Sim | Tipo de equipamento | FK -> tipo_equipamento.id |
| localizacao_id | NUMBER | Sim | Localização | FK -> Localizacao.id | 
| numero_serie | VARCHAR2(100) | Sim | Número de série | UNIQUE |
| dt_instalacao | DATE | Sim | Data de instalação | |
| horas_uso_acumuladas | NUMBER | Sim | Horas de uso acumuladas | DEFAULT 0 | 
| status | VARCHAR2(20) | Sim | Status do ativo | CHECK | 

### 🔗 Relacionamentos
- contrato.cliente_id → cliente.id
- contrato_certificacao.contrato_id → contrato.id
- contrato_certificacao.certificacao_id → certificacao.id
- regra_manutencao.contrato_id → contrato.id
- ativo.contrato_id → contrato.id
- ativo.tipo_equipamento_id → tipo_equipamento.id
- ativo.localizacao_id → localizacao.id

## 👩‍💻 MODULO 2 - Técnicos e Competências

### 👨‍🔧 Tabela tecnico

Armazena os dados dos técnicos responsáveis pelas manutenções, incluindo nível, status e localização.

| Campo                | Tipo          | Obrigatório | Descrição                     | Observação          |
| -------------------- | ------------- | ----------- | ----------------------------- | ------------------- |
| id                   | NUMBER        | Sim         | Identificador do técnico      | PK                  |
| nome                 | VARCHAR2(200) | Sim         | Nome completo                 |                     |
| email                | VARCHAR2(200) | Sim         | Email do técnico              | UNIQUE              |
| nivel                | VARCHAR2(10)  | Sim         | Nível (junior, pleno, senior) | CHECK               |
| status               | VARCHAR2(20)  | Sim         | Status atual                  | DEFAULT + CHECK     |
| localizacao_base_id  | NUMBER        | Não         | Localização base do técnico   | FK → localizacao.id |
| localizacao_atual_id | NUMBER        | Não         | Localização atual             | FK → localizacao.id |
| lat_atual            | NUMBER(10,7)  | Não         | Latitude atual                |                     |
| lng_atual            | NUMBER(10,7)  | Não         | Longitude atual               |                     |
| dt_ultimo_embarque   | DATE          | Não         | Data do último embarque       |                     |
| dt_previsto_retorno  | DATE          | Não         | Data prevista de retorno      |                     |


### 🎓 Tabela tecnico_certificacao

Relaciona técnicos com suas certificações.

| Campo           | Tipo          | Obrigatório | Descrição                     | Observação              |
| --------------- | ------------- | ----------- | ----------------------------- | ----------------------- |
| tecnico_id      | NUMBER        | Sim         | Identificador do técnico      | PK/FK → tecnico.id      |
| certificacao_id | NUMBER        | Sim         | Identificador da certificação | PK/FK → certificacao.id |
| dt_obtencao     | DATE          | Sim         | Data de obtenção              |                         |
| dt_vencimento   | DATE          | Sim         | Data de vencimento            |                         |
| documento_ref   | VARCHAR2(200) | Não         | Referência do documento       |                         |

### 📅 Tabela disponibilidade_tecnico

Controla períodos de indisponibilidade dos técnicos.

| Campo              | Tipo         | Obrigatório | Descrição                       | Observação            |
| ------------------ | ------------ | ----------- | ------------------------------- | --------------------- |
| id                 | NUMBER       | Sim         | Identificador                   | PK                    |
| tecnico_id         | NUMBER       | Sim         | Técnico associado               | FK → tecnico.id       |
| dt_inicio_bloqueio | DATE         | Sim         | Início do bloqueio              |                       |
| dt_fim_bloqueio    | DATE         | Sim         | Fim do bloqueio                 |                       |
| motivo             | VARCHAR2(20) | Sim         | Motivo (férias, embarque, etc.) | CHECK                 |
| manutencao_id      | NUMBER       | Não         | Manutenção associada            | (relacional indireto) |

### 🔗 Relacionamentos

- tecnico.localizacao_base_id → localizacao.id
- tecnico.localizacao_atual_id → localizacao.id
- tecnico_certificacao.tecnico_id → tecnico.id
- tecnico_certificacao.certificacao_id → certificacao.id
- disponibilidade_tecnico.tecnico_id → tecnico.id

## 🔧 MODULO 3 - Manutenções

### 🛠️ Tabela manutencao

Armazena as manutenções realizadas ou planejadas para os ativos, incluindo informações de execução, status e criticidade.

| Campo                  | Tipo         | Obrigatório | Descrição                                  | Observação        |
| ---------------------- | ------------ | ----------- | ------------------------------------------ | ----------------- |
| id                     | NUMBER       | Sim         | Identificador da manutenção                | PK                |
| ativo_id               | NUMBER       | Sim         | Ativo associado                            | FK → ativo.id     |
| tecnico_id             | NUMBER       | Não         | Técnico responsável                        | FK → tecnico.id   |
| tipo                   | VARCHAR2(20) | Sim         | Tipo (preventiva ou corretiva)             | CHECK             |
| status                 | VARCHAR2(20) | Sim         | Status da manutenção                       | DEFAULT + CHECK   |
| criticidade            | VARCHAR2(10) | Sim         | Nível de criticidade                       | CHECK             |
| dt_prevista            | DATE         | Sim         | Data prevista                              |                   |
| dt_limite              | DATE         | Sim         | Data limite para execução                  |                   |
| dt_inicio_execucao     | TIMESTAMP    | Não         | Início da execução                         |                   |
| dt_conclusao           | TIMESTAMP    | Não         | Conclusão da manutenção                    |                   |
| horas_uso_no_momento   | NUMBER       | Não         | Horas de uso do ativo no momento           | DEFAULT 0         |
| observacoes            | CLOB         | Não         | Observações gerais                         |                   |
| gerado_automaticamente | CHAR(1)      | Sim         | Indica se foi gerada automaticamente (S/N) | DEFAULT N + CHECK |

### 👥 Tabela equipe_manutencao

Define os técnicos alocados em uma manutenção e seus respectivos papéis.

| Campo         | Tipo         | Obrigatório | Descrição                        | Observação            |
| ------------- | ------------ | ----------- | -------------------------------- | --------------------- |
| manutencao_id | NUMBER       | Sim         | Identificador da manutenção      | PK/FK → manutencao.id |
| tecnico_id    | NUMBER       | Sim         | Técnico participante             | PK/FK → tecnico.id    |
| papel         | VARCHAR2(20) | Sim         | Papel na equipe (líder/auxiliar) | CHECK                 |
| dt_alocacao   | DATE         | Sim         | Data de alocação                 |                       |

### 📊 Tabela historico_status

Registra o histórico de mudanças de status das manutenções.

| Campo           | Tipo          | Obrigatório | Descrição              | Observação         |
| --------------- | ------------- | ----------- | ---------------------- | ------------------ |
| id              | NUMBER        | Sim         | Identificador          | PK                 |
| manutencao_id   | NUMBER        | Sim         | Manutenção associada   | FK → manutencao.id |
| status_anterior | VARCHAR2(20)  | Não         | Status anterior        |                    |
| status_novo     | VARCHAR2(20)  | Sim         | Novo status            |                    |
| dt_transicao    | TIMESTAMP     | Sim         | Data/hora da transição |                    |
| usuario_id      | NUMBER        | Não         | Usuário responsável    | (não referenciado) |
| motivo          | VARCHAR2(500) | Não         | Motivo da alteração    |                    |

### 🔗 Relacionamentos

* manutencao.ativo_id → ativo.id
* manutencao.tecnico_id → tecnico.id
* equipe_manutencao.manutencao_id → manutencao.id
* equipe_manutencao.tecnico_id → tecnico.id
* historico_status.manutencao_id → manutencao.id

## 🚚 Módulo 4 - Logística e Checklist

### 📦 Tabela: ordem_servico

Representa a execução logística de uma manutenção, controlando deslocamento e atuação do técnico em campo.

| Campo          | Tipo         | Obrigatório | Descrição                         | Observação         |
| -------------- | ------------ | ----------- | --------------------------------- | ------------------ |
| id             | NUMBER       | Sim         | Identificador da ordem de serviço | PK                 |
| manutencao_id  | NUMBER       | Sim         | Manutenção associada              | FK → manutencao.id |
| tecnico_id     | NUMBER       | Sim         | Técnico responsável               | FK → tecnico.id    |
| status         | VARCHAR2(20) | Sim         | Status da ordem                   | DEFAULT + CHECK    |
| dt_saida       | DATE         | Não         | Data de saída                     |                    |
| dt_retorno     | DATE         | Não         | Data de retorno                   |                    |
| tempo_viagem_h | NUMBER       | Não         | Tempo de viagem em horas          | DEFAULT 0          |

### 🧰 Tabela: item_estoque

Controla os itens disponíveis em estoque utilizados nas operações.

| Campo          | Tipo          | Obrigatório | Descrição             | Observação |
| -------------- | ------------- | ----------- | --------------------- | ---------- |
| id             | NUMBER        | Sim         | Identificador         | PK         |
| nome           | VARCHAR2(200) | Sim         | Nome do item          |            |
| categoria      | VARCHAR2(20)  | Sim         | Categoria do item     | CHECK      |
| qtd_disponivel | NUMBER        | Sim         | Quantidade disponível | DEFAULT 0  |


### 📤 Tabela: retirada_item

Registra a retirada e devolução de itens do estoque para ordens de serviço.

| Campo            | Tipo   | Obrigatório | Descrição            | Observação            |
| ---------------- | ------ | ----------- | -------------------- | --------------------- |
| id               | NUMBER | Sim         | Identificador        | PK                    |
| ordem_servico_id | NUMBER | Sim         | Ordem de serviço     | FK → ordem_servico.id |
| item_estoque_id  | NUMBER | Sim         | Item retirado        | FK → item_estoque.id  |
| qtd_retirada     | NUMBER | Sim         | Quantidade retirada  |                       |
| qtd_devolvida    | NUMBER | Não         | Quantidade devolvida | DEFAULT 0             |
| dt_retirada      | DATE   | Sim         | Data da retirada     |                       |


### 📋 Tabela: checklist_template

Define modelos de checklist por tipo de equipamento.

| Campo               | Tipo          | Obrigatório | Descrição           | Observação               |
| ------------------- | ------------- | ----------- | ------------------- | ------------------------ |
| id                  | NUMBER        | Sim         | Identificador       | PK                       |
| tipo_equipamento_id | NUMBER        | Sim         | Tipo de equipamento | FK → tipo_equipamento.id |
| nome                | VARCHAR2(200) | Sim         | Nome do template    |                          |

### ✅ Tabela: checklist_item

Itens que compõem um template de checklist.

| Campo       | Tipo          | Obrigatório | Descrição               | Observação                 |
| ----------- | ------------- | ----------- | ----------------------- | -------------------------- |
| id          | NUMBER        | Sim         | Identificador           | PK                         |
| template_id | NUMBER        | Sim         | Template associado      | FK → checklist_template.id |
| descricao   | VARCHAR2(500) | Sim         | Descrição do item       |                            |
| obrigatorio | CHAR(1)       | Sim         | Indica se é obrigatório | DEFAULT N + CHECK          |


### 📝 Tabela: checklist_execucao

Registra a execução dos itens de checklist em uma ordem de serviço.

| Campo            | Tipo      | Obrigatório | Descrição              | Observação             |
| ---------------- | --------- | ----------- | ---------------------- | ---------------------- |
| id               | NUMBER    | Sim         | Identificador          | PK                     |
| ordem_servico_id | NUMBER    | Sim         | Ordem de serviço       | FK → ordem_servico.id  |
| item_id          | NUMBER    | Sim         | Item do checklist      | FK → checklist_item.id |
| concluido        | CHAR(1)   | Sim         | Indica conclusão (S/N) | DEFAULT N + CHECK      |
| observacao       | CLOB      | Não         | Observações            |                        |
| dt_registro      | TIMESTAMP | Sim         | Data/hora do registro  |                        |

### 🔗 Relacionamentos

* ordem_servico.manutencao_id → manutencao.id
* ordem_servico.tecnico_id → tecnico.id
* retirada_item.ordem_servico_id → ordem_servico.id
* retirada_item.item_estoque_id → item_estoque.id
* checklist_template.tipo_equipamento_id → tipo_equipamento.id
* checklist_item.template_id → checklist_template.id
* checklist_execucao.ordem_servico_id → ordem_servico.id
* checklist_execucao.item_id → checklist_item.id

