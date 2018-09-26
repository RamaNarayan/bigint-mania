/**
 * @author: Shubham Raosaheb Kharde, Rohit Seetepalli, Arunachalam Saravanan, Rama Narayan Lakshmanan
 * Long Project LP1: Integer arithmetic with arbitrarily large numbers
 */

package rxl174430;

import java.util.ArrayDeque;
import java.util.Deque;

public class Num implements Comparable<Num> {
	static long defaultBase = 919119199;
	long base;
	long[] arr;
	boolean isNegative;
	int len;

	/**
	 * @param s
	 */
	public Num(String s) {
		base = defaultBase;
		constructStringNum(s);
	}

	/**
	 * Constructs the array from the input string with the default base.
	 *
	 * @param s
	 */
	private void constructStringNum(String s) {
		if (s.isEmpty()) {
			throw new IllegalArgumentException("Needs a valid String argument");
		}

		if ((s.charAt(0) == '-') || (s.charAt(0) == '+')) {
			if (s.charAt(0) == '-') {
				isNegative = true;
			} else if (s.charAt(0) == '+') {
				isNegative = false;
			}
			s = s.substring(1, s.length());
			if (s.isEmpty()) {
				throw new IllegalArgumentException("Needs a valid String argument");
			}

		}

		long arrLength = (long) Math.ceil(((s.length() + 1) * (Math.log(10) / Math.log(base))) + 1);

		arr = new long[(int) arrLength];

		try {
			recursive(s, 0);
		} catch (Exception E) {
			E.printStackTrace();
			throw new IllegalArgumentException("Error in handling input");
		}

		len = getLengthWithoutLeadingZeros(arr);

		if ((len == 1) && (arr[0] == 0)) {
			isNegative = false;
		}
	}

	private long getBaseLength() {
		return Long.toString(base()).length();
	}

	/**
	 * @param arr
	 * @param size
	 * @param isNegative
	 * @param base
	 */
	private Num(long[] arr, int size, boolean isNegative, long base) {
		this.arr = arr;
		this.len = size;
		this.isNegative = isNegative;
		this.base = base;
	}

