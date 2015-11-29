package server_app;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JList;
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
import java.util.Map;
import java.util.Random;
import java.awt.event.ActionEvent;
import javax.swing.JTextArea;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.AbstractListModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class serverGui {
	ServerSocket sv;
	ServerSocket gsv;
	serverMain servermain;
	serverMain groupservermain;
	private JFrame frame;
	JButton btnNewButton_1;
	JTextArea txtrServerLogs;
	//ArrayList<DataOutputStream> doses = new ArrayList<>();
	ArrayList<Socket> clients = new ArrayList<>();
	ArrayList<Map<String,ArrayList<Socket>>> groupsList = new ArrayList<>();
	ArrayList<String> usernames = new ArrayList<>();
	HashMap<String,String> groups = new HashMap<>();
	HashMap<String,ArrayList<DataOutputStream>> dosesofgroups = new HashMap<String,ArrayList<DataOutputStream>>();
	HashMap<String,ArrayList<String>> usernamesofgroups = new HashMap<String,ArrayList<String>>();
	JPanel list_panel;
	DefaultListModel model = new DefaultListModel();
	JList list;
	ArrayList<Socket> clientsbeforedelete;
	//DefaultListModel<String> model = new DefaultListModel<>();
	public class serverMain extends Thread{
		ServerSocket sv;
		Socket c;
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
	                //txtrServerLogs.append("\n New Client Arrived");
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
         private String userlistener;
		    // constructor
		    public clientListener(Socket client) {
		        this.client = client;
		    }

		    public void run() {
		        try {

		        	DataInputStream dis ;
			DataOutputStream dos;
		        	 dos = new DataOutputStream(client.getOutputStream());
		             dis = new DataInputStream(client.getInputStream());
		            
		            
		            while (true) {
		                String AN = dis.readUTF();
		                System.out.println("Listening to :" +userlistener+" Message is : "+AN );

		                if(AN.contains("newClient"))
		                {
		                	//doses.add(dos);
		                	String []user = AN.split(":");
		                	//System.out.println(user[1]);
		                	usernames.add(user[1]);
		                	userlistener = user[1];
		                	//System.out.println("created a new user :" + userlistener);

		            //doses.add(dos);        	            
		           
		                	
		                	System.out.println(user[1]);		              
		                	clients.add(client);
				            //ArrayList<Socket> groupClients = new ArrayList<>();
				            //groupClients.add(client);	
				            //Map<String,ArrayList<Socket>> group = null;
				            //group.put(user[1],groupClients);
		                	model.addElement(user[1]);
		                	list.setModel(model);
		                	txtrServerLogs.append("\n new user added:"+user[1]);
		                	dos.writeUTF("connection successfull!");
		                	String active = "";
		                	for(String userx:usernames)
		                	{
		                		if(!userx.equals(user[1]))
		                		active = active+","+userx;
		                	}
		                	if(!active.equals(""))
		                	{
		                		dos.writeUTF("activeUsers"+active);
		                		System.out.println(active);
		                	}
		                	for(Socket c :clients)

		                	{
		                		if(c != client)
		                		{
		                			DataOutputStream data = new DataOutputStream(c.getOutputStream());
		                			data.writeUTF("updateUsers:"+user[1]);
		                		}
		                	}
		                	
		                	
		                }
		                else if(AN.contains("$From"+userlistener+"$"))
		                {
		                System.out.println(AN);
		                if(AN.contains("CreateGroup")){
		                	String []order = AN.split(":");
		                	//System.out.println(AN);
		                	
				           
		                	txtrServerLogs.append("\n new group added: name:" +order[5]+ " admin:"+ order[3]);
		                	groups.put(order[5], order[3]);
		                	String []sendees = order[1].split(",");
		                	ArrayList<DataOutputStream> dosesofAgroup = new ArrayList<DataOutputStream>();
		                	ArrayList<String> usernamesofAgroup = new ArrayList<String>();
		                	for(String se : sendees){
		                		usernamesofAgroup.add(se);
		                		for(int i= 0; i < usernames.size() ; i++){
		                			if(se.equals(usernames.get(i))){
		                				DataOutputStream data = new DataOutputStream(clients.get(i)
				                				.getOutputStream());
		                				dosesofAgroup.add(data);
		                				data.writeUTF("OpenGroupGui:"+order[5]+"&users&"+order[1]+"&admin&"+ order[3]);
		                				data.writeUTF("toGroup:"+order[5]);
		                			}
		                		}
		                	}
		                	ArrayList<String> usersNotInGroup = new ArrayList<String>();
		                	ArrayList<Socket> socketsOfUsersNotInGroup = new ArrayList<Socket>();
		                	for(int i = 0; i<usernames.size();i++)
		                	{
		                		usersNotInGroup.add(usernames.get(i));
		                		socketsOfUsersNotInGroup.add(clients.get(i));
		                	}
		                	for(String se:sendees)
		                	{
		                		for(int i = 0 ;i<usersNotInGroup.size();i++)
		                		{
		                			if(se.equals(usersNotInGroup.get(i)))
		                			{
		                				usersNotInGroup.remove(i);
		                				socketsOfUsersNotInGroup.remove(i);
		                				i--;
		                			}
		                		}
		                	}
		                	for(int i = 0 ;i<usersNotInGroup.size();i++)
	                		{
		                		DataOutputStream data = new DataOutputStream(socketsOfUsersNotInGroup.get(i)
		                				.getOutputStream());
		                		data.writeUTF("NotInGroup:"+order[5]+"&users&"+order[1]+"&admin&"+ order[3]);
	                		}
		                	 usernamesofgroups.put(order[5], usernamesofAgroup);
		                	 dosesofgroups.put(order[5], dosesofAgroup);
		                	 System.out.println("Groups Available:" +dosesofgroups.size());
		                }else if(AN.contains("FromGroup")){
		                	String []groupOb = AN.split(":");
		                	String groupname = groupOb[1];
		                	ArrayList<DataOutputStream> getdoses = new ArrayList<>() ;
		                	getdoses = dosesofgroups.get(groupname);
		                	System.out.println("Sending to "+groupname+" which contains "+getdoses.size());
		                	//for(DataOutputStream data: dosesofgroups.get(groupname))
		                	for (DataOutputStream data : getdoses)
			                {
		                		System.out.println("writing to one of the clients in group"+groupname);
			                	data.writeUTF("toGroup:"+groupname+":"+userlistener+":"+groupOb[2]);
			                }
			                txtrServerLogs.append("\n"+"Sent Stuff to group:"+groupname);
		                }
		                else if(AN.contains("Ban"))
		                {
		                	model.removeElement(AN.split(":")[1]);
		                	list.setModel(model);
		                }
		                else if(AN.contains("Disconnect"))
		                {
		                	String []user = AN.split(":");
		                	System.out.println(user[1]);
		                	//usernames.remove(user[1]);
		                	model.removeElement(user[1]);
		                	list.setModel(model);
		                	txtrServerLogs.append("\n user removed:"+user[1]);
		                	//dos.writeUTF("connection lost!");
		                	//textArea.append("Connection lost\n");
		                	for (int i=0; i<usernames.size();i++)
		                	{
		                		System.out.println("username:"+usernames.get(i));
		                		if(usernames.get(i).equals(user[1]))
		                		{
		                			System.out.println(usernames.size());
		                			clientsbeforedelete = clients;
		                			
		                			usernames.remove(i);
		                			System.out.println(",ndvnk");
		                			for(Socket c : clients){
		    		                	//System.out.println(doses.size());
		    		                		DataOutputStream data = new DataOutputStream(c.getOutputStream());
		    		                		String groupnamesinarr = "nullGroup";
		    		                		for(String group : groups.keySet()){
		    		                			groupnamesinarr += ","+group ;
		    		                		}
		    		                		String usernamesinarr = "nullName";
		    		                		for(String username : usernames){
		    		                			usernamesinarr += ","+username ;
		    		                		}
		    		                		data.writeUTF("DisconnUser:"+user[1]+":actusers:"+usernamesinarr +":toGrouup:"+groupnamesinarr);
		    		                	}
		                			System.out.println(usernames.size());
		                			clients.remove(i);
		                		}
		                	}
		                	for(Socket c : clients){
		                	//System.out.println(doses.size());
		                		DataOutputStream data = new DataOutputStream(c.getOutputStream());
		                	
		                		data.writeUTF("Disconnect:"+user[1]);
		                	}
		                }
		                
		                else
		                {
		                	
			                for (Socket c : clients)
			                {
			                	DataOutputStream data = new DataOutputStream(c.getOutputStream());		
			                	data.writeUTF(AN.split(":")[1]);
			                }
			                txtrServerLogs.append("\n"+"Sent Stuff to the clients");
		                }

		            }
		                else if(AN.contains("Remove"))
		                {
		                	String [] orders = AN.split(":");
		                	String removeClient = orders[1];
		                	String fromGroup = orders[3];
		                	ArrayList<DataOutputStream> datas = new ArrayList<DataOutputStream>();
		                	ArrayList<String> users = new ArrayList<String>();
		                	datas = dosesofgroups.get(fromGroup);
		                	users = usernamesofgroups.get(fromGroup);
		                	dosesofgroups.remove(fromGroup);
		                	usernamesofgroups.remove(fromGroup);
		                	
		                	for(int i = 0 ;i<users.size();i++)
		                	{
		                		
		                		if(removeClient.equals(users.get(i)))
		                		{
		                			System.out.println("a group dos got removed"+datas.size());
		                			datas.remove(i);
		                			users.remove(i);
		                			System.out.println("a group dos got removed"+datas.size());
		                		}
		                	}
		                	dosesofgroups.put(fromGroup,datas);
		                	usernamesofgroups.put(fromGroup,users);
		                	for(int i = 0 ;i<datas.size();i++)
		                	{
		                		datas.get(i).writeUTF("Remove:"+removeClient+":From:"+fromGroup);
		                	}
		                }
		                else if(AN.contains("Add"))
		                {
		                	String [] orders = AN.split(":");
		                	String addClient = orders[1];
		                	String fromGroup = orders[3];
		                	ArrayList<DataOutputStream> datas = new ArrayList<DataOutputStream>();
		                	ArrayList<String> users = new ArrayList<String>();
		                	datas = dosesofgroups.get(fromGroup);
		                	for(DataOutputStream data :datas)
		                	{
		                		data.writeUTF(AN);
		                	}
		                	dosesofgroups.remove(fromGroup);
		                	usernamesofgroups.remove(fromGroup);
		                	for(int i = 0 ;i<usernames.size();i++)
		                	{
		                		if(addClient.equals(usernames.get(i)))
		                		{
		                			DataOutputStream addedDos = new DataOutputStream(clients.get(i).getOutputStream());
		                			datas.add(0,addedDos);
		                		}
		                	}
		                	users.add(0,addClient);
		                	usernamesofgroups.put(fromGroup,users);
		                	dosesofgroups.put(fromGroup,datas);
		                }
		                	else if(AN.contains("ChangeGroupAdmin")){
		                	
			                	groups.put(AN.split(":")[1],AN.split(":")[3]);
			                	String groupname = AN.split(":")[1];
			                	ArrayList<DataOutputStream> getdoses = new ArrayList<>() ;
			                	getdoses = dosesofgroups.get(groupname);
			                	System.out.println("ChangingGroupAdmin:"+AN.split(":")[1]+":to:"+ AN.split(":")[3]);
			                	for (DataOutputStream data : getdoses)
				                {
			                		data.writeUTF("ChangingGroupAdmin:"+AN.split(":")[1]+":to:"+ AN.split(":")[3]);
				                }
			                	
			                }
		                else if (AN.contains("sendto"))
		                {
		                	String[] message = AN.split("sendto",2);
		                	String[] att = message[1].split(",",2);
		                	System.out.println(att[0]);
			                /*for (DataOutputStream data : doses)
			                {
			                	System.out.println(data.toString());
			                	if(data.equals(att[0]))
			                		data.writeUTF(att[1]);
			                }*/
		                	for (int i =0;i<usernames.size();i++)
		                	{
			                	System.out.println(usernames.get(i));
			                	if(usernames.get(i).equals(att[0]) && !att[1].equals(""))
			                	{			                		
			                		DataOutputStream data = new DataOutputStream(clients.get(i)
			                				.getOutputStream());
			                		String Message = att[1];
		                			data.writeUTF(Message);
		                			Message = "";
			                	}
			                }
			                txtrServerLogs.append("\n"+"Sent Stuff to the clients");
		                }
		                else if(AN.contains("kickOff"))
		                {
		                	String[] data = AN.split("kickOff");
		                	/*for(String group:groups)
		                	{
		                		if(group.equals(data[0]))
		                		{
		                			groups.remove(group);
		                		}		                			
		                	}*/
		                	for(int i =0;i<usernames.size();i++)
		                	{
		                		if(usernames.get(i).equals(data[1]))
		                		{
		                			DataOutputStream clientdos = new DataOutputStream(clients.get(i)
			                				.getOutputStream());
			                		String Message = "youKickedOff"+data[0];
			                		clientdos.writeUTF(Message);
		                			Message = "";
		                		}
		                		else
		                		{
		                			DataOutputStream clientdos = new DataOutputStream(clients.get(i)
			                				.getOutputStream());
			                		String Message = data[1]+"out"+data[0];
			                		clientdos.writeUTF(Message);
		                			Message = "";
		                		}
		                		
		                	}
		                }
		                else;
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
		
		list_panel = new JPanel();
		list_panel.setBounds(0, 0, 125, 251);
		panel.add(list_panel);
		list_panel.setLayout(null);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(10, 35, 105, 205);
		list_panel.add(scrollPane_1);
		
		list = new JList();
		scrollPane_1.setViewportView(list);
		list.setModel(new AbstractListModel() {
			String[] values = new String[] {};
			public int getSize() {
				return values.length;
			}
			public Object getElementAt(int index) {
				return values[index];
			}
		});
		list.setBounds(10, 35, 105, 205);
		//list_panel.add(list);
		
		JLabel lblActiveUsers = new JLabel("Active Users");
		lblActiveUsers.setBounds(10, 11, 105, 14);
		list_panel.add(lblActiveUsers);
		
		JPanel connection_panel = new JPanel();
		connection_panel.setBounds(127, 0, 297, 38);
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
					Random rand = new Random();
					int i = rand.nextInt((1500 - 1000) + 1) + 1000;
					System.out.println(i);

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
		log.setBounds(127, 39, 297, 186);
		panel.add(log);
		log.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(43, 0, 222, 186);
		log.add(scrollPane);
		
		txtrServerLogs = new JTextArea();
		scrollPane.setViewportView(txtrServerLogs);
		txtrServerLogs.setEditable(false);
		txtrServerLogs.setText("Server Logs");
		
		JPanel contol_users_panel = new JPanel();
		contol_users_panel.setBounds(126, 226, 298, 36);
		panel.add(contol_users_panel);
		
		JButton btnDeleteUser = new JButton("Delete User");
		btnDeleteUser.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					for(Socket c :clients)
					{
						DataOutputStream data = new DataOutputStream(c.getOutputStream());		
						data.writeUTF("Ban:"+list.getSelectedValue().toString());
					}
					for(int i = 0 ;i <usernames.size();i++)
					{
						if(usernames.get(i).equals(list.getSelectedValue().toString()))
						{
							usernames.remove(i);
							clients.remove(i);
							model.removeElementAt(i);
							list.setModel(model);

						}
					}
					
					txtrServerLogs.setText("Client deleted");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		contol_users_panel.add(btnDeleteUser);
	}
}
