package rxl174430;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.Iterator;

public class Num implements Comparable<Num> {
	static long defaultBase = 10; // Change as needed
	long base = defaultBase; // Change as needed
	long[] arr; // array to store arbitrarily large integers
	boolean isNegative; // boolean flag to represent negative numbers
	int len; // actual number of elements of array that are used; number is stored in
				// arr[0..len-1]

	public Num(String s) {// assuming only positive number for now.
		long arrLength = (long) Math.ceil(s.length() * (Math.log(10) / Math.log(defaultBase)));
		arr = new long[(int) arrLength];
		// System.out.println(arrLength + " " + arr.length);
		if (s.charAt(0) == '-') {
			isNegative = true;
			recursive(s.substring(1, s.length()), 0);
		} else if (s.charAt(0) == '+') {
			isNegative = false;
			recursive(s.substring(1, s.length()), 0);
		} else
			recursive(s, 0);
	}

	private Num(long[] arr, int size, boolean isNegative) {
		this.arr = arr;
		this.len = size;
		this.isNegative = isNegative;
	}

	public void recursive(String quotient, int index) {
		if (quotient.length() == 1) {
			if (Long.parseLong(quotient) == 0) {
				len = index;
				return;
			}
		}
		long[] arrTemp;
		long arrLength = 18 - Long.toString(defaultBase).length();
		arrTemp = new long[(int) ((quotient.length() / arrLength) + ((quotient.length() % arrLength) == 0 ? 0 : 1))];
		String temp;
		long remainder = 0;
		String quotientString = "";
		// System.out.println(arrLength + " " + arrTemp.length);
		for (long i = 0; i < arrTemp.length; i = i + 1) {
			// System.out.println("i-" + i);
			if (remainder != 0) {
				temp = Long.toString(remainder)
						.concat(quotient.substring((int) (i * arrLength),
								(int) (((i * arrLength) + arrLength) < quotient.length() ? (i * arrLength) + arrLength
										: quotient.length())));
				arrTemp[(int) i] = Long.valueOf(temp);
			} else {
				arrTemp[(int) i] = Long.valueOf(quotient.substring((int) (i * arrLength),
						(int) (((i * arrLength) + arrLength) < quotient.length() ? (i * arrLength) + arrLength
								: quotient.length())));
				// if remainder becomes zero mid division, we need to add zeros to quotient

			}
			// System.out.print(arrTemp[(int) i]+"-->"+arrTemp[(int) i] / defaultBase);
			if (remainder == 0 && i != 0) {
				for (int z = 0; z < Long.toString(arrTemp[(int) i]).length() - 1; z++) {
					quotientString = quotientString.concat("0");
				}
			}
			remainder = arrTemp[(int) i] % defaultBase;
			// System.out.println("-->"+remainder);
			quotientString = quotientString.concat(Long.toString(arrTemp[(int) i] / defaultBase));

		}
		// System.out.println("quotient string-" + quotientString);
		arr[index] = remainder;
		recursive(quotientString, index + 1);
	}

