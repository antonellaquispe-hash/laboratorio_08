# laboratorio-api

API REST de **Productos y Categorias** desarrollada con Spring Boot 3 para el laboratorio de
_Implementacion de Pruebas Unitarias e Integracion en APIs REST_.

## Que se hizo

- CRUD completo para **Productos** (`/api/productos`) y **Categorias** (`/api/categorias`):
  listar, buscar, crear, obtener por id, actualizar y eliminar.
- Consultas adicionales de Productos: buscar por nombre/categoria, bajo stock y rango de precio.
- DTOs con validacion via `@Valid` (`@NotBlank`, `@Positive`, `@Min`, `@Size`) y manejo
  centralizado de errores con `@RestControllerAdvice` (400 para validacion, 404 para recursos inexistentes).
- **Pruebas unitarias** de las capas `Service` con JUnit 5 + Mockito (mocks del repositorio,
  verificaciones con `verify()`).
- **Pruebas de integracion** de los endpoints REST con `@SpringBootTest`, `@AutoConfigureMockMvc`,
  `MockMvc` y `ObjectMapper`, validando respuestas JSON, codigos HTTP (200, 201, 400, 404),
  escenarios exitosos y de error. Usan el perfil `test` con H2 en memoria.

## Comandos

```bash
# Ejecutar todas las pruebas (usa el perfil test / H2)
./mvnw test

# Ejecutar la aplicacion
./mvnw spring-boot:run
```

## Estructura

```
src/main/java/com/tecsup
  controller/   Controladores REST
  dto/          Objetos de transferencia con validaciones
  exception/    Manejador global de errores
  model/        Entidades JPA
  repository/   Repositorios Spring Data JPA
  service/      Logica de negocio

src/test/java/com/tecsup
  service/           Pruebas unitarias (Mockito)
  controller/        Pruebas de integracion de controladores
  ProductoApiIntegrationTest / ProductoFeaturesIntegrationTest   Pruebas de integracion REST
```