	/**
	 * @param quotient
	 * @param index
	 */
	private void recursive(String quotient, int index) {
		long quotientLength = quotient.length();

		if (quotientLength < 19) {
			if (Long.parseLong(quotient) == 0) {
				len = index;
				return;
			}
		}
		long baseLength = getBaseLength();
		long arrLength = 19 - baseLength;
		long temporaryLength = (quotientLength / arrLength) + ((quotientLength % arrLength) == 0 ? 0 : 1);
		long temporaryNumber;
		long temporaryNumber2;
		String temp;
		long remainder = 0;
		String quotientString = "";
		long remLength;
		long subLength;

		for (long i = 0; i < temporaryLength; i = i + 1) {

			subLength = i * arrLength;

			remLength = Long.toString(remainder).length();
			temp = Long.toString(remainder).concat(quotient.substring((int) (subLength), (int) ((subLength + arrLength) < quotientLength ? (subLength + arrLength) : quotientLength)));
			temporaryNumber = Long.valueOf(temp);

			// Quotient zero handling
			if (i != 0) {
				if (i != (temporaryLength - 1)) {
					if (remainder == 0) {

						int iter = 0;

						temporaryNumber2 = Long.valueOf(quotient.substring((int) (subLength), (int) (subLength + iter + 1)));

						while ((iter < (arrLength - 1)) && (temporaryNumber2 < base())) {

							quotientString = quotientString.concat("0");
							iter++;
							temporaryNumber2 = Long.valueOf(quotient.substring((int) (subLength), (int) (subLength + iter + 1)));
						}
					} else if (baseLength != remLength) {
						temporaryNumber2 = Long.valueOf(Long.toString(remainder).concat(quotient.substring((int) (subLength), (int) ((subLength + baseLength) - remLength))));
						if (base() <= temporaryNumber2) {
							for (int z = 0; z < (baseLength - remLength - 1); z++) {
								quotientString = quotientString.concat("0");
							}
						} else {
							for (int z = 0; z < (baseLength - remLength); z++) {
								quotientString = quotientString.concat("0");
							}

						}
					}
				} else if (i == (temporaryLength - 1)) {

					if (remainder == 0) {

						temporaryNumber2 = Long.valueOf(quotient.substring((int) (subLength), (int) (subLength + 1)));
						int iter = (int) subLength + 1;
						while ((iter < quotient.length()) && (temporaryNumber2 < base())) {
							quotientString = quotientString.concat("0");
							iter++;
							temporaryNumber2 = Long.valueOf(quotient.substring((int) (subLength), (iter)));
						}
					} else if (baseLength != remLength) {

						Long lastPart = Long.parseLong(Long.toString(remainder).concat(quotient.substring((int) subLength, (int) quotientLength)));
						if (lastPart < base()) {
							for (int z = 0; z < (quotient.substring((int) subLength, (int) quotientLength).length() - 1); z++) {
								quotientString = quotientString.concat("0");
							}

						} else {
							temporaryNumber2 = Long.valueOf(Long.toString(remainder).concat(quotient.substring((int) (subLength), (int) ((subLength + baseLength) - remLength))));
							if (base() <= temporaryNumber2) {
								for (int z = 0; z < (baseLength - remLength - 1); z++) {
									quotientString = quotientString.concat("0");
								}
							} else {
								for (int z = 0; z < (baseLength - remLength); z++) {
									quotientString = quotientString.concat("0");
								}

							}
						}
					}

				}
			}

			remainder = temporaryNumber % base();

			if (!quotientString.isEmpty() && (quotientString.length() < 19) && (Long.parseLong(quotientString) == 0)) {
				quotientString = "";
			}
			quotientString = quotientString.concat(Long.toString(temporaryNumber / base()));

		}

		arr[index] = remainder;
		recursive(quotientString, index + 1);

	}

	/**
	 * Create Num with given base
	 *
	 * @param x
	 * @param base
	 */
	private void constructLongNum(long x, long base) {
		this.base = base;
		if (x == Long.MIN_VALUE) {
			constructStringNum(Long.toString(x)); // Long.MIN_VALUE overflows. So process it as a string.
		} else {
			if (x == 0) {
				len = 1;
				arr = new long[len];
				arr[len - 1] = x;
			} else {
				this.isNegative = x < 0 ? true : false;
				int i = 0;
				x = Math.abs(x);
				len = getNumberLength(x);
				arr = new long[len];
				i = 0;
				while (x > 0) {
					arr[i] = x % base;
					x = x / base;
					i = i + 1;
				}
			}
		}
	}

	/**
	 * @param x
	 */
	public Num(long x) {
		constructLongNum(x, defaultBase);
	}

	/**
	 * @param x
	 * @param base
	 */
	private Num(long x, long base) {
		constructLongNum(x, base);
	}

	/**
	 * @param x
	 * @param base
	 */
	private Num(String x, long base) {
		this.base = base;
		constructStringNum(x);
	}

	/**
	 * Number of digits in a number
	 *
	 * @param x
	 * @return
	 */
	private int getNumberLength(long x) {
		int i = 0;
		while (x > 0) {
			x = x / base();
			i = i + 1;
		}
		return i;
	}

