import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

public class ModernUIComponents {

    // Global Modern Color Palette (Sleek Dark Mode / Indigo Palette)
    public static final Color COLOR_BG = new Color(18, 20, 29); // Dark blue-gray background
    public static final Color COLOR_CARD = new Color(28, 30, 43); // Darker card background
    public static final Color COLOR_PRIMARY = new Color(99, 102, 241); // Indigo primary
    public static final Color COLOR_PRIMARY_HOVER = new Color(79, 70, 229); // Darker indigo
    public static final Color COLOR_ACCENT = new Color(168, 85, 247); // Purple accent
    public static final Color COLOR_SUCCESS = new Color(34, 197, 94); // Emerald green
    public static final Color COLOR_DANGER = new Color(239, 68, 68); // Rose red
    public static final Color COLOR_TEXT_MAIN = new Color(243, 244, 246); // Off-white text
    public static final Color COLOR_TEXT_MUTED = new Color(156, 163, 175); // Gray text
    public static final Color COLOR_BORDER = new Color(55, 65, 81); // Slate border

    public static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 22);
    public static final Font FONT_SUBTITLE = new Font("Segoe UI", Font.BOLD, 16);
    public static final Font FONT_BODY = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_BOLD = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font FONT_SMALL = new Font("Segoe UI", Font.PLAIN, 11);

    /**
     * Rounded Panel with customized arc radius.
     */
    public static class RoundedPanel extends JPanel {
        private int arc = 15;
        private Color customBg = COLOR_CARD;

        public RoundedPanel(int arc, Color bg) {
            this.arc = arc;
            this.customBg = bg;
            setOpaque(false);
        }

        public RoundedPanel(int arc) {
            this(arc, COLOR_CARD);
        }

        public RoundedPanel() {
            this(15, COLOR_CARD);
        }

        public void setCustomBackground(Color bg) {
            this.customBg = bg;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(customBg);
            g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), arc, arc));
            g2.dispose();
            super.paintComponent(g);
        }
    }

    /**
     * Flat modern button with rounded corners, custom gradients or colors, and hover transitions.
     */
    public static class ModernButton extends JButton {
        private Color bg = COLOR_PRIMARY;
        private Color hoverBg = COLOR_PRIMARY_HOVER;
        private Color fg = COLOR_TEXT_MAIN;
        private int arc = 10;
        private boolean isHovered = false;

        public ModernButton(String text) {
            super(text);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setFont(FONT_BOLD);
            setForeground(fg);
            setCursor(new Cursor(Cursor.HAND_CURSOR));

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    isHovered = true;
                    repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    isHovered = false;
                    repaint();
                }
            });
        }

        public ModernButton(String text, Color bg, Color hoverBg) {
            this(text);
            this.bg = bg;
            this.hoverBg = hoverBg;
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            if (isEnabled()) {
                g2.setColor(isHovered ? hoverBg : bg);
            } else {
                g2.setColor(COLOR_BORDER);
            }
            
            g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), arc, arc));
            g2.dispose();
            super.paintComponent(g);
        }
    }

    /**
     * Modern design system text field with rounded container and placeholders.
     */
    public static class ModernTextField extends JTextField {
        private String placeholder;
        private int arc = 10;

        public ModernTextField(String placeholder) {
            this.placeholder = placeholder;
            setOpaque(false);
            setBackground(new Color(0, 0, 0, 0));
            setForeground(COLOR_TEXT_MAIN);
            setCaretColor(COLOR_TEXT_MAIN);
            setFont(FONT_BODY);
            setBorder(new EmptyBorder(5, 10, 5, 10));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            // Draw background card container
            g2.setColor(COLOR_CARD);
            g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), arc, arc));
            
            // Draw border
            g2.setColor(isFocusOwner() ? COLOR_PRIMARY : COLOR_BORDER);
            g2.setStroke(new BasicStroke(1.5f));
            g2.draw(new RoundRectangle2D.Double(1, 1, getWidth() - 2, getHeight() - 2, arc, arc));
            
            g2.dispose();
            super.paintComponent(g);

            // Draw placeholder
            if (getText().isEmpty() && placeholder != null) {
                Graphics2D gPlaceholder = (Graphics2D) g.create();
                gPlaceholder.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                gPlaceholder.setColor(COLOR_TEXT_MUTED);
                gPlaceholder.setFont(FONT_BODY);
                FontMetrics fm = gPlaceholder.getFontMetrics();
                int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
                gPlaceholder.drawString(placeholder, 10, y);
                gPlaceholder.dispose();
            }
        }
    }

    /**
     * Modern design system password field with rounded container and placeholders.
     */
    public static class ModernPasswordField extends JPasswordField {
        private String placeholder;
        private int arc = 10;

        public ModernPasswordField(String placeholder) {
            this.placeholder = placeholder;
            setOpaque(false);
            setBackground(new Color(0, 0, 0, 0));
            setForeground(COLOR_TEXT_MAIN);
            setCaretColor(COLOR_TEXT_MAIN);
            setFont(FONT_BODY);
            setBorder(new EmptyBorder(5, 10, 5, 10));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            // Draw background card container
            g2.setColor(COLOR_CARD);
            g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), arc, arc));
            
            // Draw border
            g2.setColor(isFocusOwner() ? COLOR_PRIMARY : COLOR_BORDER);
            g2.setStroke(new BasicStroke(1.5f));
            g2.draw(new RoundRectangle2D.Double(1, 1, getWidth() - 2, getHeight() - 2, arc, arc));
            
            g2.dispose();
            super.paintComponent(g);

            // Draw placeholder
            if (getPassword().length == 0 && placeholder != null) {
                Graphics2D gPlaceholder = (Graphics2D) g.create();
                gPlaceholder.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                gPlaceholder.setColor(COLOR_TEXT_MUTED);
                gPlaceholder.setFont(FONT_BODY);
                FontMetrics fm = gPlaceholder.getFontMetrics();
                int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
                gPlaceholder.drawString(placeholder, 10, y);
                gPlaceholder.dispose();
            }
        }
    }

    /**
     * Elegant custom progress bar for savings goals.
     */
    public static class ProgressBarCustom extends JComponent {
        private double progress = 0.0; // Range: 0.0 to 1.0
        private Color progressColor = COLOR_SUCCESS;

        public void setProgress(double progress) {
            this.progress = Math.max(0.0, Math.min(1.0, progress));
            repaint();
        }

        public void setProgressColor(Color color) {
            this.progressColor = color;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int width = getWidth();
            int height = getHeight();
            int arc = height; // Pills shape

            // Draw track (background)
            g2.setColor(COLOR_BG);
            g2.fill(new RoundRectangle2D.Double(0, 0, width, height, arc, arc));

            // Draw fill
            int fillWidth = (int) (width * progress);
            if (fillWidth > 0) {
                g2.setColor(progressColor);
                g2.fill(new RoundRectangle2D.Double(0, 0, fillWidth, height, arc, arc));
            }

            g2.dispose();
        }
    }
}
