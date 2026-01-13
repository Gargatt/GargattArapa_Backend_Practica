package com.gargattarapa.proyecto.practica1.services;

import java.util.List;

import com.gargattarapa.proyecto.practica1.models.entity.Proyecto;

public interface IProyectoService {

    List<Proyecto> findAll();

    Proyecto findById(Long id);

    Proyecto findByCodigoProyecto(String codigoProyecto);

    Proyecto save(Proyecto proyecto);

    Proyecto update(Long id, Proyecto proyecto);

    void delete(Long id);

    List<Proyecto> findByEstadoProyecto(String estadoProyecto);

    List<Proyecto> findByNombreProyecto(String nombreProyecto);

    List<Proyecto> findByCategoria(String categoria);

    List<Proyecto> findByUsuarioCreadorId(Long idUsuario);

    List<Proyecto> findByEstadoAndUsuarioCreador(String estado, Long idUsuario);

    boolean existsByCodigoProyecto(String codigoProyecto);

    Long contarProyectosPorEstado(String estado);

    List<Proyecto> busquedaAvanzada(String codigo, String nombre, String estado, String categoria);
}