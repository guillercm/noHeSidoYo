package no_he_sido_yo;
import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.JInternalFrame;
import java.awt.Toolkit;

public class Login extends JFrame {

	private JPanel contentPane;
	private JPanel panelLogin;
	private JLabel lblLogo;
	private JPasswordField contraAdmin;
	private JButton btnLogin;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Login frame = new Login();
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
	public Login() throws MalformedURLException {
		setTitle("No he sido yo");
		setIconImage(Toolkit.getDefaultToolkit().getImage(new URL(Conexion.RUTA_IMAGENES_JUEGO + "carta.png"))); //ICONO DEL PROGRAMA
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//TAMAÑO FRAME
		setBounds(100, 100, 708, 560);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		panelLogin = new JPanel();
		panelLogin.setBounds(0, 0, 691, 521);
		contentPane.add(panelLogin);
		panelLogin.setLayout(null);
		
		lblLogo = new JLabel("");
		lblLogo.setBounds(0, 0, 691, 281);
		panelLogin.add(lblLogo);
		lblLogo.setHorizontalAlignment(SwingConstants.CENTER);
		
		lblLogo.setIcon(new ImageIcon(new URL(Conexion.RUTA_IMAGENES_JUEGO + "noHeSidoYo.png"))); //IMAGEN DEL TITULO(LOGO)
		
		JLabel lblAdmin = new JLabel("Admin");
		lblAdmin.setBounds(191, 309, 307, 44);
		panelLogin.add(lblAdmin);
		lblAdmin.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblAdmin.setHorizontalAlignment(SwingConstants.CENTER);
		
		contraAdmin = new JPasswordField("123456");
		contraAdmin.setBounds(225, 357, 238, 28);
		panelLogin.add(contraAdmin);
		contraAdmin.setToolTipText("");
		
		btnLogin = new JButton("Entrar");
		btnLogin.setFocusable(false);
		btnLogin.setBounds(293, 396, 103, 37);
		panelLogin.add(btnLogin);
		
		//***************INICIAR SESIÓN EN LA APLICACIÓN***************
		btnLogin.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) {
				@SuppressWarnings("deprecation")
				String contra = contraAdmin.getText().toString();
				//String contra = "123456";
				//String sql = Conexion.setSalida();
				int login = Conexion.loginAdmin(contra);
				//System.out.print(login);
				if (login == -404) {
					JOptionPane.showMessageDialog(null, "Imposible conectarse. Problemas de conexión", "No he sido yo", 2);
				} else if (login == -1){
					JOptionPane.showMessageDialog(null, "Contraseña incorrecta. Vuelva a intentarlo", "No he sido yo", 0);
				} else {
					//Contraseña correcta
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
			}
		});
		btnLogin.setFont(new Font("Tahoma", Font.PLAIN, 16));
	}
}
