package farm.model;

// Clase que define las distintas granjas, con sus getters y setters
public class Granja {
    private int id;
    private String nombre;
    private String ubicacion;
    private String propietario;

    public Granja() {
    }

    public Granja(int id, String nombre, String ubicacion, String propietario) {
        this.id = id;
        this.nombre = nombre;
        this.ubicacion = ubicacion;
        this.propietario = propietario;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getPropietario() {
        return propietario;
    }

    public void setPropietario(String propietario) {
        this.propietario = propietario;
    }
}
