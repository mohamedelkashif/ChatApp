package client_app;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.awt.event.ActionEvent;
import javax.swing.JList;
import javax.swing.JTextArea;
import javax.swing.JLabel;

public class clientGui {
	
	JButton btnNewButton_2;
	JTextArea textArea_1; 
	JButton btnNewButton_1;
	Socket client;
	JTextArea textArea;
	
	public class clientMain extends Thread{
		public clientMain() {
	        
	    }
		public void run() {
		 try {
	            //1.Create Client Socket and connect to the server
	            client = new Socket("127.0.0.1", 1234);
	            //2.if accepted create IO streams
	            DataOutputStream dos = new DataOutputStream(client.getOutputStream());
	            DataInputStream dis = new DataInputStream(client.getInputStream());
	            //Scanner sc = new Scanner(System.in);
	            String userInput;
	            
	            while (true) {
	                
	                //read from the user
	            	//System.out.println("sent status"+btnNewButton_2.isEnabled());
	                if(!btnNewButton_2.isEnabled()){
	                userInput = textArea_1.getText() ;
	                dos.writeUTF(userInput);
	                btnNewButton_2.setEnabled(true);
	                }
	                String response = "";
	                //read the response from the server
	                //dis.read
	                //if(dis.readUTF() != null)
	               // {
	                
	                if(dis.available() >0)
	                {
	                	//System.out.println(dis.available());
	                	response = dis.readUTF();
	                	//System.out.println(response);
	                	textArea.append(response+"\n");
	                }
	                //Print response
	                
	                //}
	            }
	            
	            //dis.close();
	            //dos.close();
	            //client.close();

	        } catch (Exception e) {
	            System.out.println(e.getMessage());
	        }
		}
		
	}

	private JFrame frame;
	private JTextField textField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					clientGui window = new clientGui();
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
	public clientGui() {
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
		textField.setToolTipText("Enter your username ");
		textField.setBounds(27, 24, 86, 20);
		frame.getContentPane().add(textField);
		textField.setColumns(10);
		
		JButton btnNewButton = new JButton("Connect");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnNewButton.setEnabled(false);
				btnNewButton_1.setEnabled(true);
				clientMain clientThread = new  clientMain();
				clientThread.start();
			}
		});
		btnNewButton.setBounds(135, 23, 89, 23);
		frame.getContentPane().add(btnNewButton);
		
		btnNewButton_1 = new JButton("Disconnect");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnNewButton.setEnabled(true);
				btnNewButton_1.setEnabled(false);
				try {
					client.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnNewButton_1.setBounds(234, 23, 106, 23);
		frame.getContentPane().add(btnNewButton_1);
		
		JList list = new JList();
		list.setBounds(27, 55, 86, 183);
		frame.getContentPane().add(list);
		
		textArea = new JTextArea();
		textArea.setBounds(135, 78, 289, 135);
		frame.getContentPane().add(textArea);
		
		JLabel lblAll = new JLabel("All");
		lblAll.setBounds(145, 57, 46, 14);
		frame.getContentPane().add(lblAll);
		
		btnNewButton_2 = new JButton("Send");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnNewButton_2.setEnabled(false);
			}
		});
		btnNewButton_2.setBounds(135, 224, 89, 23);
		frame.getContentPane().add(btnNewButton_2);
		
		textArea_1 = new JTextArea();
		textArea_1.setBounds(242, 229, 182, 15);
		frame.getContentPane().add(textArea_1);
	}
}
