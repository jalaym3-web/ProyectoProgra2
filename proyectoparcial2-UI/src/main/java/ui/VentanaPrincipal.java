package ui;

import modelo.Cita;
import modelo.Cita.EstadoCita;
import service.CitaService;
 
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class VentanaPrincipal extends JFrame {

    /**
	 * 
	 */

    private final CitaService citaService = new CitaService();
    private static final DateTimeFormatter FORMATO_FECHA =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
 
    private JTable tablaCitas;
    private DefaultTableModel modeloTabla;
 
    private JTextField txtNombre;
    private JTextField txtFechaHora;
    private JTextField txtDescripcion;
    private JTextField txtDuracion;
    private JComboBox<EstadoCita> cmbEstado;
 
    private JButton btnCrear;
    private JButton btnActualizar;
    private JButton btnEliminar;
    private JButton btnLimpiar;
 
    private int idSeleccionado = -1; //"ninguna cita seleccionada"
 
    private static final String NOMBRE_NEGOCIO = "Sanatorio Los Jardines";
 
    public VentanaPrincipal() {
        super(NOMBRE_NEGOCIO + " - Gestión de Citas");
        construirInterfaz();
        cargarCitas();
 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 500);
        setLocationRelativeTo(null);
    }
 
    private void construirInterfaz() {
        setLayout(new BorderLayout(10, 10));
 
        //Encabezado
        JLabel lblEncabezado = new JLabel(NOMBRE_NEGOCIO, SwingConstants.CENTER);
        lblEncabezado.setFont(new Font("SansSerif", Font.BOLD, 20));
        lblEncabezado.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(lblEncabezado, BorderLayout.NORTH);
 
        //Panel central
        JPanel panelCentral = new JPanel(new BorderLayout(10, 10));
        add(panelCentral, BorderLayout.CENTER);
 
        //Tabla
        modeloTabla = new DefaultTableModel(
                new Object[]{"ID", "Nombre", "Fecha y hora", "Descripción", "Duración (min)", "Estado"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };
        tablaCitas = new JTable(modeloTabla);
        tablaCitas.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                cargarSeleccionEnFormulario();
            }
        });
        panelCentral.add(new JScrollPane(tablaCitas), BorderLayout.CENTER);
 
        // Formulario
        JPanel panelFormulario = new JPanel(new GridLayout(5, 2, 5, 5));
        panelFormulario.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
 
        txtNombre = new JTextField();
        txtFechaHora = new JTextField();
        txtDescripcion = new JTextField();
        txtDuracion = new JTextField();
        cmbEstado = new JComboBox<>(EstadoCita.values());
 
        panelFormulario.add(new JLabel("Nombre:"));
        panelFormulario.add(txtNombre);
 
        panelFormulario.add(new JLabel("Fecha y hora (yyyy-MM-dd HH:mm):"));
        panelFormulario.add(txtFechaHora);
 
        panelFormulario.add(new JLabel("Descripción:"));
        panelFormulario.add(txtDescripcion);
 
        panelFormulario.add(new JLabel("Duración (min):"));
        panelFormulario.add(txtDuracion);
 
        panelFormulario.add(new JLabel("Estado:"));
        panelFormulario.add(cmbEstado);
 
        panelCentral.add(panelFormulario, BorderLayout.NORTH);
 
        //Botones
        JPanel panelBotones = new JPanel();
 
        btnCrear = new JButton("Crear");
        btnActualizar = new JButton("Actualizar");
        btnEliminar = new JButton("Eliminar");
        btnLimpiar = new JButton("Limpiar / Nueva");
 
        btnCrear.addActionListener(e -> crearCita());
        btnActualizar.addActionListener(e -> actualizarCita());
        btnEliminar.addActionListener(e -> eliminarCita());
        btnLimpiar.addActionListener(e -> limpiarFormulario());
 
        panelBotones.add(btnCrear);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnLimpiar);
 
        add(panelBotones, BorderLayout.SOUTH);
    }
 
    //Cargar datos
 
    private void cargarCitas() {
        try {
            List<Cita> citas = citaService.listarTodas();
            modeloTabla.setRowCount(0); // limpia la tabla antes de recargar
 
            for (Cita cita : citas) {
                modeloTabla.addRow(new Object[]{
                        cita.getId(),
                        cita.getNombre(),
                        cita.getFechaHora().format(FORMATO_FECHA),
                        cita.getDescripcion(),
                        cita.getDuracionMin(),
                        cita.getEstado()
                });
            }
        } catch (SQLException ex) {
            mostrarError("Error al cargar las citas: " + ex.getMessage());
        }
    }
 
    private void cargarSeleccionEnFormulario() {
        int filaSeleccionada = tablaCitas.getSelectedRow();
        if (filaSeleccionada == -1) {
            return;
        }
 
        idSeleccionado = (int) modeloTabla.getValueAt(filaSeleccionada, 0);
        txtNombre.setText((String) modeloTabla.getValueAt(filaSeleccionada, 1));
        txtFechaHora.setText((String) modeloTabla.getValueAt(filaSeleccionada, 2));
        txtDescripcion.setText((String) modeloTabla.getValueAt(filaSeleccionada, 3));
        txtDuracion.setText(String.valueOf(modeloTabla.getValueAt(filaSeleccionada, 4)));
        cmbEstado.setSelectedItem(modeloTabla.getValueAt(filaSeleccionada, 5));
    }
 
    //Operaciones CRUD
 
    private void crearCita() {
        try {
            Cita cita = leerFormulario();
            citaService.crear(cita);
            JOptionPane.showMessageDialog(this, "Cita creada correctamente.");
            limpiarFormulario();
            cargarCitas();
        } catch (IllegalArgumentException ex) {
            mostrarError(ex.getMessage());
        } catch (SQLException ex) {
            mostrarError("Error al crear la cita: " + ex.getMessage());
        }
    }
 
    private void actualizarCita() {
        if (idSeleccionado == -1) {
            mostrarError("Selecciona una cita de la tabla para actualizar.");
            return;
        }
        try {
            Cita cita = leerFormulario();
            cita.setId(idSeleccionado);
            citaService.actualizar(cita);
            JOptionPane.showMessageDialog(this, "Cita actualizada correctamente.");
            limpiarFormulario();
            cargarCitas();
        } catch (IllegalArgumentException ex) {
            mostrarError(ex.getMessage());
        } catch (SQLException ex) {
            mostrarError("Error al actualizar la cita: " + ex.getMessage());
        }
    }
 
    private void eliminarCita() {
        if (idSeleccionado == -1) {
            mostrarError("Selecciona una cita de la tabla para eliminar.");
            return;
        }
 
        int confirmacion = JOptionPane.showConfirmDialog(
                this,
                "¿Seguro que deseas eliminar la cita seleccionada?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
 
        if (confirmacion != JOptionPane.YES_OPTION) {
            return;
        }
 
        try {
            citaService.eliminar(idSeleccionado);
            JOptionPane.showMessageDialog(this, "Cita eliminada correctamente.");
            limpiarFormulario();
            cargarCitas();
        } catch (SQLException ex) {
            mostrarError("Error al eliminar la cita: " + ex.getMessage());
        }
    }
 
    // utilidades
    private Cita leerFormulario() {
        Cita cita = new Cita();
        cita.setNombre(txtNombre.getText().trim());
        cita.setDescripcion(txtDescripcion.getText().trim());
        cita.setEstado((EstadoCita) cmbEstado.getSelectedItem());
 
        try {
            cita.setFechaHora(LocalDateTime.parse(txtFechaHora.getText().trim(), FORMATO_FECHA));
        } catch (DateTimeParseException ex) {
            throw new IllegalArgumentException("La fecha y hora debe tener el formato yyyy-MM-dd HH:mm");
        }
 
        try {
            cita.setDuracionMin(Integer.parseInt(txtDuracion.getText().trim()));
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("La duración debe ser un número entero.");
        }
 
        return cita;
    }
 
    private void limpiarFormulario() {
        idSeleccionado = -1;
        txtNombre.setText("");
        txtFechaHora.setText("");
        txtDescripcion.setText("");
        txtDuracion.setText("");
        cmbEstado.setSelectedIndex(0);
        tablaCitas.clearSelection();
    }
 
    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
