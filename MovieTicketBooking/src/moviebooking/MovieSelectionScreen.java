package moviebooking;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.net.URI;

public class MovieSelectionScreen extends JFrame {

    private final String username;
    private final String city;

    private static final String[][] MOVIES = {
            {"Kalki 2898 AD", "Action/Sci-Fi", "9.1", "2h 55m", "https://www.youtube.com/watch?v=pkYLn06dDgA", "UA", "#FF6B35"},
            {"Stree 2", "Horror/Comedy", "8.8", "2h 15m", "https://www.youtube.com/watch?v=JtdKhFCVXEI", "UA", "#9B59B6"},
            {"Pushpa 2", "Action/Drama", "9.3", "3h 20m", "https://www.youtube.com/watch?v=3i2IVMaH_uI", "UA", "#E74C3C"},
            {"Jigra", "Action/Thriller", "8.2", "2h 30m", "https://www.youtube.com/results?search_query=Jigra+trailer", "UA", "#27AE60"},
            {"Singham Again", "Action", "7.9", "2h 45m", "https://www.youtube.com/results?search_query=Singham+Again+trailer", "UA", "#2980B9"},
            {"The Sabarmati Report", "Drama", "8.5", "2h 10m", "https://www.youtube.com/results?search_query=sabarmati+report+trailer", "UA", "#F39C12"},
    };

    public MovieSelectionScreen(String username, String city) {
        this.username = username;
        this.city = city;
        setTitle("CinePlex Pro – Movies in " + city);
        setSize(1050, 680);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        initComponents();
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

        // ── Header ──
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
        header.setBounds(0, 0, 1050, 70);

        JLabel logo = new JLabel("🎬 CinePlex Pro");
        logo.setFont(new Font("Georgia", Font.BOLD, 20));
        logo.setForeground(new Color(255, 200, 80));
        logo.setBounds(25, 18, 220, 35);
        header.add(logo);

        JLabel cityLabel = new JLabel("📍 " + city);
        cityLabel.setFont(new Font("Arial", Font.BOLD, 15));
        cityLabel.setForeground(new Color(255, 150, 100));
        cityLabel.setBounds(400, 22, 200, 28);
        header.add(cityLabel);

        JLabel userLbl = new JLabel("👤 " + username);
        userLbl.setFont(new Font("Arial", Font.PLAIN, 13));
        userLbl.setForeground(new Color(200, 180, 220));
        userLbl.setBounds(870, 22, 160, 28);
        header.add(userLbl);

        JButton backBtn = createHeaderButton("← Back");
        backBtn.setBounds(940, 18, 90, 32);
        backBtn.addActionListener(e -> { dispose(); new CitySelectionScreen(username).setVisible(true); });
        header.add(backBtn);
        mainPanel.add(header);

        // Section title
        JLabel sectionTitle = new JLabel("NOW SHOWING IN " + city.toUpperCase());
        sectionTitle.setFont(new Font("Georgia", Font.BOLD, 20));
        sectionTitle.setForeground(new Color(255, 200, 80));
        sectionTitle.setBounds(30, 85, 600, 35);
        mainPanel.add(sectionTitle);

        JLabel note = new JLabel("Click trailer icon to watch • Click poster to book");
        note.setFont(new Font("Arial", Font.ITALIC, 12));
        note.setForeground(new Color(120, 110, 150));
        note.setBounds(30, 118, 400, 20);
        mainPanel.add(note);

        // Movie cards
        int cols = 3;
        int cardW = 300, cardH = 230;
        int gapX = 35, gapY = 25;
        int startX = 35, startY = 150;

        for (int i = 0; i < MOVIES.length; i++) {
            int row = i / cols;
            int col = i % cols;
            int x = startX + col * (cardW + gapX);
            int y = startY + row * (cardH + gapY);
            mainPanel.add(createMovieCard(MOVIES[i], x, y, cardW, cardH));
        }

        setContentPane(mainPanel);
    }

