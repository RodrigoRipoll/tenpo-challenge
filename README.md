# Application for TENPO - Backend Sr Software Engineer

Here I present my resolution to the Backend Challenge.

Sections:
- [Running the application](#run-the-app)
- [Testing](#Unit-test-and-E2E)
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
        -Important inside the docker compose you will find the `SPRING_PROFILES_ACTIVE` variable that if it takes 
         the value `dev` it will take the configuration of the `application-dev.yml` file, if it takes **any other value 
         it will take the default** configuration which is where we have the **"productive" values**.

          ```
              |                       	|       dev      	|      prod - default   	|
              |-----------------------	|:--------------:	|:---------------------:	|
              | rate limit            	|      15rpm     	|        3000rpm        	|
              | time to retry         	| 5s - 10s - 20s 	| 100ms - 200ms - 400ms 	|
              | cache in memory "tax" 	|       4m       	|           2h          	|
              | expiration tax        	|       2m       	|         30min         	|
              | file .yml		        |application-dev.yml	|     application.yml		|

    - The `port 8080` of **the TENPO API** is exposed to be able to interact with it through `localhost:8080`
    - The `port 8081` of **the Auxiliary Tax API** is exposed to be able to interact with it through `localhost:8081`
    - Redis exposes `ports - 6379:6379 - 8001:8001` Ports 6379 allow us to interact through **redis-cli**. While ports 8001 and 8002 allow us to access **RedisInsight** through `localhost:8001` to make use of a graphical interface (Not very useful in this case that a bucket is created in the cache, but it is accessible).
    - Postgresql can be access with this setup:
        - url: jdbc:postgresql://localhost:5432/tenpo-tracks
        - username: tenpo-user
        - password: tenpo-password

<br><br>
## Unit test and E2E

**Important!! TestContainers** are used for E2E testing, if Docker is not enabled and configured correctly and 
the tests may fail. Link to cases on [Mac M1](https://intellij-support.jetbrains.com/hc/en-us/community/posts/7763242546066-Could-not-find-a-valid-Docker-environment-issue).

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

Debe contener un servicio llamado por api-rest que reciba 2 n??meros, los sume, y le aplique una suba de un porcentaje que debe ser adquirido de un servicio externo (por ejemplo, si el servicio recibe 5 y 5 como valores, y el porcentaje devuelto por el servicio externo es 10, entonces (5 + 5) + 10% = 11). Se deben tener en cuenta las siguientes consideraciones:

El servicio externo puede ser un mock, tiene que devolver el % sumado.

Dado que ese % var??a poco, podemos considerar que el valor que devuelve ese servicio no va cambiar por 30 minutos.

Si el servicio externo falla, se debe devolver el ??ltimo valor retornado. Si no hay valor, debe retornar un error la api.

Si el servicio falla, se puede reintentar hasta 3 veces.

Historial de todos los llamados a todos los endpoint junto con la respuesta en caso de haber sido exitoso. Responder en Json, con data paginada. El guardado del historial de llamadas no debe sumar tiempo al servicio invocado, y en caso de falla, no debe impactar el llamado al servicio principal.

La api soporta recibir como m??ximo 3 rpm (request / minuto), en caso de superar ese umbral, debe retornar un error con el c??digo http y mensaje adecuado.

El historial se debe almacenar en una database PostgreSQL.

Incluir errores http. Mensajes y descripciones para la serie 4XX.


Se deben incluir tests unitarios.

Esta API debe ser desplegada en un docker container. Este docker puede estar en un dockerhub p??blico. La base de datos tambi??n debe correr en un contenedor docker. Recomendaci??n usar docker compose

Debes agregar un Postman Collection o Swagger para que probemos tu API

Tu c??digo debe estar disponible en un repositorio p??blico, junto con las instrucciones de c??mo desplegar el servicio y c??mo utilizarlo.

Tener en cuenta que la aplicaci??n funcionar?? de la forma de un sistema distribuido donde puede existir m??s de una r??plica del servicio funcionando en paralelo.
