import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.stream.DoubleStream;
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

		File read_file = new File(System.getProperty("user.dir"), "primes.txt");
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
				File write_file = new File(System.getProperty("user.dir"), "h1_numeros.csv");
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

//Codigo usado para generación de archivos de datos relacionados con el numero de digitos
//--------------------------------------------------------------------------------
// 		for (int digitos = 1; digitos < 20; digitos++){
// 			//System.out.println(digitos);
// 			ArrayList<Double> tiempo1 = new ArrayList<Double>();
// 			ArrayList<Double> tiempo2 = new ArrayList<Double>();
// 			ArrayList<Double> tiempo3 = new ArrayList<Double>();

// 			int bits = (int) (digitos / Math.log10(2)); // Número de bits
			
// 			for(int i = 0; i < 5 ; i++){
// 				SecureRandom random = new SecureRandom();
// 				BigInteger n = BigInteger.probablePrime((int) bits, random);
// 				//System.out.println(n);
// 				AKS obj = new AKS(n);
// 				double[] result = obj.isPrime();
// 				tiempo1.add(result[0]);
// 				tiempo2.add(result[1]);
// 				tiempo3.add(result[2]);
// 			}
// 			double media1 = calcularMedia(tiempo1);
// 			double media2 = calcularMedia(tiempo2);
// 			double media3 = calcularMedia(tiempo3);
			
// 			String s = String.format("%f;%f;%f\n", media1/1000000, media2/1000000, media3/1000000);
			
// 			//escritura en el archivo csv
// 			try{
// 				File write_file = new File(System.getProperty("user.dir"), "h1_digitos_test.csv");
// 				if (!write_file.exists()) {
// 					write_file.createNewFile();
// 				}
// 				FileWriter fw = new FileWriter(write_file, true);
// 				BufferedWriter bw = new BufferedWriter(fw);
// 				bw.write(s);
// 				bw.close();
// 			}catch (Exception e) {
// 				e.printStackTrace();
// 			}
// 		}
//--------------------------------------------------------------------------------
 	}
	public static double calcularMedia(ArrayList<Double> lista) {
		// Calcular la suma de los elementos de la lista
		double suma = 0;
        for (double valor : lista) {
            suma += valor;
        }
        return suma / lista.size();
	}
}
