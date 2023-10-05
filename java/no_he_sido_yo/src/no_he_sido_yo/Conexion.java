/*
 * Esta clase la hemos creado para poder tener las rutas de los archivos, 
 * y los metodos de las llamadas a los procedimientos almacenados
 * */

package no_he_sido_yo;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Conexion {
	//*****************VARIABLES PARA LA CONEXIÓN A LA BASE DE DATOS*****************
    private final String CONTROLADOR = "com.mysql.cj.jdbc.Driver"; //FINAL SIRVE PARA QUE SEA ESE SU ÚNICO VALOR Y NO SE PUEDA CAMBIAR
    private final static String NOMBRE_BASE_DE_DATOS = "no_he_sido_yo"; //STATIC 
    private final static String URL = "jdbc:mysql://localhost/" + NOMBRE_BASE_DE_DATOS;
    
    private final static String USUARIO = "Jaime";
    private final static String CLAVE = "1234";
    //private final static String USUARIO = "root";
    //private final static String CLAVE = "";
   
    //***************RUTAS JUEGO******************/
    //public final static String RUTA = "http://localhost/webC/proyecto/";
    public final static String RUTA = "http://nohesidoyoo.000webhostapp.com/";    
    public final static String RUTA_IMAGENES_JUEGO = RUTA + "multimedia/juego/";
    public final static String RUTA_PREGUNTAS_IMAGENES = RUTA_IMAGENES_JUEGO + "preguntas/";
    public final static String RUTA_TEMATICAS_IMAGENES = RUTA_IMAGENES_JUEGO + "tematicas/java/";
    public final static String RUTA_PERFILES_JUGADORES = RUTA + "multimedia/perfiles/";

    
    //***************MÉTODO PARA LA CONEXIÓN A LA BASE DE DATOS***************
    public static Connection getConexion() {
    	try {
			Connection conexion = DriverManager.getConnection(URL, USUARIO, CLAVE);
			return conexion;
		} catch (SQLException e) {
			return null;
		}
    }
    
    //**********MÉTODO PARA OBTENER UN SYSO CON TODAS LAS IMAGENES ALMACENADAS PARA LUEGO METERLA EN UN ARRAY Y QUE APAREZCAN EN LA SALA DE AÑADIR PREGUNTAS**********
    public static String[] nombresImagenesPreguntas() {
		File dir = new File("C:/wamp64/www/Proyecto1C/6_junio/15-06/multimedia/juego/preguntas");
		String[] ficheros = dir.list();
//		System.out.println();
//		for (String foto : ficheros) {
//			System.out.print("\"" + foto + "\", ");
//		}
		//String[] fotos = {"alcoholico.jpg", "animal1.jpg", "animal10.jpg", "animal11.jpg", "animal12.jpg", "animal2.jpg", "animal3.jpg", "animal4.jpg", "animal5.jpg", "animal6.jpg", "animal7.jpg", "animal8.jpg", "animal9.jpg", "atraco1.jpg", "atraco2.jpg", "atraco3.jpg", "atraco4.jpg", "ave1.jpg", "ave2.jpg", "ave3.jpg", "ave4.jpg", "aventurero.png", "bagabundo.png", "bambie.jpg", "botella1.jpg", "botella2.jpg", "botella3.jpg", "botella4.jpg", "brujula.png", "cactus.png", "camiseta1.jpg", "camiseta2.jpg", "camiseta3.jpg", "camiseta4.jpg", "cansado.png", "carrera.jpg", "castillo1.jpg", "castillo2.jpg", "castillo3.jpg", "castillo4.jpg", "chabola.jpg", "chuleta1.jpeg", "chuleta2.jpeg", "chuleta3.jpg", "chuleta4.jpg", "chuleton.png", "cristalcorazon.jpg", "cristalmano.jpeg", "cristalpene.jpg", "cristalsol.jpg", "deportes1.jpg", "deportes2.jpg", "deportes3.jpg", "deportes4.jpg", "elefantes.jpeg", "escopeta.png", "estuche1.jpg", "estuche2.jpg", "estuche3.jpeg", "estuche4.jpeg", "flotador1.jpeg", "flotador2.jpeg", "flotador3.jpg", "flotador4.jpg", "gacela.jpeg", "globoterra.jpg", "gordo.jpg", "gorra.jpg", "hawaii.jpg", "it.jpg", "jurado1.jpg", "jurado2.jpg", "jurado3.jpg", "jurado4.jpg", "malabares.jpg", "maleta1.jpg", "maleta2.jpg", "maleta3.jpg", "maleta4.jpg", "manzana.jpg", "matamosquitos.png", "mates.jpg", "microscopio.jpeg", "monta.jpg", "motosierra.jpg", "navaja.png", "oso.png", "perderseMar.jpg", "pescador1.jpeg", "pez.png", "piramideHumana.jpg", "pistola.jpg", "platillo.jpg", "poli1.png", "poli2.png", "poli3.png", "poli4.png", "problemas1.jpg", "problemas2.jpg", "problemas3.jpg", "problemas4.jpg", "rana.png", "rino.jpg", "seta1.jpg", "seta2.jpeg", "seta3.jpg", "seta4.jpg", "silla.png", "snack1.jpg", "snack2.jpg", "snack3.jpg", "snack4.jpg", "venecia.jpg", "verdura.png", "yeti.jpg", "zapatillas1.jpg", "zapatillas2.jpg", "zapatillas3.jpg", "zapatillas4.jpg", "zombi.png", "zumo.jpg"};
		return ficheros;
    }
    
    //***************COMPROBACION DEL LOGIN***************
    public static int loginAdmin(String pass) { 
    	/*
	     * Este método puede devolver:
	     * 		-404 		-> En caso de que no haya podido ejecutarlo por algún problema de conexión
	     * 		-1   		-> La contraseña la puso mal	
	     * 		Otro numero -> El id del usuario
	    */
    	try {
    		Connection con = Conexion.getConexion();
			CallableStatement sql = con.prepareCall("{call LoginAdmin(?, ? )}");//CALL LoginAdmin('asd',@salida);
			sql.setString(1, pass);
			sql.registerOutParameter(2, Types.INTEGER);
			sql.executeUpdate();
			int respuesta = sql.getInt(2);
			return respuesta;
		} catch (SQLException e) {
			return -404;
		}
    }
    
    /***********************LLAMADA AL PROCEDIMIENTO ALMACENADO DE LAS TEMATICAS***********************/
    public static String[] cargarTematicas() {
    	String cadena = "";
		Connection con = Conexion.getConexion();
		String sql = "CALL CargarTematicas();";
		Statement registro;
		try {
			registro = con.createStatement();
			ResultSet consulta = registro.executeQuery(sql);
//			int id_tematica = 0;
			String nombre = "";
//			String ruta_imagen = "";
			while (consulta.next()) {
				//id_tematica = consulta.getInt("id_tematica");
				nombre = consulta.getString("nombre");
//				System.out.println(nombre);
				//ruta_imagen = consulta.getString("ruta_imagen");
				cadena += nombre + ",";
			}
			cadena = cadena.substring(0, cadena.length() - 1);
//			System.out.println(cadena);
			String[] tema = cadena.split(",");
			//System.out.print("tema[0]: " + tema.length + "\t");
			return tema;
		} catch (Exception e2) {
			return null;
		}
    }
    
    
    /***********************LLAMADA AL PROCEDIMIENTO ALMACENADO DE LOS USUARIOS***********************/
    public String[] cargarUsuarios() {
    	String cadena = "";
    	Connection con = Conexion.getConexion();
		String sql = "CALL CargarUsuarios();";
		Statement registro;
		try {
			registro = con.createStatement();
			ResultSet consulta = registro.executeQuery(sql);
			int id_usuario = 0;
			String nombre = "";
			String ruta_imagen = "";
			while (consulta.next()) {
				id_usuario = consulta.getInt("id_usuario");
				nombre = consulta.getString("nombre");
				System.out.println(nombre);
				ruta_imagen = consulta.getString("img_perfil");
				cadena += nombre + ",";
			}
			cadena = cadena.substring(0, cadena.length() - 1);
			System.out.println(cadena);
			String[] tema = cadena.split(",");
			System.out.print("tema[0]: " + tema.length + "\t");
			return tema;
			
		} catch (Exception e) {
			return null;
		}
    	
    }
    
    /***********************LLAMADA AL PROCEDIMIENTO ALMACENADO PARA ELIMINAR LA PREGUNTA***********************/
    public static boolean eliminarPregunta(int idPregunta) {
    	String cadena = "";
    	Connection con = Conexion.getConexion();
		String sql = "CALL EliminarPregunta('" + idPregunta + "');";
		Statement registro;
		try {
			registro = con.createStatement();
			ResultSet consulta = registro.executeQuery(sql);
			return true;
		} catch (Exception e) {
			return false;
		}
    }    
    
    /***********************LLAMADA AL PROCEDIMIENTO ALMACENADO PARA ELIMINAR LA PREGUNTA***********************/
    public static boolean insertarPregunta(String pregunta, String resp1, String resp2, String resp3, String resp4, int fk_tematica, String tipo) {
    	String cadena = "";
    	Connection con = Conexion.getConexion();
		String sql = "CALL InsertarPregunta('" + pregunta + "','" + resp1 + "','" + resp2 + "','" + resp3 + "','" + resp4 + "','" + fk_tematica + "','" + tipo + "');";
		Statement registro;
		try {
			registro = con.createStatement();
			ResultSet consulta = registro.executeQuery(sql);
			return true;
		} catch (Exception e) {
			return false;
		}
    }
    
    /***********************LLAMADA AL PROCEDIMIENTO ALMACENADO PARA ELIMINAR USUARIO***********************/
    public static boolean eliminarUsuario(int idUsuario) {
    	String cadena = "";
    	Connection con = Conexion.getConexion();
		String sql = "CALL EliminarUsuario('" + idUsuario + "');";
		Statement registro;
		try {
			registro = con.createStatement();
			ResultSet consulta = registro.executeQuery(sql);
			return true;
		} catch (Exception e) {
			return false;
		}
    }
    
    /***********************LLAMADA AL PROCEDIMIENTO ALMACENADO PARA CAMBIAR LA CONTRA DE UN USUARIO***********************/
    public static boolean cambiarContraUsuario(int idUsuario, String nuevaContra) {
    	if (nuevaContra.trim().length() < 6) {
    		JOptionPane.showMessageDialog(null, "La contraseña tiene que tener al menos 6 caracteres","ERROR!!", 0);
    	}
    	Connection con = Conexion.getConexion();
		String sql = "CALL CambiarContra('" + idUsuario + "', '" + nuevaContra + "');";
		Statement registro;
		try {
			registro = con.createStatement();
			ResultSet consulta = registro.executeQuery(sql);
			return true;
		} catch (Exception e) {
			return false;
		}
    }
    
    
    
	//  CONSTRUYE UN STRING DE UNA LLAMADA DE PROCEDIMIENTO ALMACENADO
	  /*public static String call(String nombreProcedimientoAlmacenado,boolean salida, Object... parametros) {
			String llamada = "CALL " + nombreProcedimientoAlmacenado + "(";
			int longi = parametros.length;
		    for (int i=0 ; i < longi; i++) {
		        llamada += "\'" + parametros[i] + "\'" + ",";
		    }
		    if (salida) {
		    	llamada += "@salida);";
		    } else {
		    	llamada = llamada.substring(0,llamada.length()-1);
		    	llamada += ");";
		    }
		    return llamada;
		}*/
    
    
    public static JButton button_volver() throws MalformedURLException {
		JButton btnVolverMenu_ = new JButton("");
		btnVolverMenu_.setBounds(0, 0, 40, 40);
		btnVolverMenu_.setFocusable(false);
		ImageIcon atras = new ImageIcon(new URL(Conexion.RUTA_IMAGENES_JUEGO + "atras.png"));
		btnVolverMenu_.setIcon(atras);
		return btnVolverMenu_;
	}
    
	public static void main(String[] args) {
		//Connection conexion = Conexion.getConexion();
		//System.out.println(conexion);
		//nombresImagenesPreguntas();
	}

}

