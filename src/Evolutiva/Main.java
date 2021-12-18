package Evolutiva;
import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.Random;
import java.util.Scanner;

public class Main {
    static int GN= 1;                //Cantidad de Generaciones
    static int N= 10;                   //Cantidad de ciudades
    static int POB= 10;                //Cantidad de individuos de la poblacion 1000
    static Double PC= 0.9;              //Probabilidad de Cruce
    static Double PM= 0.3;              //Probabilidad de Mutacion
    static int TR= 4;                  //Cantidad de individuos por Torneo
    static final int CTR= (int) POB/2;  //Cantidad de Torneos
    static int ST= 2;                   //Variable de seleccion para Steady-State

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
        System.out.println("   Funcion de Fitness");

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
        /*salida.append("\n  Camino Planteado");
        for(int i=0; i<individuo.size(); i++){
            if(i < individuo.size()-1){
                aux= individuo.get(i).intValue();
                salida.append("\n"+i+": Ciudad "+ aux);
            }
            else{
                salida.append("\n"+i+": Ciudad "+ individuo.get(0).intValue());
            }
        }*/
        
  
        System.out.println("  Mostrando un Individuo");
        for(int i=0; i<individuo.size(); i++){
            System.out.println(i+": "+individuo.get(i));
        }

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

    private static void inicIndividuo(ArrayList<Double> individuo){
        for(int i= 0; i < N; i++){
            individuo.add(-1.0);
        }
    }

    private static void inicPadres(ArrayList<Double> p1, ArrayList<Double> p2){
        p1.add(0.0);
        p1.add(1.0);
        p1.add(2.0);
        p1.add(3.0);
        p1.add(4.0);
        p1.add(5.0);
        p1.add(6.0);
        p1.add(7.0);
        p1.add(8.0);
        
        p2.add(8.0);
        p2.add(2.0);
        p2.add(6.0);
        p2.add(7.0);
        p2.add(1.0);
        p2.add(5.0);
        p2.add(4.1);
        p2.add(0.0);
        p2.add(3.0);
    }

