package modelo;

import java.io.*;

// Manejo de archivos para el funcionamiento 
public class FileHandler {
	
	private static String FILE_NAME = "files/DataBase.txt";
	private File archivo = null;
	private FileReader fr = null;
    private BufferedReader br = null;
    private String linea;
    
	public FileHandler() {
		
	}
	
	public void openFile() {
		
	}
	
	public void closeFile() {
		
	}
	
	public void readFile() {
		try {
	         // Apertura del fichero y creacion de BufferedReader para poder
	         // hacer una lectura comoda (disponer del metodo readLine()).
	         archivo = new File ("C:\\archivo.txt");
	         fr = new FileReader (archivo);
	         br = new BufferedReader(fr);

	         // Lectura del fichero
	         String linea;
	         while((linea=br.readLine())!=null)
	            System.out.println(linea);
	      }
	      catch(Exception e){
	         e.printStackTrace();
	      }finally{
	         // En el finally cerramos el fichero, para asegurarnos
	         // que se cierra tanto si todo va bien como si salta 
	         // una excepcion.
	         try{                    
	            if( null != fr ){   
	               fr.close();     
	            }                  
	         }catch (Exception e2){ 
	            e2.printStackTrace();
	         }
	      }
	}
	
	
}