    private JPanel createMovieCard(String[] movie, int x, int y, int w, int h) {
        String title = movie[0], genre = movie[1], rating = movie[2],
                duration = movie[3], trailerUrl = movie[4], cert = movie[5];
        Color accentColor = Color.decode(movie[6]);

        JPanel card = new JPanel() {
            private boolean hovered = false;
            {
                addMouseListener(new MouseAdapter() {
                    @Override public void mouseEntered(MouseEvent e) { hovered = true; repaint(); }
                    @Override public void mouseExited(MouseEvent e) { hovered = false; repaint(); }
                    @Override public void mouseClicked(MouseEvent e) {
                        openLanguageDialog(title);
                    }
                });
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Shadow
                if (hovered) {
                    g2.setColor(new Color(accentColor.getRed(), accentColor.getGreen(), accentColor.getBlue(), 40));
                    g2.fillRoundRect(-5, -5, w + 10, h + 10, 20, 20);
                }
                // Card
                GradientPaint bg = new GradientPaint(0, 0, new Color(28, 25, 45),
                        0, h, new Color(18, 15, 30));
                g2.setPaint(bg);
                g2.fillRoundRect(0, 0, w, h, 14, 14);

                // Poster area bg
                int pr = Math.min(255, Math.max(0, accentColor.getRed() / 5));
                int pg = Math.min(255, Math.max(0, accentColor.getGreen() / 5));
                int pb = Math.min(255, Math.max(0, accentColor.getBlue() / 5 + 10));
                g2.setColor(new Color(pr, pg, pb));
                g2.fillRoundRect(0, 0, 100, h, 14, 14);
                g2.fillRect(86, 0, 14, h);

                // Movie initial as poster art
                g2.setColor(accentColor);
                g2.setFont(new Font("Georgia", Font.BOLD, 40));
                FontMetrics fm = g2.getFontMetrics();
                String initial = title.substring(0, 1);
                g2.drawString(initial, (100 - fm.stringWidth(initial)) / 2,
                        h / 2 + fm.getAscent() / 2);

                // Genre strip at bottom of poster
                g2.setColor(new Color(0, 0, 0, 120));
                g2.fillRect(0, h - 35, 100, 35);

                // Border
                Color bc = hovered ? accentColor : new Color(50, 45, 75);
                g2.setColor(bc);
                g2.setStroke(new BasicStroke(hovered ? 2f : 1f));
                g2.drawRoundRect(0, 0, w - 1, h - 1, 14, 14);

                // Accent line top
                if (hovered) {
                    GradientPaint ap = new GradientPaint(100, 0, accentColor, w, 0, new Color(0, 0, 0, 0));
                    g2.setPaint(ap);
                    g2.setStroke(new BasicStroke(3f));
                    g2.drawLine(100, 0, w, 0);
                }
            }
        };
        card.setLayout(null);
        card.setBounds(x, y, w, h);
        card.setOpaque(false);

        // Title
        JLabel titleLbl = new JLabel("<html><b>" + title + "</b></html>");
        titleLbl.setFont(new Font("Arial", Font.BOLD, 15));
        titleLbl.setForeground(Color.WHITE);
        titleLbl.setBounds(110, 18, 175, 35);
        card.add(titleLbl);

        // Genre
        JLabel genreLbl = new JLabel(genre);
        genreLbl.setFont(new Font("Arial", Font.PLAIN, 11));
        genreLbl.setForeground(new Color(150, 145, 185));
        genreLbl.setBounds(110, 55, 175, 18);
        card.add(genreLbl);

        // Rating stars
        JLabel ratingLbl = new JLabel("⭐ " + rating + " / 10");
        ratingLbl.setFont(new Font("Arial", Font.BOLD, 13));
        ratingLbl.setForeground(new Color(255, 210, 60));
        ratingLbl.setBounds(110, 80, 175, 22);
        card.add(ratingLbl);

        // Duration
        JLabel durLbl = new JLabel("⏱ " + duration);
        durLbl.setFont(new Font("Arial", Font.PLAIN, 12));
        durLbl.setForeground(new Color(150, 145, 185));
        durLbl.setBounds(110, 105, 175, 18);
        card.add(durLbl);

        // Certification badge
        JPanel certBadge = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(200, 150, 50));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 6, 6);
                g2.setColor(Color.BLACK);
                g2.setFont(new Font("Arial", Font.BOLD, 10));
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(cert, (getWidth() - fm.stringWidth(cert)) / 2,
                        (getHeight() + fm.getAscent()) / 2 - 3);
            }
        };
        certBadge.setBounds(110, 130, 32, 18);
        certBadge.setOpaque(false);
        card.add(certBadge);

        // Trailer button
        JButton trailerBtn = new JButton("▶ Trailer") {
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
                g2.setColor(hov ? new Color(200, 30, 30) : new Color(150, 20, 20));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Arial", Font.BOLD, 11));
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(), (getWidth() - fm.stringWidth(getText())) / 2,
                        (getHeight() + fm.getAscent()) / 2 - 3);
            }
        };
        trailerBtn.setOpaque(false);
        trailerBtn.setContentAreaFilled(false);
        trailerBtn.setBorderPainted(false);
        trailerBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        trailerBtn.setBounds(110, 155, 90, 28);
        trailerBtn.addActionListener(e -> openTrailer(trailerUrl));
        card.add(trailerBtn);

        // Book button
        JButton bookBtn = new JButton("Book Now →") {
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
                GradientPaint gp = new GradientPaint(0, 0, hov ? accentColor.brighter() : accentColor,
                        getWidth(), 0, hov ? accentColor : accentColor.darker());
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Arial", Font.BOLD, 11));
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(), (getWidth() - fm.stringWidth(getText())) / 2,
                        (getHeight() + fm.getAscent()) / 2 - 3);
            }
        };
        bookBtn.setOpaque(false);
        bookBtn.setContentAreaFilled(false);
        bookBtn.setBorderPainted(false);
        bookBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        bookBtn.setBounds(207, 155, 100, 28);
        bookBtn.addActionListener(e -> openLanguageDialog(title));
        card.add(bookBtn);

        // Genre label on poster bottom
        JLabel pgLabel = new JLabel(genre.split("/")[0]);
        pgLabel.setFont(new Font("Arial", Font.PLAIN, 9));
        pgLabel.setForeground(new Color(200, 200, 220));
        pgLabel.setBounds(5, h - 30, 90, 24);
        pgLabel.setHorizontalAlignment(SwingConstants.CENTER);
        card.add(pgLabel);

        return card;
    }

    private void openTrailer(String url) {
        try {
            Desktop.getDesktop().browse(new URI(url));
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Could not open browser.\nURL: " + url,
                    "Browser Error", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void openLanguageDialog(String movieTitle) {
        // Language popup
        String[] languages = {"Hindi", "English", "Tamil", "Telugu", "Kannada"};
        JPanel panel = new JPanel(new GridLayout(0, 1, 5, 8)) {
            @Override protected void paintComponent(Graphics g) {
                g.setColor(new Color(18, 15, 30));
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        panel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        panel.setOpaque(true);

        JLabel heading = new JLabel("🌐 Select Language");
        heading.setFont(new Font("Arial", Font.BOLD, 15));
        heading.setForeground(new Color(255, 200, 80));
        panel.add(heading);

        ButtonGroup bg = new ButtonGroup();
        JRadioButton[] rbts = new JRadioButton[languages.length];
        for (int i = 0; i < languages.length; i++) {
            rbts[i] = new JRadioButton(languages[i]);
            rbts[i].setFont(new Font("Arial", Font.PLAIN, 14));
            rbts[i].setForeground(Color.WHITE);
            rbts[i].setOpaque(false);
            rbts[i].setSelected(i == 0);
            bg.add(rbts[i]);
            panel.add(rbts[i]);
        }

        UIManager.put("OptionPane.background", new Color(18, 15, 30));
        UIManager.put("Panel.background", new Color(18, 15, 30));

        int result = JOptionPane.showConfirmDialog(this, panel, "Language Selection",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String lang = "Hindi";
            for (JRadioButton rb : rbts) if (rb.isSelected()) lang = rb.getText();
            openSeatCountDialog(movieTitle, lang);
        }
    }

    private void openSeatCountDialog(String movieTitle, String lang) {
        JPanel panel = new JPanel(new GridLayout(0, 1, 5, 10)) {
            @Override protected void paintComponent(Graphics g) {
                g.setColor(new Color(18, 15, 30));
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        panel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        panel.setOpaque(true);

        JLabel heading = new JLabel("💺 How many seats?");
        heading.setFont(new Font("Arial", Font.BOLD, 15));
        heading.setForeground(new Color(255, 200, 80));
        panel.add(heading);

        JLabel info = new JLabel("Max 10 seats per booking");
        info.setFont(new Font("Arial", Font.ITALIC, 11));
        info.setForeground(new Color(150, 140, 180));
        panel.add(info);

        SpinnerModel sm = new SpinnerNumberModel(2, 1, 10, 1);
        JSpinner spinner = new JSpinner(sm);
        spinner.setFont(new Font("Arial", Font.BOLD, 18));
        spinner.setPreferredSize(new Dimension(100, 40));
        panel.add(spinner);

        int result = JOptionPane.showConfirmDialog(this, panel, "Number of Seats",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            int seats = (int) spinner.getValue();
            dispose();
            // Route through ShowTimingsScreen before Seat Selection
            new ShowTimingsScreen(username, city, movieTitle, lang, seats).setVisible(true);
        }
    }

    private JButton createHeaderButton(String text) {
        JButton btn = new JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(255, 255, 255, 30));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.setColor(new Color(255, 255, 255, 80));
                g2.setStroke(new BasicStroke(1f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Arial", Font.BOLD, 12));
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
}
