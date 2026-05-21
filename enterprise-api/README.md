# Enterprise API

API desenvolvida como parte de um desafio Full Stack para cadastro e gerenciamento de **Empresas** e **Fornecedores**.

O projeto utiliza **Java 21**, **Spring Boot 3**, **PostgreSQL**, **Spring Data JPA**, **Flyway**, **Docker** e segue os princípios da **Clean Architecture**.

---

## Funcionalidades

- CRUD de empresas
- CRUD de fornecedores
- Associação entre empresas e fornecedores
- Uma empresa pode ter vários fornecedores
- Um fornecedor pode trabalhar para várias empresas
- Validação de CNPJ único para empresas
- Validação de CPF/CNPJ único para fornecedores
- Fornecedor pessoa física exige RG e data de nascimento
- Empresas do Paraná não podem se associar a fornecedores pessoa física menores de idade
- Filtro de fornecedores por nome e CPF/CNPJ
- Validação de CEP através da API `cep.la`

---

## Tecnologias utilizadas

### Java 21

Foi utilizado por ser uma versão moderna e estável da linguagem Java, oferecendo boa performance, recursos recentes da linguagem e suporte adequado para aplicações corporativas.

### Spring Boot 3

Foi utilizado para facilitar a criação da API REST, configuração da aplicação, injeção de dependência, validações e integração com banco de dados.

### PostgreSQL

Foi escolhido como banco de dados relacional por ser robusto, confiável e muito usado em aplicações reais. Ele atende bem ao relacionamento entre empresas e fornecedores.

### Spring Data JPA / Hibernate

Foi utilizado para simplificar a persistência dos dados, reduzindo a necessidade de escrever SQL manual para operações comuns de CRUD.

### Flyway

Foi utilizado para versionar o banco de dados através de migrations. Assim, a estrutura do banco fica controlada por código e pode ser reproduzida em qualquer ambiente.

### Docker e Docker Compose

Foram utilizados para facilitar a execução do banco PostgreSQL e do PgAdmin localmente, evitando a necessidade de instalar o banco diretamente na máquina.

### WebClient

Foi utilizado para consumir a API externa de CEP, mantendo a integração isolada na camada de infraestrutura.

### JUnit 5 e Mockito

Foram previstos para testes unitários, principalmente das regras de negócio e dos casos de uso.

---

## Arquitetura do projeto

O projeto segue uma abordagem baseada em **Clean Architecture**, separando responsabilidades em camadas:

```text
src/main/java/com/fcosta/enterprise_api

domain
  model
  exception
  service

application
  dto
  exception
  port
    out
  usecase

infrastructure
  config
  external
  persistence
    entity
    mapper
    repository
    specification

rest
  controller
  exception
  mapper
  request
  response
```

---

## Por que Clean Architecture?

A Clean Architecture foi utilizada para manter o projeto organizado, testável e com baixo acoplamento.

A ideia principal é separar as regras de negócio dos detalhes técnicos.

Por exemplo:

- O domínio não depende de Spring.
- O domínio não depende de banco de dados.
- Os controllers não possuem regra de negócio.
- Os use cases orquestram as operações da aplicação.
- A infraestrutura implementa detalhes como JPA, PostgreSQL e integrações externas.
- Os repositories são acessados por interfaces, chamadas de ports.

Essa separação facilita a manutenção e permite trocar detalhes técnicos sem afetar a regra de negócio.

---

## Responsabilidade das camadas

### Domain

Contém as regras principais do negócio.

Exemplos:

- `Company`
- `Supplier`
- `DocumentType`
- `SupplierCompanyPolicy`
- `DomainException`

Essa camada valida regras como:

- fornecedor pessoa física precisa ter RG;
- fornecedor pessoa física precisa ter data de nascimento;
- empresa do Paraná não pode se associar a fornecedor pessoa física menor de idade.

---

### Application

Contém os casos de uso da aplicação.

Exemplos:

- `CreateCompanyUseCase`
- `UpdateCompanyUseCase`
- `CreateSupplierUseCase`
- `AssociateSupplierToCompanyUseCase`
- `ListSuppliersByCompanyUseCase`

Essa camada coordena o fluxo da aplicação, chamando domínio, repositórios e integrações externas através de interfaces.

---

### Infrastructure

Contém os detalhes técnicos da aplicação.

Exemplos:

- entidades JPA;
- repositories Spring Data;
- adapters;
- integração com API de CEP;
- configurações do WebClient;
- mappers de persistência.

---

### REST

Contém a entrada HTTP da aplicação.

Exemplos:

- controllers;
- request DTOs;
- response DTOs;
- mappers REST;
- tratamento global de exceções.

Os controllers apenas recebem requisições, convertem para commands e chamam os use cases.

---

## Pré-requisitos

Antes de rodar o projeto, é necessário ter instalado:

- Java 21
- Maven ou Maven Wrapper
- Docker
- Docker Compose
- Postman ou Insomnia para testar a API

---

## Como rodar o projeto

### 1. Clone o repositório

```bash
git clone <url-do-repositorio>
cd enterprise-api
```

---

### 2. Suba o banco de dados com Docker Compose

Na raiz do projeto, rode:

```bash
docker compose up -d
```

Esse comando deve subir os containers do PostgreSQL e do PgAdmin.

---

### 3. Verifique o `application.properties`

O arquivo deve estar em:

