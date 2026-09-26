# NexusMarket — Resumen de Implementación de Servicios

**Alcance de este documento:** describe todo lo que se creó en esta iteración para desatrasar la Gestión de Servicios de NexusMarket, tanto en documentación (SDD) como en código Java, y explica cómo se usa cada elemento nuevo. Está pensado como acompañante del código; no reemplaza los documentos de negocio en `SDD/domain/service/`.

---

## 1. Qué se corrigió en la base del proyecto

Antes de construir los servicios, se corrigieron tres problemas estructurales heredados de iteraciones anteriores:

1. **Ubicación de los modelos:** estaban físicamente en `domain/servicio/models/` pero declaraban `package application.domain.models;`. Se movieron a `application/domain/models/`, donde ahora coinciden ruta física y paquete declarado.
2. **Carpeta huérfana duplicada:** existía `nexusmarket/src/...` (un solo nivel de anidación) junto a la raíz Maven real `nexusmarket/nexusmarket/src/...`. Se eliminó la carpeta huérfana.
3. **`target/` obsoleto:** contenía `.class` compilados de una versión anterior del código que ya no correspondía al código fuente actual. Se eliminó (se regenera automáticamente con `mvn compile`).

## 2. Convenciones aplicadas en todo el código nuevo

- **Idioma:** todo el código (paquetes, clases, métodos, variables) está en inglés. Las entidades de dominio conservan su nombre de negocio en español (`Usuario`, `Comprador`, `Pedido`, etc.) porque así ya estaban establecidas en el proyecto.
- **Arquitectura hexagonal:** cada caso de uso tiene una interfaz de puerto de entrada en `application.domain.ports.in` y una única clase de servicio en `application.domain.service.<subdominio>` que la implementa. La persistencia se abstrae en interfaces de puerto de salida en `application.domain.ports.out` (sin implementación todavía — eso corresponde a la capa de infraestructura/adaptadores, fuera del alcance de esta tarea).
- **SOLID:**
  - *S:* cada servicio resuelve un único caso de uso.
  - *O/D:* los servicios dependen de interfaces de puertos, nunca de una implementación concreta; se inyectan por constructor (`@RequiredArgsConstructor`).
  - *L:* los servicios que operan sobre `Usuario` o `Producto` funcionan igual con cualquiera de sus subclases.
  - *I:* cada puerto de entrada expone un único método `execute(...)`, sin mezclar operaciones no relacionadas.
- **Estado autoritativo:** ningún servicio confía en el objeto recibido como parámetro para datos críticos; siempre vuelve a consultar el estado real a través de un puerto de salida antes de mutar algo.
- **Validaciones como métodos privados**, en el mismo servicio, antes de ejecutar la operación.
- **Excepciones de dominio específicas** (27 en total) en vez de excepciones genéricas.

## 3. Inventario de lo creado

| Capa | Cantidad | Ubicación |
|---|---|---|
| Modelos de dominio (enriquecidos) | 17 | `application/domain/models/` |
| Value Objects (enumeraciones) | 8 | `application/domain/valueobjects/` |
| Excepciones de dominio | 27 | `application/domain/exception/` |
| Puertos de entrada (casos de uso) | 22 | `application/domain/ports/in/` |
| Puertos de salida (repositorios) | 10 | `application/domain/ports/out/` |
| **Servicios de aplicación** | **22** | `application/domain/service/<subdominio>/` |
| Documentos de servicios actualizados/nuevos | 5 | `SDD/domain/service/` |

Los 22 servicios superan el mínimo de 20 solicitado, y entre todos cubren cada una de las 17 clases del Modelo de Dominio — no queda ninguna entidad sin al menos un servicio que la use.

---

## 4. Value Objects creados

| Value Object | Valores | Para qué sirve |
|---|---|---|
| `Role` | BUYER, SELLER, LOGISTICS_OPERATOR, ADMINISTRATOR, SUPERVISOR | Rol de negocio de un `Usuario` |
| `UserStatus` | ACTIVE, SUSPENDED, BLOCKED | Estado operativo de una cuenta |
| `ProductStatus` | PUBLISHED, SUSPENDED, DISCONTINUED | Estado de publicación de un producto |
| `WarehouseOwnerType` | MARKETPLACE, SELLER | Tipo de propietario de una bodega |
| `InventoryMovementType` | ENTRY, RESERVATION, SALE_EXIT, ADJUSTMENT, RETURN | Razón de un movimiento de inventario |
| `OrderStatus` | PENDING_PAYMENT, PAID, DISPATCHED, DELIVERED_COMPLETED, CANCELLED | Ciclo de vida de un pedido |
| `ReturnStatus` | REQUESTED, APPROVED, REJECTED | Resolución de una solicitud de devolución |
| `RefundStatus` | PENDING, PROCESSED | Estado de un reembolso |

