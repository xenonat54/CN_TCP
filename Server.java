import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

public class Server extends JFrame {
    private JTextArea chatArea;
    private JTextField inputField;
    private JButton sendButton;

    private ServerSocket serverSocket;
    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;

    public Server() {
        setTitle("Server Chat");
        setSize(400, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        chatArea = new JTextArea();
        chatArea.setEditable(false);
        add(new JScrollPane(chatArea), BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        inputField = new JTextField();
        sendButton = new JButton("Send");

        bottomPanel.add(inputField, BorderLayout.CENTER);
        bottomPanel.add(sendButton, BorderLayout.EAST);
        add(bottomPanel, BorderLayout.SOUTH);

        sendButton.addActionListener(e -> sendMessage());

        setVisible(true);
        startServer();
    }

    private void startServer() {
        try {
            serverSocket = new ServerSocket(5000);
            chatArea.append("Server started. Waiting for client...\n");
            socket = serverSocket.accept();

            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream(), true);

            chatArea.append("Client connected.\n");

            new Thread(() -> receiveMessages()).start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendMessage() {
        String message = inputField.getText();
        writer.println(message);
        chatArea.append("Server: " + message + "\n");
        inputField.setText("");
    }

    private void receiveMessages() {
        try {
            String msg;
            while ((msg = reader.readLine()) != null) {
                chatArea.append("Client: " + msg + "\n");
            }
        } catch (IOException e) {
            chatArea.append("Connection closed.\n");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Server::new);
    }
}
