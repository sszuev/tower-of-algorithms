package com.gitlab.sszuev.tasks.algebraic.fibonacci;

import com.gitlab.sszuev.tasks.Algorithm;
import org.apache.commons.lang3.StringUtils;

import java.math.BigInteger;
import java.util.List;
import java.util.Objects;

/**
 * Created by @ssz on 21.08.2021.
 */
public class FibonacciMatrixAlgorithm implements Algorithm {

    public static BigInteger fibonacci(long n) {
        if (n <= 1) {
            return BigInteger.valueOf(n);
        }
        return Matrix2x2.BASE.pow(n - 1).r0c0;
    }

    @Override
    public final List<String> run(String arg, String... other) {
        long n = Long.parseLong(arg);
        String res = String.valueOf(fibonacci(n));
        return List.of(res);
    }

    /**
     * Represents a 2D-matrix:
     * <pre>{@code
     * | r0c0 r0c1 |
     * | r1c0 r1c1 |
     * }</pre>
     */
    public static class Matrix2x2 {
        public static final Matrix2x2 IDENTITY = of(1, 0, 0, 1);
        public static final Matrix2x2 BASE = of(1, 1, 1, 0);

        protected final BigInteger r0c0;
        protected final BigInteger r0c1;
        protected final BigInteger r1c0;
        protected final BigInteger r1c1;

        protected Matrix2x2(BigInteger a, BigInteger b, BigInteger c, BigInteger d) {
            r0c0 = Objects.requireNonNull(a);
            r0c1 = Objects.requireNonNull(b);
            r1c0 = Objects.requireNonNull(c);
            r1c1 = Objects.requireNonNull(d);
        }

        /**
         * Creates a {@code Matrix2x2}:<pre>{@code
         * | a b |
         * | c d |}</pre>
         *
         * @param a {@code long} - a value for first row, first cell
         * @param b {@code long} - a value for first row, last cell
         * @param c {@code long} - a value for last row, first cell
         * @param d {@code long} - a value for last row, last cell
         * @return {@link Matrix2x2}
         */
        public static Matrix2x2 of(long a, long b, long c, long d) {
            return of(BigInteger.valueOf(a), BigInteger.valueOf(b), BigInteger.valueOf(c), BigInteger.valueOf(d));
        }

        /**
         * Creates a {@code Matrix2x2}:<pre>{@code
         * | a b |
         * | c d |}</pre>
         *
         * @param a {@link BigInteger} - a value for first row, first cell
         * @param b {@link BigInteger} - a value for first row, last cell
         * @param c {@link BigInteger} - a value for last row, first cell
         * @param d {@link BigInteger} - a value for last row, last cell
         * @return {@link Matrix2x2}
         */
        public static Matrix2x2 of(BigInteger a, BigInteger b, BigInteger c, BigInteger d) {
            return new Matrix2x2(a, b, c, d);
        }

        private static int maxLength(String... digits) {
            int res = -1;
            for (String x : digits) {
                if (res == -1) {
                    res = x.length();
                } else {
                    res = Math.max(res, x.length());
                }
            }
            return res;
        }

        /**
         * Returns a {@code Matrix2x2} whose value is {@code (this * that)}.
         * The multiplication rule is following: if {@code this} matrix is <pre>{@code
         * | a b |
         * | c d |}</pre>, and {@code that} matrix is <pre>{@code
         * | e f |
         * | g h |}</pre>, then result matrix is  <pre>{@code
         * | (a * e + b * g) (a * f + b * h) |
         * | (c * e + d * g) (c * f + d * h) |}</pre>.
         *
         * @param that {@link Matrix2x2}, a value to be multiplied by this matrix
         * @return {@code this * that}
         */
        public Matrix2x2 multiply(Matrix2x2 that) {
            BigInteger r0c0 = this.r0c0.multiply(that.r0c0).add(this.r0c1.multiply(that.r1c0));
            BigInteger r0c1 = this.r0c0.multiply(that.r0c1).add(this.r0c1.multiply(that.r1c1));

            BigInteger r1c0 = this.r1c0.multiply(that.r0c0).add(this.r1c1.multiply(that.r1c0));
            BigInteger r1c1 = this.r1c0.multiply(that.r0c1).add(this.r1c1.multiply(that.r1c1));
            return of(r0c0, r0c1, r1c0, r1c1);
        }

        /**
         * Returns a {@code Matrix2x2} whose value is <code>(this<sup>exponent</sup>)</code>.
         *
         * @param exponent {@code long} exponent to which this {@code Matrix2x2} is to be raised
         * @return <code>this<sup>exponent</sup></code>
         */
        public Matrix2x2 pow(long exponent) {
            if (exponent < 0) throw new IllegalArgumentException();
            Matrix2x2 an = this;
            Matrix2x2 res = IDENTITY;
            for (long i = exponent; i > 0; i /= 2) {
                if (i % 2 != 0) {
                    res = res.multiply(an);
                }
                if (i > 1) {
                    an = an.multiply(an);
                }
            }
            return res;
        }

        @Override
        public String toString() {
            String sr0c0 = r0c0.toString();
            String sr1c0 = r1c0.toString();
            String sr0c1 = r0c1.toString();
            String sr1c1 = r1c1.toString();
            int length = maxLength(sr0c0, sr1c0, sr0c1, sr1c1) + 1;
            return String.format("|%s%s|\n|%s%s|",
                    StringUtils.rightPad(sr0c0, length),
                    StringUtils.leftPad(sr0c1, length),
                    StringUtils.rightPad(sr1c0, length),
                    StringUtils.leftPad(sr1c1, length));
        }
    }
}
