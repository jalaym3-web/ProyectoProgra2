package validacion;

import modelo.Cita;
import modelo.Cita.EstadoCita;
 
import java.time.LocalDateTime;

public class CitaValidacion {
	 // Valida cita completa
    public static void validar(Cita cita) {
 
        if (cita.getNombre() == null || cita.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del cliente no puede estar vacío.");
        }
 
        if (cita.getDescripcion() == null || cita.getDescripcion().trim().isEmpty()) {
            throw new IllegalArgumentException("La descripción del servicio no puede estar vacía.");
        }
 
        if (cita.getFechaHora() == null) {
            throw new IllegalArgumentException("Debe indicar la fecha y hora de la cita.");
        }
 
        if (cita.getFechaHora().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("La fecha y hora de la cita no puede ser en el pasado.");
        }
 
        if (cita.getDuracionMin() <= 0) {
            throw new IllegalArgumentException("La duración debe ser mayor a cero minutos.");
        }
 
        if (cita.getEstado() == null) {
            throw new IllegalArgumentException("Debe indicar el estado de la cita.");
        }
    }
 
    // Convierte un texto a EstadoCita
    public static EstadoCita parsearEstado(String texto) {
        if (texto == null) {
            throw new IllegalArgumentException("Debe indicar el estado de la cita.");
        }
        try {
            return EstadoCita.valueOf(texto.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                    "El estado debe ser uno de: PENDIENTE, CONFIRMADA o CANCELADA.");
        }
    }
}
