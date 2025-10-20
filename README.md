# API de Acervo de Filmes da Globo

API RESTful para gerenciar e consultar um acervo de filmes, com foco nos que foram exibidos em sessões de filmes da Globo.

## Sobre o Projeto

Esta API foi desenvolvida para catalogar filmes e seus históricos de exibição. Ela permite carregar dados de exibições, listar e paginar os filmes cadastrados, buscar filmes por título e consultar os detalhes de um filme específico, incluindo todo o seu histórico de exibições.

## Tecnologias Utilizadas

*   **Java 17**: Versão da linguagem Java utilizada.
*   **Spring Boot 3**: Framework principal para a construção da aplicação.
*   **Spring Data JPA**: Para persistência de dados e comunicação com o banco.
*   **Maven**: Gerenciador de dependências e build do projeto.
*   **MariaDB**: Banco de dados relacional para ambiente de produção.
*   **H2 Database**: Banco de dados em memória para testes.
*   **Lombok**: Para reduzir código boilerplate (getters, setters, construtores).
*   **SpringDoc OpenAPI (Swagger)**: Para documentação interativa da API.

## Começando

Siga as instruções abaixo para ter uma cópia do projeto rodando na sua máquina local para desenvolvimento e testes.

### Pré-requisitos

*   JDK 17 ou superior
*   Maven 3.6 ou superior
*   Uma instância do MariaDB rodando

### Instalação

1.  **Clone o repositório:**
    ```sh
    git clone https://github.com/MenotiFilho/api-filmesglobo.git
    cd api-filmesglobo
    ```

2.  **Configure o banco de dados:**
    *   Crie um arquivo `src/main/resources/application.properties`.
    *   Use o arquivo `src/main/resources/application.properties.example` como modelo para configurar a conexão com seu banco de dados MariaDB e definir sua chave de API.

3.  **Execute a aplicação:**
    ```sh
    mvn spring-boot:run
    ```
    A API estará disponível em `http://localhost:8080`.

## Endpoints da API

A URL base para todos os endpoints é `/api/movies`.

### `POST /load`

Carrega uma lista de exibições de filmes no sistema. Requer autenticação via chave de API.

*   **Header:** `X-API-KEY: sua_chave_secreta`
*   **Body:**
    ```json
    [
      {
        "originalTitle": "Movie Title",
        "screeningDate": "YYYY-MM-DD",
        "session": "Sessão da Tarde"
      }
    ]
    ```

### `GET /`

Lista todos os filmes cadastrados de forma paginada.

*   **Query Params:** `page` (número da página), `size` (tamanho da página).
*   **Exemplo:** `GET /api/movies?page=0&size=10`

### `GET /search`

Busca filmes pelo título em português (case-insensitive) de forma paginada.

*   **Query Params:** `portugueseTitle` (termo de busca), `page`, `size`.
*   **Exemplo:** `GET /api/movies/search?portugueseTitle=matrix&page=0&size=5`

### `GET /{id}`

Retorna os detalhes completos de um filme específico, incluindo seu histórico de exibições.

*   **Exemplo:** `GET /api/movies/1`

### `GET /{id}/screenings`

Retorna o histórico de exibições de um filme específico.

*   **Exemplo:** `GET /api/movies/1/screenings`

## Documentação da API (Swagger)

A documentação interativa da API, gerada com o SpringDoc, está disponível na interface do Swagger UI. Com a aplicação rodando, acesse:

[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

## Executando os Testes

Para rodar a suíte de testes automatizados, execute o seguinte comando na raiz do projeto:

```sh
mvn test
```