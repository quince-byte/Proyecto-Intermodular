# Diseño de Base de Datos - FarmManager

## 1. Análisis de Datos
La aplicación FarmManager tiene cuatro entidades principales:
- **Granjas:** Tiene un nombre, ubicación y propietario.
- **Corrales:** Lugares donde residen los animales. Pertenece a una granja y tiene capacidad máxima.
- **Animales:** Almacena la información de cada animal (número de crotal, especie, raza, fecha de nacimiento, peso, propósito y estado de salud), y está asignado obligatoriamente a un corral específico.
- **Tratamientos:** Registro de tratamientos aplicados a los animales.

### Relaciones
- **1 Granja posee N Corrales (1:N):** Un corral pertenece obligatoriamente a una sola granja (1,1), pero una granja puede tener múltiples corrales (1,N).
- **1 Corral alberga N Animales (1:N):** Un animal sólo puede estar y debe estar obligatoriamente en un corral (1,1), pero un corral puede tener varios animales (0,M). Si un corral se elimina, sus animales son eliminados a menos que se reubiquen antes.
- **1 Animal recibe N Tratamientos (1:N):** Un tratamiento médico se aplica a un solo animal (1,1), pero a lo largo de su vida un animal puede recibir múltiples tratamientos (0,M).

## 2. Diagrama Entidad-Relación (E/R)
El diagrama es el archivo `diagrama_ER.drawio` ubicado en la raíz del proyecto.

## 3. Modelo Relacional
A partir del diagrama E/R, se ha diseñado este esquema de tablas:

```
granjas(id PK AI, nombre VARCHAR(100) NN, ubicacion VARCHAR(150) NN, propietario VARCHAR(100) NN)

corrales(id PK AI, granja_id INT NN FK→granjas.id ON DELETE CASCADE, 
         nombre VARCHAR(50) NN, capacidad INT NN)

animales(id PK AI, corral_id INT NN FK→corrales.id ON DELETE CASCADE,
         numero_crotal VARCHAR(20) UNIQUE NN, especie VARCHAR(50) NN,
         raza VARCHAR(50) NN, fecha_nacimiento DATE NN,
         peso_kg DECIMAL(6,2) NN, estado_salud VARCHAR(50) DEFAULT 'Sano',
         proposito VARCHAR(50) NN)

tratamientos(id PK AI, animal_id INT NN FK→animales.id ON DELETE CASCADE,
             descripcion VARCHAR(200) NN, fecha DATE NN)
```

**Tabla: `granjas`**
- `id` (INT, PK, Auto): ID de la granja.
- `nombre` (VARCHAR, NOT NULL): Nombre de la granja.
- `ubicacion` (VARCHAR, NOT NULL): Ubicación.
- `propietario` (VARCHAR, NOT NULL): Nombre del dueño.

**Tabla: `corrales`**
- `id` (INT, PK, Auto): ID del corral.
- `granja_id` (INT, NOT NULL, FK): Clave foránea referenciando `granjas(id)`.
- `nombre` (VARCHAR, NOT NULL): Nombre del corral.
- `capacidad` (INT, NOT NULL): Capacidad máxima.

**Tabla: `animales`**
- `id` (INT, PK, Auto): Identificador interno numérico.
- `corral_id` (INT, NOT NULL, FK): Clave foránea referenciando `corrales(id)`.
- `numero_crotal` (VARCHAR, UNIQUE NOT NULL): Número de identificación (ej: ES123456789).
- `especie` (VARCHAR, NOT NULL): Especie (Bovino, Porcino, Ovino, Aviar).
- `raza` (VARCHAR, NOT NULL): Raza del animal.
- `fecha_nacimiento` (DATE, NOT NULL): Fecha de nacimiento.
- `peso_kg` (DECIMAL(6,2), NOT NULL): Peso actual del animal en kilos.
- `estado_salud` (VARCHAR, DEFAULT 'Sano'): Condición de salud (Sano, Enfermo).
- `proposito` (VARCHAR, NOT NULL): Tipo de producción (Carne, Leche, Lana, Huevos).

**Tabla: `tratamientos`**
- `id` (INT, PK, Auto): ID del tratamiento.
- `animal_id` (INT, NOT NULL, FK): Clave foránea referenciando a `animales(id)`.
- `descripcion` (VARCHAR, NOT NULL): Descripción del historial médico.
- `fecha` (DATE, NOT NULL): Fecha de aplicación.

## 4. Vistas
Se han definido tres vistas:

- **`vista_animales_completo`**: Resumen de todos los animales con su corral y granja.
- **`vista_animales_atencion`**: Animales enfermos o en tratamiento con conteo de tratamientos recibidos.
- **`vista_estadisticas_granja`**: Estadísticas por granja (total de corrales, animales, peso medio y enfermos).

## 5. Scripts y Consultas
En el directorio `/sql` se encuentran los siguientes archivos:
- `schema.sql`: Script DDL con la creación de tablas, claves foráneas y restricciones.
- `data.sql`: Script DML con datos iniciales de prueba.
- `consultas.sql`: Consultas DQL, DML y definición de VISTAS útiles.
