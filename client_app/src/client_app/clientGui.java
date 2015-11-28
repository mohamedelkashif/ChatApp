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
import java.net.UnknownHostException;
import java.util.Random;
import java.util.Scanner;
import java.awt.event.ActionEvent;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.AbstractListModel;

public class clientGui {
	
	JButton btnNewButton_2;
	JTextArea textArea_1; 
	JButton btnNewButton_1;
	Socket client;
	JTextArea textArea;
	private static JFrame frame;
	JLabel lblAll;
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
	private JScrollPane scrollPane;
	private JScrollPane scrollPane_1;
	JButton btnNewButton;

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

	                /*if(!btnNewButton_2.isEnabled())
	                {
		                userInput = textArea_1.getText();
		                //userInput = "create"
		                /*if(lblAll.getText().equals("All") && !!userInput.equals(""))
		                {
		                	dos.writeUTF(textField.getText()+"sendto"+"All:"+userInput );
		                }
		                else if(lblAll.getText().equals(list.getSelectedValue().toString()) && 
		                		!userInput.equals(""))
		                {
		                	dos.writeUTF(textField.getText()+"sendto"+list.getSelectedValue()
		                	.toString()+","+textField.getText()+": "+userInput);
		                }
		                if(!userInput.equals(""))
		                {
		                	//dos.writeUTF(textField.getText()+"sendto"+lblAll.getText()
		                	//.toString()+","+textField.getText()+": "+userInput);
		                dos.writeUTF(textField.getText()+"sendto"+lblAll.getText()
	                	.toString().split(": ")[1]+","+textField.getText()+": "+userInput);
		                }
		                btnNewButton_2.setEnabled(true);
		                
	                }*/
	                String response = "";
	                //read the response from the server
	                //dis.read
	                //if(dis.readUTF() != null)
	               // {
	                //String server = dis.readUTF();
            		//System.out.println(server);
            		
	                if(dis.available() >0)
	                {
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
	                	else if(response.contains("Disconnect"))
	                	{
	                		//for()
	                		model.removeElement(response.split(":")[1]);
	                		list.setModel(model);
	                		textArea.setText(response.split(":")[1] +" "+"Removed");
	                	}
	                	
	                	else if(response.contains("Ban"))
	            		{
	                		System.out.println(response+" i am "+textField.getText());
	                		if(response.split(":")[1].equals(textField.getText()))
	                		{
	                			model.removeAllElements();
	                			list.setModel(model);
	                			dis.close();
	                			dos.close();
	                			client.close();
	                			btnNewButton.setEnabled(true);
	                			btnNewButton_1.setEnabled(false);
	                			btnNewButton_2.setEnabled(false);
	                			btnNewButton_3.setEnabled(false);
	                			textArea_1.setEnabled(false);
	                			textField_1.setEnabled(false);
	                			textField.setEnabled(true);
	                			textArea.append("Connection lost\n");
	                			
	                		}
	                		else
	                		{
	                			for(int i = 0; i<model.getSize();i++)
	                			{
	                				if(model.get(i).toString().equals(response.split(":")[1]))
	                				{
	                					System.out.println("removing something");
	                					model.remove(i);
	                					textArea.append("\n user removed:"+response.split(":")[1]+"\n");
	                				}
	                				list.setModel(model);
	                			}
	                			
	                		}
	                		}
	            			//String []user = response.split(":");
		                	//System.out.println(user[1]);
		                	//usernames.remove(user[1]);
		                	/*model.removeElement(response.split(":")[1]);
		                	list.setModel(model);
		                	textArea.append("\n user removed:"+response.split(":")[1]);
		                	dos.writeUTF("connection lost!");
		                	for (int i=0; i<usernames.size();i++)
		                	{
		                		System.out.println("username:"+usernames.get(i));
		                		if(usernames.get(i).equals(response.split(":")[1]))
		                		{
		                			System.out.println(doses.size());
		                			
		                			doses.remove(i);
		                			usernames.remove(i);
		                			System.out.println(",ndvnk");
		                			
		                			System.out.println(doses.size());
		                		
		                		}
		                	}
		                	for(DataOutputStream data : doses){
			                	//System.out.println(doses.size());
			                	data.writeUTF("Ban:"+response.split(":")[1]);
			                	}
		                	client.close();
	            		}*/

	                	
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
		                	System.out.println(createdgroupName);
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
	                	else if(response.contains("youKickedOff"))
	                	{
	                		String[] resAtt = response.split("KickedOff");
	                		groupGui sendingto  = usergroups.get(resAtt[1]);
	                		sendingto.setMessage(response);
	                	}
	                	else if(response.contains("out"))
	                	{
	                		String[] resAtt = response.split("out");
	                		groupGui sendingto  = usergroups.get(resAtt[1]);
	                		sendingto.setMessage(response);
	                	}
	                	else if (response.contains(":"))
	                	{
	                		String[] s = response.split(":",2);
	                		lblAll.setText("Chat with : "+s[0]);
	                		//System.out.println(response);
		                	textArea.append(response+"\n");
	                	}
	                	else;
	                	
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
		
		btnNewButton = new JButton("Connect");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(!textField.getText().equals(""))
				{
					btnNewButton.setEnabled(false);
					btnNewButton_1.setEnabled(true);
					Random rand = new Random();
					int i = rand.nextInt((1500 - 1000) + 1) + 1000;
					System.out.println(i);
					try {
						ServerSocket cs = new ServerSocket(i);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
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
					dos.writeUTF("$From"+textField.getText()+"$"+"Disconnect:"+textField.getText());
					textArea.append("Connection lost\n");
					client.close();
					//dos.close();
					textArea.setText("Connection lost !");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				btnNewButton.setEnabled(true);
				btnNewButton_1.setEnabled(false);
			}
		});
		btnNewButton_1.setBounds(234, 23, 106, 23);
		frame.getContentPane().add(btnNewButton_1);
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(135, 78, 289, 135);
		frame.getContentPane().add(scrollPane);
		