```text
src/main/resources/application.properties
```

Exemplo de configuração:

```properties
spring.application.name=enterprise-api

server.port=8080

spring.datasource.url=jdbc:postgresql://localhost:5432/enterprise_api
spring.datasource.username=enterprise_user
spring.datasource.password=enterprise_password
spring.datasource.driver-class-name=org.postgresql.Driver

spring.jpa.hibernate.ddl-auto=validate
spring.jpa.open-in-view=false
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

spring.flyway.enabled=true
spring.flyway.locations=classpath:db/migration

external.cep-la.base-url=http://cep.la
```

---

### 4. Rode a aplicação

No Windows:

```bash
.\mvnw.cmd spring-boot:run
```

No Linux ou Mac:

```bash
./mvnw spring-boot:run
```

A aplicação deve subir em:

```text
http://localhost:8080
```

---

### 5. Teste a API

Health check:

```http
GET http://localhost:8080/health
```

Empresas:

```http
POST   http://localhost:8080/companies
GET    http://localhost:8080/companies
GET    http://localhost:8080/companies/{companyId}
PUT    http://localhost:8080/companies/{companyId}
DELETE http://localhost:8080/companies/{companyId}
```

Fornecedores:

```http
POST   http://localhost:8080/suppliers
GET    http://localhost:8080/suppliers
GET    http://localhost:8080/suppliers/{supplierId}
PUT    http://localhost:8080/suppliers/{supplierId}
DELETE http://localhost:8080/suppliers/{supplierId}
```

Associação entre empresas e fornecedores:

```http
POST   http://localhost:8080/companies/{companyId}/suppliers
GET    http://localhost:8080/companies/{companyId}/suppliers
DELETE http://localhost:8080/companies/{companyId}/suppliers/{supplierId}
```

---

## Exemplos de requisição

### Criar empresa

```http
POST http://localhost:8080/companies
Content-Type: application/json
```

```json
{
  "cnpj": "93.114.340/0001-24",
  "fantasyName": "Older Gank Enterprise",
  "zipCode": "04194260"
}
```

---

### Criar fornecedor pessoa física

```http
POST http://localhost:8080/suppliers
Content-Type: application/json
```

```json
{
  "document": "123.456.789-09",
  "documentType": "CPF",
  "name": "João da Silva",
  "email": "joao@email.com",
  "zipCode": "80010-000",
  "rg": "12.345.678-9",
  "birthDate": "2000-05-10"
}
```

---

### Criar fornecedor pessoa jurídica

```http
POST http://localhost:8080/suppliers
Content-Type: application/json
```

```json
{
  "document": "12.345.678/0001-99",
  "documentType": "CNPJ",
  "name": "Fornecedor Tech LTDA",
  "email": "contato@fornecedortech.com",
  "zipCode": "01001000"
}
```

---

### Associar fornecedor a empresa

```http
POST http://localhost:8080/companies/{companyId}/suppliers
Content-Type: application/json
```

```json
{
  "supplierId": "{supplierId}"
}
```

---

### Listar fornecedores de uma empresa

```http
GET http://localhost:8080/companies/{companyId}/suppliers
```

---

### Remover associação

```http
DELETE http://localhost:8080/companies/{companyId}/suppliers/{supplierId}
```

---

## Como acessar o PgAdmin

Caso o `docker-compose.yml` tenha o PgAdmin configurado, acesse:

```text
http://localhost:5050
```

Login padrão:

```text
E-mail: admin@admin.com
Senha: admin
```

Configuração do servidor:

```text
Host: postgres
Port: 5432
Database: enterprise_api
Username: enterprise_user
Password: enterprise_password
```

---

## Estrutura do banco de dados

As principais tabelas são:

```text
companies
suppliers
company_suppliers
```

### companies

Armazena os dados das empresas.

Campos principais:

- id
- cnpj
- fantasy_name
- zip_code
- city
- state
- created_at
- updated_at

### suppliers

Armazena os dados dos fornecedores.

Campos principais:

- id
- document
- document_type
- name
- email
- zip_code
- rg
- birth_date
- city
- state
- created_at
- updated_at

### company_suppliers

Tabela de relacionamento entre empresas e fornecedores.

Campos principais:

- company_id
- supplier_id
- created_at

---

## Regras de negócio implementadas

- CNPJ da empresa deve ser único.
- CPF/CNPJ do fornecedor deve ser único.
- Fornecedor com `documentType = CPF` deve possuir RG.
- Fornecedor com `documentType = CPF` deve possuir data de nascimento.
- Fornecedor com `documentType = CNPJ` não precisa de RG e data de nascimento.
- Empresas do Paraná não podem ser associadas a fornecedores pessoa física menores de idade.
- CEP é validado através de uma integração externa isolada na infraestrutura.

---

## Observação sobre a integração com CEP

A integração com `cep.la` foi isolada em um adapter na camada de infraestrutura.

Como a resposta da API pode vir em HTML, a lógica de parsing fica restrita à infraestrutura. Dessa forma, as camadas de domínio e aplicação não conhecem o formato da API externa.

---

## Comandos úteis

Compilar o projeto:

```bash
.\mvnw.cmd clean compile
```

Rodar aplicação:

```bash
.\mvnw.cmd spring-boot:run
```

Parar containers:

```bash
docker compose down
```

Parar containers e remover volumes:

```bash
docker compose down -v
```
