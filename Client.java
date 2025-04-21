import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

public class Client extends JFrame {
    private JTextArea chatArea;
    private JTextField inputField;
    private JButton sendButton;

    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;

    public Client() {
        setTitle("Client Chat");
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
        connectToServer();
    }

    private void connectToServer() {
        try {
            socket = new Socket("localhost", 5000);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream(), true);

            new Thread(() -> receiveMessages()).start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendMessage() {
        String message = inputField.getText();
        writer.println(message);
        chatArea.append("Client: " + message + "\n");
        inputField.setText("");
    }

    private void receiveMessages() {
        try {
            String msg;
            while ((msg = reader.readLine()) != null) {
                chatArea.append("Server: " + msg + "\n");
            }
        } catch (IOException e) {
            chatArea.append("Connection closed.\n");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Client::new);
    }
}
