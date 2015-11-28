package client_app;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTextArea;
import java.awt.BorderLayout;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JLabel;

public class p2p {

	private JFrame frame;
	private JTextField txtMessage;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					p2p window = new p2p();
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
	public p2p() {
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
		
		JTextArea textArea = new JTextArea();
		textArea.setBounds(50, 38, 365, 130);
		frame.getContentPane().add(textArea);
		
		txtMessage = new JTextField();
		txtMessage.setBounds(50, 202, 241, 33);
		frame.getContentPane().add(txtMessage);
		txtMessage.setColumns(10);
		
		JButton btnSend = new JButton("Send");
		btnSend.setBounds(321, 206, 117, 25);
		frame.getContentPane().add(btnSend);
		
		JLabel lblChatWithNone = new JLabel("Chat with: None");
		lblChatWithNone.setBounds(50, 12, 225, 14);
		frame.getContentPane().add(lblChatWithNone);
		
		JButton btnClose = new JButton("Close");
		btnClose.setBounds(158, 235, 117, 25);
		frame.getContentPane().add(btnClose);
	}
}
