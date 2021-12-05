import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.Random;
import java.util.Scanner;

public class Main {
    static int GN= 1000;                //Cantidad de Generaciones
    static final int N= 10;             //Cantidad de ciudades
    static int POB= 500;                //Cantidad de individuos de la poblacion
    static Double PC= 0.9;              //Probabilidad de Cruce
    static Double PM= 0.3;              //Probabilidad de Mutacion
    static int TR= 10;                  //Cantidad de individuos por Torneo
    static final int CTR= (int) POB/2;  //Cantidad de Torneos
    static int ST= 5;                   //Variable de seleccion para Steady-State

    static int aux;
    static double daux;
    static ArrayList<Integer> padres= new ArrayList<>();
    static ArrayList<ArrayList<Double>> hijos= new ArrayList<>();
    static StringBuilder salida = new StringBuilder();
    public static Double matrizCaminoEntreCiudades[][];

/*
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
*/

    public static void fitness(ArrayList<ArrayList<Double>> poblacion){
        Double costo=0.0;
        //System.out.println("   Funcion de Fitness");

        for(int j=0; j<POB; j++){
            ArrayList<Double> individuo = poblacion.get(j);
            
            for(int i=0; i < individuo.size(); i++){       
                if(i+1 <= individuo.size()-1){
                    costo += matrizCaminoEntreCiudades[individuo.get(i).intValue()][individuo.get(i+1).intValue()];
                }
                else{
                    costo += matrizCaminoEntreCiudades[individuo.get(i).intValue()][individuo.get(0).intValue()];
                }
            }
            individuo.add(1/costo);
            //mostrarIndividuo(individuo);
        }
    }

    public static void mostrarIndividuo(ArrayList<Double> individuo){
        salida.append("\n  Mostrando un Individuo");
        for(int i=0; i<individuo.size(); i++){
            salida.append("\n"+i+": "+individuo.get(i));
        }
        
/*  
        System.out.println("  Mostrando un Individuo");
        for(int i=0; i<individuo.size(); i++){
            System.out.println(i+": "+individuo.get(i));
        }
*/
        //System.out.println(individuo.get(N));
    }

    public static void mostrarPoblacion(ArrayList<ArrayList<Double>> poblacion){
        System.out.println("  Mostrando la Poblacion");
        for(int j=0; j<POB; j++){
            System.out.println(" Solucion "+j);
            for(int i=0; i<poblacion.get(j).size(); i++){
                System.out.println(i+": "+poblacion.get(j).get(i));
            }
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
        //System.out.println("   Inicializacion Aleatoria");

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
        
        //System.out.println("   Seleccion de Padres");

        for(int j=0; j<CTR; j++){
            daux= Math.random();
            
            if(daux <= PC){ //Probabilidad de Cruce implementada como probabilidad de hacer el torneo
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
            else{
                j--;
            }
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

        //System.out.println("   Cruce en Orden");
        for(int i=0; i<padres.size();i=i+2){
            padre1= poblacion.get(padres.get(i));
            padre2= poblacion.get(padres.get(i+1));

            while(rta){ //Eleccion de puntos de Cruce
                puntoX= (int) (N * Math.random()); //Elijo PuntoX al azar
                if(puntoX != 0 && puntoX != N){
                    puntoY= (int) (N * Math.random()); //Elijo distancia desde el PuntoX

                    if(puntoX > puntoY){
                        diferencia= puntoX;
                        puntoX= puntoY;
                        puntoY= diferencia;
                    }
                    rta= false;
                }
            }
/*
            System.out.println("  PuntoX: "+puntoX+" ... PuntoY: "+puntoY);
            System.out.println(" Padre 1");
            mostrarIndividuo(padre1);
            System.out.println(" Padre 2");
            mostrarIndividuo(padre2);
*/
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
                    }
                }

                for(int j=0; j<N && cant<=N; j++){ //de 0 a N
                    daux= padre2.get(j);
                    rta= DoubleStream.of(hijito).anyMatch(x -> x == daux);

                    if(!rta){
                        if(pos >= N){
                            pos= 0;
                        }
                        
                        hijito[pos]= daux;
                        pos++;
                        cant++;
                    }
                }

                for(int j=0; j<N; j++){
                    individuo.add(hijito[j]);
                    hijito[j]= -1.0;
                }

                hijos.add(individuo);
                padre1= padre2;
                padre2= poblacion.get(padres.get(i));
            }
        }

        if(POB%2 != 0){
            aux= (int) (POB * Math.random());
            hijos.add(poblacion.get(aux));
        }

        padres.clear();
    }

