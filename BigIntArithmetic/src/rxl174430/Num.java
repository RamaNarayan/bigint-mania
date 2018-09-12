package rxl174430;

public class Num  implements Comparable<Num> {

    static long defaultBase = 100;  // Change as needed
    long base = defaultBase;  // Change as needed
    long[] arr;  // array to store arbitrarily large integers
    boolean isNegative;  // boolean flag to represent negative numbers
    int len;  // actual number of elements of array that are used;  number is stored in arr[0..len-1]

    public Num(String s) {
    	long arrLength = 18 - Long.toString(defaultBase).length();
    	arr = new long[(int)(s.length()/arrLength + (s.length()%arrLength == 0? 0 : 1))];
    	System.out.println(arrLength+" "+arr.length );
    	
    	recursive(s, 0);
    }

    public void recursive(String quotient, int index) {
    	if (quotient.length() == 0)
    		return;
    	long[] arrTemp;
    	long arrLength = 18 - Long.toString(defaultBase).length();
    	arrTemp = new long[(int)(quotient.length()/arrLength + (quotient.length()%arrLength == 0? 0 : 1))];
    	String temp;
    	long remainder = 0;
    	String quotientString = "";
    	System.out.println(arrLength+" "+arrTemp.length );
    	for(long i=0 ; i<arrTemp.length; i = i + 1) {
    		System.out.println("i-"+i );
    		if(remainder != 0) {
    			temp = Long.toString(remainder).concat(quotient.substring((int)(i*arrLength), (int)(i*arrLength + arrLength < quotient.length()?i*arrLength + arrLength:quotient.length())));
    			arrTemp[(int)i] = Long.valueOf(temp);
    		}
    		else
    			arrTemp[(int)i] = Long.valueOf(quotient.substring((int)i, (int)i + (int)arrLength));
    		
    		remainder = arrTemp[(int)i] % defaultBase;
    		quotientString = quotientString.concat(Long.toString((arrTemp[(int)i] / defaultBase)));
    		
    	}
    	System.out.println("quotient string-"+quotientString);
    	arr[index] = remainder;
    	recursive(quotientString, index + 1);
    }

    public Num(long x) {
    	int i = 0;
    	long temp = x;
    	while(temp>0) {
    		temp = temp/base;
    		i = i + 1;
    	}
    	len = i;
    	arr = new long[len];
    	i = 0;
    	while(x>0) {
    		arr[i] = x % base;
    		x = x/base;
    		i = i + 1;
    	}
    	System.out.println(len);
    }

    public static Num add(Num a, Num b) {
    	
    	long length_a = a.arr.length;
    	long length_b = b.arr.length;
    	long carry = 0;
    	long[] sum = new long[(int)(length_a<length_b?length_b:length_a)+1];
    	long i;
    	for (i = 0; i != length_a && i != length_b; i++) {
    		
    		sum[(int)i] = (a.arr[(int)i] + b.arr[(int)i] + carry)%defaultBase;
    				
     		carry = (a.arr[(int)i] + b.arr[(int)i] + carry)/defaultBase;
    		
    	}
    	
    	while(i < length_a) {
    		sum[(int)i] = (a.arr[(int)i]  + carry)%defaultBase;
    		carry = (a.arr[(int)i] + carry)/defaultBase;
    		i++;
    	}
    	
    	while(i < length_b) {
    		sum[(int)i] = (b.arr[(int)i] + carry)%defaultBase;
    		carry = (b.arr[(int)i] + carry)/defaultBase;
    		i++;
    	}
    	System.out.println(i+" "+carry+" "+sum.length);
    	if(carry != 0)
    		sum[(int)i] = carry%defaultBase;
    	for(i = sum.length-1; i>=0; i--) {
    		if(carry == 0)
    			continue;
    		System.out.print(sum[(int)i]+" ");
    	}
    	
    	System.out.println("hello4");
    	
    	return null;
    }

    public static Num subtract(Num a, Num b) {
	return null;
    }

    public static Num product(Num a, Num b) {
	return null;
    }

    // Use divide and conquer
    public static Num power(Num a, long n) {
	return null;
    }

    // Use binary search to calculate a/b
    public static Num divide(Num a, Num b) {
	return null;
    }

    // return a%b
    public static Num mod(Num a, Num b) {
	return null;
    }

    // Use binary search
    public static Num squareRoot(Num a) {
	return null;
    }


    // Utility functions
    // compare "this" to "other": return +1 if this is greater, 0 if equal, -1 otherwise
    public int compareTo(Num other) {
	return 0;
    }
    
    // Output using the format "base: elements of list ..."
    // For example, if base=100, and the number stored corresponds to 10965,
    // then the output is "100: 65 9 1"
    public void printList() {
    	System.out.print(base()+": ");
    	for(int i = 0;i<arr.length;i++) {
    		System.out.print(arr[i] + " ");
    	}
    }
    
    // Return number to a string in base 10
    public String toString() {
	return null;
    }

    public long base() { return base; }

    // Return number equal to "this" number, in base=newBase
    public Num convertBase(int newBase) {
	return null;
    }

    // Divide by 2, for using in binary search
    public Num by2() {
	return null;
    }

    // Evaluate an expression in postfix and return resulting number
    // Each string is one of: "*", "+", "-", "/", "%", "^", "0", or
    // a number: [1-9][0-9]*.  There is no unary minus operator.
    public static Num evaluatePostfix(String[] expr) {
	return null;
    }

    // Evaluate an expression in infix and return resulting number
    // Each string is one of: "*", "+", "-", "/", "%", "^", "(", ")", "0", or
    // a number: [1-9][0-9]*.  There is no unary minus operator.
    public static Num evaluateInfix(String[] expr) {
	return null;
    }


    public static void main(String[] args) {
    //	long num = 10965;
	//Num x = new Num(num);
	Num y = new Num("91312234234233123234954944944959");
	//x.printList();
	//Num y = new Num("8");
	//Num z = Num.add(x, y);
	//System.out.println(z);
	//Num a = Num.power(x, 8);
	//System.out.println(a);
	//if(z != null) z.printList();
    }
}