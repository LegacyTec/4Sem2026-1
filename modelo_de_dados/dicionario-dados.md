# 📚 Dicionário de Dados 

## 📌 Visão Geral
Sistema de gestão de ativos, contratos, manutenções e técnicos, incluindo logística, planejamento e controle de checklists.

## Diagrama Entidade-Relacionamento (DER)

<img src="modelo_bd.png">

## Tabelas

### 📃 Tabela Contrato 

A tabela representa o contrato da empresa Altave com seus clientes.

| Campo | Tipo | Obrigatório | Descrição | Observação |
|-------|------|-------------|-----------|------------|
| id_contrato | NUMBER | Sim | Identificador do contrato | PK |
| nome_empresa | VARCHAR2(100) | Sim | Nome da empresa cliente | |
| quantidade_plantas | NUMBER | Sim | Quantidade de plantas | | 
| data_inicio | DATE | Sim | Data de início de contrato | |
| data_fim | DATE | Sim | Data de final do contrato | | 
| quantidade_ativos | NUMBER | Sim | Quantidade de ativos instalados na empresa | |
| descricao | VARCHAR2(200) | Sim | Descrição do contrato | |
| id_tecnico |  NUMBER | Sim | Técnicos atrelados ao contrato | FK -> tecnico.id |   

### 📹 Tabela Ativo

A tabela representa os ativos instalados nas empresas clientes.

| Campo | Tipo | Obrigatório | Descrição | Observação |
|-------|------|-------------|-----------|------------|
| id_ativo | NUMBER | Sim | Identificador do ativo | PK | 
| fabricante | VARCHAR2(100) | Sim | Nome do fabricante | |
| periodicidade_manutencao | NUMBER | Sim | Período para realizar a manutenção preventiva | 
| descricao | VARCHAR2(200) | Sim | Descrição do ativo | |
| planta | VARCHAR2(100) | Sim | Planta da empresa | |
| predio | VARCHAR2(100) | Sim | Predio da empresa | |
| data_instalacao | DATE | Sim | Data de instlacao do ativo | |
| id_contrato | NUMBER | Sim | Ativos pertencem a um contrato | FK -> contrato.id | 


### 🛠️ Tabela Ordem de Manutenção 

A tabela representa a ordem de manutenção, instalação e retirada do ativo.

| Campo | Tipo | Obrigatório | Descrição | Observação |
|-------|------|-------------|-----------|------------|
| id_ordem | NUMBER | Sim | Identificador da ordem | PK | 
| nome_ordem | VARCHAR2(100) | Sim | Nome da ordem | |
| categoria | VARCHAR2(50) | Sim | Categoria de manutenção | CHECK ('Software', 'HARDWARE') |
| planta | VARCHAR2(100) | Sim | Planta da empresa | |
| predio | VARCHAR2(100) | Sim | Predio da empresa | |
| descricao | VARCHAR2(200) | Sim | Descrição da ordem | |
| data_inicio | DATE | Não | Data para iniciar a ordem | |
| data_fim | DATE | Não | Data de finalização da ordem | | 
| tipo_manutencao | VARCHAR2(100) | Sim | Tipo de manutenção | CHECK('preventiva', 'corretiva') |
| anexo | BLOB | Não | Anexo de arquivos, fotos e entre outros | | 
| requisitos | VARCHAR2(200) | Requisitos para realizar a manutenção | |
| id_ativo | NUMBER | Ativo que deve ser feita a manutenção | FK -> ativo.id |

### 🧑‍🔧 Tabela Técnico

A tabela representa o técnico, planejador e administrador.

| Campo | Tipo | Obrigatório | Descrição | Observação |
|-------|------|-------------|-----------|------------|
| tecnico_id | NUMBER | Sim | Identificador do técnico | PK |
| nome_completo | VARCHAR2(100) | Sim | Nome completo do técnico | |
| email | VARCHAR2(100) | Sim | Email do técnico | UNIQUE | 
| status | VARCHAR2(50) | Sim | Status do técnico | CHECK('disponivel', 'folga', 'em manutencao') | 
| data_nascimento | DATE | Sim | Data de nascimento do técnico | | 
| cargo | VARCHAR2(50) | Sim | Cargo de técnico, planejador e Administrador | | 

### 🔗 Tabela Ordem Técnico

Tabela de relacionamento do tipo N:N, uma ordem contém vários técnicos e um técnico possui várias ordens de manutenção.

| Campo | Tipo | Obrigatório | Descrição | Observação |
|-------|------|-------------|-----------|------------|
| id_ordem_manutencao | NUMBER | Sim | Ordem de manutenção | PK  FK -> ordem_manutencao.id |
| id_tecnico | NUMBER | Sim | Técnico | PK FK -> tecnico.id |
| status_manutencao | VARCHAR2(50) | Sim | Status da manutenção | DEFAULT 'pendente' CHECK('em progresso', 'concluida') |