    public static void mutacionPorInsercion(){
        Boolean rta= true;
        int puntoX= -1;
        int puntoY= -1;
        int diferencia= -1;
        //System.out.println("   Mutacion por Insercion");

        for(int i=0; i<hijos.size(); i++){
            daux= Math.random();
            rta= true;
            
            if(daux <= PM){ //Factor que determina si el hijo sera mutado
                while(rta){ //Eleccion de puntos de Mutacion
                    puntoX= (int) (N * Math.random()); //Elijo PuntoX al azar
                    
                    if(puntoX < N){
                        puntoY= (int) (N * Math.random()); //Elijo el PuntoY al azar

                        if(puntoX > puntoY){
                            diferencia= puntoX;
                            puntoX= puntoY;
                            puntoY= diferencia;
                        }
                        rta= false;
                    }
                }
                //System.out.println("  PuntoX: "+puntoX+" ... PuntoY: "+puntoY);
                //mostrarIndividuo(hijos.get(i));
                double daux2;
                puntoX++;

                if(puntoX < puntoY){
                    daux= hijos.get(i).get(puntoX);
                    hijos.get(i).set(puntoX, hijos.get(i).get(puntoY));

                    for(int j=puntoX+1; j<=puntoY; j++){
                        daux2= hijos.get(i).get(j);
                        hijos.get(i).set(j, daux);
                        daux= daux2;
                    }
                }

                //System.out.println(" Mostrando luego de la Mutacion");
                //mostrarIndividuo(hijos.get(i));
            }
        }
    }

    public static void quickSort(ArrayList<ArrayList<Double>> poblacion, int start, int end){
        int q;

        if(start<end){
            q = partition(poblacion, start, end);
            quickSort(poblacion, start, q);
            quickSort(poblacion, q+1, end);
        }
    }

    static int nextIntInRange(int min, int max, Random rng) {
        if (min > max) {
           throw new IllegalArgumentException("Cannot draw random int from invalid range [" + min + ", " + max + "].");
        }
        int diff = max - min;
        if (diff >= 0 && diff != Integer.MAX_VALUE) {
           return (min + rng.nextInt(diff + 1));
        }
        int i;
        do {
           i = rng.nextInt();
        } while (i < min || i > max);
        return i;
     }

    static int partition(ArrayList<ArrayList<Double>> poblacion, int start, int end){
        int init = start;
        int length = end;
        ArrayList<Double> pivot;
        ArrayList<Double> temp;
        
        Random r = new Random();
        int pivotIndex = nextIntInRange(start,end,r);
        pivot = poblacion.get(pivotIndex);
                
        while(true){
            while(poblacion.get(length).get(N) > pivot.get(N) && length > start){
                length--;
            }
            
            while(poblacion.get(init).get(N) < pivot.get(N) && init < end){
                init++;
            }
            
            if(init<length){
                temp = poblacion.get(init);
                poblacion.set(init, poblacion.get(length));
                poblacion.set(length, temp);
                length--;
                init++;
            }
            else{
                return length;
            }
        } 
    }

