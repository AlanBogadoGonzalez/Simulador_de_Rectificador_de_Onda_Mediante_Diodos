
package SimuladorRectificadorDeOnda;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.LinkedHashMap;
import java.util.Map;
 
//Panel de configuración del circuito rectificador.

public class PanelConfiguracion extends JPanel {
 

    // Componentes
    private JComboBox<String> selectorTipo;
    private JTextField campoVp;
    private JTextField campoFrecuencia;
    private JTextField campoRL;
    private JTextField campoVd;
    private JTextField campoC;
    private JCheckBox  checkFiltro;
    private JLabel lblUnidadC;
 
    public PanelConfiguracion() {
        inicializarComponentes();
        construirLayout();
        configurarEventos();
    }
 
    private void inicializarComponentes() {
        selectorTipo = new JComboBox<>(new String[]{ "Media onda (1 diodo)", "Onda completa (4 diodos)"});
        campoVp = new JTextField("12.0", 8);
        campoFrecuencia = new JTextField("60.0", 8);
        campoRL = new JTextField("1000.0", 8);
        campoVd = new JTextField("0.7", 8);
        campoC = new JTextField("1000.0", 8);
        campoC.setEnabled(false);
 
        checkFiltro = new JCheckBox("Incluir capacitor de filtro");
        lblUnidadC  = new JLabel("uF");
    }
 
    private void construirLayout() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder( BorderFactory.createEtchedBorder(), "Configuracion del circuito", TitledBorder.LEFT, TitledBorder.TOP));
 
        JPanel grid = new JPanel(new GridLayout(0, 3, 4, 6));
        grid.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
 
        // Tipo
        grid.add(new JLabel("Tipo:")); grid.add(selectorTipo); grid.add(new JLabel(""));
 
        // Separador visual
        grid.add(new JSeparator()); grid.add(new JSeparator()); grid.add(new JSeparator());
 
        // Parametros
        grid.add(new JLabel("Voltaje pico Vp:")); grid.add(campoVp); grid.add(new JLabel("V"));
        grid.add(new JLabel("Frecuencia f:")); grid.add(campoFrecuencia); grid.add(new JLabel("Hz"));
 
        grid.add(new JSeparator()); grid.add(new JSeparator()); grid.add(new JSeparator());
 
        grid.add(new JLabel("Caida diodo Vd:")); grid.add(campoVd); grid.add(new JLabel("V"));
        grid.add(new JLabel("Carga RL:")); grid.add(campoRL); grid.add(new JLabel("Ohm"));
 
        grid.add(new JSeparator()); grid.add(new JSeparator()); grid.add(new JSeparator());
 
        // Filtro
        grid.add(checkFiltro); grid.add(new JLabel("")); grid.add(new JLabel(""));
        grid.add(new JLabel("Capacitancia C:")); grid.add(campoC); grid.add(lblUnidadC);
 
        add(grid, BorderLayout.CENTER);
    }
 
    private void configurarEventos() {
        checkFiltro.addActionListener(e -> {campoC.setEnabled(checkFiltro.isSelected());});
    }
 
    private boolean mostrarError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
        return false;
    }
    
    public boolean validar() {
        try {
            double vp = Double.parseDouble(campoVp.getText().trim());
            double f  = Double.parseDouble(campoFrecuencia.getText().trim());
            double rl = Double.parseDouble(campoRL.getText().trim());
            double vd = Double.parseDouble(campoVd.getText().trim());
 
            if (vp <= 0) return mostrarError("El voltaje pico debe ser positivo.");
            if (f <= 0) return mostrarError("La frecuencia debe ser positiva.");
            if (rl <= 0) return mostrarError("La resistencia de carga debe ser positiva.");
            if (vd < 0 || vd > 2.0) return mostrarError("Vd debe estar entre 0.0 y 2.0 V.");
 
            if (checkFiltro.isSelected()) {
                double c = Double.parseDouble(campoC.getText().trim());
                if (c <= 0) return mostrarError("La capacitancia debe ser positiva.");
            }
            return true;
        } catch (NumberFormatException e) {
            return mostrarError("Todos los campos deben ser numericos.");
        }
    }
 
    public CircuitoRectificador construirCircuito() {
        double vp = Double.parseDouble(campoVp.getText().trim());
        double f  = Double.parseDouble(campoFrecuencia.getText().trim());
        double rl = Double.parseDouble(campoRL.getText().trim());
        double vd = Double.parseDouble(campoVd.getText().trim());
 
        Resistencia carga = new Resistencia("RL", rl);
        Diodo diodo = new Diodo("D1", vd);
 
        Capacitor filtro = null;
        if (checkFiltro.isSelected()) {
            double cUf = Double.parseDouble(campoC.getText().trim());
            filtro = new Capacitor("C1", cUf * 1e-6);
        }
 
        boolean esOndaCompleta = selectorTipo.getSelectedIndex() == 1;
        if (esOndaCompleta) {
            Diodo[] puente = { new Diodo("D1", vd), new Diodo("D2", vd),new Diodo("D3", vd), new Diodo("D4", vd)};
            return filtro != null
                ? new RectificadorOndaCompleta(f, vp, carga, filtro, puente)
                : new RectificadorOndaCompleta(f, vp, carga, puente);
        } else {
            return filtro != null
                ? new RectificadorMediaOnda(f, vp, carga, diodo, filtro)
                : new RectificadorMediaOnda(f, vp, carga, diodo);
        }
    }
 
    public Map<String, String> getValores() {
        Map<String, String> vals = new LinkedHashMap<>();
        vals.put("tipo", String.valueOf(selectorTipo.getSelectedIndex()));
        vals.put("vp", campoVp.getText().trim());
        vals.put("frecuencia", campoFrecuencia.getText().trim());
        vals.put("rl", campoRL.getText().trim());
        vals.put("vd", campoVd.getText().trim());
        vals.put("filtro", String.valueOf(checkFiltro.isSelected()));
        vals.put("c", campoC.getText().trim());
        return vals;
    }
 
    public void setValores(Map<String, String> vals) {
        if (vals.containsKey("tipo")) selectorTipo.setSelectedIndex(Integer.parseInt(vals.get("tipo")));
        if (vals.containsKey("vp")) campoVp.setText(vals.get("vp"));
        if (vals.containsKey("frecuencia")) campoFrecuencia.setText(vals.get("frecuencia"));
        if (vals.containsKey("rl")) campoRL.setText(vals.get("rl"));
        if (vals.containsKey("vd")) campoVd.setText(vals.get("vd"));
        if (vals.containsKey("filtro")) { 
            boolean tiene = Boolean.parseBoolean(vals.get("filtro"));
            checkFiltro.setSelected(tiene);
            campoC.setEnabled(tiene);
        }
        if (vals.containsKey("c")) campoC.setText(vals.get("c"));
    }
 
    public void limpiar() {
        selectorTipo.setSelectedIndex(0);
        campoVp.setText("12.0");
        campoFrecuencia.setText("60.0");
        campoRL.setText("1000.0");
        campoVd.setText("0.7");
        checkFiltro.setSelected(false);
        campoC.setText("1000.0");
        campoC.setEnabled(false);
    }
 

 
}