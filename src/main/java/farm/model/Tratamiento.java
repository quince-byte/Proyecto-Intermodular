package farm.model;

import java.time.LocalDate;

// Clase que define los tratamientos de los animales, con sus getters y setters
public class Tratamiento {
    private int id;
    private int animalId;
    private String descripcion;
    private LocalDate fecha;

    public Tratamiento() {
    }

    public Tratamiento(int id, int animalId, String descripcion, LocalDate fecha) {
        this.id = id;
        this.animalId = animalId;
        this.descripcion = descripcion;
        this.fecha = fecha;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAnimalId() {
        return animalId;
    }

    public void setAnimalId(int animalId) {
        this.animalId = animalId;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }
}
