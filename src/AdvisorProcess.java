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

public class AdvisorProcess {
	private JFrame frame;
	public static JTextArea textArea;
	final static int ServerPort = 12345;
	private static DataOutputStream dos;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) throws UnknownHostException, IOException {
		
		// A GUI window is opened for this Process
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AdvisorProcess window = new AdvisorProcess();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		//Scanner scn = new Scanner(System.in); 
        
        // getting localhost ip 
        InetAddress ip = InetAddress.getByName("localhost"); 
          
        // establish the connection 
        Socket s = new Socket(ip, ServerPort); 
          
        // obtaining input and out streams 
        DataInputStream dis = new DataInputStream(s.getInputStream());
        dos = new DataOutputStream(s.getOutputStream());
        dos.writeUTF("Advisor Process");
  
        // The below thread keeps checking the file out.txt for any student requests and responds by taking status decision and writing in out_next.txt file
        Thread checkForMessage = new Thread(new Runnable() {
        	
        	
        	public void run() {
        		while(true) {
        			 String oldMessage="";
        		    
        		        //read the message from file , if file is empty then wait for 3 seconds and again perform read operation.
        		     // read the content from file
        				try(BufferedReader bufferedReader = new BufferedReader(new FileReader("out.txt"))) {  
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
        					textArea.append("No message found"+"\n");
        					try {
        						Thread.sleep(3000);    // Wait for 3 seconds if there is no requests pending from student side
        					} catch (InterruptedException e) {
        						// TODO Auto-generated catch block
        						e.printStackTrace();
        					}
        				}
        				else
        				{
        					StringBuffer sb =new StringBuffer();
        					String[] requests = oldMessage.substring(0, oldMessage.length()-1).split(";");
        					for(String s1: requests)
        					{
        						String studentName=s1.split(",")[0];
        						String courseName=s1.split(",")[1];
        						String status=s1.split(",")[2];
        						
        						//make and change the decision as per randomness
        						double random = Math.random();
        		                if(status.equals("-"))
        		                {
        		                	if(random <= 0.5)  // If value is less than or equal to 0.5, status is assumed as Approved
        		                	{status="Approved";
        		                	System.out.println(status);
        		                	}
        		                    else
        		                	{status="Disapproved";  //  If value is more than 0.5, status is assumed as Disapproved
        		                	System.out.println(status);
        		                	}
        		                	
        		                	textArea.append(status+"\n");  // Displaying the status on the GUI
        		                }
        						String m = studentName+","+courseName+","+status+";";
        						sb.append(m);  // Organizing the content of a single request at a time to finally write into the out_next.txt file for Notification process
        		                
        					}
        				
        			        //Write back the entire response in a separate file. Also, write the decision on the GUI.
        					 // write the content to the file
        		    		try(BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("out_next.txt"))) {  
        		    		   String message1 = new String(sb);
        		    		bufferedWriter.write(message1);  // Writing the final decision of Advisor in the out_text.txt file 
        		    		
        		    		
        		    		  
        		    		} catch (IOException e) {
        		    		    // exception handling
        		    		}
        		    		
        		    		// Flushing out all the contents of out.txt once the requests has been served.
        		    		try(BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("out.txt"))) {  
         		    		 bufferedWriter.write("");
         		    		
         		    		
         		    		  
         		    		} catch (IOException e) {
         		    		    // exception handling
         		    		}
        		}
        	}
        		}
         }
        });
        
        checkForMessage.start();
        
        
        
        
        // sendMessage thread 
        Thread sendMessage = new Thread(new Runnable()  
        { 
            @Override
            public void run() { 
                while (true) { 
  
                    // read the message to deliver. 
                   //String msg = scn.nextLine(); 
                    String request = "advisorProcess,request" ; 
                    try { 
                        // write on the output stream 
                        dos.writeUTF(request); 
                    } catch (IOException e) { 
                        e.printStackTrace(); 
                    } 
                } 
            } 
        }); 
          
        // readMessage thread 
        Thread readMessage = new Thread(new Runnable()  
        { 
            @Override
            public void run() { 
  
                while (true) { 
                    try { 
                        // read the message sent to this client 
                        String msg = dis.readUTF();
                        System.out.println(msg);
                        // Write the logic for approval/disapproval and write back to the server. If you find "no message found" then 
                        // skip and also sleep the sendMessage thread for 3 seconds to stop sending requests to server.
                        
                     String message[] = msg.split(",");
                        if(!message[0].equalsIgnoreCase("No message found"))
                        { 
                        	System.out.println(msg);
                        	String status = message[2];
                            double random = Math.random();
                            if(status.equals("-"))
                            {
                            	if(random <= 0.5)
                            	{status="Approved";
                            	System.out.println(status);
                            	}
                                else
                            	{status="Disapproved";
                            	System.out.println(status);
                            	}
                            }
                            Message m = new Message(message[0],message[1],status);
                            textArea.append("Pa");
                            textArea.append(status+"\n");                           // Printing the decision in textarea.
                            dos.writeUTF("advisorProcess,response"+m.getStudentName()+","+m.getCourseName()+","+m.getStatus());
                        }
                        else
                        {
                        	try {
								sendMessage.sleep(3000);
								//Thread.sleep(3000);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
                        }	
                        
                      //  System.out.println(msg); 
                    } catch (IOException e) { 
  
                        e.printStackTrace(); 
                    } 
                } 
            } 
        }); 
  
     //   sendMessage.start(); 
    //    readMessage.start(); 
		
	}

	/**
	 * Create the application.
	 */
	public AdvisorProcess() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setTitle("Advisor Process");
		frame.setBounds(100, 100, 806, 649);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		textArea = new JTextArea();
		textArea.append("");
		textArea.setBounds(15, 37, 490, 540);
		frame.getContentPane().add(textArea);
		
		JLabel lblAdvisorsDecision = new JLabel("Advisor's Decision:");
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
