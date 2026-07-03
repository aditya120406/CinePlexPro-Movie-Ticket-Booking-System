package moviebooking;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.util.List;

public class AdminDashboard extends JFrame {

    private final String username;
    private JTable bookingTable;
    private DefaultTableModel tableModel;
    private JLabel totalRevLbl, totalTicketsLbl, totalBookingsLbl;

    public AdminDashboard(String username) {
        this.username = username;
        setTitle("CinePlex Pro – Admin Dashboard");
        setSize(1000, 680);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        initComponents();
        loadBookings();
    }

    private void initComponents() {
        JPanel main = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setColor(new Color(8, 8, 18));
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        main.setLayout(null);

        // Header
        JPanel hdr = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0,0,new Color(30,10,60),getWidth(),0,new Color(100,20,20));
                g2.setPaint(gp); g2.fillRect(0,0,getWidth(),getHeight());
            }
        };
        hdr.setLayout(null); hdr.setBounds(0,0,1000,65);

        JLabel logo = new JLabel("🎬 CinePlex Pro  –  Admin Dashboard");
        logo.setFont(new Font("Georgia", Font.BOLD, 20));
        logo.setForeground(new Color(255,200,80));
        logo.setBounds(20,17,450,32); hdr.add(logo);

        JLabel userLbl = new JLabel("👤 " + username + "  |  🔐 Admin Mode");
        userLbl.setFont(new Font("Arial", Font.PLAIN, 13));
        userLbl.setForeground(new Color(200,180,220));
        userLbl.setBounds(680,20,300,25); hdr.add(userLbl);
        main.add(hdr);

        // ── Stat Cards ──
        String[][] stats = {
                {"💰", "Total Revenue", "₹0", "#27AE60"},
                {"🎟️", "Tickets Sold", "0", "#2980B9"},
                {"📋", "Bookings Made", "0", "#8E44AD"},
        };

        JLabel[] statValueLabels = new JLabel[3];
        for (int i = 0; i < stats.length; i++) {
            final int idx = i;
            JPanel card = new JPanel() {
                @Override protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g;
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(new Color(20,18,35));
                    g2.fillRoundRect(0,0,getWidth(),getHeight(),14,14);
                    try {
                        Color ac = Color.decode(stats[idx][3]);
                        g2.setColor(new Color(ac.getRed(),ac.getGreen(),ac.getBlue(),40));
                        g2.fillRoundRect(0,0,8,getHeight(),4,4);
                        g2.setColor(ac);
                        g2.fillRoundRect(0,0,8,getHeight(),4,4);
                    } catch(Exception ignored) {}
                    g2.setColor(new Color(50,45,80,100));
                    g2.setStroke(new BasicStroke(1f));
                    g2.drawRoundRect(0,0,getWidth()-1,getHeight()-1,14,14);
                }
            };
            card.setLayout(null);
            card.setBounds(25 + i * 320, 80, 300, 100);
            card.setOpaque(false);

            JLabel emoji = new JLabel(stats[i][0]);
            emoji.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));
            emoji.setBounds(15,15,45,45); card.add(emoji);

            JLabel name = new JLabel(stats[i][1]);
            name.setFont(new Font("Arial", Font.PLAIN, 12));
            name.setForeground(new Color(140,130,175));
            name.setBounds(65,14,220,20); card.add(name);

            statValueLabels[i] = new JLabel("Loading...");
            statValueLabels[i].setFont(new Font("Georgia", Font.BOLD, 22));
            statValueLabels[i].setForeground(Color.WHITE);
            statValueLabels[i].setBounds(65,36,220,32);
            card.add(statValueLabels[i]);

            main.add(card);
        }
        totalRevLbl = statValueLabels[0];
        totalTicketsLbl = statValueLabels[1];
        totalBookingsLbl = statValueLabels[2];

        // ── Bookings Table ──
        JLabel tableTitle = new JLabel("All Bookings");
        tableTitle.setFont(new Font("Georgia", Font.BOLD, 16));
        tableTitle.setForeground(new Color(255,200,80));
        tableTitle.setBounds(25, 200, 300, 28);
        main.add(tableTitle);

        JButton refreshBtn = buildSmallBtn("🔄 Refresh", new Color(40,80,160));
        refreshBtn.setBounds(820, 200, 110, 28);
        refreshBtn.addActionListener(e -> loadBookings());
        main.add(refreshBtn);

        JButton clearBtn = buildSmallBtn("🗑️ Clear All", new Color(140,30,30));
        clearBtn.setBounds(720, 200, 92, 28);
        clearBtn.addActionListener(e -> clearBookings());
        main.add(clearBtn);

        String[] cols = {"Booking ID", "Customer", "Movie", "City", "Seats", "Amount", "Payment", "Date & Time"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        bookingTable = new JTable(tableModel) {
            @Override public Component prepareRenderer(TableCellRenderer r, int row, int col) {
                Component c = super.prepareRenderer(r, row, col);
                c.setBackground(row % 2 == 0 ? new Color(20,18,35) : new Color(25,22,42));
                c.setForeground(new Color(210,200,235));
                if (isRowSelected(row)) {
                    c.setBackground(new Color(80,40,120));
                    c.setForeground(Color.WHITE);
                }
                return c;
            }
        };
        bookingTable.setFont(new Font("Arial", Font.PLAIN, 12));
        bookingTable.setRowHeight(26);
        bookingTable.setShowGrid(false);
        bookingTable.setIntercellSpacing(new Dimension(0, 1));
        bookingTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        bookingTable.getTableHeader().setBackground(new Color(35,15,60));
        bookingTable.getTableHeader().setForeground(new Color(255,200,80));
        bookingTable.getTableHeader().setBorder(BorderFactory.createEmptyBorder());

        // Column widths
        int[] colWidths = {90, 80, 140, 80, 120, 70, 90, 130};
        for (int i = 0; i < colWidths.length; i++)
            bookingTable.getColumnModel().getColumn(i).setPreferredWidth(colWidths[i]);

        JScrollPane scroll = new JScrollPane(bookingTable);
        scroll.setBounds(25, 235, 950, 360);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(50,45,80)));
        scroll.getViewport().setBackground(new Color(18,16,30));
        scroll.setBackground(new Color(18,16,30));
        main.add(scroll);

        // Bottom
        JButton backBtn = buildSmallBtn("← Back to App", new Color(50,45,80));
        backBtn.setBounds(25, 610, 140, 32);
        backBtn.addActionListener(e -> { dispose(); new CitySelectionScreen(username).setVisible(true); });
        main.add(backBtn);

        JLabel fileNote = new JLabel("📁  Data source: bookings.txt  (same directory as JAR)");
        fileNote.setFont(new Font("Arial", Font.ITALIC, 11));
        fileNote.setForeground(new Color(80,75,110));
        fileNote.setBounds(200, 617, 600, 18);
        main.add(fileNote);

        setContentPane(main);
    }

    private JButton buildSmallBtn(String text, Color bgColor) {
        JButton b = new JButton(text) {
            private boolean hov = false;
            { addMouseListener(new MouseAdapter() {
                @Override public void mouseEntered(MouseEvent e) { hov=true; repaint(); }
                @Override public void mouseExited(MouseEvent e) { hov=false; repaint(); }
            }); }
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(hov ? bgColor.brighter() : bgColor);
                g2.fillRoundRect(0,0,getWidth(),getHeight(),8,8);
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Arial",Font.BOLD,11));
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(),(getWidth()-fm.stringWidth(getText()))/2,(getHeight()+fm.getAscent())/2-3);
            }
        };
        b.setOpaque(false); b.setContentAreaFilled(false); b.setBorderPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }

    private void loadBookings() {
        tableModel.setRowCount(0);
        File f = new File("bookings.txt");
        if (!f.exists()) {
            totalRevLbl.setText("₹0");
            totalTicketsLbl.setText("0");
            totalBookingsLbl.setText("0");
            return;
        }

        List<Map<String,String>> records = parseBookingsFile(f);
        int totalRev = 0, totalTickets = 0;

        for (Map<String,String> r : records) {
            String seats = r.getOrDefault("SEATS","");
            int seatCount = seats.isEmpty() ? 0 : seats.split(",").length;
            totalTickets += seatCount;
            try {
                String amtStr = r.getOrDefault("AMOUNT PAID","₹0").replace("₹","").trim();
                totalRev += Integer.parseInt(amtStr);
            } catch (Exception ignored) {}

            tableModel.addRow(new Object[]{
                    r.getOrDefault("BOOKING ID",""),
                    r.getOrDefault("CUSTOMER",""),
                    r.getOrDefault("MOVIE",""),
                    r.getOrDefault("CITY",""),
                    seats,
                    r.getOrDefault("AMOUNT PAID",""),
                    r.getOrDefault("PAYMENT VIA","").split("\\(")[0].trim(),
                    r.getOrDefault("DATE & TIME","")
            });
        }

        totalRevLbl.setText("₹" + totalRev);
        totalTicketsLbl.setText(String.valueOf(totalTickets));
        totalBookingsLbl.setText(String.valueOf(records.size()));
    }

    private List<Map<String,String>> parseBookingsFile(File f) {
        List<Map<String,String>> records = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            Map<String,String> current = null;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("====")) {
                    if (current != null && !current.isEmpty()) records.add(current);
                    current = new LinkedHashMap<>();
                } else if (current != null && line.contains(":")) {
                    int colon = line.indexOf(':');
                    String key = line.substring(0, colon).trim();
                    String val = line.substring(colon + 1).trim();
                    current.put(key, val);
                }
            }
            if (current != null && !current.isEmpty()) records.add(current);
        } catch (IOException ignored) {}
        return records;
    }

    private void clearBookings() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "This will permanently delete all booking records.\nAre you sure?",
                "Clear All Bookings", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirm == JOptionPane.YES_OPTION) {
            try (FileWriter fw = new FileWriter("bookings.txt", false)) {
                fw.write(""); // clear
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Failed to clear file.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            loadBookings();
            JOptionPane.showMessageDialog(this, "All bookings cleared.", "Done", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // Entry point for testing admin panel standalone
    public static void showAdmin(String username) {
        SwingUtilities.invokeLater(() -> new AdminDashboard(username).setVisible(true));
    }
}
