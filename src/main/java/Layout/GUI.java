package Layout;

import Classes.Product;
import Classes.SearchEngineRepository;
import Classes.Website;
import Main.Main;
import Threads.Pricing;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class GUI {
    public JTextArea txtLog;
    public JPanel _panel;
    public JButton btnStart;
    private JButton summaryButton;
    private JButton clearTextButton;
    private JTabbedPane tabbedPane1;
    private JTextField httpsWwwAmazonComTextField;
    private JButton crawlDailyPriceButton;
    private JComboBox productComboBox;
    private JButton showPricesButton;
    private JButton deleteProductButton;
    private JTextArea priceTextArea;
    private JButton clearTextButton1;
    private JTextField addressTextField;
    private JTextField userNameTextField;
    private JTextField passTextField;
    private JTextField seed1NameTextField;
    private JTextField seed1UrlTextField;
    private JTextField seed2NameTextField;
    private JTextField seed2UrlTextField;
    private JTextField seed3NameTextField;
    private JTextField seed3UrlTextField;
    private JTextField seed4NameTextField;
    private JTextField seed4UrlTextField;
    private JTextField dataBaseNameTextField;
    private JTextField dataLimitTextField;
    private JTextField crawlDepthTextField;
    private JTextField maxNumberOfThreadsTextField;
    private JTextField repetitionTextField;
    private JButton applyRepetitionButton;
    public static SearchEngineRepository ser;
    Pricing productPricingThread;


    public GUI() {
        ser = new SearchEngineRepository(this);
        productPricingThread = new Pricing(this);
        fillComboBox();
        txtLog.setLineWrap(true);
        txtLog.setWrapStyleWord(true);

        btnStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!Main.crawlerISRunning) {
                    new Thread() {
                        public void run() {
                            new Main(GUI.this);
                        }
                    }.start();
                }
                btnStart.setText(Main.crawlerISRunning ? "Start Crawler" : "Stop Crawler");
                Main.crawlerISRunning = !Main.crawlerISRunning;
            }
        });
        clearTextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                txtLog.setText("");
            }
        });
        summaryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        crawlDailyPriceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Thread() {
                    public void run() {
                        productPricingThread.crawl(new Product.Builder().withURL(httpsWwwAmazonComTextField.getText()).build(), GUI.this);
                        fillComboBox();
                        clearProductSearchText();
                    }
                }.start();

            }
        });
        showPricesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                priceTextArea.append(ser.readPrices(productComboBox.getSelectedItem().toString()));
            }
        });
        deleteProductButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                priceTextArea.append(ser.deleteProduct(productComboBox.getSelectedItem().toString()));
                fillComboBox();
            }
        });
        clearTextButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                priceTextArea.setText("");
            }
        });
        summaryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                txtLog.append(ser.getSummary());
                txtLog.append("Total Size of Data Base: " + String.valueOf(ser.getDBSize()) + " KB\n");
            }
        });
        applyRepetitionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                productPricingThread.timerStop();
                productPricingThread = new Pricing(GUI.this);
            }
        });
    }

    public void fillComboBox() {
        List<String> products = ser.readProductsName();
        //productComboBox.setModel(null);
        if (products != null) {
            productComboBox.setModel(new DefaultComboBoxModel(products.toArray()));
        }
        _panel.doLayout();
    }
    public void info(String msg) {
        String timeStamp = new SimpleDateFormat("MM/dd HH:mm:ss").format(Calendar.getInstance().getTime());
        txtLog.append("INFO - " + timeStamp + " - " + msg + "\n");
        _panel.doLayout();
    }

    public void error(String msg) {
        String timeStamp = new SimpleDateFormat("MM/dd HH:mm:ss").format(Calendar.getInstance().getTime());
        txtLog.append("ERROR - " + timeStamp + " - " + msg + "\n");
        _panel.doLayout();
    }

    public void errorProductPricing(String msg) {
        String timeStamp = new SimpleDateFormat("MM/dd HH:mm:ss").format(Calendar.getInstance().getTime());
        priceTextArea.append("ERROR - " + timeStamp + " - " + msg + "\n");
        _panel.doLayout();
    }

    public void clearProductSearchText() {
        httpsWwwAmazonComTextField.setText("");
        priceTextArea.append("Product Added\n");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                //Create and set up the window.
                JFrame frame = new JFrame("GUI");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setContentPane(new GUI()._panel);
                //Display the window.
                frame.pack();
                frame.setVisible(true);
                frame.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent e) {
                        ser.closeDB();
                    }
                });
            }
        });

    }

    public String getDBAddress() {
        return addressTextField.getText();
    }

    public String getDBName() {
        return dataBaseNameTextField.getText();
    }

    public String getDBUser() {
        return userNameTextField.getText();
    }

    public String getDBPass() {
        return passTextField.getText();
    }

    public ArrayList<Website> getSeeds() {
        ArrayList<Website> seeds = new ArrayList<>();
        if (!seed1NameTextField.getText().matches("") && !seed1UrlTextField.getText().matches("")) {
            seeds.add(new Website.Builder().withSiteName(seed1NameTextField.getText())
                    .withURL(seed1UrlTextField.getText()).withDepth(0).build());
        }
        if (!seed2NameTextField.getText().matches("") && !seed2UrlTextField.getText().matches("")) {
            seeds.add(new Website.Builder().withSiteName(seed2NameTextField.getText())
                    .withURL(seed2UrlTextField.getText()).withDepth(0).build());
        }
        if (!seed3NameTextField.getText().matches("") && !seed3UrlTextField.getText().matches("")) {
            seeds.add(new Website.Builder().withSiteName(seed3NameTextField.getText())
                    .withURL(seed3UrlTextField.getText()).withDepth(0).build());
        }
        if (!seed4NameTextField.getText().matches("") && !seed4UrlTextField.getText().matches("")) {
            seeds.add(new Website.Builder().withSiteName(seed4NameTextField.getText())
                    .withURL(seed4UrlTextField.getText()).withDepth(0).build());
        }
        return seeds;
    }

    public int getMaxSize() {
        return Integer.parseInt(dataLimitTextField.getText());
    }

    public int getMaxDepth() {
        return Integer.parseInt(crawlDepthTextField.getText());
    }

    public int getMaxNumOfThreads() {
        return Integer.parseInt(maxNumberOfThreadsTextField.getText());
    }

    public long getRepetitionCycle() {
        long a = 60 * 60 * 24;
        if (repetitionTextField.getText().length() > 0) {
            try {
                a = Long.parseLong(repetitionTextField.getText());
            } catch (Exception e) {
                errorProductPricing(e.getMessage());
            }
        }
        return a;
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
        _panel = new JPanel();
        _panel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        _panel.setMinimumSize(new Dimension(800, 600));
        _panel.setPreferredSize(new Dimension(800, 600));
        tabbedPane1 = new JTabbedPane();
        _panel.add(tabbedPane1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 200), null, 0, false));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(2, 5, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane1.addTab("Crawler", panel1);
        btnStart = new JButton();
        btnStart.setText("Start Crawler");
        panel1.add(btnStart, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(75, 30), null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        panel1.add(scrollPane1, new GridConstraints(1, 0, 1, 5, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        txtLog = new JTextArea();
        scrollPane1.setViewportView(txtLog);
        summaryButton = new JButton();
        summaryButton.setText("Summary");
        panel1.add(summaryButton, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        clearTextButton = new JButton();
        clearTextButton.setText("Clear Text");
        panel1.add(clearTextButton, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(3, 7, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane1.addTab("Product Pricing", panel2);
        httpsWwwAmazonComTextField = new JTextField();
        httpsWwwAmazonComTextField.setText("");
        panel2.add(httpsWwwAmazonComTextField, new GridConstraints(0, 1, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("Enter Product URL: ");
        panel2.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        crawlDailyPriceButton = new JButton();
        crawlDailyPriceButton.setText("Crawl Product");
        panel2.add(crawlDailyPriceButton, new GridConstraints(0, 3, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        productComboBox = new JComboBox();
        panel2.add(productComboBox, new GridConstraints(1, 1, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Select Product:");
        panel2.add(label2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        showPricesButton = new JButton();
        showPricesButton.setText("Show Prices");
        panel2.add(showPricesButton, new GridConstraints(1, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        deleteProductButton = new JButton();
        deleteProductButton.setText("Delete Product");
        panel2.add(deleteProductButton, new GridConstraints(1, 4, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JScrollPane scrollPane2 = new JScrollPane();
        panel2.add(scrollPane2, new GridConstraints(2, 0, 1, 7, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        priceTextArea = new JTextArea();
        scrollPane2.setViewportView(priceTextArea);
        clearTextButton1 = new JButton();
        clearTextButton1.setText("Clear Text");
        panel2.add(clearTextButton1, new GridConstraints(0, 5, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(15, 7, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane1.addTab("Setting", panel3);
        final JLabel label3 = new JLabel();
        label3.setText("Data Base Settings:");
        panel3.add(label3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel3.add(spacer1, new GridConstraints(0, 6, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        panel3.add(spacer2, new GridConstraints(14, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setText("Address: ");
        panel3.add(label4, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label5 = new JLabel();
        label5.setText("Username: ");
        panel3.add(label5, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label6 = new JLabel();
        label6.setText("Password: ");
        panel3.add(label6, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        addressTextField = new JTextField();
        addressTextField.setText("jdbc:mysql://localhost/");
        panel3.add(addressTextField, new GridConstraints(1, 1, 1, 6, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        userNameTextField = new JTextField();
        userNameTextField.setText("root");
        panel3.add(userNameTextField, new GridConstraints(3, 1, 1, 6, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        passTextField = new JTextField();
        passTextField.setText("1111");
        panel3.add(passTextField, new GridConstraints(4, 1, 1, 6, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label7 = new JLabel();
        label7.setText("Crawling Settings:");
        panel3.add(label7, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label8 = new JLabel();
        label8.setText("Seed 1:");
        panel3.add(label8, new GridConstraints(6, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label9 = new JLabel();
        label9.setText("Site Name:");
        panel3.add(label9, new GridConstraints(6, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        seed1NameTextField = new JTextField();
        seed1NameTextField.setText("CalBaptist");
        panel3.add(seed1NameTextField, new GridConstraints(6, 2, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label10 = new JLabel();
        label10.setText("URL: ");
        panel3.add(label10, new GridConstraints(6, 4, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        seed1UrlTextField = new JTextField();
        seed1UrlTextField.setText("https://calbaptist.edu/");
        panel3.add(seed1UrlTextField, new GridConstraints(6, 5, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label11 = new JLabel();
        label11.setText("Seed 2:");
        panel3.add(label11, new GridConstraints(7, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label12 = new JLabel();
        label12.setText("Site Name: ");
        panel3.add(label12, new GridConstraints(7, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        seed2NameTextField = new JTextField();
        seed2NameTextField.setText("CNN");
        panel3.add(seed2NameTextField, new GridConstraints(7, 2, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label13 = new JLabel();
        label13.setText("URL: ");
        panel3.add(label13, new GridConstraints(7, 4, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        seed2UrlTextField = new JTextField();
        seed2UrlTextField.setText("https://www.cnn.com/");
        panel3.add(seed2UrlTextField, new GridConstraints(7, 5, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label14 = new JLabel();
        label14.setText("Seed 3:");
        panel3.add(label14, new GridConstraints(8, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label15 = new JLabel();
        label15.setText("Site Name: ");
        panel3.add(label15, new GridConstraints(8, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        seed3NameTextField = new JTextField();
        seed3NameTextField.setText("Wiki");
        panel3.add(seed3NameTextField, new GridConstraints(8, 2, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label16 = new JLabel();
        label16.setText("URL: ");
        panel3.add(label16, new GridConstraints(8, 4, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        seed3UrlTextField = new JTextField();
        seed3UrlTextField.setText("https://www.wikipedia.org/");
        panel3.add(seed3UrlTextField, new GridConstraints(8, 5, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label17 = new JLabel();
        label17.setText("Seed 4: ");
        panel3.add(label17, new GridConstraints(9, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label18 = new JLabel();
        label18.setText("Site Name: ");
        panel3.add(label18, new GridConstraints(9, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        seed4NameTextField = new JTextField();
        seed4NameTextField.setText("WhiteHouse");
        panel3.add(seed4NameTextField, new GridConstraints(9, 2, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label19 = new JLabel();
        label19.setText("URL: ");
        panel3.add(label19, new GridConstraints(9, 4, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        seed4UrlTextField = new JTextField();
        seed4UrlTextField.setText("https://www.whitehouse.gov/");
        panel3.add(seed4UrlTextField, new GridConstraints(9, 5, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label20 = new JLabel();
        label20.setText("Data Base Name:");
        panel3.add(label20, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        dataBaseNameTextField = new JTextField();
        dataBaseNameTextField.setText("SearchEngineDB");
        panel3.add(dataBaseNameTextField, new GridConstraints(2, 1, 1, 6, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label21 = new JLabel();
        label21.setText("Maximum Crawl Depth:");
        panel3.add(label21, new GridConstraints(10, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        crawlDepthTextField = new JTextField();
        crawlDepthTextField.setText("1");
        panel3.add(crawlDepthTextField, new GridConstraints(10, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label22 = new JLabel();
        label22.setText("Data Size Limit: ");
        panel3.add(label22, new GridConstraints(10, 2, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        dataLimitTextField = new JTextField();
        dataLimitTextField.setText("3145728");
        panel3.add(dataLimitTextField, new GridConstraints(10, 4, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label23 = new JLabel();
        label23.setText("KB");
        panel3.add(label23, new GridConstraints(10, 6, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label24 = new JLabel();
        label24.setText("Number Of Threads For Crawling:");
        panel3.add(label24, new GridConstraints(11, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        maxNumberOfThreadsTextField = new JTextField();
        maxNumberOfThreadsTextField.setText("10");
        panel3.add(maxNumberOfThreadsTextField, new GridConstraints(11, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label25 = new JLabel();
        label25.setText("Product Pricing Settings:");
        panel3.add(label25, new GridConstraints(12, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label26 = new JLabel();
        label26.setText("Schedule Repetition:");
        panel3.add(label26, new GridConstraints(13, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        repetitionTextField = new JTextField();
        repetitionTextField.setText("86400");
        panel3.add(repetitionTextField, new GridConstraints(13, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label27 = new JLabel();
        label27.setText("Seconds");
        panel3.add(label27, new GridConstraints(13, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        applyRepetitionButton = new JButton();
        applyRepetitionButton.setText("Apply");
        panel3.add(applyRepetitionButton, new GridConstraints(13, 3, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return _panel;
    }
}
