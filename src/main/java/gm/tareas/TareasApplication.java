package gm.tareas;

import gm.tareas.presentacion.SistemaTareasFX;
import javafx.application.Application;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TareasApplication {

	public static void main(String[] args) {

		// Ponemos entre comentarios para levantar la app basándonos en JavaFX
		// SpringApplication.run(TareasApplication.class, args);

		// Colocamos lo mismo que haría funcionar la app en IndexControlador:
		Application.launch(SistemaTareasFX.class, args);

		// El orden de la app ahora es:
		// 1. La entrada de ejecución está en TareasApplication
		// 2. TareasApplication llama a SistemaTareasFX
		// 3. SistemaTareasFX ejecuta index.fxml
	}

}
