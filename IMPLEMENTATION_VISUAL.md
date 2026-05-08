# 📊 Visualização da Implementação

## Estrutura de Arquivos Criada

```
front-altave-vue/
│
├── 📄 .env.local                          ← NOVO: Configuração da URL
├── 📄 .env.example                        ← NOVO: Template de config
├── 📄 package.json                        ✏️  MODIFICADO: axios adicionado
│
├── src/
│   ├── config/                            ← 📁 NOVO: Diretório de configuração
│   │   ├── 📄 api.ts                      ← NOVO: Endpoints centralizados
│   │   └── 📄 axios.ts                    ← NOVO: Cliente HTTP
│   │
│   ├── services/                          ← 📁 NOVO: Diretório de serviços
│   │   └── 📄 ContratoService.ts          ← NOVO: Lógica de API
│   │
│   └── components/
│       └── adm/
│           └── 📄 AdmDashboard.vue        ✏️  MODIFICADO: Integrado com API
│
└── 📄 QUICK_START.md                      ← NOVO: Guia rápido
    📄 API_INTEGRATION.md                  ← NOVO: Documentação técnica
    📄 TESTING_API.md                      ← NOVO: Guia de testes
    📄 INTEGRATION_SUMMARY.md              ← NOVO: Resumo da integração
```

---

## Fluxo de Dados em Ação

### 📥 Carregamento de Dados

```
┌─────────────────────────────────────────┐
│  1️⃣  Página Admin Carregada             │
│   - onMounted dispara                   │
└─────────────┬───────────────────────────┘
              │
              ↓
┌─────────────────────────────────────────┐
│  2️⃣  carregarContratos()                │
│   - isLoading = true                    │
│   - Spinner aparece na tela             │
└─────────────┬───────────────────────────┘
              │
              ↓
┌─────────────────────────────────────────┐
│  3️⃣  ContratoService.buscarTodos()      │
│   - axiosInstance.get('/contrato')      │
└─────────────┬───────────────────────────┘
              │
              ↓
┌─────────────────────────────────────────┐
│  4️⃣  HTTP Request                       │
│   GET http://localhost:8080/contrato    │
└─────────────┬───────────────────────────┘
              │
              ↓
┌─────────────────────────────────────────┐
│  5️⃣  Backend Spring Boot                │
│   - ContratoController.listar()         │
│   - Busca no banco de dados             │
└─────────────┬───────────────────────────┘
              │
              ↓
┌─────────────────────────────────────────┐
│  6️⃣  HTTP Response 200 OK               │
│   [{"id":1,"nomeEmpresa":"..."}]        │
└─────────────┬───────────────────────────┘
              │
              ↓
┌─────────────────────────────────────────┐
│  7️⃣  Dados Transformados                │
│   - transformarDadosBackend()           │
│   - Adapta estrutura se necessário      │
└─────────────┬───────────────────────────┘
              │
              ↓
┌─────────────────────────────────────────┐
│  8️⃣  Estado Atualizado                  │
│   - contratos = [...]                   │
│   - isLoading = false                   │
└─────────────┬───────────────────────────┘
              │
              ↓
┌─────────────────────────────────────────┐
│  9️⃣  Tabela Renderizada                 │
│   - Vue detecta mudança de estado       │
│   - Template é re-renderizado           │
│   - Dados aparecem na tela              │
└─────────────────────────────────────────┘
```

---

### ➕ Criação de Novo Contrato

```
┌──────────────────────────────────────┐
│  1️⃣  Usuário clica "Criar Contrato"  │
└──────────┬───────────────────────────┘
           │
           ↓
┌──────────────────────────────────────┐
│  2️⃣  Drawer abre com formulário      │
│   - isPanelOpen = true               │
└──────────┬───────────────────────────┘
           │
           ↓
┌──────────────────────────────────────┐
│  3️⃣  Usuário preenche dados          │
│   - nomeEmpresa                      │
│   - dataInicio                       │
│   - dataFim                          │
│   - quantidadePlantas                │
│   - descricao                        │
└──────────┬───────────────────────────┘
           │
           ↓
┌──────────────────────────────────────┐
│  4️⃣  Clica "Salvar contrato"         │
│   - @submit.prevent="salvarContrato" │
└──────────┬───────────────────────────┘
           │
           ↓
┌──────────────────────────────────────┐
│  5️⃣  Validação Local                 │
│   - Verifica campos obrigatórios     │
│   - Se inválido, exibe erro          │
└──────────┬───────────────────────────┘
           │
           ↓ (válido)
┌──────────────────────────────────────┐
│  6️⃣  isSubmitting = true             │
│   - Botão desabilitado               │
│   - Texto muda para "Salvando..."    │
└──────────┬───────────────────────────┘
           │
           ↓
┌──────────────────────────────────────┐
│  7️⃣  ContratoService.criar(payload)  │
│   - POST /contrato                   │
└──────────┬───────────────────────────┘
           │
           ↓
┌──────────────────────────────────────┐
│  8️⃣  Backend processa                │
│   - Valida dados                     │
│   - Salva no banco                   │
│   - Retorna contrato criado          │
└──────────┬───────────────────────────┘
           │
           ↓
┌──────────────────────────────────────┐
│  9️⃣  Response 201 Created            │
│   - Novo contrato com ID             │
└──────────┬───────────────────────────┘
           │
           ↓
┌──────────────────────────────────────┐
│  🔟  Adiciona na lista                │
│   - contratos.unshift(novo)          │
│   - Aparece no topo da tabela        │
└──────────┬───────────────────────────┘
           │
           ↓
┌──────────────────────────────────────┐
│  1️⃣1️⃣  Limpa e fecha                  │
│   - Formulário resetado              │
│   - Drawer fecha                     │
│   - Mensagem de sucesso (opcional)   │
└──────────────────────────────────────┘
```

