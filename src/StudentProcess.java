// Student Name: Parth B. Mehta
// Student Id: 1001668756
// Basic part or framework of source code is taken from https://www.geeksforgeeks.org/multi-threaded-chat-application-set-2/

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;


public class StudentProcess {
	
	 
	private JFrame frame;
	private static JTextField studentNameText;
	private static JTextField courseNameText;
	private static DataOutputStream dos;
	final static int ServerPort = 12345;
	
	public static void main(String[] args) throws UnknownHostException, IOException {
		
		// A GUI window is opened for this Process
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					StudentProcess window = new StudentProcess();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		InetAddress ip = InetAddress.getByName("localhost"); 
        
        // establish the connection 
        Socket s = new Socket(ip, ServerPort); 
         
        // obtaining input and out streams 
       // dis = new DataInputStream(s.getInputStream()); 
        dos = new DataOutputStream(s.getOutputStream());
        dos.writeUTF("Student Process");
	}

	/**
	 * Create the application.
	 */
	public StudentProcess() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setTitle("Student Process");
		frame.setBounds(100, 100, 743, 416);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel studentNameLabel = new JLabel("Student Name :");
		studentNameLabel.setBounds(15, 16, 124, 23);
		frame.getContentPane().add(studentNameLabel);
		
		JLabel courseNameLabel = new JLabel("Course Name :");
		courseNameLabel.setBounds(15, 52, 111, 23);
		frame.getContentPane().add(courseNameLabel);
		
		studentNameText = new JTextField();
		studentNameText.setBounds(154, 13, 146, 26);
		frame.getContentPane().add(studentNameText);
		studentNameText.setColumns(10);
		
		courseNameText = new JTextField();
		courseNameText.setBounds(154, 49, 146, 26);
		frame.getContentPane().add(courseNameText);
		courseNameText.setColumns(10);
		
		JButton btnNewButton = new JButton("Send Request");
		btnNewButton.addActionListener(new ActionListener() {         // An on-click listener for Send Button on Student GUI
			public void actionPerformed(ActionEvent arg0) {
			
				//Reading the studentName
				String studentName = studentNameText.getText();
				
				//Reading the courseName
				String courseName = courseNameText.getText();
				
				//Creating the initial Message object request to send to the Queuing Server
				Message message = new Message(studentName,courseName,"-");
				
				try {
                        
					
                    dos.writeUTF("StudentProcess,"+message.getStudentName()+","+message.getCourseName()+","+message.getStatus());
                 
                } catch (IOException e) {
                    e.printStackTrace();
                }
				
			}
		});
		btnNewButton.setBounds(154, 112, 146, 29);
		frame.getContentPane().add(btnNewButton);
		
		JButton exitButton = new JButton("Exit");
		exitButton.addActionListener(new ActionListener() {     // An on-click listener function on Close button to properly logout
			public void actionPerformed(ActionEvent e) {
				
				try {
                    dos.writeUTF("logout");
                   // frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
                    frame.setVisible(false);
                    frame.dispose();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
			}
			
		});
		exitButton.setBounds(369, 112, 132, 29);
		frame.getContentPane().add(exitButton);
		
	/*	JLabel lblStatusOfAdvisor = new JLabel("Status of Advisor and Notification Process");
		lblStatusOfAdvisor.setBounds(15, 157, 321, 26);
		frame.getContentPane().add(lblStatusOfAdvisor);
		
		JTextArea textArea = new JTextArea();
		textArea.setBounds(15, 192, 285, 152);
		frame.getContentPane().add(textArea);   */
	}
}

// A helper class that composes the message so that it can be stored as an object in the Messaging Server Queue
class Message{
	
	String studentName;
	String courseName;
	String status;
	
	Message(String studentName,String courseName,String status)
	{
		this.studentName=studentName;
		this.courseName=courseName;
		this.status=status;
	}
	
	public String getStudentName() {
		return studentName;
	}
    public String getCourseName() {
		return courseName;
	}
    public String getStatus() {
	    return status;
    }
    public String toString() {
    	return studentName+","+courseName+","+status;
    }
}