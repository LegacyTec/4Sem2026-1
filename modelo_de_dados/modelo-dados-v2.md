# 📚 Dicionário de Dados
## 📌 Visão Geral
Sistema de gestão de ativos, contratos, manutenções e usuários, incluindo logística, planejamento e controle de plantas e ordens de manutenção.
 
## Diagrama Entidade-Relacionamento (DER)

## Tabelas
 
### 🏭 Tabela Planta
A tabela representa as plantas (unidades físicas) vinculadas a um contrato.
 
| Campo | Tipo | Obrigatório | Descrição | Observação |
|-------|------|-------------|-----------|------------|
| id_planta | INT | Sim | Identificador da planta | PK |
| id_contrato | INT | Sim | Contrato ao qual a planta pertence | FK -> contrato.id_contrato |
| endereco | VARCHAR | Sim | Endereço da planta | |
| descricao | VARCHAR | Não | Descrição da planta | |
| latitude | FLOAT | Não | Coordenada geográfica de latitude | |
| longitude | FLOAT | Não | Coordenada geográfica de longitude | |
 
---
 
### 📃 Tabela Contrato
A tabela representa o contrato da empresa Altave com seus clientes.
 
| Campo | Tipo | Obrigatório | Descrição | Observação |
|-------|------|-------------|-----------|------------|
| id_contrato | INT | Sim | Identificador do contrato | PK |
| nome_empresa | VARCHAR | Sim | Nome da empresa cliente | |
| quantidade_plantas | INT | Sim | Quantidade de plantas vinculadas ao contrato | |
| data_inicio | DATE | Sim | Data de início do contrato | |
| data_fim | DATE | Sim | Data de encerramento do contrato | |
| quantidade_ativos | INT | Sim | Quantidade de ativos instalados | |
| descricao | VARCHAR | Não | Descrição do contrato | |
| quantidade_supervisoes | INT | Sim | Quantidade de supervisões previstas | |
| criador | VARCHAR | Sim | Nome do criador do contrato | |
| status | VARCHAR | Sim | Status do contrato | CHECK ('ATIVO', 'INATIVO') |
 
---
 
### 🔗 Tabela Contrato_Usuario
Tabela de relacionamento N:N entre contratos e usuários.
 
| Campo | Tipo | Obrigatório | Descrição | Observação |
|-------|------|-------------|-----------|------------|
| id_contrato | INT | Sim | Referência ao contrato | PK, FK -> contrato.id_contrato |
| id_usuario | INT | Sim | Referência ao usuário | PK, FK -> usuario.usuario_id |
 
---
 
### 👤 Tabela Usuario
A tabela representa os usuários do sistema (técnicos, supervisores, administradores).
 
| Campo | Tipo | Obrigatório | Descrição | Observação |
|-------|------|-------------|-----------|------------|
| usuario_id | INT | Sim | Identificador do usuário | PK |
| nome_completo | VARCHAR | Sim | Nome completo do usuário | |
| email | VARCHAR | Sim | Email do usuário | |
| status | VARCHAR | Sim | Status do usuário | |
| data_nascimento | DATE | Sim | Data de nascimento | |
| cargo | VARCHAR | Sim | Cargo do usuário | |
| funcao | VARCHAR | Sim | Função exercida | |
 
---
 
### 📹 Tabela Ativo
A tabela representa os ativos instalados nas plantas das empresas clientes.
 
| Campo | Tipo | Obrigatório | Descrição | Observação |
|-------|------|-------------|-----------|------------|
| id_ativo | INT | Sim | Identificador do ativo | PK |
| status | VARCHAR | Sim | Status operacional do ativo | |
| fabricante | VARCHAR | Não | Nome do fabricante | |
| tipo | VARCHAR | Sim | Tipo do ativo | |
| periodicidade_manutencao | INT | Sim | Intervalo em dias para manutenção preventiva | |
| descricao | VARCHAR | Não | Descrição do ativo | |
| data_instalacao | DATE | Sim | Data de instalação do ativo | |
| data_remocao | DATE | Não | Data de remoção do ativo | |
| predio | VARCHAR | Não | Prédio onde o ativo está instalado | |
| planta | VARCHAR | Não | Planta onde o ativo está instalado | |
| id_contrato | INT | Sim | Contrato ao qual o ativo pertence | FK -> contrato.id_contrato |
 
---
 
### 🛠️ Tabela Ordem_Manutencao
A tabela representa as ordens de manutenção vinculadas aos ativos.
 
| Campo | Tipo | Obrigatório | Descrição | Observação |
|-------|------|-------------|-----------|------------|
| id_ordem | INT | Sim | Identificador da ordem | PK |
| nome_ordem | INT | Sim | Nome da ordem de manutenção | |
| data_inicio | DATE | Sim | Data de início da ordem | |
| data_fim | DATE | Não | Data de conclusão da ordem | |
| tipo_manutencao | VARCHAR | Sim | Tipo da manutenção | CHECK ('PREVENTIVA', 'CORRETIVA') |
| id_ativo | INT | Sim | Ativo vinculado à ordem | FK -> ativo.id_ativo |
| descricao | VARCHAR | Não | Descrição da ordem | |
| status | VARCHAR | Sim | Status da ordem | CHECK ('PENDENTE', 'EM_ANDAMENTO', 'CONCLUIDA', 'CANCELADA') |
| comentario | VARCHAR | Não | Comentário adicional sobre a ordem | |
 
---
 
### 🔗 Tabela Ordem_Usuario
Tabela de relacionamento N:N entre ordens de manutenção e usuários.
 
| Campo | Tipo | Obrigatório | Descrição | Observação |
|-------|------|-------------|-----------|------------|
| id_ordem_manutencao | INT | Sim | Referência à ordem de manutenção | PK, FK -> ordem_manutencao.id_ordem |
| id_usuario | INT | Sim | Referência ao usuário | PK, FK -> usuario.usuario_id |
 
