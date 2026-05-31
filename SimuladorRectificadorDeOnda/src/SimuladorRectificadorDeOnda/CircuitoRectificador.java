
package SimuladorRectificadorDeOnda;
 
public abstract class CircuitoRectificador implements Simulable {
 
    // Constantes
    protected static final int N_PUNTOS = 500; // puntos de la forma de onda
 
    // Atributos
    private double frecuencia;          // frecuencia de la señal AC
    private double voltajeEntradaPico;  // valor pico de la señal AC
    private Resistencia carga;          // resistencia de carga RL
    private Capacitor filtro;           // capacitor de filtro, puede ser null dependiendo de lo que escoja el usuario
 
    // Constructor - sin filtro
    public CircuitoRectificador(double frecuencia,double voltajeEntradaPico, Resistencia carga) {
        this(frecuencia, voltajeEntradaPico, carga, null);
    }
 
    // Constructor - con filtro
    public CircuitoRectificador(double frecuencia, double voltajeEntradaPico, Resistencia carga, Capacitor filtro) {
        setFrecuencia(frecuencia);
        setVoltajeEntradaPico(voltajeEntradaPico);
        setCarga(carga);

        this.filtro = filtro;
    }
 
    // Getters y Setters
    public double getFrecuencia() {
        return frecuencia;
    }
 
    public void setFrecuencia(double frecuencia) {
        if (frecuencia <= 0)
            throw new IllegalArgumentException("La frecuencia debe ser positiva.");
        this.frecuencia = frecuencia;
    }
 
    public double getVoltajeEntradaPico() {
        return voltajeEntradaPico;
    }
 
    public void setVoltajeEntradaPico(double voltajeEntradaPico) {
        if (voltajeEntradaPico <= 0)
            throw new IllegalArgumentException("El voltaje pico debe ser positivo.");
        this.voltajeEntradaPico = voltajeEntradaPico;
    }
 
    public Resistencia getCarga() {
        return carga;
    }
 
    public void setCarga(Resistencia carga) {   // ojo, acá se evalua que no sea NULL no más, la clase resistencia ya debería de haber evaluado que el valor sea coherente
        if (carga == null)
            throw new IllegalArgumentException("La resistencia de carga no puede ser nula.");
        this.carga = carga;
    }
 
    public Capacitor getFiltro() {
        return filtro;
    }
 
    public void setFiltro(Capacitor filtro) {
        this.filtro = filtro;
    }

    // Métodos abstractos
    public abstract double calcularVdc();
 
    public abstract double calcularRipple();
 
    public abstract double calcularEficiencia(); //η 
 
    // Otros métodos
    public boolean tieneFiltro() {
        return filtro != null;
    }

    public double calcularPotenciaCarga() {
        return carga.getPotenciaDisipada( calcularVdc() );
    }   
    
}
