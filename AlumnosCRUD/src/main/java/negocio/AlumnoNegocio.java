/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package negocio;

import dtos.AlumnoTablaDTO;
import dtos.EditarAlumnoDTO;
import dtos.GuardarAlumnoDTO;
import entidad.AlumnoEntidad;
import java.util.ArrayList;
import java.util.List;
import persistencia.IAlumnoDAO;
import persistencia.PersistenciaException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author rramirez
 */
public class AlumnoNegocio implements IAlumnoNegocio {
    
    private IAlumnoDAO alumnoDAO;
    
    private static final Logger LOGGER = Logger.getLogger(AlumnoNegocio.class.getName());
    
    public AlumnoNegocio(IAlumnoDAO alumnoDAO) {
        this.alumnoDAO = alumnoDAO;
    }
    
    private List<AlumnoTablaDTO> convertirAlumnoTablaDTO(List<AlumnoEntidad> alumnos) throws NegocioException {
        if (alumnos == null)
        {
            throw new NegocioException("No se pudieron obtener los alumnos");
        }
        
        List<AlumnoTablaDTO> alumnosDTO = new ArrayList<>();
        for (AlumnoEntidad alumno : alumnos)
        {
            AlumnoTablaDTO dto = new AlumnoTablaDTO();
            dto.setIdAlumno(alumno.getIdAlumno());
            dto.setNombres(alumno.getNombres());
            dto.setApellidoPaterno(alumno.getApellidoPaterno());
            dto.setApellidoMaterno(alumno.getApellidoMaterno());
            dto.setEstatus(alumno.isActivo() == true ? "Activo" : "Inactivo");
            alumnosDTO.add(dto);
        }
        return alumnosDTO;
    }
    
    private GuardarAlumnoDTO convertirGuardarAlumnoDTO(AlumnoEntidad alumno) throws NegocioException {
        if (alumno == null)
        {
            throw new NegocioException("No se pudo obtener el alumno");
        }
        
        GuardarAlumnoDTO alumnoDTO = new GuardarAlumnoDTO();
        
        alumnoDTO.setIdAlumno(alumno.getIdAlumno());
        alumnoDTO.setNombres(alumno.getNombres());
        alumnoDTO.setApellidoPaterno(alumno.getApellidoPaterno());
        alumnoDTO.setApellidoMaterno(alumno.getApellidoMaterno());
        alumnoDTO.setEstatus(alumno.isActivo() == true ? "Activo" : "Inactivo");
        
        return alumnoDTO;
        
    }
    
    @Override
    public GuardarAlumnoDTO obtenerAlumnoPorId(int id) throws NegocioException {
        try
        {
            AlumnoEntidad entidad = this.alumnoDAO.obtenerAlumnoPorId(id);
            return this.convertirGuardarAlumnoDTO(entidad);
        } catch (PersistenciaException ex)
        {
            LOGGER.log(Level.SEVERE, "Error al buscar alumno por id", ex);
            System.out.println(ex.getMessage());
            throw new NegocioException(ex.getMessage());
        }
    }
    
    @Override
    public List<AlumnoTablaDTO> buscarAlumnosTabla() throws NegocioException {
        try
        {
            List<AlumnoEntidad> alumnos = this.alumnoDAO.buscarAlumnosTabla();
            return this.convertirAlumnoTablaDTO(alumnos);
        } catch (PersistenciaException ex)
        {
            LOGGER.log(Level.SEVERE, "Error al buscar alumnos", ex);
            System.out.println(ex.getMessage());
            throw new NegocioException(ex.getMessage());
        }
    }
    
    @Override
    public void agregarAlumno(GuardarAlumnoDTO alumno) throws NegocioException {
        try
        {
            AlumnoEntidad entidad = new AlumnoEntidad(0,
                    alumno.getNombres(),
                    alumno.getApellidoPaterno(),
                    alumno.getApellidoMaterno(),
                    false,
                    "Activo".equalsIgnoreCase(alumno.getEstatus())
            );
            
            this.alumnoDAO.agregarAlumno(entidad);
        } catch (PersistenciaException ex)
        {
            LOGGER.log(Level.SEVERE, "Error al agregar el alumno", ex);
            System.out.println(ex.getMessage());
            throw new NegocioException(ex.getMessage());
        }
    }
    
    @Override
    public void editarAlumno(EditarAlumnoDTO alumno) throws NegocioException {
        try
        {
            AlumnoEntidad entidad = new AlumnoEntidad(
                    alumno.getIdAlumno(),
                    alumno.getNombres(),
                    alumno.getApellidoPaterno(),
                    alumno.getApellidoMaterno(),
                    false,
                    "Activo".equalsIgnoreCase(alumno.getEstatus())
            );
            this.alumnoDAO.editarAlumno(entidad);
        } catch (PersistenciaException ex)
        {
            LOGGER.log(Level.SEVERE, "Error al editar el alumno", ex);
            System.out.println(ex.getMessage());
            throw new NegocioException(ex.getMessage());
        }
    }
    
    @Override
    public void eliminarAlumnoPorId(int id) throws NegocioException {
        try
        {
            this.alumnoDAO.eliminarAlumnoPorId(id);
        } catch (PersistenciaException ex)
        {
            LOGGER.log(Level.SEVERE, "Error al eliminar el alumno", ex);
            System.out.println(ex.getMessage());
            throw new NegocioException(ex.getMessage());
        }
    }
    
}
