// Student Name: Parth B. Mehta
// Student Id: 1001668756
// Basic part or framework of source code is taken from https://www.geeksforgeeks.org/multi-threaded-chat-application-set-1/

import java.awt.EventQueue;
import java.awt.Font;
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
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;




public class MessageQueuingServer {
	static Vector<ClientHandler> ar = new Vector<>();   // Thread-safe vector list to save client connection information
    static Queue<Message> queue = new LinkedList<Message>(); // common messaging queue to store objects of Message which is shared by all active processes 
    // counter for clients 
    static int i = 0; 
    private JFrame frame;
     static JTextArea activeProcessTextArea; // textarea to display active processes (student/advisor/notification)
  
    /**
	 * Create the application.
	 */
	public MessageQueuingServer() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 674, 571);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Message Queuing Server");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 18));
		lblNewLabel.setBounds(175, 71, 272, 47);
		frame.getContentPane().add(lblNewLabel);
		
		JLabel activeProcessLabel = new JLabel("List of Active Processes");
		activeProcessLabel.setFont(new Font("Tahoma", Font.PLAIN, 17));
		activeProcessLabel.setBounds(15, 194, 205, 20);
		frame.getContentPane().add(activeProcessLabel);
		
		activeProcessTextArea = new JTextArea();
		activeProcessTextArea.setBounds(15, 223, 205, 156);
		frame.getContentPane().add(activeProcessTextArea);
		
		JButton btnClose = new JButton("Close");
		btnClose.addActionListener(new ActionListener() {     // An on-click listener function on Close button to properly logout
			public void actionPerformed(ActionEvent e) {
				
				try {
                    
					for(ClientHandler ch:MessageQueuingServer.ar )
                    {
                    	if(ch.isloggedin)
                    	{
                    		ch.dis.close();   // Closing the respective DataInputStream of socket
                    		ch.dos.close();   // Closing the respective DataOutputStream of socket
                    		ch.s.close();     // Closing the respective socket
                    		ch.isloggedin=false;  // Logging out the client
                    		//sb.append(ch.name+"\n");
                    	}
                    }
                  //  dos.writeUTF("logout");
                   // frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
                    frame.setVisible(false);
                    frame.dispose();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
			}
			
		});
		btnClose.setBounds(244, 134, 115, 29);
		frame.getContentPane().add(btnClose);
	}
    
    
    
    
    
    public static void main(String[] args) throws IOException  
    { 
    	// Opening a GUI window for this server.
    	EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MessageQueuingServer window = new MessageQueuingServer();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
    	
        // server is listening on port 1234 
        ServerSocket ss = new ServerSocket(12345); 
          
        Socket s; 
          
        // running infinite loop for getting 
        // client request 
        while (true)  
        { 
            // Accept the incoming request and store the client socket as 's'
            s = ss.accept(); 
  
            System.out.println("New client request received : " + s); 
              
            // obtain input and output streams 
            DataInputStream dis = new DataInputStream(s.getInputStream()); 
            DataOutputStream dos = new DataOutputStream(s.getOutputStream()); 
              
            System.out.println("Creating a new handler for this client..."); 
            
            
            String nameOfProcess = dis.readUTF(); 
            
            
            // Create a new handler object for handling this request. 
          //  ClientHandler mtch = new ClientHandler(s,"client " + i, dis, dos); 
            ClientHandler mtch = new ClientHandler(s,nameOfProcess, dis, dos);
  
            // Create a new Thread with this object. 
            Thread t = new Thread(mtch); 
              
            System.out.println("Adding this client to active client list"); 
            
            activeProcessTextArea.append(nameOfProcess+"\n");
            // add this client to active clients list 
            ar.add(mtch); 
  
            // start the thread. 
            t.start(); 
  
            // increment i for new client. 
            // i is used for naming only, and can be replaced 
            // by any naming scheme 
            i++; 
  
        } 
    } 
} 
  
// ClientHandler class to keep listening for requests from every connected client and responding accordingly.
class ClientHandler implements Runnable  
{ 
    Scanner scn = new Scanner(System.in); 
    private String name; 
    final DataInputStream dis; 
    final DataOutputStream dos; 
    Socket s; 
    boolean isloggedin; 
      
    // constructor 
    public ClientHandler(Socket s, String name, 
                            DataInputStream dis, DataOutputStream dos) { 
        this.dis = dis; 
        this.dos = dos; 
        this.name = name; 
        this.s = s; 
        this.isloggedin=true; 
    } 
  
