package application;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			//BorderPane root = new BorderPane(); Ya no necesito crear el BorderpPane a mano
			FXMLLoader loader= new FXMLLoader();
			
			loader.setLocation(Main.class.getResource("VentanaInicio.fxml"));//carga el xml y lo convierte en un objeto
			//Esto le da la ubicación
			AnchorPane ventana= loader.load();//Crea la escena con ventana
			Scene scene = new Scene(ventana);
			primaryStage.setScene(scene);
			primaryStage.setTitle("Inicio de sesión");
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