	public Num(long x) {
		this.isNegative = x < 0 ? true : false;
		int i = 0;
		if (x == 0) {
			len = 1;
			arr = new long[len];
			arr[len - 1] = x;
		} else {
			x = Math.abs(x); // will fail in Long.MIN_VALUE. We should convert the value to a different base
								// to workaround
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

	}

	public static Num add(Num a, Num b) {
		if (isNumberZero(a)) {
			return b;
		}
		if (isNumberZero(b)) {
			return a;
		}
		boolean isNegative;
		if (a.isNegative && b.isNegative || !a.isNegative && !b.isNegative) {
			if (a.isNegative && b.isNegative) {
				isNegative = true;
			} else {
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
			while (i < sizeOfNumA && j < sizeOfNumB) {
				sum[k] = (firstNumber[i] + secondNumber[j] + carry) % defaultBase;
				carry = (firstNumber[i] + secondNumber[j] + carry) / defaultBase;
				i++;
				j++;
				k++;
			}
			if (i < sizeOfNumA) {
				while (i < sizeOfNumA) {
					sum[k] = (firstNumber[i] + carry) % defaultBase;
					carry = (firstNumber[i] + carry) / defaultBase;
					i++;
					k++;
				}
			} else if (j < sizeOfNumB) {
				while (j < sizeOfNumB) {
					sum[k] = (secondNumber[j] + carry) % defaultBase;
					carry = (secondNumber[j] + carry) / defaultBase;
					j++;
					k++;
				}
			}
			if (carry != 0) {
				sum[k] = carry;
			} else {
				k--;
			}
			return new Num(sum, k + 1, isNegative);
		} else {
			return subtract(a, negateNumber(b));
		}
	}

	public static boolean isSignEqual(Num a, Num b) {
		if ((a.isNegative && b.isNegative) || (!a.isNegative && !b.isNegative)) {
			return true;
		} else
			return false;
	}

	public static Num negateNumber(Num a) {
		Num negatedNum = new Num(a.arr, a.len, !a.isNegative);
		return negatedNum;
	}

	public static Num subtract(Num a, Num b) {
		if (isNumberZero(a)) {
			return negateNumber(b);
		}
		if (isNumberZero(b)) {
			return a;
		}

		if (isSignEqual(a, b)) {
			boolean isNegative;
			int sizeOfNumA = a.len;
			int sizeOfNumB = b.len;
			int sizeOfDiff;
			long[] firstNumber;
			long[] secondNumber;
			int firstNumberLength, secondNumberLength;
			if (sizeOfNumA > sizeOfNumB) {
				sizeOfDiff = sizeOfNumA;
				firstNumber = Arrays.copyOf(a.arr, sizeOfNumA);
				secondNumber = Arrays.copyOf(b.arr, sizeOfNumB);
				firstNumberLength = sizeOfNumA;
				secondNumberLength = sizeOfNumB;
				isNegative = a.isNegative;
			} else if (sizeOfNumA < sizeOfNumB) {
				sizeOfDiff = sizeOfNumB;
				firstNumber = Arrays.copyOf(b.arr, sizeOfNumB);
				secondNumber = Arrays.copyOf(a.arr, sizeOfNumA);
				firstNumberLength = sizeOfNumB;
				secondNumberLength = sizeOfNumA;
				isNegative = b.isNegative;
			} else {
				int x = sizeOfNumA - 1;
				int isEqual = 0;
				while (x >= 0) {
					if (a.arr[x] < b.arr[x]) {
						isEqual = -1;
						break;
					} else if (a.arr[x] > b.arr[x]) {
						isEqual = 1;
						break;
					}
					x--;
				}
				if (isEqual == -1) {
					sizeOfDiff = sizeOfNumB;
					firstNumber = Arrays.copyOf(b.arr, sizeOfNumB);
					secondNumber = Arrays.copyOf(a.arr, sizeOfNumA);
					firstNumberLength = sizeOfNumB;
					secondNumberLength = sizeOfNumA;
					isNegative = b.isNegative;
				} else {
					sizeOfDiff = sizeOfNumA;
					firstNumber = Arrays.copyOf(a.arr, sizeOfNumA);
					secondNumber = Arrays.copyOf(b.arr, sizeOfNumB);
					firstNumberLength = sizeOfNumA;
					secondNumberLength = sizeOfNumB;
					isNegative = a.isNegative;
				}

			}
			if ((!a.isNegative && !b.isNegative) || (a.isNegative && b.isNegative)) {
				int compare = a.compareTo(b);
				if (compare == -1) {
					isNegative = true;
				} else {
					isNegative = false;
				}
			}
			long[] difference = new long[sizeOfDiff];
			int i = 0;
			int j = 0;
			int k = 0;
			while (i < firstNumberLength && j < secondNumberLength) {
				if (firstNumber[i] >= secondNumber[j]) {
					difference[k] = firstNumber[i] - secondNumber[j];
				} else {
					difference[k] = (firstNumber[i] + defaultBase) - secondNumber[j];
					for (int borrowIndex = i + 1; borrowIndex < firstNumberLength; borrowIndex++) {
						if (firstNumber[borrowIndex] != 0) {
							firstNumber[borrowIndex] -= 1;
							break;
						} else {
							firstNumber[borrowIndex] += (defaultBase - 1);
						}
					}
				}
				i++;
				j++;
				k++;
			}
			while (i < firstNumberLength) {
				difference[k] = firstNumber[i];
				i++;
				k++;
			}
			return new Num(difference, getLengthWithoutLeadingZeros(difference), isNegative);
		} else {
			return add(a, negateNumber(b));
		}

	}

	public static int getLengthWithoutLeadingZeros(long[] a) {
		int len = a.length;
		int i = len - 1;
		int count = 0;
		while (i > 0 && a[i] == 0) {
			count++;
			i--;
		}
		return len - count;
	}

	public static Num product(Num a, Num b) {
		if (isNumberZero(a) || isNumberZero(b)) {
			return new Num(0);
		}
		boolean isNegative;
		int sizeOfLargerNum = 0;
		int sizeOfSmallerNum = 0;
		int compare = a.compareTo(b);
		long[] firstNumber;
		long[] secondNumber;
		if (compare == -1) {
			firstNumber = b.arr;
			sizeOfLargerNum = b.len;
			secondNumber = a.arr;
			sizeOfSmallerNum = a.len;

		} else {
			firstNumber = a.arr;
			sizeOfLargerNum = a.len;
			secondNumber = b.arr;
			sizeOfSmallerNum = b.len;

		}
		int sizeOfProduct = sizeOfLargerNum + sizeOfSmallerNum;
		long[] product = new long[sizeOfProduct];
		long carry = 0;
		int i;
		int j;
		int k = 0;
		for (i = 0; i < sizeOfLargerNum; i++) {
			carry = 0;
			for (j = 0; j < sizeOfSmallerNum; j++) {
				k = i + j;
				long prod = product[k] + (firstNumber[i] * secondNumber[j]) + carry;
				product[k] = prod % defaultBase;
				carry = prod / defaultBase;
			}
			if (carry != 0) {
				product[k + 1] = product[k + 1] + carry;
			}
		}
		if (carry != 0) {
			k++;
			product[k] = carry;
		}
		if ((a.isNegative && b.isNegative) || (!a.isNegative && !b.isNegative)) {
			isNegative = false;
		} else {
			isNegative = true;
		}

		return new Num(product, k + 1, isNegative);
	}

	// Use divide and conquer
	public static Num power(Num a, long n) {
		if (n == 0) {
			return new Num(1);
		} else {
			Num p = power(Num.product(a, a), n / 2);
			return (n % 2) == 1 ? Num.product(p, a) : p;
		}
	}

	// Use binary search to calculate a/b
	public static Num divide(Num a, Num b) {
		Num sign = null;
		if (isSignEqual(a, b)) {
			sign = new Num(1);
		} else {
			sign = new Num(-1);
		}
		a = a.isNegative ? negateNumber(a) : a;
		b = b.isNegative ? negateNumber(b) : b;

		if (b.compareTo(new Num(0)) == 0) {
			throw new IllegalArgumentException("Cannot divide by zero");
		}
		if (b.compareTo(new Num(1)) == 0) {
			return product(a, sign);
		}
		if (b.compareTo(a) == 0) {
			return sign;
		}
		if (b.compareTo(a) > 0) {
			return new Num(0);
		}
		Num low = new Num(0);
		Num high = a;
		while (true) {
			Num mid = add(low, ((subtract(high, low)).by2()));
			Num operation = subtract(product(b, mid), a);
			if (operation.isNegative) {
				if ((negateNumber(operation)).compareTo(b) <= 0) {
					return product(mid, sign);
				}
			} else {
				if ((operation).compareTo(new Num(0)) == 0) {
					return product(mid, sign);
				}
			}

			if (product(b, mid).compareTo(a) == -1) {
				low = mid;
			} else {
				high = mid;
			}
		}
	}

	private static boolean isNumberZero(Num a) {
		return (a.len == 1) && (a.arr[0] == 0);
	}

	// return a%b
	public static Num mod(Num a, Num b) {
		if (b.compareTo(new Num(0)) == 0) {
			throw new IllegalArgumentException("Cannot divide by zero");
		}
		if (b.compareTo(new Num(1)) == 0) {
			return new Num(0);
		}
		if (b.compareTo(a) == 0) {
			return new Num(0);
		}
		if (b.compareTo(a) > 0) {
			return a;
		}
		Num low = new Num(0);
		Num high = a;
		while (true) {
			Num mid = add(low, ((subtract(high, low)).by2()));
			Num operation = subtract(product(b, mid), a);
			if (operation.isNegative) {
				if ((negateNumber(operation)).compareTo(b) <= 0) {
					return negateNumber(operation);
				}
			} else {
				if ((operation).compareTo(new Num(0)) == 0) {
					return new Num(0);
				}
			}

			if (product(b, mid).compareTo(a) == -1) {
				low = mid;
			} else {
				high = mid;
			}
		}
	}

	// Use binary search
	public static Num squareRoot(Num a) {
		Num low = new Num(0);
		Num high = a;
		Num sqrt = new Num(1);
		while (low.compareTo(high) <= 0) {
			Num mid = add(low, ((subtract(high, low)).by2()));
			Num operation = product(mid, mid);
			int comparison = operation.compareTo(a);
			if (comparison == -1) {
				low = add(mid, new Num(1));
				sqrt = mid;
			} else if (comparison == 0) {
				return mid;
			} else {
				high = subtract(mid, new Num(1));
			}
		}
		return sqrt;
	}

	// Utility functions
	// compare "this" to "other": return +1 if this is greater, 0 if equal, -1
	// otherwise
	@Override
	public int compareTo(Num other) {
		if ((this.isNegative && other.isNegative) || (!this.isNegative && !other.isNegative)) {
			if (this.len > other.len)
				return (this.isNegative && other.isNegative) ? -1 : 1;
			else if (this.len == other.len) {
				int i = this.len - 1;
				int isEqual = 0;
				while (i >= 0) {
					if (this.arr[i] < other.arr[i]) {
						isEqual = (this.isNegative && other.isNegative) ? 1 : -1;
						break;
					} else if (this.arr[i] > other.arr[i]) {
						isEqual = (this.isNegative && other.isNegative) ? -1 : 1;
						break;
					}
					i--;
				}
				return isEqual;
			} else
				return (this.isNegative && other.isNegative) ? 1 : -1;
		} else if (this.isNegative) {
			return -1;
		} else {
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
		return null;
	}

	// Divide by 2, for using in binary search
	public Num by2() {
		long carry = 0;
		long[] newArr = new long[this.len];
		for (int i = this.len - 1; i >= 0; i--) {
			long remainder = this.arr[i] + carry * base;
			newArr[i] = remainder >> 1;
			carry = remainder - (newArr[i] * 2);
		}

		return new Num(newArr, getLengthWithoutLeadingZeros(newArr), this.isNegative);
	}

	private static boolean isOperator(String str) {
		if (str.equals("*") || str.equals("+") || str.equals("-") || str.equals("/") || str.equals("%")
				|| str.equals("^")) {
			return true;
		} else {
			return false;
		}
	}

	private static Num doOperation(Num operand1, Num operand2, String operator) throws Exception {
		Num result = null;
		switch (operator) {
		case "+":
			result = add(operand1, operand2);
			break;
		case "-":
			result = subtract(operand1, operand2);
			break;
		case "*":
			result = product(operand1, operand2);
			break;
		case "/":
			result = divide(operand1, operand2);
			break;
		case "%":
			result = mod(operand1, operand2);
			break;
		case "^": // result = power(operand1,operand2);
			break;
		default:
			throw new Exception("Unsupported Operation");
		}
		return result;
	}

	// Evaluate an expression in postfix and return resulting number
	// Each string is one of: "*", "+", "-", "/", "%", "^", "0", or
	// a number: [1-9][0-9]*. There is no unary minus operator.
	public static Num evaluatePostfix(String[] expr) throws Exception {
		Deque<String> stack = new ArrayDeque<String>();
		for (String exp : expr) {
			if (!isOperator(exp)) {
				stack.push(exp);
			} else {
				String operandString2 = stack.pop();
				String operandString1 = stack.pop();
				Num operand1 = new Num(operandString1);
				Num operand2 = new Num(operandString2);
				Num result = doOperation(operand1, operand2, exp);
				stack.push(result.toString());
			}
		}
		Num answer = new Num(stack.pop());
		return answer;
	}

	private static int getOperatorPriority(String operator) throws Exception {
		int priority = 0;
		switch(operator) {
			case "+" : priority = 1;
			break;
			case "-" : priority = 1;
				break;
			case "*" : priority = 2;
				break;
			case "/" : priority = 2;
				break;
			case "%" : priority = 2;
				break;
			case "^" : priority = 3;
				break;
			default:
				throw new Exception("Unsupported Operator");
		}
		return priority;
		
	}

	// Evaluate an expression in infix and return resulting number
	// Each string is one of: "*", "+", "-", "/", "%", "^", "(", ")", "0", or
	// a number: [1-9][0-9]*. There is no unary minus operator.
	public static Num evaluateInfix(String[] expr) throws Exception {
		Deque<String> stack = new ArrayDeque<String>();
		Deque<String> queue = new ArrayDeque<String>();

		for (String exp : expr) {
			if (!isOperator(exp) && !exp.equals("(") && !exp.equals(")")) {
				queue.addLast(exp);
			} else if (isOperator(exp)) {
				String queueFront = queue.peekFirst();
				if(!isOperator(queueFront)) {
					stack.push(exp);
				}
				else if(getOperatorPriority(exp)>getOperatorPriority(queueFront)) {
					stack.push(exp);
				}
				else {
					while(getOperatorPriority(stack.peek())>=getOperatorPriority(exp)) {
						queue.addLast(stack.pop());
					}
				}				

			} else if (exp.equals("(")) {
				stack.push(exp);
			} else if (exp.equals(")")) {
				String topOfStack = stack.peek();
				while(!topOfStack.equals("(")) {
					queue.addLast(stack.pop());
					topOfStack = stack.peek();
				}
				stack.pop();				
			}
		}
		while(stack.size()>0) {
			queue.addLast(stack.pop());
		}
		//check whether the expr is right. Remove in production mode
		Iterator it = queue.iterator();
		while(it.hasNext()) {
			System.out.println(it.next());
		}
		String[] postfixExpression = queue.toArray(new String[queue.size()]);
		return evaluatePostfix(postfixExpression); //
	}

	public static void main(String[] args) throws Exception {
		Num a = new Num(17);
		Num b = new Num(9);
		(Num.mod(a, b)).printList();
	}
}