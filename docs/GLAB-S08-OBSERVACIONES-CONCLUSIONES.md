# GLAB-S08 - Implementacion de Tests

Alumno: Coello Palomino, Ricardo
Curso: Desarrollo de Aplicaciones Web 4 - C24 - Seccion A - B

## Observaciones

1. Los tests unitarios con Mockito no levantan el contexto de Spring; por ello se debe usar la anotacion `@ExtendWith(MockitoExtension.class)` o inicializar los mocks con `MockitoAnnotations.openMocks(this)` en un metodo `@BeforeEach`. Esto acelera mucho la ejecucion frente a las pruebas de integracion.

2. En las pruebas de integracion fue necesario usar `@AutoConfigureMockMvc` y el perfil `test` (H2 en memoria) para no depender de la conexion a MySQL del laboratorio, evitando fallos por disponibilidad de la base de datos.

3. Al usar `@SpringBootTest` sin `@Transactional`, los datos creados en un metodo de prueba persisten y pueden contaminar otros metodos; fue necesario usar `@Transactional` para que cada prueba haga rollback y sea independiente, o bien dar un identificador unico a los datos creados.

4. En las pruebas de `POST /api/productos` los datos se envian como JSON con `ObjectMapper`; cuidar que el formato de los numeros (`precio` con decimal) coincida con lo esperado en `jsonPath`, ya que Jackson serializa `double` como `80.0`.

5. La validacion con `@Valid` devuelve `MethodArgumentNotValidException`, que es capturada por `GlobalExceptionHandler` y transformada en un `400 Bad Request` con un mapa de errores por campo; las pruebas deben verificar el status y, de ser posible, el mensaje de cada campo.

## Conclusiones

1. Las pruebas unitarias permiten validar la logica de negocio de la capa `Service` de forma aislada y rapida, reemplazando el repositorio con mocks y verificando las interacciones con `verify()`.

2. Las pruebas de integracion con `MockMvc` validan el flujo completo (controller, service, repository y manejo de excepciones) sin necesidad de desplegar un servidor real, cubriendo endpoints HTTP y respuestas JSON.

3. La combinacion de pruebas unitarias e integracion da mayor confianza y cobertura: las unitarias detectan errores de logica y las de integracion detectan errores de configuracion, validacion y contrato del API.

4. El desarrollo del modulo de `Categoria` reutilizo el mismo patron del modulo de `Producto` (Entity, Repository, Service, Controller, DTO, validaciones y manejo de excepciones), demostrando una arquitectura en capas consistente y facil de extender.

5. El manejo centralizado de excepciones con `@RestControllerAdvice` y una excepcion personalizada `ResourceNotFoundException` permite respuestas de error uniformes (`404`) y limpia los controladores de logica de manejo de errores.
