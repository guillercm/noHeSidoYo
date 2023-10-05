package no_he_sido_yo;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class Tematicas extends JFrame {

	private JPanel contentPane;
	private JPanel panelTemPreg;
	private JButton btnvolverMenu;
	private JPanel panelT;
	private DefaultTableModel modelo;
	private JLabel labelTitTematica;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Tematicas frame = new Tematicas();
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
	public Tematicas() throws MalformedURLException {
		setTitle("No he sido yo");
		setIconImage(Toolkit.getDefaultToolkit().getImage(new URL(Conexion.RUTA_IMAGENES_JUEGO + "carta.png")));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 708, 560);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		panelTemPreg = new JPanel();
		panelTemPreg.setBounds(0, 0, 691, 600);
		contentPane.add(panelTemPreg);
		panelTemPreg.setLayout(null);
		
		//**********BOTÓN VOLVER ATRÁS**********
		JButton btnVolverAMenu = Conexion.button_volver(); 
		btnVolverAMenu.setBounds(0, 0, 40, 40);
		btnVolverAMenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
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
		panelTemPreg.add(btnVolverAMenu);
		
		JLabel lblNewLabel = new JLabel("Temáticas");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 22));
		lblNewLabel.setBounds(86, 0, 219, 59);
		panelTemPreg.add(lblNewLabel);
		//panelTemPreg.setVisible(false);
		
		/****************************CARGA DE LAS TEMATICAS***********************/
		String[] tematicas = Conexion.cargarTematicas(); //DEVUELVE UN ARRAY DE STRING CON LOS NOMBRES DE LAS TEMÁTICAS QUE HAY EN LA BBDD
		int y = 40;
		for (String tematica : tematicas) { //TEMATICA = TEMATICAS[I]
			panelT = getPanelTematica(tematica,y);
			panelTemPreg.add(panelT);		
			y += 60;
		}
		/************************************************************************/
	}
		/******************************************CREACION TEMATICA***************************/
		public JPanel getPanelTematica(String nombreTem, int y) {
			JPanel panelTem = new JPanel();
			panelTem.setBounds(90, y, 536, 63);
			panelTem.setLayout(null);
			
			JLabel lblTem = new JLabel(nombreTem);
			lblTem.setBounds(20, 14, 97, 29);
			panelTem.add(lblTem);
			lblTem.setFont(new Font("Tahoma", Font.PLAIN, 16));
			
			JButton btnVerPreg = new JButton("Ver preguntas");
			btnVerPreg.setFocusable(false);
			btnVerPreg.setBounds(132, 11, 137, 38);
			btnVerPreg.addActionListener(new ActionListener() {
				

				public void actionPerformed(ActionEvent e) {
					dispose();
					VerPreg frame;
					try {
						frame = new VerPreg();
						frame.setVisible(true);
						frame.rellenar_tabla(nombreTem);
					} catch (MalformedURLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
				}
			});
			panelTem.add(btnVerPreg);
			
			JButton btnAnnadirPreg = new JButton("Añadir preguntas");
			btnAnnadirPreg.setFocusable(false);
			btnAnnadirPreg.setBounds(319, 11, 143, 38);
			btnAnnadirPreg.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					dispose();
					AnnadirPreg frame;
					try {
						frame = new AnnadirPreg();
						frame.setVisible(true);
						frame.cbTem.setSelectedItem(nombreTem);
					} catch (MalformedURLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
				}
			});
			panelTem.add(btnAnnadirPreg);
			return panelTem;
		}
}
