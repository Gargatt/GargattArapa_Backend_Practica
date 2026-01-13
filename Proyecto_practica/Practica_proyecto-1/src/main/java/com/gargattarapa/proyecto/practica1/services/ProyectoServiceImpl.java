package com.gargattarapa.proyecto.practica1.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gargattarapa.proyecto.practica1.models.entity.Proyecto;
import com.gargattarapa.proyecto.practica1.models.repository.ProyectoRepository;

@Service
public class ProyectoServiceImpl implements IProyectoService {

    @Autowired
    private ProyectoRepository proyectoRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Proyecto> findAll() {
        return proyectoRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Proyecto findById(Long id) {
        return proyectoRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public Proyecto findByCodigoProyecto(String codigoProyecto) {
        return proyectoRepository.findByCodigoProyecto(codigoProyecto).orElse(null);
    }

    @Override
    @Transactional
    public Proyecto save(Proyecto proyecto) {
        
        if (proyectoRepository.existsByCodigoProyecto(proyecto.getCodigoProyecto())) {
            throw new RuntimeException("El código de proyecto ya existe: " + proyecto.getCodigoProyecto());
        }

        if (proyecto.getIdUsuarioCreador() == null) {
            throw new RuntimeException("El proyecto debe tener un ID de usuario creador");
        }

        return proyectoRepository.save(proyecto);
    }

    @Override
    @Transactional
    public Proyecto update(Long id, Proyecto proyecto) {
        Proyecto proyectoExistente = findById(id);

        if (proyectoExistente == null) {
            throw new RuntimeException("Proyecto no encontrado con ID: " + id);
        }

        proyectoExistente.setNombreProyecto(proyecto.getNombreProyecto());
        proyectoExistente.setDescripcion(proyecto.getDescripcion());
        proyectoExistente.setIconoProyecto(proyecto.getIconoProyecto());
        proyectoExistente.setEstadoProyecto(proyecto.getEstadoProyecto());
        proyectoExistente.setCategoria(proyecto.getCategoria());
        proyectoExistente.setFechaInicio(proyecto.getFechaInicio());
        proyectoExistente.setFechaFinalizacion(proyecto.getFechaFinalizacion());

        if (!proyectoExistente.getCodigoProyecto().equals(proyecto.getCodigoProyecto())) {
            if (proyectoRepository.existsByCodigoProyecto(proyecto.getCodigoProyecto())) {
                throw new RuntimeException("El código de proyecto ya existe: " + proyecto.getCodigoProyecto());
            }
            proyectoExistente.setCodigoProyecto(proyecto.getCodigoProyecto());
        }

        return proyectoRepository.save(proyectoExistente);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Proyecto proyecto = findById(id);

        if (proyecto == null) {
            throw new RuntimeException("Proyecto no encontrado con ID: " + id);
        }

        proyectoRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Proyecto> findByEstadoProyecto(String estadoProyecto) {
        return proyectoRepository.findByEstadoProyecto(estadoProyecto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Proyecto> findByNombreProyecto(String nombreProyecto) {
        return proyectoRepository.findByNombreProyectoContainingIgnoreCase(nombreProyecto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Proyecto> findByCategoria(String categoria) {
        return proyectoRepository.findByCategoria(categoria);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Proyecto> findByUsuarioCreadorId(Long idUsuario) {
        return proyectoRepository.findByIdUsuarioCreador(idUsuario);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Proyecto> findByEstadoAndUsuarioCreador(String estado, Long idUsuario) {
        return proyectoRepository.findByEstadoAndUsuarioCreador(estado, idUsuario);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByCodigoProyecto(String codigoProyecto) {
        return proyectoRepository.existsByCodigoProyecto(codigoProyecto);
    }

    @Override
    @Transactional(readOnly = true)
    public Long contarProyectosPorEstado(String estado) {
        return proyectoRepository.contarProyectosPorEstado(estado);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Proyecto> busquedaAvanzada(String codigo, String nombre, String estado, String categoria) {
        return proyectoRepository.busquedaAvanzada(codigo, nombre, estado, categoria);
    }
}