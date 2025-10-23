# 🏗️ Desafio Fullstack Integrado

## 🎯 Objetivo
Criar solução completa em camadas (DB, EJB, Backend, Frontend), corrigindo bug em EJB e entregando aplicação funcional.

## 📦 Estrutura
- db/: scripts schema e seed
- ejb-module/: serviço EJB com bug a ser corrigido
- backend-module/: backend Spring Boot
- frontend/: app Angular
- docs/: instruções e critérios
- .github/workflows/: CI

## ✅ Tarefas do candidato
1. Executar db/schema.sql e db/seed.sql
2. Corrigir bug no BeneficioEjbService
3. Implementar backend CRUD + integração com EJB
4. Desenvolver frontend Angular consumindo backend
5. Implementar testes
6. Documentar (Swagger, README)
7. Submeter via fork + PR

## 🐞 Bug no EJB
- Transferência não verifica saldo, não usa locking, pode gerar inconsistência
- Espera-se correção com validações, rollback, locking/optimistic locking

## 📊 Critérios de avaliação
- Arquitetura em camadas (20%)
- Correção EJB (20%)
- CRUD + Transferência (15%)
- Qualidade de código (10%)
- Testes (15%)
- Documentação (10%)
- Frontend (10%)

---

## 📚 Documentação da API (Swagger)
Foi adicionada a documentação OpenAPI/Swagger ao backend (Spring Boot) usando o springdoc-openapi.

- Swagger UI: http://localhost:8080/swagger-ui/index.html
- Documentação JSON (OpenAPI): http://localhost:8080/v3/api-docs

A UI lista automaticamente os endpoints do controller `BeneficioController` sob o path `/api/v1/beneficios`.

### Como executar o backend
1. Certifique-se de ter o Java 17+ e Maven instalados.
2. Na raiz do projeto, rode primeiro `mvn -q -pl ejb-module -am install` para compilar o módulo EJB.
3. Depois rode o backend: `mvn -q -pl backend-module -am spring-boot:run`.
4. Acesse a documentação em `http://localhost:8080/swagger-ui/index.html`.

### Endpoints principais
- GET `/api/v1/beneficios` — Lista benefícios
- GET `/api/v1/beneficios/{id}` — Detalha um benefício
- POST `/api/v1/beneficios` — Cria
- PUT `/api/v1/beneficios/{id}` — Atualiza
- DELETE `/api/v1/beneficios/{id}` — Remove
- POST `/api/v1/beneficios/transfer` — Transfere saldo entre benefícios `{fromId, toId, amount}`

### Observações
- CORS liberado para `http://localhost:4200` (frontend Angular).
- O arquivo `OpenApiConfig.java` define metadados (título/versão) da documentação.
