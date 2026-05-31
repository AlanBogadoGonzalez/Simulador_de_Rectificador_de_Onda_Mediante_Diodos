
package SimuladorRectificadorDeOnda;

import java.util.Map;
 
public class Simulador {
 
    // Atributos
    private CircuitoRectificador circuito;
    private double[][] resultados;
 
    // Constructores
    public Simulador() {
        this.circuito   = null;
        this.resultados = null;
    }
    
    public Simulador(CircuitoRectificador circuito) {
        setCircuito(circuito);
    }
 
    // Getters y Setters
    public CircuitoRectificador getCircuito() {
        return circuito;
    }
 
    public void setCircuito(CircuitoRectificador circuito) {
        if (circuito == null)
            throw new IllegalArgumentException("El circuito no puede ser nulo.");
        this.circuito   = circuito;
        this.resultados = null; 
    }
 
    public double[][] getResultados() {
        return resultados;
    }    
    
    // Métodos
    private void validarCircuito() {
        if (circuito == null)
            throw new IllegalStateException("No hay circuito configurado.");
    }    
    
    public double[][] ejecutar() {
        validarCircuito();
        resultados = circuito.simular();
        return resultados;
    }
 
    public Map<String, Double> getParametrosCalculados() {
        validarCircuito();
        return circuito.getParametros();
    }

}