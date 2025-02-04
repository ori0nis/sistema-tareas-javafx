package gm.tareas.controlador;

import gm.tareas.modelo.Tarea;
import gm.tareas.servicio.TareaServicio;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.net.URL;
import java.util.ResourceBundle;

@Component
public class IndexControlador implements Initializable {

    private static final Logger logger = LoggerFactory.getLogger(IndexControlador.class);

    // Integramos el componente de servicio. Como IndexControlador ya es un componente de Spring, podemos
    // inyectar todos sus beans:
    @Autowired
    private TareaServicio tareaServicio;

    // Utilizamos los nombres que hemos asignado a las partes visuales de nuestra app con el Scene
    // Builder para trabajar con ellos:

    @FXML
    private TableView<Tarea> tareaTabla;

    @FXML
    private TableColumn<Tarea, Integer> idTareaColumna;

    @FXML
    private TableColumn<Tarea, String> nombreTareaColumna;

    @FXML
    private TableColumn<Tarea, String> responsableTareaColumna;

    @FXML
    private TableColumn<Tarea, String> estatusTareaColumna;

    // Configuramos una lista para que los cambios que realicemos en la tabla se reflejen de manera automática:

    private final ObservableList<Tarea> tareaList = FXCollections.observableArrayList();

    // Componentes de TextField:
    @FXML
    private TextField nombreTareaTexto;

    @FXML
    private TextField responsableTareaTexto;

    @FXML
    private TextField estatusTareaTexto;

    // Creamos este atributo para identificar qué tarea se ha seleccionado al clicar la tabla:
    private Integer idTareaInterno;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Programamos para que solo se pueda seleccionar un registro cada vez:
        tareaTabla.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        // Configuramos columnas:
        configurarColumnas();
        listarTareas();
    }

    // Este método relaciona la información que seleccionamos con la BD:
    private void configurarColumnas(){
        // Damos los nombres de los atributos que están declarados en la clase Tarea:
        idTareaColumna.setCellValueFactory(new PropertyValueFactory<>("id"));
        nombreTareaColumna.setCellValueFactory(new PropertyValueFactory<>("nombreTarea"));
        responsableTareaColumna.setCellValueFactory(new PropertyValueFactory<>("responsable"));
        estatusTareaColumna.setCellValueFactory(new PropertyValueFactory<>("estatus"));
    }

    private void listarTareas(){
        // Debajo de esta línea se puede ver el query de Hibernate en consola:
        logger.info("Ejecutando lista de tareas: ");
        tareaList.clear();
        // Dos pasos para conectar nuestra BD a la tabla:
        // 1. Esto devuelve la info en modo de objetos de tipo Tarea:
        tareaList.addAll(tareaServicio.listarTareas());
        // 2. Agregamos toda la info a la tabla:
        tareaTabla.setItems(tareaList);
    }

    public void agregarTarea(){
        if(nombreTareaTexto.getText().isEmpty()){
            mostrarMensaje("Error de validación", "Proporcione una tarea");
            nombreTareaTexto.requestFocus();
            return;
        } else {
            var tarea = new Tarea();
            recolectarDatosFormulario(tarea);
            // Con esto nos aseguramos de que la tarea no sea una modificación, sino una nueva creación. El id luego
            // es asignado por la BD.
            tarea.setId(null);
            tareaServicio.guardarTarea(tarea);
            mostrarMensaje("Información", "Tarea agregada");
            limpiarFormulario();
            listarTareas();
        }
    }

    public void cargarTareaFormulario(){
        // Con la selección única que hemos programado antes, nos aseguramos de saber siempre qué tarea ha sido
        // seleccionada:
        var tarea = tareaTabla.getSelectionModel().getSelectedItem();
        // Metemos una validación porque el evento onMouseClick también salta cuando el usuario clica en una parte de
        // la tabla que no contiene una tarea:
        if(tarea != null){
            idTareaInterno = tarea.getId();
            nombreTareaTexto.setText(tarea.getNombreTarea());
            responsableTareaTexto.setText(tarea.getResponsable());
            estatusTareaTexto.setText(tarea.getEstatus());
        }
    }

    private void mostrarMensaje(String titulo, String mensaje){
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    private void recolectarDatosFormulario(Tarea tarea){
        if(idTareaInterno != null){
            tarea.setId(idTareaInterno);
        }
        tarea.setNombreTarea(nombreTareaTexto.getText());
        tarea.setResponsable(responsableTareaTexto.getText());
        tarea.setEstatus(estatusTareaTexto.getText());
    }

    public void modificarTarea(){
        if(idTareaInterno == null){
            mostrarMensaje("Información", "Seleccione una tarea");
            return;
        }
        if(nombreTareaTexto.getText().isEmpty()){
            mostrarMensaje("Error de validación", "Proporcione una tarea");
            nombreTareaTexto.requestFocus();
            return;
        }
        var tarea = new Tarea();
        recolectarDatosFormulario(tarea);
        tareaServicio.guardarTarea(tarea);
        mostrarMensaje("Información", "Tarea modificada");
        limpiarFormulario();
        listarTareas();
    }

    public void eliminarTarea(){
        var tarea = tareaTabla.getSelectionModel().getSelectedItem();
        if(tarea != null){
            logger.info("Registro a eliminar: " + tarea.toString());
            tareaServicio.eliminarTarea(tarea);
            mostrarMensaje("Información", "Tarea eliminada");
            limpiarFormulario();
            listarTareas();
        } else {
            mostrarMensaje("Error", "No se ha seleccionado ninguna tarea");
        }
    }

    public void limpiarFormulario(){
        // Importante limpiar este id para "salir" del caso de modificar:
        idTareaInterno = null;
        nombreTareaTexto.clear();
        responsableTareaTexto.clear();
        estatusTareaTexto.clear();
    }
}
