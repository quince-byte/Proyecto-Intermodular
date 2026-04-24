package farm.model;

// Interfaz para la validación de los datos
public interface Validable {

    // Comprueba que es válido
    boolean esValido();

    // Recoge el mensaje de error
    String getMensajeError();
}
