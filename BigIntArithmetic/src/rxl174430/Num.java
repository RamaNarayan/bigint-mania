package rxl174430;

public class Num implements Comparable<Num> {

	static long defaultBase = 10; // Change as needed
	long base = defaultBase; // Change as needed
	long[] arr; // array to store arbitrarily large integers
	boolean isNegative; // boolean flag to represent negative numbers
	int len; // actual number of elements of array that are used; number is stored in arr[0..len-1]

	public Num(String s) {
		long arrLength = 18 - Long.toString(defaultBase).length();
		arr = new long[(int) ((s.length() / arrLength) + ((s.length() % arrLength) == 0 ? 0 : 1))];
		System.out.println(arrLength + " " + arr.length);

		recursive(s, 0);
	}
	
	private Num(long[] arr,int size,boolean isNegative) {
		this.arr = arr;
		this.len = size;
		this.isNegative = isNegative;
	}

	public void recursive(String quotient, int index) {
		if (quotient.length() == 0) {
			return;
		}
		long[] arrTemp;
		long arrLength = 18 - Long.toString(defaultBase).length();
		arrTemp = new long[(int) ((quotient.length() / arrLength) + ((quotient.length() % arrLength) == 0 ? 0 : 1))];
		String temp;
		long remainder = 0;
		String quotientString = "";
		System.out.println(arrLength + " " + arrTemp.length);
		for (long i = 0; i < arrTemp.length; i = i + 1) {
			System.out.println("i-" + i);
			if (remainder != 0) {
				temp = Long.toString(remainder)
						.concat(quotient.substring((int) (i * arrLength), (int) (((i * arrLength) + arrLength) < quotient.length() ? (i * arrLength) + arrLength : quotient.length())));
				arrTemp[(int) i] = Long.valueOf(temp);
			} else {
				arrTemp[(int) i] = Long.valueOf(quotient.substring((int) i, (int) i + (int) arrLength));
			}

			remainder = arrTemp[(int) i] % defaultBase;
			quotientString = quotientString.concat(Long.toString((arrTemp[(int) i] / defaultBase)));

		}
		System.out.println("quotient string-" + quotientString);
		arr[index] = remainder;
		recursive(quotientString, index + 1);
	}

	public Num(long x) {
		this.isNegative = x < 0 ? true : false;
		int i = 0;
		x = Math.abs(x); //will fail in Long.MIN_VALUE. We should convert the value to a different base to workaround
		long temp = x;
		while (temp > 0) {
			temp = temp / base;
			i = i + 1;
		}
		len = i;
		arr = new long[len];
		i = 0;
		while (x > 0) {
			arr[i] = x % base;
			x = x / base;
			i = i + 1;
		}
	}

	
	public static Num add(Num a,Num b) {
		boolean isNegative;
		if(a.isNegative&&b.isNegative || !a.isNegative&&!b.isNegative) {
			if(a.isNegative&&b.isNegative) {
				isNegative = true;
			}
			else {
				isNegative = false;
			}
			long[] firstNumber = a.arr;
			long[] secondNumber = b.arr;
			int sizeOfNumA = a.len;
			int sizeOfNumB = b.len;		
			int sizeOfSum = (sizeOfNumA > sizeOfNumB ? sizeOfNumA : sizeOfNumB) + 1;
			long[] sum = new long[sizeOfSum];
			long carry = 0;
			int i = 0;
			int j = 0;
			int k = 0;
			while(i < sizeOfNumA && j < sizeOfNumB) {
				sum[k] = (firstNumber[i] + secondNumber[j] + carry)%defaultBase;
				carry = (firstNumber[i] + secondNumber[j] + carry)/defaultBase;
				i++;
				j++;
				k++;
			}
			if(i<sizeOfNumA) {
				while(i<sizeOfNumA) {
					sum[k] = (firstNumber[i] + carry)%defaultBase;
					carry = (firstNumber[i]  + carry)/defaultBase;
					i++;
					k++;
				}			
			}
			else if(j<sizeOfNumB) {
				while(j<sizeOfNumB) {
					sum[k] = (secondNumber[j] + carry)%defaultBase;
					carry = (secondNumber[j]  + carry)/defaultBase;
					j++;
					k++;
				}			
			}
			if(carry!=0) {
				sum[k] = carry;
				System.out.println("carry"+carry+k);
			}
			else {
				k--;
			}
			return new Num(sum,k+1,isNegative);
		}
		else {
			return subtract(a,b);
		}
		
	}

