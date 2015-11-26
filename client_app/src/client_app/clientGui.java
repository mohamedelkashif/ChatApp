package client_app;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.awt.event.ActionEvent;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

public class clientGui {
	
	JButton btnNewButton_2;
	JTextArea textArea_1; 
	JButton btnNewButton_1;
	Socket client;
	JTextArea textArea;
	private static JFrame frame;
	private JTextField textField;
	private JList list;
	DefaultListModel model = new DefaultListModel();
	private JLabel lblActiveUsers;
	private JButton btnNewButton_3;
	private ArrayList<String> selectedActiveUsersToGroup = new ArrayList<String>();
	private JTextField textField_1;
	HashMap<String,groupGui> usergroups = new HashMap<String,groupGui>();
	DataOutputStream dos;
	DataInputStream dis;
	static clientGui window;
	private JTextField messageFromGroup;
	public class clientMain extends Thread{
		public clientMain() {
	        
	    }
		public void run() {
		 try {
	            //1.Create Client Socket and connect to the server
	            client = new Socket("127.0.0.1", 1234);
	            //2.if accepted create IO streams
	            dos = new DataOutputStream(client.getOutputStream());
	            dis = new DataInputStream(client.getInputStream());
	            //Scanner sc = new Scanner(System.in);
	            dos.writeUTF("newClient:"+textField.getText());
	            String userInput;
	            
	            while (true) {
	                //System.out.print(selectedActiveUsersToGroup);
	                //read from the user
	            	//System.out.println("sent status"+btnNewButton_2.isEnabled());
	                String response = "";
	                //read the response from the server
	                //dis.read
	                //if(dis.readUTF() != null)
	               // {
	                
	                if(dis.available() >0)
	                {
	                	//System.out.println(dis.available());
	                	response = dis.readUTF();
	                	System.out.println("i am "+textField.getText()+" message recieved: "+response);
	                	if(response.contains("activeUsers"))
	                	{
	                		String[] users= response.split(",");
	                		for(int i= 1 ;i<users.length;i++)
	                		{
	                			model.addElement(users[i]);
	                		}
	                		list.setModel(model);
	                	}
	                	else if(response.contains("updateUsers"))
	                	{
	                		model.addElement(response.split(":")[1]);
	                		list.setModel(model);
	                	}
	                	else if(response.contains("OpenGroupGui")){
	                		String []res = response.split(":");
	                		System.out.println(res[1]);
	                		groupGui newgroup = new groupGui();
	                		String createdgroupName = res[1].split("&")[0];
	                		newgroup.setUser(textField.getText());
	                		newgroup.setActiveUsersList(res[1].split("&")[2].split(","));
	                		//groupMain groupThreadx = newgroup.new groupMain();
	    					//groupThreadx.start();
		                	newgroup.main(res[1],window,newgroup);
		                	usergroups.put(createdgroupName, newgroup);
	                	//	newgroup.main();
	                	}
	                	else if(response.contains("toGroup"))
	                	{
	                		String createdgroupName =response.split(":")[1];
	                		System.out.println("sending to group:"+createdgroupName);
	                		groupGui sendingto  = usergroups.get(createdgroupName);
	                		sendingto.setMessage(response);
	                		
	                	}
	                	else
	                	{
	                		//System.out.println(response);
		                	textArea.append(response+"\n");
	                	}
	                	
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
	public static void main(String[] args) {
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					
					window = new clientGui();
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
		frame.setBounds(100, 100, 450, 351);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		textField = new JTextField();
		textField.setToolTipText("Enter your username ");
		textField.setBounds(10, 24, 86, 20);
		frame.getContentPane().add(textField);
		textField.setColumns(10);
		
		JButton btnNewButton = new JButton("Connect");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(!textField.getText().equals(""))
				{
					btnNewButton.setEnabled(false);
					btnNewButton_1.setEnabled(true);
					clientMain clientThread = new  clientMain();
					clientThread.start();
					textField.setEnabled(false);
					frame.setTitle("Client "+textField.getText()+" chat");
					btnNewButton_2.setEnabled(true);
					btnNewButton_3.setEnabled(true);
					textArea_1.setEditable(true);
					textField_1.setEditable(true);
					btnNewButton_1.setEnabled(true);
				}
				else
				{
					JOptionPane.showMessageDialog(frame,
						    "please enter a username.",
						    "Connection error",
						    JOptionPane.ERROR_MESSAGE);
				}
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
		
		textArea = new JTextArea();
		textArea.setBounds(135, 78, 289, 135);
		frame.getContentPane().add(textArea);
		
		JLabel lblAll = new JLabel("All");
		lblAll.setBounds(145, 57, 46, 14);
		frame.getContentPane().add(lblAll);
		
		btnNewButton_2 = new JButton("Send");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String userInput = textArea_1.getText() ;
                try {
					dos.writeUTF("$From"+textField.getText()+"$:"+userInput);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnNewButton_2.setBounds(329, 218, 89, 23);
		frame.getContentPane().add(btnNewButton_2);
		
		textArea_1 = new JTextArea();
		textArea_1.setBounds(135, 223, 184, 15);
		frame.getContentPane().add(textArea_1);
		
		
		
		list = new JList();
		list.setBounds(10, 78, 103, 159);
		frame.getContentPane().add(list);
		
		lblActiveUsers = new JLabel("Active Users");
		lblActiveUsers.setBounds(10, 55, 103, 14);
		frame.getContentPane().add(lblActiveUsers);
		
		btnNewButton_3 = new JButton("Create A Group");
		btnNewButton_3.setToolTipText("Please select multiple users first");
		btnNewButton_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				if(!textField_1.getText().equals(""))
				{
					
					textField_1.setEnabled(false);
					String userInput = "CreateGroup:";
    				int[] selectedIx = list.getSelectedIndices();      

    			    for (int i = 0; i < selectedIx.length; i++) {
    			    	userInput += (String) list.getModel().getElementAt(selectedIx[i]) + ",";
    			    }
                	userInput += textField.getText() ;
                	try {
						dos.writeUTF("$From"+textField.getText()+"$"+userInput + ":AdminOfGroup:"+textField.getText() +":GroupName:"+ textField_1.getText() );
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                	userInput = "";
				}
				else
				{
					JOptionPane.showMessageDialog(frame,
						    "please enter a groupname.",
						    "Connection error",
						    JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		btnNewButton_3.setBounds(10, 249, 133, 23);
		frame.getContentPane().add(btnNewButton_3);
		
		textField_1 = new JTextField();
		textField_1.setToolTipText("Enter group name here");
		textField_1.setBounds(158, 250, 133, 20);
		frame.getContentPane().add(textField_1);
		textField_1.setColumns(10);
		
		messageFromGroup = new JTextField();
		messageFromGroup.setBounds(338, 249, 86, 20);
		frame.getContentPane().add(messageFromGroup);
		messageFromGroup.setColumns(10);
		messageFromGroup.getDocument().addDocumentListener(new DocumentListener() {

	        @Override
	        public void removeUpdate(DocumentEvent e) {

	        }

	        @Override
	        public void insertUpdate(DocumentEvent e) {
	        	System.out.println("something updated neehaaa"+messageFromGroup.getText()+"\n");
	        	try {
					dos.writeUTF(messageFromGroup.getText());
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
	        }

	        @Override
	        public void changedUpdate(DocumentEvent arg0) {
	        	System.out.println("something changed neehaaa\n");
	        	
	        }
	    });
		messageFromGroup.setVisible(false);
		btnNewButton_2.setEnabled(false);
		btnNewButton_3.setEnabled(false);
		textArea_1.setEditable(false);
		textField_1.setEditable(false);
		btnNewButton_1.setEnabled(false);
		textArea.setEditable(false);
		
		
	}
	public DataOutputStream getDos()
	{
		return this.dos;
	}
	public DataInputStream getDis()
	{
		return this.dis;
	}
	public void setMessage(String message)
	{
		this.messageFromGroup.setText(message);
	}
}
