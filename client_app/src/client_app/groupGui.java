package client_app;

import java.awt.Component;
import java.awt.EventQueue;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

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
	 ArrayList<String> activeUsersList = new ArrayList<>();
	 JList listactiveusersingroup;
	 JButton kick;
	 String choice;
	
	/**
	 * Launch the application.
	 */
	public static void main(String args,clientGui cli,groupGui group) {
		String groupreq = args;
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
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblActiveGroups = new JLabel("Active User in Group");
		lblActiveGroups.setBounds(10, 16, 123, 14);
		frame.getContentPane().add(lblActiveGroups);
		String []resp = groupreq.split("&");
		
		listactiveusersingroup = new JList();
		listactiveusersingroup.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				choice = listactiveusersingroup.getSelectedValue().toString();
				System.out.println(choice);
			}
		});
		listactiveusersingroup.setBounds(10, 41, 123, 169);
		frame.getContentPane().add(listactiveusersingroup);
		
		
		textAregroupmessg = new JTextArea();
		textAregroupmessg.setBounds(158, 17, 273, 193);
		frame.getContentPane().add(textAregroupmessg);
		
		textAreaGroup = new JTextArea();
		textAreaGroup.setBounds(158, 221, 153, 21);
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
		btnNewButton.setBounds(321, 221, 110, 23);
		frame.getContentPane().add(btnNewButton);
		
		textFieldMessage = new JTextField();
		textFieldMessage.setBounds(190, 242, 86, 20);
		frame.getContentPane().add(textFieldMessage);
		textFieldMessage.setColumns(10);
		
		kick = new JButton("Kick");
		kick.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.out.println(client.usergroups.keySet());
				groupGui gui = client.usergroups.get(groupname);
				if(gui != null)
					try {
						client.client = new Socket("127.0.0.1", 1234);
						DataOutputStream dos = new DataOutputStream(client.client.getOutputStream());
						dos.writeUTF(groupname+"kickOff"+choice);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				else 
					System.out.println("ERROR");
				client.usergroups.remove(choice);
			}
		});
		kick.setBounds(10, 221, 123, 23);
		frame.getContentPane().add(kick);
		textFieldMessage.getDocument().addDocumentListener(new DocumentListener() {

	        @Override
	        public void removeUpdate(DocumentEvent e) {

	        }

	        @Override
	        public void insertUpdate(DocumentEvent e) {
	        	if(textFieldMessage.getText().contains("youKickedOff"))
	        	{
	        		String[] resAtt = textFieldMessage.getText().split("KickedOff");
	        	}
	        	if(textFieldMessage.getText().contains("out"))
	        	{
	        		String[] resAtt = textFieldMessage.getText().split("out");
	        	}
	        	else
	        	{
		        	textAregroupmessg.append(textFieldMessage.getText()+"\n");
		        	System.out.println("something updated neehaaa"+textFieldMessage.getText()+"\n");
	        	}
	        }

	        @Override
	        public void changedUpdate(DocumentEvent arg0) {
	        	System.out.println("something changed neehaaa\n");
	        	textAregroupmessg.append(textFieldMessage.getText()+"\n");
	        }
	    });
		textFieldMessage.setVisible(false);
		kick.setEnabled(false);
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
	public void setActiveUsersList(String[] list)
	{
		for(String s : list)
		{
			activeUsersList.add(s);
			if(!s.equals(userx))
				model.addElement(s);
			listactiveusersingroup.setModel(model);
		}
		if(list[list.length-1].equals(userx))
		{
			kick.setEnabled(true);
			textAregroupmessg.append("u are the Admin of this Group \n only you Can Kick people\n");
		}
	}
}
