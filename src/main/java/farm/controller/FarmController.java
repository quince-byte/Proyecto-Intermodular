package farm.controller;

import farm.model.Animal;
import farm.model.Tratamiento;
import farm.service.FarmService;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class FarmController {

    // Interfaz, tablas y columnas de la GUI
    @FXML private Canvas canvas;
    @FXML private TableView<Animal> tableAnimales;
    @FXML private TableColumn<Animal, Integer> colId;
    @FXML private TableColumn<Animal, String> colCrotal;
    @FXML private TableColumn<Animal, String> colEspecie;
    @FXML private TableColumn<Animal, String> colRaza;
    @FXML private TableColumn<Animal, java.time.LocalDate> colFechaNac;
    @FXML private TableColumn<Animal, Double> colPeso;
    @FXML private TableColumn<Animal, String> colEstado;
    @FXML private TableColumn<Animal, String> colProposito;
    @FXML private TableColumn<Animal, String> colCorral;
    @FXML private TableColumn<Animal, String> colGranja;

    @FXML private TextField txtBuscar;
    @FXML private TextField txtCrotal;
    @FXML private TextField txtEspecie;
    @FXML private TextField txtRaza;
    @FXML private DatePicker dpFechaNac;
    @FXML private TextField txtPeso;
    @FXML private TextField txtEstado;
    @FXML private TextField txtProposito;
    @FXML private TextField txtGranja;
    @FXML private TextField txtCorral;
    @FXML private Label lblStatus;

    private FarmService farmService;
    private ObservableList<Animal> animalList;
    private List<AnimalVisual> animalVisualList;
    private Timeline timeline;
    private GraphicsContext gc;
    
    // Animal seleccionado para editar
    private Animal animalSeleccionado = null;

    // Inicializa la tabla, el canvas y los datos de la interfaz
    public void initialize() {
        farmService = new FarmService();
        animalVisualList = new ArrayList<>();
        gc = canvas.getGraphicsContext2D();
        gc.setFont(new Font(40));

        // Celdas para escribir
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colCrotal.setCellValueFactory(new PropertyValueFactory<>("numeroCrotal"));
        colEspecie.setCellValueFactory(new PropertyValueFactory<>("especie"));
        colRaza.setCellValueFactory(new PropertyValueFactory<>("raza"));
        colFechaNac.setCellValueFactory(new PropertyValueFactory<>("fechaNacimiento"));
        colPeso.setCellValueFactory(new PropertyValueFactory<>("pesoKg"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estadoSalud"));
        colProposito.setCellValueFactory(new PropertyValueFactory<>("proposito"));
        colCorral.setCellValueFactory(new PropertyValueFactory<>("corralNombre"));
        colGranja.setCellValueFactory(new PropertyValueFactory<>("granjaNombre"));
        
        // Listener para rellenar el formulario al seleccionar una fila
        tableAnimales.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                animalSeleccionado = newSelection;
                rellenarFormulario(newSelection);
            }
        });

        loadData();
        startAnimation();
    }
    
    // Rellena los campos del formulario con los datos del animal seleccionado
    private void rellenarFormulario(Animal a) {
        txtCrotal.setText(a.getNumeroCrotal());
        txtEspecie.setText(a.getEspecie());
        txtRaza.setText(a.getRaza());
        dpFechaNac.setValue(a.getFechaNacimiento());
        txtPeso.setText(String.valueOf(a.getPesoKg()));
        txtEstado.setText(a.getEstadoSalud());
        txtProposito.setText(a.getProposito());
        txtGranja.setText(a.getGranjaNombre() != null ? a.getGranjaNombre() : "");
        txtCorral.setText(a.getCorralNombre() != null ? a.getCorralNombre() : "");
    }
    
    // Vacía todos los campos del formulario
    private void limpiarFormulario() {
        txtCrotal.clear();
        txtEspecie.clear();
        txtRaza.clear();
        dpFechaNac.setValue(null);
        txtPeso.clear();
        txtEstado.clear();
        txtProposito.clear();
        txtGranja.clear();
        txtCorral.clear();
        animalSeleccionado = null;
    }

    // Carga los animales de la base de datos en la tabla y prepara la animación
    @FXML
    public void loadData() {
        try {
            List<Animal> fromDb = farmService.getAllAnimals();
            animalList = FXCollections.observableArrayList(fromDb);
            tableAnimales.setItems(animalList);
            
            animalVisualList.clear();
            for (Animal a : fromDb) {
                animalVisualList.add(new AnimalVisual(a));
            }
            
            lblStatus.setText("Datos cargados correctamente. Total: " + fromDb.size() + " animales.");
            lblStatus.setTextFill(Color.GREEN);
        } catch (Exception e) {
            lblStatus.setText("Error cargando datos: " + e.getMessage());
            lblStatus.setTextFill(Color.RED);
        }
    }

    // Añade un nuevo animal a la base de datos con los datos del formulario
    @FXML
    public void addAnimal(ActionEvent event) {
        try {
            Animal a = construirAnimalDesdeFormulario(0);
            if (a == null) return;
            
            String granja = txtGranja.getText().trim();
            String corral = txtCorral.getText().trim();

            farmService.addAnimal(a, granja, corral);
            
            limpiarFormulario();
            loadData();
            lblStatus.setText("Animal añadido con éxito.");
            lblStatus.setTextFill(Color.GREEN);
        } catch (IllegalArgumentException e) {
            lblStatus.setText("Validación: " + e.getMessage());
            lblStatus.setTextFill(Color.RED);
        } catch (Exception e) {
            String errorMsg = e.getMessage();
            if (errorMsg != null && errorMsg.toLowerCase().contains("duplicate entry")) {
                lblStatus.setText("Error: El animal que estás intentando añadir ya está en el registro");
            } else {
                lblStatus.setText("Error: " + errorMsg);
            }
            lblStatus.setTextFill(Color.RED);
        }
    }

    // Actualiza en la base de datos los datos del animal seleccionado
    @FXML
    public void updateAnimal(ActionEvent event) {
        try {
            if (animalSeleccionado == null) {
                lblStatus.setText("Selecciona un animal de la tabla para editar.");
                lblStatus.setTextFill(Color.RED);
                return;
            }
            
            Animal a = construirAnimalDesdeFormulario(animalSeleccionado.getId());
            if (a == null) return;
            
            String granja = txtGranja.getText().trim();
            String corral = txtCorral.getText().trim();

            farmService.updateAnimal(a, granja, corral);
            
            limpiarFormulario();
            loadData();
            lblStatus.setText("Animal actualizado con éxito.");
            lblStatus.setTextFill(Color.GREEN);
        } catch (IllegalArgumentException e) {
            lblStatus.setText("Validación: " + e.getMessage());
            lblStatus.setTextFill(Color.RED);
        } catch (Exception e) {
            String errorMsg = e.getMessage();
            if (errorMsg != null && errorMsg.toLowerCase().contains("duplicate entry")) {
                lblStatus.setText("Error: El animal que estás intentando actualizar ya está en el registro");
            } else {
                lblStatus.setText("Error: " + errorMsg);
            }
            lblStatus.setTextFill(Color.RED);
        }
    }
    
    // Crea un objeto Animal leyendo los campos de la vista
    private Animal construirAnimalDesdeFormulario(int id) {
        String crotal = txtCrotal.getText().trim();
        String especie = txtEspecie.getText().trim();
        String raza = txtRaza.getText().trim();
        java.time.LocalDate fecha = dpFechaNac.getValue();
        double peso;
        try {
            peso = Double.parseDouble(txtPeso.getText().trim());
        } catch (NumberFormatException e) {
            lblStatus.setText("Error: El peso debe ser un número válido.");
            lblStatus.setTextFill(Color.RED);
            return null;
        }
        String estado = txtEstado.getText().trim();
        String proposito = txtProposito.getText().trim();
        
        // Normalizar estado de salud: aceptar sano/sana/enfermo/enferma
        String estadoNormalizado = estado;
        String estadoLower = estado.toLowerCase();
        if (estadoLower.contains("san")) {
            estadoNormalizado = "Sano";
        } else if (estadoLower.contains("enferm")) {
            estadoNormalizado = "Enfermo";
        }

        int corralId = (id > 0 && animalSeleccionado != null) ? animalSeleccionado.getCorralId() : 0;
        return new Animal(id, crotal, especie, raza, fecha, peso, estadoNormalizado, proposito, corralId);
    }

    // Elimina de la base de datos el animal seleccionado pidiendo confirmación
    @FXML
    public void deleteAnimal(ActionEvent event) {
        Animal selected = tableAnimales.getSelectionModel().getSelectedItem();
        if (selected != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, 
                "¿Eliminar el animal con crotal " + selected.getNumeroCrotal() + "?", 
                ButtonType.YES, ButtonType.NO);
            alert.setTitle("Confirmar eliminación");
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.YES) {
                    try {
                        farmService.deleteAnimal(selected.getId());
                        limpiarFormulario();
                        loadData();
                        lblStatus.setText("Animal borrado.");
                        lblStatus.setTextFill(Color.GREEN);
                    } catch (Exception e) {
                        lblStatus.setText("Error al borrar: " + e.getMessage());
                        lblStatus.setTextFill(Color.RED);
                    }
                }
            });
        } else {
            lblStatus.setText("Selecciona un animal para borrar.");
            lblStatus.setTextFill(Color.RED);
        }
    }
    
    
    // Filtra la lista de animales según el texto de búsqueda
    @FXML
    public void buscarAnimal(ActionEvent event) {
        String criterio = txtBuscar.getText().trim();
        if (criterio.isEmpty()) {
            loadData();
            return;
        }
        
        try {
            List<Animal> filtrados = farmService.buscarAnimales(criterio);
            animalList = FXCollections.observableArrayList(filtrados);
            tableAnimales.setItems(animalList);
            
            // Actualizar visuales de la animación
            animalVisualList.clear();
            for (Animal a : filtrados) {
                animalVisualList.add(new AnimalVisual(a));
            }
            
            lblStatus.setText("Búsqueda completada. Resultados: " + filtrados.size());
            lblStatus.setTextFill(Color.GREEN);
        } catch (Exception e) {
            lblStatus.setText("Error en búsqueda: " + e.getMessage());
            lblStatus.setTextFill(Color.RED);
        }
    }

    // Limpia el campo de búsqueda y recarga los datos completos
    @FXML
    public void limpiarBusqueda(ActionEvent event) {
        txtBuscar.clear();
        loadData();
    }
    
    // Abre un diálogo para consultar y registrar tratamientos médicos al animal
    @FXML
    public void gestionarTratamientos(ActionEvent event) {
        Animal selected = tableAnimales.getSelectionModel().getSelectedItem();
        if (selected == null) {
            lblStatus.setText("Selecciona un animal para ver sus tratamientos.");
            lblStatus.setTextFill(Color.RED);
            return;
        }
        
        // Crear diálogo completo para mostrar y añadir tratamientos
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Tratamientos de " + selected.getNumeroCrotal());
        dialog.setHeaderText("Historial médico del animal");
        
        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.getButtonTypes().addAll(ButtonType.OK);
        
        VBox content = new VBox(10);
        content.setPadding(new javafx.geometry.Insets(10));
        
        // Lista de tratamientos existentes
        Label lblLista = new Label("Tratamientos registrados:");
        ListView<String> listView = new ListView<>();
        listView.setPrefHeight(150);
        
        List<Tratamiento> tratamientos = farmService.getTratamientosByAnimal(selected.getId());
        if (tratamientos.isEmpty()) {
            listView.getItems().add("No hay tratamientos registrados.");
        } else {
            for (Tratamiento t : tratamientos) {
                listView.getItems().add(t.getFecha() + " - " + t.getDescripcion());
            }
        }
        
        // Formulario para añadir nuevo tratamiento
        Label lblNuevo = new Label("Añadir nuevo tratamiento:");
        TextField txtDesc = new TextField();
        txtDesc.setPromptText("Descripción del tratamiento (ej: Vacunación)");
        DatePicker dpFechaTrat = new DatePicker();
        dpFechaTrat.setPromptText("Fecha del tratamiento");
        
        Button btnAdd = new Button("Añadir Tratamiento");
        Label lblDialogStatus = new Label();
        lblDialogStatus.setTextFill(Color.RED);
        
        btnAdd.setOnAction(e -> {
            String desc = txtDesc.getText().trim();
            java.time.LocalDate fecha = dpFechaTrat.getValue();
            
            if (desc.isEmpty()) {
                lblDialogStatus.setText("La descripción es obligatoria.");
                return;
            }
            if (fecha == null) {
                lblDialogStatus.setText("La fecha es obligatoria.");
                return;
            }
            
            try {
                Tratamiento nuevo = new Tratamiento(0, selected.getId(), desc, fecha);
                farmService.addTratamiento(nuevo);
                
                // Refrescar lista
                listView.getItems().clear();
                List<Tratamiento> actualizados = farmService.getTratamientosByAnimal(selected.getId());
                for (Tratamiento t : actualizados) {
                    listView.getItems().add(t.getFecha() + " - " + t.getDescripcion());
                }
                
                txtDesc.clear();
                dpFechaTrat.setValue(null);
                lblDialogStatus.setTextFill(Color.GREEN);
                lblDialogStatus.setText("Tratamiento añadido correctamente.");
            } catch (Exception ex) {
                lblDialogStatus.setTextFill(Color.RED);
                lblDialogStatus.setText("Error: " + ex.getMessage());
            }
        });
        
        content.getChildren().addAll(
            lblLista, listView,
            new Separator(),
            lblNuevo, txtDesc, dpFechaTrat, btnAdd, lblDialogStatus
        );
        
        dialogPane.setContent(content);
        dialog.showAndWait();
        
        lblStatus.setText("Tratamientos consultados para " + selected.getNumeroCrotal());
        lblStatus.setTextFill(Color.GREEN);
    }

    // Inicia la animación de los animales moviéndose en el canvas
    private void startAnimation() {
        if (timeline != null && timeline.getStatus() == Animation.Status.RUNNING) {
            timeline.stop();
        }

        KeyFrame frame = new KeyFrame(Duration.millis(16), e -> {
            double w = canvas.getWidth();
            double h = canvas.getHeight();
            
            // Draw background
            gc.setFill(Color.web("#8BC34A")); // Green grass
            gc.fillRect(0, 0, w, h);

            for (AnimalVisual v : animalVisualList) {
                // Actualizar posición
                v.setPosX(v.getPosX() + v.getVelX());
                v.setPosY(v.getPosY() + v.getVelY());
                
                // Rebotan si llegan a los bordes
                if (v.getPosX() < 0 || v.getPosX() > w - 40) {
                    v.setVelX(-v.getVelX());
                    v.setPosX(Math.max(0, Math.min(v.getPosX(), w - 40)));
                }
                if (v.getPosY() < 40 || v.getPosY() > h) {
                    v.setVelY(-v.getVelY());
                    v.setPosY(Math.max(40, Math.min(v.getPosY(), h)));
                }
                
                String especie = v.getAnimal().getEspecie().toLowerCase();
                String emoji = "❓"; // Valor por defecto si no se reconoce el animal

                if (especie.contains("bovino")) {
                    emoji = "🐄";
                } else if (especie.contains("oveja") || especie.contains("ovino")) {
                    emoji = "🐑";
                } else if (especie.contains("cerdo") || especie.contains("porcino")) {
                    emoji = "🐖";
                } else if (especie.contains("gallina") || especie.contains("ave") || especie.contains("aviar")) {
                    emoji = "🐔";
                } else if (especie.contains("conejo") || especie.contains("cun")) {
                    emoji = "🐇";
                }

                
                gc.setFill(Color.BLACK);
                gc.fillText(emoji, v.getPosX(), v.getPosY());
                
                // Etiqueta de crotal del animal
                gc.setFont(new Font(12));
                gc.fillText(v.getAnimal().getNumeroCrotal(), v.getPosX(), v.getPosY() - 40);
                gc.setFont(new Font(40));
            }
        });

        timeline = new Timeline(frame);
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    // Exporta la lista actual de animales a un archivo XML
    @FXML
    public void exportXML(ActionEvent event) {
        try {
            File file = new File("xml/inventario.xml");
            file.getParentFile().mkdirs();
            FileWriter writer = new FileWriter(file);
            writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            writer.write("<granja xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"esquema.xsd\">\n");
            
            for (Animal a : animalList) {
                writer.write("    <animal id=\"" + a.getId() + "\">\n");
                writer.write("        <crotal>" + a.getNumeroCrotal() + "</crotal>\n");
                writer.write("        <especie>" + a.getEspecie() + "</especie>\n");
                writer.write("        <raza>" + a.getRaza() + "</raza>\n");
                writer.write("        <fechaNacimiento>" + a.getFechaNacimiento() + "</fechaNacimiento>\n");
                writer.write("        <pesoKg>" + a.getPesoKg() + "</pesoKg>\n");
                writer.write("        <estado>" + a.getEstadoSalud() + "</estado>\n");
                writer.write("        <proposito>" + a.getProposito() + "</proposito>\n");
                writer.write("        <corral>" + (a.getCorralNombre() != null ? a.getCorralNombre() : "") + "</corral>\n");
                writer.write("        <granja>" + (a.getGranjaNombre() != null ? a.getGranjaNombre() : "") + "</granja>\n");
                writer.write("    </animal>\n");
            }
            writer.write("</granja>");
            writer.close();
            lblStatus.setText("Exportado a xml/inventario.xml");
            lblStatus.setTextFill(Color.GREEN);
        } catch (Exception e) {
            lblStatus.setText("Error al exportar XML: " + e.getMessage());
            lblStatus.setTextFill(Color.RED);
        }
    }
}
