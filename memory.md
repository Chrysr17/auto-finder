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

## Próxima actualización
- Registrar la checklist de la fase 1.
- Marcar la primera mejora a implementar.
