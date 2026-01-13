package com.gargattarapa.proyecto.practica1.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gargattarapa.proyecto.practica1.models.entity.MiembroProyecto;
import com.gargattarapa.proyecto.practica1.models.repository.MiembroProyectoRepository;

@Service
public class MiembroProyectoServiceImpl implements IMiembroProyectoService {

    @Autowired
    private MiembroProyectoRepository miembroProyectoRepository;

    @Override
    @Transactional(readOnly = true)
    public List<MiembroProyecto> findAll() {
        return miembroProyectoRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public MiembroProyecto findById(Long id) {
        return miembroProyectoRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public MiembroProyecto save(MiembroProyecto miembroProyecto) {
        if (miembroProyectoRepository.existsByIdProyectoAndIdUsuario(
                miembroProyecto.getIdProyecto(), 
                miembroProyecto.getIdUsuario())) {
            throw new RuntimeException("El usuario ya es miembro de este proyecto");
        }

        return miembroProyectoRepository.save(miembroProyecto);
    }

    @Override
    @Transactional
    public MiembroProyecto update(Long id, MiembroProyecto miembroProyecto) {
        MiembroProyecto miembroExistente = findById(id);

        if (miembroExistente == null) {
            throw new RuntimeException("Miembro no encontrado con ID: " + id);
        }

        miembroExistente.setRol(miembroProyecto.getRol());
        miembroExistente.setEstado(miembroProyecto.getEstado());

        return miembroProyectoRepository.save(miembroExistente);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        MiembroProyecto miembro = findById(id);

        if (miembro == null) {
            throw new RuntimeException("Miembro no encontrado con ID: " + id);
        }

        miembro.setEstado("inactivo");
        miembro.setFechaSalida(LocalDateTime.now());
        miembroProyectoRepository.save(miembro);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MiembroProyecto> findByIdProyecto(Long idProyecto) {
        return miembroProyectoRepository.findByIdProyecto(idProyecto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MiembroProyecto> findByIdUsuario(Long idUsuario) {
        return miembroProyectoRepository.findByIdUsuario(idUsuario);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MiembroProyecto> findMiembrosActivosPorProyecto(Long idProyecto) {
        return miembroProyectoRepository.findMiembrosActivosPorProyecto(idProyecto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MiembroProyecto> findProyectosActivosPorUsuario(Long idUsuario) {
        return miembroProyectoRepository.findProyectosActivosPorUsuario(idUsuario);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByIdProyectoAndIdUsuario(Long idProyecto, Long idUsuario) {
        return miembroProyectoRepository.existsByIdProyectoAndIdUsuario(idProyecto, idUsuario);
    }

    @Override
    @Transactional(readOnly = true)
    public MiembroProyecto findByIdProyectoAndIdUsuario(Long idProyecto, Long idUsuario) {
        return miembroProyectoRepository.findByIdProyectoAndIdUsuario(idProyecto, idUsuario).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MiembroProyecto> findByProyectoAndRol(Long idProyecto, String rol) {
        return miembroProyectoRepository.findByProyectoAndRol(idProyecto, rol);
    }

    @Override
    @Transactional(readOnly = true)
    public Long contarMiembrosActivos(Long idProyecto) {
        return miembroProyectoRepository.contarMiembrosActivos(idProyecto);
    }

    @Override
    @Transactional
    public MiembroProyecto agregarMiembro(Long idProyecto, Long idUsuario, String rol) {
        if (existsByIdProyectoAndIdUsuario(idProyecto, idUsuario)) {
            throw new RuntimeException("El usuario ya es miembro de este proyecto");
        }

        if (!rol.equals("OWNER") && !rol.equals("ADMIN") && 
            !rol.equals("MIEMBRO") && !rol.equals("OBSERVADOR")) {
            throw new RuntimeException("Rol inválido. Use: OWNER, ADMIN, MIEMBRO, OBSERVADOR");
        }

        MiembroProyecto miembro = new MiembroProyecto(idProyecto, idUsuario, rol);
        return miembroProyectoRepository.save(miembro);
    }

    @Override
    @Transactional
    public void removerMiembro(Long idProyecto, Long idUsuario) {
        MiembroProyecto miembro = findByIdProyectoAndIdUsuario(idProyecto, idUsuario);

        if (miembro == null) {
            throw new RuntimeException("El usuario no es miembro de este proyecto");
        }

        if (miembro.getRol().equals("OWNER")) {
            List<MiembroProyecto> owners = miembroProyectoRepository.findOwnersPorProyecto(idProyecto);
            if (owners.size() <= 1) {
                throw new RuntimeException("No se puede remover el único OWNER del proyecto");
            }
        }

        miembro.setEstado("inactivo");
        miembro.setFechaSalida(LocalDateTime.now());
        miembroProyectoRepository.save(miembro);
    }

    @Override
    @Transactional
    public MiembroProyecto cambiarRol(Long idProyecto, Long idUsuario, String nuevoRol) {
        MiembroProyecto miembro = findByIdProyectoAndIdUsuario(idProyecto, idUsuario);

        if (miembro == null) {
            throw new RuntimeException("El usuario no es miembro de este proyecto");
        }

        if (!nuevoRol.equals("OWNER") && !nuevoRol.equals("ADMIN") && 
            !nuevoRol.equals("MIEMBRO") && !nuevoRol.equals("OBSERVADOR")) {
            throw new RuntimeException("Rol inválido. Use: OWNER, ADMIN, MIEMBRO, OBSERVADOR");
        }

        if (miembro.getRol().equals("OWNER") && !nuevoRol.equals("OWNER")) {
            List<MiembroProyecto> owners = miembroProyectoRepository.findOwnersPorProyecto(idProyecto);
            if (owners.size() <= 1) {
                throw new RuntimeException("No se puede cambiar el rol del único OWNER");
            }
        }

        miembro.setRol(nuevoRol);
        return miembroProyectoRepository.save(miembro);
    }
}
