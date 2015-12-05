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
import java.util.Collections;
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
	ArrayList<Socket> clients = new ArrayList<>();
	ArrayList<Map<String,ArrayList<Socket>>> groupsList = new ArrayList<>();
	ArrayList<String> usernames = new ArrayList<>();
	ArrayList<String> ports = new ArrayList<>();
	HashMap<String,String> groups = new HashMap<>();
	HashMap<String,ArrayList<DataOutputStream>> dosesofgroups = new HashMap<String,ArrayList<DataOutputStream>>();
	HashMap<String,ArrayList<String>> usernamesofgroups = new HashMap<String,ArrayList<String>>();
	JPanel list_panel;
	DefaultListModel model = new DefaultListModel();
	JList list;
	ArrayList<Socket> clientsbeforedelete;
	/**
	 * @author hussein
	 *
	 */
	public class serverMain extends Thread{
		ServerSocket sv;
		Socket c;
		public JTextArea jx ;
		/**
		 * constructor of server main thread
		 * @param sv serverSocket of the server
		 */
		public serverMain(ServerSocket sv) {
	        this.sv = sv;
	    }
		
		
		public void run() {
			try {
					txtrServerLogs.append("\n waiting for clients");
					while (true) { 	
						Socket c;
	                c = sv.accept();
	                clientListener ch = new clientListener(c);
	                ch.start();

	            }

	        } catch (Exception e1) {
	            System.out.println(e1.getMessage());
	        }
		}
		
		

	}
	/**
	 * @author hussein
	 *
	 */
	public class clientListener extends Thread{
		 private Socket client;
         private String userlistener;
		    /**
		     * constructor of the client listener thread
		     * @param client clienSocket that the server accepted 
		     */
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
		                	createNewClient(dos, AN);	                	
		                }	
		                else if (AN.contains("sendto"))
		                {
		                	sendMSGtoClient(AN);
		                }
		                else if(AN.contains("CreateGroup")){
		                	createNewGroup(AN);
		                }else if(AN.contains("FromGroup")){
		                	sendMSGtoGroup(AN);
		                }
		                else if(AN.contains("Ban"))
		                {
		                	model.removeElement(AN.split(":")[1]);
		                	list.setModel(model);
		                }
		                else if(AN.contains("Disconnect"))
		                {
		                	disconnectClient(AN);
		                }		             
		                else if(AN.contains("Remove"))
		                {
		                	removeFromGroup(AN);
		                }
		                else if(AN.contains("Add"))
		                {
		                	addToGroup(AN);
		                }
		                else if(AN.contains("ChangeGroupAdmin")){
		                	
			                	changeAdmin(AN);			                	
			                }		                
		                else if(AN.contains("getIP"))
		                {
		                	createP2P(AN);
		                }
		                else if(AN.contains("kickOff"))
		                {
		                	kickOffClient(AN);
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
		        } catch (Exception e) {
		            System.out.println(e.getMessage());
		        }
		        
		    }

			/**
			 * kick off client from group
			 * @param AN contains username and groupname
			 * @throws IOException at data input stream
			 */
			public void kickOffClient(String AN) throws IOException {
				String[] data = AN.split("kickOff");
				String groupname = data[0];
				String username = data[1];
				ArrayList<String> others = new ArrayList<>();
				others = usernames;
				for(int i=0;i<usernamesofgroups.get(data[0]).size();i++)
				{
					if(usernamesofgroups.get(data[0]).get(i).equals(data[1]))
					{
						dosesofgroups.get(data[0]).get(i).writeUTF("youKickedOff"+data[0]);
						usernamesofgroups.get(data[0]).remove(i);
						dosesofgroups.get(data[0]).remove(i);
					}
					dosesofgroups.get(data[0]).get(i).writeUTF(data[1]+"out"+data[0]);
					others.remove(i);
				}		                	
				for(int i =0;i<others.size();i++)
				{		                		
					dosesofgroups.get(data[0]).get(i).writeUTF(data[1]+"update"+data[0]);
					
				}
				ArrayList<DataOutputStream> datas = new ArrayList<DataOutputStream>();
				ArrayList<String> users = new ArrayList<String>();
				datas = dosesofgroups.get(groupname);
				users = usernamesofgroups.get(groupname);
				dosesofgroups.remove(groupname);
				usernamesofgroups.remove(groupname);
				
				for(int i = 0 ;i<users.size();i++)
				{
					
					if(username.equals(users.get(i)))
					{
						System.out.println("a group dos got removed"+datas.size());
						datas.remove(i);
						users.remove(i);
						System.out.println("a group dos got removed"+datas.size());
					}
				}
				dosesofgroups.put(groupname,datas);
				usernamesofgroups.put(groupname,users);
			}

			/**
			 * create p2p connection between two clients
			 * @param AN contains username and ip
			 * @throws IOException at data input stream
			 */
			public void createP2P(String AN) throws IOException {
				String[] att = AN.split("getIP");
				String port = "";
				for(int i=0;i<usernames.size();i++)
				{
					if(usernames.get(i).equals(att[1]))
					{
						port = ports.get(i);
						DataOutputStream data = new DataOutputStream(clients.get(i)
								.getOutputStream());
						String Message = "openP2P:"+att[0];
						data.writeUTF(Message);
						System.out.println(Message);
						Message = "";
					}
				}
				if(!port.equals(""))
				{
					for(int i=0;i<usernames.size();i++)
					{
						if(usernames.get(i).equals(att[0]))
						{
							DataOutputStream data = new DataOutputStream(clients.get(i)
				    				.getOutputStream());
				    		String Message = att[1]+"sendIP"+port;
							data.writeUTF(Message);
							System.out.println(Message);
							Message = "";
						}
					}
				}
			}

			/**
			 * chane admin of group
			 * @param AN contains username and groupname
			 * @throws IOException at data input stream
			 */
			public void changeAdmin(String AN) throws IOException {
				groups.put(AN.split(":")[1],AN.split(":")[3]);
				String groupname = AN.split(":")[1];
				ArrayList<DataOutputStream> getdoses = new ArrayList<>() ;
				getdoses = dosesofgroups.get(groupname);
				int getindexofoldAdmin = (usernamesofgroups.get(groupname)).size()-1;
				int getindexofnewAdmin = 0 ;
				for(String usr : usernamesofgroups.get(groupname)){
					if(usr.equals(AN.split(":")[3])){
						getindexofnewAdmin = usernamesofgroups.get(groupname).indexOf(usr);
						break;
						}
				}
				Collections.swap(usernamesofgroups.get(groupname),getindexofoldAdmin,getindexofnewAdmin);
				Collections.swap(dosesofgroups.get(groupname),getindexofoldAdmin,getindexofnewAdmin);
				System.out.println("ChangingGroupAdmin:"+AN.split(":")[1]+":to:"+ AN.split(":")[3]);
				for (DataOutputStream data : getdoses)
				{
					data.writeUTF("ChangingGroupAdmin:"+AN.split(":")[1]+":to:"+ AN.split(":")[3]);
				}
			}

			/**
			 * add client to group
			 * @param AN contains username and groupname
			 * @throws IOException at data input stream
			 */
			public void addToGroup(String AN) throws IOException {
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

			/**
			 * remove user from group
			 * @param AN contains username and groupname
			 * @throws IOException at data input stream
			 */
			public void removeFromGroup(String AN) throws IOException {
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

			/**
			 * disconnect a client
			 * @param AN contains username of the client
			 * @throws IOException at data input stream
			 */
			public void disconnectClient(String AN) throws IOException {
				String []user = AN.split(":");
				System.out.println(user[1]);
				model.removeElement(user[1]);
				list.setModel(model);
				txtrServerLogs.append("\n user removed:"+user[1]);		                	
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
					DataOutputStream data = new DataOutputStream(c.getOutputStream());
				
					data.writeUTF("Disconnect:"+user[1]);
				}
			}

			/**
			 * send message to other group clients
			 * @param AN contains message and groupname
			 * @throws IOException at data input stream
			 */
			public void sendMSGtoGroup(String AN) throws IOException {
				String []groupOb = AN.split(":");
				String groupname = groupOb[1];
				ArrayList<DataOutputStream> getdoses = new ArrayList<>() ;
				getdoses = dosesofgroups.get(groupname);
				System.out.println("Sending to "+groupname+" which contains "+getdoses.size());
				for (DataOutputStream data : getdoses)
				{
					System.out.println("writing to one of the clients in group"+groupname);
					data.writeUTF("toGroup:"+groupname+":"+userlistener+":"+groupOb[2]);
				}
				txtrServerLogs.append("\n"+"Sent Stuff to group:"+groupname);
			}

			/**
			 * create new chat group
			 * @param AN contains usernames and groupname
			 * @throws IOException at input stream
			 */
			public void createNewGroup(String AN) throws IOException {
				String []order = AN.split(":");		                	
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
			}

			/**
			 * send message from client to another
			 * @param AN contains the two usernames and message
			 * @throws IOException at input stream
			 */
			public void sendMSGtoClient(String AN) throws IOException {
				String[] message = AN.split("sendto",2);
				String[] att = message[1].split(",",2);
				System.out.println(att[0]);			                
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
					if(usernames.get(i).equals(message[0]) && !att[1].equals(""))
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

			/**
			 * create new client connection
			 * @param dos data output stream of server
			 * @param AN contains username and port number
			 * @throws IOException at data input stream
			 */
			public void createNewClient(DataOutputStream dos, String AN) throws IOException {
				String[] att = AN.split(":");
				String []user = att[1].split("&");
				ports.add(user[0]);
				usernames.add(user[1]);
				userlistener = user[1];		                	
				System.out.println(user[1]);		              
				clients.add(client);				            
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
		JLabel lblActiveUsers = new JLabel("Active Users");
		lblActiveUsers.setBounds(10, 11, 105, 14);
		list_panel.add(lblActiveUsers);
		
		JPanel connection_panel = new JPanel();
		connection_panel.setBounds(127, 0, 297, 38);
		panel.add(connection_panel);
		
		JButton btnNewButton = new JButton("Connect");
		
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {				
				btnNewButton.setEnabled(false);				
				try {					
					connectServer();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				
			}

			/**
			 * establish server connection
			 * @throws IOException at creating serverSocket
			 */
			public void connectServer() throws IOException {
				sv = new ServerSocket(1234);					
				servermain = new serverMain(sv);
				servermain.start();					
				btnNewButton_1.setEnabled(true);
			}
		});
		connection_panel.add(btnNewButton);		
		btnNewButton_1 = new JButton("Disconnect");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					closeServer();
					btnNewButton.setEnabled(true);
					btnNewButton_1.setEnabled(false);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}

			/**
			 * close Server connection
			 * @throws IOException at closing server
			 */
			public void closeServer() throws IOException {
				sv.close();
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
				deleteUser();
			}

			/**
			 * delete client from server
			 */
			public void deleteUser() {
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
					e.printStackTrace();
				}
			}
		});
		contol_users_panel.add(btnDeleteUser);
	}
}
