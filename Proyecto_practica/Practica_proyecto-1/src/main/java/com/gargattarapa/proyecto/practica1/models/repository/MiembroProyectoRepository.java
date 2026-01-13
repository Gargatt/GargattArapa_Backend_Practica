package com.gargattarapa.proyecto.practica1.models.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.gargattarapa.proyecto.practica1.models.entity.MiembroProyecto;

@Repository
public interface MiembroProyectoRepository extends JpaRepository<MiembroProyecto, Long> {

    List<MiembroProyecto> findByIdProyecto(Long idProyecto);

    List<MiembroProyecto> findByIdUsuario(Long idUsuario);

    @Query("SELECT m FROM MiembroProyecto m WHERE m.idProyecto = :idProyecto AND m.estado = 'activo'")
    List<MiembroProyecto> findMiembrosActivosPorProyecto(@Param("idProyecto") Long idProyecto);

    @Query("SELECT m FROM MiembroProyecto m WHERE m.idUsuario = :idUsuario AND m.estado = 'activo'")
    List<MiembroProyecto> findProyectosActivosPorUsuario(@Param("idUsuario") Long idUsuario);

    boolean existsByIdProyectoAndIdUsuario(Long idProyecto, Long idUsuario);

    Optional<MiembroProyecto> findByIdProyectoAndIdUsuario(Long idProyecto, Long idUsuario);

    @Query("SELECT m FROM MiembroProyecto m WHERE m.idProyecto = :idProyecto AND m.rol = :rol")
    List<MiembroProyecto> findByProyectoAndRol(@Param("idProyecto") Long idProyecto, 
                                                 @Param("rol") String rol);

    @Query("SELECT COUNT(m) FROM MiembroProyecto m WHERE m.idProyecto = :idProyecto AND m.estado = 'activo'")
    Long contarMiembrosActivos(@Param("idProyecto") Long idProyecto);

    @Query("SELECT m FROM MiembroProyecto m WHERE m.idProyecto = :idProyecto AND m.rol = 'OWNER' AND m.estado = 'activo'")
    List<MiembroProyecto> findOwnersPorProyecto(@Param("idProyecto") Long idProyecto);
}