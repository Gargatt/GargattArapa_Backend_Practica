package com.gargattarapa.proyecto.practica1.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.gargattarapa.proyecto.practica1.models.entity.MiembroProyecto;
import com.gargattarapa.proyecto.practica1.services.IMiembroProyectoService;

@RestController
@RequestMapping("/api/miembros")
@CrossOrigin(origins = "*")
public class MiembroProyectoController {

    @Autowired
    private IMiembroProyectoService miembroProyectoService;

    /**
     * Listar todos los miembros
     * GET http://localhost:8081/api/miembros
     */
    @GetMapping
    public ResponseEntity<List<MiembroProyecto>> listarTodos() {
        List<MiembroProyecto> miembros = miembroProyectoService.findAll();
        return ResponseEntity.ok(miembros);
    }

    /**
     * Buscar miembro por ID
     * GET http://localhost:8081/api/miembros/1
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        MiembroProyecto miembro = miembroProyectoService.findById(id);

        if (miembro == null) {
            Map<String, String> response = new HashMap<>();
            response.put("mensaje", "Miembro no encontrado con ID: " + id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        return ResponseEntity.ok(miembro);
    }

    /**
     * Listar miembros de un proyecto
     * GET http://localhost:8081/api/miembros/proyecto/1
     */
    @GetMapping("/proyecto/{idProyecto}")
    public ResponseEntity<List<MiembroProyecto>> listarPorProyecto(@PathVariable Long idProyecto) {
        List<MiembroProyecto> miembros = miembroProyectoService.findByIdProyecto(idProyecto);
        return ResponseEntity.ok(miembros);
    }

    /**
     * Listar proyectos de un usuario
     * GET http://localhost:8081/api/miembros/usuario/1
     */
    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<MiembroProyecto>> listarPorUsuario(@PathVariable Long idUsuario) {
        List<MiembroProyecto> miembros = miembroProyectoService.findByIdUsuario(idUsuario);
        return ResponseEntity.ok(miembros);
    }

    /**
     * Listar miembros activos de un proyecto
     * GET http://localhost:8081/api/miembros/proyecto/1/activos
     */
    @GetMapping("/proyecto/{idProyecto}/activos")
    public ResponseEntity<List<MiembroProyecto>> listarActivosPorProyecto(@PathVariable Long idProyecto) {
        List<MiembroProyecto> miembros = miembroProyectoService.findMiembrosActivosPorProyecto(idProyecto);
        return ResponseEntity.ok(miembros);
    }

    /**
     * Listar proyectos activos de un usuario
     * GET http://localhost:8081/api/miembros/usuario/1/activos
     */
    @GetMapping("/usuario/{idUsuario}/activos")
    public ResponseEntity<List<MiembroProyecto>> listarActivosPorUsuario(@PathVariable Long idUsuario) {
        List<MiembroProyecto> proyectos = miembroProyectoService.findProyectosActivosPorUsuario(idUsuario);
        return ResponseEntity.ok(proyectos);
    }

    /**
     * Agregar miembro a un proyecto
     * POST http://localhost:8081/api/miembros/agregar
     * Body: {
     *   "idProyecto": 1,
     *   "idUsuario": 2,
     *   "rol": "MIEMBRO"
     * }
     */
    @PostMapping("/agregar")
    public ResponseEntity<?> agregarMiembro(@RequestBody Map<String, Object> body) {
        try {
            Long idProyecto = Long.valueOf(body.get("idProyecto").toString());
            Long idUsuario = Long.valueOf(body.get("idUsuario").toString());
            String rol = (String) body.getOrDefault("rol", "MIEMBRO");

            MiembroProyecto miembro = miembroProyectoService.agregarMiembro(idProyecto, idUsuario, rol);

            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Miembro agregado exitosamente");
            response.put("miembro", miembro);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (RuntimeException e) {
            Map<String, String> response = new HashMap<>();
            response.put("mensaje", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);

        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("mensaje", "Error al agregar miembro");
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Remover miembro de un proyecto (borrado lógico)
     * DELETE http://localhost:8081/api/miembros/remover/proyecto/1/usuario/2
     */
    @DeleteMapping("/remover/proyecto/{idProyecto}/usuario/{idUsuario}")
    public ResponseEntity<?> removerMiembro(@PathVariable Long idProyecto, @PathVariable Long idUsuario) {
        try {
            miembroProyectoService.removerMiembro(idProyecto, idUsuario);

            Map<String, String> response = new HashMap<>();
            response.put("mensaje", "Miembro removido exitosamente");

            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            Map<String, String> response = new HashMap<>();
            response.put("mensaje", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);

        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("mensaje", "Error al remover miembro");
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Cambiar rol de un miembro
     * PUT http://localhost:8081/api/miembros/cambiar-rol
     * Body: {
     *   "idProyecto": 1,
     *   "idUsuario": 2,
     *   "nuevoRol": "ADMIN"
     * }
     */
    @PutMapping("/cambiar-rol")
    public ResponseEntity<?> cambiarRol(@RequestBody Map<String, Object> body) {
        try {
            Long idProyecto = Long.valueOf(body.get("idProyecto").toString());
            Long idUsuario = Long.valueOf(body.get("idUsuario").toString());
            String nuevoRol = (String) body.get("nuevoRol");

            MiembroProyecto miembro = miembroProyectoService.cambiarRol(idProyecto, idUsuario, nuevoRol);

            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Rol cambiado exitosamente");
            response.put("miembro", miembro);

            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            Map<String, String> response = new HashMap<>();
            response.put("mensaje", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);

        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("mensaje", "Error al cambiar rol");
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Contar miembros activos de un proyecto
     * GET http://localhost:8081/api/miembros/proyecto/1/contar
     */
    @GetMapping("/proyecto/{idProyecto}/contar")
    public ResponseEntity<?> contarMiembrosActivos(@PathVariable Long idProyecto) {
        Long cantidad = miembroProyectoService.contarMiembrosActivos(idProyecto);

        Map<String, Object> response = new HashMap<>();
        response.put("idProyecto", idProyecto);
        response.put("cantidadMiembros", cantidad);

        return ResponseEntity.ok(response);
    }

    /**
     * Verificar si un usuario es miembro de un proyecto
     * GET http://localhost:8081/api/miembros/verificar/proyecto/1/usuario/2
     */
    @GetMapping("/verificar/proyecto/{idProyecto}/usuario/{idUsuario}")
    public ResponseEntity<?> verificarMiembro(@PathVariable Long idProyecto, @PathVariable Long idUsuario) {
        boolean esMiembro = miembroProyectoService.existsByIdProyectoAndIdUsuario(idProyecto, idUsuario);

        Map<String, Object> response = new HashMap<>();
        response.put("idProyecto", idProyecto);
        response.put("idUsuario", idUsuario);
        response.put("esMiembro", esMiembro);

        return ResponseEntity.ok(response);
    }
}