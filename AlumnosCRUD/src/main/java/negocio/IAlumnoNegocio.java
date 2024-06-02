/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package negocio;

import dtos.AlumnoTablaDTO;
import dtos.EditarAlumnoDTO;
import dtos.GuardarAlumnoDTO;
import entidad.AlumnoEntidad;
import java.util.List;

/**
 *
 * @author rramirez
 */
public interface IAlumnoNegocio {

    public List<AlumnoTablaDTO> buscarAlumnosTabla() throws NegocioException;
    
    public GuardarAlumnoDTO obtenerAlumnoPorId(int id) throws NegocioException;

    public void agregarAlumno(GuardarAlumnoDTO alumno) throws NegocioException;
    
    public void editarAlumno(EditarAlumnoDTO alumno) throws NegocioException;
    
    public void eliminarAlumnoPorId(int id) throws NegocioException;
     
}
