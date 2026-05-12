# 🔍 Guia de Debug - Frontend Não Carrega Dados

## Problema Identificado

Frontend Vue não estava carregando dados do backend, mesmo com backend rodando.

## Causas Possíveis

1. **CORS bloqueando requisições**
2. **URL do backend incorreta**
3. **Frontend não conseguindo conectar ao backend**
4. **H2 Database perdendo dados ao reiniciar**
5. **Proxy não configurado no Vite**

## Soluções Aplicadas

### ✅ 1. Configuração do Vite Proxy

Adicionado proxy no `vite.config.ts` para redirecionar requisições:

```typescript
server: {
  proxy: {
    '/contrato': {
      target: 'http://localhost:8080',
      changeOrigin: true,
    },
    // ... outros endpoints
  },
},
```

### ✅ 2. Configuração do Axios

Atualizado `src/config/axios.ts` para usar a URL correta:

```typescript
const API_BASE_URL = import.meta.env.DEV
  ? "http://localhost:8080"  // Desenvolvimento
  : import.meta.env.VITE_API_URL || "http://localhost:8080"; // Produção
```

### ✅ 3. Adição de Logs Detalhados

Adicionados logs em vários pontos:

**axios.ts:**
```javascript
console.log("📡 Requisição GET para", url);
console.log("✅ Resposta recebida:", status, url);
console.log("❌ Erro na resposta:", status, mensagem);
```

**ContratoService.ts:**
```javascript
console.log("📡 [ContratoService.buscarTodos] Chamando:", endpoint);
console.log("✅ [ContratoService.buscarTodos] Resposta:", data);
console.log("❌ [ContratoService.buscarTodos] Erro:", error);
```

**AdmDashboard.vue:**
```javascript
console.log("🔵 [AdmDashboard] Componente montado");
console.log("📡 [AdmDashboard] Iniciando carregamento");
console.log("✅ [AdmDashboard] Contratos carregados");
console.log("❌ [AdmDashboard] Erro ao carregar");
```

## 🔍 Como Debugar

### Passo 1: Verificar Backend

```bash
# Backend deve estar respondendo
curl http://localhost:8080/contrato

# Você deve ver 4 contratos em JSON
```

### Passo 2: Abrir Console do Navegador

1. Abra http://localhost:5173
2. Pressione **F12** (ou Cmd+Option+I no Mac)
3. Vá na aba **Console**

### Passo 3: Recarregar Página

Pressione **Ctrl+R** (ou Cmd+R no Mac) para forçar reload da página

### Passo 4: Procurar pelos Logs

Você deve ver logs assim:

```
🔵 [AdmDashboard] Componente montado, carregando contratos...
📡 [AdmDashboard] Iniciando carregamento de contratos...
📡 [ContratoService] Chamando buscarTodos()
📡 Requisição GET para http://localhost:8080/contrato
✅ Resposta recebida: 200
✅ [ContratoService] Dados recebidos: [Array(4)]
📊 Total de contratos: 4
✅ [AdmDashboard] Contratos carregados e transformados
```

## ❌ Possíveis Erros e Soluções

### Erro: "Erro ao carregar contratos"

**Causa:** Backend offline ou URL incorreta

**Solução:**
```bash
# 1. Verifique se backend está rodando
curl http://localhost:8080/contrato

# 2. Verifique se porta é 8080 (não 8081, 9000, etc)

# 3. Reinicie backend
pkill -f "spring-boot:run"
cd altave-backend
./mvnw spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=h2"
```

### Erro: "CORS policy blocked"

**Causa:** Backend não permitindo requisições cross-origin

**Solução:**
- Backend já tem `@CrossOrigin` configurado
- Verifique se está realmente rodando o backend com H2 profile

### Erro: Contratos aparecem mas desaparecem ao recarregar

**Causa:** H2 Database é em memória (usa `create-drop`)

**Solução:**
- Isso é normal em desenvolvimento
- Dados são perdidos ao reiniciar o backend
- Para manter dados, adicione contratos via API ao iniciar

