# API de talleres culturales

API REST hecha con Spring Boot para gestionar los talleres de un centro cultural: los **organizadores** que los imparten, los **talleres** y los **participantes** que se inscriben en ellos.

Proyecto individual de los módulos de **Acceso a Datos** y **Programación de Servicios y Procesos** de 2º DAM (Salesianos Triana).

## Tecnologías

- Java 21
- Spring Boot 4.1.1 (Web MVC, Data JPA y Validation)
- Base de datos H2 en memoria
- Lombok
- springdoc-openapi 3.1.1 (Swagger UI)
- Maven (con el wrapper `mvnw`, no hace falta tenerlo instalado)

## Cómo arrancarlo

Solo hace falta tener instalado **Java 21**.

```bash
git clone https://github.com/ignacio-salesianos/proyecto-talleres-api.git
cd proyecto-talleres-api
./mvnw spring-boot:run
```

En Windows, en lugar de `./mvnw` se usa `mvnw.cmd spring-boot:run`. También se puede abrir con IntelliJ y ejecutar la clase `ProyectoTalleresApiApplication`.

La API arranca en `http://localhost:8080`.

| Qué | Dirección |
|---|---|
| Swagger UI | http://localhost:8080/swagger-ui.html |
| Documentación OpenAPI (JSON) | http://localhost:8080/v3/api-docs |
| Consola de H2 | http://localhost:8080/h2-console |

Para entrar en la consola de H2: JDBC URL `jdbc:h2:mem:talleresdb`, usuario `sa` y contraseña vacía.

### Datos de ejemplo

Al arrancar, la clase `DataSeed` carga unos datos para poder probar la API desde el principio:

- 3 organizadores: Lucía Fernández, Marcos Romero y Elena Castillo (esta última sin talleres).
- 4 participantes.
- 3 talleres: dos de Lucía y uno de Marcos, con algunos participantes ya inscritos.

Como la base de datos está en memoria, cada vez que se reinicia la aplicación se vuelve a estos datos.

## Modelo de datos

| Entidad | Atributos |
|---|---|
| `Organizador` | `id`, `nombre`, `email` |
| `Taller` | `id`, `titulo` (único), `fechaHoraInicio`, `fechaHoraFin`, `organizador`, `participantes` |
| `Participante` | `id`, `nombreCompleto` |

Relaciones:

- **Taller → Organizador (N:1)**: cada taller tiene un organizador obligatorio y un organizador puede tener varios talleres. Se guarda en la columna `organizador_id` de la tabla `taller`.
- **Taller ↔ Participante (N:M)**: un taller tiene varios participantes y un participante puede estar inscrito en varios talleres. Se guarda en la tabla intermedia `inscripcion` (`taller_id`, `participante_id`).

Las dos relaciones son **unidireccionales** y solo se recorren desde `Taller`: ni `Organizador` ni `Participante` tienen una lista de talleres. Así se evitan bucles al convertir a JSON y es la forma de trabajar que hemos visto en clase.

## Endpoints

Todas las rutas empiezan por `/api/v1`.

### Organizadores

| Método | Ruta | Qué hace | Respuestas |
|---|---|---|---|
| GET | `/api/v1/organizadores` | Lista todos los organizadores | 200 |
| GET | `/api/v1/organizadores/{id}` | Obtiene un organizador | 200, 404 |
| POST | `/api/v1/organizadores` | Crea un organizador | 201, 400 |
| PUT | `/api/v1/organizadores/{id}` | Edita un organizador | 200, 400, 404 |
| DELETE | `/api/v1/organizadores/{id}` | Borra un organizador | 204, 404, 409 |

### Talleres

| Método | Ruta | Qué hace | Respuestas |
|---|---|---|---|
| GET | `/api/v1/talleres` | Lista todos los talleres con su organizador y sus participantes | 200 |
| GET | `/api/v1/talleres/{id}` | Obtiene un taller | 200, 404 |
| POST | `/api/v1/talleres` | Crea un taller | 201, 400, 404, 409 |
| PUT | `/api/v1/talleres/{id}` | Edita un taller | 200, 400, 404, 409 |
| DELETE | `/api/v1/talleres/{id}` | Borra un taller y sus inscripciones | 204, 404 |

### Participantes

| Método | Ruta | Qué hace | Respuestas |
|---|---|---|---|
| GET | `/api/v1/participantes` | Lista todos los participantes | 200 |
| POST | `/api/v1/participantes` | Da de alta un participante | 201, 400 |

### Inscripciones

| Método | Ruta | Qué hace | Respuestas |
|---|---|---|---|
| POST | `/api/v1/talleres/{tallerId}/participantes/{participanteId}` | Inscribe un participante en un taller | 201, 404, 409 |
| GET | `/api/v1/talleres/{tallerId}/participantes` | Lista los inscritos de un taller | 200, 404 |
| DELETE | `/api/v1/talleres/{tallerId}/participantes/{participanteId}` | Desinscribe un participante de un taller | 204, 404 |

### Ejemplo de cuerpo para crear un taller

```json
{
  "titulo": "Iniciación a la acuarela",
  "fechaHoraInicio": "2026-11-12T17:00:00",
  "fechaHoraFin": "2026-11-12T19:30:00",
  "organizadorId": 3
}
```

