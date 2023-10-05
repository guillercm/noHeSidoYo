package no_he_sido_yo;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.net.URL;
import java.awt.event.ActionEvent;

public class Menu extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Menu frame = new Menu();
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
	public Menu() throws MalformedURLException {
		setTitle("No he sido yo");
		setIconImage(Toolkit.getDefaultToolkit().getImage(new URL(Conexion.RUTA_IMAGENES_JUEGO + "carta.png")));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 708, 560);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		//***************BOTÓN USUARIOS***************
		JButton btnUsuarios = new JButton("Usuarios");
		btnUsuarios.setFocusable(false);
		btnUsuarios.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();//SE OCULTA Y SE MUESTRA EL NUEVO
				Usuarios frame;
				try {
					frame = new Usuarios();
					frame.setVisible(true);
				} catch (MalformedURLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
		});
		btnUsuarios.setFont(new Font("Tahoma", Font.PLAIN, 21));
		btnUsuarios.setBounds(177, 77, 331, 111);
		contentPane.add(btnUsuarios);
		
		//***************BOTÓN TEMÁTICAS***************
		JButton btnVerTem = new JButton("Temáticas");
		btnVerTem.setFocusable(false);
		btnVerTem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();//SE OCULTA Y SE MUESTRA EL NUEVO
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
		btnVerTem.setFont(new Font("Tahoma", Font.PLAIN, 21));
		btnVerTem.setBounds(177, 307, 331, 111);
		contentPane.add(btnVerTem);
	}
}