    public static void cruceBasadoEnCiclos(ArrayList<ArrayList<Double>> poblacion){
        ArrayList<Double> padre1 = new ArrayList<>();
        ArrayList<Double> padre2 = new ArrayList<>();
        ArrayList<Double> individuo= new ArrayList<>();
        ArrayList<Double> individuo2= new ArrayList<>();
        inicIndividuo(individuo);
        inicIndividuo(individuo2);
        //inicPadres(padre1, padre2);
        double[] ciclos;
        boolean rta= false;
        int pos= 0;
        int icant= 0;

        System.out.println("   Cruce Basado en Ciclos");
        for(int i=0; i<padres.size();i=i+2){
            ciclos= new double[N];
            rta= false;
            pos= 0;
            icant= 0;
            padre1= poblacion.get(padres.get(i));
            padre2= poblacion.get(padres.get(i+1));
            mostrarIndividuo(padre1);
            mostrarIndividuo(padre2);
            
            while(icant < N){// 0< 10
                ciclos[pos]= padre1.get(pos); //9
                icant++; //1
                individuo.set(pos, ciclos[pos]); // 9
                daux= padre2.get(pos); //4
                rta= DoubleStream.of(ciclos).anyMatch(x -> x == daux); //false
                
                for(int j=1; j<N; j++){ //j=2 < 10
                    individuo2.set(pos, daux); // 4 -1-1-1 8 -1-1-1-1-1
                    pos= (int)daux; //8
                    daux= padre1.get(pos); //5
                    
                    if(!rta){ //true
                        ciclos[j]= daux;//9 6 5
                        icant++; //3
                        individuo.set(pos, daux);// 9 -1-1-1 6 -1-1-1 5 -1
                        daux= padre2.get(pos);// 3
                        rta= DoubleStream.of(ciclos).anyMatch(x -> x == daux); //false
                    }
                    else{
                        j= N;
                    }
                }
                
                for(int j=1; j<N; j++){
                    daux= padre2.get(j);
                    rta= DoubleStream.of(ciclos).anyMatch(x -> x == daux);

                    if(!rta){
                        pos= j;
                        j= N;
                    }
                }

                ciclos[pos]= padre2.get(pos);
                icant++;
                individuo.set(pos, ciclos[pos]);
                daux= padre1.get(pos);
                rta= DoubleStream.of(ciclos).anyMatch(x -> x == daux);
                
                for(int j=1; j<N; j= j+2){
                    individuo2.set(pos, daux);
                    pos= (int)daux;
                    daux= padre2.get(pos);
                    
                    if(!rta){
                        ciclos[j]= daux;
                        icant++;
                        individuo.set(pos, daux);
                        pos= (int)daux;
                        daux= padre1.get(pos);
                        rta= DoubleStream.of(ciclos).anyMatch(x -> x == daux);
                    }
                    else{
                        j= N;
                    }
                }
            }
            mostrarIndividuo(individuo);
            mostrarIndividuo(individuo2);
        }
        hijos.add(individuo);
        hijos.add(individuo2);
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
        salida.append("Cantidad de Ciudades (N): "+N+" \n");

        System.out.println("Cantidad de Generaciones (GN): ");
        GN= lectura.nextInt();
        salida.append("Cantidad de Generaciones (GN): "+GN+" \n");
        
        System.out.println("Cantidad de Individuos de la Poblacion (POB): ");
        POB= lectura.nextInt();
        salida.append("Cantidad de Individuos de la Poblacion (POB): "+POB+" \n");
        
        System.out.println("Probabilidad de Cruce (PC) ~ Double con coma: ");
        PC= lectura.nextDouble();
        salida.append("Probabilidad de Cruce (PC) ~ Double con coma: "+PC+" \n");

        System.out.println("Probabilidad de Mutacion (PM) ~ Double con coma: ");
        PM= lectura.nextDouble();
        salida.append("Probabilidad de Mutacion (PM) ~ Double con coma: "+PM+" \n");

        System.out.println("Cantidad de Individuos por Torneo (TR): ");
        TR= lectura.nextInt();
        salida.append("Cantidad de Individuos por Torneo (TR): "+TR+" \n");
        
        System.out.println("Cantidad de Individuos para Steady-State (ST): ");
        ST= lectura.nextInt();
        salida.append("Cantidad de Individuos para Steady-State (ST): "+ST+" \n");

        lectura.close();
        //System.out.println(GN+" "+POB+" "+PC+" "+PM+" "+TR+" "+ST);
    }

    public static void main(String[] args) throws IOException { 
        FileChooser file = new FileChooser();
        String dir = file.run();
        Archivo.getInstance().read(dir);
        String[] saux= dir.split("/");
        salida.append("Archivo: "+saux[saux.length-1]+"\n");

        ArrayList<ArrayList<Double>> poblacion = new ArrayList<>(); //Representacion de la poblacion actual de individuos
        double sol;
        //menuInicio();
        long startTime = System.currentTimeMillis();
        inicializacionAleatoria(poblacion);
        fitness(poblacion);
        
        for(int i=0; i<GN; i++){
            System.out.println("------- Generacion "+i+" -------");
            //salida.append("\n ------- Generacion "+i+" -------");
            seleccionPadres(poblacion);
            cruceBasadoEnCiclos(poblacion);
            /*cruceEnOrden(poblacion);
            mutacionPorInsercion();
            fitness(hijos);
            seleccionPadres(poblacion);
            sobrevivientesStady_State(poblacion);

            //Muesto la mejor solucion de la Generacion
            quickSort(poblacion, 0, POB-1);
            sol= 1/poblacion.get(POB-1).get(N);
            salida.append("\n *** Mejor Solucion: "+ sol+"\n");
            //System.out.println("\n *** Mejor Solucion: "+ sol);*/
        }
/*
        long endTime = System.currentTimeMillis();
        mostrarIndividuo(poblacion.get(POB-1));
        salida.append("\n\nTiempo aproximado de ejecucion " + ((endTime - startTime)/1000) + " segundos");
        //System.out.println("\n\nTiempo aproximado de ejecucion " + ((endTime - startTime)/1000) + " segundos");

        Archivo.getInstance().write(salida.toString(), saux[saux.length-1]);
*/
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