import java.io.*;
import java.net.*;

class ClientHandler implements Runnable {
    private Socket socket;
    private BufferedReader in;
    private BufferedWriter out;
    private String username;

    public ClientHandler(Socket socket) {
        try {
            this.socket = socket;
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            
            // Register username
            this.username = in.readLine();
            ServerSide.addUser(username, this);
            broadcast("Server: " + username + " has joined the chat.");
        } catch (IOException e) {
            closeEverything();
        }
    }

    @Override
    public void run() {
        String message;
        while (socket.isConnected()) {
            try {
                message = in.readLine();
                if (message.startsWith("/message")) {
                    privateMessage(message);
                } else if (message.equals("/online")) {
                    listOnlineUsers();
                } else {
                    broadcast(username + ": " + message);
                }
            } catch (IOException e) {
                closeEverything();
                break;
            }
        }
    }

    private void broadcast(String message) {
        for (ClientHandler client : ServerSide.userMap.values()) {
            try {
                if (!client.username.equals(username)) {
                    client.out.write(message);
                    client.out.newLine();
                    client.out.flush();
                }
            } catch (IOException e) {
                client.closeEverything();
            }
        }
    }

    private void privateMessage(String command) {
        String[] parts = command.split(" ", 3);
        if (parts.length < 3) {
            sendMessage("Invalid command. Use /message <username> <message>");
            return;
        }
        String targetUsername = parts[1];
        String privateMessage = parts[2];
        ClientHandler targetClient = ServerSide.getUser(targetUsername);
        if (targetClient != null) {
            targetClient.sendMessage("Private from " + username + ": " + privateMessage);
        } else {
            sendMessage("User " + targetUsername + " not found.");
        }
    }

    private void listOnlineUsers() {
        sendMessage("Online users: " + ServerSide.getAllUsernames());
    }

    public void sendMessage(String message) {
        try {
            out.write(message);
            out.newLine();
            out.flush();
        } catch (IOException e) {
            closeEverything();
        }
    }

    private void closeEverything() {
        try {
            ServerSide.removeUser(username);
            if (in != null) in.close();
            if (out != null) out.close();
            if (socket != null) socket.close();
            broadcast("Server: " + username + " has left the chat.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
