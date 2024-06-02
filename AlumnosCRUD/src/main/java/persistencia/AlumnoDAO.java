/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistencia;

import entidad.AlumnoEntidad;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import negocio.AlumnoNegocio;

/**
 *
 * @author rramirez
 */
public class AlumnoDAO implements IAlumnoDAO {

    private IConexionBD conexionBD;

    private static final Logger LOGGER = Logger.getLogger(AlumnoNegocio.class.getName());

    public AlumnoDAO(IConexionBD conexionBD) {
        this.conexionBD = conexionBD;
    }

    @Override
    public List<AlumnoEntidad> buscarAlumnosTabla() throws PersistenciaException {
        try
        {
            List<AlumnoEntidad> alumnosLista = null;

            Connection conexion = this.conexionBD.crearConexion();
            String codigoSQL = "SELECT idAlumno, nombres, apellidoPaterno, apellidoMaterno, eliminado, activo FROM alumnos";
            Statement comandoSQL = conexion.createStatement();
            ResultSet resultado = comandoSQL.executeQuery(codigoSQL);
            while (resultado.next())
            {
                if (alumnosLista == null)
                {
                    alumnosLista = new ArrayList<>();
                }
                AlumnoEntidad alumno = this.convertirAEntidad(resultado);
                alumnosLista.add(alumno);
            }
            conexion.close();
            return alumnosLista;
        } catch (SQLException ex)
        {
            LOGGER.log(Level.SEVERE, "Error al buscar alumnos", ex);
            System.out.println(ex.getMessage());
            throw new PersistenciaException("Ocurrió un error al leer la base de datos, inténtelo de nuevo y si el error persiste comuníquese con el encargado del sistema.");
        }
    }

    @Override
    public AlumnoEntidad obtenerAlumnoPorId(int id) throws PersistenciaException {
        try
        {
            AlumnoEntidad alumno = null;

            Connection conexion = this.conexionBD.crearConexion();
            String codigoSQL = "SELECT * FROM alumnos WHERE idAlumno = ?";
            PreparedStatement comandoSQL = conexion.prepareStatement(codigoSQL);
            comandoSQL.setInt(1, id);

            ResultSet resultado = comandoSQL.executeQuery();

            if (resultado.next())
            {
                alumno = this.convertirAEntidad(resultado);
            }

            return alumno;

        } catch (SQLException ex)
        {

            LOGGER.log(Level.SEVERE, "Error al buscar alumno por id", ex);
            System.out.println(ex.getMessage());
            throw new PersistenciaException("Ocurrió un error al leer la base de datos, inténtelo de nuevo y si el error persiste comuníquese con el encargado del sistema.");

        }
    }

    @Override
    public void agregarAlumno(AlumnoEntidad alumno) throws PersistenciaException {
        AlumnoEntidad alumnoInsertado = null;

        try
        {
            Connection conexion = this.conexionBD.crearConexion();
            Statement comandoSQL = conexion.createStatement();

            String codigoSQL = String.format("INSERT INTO alumnos (nombres, apellidoPaterno, apellidoMaterno, eliminado, activo) VALUES ('%s', '%s', '%s', %b, %b)",
                    alumno.getNombres(),
                    alumno.getApellidoPaterno(),
                    alumno.getApellidoMaterno(),
                    alumno.isEliminado(),
                    alumno.isActivo());

            comandoSQL.executeUpdate(codigoSQL, Statement.RETURN_GENERATED_KEYS);

            ResultSet resultado = comandoSQL.getGeneratedKeys();
            if (resultado.next())
            {
                int idGenerado = resultado.getInt(1);
                alumnoInsertado = new AlumnoEntidad(idGenerado, alumno.getNombres(), alumno.getApellidoPaterno(), alumno.getApellidoMaterno(), alumno.isEliminado(), alumno.isActivo());
            }

            conexion.close();
        } catch (SQLException ex)
        {
            LOGGER.log(Level.SEVERE, "Error al agregar el alumno", ex);
            System.out.println(ex.getMessage());
            throw new PersistenciaException("Ocurrió un error al leer la base de datos, inténtelo de nuevo y si el error persiste comuníquese con el encargado del sistema.");
        }

    }

    @Override
    public void editarAlumno(AlumnoEntidad alumno) throws PersistenciaException {
        try
        {
            Connection conexion = this.conexionBD.crearConexion();
            String codigoSQL = "UPDATE alumnos SET nombres = ?, apellidoPaterno = ?, apellidoMaterno = ?, eliminado = ?, activo = ? WHERE idAlumno = ?";
            PreparedStatement comandoSQL = conexion.prepareStatement(codigoSQL);

            comandoSQL.setString(1, alumno.getNombres());
            comandoSQL.setString(2, alumno.getApellidoPaterno());
            comandoSQL.setString(3, alumno.getApellidoMaterno());
            comandoSQL.setBoolean(4, alumno.isEliminado());
            comandoSQL.setBoolean(5, alumno.isActivo());
            comandoSQL.setInt(6, alumno.getIdAlumno());

            int filasAfectadas = comandoSQL.executeUpdate();

            if (filasAfectadas == 0)
            {
                throw new PersistenciaException("No se encontró ningún alumno con el ID proporcionado para editar.");
            }

            conexion.close();
        } catch (SQLException ex)
        {
            LOGGER.log(Level.SEVERE, "Error al editar el alumno", ex);
            throw new PersistenciaException("Ocurrió un error al actualizar el alumno en la base de datos, inténtelo de nuevo y si el error persiste comuníquese con el encargado del sistema.");
        }
    }

    @Override
    public void eliminarAlumnoPorId(int id) throws PersistenciaException {
        try
        {
            Connection conexion = this.conexionBD.crearConexion();
            String codigoSQL = "DELETE FROM alumnos WHERE idAlumno = ?";
            PreparedStatement comandoSQL = conexion.prepareStatement(codigoSQL);

            comandoSQL.setInt(1, id);

            int filasAfectadas = comandoSQL.executeUpdate();

            if (filasAfectadas == 0)
            {
                throw new PersistenciaException("No se encontró ningún alumno con el ID proporcionado.");
            }

        } catch (SQLException ex)
        {
            LOGGER.log(Level.SEVERE, "Error al eliminar el alumno", ex);
            throw new PersistenciaException("Ocurrió un error al eliminar el alumno en la base de datos, inténtelo de nuevo y si el error persiste comuníquese con el encargado del sistema.");
        }
    }

    private AlumnoEntidad convertirAEntidad(ResultSet resultado) throws SQLException {
        int id = resultado.getInt("idAlumno");
        String nombre = resultado.getString("nombres");
        String paterno = resultado.getString("apellidoPaterno");
        String materno = resultado.getString("apellidoMaterno");
        boolean eliminado = resultado.getBoolean("eliminado");
        boolean activo = resultado.getBoolean("activo");
        return new AlumnoEntidad(id, nombre, paterno, materno, eliminado, activo);
    }

}
