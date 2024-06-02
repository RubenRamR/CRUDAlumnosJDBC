/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package persistencia;

import dtos.EditarAlumnoDTO;
import entidad.AlumnoEntidad;
import java.util.List;

/**
 *
 * @author rramirez
 */
public interface IAlumnoDAO {

    public List<AlumnoEntidad> buscarAlumnosTabla() throws PersistenciaException;
    
    public AlumnoEntidad obtenerAlumnoPorId(int id)throws PersistenciaException;

    public void agregarAlumno(AlumnoEntidad alumno) throws PersistenciaException;
    
    public void editarAlumno(AlumnoEntidad alumno) throws PersistenciaException;
    
    public void eliminarAlumnoPorId(int id) throws PersistenciaException;

}
