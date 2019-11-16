package org.gendut.arithmetic;

/*
 * originally from Apache Harmony Project.
 * 
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */


/**
 * Immutable objects describing settings such as rounding mode and digit
 * precision for the numerical operations provided by class {@link LargeDecimal}.
 */
public final class MathContext {

    /**
     * A {@code MathContext} which corresponds to the IEEE 754r quadruple
     * decimal precision format: 34 digit precision and
     * {@link RoundingMode#HALF_EVEN} rounding.
     */
    public static final MathContext DECIMAL128 = new MathContext(34,
    		LargeDecimal.ROUND_HALF_EVEN);

    /**
     * A {@code MathContext} which corresponds to the IEEE 754r single decimal
     * precision format: 7 digit precision and {@link RoundingMode#HALF_EVEN}
     * rounding.
     */
    public static final MathContext DECIMAL32 = new MathContext(7,
    		LargeDecimal.ROUND_HALF_EVEN);

    /**
     * A {@code MathContext} which corresponds to the IEEE 754r double decimal
     * precision format: 16 digit precision and {@link RoundingMode#HALF_EVEN}
     * rounding.
     */
    public static final MathContext DECIMAL64 = new MathContext(16,
    		LargeDecimal.ROUND_HALF_EVEN);

    /**
     * A {@code MathContext} for unlimited precision with
     * {@link RoundingMode#HALF_UP} rounding.
     */
    public static final MathContext UNLIMITED = new MathContext(0,
    		LargeDecimal.ROUND_HALF_UP);

    /**
     * The number of digits to be used for an operation; results are rounded to
     * this precision.
     */
    private int precision;

    /**
     * A {@code RoundingMode} object which specifies the algorithm to be used
     * for rounding.
     */
    private int roundingMode;

    /**
     * An array of {@code char} containing: {@code
     * 'p','r','e','c','i','s','i','o','n','='}. It's used to improve the
     * methods related to {@code String} conversion.
     *
     * @see #MathContext(String)
     * @see #toString()
     */
    private final static char[] chPrecision = { 'p', 'r', 'e', 'c', 'i', 's',
            'i', 'o', 'n', '=' };

    /**
     * An array of {@code char} containing: {@code
     * 'r','o','u','n','d','i','n','g','M','o','d','e','='}. It's used to
     * improve the methods related to {@code String} conversion.
     *
     * @see #MathContext(String)
     * @see #toString()
     */
    private final static char[] chRoundingMode = { 'r', 'o', 'u', 'n', 'd',
            'i', 'n', 'g', 'M', 'o', 'd', 'e', '=' };

    /**
     * Constructs a new {@code MathContext} with the specified precision and
     * with the rounding mode {@link RoundingMode#HALF_UP HALF_UP}. If the
     * precision passed is zero, then this implies that the computations have to
     * be performed exact, the rounding mode in this case is irrelevant.
     *
     * @param precision
     *            the precision for the new {@code MathContext}.
     * @throws IllegalArgumentException
     *             if {@code precision < 0}.
     */
    public MathContext(int precision) {
        this(precision, LargeDecimal.ROUND_HALF_UP);
    }

    /**
     * Constructs a new {@code MathContext} with the specified precision and
     * with the specified rounding mode. If the precision passed is zero, then
     * this implies that the computations have to be performed exact, the
     * rounding mode in this case is irrelevant.
     *
     * @param precision
     *            the precision for the new {@code MathContext}.
     * @param roundingMode
     *            the rounding mode for the new {@code MathContext}.
     * @throws IllegalArgumentException
     *             if {@code precision < 0}.
     * @throws NullPointerException
     *             if {@code roundingMode} is {@code null}.
     */
    public MathContext(int precision, int roundingMode) {
        if (precision < 0) {
            // math.0C=Digits < 0
            throw new IllegalArgumentException(Messages.getString("math.0C")); //$NON-NLS-1$
        }
        this.precision = precision;
        this.roundingMode = roundingMode;
    }

    
    /* Public Methods */

    /**
     * Returns the precision. The precision is the number of digits used for an
     * operation. Results are rounded to this precision. The precision is
     * guaranteed to be non negative. If the precision is zero, then the
     * computations have to be performed exact, results are not rounded in this
     * case.
     *
     * @return the precision.
     */
    public int getPrecision() {
        return precision;
    }

    /**
     * Returns the rounding mode. The rounding mode is the strategy to be used
     * to round results.
     * <p>
     * The rounding mode is one of
     * {@link RoundingMode#UP},
     * {@link RoundingMode#DOWN},
     * {@link RoundingMode#CEILING},
     * {@link RoundingMode#FLOOR},
     * {@link RoundingMode#HALF_UP},
     * {@link RoundingMode#HALF_DOWN},
     * {@link RoundingMode#HALF_EVEN}, or
     * {@link RoundingMode#UNNECESSARY}.
     *
     * @return the rounding mode.
     */
    public int getRoundingMode() {
        return roundingMode;
    }

    /**
     * Returns true if x is a {@code MathContext} with the same precision
     * setting and the same rounding mode as this {@code MathContext} instance.
     *
     * @param x
     *            object to be compared.
     * @return {@code true} if this {@code MathContext} instance is equal to the
     *         {@code x} argument; {@code false} otherwise.
     */
    @Override
    public boolean equals(Object x) {
        return ((x instanceof MathContext)
                && (((MathContext) x).getPrecision() == precision) && (((MathContext) x)
                .getRoundingMode() == roundingMode));
    }

    /**
     * Returns the hash code for this {@code MathContext} instance.
     *
     * @return the hash code for this {@code MathContext}.
     */
    @Override
    public int hashCode() {
        // Make place for the necessary bits to represent 8 rounding modes
        return ((precision << 3) | roundingMode);
    }

    /**
     * Returns the string representation for this {@code MathContext} instance.
     * The string has the form
     * {@code
     * "precision=&lt;precision&gt; roundingMode=&lt;roundingMode&gt;"
     * } where {@code &lt;precision&gt;} is an integer describing the number
     * of digits used for operations and {@code &lt;roundingMode&gt;} is the
     * string representation of the rounding mode.
     *
     * @return a string representation for this {@code MathContext} instance
     */
    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer(45);

        sb.append(chPrecision);
        sb.append(precision);
        sb.append(' ');
        sb.append(chRoundingMode);
        sb.append(roundingMode);
        return sb.toString();
    }


}