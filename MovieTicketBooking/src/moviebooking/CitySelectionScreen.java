package moviebooking;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

public class CitySelectionScreen extends JFrame {

    private final String username;

    private static final String[][] CITIES = {
            {"Mumbai", "🏙️", "Financial Capital"},
            {"Delhi", "🕌", "National Capital"},
            {"Bangalore", "💻", "Silicon Valley of India"},
            {"Chennai", "🎭", "Cultural Hub"},
            {"Kolkata", "🌉", "City of Joy"},
            {"Hyderabad", "💎", "City of Pearls"},
            {"Pune", "🏛️", "Oxford of the East"},
            {"Ahmedabad", "🦁", "City of Textiles"}
    };

    public CitySelectionScreen(String username) {
        this.username = username;
        setTitle("CinePlex Pro – Select City");
        setSize(900, 620);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        initComponents();
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                GradientPaint bg = new GradientPaint(0, 0, new Color(5, 5, 15),
                        getWidth(), getHeight(), new Color(10, 5, 25));
                g2.setPaint(bg);
                g2.fillRect(0, 0, getWidth(), getHeight());

                // Grid pattern
                g2.setColor(new Color(255, 255, 255, 6));
                for (int x = 0; x < getWidth(); x += 40) g2.drawLine(x, 0, x, getHeight());
                for (int y = 0; y < getHeight(); y += 40) g2.drawLine(0, y, getWidth(), y);
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
        header.setBounds(0, 0, 900, 90);

        JLabel logo = new JLabel("🎬 CinePlex Pro");
        logo.setFont(new Font("Georgia", Font.BOLD, 22));
        logo.setForeground(new Color(255, 200, 80));
        logo.setBounds(30, 28, 250, 35);
        header.add(logo);

        JLabel userInfo = new JLabel("👤 " + username + "  |  🏙️ Choose Your City");
        userInfo.setFont(new Font("Arial", Font.PLAIN, 13));
        userInfo.setForeground(new Color(200, 180, 220));
        userInfo.setBounds(550, 32, 330, 25);
        header.add(userInfo);

        mainPanel.add(header);

        // Title section
        JLabel title = new JLabel("SELECT YOUR CITY") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, new Color(255, 200, 80),
                        getWidth(), 0, new Color(255, 100, 50));
                g2.setPaint(gp);
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(), (getWidth() - fm.stringWidth(getText())) / 2,
                        (getHeight() + fm.getAscent()) / 2 - 4);
            }
        };
        title.setFont(new Font("Georgia", Font.BOLD, 28));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setBounds(0, 100, 900, 45);
        mainPanel.add(title);

        JLabel subtitle = new JLabel("Where would you like to watch movies today?");
        subtitle.setFont(new Font("Arial", Font.ITALIC, 14));
        subtitle.setForeground(new Color(160, 150, 190));
        subtitle.setHorizontalAlignment(SwingConstants.CENTER);
        subtitle.setBounds(0, 148, 900, 25);
        mainPanel.add(subtitle);

        // City grid
        int cols = 4;
        int cardW = 180, cardH = 120;
        int gapX = 25, gapY = 20;
        int totalW = cols * cardW + (cols - 1) * gapX;
        int startX = (900 - totalW) / 2;
        int startY = 195;

        for (int i = 0; i < CITIES.length; i++) {
            int row = i / cols;
            int col = i % cols;
            int x = startX + col * (cardW + gapX);
            int y = startY + row * (cardH + gapY);
            String[] cityData = CITIES[i];
            mainPanel.add(createCityCard(cityData[0], cityData[1], cityData[2], x, y, cardW, cardH));
        }

        // Bottom bar
        JPanel bottomBar = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setColor(new Color(20, 20, 35));
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setColor(new Color(220, 50, 50, 80));
                g2.fillRect(0, 0, getWidth(), 1);
            }
        };
        bottomBar.setLayout(null);
        bottomBar.setBounds(0, 570, 900, 50);

        JLabel footer = new JLabel("🎟️ Book tickets for the latest blockbusters  •  Multiple payment options  •  Instant confirmation");
        footer.setFont(new Font("Arial", Font.PLAIN, 12));
        footer.setForeground(new Color(100, 100, 130));
        footer.setHorizontalAlignment(SwingConstants.CENTER);
        footer.setBounds(0, 15, 900, 20);
        bottomBar.add(footer);
        mainPanel.add(bottomBar);

        setContentPane(mainPanel);
    }

    private JPanel createCityCard(String city, String emoji, String desc, int x, int y, int w, int h) {
        JPanel card = new JPanel() {
            private boolean hovered = false;
            {
                addMouseListener(new MouseAdapter() {
                    @Override public void mouseEntered(MouseEvent e) {
                        hovered = true;
                        repaint();
                        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                    }
                    @Override public void mouseExited(MouseEvent e) {
                        hovered = false;
                        repaint();
                    }
                    @Override public void mouseClicked(MouseEvent e) {
                        Toolkit.getDefaultToolkit().beep();
                        CitySelectionScreen.this.dispose();
                        new MovieSelectionScreen(username, city).setVisible(true);
                    }
                });
            }
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (hovered) {
                    // Glow effect
                    g2.setColor(new Color(220, 50, 50, 30));
                    g2.fillRoundRect(-4, -4, getWidth() + 8, getHeight() + 8, 18, 18);
                }
                // Card bg
                GradientPaint bg = hovered
                        ? new GradientPaint(0, 0, new Color(45, 20, 20), 0, getHeight(), new Color(30, 15, 45))
                        : new GradientPaint(0, 0, new Color(25, 22, 40), 0, getHeight(), new Color(18, 15, 30));
                g2.setPaint(bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);

                // Border
                Color borderColor = hovered ? new Color(220, 50, 50, 200) : new Color(60, 55, 90, 150);
                g2.setColor(borderColor);
                g2.setStroke(new BasicStroke(hovered ? 1.8f : 1f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 14, 14);

                // Top accent line
                if (hovered) {
                    g2.setColor(new Color(220, 50, 50));
                    g2.setStroke(new BasicStroke(2.5f));
                    g2.drawLine(20, 0, getWidth() - 20, 0);
                }
            }
        };
        card.setLayout(null);
        card.setBounds(x, y, w, h);
        card.setOpaque(false);

        JLabel emojiLabel = new JLabel(emoji);
        emojiLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 30));
        emojiLabel.setBounds(0, 12, w, 40);
        emojiLabel.setHorizontalAlignment(SwingConstants.CENTER);
        card.add(emojiLabel);

        JLabel nameLabel = new JLabel(city);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setBounds(0, 55, w, 22);
        nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        card.add(nameLabel);

        JLabel descLabel = new JLabel(desc);
        descLabel.setFont(new Font("Arial", Font.PLAIN, 10));
        descLabel.setForeground(new Color(140, 130, 170));
        descLabel.setBounds(0, 78, w, 18);
        descLabel.setHorizontalAlignment(SwingConstants.CENTER);
        card.add(descLabel);

        return card;
    }
}
