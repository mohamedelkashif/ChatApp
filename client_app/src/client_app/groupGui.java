package client_app;

import java.awt.Component;
import java.awt.EventQueue;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JLabel;
import javax.swing.JList;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextArea;

public class groupGui {

	private JFrame frame;
	private JTextField textField_1;
	JButton btnSend;
	JTextArea textArea;
	public static  String groupname = "uu&users&nn,mm";
	DefaultListModel model = new DefaultListModel();
	public class groupMain extends Thread{
		public groupMain() {
	        
	    }
		public void run() {
		 try {
	            //1.Create Client Socket and connect to the server
	            Socket group = new Socket("127.0.0.1", 1234);
	            //2.if accepted create IO streams
	            DataOutputStream dos = new DataOutputStream(group.getOutputStream());
	            DataInputStream dis = new DataInputStream(group.getInputStream());
	            String userInput;
	            
	            while (true) {
	                
					//System.out.print(selectedActiveUsersToGroup);
	                //read from the user
	            	//System.out.println("sent status"+btnNewButton_2.isEnabled());
	                if(!btnSend.isEnabled()){
	                userInput = textField_1.getText() ;
	                System.out.println(userInput);
	                dos.writeUTF(userInput);
	                btnSend.setEnabled(true);
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
	/**
	 * Launch the application.
	 */
	public static void main() {
		//groupname = args;
		//System.out.println("m*"+args);
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
	 * @param res 
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
		
		 btnSend = new JButton("Send");
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				btnSend.setEnabled(false);
			}
		});
		btnSend.setBounds(158, 221, 89, 23);
		frame.getContentPane().add(btnSend);
		
		textField_1 = new JTextField();
		textField_1.setBounds(257, 221, 167, 20);
		frame.getContentPane().add(textField_1);
		textField_1.setColumns(10);
		
		JLabel lblActiveGroups = new JLabel("Active User in Group");
		lblActiveGroups.setBounds(10, 43, 103, 14);
		frame.getContentPane().add(lblActiveGroups);
		
		JTextPane textPane_2 = new JTextPane();
		textPane_2.setBounds(10, 11, 83, 21);
		frame.getContentPane().add(textPane_2);
		String []resp = groupname.split("&");
		textPane_2.setText(resp[0]);
		
		JList list = new JList();
		list.setBounds(10, 68, 103, 159);
		frame.getContentPane().add(list);
		String []actusers = resp[2].split(",");
		for(int i = 0 ; i < actusers.length ; i++){
			model.addElement(actusers[i]);
			
		}
		list.setModel(model);
		
		 textArea = new JTextArea();
		textArea.setBounds(151, 11, 273, 193);
		frame.getContentPane().add(textArea);
	}
}