		textArea = new JTextArea();
		scrollPane.setViewportView(textArea);
		
		lblAll = new JLabel("Chat with : None");
		lblAll.setBounds(135, 58, 188, 14);
		frame.getContentPane().add(lblAll);
		
		btnNewButton_2 = new JButton("Send");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
                
				btnNewButton_2.setEnabled(false);
				try {
					client = new Socket("127.0.0.1", 1234);
					DataOutputStream dos = new DataOutputStream(client.getOutputStream());
		            String userInput;
					userInput = textArea_1.getText();
	                if(!userInput.equals(""))
	                {
	                	//dos.writeUTF(textField.getText()+"sendto"+lblAll.getText()
	                	//.toString()+","+textField.getText()+": "+userInput);*/
	                dos.writeUTF(textField.getText()+"sendto"+lblAll.getText()
	            	.toString().split(": ")[1]+","+textField.getText()+": "+userInput);
	                }
				} catch (UnknownHostException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}				
                btnNewButton_2.setEnabled(true);
			}
		});
		btnNewButton_2.setBounds(329, 218, 89, 23);
		frame.getContentPane().add(btnNewButton_2);
		
		textArea_1 = new JTextArea();
		textArea_1.setBounds(135, 223, 184, 15);
		frame.getContentPane().add(textArea_1);
		
		scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(10, 78, 103, 159);
		frame.getContentPane().add(scrollPane_1);
		
		
		
		list = new JList();

		scrollPane_1.setViewportView(list);

		list.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				lblAll.setText("Chat with : "+list.getSelectedValue().toString());
			}
		});
		list.setModel(new AbstractListModel() {
			String[] values = new String[] {};
			public int getSize() {
				return values.length;
			}
			public Object getElementAt(int index) {
				return values[index];
			}
		});
		list.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				String selection = list.getSelectedValue().toString();
				
			}
		});
		list.setBounds(10, 78, 103, 159);
		
		//frame.getContentPane().add(list);
		
		lblActiveUsers = new JLabel("Active Users");
		lblActiveUsers.setBounds(10, 55, 103, 14);
		frame.getContentPane().add(lblActiveUsers);
		
		btnNewButton_3 = new JButton("Create A Group");
		btnNewButton_3.setToolTipText("Please select multiple users first");
		btnNewButton_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				if(!textField_1.getText().equals(""))
				{
					String userInput = "CreateGroup:";
    				int[] selectedIx = list.getSelectedIndices();      
                    if(selectedIx.length>=1)
                    {
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
                    }
                    else
                    {
                    	JOptionPane.showMessageDialog(frame,
    						    "Sorry group must contain at least two persons",
    						    "Connection error",
    						    JOptionPane.ERROR_MESSAGE);
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
