# Integração Front-end com Backend Altave

Este documento descreve como o front-end Vue está integrado com o backend Spring Boot.

## Estrutura de Integração

### Diretório de Serviços (`src/services/`)

- **`ContratoService.ts`** - Serviço que encapsula todas as requisições relacionadas a contratos
  - `buscarTodos()` - GET /contrato - Carrega todos os contratos
  - `criar(payload)` - POST /contrato - Cria um novo contrato
  - `formatarData(isoDate)` - Formata datas no padrão brasileiro
  - `resolverStatus(dataFim)` - Determina o status do contrato

### Diretório de Configuração (`src/config/`)

- **`api.ts`** - Configuração centralizada de endpoints
  - `API_BASE_URL` - URL base do backend (configurável via variável de ambiente)
  - `API_ENDPOINTS` - Mapeamento de rotas da API

- **`axios.ts`** - Instância configurada do axios
  - Interceptadores de erro centralizados
  - Headers padrão (Content-Type, etc)

## Configuração

### Variáveis de Ambiente

O projeto utiliza um arquivo `.env.local` para configurar a URL do backend:

```bash
# .env.local
VITE_API_URL=http://localhost:8080
```

Para diferentes ambientes:

- **Desenvolvimento**: `http://localhost:8080`
- **Staging**: `https://staging-altave.seu-dominio.com`
- **Produção**: `https://altave.seu-dominio.com`

### Instalação e Setup

1. **Instale as dependências**:
   ```bash
   npm install
   ```

2. **Configure a URL do backend** em `.env.local`:
   ```bash
   VITE_API_URL=http://localhost:8080
   ```

3. **Inicie o servidor de desenvolvimento**:
   ```bash
   npm run dev
   ```

4. **Certifique-se que o backend está rodando** em `http://localhost:8080`

## Componentes que Consomem a API

### AdmDashboard.vue (`src/components/adm/AdmDashboard.vue`)

- **Carregamento de contratos**: Chama `ContratoService.buscarTodos()` ao montar
- **Criação de contratos**: Chama `ContratoService.criar(payload)` ao submeter formulário
- **Tratamento de erros**: Exibe mensagens de erro amigáveis ao usuário
- **Estado de carregamento**: Mostra spinner enquanto carrega dados

## Como Adicionar Novos Endpoints

### 1. Atualize a configuração de endpoints

Em `src/config/api.ts`:

```typescript
export const API_ENDPOINTS = {
  contratos: {
    list: '/contrato',
    create: '/contrato',
    get: '/contrato/:id',      // Novo
    update: '/contrato/:id',   // Novo
    delete: '/contrato/:id',   // Novo
  },
  ativos: {                     // Novo recurso
    list: '/ativo',
    create: '/ativo',
  }
}
```

### 2. Crie ou estenda o serviço

Em `src/services/AtivoService.ts` (exemplo para novo recurso):

```typescript
import axiosInstance from '@/config/axios'
import { API_ENDPOINTS } from '@/config/api'

export interface IAtivo {
  id: number
  // ... outros campos
}

class AtivoService {
  async buscarTodos(): Promise<IAtivo[]> {
    const response = await axiosInstance.get<IAtivo[]>(API_ENDPOINTS.ativos.list)
    return response.data
  }

  async buscarPorId(id: number): Promise<IAtivo> {
    const response = await axiosInstance.get<IAtivo>(`${API_ENDPOINTS.ativos.list}/${id}`)
    return response.data
  }

  async criar(payload: IAtivo): Promise<IAtivo> {
    const response = await axiosInstance.post<IAtivo>(API_ENDPOINTS.ativos.create, payload)
    return response.data
  }
}

export default new AtivoService()
```

### 3. Use o serviço no componente

```typescript
import AtivoService from '@/services/AtivoService'

// No setup
const ativos = ref<IAtivo[]>([])

onMounted(async () => {
  ativos.value = await AtivoService.buscarTodos()
})
```

## Tratamento de Erros

O axios está configurado com interceptadores que tratam erros automaticamente:

- **401 Unauthorized**: Não autorizado
- **403 Forbidden**: Acesso proibido
- **404 Not Found**: Recurso não encontrado
- **500 Internal Server Error**: Erro no servidor

Os componentes também implementam tratamento local de erros e exibem mensagens ao usuário.

## CORS (Cross-Origin Resource Sharing)

O backend está configurado com `@CrossOrigin` permitindo requisições do front-end.

Se encontrar problemas de CORS:

1. Verifique se o backend tem `@CrossOrigin` na classe controller
2. Verifique se a URL em `VITE_API_URL` está correta
3. Certifique-se que o backend está rodando e acessível

## Debugging

Para debugar requisições da API:

1. **Abra o Developer Tools** (F12 ou Cmd+Shift+I)
2. **Vá na aba Network** para ver as requisições HTTP
3. **Verifique o Console** para ver mensagens de erro

Exemplo de log útil:
```typescript
console.log('Carregando contratos...', resultado)
```

## Próximos Passos

1. **Implementar mais endpoints** conforme necessário
2. **Adicionar autenticação** (JWT ou similar)
3. **Criar store Pinia** para gerenciar estado global
4. **Adicionar testes** para os serviços
5. **Melhorar tratamento de erros** com notificações

## Referências

- [Axios Documentation](https://axios-http.com/)
- [Vue 3 Composition API](https://vuejs.org/guide/extras/composition-api-faq.html)
- [Vite Environment Variables](https://vitejs.dev/guide/env-and-modes)
