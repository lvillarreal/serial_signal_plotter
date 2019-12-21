package modelo;

import java.io.*;

// Manejo de archivos para el funcionamiento 
public class FileHandler {
	
	private String FILE_NAME;
	private File archivo;
	private FileReader fr;
	private FileWriter fw;
	private PrintWriter pw;
    private BufferedReader br;
    private String linea;

    
	public FileHandler() {
		FILE_NAME = null;
		archivo = null;
		fr = null;
		pw = null;
		br = null;
		linea = null;
	}
	
	public byte openFile(String FILE_NAME) {
		try {
			this.FILE_NAME = FILE_NAME;
	        
			
			// Apertura del fichero y creacion de BufferedReader para poder
	         // hacer una lectura comoda (disponer del metodo readLine()).
			  
			 archivo = new File (FILE_NAME);
	         fr = new FileReader (archivo);
	         br = new BufferedReader(fr);
	         //System.out.println("ARCHIVO ABIERTO");
	         return InterfaceModelo.OpenFileSuccessfully;
		}catch(Exception e) {
			//System.out.println("ERROR, NO SE ENCUENTRA EL ARCHIVO");
			e.printStackTrace();
			return InterfaceModelo.OpenFileError;
		}
	}
	
	
	public byte closeFile() {
		
		 try{      
			
	            if( fr != null ){   
	            	
	               fr.close();   
	               //System.out.println("ARCHIVO CERRADO");
	               return InterfaceModelo.closeFileSuccessfully;
	            		  
	            }else if( fw != null ){   
		               fw.close();   
		               //System.out.println("ARCHIVO CERRADO");
		               return InterfaceModelo.closeFileSuccessfully;
		            		  
		            }else return 1;
				}catch (Exception e2){ 
			         e2.printStackTrace();
			         return InterfaceModelo.CloseFileError;
			    }
		 
	}
	
	public byte printLine(String line) {
		try {
			pw.println(line);
			return InterfaceModelo.printLineSuccessfully;
		}catch(Exception e) {
			return InterfaceModelo.printLineError;
		}
	}
	
	public byte createFile(String name) {
        try
        {
            fw = new FileWriter(name);
            pw = new PrintWriter(fw);

            return InterfaceModelo.CreateFileSuccessfully;
        } catch (Exception e) {
            e.printStackTrace();
            return InterfaceModelo.CreateFileError;
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
	
	public int getCantLines() {
		if (br != null) return (int)br.lines().count();
		else return InterfaceModelo.OpenFileError;
	}
	
	public void delete() {
		archivo.delete();
	}
	
}
