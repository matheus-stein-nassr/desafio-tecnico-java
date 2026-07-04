# Arquitetura do frontend

O projeto segue uma arquitetura modular por responsabilidade e por funcionalidade.

```text
src/app
|-- core/                       # infraestrutura global e singletons
|   |-- interceptors/           # regras transversais do HttpClient
|   |-- models/                 # contratos compartilhados com a API
|   `-- services/               # configurações globais
|-- shared/                     # UI reutilizável e sem regra de negócio
|   |-- components/
|   |-- pipes/
|   `-- shared.module.ts
|-- features/
|   `-- events/                 # feature autocontida de eventos
|       |-- event-list/         # consulta e paginação
|       |-- event-form/         # criação e edição
|       |-- event-detail/       # visualização
|       |-- services/           # acesso ao endpoint /events
|       |-- events-routing.module.ts
|       `-- events.module.ts
|-- app-routing.module.ts       # lazy loading da feature e redirects
`-- app.module.ts               # composição da aplicação
```

## Composição dos módulos

- [AppModule](src/app/app.module.ts) carrega `BrowserModule`, `CoreModule` e `AppRoutingModule`.
- [CoreModule](src/app/core/core.module.ts) centraliza `HttpClientModule` e o interceptor global.
- [SharedModule](src/app/shared/shared.module.ts) reexporta componentes, pipes e módulos Material usados pela feature.
- [EventsModule](src/app/features/events/events.module.ts) declara as telas de eventos e importa `EventsRoutingModule`.

## Fluxo de dados

1. O componente recebe a interação do usuário.
2. O componente chama `EventService` com dados tipados.
3. O `EventService` usa `HttpClient` com o endpoint relativo `/events`.
4. O interceptor adiciona o prefixo `/api` quando a URL ainda não contém esse prefixo.
5. Durante o desenvolvimento, `proxy.conf.json` encaminha a chamada ao backend em `http://localhost:8080`.
6. O componente representa os estados de carregamento, sucesso, vazio ou erro.

Observações importantes:

- URLs absolutas passam direto pelo interceptor.
- Requisições que já começam com `/api/` não recebem novo prefixo.

## Contrato de eventos

- `EventModel`: resposta completa da API.
- `EventPayload`: corpo aceito por POST e PUT.
- `PageResponse<T>`: página retornada pelo Spring Data.
- A página do Angular Material é baseada em zero, assim como o parâmetro `page` do backend.
- `EventService` expõe `getEvents`, `getEventById`, `createEvent`, `updateEvent` e `deleteEvent`.

## Rotas

| Rota | Responsabilidade |
|---|---|
| `/events` | listagem paginada |
| `/events/new` | criação |
| `/events/:id/edit` | edição |
| `/events/:id` | detalhes |

O módulo de eventos é carregado sob demanda por [AppRoutingModule](src/app/app-routing.module.ts), que também redireciona `/` e rotas desconhecidas para `/events`.
