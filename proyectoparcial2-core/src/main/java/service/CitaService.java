package service;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import dao.CitaDAO;
import modelo.Cita;
import validacion.CitaValidacion;

public class CitaService {

    private final CitaDAO citaDAO;
 
    public CitaService() {
        this.citaDAO = new CitaDAO();
    }
 
    // Constructor alterno
    public CitaService(CitaDAO citaDAO) {
        this.citaDAO = citaDAO;
    }
 
    public int crear(Cita cita) throws SQLException {
        CitaValidacion.validar(cita);
        return citaDAO.crear(cita);
    }
 
    public Optional<Cita> buscarPorId(int id) throws SQLException {
        return citaDAO.buscarPorId(id);
    }
 
    public List<Cita> listarTodas() throws SQLException {
        return citaDAO.listarTodas();
    }
 
    public boolean actualizar(Cita cita) throws SQLException {
        CitaValidacion.validar(cita);
        return citaDAO.actualizar(cita);
    }
 
    public boolean eliminar(int id) throws SQLException {
        return citaDAO.eliminar(id);
    }
}
