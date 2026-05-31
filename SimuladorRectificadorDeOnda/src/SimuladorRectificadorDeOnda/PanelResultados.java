
package SimuladorRectificadorDeOnda;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.Map;
 
//Panel que muestra los resultados numéricos calculados por el Simulador.

public class PanelResultados extends JPanel {
 
    private JLabel lblVdc;
    private JLabel lblRipple;
    private JLabel lblEficiencia;
    private JLabel lblPotencia;
    private JLabel lblVd;
 
    public PanelResultados() {
        inicializarComponentes();
        construirLayout();
    }
 
    private void inicializarComponentes() {
        lblVdc = new JLabel("---");
        lblRipple = new JLabel("---");
        lblEficiencia = new JLabel("---");
        lblPotencia = new JLabel("---");
        lblVd = new JLabel("---");
    }
 
    private void construirLayout() {
        setLayout(new GridLayout(0, 3, 4, 4));
        setBorder(BorderFactory.createTitledBorder( BorderFactory.createEtchedBorder(), "Resultados", TitledBorder.LEFT, TitledBorder.TOP));
 
        setBorder(BorderFactory.createTitledBorder("Resultados"));
 
        JPanel grid = new JPanel(new GridLayout(0, 3, 6, 6));
        grid.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
 
        grid.add(new JLabel("Voltaje DC (Vdc):")); grid.add(lblVdc); grid.add(new JLabel("V"));
        grid.add(new JLabel("Ripple (Vpp):")); grid.add(lblRipple); grid.add(new JLabel("V"));
        grid.add(new JLabel("Eficiencia:")); grid.add(lblEficiencia); grid.add(new JLabel("%"));
        grid.add(new JLabel("Potencia carga:")); grid.add(lblPotencia); grid.add(new JLabel("W"));
        grid.add(new JSeparator()); grid.add(new JSeparator()); grid.add(new JSeparator());
        grid.add(new JLabel("Caida diodo (Vd):")); grid.add(lblVd); grid.add(new JLabel("V"));
 
        add(grid);
    }
 
    public void actualizar(Map<String, Double> params) {
        if (params == null) { limpiar(); return; }
 
        Double vdc = params.get("Vdc");
        Double rip = params.get("Ripple");
        Double ef = params.get("Eficiencia");
        Double pot = params.get("Potencia");
        Double vd = params.get("Vd");
 
        lblVdc.setText( vdc  != null ? String.format("%.3f", vdc) : "---");
        lblRipple.setText( rip  != null ? String.format("%.3f", rip) : "---");
        lblEficiencia.setText(ef != null ? String.format("%.2f", ef * 100.0) : "---");
        lblPotencia.setText( pot != null ? String.format("%.4f", pot) : "---");
        lblVd.setText( vd != null ? String.format("%.2f", vd) : "---");
    }
 
    public void limpiar() {
        lblVdc.setText("---");
        lblRipple.setText("---");
        lblEficiencia.setText("---");
        lblPotencia.setText("---");
        lblVd.setText("---");
    }
}