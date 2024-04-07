import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class MyGui extends JFrame {
    private JButton liveButton, emptyButton, reload, phone;
    private JTextField[] textFields = new JTextField[3];
    private ArrayList<JTextField> phoneFields = new ArrayList<JTextField>();
    private JTextField inputField1, inputField2;
    private JTextField phoneInputNumber = new JTextField();
    private JTextField phoneInputType = new JTextField();
    private JTextField liveChances = new JTextField();

    public static int liveBucks = 0;
    public static int emptyBucks = 0;
    public static int allBucks = 0;
    public static int currentRound = 1;

    public MyGui() {
        setTitle("BuckshotCounter");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 600);
        setLayout(new GridLayout(10, 3, 10, 10)); // Adjust layout as needed

        // Create buttons
        liveButton = new JButton("Live");
        emptyButton = new JButton("Empty");
        reload = new JButton("Reload");
        phone = new JButton("Phone");
        liveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                liveHit();
            }
        });

        emptyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                emptyHit();
            }
        });

        reload.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                reload();
            }
        });

        phone.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                phone();
            }
        });

        // Create text fields
        for (int i = 0; i < textFields.length; i++) {
            JTextField current = new JTextField("Text Field " + (i + 1));
            current.setEditable(false);
            textFields[i] = current;
            add(textFields[i]);
        }
        liveChances.setEditable(false);
        add(liveChances);

        textFields[0].setText("All: " + allBucks);
        textFields[1].setText("Live: " + liveBucks);
        textFields[2].setText("Empty: " + emptyBucks);

        // Create input fields
        JTextField infoField1 = new JTextField("All:");
        inputField1 = new JTextField();
        JTextField infoField2 = new JTextField("Live:");
        inputField2 = new JTextField();

        infoField1.setEditable(false);
        infoField2.setEditable(false);

        // Add components to the frame
        add(liveButton);
        add(emptyButton);

        JTextField phoneInfoNumber = new JTextField("Round:");
        JTextField phoneInfoType = new JTextField("Type:");
        phoneInfoType.setEditable(false);
        phoneInfoNumber.setEditable(false);

        add(infoField1);
        add(inputField1);
        add(infoField2);
        add(inputField2);
        reload.setSize(300, 50);
        add(reload);

        JTextField empty = new JTextField();
        empty.setEditable(false);
        add(empty);

        add(phoneInfoNumber);
        add(phoneInputNumber);
        add(phoneInfoType);
        add(phoneInputType);
        add(phone);

        setVisible(true);
    }

    public static void main(String[] args) {
        // Run the GUI on the Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MyGui();
            }
        });
    }

    private void liveHit() {
        currentRound++;
        liveBucks--;
        allBucks--;
        textFields[1].setText("Live: " + liveBucks);
        newChances();
    }

    private void emptyHit() {
        currentRound++;
        emptyBucks--;
        allBucks--;
        textFields[2].setText("Empty: " + emptyBucks);
        newChances();
    }

    private void reload() {
        currentRound = 1;
        allBucks = Integer.parseInt(inputField1.getText());
        liveBucks = Integer.parseInt(inputField2.getText());
        emptyBucks = allBucks - liveBucks;
        textFields[0].setText("All: " + allBucks);
        textFields[1].setText("Live: " + liveBucks);
        textFields[2].setText("Empty: " + emptyBucks);
        newChances();
        phoneFields.forEach(this::remove);
        phoneFields = new ArrayList<JTextField>();
        revalidate();
        repaint();
    }

    private void phone() {
        JTextField newPhone = new JTextField("Round: " + phoneInputNumber.getText() + " " + phoneInputType.getText());
        newPhone.setEditable(false);
        phoneFields.add(newPhone);
        add(newPhone);
        revalidate();
        repaint();
    }

    private void newChances() {
        textFields[0].setText("All: " + allBucks);
        liveChances.setText("Live chances: " + String.format("%.1f", (Double.parseDouble("" + liveBucks) / allBucks * 100)) + "%");
    }
}