## Errores

Todos los errores se devuelven con el formato estándar **ProblemDetail** (`application/problem+json`), gestionados en `GlobalExceptionHandler`.

| Código | Cuándo | `type` |
|---|---|---|
| 400 | Los datos enviados no son válidos (campos vacíos, email mal escrito, fecha de fin no posterior a la de inicio...) | `https://talleres-api.com/errors/validation` |
| 404 | No existe el organizador, el taller o el participante indicado | `https://talleres-api.com/errors/not-found` |
| 409 | Ya existe un taller con ese título | `https://talleres-api.com/errors/taller-ya-existe` |
| 409 | El participante ya está inscrito en ese taller | `https://talleres-api.com/errors/participante-ya-inscrito` |
| 409 | Se intenta borrar un organizador que tiene talleres | `https://talleres-api.com/errors/organizador-con-talleres` |

En los errores de validación (400) se añade la lista `invalid-params` con cada campo que ha fallado:

```json
{
  "type": "https://talleres-api.com/errors/validation",
  "title": "Datos no válidos",
  "status": 400,
  "detail": "Error de validación",
  "instance": "/api/v1/talleres",
  "invalid-params": [
    {
      "object": "tallerRequestDTO",
      "message": "El título del taller es obligatorio",
      "field": "titulo",
      "rejectedValue": ""
    },
    {
      "object": "tallerRequestDTO",
      "message": "La fecha y hora de fin debe ser posterior a la de inicio"
    }
  ]
}
```

Los mensajes de validación están en `src/main/resources/messages/errors.properties`.

## Decisiones tomadas

- **400 frente a 409.** Se devuelve 400 cuando la petición está mal por sí sola (le falta un dato, tiene un formato incorrecto o las fechas no tienen sentido). Se devuelve 409 cuando la petición es correcta pero choca con el estado actual de los datos: un título que ya usa otro taller, un participante que ya estaba inscrito o un organizador que todavía tiene talleres.
- **Borrar un organizador con talleres da 409.** Cada taller tiene que tener siempre un organizador, así que no se borra en cascada: antes hay que borrar sus talleres o asignarlos a otro organizador.
- **Borrar un taller** borra también sus inscripciones, pero los participantes se conservan.
- **Título único** sin tener en cuenta mayúsculas ni los espacios del principio y del final ("Escritura creativa" y "  ESCRITURA creativa " se consideran el mismo título). Al editar un taller se puede mantener su propio título.
- **Fecha de fin posterior a la de inicio** con una validación propia, `@FechaFinPosterior`, que se pone a nivel de clase en `TallerRequestDTO` porque necesita comparar dos campos.
- **Desinscribir a alguien que no estaba inscrito** no da error: devuelve 204 y no cambia nada. Sí da 404 si no existe el taller o el participante.
- **Listas vacías en vez de 404.** Los GET que devuelven listas responden `[]` cuando no hay datos. El enunciado pide lista vacía para los inscritos de un taller y, por coherencia, se hace igual en el resto de listados.
- **Rutas con `/api/v1`** en todos los endpoints, también en las inscripciones, por si en el futuro hubiera una segunda versión de la API.
- **H2 en memoria** para que el proyecto se pueda arrancar sin instalar ninguna base de datos.
- **DTOs como `record`**: los `RequestDTO` llevan las validaciones y un método `to()` que crea la entidad, y los `ResponseDTO`/`SimpleDTO` tienen un método `of()` que se construye a partir de la entidad. Así nunca se devuelven entidades directamente.

## Documentación con Swagger

Con la aplicación arrancada, la documentación está en **http://localhost:8080/swagger-ui.html**. Cada endpoint tiene su descripción, sus posibles respuestas y ejemplos de los errores, y se puede probar desde ahí con *Try it out*.

## Colección de Postman

En la carpeta `postman/` está la colección `talleres-api.postman_collection.json` con 37 peticiones, organizadas en cuatro carpetas: organizadores, talleres, participantes e inscripciones. Incluye casos buenos y casos de error, y cada petición lleva en el nombre el código que debe devolver.

Para usarla:

1. Arrancar la aplicación.
2. En Postman, pulsar **Import** y elegir el archivo `postman/talleres-api.postman_collection.json`.
3. Ejecutar las peticiones en orden, o toda la colección a la vez con **Run collection**. Algunas peticiones usan los datos creados por las anteriores, así que conviene lanzarla justo después de arrancar la aplicación.

La URL de la API está en la variable de colección `baseUrl` (`http://localhost:8080/api/v1`).

## Estructura del proyecto

```
src/main/java/com/salesianostriana/dam/talleres
├── config        ValidationConfig (mensajes de errors.properties)
├── controller    Controladores REST documentados con OpenAPI
├── dto           DTOs de entrada y salida (records)
├── error         GlobalExceptionHandler y excepciones propias
│   └── notfound  Excepciones 404
├── model         Entidades JPA
├── repository    Repositorios de Spring Data JPA
├── service       Lógica de negocio
├── utils         DataSeed con los datos de ejemplo
└── validation    Validación personalizada @FechaFinPosterior
```

## Autor

Ignacio Pérez · 2º DAM · Salesianos Triana
