# Servicios de Gestión de Compras

## Introducción

Este documento describe el corazón del negocio de **NexusMarket**: el proceso mediante el cual los usuarios seleccionan productos y los adquieren formalmente. Abarca desde la gestión del carrito de compras hasta la confirmación de pedidos y su posterior facturación.

Todo el lenguaje utilizado aquí se enfoca en el negocio, evitando detalles técnicos de programación o bases de datos.

---

# 1. Gestión del Carrito de Compras

## Descripción

Este servicio permite a un **Comprador** organizar los productos que desea adquirir de forma provisional, antes de tomar la decisión de confirmarlos.

## Información Necesaria

* El Comprador dueño del carrito.
* El o los Productos que se desean agregar o retirar del carrito.
* Las cantidades seleccionadas para cada producto.

## Reglas de Negocio (Validaciones)

1. **Estado del Producto:** El sistema debe verificar que los productos que se intentan agregar estén en estado "Publicado". Si un producto está suspendido o descontinuado, no se puede añadir al carrito.
2. **Reserva de Inventario (Opcional en este paso):** Agregar un producto al carrito de compras **no** descuenta inmediatamente las unidades de la bodega (el inventario no se aparta hasta que se confirma la compra). Esto significa que un producto podría agotarse mientras está en el carrito de un usuario.
3. **Propiedad del Carrito:** Solo el Comprador dueño de la cuenta puede ver y gestionar el contenido de su propio carrito de compras.

## Guardado de Información

Los productos seleccionados se guardan de forma temporal en el carrito asociado a la cuenta del comprador, permitiéndole continuar su navegación o cerrar la sesión sin perder su selección.

---

# 2. Confirmación de Pedido

## Descripción

Este es el proceso crítico donde la selección provisional (Carrito de Compras) se convierte en un compromiso comercial formal (Pedido). Una vez que un pedido es creado, sus detalles (precios, productos, cantidades) quedan bloqueados y no pueden ser modificados.

## Información Necesaria

* El Carrito de Compras actual del comprador.
* La dirección de entrega seleccionada por el comprador.
* El método de pago elegido.

## Reglas de Negocio (Validaciones)

1. **Validación de Inventario (Crucial):** El sistema debe consultar de inmediato el inventario real de todos los productos físicos del carrito. Si no hay existencias suficientes para uno de los productos, la confirmación del pedido se detiene y se alerta al comprador.
2. **Reserva de Productos:** Si hay inventario suficiente, el sistema debe apartar (reservar) de forma inmediata esas unidades en la bodega correspondiente, para evitar que otro comprador adquiera el mismo producto.
3. **Generación del Compromiso:** Se crea un nuevo Pedido con el estado inicial **"PendienteDePago"**. Los precios y promociones aplicadas en este instante se congelan. Si el precio del producto cambia al día siguiente, el pedido mantendrá el precio original pactado al momento de confirmar.

## Guardado de Información

El carrito de compras se vacía. El nuevo Pedido se guarda en el historial de compras del usuario y en los registros del vendedor, quedando a la espera del pago. Las reservas de inventario se registran formalmente.

---

# 3. Validación de Pago y Facturación

## Descripción

Este servicio reacciona una vez que el comprador realiza el pago de un pedido pendiente. Transforma el estado del pedido a pagado y detona las acciones subsecuentes, como la facturación y la logística.

## Información Necesaria

* El Pedido que se encontraba en estado "PendienteDePago".
* La confirmación externa de que el pago fue exitoso (proveniente de la pasarela de pagos).

## Reglas de Negocio (Validaciones)

1. **Transición de Estado:** El pedido cambia obligatoriamente de "PendienteDePago" a **"Pagado"**.
2. **Emisión de Factura:** Como regla comercial, una vez que el pago es exitoso, el sistema debe emitir la **Factura** correspondiente, asociándola a este pedido de forma permanente.
3. **Detonación Logística (Si aplica):** Si el pedido contiene **Productos Físicos**, el sistema alerta a la bodega y al operador logístico para iniciar el proceso de despacho.
4. **Entrega Inmediata (Si aplica):** Si el pedido contiene **Productos Digitales**, el sistema debe proveer inmediatamente el acceso o la descarga al comprador, finalizando el flujo para esos productos específicos.

## Guardado de Información

El cambio de estado del pedido se guarda permanentemente. La Factura generada se almacena y se pone a disposición tanto del Comprador como del Vendedor.
