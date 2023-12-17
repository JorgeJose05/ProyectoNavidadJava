package ProyectoNavidadJava.Juego.src.Juego;

//!!!---------------Notas---------------¡¡¡ 

/* 
********Investigar sobre otas funciones, metodos o lo que sea que sean mas 
        funcionales para el codigo pero eso ya a futuro
-Thread es una clase que significa cada uno de los procesos o subprocesos
del programa
-La granularidad es la cantidad de tiempo minima que puede percibir
o calcular un SO(Sistema Operativo)
***Esto se usa para la actualizacion




 */

import javax.swing.JFrame;

import java.awt.BorderLayout;
import java.awt.Canvas;
/*Importamos la clase canvas para crear unas ventanas que no gastan
 * tanto recursos como la .swing o JFrame
 */
import java.awt.Dimension;

public class juego extends Canvas implements Runnable{
    //El canvas es para las ventanas y el runnable es para que lo procesadores
    //cojan todos los main y metodos como un proceso de java y es el que te permite ejecutar los thread

    private static final long serialVersionUID = -2284879212465893870L;
    /*Esto sirve para mantenerse en la misma version de la clase Canvas */
    
    private static final int ancho =400;
    private static final int alto =340;
    /*Tamaño de la ventana */

    private static volatile boolean enFuncionamiento = false;
    /*Variable para que el programa se ejecute cuando este en funcionamiento
     * tambien le he añadido volatile para que solo puede usarse en un thread
     * a la vez porque sino cuando se detenga el programa el tread iniciar
     * segira declarando que es true i el detener que es false*/

    private static int aps =0;
    private static int fps =0;//contador de actualizaciones y frames por segundo

    private static final String nombre = "Juego";

    private static JFrame ventana;
    /*La ventana que utiliza java */

    private static Thread proceso;
    //esto es el proceso creo, y tread es una clase que indica que lo es

    private juego(){
    /*constructor de la clase */

    setPreferredSize(new Dimension(ancho, alto));
    /*Indicarle las dimensiones de la ventana que estan incializadas antes */

    ventana = new JFrame(nombre);
    /*Crear el objeto ventana */

    ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    /*esto es una operacion para que cuando se cierre la ventana se deje de ejecutar */

    ventana.setResizable(false);
    //Para que no pueda cambier el tamaño de la ventana

    ventana.setLayout(new BorderLayout());
    /*El set layout controla lo que sale por la ventana y el borderLayout es un gestor */

    ventana.add(this,BorderLayout.CENTER);
    /*hACE que el objeto ventana se coloque en el centro segun los bordes */

    ventana.pack();
    /*Para que el tamaño de las cosas se ajusten al tamaño de la ventana */

    ventana.setLocationRelativeTo(null);
    /*Establece la posicion de la ventana dentro del escritorio */

    ventana.setVisible(true);
    /*Permite que la ventana pueda verse */



    }

    public static void main(String args[]){
        juego jogo = new juego();
        jogo.iniciar();
        /*llama al metodo iniciar y lo ejecuta */


    }

    private synchronized void iniciar(){//Inicia el metodo detener y el juego en general
        enFuncionamiento = true;

    //Lo primero que se inicializa es el iniciar porque sino el resto
    // del programa nunca llega a ejecutarse porque nunca llega a ser true

        proceso = new Thread(this,"Graficos");
        /*Graficos es para identificarlo y porque creara los graficos */
        proceso.start();//todo lo que este en el run se ejecuta de manera secuencial
        
    }

    private synchronized void detener(){
        //Synchronized es para que solo se pueda ejecutar en un hilo
        // a la vez y no de los mismos problemas que con volatil

    //Lo primero que se inicializa es el detener porque sino el resto
    // del programa nunca llega a ejecutarse porque nunca llega a ser true

        enFuncionamiento = false;

        try {//Tengo que poner el try porque lo que ejecuto puede dar errores y que se quede pillado el programa
            proceso.join();
        //El join lo que hace es detener el programa pero para que las
        //variables no se corrompan primero espera que termine el thread que se esta ejecutando y luego cierra
        } catch (InterruptedException e) {
            
            e.printStackTrace();
        }
        /*el try intenta ejecutar el join y el catch (identifica una excepcion)
         * y lo que hay entre llaves nos informa por consola
        */

    }

    private void actualizar(){//es el reloj que dice cada cuanto se actualiza
        aps++;
    }

    private void mostrar(){//decide lo que se muestra por pantalla
        fps++;
    }


    public void run() {
        final int NS_POR_SEGUNDO = 1000000000;
        //equivalencia cuantos nanosegundos hay en un segundo 
        final byte APS_OBJETIVO = 60;
        //Las actualizaciones que tendra por segndo
        final double NS_POR_ACTUALIZACION = NS_POR_SEGUNDO / APS_OBJETIVO;
        //La cantidad de nanosegundos que habra por cada actualizacion

        long referenciaActualizacion = System.nanoTime();
        //Se le asignara a la variable el nanosegundo que ocurra
        long referenciaContador = System.nanoTime();


        double tiempoTranscurrido;
        double delta =0;
    //Se le llama delta a la variable por convencion en el desarrollo de videojuegos,es el tiempo
    //transcurrido desde la ultima actualizacion o bucle

        while (enFuncionamiento) {
            final long inicioBucle = System.nanoTime();
            /*Almacena el nanosegundo en el que se a iniciado el bucle actual */

            tiempoTranscurrido = inicioBucle - referenciaActualizacion;
            //Almacena cuanto ha durado el anterior bucle
            referenciaActualizacion = inicioBucle;
            //Almacena cuando se inicia este bucle

            delta += tiempoTranscurrido / NS_POR_ACTUALIZACION;
            //Delta hace de contador para dar señal de que actualize

            while (delta>=1) {
                actualizar();
                delta--;
                //a lo mejor quedan unos decimales pero no pasa nada no va a llegar a 1
            }
        

        //System.nanoTime();
        //no depende del SO sino del microprocesador y toma el ciclo del procesador */
            
            mostrar();
            //Ejecuta los dos threads mas importantes primero es deci: actualizar y mostrar
        
            if(System.nanoTime() - referenciaContador > NS_POR_SEGUNDO){
                ventana.setTitle(nombre + " || APS: " + aps + " || FPS: " + fps);
                aps=0;
                fps=0;
                referenciaContador= System.nanoTime();
            }
        }
    }

}
