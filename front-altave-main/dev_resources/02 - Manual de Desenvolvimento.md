# 02 — Manual de desenvolvimento (componentes e páginas)

Este texto explica, em linguagem simples, **como se organiza a interface em React** no dia a dia: onde mora um **Navbar**, um **Footer**, e como **encaixar** isso numa **página**. É a ideia central do React — **componentes reutilizáveis** que você **importa** onde precisar.

---

## A ideia em uma frase

No React, a UI é um **Lego**: você define **peças** (componentes) e **monta** telas **juntando** essas peças. Uma **página** é só mais um componente “de topo”, que por dentro usa Navbar, conteúdo, Footer, etc.

---

## O que é um componente?

Um **componente** é uma **função** (ou, em código mais antigo, uma classe) que **devolve** o que deve aparecer no ecrã — em geral **JSX** (HTML dentro do JavaScript/TypeScript).

- Tem **nome** em maiúscula (`Navbar`, `Footer`, `ProductCard`).
- Pode receber **props** (propriedades): dados e opções de fora para dentro (`<Button label="Entrar" />`).

Exemplo mental:

```tsx
function Navbar() {
  return <nav>...</nav>;
}
```

Isso **não** “abre sozinho” numa URL. Alguém precisa **usar** `<Navbar />` dentro de outro componente (por exemplo uma **página** ou um **layout**).

---

## Onde guardar Navbar, Footer, etc.?

Por convenção neste tipo de projeto, criamos uma pasta na raiz:

**`components/`**

Dentro dela costumamos **subpastas** por papel, para não virar uma sopa de ficheiros:

| Pasta (exemplo) | Para quê |
| ----------------- | -------- |
| `components/layout/` | Coisas que **estruturam** o site: **Navbar**, **Footer**, **Sidebar**, shell da aplicação. |
| `components/shared/` ou `components/common/` | Peças genéricas usadas em muitos sítios: botões custom, cartões, modais simples. |
| `components/ui/` | Se usarem **shadcn** ou design system : botões, inputs, etc. vindos do CLI. |

Ou seja: **ah, a Navbar e o Footer são componentes**; ficam em ficheiros tipo:

- `components/layout/navbar.tsx`
- `components/layout/footer.tsx`

(Os nomes dos ficheiros podem ser `Navbar.tsx` / `navbar.tsx`; o importante é o time **combinar** um padrão e manter.)

---

## “Puxar” o componente para a página

Em React/Next não se “copia” HTML entre ficheiros: **importa-se** o componente e **usa-se** como uma **tag** com o mesmo nome.

Fluxo típico:

1. **Crias** `components/layout/navbar.tsx` com `export function Navbar() { ... }`.
2. **Na página** (ex. `app/page.tsx` ou `app/dashboard/page.tsx`), no topo do ficheiro:

   ```tsx
   import { Navbar } from "@/components/layout/navbar";
   import { Footer } from "@/components/layout/footer";
   ```

3. **Dentro** do `return` da página, montas a árvore:

   ```tsx
   export default function HomePage() {
     return (
       <>
         <Navbar />
         <main>{/* conteúdo só desta página */}</main>
         <Footer />
       </>
     );
   }
   ```

Isto é **composição**: a página **orquestra**; os **layout components** não precisam saber qual URL é — só de **serem** Navbar e Footer.

O alias **`@/`** aponta para a **raiz do projeto** (ver doc 01), por isso `@/components/...` é o caminho habitual.

---

## Página vs layout no Next.js (App Router)

Além de **`page.tsx`**, existe **`layout.tsx`**:

| Ficheiro | Papel |
| --------- | ------ |
| **`layout.tsx`** | Envolve **várias rotas** filhas. Ideal para Navbar + Footer **fixos** em **todas** as páginas desse grupo (ex. área logada). |
| **`page.tsx`** | Uma **rota** específica. O que for **só** dessa URL vai aqui (`main`, títulos, listas, formulários da página). |

Exemplo de mentalidade:

- **Layout:** “Em **tudo** o que está sob `/dashboard`, quero Navbar lateral + topo.”
- **Página:** “Em **`/dashboard/relatorios`**, o **miolo** é a tabela de relatórios.”

Assim não repetimos Navbar/Footer em **cada** `page.tsx` — colocamos uma vez no **`layout.tsx`** do grupo.

---

## Props: passar dados para dentro

Um **Footer** pode precisar do ano ou de links vindos de configuração:

```tsx
type FooterProps = {
  companyName: string;
};

export function Footer({ companyName }: FooterProps) {
  return <footer>© {companyName}</footer>;
}
```

Na página:

```tsx
<Footer companyName="Altave" />
```

**Regra:** dados que **mudam** por página ou por contexto → **props** ou hooks mais à frente. Texto **fixo** pode ficar dentro do próprio componente se fizer sentido.

---

## Um diagrama mental (árvore)

```text
RootLayout (app/layout.tsx)
  └── DashboardLayout (app/(dashboard)/layout.tsx)  ← opcional
        ├── Navbar
        ├── { children }  ← página atual (page.tsx)
        └── Footer
```

`children` é o que o Next **injeta**: a `page.tsx` da rota ativa.

---

## Boas práticas rápidas (para estagiários)

1. **Um componente por responsabilidade** — Navbar não deve carregar dados de 50 endpoints; pode **delegar** a subcomponentes.
2. **Nomes claros** — `SiteHeader` melhor que `Top`.
3. **Pastas estáveis** — `components/layout`, `components/shared`, etc., combinadas com o time.
4. **Reutilizar** — se um bloco aparece em **duas** páginas, extraí para `components/…` em vez de duplicar JSX.

---

## Relação com o doc 01

- **Doc 01:** máquina, pastas da raiz, npm, alias `@/*`.
- **Doc 02 (este):** **como pensar em componentes** (Navbar, Footer) e **como encaixá-los** em páginas e layouts.

Quando fores implementar, abre `app/page.tsx`, cria `components/layout/navbar.tsx`, **importa** e vê o resultado no `npm run dev`.