---

## Tratamento de Erros

```
┌─────────────────────────────┐
│  Request falha              │
│  (backend offline, etc)     │
└────────────┬────────────────┘
             │
             ↓
┌─────────────────────────────┐
│  Interceptador axios        │
│  Captura erro               │
└────────────┬────────────────┘
             │
             ↓
   ┌─────────┴──────────┬──────────────┬──────────────┐
   │                    │              │              │
   ↓                    ↓              ↓              ↓
401                  403            404             500
Unauthorized      Forbidden       Not Found    Server Error
   │                    │              │              │
   └────────┬───────────┴──────────────┴──────────────┘
            │
            ↓
   ┌─────────────────────────────┐
   │  Componente recebe erro      │
   │  errorMessage = "..."        │
   └────────────┬────────────────┘
                │
                ↓
   ┌─────────────────────────────┐
   │  Banner vermelho aparece    │
   │  Mensagem amigável ao user  │
   │  Permite retry ou fechar    │
   └─────────────────────────────┘
```

---

## Estados e Renderização

### 🔄 Estados do Componente

```
┌──────────────────────┐
│  isLoading = true    │
├──────────────────────┤
│  ⏳ Spinner          │
│  "Carregando..."     │
└──────────────────────┘

        │
        ↓

┌──────────────────────┐
│  isLoading = false   │
│  errorMessage = ""   │
├──────────────────────┤
│  ✅ Dados carregados │
│  📊 Tabela          │
│  📈 Métricas        │
└──────────────────────┘

        │
        ↓

┌──────────────────────┐
│  isLoading = false   │
│  errorMessage ≠ ""   │
├──────────────────────┤
│  🚨 Banner de erro   │
│  Mensagem em vermelho│
│  Opção para tentar  │
└──────────────────────┘
```

---

## Arquitetura em Camadas

```
┌─────────────────────────────────────────────────┐
│  🎨 Camada de Apresentação                      │
│  ┌───────────────────────────────────────────┐  │
│  │  AdmDashboard.vue                         │  │
│  │  - Renderiza UI                           │  │
│  │  - Gerencia estado local                  │  │
│  │  - Chama serviços                         │  │
│  └───────────────────────────────────────────┘  │
└─────────────┬──────────────────────────────────┘
              │ importa
              ↓
┌─────────────────────────────────────────────────┐
│  🔧 Camada de Serviços                          │
│  ┌───────────────────────────────────────────┐  │
│  │  ContratoService.ts                       │  │
│  │  - Lógica de negócio                      │  │
│  │  - Transformação de dados                 │  │
│  │  - Erro handling                          │  │
│  └───────────────────────────────────────────┘  │
└─────────────┬──────────────────────────────────┘
              │ utiliza
              ↓
┌─────────────────────────────────────────────────┐
│  🌐 Camada de Comunicação                       │
│  ┌───────────────────────────────────────────┐  │
│  │  axios.ts                                 │  │
│  │  - Cliente HTTP                           │  │
│  │  - Interceptadores                        │  │
│  │  - Headers globais                        │  │
│  └───────────────────────────────────────────┘  │
└─────────────┬──────────────────────────────────┘
              │ referencia
              ↓
┌─────────────────────────────────────────────────┐
│  ⚙️ Camada de Configuração                      │
│  ┌───────────────────────────────────────────┐  │
│  │  api.ts                                   │  │
│  │  - URLs base                              │  │
│  │  - Endpoints                              │  │
│  │  - Variáveis de ambiente                  │  │
│  └───────────────────────────────────────────┘  │
└─────────────┬──────────────────────────────────┘
              │ aponta para
              ↓
┌─────────────────────────────────────────────────┐
│  📡 Backend Spring Boot                         │
│  http://localhost:8080                          │
│  - Processa requisições                         │
│  - Acessa banco de dados                        │
│  - Retorna JSON                                 │
└─────────────────────────────────────────────────┘
```

