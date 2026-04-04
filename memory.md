# Memory

## Objetivo
Este archivo funciona como memoria operativa del proyecto. Se usará para registrar el contexto de trabajo y evitar perder decisiones entre sesiones.

## Cómo usarlo
- Registrar aquí fases, checklist, mejoras acordadas y cambios implementados.
- Anotar decisiones técnicas relevantes y su motivo.
- Dejar pendientes concretos y próximos pasos.
- Actualizar este archivo cada vez que se cierre una tarea importante.

## Formato sugerido
### Fecha
- Contexto:
- Cambio realizado:
- Archivos tocados:
- Decisiones:
- Pendientes:
- Siguiente paso:

## Estado actual
- Se creó `AGENTS.md` como guía de contribución del repositorio.
- Este archivo queda preparado para documentar el desarrollo de las siguientes fases y mejoras.
- Se inspeccionó la arquitectura actual del backend:
- `auth-service`: login, registro, JWT y usuarios.
- `auto-service`: catálogo base de autos, marcas, modelos, categorías e imágenes.
- `comparador-service`: compara autos consultando `auto-service` por Feign.
- `favorito-service`: guarda favoritos por usuario y consulta detalle en `auto-service`.
- `gateway-service`: enruta servicios y valida JWT en el borde.

## Hallazgos de arquitectura
- El sistema ya cubre autenticación, catálogo, comparación y favoritos.
- La lógica actual está orientada a CRUD y comparación simple, todavía no a discovery avanzado tipo Kimovil.
- `auto-service` tiene consultas básicas por marca y categoría, pero no búsqueda avanzada, paginación ni ordenamiento.
- `comparador-service` compara por criterio simple (`general`, `precio`, `anio`, `marca`), sin matriz rica de atributos.
- `favorito-service` devuelve favoritos y detalle, pero no métricas de uso ni recomendaciones.
- La seguridad JWT está replicada en varios servicios.

## Mejoras candidatas priorizadas
- Fase 1: búsqueda avanzada de autos con filtros, paginación y ordenamiento.
- Fase 1: validaciones consistentes y manejo global de errores en todos los microservicios.
- Fase 1: endurecer autorizaciones por rol en endpoints de escritura.
- Fase 1: enriquecer el dominio de autos con más atributos comparables.
- Fase 2: comparación avanzada con diferencias destacadas y ranking por criterio.
- Fase 2: favoritos enriquecidos con listas, notas y señales para recomendaciones.
- Fase 3: observabilidad, trazabilidad, rate limiting y resiliencia entre servicios.

## Avance Fase 1
### 2026-04-02
- Contexto: inicio de implementación de búsqueda avanzada en `auto-service`.
- Cambio realizado:
- Se agregó endpoint `GET /api/autos/buscar`.
- Se agregaron filtros por `marcaId`, `modeloId`, `categoriaId`, rangos de precio, rangos de año y `color`.
- Se agregó soporte de paginación (`page`, `size`) y ordenamiento (`sortBy`, `direction`).
- Se implementó búsqueda dinámica con `JpaSpecificationExecutor` y `AutoSpecification`.
- Archivos tocados:
- `auto-service/src/main/java/com/example/autoservice/controller/AutoController.java`
- `auto-service/src/main/java/com/example/autoservice/service/AutoService.java`
- `auto-service/src/main/java/com/example/autoservice/service/impl/AutoServiceImpl.java`
- `auto-service/src/main/java/com/example/autoservice/repository/AutoRepositoy.java`
- `auto-service/src/main/java/com/example/autoservice/repository/AutoSpecification.java`
- `auto-service/src/main/java/com/example/autoservice/dto/AutoFiltroRequestDTO.java`
- `auto-service/src/main/java/com/example/autoservice/dto/AutoBusquedaResponseDTO.java`
- Verificación:
- `mvn -q -DskipTests compile` pasó correctamente en `auto-service`.
- `mvn -q test` no es confiable en el estado actual del módulo porque el `SpringBootTest` existente falla por datasource no configurado.
- Pendientes:
- Validar reglas de negocio de filtros inválidos.
- Estandarizar respuesta de error para búsqueda y CRUD.
- Agregar tests unitarios de búsqueda avanzada.

