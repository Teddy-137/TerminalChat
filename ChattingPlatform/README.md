Chatting platform project Software Architecture

Note: this project is still Underdevelopment, so it contains several bugs. I'll figure out how to debug them in the future

#Overview

    This platform enables realtime text communication between clients connected to the server.
    It has two major classes:

        -ClientSide 
        -ServerSide

#fuctionalities

    i. send and receive messages in realtime
    ii. broadcasting messages for the clients in a chatroom
    iii. user authentication for login page(under development)
    iv.chat history(under development)

#Software Architecture

    -Client-Server Architecture
    -TCP/IP protocol
    -Multi-threading

#Tools and Libraries

    -Java Networking (java.net): ServerSocket and Socket.
    -Concurrency (java.util.concurrent): For managing threads.
    -Input/Output (java.io)

#Design Details

    Server Design
        Classes:
        -ServerSide:
            -Starts the server.
            -Manages client connections using threads.
            -Broadcasts messages to all connected clients.
        -ClientHandler:
            -thread that handles a single client.
            -Reads messages from the client and passes them to the server for broadcasting.
            -Data Structures:
                ArrayList of Clients
        -ClientSide Design
            Classes:
             i.ClientSide:
                -Connects to the server.
                -Sends messages typed by the user.
                -Listens for incoming messages and displays them.

#Underdevelopment
             
                -authentication
                -GUI
                -Encryption
    
