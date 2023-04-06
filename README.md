<h1 align="center">  DS CATALOG  </h1>
<p align="center">
  <img src="https://img.shields.io/static/v1?label=spring&message=framework&color=blue&style=for-the-badge&logo=SPRING"/>
  <img src="https://img.shields.io/static/v1?label=Postman&message=API management&color=blue&style=for-the-badge&logo=postman"/>
  <img src="https://img.shields.io/static/v1?label=Apache&message=Dependency manager&color=blue&style=for-the-badge&logo=apache"/>
  <img src="http://img.shields.io/static/v1?label=License&message=MIT&color=green&style=for-the-badge"/>
  <img src="http://img.shields.io/static/v1?label=FASE&message=BACKEND&color=RED&style=for-the-badge"/>
</p>  

> Status do Projeto: :heavy_check_mark: (Concluído)

### Tópicos

:small_blue_diamond: [Sobre o projeto](#Sobre-o-projeto-open_file_folder)

:small_blue_diamond: [Funcionalidades](#Funcionalidades)

:small_blue_diamond: [Modelo Conceitual](#Modelo-conceitual-page_with_curl)

:small_blue_diamond: [Layout](#Layout-mag_right)

:small_blue_diamond: [Como rodar a aplicação](#como-rodar-a-aplicação-arrow_forward)

# Sobre o projeto :open_file_folder:

O DS Catalog é um dos projetos que desenvolvi no curso da [DevSuperior](https://devsuperior.com.br/cursos) para atestar cada conhecimento obtido ao longo dos módulos dentre eles foram: CRUD, TDD, validação e segurança com personalização, Domínio e ORM, autorização e consultas ao banco de dados customizadas (JPA e JPQL). 

<p>
O sistema DS Catalog consiste em um site de catalagos de produtos com suas categorias, os quais podem ser listados, detalhados, inseridos, atualizados e deletados pelos usuários. Usuários podem ser operadores(OPERATOR) e administradores (ADMIN). Apenas
usuários administradores(ADMIN) podem inserir, listar, atualizar, deletar e filtrar usuários no sistema.
</p>

## Funcionalidades

:heavy_check_mark: Testa componentes da aplicação.

:heavy_check_mark: Listagem de usuários, produtos e categorias paginados.

:heavy_check_mark: Listagem de produtos e categorias paginados e ordenados por nome.

:heavy_check_mark: Filtra os usuários, produtos e categorias por id.

:heavy_check_mark: Obtém página detalhada do produto.

:heavy_check_mark: Autorização e autenticação.

:heavy_check_mark: Validação.

# Layout :mag_right:

- Login:

  ![Login](/backend/src/main/assets/login-dscatalog.png)

- CRUD categoria  

  ![Crud categoria](/backend/src/main/assets/crud-categoria-dscatalog.png)

- CRUD produto

  ![Crud produto](/backend/src/main/assets/crud-produto-dscatalog.png)

- CRUD usuário

  ![Crud usuarios](/backend/src/main/assets/crud-usuarios-dscatalog.png)

- Detalhe do produto  

  ![Informações detalhada do produto](/backend/src/main/assets/details-dscatalog.png)

- Endpoints:

![Gif dos endpoints](/backend/src/main/assets/dscatalog-endpoints.gif)

- Validações, autenticação e autorização:

![Gif das Validações, autenticação e autorização](/backend/src/main/assets/dscatalog-validacao-autenticacao.gif)

- Testes:

![Gif dos testes](/backend/src/main/assets/dscatalog-tests.gif)

# Modelo conceitual  :page_with_curl:
![Modelo Conceitual](/backend/src/main/assets/domain-model-dscatalog.png)

- Padrão em camadas

![Padrão camadas](/backend/src/main/assets/padrao-camadas.png)

## Linguagens, dependencias e libs utilizadas :books:
- [JAVA](https://www.java.com/pt-BR/)
- [JPA](https://spring.io/projects/spring-data-jpa) / [Hibernate](https://hibernate.org/)
- [Maven](https://maven.apache.org/)
- [H2](https://www.h2database.com/html/main.html)
- [JUnit](https://junit.org/junit5/)
- [Mockito](https://site.mockito.org/)
- [Jakarta Bean validation](https://beanvalidation.org)
- [JWT](https://jwt.io)
- [OAuth 2.0](https://oauth.net/2/)
- [Spring Security](https://docs.spring.io/spring-security/reference/index.html)
- [Spring Cloud](https://docs.spring.io/spring-cloud/docs/current/reference/html)
- [Spring Data JPA](https://docs.spring.io/spring-data/jpa/docs/current/reference/html)


## BD :floppy_disk:

### Usuários:

| first_name | last_name |  email   | password |
|------|-----------------|----------|----------|
| Ana  | ana@hotmail.com | alex@gmail.com | $2a$10$eACCYoNOHEqXve8aIWT8Nu3PkMXWBaOxJ9aORUYzfMQCbVBIhZ8tG
| Bob  | bob@hotmail.com | maria@gmail.com |$2a$10$eACCYoNOHEqXve8aIWT8Nu3PkMXWBaOxJ9aORUYzfMQCbVBIhZ8tG

### Categoria:

| name    | 
|---------|
| Livros |
| Eletrônicos|
| Computadores|

### Papel:

| authority |
|-----------|
| OPERATOR  |
| ADMIN     |

# Como rodar a aplicação :arrow_forward:

Pré-requisitos: Java 17
### Acessar o terminal / CLI
```bash
# clonar repositório
https://github.com/4lmeida/dscatalog.git
```
```bash
# entrar na pasta do projeto dscatalog
cd dscatalog
```
```bash
# executar o projeto
./mvnw spring-boot:run
```
# Autores

| [<img src="https://avatars.githubusercontent.com/u/93017964?v=4" width=115><br><sub>Luís Almeida</sub>](https://github.com/4lmeida) |
| :---: | 



## Licença

The [MIT License](/LICENSE)(MIT)

Copyright :copyright: 2023 - DS Catalog

:top: [Voltar para o top](#Tópicos)