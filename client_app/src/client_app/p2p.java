package client_app;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JTextArea;
import java.awt.BorderLayout;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JLabel;

/**
 * @author hussein
 *
 */
public class p2p {

	 private JFrame frame;
	 JTextField txtMessage;
	 JTextArea textArea;
	 JLabel lblChatWithNone;
	 private String type;
	 private Socket client;
	 private int portip;
	 private String sender;
	 private String reciever;

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


	public p2p(Socket c, String string, String text, String string2) {
		type = string;
		client = c;
		sender = text;
		reciever = string2;
		initialize();
	}

	public p2p(int port, String string, String text, String string2) {
		type = string;
		portip = port; 
		sender = text;
		reciever = string2;
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		setFrame(new JFrame());
		getFrame().setBounds(100, 100, 450, 300);
		getFrame().setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getFrame().getContentPane().setLayout(null);
		getFrame().setTitle("Client: "+sender);
		
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
				btnSend.setEnabled(false);
				sendMSGP2P();
				btnSend.setEnabled(true);
			}

			/**
			 * send message using peer to peer
			 */
			public void sendMSGP2P() {
				String message = "client sendTo"+sender+": "+txtMessage.getText();
				if(type.equals("server"))
				{
					try {
						DataOutputStream dos = new DataOutputStream(client.getOutputStream());
						dos.writeUTF(message);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
				else if(type.equals("client"))
				{
					try {
						DataOutputStream dos = new DataOutputStream(client.getOutputStream());
						dos.writeUTF(message);
					} catch (IOException e1) {				
						e1.printStackTrace();
					}
				}
				textArea.append(sender+": "+txtMessage.getText()+"\n");
			}
		});
		btnSend.setBounds(321, 206, 117, 25);
		getFrame().getContentPane().add(btnSend);
		
		lblChatWithNone = new JLabel("Chat with: None");
		lblChatWithNone.setText("Chat With: "+reciever);
		lblChatWithNone.setBounds(50, 12, 225, 14);
		getFrame().getContentPane().add(lblChatWithNone);
		
		JButton btnClose = new JButton("Close");
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				closeP2P();
			}

			/**
			 * close P2P connection
			 */
			public void closeP2P() {
				String message = "BYEBYE";
				if(type.equals("server"))
				{
					try {
						DataOutputStream dos = new DataOutputStream(client.getOutputStream());
						dos.writeUTF(message);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
				else if(type.equals("client"))
				{
					try {
						DataOutputStream dos = new DataOutputStream(client.getOutputStream());
						dos.writeUTF(message);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
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
