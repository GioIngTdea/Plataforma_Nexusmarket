# Servicios de Gestión de Usuarios

## Introducción

Este documento describe los procesos de negocio (servicios) relacionados con las personas y roles que interactúan con **NexusMarket**. Estos servicios se aseguran de que el registro y la modificación de usuarios cumplan con todas las reglas establecidas por el modelo de negocio, antes de ser guardados en el sistema.

Todo el lenguaje utilizado aquí se enfoca en el negocio, evitando detalles técnicos de programación o bases de datos.

---

# 1. Registro de Comprador

## Descripción

Este servicio permite que una nueva persona se registre en la plataforma como **Comprador**. El comprador es el usuario principal de la plataforma, encargado de navegar por el catálogo y realizar compras.

## Información Necesaria

Para registrar a un comprador, se requiere que la persona proporcione toda su información básica de perfil:
* Identificador único (ej. número de documento)
* Nombre completo
* Correo electrónico
* Dirección principal (para envíos)

## Reglas de Negocio (Validaciones)

Antes de crear la cuenta del comprador, el servicio debe validar lo siguiente:

1. **Correo Electrónico Único:** El sistema debe consultar sus registros previos para asegurarse de que el correo electrónico ingresado no pertenezca a otra cuenta existente. Si ya está en uso, el registro es rechazado.
2. **Identificador Único:** De forma similar, el identificador único no puede estar duplicado en el sistema.
3. **Estado Inicial:** Todo nuevo comprador debe iniciar con un estado de cuenta "Activo" y un estado comercial "Normal" por defecto.

## Guardado de Información

Una vez que toda la información provista es válida y cumple con las reglas, el servicio solicita que los datos del nuevo comprador se guarden de forma permanente en el sistema.

---

# 2. Registro de Vendedor

## Descripción

Permite la creación de un nuevo **Vendedor** dentro del marketplace. A diferencia de los compradores, los vendedores no pueden registrarse por sí mismos; deben ser incorporados por un Administrador de NexusMarket.

## Información Necesaria

El servicio requiere la siguiente información:
* Datos completos del nuevo vendedor (Identificador, Nombre, Correo).
* Los datos del **Administrador** que está realizando el registro (para fines de auditoría y seguimiento).

## Reglas de Negocio (Validaciones)

1. **Autorización:** El sistema valida que el usuario que intenta registrar al vendedor sea efectivamente un Administrador autorizado.
2. **Identificador y Correo Único:** Al igual que con los compradores, se verifica consultando al sistema que el correo y el identificador no estén registrados previamente.
3. **Creación de Bodega Inicial:** Como regla de negocio de NexusMarket, cuando un vendedor es creado, el sistema también debe prepararse para asignarle o crearle su primera bodega, lo cual puede desencadenar otro servicio interno de logística.

## Guardado de Información

Si el registro es aprobado y ejecutado por un Administrador válido, el vendedor es guardado en el sistema y queda habilitado para empezar a publicar sus productos.

---

# 3. Cambio de Estado de Usuario

## Descripción

Este proceso permite modificar la situación operativa actual de un usuario (por ejemplo, suspender a un vendedor por malas prácticas o bloquear a un comprador). 

## Información Necesaria

* La información del usuario cuyo estado se desea cambiar.
* El nuevo estado deseado (Ej. Activo, Suspendido, Bloqueado).

## Reglas de Negocio (Validaciones)

1. **Transición Válida:** El servicio verifica si el cambio de estado tiene sentido. Por ejemplo, no se puede "Activar" un usuario que ya está "Activo", y tal vez un usuario "Bloqueado" permanentemente no pueda volver a ser "Activo" dependiendo de las políticas de seguridad.
2. **Validación de Impacto:** En el caso de los vendedores, suspender su cuenta podría requerir validar qué sucede con sus productos publicados actualmente (por ejemplo, ocultarlos del catálogo).

## Guardado de Información

Una vez validadas las reglas de transición, el nuevo estado se actualiza y se guarda en el historial del usuario dentro de la plataforma.
