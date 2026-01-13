package com.gargattarapa.proyecto.practica1.models.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.gargattarapa.proyecto.practica1.models.entity.Proyecto;

@Repository
public interface ProyectoRepository extends JpaRepository<Proyecto, Long> {

    Optional<Proyecto> findByCodigoProyecto(String codigoProyecto);

    boolean existsByCodigoProyecto(String codigoProyecto);

    List<Proyecto> findByEstadoProyecto(String estadoProyecto);

    List<Proyecto> findByNombreProyectoContainingIgnoreCase(String nombreProyecto);

    List<Proyecto> findByCategoria(String categoria);

    List<Proyecto> findByIdUsuarioCreador(Long idUsuarioCreador);

    @Query("SELECT p FROM Proyecto p WHERE p.estadoProyecto = :estado AND p.idUsuarioCreador = :idUsuario")
    List<Proyecto> findByEstadoAndUsuarioCreador(@Param("estado") String estado, 
                                                   @Param("idUsuario") Long idUsuario);

    @Query("SELECT COUNT(p) FROM Proyecto p WHERE p.estadoProyecto = :estado")
    Long contarProyectosPorEstado(@Param("estado") String estado);

    
    @Query("SELECT p FROM Proyecto p WHERE " +
           "(:codigo IS NULL OR p.codigoProyecto LIKE %:codigo%) AND " +
           "(:nombre IS NULL OR LOWER(p.nombreProyecto) LIKE LOWER(CONCAT('%', :nombre, '%'))) AND " +
           "(:estado IS NULL OR p.estadoProyecto = :estado) AND " +
           "(:categoria IS NULL OR p.categoria = :categoria)")
    List<Proyecto> busquedaAvanzada(@Param("codigo") String codigo,
                                     @Param("nombre") String nombre,
                                     @Param("estado") String estado,
                                     @Param("categoria") String categoria);
}