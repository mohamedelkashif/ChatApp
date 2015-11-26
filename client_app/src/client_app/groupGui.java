package client_app;

import java.awt.Component;
import java.awt.EventQueue;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.JLabel;
import javax.swing.JList;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextArea;
import java.awt.event.InputMethodListener;
import java.awt.event.InputMethodEvent;

public class groupGui {

	private static  JFrame frame;
	static JTextArea textAregroupmessg;
	public static  String groupreq = "uu&users&nn,mm";
	static String groupname ;
	static Socket group;
	DefaultListModel model = new DefaultListModel();
	static JTextArea textAreaGroup;
	 static JButton btnNewButton;
	 static String userInputingroup;
	 static clientGui client;
	 static String userx;
	 private JTextField textFieldMessage;
	
	/**
	 * Launch the application.
	 */
	public static void main(String args,clientGui cli,groupGui group) {
		groupreq = args;
		client = cli;
		String []resp = groupreq.split("&");
		groupname = resp[0];
		frame.setTitle("client "+userx+" chat with group "+groupname);
		//System.out.println("m*"+args);
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					
					//groupGui window = new groupGui();
					group.frame.setVisible(true);
					
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
				userInputingroup = textAreaGroup.getText() ;
	               // System.out.println("gr"+userInputingroup);
				   //dos.writeUTF("$From"+userx+"$"+"FromGroup:"+ groupname +":"+ userInputingroup);
				System.out.println("i am "+userx);
				client.setMessage("$From"+userx+"$"+"FromGroup:"+ groupname +":"+ userInputingroup);
					
			}
		});
		btnNewButton.setBounds(158, 222, 89, 23);
		frame.getContentPane().add(btnNewButton);
		
		textFieldMessage = new JTextField();
		textFieldMessage.setBounds(7, 231, 86, 20);
		frame.getContentPane().add(textFieldMessage);
		textFieldMessage.setColumns(10);
		textFieldMessage.getDocument().addDocumentListener(new DocumentListener() {

	        @Override
	        public void removeUpdate(DocumentEvent e) {

	        }

	        @Override
	        public void insertUpdate(DocumentEvent e) {
	        	textAregroupmessg.append(textFieldMessage.getText()+"\n");
	        	System.out.println("something updated neehaaa"+textFieldMessage.getText()+"\n");
	        	
	        }

	        @Override
	        public void changedUpdate(DocumentEvent arg0) {
	        	System.out.println("something changed neehaaa\n");
	        	textAregroupmessg.append(textFieldMessage.getText()+"\n");
	        }
	    });
		textFieldMessage.setVisible(false);
		
	}

	public void setUser(String user)
	{
		System.out.println("Setting the user to : "+user);
		this.userx = user;
	}
	public void setMessage(String message)
	{
		System.out.println("message is being set neehaaa\n");
		textFieldMessage.setText(message);
	}
}
