package com.gargattarapa.proyecto.practica1.services;

import java.util.List;

import com.gargattarapa.proyecto.practica1.models.entity.MiembroProyecto;

public interface IMiembroProyectoService {

    List<MiembroProyecto> findAll();

    MiembroProyecto findById(Long id);

    MiembroProyecto save(MiembroProyecto miembroProyecto);

    MiembroProyecto update(Long id, MiembroProyecto miembroProyecto);

    void delete(Long id);

    List<MiembroProyecto> findByIdProyecto(Long idProyecto);

    List<MiembroProyecto> findByIdUsuario(Long idUsuario);

    List<MiembroProyecto> findMiembrosActivosPorProyecto(Long idProyecto);

    List<MiembroProyecto> findProyectosActivosPorUsuario(Long idUsuario);

    boolean existsByIdProyectoAndIdUsuario(Long idProyecto, Long idUsuario);

    MiembroProyecto findByIdProyectoAndIdUsuario(Long idProyecto, Long idUsuario);

    List<MiembroProyecto> findByProyectoAndRol(Long idProyecto, String rol);

    Long contarMiembrosActivos(Long idProyecto);

    MiembroProyecto agregarMiembro(Long idProyecto, Long idUsuario, String rol);

    void removerMiembro(Long idProyecto, Long idUsuario);

    MiembroProyecto cambiarRol(Long idProyecto, Long idUsuario, String nuevoRol);
}