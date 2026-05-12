# 🚀 Quick Start - Integração Front-end + Backend

## 5 Passos para Começar

### 1️⃣ Backend Rodando
```bash
cd altave-backend
mvn spring-boot:run
```
✅ Debe estar disponível em: `http://localhost:8080`

### 2️⃣ Instale Dependências (Se não tiver feito)
```bash
cd front-altave-vue
npm install
```

### 3️⃣ Configure a URL (Já feito!)
O arquivo `.env.local` já está criado com:
```
VITE_API_URL=http://localhost:8080
```

### 4️⃣ Inicie o Front-end
```bash
npm run dev
```
✅ Acesse em: `http://localhost:5173`

### 5️⃣ Teste
1. Abra a página de Admin
2. Você deve ver um **spinner carregando**
3. Depois verá os **contratos do backend**
4. Clique em "Criar Contrato" para adicionar um novo

---

## 📁 Arquivos Criados/Modificados

### Novos Arquivos
```
src/
├── config/
│   ├── api.ts              ← Endpoints da API
│   └── axios.ts            ← Cliente HTTP
└── services/
    └── ContratoService.ts  ← Lógica de requisições

.env.local                   ← URL do backend
.env.example                 ← Template de configuração
```

### Modificados
```
src/components/adm/AdmDashboard.vue  ← Integrado com API
package.json                         ← axios adicionado
```

---

## ✨ O que Funciona Agora

| Feature | Status |
|---------|--------|
| 📋 Carrega contratos da API | ✅ |
| ➕ Criar novo contrato | ✅ |
| 💾 Salva no banco de dados | ✅ |
| ⏳ Loading spinner | ✅ |
| 🚨 Mensagens de erro | ✅ |
| 🔄 Atualiza lista automaticamente | ✅ |

---

## 🔍 Como Validar

### No Browser (Recomendado)
1. Abra `http://localhost:5173`
2. Vá à página Admin
3. Você deve ver a tabela carregando dados

### Via Dev Tools
1. Pressione `F12`
2. Vá na aba **Network**
3. Procure por `/contrato` nas requisições
4. Deve ver `GET /contrato` quando carrega

### Via Terminal
```bash
# Teste o endpoint direto
curl http://localhost:8080/contrato

# Crie um contrato
curl -X POST http://localhost:8080/contrato \
  -H "Content-Type: application/json" \
  -d '{
    "nomeEmpresa": "Empresa Teste",
    "quantidadePlanta": 1,
    "dataInicio": "2024-01-01",
    "dataFim": "2025-12-31",
    "descricao": "Teste"
  }'
```

---

## ⚠️ Se Algo Não Funcionar

### Backend não está rodando?
```bash
# Verifique se a porta 8080 está livre
lsof -i :8080

# Se estiver em uso, libere a porta ou mude no backend
```

### Erro de CORS?
- Verifique se o backend tem `@CrossOrigin` (já tem!)
- Verifique se `.env.local` tem `VITE_API_URL=http://localhost:8080`

### Dados não aparecem?
1. Abra Dev Tools (F12)
2. Vá em Console
3. Procure por mensagens de erro em vermelho
4. Verifique os logs do backend

---

## 📚 Documentação Completa

- **`API_INTEGRATION.md`** - Guia técnico detalhado
- **`TESTING_API.md`** - Como testar tudo
- **`INTEGRATION_SUMMARY.md`** - Resumo do que foi feito

---

## 🎯 Próximos Passos

Depois que validar tudo funcionando:

1. **Adicione mais endpoints** (GET /ativo, DELETE /contrato/:id, etc)
2. **Implemente autenticação** (JWT tokens)
3. **Crie mais serviços** (AtivoService, OrdemService, etc)
4. **Adicione Pinia store** para gerenciar estado global

---

## 💡 Dicas

- Mantenha o backend rodando em um terminal separado
- Use `npm run dev` para desenvolvimento com hot reload
- Abra Dev Tools sempre que testar (vê os logs e requisições)
- Os dados são reais - tudo salvo no banco!

---

## ✅ Checklist Final

- [ ] Backend rodando em http://localhost:8080
- [ ] `.env.local` existe e configurado
- [ ] `npm install` executado
- [ ] `npm run dev` rodando
- [ ] Página Admin carrega sem erros
- [ ] Contratos aparecem na tabela
- [ ] Consegue criar novo contrato
- [ ] Novo contrato salva no banco

**Tudo verde? Parabéns! Sua integração está funcionando! 🎉**

---

## 📞 Suporte

Dúvidas? Verifique em ordem:

1. `QUICK_START.md` (este arquivo)
2. `API_INTEGRATION.md` (detalhes técnicos)
3. `TESTING_API.md` (guia de teste)
4. Dev Tools do navegador (F12 > Network)
5. Logs do terminal do backend
