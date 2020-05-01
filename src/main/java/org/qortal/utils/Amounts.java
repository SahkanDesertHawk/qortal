package org.qortal.utils;

import java.math.BigDecimal;
import java.math.BigInteger;

public abstract class Amounts {

	public static final long MULTIPLIER = 100000000L;

	// For calculations that might overflow longs
	public static final BigInteger MULTIPLIER_BI = BigInteger.valueOf(MULTIPLIER);
	public static final BigInteger ROUNDING = MULTIPLIER_BI.subtract(BigInteger.ONE);

	public static String prettyAmount(long amount) {
		StringBuilder stringBuilder = new StringBuilder(20);

		stringBuilder.append(amount / MULTIPLIER);

		stringBuilder.append('.');

		int dpLength = stringBuilder.length();

		stringBuilder.append(Math.abs(amount % MULTIPLIER));

		int paddingRequired = 8 - (stringBuilder.length() - dpLength);
		if (paddingRequired > 0)
			stringBuilder.append("00000000", 0, paddingRequired);

		return stringBuilder.toString();
	}

	public static BigDecimal toBigDecimal(long amount) {
		return BigDecimal.valueOf(amount, 8);
	}

	public static long greatestCommonDivisor(long a, long b) {
		if (b == 0)
			return Math.abs(a);
		else if (a == 0)
			return Math.abs(b);

		while (b != 0) {
			long r = a % b;
			a = b;
			b = r;
		}

		return Math.abs(a);
	}

	public static long roundUpScaledMultiply(BigInteger multiplicand, BigInteger multiplier) {
		return multiplicand.multiply(multiplier).add(ROUNDING).divide(MULTIPLIER_BI).longValue();
	}

	public static long roundUpScaledMultiply(long multiplicand, long multiplier) {
		return roundUpScaledMultiply(BigInteger.valueOf(multiplicand), BigInteger.valueOf(multiplier));
	}

	public static long roundDownScaledMultiply(BigInteger multiplicand, BigInteger multiplier) {
		return multiplicand.multiply(multiplier).divide(MULTIPLIER_BI).longValue();
	}

	public static long roundDownScaledMultiply(long multiplicand, long multiplier) {
		return roundDownScaledMultiply(BigInteger.valueOf(multiplicand), BigInteger.valueOf(multiplier));
	}

	public static long scaledDivide(long dividend, long divisor) {
		return BigInteger.valueOf(dividend).multiply(Amounts.MULTIPLIER_BI).divide(BigInteger.valueOf(divisor)).longValue();
	}

}