    @Override
    public void run() { 
  
        String received; 
        while (true)  
        { 
            try
            { 
                // receive the string 
                received = dis.readUTF(); 
                  
             //   System.out.println(received); 
                  
                String message[]=received.split(",");
                String sendersName = message[0];
                if(sendersName.equalsIgnoreCase("studentProcess")) // handling messages from Student Process
                {
                	String studentName = message[1];
                	String courseName = message[2];
                	String status = message[3];
                	String oldMessage="";
                	//Message m = new Message(message[1],message[2],message[3]);
                	
                	// Adding data into file
                	// read the content from file, this is the old content which contains old requests from student that has not been processed 
            		try(BufferedReader bufferedReader = new BufferedReader(new FileReader("out.txt"))) {  
            		    oldMessage = bufferedReader.readLine();
//            		    while(oldMessage != null) {
//            		        System.out.println(oldMessage);
//            		        oldMessage = bufferedReader.readLine();
//            		    }
            		} catch (FileNotFoundException e) {
            		    // exception handling
            		} catch (IOException e) {
            		    // exception handling
            		}
                        
    				  // writing the old and new request back in the file to store requests persistently
                		try(BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("out.txt"))) {  
                		   String message1 = studentName+","+courseName+","+status+";";
                		   if(oldMessage == null)
                			   oldMessage="";
                		bufferedWriter.write(oldMessage+message1);
                		  
                		} catch (IOException e) {
                		    // exception handling
                		}

                		
                    
                	
                	
                	
                	
                	//Message m = new Message(studentName,courseName,status);
                	//MessageQueuingServer.queue.add(m);	
                	//System.out.println(MessageQueuingServer.queue.size());
                	
                }
                else if(sendersName.equalsIgnoreCase("advisorProcess"))  // handling messages with Advisor Process
                {
                	String reqORresp = message[1];
                	if(reqORresp.equalsIgnoreCase("request"))    // action when receiving message form Advisor
                	{
                		Message m = MessageQueuingServer.queue.poll();   // removing the first student request from queue and passing it to the advisor for decision 
                		
                	    if(m != null)
	                	{
                	    	System.out.println(m.toString());
	                		dos.writeUTF(m.getStudentName()+","+m.getCourseName()+","+m.getStatus());  // sending student request to advisor
	                	}
	                	else {
	                		dos.writeUTF("No message found,");     // If no requests are present, then sending this message to advisor
	                	}
                	}
                	else if(reqORresp.equalsIgnoreCase("response"))  // action taken when Advisor has taken a decision on student's request
                	{
                		String studentName = message[2];
                    	String courseName = message[3];
                    	String status = message[4];
                    	Message m = new Message(studentName,courseName,status);
                    	MessageQueuingServer.queue.add(m);		    // After the advisor has decided the approval/disapproval, storing this message back into the queue
                	}
                }
                else if(sendersName.equalsIgnoreCase("notificationProcess"))  // handling messages with Notification Process
                {
                	String reqORresp = message[1];
                	if(reqORresp.equalsIgnoreCase("request"))   // action when receiving message form Notification
                	{
                		Message m = MessageQueuingServer.queue.poll();
                	    if(m != null)
	                	{
	                		dos.writeUTF(m.getStudentName()+","+m.getCourseName()+","+m.getStatus());  // sending final decision taken by advisor to notification
	                	}
	                	else {
	                		dos.writeUTF("No message found");  // if no decision has been made or no pending decision is left by advisor then this message is sent to notification
	                	}
                	}
                }
                
                if(sendersName.equals("logout")){     // Handling logout event (without crash) from all all three processes
                    this.isloggedin=false; 
                    this.s.close();
                    StringBuffer sb=new StringBuffer();
                    for(ClientHandler ch:MessageQueuingServer.ar )
                    {
                    	if(ch.isloggedin)
                    	{
                    		sb.append(ch.name+"\n");
                    	}
                    }
                    MessageQueuingServer.activeProcessTextArea.setText(sb.toString());  // Setting the new updated list of active processes
                    break; 
                } 
                    
                
            } catch (IOException e) { 
                  
                e.printStackTrace(); 
            } 
              
        } 
        try
        { 
            // closing resources 
            this.dis.close(); 
            this.dos.close(); 
              
        }catch(IOException e){ 
            e.printStackTrace(); 
        } 
    } 
} 
