package com.gargattarapa.proyecto.practica.models.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.gargattarapa.proyecto.practica.models.entity.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByCorreo(String correo);
    boolean existsByCorreo(String correo);
    List<Usuario> findByEstado(String estado);
List<Usuario> findByNombreContainingIgnoreCase(String nombre);
    @Query("SELECT u FROM Usuario u WHERE u.estado = 'activo'")
    List<Usuario> findAllActivos();
   
}
