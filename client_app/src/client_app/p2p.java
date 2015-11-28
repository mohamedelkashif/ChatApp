package client_app;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JTextArea;
import java.awt.BorderLayout;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JLabel;

public class p2p {

	 private JFrame frame;
	 JTextField txtMessage;
	 JTextArea textArea;
	 JLabel lblChatWithNone;
	 private String type;
	 private Socket client;
	 private int portip;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					p2p window = new p2p();
					window.getFrame().setVisible(true);
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


	public p2p(Socket c, String string) {
		type = string;
		client = c;
	}

	public p2p(int port, String string) {
		type = string;
		portip = port; 
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		setFrame(new JFrame());
		getFrame().setBounds(100, 100, 450, 300);
		getFrame().setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getFrame().getContentPane().setLayout(null);
		
		textArea = new JTextArea();
		textArea.setBounds(50, 38, 365, 130);
		getFrame().getContentPane().add(textArea);
		
		txtMessage = new JTextField();
		txtMessage.setBounds(50, 202, 241, 33);
		getFrame().getContentPane().add(txtMessage);
		txtMessage.setColumns(10);
		
		JButton btnSend = new JButton("Send");
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(type.equals("server"))
				{
					
				}
				else if(type.equals("client"))
				{
					
				}
			}
		});
		btnSend.setBounds(321, 206, 117, 25);
		getFrame().getContentPane().add(btnSend);
		
		lblChatWithNone = new JLabel("Chat with: None");
		lblChatWithNone.setBounds(50, 12, 225, 14);
		getFrame().getContentPane().add(lblChatWithNone);
		
		JButton btnClose = new JButton("Close");
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(type.equals("server"))
				{
					
				}
				else if(type.equals("client"))
				{
					
				}
			}
		});
		btnClose.setBounds(158, 235, 117, 25);
		getFrame().getContentPane().add(btnClose);
	}

	public JFrame getFrame() {
		return frame;
	}

	public void setFrame(JFrame frame) {
		this.frame = frame;
	}
}