	/**
	 * Add two Nums and return the result Num
	 *
	 * @param a
	 * @param b
	 * @return
	 */
	public static Num add(Num a, Num b) {
		if (isNumberZero(a)) {
			return b;
		}
		if (isNumberZero(b)) {
			return a;
		}
		boolean isNegative;
		if (isSignEqual(a, b)) {
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
			long base = a.base();
			while ((i < sizeOfNumA) && (j < sizeOfNumB)) {
				sum[k] = (firstNumber[i] + secondNumber[j] + carry) % base;
				carry = (firstNumber[i] + secondNumber[j] + carry) / base;
				i++;
				j++;
				k++;
			}
			if (i < sizeOfNumA) {
				while (i < sizeOfNumA) {
					sum[k] = (firstNumber[i] + carry) % base;
					carry = (firstNumber[i] + carry) / base;
					i++;
					k++;
				}
			} else if (j < sizeOfNumB) {
				while (j < sizeOfNumB) {
					sum[k] = (secondNumber[j] + carry) % base;
					carry = (secondNumber[j] + carry) / base;
					j++;
					k++;
				}
			}
			if (carry != 0) {
				sum[k] = carry;
			} else {
				k--;
			}
			return new Num(sum, k + 1, isNegative, base);
		} else {
			// If one number is negative, perform subtraction of the numbers
			return subtract(a, negateNumber(b));
		}
	}

