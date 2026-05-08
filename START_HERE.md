# 🚀 START HERE - Integração Front-end + Backend

## ⚡ Comece em 5 Minutos

### Passo 1: Backend Rodando
```bash
cd altave-backend
mvn spring-boot:run
```
Aguarde até ver: `Spring Boot started`

### Passo 2: Frontend Rodando
```bash
cd front-altave-vue
npm install  # primeira vez
npm run dev
```
Aguarde até ver: `Local: http://localhost:5173`

### Passo 3: Teste
Abra no navegador: **http://localhost:5173**

Vá à página **Admin** (Dashboard)

Você deve ver:
- ⏳ Spinner carregando
- 📊 Tabela com contratos do banco
- ➕ Botão "Criar Contrato"

## ✅ Pronto!

Se conseguiu chegar até aqui, **tudo está funcionando corretamente**! 🎉

---

## 📚 Documentação Disponível

| Documento | Tempo | Conteúdo |
|-----------|-------|----------|
| **QUICK_START.md** | 5 min | 5 passos para começar |
| **INTEGRATION_SUMMARY.md** | 10 min | O que foi implementado |
| **API_INTEGRATION.md** | 20 min | Detalhes técnicos |
| **TESTING_API.md** | 30 min | Como testar tudo |
| **IMPLEMENTATION_VISUAL.md** | 15 min | Diagramas e fluxos |
| **README_INTEGRATION.md** | 10 min | Índice e FAQ |

---

## 🔍 Se Algo Não Funcionar

### Backend não conecta?
```bash
# Verifique se a porta está livre
lsof -i :8080

# Se tiver algo, libere ou mude a porta
```

### Frontend não carrega dados?
1. Abra DevTools (F12)
2. Vá em "Console"
3. Procure por erros em vermelho
4. Verifique se backend está realmente rodando em :8080

### Erro de CORS?
Verifique que `.env.local` tem:
```
VITE_API_URL=http://localhost:8080
```

---

## 🎯 Próximos Passos

Depois de validar tudo funciona:

1. **Testar com dados reais** do banco
2. **Criar novo contrato** e validar se salva
3. **Explorar o código** em `src/services/` e `src/config/`
4. **Ler documentação técnica** para expandir

---

## 💡 O Que Foi Criado

✅ Serviço de API (`ContratoService.ts`)
✅ Cliente HTTP configurado (`axios.ts`)
✅ Endpoints centralizados (`api.ts`)
✅ Componente integrado (`AdmDashboard.vue`)
✅ Configuração por ambiente (`.env.local`)
✅ Documentação profissional (6 guias)

---

## 📞 Precisa de Ajuda?

1. **Para começar**: Leia este arquivo (você está aqui!)
2. **Para entender**: Leia `INTEGRATION_SUMMARY.md`
3. **Para detalhes**: Leia `API_INTEGRATION.md`
4. **Para testar**: Leia `TESTING_API.md`
5. **Para visualizar**: Leia `IMPLEMENTATION_VISUAL.md`

---

## ✨ Status

```
✅ Integração Completa
✅ Código Pronto
✅ Documentação Feita
✅ Pronto para Produção
```

**Você está 100% pronto para começar!** 🚀
