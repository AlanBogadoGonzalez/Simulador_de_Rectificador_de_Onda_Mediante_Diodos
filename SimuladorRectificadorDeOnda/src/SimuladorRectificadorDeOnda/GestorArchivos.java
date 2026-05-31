
package SimuladorRectificadorDeOnda;

import java.io.*;
import java.util.LinkedHashMap;
import java.util.Map;

public class GestorArchivos {
 
    // Atributos
    private String rutaArchivo;
 
    // Constructores
    public GestorArchivos() {
        this.rutaArchivo = "circuito.dat";
    }
 
    public GestorArchivos(String rutaArchivo) {
        setRutaArchivo(rutaArchivo);
    }
 
    // Getters y Setters
    public String getRutaArchivo() {
        return rutaArchivo;
    }
 
    public void setRutaArchivo(String rutaArchivo) {
        if (rutaArchivo == null || rutaArchivo.trim().isEmpty())
            throw new IllegalArgumentException("La ruta no puede ser nula o vacía.");
        this.rutaArchivo = rutaArchivo;
    }

    // Métodos
    
    // para binario
    public void guardar(CircuitoRectificador circuito) throws IOException {
        if (circuito == null) throw new IllegalArgumentException("El circuito no puede ser nulo.");
 
        try (ObjectOutputStream ARCHIVO = new ObjectOutputStream(new FileOutputStream(rutaArchivo))) {ARCHIVO.writeObject(circuito);}
    }
 
    public CircuitoRectificador cargar() throws IOException, ClassNotFoundException {
        try (ObjectInputStream ARCHIVO = new ObjectInputStream(new FileInputStream(rutaArchivo))) {
            return (CircuitoRectificador) ARCHIVO.readObject();
        }
    }
 
    
    // para texto plano
    public void exportarTxt(String ruta, Map<String, String> valores) throws IOException {
        if (ruta == null || ruta.trim().isEmpty())
            throw new IllegalArgumentException("La ruta no puede ser nula o vacía.");
        if (valores == null || valores.isEmpty())
            throw new IllegalArgumentException("Los valores no pueden ser nulos o vacíos.");
 
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ruta))) {
            bw.write("# Configuración del simulador de circuitos rectificadores");
            bw.newLine();
            for (Map.Entry<String, String> entrada : valores.entrySet()) {
                bw.write(entrada.getKey() + "=" + entrada.getValue());
                bw.newLine();
            }
        }
    }
 
    public Map<String, String> importarTxt(String ruta) throws IOException {
        if (ruta == null || ruta.trim().isEmpty())
            throw new IllegalArgumentException("La ruta no puede ser nula o vacía.");
 
        Map<String, String> valores = new LinkedHashMap<>();
 
        try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                linea = linea.trim();
                if (linea.isEmpty() || linea.startsWith("#")) continue;
 
                String[] partes = linea.split("=", 2);
                if (partes.length == 2) {
                    valores.put(partes[0].trim(), partes[1].trim());
                }
            }
        }
        return valores;
    }
 
    public CircuitoRectificador reconstruirCircuito(Map<String, String> valores) {
        try {
            int tipo = Integer.parseInt(valores.get("tipo"));
            double vp = Double.parseDouble(valores.get("vp"));
            double f = Double.parseDouble(valores.get("frecuencia"));
            double rl = Double.parseDouble(valores.get("rl"));
            double vd = Double.parseDouble(valores.get("vd"));
            boolean tieneFiltro = Boolean.parseBoolean(valores.get("filtro"));
 
            Resistencia carga = new Resistencia("RL", rl);
            Diodo diodo = new Diodo("D1", vd);
 
            Capacitor filtro = null;
            if (tieneFiltro) {
                double c = Double.parseDouble(valores.get("c"));
                filtro = new Capacitor("C1", c);
            }
 
            if (tipo == 1) {
                Diodo[] puente = { new Diodo("D1", vd), new Diodo("D2", vd), new Diodo("D3", vd), new Diodo("D4", vd) };
                return filtro != null ? new RectificadorOndaCompleta(f, vp, carga, filtro, puente) : new RectificadorOndaCompleta(f, vp, carga, puente);
            } else {
                return filtro != null ? new RectificadorMediaOnda(f, vp, carga, diodo, filtro) : new RectificadorMediaOnda(f, vp, carga, diodo);
            }
        } catch (NumberFormatException | NullPointerException e) {
            throw new IllegalArgumentException("El archivo de configuración tiene un formato inválido: " + e.getMessage());
        }
    }
}
