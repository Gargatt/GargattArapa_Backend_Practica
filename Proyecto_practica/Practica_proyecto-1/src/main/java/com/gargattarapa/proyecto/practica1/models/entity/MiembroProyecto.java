package com.gargattarapa.proyecto.practica1.models.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
@Table(name = "miembros_proyecto")
public class MiembroProyecto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idMiembroProyecto;

    
    @Column(name = "id_proyecto", nullable = false)
    private Long idProyecto;

  
    @Column(name = "id_usuario", nullable = false)
    private Long idUsuario;

    @Column(nullable = false, length = 50)
    private String rol; 

    @Column(nullable = false)
    private LocalDateTime fechaIngreso;

    private LocalDateTime fechaSalida;

    @Column(nullable = false, length = 20)
    private String estado = "activo"; 

 
    @PrePersist
    public void prePersist() {
        this.fechaIngreso = LocalDateTime.now();
        if (this.estado == null) {
            this.estado = "activo";
        }
        if (this.rol == null) {
            this.rol = "MIEMBRO";
        }
    }

  
    public MiembroProyecto() {
    }

    public MiembroProyecto(Long idProyecto, Long idUsuario, String rol) {
        this.idProyecto = idProyecto;
        this.idUsuario = idUsuario;
        this.rol = rol;
    }

 
    public Long getIdMiembroProyecto() {
        return idMiembroProyecto;
    }

    public void setIdMiembroProyecto(Long idMiembroProyecto) {
        this.idMiembroProyecto = idMiembroProyecto;
    }

    public Long getIdProyecto() {
        return idProyecto;
    }

    public void setIdProyecto(Long idProyecto) {
        this.idProyecto = idProyecto;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public LocalDateTime getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(LocalDateTime fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public LocalDateTime getFechaSalida() {
        return fechaSalida;
    }

    public void setFechaSalida(LocalDateTime fechaSalida) {
        this.fechaSalida = fechaSalida;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}