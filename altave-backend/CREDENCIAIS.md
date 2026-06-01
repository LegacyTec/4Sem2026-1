# Credenciais de acesso — Altave SGM (ambiente demo)

**Senha padrão para todos os usuários:** `12345`

## Administradores

| Nome | E-mail | Cargo |
|------|--------|-------|
| Marcos Fernando | marcos.fernando@altave.com | ADM |
| Juliana Rocha | juliana.rocha@altave.com | ADM |

## Supervisores

| Nome | E-mail | Contrato principal |
|------|--------|-------------------|
| Carla Souza | carla.souza@altave.com | PETROBRAS SJK |
| Fernanda Lima | fernanda.lima@altave.com | VALE ITABIRA |

## Planejadores

| Nome | E-mail | Contrato principal |
|------|--------|-------------------|
| Pedro Almeida | pedro.almeida@altave.com | PETROBRAS SJK |
| Lucas Pereira | lucas.pereira@altave.com | VALE ITABIRA |

## Técnicos

| Nome | E-mail | Contrato principal |
|------|--------|-------------------|
| João Silva | joao.silva@altave.com | PETROBRAS SJK |
| Ana Costa | ana.costa@altave.com | PETROBRAS SJK |
| Ricardo Mendes | ricardo.mendes@altave.com | PETROBRAS SJK |
| Bruno Santos | bruno.santos@altave.com | PETROBRAS SJK |

---

## Dados populados no banco

### Contratos

1. **PETROBRAS SJK** — 3 plantas (SJK, BOT, ITA), 2 supervisões, 14 ativos, ~18 ordens
2. **VALE ITABIRA** — 2 plantas, 4 ativos, ordens abertas e concluídas
3. **BRASKEM CAMAÇARI** — 2 plantas, 3 ativos, contrato próximo do vencimento

### Como repopular

O seed roda automaticamente ao iniciar o backend (`DemoDataSeeder`).
Se o contrato **PETROBRAS SJK** já existir, o seed é ignorado (idempotente).

Para forçar novo seed, remova os contratos demo do banco e reinicie a aplicação:

```bash
cd altave-backend
./mvnw spring-boot:run
```
