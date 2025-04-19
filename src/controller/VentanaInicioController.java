package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import DataBase.GestionBD;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class VentanaInicioController implements Initializable {
	

    @FXML
    private Button btnEntrar;

    @FXML
    private Button btnLimpiar;

    @FXML
    private Button btnSalir;

    @FXML
    private ComboBox<String> cbCargo;
    

    @FXML
    private Label lbCargo;

    @FXML
    private Label lblPass;

    @FXML
    private Label lblUsuario;

    @FXML
    private PasswordField pfContraseña;

    @FXML
    private TextField tfUsuario;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
     String[] items= {"Alumno", "Profesor"};
     cbCargo.getItems().addAll(items);
     
    }

    @FXML
    void btnEntrar(ActionEvent event) {
    	//Para entrar lo primero es obtener la información introducida por el usuario
    	String nombre =tfUsuario.getText();
    	String contraseña=pfContraseña.getText();
    	String cargo = (String) cbCargo.getSelectionModel().getSelectedItem();
    	boolean permitido= false;
    	if(nombre.isEmpty()|| contraseña.isEmpty() || cargo ==null || cargo.equals("Elige")) {
    		mostrarAlerta("Campos incompletos", "Rellene todos los campos.", AlertType.WARNING);
    		return;
    	}
    	
    	if(cargo.equals("Alumno")) {
    		
    		try {
    			GestionBD gbd = new GestionBD();
				ResultSet rs =gbd.buscarAlumno(nombre, contraseña);
				if(rs != null && rs.next() ){
					//Si encontramos el alumno obtenemos su dni
					
					ResultSet rsDni = gbd.obtenerDniPorNombre(nombre, contraseña);
					String dni = "";
					if (rsDni != null && rsDni.next()) {
					    dni = rsDni.getString("dni_alumno"); 
					    mostrarAlerta("Bienvenido "+nombre,"Te has logeado con éxito" ,AlertType.CONFIRMATION);
					} else {
					    mostrarAlerta("Error de acceso", "No se encuentra el dni.", AlertType.ERROR);
					    return;
					}

					
					FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/VentanaAlumno.fxml"));
					Parent root =loader.load();
					VentanaAlumnoController controlador = loader.getController();
					controlador.ColocarDniAlumno(dni); //Tengo que pasarle el dni del alumno a la otra ventana
					controlador.colocarNombreAlumno(tfUsuario.getText());

					controlador.cargarModulos();//Lo módulos para ese dni
					
					Stage stage= new Stage(); //Tengo que crear la nueva ventana
					stage.setTitle("Ventana Alumno");
					stage.setScene(new Scene(root));
					stage.show();
					
					//Cerramos esta ventana
					Stage logeo = (Stage) btnEntrar.getScene().getWindow();
					logeo.close();
				 } else {
			            mostrarAlerta("Error de acceso", "Nombre o contraseña incorrectos.", AlertType.ERROR);
			            return;
				 }
				
				
				
			} catch (SQLException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		
    	}else if(cargo.equals("Profesor")) {
    		
    		try {
    			GestionBD gbd =new GestionBD();
				ResultSet dniProfesor=gbd.buscarProfesor(nombre, contraseña);
				String dni ="";
									
					if(dniProfesor !=null && dniProfesor.next()) {
						dni= dniProfesor.getString("dni_profesor");
						//System.out.println("El profesor encontrado es: "+ dniProfesor.getString("nombre")+ dniProfesor.getString("apellidos")+ " con dni: "+ dni);
						//Prepara la ventana
						mostrarAlerta("Validación exitosa", "Bienvenido "+ nombre, AlertType.CONFIRMATION);
						FXMLLoader loader= new FXMLLoader(getClass().getResource("/application/VentanaProfesor.fxml"));
						Parent root=loader.load();//Construye los componentes
						VentanaProfesorController controllerP= loader.getController();
						//controllerP es una instancia de VentanaProfesorController, hay lo que hay allí
						//Esto nos permite recuperar nuestra referencia
						controllerP.colocarDniProfesor(dni);
						controllerP.cargarModulosProfe();
						
						//Creamos la ventana que va a abrir
						Stage stage = new Stage();
						stage.setTitle("Ventana Profesor");
						stage.setScene(new Scene(root));
						stage.show();
						
						//Hay que cerrar esta ventana
						Stage logeo = (Stage) btnEntrar.getScene().getWindow();
						logeo.close();
					}else {mostrarAlerta("Error de acceso", "Nombre o contraseña incorrectos.", AlertType.ERROR);}

				
				
				
				
			} catch (SQLException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}

    }

    @FXML
    void btnLimpiar(ActionEvent event) {
    	tfUsuario.clear();
    	pfContraseña.clear();
    	cbCargo.getSelectionModel().clearAndSelect(0);

    }

    @FXML
    void btnSalir(ActionEvent event) {
        Stage stage = (Stage) btnSalir.getScene().getWindow();
        stage.close();
    }

    
    private void mostrarAlerta(String titulo, String mensaje, AlertType tipo) {
    	Alert alert = new Alert(tipo);
    	alert.setTitle(titulo);
    	alert.setHeaderText(null);
    	alert.setContentText(mensaje);
    	alert.showAndWait();
    	
    }
    
}
