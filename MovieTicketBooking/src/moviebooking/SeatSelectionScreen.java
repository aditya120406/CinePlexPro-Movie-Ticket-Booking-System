package moviebooking;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class SeatSelectionScreen extends JFrame {

    private final String username, city, movieTitle, language;
    private final int numSeats;

    // Seat layout: 0=Silver, 1=Gold, 2=Recliner
    private static final int ROWS = 10, COLS = 12;
    private static final int[] ROW_CATEGORY = {2, 2, 1, 1, 1, 1, 0, 0, 0, 0}; // top=Recliner
    private static final int[] PRICES = {200, 300, 500};
    private static final String[] CAT_NAMES = {"Silver", "Gold", "Recliner"};
    private static final Color[] CAT_COLORS = {
            new Color(160, 140, 100),   // Silver
            new Color(212, 175, 55),    // Gold
            new Color(200, 100, 150)    // Recliner (pink/red)
    };

    private boolean[][] selected = new boolean[ROWS][COLS];
    private boolean[][] occupied = new boolean[ROWS][COLS];
    private JLabel priceLabel, seatsLabel, totalLabel;
    private JButton proceedBtn;
    private int totalPrice = 0;
    private int selectedCount = 0;

    public SeatSelectionScreen(String username, String city, String movieTitle, String language, int numSeats) {
        this.username = username;
        this.city = city;
        this.movieTitle = movieTitle;
        this.language = language;
        this.numSeats = numSeats;
        randomlyOccupySeats();
        setTitle("CinePlex Pro – Seat Selection");
        setSize(1000, 730);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        initComponents();
    }

    private void randomlyOccupySeats() {
        Random rnd = new Random(42);
        for (int r = 0; r < ROWS; r++)
            for (int c = 0; c < COLS; c++)
                occupied[r][c] = rnd.nextFloat() < 0.35f;
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setColor(new Color(8, 8, 18));
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setLayout(null);

        // Header
        JPanel header = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0, 0, new Color(140, 20, 20),
                        getWidth(), 0, new Color(60, 10, 80));
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        header.setLayout(null);
        header.setBounds(0, 0, 1000, 65);

        JLabel logo = new JLabel("🎬 " + movieTitle);
        logo.setFont(new Font("Georgia", Font.BOLD, 18));
        logo.setForeground(new Color(255, 200, 80));
        logo.setBounds(20, 17, 400, 32);
        header.add(logo);

        JLabel info = new JLabel("📍 " + city + "  |  🌐 " + language + "  |  💺 Select " + numSeats + " seat(s)");
        info.setFont(new Font("Arial", Font.PLAIN, 13));
        info.setForeground(new Color(200, 180, 220));
        info.setBounds(430, 20, 450, 25);
        header.add(info);
        mainPanel.add(header);

        // Screen indicator
        JPanel screenPanel = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, new Color(255, 255, 255, 0),
                        getWidth() / 2, 0, new Color(255, 255, 255, 100));
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
                GradientPaint gp2 = new GradientPaint(getWidth() / 2, 0, new Color(255, 255, 255, 100),
                        getWidth(), 0, new Color(255, 255, 255, 0));
                g2.setPaint(gp2);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        screenPanel.setBounds(150, 80, 700, 6);
        mainPanel.add(screenPanel);

        JLabel screenLbl = new JLabel("SCREEN");
        screenLbl.setFont(new Font("Arial", Font.BOLD, 11));
        screenLbl.setForeground(new Color(160, 155, 200));
        screenLbl.setBounds(450, 90, 100, 18);
        mainPanel.add(screenLbl);

        // Seat grid panel
        JPanel gridPanel = new JPanel(null) {
            @Override protected void paintComponent(Graphics g) {
                g.setColor(new Color(12, 12, 25));
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        gridPanel.setBounds(40, 115, 920, 440);

        int seatW = 40, seatH = 32, gapX = 8, gapY = 10;
        int totalSeatWidth = COLS * seatW + (COLS - 1) * gapX;
        int startX = (920 - totalSeatWidth) / 2;

        for (int row = 0; row < ROWS; row++) {
            int cat = ROW_CATEGORY[row];
            int y = row * (seatH + gapY) + 20;

            // Row label
            JLabel rowLabel = new JLabel(String.valueOf((char) ('A' + row)));
            rowLabel.setFont(new Font("Arial", Font.BOLD, 12));
            rowLabel.setForeground(CAT_COLORS[cat]);
            rowLabel.setBounds(startX - 28, y + 6, 20, 20);
            gridPanel.add(rowLabel);

            for (int col = 0; col < COLS; col++) {
                int x = startX + col * (seatW + gapX);
                final int r = row, c = col;
                JButton seat = createSeatButton(r, c, cat);
                seat.setBounds(x, y, seatW, seatH);
                gridPanel.add(seat);
            }

            // Category label on right
            JLabel catLabel = new JLabel(CAT_NAMES[cat] + " ₹" + PRICES[cat]);
            catLabel.setFont(new Font("Arial", Font.PLAIN, 10));
            catLabel.setForeground(CAT_COLORS[cat]);
            catLabel.setBounds(startX + totalSeatWidth + 8, y + 6, 100, 18);
            gridPanel.add(catLabel);
        }

        mainPanel.add(gridPanel);

        // Legend
        JPanel legendPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 8)) {
            @Override protected void paintComponent(Graphics g) {
                g.setColor(new Color(15, 13, 28));
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        legendPanel.setBounds(0, 555, 1000, 45);

        String[][] legendItems = {
                {"Available", "#282530"},
                {"Selected", "#22AA55"},
                {"Occupied", "#443344"},
                {"Silver ₹200", "#A08C64"},
                {"Gold ₹300", "#D4AF37"},
                {"Recliner ₹500", "#C86496"}
        };
        for (String[] item : legendItems) {
            JPanel dot = new JPanel() {
                @Override protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g;
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    try { g2.setColor(Color.decode(item[1])); } catch(Exception e) { g2.setColor(Color.GRAY); }
                    g2.fillRoundRect(0, 4, 14, 14, 4, 4);
                }
            };
            dot.setPreferredSize(new Dimension(14, 22));
            dot.setOpaque(false);
            legendPanel.add(dot);
            JLabel lbl = new JLabel(item[0]);
            lbl.setFont(new Font("Arial", Font.PLAIN, 11));
            lbl.setForeground(new Color(160, 150, 190));
            legendPanel.add(lbl);
        }
        mainPanel.add(legendPanel);

        // Bottom panel
        JPanel bottomPanel = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setColor(new Color(18, 16, 32));
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setColor(new Color(220, 50, 50, 60));
                g2.fillRect(0, 0, getWidth(), 1);
            }
        };
        bottomPanel.setLayout(null);
        bottomPanel.setBounds(0, 600, 1000, 130);

        seatsLabel = new JLabel("Selected: 0 / " + numSeats);
        seatsLabel.setFont(new Font("Arial", Font.BOLD, 14));
        seatsLabel.setForeground(new Color(200, 195, 230));
        seatsLabel.setBounds(30, 20, 200, 25);
        bottomPanel.add(seatsLabel);

        priceLabel = new JLabel("Amount: ₹0");
        priceLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        priceLabel.setForeground(new Color(160, 155, 200));
        priceLabel.setBounds(30, 50, 200, 22);
        bottomPanel.add(priceLabel);

        totalLabel = new JLabel("TOTAL: ₹0");
        totalLabel.setFont(new Font("Georgia", Font.BOLD, 22));
        totalLabel.setForeground(new Color(255, 200, 80));
        totalLabel.setBounds(380, 30, 240, 40);
        bottomPanel.add(totalLabel);

        JLabel convFee = new JLabel("+ ₹30 convenience fee");
        convFee.setFont(new Font("Arial", Font.ITALIC, 11));
        convFee.setForeground(new Color(110, 105, 140));
        convFee.setBounds(380, 72, 240, 18);
        bottomPanel.add(convFee);

        proceedBtn = new JButton("PROCEED TO PAYMENT →") {
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
                Color c1 = isEnabled() ? (hov ? new Color(50, 210, 110) : new Color(40, 180, 90))
                        : new Color(50, 50, 70);
                Color c2 = isEnabled() ? (hov ? new Color(30, 180, 100) : new Color(25, 150, 75))
                        : new Color(40, 40, 60);
                GradientPaint gp = new GradientPaint(0, 0, c1, getWidth(), 0, c2);
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Arial", Font.BOLD, 14));
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(), (getWidth() - fm.stringWidth(getText())) / 2,
                        (getHeight() + fm.getAscent()) / 2 - 4);
            }
        };
        proceedBtn.setOpaque(false);
        proceedBtn.setContentAreaFilled(false);
        proceedBtn.setBorderPainted(false);
        proceedBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        proceedBtn.setBounds(680, 28, 280, 50);
        proceedBtn.setEnabled(false);
        proceedBtn.addActionListener(e -> proceedToPayment());
        bottomPanel.add(proceedBtn);

        JButton backBtn = new JButton("← Back to Movies") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(40, 35, 60));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.setColor(new Color(200, 50, 50));
                g2.setStroke(new BasicStroke(1f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
                g2.setColor(new Color(200, 190, 220));
                g2.setFont(new Font("Arial", Font.PLAIN, 12));
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(), (getWidth() - fm.stringWidth(getText())) / 2,
                        (getHeight() + fm.getAscent()) / 2 - 3);
            }
        };
        backBtn.setOpaque(false);
        backBtn.setContentAreaFilled(false);
        backBtn.setBorderPainted(false);
        backBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        backBtn.setBounds(680, 85, 280, 32);
        backBtn.addActionListener(e -> { dispose(); new MovieSelectionScreen(username, city).setVisible(true); });
        bottomPanel.add(backBtn);

        mainPanel.add(bottomPanel);
        setContentPane(mainPanel);
    }

    private JButton createSeatButton(int row, int col, int cat) {
        JButton btn = new JButton() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color baseColor;
                if (occupied[row][col]) {
                    baseColor = new Color(50, 40, 60); // occupied
                } else if (selected[row][col]) {
                    baseColor = new Color(40, 180, 90); // selected
                } else {
                    // Available – category color dimmed
                    Color cc = CAT_COLORS[cat];
                    baseColor = new Color(cc.getRed() / 3, cc.getGreen() / 3, cc.getBlue() / 3 + 10);
                }
                g2.setColor(baseColor);
                g2.fillRoundRect(2, 2, getWidth() - 4, getHeight() - 4, 6, 6);
                // Seat back
                if (!occupied[row][col]) {
                    Color lighter = baseColor.brighter();
                    g2.setColor(new Color(lighter.getRed(), lighter.getGreen(), lighter.getBlue(), 80));
                    g2.fillRoundRect(4, 2, getWidth() - 8, (getHeight() - 4) / 2, 4, 4);
                }
                // Border
                if (selected[row][col]) {
                    g2.setColor(new Color(80, 255, 140));
                } else {
                    g2.setColor(new Color(60, 55, 80));
                }
                g2.setStroke(new BasicStroke(1f));
                g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 6, 6);
                // Seat number
                if (!occupied[row][col]) {
                    g2.setColor(selected[row][col] ? Color.WHITE : new Color(180, 170, 200));
                    g2.setFont(new Font("Arial", Font.PLAIN, 8));
                    String seatNum = (col + 1) + "";
                    FontMetrics fm = g2.getFontMetrics();
                    g2.drawString(seatNum, (getWidth() - fm.stringWidth(seatNum)) / 2,
                            getHeight() - 4);
                }
            }
        };
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        if (!occupied[row][col]) {
            btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            btn.addActionListener(e -> toggleSeat(row, col, cat, btn));
        } else {
            btn.setEnabled(false);
        }
        return btn;
    }

    private void toggleSeat(int row, int col, int cat, JButton btn) {
        if (selected[row][col]) {
            // Deselect
            selected[row][col] = false;
            selectedCount--;
            totalPrice -= PRICES[cat];
        } else {
            if (selectedCount >= numSeats) {
                Toolkit.getDefaultToolkit().beep();
                JOptionPane.showMessageDialog(this,
                        "You can only select " + numSeats + " seat(s).",
                        "Seat Limit", JOptionPane.WARNING_MESSAGE);
                return;
            }
            selected[row][col] = true;
            selectedCount++;
            totalPrice += PRICES[cat];
        }
        Toolkit.getDefaultToolkit().beep();
        updatePriceDisplay();
        btn.repaint();
    }

    private void updatePriceDisplay() {
        seatsLabel.setText("Selected: " + selectedCount + " / " + numSeats);
        priceLabel.setText("Seats: ₹" + totalPrice + "  +  Conv: ₹30");
        totalLabel.setText("TOTAL: ₹" + (totalPrice + 30));
        proceedBtn.setEnabled(selectedCount == numSeats);
        proceedBtn.repaint();
    }

    private void proceedToPayment() {
        // Build seat list string
        List<String> seatNames = new ArrayList<>();
        for (int r = 0; r < ROWS; r++)
            for (int c = 0; c < COLS; c++)
                if (selected[r][c])
                    seatNames.add(String.valueOf((char)('A' + r)) + (c + 1));

        dispose();
        new PaymentScreen(username, city, movieTitle, language, seatNames, totalPrice + 30).setVisible(true);
    }
}