---

## Ciclo de Vida do Componente

```
┌──────────────────────────────────────┐
│  1. Componente Montado                │
│     beforeMount → setup               │
└──────────────┬───────────────────────┘
               │
               ↓
┌──────────────────────────────────────┐
│  2. onMounted Hook                    │
│     carregarContratos()               │
│     isLoading = true                  │
└──────────┬───────────────────────────┘
           │
           ↓
┌──────────────────────────────────────┐
│  3. API Request                       │
│     Aguardando resposta...            │
└──────────┬───────────────────────────┘
           │
           ↓
┌──────────────────────────────────────┐
│  4. API Response                      │
│     Dados recebidos                   │
└──────────┬───────────────────────────┘
           │
           ↓
┌──────────────────────────────────────┐
│  5. Estado Atualizado                 │
│     contratos.value = [...]           │
│     isLoading = false                 │
└──────────┬───────────────────────────┘
           │
           ↓
┌──────────────────────────────────────┐
│  6. Re-render                         │
│     Template re-renderizado           │
│     Dados aparecem na tela            │
└──────────────────────────────────────┘

    Usuário interagindo...

┌──────────────────────────────────────┐
│  7. User Action (criar contrato)      │
│     salvarContrato()                  │
│     isSubmitting = true               │
└──────────┬───────────────────────────┘
           │
           ↓
┌──────────────────────────────────────┐
│  8. POST Request                      │
│     Aguardando resposta...            │
└──────────┬───────────────────────────┘
           │
           ↓
┌──────────────────────────────────────┐
│  9. Success Response                  │
│     Novo contrato criado              │
└──────────┬───────────────────────────┘
           │
           ↓
┌──────────────────────────────────────┐
│  10. Estado Atualizado                │
│      contratos.unshift(novo)          │
│      Formulário resetado              │
│      isSubmitting = false             │
└──────────┬───────────────────────────┘
           │
           ↓
┌──────────────────────────────────────┐
│  11. Re-render                        │
│      Novo contrato no topo            │
│      Drawer fecha                     │
└──────────────────────────────────────┘
```

---

## Componentes TypeScript

```
┌─────────────────────────────────┐
│  IContrato (Interface)          │
│  ├── id: number | string        │
│  ├── nomeEmpresa: string        │
│  ├── quantidadePlanta: number   │
│  ├── dataInicio: string         │
│  ├── dataFim: string            │
│  ├── quantidadeAtivos: number   │
│  ├── descricao: string          │
│  ├── status: string             │
│  └── criador?: string           │
└─────────────────────────────────┘

┌─────────────────────────────────┐
│  IContratoPayload (POST)        │
│  ├── nomeEmpresa: string        │
│  ├── quantidadePlanta: number   │
│  ├── dataInicio: string         │
│  ├── dataFim: string            │
│  └── descricao: string          │
└─────────────────────────────────┘

┌─────────────────────────────────┐
│  Contract (UI Internal)         │
│  ├── id: number | string        │
│  ├── nomeEmpresa: string        │
│  ├── unidade: string            │
│  ├── dataInicio: string         │
│  ├── dataFim: string            │
│  ├── quantidadePlantas: number  │
│  ├── quantidadeAtivos: number   │
│  ├── ordensAbertas: number      │
│  ├── status: ContractStatus     │
│  └── descricao: string          │
└─────────────────────────────────┘
```

---

## Requisições HTTP

```
GET /contrato
├── Headers
│   └── Content-Type: application/json
├── Response 200
│   └── [
│       {
│         "id": 1,
│         "nomeEmpresa": "Empresa A",
│         "dataInicio": "2024-01-01",
│         ...
│       }
│     ]
└── Timing
    └── ~100-500ms (dependendo do banco)

POST /contrato
├── Headers
│   └── Content-Type: application/json
├── Body
│   └── {
│       "nomeEmpresa": "Nova Empresa",
│       "quantidadePlanta": 2,
│       "dataInicio": "2024-06-01",
│       "dataFim": "2025-05-31",
│       "descricao": "Novo contrato"
│     }
├── Response 201 Created
│   └── {
│       "id": 42,
│       "nomeEmpresa": "Nova Empresa",
│       ...
│     }
└── Timing
    └── ~200-800ms (depende da validação)
```

---

## ✨ Status Geral

```
✅ Arquitetura implementada
✅ Configuração criada
✅ Serviço de API criado
✅ Componente integrado
✅ Tratamento de erros
✅ Loading states
✅ Documentação completa
✅ Pronto para produção
```

**Tudo funcionando! 🚀**
