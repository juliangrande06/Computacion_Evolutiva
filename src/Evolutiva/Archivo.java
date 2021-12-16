package Evolutiva;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Archivo {
	private static Archivo archivo;
	private Archivo() {};
	
	public static Archivo getInstance() {
		if ( archivo== null)
			archivo= new Archivo();
		return archivo;
	}   
	
    public void read(String arch){
        try {
            FileReader fr = new FileReader(arch);  
		    BufferedReader br= new BufferedReader(fr);
			int longitud=0;
			String linea = br.readLine(); //Leo primera linea del archivo
			String[] cabecera = linea.split(": ");

			while(!cabecera[0].equals("DIMENSION")){
				linea = br.readLine();
				cabecera = linea.split(": ");
			}

			if(cabecera[0].equals("DIMENSION")){
				cabecera = cabecera[1].split(" ");
				for(int i=0; i<cabecera.length; i++){
					if(!cabecera[i].equals(" ") && !cabecera[i].equals("")){
						longitud = Integer.parseInt(cabecera[i]);
						Main.N= longitud;
					}
				}
			}

			while(!cabecera[0].equals("EDGE_WEIGHT_SECTION")){
				linea = br.readLine();
				cabecera = linea.split(": ");
			}

			Main.matrizCaminoEntreCiudades = new Double[longitud][longitud];
			
			linea = br.readLine(); //Las siguientes lineas son filas de la matriz
			int fila = 0;
			while(linea != null && !linea.equals("EOF")) {
				String[] enteros = linea.split(" ");

				int j=0;
                for (int i = 0; i < enteros.length; i++){
					if(!enteros[i].equals(" ") && !enteros[i].equals("")){
						Main.matrizCaminoEntreCiudades[fila][j] = Double.parseDouble(enteros[i]);
						j++;
					}
                }
				
                fila++;
				linea = br.readLine(); //Leemos siguiente línea
			}
			br.close(); //Cierro el lector de ficheros
/*
			//Muesta la matriz leída
			for (int i = 0; i < longitud; i++) {
				for (int j = 0; j < longitud; j++)
					System.out.print(Main.matrizCaminoEntreCiudades[i][j] + " ");
				System.out.println();
			}
*/
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

	public void write(String codigo, String name) throws IOException {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss");

		File file = new File(name+"_salida_"+dtf.format(LocalDateTime.now())+".txt");
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(file));
			writer.write(codigo);
		} finally {
			if (writer != null) writer.close();
		}	
	}

}