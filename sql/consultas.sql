
-- Consultas DQL y DML para FarmManager

USE farm_db;

-- 1. Consultar todos los animales ordenados por fecha de nacimiento
SELECT * FROM animales ORDER BY fecha_nacimiento DESC;

-- 2. Consultar animales enfermos con su corral y granja
SELECT a.numero_crotal, a.especie, c.nombre as corral, g.nombre as granja
FROM animales a
INNER JOIN corrales c ON a.corral_id = c.id
INNER JOIN granjas g ON c.granja_id = g.id
WHERE a.estado_salud = 'Enfermo';

-- 3. Contar la cantidad de animales por granja
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

-- 6. Insertar un nuevo animal en un corral específico
INSERT INTO animales (corral_id, numero_crotal, especie, raza, fecha_nacimiento, peso_kg, estado_salud, proposito) 
VALUES (2, 'ES111222333', 'Ovino', 'Merina', '2023-05-10', 45.5, 'Sano', 'Lana');

-- 7. Actualizar el estado de salud de un animal y moverlo de corral
UPDATE animales 
SET estado_salud = 'Sano', corral_id = 1
WHERE numero_crotal = 'ES456123789';

-- 8. Eliminar un corral
-- Reasignar los animales al corral con ID 1
UPDATE animales SET corral_id = 1 WHERE corral_id = (SELECT id FROM corrales WHERE nombre = 'Establo Principal');
-- Eliminar el corral vacío
DELETE FROM corrales WHERE nombre = 'Establo Principal';