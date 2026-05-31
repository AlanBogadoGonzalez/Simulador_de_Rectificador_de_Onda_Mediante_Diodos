# Simulador_de_Rectificador_de_Onda_Mediante_Diodos
Simula el funcionamiento de un rectificador de media onda con 1 diodo o de un rectificador de onda completa mediante puente de Graetz (4 diodos) mediante un proyecto en Java

# Descripción del problema
En electrónica analógica, la conversión de corriente alterna (AC) a corriente continua (DC) es una operación fundamental presente en prácticamente toda fuente de alimentación electrónica. Esta conversión se realiza mediante circuitos rectificadores construidos con diodos, que permiten el paso de la corriente en un solo sentido.
El problema que aborda este proyecto es el análisis y la simulación de dos tipos de circuitos rectificadores ampliamente utilizados en ingeniería:
Rectificador de media onda — utiliza un único diodo para conducir únicamente durante el semiciclo positivo de la señal de entrada, bloqueando el semiciclo negativo. El resultado es una señal pulsante de frecuencia igual a la de la red.
Rectificador de onda completa con puente de Graetz — utiliza cuatro diodos organizados en configuración de puente. Conduce durante ambos semiciclos de la señal de entrada, rectificando el semiciclo negativo y convirtiéndolo en positivo. El resultado es una señal que pulsa al doble de la frecuencia de la red, con un valor DC promedio mayor y un rizado menor que el rectificador de media onda.
En ambos casos, el sistema permite agregar un capacitor de filtro en paralelo con la carga. Este capacitor se carga hasta el voltaje pico disponible y se descarga lentamente entre pulsos a través de la resistencia de carga, suavizando la señal de salida. El rizado residual que permanece en la señal filtrada depende de la constante de tiempo τ = RL · C y de la frecuencia de los pulsos.

# Manual de usuario
1- Seleccionar el tipo de rectificador: Media onda u Onda completa.
2- Ingresar los parámetros del circuito: voltaje pico Vp, frecuencia f, caída del diodo Vd y resistencia de carga RL.
3- Opcionalmente activar el capacitor de filtro e ingresar su capacitancia en µF.
4- Presionar Simular para obtener la gráfica de formas de onda y los resultados numéricos.
5- Usar Guardar para exportar la configuración en formato .dat (binario) o .txt (texto plano).
6- Usar Cargar para recuperar una configuración previamente guardada.
