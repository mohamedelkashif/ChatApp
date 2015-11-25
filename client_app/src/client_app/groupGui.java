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

	private  JFrame frame;
	static JTextArea textAregroupmessg;
	public static  String groupreq = "uu&users&nn,mm";
	static String groupname ;
	static Socket group;
	DefaultListModel model = new DefaultListModel();
	static JTextArea textAreaGroup;
	 static JButton btnNewButton;
	 static String userInputingroup;
	public class groupMain extends Thread{
		public groupMain() {
	        
	    }
		public void run() {
		 try {
	            //1.Create Client Socket and connect to the server
	           // group = new Socket("127.0.0.1", 1234);
	            
	            //2.if accepted create IO streams
	            DataOutputStream dos = new DataOutputStream(group.getOutputStream());
	            DataInputStream  dis = new DataInputStream(group.getInputStream());
	            
	            
	            while (true) {
	                //read from the user
	            	//System.out.println("ts");
	                if(!btnNewButton.isEnabled()){
	                	userInputingroup = textAreaGroup.getText() ;
	               // System.out.println("gr"+userInputingroup);
	                dos.writeUTF("FromGroup:"+ groupname +":"+ userInputingroup);
	                	btnNewButton.setEnabled(true);
	                }

	              
	                String response = "";
	                //read the response from the server
	                if(dis.available() >0)
	                {
	                	System.out.println("ave"+dis.available());
	                	response = dis.readUTF();       	
	                	System.out.println("resfromGroup"+response);
		                	textAregroupmessg.append(response+"\n");
	        
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
	public static void main(String args,Socket cli) {
		groupreq = args;
		group = cli;
		String []resp = groupreq.split("&");
		groupname = resp[0];
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
		
		JLabel lblActiveGroups = new JLabel("Active User in Group");
		lblActiveGroups.setBounds(10, 43, 103, 14);
		frame.getContentPane().add(lblActiveGroups);
		
		JTextPane textPane_2 = new JTextPane();
		textPane_2.setBounds(10, 11, 83, 21);
		frame.getContentPane().add(textPane_2);
		String []resp = groupreq.split("&");
		textPane_2.setText(resp[0]);
		
		JList listactiveusersingroup = new JList();
		listactiveusersingroup.setBounds(10, 68, 103, 159);
		frame.getContentPane().add(listactiveusersingroup);
		String []actusers = resp[2].split(",");
		for(int i = 0 ; i < actusers.length ; i++){
			model.addElement(actusers[i]);
			
		}
		listactiveusersingroup.setModel(model);
		
		 textAregroupmessg = new JTextArea();
		textAregroupmessg.setBounds(158, 17, 273, 193);
		frame.getContentPane().add(textAregroupmessg);
		
		textAreaGroup = new JTextArea();
		textAreaGroup.setBounds(257, 221, 167, 21);
		frame.getContentPane().add(textAreaGroup);
		
		btnNewButton = new JButton("Send To group");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				btnNewButton.setEnabled(false);
			}
		});
		btnNewButton.setBounds(158, 222, 89, 23);
		frame.getContentPane().add(btnNewButton);
	}
}
