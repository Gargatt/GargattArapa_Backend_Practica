package com.gargattarapa.proyecto.practica.services;  // Tu paquete

import com.gargattarapa.proyecto.practica.models.entity.Usuario;
import com.gargattarapa.proyecto.practica.models.repository.UsuarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class UsuarioServiceImpl implements IUsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Usuario findById(Long id) {
        return usuarioRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public Usuario findByCorreo(String correo) {
        return usuarioRepository.findByCorreo(correo).orElse(null);
    }

    @Override
    @Transactional
    public Usuario save(Usuario usuario) {
        if (usuarioRepository.existsByCorreo(usuario.getCorreo())) {
            throw new RuntimeException("El correo ya está registrado");
        }
        
        
        return usuarioRepository.save(usuario);
    }

    @Override
    @Transactional
    public Usuario update(Long id, Usuario usuario) {
        Usuario usuarioExistente = findById(id);
        
        if (usuarioExistente == null) {
            throw new RuntimeException("Usuario no encontrado con ID: " + id);
        }

        usuarioExistente.setNombre(usuario.getNombre());
        usuarioExistente.setApellidos(usuario.getApellidos());
        usuarioExistente.setTelefono(usuario.getTelefono());
        usuarioExistente.setEmpresa(usuario.getEmpresa());
        usuarioExistente.setCargo(usuario.getCargo());
        usuarioExistente.setFoto(usuario.getFoto());
        
        if (!usuarioExistente.getCorreo().equals(usuario.getCorreo())) {
            if (usuarioRepository.existsByCorreo(usuario.getCorreo())) {
                throw new RuntimeException("El correo ya está registrado");
            }
            usuarioExistente.setCorreo(usuario.getCorreo());
        }

        return usuarioRepository.save(usuarioExistente);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Usuario usuario = findById(id);
        
        if (usuario == null) {
            throw new RuntimeException("Usuario no encontrado con ID: " + id);
        }
        
        usuario.setEstado("inactivo");
        usuarioRepository.save(usuario);
    }

    @Override
    @Transactional
    public void deletePhysical(Long id) {
        Usuario usuario = findById(id);
        
        if (usuario == null) {
            throw new RuntimeException("Usuario no encontrado con ID: " + id);
        }
        
        
        usuarioRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> findAllActivos() {
        return usuarioRepository.findAllActivos();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> findByNombre(String nombre) {
        return usuarioRepository.findByNombreContainingIgnoreCase(nombre);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByCorreo(String correo) {
        return usuarioRepository.existsByCorreo(correo);
    }

    @Override
    @Transactional(readOnly = true)
    public Usuario login(String correo, String password) {
        Usuario usuario = findByCorreo(correo);
        
        if (usuario == null) {
            return null; 
        }
        
        if (!usuario.getPassword().equals(password)) {
            return null; 
        }
        
        if (!"activo".equals(usuario.getEstado())) {
            throw new RuntimeException("Usuario inactivo");
        }
        
        return usuario;
    }

    @Override
    @Transactional
    public boolean changePassword(Long id, String oldPassword, String newPassword) {
        Usuario usuario = findById(id);
        
        if (usuario == null) {
            return false;
        }
        
        if (!usuario.getPassword().equals(oldPassword)) {
            return false; 
        }
        
        usuario.setPassword(newPassword);
        
        usuarioRepository.save(usuario);
        return true;
    }
}