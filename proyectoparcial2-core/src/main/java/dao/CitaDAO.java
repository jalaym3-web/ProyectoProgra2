package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;
import java.util.Optional;

import modelo.Cita;
import modelo.Cita.EstadoCita;
import validacion.CitaValidacion;

public class CitaDAO {

	private static final String URL = "jdbc:mysql://localhost:3306/prog2_db?useSSL=false&serverTimezone=UTC";
    private static final String USUARIO = "root";
    private static final String PASSWORD = "josh1304";
    
    // CREATE
    public int crear(Cita cita) throws SQLException {
    	CitaValidacion.validar(cita);
        String sql = "INSERT INTO citas (nombre, fecha_hora, descripcion, duracion_min, estado) "
                   + "VALUES (?, ?, ?, ?, ?)";
 
        try (Connection conexion = DriverManager.getConnection(URL, USUARIO, PASSWORD);
             PreparedStatement statement = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
 
            statement.setString(1, cita.getNombre());
            statement.setTimestamp(2, Timestamp.valueOf(cita.getFechaHora()));
            statement.setString(3, cita.getDescripcion());
            statement.setInt(4, cita.getDuracionMin());
            statement.setString(5, cita.getEstado().name().toLowerCase());
            statement.executeUpdate();
 
            try (ResultSet claves = statement.getGeneratedKeys()) {
                if (claves.next()) {
                    return claves.getInt(1);
                }
                return -1;
            }
        }
    }
 
 
    // READ todos
    public List<Cita> listarTodas() throws SQLException {
        String sql = "SELECT * FROM citas ORDER BY fecha_hora";
        List<Cita> citas = new ArrayList<>();
 
        try (Connection conexion = DriverManager.getConnection(URL, USUARIO, PASSWORD);
             PreparedStatement statement = conexion.prepareStatement(sql);
             ResultSet resultado = statement.executeQuery()) {
 
            while (resultado.next()) {
                citas.add(mapearCita(resultado));
            }
        }
        return citas;
    }
    
    // READ - buscar por id
    public Optional<Cita> buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM citas WHERE id = ?";

        try (Connection conexion = DriverManager.getConnection(URL, USUARIO, PASSWORD);
             PreparedStatement statement = conexion.prepareStatement(sql)) {

            statement.setInt(1, id);

            try (ResultSet resultado = statement.executeQuery()) {
                if (resultado.next()) {
                    return Optional.of(mapearCita(resultado));
                }
                return Optional.empty();
            }
        }
    }
 
    // UPDATE
    public boolean actualizar(Cita cita) throws SQLException {
        String sql = "UPDATE citas SET nombre = ?, fecha_hora = ?, descripcion = ?, "
                   + "duracion_min = ?, estado = ? WHERE id = ?";
 
        try (Connection conexion = DriverManager.getConnection(URL, USUARIO, PASSWORD);
             PreparedStatement statement = conexion.prepareStatement(sql)) {
 
            statement.setString(1, cita.getNombre());
            statement.setTimestamp(2, Timestamp.valueOf(cita.getFechaHora()));
            statement.setString(3, cita.getDescripcion());
            statement.setInt(4, cita.getDuracionMin());
            statement.setString(5, cita.getEstado().name().toLowerCase());
            statement.setInt(6, cita.getId());
 
            int filasAfectadas = statement.executeUpdate();
            return filasAfectadas > 0;
        }
    }
 
    // DELETE
    public boolean eliminar(int id) throws SQLException {
        String sql = "DELETE FROM citas WHERE id = ?";
 
        try (Connection conexion = DriverManager.getConnection(URL, USUARIO, PASSWORD);
             PreparedStatement statement = conexion.prepareStatement(sql)) {
 
            statement.setInt(1, id);
            int filasAfectadas = statement.executeUpdate();
            return filasAfectadas > 0;
        }
    }
 
    // Mapeo
    private Cita mapearCita(ResultSet resultado) throws SQLException {
        Cita cita = new Cita();
        cita.setId(resultado.getInt("id"));
        cita.setNombre(resultado.getString("nombre"));
 
        Timestamp timestamp = resultado.getTimestamp("fecha_hora");
        LocalDateTime fechaHora = (timestamp != null) ? timestamp.toLocalDateTime() : null;
        cita.setFechaHora(fechaHora);
 
        cita.setDescripcion(resultado.getString("descripcion"));
        cita.setDuracionMin(resultado.getInt("duracion_min"));
        cita.setEstado(EstadoCita.valueOf(resultado.getString("estado").toUpperCase()));
 
        return cita;
    }
}