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
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;

public class NotificationProcess {
	private JFrame frame;
    private static JTextArea textArea;
    final static int ServerPort = 12345;
    private static DataOutputStream dos;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args)  throws UnknownHostException, IOException{
		
		// A GUI window is opened for this Process
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					NotificationProcess window = new NotificationProcess();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		// getting localhost ip 
        InetAddress ip = InetAddress.getByName("localhost"); 
          
        // establish the connection 
        Socket s = new Socket(ip, ServerPort); 
        
          
        // obtaining input and out streams 
        DataInputStream dis = new DataInputStream(s.getInputStream()); 
        dos = new DataOutputStream(s.getOutputStream());
        dos.writeUTF("Notification Process");
        
        // Below thread checks in the persistent storage file which stores decisions from Advisor so as to show on its GUI. 
        Thread checkForDecision = new Thread(new Runnable() {
        	
        	
        	public void run() {
        		while(true) {
        			 String oldMessage="";
        		    
        		     //read the message from file , if file is empty then wait for 3 seconds and again perform read operation.
        		     // read the content from file
        				try(BufferedReader bufferedReader = new BufferedReader(new FileReader("out_next.txt"))) {  
        				    oldMessage = bufferedReader.readLine();

        				} catch (FileNotFoundException e) {
        				    // exception handling
        				} catch (IOException e) {
        				    // exception handling
        				}
        				if(textArea != null) {
        				if(oldMessage == null || oldMessage.equals(""))
        				{
        					//Thread.wait(2000);
        					
        					textArea.append("No message found"+"\n"); // Notification GUI shows this message if there are no more decisions (made by advisor) available in the file 
        					try {
        						Thread.sleep(7000);  // Wait for 7 seconds if there is no decision from advisors
        					} catch (InterruptedException e) {
        						// TODO Auto-generated catch block
        						e.printStackTrace();
        					}
        				}
        				else
        				{
        					StringBuffer sb =new StringBuffer();
        					String[] requests = oldMessage.substring(0, oldMessage.length()-1).split(";");
        					for(String s1: requests)    // Extracting the values from the received content in the file out_next.txt
        					{
        						String studentName=s1.split(",")[0];
        						String courseName=s1.split(",")[1];
        						String status=s1.split(",")[2];
        						
        						
        						String m = "Student Name: "+studentName+","+"Course Name: "+courseName+","+"Status: "+status+";";
        						textArea.append(m+"\n");  // Notification GUI simply displays each decision made by the Advisor on each Student's request.
        		                
        					}
        				
        			        //Write back the entire response in a separate file. Also, write the decision on the GUI.
        					// Flushing out all the contents of out_next.txt once the decisions of advisor are all displayed.
        		    		try(BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("out_next.txt"))) {  
        		    	        bufferedWriter.write("");
        		    		
        		    		} catch (IOException e) {
        		    		    // exception handling
        		    		}
        		    		
        		}
        	} }
         }
        });
        
        checkForDecision.start();
		
		
	}

	/**
	 * Create the application.
	 */
	public NotificationProcess() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setTitle("Notification Process");
		frame.setBounds(100, 100, 806, 649);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		textArea = new JTextArea();
		textArea.setBounds(15, 37, 490, 540);
		frame.getContentPane().add(textArea);
		
		JLabel lblAdvisorsDecision = new JLabel("Notifications for Student:");
		lblAdvisorsDecision.setBounds(15, 16, 166, 20);
		frame.getContentPane().add(lblAdvisorsDecision);
		
		JButton btnExit = new JButton("Exit");
		btnExit.addActionListener(new ActionListener() {     // An on-click listener function on Close button to properly logout
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
		btnExit.setBounds(603, 37, 115, 29);
		frame.getContentPane().add(btnExit);
	}
}
