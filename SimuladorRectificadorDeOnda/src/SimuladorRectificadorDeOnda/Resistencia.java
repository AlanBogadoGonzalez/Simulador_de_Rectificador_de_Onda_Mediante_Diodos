
package SimuladorRectificadorDeOnda;

public class Resistencia extends ComponenteElectrico{
 
    // Constantes
    private static final double R_MIN = 1;
    private static final double R_MAX = 1000000.0;  //Colocado para que sea coherente
 
    // Atributos
    private double resistencia;
 
    // Constructor
    public Resistencia(String nombre, double resistencia){
        super(nombre);
        setResistencia(resistencia);
    }

    // Getters y Setters
    public double getResistencia(){
        return resistencia;
    }
 
    public void setResistencia(double resistencia){
        if (resistencia < R_MIN || resistencia > R_MAX) {
            throw new IllegalArgumentException( "La resistencia debe estar entre"+ R_MIN + " Ω y " + R_MAX + " Ω."  );
        }
        this.resistencia = resistencia;
    }
 
    // Métodos
    public double getPotenciaDisipada(double voltaje){
        if (voltaje < 0)
            throw new IllegalArgumentException("El voltaje no puede ser negativo.");
        return (voltaje * voltaje) / resistencia;
    }    
    
    @Override
    public String toString(){
        return "Resistencia "+ getNombre() + " : R = " + resistencia + " Ω";
    }
}