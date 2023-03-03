import java.math.BigInteger;
import java.lang.Thread;


/**
 * 
 */

/**
 * @author Vincent
 *
 */
public class AKS extends Thread
{

	static boolean verbose = false;
	
	BigInteger n;
	boolean n_isprime;
	BigInteger factor;
	double timeelapsed;

	/***
	 * Constructor--just save the number
	 * @param n
	 */
	public AKS(BigInteger n)
	{
		this.n = n;
	}
	
	/***
	 * Run AKS.isprime as a thread
	 */
	public void run()
	{
	  this.isPrime();
	}
	
	/***
	 * Run the AKS primality test and time it
	 * 
	 * @return true if n is prime
	 */
// 	public boolean isPrimeTimed()
//   {
//     double start = System.currentTimeMillis();
//     boolean rtn = isPrime();
//     timeelapsed = System.currentTimeMillis() - start;
//     return rtn;
//   }
	
	/***
	 * Run the AKS primality test
	 * 
	 * @return true if n is prime
	 */
	public double[] isPrime() 
	{
		// TODO: Do this in linear time http://www.ams.org/journals/mcom/1998-67-223/S0025-5718-98-00952-1/S0025-5718-98-00952-1.pdf
		// If ( n = a^b for a in natural numbers and b > 1), output COMPOSITE
		
		double[] time_result = new double[5];
		double start_time = System.nanoTime();


		BigInteger base = BigInteger.valueOf(2); // Crea un objeto BigInteger con valor 2 (se empieza la búsqueda por el 2)
		BigInteger aSquared;
		
		do
		{
			BigInteger result;

			int power = Math.max((int) (log()/log(base) - 2),1); // Dentro del bucle externo. hace el max entre el entero resultante de la división entre log() "He buscado y literalmente
			// no debería ser posible hacer eso así que no sé cuál es el parámetro que utiliza" y el logaritmo en base n de 2, a lo que resta 2.
			int comparison;
			
			do
			{
				power++; //Bucle interno, se suma uno al power, se eleva la base a ese power, y se compara con el BigInteger result "De nuevo, result no debería ser nada, puesto que no se
						 // le da valor alguno. Si n es mayor que result, comparison > 0.
				result = base.pow(power);
				comparison = n.compareTo(result);
			}
			while( comparison > 0 && power < Integer.MAX_VALUE ); 	// La condición para continuar en este bucle interno es que comparison sea positivo, y el power utilizado sea menor que
																	// el número entero más grande que admite Java 2³¹-1
			
			if( comparison == 0 ) // Si n y result son iguales, 
			{
				if (verbose) System.out.println(n + " is a perfect power of " + base); // Potencia perfecta
				factor = base; // 
				n_isprime = false;
				//return n_isprime;
			}
			
			if (verbose) System.out.println(n + " is not a perfect power of " + base); // Termina el bucle interior, n no es potencia perfecta de la base

			base = base.add(BigInteger.ONE); // suma 1 a la base
			aSquared = base.pow(2);			 // aSquared = base²
		}
		while (aSquared.compareTo(this.n) <= 0);  // Condición del bucle externo, el bucle continúa siempre y cuando el bigInteger "aSquared" sea menor o igual que n
		if (verbose) System.out.println(n + " is not a perfect power of any integer less than its square root"); // Termina el bucle externo sin solución
		
		double end_time = System.nanoTime();
		time_result[0] = end_time - start_time;
		start_time = System.nanoTime();

		// Find the smallest r such that o_r(n) > log^2 n
		// o_r(n) is the multiplicative order of n modulo r
		// the multiplicative order of n modulo r is the 
		// smallest positive integer k with	n^k = 1 (mod r).
		double log = this.log();
		double logSquared = log*log;
		BigInteger k = BigInteger.ONE;	// k = 1
		BigInteger r = BigInteger.ONE;	// r = 1
		do
		{
			r = r.add(BigInteger.ONE); // r += 1
			if (verbose) System.out.println("trying r = " + r);
			k = multiplicativeOrder(r); // El entero más pequeño tal que n^r = 1 mod r
		}
		while( k.doubleValue() < logSquared ); // Condición del bucle, si el float k es menor que el cuadrado del logaritmo, se sigue con el bucle
		if (verbose) System.out.println("r is " + r);

		end_time = System.nanoTime();
		time_result[1] = end_time - start_time;
		start_time = System.nanoTime();
		
		// If 1 < gcd(a,n) < n for some a <= r, output COMPOSITE
		for( BigInteger i = BigInteger.valueOf(2); i.compareTo(r) <= 0; i = i.add(BigInteger.ONE) ) // for (i = 2; i <= r, i++)
		{
			BigInteger gcd = n.gcd(i); // gcd(n,i)
			if (verbose) System.out.println("gcd(" + n + "," + i + ") = " + gcd);
			if ( gcd.compareTo(BigInteger.ONE) > 0 && gcd.compareTo(n) < 0 ) // Si gcd(n,i) > 1 y gcd(n,i) < n, no es primo
			{
				factor = i;
				n_isprime = false;
				//return false;
			}
		}
		
		end_time = System.nanoTime();
		time_result[2] = end_time - start_time;
		start_time = System.nanoTime();

		return time_result;
		
		// If n <= r, output PRIME
		if( n.compareTo(r) <= 0 )
		 {
		 	n_isprime = true;
		 	return true;
		}

		end_time = System.nanoTime();
		time_result[3] = end_time - start_time;
		start_time = System.nanoTime();

		
		// // For i = 1 to sqrt(totient)log(n) do
		// // if (X+i)^n <>�X^n + i (mod X^r - 1,n), output composite;

		// // sqrt(totient)log(n)
		// int limit = (int) (Math.sqrt(totient(r).doubleValue()) * this.log()); // Se hace la Euler's totient function de r (primos relativos a r en el set[1,r-1] de números naturales)
																				 // Se calcula la raíz de lo anterior, se multiplica por el logaritmo de n y se pasa a entero
		// // X^r - 1
		// Poly modPoly = new Poly(BigInteger.ONE, r.intValue()).minus(new Poly(BigInteger.ONE,0)); //
		// // X^n (mod X^r - 1, n)
		// Poly partialOutcome = new Poly(BigInteger.ONE, 1).modPow(n, modPoly, n);
		// for( int i = 1; i <= limit; i++ )
		// {
		// 	Poly polyI = new Poly(BigInteger.valueOf(i),0);
		// 	// X^n + i (mod X^r - 1, n)
		// 	Poly outcome = partialOutcome.plus(polyI);
		// 	Poly p = new Poly(BigInteger.ONE,1).plus(polyI).modPow(n, modPoly, n);
		// 	if( !outcome.equals(p) )
		// 	{
		// 		if (verbose) System.out.println( "(x+" + i + ")^" + n + " mod (x^" + r + " - 1, " + n + ") = " + outcome);
		// 		if (verbose) System.out.println( "x^" + n + " + " + i + " mod (x^" + r + " - 1, " + n + ") = " + p);
		// 		// if (verbose) System.out.println("(x+i)^" + n + " = x^" + n + " + " + i + " (mod x^" + r + " - 1, " + n + ") failed");
		// 		factor = BigInteger.valueOf(i);
		// 		n_isprime = false;
		// 		return n_isprime;
		// 	}
		// 	else
		// 		if (verbose) System.out.println("(x+" + i + ")^" + n + " = x^" + n + " + " + i + " mod (x^" + r + " - 1, " + n + ") true");
		// }
		
		// n_isprime = true;
		end_time = System.nanoTime();
		time_result[4] = end_time - start_time;
	    return time_result;
	}

	
	/***
	 * Calculate the totient of a BigInteger r
	 * Based on this algorithm:
	 * 
	 * http://community.topcoder.com/tc?module=Static&d1=tutorials&d2=primeNumbers
	 * 
	 * @param r BigInteger to calculate the totient of
	 * @return phi(r)--number of integers less than r that are coprime
	 */
    BigInteger totient(BigInteger n) 
    { 
    	BigInteger result = n; 
      
    	for( BigInteger i = BigInteger.valueOf(2); n.compareTo(i.multiply(i)) > 0; i = i.add(BigInteger.ONE) ) 
    	{ 
    		if (n.mod(i).compareTo(BigInteger.ZERO) == 0) 
    			result = result.subtract(result.divide(i));
    		
    		while (n.mod(i).compareTo(BigInteger.ZERO) == 0)
    			n = n.divide(i); 
    	}
    	
    	if (n.compareTo(BigInteger.ONE) > 0) 
    		result = result.subtract(result.divide(n));
    	
    	return result;
    	
    } 

