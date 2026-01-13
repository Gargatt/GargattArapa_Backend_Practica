package com.gargattarapa.proyecto.practica.services;

import java.util.List;

import com.gargattarapa.proyecto.practica.models.entity.Usuario;


public interface IUsuarioService {

    List<Usuario> findAll();

    Usuario findById(Long id);

    Usuario findByCorreo(String correo);

    Usuario save(Usuario usuario);
    
    Usuario update(Long id, Usuario usuario);

    void delete(Long id);

    void deletePhysical(Long id);

    List<Usuario> findAllActivos();

    List<Usuario> findByNombre(String nombre);

    boolean existsByCorreo(String correo);

    Usuario login(String correo, String password);

    boolean changePassword(Long id, String oldPassword, String newPassword);
}
