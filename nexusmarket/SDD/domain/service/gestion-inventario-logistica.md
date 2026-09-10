# Servicios de Gestión de Inventario y Logística

## Introducción

Este documento describe los procesos de negocio relacionados con el almacenamiento físico de los productos, el manejo de existencias y los pasos necesarios para llevar un producto físico desde la bodega hasta las manos del comprador.

Todo el lenguaje utilizado aquí se enfoca en el negocio, evitando detalles técnicos de programación o bases de datos.

---

# 1. Gestión de Bodegas

## Descripción

Permite la creación de un nuevo espacio físico de almacenamiento. Las bodegas pueden pertenecer directamente a NexusMarket (Bodegas del Marketplace) o ser administradas por un vendedor en particular.

## Información Necesaria

* La información de ubicación y capacidad de la bodega.
* El propietario de la bodega (El Marketplace o un Vendedor específico).
* El Administrador responsable de registrar la bodega.

## Reglas de Negocio (Validaciones)

1. **Autorización:** Únicamente un Administrador tiene la potestad de crear y registrar nuevas bodegas en el sistema. Ningún Vendedor u Operador Logístico puede crear una bodega por su cuenta.
2. **Propiedad Clara:** Toda bodega debe tener un propietario asignado desde su creación para saber a quién pertenecen los productos almacenados allí.

## Guardado de Información

La nueva bodega se registra en el sistema y queda lista para empezar a recibir productos físicos.

---

# 2. Actualización de Inventario

## Descripción

Este servicio controla las existencias reales (el stock) de los productos físicos almacenados en las bodegas. 

## Información Necesaria

* El Producto físico específico.
* La Bodega donde ocurrirá el movimiento.
* El tipo de movimiento (Ingreso de nueva mercancía, Salida por venta, Ajuste por pérdida, Devolución).
* La cantidad de unidades a mover.

## Reglas de Negocio (Validaciones)

1. **Restricción de Inventario Negativo:** La regla de oro del inventario es que ninguna bodega puede registrar una existencia negativa de un producto. Si se intenta registrar una salida (por ejemplo, por una venta) mayor a la cantidad disponible, el servicio debe bloquear la operación.
2. **Seguimiento de Movimientos:** Todo cambio en la cantidad de unidades debe quedar registrado especificando la razón del movimiento (ej. "Salida por venta confirmada").

## Guardado de Información

El servicio actualiza la cantidad total disponible del producto en la bodega y guarda el registro histórico del movimiento para futuras auditorías o revisiones por parte del Operador Logístico o el Vendedor.

---

# 3. Despacho de Pedido

## Descripción

Este proceso representa la fase logística donde un **Operador Logístico** toma un pedido que ya fue pagado, empaca los productos físicos correspondientes y coordina su envío hacia el comprador.

## Información Necesaria

* El Pedido que se va a despachar.
* El Operador Logístico que asume la responsabilidad del envío.

## Reglas de Negocio (Validaciones)

1. **Estado del Pedido:** Un pedido solo puede ser despachado si se encuentra en estado **"Pagado"**. El servicio bloquea cualquier intento de despachar pedidos "Pendientes de Pago" o que ya hayan sido entregados.
2. **Deducción Definitiva:** Al momento del despacho, los productos (que previamente habían sido reservados al confirmar el pedido) salen definitivamente de la bodega, registrándose el movimiento correspondiente en el inventario.
3. **Generación de Envío:** El sistema crea un registro formal de **Envío**, vinculando el pedido con el operador logístico a cargo.
4. **Cambio de Estado:** El pedido cambia formalmente de "Pagado" a **"Despachado"**.

## Guardado de Información

El nuevo estado del pedido y la creación del Envío se guardan en el sistema. Esto permite al Comprador rastrear su pedido y saber que ya está en camino a su dirección principal o adicional.

---

# 4. Finalización de Entrega

## Descripción

Este servicio marca el final del ciclo de vida de un pedido físico, cuando el comprador recibe satisfactoriamente sus productos.

## Información Necesaria

* El Envío o el Pedido despachado.
* La confirmación de recepción.

## Reglas de Negocio (Validaciones)

1. **Secuencia Correcta:** Solo un pedido en estado **"Despachado"** puede ser marcado como entregado.
2. **Cierre Comercial:** Al pasar al estado **"EntregadoFinalizado"**, el pedido se considera cerrado. A partir de este momento, ya no admite modificaciones. Cualquier problema posterior (ej. productos dañados) deberá manejarse a través de un servicio distinto como el de "Devolución" o "Reembolso", pero el pedido original permanece inalterado como registro histórico de la compra.

## Guardado de Información

El estado final del pedido se actualiza a "EntregadoFinalizado" de manera permanente.
