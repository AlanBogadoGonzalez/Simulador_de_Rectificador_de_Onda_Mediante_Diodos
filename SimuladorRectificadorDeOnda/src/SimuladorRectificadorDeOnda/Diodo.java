
package SimuladorRectificadorDeOnda;

public class Diodo extends ComponenteElectrico {
    
    // Constantes
    private static final double VD_MIN = 0.0;
    private static final double VD_MAX = 2.0; //Colocado para que sea coherente
    
    // Atributos
    private double vd;

    // Constructor
    public Diodo(String nombre, double vd) {
        super(nombre);
        setVd(vd);
    }

    // Getters y Setters
    public double getVd() {
        return vd;
    }

    public void setVd(double vd) {
        if (vd < VD_MIN || vd > VD_MAX) {
            throw new IllegalArgumentException("La caída de voltaje vd debe estar entre "+ VD_MIN + " V y " + VD_MAX + " V.");
        }
        this.vd = vd;
    }

    @Override
    public String toString() {
        return "Diodo" + getNombre() + ": vd = " + vd + " V.";
    }
}