This program served as a project for the subject Distributed Systems.

Project description:
Develop a Distributed System (DS) for a Typing Relay Race(Typera),
in this typing relay race a team of two elements have to write 140 characters (including spaces) as fast as possible.
Two main phases where the Typera DS operates:
1. The phase in which the teams are built
2. The phase in which a team plays

Users must be able to interact with the system by using a client application. The client
applications are capable of interacting with a multi-threaded server application. The
communication between the server application and the client applications is performed via TCP
sockets. The software must be written in Java.

2.1 Functionalities
1. User registration: Given a username and password, a user can be registered in the server
2. User login: Whenever a user wants to interact with the service, he/she must be able to
establish a connection authenticated by the server
3. Join a team: An authenticated user must be able to join a team
4. Play the game: An authenticated user that is a member of a team shall type a chunk of
text (80 characters) provided by the server as fast as possible

2.2 Team making
 Each team must be composed of 2 players
 As soon as a team is built, the 2 clients must be notified
 Clients must notify the server that they are ready to play

2.3 Playing the game
 The server shall send a synchronized countdown to all the team members
 The server shall send, in sequence, 2 chunks of texts to the clients
o The server chooses randomly, the order in which clients will be required to play
o The server can only send the 1st
 chunk of text (80 characters) when the count
down finishes
o The server can only send the 2nd chunk of text (80 characters) when the 1st
player already submitted his chunk of text
 The server has to count the time a team needs to finish a session (the 2 players typing
their texts chunks in sequence)
 If one player submit a text with errors, then it will not be counted as a valid result
 At the end of the game, the server has to send to all the team members the updated
record board (arranged from the smallest to the biggest).
 If the result is a record, the server also has to send a congratulation message.2.4 Notes
 It should be noted that there may be several teams simultaneously playing their own
game, which the server will have to manage concurrently.

2.5 Client
The client must provide an interface that supports the functionalities previously described. It is
not required that the client provides graphical user interfaces (windows, menus, buttons, etc.);
a command-line interface is sufficient (you will not get extra points for a pretty graphical user
interface so focus on the essential). The client must be written in Java and interact with the
server via TCP Sockets.

2.6 Server
The server must be written in Java and use Threads and TCP Sockets. Each thread cannot hold
more than one socket. The information related with the functionalities, communication and
user inputs must be recorded in the memory.