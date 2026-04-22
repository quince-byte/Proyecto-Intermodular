package farm.model;

// Clase que define los distintos corrales, con sus getters y setters
public class Corral {
    private int id;
    private int granjaId;
    private String nombre;
    private int capacidad;

    public Corral() {
    }

    public Corral(int id, int granjaId, String nombre, int capacidad) {
        this.id = id;
        this.granjaId = granjaId;
        this.nombre = nombre;
        this.capacidad = capacidad;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGranjaId() {
        return granjaId;
    }

    public void setGranjaId(int granjaId) {
        this.granjaId = granjaId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }
}
