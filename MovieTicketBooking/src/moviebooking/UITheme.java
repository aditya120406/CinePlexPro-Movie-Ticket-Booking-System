package moviebooking;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

/**
 * UITheme – Centralised styling utilities for CinePlex Pro.
 *
 * Provides:
 *  • Colour palette constants
 *  • Factory methods for common widgets (buttons, labels, panels)
 *  • Gradient / painting helpers
 *  • Tooltip styling
 */
public final class UITheme {

    // ─── Palette ────────────────────────────────────────────────────────────
    public static final Color BG_DEEP       = new Color(8,   8,  18);
    public static final Color BG_CARD       = new Color(22,  20,  38);
    public static final Color BG_CARD_HOVER = new Color(32,  28,  52);
    public static final Color ACCENT_RED    = new Color(220, 50,  50);
    public static final Color ACCENT_GOLD   = new Color(255, 200, 80);
    public static final Color ACCENT_GREEN  = new Color(40,  180, 90);
    public static final Color ACCENT_PURPLE = new Color(130, 60,  200);
    public static final Color TEXT_PRIMARY  = Color.WHITE;
    public static final Color TEXT_SECONDARY= new Color(160, 150, 200);
    public static final Color TEXT_MUTED    = new Color(90,  85,  120);
    public static final Color BORDER_SUBTLE = new Color(55,  50,  85);
    public static final Color BORDER_ACCENT = new Color(220, 50,  50,  120);

    // ─── Fonts ──────────────────────────────────────────────────────────────
    public static final Font FONT_TITLE   = new Font("Georgia", Font.BOLD,  22);
    public static final Font FONT_HEADING = new Font("Georgia", Font.BOLD,  16);
    public static final Font FONT_BODY    = new Font("Arial",   Font.PLAIN, 13);
    public static final Font FONT_SMALL   = new Font("Arial",   Font.PLAIN, 11);
    public static final Font FONT_BOLD    = new Font("Arial",   Font.BOLD,  13);
    public static final Font FONT_MONO    = new Font("Courier New", Font.PLAIN, 12);

    private UITheme() {} // static utility class

    // ─── Header Panel ───────────────────────────────────────────────────────

    /**
     * Creates the standard gradient header bar.
     * @param title  Text to show (HTML supported)
     * @param width  Total width of the header
     */
    public static JPanel createHeader(String title, int width) {
        JPanel hdr = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0, 0, new Color(140,20,20),
                        getWidth(), 0, new Color(60,10,80));
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        hdr.setLayout(null);
        hdr.setBounds(0, 0, width, 65);

