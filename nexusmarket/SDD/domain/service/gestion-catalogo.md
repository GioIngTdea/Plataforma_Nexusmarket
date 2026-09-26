# Servicios de Gestión de Catálogo

## Introducción

Este documento describe los procesos de negocio (servicios) relacionados con la oferta de productos en el ecosistema de **NexusMarket**. Estos servicios validan la lógica de negocio antes de permitir que un vendedor ofrezca bienes físicos o digitales a los compradores.

Todo el lenguaje utilizado aquí se enfoca en el negocio, evitando detalles técnicos de programación o bases de datos.

---

# 1. Publicación de Producto Físico

## Descripción

Permite a un **Vendedor** agregar un nuevo producto físico al catálogo del marketplace. Los productos físicos son aquellos que requieren almacenamiento en una bodega y posteriormente un proceso de envío logístico al comprador.

## Información Necesaria

El servicio requiere:
* La información detallada del producto (nombre, descripción, precio, etc.).
* Las posibles variantes del producto (por ejemplo: diferentes tallas, colores o modelos).
* El Vendedor responsable de la publicación.

## Reglas de Negocio (Validaciones)

1. **Estado del Vendedor:** El sistema verifica que el vendedor que intenta publicar el producto tenga una cuenta "Activa" y habilitada para vender. Un vendedor suspendido no puede agregar nuevos productos.
2. **Definición de Variantes:** Todo producto físico debe tener al menos una variante (incluso si es genérica) para poder gestionar el inventario correctamente de esa versión específica del producto.
3. **Estado Inicial del Producto:** Al crearse, el producto queda en estado "Publicado" por defecto, haciéndolo visible en el catálogo de ventas.
4. **Vínculo con Inventario (Opcional en este paso):** Aunque el producto exista, no podrá ser vendido hasta que el vendedor o el administrador agreguen inventario (existencias) a las variantes correspondientes en una bodega válida. Esta validación pertenece al servicio de inventario.

## Guardado de Información

Una vez el producto es validado según las reglas, se almacena en la información de catálogo del sistema y queda asociado permanentemente a su Vendedor.

---

# 2. Publicación de Producto Digital

## Descripción

Permite a un **Vendedor** agregar un producto digital al catálogo. A diferencia de los productos físicos, estos productos son de entrega inmediata (por ejemplo, una tarjeta de regalo, un software o un documento) y no requieren despacho físico ni validación de inventario en bodega.

## Información Necesaria

* La información detallada del producto digital.
* Las variantes del producto, si aplica.
* El Vendedor responsable.

## Reglas de Negocio (Validaciones)

1. **Estado del Vendedor:** Al igual que con los físicos, el vendedor debe estar en un estado operativo "Activo".
2. **Naturaleza del Producto:** El sistema asegura que este producto se marque como "Digital", lo que eximirá a las futuras compras de los procesos logísticos de envío.
3. **Entrega del Bien:** El vendedor debe proporcionar o configurar la forma en que el producto digital será entregado al comprador una vez confirmado el pago (por ejemplo, un enlace de descarga o un código).

## Guardado de Información

Si la información es correcta, el producto digital se registra en el catálogo y queda disponible inmediatamente para la venta sin requerir existencias en bodega.

---

# 3. Actualización de Producto

## Descripción

Este proceso permite al vendedor o a un administrador modificar la información de un producto existente, sus variantes, o cambiar su estado de publicación.

## Información Necesaria

* El producto que se desea modificar.
* La nueva información o el nuevo estado deseado (Ej. Publicado, Suspendido, Descontinuado).

## Reglas de Negocio (Validaciones)

1. **Autorización de Modificación:** El sistema verifica que la persona intentando actualizar el producto sea el Vendedor dueño del mismo, o un Administrador con permisos superiores.
2. **Transición de Estado:** Si el vendedor decide "Descontinuar" un producto, el sistema valida que esta acción impida futuras ventas. Sin embargo, no debe afectar a los pedidos que ya fueron pagados y están en proceso de envío.
3. **Actualización de Precios:** Si el precio de un producto cambia, la regla de negocio dicta que este nuevo precio no afectará de forma retroactiva a los carritos de compra que ya confirmaron su pedido con el precio anterior.

## Guardado de Información

Una vez comprobada la propiedad del producto y las reglas de negocio, los cambios son actualizados y reflejados en el catálogo del sistema.

---

# 4. Ocultamiento de Productos de un Vendedor Suspendido

## Descripción

Este proceso interno se dispara automáticamente cuando la cuenta de un Vendedor es suspendida (ver "Cambio de Estado de Usuario" en Gestión de Usuarios). Su objetivo es retirar de la visibilidad del catálogo todos los productos que ese vendedor tiene publicados, sin eliminarlos ni afectar los pedidos que ya fueron pagados sobre ellos.

## Información Necesaria

* El Vendedor cuya cuenta fue suspendida.

## Reglas de Negocio (Validaciones)

1. **Alcance:** El proceso debe afectar únicamente a los productos actualmente publicados por ese vendedor específico; no debe tocar productos de otros vendedores.
2. **No Retroactividad:** Los pedidos que ya fueron pagados o despachados sobre esos productos no se ven afectados; el ocultamiento solo impide nuevas selecciones desde el catálogo hacia adelante.

## Guardado de Información

El estado de publicación de cada producto afectado cambia a "Suspendido" y se refleja de inmediato en el catálogo visible para los compradores.
