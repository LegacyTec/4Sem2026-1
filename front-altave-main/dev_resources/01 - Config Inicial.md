# 01 — Config inicial e mapa do projeto

Este manual é um **sumário** para quem está começando no **front-altave**: o que o projeto usa, onde está cada coisa e por onde começar. Não substitui a documentação oficial do Next.js ou do React; serve para **orientar** na primeira semana.

---

## O que é este repositório

É um **frontend** em **Next.js** (React + TypeScript). Hoje o código está propositalmente **enxuto**: uma página principal em branco, pronta para vocês irem montando telas, API e componentes em cima dessa base.

- **Nome do pacote:** `front-altave`
- **Linguagem:** TypeScript (`.ts` e `.tsx`)
- **Estilo de rotas:** **App Router** (pasta `app/`)

---

## Ferramentas que você precisa na máquina

| Ferramenta | Para quê |
| ----------- | -------- |
| **Node.js** | Rodar o Next, npm e o servidor de desenvolvimento. Use uma versão **LTS** recomendada pelo time (compatível com Next 16). |
| **npm** | Instalar dependências (`npm install`) e rodar scripts (`npm run dev`, etc.). |
| **Git** | Clonar o repo, criar branch, commit, push. |
| **Editor** | VS Code, Cursor ou outro com suporte a TypeScript e ESLint ajuda bastante. |

Opcional, mas comum:

- **Navegador** com DevTools (Chrome, Edge, Firefox) para inspecionar rede e consola.

---

## Pastas e ficheiros na raiz (mapa rápido)

| Caminho | O que é |
| -------- | -------- |
| **`app/`** | **Coração do App Router.** Cada `page.tsx` vira uma rota. `layout.tsx` envolve páginas, `globals.css` são estilos globais. |
| **`public/`** | Ficheiros estáticos servidos na raiz do site (imagens, `robots.txt`, etc.). Ex.: `public/logo.png` → URL `/logo.png`. |
| **`node_modules/`** | Dependências instaladas pelo npm. **Não edites à mão**; regenera com `npm install`. Está no `.gitignore`. |
| **`.next/`** | **Build** e cache do Next. Aparece após `npm run dev` ou `npm run build`. **Não commitas**; está ignorado no Git. |
| **`package.json`** | Lista de dependências e **scripts** (`dev`, `build`, `lint`). |
| **`package-lock.json`** | Trava versões exatas das dependências para todos terem o mesmo `npm install`. |
| **`tsconfig.json`** | Configuração do **TypeScript** (modo strict, caminhos de importação, etc.). |
| **`next.config.ts`** | Configuração do **Next.js** (redirecionamentos, domínios de imagens, etc.). |
| **`postcss.config.mjs`** | Integração do **PostCSS** (aqui liga o **Tailwind v4** ao build). |
| **`eslint.config.mjs`** | Regras do **ESLint** para manter qualidade e consistência do código. |
| **`next-env.d.ts`** | Tipos gerados automaticamente pelo Next (referências TS). |
| **`.gitignore`** | O que o Git **não** acompanha (`node_modules`, `.next`, `.env*`). |
| **`README.md`** | Apresentação geral do projeto no GitHub (pode resumir clone + `npm install` + `dev`). |
| **`dev_resources/`** | **Manuais internos** (como este). Não é código da app; é documentação para a equipa. |

Pastas que **ainda não existem** mas são habituais em projetos Next maiores (e podem aparecer no futuro):

- **`components/`** — componentes React reutilizáveis.
- **`lib/`** — funções utilitárias, cliente HTTP, autenticação, etc.
- **`hooks/`** — hooks personalizados.

Se não estiverem na raiz, é porque o projeto ainda não as criou.

---

## Alias de importação `@/*`

No `tsconfig.json` existe `"@/*": ["./*"]`.

Isso permite importar a partir da **raiz do projeto** sem `../../../`:

```ts
// Exemplo futuro (quando existir ficheiro em lib/foo.ts):
import { algo } from "@/lib/foo";
```

