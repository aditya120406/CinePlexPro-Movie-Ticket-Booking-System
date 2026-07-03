package moviebooking;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class PaymentScreen extends JFrame {

    private final String username, city, movieTitle, language;
    private final List<String> seats;
    private final int totalAmount;

    private JTextField upiField;
    private JLabel statusLabel;
    private String selectedMethod = "PhonePe";
    private JPanel[] methodCards;

    private static final String[][] UPI_METHODS = {
            {"PhonePe",    "#5F259F", "📱"},
            {"Google Pay", "#1A73E8", "💳"},
            {"FamPay",     "#E8A000", "👨‍👩‍👧"}
    };

    public PaymentScreen(String username, String city, String movieTitle,
                         String language, List<String> seats, int totalAmount) {
        this.username    = username;
        this.city        = city;
        this.movieTitle  = movieTitle;
        this.language    = language;
        this.seats       = seats;
        this.totalAmount = totalAmount;

        setTitle("CinePlex Pro – Payment");
        setSize(820, 640);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        initComponents();
    }

    private void initComponents() {

        // Root panel – solid dark background
        JPanel root = new JPanel(null);
        root.setBackground(new Color(10, 10, 22));

        // ── Header ───────────────────────────────────────────────────────────
        JPanel header = new JPanel(null) {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setPaint(new GradientPaint(0, 0, new Color(140, 20, 20),
                        getWidth(), 0, new Color(60, 10, 80)));
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        header.setOpaque(false);
        header.setBounds(0, 0, 820, 62);

        JLabel hTitle = new JLabel("🔒  Secure Payment");
        hTitle.setFont(new Font("Georgia", Font.BOLD, 19));
        hTitle.setForeground(new Color(255, 200, 80));
        hTitle.setBounds(20, 15, 280, 32);
        header.add(hTitle);

        JLabel hSsl = new JLabel("🛡️  SSL Encrypted  |  256-bit Security");
        hSsl.setFont(new Font("Arial", Font.PLAIN, 12));
        hSsl.setForeground(new Color(100, 220, 130));
        hSsl.setBounds(490, 20, 320, 22);
        header.add(hSsl);
        root.add(header);

        // ── LEFT card – Order Summary ─────────────────────────────────────────
        JPanel leftCard = roundedCard(new Color(22, 20, 38), new Color(55, 50, 85));
        leftCard.setBounds(18, 75, 360, 490);
        root.add(leftCard);

        lbl(leftCard, "ORDER SUMMARY",     18, 16, 320, 16, new Font("Arial", Font.BOLD, 10), new Color(220, 50, 50));
        lbl(leftCard, "Movie",             18, 44, 320, 13, new Font("Arial", Font.PLAIN, 10), new Color(130, 120, 170));
        lbl(leftCard, movieTitle,          18, 58, 320, 20, new Font("Arial", Font.BOLD, 14), Color.WHITE);
        lbl(leftCard, "City  /  Language", 18, 90, 320, 13, new Font("Arial", Font.PLAIN, 10), new Color(130, 120, 170));
        lbl(leftCard, city + "  •  " + language, 18, 104, 320, 18, new Font("Arial", Font.BOLD, 13), Color.WHITE);
        lbl(leftCard, "Seats",             18, 136, 320, 13, new Font("Arial", Font.PLAIN, 10), new Color(130, 120, 170));
        lbl(leftCard, "<html>" + String.join(", ", seats) + "</html>",
                                           18, 150, 320, 34, new Font("Arial", Font.BOLD, 13), Color.WHITE);
        lbl(leftCard, "Booked by",         18, 196, 320, 13, new Font("Arial", Font.PLAIN, 10), new Color(130, 120, 170));
        lbl(leftCard, username,            18, 210, 320, 18, new Font("Arial", Font.BOLD, 13), Color.WHITE);

        JSeparator sep1 = new JSeparator();
        sep1.setBounds(18, 248, 320, 1);
        sep1.setForeground(new Color(55, 50, 85));
        leftCard.add(sep1);

        int base = totalAmount - 30;
        lbl(leftCard, "Ticket price",    18, 262, 200, 16, new Font("Arial", Font.PLAIN, 11), new Color(140, 130, 175));
        lbl(leftCard, "₹" + base,       260, 262, 80,  16, new Font("Arial", Font.BOLD, 12),  Color.WHITE);
        lbl(leftCard, "Convenience fee", 18, 284, 200, 16, new Font("Arial", Font.PLAIN, 11), new Color(140, 130, 175));
        lbl(leftCard, "₹30",            260, 284, 80,  16, new Font("Arial", Font.BOLD, 12),  Color.WHITE);

        JSeparator sep2 = new JSeparator();
        sep2.setBounds(18, 308, 320, 1);
        sep2.setForeground(new Color(55, 50, 85));
        leftCard.add(sep2);

        lbl(leftCard, "TOTAL",     18, 320, 160, 28, new Font("Arial",   Font.BOLD, 11), new Color(140, 130, 175));
        lbl(leftCard, "₹" + totalAmount, 180, 312, 160, 34, new Font("Georgia", Font.BOLD, 24), new Color(60, 220, 120));

        // ── RIGHT card – UPI Payment ──────────────────────────────────────────
        JPanel rightCard = roundedCard(new Color(22, 20, 38), new Color(55, 50, 85));
        rightCard.setBounds(392, 75, 412, 490);
        root.add(rightCard);

        lbl(rightCard, "PAY VIA UPI",           18, 16, 370, 16, new Font("Arial", Font.BOLD, 10),  new Color(220, 50, 50));
        lbl(rightCard, "Select payment method", 18, 34, 370, 15, new Font("Arial", Font.PLAIN, 11), new Color(130, 120, 170));

        // UPI method selector cards
        methodCards = new JPanel[UPI_METHODS.length];
        for (int i = 0; i < UPI_METHODS.length; i++) {
            final int idx = i;
            methodCards[i] = buildMethodCard(UPI_METHODS[i], i == 0);
            methodCards[i].setBounds(16, 58 + i * 72, 378, 62);
            methodCards[i].addMouseListener(new MouseAdapter() {
                @Override public void mouseClicked(MouseEvent e) { selectMethod(idx); }
            });
            rightCard.add(methodCards[i]);
        }

        // UPI ID
        lbl(rightCard, "UPI ID", 16, 288, 370, 16, new Font("Arial", Font.BOLD, 11), new Color(200, 50, 50));

        upiField = new JTextField("yourname@upi");
        upiField.setFont(new Font("Arial", Font.PLAIN, 14));
        upiField.setForeground(new Color(200, 190, 230));
        upiField.setBackground(new Color(30, 27, 50));
        upiField.setCaretColor(Color.WHITE);
        upiField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(80, 70, 120), 1),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)));
        upiField.setBounds(16, 306, 378, 38);
        upiField.addFocusListener(new FocusAdapter() {
            @Override public void focusGained(FocusEvent e) {
                if (upiField.getText().equals("yourname@upi")) upiField.setText("");
            }
            @Override public void focusLost(FocusEvent e) {
                if (upiField.getText().trim().isEmpty()) upiField.setText("yourname@upi");
            }
        });
        rightCard.add(upiField);

        // Status
        statusLabel = new JLabel("", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Arial", Font.BOLD, 11));
        statusLabel.setBounds(16, 350, 378, 20);
        rightCard.add(statusLabel);

        // Pay button
        JButton payBtn = buildPayButton();
        payBtn.setBounds(16, 376, 378, 52);
        payBtn.addActionListener(e -> processPayment());
        rightCard.add(payBtn);

        lbl(rightCard, "By paying you agree to CinePlex Terms of Service",
                16, 438, 378, 16, new Font("Arial", Font.ITALIC, 10), new Color(80, 75, 110));

        // Back button
        JButton backBtn = buildBackButton();
        backBtn.setBounds(18, 580, 170, 34);
        backBtn.addActionListener(e -> {
            dispose();
            new MovieSelectionScreen(username, city).setVisible(true);
        });
        root.add(backBtn);

        setContentPane(root);
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private JPanel roundedCard(Color bg, Color border) {
        JPanel p = new JPanel(null) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                g2.setColor(border);
                g2.setStroke(new BasicStroke(1f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 16, 16);
            }
        };
        p.setOpaque(false);
        return p;
    }

    private void lbl(JPanel p, String text, int x, int y, int w, int h, Font f, Color fg) {
        JLabel l = new JLabel(text);
        l.setFont(f);
        l.setForeground(fg);
        l.setBounds(x, y, w, h);
        p.add(l);
    }

    private JPanel buildMethodCard(String[] method, boolean initialSel) {
        JPanel card = new JPanel(null) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                boolean sel = Boolean.TRUE.equals(getClientProperty("sel"));
                g2.setColor(sel ? new Color(35, 28, 58) : new Color(28, 25, 46));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                try {
                    Color ac = Color.decode(method[1]);
                    g2.setColor(sel ? ac : new Color(ac.getRed(), ac.getGreen(), ac.getBlue(), 80));
                } catch (Exception ignored) { g2.setColor(Color.GRAY); }
                g2.setStroke(new BasicStroke(sel ? 2f : 1f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
                int rx = getWidth() - 26, ry = (getHeight() - 14) / 2;
                g2.setColor(new Color(60, 55, 90));
                g2.fillOval(rx, ry, 14, 14);
                if (sel) {
                    try { g2.setColor(Color.decode(method[1])); } catch (Exception ignored) {}
                    g2.fillOval(rx + 3, ry + 3, 8, 8);
                }
            }
        };
        card.setOpaque(false);
        card.putClientProperty("sel", initialSel);
        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JLabel emojiLbl = new JLabel(method[2]);
        emojiLbl.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 22));
        emojiLbl.setBounds(10, 17, 32, 28);
        card.add(emojiLbl);

        JLabel nameLbl = new JLabel(method[0]);
        nameLbl.setFont(new Font("Arial", Font.BOLD, 14));
        nameLbl.setForeground(Color.WHITE);
        nameLbl.setBounds(50, 9, 260, 22);
        card.add(nameLbl);

        JLabel subLbl = new JLabel("Pay using " + method[0] + " UPI");
        subLbl.setFont(new Font("Arial", Font.PLAIN, 11));
        subLbl.setForeground(new Color(130, 120, 165));
        subLbl.setBounds(50, 33, 260, 16);
        card.add(subLbl);

        return card;
    }

    private void selectMethod(int idx) {
        selectedMethod = UPI_METHODS[idx][0];
        for (int i = 0; i < methodCards.length; i++) {
            methodCards[i].putClientProperty("sel", i == idx);
            methodCards[i].repaint();
        }
    }

    private JButton buildPayButton() {
        JButton btn = new JButton("PAY  ₹" + totalAmount + "  🔒") {
            private boolean hov = false;
            { addMouseListener(new MouseAdapter() {
                @Override public void mouseEntered(MouseEvent e) { hov = true;  repaint(); }
                @Override public void mouseExited (MouseEvent e) { hov = false; repaint(); }
            }); }
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setPaint(new GradientPaint(0, 0,
                        hov ? new Color(50, 210, 110) : new Color(35, 175, 85),
                        getWidth(), 0,
                        hov ? new Color(30, 175, 95)  : new Color(20, 145, 70)));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.setColor(new Color(255, 255, 255, 25));
                g2.fillRoundRect(0, 0, getWidth(), getHeight() / 2, 12, 12);
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Arial", Font.BOLD, 16));
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(),
                        (getWidth()  - fm.stringWidth(getText())) / 2,
                        (getHeight() + fm.getAscent()) / 2 - 4);
            }
        };
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private JButton buildBackButton() {
        JButton btn = new JButton("← Back to Movies") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(38, 34, 58));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.setColor(new Color(100, 90, 140));
                g2.setStroke(new BasicStroke(1f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);
                g2.setColor(new Color(190, 180, 220));
                g2.setFont(new Font("Arial", Font.PLAIN, 12));
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(),
                        (getWidth()  - fm.stringWidth(getText())) / 2,
                        (getHeight() + fm.getAscent()) / 2 - 3);
            }
        };
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void processPayment() {
        String upiId = upiField.getText().trim();
        if (upiId.isEmpty() || upiId.equals("yourname@upi") || !upiId.contains("@")) {
            statusLabel.setForeground(new Color(255, 80, 80));
            statusLabel.setText("✗  Enter a valid UPI ID  (e.g.  name@okaxis)");
            Toolkit.getDefaultToolkit().beep();
            return;
        }

        upiField.setEnabled(false);
        statusLabel.setForeground(new Color(220, 200, 60));
        statusLabel.setText("⏳  Processing payment…");

        Timer t1 = new Timer(1800, e -> {
            statusLabel.setForeground(new Color(50, 220, 100));
            statusLabel.setText("✓  Payment successful!");
            Toolkit.getDefaultToolkit().beep();
            Timer t2 = new Timer(700, e2 -> {
                dispose();
                new ConfirmationScreen(username, city, movieTitle, language,
                        seats, totalAmount, selectedMethod, upiId).setVisible(true);
            });
            t2.setRepeats(false);
            t2.start();
        });
        t1.setRepeats(false);
        t1.start();
    }
}
