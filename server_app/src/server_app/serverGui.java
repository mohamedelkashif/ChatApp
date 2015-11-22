package server_app;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JList;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.awt.event.ActionEvent;
import javax.swing.JTextArea;

public class serverGui {
	ServerSocket sv;
	serverMain servermain;
	private JFrame frame;
	JButton btnNewButton_1;
	JTextArea txtrServerLogs;
	public class serverMain extends Thread{
		ServerSocket sv;
		public JTextArea jx ;
		public serverMain(ServerSocket sv) {
	        this.sv = sv;
	    }

		
		
		public void run() {
			try {
	            //1.Create Server Socket
					txtrServerLogs.append("\n waiting for clients");
					while (true) {
	            	
	                //2.Listen for Clients
	                Socket c;
	                c = sv.accept();
	                txtrServerLogs.append("\n New Client Arrived");
	                clientListener ch = new clientListener(c);
	                ch.start();

	            }

	        } catch (Exception e1) {
	            System.out.println(e1.getMessage());
	        }
		}
		
		

	}
	public class clientListener extends Thread{
		 private Socket client;

		    // constructor
		    public clientListener(Socket client) {
		        this.client = client;
		    }

		    public void run() {
		        try {
		        	DataOutputStream dos = new DataOutputStream(client.getOutputStream());
		            DataInputStream dis = new DataInputStream(client.getInputStream());
		            while (true) {
		                String AN = dis.readUTF();
		                txtrServerLogs.append("\n"+AN);
		                dos.writeUTF(AN);
		                txtrServerLogs.append("\n"+"Sent Stuff to the clients");

		            }
		            //Close/release resources
		            //dis.close();
		           // dos.close();
		            //client.close();
		        } catch (Exception e) {
		            System.out.println(e.getMessage());
		        }
		    }

	}


	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					serverGui window = new serverGui();
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
	public serverGui() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel();
		frame.getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(null);
		
		JPanel list_panel = new JPanel();
		list_panel.setBounds(0, 0, 125, 262);
		panel.add(list_panel);
		
		JList list = new JList();
		list_panel.add(list);
		
		JPanel connection_panel = new JPanel();
		connection_panel.setBounds(127, 0, 307, 38);
		panel.add(connection_panel);
		
		JButton btnNewButton = new JButton("Connect");
		
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				/*try {
		            //1.Create Server Socket
		           // sv = new ServerSocket(1234);
		            btnNewButton.setEnabled(false);
		            while (true) {
		                //2.Listen for Clients
		                Socket c;
		                c = sv.accept();
		                System.out.println("New Client Arrived");
		                clientListener ch = new clientListener(c);
		                ch.start();

		            }

		        } catch (Exception e1) {
		            System.out.println(e1.getMessage());
		        }*/
				btnNewButton.setEnabled(false);
				
				try {
					
					sv = new ServerSocket(1234);
					servermain = new serverMain(sv);
					servermain.start();
					//txtrServerLogs.setText(txtrServerLogs.getText()+"\n"+servermain.message);
					btnNewButton_1.setEnabled(true);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
		});
		connection_panel.add(btnNewButton);
		
		btnNewButton_1 = new JButton("Disconnect");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					sv.close();
					btnNewButton.setEnabled(true);
					btnNewButton_1.setEnabled(false);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
		connection_panel.add(btnNewButton_1);
		btnNewButton_1.setEnabled(false);
		JPanel log = new JPanel();
		log.setBounds(127, 39, 307, 186);
		panel.add(log);
		log.setLayout(null);
		
		txtrServerLogs = new JTextArea();
		txtrServerLogs.setEditable(false);
		txtrServerLogs.setBounds(43, 0, 222, 186);
		txtrServerLogs.setText("Server Logs");
		log.add(txtrServerLogs);
		
		JPanel contol_users_panel = new JPanel();
		contol_users_panel.setBounds(126, 226, 308, 36);
		panel.add(contol_users_panel);
		
		JButton btnDeleteUser = new JButton("Delete User");
		contol_users_panel.add(btnDeleteUser);
	}
}
