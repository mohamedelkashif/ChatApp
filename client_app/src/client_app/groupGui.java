package client_app;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JLabel;
import javax.swing.JList;

public class groupGui {

	private JFrame frame;
	private JTextField textField_1;
	public static String groupname;
	DefaultListModel model = new DefaultListModel();
	/**
	 * Launch the application.
	 */
	public static void main(String args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					groupname = args;
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
		
		JTextPane textPane_1 = new JTextPane();
		textPane_1.setBounds(148, 40, 276, 170);
		frame.getContentPane().add(textPane_1);
		
		JButton btnSend = new JButton("Send");
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
		for(String actuser:actusers){
			model.addElement(actuser);
			
		}
		list.setModel(model);
	}
}
