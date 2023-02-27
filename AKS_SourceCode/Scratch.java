import java.math.BigInteger;
import java.security.SecureRandom;
import java.io.File;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;



public class Scratch {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		//Lectura del txt con los números a procesar
		// Se cargan todos los valores en un array de tamaño variable

		File read_file = new File(System.getProperty("user.dir"), "primos_grandes.txt");
		String cadena;
		List<BigInteger> number_array = new ArrayList<BigInteger>();

		try{
			FileReader fr = new FileReader(read_file);
			BufferedReader br = new BufferedReader(fr);
			while((cadena = br.readLine()) != null){
				number_array.add(new BigInteger(cadena));
			}
			br.close();
		}catch (Exception e) {
            e.printStackTrace();
        }	
		
		for (int i = 0; i < number_array.size(); i++){
			// lectura del número
			BigInteger n = number_array.get(i); 
			
			//inicializar el objeto AKS y ejecutar función
			AKS obj = new AKS(n);
			double[] result = obj.isPrime();
			String s = String.format("%d;%f;%f;%f\n", n, result[0]/1000000, result[1]/1000000, result[2]/1000000);
			
			//escritura en el archivo csv
			try{
				File write_file = new File(System.getProperty("user.dir"), "h1_digitos.csv");
				if (!write_file.exists()) {
					write_file.createNewFile();
				}
				FileWriter fw = new FileWriter(write_file, true);
				BufferedWriter bw = new BufferedWriter(fw);
				bw.write(s);
				bw.close();
			}catch (Exception e) {
				e.printStackTrace();
			}
		}	
	}
}
