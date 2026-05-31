
package SimuladorRectificadorDeOnda;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.Map;
 
/**
 * Ventana principal del Simulador de Circuitos Rectificadores.
 * Integra y coordina los tres paneles funcionales:
 *   - PanelConfiguracion : entrada de parámetros del circuito
 *   - PanelFormasDeOnda  : gráfica de Vin y Vout
 *   - PanelResultados    : tabla de parámetros calculados
 */
public class VentanaPrincipal extends JFrame {
 
    // Componentes
    private PanelConfiguracion panelConfig;
    private PanelFormasDeOnda panelGrafica;
    private PanelResultados panelResultados;
 
    // Dominio
    private Simulador simulador;
    private GestorArchivos gestor;
 
    // Botones
    private JButton btnSimular;
    private JButton btnGuardar;
    private JButton btnCargar;
    private JButton btnLimpiar;
 
 
    public VentanaPrincipal() {
        simulador = new Simulador();
        gestor = new GestorArchivos();
        inicializarComponentes();
        construirLayout();
        configurarVentana();
        manejarEventos();
    }
 
    private void inicializarComponentes() {
        panelConfig = new PanelConfiguracion();
        panelGrafica = new PanelFormasDeOnda();
        panelResultados = new PanelResultados();
 
        btnSimular = new JButton("Simular");
        btnGuardar = new JButton("Guardar");
        btnCargar = new JButton("Cargar");
        btnLimpiar = new JButton("Limpiar");
 
        
    }
 
    private void construirLayout() {
        setLayout(new BorderLayout(5, 5));
 
        // Panel izquierdo: configuracion y botones
        JPanel panelIzq = new JPanel(new BorderLayout(5, 5));
        panelIzq.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 4));
        panelIzq.add(panelConfig, BorderLayout.CENTER);
        panelIzq.add(construirPanelBotones(), BorderLayout.SOUTH);
        panelIzq.setPreferredSize(new Dimension(260, 0));
 
        // Panel derecho: grafica y resultados
        JPanel panelDer = new JPanel(new BorderLayout(5, 5));
        panelDer.setBorder(BorderFactory.createEmptyBorder(8, 4, 8, 8));
        panelDer.add(panelGrafica, BorderLayout.CENTER);
        panelDer.add(panelResultados, BorderLayout.SOUTH);
        panelResultados.setPreferredSize(new Dimension(0, 160));

        add(panelIzq, BorderLayout.WEST);
        add(panelDer, BorderLayout.CENTER);
        
    }
 
    private JPanel construirPanelBotones() {
        JPanel panel = new JPanel(new GridLayout(4, 1, 4, 4));
        panel.setBorder(BorderFactory.createEmptyBorder(4, 0, 0, 0));
        panel.add(btnSimular);
        panel.add(btnGuardar);
        panel.add(btnCargar);
        panel.add(btnLimpiar);
        return panel;
    }
 
    private void configurarVentana() {
        setTitle("Simulador de Circuitos Rectificadores");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 620);
        setMinimumSize(new Dimension(850, 500));
        setLocationRelativeTo(null);
    }
 
    private void manejarEventos() {
        btnSimular.addActionListener((ActionEvent e) -> manejarSimular());
        btnGuardar.addActionListener((ActionEvent e) -> manejarGuardar());
        btnCargar .addActionListener((ActionEvent e) -> manejarCargar());
        btnLimpiar.addActionListener((ActionEvent e) -> manejarLimpiar());
    }
 
    public void manejarSimular() {
        if (!panelConfig.validar()) return;
        try {
            CircuitoRectificador circuito = panelConfig.construirCircuito();
            simulador.setCircuito(circuito);
            double[][] datos = simulador.ejecutar();
            Map<String, Double> params = simulador.getParametrosCalculados();
            panelGrafica.setDatos(datos);
            panelGrafica.setNivelDC(params.getOrDefault("Vdc", 0.0));
            panelResultados.actualizar(params);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
 
    public void manejarGuardar() {
        if (simulador.getCircuito() == null) {
            JOptionPane.showMessageDialog(this, "Primero ejecute una simulacion.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
 
        String[] opciones = {"Binario (.dat)", "Texto (.txt)", "Cancelar"};
        int eleccion = JOptionPane.showOptionDialog(this,"Seleccione el formato:", "Guardar", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,null, opciones, opciones[0]);
        
        if (eleccion == 2 || eleccion == JOptionPane.CLOSED_OPTION) return;
 
        JFileChooser fc = new JFileChooser();
        if (eleccion == 0) {
            fc.setSelectedFile(new File("circuito.dat"));
        } else {
            fc.setSelectedFile(new File("circuito.txt"));
        }
 
        if (fc.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) return;
        String ruta = fc.getSelectedFile().getAbsolutePath();
 
        try {
            if (eleccion == 0) {
                gestor.setRutaArchivo(ruta);
                gestor.guardar(simulador.getCircuito());
            } else {
                gestor.exportarTxt(ruta, panelConfig.getValores());
            }
            JOptionPane.showMessageDialog(this, "Guardado correctamente.", "Ok", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error al guardar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
 
    public void manejarCargar() {
        JFileChooser fc = new JFileChooser();
        fc.setFileFilter(new FileNameExtensionFilter("Configuracion (*.dat, *.txt)", "dat", "txt"));
        if (fc.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) return;
 
        String ruta = fc.getSelectedFile().getAbsolutePath();
        try {
            if (ruta.endsWith(".dat")) {
                gestor.setRutaArchivo(ruta);
                simulador.setCircuito(gestor.cargar());
            } else {
                panelConfig.setValores(gestor.importarTxt(ruta));
            }
            
            JOptionPane.showMessageDialog(this, "Cargado correctamente.", "Ok", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException | ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
 
    public void manejarLimpiar() {
        panelConfig.limpiar();
        panelGrafica.limpiar();
        panelResultados.limpiar();
        simulador = new Simulador();
        
    }
 
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new VentanaPrincipal().setVisible(true));
    }
 
}