	/***
	 * Calculate the multiplicative order of n modulo r
	 * This is defined as the smallest positive integer k 
	 * for which n^k = 1 (mod r).
	 * 
	 * @param r modulus for mutliplicative order
	 * @return multiplicative order or -1 if none exists
	 */
	BigInteger multiplicativeOrder(BigInteger r)
	{
		// TODO Consider implementing an alternative algorithm http://rosettacode.org/wiki/Multiplicative_order
		BigInteger k = BigInteger.ZERO;
		BigInteger result;
		
		do
		{
			k = k.add(BigInteger.ONE);
			result = this.n.modPow(k,r);
		}
		while( result.compareTo(BigInteger.ONE) != 0 && r.compareTo(k) > 0);
		
		if (r.compareTo(k) <= 0)
			return BigInteger.ONE.negate();
		else
		{
			if (verbose) System.out.println(n + "^" + k + " mod " + r + " = " + result);
			return k;
		}
	}
	

	// Save log n here
	double logSave = -1;

	/***
	 * 
	 * @return log base 2 of n
	 */
	double log()
	{
		if ( logSave != -1 )
			return logSave;
		
		// from http://world.std.com/~reinhold/BigNumCalcSource/BigNumCalc.java
		BigInteger b;
		
	    int temp = n.bitLength() - 1000;
	    if (temp > 0) 
	    {
	    	b=n.shiftRight(temp); 
	        logSave = (Math.log(b.doubleValue()) + temp)*Math.log(2);
	    }
	    else 
	    	logSave = (Math.log(n.doubleValue()))*Math.log(2);

	    return logSave;
	}

	
	/**
	 * log base 2 method that takes a parameter
	 * @param x
	 * @return
	 */
	double log(BigInteger x)
	{
		// from http://world.std.com/~reinhold/BigNumCalcSource/BigNumCalc.java
		BigInteger b;
		
	    int temp = x.bitLength() - 1000;
	    if (temp > 0) 
	    {
	    	b=x.shiftRight(temp); 
	        return (Math.log(b.doubleValue()) + temp)*Math.log(2);
	    }
	    else 
	    	return (Math.log(x.doubleValue())*Math.log(2));
	}
	
	public BigInteger getFactor()
	{
		return factor;
	}

  public double GetElapsedTime() {
    return timeelapsed;
  }
	
}
