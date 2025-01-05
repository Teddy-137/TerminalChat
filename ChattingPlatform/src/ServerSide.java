
import java.io.*;
import java.net.*;
import java.util.*;
class ServerSide {
    private ServerSocket serverSocket;
    static HashMap<String, ClientHandler> userMap = new HashMap<>();

    public ServerSide(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public void startServer() throws IOException {
        System.out.println("Server is running...");
        while (!serverSocket.isClosed()) {
            Socket socket = serverSocket.accept();
            System.out.println("A new client has connected.");
            ClientHandler clientHandler = new ClientHandler(socket);
            Thread thread = new Thread(clientHandler);
            thread.start();
        }
    }

    public static synchronized void addUser(String username, ClientHandler handler) {
        userMap.put(username, handler);
    }

    public static synchronized void removeUser(String username) {
        userMap.remove(username);
    }

    public static synchronized ClientHandler getUser(String username) {
        return userMap.get(username);
    }

    public static synchronized Set<String> getAllUsernames() {
        return userMap.keySet();
    }

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(9876);
        ServerSide serverSide = new ServerSide(serverSocket);
        serverSide.startServer();
    }
}