package modelo;

import java.io.*;

// Manejo de archivos para el funcionamiento 
public class FileHandler {
	
	private String FILE_NAME = null;
	private File archivo = null;
	private FileReader fr = null;
    private BufferedReader br = null;
    private String linea;
    
	public FileHandler() {
		
	}
	
	public String openFile(String FILE_NAME) {
		try {
			this.FILE_NAME = FILE_NAME;
	         // Apertura del fichero y creacion de BufferedReader para poder
	         // hacer una lectura comoda (disponer del metodo readLine()).
			 //System.out.println("ENTRO openFile");
	         archivo = new File (FILE_NAME);
	         fr = new FileReader (archivo);
	         br = new BufferedReader(fr);
	         //System.out.println("ARCHIVO ABIERTO");
	         return "Archivo "+" \""+FILE_NAME+"\" "+ "abierto correctamente";
		}catch(Exception e) {
			//System.out.println("ERROR, NO SE ENCUENTRA EL ARCHIVO");
			e.printStackTrace();
			return "No se encuentra el Archivo "+" "+"\""+FILE_NAME+"\"";
		}
	}
	
	public String closeFile() {
		 try{                    
            if( fr != null ){   
               fr.close();   
               //System.out.println("ARCHIVO CERRADO");
               return "Archivo "+" \""+FILE_NAME+"\" "+ "cerrado correctamente";
            }else return "Se intenta cerrar archivo y aún no se ha leido completamente";                  
		  }catch (Exception e2){ 
	           e2.printStackTrace();
	           return "ERROR al cerrar el archivo "+" \""+FILE_NAME+"\"";
	        }
		 
	}
	
	public String readLine() {
		String linea = null;
		try {
			linea = br.readLine();
		}catch(Exception e) {
			linea = "ERROR al leer archivo";
		}
		return linea;
		
	}
	
	/*public void readFile() {


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
	}*/
	
	
}
