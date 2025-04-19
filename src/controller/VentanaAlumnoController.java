package controller;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import DataBase.GestionBD;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class VentanaAlumnoController {
	private String dniAlumno;
	private String nombreCompletoAlumno;

    @FXML
    private Button btnSalir;

    @FXML
    private ComboBox<String> cbModulos;

    @FXML
    private Label lbModulos;

    @FXML
    private Label lbNota;

    @FXML
    private TextField tfNota;
    @FXML
    private Label lbCabecera;
    
    
    public void cargarModulos() {
		// Este método carga todos los módulos en los que está matriculado el alumno

		try {
			GestionBD gbd =new GestionBD();
			ResultSet rs= gbd.obtenerAsignaturasDeAlumno(dniAlumno) ;
			while(rs.next()) {
				cbModulos.getItems().add(rs.getString("denominacion"));
			}
			cbModulos.setOnAction(event -> mostrarNota(event));//Evento al cambiar la selección
			//Al cambiar la selección del módulo cambia la nota
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	public void colocarNombreAlumno(String nombre) {
	    lbCabecera.setText("Consultar notas. Alumno: " + nombre);
	}
    

    @FXML
    void mostrarNota(ActionEvent event) {
    	String moduloElegido=cbModulos.getValue();//Obtenemos el módulo elegido
    	if(moduloElegido != null) {
    		
    		try {
    			GestionBD gbd = new GestionBD();
        		String idAsig= gbd.obtenerIdAsignaturaPorNombre(moduloElegido);
				ResultSet rs= gbd.notaAlumnoAsignatura(dniAlumno, idAsig);
				if(rs.next()) {
					tfNota.setText(rs.getString("calificacion"));//Le coloco la nota que en mi base de datos está en la columna calificación
				}else {
					tfNota.setText("");
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}

    }

    @FXML
    void salir(ActionEvent event) {
    	((Stage) btnSalir.getScene().getWindow()).close();

    }

	public void ColocarDniAlumno(String dni) {//Este método me hace falta para pasarle el dni desde la otra clase
		this.dniAlumno=dni;
	}
	
}
