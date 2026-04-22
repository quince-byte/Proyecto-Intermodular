package farm.model;

import java.time.LocalDate;

// Clase que define los distintos animales, con sus getters y setters
public class Animal implements Validable {
    private int id;
    private String numeroCrotal;
    private String especie;
    private String raza;
    private LocalDate fechaNacimiento;
    private double pesoKg;
    private String estadoSalud;
    private String proposito;

    private int corralId;
    private String corralNombre;
    private String granjaNombre;

    private String mensajeError;

    public Animal() {
    }

    public Animal(int id, String numeroCrotal, String especie, String raza, LocalDate fechaNacimiento, double pesoKg,
            String estadoSalud, String proposito, int corralId) {
        this.id = id;
        this.numeroCrotal = numeroCrotal;
        this.especie = especie;
        this.raza = raza;
        this.fechaNacimiento = fechaNacimiento;
        this.pesoKg = pesoKg;
        this.estadoSalud = estadoSalud;
        this.proposito = proposito;
        this.corralId = corralId;
    }

    @Override
    public boolean esValido() {
        if (numeroCrotal == null || !numeroCrotal.toUpperCase().matches("[A-Z]{2}\\d+")) {
            mensajeError = "El crotal debe tener formato: dos letras seguidas de dígitos (ej: ES123456789)";
            return false;
        }
        if (especie == null || especie.trim().isEmpty()) {
            mensajeError = "La especie es obligatoria";
            return false;
        }
        if (raza == null || raza.trim().isEmpty()) {
            mensajeError = "La raza es obligatoria";
            return false;
        }
        if (fechaNacimiento == null || fechaNacimiento.isAfter(LocalDate.now())) {
            mensajeError = "La fecha de nacimiento no puede ser futura";
            return false;
        }
        if (pesoKg <= 0) {
            mensajeError = "El peso debe ser mayor que 0";
            return false;
        }
        if (estadoSalud == null || estadoSalud.trim().isEmpty()) {
            mensajeError = "El estado de salud es obligatorio";
            return false;
        }
        if (proposito == null || proposito.trim().isEmpty()) {
            mensajeError = "El propósito es obligatorio";
            return false;
        }
        if (id > 0 && corralId <= 0) {
            mensajeError = "Debe asignarse un corral válido";
            return false;
        }
        mensajeError = null;
        return true;
    }

    @Override
    public String getMensajeError() {
        return mensajeError;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumeroCrotal() {
        return numeroCrotal;
    }

    public void setNumeroCrotal(String numeroCrotal) {
        this.numeroCrotal = numeroCrotal;
    }

    public String getEspecie() {
        return especie;
    }

    public void setEspecie(String especie) {
        this.especie = especie;
    }

    public String getRaza() {
        return raza;
    }

    public void setRaza(String raza) {
        this.raza = raza;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public double getPesoKg() {
        return pesoKg;
    }

    public void setPesoKg(double pesoKg) {
        this.pesoKg = pesoKg;
    }

    public String getEstadoSalud() {
        return estadoSalud;
    }

    public void setEstadoSalud(String estadoSalud) {
        this.estadoSalud = estadoSalud;
    }

    public String getProposito() {
        return proposito;
    }

    public void setProposito(String proposito) {
        this.proposito = proposito;
    }

    public int getCorralId() {
        return corralId;
    }

    public void setCorralId(int corralId) {
        this.corralId = corralId;
    }

    public String getCorralNombre() {
        return corralNombre;
    }

    public void setCorralNombre(String corralNombre) {
        this.corralNombre = corralNombre;
    }

    public String getGranjaNombre() {
        return granjaNombre;
    }

    public void setGranjaNombre(String granjaNombre) {
        this.granjaNombre = granjaNombre;
    }
}
