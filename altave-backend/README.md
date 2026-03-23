# Backend — Sistema de Gestão de Manutenções

Stack: **Spring Boot 3.2 + Spring Data JPA + Oracle Database**

---

## Pré-requisitos

- Java 17
- Maven 3.8+
- Oracle Database XE (local) ou acesso ao OCI

---

## Configurar o banco Oracle local

### 1. Criar o usuário e schema

Conecte como `SYSTEM` e execute:

```sql
CREATE USER altave IDENTIFIED BY altave123;
GRANT CONNECT, RESOURCE, DBA TO altave;
```

### 2. Executar o script de criação das tabelas

Com o usuário `altave`:

```bash
sqlplus altave/altave123@localhost:1521/XE @src/main/resources/banco.sql
```

Ou abra o arquivo `src/main/resources/banco.sql` no SQL Developer e execute conectado como `altave`.

O script cria:
- Todas as sequences e tabelas
- Triggers de atualização de status do ativo
- Dados iniciais para teste (clientes, contratos, ativos, técnicos, manutenções e checklist)

---

## Configurar a conexão

Edite `src/main/resources/application.properties` se a sua instância Oracle usar porta ou SID diferente:

```properties
spring.datasource.url=jdbc:oracle:thin:@localhost:1521:XE
spring.datasource.username=altave
spring.datasource.password=altave123
```

---

## Rodar a aplicação

```bash
./mvnw spring-boot:run
```

A API sobe em `http://localhost:8080`.

---

## Documentação dos endpoints (Swagger)

Acesse em: `http://localhost:8080/swagger-ui.html`

### Endpoints disponíveis

| Método | Rota | Descrição |
|--------|------|-----------|
| `POST` | `/manutencoes` | Cria uma manutenção corretiva |
| `GET` | `/manutencoes/fila` | Lista fila priorizada de pendentes |
| `GET` | `/manutencoes/{id}` | Busca manutenção por id |
| `GET` | `/manutencoes/historico/ativo/{ativoId}` | Histórico de um ativo |
| `PATCH` | `/manutencoes/{id}/status` | Altera status da manutenção |
| `GET` | `/ordens/{ordemId}/checklist` | Lista itens do checklist |
| `PATCH` | `/ordens/checklist/{itemId}` | Atualiza item do checklist |
| `GET` | `/ordens/{ordemId}/checklist/validar` | Valida se obrigatórios estão concluídos |
| `GET` | `/ativos` | Lista todos os ativos |
| `GET` | `/ativos/{id}` | Busca ativo por id |
| `GET` | `/ativos/contrato/{contratoId}` | Ativos de um contrato |
| `GET` | `/ativos/status/{status}` | Ativos por status |
| `GET` | `/tecnicos` | Lista todos os técnicos |
| `GET` | `/tecnicos/{id}` | Busca técnico por id |
| `GET` | `/tecnicos/disponiveis` | Lista técnicos disponíveis |

---

## Estrutura do projeto

```
src/main/java/com/legacytech/altave/
├── AltaveApplication.java       # Ponto de entrada
├── CorsConfig.java              # Configuração de CORS
├── controller/
│   ├── ManutencaoController.java
│   ├── ChecklistController.java
│   ├── AtivoController.java
│   └── TecnicoController.java
├── service/
│   ├── ManutencaoService.java
│   ├── ChecklistService.java
│   ├── AtivoService.java
│   └── TecnicoService.java
├── repository/
│   ├── ManutencaoRepository.java
│   ├── AtivoRepository.java
│   ├── TecnicoRepository.java
│   ├── HistoricoStatusRepository.java
│   ├── OrdemServicoRepository.java
│   └── ChecklistExecucaoRepository.java
├── model/
│   ├── Cliente.java
│   ├── Contrato.java
│   ├── Certificacao.java
│   ├── Localizacao.java
│   ├── TipoEquipamento.java
│   ├── RegraManutencao.java
│   ├── Ativo.java
│   ├── Tecnico.java
│   ├── TecnicoCertificacao.java
│   ├── TecnicoCertificacaoId.java
│   ├── Manutencao.java
│   ├── HistoricoStatus.java
│   ├── OrdemServico.java
│   ├── ChecklistTemplate.java
│   ├── ChecklistItem.java
│   └── ChecklistExecucao.java
└── dto/
    ├── ManutencaoRequest.java
    ├── ManutencaoResponse.java
    ├── AlterarStatusRequest.java
    └── ChecklistExecucaoRequest.java

src/main/resources/
├── application.properties
└── banco.sql
```

---

## Board do projeto

[Sprint 1 — GitHub Projects](https://github.com/orgs/LegacyTec/projects/16/views/1)

---

## Exemplos de requisição

### Criar manutenção corretiva

```http
POST /manutencoes
Content-Type: application/json

{
  "ativoId": 3,
  "tipo": "corretiva",
  "criticidade": "alta",
  "dtPrevista": "2025-03-25",
  "dtLimite": "2025-03-30",
  "observacoes": "Falha na comunicacao detectada pelo operador"
}
```

### Avançar status para em execução

```http
PATCH /manutencoes/1/status
Content-Type: application/json

{
  "novoStatus": "em_execucao",
  "usuarioId": 1
}
```

### Cancelar com motivo obrigatório

```http
PATCH /manutencoes/1/status
Content-Type: application/json

{
  "novoStatus": "cancelada",
  "motivo": "Tecnico indisponivel, reagendado para proxima semana",
  "usuarioId": 1
}
```

### Marcar item do checklist como concluído

```http
PATCH /ordens/checklist/2
Content-Type: application/json

{
  "concluido": "S",
  "observacao": "Helices inspecionadas, sem danos visíveis"
}
```
