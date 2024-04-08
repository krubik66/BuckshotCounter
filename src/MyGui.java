import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class MyGui extends JFrame {
    private JButton liveButton, emptyButton, reload, phone;
    private JTextField[] textFields = new JTextField[3];
    private ArrayList<JTextField> phoneFields = new ArrayList<JTextField>();
    private ArrayList<PhoneRecord> phoneRecords = new ArrayList<PhoneRecord>();
    private JTextField inputField1, inputField2;
    private JTextField phoneInputNumber = new JTextField();
    private JTextField phoneInputType = new JTextField();
    private JTextField liveChances = new JTextField();
    private JTextField empty = new JTextField();
    private ButtonGroup phoneTypeButtons = new ButtonGroup();

    public static int liveBucks = 0;
    public static int emptyBucks = 0;
    public static int allBucks = 0;
    public static int currentRound = 1;
    private static boolean currentChoosenPhoneType = false;

    public MyGui() {
        setTitle("BuckshotCounter");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 600);
        setLayout(new GridLayout(15, 2, 10, 10)); // Adjust layout as needed

        // Create buttons
        liveButton = new JButton("Live");
        emptyButton = new JButton("Blank");
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
        textFields[2].setText("Blank: " + emptyBucks);

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

        empty.setEditable(false);
        empty.setText("Current shot: " + currentRound);
        add(empty);

        add(phoneInfoNumber);
        add(phoneInputNumber);
        add(phoneInfoType);
        JRadioButton option1 = new JRadioButton("Live");
        JRadioButton option2 = new JRadioButton("Blank");

        // Group the radio buttons

        phoneTypeButtons.add(option1);
        phoneTypeButtons.add(option2);

        // Add action listener to radio buttons
        ActionListener radioListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JRadioButton selectedRadioButton = (JRadioButton) e.getSource();
                currentChoosenPhoneType = (selectedRadioButton == option1);
            }
        };
        option1.addActionListener(radioListener);
        option2.addActionListener(radioListener);
        JPanel phoneTypePanel = new JPanel(new GridLayout(2, 1));
        phoneTypePanel.add(option1);
        phoneTypePanel.add(option2);
        add(phoneTypePanel);
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
        textFields[2].setText("Blank: " + emptyBucks);
        newChances();
    }

    private void reload() {
        currentRound = 1;
        allBucks = Integer.parseInt(inputField1.getText());
        liveBucks = Integer.parseInt(inputField2.getText());
        emptyBucks = allBucks - liveBucks;
        textFields[0].setText("All: " + allBucks);
        textFields[1].setText("Live: " + liveBucks);
        textFields[2].setText("Blank: " + emptyBucks);
        inputField1.setText("");
        inputField2.setText("");
        newChances();
        phoneFields.forEach(this::remove);
        phoneFields = new ArrayList<JTextField>();
        revalidate();
        repaint();
    }

    private void phone() {
        String currentBullet = "Blank";
        if(currentChoosenPhoneType) {
            currentBullet = "Live";
        }
        JTextField newPhone = new JTextField("Round: " + phoneInputNumber.getText() + " " + currentBullet);
        newPhone.setEditable(false);
        phoneRecords.add(new PhoneRecord(Integer.parseInt(phoneInputNumber.getText()), currentChoosenPhoneType));
        if(currentChoosenPhoneType) {
            liveBucks--;
            allBucks--;
            textFields[1].setText("Live: " + liveBucks);
            newChances();
        }
        else {
            emptyBucks--;
            allBucks--;
            textFields[2].setText("Blank: " + emptyBucks);
            newChances();
        }
        phoneFields.add(newPhone);
        add(newPhone);
        phoneInputType.setText("");
        phoneInputNumber.setText("");
        revalidate();
        repaint();
    }

    private void newChances() {
        phoneRecords.forEach(record -> {
            if(record.round == currentRound) {
                String currentBullet = "Blank";
                if(record.isLive) {
                    currentBullet = "Live";
                }
                JOptionPane.showMessageDialog(MyGui.this, "Current bullet: " + currentBullet);
                currentRound++;
            }
        });
        textFields[0].setText("All: " + allBucks);
        empty.setText("Current shot: " + currentRound);
        liveChances.setText("Live chances: " + String.format("%.1f", (Double.parseDouble("" + liveBucks) / allBucks * 100)) + "%");
    }

    private class PhoneRecord {
        public int round;
        public boolean isLive;
        public PhoneRecord(int round, boolean isLive) {
            this.round = round;
            this.isLive = isLive;
        }
    }
}
