package org.proyectoparcial2.core;

import java.sql.SQLException;
import java.util.List;

import modelo.Cita;
import dao.CitaDAO;


public class App 
{
    public static void main( String[] args )
    {
        CitaDAO citaDAO = new CitaDAO();

        try {
            List<Cita> citas = citaDAO.listarTodas();

            if (citas.isEmpty()) {
                System.out.println("No hay citas registradas.");
            } else {
                for (Cita cita : citas) {
                    System.out.println("ID: " + cita.getId()
                            + " | Nombre: " + cita.getNombre()
                            + " | Fecha: " + cita.getFechaHora()
                            + " | Estado: " + cita.getEstado());
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al listar citas: " + e.getMessage());
        }
    }
}
