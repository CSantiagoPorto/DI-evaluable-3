package controller;


import java.sql.ResultSet;
import java.sql.SQLException;

import DataBase.GestionBD;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;



public class VentanaProfesorController {
	
	

    @FXML
    private Button btnGuardar;

    @FXML
    private Button btnSalir;

    @FXML
    private ComboBox<String> cbModulos;
    @FXML
    private TextField tfNota;

    @FXML
    private ComboBox<String> cbSeleccionarAlumno;

    @FXML
    private Label lbCabecera;

    @FXML
    private Label lbModulos;

    @FXML
    private Label lbNota;
    
    
    private String dniProfesor;
	
	public void colocarDniProfesor(String dni) {
		this.dniProfesor= dni;
		//Con esto guardaré el dni del profesor como variable de clase
	}

    @FXML
    void SeleccionarAlumno(ActionEvent event) {
    	String modulo=cbModulos.getValue();
    	String alumno= cbSeleccionarAlumno.getValue();//guardamos el alumno
    	String[]partes= alumno.split(" ");
    	String nombre= partes[0];
    	String apellido=partes[1];
    	GestionBD gbd = new GestionBD();
    	try {
    		String idAsig= gbd.obtenerIdAsignaturaPorNombre(modulo);
    		ResultSet rs=gbd.obtenerDniNombreCompleto(nombre, apellido);
    		if(rs.next()) {
    			String dniAlumno = rs.getString("dni_alumno");
    			ResultSet rsNota= gbd.notaAlumnoAsignatura(dniAlumno, idAsig);
    			//Tengo la nota del alumno para la asignatura pero ahora hay que mostrarla en el tfNota
    			if(rsNota.next()) {
    				tfNota.setText(rsNota.getString("calificacion"));
    				//Colocamos la nota con set. Y la vamos a obtener de rs, que accede a ella como String en la tabla
    				
    			}
    		}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    }

    @FXML
    void guardarNota(ActionEvent event) {
    	String modulo = cbModulos.getValue();
    	String nota= tfNota.getText();
    	String alumno = cbSeleccionarAlumno.getValue();
    	String [] partes= alumno.split(" ");
    	String nombre =partes[0];
    	String apellido=partes[1];
    	
    	GestionBD gbd = new GestionBD();
    	
    	try {
    		String idAsig = gbd.obtenerIdAsignaturaPorNombre(modulo);//Obtengo el id modulo
    		
			ResultSet dniAlumno= gbd.obtenerDniNombreCompleto(nombre,apellido) ;//obtengo el dni
			if(dniAlumno!=null && dniAlumno.next()) {
				String dni= dniAlumno.getString("dni_alumno");
				String idNota=gbd.generarIdNota(dni, idAsig);
				double notaNueva;
				notaNueva= Double.parseDouble(nota);
				boolean guardado= gbd.ponerNota(idNota,dni, idAsig, nota);
				if(guardado) {
					mostrarAlerta("Nota guardada correctamente","Proceso completado con éxtio", AlertType.INFORMATION);
				}
				
				
				
				
			}
			if(dniAlumno==null) {
				mostrarAlerta("No se encontró un alumno con es dni", " Inténtenlo de nuevo", AlertType.ERROR);
				return;
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    }

    @FXML
    void salir(ActionEvent event) {
    	((Stage) btnSalir.getScene().getWindow()).close();

    }

    @FXML
    void seleccionarModulo(ActionEvent event) {
    	cbSeleccionarAlumno.getItems().clear();//Necesito que se limpie para que no se queden los alumnos
    	//de otras asignaturas
    	
    	try {
    		String modulo=cbModulos.getValue(); //Guardamos en un String el valor elegido
        	GestionBD gbd= new GestionBD();
        	//Tengo el nombre de la asignatura, entonces a partir de eso necesito obtener su id
        	String idAsig= gbd.obtenerIdAsignaturaPorNombre(modulo);
        	//Ya tengo el id de la asignatura. Ahora necesito obtener los alumnos matriculados en esa asignatura.
        	//Ese es el valor que tengo que pasarle al siguiente cb, por tanto necesito un ResultSet
			ResultSet rs= gbd.obtenerAlumnoAsignatura(idAsig);
			//Ahora ya tengo en mi variable el acceso a los alumnos. Ahora es ir recorriendo la bd
			//y añadiéndolo a nuestro combobox
			while(rs.next()) {
				String nombreAlumno =rs.getString("nombre")+" "+ rs.getString("apellidos");
				cbSeleccionarAlumno.getItems().add(nombreAlumno);
				//Recorremos con next mientras haya registros. Guardamos en el String el nombre + apellidos
				//obtenemos los items y añadimos en el cb de los alumnos
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	

    }

    @FXML
    void tfNota(ActionEvent event) {

    }
    public void cargarModulosProfe() {
    	GestionBD gbd = new GestionBD();
    	try {
			ResultSet rs= gbd.modulosProfesor(dniProfesor);
			while(rs.next()) {
				cbModulos.getItems().add(rs.getString("denominacion"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }
    private void mostrarAlerta(String titulo, String mensaje, AlertType tipo) {
    	Alert alert = new Alert(tipo);
    	alert.setTitle(titulo);
    	alert.setHeaderText(null);
    	alert.setContentText(mensaje);
    	alert.showAndWait();
    	
    }
    
    
    

}