### Erro: "Cannot get /contrato"

**Causa:** Axios está usando URL relativa que não existe

**Solução:**
- Verifique `src/config/axios.ts`
- Deve usar `http://localhost:8080` em desenvolvimento
- NÃO usar URL relativa (`/contrato`)

## 📊 Verificar Status

### Backend está respondendo?

```bash
curl -v http://localhost:8080/contrato
# Deve retornar: HTTP/1.1 200 OK
```

### Frontend está rodando?

```bash
curl http://localhost:5173
# Deve retornar: <!DOCTYPE html>...
```

### Dados estão no banco?

```bash
# Acesse H2 Console
http://localhost:8080/h2-console

# Usuário: sa
# Senha: (vazia)

# Execute: SELECT COUNT(*) FROM contrato;
# Deve mostrar: 4
```

## 🛠️ Ferramentas de Debug

### Console do Navegador (F12)

- **Console tab:** Veja todos os logs do JavaScript
- **Network tab:** Veja requisições HTTP
  - Filter por `/contrato`
  - Procure por GET e POST
  - Verifique Status (200 = OK, 4xx = erro, 5xx = erro servidor)

### cURL

```bash
# Teste GET
curl -v http://localhost:8080/contrato

# Teste POST
curl -X POST http://localhost:8080/contrato \
  -H "Content-Type: application/json" \
  -d '{
    "nomeEmpresa": "Teste",
    "quantidadePlanta": 1,
    "dataInicio": "2024-01-01",
    "dataFim": "2025-12-31",
    "descricao": "Teste",
    "criador": "Teste",
    "quantidadeSupervisao": 1,
    "status": "ATIVO",
    "usuarios": []
  }'
```

### H2 Console

```
URL: http://localhost:8080/h2-console
- Verifique dados na tabela contrato
- Execute queries SQL
- Veja estrutura das tabelas
```

### Swagger UI

```
URL: http://localhost:8080/swagger-ui.html
- Teste endpoints interativamente
- Veja documentação automática
- Teste GET /contrato direto
```

## 📝 Checklist de Debug

- [ ] Backend rodando (`mvn spring-boot:run` with h2 profile)
- [ ] Frontend rodando (`npm run dev`)
- [ ] Abra http://localhost:5173
- [ ] Abra Dev Tools (F12)
- [ ] Recarregue página (Ctrl+R)
- [ ] Procure por logs em Console
- [ ] Veja "Total de contratos: 4"
- [ ] Veja os 4 contratos na tabela
- [ ] Network tab mostra GET /contrato com status 200
- [ ] Clique "Criar Contrato"
- [ ] Preencha e salve
- [ ] Nova linha aparece na tabela imediatamente
- [ ] Recarregue página
- [ ] Novo contrato ainda está lá

## 🚀 Se Tudo Funcionar

```
✅ Backend respondendo em http://localhost:8080
✅ Frontend carregando dados em http://localhost:5173
✅ Console mostrando logs detalhados
✅ Tabela exibindo 4 contratos
✅ Criar novo contrato funciona
✅ Dados persistem após reload
```

## 📞 Próximos Passos

1. Explore a interface Admin
2. Teste criar contrato novo
3. Abra H2 Console para verificar banco
4. Teste Swagger UI
5. Estude os logs para entender fluxo

## 🎯 O Que Foi Corrigido

1. ✅ Adicionado H2 Database para testes rápidos
2. ✅ Criado profile 'h2' no backend
3. ✅ Corrigido ContratoServiceImpl (aceita contratos sem usuários)
4. ✅ Configurado Vite proxy para redirecionar requisições
5. ✅ Atualizado Axios para usar URL correta
6. ✅ Adicionados logs detalhados em 3 arquivos
7. ✅ Documentação de debug

---

**Se ainda não ver dados, abra o console (F12) e procure pelos logs!**
**Os logs vão dizer exatamente onde está o problema.**

