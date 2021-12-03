import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.stream.IntStream;

public class Main {
    
    static final int N= 10;      //Cantidad de ciudades
    static final int POB= 1000;     //Cantidad de individuos de la poblacion (debe ser un valor par)
    static final Double PC= 0.9; //Probabilidad de Cruce
    static final Double PM= 0.3; //Probabilidad de Mutacion
    static final int TR= 10;     //Valor para la seleccion por Torneo
    static final int CTR= POB/2; //Cantidad de Torneos
    static int aux;
    
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
        ArrayList<Integer> padres= new ArrayList<>();
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

            System.out.println("  Torneo de Padre 1");
            for(int k=0; k<index.length; k++){
                System.out.println(k+": "+index[k]);
            }
 
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

            System.out.println("  Torneo de Padre 2");
            for(int k=0; k<index.length; k++){
                System.out.println(k+": "+index[k]);
            }
        }

        System.out.println(" Lista de Padres");
        for(int i=0; i<padres.size();i++){
            System.out.println(i+": "+padres.get(i));
        }
    }

    public static void main(String[] args) throws IOException { 
        ArrayList<ArrayList<Double>> poblacion = new ArrayList<>(); //Representacion de la poblacion actual de individuos
        
        System.out.println("   Main");
        inicializacionAleatoria(poblacion);
        fitness(poblacion);
        seleccionPadres(poblacion);
    }
}