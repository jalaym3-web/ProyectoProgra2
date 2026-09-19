
CREATE DATABASE IF NOT EXISTS prog2_db;

USE prog2_db;

CREATE TABLE IF NOT EXISTS citas (
    id        		INT AUTO_INCREMENT PRIMARY KEY,
    nombre    		VARCHAR(100)   NOT NULL,
    fecha_hora 		DATETIME,
    descripcion  	VARCHAR(100)  NOT NULL,
    duracion_min    INT           NOT NULL DEFAULT 0,
	estado ENUM('pendiente', 'confirmada', 'cancelada') NOT NULL DEFAULT 'pendiente'  
);

INSERT IGNORE INTO citas (id, nombre, fecha_hora, descripcion, duracion_min, estado) VALUES
(1, "Miguel Benitez", '2026-10-01 14:00:00', "visita anual", 60, "pendiente"),
(2, "Marcela Mendez", '2026-10-05 11:00:00', "consulta trimestral", 60, "pendiente"),
(3, "Carlos Perez", '2026-10-07 10:30:00', "sesion de fisioterapia", 90, "confirmada");