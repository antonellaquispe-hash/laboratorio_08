# Laboratorio 08 - Implementación de Pruebas Unitarias e Integración en APIs REST con Spring Boot

API REST de **Productos** y **Categorías** construida con **Spring Boot 3**, en la que se implementaron
pruebas automatizadas (unitarias e integración) usando **JUnit 5, Mockito, MockMvc, Spring Boot Test
y ObjectMapper**.

## Descripción

El laboratorio consiste en una API REST que gestiona productos (`/api/productos`) y categorías
(`/api/categorias`), con validación de datos mediante DTOs y `@Valid`, manejo centralizado de errores
(`@RestControllerAdvice`) y consultas de negocio (búsqueda por nombre/categoría, bajo stock y rango
de precio). Sobre esta API se desarrolló una suite de pruebas que valida la lógica del `Service` de
forma aislada y el comportamiento completo de los endpoints REST, comprobando **códigos HTTP,
respuestas JSON, validaciones** y **escenarios de éxito y error**, sin desplegar un servidor real.

## Lo que incluye (según el documento del laboratorio)

- **Pruebas unitarias**: JUnit 5 + Mockito para evaluar los métodos del `Service` de manera aislada
  (`@Mock`, `@InjectMocks`, `MockitoAnnotations.openMocks`), con mocks del repositorio y
  verificaciones de interacción mediante `verify()`.
- **Pruebas de integración**: `@SpringBootTest` + `@AutoConfigureMockMvc` para probar controladores,
  repositorios y manejo de excepciones sobre endpoints REST con `MockMvc`, simulando peticiones HTTP
  sin levantar el servidor (perfil `test` con **H2 en memoria** para no depender de MySQL).
- **ObjectMapper**: serialización/deserialización de objetos JSON en las peticiones y respuestas,
  y verificación de los campos con `jsonPath`.
- **Códigos HTTP validados**: `201` (creación), `200` (lectura/actualización/eliminación),
  `400` (datos inválidos por `@Valid`) y `404` (recurso inexistente).
- **Cobertura**: escenarios exitosos y casos de error de toda la API (CRUD completo, búsquedas y validación).

## Endpoints

### Productos — `/api/productos`

| Método | Ruta | Descripción | Código esperado |
|--------|------|-------------|-----------------|
| GET | `/api/productos` | Listar todos | 200 |
| GET | `/api/productos/buscar?nombre=mon` | Buscar por nombre parcial | 200 |
| GET | `/api/productos/categoria/{categoria}` | Buscar por categoría | 200 |
| GET | `/api/productos/bajo-stock?minimo=5` | Productos con stock bajo | 200 |
| GET | `/api/productos/precio?min=10&max=100` | Buscar por rango de precio | 200 |
| GET | `/api/productos/{id}` | Obtener por id | 200 / 404 |
| POST | `/api/productos` | Crear producto | 201 / 400 |
| PUT | `/api/productos/{id}` | Actualizar producto | 200 / 404 / 400 |
| DELETE | `/api/productos/{id}` | Eliminar producto | 200 / 404 |

### Categorías — `/api/categorias`

| Método | Ruta | Descripción | Código esperado |
|--------|------|-------------|-----------------|
| GET | `/api/categorias` | Listar todas | 200 |
| GET | `/api/categorias/buscar?nombre=tec` | Buscar por nombre parcial | 200 |
| GET | `/api/categorias/{id}` | Obtener por id | 200 / 404 |
| POST | `/api/categorias` | Crear categoría | 201 / 400 |
| PUT | `/api/categorias/{id}` | Actualizar categoría | 200 / 404 / 400 |
| DELETE | `/api/categorias/{id}` | Eliminar categoría | 200 / 404 |

### Ejemplos de cuerpo (JSON, `Content-Type: application/json`)

Producto:

```json
{ "nombre": "Monitor", "precio": 50.5, "stock": 20, "categoria": "Tecnologia" }
```

Categoría:

```json
{ "nombre": "Tecnologia", "descripcion": "Productos electronicos" }
```

## Ejecución

```bash
# Ejecutar todas las pruebas (usa el perfil test / H2 en memoria)
./mvnw test

# Ejecutar la aplicación (perfil por defecto: MySQL en localhost:3306/lab05)
./mvnw spring-boot:run

# Ejecutar la aplicación sin depender de MySQL (H2 en memoria, puerto 8080)
./mvnw spring-boot:run -Dspring-boot.run.profiles=test -Dspring-boot.run.arguments=--server.port=8080
```

## Estructura del proyecto

```
src/main/java/com/tecsup
  controller/   Controladores REST
  dto/          DTOs con validaciones (@NotBlank, @Positive, @Min, @Size)
  exception/    GlobalExceptionHandler (@RestControllerAdvice) y ResourceNotFoundException
  model/        Entidades JPA (Producto, Categoria)
  repository/   Repositorios Spring Data JPA
  service/      Lógica de negocio

src/test/java/com/tecsup
  service/                      Pruebas unitarias del Service (Mockito)
  controller/                   Pruebas de integración de controladores (MockMvc)
  ProductoApiIntegrationTest    Pruebas de integración del CRUD REST
  ProductoFeaturesIntegrationTest  Pruebas de integración de las búsquedas
```

## Tecnologías

- Java 17, Spring Boot 3.5
- Spring Web, Spring Data JPA, Bean Validation
- JUnit 5, Mockito, MockMvc, Spring Boot Test, Hamcrest
- H2 (in-memory para pruebas), MySQL (producción)