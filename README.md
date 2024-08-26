Back-end Run The Bank!

## Índice

1. [Tecnologias utilizadas](#tecnologias-utilizadas)
2. [Como rodar o projeto](#como-rodar-o-projeto)
3. [Endpoints da API](#endpoints-da-api)
   - [Cliente](#cliente)
   - [Conta Bancária](#conta-bancária)
   - [Pagamento](#pagamento)
4. [Testes](#testes)
5. [Postman Collection](#postman-collection)

---

## Tecnologias utilizadas
- Java 17
- Spring Boot 3.3.2
- Spring Data JPA
- Banco de dados H2
- Maven
- Lombok
---

## Como rodar o projeto
- Clone o repositório: `git clone git@github.com:windson-dev/GFT-CHALLENGE.git`
- Instale as dependências: `mvn install`
- Rodar o projeto: `mvn spring-boot:run` ou `shift + f10` na IDEA Intellij

## Testes
- Comando para rodar os testes: `mvn test`


## Postman Collection

> | Collection |
> |-------------------|
> | [<img src="https://assets.getpostman.com/common-share/postman-logo.png" alt="Postman Collection" width="50px">](https://github.com/user-attachments/files/16741009/API.run.the.bank.postman_collection.json) |


## Endpoints da API

### Cliente

<details>
 <summary><code>POST</code> <code><b>/customer</b></code> <code>(Criar cliente)</code></summary>

##### Schema

> | name              |  document     | address      | password                         |
> |-------------------|-----------|----------------|-------------------------------------|
> | String(obrigatório) |  String(obrigatório) | String(obrigatório)   | String(obrigatório)        |


##### Exemplo de Requisição

> ```json
> {
>   "name": "Ash Ketchum",
>   "document": "65620911012",
>   "address": "Cidade de Pallet",
>   "password": "StrongPassword951!#@"
> }
> ```

##### Respostas

> | http code | content-type         | resposta                                            |
> |-----------|----------------------|-----------------------------------------------------|
> | `201`     | `application/json`   |         |
> | `400`     | `application/json`   | `{"message":"VALIDATION_ERROR","errors":["Mandatory field password","Invalid password was informed","Mandatory field name","Mandatory field document","Mandatory field address"]}`           |
> | `400`     | `application/json`   | `{"message":"VALIDATION_ERROR","errors":["Invalid document format or document does not exist"]}`           |
> | `400`     | `application/json`   | `{"message":"VALIDATION_ERROR", "errors"["Password must contain 1 or more uppercase characters.","Password must contain 1 or more lowercase characters.","Password must contain 1 or more digit characters.","Password must contain 1 or more special    characters.","Password must be 6 or more characters in length."]}`           |
> | `409`     | `application/json`   | `{"message": "Document already exists"}`           |


</details>

<details>
 <summary><code>GET</code> <code><b>/customer/:id</b></code> <code>(Buscar cliente por id)</code></summary>

##### Parameters

> | nome | tipo | tipo de dado | descrição                  |
> |------|------|-----------|------------------------------|
> | `id` | obrigatório | int     | ID do cliente                |

##### Respostas

> | http code | content-type         | resposta                                            |
> |-----------|----------------------|-----------------------------------------------------|
> | `200`     | `application/json`   | ` {"id": 1, "name": "Ash Ketchum","document": "60400404087","address": "Cidade de Pallet"}`            |
> | `404`     | `application/json`   | `{"message":"Customer 2 not found"}` |

</details>

<details>
 <summary><code>GET</code> <code><b>/customer</b></code> <code>(Buscar todos os clientes)</code></summary>

##### Respostas

> | http code | content-type         | resposta                                            |
> |-----------|----------------------|-----------------------------------------------------|
> | `200`     | `application/json`   | `[{ "id": 1, "name": "Ash Ketchum", ... }, ...]`   |

</details>

<details>
 <summary><code>GET</code> <code><b>/customer/paged?page=0&size=10</b></code> <code>(Buscar todos os clientes paginado e com filtro)</code></summary>

##### Parameters

> | name | type | tipo de dado | description                  |
> |------|------|-----------|------------------------------|
> | `page` | obrigatório | int   | Página de resultados (default é 0) |
> | `size` | obrigatório | int   | Número de resultados por página (default é 10) |
> | `name` | opcional | string   | Filtra a paginação por nome do cliente |
> | `document` | opcional | string   | Filtra a paginação por documento do cliente |
> | `address` | opcional | string   | Filtra a paginação por endereço do cliente |

##### Respostas

> | http code | content-type         | resposta                                            |
> |-----------|----------------------|-----------------------------------------------------|
> | `200`     | `application/json`   | `{"content":[{"id":1,"name":"Ash Ketchum","document":"65620911012","address":"Cidade de Pallet"}],"pageable":{"pageNumber":0,"pageSize":10,"sort":{"empty":false,"sorted":true,"unsorted":false},"offset":0,"paged":true,"unpaged":false},"last":true,"totalElements":1,"totalPages":1,"size":10,"number":0,"sort":{"empty":false,"sorted":true,"unsorted":false},"first":true,"numberOfElements":1,"empty":false}`   |

</details>

<details>
 <summary><code>PATCH</code> <code><b>/customer/:id</b></code> <code>(Atualização parcial de cliente)</code></summary>

##### Parameters

> | nome | tipo | tipo de dado | description                  |
> |------|------|-----------|------------------------------|
> | `id` | obrigatório | int     | ID do cliente                |

##### Body da Requisição

> ```json
> {
>   "name": "Ash Ketchum Smith",
>   "address": "Cidade de São Paulo"
>   "password": "@Minhasenha2024"
> }
> ```

##### Respostas

> | http code | content-type         | resposta                                            |
> |-----------|----------------------|-----------------------------------------------------|
> | `200`     | `application/json`   |     |
> | `400`     | `application/json`   | `{"message": "Customer 2 not found"}`           |
> | `400`     | `application/json`   | `{"message":"VALIDATION_ERROR","errors":["Password must be 6 or more characters in length.","Password must contain 1 or more lowercase characters.","Password must contain 1 or more digit characters.","Password must contain 1 or more special characters.","Password must contain 1 or more uppercase characters."]}`           |


</details>

<details>
 <summary><code>DELETE</code> <code><b>/customer/:id</b></code> <code>(Deletar cliente)</code></summary>

##### Parameters

> | nome | tipo | tipo de dado | descrição                  |
> |------|------|-----------|------------------------------|
> | `id` | obrigatório | int     | ID do cliente                |

##### Respostas

> | http code | content-type         | resposta                                            |
> |-----------|----------------------|-----------------------------------------------------|
> | `204`     | `application/json`   |      |
> | `404`     | `application/json`   | `{"message": "Customer 2 not found"}` |

</details>

### Conta Bancária

<details>
 <summary><code>POST</code> <code><b>/account-bank</b></code> <code>(Criar conta bancária para cliente)</code></summary>

 ##### Schema

> | agency              |  balance     | customerId     |
> |-------------------|-----------|----------------|
> | String(obrigatório) |  Double(obrigatório) | Int(obrigatório)   | 


##### Exemplo de requisição

> ```json
> {
>   "agency": "01",
>   "customerId": 1,
>   "balance": 1000.00
> }
> ```

##### Respostas

> | http code | content-type         | resposta                                            |
> |-----------|----------------------|-----------------------------------------------------|
> | `201`     | `application/json`   |  |
> | `400`     | `application/json`   | `{"message": "Customer not found"}`           |
> | `400`     | `application/json`   | `{"message":"VALIDATION_ERROR","errors":["Mandatory field agency","Mandatory field balance","Mandatory field customerId"]}`           |
> | `409`     | `application/json`   | `{"message":"Agency already exists"}`           |

</details>

<details>
 <summary><code>GET</code> <code><b>/account-bank/:id</b></code> <code>(Buscar conta bancária por id)</code></summary>

##### Parameters

> | nome | tipo | tipo de dado | description                  |
> |------|------|-----------|------------------------------|
> | `id` | obrigatório | int     | ID da conta bancária         |

##### Respostas

> | http code | content-type         | resposta                                            |
> |-----------|----------------------|-----------------------------------------------------|
> | `200`     | `application/json`   | `{"id":1,"agency":"01","balance":4000.0,"status":"ACTIVE","customer":{"id":1,"name":"Ash Ketchum","document":"47931727843","address":"Cidade de Pallet"}}`   |
> | `404`     | `application/json`   | `{"message": "Account Bank 10 not found"}` |

</details>

<details>
 <summary><code>GET</code> <code><b>/account-bank</b></code> <code>(Buscar todas as contas bancárias)</code></summary>

##### Respostas

> | http code | content-type         | resposta                                            |
> |-----------|----------------------|-----------------------------------------------------|
> | `200`     | `application/json`   | `[{ "id": 1, "customerId": 1, "balance": 1000.00 }, ...]` |

</details>

<details>
 <summary><code>GET</code> <code><b>/account-bank/paged?page=0&size=10</b></code> <code>(Buscar todas as contas bancárias paginado e com filtro)</code></summary>

##### Parameters

> | nome | tipo | tipo de dado | description                  |
> |------|------|-----------|------------------------------|
> | `page` | obrigatório | int   | Página de resultados (default é 0) |
> | `size` | obrigatório | int   | Número de resultados por página (default é 10) |
> | `status` | opcional | string   | Filtra contas bancárias por status |

##### Respostas

> | http code | content-type         | resposta                                            |
> |-----------|----------------------|-----------------------------------------------------|
> | `200`     | `application/json`   | `{"content":[{"id":1,"agency":"01","balance":4000.0,"status":"ACTIVE","customer":{"id":1,"name":"Ash Ketchum","document":"47931727843","address":"Cidade de Pallet"}}],"pageable":{"pageNumber":0,"pageSize":10,"sort":{"empty":false,"sorted":true,"unsorted":false},"offset":0,"paged":true,"unpaged":false},"last":true,"totalElements":1,"totalPages":1,"size":10,"number":0,"sort":{"empty":false,"sorted":true,"unsorted":false},"first":true,"numberOfElements":1,"empty":false}` |

</details>

<details>
 <summary><code>PATCH</code> <code><b>/account-bank/:id</b></code> <code>(Atualização parcial de conta bancária)</code></summary>

##### Parameters

> | nome | tipo | tipo de dado | description                  |
> |------|------|-----------|------------------------------|
> | `id` | obrigatório | int     | ID da conta bancária         |

##### Body da Requisição

> ```json
> {
>   "balance": 1200.00
> }
> ```

##### Respostas

> | http code | content-type         | resposta                                            |
> |-----------|----------------------|-----------------------------------------------------|
> | `200`     | `application/json`   |  |
> | `400`     | `application/json`   | `{"message": "Account Bank 10 not found"}`           |

</details>

<details>
 <summary><code>DELETE</code> <code><b>/account-bank/:id</b></code> <code>(Deletar conta bancária)</code></summary>

##### Parameters

> | nome | tipo | tipo de dado | description                  |
> |------|------|-----------|------------------------------|
> | `id` | obrigatório | int     | ID da conta bancária         |

##### Respostas

> | http code | content-type         | resposta                                            |
> |-----------|----------------------|-----------------------------------------------------|
> | `204`     | `application/json`   |  |
> | `404`     | `application/json`   | `{"message": "Account Bank 10 not found"}` |

</details>

<details>
 <summary><code>PATCH</code> <code><b>/account-bank/:id/status</b></code> <code>(Ativar conta bancária ou Desativar conta bancária)</code></summary>

##### Parameters

> | nome | tipo | tipo de dado | description                  |
> |------|------|-----------|------------------------------|
> | `id` | obrigatório | int     | ID da conta bancária         |

##### Respostas

> | http code | content-type         | resposta                                            |
> |-----------|----------------------|-----------------------------------------------------|
> | `200`     | `application/json`   |  |
> | `404`     | `application/json`   | `{"message": "Account Bank 10 not found"}` |

</details>

### Pagamento

<details>
 <summary><code>POST</code> <code><b>/payment</b></code> <code>(Criar pagamento entre contas)</code></summary>

##### Body da Requisição

> ```json
> {
>   "senderAccountId": 1,
>   "receiverAccountId": 2,
>   "amount": 100.00
> }
> ```

##### Respostas

> | http code | content-type         | resposta                                            |
> |-----------|----------------------|-----------------------------------------------------|
> | `201`     | `application/json`   |      |
> | `400`     | `application/json`   | `{"message":"VALIDATION_ERROR","errors":["Mandatory field receiverAccountId","Mandatory field senderAccountId","Mandatory field amount"]}`           |
> | `404`     | `application/json`   | `{"message": "Sender account not found"}`           |
> | `404`     | `application/json`   | `{"message": "Receiver account not found"}`           |


</details>

<details>
 <summary><code>GET</code> <code><b>/payment/:id</b></code> <code>(Buscar pagamento por id)</code></summary>

##### Parameters

> | nome | tipo | tipo de dado | description                  |
> |------|------|-----------|------------------------------|
> | `id` | obrigatório | int     | ID do pagamento              |

##### Respostas

> | http code | content-type         | resposta                                            |
> |-----------|----------------------|-----------------------------------------------------|
> | `200`     | `application/json`   | `{"amount":400.0,"senderAccount":{"id":1,"agency":"01","balance":3700.0,"status":"ACTIVE","customer":{"id":1,"name":"Ash Ketchum","document":"47931727843","address":"Cidade de Pallet"}},"receiverAccount":{"id":2,"agency":"010","balance":4800.0,"status":"ACTIVE","customer":{"id":1,"name":"Ash Ketchum","document":"47931727843","address":"Cidade de Pallet"}}}` |
> | `404`     | `application/json`   | `{"message": "Payment not found"}` |

</details>

<details>
 <summary><code>GET</code> <code><b>/payment</b></code> <code>(Buscar todos os pagamentos)</code></summary>

##### Respostas

> | http code | content-type         | resposta                                            |
> |-----------|----------------------|-----------------------------------------------------|
> | `200`     | `application/json`   | `[{ "id": 1, "senderAccountId": 1, "receiverAccountId": 2, "amount": 100.00 }, ...]` |

</details>

<details>
 <summary><code>GET</code> <code><b>/payment/paged?page=0&size=10</b></code> <code>(Buscar todos os pagamentos paginado e com filtro)</code></summary>

##### Parameters

> | nome | tipo | tipo de dado | description                  |
> |------|------|-----------|------------------------------|
> | `page` | obrigatório | int   | Página de resultados (default é 0) |
> | `size` | obrigatório | int   | Número de resultados por página (default é 10) |
> | `customerName` | opcional | string   | Número de resultados por página (default é 10) |

##### Respostas

> | http code | content-type         | resposta                                            |
> |-----------|----------------------|-----------------------------------------------------|
> | `200`     | `application/json`   | `{"content":[{"amount":400.0,"senderAccount":{"id":1,"agency":"01","balance":3700.0,"status":"ACTIVE","customer":{"id":1,"name":"Ash Ketchum","document":"47931727843","address":"Cidade de Pallet"}},"receiverAccount":{"id":2,"agency":"010","balance":4800.0,"status":"ACTIVE","customer":{"id":1,"name":"Ash Ketchum","document":"47931727843","address":"Cidade de Pallet"}}},{"amount":400.0,"senderAccount":{"id":1,"agency":"01","balance":3700.0,"status":"ACTIVE","customer":{"id":1,"name":"Ash Ketchum","document":"47931727843","address":"Cidade de Pallet"}},"receiverAccount":{"id":2,"agency":"010","balance":4800.0,"status":"ACTIVE","customer":{"id":1,"name":"Ash Ketchum","document":"47931727843","address":"Cidade de Pallet"}}}],"pageable":{"pageNumber":0,"pageSize":10,"sort":{"empty":false,"sorted":true,"unsorted":false},"offset":0,"paged":true,"unpaged":false},"last":true,"totalElements":2,"totalPages":1,"size":10,"number":0,"sort":{"empty":false,"sorted":true,"unsorted":false},"first":true,"numberOfElements":2,"empty":false}` |

</details>

---

# Propostas de melhoria na arquitetura:
- Adicionar authenticação JWT para endpoints que necessitam de mais segurança.
- Adicionar uma nova tabela e um novo serviço de Usuários com sistema de roles para definir quais tipos de usuários podem fazer determinados tipos de requisições.