**Cómo se usan:** son parámetros o campos tipados en servicios y modelos, en vez de `String` sueltos (evita errores de digitación y hace explícitos los valores válidos).

## 5. Los 22 servicios: qué hacen y cómo se usan

Cada entrada indica el puerto de entrada, la clase que lo implementa, sus dependencias (inyectadas por constructor), qué valida y un ejemplo mínimo de invocación.

### 5.1 Gestión de Usuarios (3 servicios)

**1. `RegisterBuyerPort` → `RegisterBuyerService`**
Registra un nuevo comprador. Valida correo e identificador únicos; asigna `Role.BUYER`, `UserStatus.ACTIVE` y estado comercial `"NORMAL"`.
```java
Comprador buyer = new Comprador();
buyer.setId("CC123"); buyer.setEmail("ana@mail.com"); buyer.setFullName("Ana Ruiz");
buyer.setPrimaryAddress("Calle 10 #5-20");
Comprador saved = registerBuyerPort.execute(buyer);
```

**2. `RegisterSellerPort` → `RegisterSellerService`**
Registra un nuevo vendedor. Solo un `Administrador` puede ejecutarlo. Internamente invoca `RegisterWarehousePort` para crear la primera bodega del vendedor.
```java
Vendedor seller = new Vendedor();
seller.setId("NIT456"); seller.setEmail("tienda@mail.com"); seller.setFullName("Tienda XYZ");
Vendedor saved = registerSellerPort.execute(seller, "ADMIN01");
```

**3. `ChangeUserStatusPort` → `ChangeUserStatusService`**
Cambia el estado de una cuenta. Solo un `Administrador` puede ejecutarlo. Valida el grafo de transiciones (`ACTIVE↔SUSPENDED`, `ACTIVE/SUSPENDED→BLOCKED`, nada sale de `BLOCKED`). Si el usuario es un vendedor y se suspende, invoca `HideSellerProductsPort` automáticamente.
```java
changeUserStatusPort.execute("NIT456", UserStatus.SUSPENDED, "ADMIN01");
```

### 5.2 Gestión de Catálogo (4 servicios)

**4. `PublishPhysicalProductPort` → `PublishPhysicalProductService`**
Publica un producto físico. Exige que el vendedor esté activo y que el producto tenga al menos una variante.
```java
ProductoFisico product = new ProductoFisico();
product.setId("P001"); product.setPrice(new BigDecimal("59900"));
product.setVariants(List.of("Talla M"));
product.setPublishedBy(seller);
publishPhysicalProductPort.execute(product);
```

**5. `PublishDigitalProductPort` → `PublishDigitalProductService`**
Publica un producto digital. Exige vendedor activo y una configuración de entrega definida (link o código).
```java
ProductoDigital product = new ProductoDigital();
product.setId("P002"); product.setPrice(new BigDecimal("29900"));
product.setDeliveryConfig("https://descargas.nexusmarket.com/xyz");
product.setPublishedBy(seller);
publishDigitalProductPort.execute(product);
```

**6. `UpdateProductPort` → `UpdateProductService`**
Actualiza nombre, descripción, precio, variantes o estado de un producto. Solo el vendedor dueño o un administrador pueden hacerlo.
```java
Producto changes = existingProduct;
changes.setStatus(ProductStatus.DISCONTINUED);
updateProductPort.execute(changes, "NIT456");
```

**7. `HideSellerProductsPort` → `HideSellerProductsService`**
Suspende todos los productos publicados por un vendedor. Se invoca automáticamente al suspender la cuenta del vendedor, pero también puede llamarse directamente.
```java
hideSellerProductsPort.execute("NIT456");
```

### 5.3 Gestión de Inventario y Logística (7 servicios)

**8. `RegisterWarehousePort` → `RegisterWarehouseService`**
Registra una bodega nueva. Solo un administrador puede crearla; se usa tanto de forma directa como internamente al registrar un vendedor.
```java
Bodega warehouse = new Bodega();
warehouse.setLocation("Zona Franca Medellín"); warehouse.setCapacity(1000);
warehouse.setOwnerType(WarehouseOwnerType.MARKETPLACE);
registerWarehousePort.execute(warehouse, "ADMIN01");
```

**9. `RegisterInventoryEntryPort` → `RegisterInventoryEntryService`**
Registra ingreso de mercancía nueva a una bodega (movimiento `ENTRY`).
```java
registerInventoryEntryPort.execute("P001", "WH01", 50, "NIT456");
```

**10. `AdjustInventoryPort` → `AdjustInventoryService`**
Ajusta manualmente el stock existente (pérdidas, ajustes, devoluciones manuales), respetando la regla de no-negativos.
```java
adjustInventoryPort.execute("P001", "WH01", 3, InventoryMovementType.ADJUSTMENT, "NIT456");
```

