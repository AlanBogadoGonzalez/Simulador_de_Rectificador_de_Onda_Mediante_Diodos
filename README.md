# Simulador_de_Rectificador_de_Onda_Mediante_Diodos
Simula el funcionamiento de un rectificador de media onda con 1 diodo o de un rectificador de onda completa mediante puente de Graetz (4 diodos) mediante un proyecto en Java

# Descripción del problema
En electrónica analógica, la conversión de corriente alterna (AC) a corriente continua (DC) es una operación fundamental presente en prácticamente toda fuente de alimentación electrónica. Esta conversión se realiza mediante circuitos rectificadores construidos con diodos, que permiten el paso de la corriente en un solo sentido.
El problema que aborda este proyecto es el análisis y la simulación de dos tipos de circuitos rectificadores ampliamente utilizados en ingeniería:

- **Rectificador de media onda**: utiliza un único diodo para conducir únicamente durante el semiciclo positivo de la señal de entrada, bloqueando el semiciclo negativo. El resultado es una señal pulsante de frecuencia igual a la de la red.
- **Rectificador de onda completa con puente de Graetz**: utiliza cuatro diodos organizados en configuración de puente. Conduce durante ambos semiciclos de la señal de entrada, rectificando el semiciclo negativo y convirtiéndolo en positivo. El resultado es una señal que pulsa al doble de la frecuencia de la red, con un valor DC promedio mayor y un rizado menor que el rectificador de media onda.

En ambos casos, el sistema permite agregar un capacitor de filtro en paralelo con la carga. Este capacitor se carga hasta el voltaje pico disponible y se descarga lentamente entre pulsos a través de la resistencia de carga, suavizando la señal de salida. El rizado residual que permanece en la señal filtrada depende de la constante de tiempo τ = RL · C y de la frecuencia de los pulsos.

# Manual de usuario
1. Seleccionar el tipo de rectificador en el desplegable: Media onda u Onda completa.
2. Ingresar el voltaje pico de entrada Vp (por defecto 12.0 V) y la frecuencia f (por defecto 60.0 Hz).
3. Ingresar la caída de voltaje del diodo Vd (por defecto 0.7 V) y la resistencia de carga RL (por defecto 1000.0 Ω).
4. (Opcional) Activar la casilla Incluir capacitor de filtro e ingresar la capacitancia C en µF (por defecto 1000.0 µF).
5. Pulsar Simular.
6. Observar las formas de onda dibujadas en el panel derecho: Vin(t) en azul, Vout(t) en naranja y el nivel Vdc en verde.
7. Revisar los resultados numéricos en el panel inferior: Vdc, Ripple, Eficiencia, Potencia y Vd.
8. Usar Guardar para exportar la configuración en formato .dat (binario) o .txt (texto plano legible).
9. Usar Cargar para recuperar una configuración previamente guardada.
10. Pulsar Limpiar para restablecer todos los campos a sus valores por def

# Características
- Simulación de rectificador de media onda con un diodo.
- Simulación de rectificador de onda completa con puente de Graetz de cuatro diodos. En el apartado de los circuitos dejé la posibilidad de añadir nombres y voltajes individuales a los diodos para posibles mejoras o implementaciones futuras, la idea es poder mostrar el circuito al usuario.
- Filtrado opcional con capacitor de filtro — muestra el rizado real por carga y descarga exponencial del capacitor, no una señal DC plana.
- Cálculo de parámetros: Vdc, Vripple, Eficiencia, Potencia en carga y Vd, con fórmulas distintas para el caso con y sin filtro.
- Visualización gráfica de Vin(t) y Vout(t) con nivel Vdc como referencia.
- Persistencia de configuraciones en formato binario .dat (serialización de objetos) y texto plano .txt.
- Validación de entradas con mensajes de error al usuario.
- Interfaz gráfica con Swing.

## Diagrama UML
<img width="2030" height="1510" alt="UML" src="https://github.com/user-attachments/assets/3d8c811f-251f-4bb1-815b-f994b9b75f71" />

## Interfaz Gráfica
<img width="986" height="611" alt="Onda_Completa_Sin_Filtro" src="https://github.com/user-attachments/assets/1d757ffd-9c17-4266-ba11-44727306498b" />
<img width="987" height="611" alt="Onda_Completa_Con_Filtro" src="https://github.com/user-attachments/assets/6b765c92-2a92-42b8-96ed-adef5ffee364" />
<img width="983" height="611" alt="Media_Onda_Sin_Filtro" src="https://github.com/user-attachments/assets/6d075a02-a16e-4698-b1c6-9d52c08d6293" />
<img width="984" height="610" alt="Media_Onda_Con_Filtro" src="https://github.com/user-attachments/assets/c7394486-0384-43c7-a4d6-a41aa8a6ae4f" />

## Formato del archivo de configuración (.txt)

```
# Configuración del simulador de circuitos rectificadores
tipo=0
vp=12.0
frecuencia=60.0
rl=10.0
vd=0.7
filtro=true
c=1000.0
```

## Fundamento matemático

La señal de entrada es senoidal:

```
Vin(t) = Vp · sin(2π · f · t)
```

El capacitor de filtro, cuando el diodo no conduce, se descarga exponencialmente:

```
Vc(t) = Vc · e^(−Δt / τ)     donde  τ = RL · C
```

---

### Rectificador de media onda

|  | Sin filtro | Con filtro |
|---|---|---|
| **Vp.rect** | `Vp − Vd` | `Vp − Vd` |
| **Vripple** | `Vp.rect` | `Vp.rect / (f · RL · C)` |
| **Vdc** | `Vp.rect / π` | `Vp.rect − Vripple / 2` |
| **η** | `4 / π²` | `Vdc² / (Vdc² + Vripple² / 12)` |
| **P_RL** | `Vp.rect² / (4RL)` | `Vdc² / RL + Vripple² / (12RL)` |

---

### Rectificador de onda completa — Puente de Graetz

|  | Sin filtro | Con filtro |
|---|---|---|
| **Vp.rect** | `Vp − 2·Vd` | `Vp − 2·Vd` |
| **Vripple** | `Vp.rect` | `Vp.rect / (2·f · RL · C)` |
| **Vdc** | `2 · Vp.rect / π` | `Vp.rect − Vripple / 2` |
| **η** | `8 / π²` | `Vdc² / (Vdc² + Vripple² / 12)` |
| **P_RL** | `Vprect² / RL` | `Vdc² / RL + Vripple² / (12RL)` |

---


# Páginas de interés
- Para entender el funcionamiento de los rectificadores: https://youtu.be/jzibrKq4ing?si=MlmV7KyCYzfAILu7
- Para entender el funcionamiento del rectificador de media onda: https://youtu.be/164CzATS5K0?si=EdFTdzka64azdZlt
- Para entender el funcionamiento del rectificador de onda completa con puente de Graetz: https://www.electronics-tutorials.ws/diode/diode_6.html
- Para entender la constante de tiempo RC y el filtrado de rizado: https://www.electronics-tutorials.ws/rc/rc_1.html
