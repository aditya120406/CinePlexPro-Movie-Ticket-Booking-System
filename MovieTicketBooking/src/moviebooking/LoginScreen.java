package moviebooking;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

public class LoginScreen extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JLabel messageLabel;

    // Demo credentials
    private static final String DEMO_USER = "admin";
    private static final String DEMO_PASS = "1234";

    public LoginScreen() {
        setTitle("CinePlex Pro – Login");
        setSize(900, 560);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        initComponents();
    }

    private void initComponents() {
        JPanel root = new JPanel(new BorderLayout());

        // ── LEFT PANEL (cinematic art) ──────────────────────────────────
        JPanel leftPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Background gradient
                GradientPaint bg = new GradientPaint(0, 0, new Color(10, 5, 20),
                        0, getHeight(), new Color(80, 10, 10));
                g2.setPaint(bg);
                g2.fillRect(0, 0, getWidth(), getHeight());

                // Curtain effect
                drawCurtain(g2);

                // Screen glow
                RadialGradientPaint glow = new RadialGradientPaint(
                        new Point2D.Float(getWidth() / 2f, getHeight() / 2f), 180,
                        new float[]{0f, 1f},
                        new Color[]{new Color(255, 200, 100, 30), new Color(0, 0, 0, 0)}
                );
                g2.setPaint(glow);
                g2.fillOval(getWidth() / 2 - 180, getHeight() / 2 - 180, 360, 360);

                // Movie screen rectangle
                g2.setColor(new Color(255, 255, 240, 15));
                g2.fillRoundRect(40, 80, getWidth() - 80, 200, 8, 8);
                g2.setColor(new Color(255, 255, 255, 30));
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(40, 80, getWidth() - 80, 200, 8, 8);

                // Seats silhouette
                drawSeats(g2);
            }

            private void drawCurtain(Graphics2D g2) {
                // Left curtain
                GradientPaint lc = new GradientPaint(0, 0, new Color(100, 20, 20, 200),
                        60, 0, new Color(100, 20, 20, 0));
                g2.setPaint(lc);
                g2.fillRect(0, 0, 60, getHeight());
                // Right curtain
                GradientPaint rc = new GradientPaint(getWidth() - 60, 0, new Color(100, 20, 20, 0),
                        getWidth(), 0, new Color(100, 20, 20, 200));
                g2.setPaint(rc);
                g2.fillRect(getWidth() - 60, 0, 60, getHeight());
            }

            private void drawSeats(Graphics2D g2) {
                g2.setColor(new Color(80, 80, 120, 150));
                int startY = 310;
                for (int row = 0; row < 4; row++) {
                    int y = startY + row * 30;
                    int seatsInRow = 5 + row;
                    int totalW = seatsInRow * 28;
                    int startX = (getWidth() - totalW) / 2;
                    for (int col = 0; col < seatsInRow; col++) {
                        int x = startX + col * 28;
                        g2.fillRoundRect(x, y, 22, 18, 5, 5);
                    }
                }
            }
        };
        leftPanel.setLayout(null);
        leftPanel.setPreferredSize(new Dimension(390, 560));

        // Title on left
        JLabel movieTitle = new JLabel("<html><center><font size=5>🎬</font><br>CINEPLEX PRO</center></html>") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                super.paintComponent(g);
            }
        };
        movieTitle.setFont(new Font("Georgia", Font.BOLD, 22));
        movieTitle.setForeground(new Color(255, 200, 80));
        movieTitle.setHorizontalAlignment(SwingConstants.CENTER);
        movieTitle.setBounds(0, 155, 390, 80);
        leftPanel.add(movieTitle);

        JLabel tagline = new JLabel("Where Every Seat Tells a Story");
        tagline.setFont(new Font("Arial", Font.ITALIC, 13));
        tagline.setForeground(new Color(200, 180, 220, 200));
        tagline.setHorizontalAlignment(SwingConstants.CENTER);
        tagline.setBounds(0, 230, 390, 25);
        leftPanel.add(tagline);

        // Feature badges
        String[] features = {"🎭 Latest Blockbusters", "💺 Premium Seating", "🎵 Dolby Atmos"};
        for (int i = 0; i < features.length; i++) {
            JLabel badge = new JLabel(features[i]);
            badge.setFont(new Font("Arial", Font.PLAIN, 12));
            badge.setForeground(new Color(180, 180, 210));
            badge.setHorizontalAlignment(SwingConstants.CENTER);
            badge.setBounds(0, 460 + i * 22, 390, 20);
            leftPanel.add(badge);
        }

        // ── RIGHT PANEL (login form) ────────────────────────────────────
        JPanel rightPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(15, 15, 25));
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        rightPanel.setLayout(null);
        rightPanel.setPreferredSize(new Dimension(510, 560));

        // Form card
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(22, 22, 38));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.setColor(new Color(220, 50, 50, 80));
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
            }
        };
        card.setLayout(null);
        card.setBounds(40, 50, 430, 450);
        card.setOpaque(false);
        rightPanel.add(card);

        // Welcome text
        JLabel welcome = new JLabel("Welcome Back!");
        welcome.setFont(new Font("Georgia", Font.BOLD, 26));
        welcome.setForeground(new Color(255, 255, 255));
        welcome.setBounds(30, 30, 370, 40);
        card.add(welcome);

        JLabel subtitle = new JLabel("Sign in to book your favourite movies");
        subtitle.setFont(new Font("Arial", Font.PLAIN, 13));
        subtitle.setForeground(new Color(140, 140, 170));
        subtitle.setBounds(30, 72, 370, 20);
        card.add(subtitle);

        // Divider
        JPanel divider = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                g.setColor(new Color(220, 50, 50, 100));
                g.fillRect(0, 0, getWidth(), 1);
            }
        };
        divider.setBounds(30, 105, 370, 1);
        divider.setOpaque(false);
        card.add(divider);

        // Username
        JLabel userLabel = new JLabel("USERNAME");
        userLabel.setFont(new Font("Arial", Font.BOLD, 11));
        userLabel.setForeground(new Color(220, 50, 50));
        userLabel.setBounds(30, 130, 370, 18);
        card.add(userLabel);

        usernameField = createStyledTextField();
        usernameField.setBounds(30, 152, 370, 42);
        card.add(usernameField);

        // Password
        JLabel passLabel = new JLabel("PASSWORD");
        passLabel.setFont(new Font("Arial", Font.BOLD, 11));
        passLabel.setForeground(new Color(220, 50, 50));
        passLabel.setBounds(30, 210, 370, 18);
        card.add(passLabel);

        passwordField = new JPasswordField() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(30, 30, 50));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.setColor(new Color(220, 50, 50, 150));
                g2.setStroke(new BasicStroke(1.2f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
                super.paintComponent(g);
            }
        };
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordField.setForeground(Color.WHITE);
        passwordField.setCaretColor(Color.WHITE);
        passwordField.setOpaque(false);
        passwordField.setBorder(BorderFactory.createEmptyBorder(8, 14, 8, 14));
        passwordField.setBounds(30, 232, 370, 42);
        card.add(passwordField);

        // Demo hint
        JLabel hint = new JLabel("User: admin/1234  •  Admin panel: admin/admin");
        hint.setFont(new Font("Arial", Font.ITALIC, 11));
        hint.setForeground(new Color(100, 100, 140));
        hint.setBounds(30, 282, 370, 18);
        card.add(hint);

        // Message label
        messageLabel = new JLabel("");
        messageLabel.setFont(new Font("Arial", Font.BOLD, 12));
        messageLabel.setForeground(new Color(255, 80, 80));
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        messageLabel.setBounds(30, 305, 370, 20);
        card.add(messageLabel);

        // Login button
        JButton loginBtn = createLoginButton();
        loginBtn.setBounds(30, 335, 370, 48);
        card.add(loginBtn);

        // Guest login
        JLabel guestLabel = new JLabel("<html><center><font color='#888899'>or continue as</font> " +
                "<font color='#ff6644'><u>Guest</u></font></center></html>");
        guestLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        guestLabel.setHorizontalAlignment(SwingConstants.CENTER);
        guestLabel.setBounds(30, 395, 370, 25);
        guestLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        guestLabel.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                proceedToApp("Guest");
            }
        });
        card.add(guestLabel);

        // Actions
        loginBtn.addActionListener(e -> performLogin());
        passwordField.addActionListener(e -> performLogin());

        root.add(leftPanel, BorderLayout.WEST);
        root.add(rightPanel, BorderLayout.CENTER);
        setContentPane(root);
    }

    private JTextField createStyledTextField() {
        JTextField field = new JTextField() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(30, 30, 50));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.setColor(new Color(220, 50, 50, 150));
                g2.setStroke(new BasicStroke(1.2f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
                super.paintComponent(g);
            }
        };
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        field.setForeground(Color.WHITE);
        field.setCaretColor(Color.WHITE);
        field.setOpaque(false);
        field.setBorder(BorderFactory.createEmptyBorder(8, 14, 8, 14));
        return field;
    }

    private JButton createLoginButton() {
        JButton btn = new JButton("SIGN IN") {
            private boolean hovered = false;
            {
                addMouseListener(new MouseAdapter() {
                    @Override public void mouseEntered(MouseEvent e) { hovered = true; repaint(); }
                    @Override public void mouseExited(MouseEvent e) { hovered = false; repaint(); }
                });
            }
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color c1 = hovered ? new Color(255, 70, 70) : new Color(220, 50, 50);
                Color c2 = hovered ? new Color(255, 120, 50) : new Color(180, 30, 30);
                GradientPaint gp = new GradientPaint(0, 0, c1, getWidth(), 0, c2);
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                // Shine
                g2.setColor(new Color(255, 255, 255, 30));
                g2.fillRoundRect(0, 0, getWidth(), getHeight() / 2, 12, 12);
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Arial", Font.BOLD, 15));
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

    private void performLogin() {
        String user = usernameField.getText().trim();
        String pass = new String(passwordField.getPassword());
        UITheme.installTooltipStyle();
        if (user.equals("admin") && pass.equals("admin")) {
            // Admin mode
            messageLabel.setForeground(new Color(200, 150, 255));
            messageLabel.setText("🔐 Admin access granted!");
            Toolkit.getDefaultToolkit().beep();
            Timer t = new Timer(600, e -> { dispose(); new AdminDashboard("admin").setVisible(true); });
            t.setRepeats(false); t.start();
        } else if (user.equals(DEMO_USER) && pass.equals(DEMO_PASS)) {
            messageLabel.setForeground(new Color(50, 200, 100));
            messageLabel.setText("✓ Login successful! Loading...");
            Toolkit.getDefaultToolkit().beep();
            Timer t = new Timer(600, e -> proceedToApp(user));
            t.setRepeats(false);
            t.start();
        } else {
            messageLabel.setForeground(new Color(255, 80, 80));
            messageLabel.setText("✗ Invalid credentials. Try admin / 1234");
            Toolkit.getDefaultToolkit().beep();
        }
    }

    private void proceedToApp(String username) {
        dispose();
        new CitySelectionScreen(username).setVisible(true);
    }
}
