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
    (2, 'Establo Principal', 5);

-- Animales
INSERT INTO animales (corral_id, numero_crotal, especie, raza, fecha_nacimiento, peso_kg, estado_salud, proposito) VALUES 
    (1, 'ES123456789', 'Bovino', 'Frisona', '2021-05-12', 650.50, 'Sano', 'Leche'),
    (1, 'ES987654321', 'Bovino', 'Angus', '2022-03-20', 420.00, 'Sano', 'Carne'),
    (3, 'ES456123789', 'Porcino', 'Duroc', '2023-01-15', 110.20, 'Enfermo', 'Carne');

-- Tratamientos médicos
INSERT INTO tratamientos (animal_id, descripcion, fecha) VALUES 
    (3, 'Administración de antibióticos por infección', '2023-10-15');
