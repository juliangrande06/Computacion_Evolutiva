package Evolutiva;
import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.Random;
import java.util.Scanner;

public class Main {
    static int GN;                //Cantidad de Generaciones
    static int N;                   //Cantidad de ciudades
    static int POB;                //Cantidad de individuos de la poblacion
    static Double PC;              //Probabilidad de Cruce
    static Double PM;              //Probabilidad de Mutacion
    static int TR;                  //Cantidad de individuos por Torneo
    static int ST;                   //Variable de seleccion para Steady-State

    static int aux;
    static double daux;
    static ArrayList<Integer> padres= new ArrayList<>();
    static ArrayList<ArrayList<Double>> hijos= new ArrayList<>();
    static StringBuilder salida = new StringBuilder();
    public static Double matrizCaminoEntreCiudades[][];

    static int eleccionCruce;
    static int eleccionMutacion;
    

    public static void fitness(ArrayList<ArrayList<Double>> poblacion){
        Double costo=0.0;
        //System.out.println("   Funcion de Fitness");

        for(int j=0; j<POB; j++){
            ArrayList<Double> individuo = poblacion.get(j);
            //mostrarIndividuo(individuo);
            for(int i=0; i < individuo.size(); i++){       
                if(i+1 <= individuo.size()-1){
                    //System.out.println(" valor de i: "+i);
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
        salida.append("\n  Camino Planteado");
        for(int i=0; i<individuo.size(); i++){
            if(i < individuo.size()-1){
                aux= individuo.get(i).intValue();
                salida.append("\n"+i+": Ciudad "+ aux);
            }
            else{
                salida.append("\n"+i+": Ciudad "+ individuo.get(0).intValue());
            }
        }
        /*
  
        System.out.println("  Mostrando un Individuo");
        for(int i=0; i<individuo.size(); i++){
            System.out.println(i+": "+individuo.get(i));
        }*/

        //System.out.println(individuo.get(N));
    }

    public static void mostrarPoblacion(ArrayList<ArrayList<Double>> poblacion){
        //System.out.println("  Mostrando la Poblacion");
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
        int CTR= (int) POB/2;
        
        //System.out.println("   Seleccion de Padres");

        while(padres.size() < POB){
            daux= Math.random();
            
            if(daux <= PC){ //Probabilidad de Cruce implementada como probabilidad de hacer el torneo entre padres
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
        p1.add(5.0);
        p1.add(1.0);
        p1.add(2.0);
        p1.add(7.0);
        p1.add(8.0);
        p1.add(6.0);
        p1.add(0.0);
        p1.add(3.0);
        p1.add(4.0);
        p1.add(9.0);
        
        p2.add(8.0);
        p2.add(3.0);
        p2.add(0.0);
        p2.add(7.0);
        p2.add(2.0);
        p2.add(6.0);
        p2.add(1.0);
        p2.add(5.0);
        p2.add(4.0);
        p2.add(9.0);
    }

    private static void inicCiclos(double[] ciclos){
        for(int i=0; i<ciclos.length; i++){
            ciclos[i]= -1.0;
        }
    }

    public static void cruceBasadoEnCiclos(ArrayList<ArrayList<Double>> poblacion){
        ArrayList<Double> padre1 = new ArrayList<>();
        ArrayList<Double> padre2 = new ArrayList<>();
        double[] ciclos= new double[N];
        boolean rta= false;
        int pos= 0;
        int icant= 0;

        //System.out.println("   Cruce Basado en Ciclos");
        for(int i=0; i<POB;i= i+2){
            ArrayList<Double> individuo= new ArrayList<>();
            ArrayList<Double> individuo2= new ArrayList<>();
            inicCiclos(ciclos);
            inicIndividuo(individuo);
            inicIndividuo(individuo2);
            rta= false;
            pos= 0;
            aux=0;
            icant= 0;
            padre1= poblacion.get(padres.get(i));
            padre2= poblacion.get(padres.get(i+1));

            //System.out.println(" Padres antes del cruce");
            //mostrarIndividuo(padre1);
            //mostrarIndividuo(padre2);
       
            while(icant < N){
                ciclos[aux]= padre1.get(pos);
                icant++;
                individuo.set(pos, ciclos[aux]);
                daux= padre2.get(pos);
                
                for(int j=1; j<N; j++){
                    individuo2.set(pos, daux);
                    pos= padre1.indexOf(daux);
                    rta= DoubleStream.of(ciclos).anyMatch(x -> x == daux);
                    
                    if(!rta){
                        ciclos[icant]= daux;
                        icant++;
                        //System.out.println("pos: "+pos+" ... daux: "+daux);
                        individuo.set(pos, daux);
                        daux= padre2.get(pos);
                    }
                    else{
                        aux= icant;
                        j= N;
                    }
                }

                if(icant == N && individuo2.get(pos) == -1.0){
                    individuo2.set(pos, daux);
                }

                for(int j=0; j<N; j++){
                    daux= padre2.get(j);
                    rta= DoubleStream.of(ciclos).anyMatch(x -> x == daux);

                    if(!rta){
                        pos= j;
                        j= N;
                    }
                }

                if(icant < N){
                    ciclos[aux]= padre2.get(pos);
                    icant++;
                    individuo.set(pos, ciclos[aux]);
                    daux= padre1.get(pos);
                    
                    for(int j=icant; j<=N; j++){
                        individuo2.set(pos, daux);
                        pos= padre2.indexOf(daux);
                        rta= DoubleStream.of(ciclos).anyMatch(x -> x == daux);
                        
                        if(!rta){
                            ciclos[icant]= daux;
                            icant++;
                            individuo.set(pos, daux);
                            daux= padre1.get(pos);
                        }
                        else{
                            aux= icant;
                            j= N+1;
                        }
                    }

                    if(icant < N){
                        for(int j=0; j<N; j++){
                            daux= padre1.get(j);
                            rta= DoubleStream.of(ciclos).anyMatch(x -> x == daux);
        
                            if(!rta){
                                pos= j;
                                j= N;
                            }
                        }
                    }
                }
            }
            //System.out.println(" Hijos generados en el cruce");
            //mostrarIndividuo(individuo);
            //mostrarIndividuo(individuo2);

            hijos.add(individuo);
            hijos.add(individuo2);
        }

        if(POB%2 != 0){
            aux= (int) (POB * Math.random());
            hijos.add(poblacion.get(aux));
        }
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

    public static void mutacionPorInversion(){
        int puntoX;
        int puntoY;
        int diferencia;

        //System.out.println("   Mutacion por Inversion");
        for(int i=0; i<hijos.size(); i++){
            daux= Math.random();
            
            if(daux <= PM){ //Factor que determina si el hijo sera mutado
                puntoX= (int) (N * Math.random()); //Elijo PuntoX al azar
                puntoY= (int) (N * Math.random()); //Elijo distancia desde el PuntoX
        
                if(puntoX > puntoY){
                    diferencia= puntoX;
                    puntoX= puntoY;
                    puntoY= diferencia;
                }
                //System.out.println("  PuntoX: "+puntoX+" ... PuntoY: "+puntoY);
                //mostrarIndividuo(hijos.get(i));
                for(puntoX= puntoX; puntoX<puntoY; puntoX++,puntoY--){
                    daux= hijos.get(i).get(puntoX);
                    hijos.get(i).set(puntoX, hijos.get(i).get(puntoY));
                    hijos.get(i).set(puntoY, daux);
                }
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
        padres.clear();
    }

    public static void menu(ArrayList<ArrayList<Double>> poblacion){
        Scanner lectura = new Scanner (System.in);
        int rta= 0;
        salida.append("Cantidad de Ciudades (N): "+N+" \n");

        System.out.println("Prueba manual de solucion (1) o Buscar parametros de solucion particular (2): ");
        rta= lectura.nextInt();

        while(rta != 1 && rta != 2){
            System.out.println("ELECCION NO VALIDA \n");
            System.out.println("Prueba manual de solucion (1) o Buscar parametros de solucion particular (2): ");
            rta= lectura.nextInt();
        }

        if(rta == 1){
            salida.append("\n PRUEBA MANUAL DE SOLUCION \n");
            menuInicio(poblacion);
        }
        else{
            salida.append("\n BUSQUEDA DE PARAMETROS \n");
            inicio(poblacion);
        }
    }

    public static void menuInicio(ArrayList<ArrayList<Double>> poblacion){
        Scanner lectura = new Scanner (System.in);

        System.out.println("Cantidad de Generaciones (GN): ");
        GN= lectura.nextInt();
        salida.append("Cantidad de Generaciones (GN): "+GN+" \n");
        
        System.out.println("Cantidad de Individuos de la Poblacion (POB): ");
        POB= lectura.nextInt();
        salida.append("Cantidad de Individuos de la Poblacion (POB): "+POB+" \n");
        
        while(eleccionCruce != 1 && eleccionCruce != 2){
            System.out.println("Cruce en Orden (1) o Cruce basado en Ciclos (2): ");
            eleccionCruce= lectura.nextInt();

            if(eleccionCruce != 1 && eleccionCruce != 2){
                System.out.println("Opcion no valida");
            }
            else{
                System.out.println("Probabilidad de Cruce (PC) ~ Double con coma: ");
                PC= lectura.nextDouble();
                
                switch(eleccionCruce){
                    case(1):
                        salida.append("Cruce en Orden con (PC): "+PC+" \n");
                        break;
                    case(2):
                        salida.append("Cruce basado en Ciclos con (PC): "+PC+" \n");
                        break;
                }
            }
        }

        while(eleccionMutacion != 1 && eleccionMutacion != 2){
            System.out.println("Mutacion por Insercion (1) o Mutacion por Inversion (2): ");
            eleccionMutacion= lectura.nextInt();

            if(eleccionMutacion != 1 && eleccionMutacion != 2){
                System.out.println("Opcion no valida");
            }
            else{
                System.out.println("Probabilidad de Mutacion (PM) ~ Double con coma: ");
                PM= lectura.nextDouble();
                
                switch(eleccionMutacion){
                    case(1):
                        salida.append("Mutacion por Insercion con (PM): "+PM+" \n");
                        break;
                    case(2):
                        salida.append("Mutacion por Inversion con (PM): "+PM+" \n");
                        break;
                }
            }
        }

        System.out.println("Cantidad de Individuos por Torneo (TR): ");
        TR= lectura.nextInt();
        salida.append("Cantidad de Individuos por Torneo (TR): "+TR+" \n");
        
        System.out.println("Cantidad de Individuos para Steady-State (ST): ");
        ST= lectura.nextInt();
        salida.append("Cantidad de Individuos para Steady-State (ST): "+ST+" \n");

        lectura.close();
        //System.out.println(GN+" "+POB+" "+PC+" "+PM+" "+TR+" "+ST);

        double sol;
        long startTime = System.currentTimeMillis();
        inicializacionAleatoria(poblacion);
        fitness(poblacion);
        
        for(int i=0; i<GN; i++){
            //System.out.println("------- Generacion "+i+" -------");
            salida.append("\n ------- Generacion "+i+" -------");
            seleccionPadres(poblacion);
            
            if(eleccionCruce == 1){
                cruceEnOrden(poblacion);
            }
            else{
                cruceBasadoEnCiclos(poblacion);
            }

            if(eleccionMutacion == 1){
                mutacionPorInsercion();
            }
            else{
                mutacionPorInversion();
            }

            fitness(hijos);
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
    }

    public static void inicio(ArrayList<ArrayList<Double>> poblacion){
        Scanner lectura = new Scanner (System.in);
        double solucionIdeal;
        double margenError;
        int GNmax;
        int POBmin;
        int POBmax;
        int TRmin;
        int TRmax;
        int STmin;
        int STmax;
        double PCmin=0.0;
        double PCmax=0.0;
        double PMmin=0.0;
        double PMmax=0.0;

        System.out.println("Cantidad de Generaciones MAXIMAS (GN): ");
        GNmax= lectura.nextInt();
        salida.append("Cantidad de Generaciones MAXIMAS (GN): "+GNmax+" \n");

        System.out.println("Poblacion MINIMA (POBmin): ");
        POBmin= lectura.nextInt();
        System.out.println("Poblacion MAXIMA (POBmax): ");
        POBmax= lectura.nextInt();
        salida.append("Poblacion "+POBmin+" ~ "+POBmax+" \n");

        System.out.println("Solucion Ideal: ");
        solucionIdeal= lectura.nextDouble();
        salida.append("Solucion Ideal: "+solucionIdeal+" \n");

        System.out.println("Margen de Error entre soluciones: ");
        margenError= lectura.nextDouble();
        salida.append("Margen de Error entre soluciones: "+margenError+" \n");

        while(eleccionCruce != 1 && eleccionCruce != 2){
            System.out.println("Cruce en Orden (1) o Cruce basado en Ciclos (2): ");
            eleccionCruce= lectura.nextInt();

            if(eleccionCruce != 1 && eleccionCruce != 2){
                System.out.println("Opcion no valida");
            }
            else{
                System.out.println("Probabilidad de Cruce MINIMA (PCmin) ~ Double con coma: ");
                PCmin= lectura.nextDouble();
                System.out.println("Probabilidad de Cruce MAXIMA (PCmax) ~ Double con coma: ");
                PCmax= lectura.nextDouble();
                
                switch(eleccionCruce){
                    case(1):
                        salida.append("Cruce en Orden "+PCmin+" ~ "+PCmax+" \n");
                        break;
                    case(2):
                        salida.append("Cruce basado en Ciclos "+PCmin+" ~ "+PCmax+" \n");
                        break;
                }
            }
        }

        while(eleccionMutacion != 1 && eleccionMutacion != 2){
            System.out.println("Mutacion por Insercion (1) o Mutacion por Inversion (2): ");
            eleccionMutacion= lectura.nextInt();

            if(eleccionMutacion != 1 && eleccionMutacion != 2){
                System.out.println("Opcion no valida");
            }
            else{
                System.out.println("Probabilidad de Mutacion MINIMA (PM) ~ Double con coma: ");
                PMmin= lectura.nextDouble();
                System.out.println("Probabilidad de Mutacion MAXIMA (PM) ~ Double con coma: ");
                PMmax= lectura.nextDouble();
                
                switch(eleccionMutacion){
                    case(1):
                        salida.append("Mutacion por Insercion "+PMmin+" ~ "+PMmax+" \n");
                        break;
                    case(2):
                        salida.append("Mutacion por Inversion "+PMmin+" ~ "+PMmax+" \n");
                        break;
                }
            }
        }

        System.out.println("Cantidad de individuos MINIMO por Torneo (TRmin): ");
        TRmin= lectura.nextInt();
        System.out.println("Cantidad de individuos MAXIMO por Torneo (TRmax): ");
        TRmax= lectura.nextInt();
        salida.append("Cantidad de individuos por Torneo "+TRmin+" ~ "+TRmax+" \n");
        
        System.out.println("Cantidad de individuos MINIMO para Steady-State (STmin): ");
        STmin= lectura.nextInt();
        System.out.println("Cantidad de individuos MAXIMO para Steady-State (STmax): ");
        STmax= lectura.nextInt();
        salida.append("Cantidad de Individuos para Steady-State "+STmin+" ~ "+STmax+" \n");

        double solucionActual=0.0;
        ArrayList<Double> mejorS= new ArrayList<>();
        double sol=Double.POSITIVE_INFINITY;

        long startTime = System.currentTimeMillis();
        for(POB=POBmin; (solucionActual != solucionIdeal) && (Math.abs(solucionActual-solucionIdeal) > margenError) && POB<=POBmax; POB= POB+100){
            System.out.println("   En Proceso ... POB: "+POB);
            
            for(PC=PCmin; (solucionActual != solucionIdeal) && (Math.abs(solucionActual-solucionIdeal) > margenError) && PC<=PCmax; PC= PC+0.1){
                for(PM=PMmin; (solucionActual != solucionIdeal) && (Math.abs(solucionActual-solucionIdeal) > margenError) && PM<=PMmax; PM= PM+0.1){
                    System.out.println("Mutando con: "+PM);
                    for(TR=TRmin; (solucionActual != solucionIdeal) && (Math.abs(solucionActual-solucionIdeal) > margenError) && TR<=TRmax; TR++){
                        for(ST=STmin; (solucionActual != solucionIdeal) && (Math.abs(solucionActual-solucionIdeal) > margenError) && (ST<=STmax); ST++){
                            poblacion= new ArrayList<>();
                            inicializacionAleatoria(poblacion);
                            fitness(poblacion);
                            
                            for(GN=0; (solucionActual != solucionIdeal) && (Math.abs(solucionActual-solucionIdeal) > margenError) && GN<=GNmax; GN++){
                                
                                if(solucionActual != solucionIdeal && Math.abs(solucionActual-solucionIdeal) > margenError){
                                    seleccionPadres(poblacion);
                                    
                                    if(eleccionCruce == 1){
                                        cruceEnOrden(poblacion);
                                    }
                                    else{
                                        cruceBasadoEnCiclos(poblacion);
                                    }

                                    if(eleccionMutacion == 1){
                                        mutacionPorInsercion();
                                    }
                                    else{
                                        mutacionPorInversion();
                                    }

                                    fitness(hijos);
                                    sobrevivientesStady_State(poblacion);
                                    
                                    
                                    quickSort(poblacion, 0, POB-1);
                                    solucionActual= 1/poblacion.get(POB-1).get(N); //Mejor solucion de la Generacion

                                    if(solucionActual < sol){
                                        mejorS= poblacion.get(POB-1);
                                        sol= solucionActual;
                                        salida.append("\n *** Generacion: "+ GN+"\n");
                                        salida.append(" ** Mejor Solucion: "+ sol+"\n");
                                        salida.append(" * Parametros: \nPOB:"+ POB+"\nPC: "+ PC+"\nPM: "+ PM+"\nTR: "+ TR+"\nST: "+ ST+"\n");
                                    }

                                    if(solucionActual == solucionIdeal){
                                        salida.append("\n *** Se alcanzo la Solucion Ideal: "+ solucionActual+"\n");
                                    }
                                }
                            }

                        }
                    }
                }
            }
        }

        long endTime = System.currentTimeMillis();
        mostrarIndividuo(mejorS); //poblacion.get(POB-101)
        salida.append("\n\nTiempo aproximado de ejecucion " + ((endTime - startTime)/1000) + " segundos");
        //System.out.println("\n\nTiempo aproximado de ejecucion " + ((endTime - startTime)/1000) + " segundos");
    }

    public static void main(String[] args) throws IOException { 
        FileChooser file = new FileChooser();
        String dir = file.run();
        Archivo.getInstance().read(dir);
        String[] saux= dir.split("/");
        salida.append("Archivo: "+saux[saux.length-1]+"\n");

        ArrayList<ArrayList<Double>> poblacion = new ArrayList<>(); //Representacion de la poblacion actual de individuos
        menu(poblacion);

        Archivo.getInstance().write(salida.toString(), saux[saux.length-1]);
    }
}