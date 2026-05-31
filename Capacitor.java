
package SimuladorRectificadorDeOnda;

public class Capacitor extends ComponenteElectrico {

    // Constantes
    private static final double C_MIN = 1e-15;
    private static final double C_MAX = 1.0; //Colocado para que sea coherente

    // Atributos
    private double capacitancia;

    // Constructor
    public Capacitor(String nombre, double capacitancia){
        super(nombre);
        setCapacitancia(capacitancia);
    }

    // Getters y Setters
    public double getCapacitancia(){
        return capacitancia;
    }

    public double getCapacitanciaUF(){  //Para capacitancia es más utilizado microfaradios
        return capacitancia * 1e6;
    }
    public void setCapacitancia(double capacitancia){
        if (capacitancia <= C_MIN || capacitancia > C_MAX) {
            throw new IllegalArgumentException( "La capacitancia debe estar entre " + C_MIN + " y " + C_MAX + " F.");
        }
        this.capacitancia = capacitancia;
    }

    // Métodos
    public double calcularRipple(double Vprect, double frecuencia, double rl){
        if (Vprect <= 0)
            throw new IllegalArgumentException("Vprect debe ser positivo.");
        if (frecuencia <= 0)
            throw new IllegalArgumentException("La frecuencia debe ser positiva.");
        if (rl <= 0)
            throw new IllegalArgumentException("La resistencia de carga RL debe ser positiva.");

        return Vprect / (frecuencia * rl * capacitancia); //se usa f para media onda, 2f para onda completa, el rectificador pasa el valor correspondiente
    }    
    
    @Override
    public String toString(){
        return "Capacitor" + getNombre() + " : C = " + getCapacitanciaUF() + " µF.";
    }
}