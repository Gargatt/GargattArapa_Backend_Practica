package com.gargattarapa.proyecto.practica.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.gargattarapa.proyecto.practica.models.entity.Usuario;
import com.gargattarapa.proyecto.practica.services.IUsuarioService;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*") // Permitir peticiones desde cualquier origen (para desarrollo)
public class UsuarioController {

    @Autowired
    private IUsuarioService usuarioService;

    /**
     * Listar todos los usuarios
     * GET http://localhost:8080/api/usuarios
     */
    @GetMapping
    public ResponseEntity<List<Usuario>> listarTodos() {
        List<Usuario> usuarios = usuarioService.findAll();
        return ResponseEntity.ok(usuarios);
    }

    /**
     * Listar solo usuarios activos
     * GET http://localhost:8080/api/usuarios/activos
     */
    @GetMapping("/activos")
    public ResponseEntity<List<Usuario>> listarActivos() {
        List<Usuario> usuarios = usuarioService.findAllActivos();
        return ResponseEntity.ok(usuarios);
    }

    /**
     * Buscar usuario por ID
     * GET http://localhost:8080/api/usuarios/1
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        Usuario usuario = usuarioService.findById(id);
        
        if (usuario == null) {
            Map<String, String> response = new HashMap<>();
            response.put("mensaje", "Usuario no encontrado con ID: " + id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        
        return ResponseEntity.ok(usuario);
    }

    /**
     * Buscar usuario por correo
     * GET http://localhost:8080/api/usuarios/correo/juan@mail.com
     */
    @GetMapping("/correo/{correo}")
    public ResponseEntity<?> buscarPorCorreo(@PathVariable String correo) {
        Usuario usuario = usuarioService.findByCorreo(correo);
        
        if (usuario == null) {
            Map<String, String> response = new HashMap<>();
            response.put("mensaje", "Usuario no encontrado con correo: " + correo);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        
        return ResponseEntity.ok(usuario);
    }

    /**
     * Buscar usuarios por nombre
     * GET http://localhost:8080/api/usuarios/buscar?nombre=Juan
     */
    @GetMapping("/buscar")
    public ResponseEntity<List<Usuario>> buscarPorNombre(@RequestParam String nombre) {
        List<Usuario> usuarios = usuarioService.findByNombre(nombre);
        return ResponseEntity.ok(usuarios);
    }

    /**
     * Crear nuevo usuario (Registro)
     * POST http://localhost:8080/api/usuarios
     * Body: { "nombre": "Juan", "apellidos": "Pérez", "correo": "juan@mail.com", "password": "12345" }
     */
    @PostMapping
    public ResponseEntity<?> crear(@RequestBody Usuario usuario) {
        try {
            // Validar que el correo no exista
            if (usuarioService.existsByCorreo(usuario.getCorreo())) {
                Map<String, String> response = new HashMap<>();
                response.put("mensaje", "El correo ya está registrado");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            
            Usuario nuevoUsuario = usuarioService.save(usuario);
            
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Usuario creado exitosamente");
            response.put("usuario", nuevoUsuario);
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
            
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("mensaje", "Error al crear usuario");
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Actualizar usuario
     * PUT http://localhost:8080/api/usuarios/1
     * Body: { "nombre": "Juan", "apellidos": "Pérez", "correo": "juan@mail.com", "telefono": "987654321", ... }
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody Usuario usuario) {
        try {
            Usuario usuarioActualizado = usuarioService.update(id, usuario);
            
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Usuario actualizado exitosamente");
            response.put("usuario", usuarioActualizado);
            
            return ResponseEntity.ok(response);
            
        } catch (RuntimeException e) {
            Map<String, String> response = new HashMap<>();
            response.put("mensaje", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("mensaje", "Error al actualizar usuario");
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Eliminar usuario (borrado lógico)
     * DELETE http://localhost:8080/api/usuarios/1
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        try {
            usuarioService.delete(id);
            
            Map<String, String> response = new HashMap<>();
            response.put("mensaje", "Usuario eliminado exitosamente");
            
            return ResponseEntity.ok(response);
            
        } catch (RuntimeException e) {
            Map<String, String> response = new HashMap<>();
            response.put("mensaje", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("mensaje", "Error al eliminar usuario");
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Login de usuario
     * POST http://localhost:8080/api/usuarios/login
     * Body: { "correo": "juan@mail.com", "password": "12345" }
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credenciales) {
        try {
            String correo = credenciales.get("correo");
            String password = credenciales.get("password");
            
            if (correo == null || password == null) {
                Map<String, String> response = new HashMap<>();
                response.put("mensaje", "Correo y contraseña son obligatorios");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            
            Usuario usuario = usuarioService.login(correo, password);
            
            if (usuario == null) {
                Map<String, String> response = new HashMap<>();
                response.put("mensaje", "Credenciales inválidas");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
            
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Login exitoso");
            response.put("usuario", usuario);
            
            return ResponseEntity.ok(response);
            
        } catch (RuntimeException e) {
            Map<String, String> response = new HashMap<>();
            response.put("mensaje", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("mensaje", "Error en el login");
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Cambiar contraseña
     * PUT http://localhost:8080/api/usuarios/1/cambiar-password
     * Body: { "oldPassword": "12345", "newPassword": "54321" }
     */
    @PutMapping("/{id}/cambiar-password")
    public ResponseEntity<?> cambiarPassword(@PathVariable Long id, @RequestBody Map<String, String> passwords) {
        try {
            String oldPassword = passwords.get("oldPassword");
            String newPassword = passwords.get("newPassword");
            
            if (oldPassword == null || newPassword == null) {
                Map<String, String> response = new HashMap<>();
                response.put("mensaje", "Contraseña actual y nueva son obligatorias");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            
            boolean cambiado = usuarioService.changePassword(id, oldPassword, newPassword);
            
            if (!cambiado) {
                Map<String, String> response = new HashMap<>();
                response.put("mensaje", "Contraseña actual incorrecta");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            
            Map<String, String> response = new HashMap<>();
            response.put("mensaje", "Contraseña actualizada exitosamente");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("mensaje", "Error al cambiar contraseña");
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}