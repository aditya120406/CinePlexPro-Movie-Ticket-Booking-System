package moviebooking;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

public class ConfirmationScreen extends JFrame {

    private final String username, city, movieTitle, language, paymentMethod, upiId;
    private final List<String> seats;
    private final int totalAmount;
    private final String bookingId;
    private final String bookingTime;

    public ConfirmationScreen(String username, String city, String movieTitle, String language,
                              List<String> seats, int totalAmount, String paymentMethod, String upiId) {
        this.username = username;
        this.city = city;
        this.movieTitle = movieTitle;
        this.language = language;
        this.seats = seats;
        this.totalAmount = totalAmount;
        this.paymentMethod = paymentMethod;
        this.upiId = upiId;
        this.bookingId = generateBookingId();
        this.bookingTime = new SimpleDateFormat("dd-MMM-yyyy HH:mm").format(new Date());

        saveBookingToFile();

        setTitle("CinePlex Pro – Booking Confirmed!");
        setSize(700, 660);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        initComponents();
    }

    private String generateBookingId() {
        return "CPX" + (new Random().nextInt(900000) + 100000);
    }

    private void saveBookingToFile() {
        try (FileWriter fw = new FileWriter("bookings.txt", true);
             BufferedWriter bw = new BufferedWriter(fw)) {
            bw.write("==========================================");
            bw.newLine();
            bw.write("BOOKING ID   : " + bookingId);
            bw.newLine();
            bw.write("DATE & TIME  : " + bookingTime);
            bw.newLine();
            bw.write("CUSTOMER     : " + username);
            bw.newLine();
            bw.write("MOVIE        : " + movieTitle);
            bw.newLine();
            bw.write("CITY         : " + city);
            bw.newLine();
            bw.write("LANGUAGE     : " + language);
            bw.newLine();
            bw.write("SEATS        : " + String.join(", ", seats));
            bw.newLine();
            bw.write("AMOUNT PAID  : ₹" + totalAmount);
            bw.newLine();
            bw.write("PAYMENT VIA  : " + paymentMethod + " (" + upiId + ")");
            bw.newLine();
            bw.write("==========================================");
            bw.newLine();
            bw.newLine();
        } catch (IOException e) {
            System.err.println("Could not save booking: " + e.getMessage());
        }
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setColor(new Color(8, 8, 18));
                g2.fillRect(0, 0, getWidth(), getHeight());
                // Confetti particles (static)
                Random rnd = new Random(123);
                Color[] confettiColors = {
                        new Color(220, 50, 50, 180), new Color(50, 200, 100, 180),
                        new Color(255, 200, 50, 180), new Color(100, 150, 255, 180)
                };
                for (int i = 0; i < 60; i++) {
                    int x = rnd.nextInt(700), y = rnd.nextInt(120);
                    int w = rnd.nextInt(8) + 3, h = rnd.nextInt(5) + 2;
                    g2.setColor(confettiColors[rnd.nextInt(confettiColors.length)]);
                    g2.fillRoundRect(x, y, w, h, 2, 2);
                }
            }
        };
        mainPanel.setLayout(null);

        // Success banner
        JPanel banner = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, new Color(25, 120, 60),
                        getWidth(), 0, new Color(15, 80, 45));
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 0, 0);
            }
        };
        banner.setLayout(null);
        banner.setBounds(0, 0, 700, 80);

        JLabel checkIcon = new JLabel("✅");
        checkIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 30));
        checkIcon.setBounds(20, 22, 45, 40);
        banner.add(checkIcon);

        JLabel successTitle = new JLabel("BOOKING CONFIRMED!");
        successTitle.setFont(new Font("Georgia", Font.BOLD, 22));
        successTitle.setForeground(Color.WHITE);
        successTitle.setBounds(70, 14, 360, 32);
        banner.add(successTitle);

        JLabel successSub = new JLabel("Your tickets have been booked successfully");
        successSub.setFont(new Font("Arial", Font.PLAIN, 13));
        successSub.setForeground(new Color(160, 240, 180));
        successSub.setBounds(70, 46, 370, 22);
        banner.add(successSub);

        JLabel bookingIdLbl = new JLabel("ID: " + bookingId);
        bookingIdLbl.setFont(new Font("Arial", Font.BOLD, 14));
        bookingIdLbl.setForeground(new Color(255, 220, 80));
        bookingIdLbl.setBounds(510, 28, 170, 25);
        banner.add(bookingIdLbl);

        mainPanel.add(banner);

        // ── TICKET CARD ──
        JPanel ticket = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Ticket background
                g2.setColor(new Color(22, 20, 38));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 18, 18);
                g2.setColor(new Color(80, 70, 120, 120));
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 18, 18);

                // Perforation line
                g2.setColor(new Color(40, 38, 60));
                g2.setStroke(new BasicStroke(1.5f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0,
                        new float[]{8, 8}, 0));
                g2.drawLine(0, 185, getWidth(), 185);

                // Notch circles on perforation
                g2.setColor(new Color(8, 8, 18));
                g2.fillOval(-14, 178, 28, 14);
                g2.fillOval(getWidth() - 14, 178, 28, 14);

                // Left accent stripe
                GradientPaint stripe = new GradientPaint(0, 0, new Color(220, 50, 50),
                        0, getHeight(), new Color(100, 30, 120));
                g2.setPaint(stripe);
                g2.fillRoundRect(0, 0, 8, getHeight(), 4, 4);
            }
        };
        ticket.setLayout(null);
        ticket.setBounds(40, 100, 620, 390);
        ticket.setOpaque(false);
        mainPanel.add(ticket);

        // Top part of ticket
        JLabel movieEmoji = new JLabel("🎬");
        movieEmoji.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 36));
        movieEmoji.setBounds(20, 20, 50, 45);
        ticket.add(movieEmoji);

        JLabel movieNameLbl = new JLabel(movieTitle);
        movieNameLbl.setFont(new Font("Georgia", Font.BOLD, 20));
        movieNameLbl.setForeground(Color.WHITE);
        movieNameLbl.setBounds(75, 20, 500, 30);
        ticket.add(movieNameLbl);

        JLabel langLbl = new JLabel(language + "  |  " + city);
        langLbl.setFont(new Font("Arial", Font.PLAIN, 13));
        langLbl.setForeground(new Color(150, 140, 190));
        langLbl.setBounds(75, 52, 500, 20);
        ticket.add(langLbl);

        // Divider
        JSeparator sep1 = new JSeparator();
        sep1.setBounds(20, 85, 580, 1);
        sep1.setForeground(new Color(60, 55, 90));
        ticket.add(sep1);

        // Ticket details grid
        String[][] details = {
                {"👤 Customer", username, "🎟️ Booking ID", bookingId},
                {"💺 Seats", String.join(", ", seats), "📅 Date", bookingTime},
                {"💳 Via", paymentMethod, "💰 Amount", "₹" + totalAmount}
        };

        for (int row = 0; row < details.length; row++) {
            for (int col = 0; col < 2; col++) {
                int xOff = 20 + col * 295;
                int yOff = 100 + row * 55;

                JLabel keyL = new JLabel(details[row][col * 2]);
                keyL.setFont(new Font("Arial", Font.PLAIN, 10));
                keyL.setForeground(new Color(120, 110, 160));
                keyL.setBounds(xOff, yOff, 270, 16);
                ticket.add(keyL);

                JLabel valL = new JLabel(details[row][col * 2 + 1]);
                valL.setFont(new Font("Arial", Font.BOLD, 13));
                valL.setForeground(Color.WHITE);
                valL.setBounds(xOff, yOff + 18, 270, 20);
                ticket.add(valL);
            }
        }

        // Bottom part of ticket (barcode area)
        JLabel barLabel = new JLabel("SCAN QR OR SHOW BOOKING ID AT COUNTER");
        barLabel.setFont(new Font("Arial", Font.BOLD, 10));
        barLabel.setForeground(new Color(120, 110, 160));
        barLabel.setHorizontalAlignment(SwingConstants.CENTER);
        barLabel.setBounds(20, 200, 580, 18);
        ticket.add(barLabel);

        // Simple barcode art
        JPanel barcode = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setColor(new Color(28, 25, 45));
                g2.fillRect(0, 0, getWidth(), getHeight());
                Random rnd = new Random(bookingId.hashCode());
                int x = 5;
                while (x < getWidth() - 5) {
                    int barW = rnd.nextInt(4) + 1;
                    if (rnd.nextBoolean()) {
                        g2.setColor(new Color(220, 200, 255));
                        g2.fillRect(x, 5, barW, getHeight() - 10);
                    }
                    x += barW + rnd.nextInt(3) + 1;
                }
                g2.setColor(new Color(60, 55, 90));
                g2.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
            }
        };
        barcode.setBounds(120, 225, 380, 45);
        barcode.setOpaque(false);
        ticket.add(barcode);

        JLabel barcodeId = new JLabel(bookingId);
        barcodeId.setFont(new Font("Courier New", Font.BOLD, 12));
        barcodeId.setForeground(new Color(180, 170, 220));
        barcodeId.setHorizontalAlignment(SwingConstants.CENTER);
        barcodeId.setBounds(120, 272, 380, 18);
        ticket.add(barcodeId);

        // ── Buttons ──
        JButton newBookingBtn = createActionButton("🎬  Book Another Movie", new Color(220, 50, 50), new Color(160, 30, 30));
        newBookingBtn.setBounds(40, 510, 290, 45);
        newBookingBtn.addActionListener(e -> { dispose(); new CitySelectionScreen(username).setVisible(true); });
        mainPanel.add(newBookingBtn);

        JButton viewFileBtn = createActionButton("📄  View Bookings File", new Color(60, 100, 180), new Color(40, 70, 140));
        viewFileBtn.setBounds(345, 510, 290, 45);
        viewFileBtn.addActionListener(e -> openBookingsFile());
        mainPanel.add(viewFileBtn);

        JButton exitBtn = createActionButton("🚪  Exit Application", new Color(50, 45, 70), new Color(35, 30, 55));
        exitBtn.setBounds(195, 565, 300, 38);
        exitBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Thank you for using CinePlex Pro!\nEnjoy your movie! 🎬",
                    "Goodbye!", JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
        });
        mainPanel.add(exitBtn);

        // Saved note
        JLabel savedNote = new JLabel("📁 Booking saved to bookings.txt");
        savedNote.setFont(new Font("Arial", Font.ITALIC, 11));
        savedNote.setForeground(new Color(80, 150, 100));
        savedNote.setHorizontalAlignment(SwingConstants.CENTER);
        savedNote.setBounds(0, 612, 700, 18);
        mainPanel.add(savedNote);

        setContentPane(mainPanel);
    }

    private JButton createActionButton(String text, Color c1, Color c2) {
        JButton btn = new JButton(text) {
            private boolean hov = false;
            {
                addMouseListener(new MouseAdapter() {
                    @Override public void mouseEntered(MouseEvent e) { hov = true; repaint(); }
                    @Override public void mouseExited(MouseEvent e) { hov = false; repaint(); }
                });
            }
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color fc1 = hov ? c1.brighter() : c1;
                Color fc2 = hov ? c2.brighter() : c2;
                GradientPaint gp = new GradientPaint(0, 0, fc1, getWidth(), 0, fc2);
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Arial", Font.BOLD, 13));
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(), (getWidth() - fm.stringWidth(getText())) / 2,
                        (getHeight() + fm.getAscent()) / 2 - 4);
            }
        };
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void openBookingsFile() {
        File f = new File("bookings.txt");
        if (!f.exists()) {
            JOptionPane.showMessageDialog(this, "No bookings file found.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        try {
            Desktop.getDesktop().open(f);
        } catch (Exception ex) {
            // Fallback: show in dialog
            try (BufferedReader br = new BufferedReader(new FileReader(f))) {
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) sb.append(line).append("\n");
                JTextArea ta = new JTextArea(sb.toString());
                ta.setFont(new Font("Courier New", Font.PLAIN, 12));
                ta.setEditable(false);
                ta.setBackground(new Color(18, 15, 30));
                ta.setForeground(Color.WHITE);
                JScrollPane sp = new JScrollPane(ta);
                sp.setPreferredSize(new Dimension(500, 400));
                JOptionPane.showMessageDialog(this, sp, "Bookings File", JOptionPane.PLAIN_MESSAGE);
            } catch (Exception e2) {
                JOptionPane.showMessageDialog(this, "Could not open file.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
