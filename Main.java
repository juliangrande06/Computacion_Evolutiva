import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;

public class Main {
    
    static final int N= 10;      //Cantidad de ciudades
    static final int POB= 12;     //Cantidad de individuos de la poblacion (debe ser un valor par)
    static final Double PC= 0.9; //Probabilidad de Cruce
    static final Double PM= 0.3; //Probabilidad de Mutacion
    static final int TR= 10;     //Valor para la seleccion por Torneo
    static final int CTR= POB/2; //Cantidad de Torneos
    static int aux;
    static double daux;
    static ArrayList<Integer> padres= new ArrayList<>();
    static ArrayList<ArrayList<Double>> hijos= new ArrayList<>();
    
    
    private static Double matrizCaminoEntreCiudades[][] = { //Entrada
        //Ciudad 0
        {0.0, 20.0, 15.0, 30.0, 25.0, 40.0, 30.0, 60.0, 50.0, 95.0},
        //Ciudad 1
        {20.0, 0.0, 30.0, 20.0, 15.0, 15.0, 20.0, 40.0, 40.0, 30.0},
        //Ciudad 2
        {15.0, 30.0, 0.0, 25.0, 30.0, 20.0, 30.0, 40.0, 45.0, 50.0},
        //Ciudad 3
        {30.0, 20.0, 25.0, 0.0, 20.0, 30.0, 10.0, 50.0, 40.0, 30.0},
        //Ciudad 4
        {25.0, 15.0, 30.0, 20.0, 0.0, 10.0, 20.0, 20.0, 30.0, 40.0},
        //Ciudad 5
        {40.0, 15.0, 20.0, 30.0, 10.0, 0.0, 20.0, 30.0, 20.0, 30.0},
        //Ciudad 6
        {30.0, 20.0, 30.0, 10.0, 20.0, 20.0, 0.0, 25.0, 40.0, 30.0},
        //Ciudad 7
        {60.0, 40.0, 40.0, 50.0, 20.0, 30.0, 35.0, 0.0, 30.0, 15.0},
        //Ciudad 8
        {50.0, 40.0, 45.0, 40.0, 30.0, 20.0, 40.0, 30.0, 0.0, 20.0},
        //Ciudad 9
        {95.0, 30.0, 50.0, 30.0, 40.0, 30.0, 30.0, 15.0, 20.0, 0.0}
    };

    public static void fitness(ArrayList<ArrayList<Double>> poblacion){
        Double costo=0.0;
        System.out.println("   Funcion de Fitness");

        for(int j=0; j<POB; j++){
            ArrayList<Double> individuo = poblacion.get(j);
            for(int i=0; i < individuo.size()-1; i++){
                costo += matrizCaminoEntreCiudades[individuo.get(i).intValue()][individuo.get(i+1).intValue()];
                
                if(i+1 == individuo.size()-1){
                //    System.out.println("Entre al if");
                    costo += matrizCaminoEntreCiudades[individuo.get(i+1).intValue()][individuo.get(0).intValue()];
                }
            }
            individuo.add(1/costo);
            //mostrarIndividuo(individuo);
        }
    }

    public static void mostrarIndividuo(ArrayList<Double> individuo){
        System.out.println("  Mostrando un Individuo");
        for(int i=0; i<individuo.size(); i++){
            System.out.println(i+": "+individuo.get(i));
        }
    }

    public static void inicializacionAleatoria(ArrayList<ArrayList<Double>> poblacion){
        ArrayList<Double> individuo; //Solucion particular
        int[] numeros = new int[N]; //Todos los ID de las ciudades
        int[] aux_numeros = new int[N];
        int numRestantes;       //Cantidad faltante de ciudades a insetar

        for(int i = 0; i < N; i++){
            numeros[i]= i;
        }
        System.out.println("   Inicializacion Aleatoria");

        for(int j=0; j<POB; j++){
            individuo = new ArrayList<>();
            aux_numeros = numeros;
            numRestantes = N;
            for(int i=0; i<N; i++){
                int index = (int) (numRestantes * Math.random());            
                int randNum = aux_numeros[index];
                
                numRestantes--;
                aux_numeros[index] = numeros[numRestantes];
                aux_numeros[numRestantes] = randNum;
    
                double dindex= randNum;
                individuo.add(dindex);
            }
            poblacion.add(individuo);
            //mostrarIndividuo(individuo);
        }
    }
    
