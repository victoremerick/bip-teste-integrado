# Frontend Angular

Aplicação Angular simples para consumir o backend (CRUD de Benefício e Transferência).

## Pré‑requisitos
- Node.js 18+ e npm
- Backend rodando em http://localhost:8080

## Como executar
1. Abra este diretório (frontend/) no terminal
2. Instale as dependências:
   - npm install
3. Inicie em modo desenvolvimento:
   - npm start
4. Acesse: http://localhost:4200

## Funcionalidades
- Listar benefícios
- Criar/Editar/Excluir benefício
- Efetuar transferência entre benefícios

## Observações
- O backend foi configurado com CORS para permitir chamadas a partir de http://localhost:4200.
- URLs da API usadas: http://localhost:8080/api/v1/beneficios