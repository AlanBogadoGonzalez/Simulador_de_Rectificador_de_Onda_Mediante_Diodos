
package SimuladorRectificadorDeOnda;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;
 
public class RectificadorMediaOnda extends CircuitoRectificador implements Serializable {
 
    private static final long serialVersionUID = 1L;
 
    // Atributo
    private Diodo diodo;
 
    // Constructores
    public RectificadorMediaOnda(double frecuencia, double voltajeEntradaPico, Resistencia carga, Diodo diodo) {
        super(frecuencia, voltajeEntradaPico, carga);
        setDiodo(diodo);
    }
 
    public RectificadorMediaOnda(double frecuencia, double voltajeEntradaPico, Resistencia carga, Diodo diodo, Capacitor filtro) {
        super(frecuencia, voltajeEntradaPico, carga, filtro);
        setDiodo(diodo);
    }
 
    // Getters y Setters
    public Diodo getDiodo() { return diodo; }
 
    public void setDiodo(Diodo diodo) {
        if (diodo == null)
            throw new IllegalArgumentException("El diodo no puede ser nulo.");
        this.diodo = diodo;
    }
    
    // Métodos
    private double calcularVpRect() {
        return Math.max( getVoltajeEntradaPico() - diodo.getVd(), 0.0 ); // Vprect = Vp − Vd
    }

    @Override
    public double calcularVdc() {
        double vpRect = calcularVpRect();
        
        if (tieneFiltro()) {
            return Math.max( vpRect - calcularRipple() / 2.0, 0.0 );   //Vdc = Vprect − Vripple / 2
        }
        return vpRect / Math.PI; //Vdc = Vprect / π
    }
 
    @Override
    public double calcularRipple() {
        double VpRect = calcularVpRect();   //Vripple = Vprect

        if (tieneFiltro()) {
            return getFiltro().calcularRipple( VpRect, getFrecuencia(), getCarga().getResistencia() ); //Vripple = (1 / f·RL·C)*Vprect
        }
        return VpRect;
    }
 
    @Override
    public double calcularEficiencia() {
        if (tieneFiltro()) {
            double vdc = calcularVdc();
            double vripple = calcularRipple();
            if(vdc == 0){return 0;}
            return (vdc * vdc) / ( (vdc * vdc) + (vripple * vripple) / 12.0 ) ;   //η = Vdc² / (Vdc² + Vripple²/12)
        }

        return 4.0 / (Math.PI * Math.PI); //η = (4/π²)
    }
 
    @Override
    public double calcularPotenciaCarga() {
        double rl = getCarga().getResistencia();
        if (tieneFiltro()) {
            double vdc     = calcularVdc();
            double vripple = calcularRipple();
            return (vdc * vdc) / rl + (vripple * vripple) / (12.0 * rl);    // P = Vdc²/RL + Vripple²/(12·RL)
        }
        double vpRect = calcularVpRect();
        return (vpRect * vpRect) / (4.0 * rl); // P = Vp.rect² / (4·RL)
    }
 
    /**
    SIN filtro: genera la media onda corregida, Vout(t) = max(Vp − Vd , 0)
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
        double vd = diodo.getVd();
        double tMax = 3.0 / f;
 
        double[][] resultado = new double[3][n];
 
        if (tieneFiltro()) {
            double tau = getCarga().getResistencia() * getFiltro().getCapacitancia();
            double dt = tMax / (n - 1);
            double vc = 0.0;
 
            for (int i = 0; i < n; i++) {
                double t = i * dt;
                double vin = vp * Math.sin(2.0 * Math.PI * f * t);
                double vDisponible = vin - vd;
 
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
                resultado[0][i] = t;
                resultado[1][i] = vin;
                resultado[2][i] = Math.max(vin - vd, 0.0);
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
        params.put("Vd", diodo.getVd());
        
        return params;
    }
 
}