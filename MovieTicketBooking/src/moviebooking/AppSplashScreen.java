package moviebooking;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

public class AppSplashScreen extends JWindow {

    private JProgressBar progressBar;
    private JLabel statusLabel;
    private JLabel titleLabel;
    private Timer animTimer;
    private int progress = 0;
    private float glowPhase = 0f;
    private Timer glowTimer;

    public AppSplashScreen() {
        setSize(700, 420);
        setLocationRelativeTo(null);
        initComponents();
    }

    private void initComponents() {
        // Main panel with custom painting
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Deep dark background
                GradientPaint bg = new GradientPaint(0, 0, new Color(5, 5, 15),
                        getWidth(), getHeight(), new Color(15, 10, 30));
                g2.setPaint(bg);
                g2.fillRect(0, 0, getWidth(), getHeight());

                // Film strip top
                drawFilmStrip(g2, 0, 0, getWidth(), 40);
                drawFilmStrip(g2, 0, getHeight() - 40, getWidth(), 40);

                // Glow circle behind logo
                float glow = (float)(Math.sin(glowPhase) * 0.5 + 0.5);
                int glowRadius = (int)(120 + glow * 20);
                RadialGradientPaint radial = new RadialGradientPaint(
                        new Point2D.Float(350, 180),
                        glowRadius,
                        new float[]{0f, 1f},
                        new Color[]{new Color(220, 50, 50, (int)(60 * glow)), new Color(0, 0, 0, 0)}
                );
                g2.setPaint(radial);
                g2.fillOval(350 - glowRadius, 180 - glowRadius, glowRadius * 2, glowRadius * 2);

                // Stars / particles
                g2.setColor(new Color(255, 255, 255, 60));
                int[][] stars = {{50,80},{120,60},{200,90},{280,55},{400,75},{480,65},
                        {560,85},{630,70},{80,300},{150,320},{250,310},{450,295},{530,315},{620,305}};
                for (int[] s : stars) {
                    g2.fillOval(s[0] - 1, s[1] - 1, 2, 2);
                }
            }

            private void drawFilmStrip(Graphics2D g2, int x, int y, int w, int h) {
                g2.setColor(new Color(20, 20, 30));
                g2.fillRect(x, y, w, h);
                g2.setColor(new Color(40, 40, 55));
                for (int i = 10; i < w; i += 30) {
                    g2.fillRoundRect(i, y + 6, 18, h - 12, 4, 4);
                }
            }
        };
        mainPanel.setLayout(null);

        // Cinema icon
        JLabel cinemaIcon = new JLabel("🎬") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                super.paintComponent(g);
            }
        };
        cinemaIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 60));
        cinemaIcon.setBounds(310, 70, 80, 80);
        mainPanel.add(cinemaIcon);

        // Title
        titleLabel = new JLabel("CINEPLEX PRO") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                // Text gradient
                GradientPaint gp = new GradientPaint(0, 0, new Color(255, 200, 50),
                        getWidth(), 0, new Color(255, 100, 50));
                g2.setPaint(gp);
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(), (getWidth() - fm.stringWidth(getText())) / 2,
                        (getHeight() + fm.getAscent()) / 2 - 4);
            }
        };
        titleLabel.setFont(new Font("Georgia", Font.BOLD, 42));
        titleLabel.setBounds(0, 145, 700, 60);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(titleLabel);

        // Tagline
        JLabel tagline = new JLabel("Your Ultimate Movie Experience");
        tagline.setFont(new Font("Arial", Font.ITALIC, 16));
        tagline.setForeground(new Color(180, 160, 220));
        tagline.setHorizontalAlignment(SwingConstants.CENTER);
        tagline.setBounds(0, 205, 700, 30);
        mainPanel.add(tagline);

        // Separator line
        JSeparator sep = new JSeparator();
        sep.setBounds(150, 250, 400, 2);
        sep.setForeground(new Color(220, 50, 50, 150));
        mainPanel.add(sep);

        // Progress bar
        progressBar = new JProgressBar(0, 100) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Background track
                g2.setColor(new Color(30, 30, 50));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                // Progress fill
                int fillW = (int)((getValue() / 100.0) * getWidth());
                if (fillW > 0) {
                    GradientPaint gp = new GradientPaint(0, 0, new Color(220, 50, 50),
                            fillW, 0, new Color(255, 150, 50));
                    g2.setPaint(gp);
                    g2.fillRoundRect(0, 0, fillW, getHeight(), 10, 10);
                    // Shine
                    g2.setColor(new Color(255, 255, 255, 40));
                    g2.fillRoundRect(0, 0, fillW, getHeight() / 2, 10, 10);
                }
            }
        };
        progressBar.setBounds(150, 270, 400, 14);
        progressBar.setBorderPainted(false);
        progressBar.setOpaque(false);
        mainPanel.add(progressBar);

        // Status label
        statusLabel = new JLabel("Initializing...");
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        statusLabel.setForeground(new Color(150, 150, 180));
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        statusLabel.setBounds(0, 295, 700, 25);
        mainPanel.add(statusLabel);

        // Version
        JLabel version = new JLabel("v2.0  |  © 2024 CinePlex Pro");
        version.setFont(new Font("Arial", Font.PLAIN, 11));
        version.setForeground(new Color(80, 80, 100));
        version.setHorizontalAlignment(SwingConstants.CENTER);
        version.setBounds(0, 370, 700, 20);
        mainPanel.add(version);

        // Border
        mainPanel.setBorder(BorderFactory.createLineBorder(new Color(220, 50, 50, 120), 2));

        setContentPane(mainPanel);
    }

    public void showSplash() {
        setVisible(true);

        String[] statuses = {
                "Loading movie database...",
                "Fetching latest shows...",
                "Preparing seat layouts...",
                "Setting up payment gateway...",
                "Launching CinePlex Pro..."
        };

        // Glow animation
        glowTimer = new Timer(30, e -> {
            glowPhase += 0.05f;
            repaint();
        });
        glowTimer.start();

        // Progress animation
        animTimer = new Timer(25, null);
        animTimer.addActionListener(e -> {
            progress++;
            progressBar.setValue(progress);

            int statusIdx = Math.min(progress / 20, statuses.length - 1);
            statusLabel.setText(statuses[statusIdx]);

            if (progress >= 100) {
                animTimer.stop();
                glowTimer.stop();
                Timer delay = new Timer(400, ev -> {
                    dispose();
                    new LoginScreen().setVisible(true);
                });
                delay.setRepeats(false);
                delay.start();
            }
        });
        animTimer.start();
    }
}
