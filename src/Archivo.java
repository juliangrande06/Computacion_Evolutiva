import java.io.*;

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
			String linea = br.readLine(); //Primera linea nos dice longitud de la matriz
			int longitud = Integer.parseInt(linea);
			Main.matrizCaminoEntreCiudades = new Double[longitud][longitud];
			
			linea = br.readLine(); //Las siguientes lineas son filas de la matriz
			int fila = 0;
			while(linea != null) {
				String[] enteros = linea.split(" ");
				
                for (int i = 0; i < enteros.length; i++){
					Main.matrizCaminoEntreCiudades[fila][i] = Double.parseDouble(enteros[i]);
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

}