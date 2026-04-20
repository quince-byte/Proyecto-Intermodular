-- Script DDL

DROP DATABASE IF EXISTS farm_db;
CREATE DATABASE farm_db;
USE farm_db;

-- Tabla de granjas
CREATE TABLE IF NOT EXISTS granjas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    ubicacion VARCHAR(150) NOT NULL,
    propietario VARCHAR(100) NOT NULL
);

-- Tabla de corrales
CREATE TABLE IF NOT EXISTS corrales (
    id INT AUTO_INCREMENT PRIMARY KEY,
    granja_id INT NOT NULL,
    nombre VARCHAR(50) NOT NULL,
    capacidad INT NOT NULL,
    FOREIGN KEY (granja_id) REFERENCES granjas(id) ON DELETE CASCADE
);

-- Tabla de animales
CREATE TABLE IF NOT EXISTS animales (
    id INT AUTO_INCREMENT PRIMARY KEY,
    corral_id INT NOT NULL,
    numero_crotal VARCHAR(20) NOT NULL UNIQUE,
    especie VARCHAR(50) NOT NULL,
    raza VARCHAR(50) NOT NULL,
    fecha_nacimiento DATE NOT NULL,
    peso_kg DECIMAL(6,2) NOT NULL,
    estado_salud VARCHAR(50) DEFAULT 'Sano',
    proposito VARCHAR(50) NOT NULL,
    FOREIGN KEY (corral_id) REFERENCES corrales(id) ON DELETE CASCADE
);

-- Tabla de tratamientos médicos
CREATE TABLE IF NOT EXISTS tratamientos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    animal_id INT NOT NULL,
    descripcion VARCHAR(200) NOT NULL,
    fecha DATE NOT NULL,
    FOREIGN KEY (animal_id) REFERENCES animales(id) ON DELETE CASCADE
);