    public static void sobrevivientesStady_State(ArrayList<ArrayList<Double>> poblacion){
        int pos=POB-1;
        //System.out.println("   Seleccion de Sobrevivientes");
        //System.out.println("  Ordenando Poblacion con QuickSort");
        quickSort(poblacion, 0, POB-1);
/*
        System.out.println("  Mostrando luego del QuickSort");
        for(int i=0; i<poblacion.size(); i++){
            System.out.println(poblacion.get(i).get(N));
        }
*/
        //System.out.println("  Ordenando Hijos con QuickSort");
        quickSort(hijos, 0, POB-1);
/*
        System.out.println("  Mostrando luego del QuickSort");
        for(int i=0; i<hijos.size(); i++){
            System.out.println(hijos.get(i).get(N));
        }
*/
        //System.out.println("  Reemplazo los "+ST+" peores Padres por Hijos");
        for(int i=0; i<ST; i++){
            //mostrarIndividuo(poblacion.get(i));
            poblacion.set(i, hijos.get(pos));
            //mostrarIndividuo(poblacion.get(i));
            pos--;
        }

        hijos.clear();
    }

    public static void menuInicio(){
        Scanner lectura = new Scanner (System.in);
        System.out.println("Cantidad de Generaciones (GN): ");
        GN= lectura.nextInt();
        System.out.println("Cantidad de Individuos de la Poblacion (POB): ");
        POB= lectura.nextInt();
        System.out.println("Probabilidad de Cruce (PC) ~ Double con coma: ");
        PC= lectura.nextDouble();
        System.out.println("Probabilidad de Mutacion (PM) ~ Double con coma: ");
        PM= lectura.nextDouble();
        System.out.println("Cantidad de Individuos por Torneo (TR): ");
        TR= lectura.nextInt();
        System.out.println("Cantidad de Individuos para Steady-State (ST): ");
        ST= lectura.nextInt();
        lectura.close();
        //System.out.println(GN+" "+POB+" "+PC+" "+PM+" "+TR+" "+ST);
    }

    public static void main(String[] args) throws IOException { 
        FileChooser file = new FileChooser();
        String dir = file.run();
        Archivo.getInstance().read(dir);

        ArrayList<ArrayList<Double>> poblacion = new ArrayList<>(); //Representacion de la poblacion actual de individuos
        double sol;
        menuInicio();
        long startTime = System.currentTimeMillis();
        inicializacionAleatoria(poblacion);
        fitness(poblacion);
        
        for(int i=0; i<GN; i++){
            //System.out.println("------- Generacion "+i+" -------");
            salida.append("\n ------- Generacion "+i+" -------");
            seleccionPadres(poblacion);
            cruceEnOrden(poblacion);
            mutacionPorInsercion();
            fitness(hijos);
            seleccionPadres(poblacion);
            sobrevivientesStady_State(poblacion);

            //Muesto la mejor solucion de la Generacion
            quickSort(poblacion, 0, POB-1);
            sol= 1/poblacion.get(POB-1).get(N);
            salida.append("\n *** Mejor Solucion: "+ sol+"\n");
            //System.out.println("\n *** Mejor Solucion: "+ sol);
        }

        long endTime = System.currentTimeMillis();
        mostrarIndividuo(poblacion.get(POB-1));
        salida.append("\n\nTiempo aproximado de ejecucion " + ((endTime - startTime)/1000) + " segundos");
        //System.out.println("\n\nTiempo aproximado de ejecucion " + ((endTime - startTime)/1000) + " segundos");

        Archivo.getInstance().write(salida.toString());

    }
}

/*
Consultas:
* La probabilidad de cruce va en la seleccion de padres o en el algoritmo de cruce?
    Si va en el algoritmo de cruce no voy a generar 100 hijos, sino menos.
    Si va en la seleccion de padres puede que ese torneo se descarte por antidoping (elegi este)

Cosas por hacer:
* Crear interfaz de menu para solicitar los valores de las variables generales (hecho)
* Guardar la info en un archivo (hecho)
* Llevar un cronometro de tiempo de ejecucion (hecho)
* Leer valores de costos desde archivo externo (hecho)
* Crear ejecutable
* README
* Implementar otros algoritmos?
*/