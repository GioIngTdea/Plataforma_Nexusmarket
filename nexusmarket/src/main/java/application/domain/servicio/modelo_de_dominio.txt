# Domain Model

## Introducción

El Modelo de Dominio representa las entidades de negocio centrales del sistema NexusMarket. Estas entidades encapsulan las reglas de negocio, los datos y las relaciones descritas en la especificación funcional del proyecto.

El modelo sigue los principios del Diseño Orientado a Objetos y aplica herencia para eliminar información duplicada, favoreciendo la reutilización y el mantenimiento del sistema. Además de la herencia, se documentan las asociaciones entre entidades como atributos tipados, siguiendo la relación explícita que describe la especificación funcional entre cada par de clases.

---

# Jerarquía de Clases de Dominio

```
Usuario (Abstracta)
├── Comprador
├── Vendedor
├── OperadorLogistico
├── Administrador
└── Supervisor

Producto (Abstracta)
├── ProductoFisico
└── ProductoDigital

CarritoCompra

Bodega

Inventario

Pedido

Envio

Factura

Devolucion

Reembolso
```

---

# Entidades

---

# Usuario (Abstracta)

## Descripción

Representa a cualquier persona autorizada para interactuar con el sistema NexusMarket. Esta clase abstracta centraliza la información de identificación y de cuenta compartida por todos los roles del negocio (Comprador, Vendedor, Operador Logístico, Administrador y Supervisor).

Esta clase no puede ser instanciada directamente.

## Atributos

| Atributo | Tipo   | Descripción                                             |
| -------- | ------ | -------------------------------------------------------- |
| id       | String | Identificador único del usuario dentro del sistema.       |
| fullName | String | Nombre completo del usuario.                              |
| email    | String | Correo electrónico registrado del usuario.                |
| role     | String | Rol de negocio asignado al usuario.                        |
| status   | String | Estado operativo actual de la cuenta del usuario.          |

---

# Comprador

## Descripción

Representa a un usuario que realiza compras dentro del marketplace. El comprador selecciona productos, gestiona su carrito y confirma pedidos, pero nunca administra información de otros compradores ni de inventarios.

Especializa la entidad Usuario.

## Atributos

| Atributo             | Tipo   | Descripción                                                |
| --------------------- | ------ | ------------------------------------------------------------ |
| primaryAddress        | String | Dirección principal utilizada para las entregas del comprador. |
| additionalAddresses   | String | Direcciones adicionales registradas por el comprador.          |
| commercialStatus      | String | Estado comercial del comprador dentro del sistema.              |

---

# Vendedor

## Descripción

Representa a un usuario responsable de comercializar productos dentro del marketplace. Los vendedores no pueden auto-registrarse: son incorporados exclusivamente por un Administrador, junto con su primera bodega.

## Atributos

| Atributo     | Tipo          | Descripción                                                     |
| ------------- | ------------- | ------------------------------------------------------------------ |
| registeredBy  | Administrador | Administrador que incorporó al vendedor al marketplace.              |

---

# OperadorLogistico

## Descripción

Representa al usuario encargado de la operación física de las bodegas y de los despachos asociados a los pedidos.

No cuenta con atributos propios más allá de los heredados de Usuario.

---

# Administrador

## Descripción

Representa al usuario responsable de incorporar vendedores, registrar bodegas y administrar el funcionamiento general del sistema.

No cuenta con atributos propios más allá de los heredados de Usuario.

---

# Supervisor

## Descripción

Representa un perfil de solo consulta y seguimiento operativo del negocio.

No cuenta con atributos propios más allá de los heredados de Usuario.

---

# CarritoCompra

## Descripción

Representa la selección provisional de productos realizada por un comprador antes de confirmar una compra. Al confirmarse, el carrito da origen a un Pedido.

## Atributos

| Atributo  | Tipo             | Descripción                                              |
| --------- | ---------------- | ------------------------------------------------------------ |
| owner     | Comprador        | Comprador propietario del carrito de compras.                  |
| products  | List\<Producto\> | Productos seleccionados dentro del carrito antes de confirmar. |

---

# Producto (Abstracta)

## Descripción

Representa cualquier bien ofrecido dentro del catálogo del marketplace. El catálogo diferencia entre productos físicos, que requieren inventario y despacho, y productos digitales, de entrega inmediata tras el pago.

Esta clase no puede ser instanciada directamente.

## Atributos

| Atributo    | Tipo      | Descripción                                                                |
| ------------ | --------- | ----------------------------------------------------------------------------- |
| variantes    | List      | Variantes del producto (diferencias de color, talla, modelo, entre otras).     |
| status       | String    | Estado de publicación del producto (Publicado, Suspendido o Descontinuado).    |
| publishedBy  | Vendedor  | Vendedor que registró y publicó el producto en el catálogo.                    |