### 2026-04-02 - incremento 2
- Contexto: validación de filtros inválidos para la búsqueda avanzada en `auto-service`.
- Cambio realizado:
- Se agregó `InvalidSearchFilterException`.
- Se agregó `GlobalExceptionHandler` para responder `400 Bad Request` con payload consistente cuando los filtros de búsqueda son inválidos.
- Se validan estos casos en la búsqueda:
- `precioMin > precioMax`
- `anioMin > anioMax`
- `page < 0`
- `size <= 0`
- `size > 100`
- `sortBy` fuera de `precio`, `anioFabricacion`, `color`, `marca`
- `direction` fuera de `asc`, `desc`
- Archivos tocados:
- `auto-service/src/main/java/com/example/autoservice/service/impl/AutoServiceImpl.java`
- `auto-service/src/main/java/com/example/autoservice/exception/InvalidSearchFilterException.java`
- `auto-service/src/main/java/com/example/autoservice/exception/GlobalExceptionHandler.java`
- Verificación:
- `mvn -q -DskipTests compile` pasó correctamente en `auto-service`.

### 2026-04-03 - incremento 3
- Contexto: ajuste gradual de seguridad para alinear el producto con un discovery público tipo Kimovil.
- Cambio realizado:
- Se abrió el acceso público al catálogo en el gateway para rutas de lectura de `autos`, `marcas`, `modelos` y `categorias`.
- Se corrigió el filtro JWT del gateway para no exigir token en rutas públicas y seguir autenticando cuando el token sí viene presente y es válido.
- Se abrió el acceso `GET` público en `auto-service` para catálogo, detalle, búsqueda, marcas, modelos y categorías.
- Se mantuvo la escritura del catálogo restringida a `ADMIN`.
- Archivos tocados:
- `gateway-service/src/main/java/com/example/gatewayservice/security/SecurityConfig.java`
- `gateway-service/src/main/java/com/example/gatewayservice/security/JwtAuthWebFilter.java`
- `auto-service/src/main/java/com/example/autoservice/security/SecurityConfig.java`
- Verificación:
- `mvn -q -DskipTests compile` pasó correctamente en `gateway-service`.
- `mvn -q -DskipTests compile` pasó correctamente en `auto-service`.
- Decisiones:
- El catálogo debe ser público para favorecer exploración y búsqueda inicial.
- Favoritos se mantiene autenticado.
- La comparación avanzada seguirá evaluándose por separado en el siguiente incremento.

### 2026-04-03 - incremento 4
- Contexto: apertura gradual del comparador actual como parte del discovery público del producto.
- Cambio realizado:
- Se abrió el acceso público al endpoint actual de comparación en el gateway.
- Se abrió el acceso público a `/api/comparar/**` en `comparador-service`.
- Se mantiene la idea de separar más adelante una comparación avanzada autenticada si el servicio crece en personalización o funciones exclusivas.
- Archivos tocados:
- `gateway-service/src/main/java/com/example/gatewayservice/security/SecurityConfig.java`
- `gateway-service/src/main/java/com/example/gatewayservice/security/JwtAuthWebFilter.java`
- `comparador-service/src/main/java/com/example/comparador_service/security/SecurityConfig.java`
- Verificación:
- `mvn -q -DskipTests compile` pasó correctamente en `gateway-service`.
- `mvn -q -DskipTests compile` pasó correctamente en `comparador-service`.
- Decisiones:
- El comparador actual se considera comparación simple y por tanto parte del flujo público de exploración.
- Favoritos continúa como funcionalidad autenticada.

## Próxima actualización
- Retomar los pendientes de Fase 1 en `auto-service`: tests de búsqueda avanzada y estandarización de errores CRUD.
