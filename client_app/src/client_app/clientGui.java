package client_app;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JTextField;
import javax.swing.DefaultListModel;
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
	JLabel lblAll;
	private JFrame frame;
	private JTextField textField;
	private JList list;
	DefaultListModel model = new DefaultListModel();
	private JLabel lblActiveUsers;
	
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
	            dos.writeUTF("newClient:"+textField.getText());
	            String userInput;
	            
	            while (true) {
	                
	                //read from the user
	            	//System.out.println("sent status"+btnNewButton_2.isEnabled());
	                if(!btnNewButton_2.isEnabled())
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
		                }*/
		                if(!userInput.equals(""))
		                {
		                	//dos.writeUTF(textField.getText()+"sendto"+lblAll.getText()
		                	//.toString()+","+textField.getText()+": "+userInput);*/
		                dos.writeUTF(textField.getText()+"sendto"+lblAll.getText()
	                	.toString().split(": ")[1]+","+textField.getText()+": "+userInput);
		                }
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
	                	if(response.contains("activeUsers"))
	                	{
	                		String[] users= response.split(",",2);
	                		for(int i= 1 ;i<users.length;i++)
	                		{
	                			model.addElement(users[i]);
	                		}
	                		list.setModel(model);
	                	}
	                	else if(response.contains("updateUsers"))
	                	{
	                		model.addElement(response.split(":",2)[1]);
	                		list.setModel(model);
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
		
		lblAll = new JLabel("Chat with : None");
		lblAll.setBounds(135, 58, 188, 14);
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
		
		
		
		list = new JList();
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
		
		frame.getContentPane().add(list);
		
		lblActiveUsers = new JLabel("Active Users");
		lblActiveUsers.setBounds(10, 55, 103, 14);
		frame.getContentPane().add(lblActiveUsers);
		
		
		
	}
}