**11. `ReserveInventoryPort` → `ReserveInventoryService`** *(interno)*
Aparta stock físico al confirmar un pedido. Lo invoca `ConfirmOrderService`; no se expone directamente al usuario final del sistema.
```java
reserveInventoryPort.execute("P001", "WH01", 2);
```

**12. `DispatchOrderPort` → `DispatchOrderService`**
Un operador logístico despacha un pedido pagado: descuenta definitivamente la reserva, crea el `Envio` y pasa el pedido a `DISPATCHED`.
```java
Envio shipment = dispatchOrderPort.execute("ORD100", "OPLOG01");
```

**13. `CompleteDeliveryPort` → `CompleteDeliveryService`**
Marca un pedido despachado como entregado y lo cierra definitivamente.
```java
completeDeliveryPort.execute("ORD100");
```

**14. `RestockReturnedInventoryPort` → `RestockReturnedInventoryService`** *(interno)*
Repone a la bodega el stock de una devolución aprobada. Lo invoca `ApproveOrRejectReturnService`.
```java
restockReturnedInventoryPort.execute("P001", "WH01", 1);
```

### 5.4 Gestión de Compras (6 servicios)

**15. `AddProductToCartPort` → `AddProductToCartService`**
Agrega un producto publicado al carrito del comprador (crea el carrito si no existe).
```java
addProductToCartPort.execute("CC123", "P001", 2);
```

**16. `RemoveProductFromCartPort` → `RemoveProductFromCartService`**
Retira un producto del carrito. Valida que el carrito pertenezca al comprador.
```java
removeProductFromCartPort.execute("CC123", "P001");
```

**17. `ConfirmOrderPort` → `ConfirmOrderService`**
Convierte el carrito en un `Pedido` formal: valida stock real de cada producto físico, lo reserva (vía `ReserveInventoryPort`), congela precios y vacía el carrito.
```java
Pedido order = confirmOrderPort.execute("CC123", "Calle 10 #5-20");
```

**18. `ValidatePaymentAndGenerateInvoicePort` → `ValidatePaymentAndGenerateInvoiceService`**
Reacciona a un pago exitoso: pasa el pedido a `PAID` y emite la `Factura`.
```java
Factura invoice = validatePaymentAndGenerateInvoicePort.execute("ORD100");
```

**19. `CancelOrderPort` → `CancelOrderService`**
Cancela un pedido aún no pagado (comprador dueño o administrador) y libera cualquier inventario reservado.
```java
cancelOrderPort.execute("ORD100", "CC123");
```

**20. `RequestReturnPort` → `RequestReturnService`**
Un comprador solicita la devolución de un producto de un pedido ya entregado.
```java
Devolucion request = requestReturnPort.execute("ORD100", "P001", "CC123", "Producto llegó defectuoso");
```

### 5.5 Gestión de Post-Venta (2 servicios — documento nuevo `gestion-postventa.md`)

**21. `ApproveOrRejectReturnPort` → `ApproveOrRejectReturnService`**
El vendedor dueño del producto o un administrador resuelve la solicitud. Si se aprueba, dispara `RestockReturnedInventoryPort`.
```java
Devolucion resolved = approveOrRejectReturnPort.execute("RET001", true, "NIT456");
```

**22. `ProcessRefundPort` → `ProcessRefundService`**
Un administrador procesa el reembolso de una devolución ya aprobada (una sola vez por devolución).
```java
Reembolso refund = processRefundPort.execute("RET001", "ADMIN01");
```

## 6. Lo que falta (fuera del alcance de esta tarea)

Esta entrega cubre la **capa de dominio** (modelos, value objects, excepciones, puertos y servicios de aplicación). Para que el proyecto sea ejecutable de punta a punta todavía falta:

- **Adaptadores de persistencia** (implementaciones concretas de los 10 puertos de salida — en memoria, JPA, etc.).
- **Adaptadores de entrada** (controladores REST u otro mecanismo que invoque los puertos de entrada).
- **Pruebas unitarias** de los 22 servicios.
- Cableado de Spring (los `@Service` ya están anotados y listos para ser inyectados en cuanto existan los adaptadores).

## 7. Supuestos de diseño documentados

Dos entidades del Modelo de Dominio (`Devolucion` y `Reembolso`) no tenían atributos definidos en la especificación funcional original. Se les asignaron atributos razonables en esta iteración, dejados explícitamente como propuesta a validar con el negocio tanto en `Domain Model.md` como en el nuevo `gestion-postventa.md`:

- `Devolucion`: pedido, producto, comprador solicitante, motivo, estado, quién la resolvió.
- `Reembolso`: devolución de origen, monto, estado, quién lo procesó.
