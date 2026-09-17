
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
