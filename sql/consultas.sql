
-- Consultas DQL, DML y VISTAS

USE farm_db;

-- CONSULTAS DQL

-- 1. Consultar todos los animales ordenados por fecha de nacimiento
SELECT * FROM animales ORDER BY fecha_nacimiento DESC;

-- 2. Consultar animales enfermos con su corral y granja
SELECT a.numero_crotal, a.especie, c.nombre as corral, g.nombre as granja
FROM animales a
INNER JOIN corrales c ON a.corral_id = c.id
INNER JOIN granjas g ON c.granja_id = g.id
WHERE a.estado_salud = 'Enfermo';

-- 3. Contar la cantidad de animales por granja (LEFT JOIN + GROUP BY)
SELECT g.nombre as granja, COUNT(a.id) as cantidad_animales 
FROM granjas g
LEFT JOIN corrales c ON g.id = c.granja_id
LEFT JOIN animales a ON c.id = a.corral_id
GROUP BY g.id;

-- 4. Obtener todos los tratamientos médicos de un animal específico
SELECT a.numero_crotal, a.especie, t.descripcion, t.fecha 
FROM animales a
INNER JOIN tratamientos t ON a.id = t.animal_id
WHERE a.numero_crotal = 'ES456123789';

-- 5. Obtener el promedio de peso de los animales por especie
SELECT especie, AVG(peso_kg) as peso_medio 
FROM animales 
GROUP BY especie;

-- 6. Listar corrales con su ocupación actual vs capacidad
SELECT c.nombre as corral, c.capacidad, COUNT(a.id) as ocupacion_actual,
       (c.capacidad - COUNT(a.id)) as plazas_libres
FROM corrales c
LEFT JOIN animales a ON c.id = a.corral_id
GROUP BY c.id;

-- CONSULTAS DML

-- 7. Insertar un nuevo animal en un corral específico
INSERT INTO animales (corral_id, numero_crotal, especie, raza, fecha_nacimiento, peso_kg, estado_salud, proposito) 
VALUES (2, 'ES111222333', 'Ovino', 'Merina', '2023-05-10', 45.5, 'Sano', 'Lana');

-- 8. Actualizar el estado de salud de un animal y moverlo de corral
UPDATE animales 
SET estado_salud = 'Sano', corral_id = 1
WHERE numero_crotal = 'ES456123789';

-- 9. Registrar un nuevo tratamiento médico
INSERT INTO tratamientos (animal_id, descripcion, fecha) 
VALUES (1, 'Vacunación anual brucelosis', '2024-04-15');

-- 10. Eliminar un corral
-- Reasignar los animales al corral con ID 1
UPDATE animales SET corral_id = 1 WHERE corral_id = (SELECT id FROM corrales WHERE nombre = 'Establo Principal');
-- Eliminar el corral vacío
DELETE FROM corrales WHERE nombre = 'Establo Principal';

-- VISTAS

-- 11. Resumen completo de animales con ubicación
CREATE OR REPLACE VIEW vista_animales_completo AS
SELECT 
    a.id,
    a.numero_crotal,
    a.especie,
    a.raza,
    a.fecha_nacimiento,
    a.peso_kg,
    a.estado_salud,
    a.proposito,
    c.nombre AS corral,
    g.nombre AS granja,
    g.ubicacion
FROM animales a
INNER JOIN corrales c ON a.corral_id = c.id
INNER JOIN granjas g ON c.granja_id = g.id;

-- 12. Animales que requieren atención
CREATE OR REPLACE VIEW vista_animales_atencion AS
SELECT 
    a.numero_crotal,
    a.especie,
    a.estado_salud,
    a.peso_kg,
    c.nombre AS corral,
    COUNT(t.id) AS total_tratamientos
FROM animales a
INNER JOIN corrales c ON a.corral_id = c.id
LEFT JOIN tratamientos t ON a.id = t.animal_id
WHERE a.estado_salud IN ('Enfermo', 'Tratamiento')
GROUP BY a.id;

-- 13. Estadísticas por granja
CREATE OR REPLACE VIEW vista_estadisticas_granja AS
SELECT 
    g.nombre AS granja,
    g.propietario,
    COUNT(DISTINCT c.id) AS total_corrales,
    COUNT(a.id) AS total_animales,
    AVG(a.peso_kg) AS peso_medio,
    SUM(CASE WHEN a.estado_salud = 'Enfermo' THEN 1 ELSE 0 END) AS animales_enfermos
FROM granjas g
LEFT JOIN corrales c ON g.id = c.granja_id
LEFT JOIN animales a ON c.id = a.corral_id
GROUP BY g.id;
