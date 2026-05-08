# Guia de Teste da Integração API

Este documento descreve como testar a integração do front-end com o backend.

## Pré-requisitos

1. **Backend rodando**: Certifique-se que o Spring Boot está rodando em `http://localhost:8080`
2. **Front-end configurado**: A URL do backend deve estar em `.env.local`
3. **Node.js e npm**: Instalados na máquina

## Passos para Testar

### 1. Inicie o Backend

```bash
cd altave-backend

# Maven
mvn spring-boot:run

# Ou se estiver usando IDE (IntelliJ, Eclipse, VS Code)
# Execute a classe principal AltaveBackendApplication
```

O backend deve estar disponível em: `http://localhost:8080`

Você pode validar acessando: `http://localhost:8080/swagger-ui.html`

### 2. Configure o Front-end

```bash
cd front-altave-vue

# Certifique-se que .env.local existe
cat .env.local
# Deve conter: VITE_API_URL=http://localhost:8080

# Se não existir, crie:
echo "VITE_API_URL=http://localhost:8080" > .env.local
```

### 3. Inicie o Front-end

```bash
npm install  # Se não tiver feito ainda
npm run dev
```

O front-end estará disponível em: `http://localhost:5173` (porta padrão do Vite)

### 4. Teste no Browser

1. Abra `http://localhost:5173` no navegador
2. Vá para a página de Admin (Dashboard)
3. Você deve ver:
   - Um **spinner de carregamento** enquanto busca contratos
   - Os **contratos carregados** do backend (se houver algum)
   - Ou uma **mensagem de vazio** se não houver contratos

### 5. Teste a Criação de Contrato

1. Clique no botão **"Criar Contrato"**
2. Preencha o formulário:
   - **Empresa**: Nome da empresa
   - **Data início**: Data de início
   - **Data término**: Data de término
   - **Plantas**: Número de plantas
   - **Descrição**: Descrição do contrato
3. Clique em **"Salvar contrato"**
4. O novo contrato deve aparecer na tabela

## Verificando Requisições HTTP

### Via Developer Tools do Navegador

1. Abra o navegador com o Dev Tools (F12)
2. Vá à aba **"Network"**
3. Recarregue a página (F5)
4. Procure por requisições GET/POST para `/contrato`

Você deve ver:
- `GET /contrato` - Carregamento de contratos
- `POST /contrato` - Criação de novo contrato

### Via Command Line (curl)

```bash
# Listar todos os contratos
curl -X GET http://localhost:8080/contrato

# Criar um novo contrato
curl -X POST http://localhost:8080/contrato \
  -H "Content-Type: application/json" \
  -d '{
    "nomeEmpresa": "Empresa Teste",
    "quantidadePlanta": 2,
    "dataInicio": "2024-01-01",
    "dataFim": "2025-12-31",
    "descricao": "Contrato de teste"
  }'
```

### Via Swagger (Recomendado)

1. Abra `http://localhost:8080/swagger-ui.html`
2. Você verá todos os endpoints disponíveis
3. Clique em cada endpoint para testar diretamente na interface

## Troubleshooting

### Erro: "Erro ao carregar contratos. Verifique se o backend está rodando."

**Solução**:
1. Verifique se o backend está realmente rodando em `http://localhost:8080`
2. No terminal do backend, procure por mensagens de erro
3. Verifique se a porta 8080 está aberta/disponível
4. Tente acessar `http://localhost:8080/contrato` diretamente no navegador

### Erro: CORS

**Solução**:
1. Verifique se o controller tem `@CrossOrigin` (já está configurado)
2. Verifique se a URL em `.env.local` está correta
3. No console do navegador, procure por mensagens de CORS

```
Access to XMLHttpRequest at 'http://localhost:8080/contrato' 
from origin 'http://localhost:5173' has been blocked by CORS policy
```

Se isto acontecer:
1. Verifique `src/config/axios.ts`
2. Verifique o backend controller com `@CrossOrigin`

### Erro 404: Contrato não encontrado

**Solução**:
1. Verifique se o endpoint está correto em `src/config/api.ts`
2. Verifique se o backend tem este endpoint implementado
3. No Swagger, confirme que o endpoint existe

### Formulário não envia

**Solução**:
1. Abra o console (F12 > Console)
2. Procure por mensagens de erro em vermelho
3. Tente preencher todos os campos obrigatórios
4. Verifique se não há validação bloqueando

## Monitorando Logs

### Backend

Os logs do backend mostram cada requisição:

```
2024-01-15 10:30:15 [INFO] GET /contrato
2024-01-15 10:30:15 [INFO] Listando contratos
2024-01-15 10:30:15 [INFO] Retornando 3 contratos
```

### Front-end

Abra o console (F12 > Console) para ver logs do front-end:

```javascript
// Ao carregar a página
"Carregando contratos..."

// Ao criar um contrato
"Contrato criado com sucesso"

// Se houver erro
"Erro ao carregar contratos:" [Error object]
```

## Dados de Teste

Para testar com dados realistas, crie alguns contratos:

### Contrato 1 - Ativo
```json
{
  "nomeEmpresa": "Empresa A",
  "quantidadePlanta": 3,
  "dataInicio": "2023-01-01",
  "dataFim": "2025-12-31",
  "descricao": "Contrato principal"
}
```

### Contrato 2 - Expirando (dentro de 30 dias)
```json
{
  "nomeEmpresa": "Empresa B",
  "quantidadePlanta": 2,
  "dataInicio": "2022-06-01",
  "dataFim": "2025-02-15",
  "descricao": "Contrato expirando em breve"
}
```

### Contrato 3 - Inativo (data passada)
```json
{
  "nomeEmpresa": "Empresa C",
  "quantidadePlanta": 1,
  "dataInicio": "2020-01-01",
  "dataFim": "2024-01-01",
  "descricao": "Contrato inativo"
}
```

## Checklist de Teste

- [ ] Backend está rodando em http://localhost:8080
- [ ] Front-end carrega contratos ao iniciar
- [ ] Spinner aparece durante carregamento
- [ ] Contratos são exibidos na tabela
- [ ] Status dos contratos está correto (Ativo/Expirando/Inativo)
- [ ] Posso criar um novo contrato
- [ ] Novo contrato aparece na tabela imediatamente
- [ ] Dados são salvos no backend (recarregar página e aparecem)
- [ ] Mensagens de erro aparecem quando o backend está offline
- [ ] Formatação de datas está em pt-BR

## Próximos Testes

1. **Teste de Performance**: Carregar 1000+ contratos
2. **Teste de Erro**: Desligar o backend e ver comportamento
3. **Teste de Validação**: Enviar dados inválidos
4. **Teste de Responsividade**: Testar em diferentes tamanhos de tela

## Suporte

Se encontrar problemas:

1. Verifique os logs do backend e frontend
2. Abra o Network tab no Dev Tools
3. Procure por padrões nos erros
4. Consulte o arquivo `API_INTEGRATION.md`
