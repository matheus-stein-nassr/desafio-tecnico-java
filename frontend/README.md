# Frontend

Frontend de gestão de eventos desenvolvido com Angular 17.3.17.

Consulte [ARCHITECTURE.md](./ARCHITECTURE.md) para entender a organização dos módulos, o fluxo HTTP e as regras de evolução do projeto.

## Visão Geral

O aplicativo segue uma arquitetura modular com separação entre infraestrutura global, componentes reutilizáveis e funcionalidades de negócio.

- `core`: serviços, interceptors e modelos compartilhados com a aplicação inteira.
- `shared`: componentes, pipes e módulos reutilizáveis.
- `features/events`: lista, formulário, detalhes e rotas da funcionalidade de eventos.

## Requisitos

- Node.js e npm instalados.
- Backend disponível em `http://localhost:8080`.

Durante o desenvolvimento, o proxy configurado em [proxy.conf.json](./proxy.conf.json) encaminha automaticamente as requisições feitas em `/api` para o backend.

## Como Executar

1. Instale as dependências com `npm install`.
2. Inicie o backend na porta `8080`.
3. Execute o frontend com `npm start`.
4. A aplicação ficará disponível em `http://localhost:4200/`.

## Scripts Disponíveis

- `npm start`: sobe o servidor de desenvolvimento com proxy configurado.
- `npm run build`: gera a versão de produção em `dist/`.
- `npm test`: executa os testes unitários com Karma.

## Rotas Principais

- `/events`: listagem paginada.
- `/events/new`: criação de evento.
- `/events/:id/edit`: edição de evento.
- `/events/:id`: detalhes do evento.
