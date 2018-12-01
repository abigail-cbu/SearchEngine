package Layout;

import com.intellij.uiDesigner.core.GridLayoutManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class MainGUI {
    final static boolean shouldFill = true;
    final static boolean shouldWeightX = true;
    final static boolean RIGHT_TO_LEFT = false;


    public static JTextArea chatTextArea;
    public static JButton graphButton;
    public static JTextField productURL;
    public static JTextField portTextField;

    public static JTextField ipTextField;

    public static void addComponentsToPane(Container pane) {
        if (RIGHT_TO_LEFT) {
            pane.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        }

        final JButton clearText;

        final JButton serverButton;
        final JButton summaryButton;

        final JButton productCrawlButton;
        final JButton productPrices;

        pane.setLayout(new GridBagLayout());
        GridBagConstraints grid = new GridBagConstraints();
        if (shouldFill) {
            //natural height, maximum width
            grid.fill = GridBagConstraints.HORIZONTAL;
        }


        summaryButton = new JButton("Summary");
        //serverButton.setEnabled(false);
        grid.fill = GridBagConstraints.HORIZONTAL;
        //grid.weightx = 0.1;
        grid.gridx = 0;
        grid.gridy = 0;
        grid.gridwidth = 1;

        pane.add(summaryButton, grid);


        clearText = new JButton("Clear Text");
        grid.fill = GridBagConstraints.HORIZONTAL;

        grid.weightx = 0.3;
        grid.gridx = 1;
        grid.gridy = 0;
        pane.add(clearText, grid);

        graphButton = new JButton("Crawling Graph");
        grid.fill = GridBagConstraints.HORIZONTAL;
        grid.weightx = 1.0;
        grid.gridx = 2;
        grid.gridy = 0;
        pane.add(graphButton, grid);


        serverButton = new JButton("Run Server");
        //serverButton.setEnabled(false);
        grid.fill = GridBagConstraints.HORIZONTAL;
        //grid.weightx = 0.1;
        grid.gridx = 3;
        grid.gridy = 0;
        grid.gridwidth = 1;

        pane.add(serverButton, grid);


        JLabel ipAddressLabel = new JLabel(" IP: ");
        grid.gridx = 0;
        grid.gridy = 1;
        grid.gridwidth = 1;
        pane.add(ipAddressLabel, grid);

        ipTextField = new JTextField("127.0.0.1");
        grid.gridx = 1;
        grid.gridy = 1;
        grid.gridwidth = 1;
        pane.add(ipTextField, grid);

        JLabel portLabel = new JLabel(" Port: ");
        grid.gridx = 2;
        grid.gridy = 1;
        grid.gridwidth = 1;
        pane.add(portLabel, grid);

        portTextField = new JTextField("5051");
        grid.gridx = 3;
        grid.gridy = 1;
        grid.gridwidth = 1;
        pane.add(portTextField, grid);


        chatTextArea = new JTextArea("");
        chatTextArea.setEnabled(false);
        //chatTextArea.setFocusable(false);
        chatTextArea.setEditable(false);
        chatTextArea.setRows(5);
        grid.fill = GridBagConstraints.HORIZONTAL;
        //grid.gridheight = 3;
        // grid.ipady = 420;
        // grid.weighty =1.0;
        // grid.ipadx = 300;//make this component tall
        grid.weightx = 1.0;
        grid.gridwidth = 4;
        grid.gridx = 0;
        grid.gridy = 2;
        //  pane.add(chatTextArea,grid);

        JScrollPane scrollPane = new JScrollPane(chatTextArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        grid.fill = GridBagConstraints.HORIZONTAL;
        grid.gridx = 0;
        grid.gridy = 2;
        grid.ipady = 420;
        grid.gridheight = 3;
        pane.add(scrollPane, grid);

        productURL = new JTextField("Enter Product URL");
        /*productURL.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
                if(productURL.getText().equals("Start Typing..."))
                    productURL.setText("");
                productURL.addKeyListener(new KeyListener() {
                    public void keyTyped(KeyEvent e) {
                        //does nothing
                    }

                    public void keyPressed(KeyEvent e) {
                        if (e.getKeyChar() == '\n'&&!productURL.getText().equals(""))
                            productCrawlButton.doClick();
                    }

                    public void keyReleased(KeyEvent e) {
                        //does nothing
                    }
                });
            }

            public void focusLost(FocusEvent e) {
                if(productURL.getText().equals(""))
                    productURL.setText("Start Typing...");
            }
        });*/
        grid.fill = GridBagConstraints.HORIZONTAL;
        grid.weighty = 1.0;
        grid.ipady = 13;      //make this component tall
        grid.anchor = GridBagConstraints.PAGE_END; //bottom of space
        grid.weightx = 1.0;
        grid.gridwidth = 2;
        grid.gridx = 0;
        grid.gridy = 3;
        pane.add(productURL, grid);

        //Configuring sendButton properties
        productCrawlButton = new JButton("Crawl Product");
        grid.fill = GridBagConstraints.HORIZONTAL;
        grid.weighty = 1.0;
        grid.ipady = 8;       //reset to default
        grid.anchor = GridBagConstraints.PAGE_END; //bottom of space
        grid.insets = new Insets(0, 0, 0, 0);  //top padding
        grid.gridx = 2;       //aligned with sendButton 3
        grid.gridwidth = 1;   //2 columns wide
        grid.gridy = 3;       //third row
        pane.add(productCrawlButton, grid);
        productCrawlButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!productURL.getText().equals("Start Typing...")) {

                }
            }
        });

        productPrices = new JButton("Product Prices");
        grid.fill = GridBagConstraints.HORIZONTAL;
        grid.weighty = 1.0;
        grid.ipady = 8;       //reset to default
        grid.anchor = GridBagConstraints.PAGE_END; //bottom of space
        grid.insets = new Insets(0, 0, 0, 0);  //top padding
        grid.gridx = 3;       //aligned with sendButton 3
        grid.gridwidth = 1;   //2 columns wide
        grid.gridy = 3;       //third row
        pane.add(productPrices, grid);

        serverButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {


            }
        });


    }

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("MainGUI");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Set up the content pane.
        addComponentsToPane(frame.getContentPane());

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });

    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
    }
}