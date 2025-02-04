package gm.tareas.presentacion;

import gm.tareas.TareasApplication;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;

public class SistemaTareasFX extends Application {

      // Comentamos el método con el que normalmente se ejecutan las apps de JavaFX para poder
      // conectar con Spring (el método main va a estar en TareasApplication)

//    public static void main(String[] args) {
//        launch(args);
//    }

    // Procedemos a levantar la fábrica de Spring
    private ConfigurableApplicationContext applicationContext;

    // Este nétodo se ejecuta antes que start:
    @Override
    public void init(){
        this.applicationContext = new SpringApplicationBuilder(TareasApplication.class).run();
    }

    @Override
    public void start(Stage stage) throws Exception {
        // Cargamos en memoria la interfaz gráfica:
        FXMLLoader loader = new FXMLLoader(TareasApplication.class
                            .getResource("/templates/index.fxml"));
        // Con esta línea cargamos todos los objetos de Spring a JavaFX:
        loader.setControllerFactory(applicationContext::getBean);
        Scene scene = new Scene(loader.load());
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop(){
        // Cerramos la fábrica de Spring y la app
        applicationContext.close();
        Platform.exit();
    }
}