        JLabel lbl = new JLabel("🎬  " + title);
        lbl.setFont(FONT_TITLE);
        lbl.setForeground(ACCENT_GOLD);
        lbl.setBounds(20, 16, width - 40, 34);
        hdr.add(lbl);
        return hdr;
    }

    // ─── Gradient Buttons ───────────────────────────────────────────────────

    public static JButton createPrimaryButton(String text) {
        return createGradientButton(text, ACCENT_RED, new Color(180,30,30));
    }

    public static JButton createSuccessButton(String text) {
        return createGradientButton(text, ACCENT_GREEN, new Color(25,150,75));
    }

    public static JButton createNeutralButton(String text) {
        return createGradientButton(text, new Color(50,45,70), new Color(35,30,55));
    }

    public static JButton createGradientButton(String text, Color c1, Color c2) {
        JButton btn = new JButton(text) {
            private boolean hov = false;
            {
                addMouseListener(new MouseAdapter() {
                    @Override public void mouseEntered(MouseEvent e) { hov=true; repaint(); }
                    @Override public void mouseExited(MouseEvent e)  { hov=false; repaint(); }
                });
            }
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color fc1 = hov ? c1.brighter() : c1;
                Color fc2 = hov ? c2.brighter() : c2;
                GradientPaint gp = new GradientPaint(0,0,fc1,getWidth(),0,fc2);
                g2.setPaint(gp);
                g2.fillRoundRect(0,0,getWidth(),getHeight(),12,12);
                // shine
                g2.setColor(new Color(255,255,255,25));
                g2.fillRoundRect(0,0,getWidth(),getHeight()/2,12,12);
                g2.setColor(TEXT_PRIMARY);
                g2.setFont(FONT_BOLD);
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(),(getWidth()-fm.stringWidth(getText()))/2,
                        (getHeight()+fm.getAscent())/2 - 4);
            }
        };
        btn.setOpaque(false); btn.setContentAreaFilled(false); btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(160, 42));
        return btn;
    }

    // ─── Styled Text Field ──────────────────────────────────────────────────

    public static JTextField createTextField(String placeholder) {
        JTextField f = new JTextField(placeholder) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(28,25,45));
                g2.fillRoundRect(0,0,getWidth(),getHeight(),10,10);
                g2.setColor(BORDER_ACCENT);
                g2.setStroke(new BasicStroke(1.2f));
                g2.drawRoundRect(0,0,getWidth()-1,getHeight()-1,10,10);
                super.paintComponent(g);
            }
        };
        f.setFont(FONT_BODY);
        f.setForeground(TEXT_PRIMARY);
        f.setCaretColor(TEXT_PRIMARY);
        f.setOpaque(false);
        f.setBorder(BorderFactory.createEmptyBorder(8,14,8,14));
        f.addFocusListener(new FocusAdapter() {
            @Override public void focusGained(FocusEvent e) {
                if (f.getText().equals(placeholder)) f.setText("");
            }
            @Override public void focusLost(FocusEvent e) {
                if (f.getText().isEmpty()) f.setText(placeholder);
            }
        });
        return f;
    }

    // ─── Card Panel ─────────────────────────────────────────────────────────

    public static JPanel createCard(int arcRadius) {
        return new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(BG_CARD);
                g2.fillRoundRect(0,0,getWidth(),getHeight(),arcRadius,arcRadius);
                g2.setColor(BORDER_SUBTLE);
                g2.setStroke(new BasicStroke(1f));
                g2.drawRoundRect(0,0,getWidth()-1,getHeight()-1,arcRadius,arcRadius);
            }
        };
    }

    // ─── Section Label ──────────────────────────────────────────────────────

    public static JLabel createSectionLabel(String text) {
        JLabel l = new JLabel(text.toUpperCase());
        l.setFont(new Font("Arial", Font.BOLD, 10));
        l.setForeground(ACCENT_RED);
        return l;
    }

    // ─── Badge / Chip ───────────────────────────────────────────────────────

    public static JPanel createBadge(String text, Color bg) {
        JPanel badge = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(bg);
                g2.fillRoundRect(0,0,getWidth(),getHeight(),8,8);
                g2.setColor(Color.BLACK);
                g2.setFont(FONT_SMALL);
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(text,(getWidth()-fm.stringWidth(text))/2,
                        (getHeight()+fm.getAscent())/2 - 3);
            }
        };
        badge.setPreferredSize(new Dimension(text.length() * 7 + 16, 20));
        badge.setOpaque(false);
        return badge;
    }

    // ─── Background Panel ───────────────────────────────────────────────────

    /** Full dark background panel */
    public static JPanel createDarkBackground() {
        return new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                g.setColor(BG_DEEP);
                g.fillRect(0,0,getWidth(),getHeight());
            }
        };
    }

    // ─── Divider ────────────────────────────────────────────────────────────

    public static JSeparator createAccentDivider() {
        JSeparator sep = new JSeparator();
        sep.setForeground(new Color(220,50,50,100));
        return sep;
    }

    // ─── Progress Bar ───────────────────────────────────────────────────────

    public static JProgressBar createProgressBar() {
        return new JProgressBar(0, 100) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(30,28,50));
                g2.fillRoundRect(0,0,getWidth(),getHeight(),10,10);
                int fw = (int)((getValue()/100.0)*getWidth());
                if (fw > 2) {
                    GradientPaint gp = new GradientPaint(0,0,ACCENT_RED,fw,0,new Color(255,150,50));
                    g2.setPaint(gp);
                    g2.fillRoundRect(0,0,fw,getHeight(),10,10);
                }
            }
        };
    }

    // ─── Tooltip ────────────────────────────────────────────────────────────

    /** Call once at startup to style all JToolTips */
    public static void installTooltipStyle() {
        UIManager.put("ToolTip.background", new Color(25,22,42));
        UIManager.put("ToolTip.foreground", new Color(210,200,235));
        UIManager.put("ToolTip.font",       FONT_SMALL);
        UIManager.put("ToolTip.border",     BorderFactory.createLineBorder(new Color(80,70,120)));
    }

    // ─── Painting Helpers ───────────────────────────────────────────────────

    /**
     * Draws a radial glow centred at (cx,cy).
     * alpha = 0–255 max opacity at centre.
     */
    public static void drawGlow(Graphics2D g2, int cx, int cy, int radius, Color color, int alpha) {
        RadialGradientPaint rg = new RadialGradientPaint(
                new Point2D.Float(cx, cy), radius,
                new float[]{0f, 1f},
                new Color[]{new Color(color.getRed(),color.getGreen(),color.getBlue(),alpha),
                        new Color(0,0,0,0)}
        );
        g2.setPaint(rg);
        g2.fillOval(cx - radius, cy - radius, radius*2, radius*2);
    }

    /**
     * Paints a simple film-strip decoration across width w.
     */
    public static void drawFilmStrip(Graphics2D g2, int x, int y, int w, int h) {
        g2.setColor(new Color(20,18,30));
        g2.fillRect(x, y, w, h);
        g2.setColor(new Color(40,38,55));
        for (int i = x + 10; i < x + w; i += 30) {
            g2.fillRoundRect(i, y + 5, 18, h - 10, 4, 4);
        }
    }
}
