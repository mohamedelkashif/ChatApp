package client_app;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JLabel;

public class groupGui {

	private JFrame frame;
	private JTextField textField;
	private JTextField textField_1;

	/**
	 * Launch the application.
	 */
	public static void main() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					groupGui window = new groupGui();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public groupGui() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		textField = new JTextField();
		textField.setBounds(10, 12, 86, 20);
		frame.getContentPane().add(textField);
		textField.setColumns(10);
		
		JTextPane textPane = new JTextPane();
		textPane.setBounds(10, 68, 112, 170);
		frame.getContentPane().add(textPane);
		
		JTextPane textPane_1 = new JTextPane();
		textPane_1.setBounds(148, 40, 276, 170);
		frame.getContentPane().add(textPane_1);
		
		JButton btnSend = new JButton("Send");
		btnSend.setBounds(158, 221, 89, 23);
		frame.getContentPane().add(btnSend);
		
		textField_1 = new JTextField();
		textField_1.setBounds(257, 221, 167, 20);
		frame.getContentPane().add(textField_1);
		textField_1.setColumns(10);
		
		JLabel lblActiveGroups = new JLabel("Active User in Group");
		lblActiveGroups.setBounds(10, 43, 103, 14);
		frame.getContentPane().add(lblActiveGroups);
		
		JButton btnNewButton = new JButton("Ok for GroupName");
		btnNewButton.setBounds(116, 11, 131, 23);
		frame.getContentPane().add(btnNewButton);
	}
}
