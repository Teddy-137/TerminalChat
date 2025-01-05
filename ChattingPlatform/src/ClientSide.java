import java.io.*;
import java.net.*;
import java.util.*;
class ClientSide {
    private Socket socket;
    private BufferedReader in;
    private BufferedWriter out;
    public ClientSide(Socket socket, String username) {
        try {
            this.socket = socket;
            this.out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            // Send username to server
            out.write(username);
            out.newLine();
            out.flush();
        } catch (IOException e) {
            closeEverything();
        }
    }

    public void sendMessage() {
        try (Scanner scanner = new Scanner(System.in)) {
            while (socket.isConnected()) {
                String message = scanner.nextLine();
                out.write(message);
                out.newLine();
                out.flush();
            }
        } catch (IOException e) {
            closeEverything();
        }
    }

    public void listenForMessages() {
        new Thread(() -> {
            String message;
            while (socket.isConnected()) {
                try {
                    message = in.readLine();
                    System.out.println(message);
                } catch (IOException e) {
                    closeEverything();
                    break;
                }
            }
        }).start();
    }

    private void closeEverything() {
        try {
            if (socket != null) socket.close();
            if (in != null) in.close();
            if (out != null) out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("Enter your username: ");
            String username = scanner.nextLine();

            Socket socket = new Socket("localhost", 9876);
            ClientSide client = new ClientSide(socket, username);

            client.listenForMessages();
            client.sendMessage();
        }
    }
}