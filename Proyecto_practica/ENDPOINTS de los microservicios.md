ENDPOINTS de los microservicios:



**USUARIOS:
+Crear Usuario: POST http://localhost:8080/api/usuarios

{**

    **"nombre": "Juan",**

    **"apellidos": "Pérez",**

    **"correo": "juan@mail.com",**

    **"password": "12345"**

**}**



**+Listar todos los usuarios: GET http://localhost:8080/api/usuarios**

**+Buscar usuario por ID: GET http://localhost:8080/api/usuarios/1
+Buscar usuario por correo: GET http://localhost:8080/api/usuarios/correo/juan@mail.com**

**+Login: 
{**

    **"correo": "juan@mail.com",**

    **"password": "12345"**

**}**



**+Buscar usuarios por nombre: GET http://localhost:8080/api/usuarios/buscar?nombre=Juan**

**+Cambiar contraseña: PUT http://localhost:8080/api/usuarios/1/cambiar-password**

**{**

    **"oldPassword": "12345",**

    **"newPassword": "nueva123"**

**}**



**+Listar usuarios activos: GET http://localhost:8080/api/usuarios/activos**

**+Eliminar usuario: DELETE http://localhost:8080/api/usuarios/3**



**PROYECTO:**



**+Crear Proyecto: POST http://localhost:8081/api/proyectos**

**{**

    **"codigoProyecto": "ATA-1",**

    **"nombreProyecto": "App de Gestión de Proyectos",**

    **"descripcion": "Sistema web para gestionar proyectos y tareas",**

    **"estadoProyecto": "PLANIFICACION",**

    **"categoria": "Desarrollo Web",**

    **"fechaInicio": "2026-01-10",**

    **"fechaFinalizacion": "2026-06-30",**

    **"idUsuarioCreador": 1**

**}**



**+Listar todos los Proyectos: GET http://localhost:8081/api/proyectos**

**+Buscar proyecto por ID: GET http://localhost:8081/api/proyectos/1**

**+Buscar proyectos por estado: GET http://localhost:8081/api/proyectos/estado/EN\_CURSO**

**+Buscar proyectos por nombre: GET http://localhost:8081/api/proyectos/buscar?nombre=App**

**+Buscar proyectos por usuario creador: GET http://localhost:8081/api/proyectos/usuario/1**

**+Búsqueda avanzada (múltiples filtros): GET http://localhost:8081/api/proyectos/busqueda-avanzada?codigo=ATA\&estado=PLANIFICACION**

**+Actualizar proyecto: PUT http://localhost:8081/api/proyectos/1**

**{**

    **"codigoProyecto": "ATA-1",**

    **"nombreProyecto": "App de Gestión de Proyectos",**

    **"descripcion": "Sistema web para gestionar proyectos y tareas - Actualizado",**

    **"estadoProyecto": "EN\_CURSO",**

    **"categoria": "Desarrollo Web",**

    **"fechaInicio": "2026-01-10",**

    **"fechaFinalizacion": "2026-06-30"**

**}**



**+Contar proyectos por estado: GET http://localhost:8081/api/proyectos/contar/EN\_CURSO**

**+Eliminar proyecto: DELETE http://localhost:8081/api/proyectos/3**



**Miembros-Proyecto:**

**+Agregar miembro: POST http://localhost:8081/api/miembros/agregar**

**{**

    **"idProyecto": 1,**

    **"idUsuario": 1,**

    **"rol": "OWNER"**

**}**



**+Listar todos los miembros: GET http://localhost:8081/api/miembros**

**+Listar miembros de un proyecto: GET http://localhost:8081/api/miembros/proyecto/1**

**+Listar proyectos de un usuario: GET http://localhost:8081/api/miembros/usuario/1**

**+Listar miembros activos de un proyecto: GET http://localhost:8081/api/miembros/proyecto/1/activos**

**+Cambiar rol de un miembro: PUT http://localhost:8081/api/miembros/cambiar-rol**

**{**

    **"idProyecto": 1,**

    **"idUsuario": 2,**

    **"nuevoRol": "MIEMBRO"**

**}**



**+Remover miembro de proyecto: DELETE http://localhost:8081/api/miembros/remover/proyecto/1/usuario/2**

