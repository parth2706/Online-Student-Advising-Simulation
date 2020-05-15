# Online-Student-Advising-Simulation
By using principles of network programming in Java, this project serves the purpose of implementing a messaging queue between an Advisor and Student by showing timely notifications

This is an academic project based on my CSE 5306 - Distributed Systems Course by Prof. Chance Eary. This advising simulation is based on a client-server architecture model.

Process flow of the aplication --
The project consists of a student GUI window sending approval requests for different subjects to the Advisor via a central Server which manages connections of each student with respective Advisor.
The server also buffers the requests by storing into a messaging queue so that it can send to the Advisor GUI when it is active.
After receiving the request of Student from the Server, the Advisor randomly and automatically approves or disapproves the request for that  subject and the final result is shown on the Notification GUI screen which is again passed via the Server.


Running the application --
Run the below files in exactly the same order as below -
         -> MessageQueuingServer.java
         -> AdvisorProcess.java
         -> StudentProcess.java
         -> NotificationProcess.java


Visual Illustration --
1) After running MessageQueuingServer.java, a window opens which will have a real-time list of processes active (Advisor/Student(s)/Notification). The server also buffers the requests by storing into a messaging queue so that it can send to the Advisor GUI when it is active. 

2) After running AdvisorProcess.java, a window opens which keeps checking every 3 seconds for any course registeration request from Student. If it receives any request then it randomly selects "Approved"/"Disapproved" and shows on screen otherwise if the request was not made then it simply prints "No message found".

3) After running StudentProcess.java, a window opens which asks for student name and course name to send a request for registration to the available advisor.

4) After running the NotificationProcess.java, a window opens which shows student name, course name requested to the advisor and what was the status. If there is no request made, then it simple prints "No message found".
