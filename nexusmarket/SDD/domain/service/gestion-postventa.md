# Servicios de Gestión de Post-Venta

## Introducción

Este documento describe los procesos de negocio relacionados con lo que ocurre después de que un pedido ha sido entregado: las solicitudes de devolución de productos y los reembolsos de dinero derivados de ellas.

La especificación funcional original de NexusMarket no detalla los atributos ni la relación estructural exacta de las entidades **Devolucion** y **Reembolso** con Pedido; este documento, junto con el Modelo de Dominio, deja registrado como un diseño propuesto —a validar con el negocio— la forma en que ambas entidades se relacionan entre sí y con el Pedido original, a partir de la Solicitud de Devolución descrita en Gestión de Compras.

Todo el lenguaje utilizado aquí se enfoca en el negocio, evitando detalles técnicos de programación o bases de datos.

---

# 1. Resolución de Solicitud de Devolución

## Descripción

Este servicio permite que el Vendedor dueño del producto, o un Administrador, evalúe una solicitud de devolución previamente creada por un Comprador (ver "Solicitud de Devolución" en Gestión de Compras) y decida si la aprueba o la rechaza.

## Información Necesaria

* La Solicitud de Devolución a resolver.
* La decisión (Aprobar o Rechazar).
* La persona que resuelve la solicitud (Vendedor dueño del producto o Administrador).

## Reglas de Negocio (Validaciones)

1. **Autorización:** Solo el Vendedor que publicó el producto involucrado, o un Administrador, pueden resolver la solicitud.
2. **Solicitud Pendiente:** Únicamente se pueden resolver solicitudes que se encuentren en estado "Solicitada". Una solicitud ya aprobada o rechazada no puede volver a resolverse.
3. **Efecto de la Aprobación:** Si la solicitud se aprueba y el producto involucrado es físico, se dispara el proceso de Reposición de Inventario (ver Gestión de Inventario y Logística) para devolver las unidades a la bodega de origen.

## Guardado de Información

El estado de la solicitud se actualiza a "Aprobada" o "Rechazada", junto con el registro de quién tomó la decisión.

---

# 2. Procesamiento de Reembolso

## Descripción

Una vez que una solicitud de devolución ha sido aprobada, este servicio permite a un Administrador ejecutar el reembolso económico correspondiente al comprador.

## Información Necesaria

* La Solicitud de Devolución ya aprobada.
* El Administrador que procesa el reembolso.

## Reglas de Negocio (Validaciones)

1. **Autorización:** Únicamente un Administrador puede procesar reembolsos.
2. **Devolución Aprobada:** El reembolso solo puede procesarse sobre una solicitud de devolución que ya fue aprobada; no se pueden reembolsar devoluciones pendientes o rechazadas.
3. **Reembolso Único:** Una misma solicitud de devolución no puede generar más de un reembolso.

## Guardado de Información

Se crea un nuevo Reembolso en estado "Procesado", vinculado a la solicitud de devolución que le dio origen, y queda disponible como comprobante para el Comprador y el Administrador.
