package com.gargattarapa.proyecto.practica1.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.gargattarapa.proyecto.practica1.models.entity.Proyecto;
import com.gargattarapa.proyecto.practica1.services.IProyectoService;

@RestController
@RequestMapping("/api/proyectos")
@CrossOrigin(origins = "*")
public class ProyectoController {

    @Autowired
    private IProyectoService proyectoService;

    /**
     * Listar todos los proyectos
     * GET http://localhost:8081/api/proyectos
     */
    @GetMapping
    public ResponseEntity<List<Proyecto>> listarTodos() {
        List<Proyecto> proyectos = proyectoService.findAll();
        return ResponseEntity.ok(proyectos);
    }

    /**
     * Buscar proyecto por ID
     * GET http://localhost:8081/api/proyectos/1
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        Proyecto proyecto = proyectoService.findById(id);

        if (proyecto == null) {
            Map<String, String> response = new HashMap<>();
            response.put("mensaje", "Proyecto no encontrado con ID: " + id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        return ResponseEntity.ok(proyecto);
    }

    /**
     * Buscar proyecto por código
     * GET http://localhost:8081/api/proyectos/codigo/ATA-1
     */
    @GetMapping("/codigo/{codigo}")
    public ResponseEntity<?> buscarPorCodigo(@PathVariable String codigo) {
        Proyecto proyecto = proyectoService.findByCodigoProyecto(codigo);

        if (proyecto == null) {
            Map<String, String> response = new HashMap<>();
            response.put("mensaje", "Proyecto no encontrado con código: " + codigo);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        return ResponseEntity.ok(proyecto);
    }

    /**
     * Buscar proyectos por estado
     * GET http://localhost:8081/api/proyectos/estado/EN_CURSO
     */
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<Proyecto>> buscarPorEstado(@PathVariable String estado) {
        List<Proyecto> proyectos = proyectoService.findByEstadoProyecto(estado);
        return ResponseEntity.ok(proyectos);
    }

    /**
     * Buscar proyectos por nombre
     * GET http://localhost:8081/api/proyectos/buscar?nombre=Diseño
     */
    @GetMapping("/buscar")
    public ResponseEntity<List<Proyecto>> buscarPorNombre(@RequestParam String nombre) {
        List<Proyecto> proyectos = proyectoService.findByNombreProyecto(nombre);
        return ResponseEntity.ok(proyectos);
    }

    /**
     * Buscar proyectos por usuario creador
     * GET http://localhost:8081/api/proyectos/usuario/1
     */
    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<Proyecto>> buscarPorUsuario(@PathVariable Long idUsuario) {
        List<Proyecto> proyectos = proyectoService.findByUsuarioCreadorId(idUsuario);
        return ResponseEntity.ok(proyectos);
    }

    /**
     * Búsqueda avanzada con múltiples filtros
     * GET http://localhost:8081/api/proyectos/busqueda-avanzada?codigo=ATA&nombre=App&estado=EN_CURSO&categoria=Desarrollo
     */
    @GetMapping("/busqueda-avanzada")
    public ResponseEntity<List<Proyecto>> busquedaAvanzada(
            @RequestParam(required = false) String codigo,
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String estado,
            @RequestParam(required = false) String categoria) {
        
        List<Proyecto> proyectos = proyectoService.busquedaAvanzada(codigo, nombre, estado, categoria);
        return ResponseEntity.ok(proyectos);
    }

    /**
     * Crear nuevo proyecto
     * POST http://localhost:8081/api/proyectos
     * Body: {
     *   "codigoProyecto": "ATA-1",
     *   "nombreProyecto": "App de Gestión",
     *   "descripcion": "Proyecto de ejemplo",
     *   "estadoProyecto": "PLANIFICACION",
     *   "categoria": "Desarrollo",
     *   "fechaInicio": "2026-01-10",
     *   "idUsuarioCreador": 1
     * }
     */
    @PostMapping
    public ResponseEntity<?> crear(@RequestBody Proyecto proyecto) {
        try {
            // Validar que el código no exista
            if (proyectoService.existsByCodigoProyecto(proyecto.getCodigoProyecto())) {
                Map<String, String> response = new HashMap<>();
                response.put("mensaje", "El código de proyecto ya existe: " + proyecto.getCodigoProyecto());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            // Validar que tenga ID de usuario creador
            if (proyecto.getIdUsuarioCreador() == null) {
                Map<String, String> response = new HashMap<>();
                response.put("mensaje", "El proyecto debe tener un ID de usuario creador");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            Proyecto nuevoProyecto = proyectoService.save(proyecto);

            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Proyecto creado exitosamente");
            response.put("proyecto", nuevoProyecto);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("mensaje", "Error al crear proyecto");
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Actualizar proyecto
     * PUT http://localhost:8081/api/proyectos/1
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody Proyecto proyecto) {
        try {
            Proyecto proyectoActualizado = proyectoService.update(id, proyecto);

            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Proyecto actualizado exitosamente");
            response.put("proyecto", proyectoActualizado);

            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            Map<String, String> response = new HashMap<>();
            response.put("mensaje", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);

        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("mensaje", "Error al actualizar proyecto");
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Eliminar proyecto
     * DELETE http://localhost:8081/api/proyectos/1
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        try {
            proyectoService.delete(id);

            Map<String, String> response = new HashMap<>();
            response.put("mensaje", "Proyecto eliminado exitosamente");

            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            Map<String, String> response = new HashMap<>();
            response.put("mensaje", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);

        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("mensaje", "Error al eliminar proyecto");
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Contar proyectos por estado
     * GET http://localhost:8081/api/proyectos/contar/EN_CURSO
     */
    @GetMapping("/contar/{estado}")
    public ResponseEntity<?> contarPorEstado(@PathVariable String estado) {
        Long cantidad = proyectoService.contarProyectosPorEstado(estado);
        
        Map<String, Object> response = new HashMap<>();
        response.put("estado", estado);
        response.put("cantidad", cantidad);
        
        return ResponseEntity.ok(response);
    }
}