---

## Scripts npm (comandos do dia a dia)

| Comando | O que faz |
| -------- | ---------- |
| `npm install` | Instala as dependências listadas no `package.json` (usa o `package-lock.json`). Rode sempre após clonar o repo. |
| `npm run dev` | Sobe o **servidor de desenvolvimento** (hot reload). Abre em geral `http://localhost:3000`. |
| `npm run build` | Gera a **versão de produção** na pasta `.next/`. |
| `npm run start` | Serve a build de produção (rode `build` antes, em deploy ou teste local). |
| `npm run lint` | Corre o **ESLint** para apontar problemas de código e convenções. |

Se o `build` falhar com mensagens estranhas sobre `NODE_ENV`, vale testar com `NODE_ENV=production npm run build` (depende do ambiente da tua máquina ou CI).

---

## Principais dependências (visão de estagiário)

Não precisas memorizar tudo de uma vez. Esta tabela explica **para que serve cada bloco** no estado atual do `package.json`:

| Pacote / grupo | Ideia simples |
| ---------------- | ------------- |
| **next**, **react**, **react-dom** | Framework e biblioteca UI. O Next organiza rotas, SSR/navegação; o React desenha componentes. |
| **typescript** (dev) | Tipagem estática; o compilador e o editor avisam erros cedo. |
| **tailwindcss**, **@tailwindcss/postcss** | **CSS utilitário:** classes como `flex`, `p-4` em vez de escrever muitos ficheiros CSS à mão. |
| **eslint**, **eslint-config-next** | Qualidade e padrões alinhados com o ecossistema Next. |
| **axios** | Cliente HTTP (chamadas a APIs). |
| **react-hook-form**, **zod**, **@hookform/resolvers** | Formulários e **validação** de dados (Zod define “formatos” dos inputs). |
| **dayjs** | Datas e horas (mais leve que moment). |
| **jose** | JWT e cripto útil em contexto web (ex.: tokens). |
| **zustand** | Estado global **simples** em React (lojas pequenas sem vários níveis de boilerplate). |
| **lucide-react** | Ícones SVG como componentes. |
| **next-themes** | Alternar tema claro/escuro de forma compatível com Next. |
| **shadcn**, **@base-ui/react**, **class-variance-authority**, **clsx**, **tailwind-merge**, **sonner**, **tw-animate-css** | Ecossistema típico do **shadcn/ui** e animações; útil quando adicionarem componentes prontos (`components/ui`, etc.). Hoje a pasta pode estar vazia — os pacotes já ficam para quando avançarem com UI. |

---

## Como as rotas funcionam (App Router)

- Ficheiro **`app/page.tsx`** → rota **`/`** (página inicial).
- **`app/layout.tsx`** → envolve **todas** as páginas sob `app/` (HTML base, fontes, `<body>`).
- Criar **`app/sobre/page.tsx`** → rota **`/sobre`**.
- Parênteses em pastas, tipo **`app/(grupo)/login/page.tsx`**, formam **route groups**: o nome `(grupo)` **não** entra na URL; só organiza ficheiros (ex.: separar área autenticada da área pública).

A documentação oficial do Next sobre App Router vale a pena ler uma vez: [App Router](https://nextjs.org/docs/app).

---

## Variáveis de ambiente (nota)

Ficheiros **`.env`**, `.env.local`, etc. costumam guardar URLs de API e segredos. O template ignora **`.env*`** no Git por segurança. Quando o backend existir, o time passará um exemplo (`.env.example`) com chaves **sem valores secretos**.

---

## Onde ir depois deste documento

1. Clona o repo, `npm install`, `npm run dev`, abre `http://localhost:3000`.
2. Lê o `app/page.tsx` e o `app/layout.tsx` para ver a estrutura mínima.
3. Explora `dev_resources/README.md` para novos manuais à medida que forem escritos.
4. Tira dúvidas de produto e de API com o mentor ou responsável pelo backend.

Boa integração.
