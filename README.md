# Product API

API REST minimalista para **produtos**, feita com **Spring Boot 3 (Java 17)**.  
Durante a disciplina de **Gestão de Configuração II**, a API será evoluída — o foco principal é o **flow (GitHub Flow) + CI/CD**.

O **GitHub Flow** foi o flow escolhido, pois trata-se de uma API simples, então um fluxo de trabalho mais simples é mais adequado.


---

## Status

[![Docker Pulls](https://badgen.net/docker/pulls/jontinhaj/product-api)](https://hub.docker.com/r/jontinhaj/product-api)

**Docker Hub:** https://hub.docker.com/r/jontinhaj/product-api


## ⚙️ Requisitos

- Java 17
- Maven 3.9+
- (Opcional) Postman ou curl para testar

Dependências (via Spring Initializr):
- Spring Web
- Spring Data JPA
- H2 Database
- Validation (Jakarta Validation)
- (Opcional) DevTools, Lombok

---

## ▶️ Como rodar

```bash
mvn spring-boot:run
# ou
mvn clean package
java -jar target/*.jar
```

A API sobe por padrão em: `http://localhost:8080`

---

## 🗄️ Banco H2 (memória)

Config esperado em `application.properties`:

```properties
spring.h2.console.enabled=true
spring.h2.console.path=/h2

spring.datasource.url=jdbc:h2:mem:productsdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

### Acessar o console H2
1. Suba a aplicação.
2. Abra `http://localhost:8080/h2` no navegador.
3. Preencha:
   - **JDBC URL**: `jdbc:h2:mem:productsdb`
   - **User**: `sa`
   - **Password**: *(vazio)*
4. Clique em **Connect**.

> O banco em memória existe **apenas enquanto a aplicação estiver rodando**.

---

## ➕ Inserindo dados manualmente

### Opção A: pelo console H2 (gráfico)
1. Em `http://localhost:8080/h2`, clique em **New**.
2. Selecione a tabela `PRODUTO`.
3. Preencha os campos (ex.: `nome = Laptop`, `categoria = Eletronico`) e **Save**.

### Opção B: via SQL no console H2
No editor SQL do console, rode:
```sql
INSERT INTO produto (nome, categoria) VALUES ('Laptop', 'Eletronico');
INSERT INTO produto (nome, categoria) VALUES ('Mouse Gamer', 'Periferico');
```

> **Dica (caso use `data.sql` e dê erro)**: adicione esta propriedade para garantir que o `data.sql` rode **depois** do JPA criar o schema:
> ```properties
> spring.jpa.defer-datasource-initialization=true
> ```

---

## 🌐 Endpoints

- **GET** `/api/produtos` — lista todos os produtos  
- - **POST** `/api/produtos` — cria um produto
  - Body (JSON):
    ```json
    { "id": 1, "nome": "Headset USB", "categoria": "Periferico" }
    ```

---

## 🧪 Testando com Postman

### GET
1. Abra o Postman → **New → HTTP Request**.
2. Método: **GET**  
   URL: `http://localhost:8080/api/produtos`
3. Send → Deve retornar `200 OK` com a lista de produtos.

### POST *(disponível após a Feature 2)*
1. Método: **POST**  
   URL: `http://localhost:8080/api/produtos`
2. Aba **Headers**:  
   `Content-Type: application/json`
3. Aba **Body** → **raw** → **JSON**:
   ```json
   { "nome": "Teclado Mecânico", "categoria": "Periferico" }
   ```
4. Send → Esperado **201 Created** + **Location** no header apontando para `/api/produtos/{id}`.
5. Faça um GET novamente e confirme o novo item na lista.
---

## 🧰 Testando com curl (alternativa)

```bash
# GET
curl -s http://localhost:8080/api/produtos | jq
``` 

## Docker

Imagens oficiais no Docker Hub:  
`https://hub.docker.com/r/JontinhaJ/product-api`

### Como executar
```bash
docker run --rm -p 8080:8080 JontinhaJ/product-api:latest
# GET http://localhost:8080/api/products
