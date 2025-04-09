package controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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
     String[] items= {"Elige", "Alumno", "Profesor"};
     cbCargo.getItems().addAll(items);
     
    }

    @FXML
    void btnEntrar(ActionEvent event) {
    	//Para entrar lo primero es obtener la información introducida por el usuario
    	String alumnoIngresado =tfUsuario.getText();
    	String contraseña=pfContraseña.getText();
    	String cargo = (String) cbCargo.getSelectionModel().getSelectedItem();

    }

    @FXML
    void btnLimpiar(ActionEvent event) {

    }

    @FXML
    void btnSalir(ActionEvent event) {
    	btnSalir.setOnAction(e->closeApplication());

    }
    private void closeApplication() {//Este método cierra la app
    	Stage stage = (Stage) btnSalir.getScene().getWindow();
    	stage.close();
    	
    	
    }
    private static final String ALUMNO ="carla";
    private static final String CONTRASEÑA= "uem";

}
