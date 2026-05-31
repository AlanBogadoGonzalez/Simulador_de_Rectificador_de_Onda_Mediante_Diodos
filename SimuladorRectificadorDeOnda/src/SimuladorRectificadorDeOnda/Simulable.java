
package SimuladorRectificadorDeOnda;

import java.util.Map;
 
public interface Simulable { // esta clase la implementó la clase abstracta Circuito rectificador y esta la terminaron implementando la clase RectificadorOndaCompleta y RectificadorMediaOnda

    double[][] simular();

    Map<String, Double> getParametros();
}
