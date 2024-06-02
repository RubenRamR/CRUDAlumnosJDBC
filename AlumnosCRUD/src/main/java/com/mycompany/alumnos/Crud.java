///*
// * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
// * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
// */
//package com.mycompany.alumnos;
//
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.sql.Statement;
//
///**
// *
// * @author rramirez
// */
//public class Crud {
//
//    // Constantes para la conexión a la base de datos
//    final String SERVER = "localhost"; // Servidor de la base de datos
//    final String BASE_DATOS = "activistasecologicos"; // Nombre de la base de datos
//    final String CADENA_CONEXION = "jdbc:mysql://" + SERVER + "/" + BASE_DATOS; // URL de conexión a la base de datos
//    final String USUARIO = "root"; // Usuario de la base de datos
//    final String CONTRASEÑA = "M"; // Contraseña del usuario de la base de datos
//
//    public void insertar() {
//        try {
//
//            // Establecer la conexión a la base de datos
//            Connection conexion = DriverManager.getConnection(CADENA_CONEXION, USUARIO, CONTRASEÑA);
//
//            // Sentencia SQL para insertar un nuevo alumno en la tabla 'alumnos'
//            String sentenciaSql = "INSERT INTO alumnos (nombre, apellidoPeterno, apellidoMaterno) VALUES (?, ?, ?);";
//            // Preparar la sentencia SQL, permitiendo obtener las claves generadas automáticamente
//            PreparedStatement preparedStatement = conexion.prepareStatement(sentenciaSql, Statement.RETURN_GENERATED_KEYS);
//            // Establecer los valores para los parámetros de la sentencia SQL
//            preparedStatement.setString(1, "Carlos");
//            preparedStatement.setString(2, "Rios");
//            preparedStatement.setString(3, "Otro");
//
//            // Ejecutar la sentencia SQL de inserción
//            preparedStatement.executeUpdate();
//
//            // Obtener las claves generadas automáticamente (por ejemplo, el ID del nuevo registro)
//            ResultSet resultado = preparedStatement.getGeneratedKeys();
//            while (resultado.next()) {
//                // Imprimir el ID generado para el nuevo registro
//                System.out.println(resultado.getInt(1));
//            }
//        } catch (SQLException ex) {
//            // Capturar y manejar cualquier excepción SQL que ocurra
//            System.out.println("Ocurrio un error  " + ex.getMessage());
//        }
//    }
//    
//    public void buscar() {
//        try
//        {
//            // Establecer la conexión a la base de datos
//            Connection conexion = DriverManager.getConnection(CADENA_CONEXION, USUARIO, CONTRASEÑA);
//
//            // Sentencia SQL para seleccionar un alumno por su id
//            String sentenciaSql = "SELECT * FROM alumnos WHERE idAlumno = ?;";
//            // Preparar la sentencia SQL
//            PreparedStatement preparedStatement = conexion.prepareStatement(sentenciaSql);
//            // Establecer los valores para los parámetros de la sentencia SQL
//            preparedStatement.setInt(1, 1);
//
//            // Ejecutar la sentencia SQL y obtener el resultado
//            ResultSet resultado = preparedStatement.executeQuery();
//
//            // Procesar el resultado
//            while (resultado.next())
//            {
//                int idAlumno = resultado.getInt("idAlumno");
//                String nombres = resultado.getString("nombre");
//                String apellidoPaterno = resultado.getString("apellidoPeterno");
//                String apellidoMaterno = resultado.getString("apellidoMaterno");
//                boolean eliminado = resultado.getBoolean("eliminado");
//                boolean activo = resultado.getBoolean("activo");
//
//                System.out.println("ID del Alumno: " + idAlumno);
//                System.out.println("Nombres: " + nombres);
//                System.out.println("Apellido Paterno: " + apellidoPaterno);
//                System.out.println("Apellido Materno: " + apellidoMaterno);
//                System.out.println("Eliminado: " + eliminado);
//                System.out.println("Activo: " + activo);
//            }
//        } catch (SQLException ex)
//        {
//            // Capturar y manejar cualquier excepción SQL que ocurra
//            System.out.println("Ocurrio un error  " + ex.getMessage());
//        }
//
//}
//    public static void main(String[] args) {
//        Crud m = new Crud();
//        m.insertar();
//        m.buscar();
//    }
//}
