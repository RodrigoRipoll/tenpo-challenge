# Application for TENPO - Backend Sr Software Engineer

Here I present my resolution to the Backend Challenge.

Sections:
- [Running the application](#run-the-app)
- [OpenAPI Specification and Postman](#openapi-specification-and-postman)
- [Challenge Specifications - e-mail](#challenge-specifications)

## Run the App

In the root folder we have a docker-compose:
- `docker-compose.yml`
    - it is used to test **TENPO APP**
    - makes use of **Redis as Distributed Cache**
    - makes use of **Postgresql for data persistence**
    - makes use of a Image in Docker-hub to start **the Auxiliary Tax API**
        - image: ripollrodrigo/tenpo_tax_percentage:lastest
    - **TENPO APP** is built from the `Dockerfile`
    - **run command**: `docker-compose up` or `docker-compose up --build`
      -Important inside docker-compose we can find `SPRING_PROFILES_ACTIVE=dev`, this can take 2 values **dev** or **prod**
      each one provides us with different settings in the general environment variables, such as CB, cache, expiration times.
      It is set as **prod** but you can change and build again, including changing the parameters in the `application-dev.yml`
      or `application.yml` files.
        ```
            |                       	|       dev      	|          prod         	|
            |-----------------------	|:--------------:	|:---------------------:	|
            | rate limit            	|      15rpm     	|        3000rpm        	|
            | time to retry         	| 5s - 10s - 20s 	| 100ms - 200ms - 400ms 	|
            | cache in memory "tax" 	|       4m       	|           2h          	|
            | expiration tax        	|       2m       	|         30min         	|
            | file .yml             	|application-dev.yml|     application.yml     	|

    - The `port 8080` of **the TENPO API** is exposed to be able to interact with it through `localhost:8080`
    - The `port 8081` of **the Auxiliary Tax API** is exposed to be able to interact with it through `localhost:8081`
    - Redis exposes `ports - 6379:6379 - 8001:8001` Ports 6379 allow us to interact through **redis-cli**. While ports 8001 and 8002 allow us to access **RedisInsight** through `localhost:8001` to make use of a graphical interface (Not very useful in this case that a bucket is created in the cache, but it is accessible).
    - Postgresql can be access with this setup:
        - url: jdbc:postgresql://localhost:5432/tenpo-tracks
        - username: tenpo-user
        - password: tenpo-password

<br><br>
## OpenAPI Specification and Postman

In the path `/src/main/resources` a `swagger.yaml` can be found with the contracts stipulated for the API.

Right there we have a folder with **Postman collections**, we are going to find 2 files.json, which when imported into Postman will see the collections:
- **TENPO API**: collection for the main challenge api.
- **TENPO Auxiliary Tax API**: collection for an api (which, although it could be mocked, is not in this case) so that they can check if they like the generation of %. And to be able to easily check that the main api cache works.

<br><br>
## Challenge Specifications

Los requerimientos son los siguientes:

Debes desarrollar una API REST en Spring Boot utilizando java 11 o superior, con las siguientes funcionalidades:

Debe contener un servicio llamado por api-rest que reciba 2 números, los sume, y le aplique una suba de un porcentaje que debe ser adquirido de un servicio externo (por ejemplo, si el servicio recibe 5 y 5 como valores, y el porcentaje devuelto por el servicio externo es 10, entonces (5 + 5) + 10% = 11). Se deben tener en cuenta las siguientes consideraciones:

El servicio externo puede ser un mock, tiene que devolver el % sumado.

Dado que ese % varía poco, podemos considerar que el valor que devuelve ese servicio no va cambiar por 30 minutos.

Si el servicio externo falla, se debe devolver el último valor retornado. Si no hay valor, debe retornar un error la api.

Si el servicio falla, se puede reintentar hasta 3 veces.

Historial de todos los llamados a todos los endpoint junto con la respuesta en caso de haber sido exitoso. Responder en Json, con data paginada. El guardado del historial de llamadas no debe sumar tiempo al servicio invocado, y en caso de falla, no debe impactar el llamado al servicio principal.

La api soporta recibir como máximo 3 rpm (request / minuto), en caso de superar ese umbral, debe retornar un error con el código http y mensaje adecuado.

El historial se debe almacenar en una database PostgreSQL.

Incluir errores http. Mensajes y descripciones para la serie 4XX.


Se deben incluir tests unitarios.

Esta API debe ser desplegada en un docker container. Este docker puede estar en un dockerhub público. La base de datos también debe correr en un contenedor docker. Recomendación usar docker compose

Debes agregar un Postman Collection o Swagger para que probemos tu API

Tu código debe estar disponible en un repositorio público, junto con las instrucciones de cómo desplegar el servicio y cómo utilizarlo.

Tener en cuenta que la aplicación funcionará de la forma de un sistema distribuido donde puede existir más de una réplica del servicio funcionando en paralelo.
