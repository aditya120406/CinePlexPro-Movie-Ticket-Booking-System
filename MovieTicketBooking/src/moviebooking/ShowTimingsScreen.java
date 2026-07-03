package moviebooking;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class ShowTimingsScreen extends JFrame {

    private final String username, city, movieTitle, language;
    private final int numSeats;

    private static final String[] DATES;
    private static final String[][] TIME_SLOTS = {
            {"10:00 AM", "Morning Show",   "AVAILABLE",  "#27AE60"},
            {"01:30 PM", "Matinee",        "FILLING FAST","#F39C12"},
            {"04:30 PM", "Evening Show",   "AVAILABLE",  "#27AE60"},
            {"07:00 PM", "Prime Time",     "HOUSEFUL",   "#E74C3C"},
            {"10:15 PM", "Night Show",     "AVAILABLE",  "#27AE60"},
    };

    static {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM");
        DATES = new String[7];
        for (int i = 0; i < 7; i++) {
            DATES[i] = sdf.format(cal.getTime());
            cal.add(Calendar.DAY_OF_YEAR, 1);
        }
    }

    private int selectedDateIdx = 0;
    private JPanel dateStrip;
    private JPanel[] dateBtns = new JPanel[7];

    public ShowTimingsScreen(String username, String city, String movieTitle,
                             String language, int numSeats) {
        this.username = username;
        this.city = city;
        this.movieTitle = movieTitle;
        this.language = language;
        this.numSeats = numSeats;
        setTitle("CinePlex Pro – Select Show");
        setSize(820, 580);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        initComponents();
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
        JPanel hdr = buildHeader();
        main.add(hdr);

        // Movie info strip
        JPanel infoStrip = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0,0,new Color(30,15,50),getWidth(),0,new Color(15,10,30));
                g2.setPaint(gp); g2.fillRect(0,0,getWidth(),getHeight());
            }
        };
        infoStrip.setLayout(null);
        infoStrip.setBounds(0, 65, 820, 55);

        JLabel movieLbl = new JLabel("🎬  " + movieTitle + "   |   🌐 " + language +
                "   |   💺 " + numSeats + " seat(s)   |   📍 " + city);
        movieLbl.setFont(new Font("Arial", Font.PLAIN, 14));
        movieLbl.setForeground(new Color(200, 185, 230));
        movieLbl.setBounds(25, 15, 750, 25);
        infoStrip.add(movieLbl);
        main.add(infoStrip);

        // Date selector
        JLabel dateLbl = new JLabel("SELECT DATE");
        dateLbl.setFont(new Font("Arial", Font.BOLD, 11));
        dateLbl.setForeground(new Color(220, 50, 50));
        dateLbl.setBounds(25, 132, 200, 18);
        main.add(dateLbl);

        dateStrip = new JPanel(null) {
            @Override protected void paintComponent(Graphics g) {
                g.setColor(new Color(15,13,28)); g.fillRect(0,0,getWidth(),getHeight());
            }
        };
        dateStrip.setBounds(0, 152, 820, 72);

        for (int i = 0; i < 7; i++) {
            final int idx = i;
            String[] parts = DATES[i].split(", ");
            JPanel dateCard = buildDateCard(parts[0], parts[1], i == 0);
            dateCard.setBounds(20 + i * 113, 8, 105, 56);
            dateCard.addMouseListener(new MouseAdapter() {
                @Override public void mouseClicked(MouseEvent e) { selectDate(idx); }
            });
            dateBtns[i] = dateCard;
            dateStrip.add(dateCard);
        }
        main.add(dateStrip);

        // Show timings
        JLabel showsLbl = new JLabel("SELECT SHOW TIME");
        showsLbl.setFont(new Font("Arial", Font.BOLD, 11));
        showsLbl.setForeground(new Color(220, 50, 50));
        showsLbl.setBounds(25, 238, 200, 18);
        main.add(showsLbl);

        for (int i = 0; i < TIME_SLOTS.length; i++) {
            String[] slot = TIME_SLOTS[i];
            boolean houseful = slot[2].equals("HOUSEFUL");
            JPanel timeCard = buildTimeCard(slot[0], slot[1], slot[2], slot[3], houseful);
            timeCard.setBounds(25 + i * 157, 262, 148, 100);
            if (!houseful) {
                final String time = slot[0];
                timeCard.addMouseListener(new MouseAdapter() {
                    @Override public void mouseClicked(MouseEvent e) {
                        Toolkit.getDefaultToolkit().beep();
                        dispose();
                        new SeatSelectionScreen(username, city, movieTitle, language, numSeats).setVisible(true);
                    }
                });
            }
            main.add(timeCard);
        }

        // Venue info
        JPanel venueCard = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(20,18,35));
                g2.fillRoundRect(0,0,getWidth(),getHeight(),14,14);
                g2.setColor(new Color(50,45,80,150));
                g2.setStroke(new BasicStroke(1f));
                g2.drawRoundRect(0,0,getWidth()-1,getHeight()-1,14,14);
            }
        };
        venueCard.setLayout(null);
        venueCard.setBounds(25, 385, 770, 90);
        venueCard.setOpaque(false);

        JLabel venueTitle = new JLabel("🏛️  Venue: CinePlex " + city + " – PVR IMAX");
        venueTitle.setFont(new Font("Arial", Font.BOLD, 14));
        venueTitle.setForeground(Color.WHITE);
        venueTitle.setBounds(20, 12, 500, 22);
        venueCard.add(venueTitle);

        JLabel venueDetails = new JLabel("Dolby Atmos  •  4DX Available  •  Recliner Lounge  •  Free Parking  •  Cafeteria");
        venueDetails.setFont(new Font("Arial", Font.PLAIN, 12));
        venueDetails.setForeground(new Color(140,130,175));
        venueDetails.setBounds(20, 38, 700, 20);
        venueCard.add(venueDetails);

        JLabel foodOffer = new JLabel("🍿  Combo Offer: Large Popcorn + 2 Drinks @ ₹349  (Add at counter)");
        foodOffer.setFont(new Font("Arial", Font.ITALIC, 11));
        foodOffer.setForeground(new Color(255,180,50));
        foodOffer.setBounds(20, 62, 700, 18);
        venueCard.add(foodOffer);
        main.add(venueCard);

        // Legend
        JPanel legend = new JPanel(new FlowLayout(FlowLayout.CENTER, 25, 5)) {
            @Override protected void paintComponent(Graphics g) {
                g.setColor(new Color(12,11,22)); g.fillRect(0,0,getWidth(),getHeight());
            }
        };
        legend.setBounds(0, 490, 820, 40);
        for (String[] item : new String[][]{{"● AVAILABLE","#27AE60"},{"● FILLING FAST","#F39C12"},{"● HOUSEFUL","#E74C3C"}}) {
            JLabel l = new JLabel(item[0]);
            l.setFont(new Font("Arial", Font.BOLD, 12));
            try { l.setForeground(Color.decode(item[1])); } catch(Exception ignored){}
            legend.add(l);
        }
        main.add(legend);

        // Back button
        JButton back = buildBackBtn();
        back.setBounds(680, 500, 120, 32);
        back.addActionListener(e -> { dispose(); new MovieSelectionScreen(username, city).setVisible(true); });
        main.add(back);

        setContentPane(main);
    }

    private JPanel buildHeader() {
        JPanel hdr = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0,0,new Color(140,20,20),getWidth(),0,new Color(60,10,80));
                g2.setPaint(gp); g2.fillRect(0,0,getWidth(),getHeight());
            }
        };
        hdr.setLayout(null); hdr.setBounds(0,0,820,65);
        JLabel logo = new JLabel("🎬 CinePlex Pro  –  Show Timings");
        logo.setFont(new Font("Georgia", Font.BOLD, 18));
        logo.setForeground(new Color(255,200,80));
        logo.setBounds(20,18,450,30); hdr.add(logo);
        return hdr;
    }

    private JPanel buildDateCard(String day, String date, boolean selected) {
        JPanel card = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                boolean sel = Boolean.TRUE.equals(getClientProperty("sel"));
                g2.setColor(sel ? new Color(220,50,50) : new Color(28,25,45));
                g2.fillRoundRect(0,0,getWidth(),getHeight(),10,10);
                g2.setColor(sel ? new Color(255,120,80) : new Color(60,55,90));
                g2.setStroke(new BasicStroke(1.2f));
                g2.drawRoundRect(0,0,getWidth()-1,getHeight()-1,10,10);
            }
        };
        card.putClientProperty("sel", selected);
        card.setLayout(null); card.setOpaque(false);
        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JLabel dayL = new JLabel(day);
        dayL.setFont(new Font("Arial", Font.BOLD, 11));
        dayL.setForeground(selected ? Color.WHITE : new Color(160,150,200));
        dayL.setBounds(0,6,105,18); dayL.setHorizontalAlignment(SwingConstants.CENTER);
        card.add(dayL);

        JLabel dateL = new JLabel(date);
        dateL.setFont(new Font("Georgia", Font.BOLD, 14));
        dateL.setForeground(selected ? new Color(255,220,100) : Color.WHITE);
        dateL.setBounds(0,26,105,22); dateL.setHorizontalAlignment(SwingConstants.CENTER);
        card.add(dateL);

        return card;
    }

    private void selectDate(int idx) {
        selectedDateIdx = idx;
        for (int i = 0; i < 7; i++) {
            dateBtns[i].putClientProperty("sel", i == idx);
            dateBtns[i].repaint();
            // update child label colors
            for (Component c : dateBtns[i].getComponents()) {
                if (c instanceof JLabel) {
                    ((JLabel)c).setForeground(i == idx ? Color.WHITE : new Color(160,150,200));
                }
            }
        }
        Toolkit.getDefaultToolkit().beep();
    }

    private JPanel buildTimeCard(String time, String name, String status, String colorHex, boolean disabled) {
        JPanel card = new JPanel() {
            private boolean hov = false;
            {
                if (!disabled) addMouseListener(new MouseAdapter() {
                    @Override public void mouseEntered(MouseEvent e) { hov=true; repaint(); setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); }
                    @Override public void mouseExited(MouseEvent e) { hov=false; repaint(); }
                });
            }
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color bg = disabled ? new Color(20,18,32) : (hov ? new Color(35,28,55) : new Color(25,22,42));
                g2.setColor(bg);
                g2.fillRoundRect(0,0,getWidth(),getHeight(),12,12);
                try {
                    Color ac = Color.decode(colorHex);
                    g2.setColor(disabled ? new Color(80,70,90) : (hov ? ac : new Color(ac.getRed(),ac.getGreen(),ac.getBlue(),120)));
                } catch (Exception ignored) {}
                g2.setStroke(new BasicStroke(hov&&!disabled?2f:1.2f));
                g2.drawRoundRect(0,0,getWidth()-1,getHeight()-1,12,12);
                if (!disabled && hov) {
                    try { g2.setColor(Color.decode(colorHex)); } catch(Exception ignored){}
                    g2.setStroke(new BasicStroke(3f));
                    g2.drawLine(14,0,getWidth()-14,0);
                }
            }
        };
        card.setLayout(null); card.setOpaque(false);

        JLabel timeLbl = new JLabel(time);
        timeLbl.setFont(new Font("Georgia", Font.BOLD, 18));
        timeLbl.setForeground(disabled ? new Color(80,70,90) : Color.WHITE);
        timeLbl.setBounds(0,12,148,28); timeLbl.setHorizontalAlignment(SwingConstants.CENTER);
        card.add(timeLbl);

        JLabel nameLbl = new JLabel(name);
        nameLbl.setFont(new Font("Arial", Font.PLAIN, 11));
        nameLbl.setForeground(new Color(130,120,165));
        nameLbl.setBounds(0,42,148,18); nameLbl.setHorizontalAlignment(SwingConstants.CENTER);
        card.add(nameLbl);

        JLabel statusLbl = new JLabel(status);
        statusLbl.setFont(new Font("Arial", Font.BOLD, 10));
        try { statusLbl.setForeground(disabled ? new Color(120,50,50) : Color.decode(colorHex)); } catch(Exception ignored){}
        statusLbl.setBounds(0,65,148,18); statusLbl.setHorizontalAlignment(SwingConstants.CENTER);
        card.add(statusLbl);

        return card;
    }

    private JButton buildBackBtn() {
        JButton b = new JButton("← Back") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(38,33,60));
                g2.fillRoundRect(0,0,getWidth(),getHeight(),8,8);
                g2.setColor(new Color(200,50,50,150));
                g2.setStroke(new BasicStroke(1f));
                g2.drawRoundRect(0,0,getWidth()-1,getHeight()-1,8,8);
                g2.setColor(new Color(200,190,220));
                g2.setFont(new Font("Arial",Font.PLAIN,12));
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(),(getWidth()-fm.stringWidth(getText()))/2,(getHeight()+fm.getAscent())/2-3);
            }
        };
        b.setOpaque(false); b.setContentAreaFilled(false); b.setBorderPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }
}
