package no_he_sido_yo;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.TextArea;
import java.awt.Toolkit;
import java.awt.event.HierarchyListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.HierarchyEvent;
import java.awt.Color;

public class AnnadirPreg extends JFrame {

	private JPanel contentPane;
	private JComboBox cbTipo;
	public JComboBox cbTem;
	private JTextArea txtResp1;
	private JTextArea txtResp2;
	private JTextArea txtResp3;
	private JTextArea txtResp4;
	
	private String tipoPregunta = null;
	private int tematicaElegida = 1;
	
	private JLabel lblPersona1;
	private JLabel lblPersona2;
	private JLabel lblPersona3;
	private JLabel lblPersona4;
	private JTextArea txtPreg;
	private JLabel lblFondo;
	private JComboBox cbResp1;
	private JComboBox cbResp2;
	private JComboBox cbResp3;
	private JComboBox cbResp4;
	private JLabel lblImgResp1;
	private JLabel lblImgResp2;
	private JLabel lblImgResp3;
	private JLabel lblImgResp4;
	private JLabel lblNewLabel;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AnnadirPreg frame = new AnnadirPreg();
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
	public AnnadirPreg() throws MalformedURLException {
		setTitle("No he sido yo");
		setIconImage(Toolkit.getDefaultToolkit().getImage(new URL(Conexion.RUTA_IMAGENES_JUEGO + "carta.png")));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 708, 560);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		//**********BOTÓN GUARDAR**********
		JButton btnGuardarPreg = new JButton("Guardar");
		btnGuardarPreg.setFocusable(false);
		btnGuardarPreg.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean ejecutarProcedimeto = false;
				if (tipoPregunta == null) {//COMPRUEBA QUE SE HA ELEGIDO UN TIPO DE PREGUNTA
					JOptionPane.showMessageDialog(null, "Tienes que marcar el tipo de pregunta a elegir" ,"ERROR", 0); //NO SE HA ESCOGIDO NINGÚN TIPO DE PREGUNTA 
				} else {
					String pregunta = txtPreg.getText().toString().trim(); 
					String resp1 = " ";
					String resp2 = " ";
					String resp3 = " ";
					String resp4 = " ";
					if (pregunta.length() == 0 || pregunta.length() > 250) {//PONE LÍMITE DE CARACTERES A LA PREGUNTA
						JOptionPane.showMessageDialog(null, "La pregunta tiene que tener un tamaño de caractéres entre 0 y 250" ,"ERROR", 0);
					} else {
						System.out.println(tematicaElegida + "\t" + tipoPregunta);
						if (tipoPregunta.equals("respFrases")) { //PARA LA PREGUNTAS DE TIPO FRASES SE COGE EL TEXTO, SE PASA A STRING Y SE QUITAN LOS ESPACIOS EN BLANCO DEL PRINCIPIO Y DEL FINAL
							resp1 = txtResp1.getText().toString().trim();
							resp2 = txtResp2.getText().toString().trim();
							resp3 = txtResp3.getText().toString().trim();
							resp4 = txtResp4.getText().toString().trim();
							if (resp1.length() == 0 || resp2.length() == 0) {//ES OBLIGATORIO ESCRIBIR AL MENOS DOS RESPUESTAS
								JOptionPane.showMessageDialog(null, "Al menos las dos primeras cartas tienen que tener respuesta" ,"ERROR", 0);
							} else {
								ejecutarProcedimeto = true; 
							}
						} else if (tipoPregunta.equals("respImg")) { //COGE DEL CB LOS TITULOS DE LA IMAGEN Y LO PASA STRING QUE IRA A LA BBDD
							ejecutarProcedimeto = true;
							resp1 = cbResp1.getSelectedItem().toString();
							resp2 = cbResp2.getSelectedItem().toString();
							resp3 = cbResp3.getSelectedItem().toString();
							resp4 = cbResp4.getSelectedItem().toString();
						} else if (tipoPregunta.equals("respJug")) { //SOLO ES NECESARIO LA PREGUNTA
							ejecutarProcedimeto = true;
						}
					}
					if (ejecutarProcedimeto) {//PARA QUE NO SE LLENE LA BASE DE DATOS CON NULOS
						if (resp1.length() == 0) {
							resp1 = " ";
						}
						if (resp2.length() == 0) {
							resp2 = " ";
						}
						if (resp3.length() == 0) {
							resp3 = " ";
						}
						if (resp4.length() == 0) {
							resp4 = " ";
						}
						if (Conexion.insertarPregunta(pregunta, resp1, resp2, resp3, resp4, tematicaElegida, tipoPregunta)) { //INSERTA LA PREGUNTA EN LA BASE DE DATOS
							JOptionPane.showMessageDialog(null, "La pregunta se añadió con éxito" ,null, 1);
						} else {
							JOptionPane.showMessageDialog(null, "La pregunta no se pudo añadir, revise su conexion" ,"ERROR", 0);
						}
					}
				}
			}
		});
		
		lblNewLabel = new JLabel("Crea una pregunta");
		lblNewLabel.setBackground(Color.WHITE);
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 17));
		lblNewLabel.setBounds(264, 35, 181, 22);
		contentPane.add(lblNewLabel);
		btnGuardarPreg.setFont(new Font("Tahoma", Font.PLAIN, 18));
		btnGuardarPreg.setBounds(281, 448, 121, 42);
		contentPane.add(btnGuardarPreg);
		
		txtResp1 = new JTextArea(2,2);
		txtResp1.setWrapStyleWord(true);
		txtResp1.setLineWrap(true);
		txtResp1.setBounds(42, 264, 92, 134);
		contentPane.add(txtResp1);
	
		
		txtResp2 = new JTextArea();
		txtResp2.setWrapStyleWord(true);
		txtResp2.setLineWrap(true);
		txtResp2.setBounds(203, 264, 92, 134);
		contentPane.add(txtResp2);
		
		
		txtResp3 = new JTextArea();
		txtResp3.setWrapStyleWord(true);
		txtResp3.setLineWrap(true);
		txtResp3.setBounds(391, 264, 92, 134);
		contentPane.add(txtResp3);
		
		txtResp4 = new JTextArea();
		txtResp4.setWrapStyleWord(true);
		txtResp4.setLineWrap(true);
		txtResp4.setBounds(545, 264, 92, 134);
		contentPane.add(txtResp4);
		
		lblPersona1 = new JLabel("");
		lblPersona1.setBounds(46, 269, 92, 129);
		lblPersona1.setIcon(new ImageIcon(new URL(Conexion.RUTA_IMAGENES_JUEGO + "persona1.jpg")));
		contentPane.add(lblPersona1);
		
		lblPersona2 = new JLabel("");
		lblPersona2.setBounds(203, 269, 92, 129);
		lblPersona2.setIcon(new ImageIcon(new URL(Conexion.RUTA_IMAGENES_JUEGO + "persona2.jpg")));
		contentPane.add(lblPersona2);
		
		lblPersona3 = new JLabel("");
		lblPersona3.setBounds(391, 269, 92, 129);
		lblPersona3.setIcon(new ImageIcon(new URL(Conexion.RUTA_IMAGENES_JUEGO + "persona3.jpg")));
		contentPane.add(lblPersona3);
		
		lblPersona4 = new JLabel("");
		lblPersona4.setBounds(545, 269, 92, 129);
		lblPersona4.setIcon(new ImageIcon(new URL(Conexion.RUTA_IMAGENES_JUEGO + "persona4.jpg")));
		contentPane.add(lblPersona4);
		

		
		
		cbTipo = new JComboBox();
		
		cbResp1 = new JComboBox();
		cbResp2 = new JComboBox();
		cbResp3 = new JComboBox();
		cbResp4 = new JComboBox();
		lblImgResp1 = new JLabel("");
		lblImgResp2 = new JLabel("");
		lblImgResp3 = new JLabel("");
		lblImgResp4 = new JLabel("");
		lblImgResp1.setBounds(39, 264, 95, 134);
		contentPane.add(lblImgResp1);
		
		lblImgResp2.setBounds(200, 264, 95, 134);
		contentPane.add(lblImgResp2);
		
		lblImgResp3.setBounds(388, 264, 95, 134);
		contentPane.add(lblImgResp3);
		
		lblImgResp4.setBounds(542, 264, 95, 134);
		contentPane.add(lblImgResp4);		
		
	/********************************ESCUCHADOR EN EL CB PARA QUE CUANDO CLICKES SE MUESTRE LA IMAGEN****************************************/
		cbResp1.addActionListener (new ActionListener () {
			@Override
			public void actionPerformed(ActionEvent e) {
				String nombreFoto = cbResp1.getSelectedItem().toString();
				try {
					lblImgResp1.setIcon(new ImageIcon(new URL(Conexion.RUTA_PREGUNTAS_IMAGENES + nombreFoto)));
				} catch (MalformedURLException e1) {
					
					e1.printStackTrace();
				}
			}
		});
		cbResp2.addActionListener (new ActionListener () {
			@Override
			public void actionPerformed(ActionEvent e) {
				String nombreFoto = cbResp2.getSelectedItem().toString();
				try {
					lblImgResp2.setIcon(new ImageIcon(new URL(Conexion.RUTA_PREGUNTAS_IMAGENES + nombreFoto)));
				} catch (MalformedURLException e1) {
					
					e1.printStackTrace();
				}
			}
		});
		cbResp3.addActionListener (new ActionListener () {
			@Override
			public void actionPerformed(ActionEvent e) {
				String nombreFoto = cbResp3.getSelectedItem().toString();
				try {
					lblImgResp3.setIcon(new ImageIcon(new URL(Conexion.RUTA_PREGUNTAS_IMAGENES + nombreFoto)));
				} catch (MalformedURLException e1) {
					
					e1.printStackTrace();
				}
			}
		});
		cbResp4.addActionListener (new ActionListener () {
			@Override
			public void actionPerformed(ActionEvent e) {
				String nombreFoto = cbResp4.getSelectedItem().toString();
				try { 
					lblImgResp4.setIcon(new ImageIcon(new URL(Conexion.RUTA_PREGUNTAS_IMAGENES + nombreFoto)));
				} catch (MalformedURLException e1) {
					
					e1.printStackTrace();
				}
			}
		});
		/****************************************************************************************************************************************/
		
		//**********CARGA LAS EN EL COMBOBOX**********
		String[] fotosPreguntas = Conexion.nombresImagenesPreguntas();
		
		
		for (String foto : fotosPreguntas) {
			cbResp1.addItem(foto);
			cbResp2.addItem(foto);
			cbResp3.addItem(foto);
			cbResp4.addItem(foto);
		}
		
		//**********AÑADE UN ESCUCHARDOR AL COMBOBOX DEL TIPO DE PREGUNTA**********
		cbTipo.addActionListener (new ActionListener () {
			@Override
			public void actionPerformed(ActionEvent e) {
				String opcion = cbTipo.getSelectedItem().toString();
				//System.out.println(opcion);
				if (opcion.equals("Respuestas de jugador")) {
					opcion = "respJug";
					cartasTexto(false);
					cartasJugadores(true);
					elegirImagenesRespuestas(false);
				} else if (opcion.equals("Respuestas de texto")) {
					opcion = "respFrases";
					cartasTexto(true);
					cartasJugadores(false);
					elegirImagenesRespuestas(false);
				} else if (opcion.equals("Respuestas de imágenes")) {
					opcion = "respImg";
					cartasTexto(false);
					cartasJugadores(false);
					elegirImagenesRespuestas(true);
				} else {
					opcion = null;
					cartasTexto(false);
					cartasJugadores(false);
					elegirImagenesRespuestas(false);
				}
				tipoPregunta = opcion; //VARIABLE GLOBAL QUE SE LE PASARA MAS TARDE AL PREOCEDIMIENTO ALMACENADO DE AÑADIR PREGUNTA
			} 
		});
		cbTipo.setBounds(20, 163, 232, 22);
		contentPane.add(cbTipo);
		cbTipo.addItem("Tipo de pregunta");
		cbTipo.addItem("Respuestas de jugador");
		cbTipo.addItem("Respuestas de texto");
		cbTipo.addItem("Respuestas de imágenes");
		
		
		
		
		
		JLabel carta_1 = new JLabel("");
		carta_1.setBounds(23, 238, 128, 185);
		
		carta_1.setHorizontalAlignment(SwingConstants.CENTER);
		carta_1.setIcon(new ImageIcon(new URL(Conexion.RUTA_IMAGENES_JUEGO + "carta-java.png")));
		contentPane.add(carta_1);
		
		JLabel carta_2 = new JLabel("");
		carta_2.setHorizontalAlignment(SwingConstants.CENTER);
		carta_2.setBounds(186, 238, 128, 185);
		carta_2.setHorizontalAlignment(SwingConstants.CENTER);
		carta_2.setIcon(new ImageIcon(new URL(Conexion.RUTA_IMAGENES_JUEGO + "carta-java.png")));
		contentPane.add(carta_2);
		
		JLabel carta_3 = new JLabel("");
		carta_3.setHorizontalAlignment(SwingConstants.CENTER);
		carta_3.setBounds(372, 238, 128, 185);
		carta_3.setHorizontalAlignment(SwingConstants.CENTER);
		carta_3.setIcon(new ImageIcon(new URL(Conexion.RUTA_IMAGENES_JUEGO + "carta-java.png")));
		contentPane.add(carta_3);
		
		JLabel carta_4 = new JLabel("");
		carta_4.setHorizontalAlignment(SwingConstants.CENTER);
		carta_4.setBounds(525, 238, 128, 185);
		carta_4.setHorizontalAlignment(SwingConstants.CENTER);
		carta_4.setIcon(new ImageIcon(new URL(Conexion.RUTA_IMAGENES_JUEGO + "carta-java.png")));
		contentPane.add(carta_4);
		
		txtPreg = new JTextArea(4,4);
		txtPreg.setWrapStyleWord(true);
		txtPreg.setLineWrap(true);
		txtPreg.setBounds(30, 61, 650, 91);
		//TextPrompt placeholder = new TextPrompt("Apellido Paterno", textField);
	    //placeholder.changeAlpha(0.75f);
	    //placeholder.changeStyle(Font.ITALIC);
		contentPane.add(txtPreg);
		
		cbTem = new JComboBox();
		cbTem.setBounds(438, 163, 232, 22);
		contentPane.add(cbTem);
		
		//**********BOTÓN VOLVER ATRÁS**********
		JButton btnVolverATematicas = Conexion.button_volver(); 
		btnVolverATematicas.setBounds(0, 0, 40, 40);
		btnVolverATematicas.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
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
		
		contentPane.add(btnVolverATematicas);
		
		cbResp1.setBounds(20, 205, 121, 22);
		contentPane.add(cbResp1);
		
		
		cbResp2.setBounds(186, 205, 121, 22);
		contentPane.add(cbResp2);
		
		
		cbResp3.setBounds(372, 205, 121, 22);
		contentPane.add(cbResp3);
		
		
		cbResp4.setBounds(525, 205, 121, 22);
		contentPane.add(cbResp4);
		
		lblFondo = new JLabel("");
		lblFondo.setBounds(0, 0, 692, 521);
		contentPane.add(lblFondo);
		
		

		
		
		//ESCUCHADOR PARA QUE CUANDO SE SELLECIONE LA TEMÁTICA SE PONGA EL FONDO DE CADA TEATICA
		cbTem.addActionListener (new ActionListener () {
			@Override
			public void actionPerformed(ActionEvent e) {
				tematicaElegida = cbTem.getSelectedIndex() + 1;
				String temElegida = cbTem.getSelectedItem().toString().trim().toLowerCase();
				// aquí es dónde se podía poner de fondo la imágen de la temática
				try {
					lblFondo.setIcon(new ImageIcon(new URL(Conexion.RUTA_TEMATICAS_IMAGENES + temElegida +".jpg")));
				} catch (MalformedURLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
		String[] tematicas = Conexion.cargarTematicas();
		for (String tematica : tematicas) {
			cbTem.addItem(tematica);
		}
	}
	
	
	
	private void cartasTexto(boolean mostrarOcultar) {
		txtResp1.setVisible(mostrarOcultar);
		txtResp2.setVisible(mostrarOcultar);
		txtResp3.setVisible(mostrarOcultar);
		txtResp4.setVisible(mostrarOcultar);
	}
	
	private void cartasJugadores(boolean mostrarOcultar) {
		lblPersona1.setVisible(mostrarOcultar);
		lblPersona2.setVisible(mostrarOcultar);
		lblPersona3.setVisible(mostrarOcultar);
		lblPersona4.setVisible(mostrarOcultar);
	}
	
	private void elegirImagenesRespuestas(boolean mostrarOcultar) {
		cbResp1.setVisible(mostrarOcultar);
		cbResp2.setVisible(mostrarOcultar);
		cbResp3.setVisible(mostrarOcultar);
		cbResp4.setVisible(mostrarOcultar);
		lblImgResp1.setVisible(mostrarOcultar);
		lblImgResp2.setVisible(mostrarOcultar);
		lblImgResp3.setVisible(mostrarOcultar);
		lblImgResp4.setVisible(mostrarOcultar);
	}
}
