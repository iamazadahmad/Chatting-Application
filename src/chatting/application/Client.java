package chatting.application;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Client implements ActionListener {
    static JFrame frame = new JFrame();
    JTextField text;
    static JPanel messageArea;
    static Box vertical = Box.createVerticalBox();
    static DataOutputStream dataOutputStream;

    Client() {
//        Creating frame
        frame.setSize(450, 700);
        frame.setLocation(800, 24);
        frame.setLayout(null);
        frame.getContentPane().setBackground(Color.WHITE);

//        Adding nav-bar to the frame
        JPanel panel1 = new JPanel();
        panel1.setBackground(Color.BLUE);
        panel1.setBounds(0, 0, 450, 50);
        panel1.setLayout(null);
        frame.add(panel1);

//        Adding profile Image
        ImageIcon icon1 = new ImageIcon(ClassLoader.getSystemResource("assets/client.png"));
        Image icon2 = icon1.getImage().getScaledInstance(48, 48, Image.SCALE_DEFAULT);
        ImageIcon icon3 = new ImageIcon(icon2);
        JLabel profile = new JLabel(icon3);
        profile.setBounds(2, 1, 48, 48);
        panel1.add(profile);


//        Adding video call icon
        ImageIcon icon4 = new ImageIcon(ClassLoader.getSystemResource("assets/video.png"));
        Image icon5 = icon4.getImage().getScaledInstance(48, 48, Image.SCALE_DEFAULT);
        ImageIcon icon6 = new ImageIcon(icon5);
        JLabel video = new JLabel(icon6);
        video.setBounds(290, 1, 48, 48);
        panel1.add(video);

//        Adding call icon
        ImageIcon icon7 = new ImageIcon(ClassLoader.getSystemResource("assets/call.png"));
        Image icon8 = icon7.getImage().getScaledInstance(48, 48, Image.SCALE_DEFAULT);
        ImageIcon icon9 = new ImageIcon(icon8);
        JLabel call = new JLabel(icon9);
        call.setBounds(355, 1, 48, 48);
        panel1.add(call);

//        Adding menu button
        ImageIcon icon10 = new ImageIcon(ClassLoader.getSystemResource("assets/menu.png"));
        Image icon11 = icon10.getImage().getScaledInstance(48, 48, Image.SCALE_DEFAULT);
        ImageIcon icon12 = new ImageIcon(icon11);
        JLabel menu = new JLabel(icon12);
        menu.setBounds(400, 1, 48, 48);
        panel1.add(menu);

//        Adding profile name
        JLabel clientName = new JLabel("Client");
        clientName.setBounds(52, 10, 100, 20);
        clientName.setForeground(Color.WHITE);
        clientName.setFont(new Font("POPPINS", Font.BOLD, 18));
        panel1.add(clientName);

        JLabel online = new JLabel("online");
        online.setBounds(52, 30, 100, 20);
        online.setForeground(Color.WHITE);
        online.setFont(new Font("POPPINS", Font.BOLD, 10));
        panel1.add(online);

//        Creating message area
        messageArea = new JPanel();
        JScrollPane scrollPane = new JScrollPane(messageArea);
        scrollPane.setBounds(5, 55, 440, 600);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        frame.add(scrollPane);

        text = new JTextField();
        text.setBounds(5, 660, 350, 40);
        text.setFont(new Font("POPPINS", Font.PLAIN, 16));
        frame.add(text);

        JButton send = new JButton("Send");
        send.setBounds(360, 660, 85, 40);
        send.setBackground(Color.blue);
        send.setFont(new Font("POPPINS", Font.PLAIN, 16));
        send.setForeground(Color.white);
        frame.add(send);
        send.addActionListener(this);

//        Set visibility
        frame.setUndecorated(true);
        frame.setVisible(true);
    }

    public void actionPerformed(ActionEvent ae) {
        try {

//            Retrieves the text content entered by the user in the input text field
            String message = text.getText();

            if (!(message.equals(""))) {

//                Creates a panel to hold the message content and the timestamp
                JPanel messagePanel = formatLabel(message, false);
                messageArea.setLayout(new BorderLayout());
                JPanel right = new JPanel(new BorderLayout());
                right.add(messagePanel, BorderLayout.LINE_END);

//                Aligning the messages in a vertical order
                vertical.add(right);
                vertical.add(Box.createVerticalStrut(15));
                messageArea.add(vertical, BorderLayout.PAGE_END);
                dataOutputStream.writeUTF(message);
                text.setText("");

//                Refreshing the frame to update messages
                frame.repaint();
                frame.invalidate();
                frame.validate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static JPanel formatLabel(String message, boolean isReceived) {

//        Creates a panel to hold the message content and the timestamp
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

//        Creates JLabel called output, which will display the message content
        JLabel output = new JLabel(message);
        output.setFont(new Font("POPPINS", Font.PLAIN, 16));

//        Coloring the output
        if (isReceived) {
            output.setBackground(new Color(239, 118, 122));
        } else {
            output.setBackground(Color.blue);
        }
        output.setForeground(Color.white);
        output.setOpaque(true);
        output.setBorder(new EmptyBorder(15, 15, 15, 50));
        panel.add(output);

//        Adding Time
        Calendar calender = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
        JLabel time = new JLabel();
        time.setText(sdf.format(calender.getTime()));
        panel.add(time);

//        Return
        return panel;
    }

    public static void main(String[] args) {
        new Client();

        try {

//            Creates a new client-side Socket and connects it to the server running on the same machine (localhost) at port 6001
            Socket socket = new Socket("127.0.0.1", 6001);

//            Creates a DataInputStream to read data from the socket's input stream
            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());

//            Creates a DataOutputStream to write data to the socket's output stream
            dataOutputStream = new DataOutputStream(socket.getOutputStream());

            while (true) {

//                Reads a message from the server through the dataInputStream
                messageArea.setLayout(new BorderLayout());
                String message = dataInputStream.readUTF();

//                Creates a panel to hold the message content and the timestamp
                JPanel panel = formatLabel(message, true);
                JPanel left = new JPanel(new BorderLayout());
                left.add(panel, BorderLayout.LINE_START);

//                Aligning the messages in a vertical order
                vertical.add(left);
                vertical.add(Box.createVerticalStrut(15));
                messageArea.add(vertical, BorderLayout.PAGE_END);
                frame.validate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}