	public static Num subtract(Num a, Num b) {
		return null;
	}

	public static Num product(Num a, Num b) {
		boolean isNegative;
		int sizeOfNumA = a.len;
		int sizeOfNumB = b.len;
		int compare = a.compareTo(b);
		long[] firstNumber;
		long[] secondNumber;
		if(compare==-1) {
			firstNumber = b.arr;
			secondNumber = a.arr;
		}
		else {
			firstNumber = a.arr;
			secondNumber = b.arr;
		}
		int sizeOfProduct = sizeOfNumA + sizeOfNumB;
		long[] product = new long[sizeOfProduct];
		long carry = 0;
		int i;
		int j;
		int k = 0;
		for(i = 0;i<sizeOfNumB;i++) {
			carry = 0;
			for(j = 0;j<sizeOfNumA;j++) {
				k = i + j;
				long prod = product[k] + (secondNumber[i] * firstNumber[j]) + carry;
				product[k] = prod % defaultBase; 
				carry = prod /defaultBase;
			}
			if(carry!=0) {				
				product[k+1] = product[k+1]+carry;
			}
		}
		if(carry!=0) {
			k++;
			product[k] = carry;
		}
		
		if((a.isNegative&&b.isNegative) || (!a.isNegative&&!b.isNegative)){
			isNegative = false;
		}
		else {
			isNegative = true;
		}
		return new Num(product,k+1,isNegative);
	}

	// Use divide and conquer
	public static Num power(Num a, long n) {
		if (n == 1) {
			return new Num(1);
		} else {
			Num p = power(Num.product(a, a), n / 2);
			return (n % 2) == 1 ? Num.product(p, a) : p;
		}
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
	// compare "this" to "other": return +1 if this is greater, 0 if equal, -1
	// otherwise
	@Override
	public int compareTo(Num other) {
		if((this.isNegative && other.isNegative) || (!this.isNegative && !other.isNegative)) {
			if(this.len>other.len)
				return 1;
			else if(this.len==other.len) {
				int i = this.len-1;
				int isEqual = 0;
				while(i>=0) {
					if(this.arr[i]<other.arr[i]) {
						isEqual = -1;
						break;
					}
					else if(this.arr[i]>other.arr[i]) {
						isEqual = 1;
						break;
					}
					i--;
				}
				return isEqual;
			}
			else
				return -1;
		}
		else if(this.isNegative) {
			return -1;
		}
		else {
			return 1;
		}
	}

	// Output using the format "base: elements of list ..."
	// For example, if base=100, and the number stored corresponds to 10965,
	// then the output is "100: 65 9 1"
	public void printList() {
		System.out.print(base() + ": ");
		for (int i = 0; i < len; i++) {
			System.out.print(arr[i] + " ");
		}
		System.out.println(isNegative ? "-" : "");
	}

	// Return number to a string in base 10
	@Override
	public String toString() {
		return null;
	}

	public long base() {
		return base;
	}

	// Return number equal to "this" number, in base=newBase
	public Num convertBase(int newBase) {
		long[] arr = this.arr;
		return null;
	}

	// Divide by 2, for using in binary search
	public Num by2() {
		return null;
	}

	// Evaluate an expression in postfix and return resulting number
	// Each string is one of: "*", "+", "-", "/", "%", "^", "0", or
	// a number: [1-9][0-9]*. There is no unary minus operator.
	public static Num evaluatePostfix(String[] expr) {
		return null;
	}

	// Evaluate an expression in infix and return resulting number
	// Each string is one of: "*", "+", "-", "/", "%", "^", "(", ")", "0", or
	// a number: [1-9][0-9]*. There is no unary minus operator.
	public static Num evaluateInfix(String[] expr) {
		return null;
	}

	public static void main(String[] args) {
		long num = Long.MAX_VALUE;
		Num a = new Num(num);
		Num b = new Num(num);
		Num x = new Num(2);
		Num y = new Num(2);
		Num sum = Num.add(a, b);		
		Num product = Num.product(x, y);
		sum.printList();
		System.out.println();
		product.printList();
		System.out.println();
	}
}