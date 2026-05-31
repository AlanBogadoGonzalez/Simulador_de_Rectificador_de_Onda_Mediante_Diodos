
package SimuladorRectificadorDeOnda;

public abstract class ComponenteElectrico {
    // Atributos
    private String nombre;
    
    // Constructor
    public ComponenteElectrico(String nombre) {
        setNombre(nombre);
    }

    // Getters y Setters
    public String getNombre() {
        return nombre;
    }

    public final void setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del componente no puede ser nulo o vacío.");
        }
        this.nombre = nombre;
    }


    @Override
    public String toString() {
        return getClass().getSimpleName() + " [" + nombre + "]";
    }
}