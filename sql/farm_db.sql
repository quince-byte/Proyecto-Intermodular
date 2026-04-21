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

-- Inserción de datos de ejemplo
-- Script DML
USE farm_db;

-- Granjas
INSERT INTO granjas (nombre, ubicacion, propietario) VALUES 
    ('Granja Los Pinos', 'Norte', 'Carlos Pérez'),
    ('Granja El Valle', 'Sur', 'María Gómez');

-- Corrales
INSERT INTO corrales (granja_id, nombre, capacidad) VALUES 
    (1, 'Corral Norte', 10),
    (1, 'Corral Sur', 15),
    (2, 'Establo Principal', 5),
    (2, 'Gallinero', 20),
    (2, 'Cunero', 10);

-- Animales
INSERT INTO animales (corral_id, numero_crotal, especie, raza, fecha_nacimiento, peso_kg, estado_salud, proposito) VALUES 
    (1, 'ES123456789', 'Bovino', 'Frisona', '2021-05-12', 650.50, 'Sano', 'Leche'),
    (1, 'ES987654321', 'Bovino', 'Angus', '2022-03-20', 420.00, 'Sano', 'Carne'),
    (3, 'ES456123789', 'Porcino', 'Duroc', '2023-01-15', 110.20, 'Enfermo', 'Carne'),
    (2, 'ES001122334', 'Ovino', 'Merina', '2023-06-10', 45.00, 'Sano', 'Lana'),
    (4, 'ES556677889', 'Aviar', 'Leghorn', '2024-01-05', 2.10, 'Sano', 'Huevos'),
    (5, 'ES998877665', 'Cunícola', 'Gigante de Flandes', '2024-02-14', 5.30, 'Sano', 'Carne');

-- Tratamientos médicos
INSERT INTO tratamientos (animal_id, descripcion, fecha) VALUES 
    (3, 'Administración de antibióticos por infección', '2023-10-15');
