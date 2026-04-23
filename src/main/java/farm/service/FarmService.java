package farm.service;

import farm.dao.AnimalDAO;
import farm.dao.CorralDAO;
import farm.dao.GranjaDAO;
import farm.model.Animal;
import farm.dao.TratamientoDAO;
import farm.model.Corral;
import farm.model.Granja;
import farm.model.Tratamiento;
import java.util.List;
import java.util.stream.Collectors;

public class FarmService {
    
    private GranjaDAO granjaDAO;
    private CorralDAO corralDAO;
    private AnimalDAO animalDAO;
    private TratamientoDAO tratamientoDAO;

    // Constructor del servicio
    public FarmService() {
        this.granjaDAO = new GranjaDAO();
        this.corralDAO = new CorralDAO();
        this.animalDAO = new AnimalDAO();
        this.tratamientoDAO = new TratamientoDAO();
    }
    // Función para obtener los animales
    public List<Animal> getAllAnimals() {
        return animalDAO.getAll();
    }
    // Función para eliminar un animal
    public void deleteAnimal(int id) throws Exception {
        animalDAO.delete(id);
    }

    // Función para crear corrales y granjas
    private Corral resolveCorral(String nombreGranja, String nombreCorral) throws Exception {
        // Buscar o crear Granja
        Granja granja = granjaDAO.getByNombre(nombreGranja);
        if (granja == null) {
            granja = new Granja(0, nombreGranja, "Desconocida", "Sistema");
            granja = granjaDAO.insert(granja);
            if (granja == null) {
                throw new Exception("No se pudo crear la granja en la base de datos");
            }
        }

        // Buscar o crear Corral
        Corral corral = corralDAO.getByNombreAndGranja(nombreCorral, granja.getId());
        if (corral == null) {
            corral = new Corral(0, granja.getId(), nombreCorral, 100);
            corral = corralDAO.insert(corral);
            if (corral == null) {
                throw new Exception("No se pudo crear el corral en la base de datos");
            }
        }
        
        return corral;
    }
    // Función para añadir un animal
    public void addAnimal(Animal animal, String nombreGranja, String nombreCorral) throws Exception {
        if (!animal.esValido()) {
            throw new IllegalArgumentException(animal.getMensajeError());
        }
        
        Corral corral = resolveCorral(nombreGranja, nombreCorral);
        animal.setCorralId(corral.getId());
        animalDAO.insert(animal);
    }
    // Función para actualizar los datos de un animal
    public void updateAnimal(Animal animal, String nombreGranja, String nombreCorral) throws Exception {
        if (!animal.esValido()) {
            throw new IllegalArgumentException(animal.getMensajeError());
        }
        
        Corral corral = resolveCorral(nombreGranja, nombreCorral);
        animal.setCorralId(corral.getId());
        animalDAO.update(animal);
    }
    // Función para buscar un animal
    public List<Animal> buscarAnimales(String criterio) {
        String criterioLower = criterio.toLowerCase();
        return animalDAO.getAll().stream()
            .filter(a -> a.getNumeroCrotal().toLowerCase().contains(criterioLower)
                      || a.getEspecie().toLowerCase().contains(criterioLower)
                      || a.getRaza().toLowerCase().contains(criterioLower)
                      || (a.getCorralNombre() != null && a.getCorralNombre().toLowerCase().contains(criterioLower))
                      || (a.getGranjaNombre() != null && a.getGranjaNombre().toLowerCase().contains(criterioLower)))
            .collect(Collectors.toList());
    }

    // Función para obtener los tratamientos de un animal
    public List<Tratamiento> getTratamientosByAnimal(int animalId) {
        return tratamientoDAO.getByAnimalId(animalId);
    }
    // Función para añadir tratamientos
    public void addTratamiento(Tratamiento tratamiento) throws Exception {
        if (tratamiento.getAnimalId() <= 0) {
            throw new IllegalArgumentException("Debe seleccionar un animal válido");
        }
        if (tratamiento.getDescripcion() == null || tratamiento.getDescripcion().trim().isEmpty()) {
            throw new IllegalArgumentException("La descripción del tratamiento es obligatoria");
        }
        if (tratamiento.getFecha() == null) {
            throw new IllegalArgumentException("La fecha del tratamiento es obligatoria");
        }
        Tratamiento t = tratamientoDAO.insert(tratamiento);
        if (t == null) {
            throw new Exception("Error al insertar el tratamiento en la base de datos");
        }
    }
}