	/**
	 * Check if both numbers have the same sign
	 *
	 * @param a
	 * @param b
	 * @return
	 */
	private static boolean isSignEqual(Num a, Num b) {
		if ((a.isNegative && b.isNegative) || (!a.isNegative && !b.isNegative)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Negate a number
	 *
	 * @param a
	 * @return
	 */
	private static Num negateNumber(Num a) {
		return new Num(a.arr, a.len, !a.isNegative, a.base());
	}

	/**
	 * Compare 2 numbers irrespective of their signs
	 *
	 * @param a
	 * @param b
	 * @return
	 */
	private static int unsignedCompare(Num a, Num b) {
		int sizeOfNumA = a.len;
		int sizeOfNumB = b.len;
		if (sizeOfNumA > sizeOfNumB) {
			return 1;

		} else if (sizeOfNumA < sizeOfNumB) {
			return -1;

		} else {
			int x = sizeOfNumA - 1;
			while (x >= 0) {
				if (a.arr[x] < b.arr[x]) {
					return -1;
				} else if (a.arr[x] > b.arr[x]) {
					return 1;
				}
				x--;
			}
			return 0;
		}
	}

	/**
	 * Subtract 2 numbers and return the difference
	 *
	 * @param a
	 * @param b
	 * @return
	 */
	public static Num subtract(Num a, Num b) {
		if (isNumberZero(a)) {
			return negateNumber(b);
		}
		if (isNumberZero(b)) {
			return a;
		}

		if (a.compareTo(b) == 0) {
			return new Num(0, a.base());
		}

		if (isSignEqual(a, b)) {
			boolean isNegative = a.compareTo(b) == -1 ? true : false;
			int sizeOfNumA = a.len;
			int sizeOfNumB = b.len;
			int sizeOfDifference, firstNumberLength, secondNumberLength;
			long base = a.base();
			long[] firstNumber;
			long[] secondNumber;
			if (unsignedCompare(a, b) == -1) {
				sizeOfDifference = sizeOfNumB;
				firstNumber = b.arr;
				secondNumber = a.arr;
				firstNumberLength = sizeOfNumB;
				secondNumberLength = sizeOfNumA;
			} else {
				sizeOfDifference = sizeOfNumA;
				firstNumber = a.arr;
				secondNumber = b.arr;
				firstNumberLength = sizeOfNumA;
				secondNumberLength = sizeOfNumB;
			}
			long[] difference = new long[sizeOfDifference];
			int i = 0;
			int j = 0;
			int k = 0;
			long carry = 0;
			while ((i < firstNumberLength) && (j < secondNumberLength)) {
				if ((firstNumber[i] - carry) >= secondNumber[j]) {
					difference[k] = firstNumber[i] - secondNumber[j] - carry;
					carry = 0;
				} else {
					difference[k] = (firstNumber[i] + base) - secondNumber[j] - carry;
					carry = 1;
				}
				i++;
				j++;
				k++;

			}
			while (i < firstNumberLength) {
				if ((firstNumber[i] - carry) >= 0) {
					difference[k] = firstNumber[i] - carry;
					carry = 0;
				} else {
					difference[k] = (firstNumber[i] + base) - carry;
					carry = 1;
				}
				i++;
				k++;
			}
			return new Num(difference, getLengthWithoutLeadingZeros(difference), isNegative, base);
		} else {
			// Perform add if the 2 numbers have different signs
			return add(a, negateNumber(b));
		}

	}

	/**
	 * Neglect the leading zeros and find the length of the number
	 *
	 * @param a
	 * @return
	 */
	private static int getLengthWithoutLeadingZeros(long[] a) {
		int len = a.length;
		int i = len - 1;
		int count = 0;
		while ((i > 0) && (a[i] == 0)) {
			count++;
			i--;
		}
		return len - count;
	}

	/**
	 * Multiplies 2 numbers and returns the result
	 *
	 * @param a
	 * @param b
	 * @return
	 */
	public static Num product(Num a, Num b) {
		if (isNumberZero(a) || isNumberZero(b)) {
			return new Num(0, a.base());
		}
		boolean isNegative = isSignEqual(a, b) ? false : true;
		int sizeOfLargerNum;
		int sizeOfSmallerNum;
		int unsignedCompare = unsignedCompare(a, b);
		long[] firstNumber;
		long[] secondNumber;
		long base = a.base();
		if (unsignedCompare == -1) {
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
		int k = 0;
		long prod;
		for (int i = 0; i < sizeOfLargerNum; i++) {
			carry = 0;
			for (int j = 0; j < sizeOfSmallerNum; j++) {
				k = i + j;
				prod = product[k] + (firstNumber[i] * secondNumber[j]) + carry;
				product[k] = prod % base;
				carry = prod / base;
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

		return new Num(product, k + 1, isNegative, base);
	}

	/**
	 * Find power of one number raised to another
	 *
	 * @param a
	 * @param n
	 * @return
	 */
	public static Num power(Num a, long n) {
		return power(a, new Num(n, a.base()));
	}

	/**
	 * Find power of one number raised to another
	 *
	 * @param a
	 * @param n
	 * @return
	 */
	private static Num power(Num a, Num n) {
		if (isNumberZero(n)) {
			return new Num(1, a.base());
		} else {
			Num p = power(product(a, a), n.by2());
			return mod(n, new Num(2, a.base())).compareTo(new Num(1)) == 0 ? product(p, a) : p;
		}
	}

	/**
	 * Divide 2 numbers and return the quotient number
	 *
	 * @param a
	 * @param b
	 * @return
	 */
	public static Num divide(Num a, Num b) {
		Num zero = new Num(0, a.base());
		Num one = new Num(1, a.base());
		Num minus_one = new Num(-1, a.base());

		Num sign = null;
		if (isSignEqual(a, b)) {
			sign = one;
		} else {
			sign = minus_one;
		}

		Num dividend = a.isNegative ? negateNumber(a) : a;
		Num divisor = b.isNegative ? negateNumber(b) : b;

		if (divisor.compareTo(zero) == 0) {
			throw new IllegalArgumentException("Cannot divide by zero");
		}
		if (divisor.compareTo(one) == 0) {
			return product(dividend, sign);
		}
		if (divisor.compareTo(dividend) == 0) {
			return sign;
		}
		if (divisor.compareTo(dividend) > 0) {
			return zero;
		}
		Num low = zero;
		Num high = dividend;
		while (true) {
			Num quotient = add(low, (subtract(high, low)).by2());
			Num divisor_quotient_product = product(divisor, quotient);
			Num remainder = subtract(divisor_quotient_product, dividend);
			if (remainder.isNegative) {
				remainder.isNegative = false;
				if ((remainder).compareTo(divisor) < 0) {
					return product(quotient, sign);
				}
			} else {
				if ((remainder).compareTo(zero) == 0) {
					return product(quotient, sign);
				}
			}

			if (divisor_quotient_product.compareTo(dividend) == -1) {
				low = quotient;
			} else {
				high = quotient;
			}
		}
	}

	/**
	 * Check if the number is 0
	 *
	 * @param a
	 * @return
	 */
	private static boolean isNumberZero(Num a) {
		return (a.len == 1) && (a.arr[0] == 0);
	}

	/**
	 * Performs modulo of 2 numbers and return the remainder
	 *
	 * @param dividend
	 * @param divisor
	 * @return
	 */
	public static Num mod(Num dividend, Num divisor) {
		Num zero = new Num(0, dividend.base());
		Num one = new Num(1, dividend.base());
		if (divisor.compareTo(zero) == 0) {
			throw new IllegalArgumentException("Cannot divide by zero");
		}
		if (divisor.compareTo(one) == 0) {
			return zero;
		}
		if (divisor.compareTo(dividend) == 0) {
			return zero;
		}
		if (divisor.compareTo(dividend) > 0) {
			return dividend;
		}
		Num low = zero;
		Num high = dividend;
		while (true) {
			Num quotient = add(low, ((subtract(high, low)).by2()));
			Num divisor_quotient_product = product(divisor, quotient);
			Num remainder = subtract(divisor_quotient_product, dividend);
			if (remainder.isNegative) {
				remainder.isNegative = false;
				if (remainder.compareTo(divisor) < 0) {
					return remainder;
				}
			} else {
				if ((remainder).compareTo(zero) == 0) {
					return remainder;
				}
			}

			if (divisor_quotient_product.compareTo(dividend) == -1) {
				low = quotient;
			} else {
				high = quotient;
			}
		}
	}

	/**
	 * Determine the square root of a number
	 *
	 * @param a
	 * @return
	 */
	public static Num squareRoot(Num a) {
		if (a.isNegative) {
			throw new IllegalArgumentException("Square root of negative numbers not supported");
		}
		if (a.compareTo(new Num(0)) == 0) {
			return a;
		}
		Num low = new Num(0, a.base());
		Num high = a;
		Num sqrt = new Num(1, a.base());
		while (low.compareTo(high) <= 0) {
			Num mid = add(low, ((subtract(high, low)).by2()));
			Num operation = product(mid, mid);
			int comparison = operation.compareTo(a);
			if (comparison == -1) {
				low = add(mid, new Num(1, a.base()));
				sqrt = mid;
			} else if (comparison == 0) {
				return mid;
			} else {
				high = subtract(mid, new Num(1, a.base()));
			}
		}
		return sqrt;
	}

	/**
	 * Compare the 2 numbers and return the result -1 if first < second. 0 if both are equal 1 if first > second
	 */
	@Override
	public int compareTo(Num other) {
		if ((this.isNegative && other.isNegative) || (!this.isNegative && !other.isNegative)) {
			if (this.len > other.len) {
				return (this.isNegative && other.isNegative) ? -1 : 1;
			} else if (this.len == other.len) {
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
			} else {
				return (this.isNegative && other.isNegative) ? 1 : -1;
			}
		} else if (this.isNegative) {
			return -1;
		} else {
			return 1;
		}
	}

	/**
	 * Print the number(Which is in Array format)in default base from LSD to MSD
	 */
	public void printList() {
		System.out.print(base() + ": ");
		System.out.print(isNegative ? "- " : "");
		for (int i = 0; i < len; i++) {
			System.out.print(arr[i] + " ");
		}

	}

	/**
	 * Convert the number to string format in base 10
	 */
	@Override
	public String toString() {
		Num n = convertNumToBase10(this);
		System.out.println("Converted");
		StringBuilder sb = new StringBuilder();
		if (n.isNegative && !isNumberZero(n)) {
			sb.append('-');
		}
		for (int i = n.len - 1; i >= 0; i--) {
			sb.append(Long.toString(n.arr[i]));
		}
		return sb.toString();
	}

	/**
	 * Returns the base of the number
	 *
	 * @return
	 */
	public long base() {
		return base;
	}

	/**
	 * Convert the number to Base 10
	 *
	 * @param num
	 * @return
	 */
	private Num convertNumToBase10(Num num) {
		int i = num.len - 1;
		long originalBase = defaultBase;
		defaultBase = 10;
		Num newNum = new Num(num.arr[i]);
		Num baseNum = new Num(base());
		while (i > 0) {
			Num prodNum = product(newNum, baseNum);
			newNum = add(prodNum, new Num(num.arr[i - 1]));
			i--;
		}
		defaultBase = originalBase;
		newNum.isNegative = num.isNegative;
		return newNum;
	}

	/**
	 * Convert the number from default base to the user input base
	 *
	 * @param newBase
	 * @return
	 */
	public Num convertBase(int newBase) {
		if (newBase != base) {
			return new Num(toString(), newBase);
		} else {
			return this;
		}
	}

	/**
	 * Divides the number by 2
	 *
	 * @return
	 */
	public Num by2() {
		long carry = 0;
		long[] newArr = new long[this.len];
		for (int i = this.len - 1; i >= 0; i--) {
			long remainder = this.arr[i] + (carry * base());
			newArr[i] = remainder >> 1;
			carry = remainder - (newArr[i] * 2);
		}

		return new Num(newArr, getLengthWithoutLeadingZeros(newArr), this.isNegative, base());
	}

	/**
	 * Check if the string is an operator
	 *
	 * @param str
	 * @return
	 */
	private static boolean isOperator(String str) {
		if (str.equals("*") || str.equals("+") || str.equals("-") || str.equals("/") || str.equals("%") || str.equals("^")) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Perform arithmetic operations based on the operator
	 *
	 * @param operand1
	 * @param operand2
	 * @param operator
	 * @return
	 * @throws Exception
	 */
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
		case "^":
			result = power(operand1, operand2);
			break;
		default:
			throw new Exception("Unsupported Operation");
		}
		return result;
	}

	/**
	 * Evaluates a postfix expression and returns the result
	 *
	 * @param expr
	 * @return
	 * @throws Exception
	 */
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

	/**
	 * Determine the operator precedence
	 *
	 * @param operator
	 * @return
	 * @throws Exception
	 */
	private static int getOperatorPriority(String operator) throws Exception {
		int priority = 0;
		switch (operator) {
		case "+":
			priority = 1;
			break;
		case "-":
			priority = 1;
			break;
		case "*":
			priority = 2;
			break;
		case "/":
			priority = 2;
			break;
		case "%":
			priority = 2;
			break;
		case "^":
			priority = 3;
			break;
		default:
			throw new Exception("Unsupported Operator");
		}
		return priority;

	}

	/**
	 * Evaluate the infix expression and return the result
	 *
	 * @param expr
	 * @return
	 * @throws Exception
	 */
	public static Num evaluateInfix(String[] expr) throws Exception {
		Deque<String> stack = new ArrayDeque<String>();
		Deque<String> queue = new ArrayDeque<String>();

		for (String exp : expr) {
			if (!isOperator(exp) && !exp.equals("(") && !exp.equals(")")) {
				queue.addLast(exp);
			} else if (isOperator(exp)) {
				while ((stack.peek() != null) && isOperator(stack.peek()) && (getOperatorPriority(stack.peek()) >= getOperatorPriority(exp))) {
					queue.addLast(stack.pop());
				}
				stack.push(exp);
			} else if (exp.equals("(")) {
				stack.push(exp);
			} else if (exp.equals(")")) {
				while (!stack.peek().equals("(")) {
					queue.addLast(stack.pop());
				}
				stack.pop();
			}
		}
		while (stack.size() > 0) {
			queue.addLast(stack.pop());
		}
		String[] postfixExpression = queue.toArray(new String[queue.size()]);
		return evaluatePostfix(postfixExpression);
	}

	public static void main(String[] args) throws Exception {
		Num a = new Num(Long.toString(Long.MAX_VALUE));
		a.printList();
		(a.convertBase(15)).printList();
	}
}