    public static void seleccionPadres(ArrayList<ArrayList<Double>> poblacion){
        Boolean rta = false;
        int[] index = new int[TR];
        int pos = 0;
        int padre1 = -1;
        double max1 = 0.0;
        int padre2 = -1;
        double max2 = 0.0;
        
        System.out.println("   Seleccion de Padres");

        for(int j=0; j<CTR; j++){
            // --------- Padre 1 ---------
            rta = false;
            index = new int[TR];
            pos = 0;
            padre1 = -1;
            max1 = 0.0;

            for(int i=0; i<TR; i++){ //selecciono TR candidatos sin repetir
                aux= (int) (POB * Math.random());
                rta= IntStream.of(index).anyMatch(x -> x == aux);
                
                if(!rta){
                    if(pos != 0){
                        double sss= poblacion.get(aux).get(N);
                        int val= Double.compare(sss, max1);
                        if(val > 0){
                            padre1= aux;
                            max1= poblacion.get(aux).get(N);
                        }
                    }
                    else{
                        max1= poblacion.get(aux).get(N);
                        padre1= aux;
                    }

                    index[pos]= aux;
                    pos++;
                }
                else{
                    i= pos-1;
                }
            }
            padres.add(padre1);
/*
            System.out.println("  Torneo de Padre 1");
            for(int k=0; k<index.length; k++){
                System.out.println(k+": "+index[k]);
            }
 */
            // --------- Padre 2 ---------
            rta = false;
            index = new int[TR];
            pos = 0;
            padre2 = -1;
            max2 = 0.0;

            for(int i=0; i<TR; i++){ //selecciono TR candidatos sin repetir
                aux= (int) (POB * Math.random());
                rta= IntStream.of(index).anyMatch(x -> x == aux);
                
                if((int)aux != (int)padre1){
                    rta= IntStream.of(index).anyMatch(x -> x == aux);
                    if(!rta){
                        if(pos != 0){
                            double sss= poblacion.get(aux).get(N);
                            int val= Double.compare(sss, max2);
                            if(val > 0){
                                padre2= aux;
                                max2= poblacion.get(aux).get(N);
                            }
                        }
                        else{
                            max2= poblacion.get(aux).get(N);
                            padre2= aux;
                        }

                        index[pos]= aux;
                        pos++;
                    }
                    else{
                        i= pos-1;
                    }
                }
                else{
                    i= pos-1;
                }
            }
            padres.add(padre2);
/*
            System.out.println("  Torneo de Padre 2");
            for(int k=0; k<index.length; k++){
                System.out.println(k+": "+index[k]);
            }
*/
        }
/*
        System.out.println(" Lista de Padres");
        for(int i=0; i<padres.size();i++){
            System.out.println(i+": "+padres.get(i));
        }
*/
    }

    public static void cruceEnOrden(ArrayList<ArrayList<Double>> poblacion){
        ArrayList<Double> padre1 = new ArrayList<>();
        ArrayList<Double> padre2 = new ArrayList<>();
        ArrayList<Double> individuo;
        double[] hijito= new double[N];
        int puntoX= -1;
        int puntoY= -1;
        int diferencia= -1;
        Boolean rta= true;

        System.out.println("   Cruce en Orden");
        for(int i=0; i<padres.size();i=i+2){
            padre1= poblacion.get(padres.get(i));
            padre2= poblacion.get(padres.get(i+1));

            while(rta){ //Eleccion de puntos de Cruce
                puntoX= (int) (N * Math.random()); //Elijo PuntoX al azar
                if(puntoX != 0 && puntoX != N){
                    puntoY= (int) (N * Math.random()); //Elijo distancia desde el PuntoX
                    diferencia= puntoX -puntoY;
                    
                    if(diferencia != 0){
                        if(diferencia > 0){
                            int aux_diferencia= puntoX;
                            puntoX= puntoY;
                            puntoY= aux_diferencia; 
                        }
                        rta= false;
                    }
                }
            }

            System.out.println("  PuntoX: "+puntoX+" ... PuntoY: "+puntoY);
            System.out.println(" Padre 1");
            mostrarIndividuo(padre1);
            System.out.println(" Padre 2");
            mostrarIndividuo(padre2);

            for(int l=0; l<2; l++){
                individuo= new ArrayList<>();
                rta= false;
                int pos= puntoY+1;
                int cant= 0;

                for(int j=puntoX; j<=puntoY; j++){ //Copio en Hijo los valores entre X e Y
                    hijito[j]= padre1.get(j);
                    cant++;
                }

                for(int j=puntoY; j<N && cant<=N; j++){ //de puntoY a N
                    daux= padre2.get(j);
                    rta= DoubleStream.of(hijito).anyMatch(x -> x == daux);
                    
                    if(!rta){
                        if(pos >= N){
                            pos= 0;
                        }
                        
                        hijito[pos]= daux;
                        pos++;
                        cant++;
                        System.out.println("Cantidad es: "+cant+" ... J es: "+j);
                    }
                }

                for(int j=0; j<N && cant<=N; j++){ //de 0 a N
                    daux= padre2.get(j);
                    rta= DoubleStream.of(hijito).anyMatch(x -> x == daux);
                    System.out.println("daux: "+daux+" ... rta: "+rta);
                    if(!rta){
                        if(pos >= N){
                            pos= 0;
                        }
                        
                        hijito[pos]= daux;
                        pos++;
                        cant++;
                        System.out.println("Cantidad es: "+cant+" ... J es: "+j);
                    }
                }

                System.out.println(" Hijo "+l);
                for(int j=0; j<N; j++){
                    individuo.add(hijito[j]);
                    hijito[j]= -1.0;
                }
                mostrarIndividuo(individuo);

                hijos.add(individuo);
                padre1= padre2;
                padre2= poblacion.get(padres.get(i));
            }
        }
    }

    public static void main(String[] args) throws IOException { 
        ArrayList<ArrayList<Double>> poblacion = new ArrayList<>(); //Representacion de la poblacion actual de individuos
        
        System.out.println("   Main");
        inicializacionAleatoria(poblacion);
        fitness(poblacion);
        seleccionPadres(poblacion);
        cruceEnOrden(poblacion);
    }
}