---

# ProductoFisico

## Descripción

Representa un producto que requiere inventario y un proceso de despacho para llegar al comprador.

No cuenta con atributos propios más allá de los heredados de Producto.

---

# ProductoDigital

## Descripción

Representa un producto de entrega inmediata tras la confirmación del pago, sin necesidad de inventario físico.

No cuenta con atributos propios más allá de los heredados de Producto.

---

# Bodega

## Descripción

Representa un espacio físico de almacenamiento de inventario. Se distinguen bodegas del Marketplace y bodegas propias de los Vendedores.

## Atributos

| Atributo     | Tipo          | Descripción                                                    |
| ------------- | ------------- | -------------------------------------------------------------------- |
| dueño         | String        | Propietario de la bodega (Marketplace o Vendedor asociado).           |
| tipo          | String        | Clasificación de la bodega según su propietario.                        |
| registeredBy  | Administrador | Administrador que registró la bodega en el sistema.                       |

---

# Inventario

## Descripción

Representa las existencias disponibles de un producto en una bodega específica. El inventario es distribuido y debe estar vinculado obligatoriamente a un producto y a una bodega. No se permiten existencias negativas. Su administración está a cargo tanto del Vendedor como del Operador Logístico, según la matriz de responsabilidades del negocio.

## Atributos

| Atributo     | Tipo    | Descripción                                                                    |
| ------------- | ------- | ------------------------------------------------------------------------------- |
| product       | Producto | Producto al cual está vinculado obligatoriamente el registro de inventario.       |
| warehouse     | Bodega  | Bodega en la que se almacena el producto.                                          |
| movementType  | String  | Tipo de movimiento registrado (Ingreso, Reserva, Salida por venta, Ajuste o Devolución). |
| stock         | int     | Cantidad disponible del producto en la bodega asociada.                            |

---

# Pedido

## Descripción

Representa el compromiso comercial formal entre un comprador y el marketplace. Un pedido finalizado no puede ser modificado bajo ninguna circunstancia. Su gestión involucra tanto al Comprador como al Vendedor y al Operador Logístico, según la matriz de responsabilidades del negocio.

## Atributos

| Atributo | Tipo             | Descripción                                                |
| -------- | ---------------- | -------------------------------------------------------------- |
| buyer    | Comprador        | Comprador que realizó el pedido.                                  |
| cart     | CarritoCompra    | Carrito de compras que dio origen al pedido al ser confirmado.      |
| products | List\<Producto\> | Productos incluidos en el pedido.                                     |
| estado   | EstadoPedido     | Estado actual del pedido dentro de su ciclo de vida.                   |

---

# EstadoPedido (Enumeración)

## Descripción

Representa los posibles estados por los que transita un pedido a lo largo de su ciclo de vida, una vez confirmado desde el carrito de compra.

## Valores

| Valor               | Descripción                                                      |
| -------------------- | -------------------------------------------------------------------- |
| PendienteDePago       | El pedido fue confirmado desde el carrito y espera el pago.            |
| Pagado                | El pago del pedido fue validado.                                        |
| Despachado            | El pedido fue empacado y entregado al transporte.                        |
| EntregadoFinalizado   | El pedido fue entregado y cerrado; no admite modificaciones.               |

---

# Envio

## Descripción

Representa el proceso logístico de empaque, despacho y transporte asociado a un pedido con productos físicos.

## Atributos

| Atributo   | Tipo               | Descripción                                          |
| ----------- | ------------------ | ---------------------------------------------------------- |
| order       | Pedido             | Pedido asociado al envío.                                     |
| handledBy   | OperadorLogistico  | Operador Logístico encargado del empaque y despacho.            |

---

# Factura

## Descripción

Representa la información comercial asociada a la venta de un pedido.

## Atributos

| Atributo | Tipo   | Descripción                        |
| -------- | ------ | --------------------------------------- |
| order    | Pedido | Pedido cuya venta describe la factura.    |

---

# Devolucion

## Descripción

Representa el proceso mediante el cual un comprador solicita devolver un producto adquirido.

La especificación funcional no detalla sus atributos ni su relación formal con Pedido o Reembolso; se deja documentado como un vacío de la especificación a validar con el negocio.

---

# Reembolso

## Descripción

Representa el proceso de devolución del dinero asociado a una compra. Su gestión está a cargo del Comprador y del Administrador, según la matriz de responsabilidades del negocio.

La especificación funcional no detalla sus atributos ni su relación estructural con Pedido o Devolución; se deja documentado como un vacío de la especificación a validar con el negocio.