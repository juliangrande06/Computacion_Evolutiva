import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Archivo {
	private static Archivo archivo;
	private Archivo() {};
	
	public static Archivo getInstance() {
		if ( archivo== null)
			archivo= new Archivo();
		return archivo;
	}   
	
	public static ArrayList<String> read(String arch){
		ArrayList<String> datos = new ArrayList<String>();
		
		try {
			FileReader fr = new FileReader(arch);  
			BufferedReader entry= new BufferedReader(fr);
			String s = new String(); //obtengo entrada

            while((s = entry.readLine()) != null){
                String space[] = s.split(" ");

                for(int i=0; i<space.length; i++){
                    datos.add(String.valueOf(space[i]));
                }
                datos.add("\n");
            }
/*
			while ((s = entry.readLine()) != null) {
                String space[] = s.split(" ");
				for (char val : s.toCharArray()){
					datos.add(String.valueOf(val));
				}
				datos.add("/n");
			}*/
			datos.remove(datos.size()-1);
			System.out.println(datos);
			entry.close();
			
		}catch (Exception e) {
			System.out.println("Archive do not find");
		}
		datos.add(" ");
		return datos;
	}

    public void read2(String arch){
        //int[][] matrizCaminoEntreCiudades;

        try {
            FileReader fr = new FileReader(arch);  
		    BufferedReader br= new BufferedReader(fr);

			//Primera linea nos dice longitud de la matriz
			String linea = br.readLine();
			int longitud = Integer.parseInt(linea);
			Main.matrizCaminoEntreCiudades = new Double[longitud][longitud];
			//Las siguientes lineas son filas de la matriz
			linea = br.readLine();
			int fila = 0; //Para recorrer las filas de la matrizC
			while(linea != null) {
				/*
				 * Tenemos todos los enteros JUNTOS en el String linea.
				 * Con split() los SEPARAMOS en un array donde cada entero
				 * es un String individual. Con un bucle, los parseamos a Integer
				 * para guardarlos en la matriz
				 */
				String[] enteros = linea.split(" ");
				for (int i = 0; i < enteros.length; i++)
					Main.matrizCaminoEntreCiudades[fila][i] = Double.parseDouble(enteros[i]);
 
				fila++; //Incrementamos fila para la próxima línea de enteros
				linea = br.readLine(); //Leemos siguiente línea
			}
			br.close(); //Cerramos el lector de ficheros
 /*
			//Mostramos la matriz leída
			for (int i = 0; i < longitud; i++) {
				for (int j = 0; j < longitud; j++)
					System.out.print(Main.matrizCaminoEntreCiudades[i][j] + " ");
				System.out.println();
			}*/
		} catch (FileNotFoundException e) {
			System.out.println("No se encuentra archivo");
			e.printStackTrace();
		} catch (NumberFormatException e) {
			System.out.println("No se pudo convertir a Double");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Error accediendo al archivo.");
			e.printStackTrace();
		}
	}

	public void write(String codigo) throws IOException {
		File file = new File("salida.txt");
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(file));
			writer.write(codigo);
		} finally {
			if (writer != null) writer.close();
		}	
	}
	

	/*public static void main(String[] args){
		System.out.println("holaa");
		read("README.md");
	}*/
}