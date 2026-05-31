
package SimuladorRectificadorDeOnda;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

// Panel gráfico que dibuja las formas de onda del circuito rectificador.

public class PanelFormasDeOnda extends JPanel {
    // Datos de la simulacion
    private double[][] datos;
    private double nivelDC;
    private boolean tieneDatos;
 
    // Margenes del area de dibujo
    private static final int MARGEN_IZQ = 50;
    private static final int MARGEN_DER = 10;
    private static final int MARGEN_SUP = 20;
    private static final int MARGEN_INF = 30;
 
    // Colores de las señales
    private static final Color COLOR_VIN = Color.BLUE;
    private static final Color COLOR_VOUT = Color.ORANGE;
    private static final Color COLOR_DC  = Color.GREEN.darker();
 
    public PanelFormasDeOnda() {
        tieneDatos = false;
        nivelDC    = 0.0;
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(560, 280));
        setBorder(BorderFactory.createTitledBorder( BorderFactory.createEtchedBorder(), "Formas de onda", TitledBorder.LEFT, TitledBorder.TOP));
    }
 
    public void setDatos(double[][] d) {
        this.datos = d;
        this.tieneDatos = (d != null && d.length >= 3 && d[0].length > 1);
        repaint();
    }
 
    public void setNivelDC(double vdc) {
        this.nivelDC = vdc;
        repaint();
    }
 
    public void limpiar() {
        datos = null;
        tieneDatos = false;
        nivelDC = 0.0;
        repaint();
    }
 
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
 
        if (!tieneDatos) {
            g.setColor(Color.GRAY);
            g.setFont(new Font("SansSerif", Font.PLAIN, 12));
            String msg = "Configure los parametros y presione Simular";
            g.drawString(msg, 60, getHeight() / 2);
            return;
        }
 
        int ancho = getWidth() - MARGEN_IZQ - MARGEN_DER;
        int alto = getHeight() - MARGEN_SUP  - MARGEN_INF;
        if (ancho <= 0 || alto <= 0) return;
 
        // Calcular escala
        double rango = calcularMaxAbs() * 1.2;
        if (rango < 0.01) rango = 1.0;
 
        dibujarGrilla(g, ancho, alto);
        dibujarNivelDC(g, ancho, alto, rango);
        dibujarSenal(g, datos[1], ancho, alto, rango, COLOR_VIN,  1);
        dibujarSenal(g, datos[2], ancho, alto, rango, COLOR_VOUT, 2);
        dibujarEjes(g, ancho, alto, rango);
        dibujarLeyenda(g);
    }
 
    private void dibujarGrilla(Graphics g, int ancho, int alto) {
        g.setColor(new Color(220, 220, 220));
        for (int i = 0; i <= 8; i++) {
            int y = MARGEN_SUP + i * alto / 8;
            g.drawLine(MARGEN_IZQ, y, MARGEN_IZQ + ancho, y);
        }
        for (int i = 0; i <= 10; i++) {
            int x = MARGEN_IZQ + i * ancho / 10;
            g.drawLine(x, MARGEN_SUP, x, MARGEN_SUP + alto);
        }
    }
 
    private void dibujarNivelDC(Graphics g, int ancho, int alto, double rango) {
        if (nivelDC <= 0.0) return;
        int yDC = vAPixel(nivelDC, alto, rango);
        g.setColor(COLOR_DC);
 
        // Línea punteada simple
        for (int x = MARGEN_IZQ; x < MARGEN_IZQ + ancho; x += 10) {
            g.drawLine(x, yDC, Math.min(x + 6, MARGEN_IZQ + ancho), yDC);
        }
        g.setFont(new Font("SansSerif", Font.PLAIN, 10));
        g.drawString(String.format("Vdc=%.2fV", nivelDC), MARGEN_IZQ + 4, yDC - 4);
    }
 
    private void dibujarSenal(Graphics g, double[] vals, int ancho, int alto, double rango, Color color, int grosor) {
        if (vals == null) return;
        g.setColor(color);
        int n = vals.length;
        for (int i = 1; i < n; i++) {
            int x1 = MARGEN_IZQ + (i - 1) * ancho / (n - 1);
            int y1 = vAPixel(vals[i - 1], alto, rango);
            int x2 = MARGEN_IZQ + i * ancho / (n - 1);
            int y2 = vAPixel(vals[i], alto, rango);
            for (int k = 0; k < grosor; k++) {
                g.drawLine(x1, y1 + k, x2, y2 + k);
            }
        }
    }
 
    private void dibujarEjes(Graphics g, int ancho, int alto, double rango) {
        g.setColor(Color.BLACK);
 
        // Eje Y
        g.drawLine(MARGEN_IZQ, MARGEN_SUP, MARGEN_IZQ, MARGEN_SUP + alto);
        // Eje X (cero voltios)
        int y0 = vAPixel(0.0, alto, rango);
        g.drawLine(MARGEN_IZQ, y0, MARGEN_IZQ + ancho, y0);
 
        // Etiquetas eje Y
        g.setFont(new Font("SansSerif", Font.PLAIN, 9));
        for (int i = 0; i <= 8; i++) {
            double v = rango - i * 2.0 * rango / 8;
            int    y = MARGEN_SUP + i * alto / 8;
            String s = String.format("%.1f", v);
            g.drawString(s, MARGEN_IZQ - 38, y + 4);
        }
 
        // Titulos
        g.setFont(new Font("SansSerif", Font.PLAIN, 10));
        g.drawString("V", 4, MARGEN_SUP + alto / 2);
        g.drawString("Tiempo", MARGEN_IZQ + ancho / 2 - 15, MARGEN_SUP + alto + 20);
    }
 
    private void dibujarLeyenda(Graphics g) {
        int x = MARGEN_IZQ + 10;
        int y = MARGEN_SUP + 14;
 
        g.setFont(new Font("SansSerif", Font.PLAIN, 11));
 
        g.setColor(COLOR_VIN);
        g.drawLine(x, y, x + 20, y);
        g.setColor(Color.BLACK);
        g.drawString("Vin(t)", x + 24, y + 4);
 
        g.setColor(COLOR_VOUT);
        g.drawLine(x, y + 16, x + 20, y + 16);
        g.setColor(Color.BLACK);
        g.drawString("Vout(t)", x + 24, y + 20);
    }
 
    private int vAPixel(double v, int alto, double rango) {
        int centro = MARGEN_SUP + alto / 2;
        return (int) (centro - v * (alto / 2.0) / rango);
    }
 
    private double calcularMaxAbs() {
        double max = 1.0;
        if (datos == null) return max;
        for (double[] canal : datos)
            for (double v : canal)
                if (Math.abs(v) > max) max = Math.abs(v);
        return max;
    }

}