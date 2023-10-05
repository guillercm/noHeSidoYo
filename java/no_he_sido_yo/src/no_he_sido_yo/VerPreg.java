package no_he_sido_yo;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JFrame;
import javax.swing.JPanel;
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

public class VerPreg extends JFrame {

	private JPanel contentPane;
	private DefaultTableModel modelo;
	private JTable tabla;
	private JLabel lblTematica;
	private JButton btnNewButton;
	private String tem = null;
	private static int idPregunta = 0;
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					VerPreg frame = new VerPreg();
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
	public VerPreg() throws MalformedURLException {
		setTitle("No he sido yo");
		setIconImage(Toolkit.getDefaultToolkit().getImage(new URL(Conexion.RUTA_IMAGENES_JUEGO + "carta.png")));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 708, 560);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		
		//**********BOTÓN VOLVER ATRÁS**********
		JButton btnVolverTem = Conexion.button_volver(); 
		
		btnVolverTem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				Tematicas frame;
				try {
					frame = new Tematicas();
					frame.setVisible(true);
				} catch (MalformedURLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
		});
		btnVolverTem.setBounds(10, 11, 42, 36);
		contentPane.add(btnVolverTem);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(37, 49, 618, 429);
		contentPane.add(scrollPane);
		
		//**********CREACIÓN DE LA TABLA**********
		tabla = new JTable();
		
		tabla.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int fila = tabla.rowAtPoint(e.getPoint()); //devuelve la fila donde se clicka
				//System.out.println(fila);
				int columna = 0;
				if ((fila>-1)&&(columna>-1)) {
					// modelo.getValueAt(fila, columna);
					idPregunta = (int) modelo.getValueAt(fila, columna);
					//System.out.println(idPregunta);
					btnNewButton.setEnabled(true);
					// eliminarPregunta(int idPregunta)
				} 
			}
		});
		scrollPane.setViewportView(tabla);
		
		//**********SE AÑADEN COLUMNAS A LA TABLA**********
		modelo  = new DefaultTableModel();
	
		modelo.addColumn("Id");
		modelo.addColumn("Pregunta");
		modelo.addColumn("Resp1");
		modelo.addColumn("Resp2");
		modelo.addColumn("Resp3");
		modelo.addColumn("Resp4");
		modelo.addColumn("Tipo");
		
		tabla.setModel(modelo);
		
		//**********TAMAÑO DE LAS COLUMNAS**********
		tabla.getColumnModel().getColumn(0).setPreferredWidth(5); 
		tabla.getColumnModel().getColumn(1).setPreferredWidth(150);
		tabla.getColumnModel().getColumn(2).setPreferredWidth(15);
		tabla.getColumnModel().getColumn(3).setPreferredWidth(15);
		tabla.getColumnModel().getColumn(4).setPreferredWidth(15);
		tabla.getColumnModel().getColumn(5).setPreferredWidth(15);
		tabla.getColumnModel().getColumn(6).setPreferredWidth(2);
		
		lblTematica = new JLabel("Temática");
		lblTematica.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblTematica.setBounds(262, 11, 169, 32);
		contentPane.add(lblTematica);
		
		btnNewButton = new JButton("Eliminar");
		btnNewButton.setFocusable(false);
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (idPregunta != 0) {
					if (Conexion.eliminarPregunta(idPregunta)) {
						JOptionPane.showMessageDialog(null, "Se ha eliminado la pregunta con éxito", "Terminado",1);
						idPregunta = 0;
						btnNewButton.setEnabled(false);
						rellenar_tabla(tem);
					} else {
						JOptionPane.showMessageDialog(null, "No se ha podido eliminar la pregunta. Compruebe su conexión", "Error",0);
						
					}
				}
			}
		});
		btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnNewButton.setBounds(295, 487, 103, 23);
		contentPane.add(btnNewButton);
		btnNewButton.setEnabled(false);
	}
	
	//**********RELLENA LA TABLA**********
	public void rellenar_tabla(String tematica) {
		lblTematica.setText(tematica);
		tem = tematica;
		String sql = "CALL VerPreguntas('"+tematica+"');";
		System.out.println(sql);		
		try {
			//Statement necesario para ir a la base de datos
			Connection con = Conexion.getConexion();
			Statement comando = con.createStatement();
			comando.execute(sql);
			ResultSet resultado = comando.executeQuery(sql);
			Object[] fila = new Object[7];
			/*ELIMINA LAS FILAS DE LA TABLA PARA QUE CUANDO SE RECARGUE NO SE VUELVAN A CARGAR*/
			int longi = modelo.getRowCount();
			for (int i = 0; i < longi; i++) {
				modelo.removeRow(0);
			}
			while (resultado.next()) {
				//lo que hay entre comillas son lo campos de la base de datos
				fila[0] = resultado.getInt("id_pregunta");
				fila[1] = resultado.getString("pregunta");
				fila[2] = resultado.getString("resp1");
				fila[3] = resultado.getString("resp2");
				fila[4] = resultado.getString("resp3");
				fila[5] = resultado.getString("resp4");
				fila[6] = resultado.getString("tipo").replace("resp","");
				modelo.addRow(fila);
			}
		} catch (SQLException e2) {
			// ERRORES
			//System.out.println(e2.getMessage().toString());
		}
	}
}
