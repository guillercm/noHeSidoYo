package no_he_sido_yo;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.MalformedURLException;
import java.net.URL;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.Toolkit;

import javax.swing.ScrollPaneConstants;
import javax.swing.JTextField;

public class Usuarios extends JFrame {

	private JPanel contentPane;
	private DefaultTableModel modelo;
	private JTable tabla;
	private JLabel lblTematica;
	private JButton btnEliminar;
	private String tem = null;
	private static int idUsuario = 0;
	private JPasswordField txtContra;
	private JButton btnCambiarContrasena;
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Usuarios frame = new Usuarios();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 * @throws MalformedURLException 
	 */
	public Usuarios() throws MalformedURLException {
		setTitle("No he sido yo");
		setIconImage(Toolkit.getDefaultToolkit().getImage(new URL(Conexion.RUTA_IMAGENES_JUEGO + "carta.png")));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 708, 560);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		//**********BOTÓN VOLVER ATRÁS**********
		JButton btnVolverMenu = Conexion.button_volver();
		btnVolverMenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				Menu frame;
				try {
					frame = new Menu();
					frame.setVisible(true);
				} catch (MalformedURLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
		});
		btnVolverMenu.setBounds(10, 11, 42, 36);
		contentPane.add(btnVolverMenu);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(37, 49, 618, 429);
		contentPane.add(scrollPane);
		 
		//**********CREACION DE LA TABLA DE USUARIOS**********
		tabla = new JTable();
		tabla.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int fila = tabla.rowAtPoint(e.getPoint()); //devuelve la fila donde se clicka
				//System.out.println(fila);
				int columna = 0;
				if ((fila>-1)&&(columna>-1)) {
					idUsuario = (int) modelo.getValueAt(fila, columna);//TE DEVUELVE EL IDUSUARIO QUE HAS PICHADO COMO VARIABLE DE LA CLASE
					btnEliminar.setEnabled(true);
					btnCambiarContrasena.setEnabled(true);
					
				} 
			}
		});
		scrollPane.setViewportView(tabla);
		
		modelo  = new DefaultTableModel();
		//**********SE AÑADEN LAS COLUMNAS A LA TABLA*********
		modelo.addColumn("Id");
		modelo.addColumn("Nombre");
		modelo.addColumn("Contraseña");
		modelo.addColumn("Perfil");
		
		tabla.setModel(modelo);
		
		lblTematica = new JLabel("Usuarios");
		lblTematica.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblTematica.setBounds(262, 11, 169, 32);
		contentPane.add(lblTematica);
		
		btnEliminar = new JButton("Eliminar");
		btnEliminar.setFocusable(false);
		btnEliminar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (idUsuario != 0) {
					if (idUsuario !=1) {
						if (Conexion.eliminarUsuario(idUsuario)) {
							JOptionPane.showMessageDialog(null, "Se ha eliminado con exito","Todo ok!!", 1);
							idUsuario = 0;
							btnEliminar.setEnabled(false);
							btnCambiarContrasena.setEnabled(false);
							rellenar_tabla();
						} else {
							JOptionPane.showMessageDialog(null, "No se ha podido eliminar al usuario, compruebe tu conexión","ERROR!!", 0);

						}               
					}else {
						JOptionPane.showMessageDialog(null, "No puedes eliminar al admin, para eliminarlo te tendras que batir en un duelo con el con espadas","ERROR!!", 0);
					}
				}
			}
		});
		btnEliminar.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnEliminar.setBounds(94, 489, 103, 23);
		contentPane.add(btnEliminar);
		btnEliminar.setEnabled(false);
		
		btnCambiarContrasena = new JButton("Cambiar Contrase\u00F1a");
		btnCambiarContrasena.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String nuevaContra = txtContra.getText().toString();
				if (idUsuario != 0) {
						if (Conexion.cambiarContraUsuario(idUsuario, nuevaContra)) {
							JOptionPane.showMessageDialog(null, "Se cambió la contraseña satisfactoriamente","Todo ok!!", 1);
							txtContra.setText("");
						} else {
						// error
							JOptionPane.showMessageDialog(null, "No se ha podido cambiar la contraseña, comprueba tu conexión","ERROR!!", 0);
							txtContra.setText("");
					}               
				}
			}
		});
		btnCambiarContrasena.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnCambiarContrasena.setFocusable(false);
		btnCambiarContrasena.setEnabled(false);
		btnCambiarContrasena.setBounds(446, 489, 209, 23);
		contentPane.add(btnCambiarContrasena);
		
		txtContra = new JPasswordField();
		txtContra.setBounds(308, 489, 130, 22);
		contentPane.add(txtContra);
		txtContra.setColumns(10);
		rellenar_tabla();
	}
	
	//MÉTODO PARA LLENAR LA TABLA DE USUARIOS
	public void rellenar_tabla() {
		String sql = "CALL CargarUsuarios();";
		//System.out.println(sql);		
		try {
			//Statement necesario para ir a la base de datos
			Connection con = Conexion.getConexion();
			Statement comando = con.createStatement();
			comando.execute(sql);
			ResultSet resultado = comando.executeQuery(sql);
			Object[] fila = new Object[4];
			/*ELIMINA LAS FILAS DE LA TABLA PARA QUE CUANDO SE RECARGUE NO SE VUELVAN A CARGAR*/
			int longi = modelo.getRowCount();
			for (int i = 0; i < longi; i++) {
				modelo.removeRow(0);
			}
			while (resultado.next()) {
				//lo que hay entre comillas son lo campos de la base de datos
				fila[0] = resultado.getInt("id_usuario");
				fila[1] = resultado.getString("nombre");
				fila[2] = "**********************";
				fila[3] = resultado.getString("img_perfil");
				modelo.addRow(fila);
				//System.out.println(resultado.getString("user"));
				//modelo.removeRow();
			}
		} catch (SQLException e2) {
			// ERRORES
			System.out.println(e2.getMessage().toString());
		}
	}
}
