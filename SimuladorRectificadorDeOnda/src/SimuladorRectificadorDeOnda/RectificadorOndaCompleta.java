
package SimuladorRectificadorDeOnda;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

public class RectificadorOndaCompleta extends CircuitoRectificador implements Serializable {
 
    private static final long serialVersionUID = 2L;
    private static final int  N_DIODOS = 4;
 
    // Atributos
    private Diodo[] puente;
 
    // Constructores
    public RectificadorOndaCompleta(double frecuencia, double voltajeEntradaPico, Resistencia carga, Diodo[] puente) {
        super(frecuencia, voltajeEntradaPico, carga);
        setPuente(puente);
    }
 
    public RectificadorOndaCompleta(double frecuencia, double voltajeEntradaPico, Resistencia carga, Capacitor filtro, Diodo[] puente) {
        super(frecuencia, voltajeEntradaPico, carga, filtro);
        setPuente(puente);
    }
    
    // Getters y Setters
    public Diodo[] getPuente() { return puente.clone(); }
 
    public void setPuente(Diodo[] puente) {
        if (puente == null || puente.length != N_DIODOS)
            throw new IllegalArgumentException( "El puente debe contener exactamente " + N_DIODOS + " diodos." );
        for (int i = 0; i < N_DIODOS; i++) {
            if (puente[i] == null)
                throw new IllegalArgumentException( "El diodo en posición " + i + " no puede ser nulo." );
        }
        this.puente = puente.clone();
    }

    // Métodos
    private double calcularVpRect() {
        return Math.max( getVoltajeEntradaPico() - 2.0 * puente[0].getVd(), 0.0 );   //Vprect = Vp − 2·Vd
    }
 
    @Override
    public double calcularVdc() {
        double vpRect = calcularVpRect();
        
        if (tieneFiltro()) {
            return Math.max(vpRect - calcularRipple() / 2.0, 0.0); // Vdc = Vp.rect − Vripple / 2
        }
        
        return (2.0 * vpRect) / Math.PI; // Vdc = 2·Vp.rect / π
    }

    @Override
    public double calcularRipple() {
        double VpRect = calcularVpRect();
        
        if (tieneFiltro()) {
            return getFiltro().calcularRipple( VpRect, 2*getFrecuencia(), getCarga().getResistencia() );
        }

        return VpRect;
    }
 
    @Override
    public double calcularEficiencia() {
        if (tieneFiltro()) {
            double vdc     = calcularVdc();
            double vripple = calcularRipple();
            double denom   = (vdc * vdc) + (vripple * vripple) / 12.0;
            return denom > 0 ? (vdc * vdc) / denom : 0.0; // CON filtro : η = Vdc² / (Vdc² + Vripple²/12) · 100%
        }
        
        return 8.0 / (Math.PI * Math.PI); // Sin filtro: 8/π²
    }
 

    @Override
    public double calcularPotenciaCarga() {
        double rl = getCarga().getResistencia();
        if (tieneFiltro()) {
            double vdc     = calcularVdc();
            double vripple = calcularRipple();
            return (vdc * vdc) / rl + (vripple * vripple) / (12.0 * rl);
        }
        double vpRect = calcularVpRect();
        return (vpRect * vpRect) / (2.0 * rl);
    }
 
    /**
    SIN filtro: genera la onda corregida, Vout(t) = max(Vp − Vd , 0)
    CON filtro: simula la carga y descarga real del capacitor:
        cuando el diodo conduce: Vc sube a Vp−Vd
        y cuando está bloqueado: Vc·e^(−Δt/τ),  τ = RL·C
    Finalmente se cargan los resultados en "resultado":
        resultado[0] = tiempo [s]
        resultado[1] = Vin(t) [V]
        resultado[2] = Vout(t) [V]
    */

    @Override
    public double[][] simular() {
        int n = N_PUNTOS;
        double f = getFrecuencia();
        double vp = getVoltajeEntradaPico();
        double vd = puente[0].getVd();
        double tMax = 3.0 / f;
 
        double[][] resultado = new double[3][n];
 
        if (tieneFiltro()) {
            double tau = getCarga().getResistencia() * getFiltro().getCapacitancia();
            double dt  = tMax / (n - 1);
            double vc  = 0.0;
 
            for (int i = 0; i < n; i++) {
                double t = i * dt;
                double vin  = vp * Math.sin(2.0 * Math.PI * f * t);
                double vDisponible = Math.abs(vin) - 2.0 * vd;
 
                if (vDisponible > vc) {
                    vc = vDisponible;              // carga
                } else {
                    vc = vc * Math.exp(-dt / tau); // descarga
                }
 
                resultado[0][i] = t;
                resultado[1][i] = vin;
                resultado[2][i] = Math.max(vc, 0.0);
            }
        } else {
            for (int i = 0; i < n; i++) {
                double t = i * tMax / (n - 1);
                double vin = vp * Math.sin(2.0 * Math.PI * f * t);
                double vout = Math.max(Math.abs(vin) - 2.0 * vd, 0.0);
                resultado[0][i] = t;
                resultado[1][i] = vin;
                resultado[2][i] = vout;
            }
        }
        return resultado;
    }
 
    @Override
    public Map<String, Double> getParametros() {
        Map<String, Double> params = new LinkedHashMap<>();
        
        params.put("Vdc", calcularVdc());
        params.put("Ripple", calcularRipple());
        params.put("Eficiencia", calcularEficiencia());
        params.put("Potencia", calcularPotenciaCarga());
        params.put("Vd", puente[0].getVd());
        
        return params;
    }
 

}
 