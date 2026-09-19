package modelo;


import java.time.LocalDateTime;

public class Cita {
	
	public enum EstadoCita {
		PENDIENTE,
	    CONFIRMADA,
	    CANCELADA
	}
		private int id;
		private String nombre;
		private LocalDateTime fechaHora;
		private String descripcion;
		private int duracionMin;
		private EstadoCita estado;
		
		public Cita(int id, String nombre, LocalDateTime fechaHora, String descripcion, int duracionMin,
				EstadoCita estado) {
			this.id = id;
			this.nombre = nombre;
			this.fechaHora = fechaHora;
			this.descripcion = descripcion;
			this.duracionMin = duracionMin;
			this.estado = estado;
		}
		
		public Cita() {
		}
		
		public Cita(String nombre, LocalDateTime fechaHora, String descripcion, int duracionMin,
				EstadoCita estado) {
			this(0, nombre, fechaHora, descripcion, duracionMin, estado);
		}


		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public String getNombre() {
			return nombre;
		}

		public void setNombre(String nombre) {
			if (nombre == null || nombre.trim().isEmpty()) {
		        throw new IllegalArgumentException("El paciente no puede estar vacío");
		    }
		    this.nombre = nombre;
		}

		public LocalDateTime getFechaHora() {
			return fechaHora;
		}

		public void setFechaHora(LocalDateTime fechaHora) {
			this.fechaHora = fechaHora;
		}

		public String getDescripcion() {
			return descripcion;
		}

		public void setDescripcion(String descripcion) {
			this.descripcion = descripcion;
		}

		public int getDuracionMin() {
			return duracionMin;
		}

		public void setDuracionMin(int duracionMin) {
			this.duracionMin = duracionMin;
		}

		public EstadoCita getEstado() {
			return estado;
		}

		public void setEstado(EstadoCita estado) {
			this.estado = estado;
		}
		
		
		
		
}


