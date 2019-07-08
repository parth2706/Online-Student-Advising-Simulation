# Online-Student-Advising-Simulation
By using principles of network programming in Java, this project serves the purpose of implementing a messaging queue between an Advisor and Student by showing timely notifications

This is an academic project based on my CSE 5306 - Distributed Systems Course by Prof. Chance Eary. This advising simulation is based on a client-server architecture model.

The project consists of a student GUI window sending approval requests for different subjects to the Advisor via a central Server which manages 
connections of each student with respective Advisor.

The server also buffers the requests by storing into a messaging queue so that it can send to the Advisor GUI when it is active.

After receiving the request of Student from the Server, the Advisor randomly and automatically approves or disapproves the request for that  subject and the final result is shown on the Notification GUI screen which is again passed via the Server.
