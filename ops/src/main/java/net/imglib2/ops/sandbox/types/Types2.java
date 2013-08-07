package net.imglib2.ops.sandbox.types;


import java.math.BigDecimal;
import java.math.BigInteger;


// A port of the Haskell numeric types to Java. Mostly just signatures right now.
// This example pulls apart the association of types with fixed ops. Instead ops
// can be defined for types as separate classes. This is more flexible and
// extensible. However package structure might end up a bit messy. 
// Mostly from:
// http://www.haskell.org/ghc/docs/latest/html/libraries/base/Prelude.html

/*

A criticism of the Haskell Type system I found online:

The Prelude for Haskell 98 offers a well-considered set of numeric classes
which covers the standard numeric types
('Integer', 'Int', 'Rational', 'Float', 'Double', 'Complex') quite well.
But they offer limited extensibility and have a few other flaws.
In this proposal we will revisit these classes, addressing the following concerns:
.
[1] The current Prelude defines no semantics for the fundamental operations.
    For instance, presumably addition should be associative
    (or come as close as feasible),
    but this is not mentioned anywhere.
.
[2] There are some superfluous superclasses.
    For instance, 'Eq' and 'Show' are superclasses of 'Num'.
    Consider the data type
    @   data IntegerFunction a = IF (a -> Integer) @
    One can reasonably define all the methods of 'Algebra.Ring.C' for
    @IntegerFunction a@ (satisfying good semantics),
    but it is impossible to define non-bottom instances of 'Eq' and 'Show'.
    In general, superclass relationship should indicate
    some semantic connection between the two classes.
.
[3] In a few cases, there is a mix of semantic operations and
    representation-specific operations.
    'toInteger', 'toRational',
    and the various operations in 'RealFloating' ('decodeFloat', ...)
    are the main examples.
*/

public class Types2 {

	// **************************************************************
	// TYPES
	// **************************************************************

	/**
	 * Things that can be tested for equality: isEqual, isNotEqual
	 */
	private interface Equable<T> {}

	/**
	 * Things that are ordered and can test less than, greater than, etc.
	 */
	private interface Ordered<T> extends Equable<T> {}

	/**
	 * Things that can be incremented and decremented
	 */
	private interface Enumerable<T> {}

	/**
	 * Things that have bounds that can be queried
	 */
	private interface Bounded<T> {}

	/**
	 * Numbers that can be added, subtracted, raised to powers, etc.
	 */
	private interface Number<T> {}

	/**
	 * Real numbers that can be converted to/from (sometimes approximate)
	 * fractions.
	 */
	private interface Real<T> extends Number<T>, Ordered<T> {}

	/**
	 * Whole numbers with which we can do div, mod, gcd, isOdd, etc.
	 */
	private interface Integral<T> extends Real<T>, Enumerable<T> {}

	/**
	 * A ratio based on Integers
	 */
	private interface Rational extends Ratio<Integer> {
		// nothing to declare
	}

	/**
	 * A fraction
	 */
	private interface Fractional<T> extends Number<T> {}

	/**
	 * A floating point type that supports operations like sin, cos, log, etc.
	 */
	private interface Floating<T> extends Fractional<T> {}

	/**
	 * A real that can support fractional methods like trunc, round, ceil, floor.
	 */
	private interface RealFrac<T> extends Real<T>, Fractional<T> {}

	/**
	 * A real that can support encoding, decoding, isNaN, isInfinite, etc.
	 */
	private interface RealFloat<T> extends RealFrac<T>, Floating<T> {}

	// **************************************************************
	// DATA IMPLEMENTATIONS
	// **************************************************************
	
	private interface Factory<T> {

		T create();

		T copy();
	}

	private interface Access<T> {

		void setValue(T input);

		void getValue(T result);
	}

	// primitive boolean type
	// TODO - make it a class

	private interface Bool extends Factory<Bool>, Access<Bool> {

		// T-based accessors supported by Access
		// primitive based accessors provided here too

		boolean getValue();

		void setValue(boolean b);
	}

	// primitive integer type
	// TODO - here is an example of implementing one of the numeric interfaces

	private class Int implements Integral<Int>, Factory<Int>, Access<Int>,
		Bounded<Int>
	{

		int v;

		public Int() {
			v = 0;
		}

		public Int(Int other) {
			v = other.v;
		}

		public Int(int i) {
			v = i;
		}

		public void setZero() {
			v = 0;
		}

		@Override
		public void setValue(Int input) {
			v = input.v;
		}

		@Override
		public void getValue(Int result) {
			result.setValue(this);
		}

		@Override
		public Int create() {
			return new Int();
		}

		@Override
		public Int copy() {
			return new Int(this);
		}
	}

	// unbounded integer type
	// TODO - here is an example of implementing one of the numeric interfaces

	private class Integer implements Integral<Integer>, Factory<Integer>,
		Access<Integer>
	{

		BigInteger v;

		public Integer() {
			v = BigInteger.ZERO;
		}

		public Integer(Integer other) {
			setValue(other);
		}

		public Integer(BigInteger i) {
			v = i;
		}

		public Integer(long i) {
			v = BigInteger.valueOf(i);
		}

		public void setZero() {
			v = BigInteger.ZERO;
		}

		@Override
		public void setValue(Integer input) {
			v = input.v;
		}

		@Override
		public void getValue(Integer result) {
			result.setValue(this);
		}

		@Override
		public Integer create() {
			return new Integer();
		}

		@Override
		public Integer copy() {
			return new Integer(this);
		}
	}

	// primitive float type (which is bounded)
	// TODO - here is an example of implementing one of the numeric interfaces

	private class Float implements RealFloat<Float>, Factory<Float>,
		Access<Float>, Bounded<Float>
	{

		float v;

		public Float() {
			v = 0;
		}

		public Float(Float other) {
			setValue(other);
		}

		public Float(float i) {
			v = i;
		}

		public void setZero() {
			v = 0;
		}

		@Override
		public void setValue(Float input) {
			v = input.v;
		}

		@Override
		public void getValue(Float result) {
			result.setValue(this);
		}

		@Override
		public Float create() {
			return new Float();
		}

		@Override
		public Float copy() {
			return new Float(this);
		}
	}

	// arbitrarily precise float (unbounded)
	// TODO - here is an example of implementing one of the numeric interfaces

	private class PreciseFloat implements RealFloat<PreciseFloat>,
		Factory<PreciseFloat>, Access<PreciseFloat>
	{

		BigDecimal v;

		public PreciseFloat() {
			v = BigDecimal.ZERO;
		}

		public PreciseFloat(PreciseFloat other) {
			setValue(other);
		}

		public PreciseFloat(BigDecimal i) {
			v = i;
		}

		public PreciseFloat(double i) {
			v = BigDecimal.valueOf(i);
		}

		public void setZero() {
			v = BigDecimal.ZERO;
		}

		@Override
		public void setValue(PreciseFloat input) {
			v = input.v;
		}

		@Override
		public void getValue(PreciseFloat result) {
			result.setValue(this);
		}

		@Override
		public PreciseFloat create() {
			return new PreciseFloat();
		}

		@Override
		public PreciseFloat copy() {
			return new PreciseFloat(this);
		}
	}

	// TODO - some subtyping not enforced here. See definition at:
	// http://www.haskell.org/ghc/docs/latest/html/libraries/base/Data-Complex.html
	// TODO - that might also be why as declared one could have Complex<String>

	/**
	 * A complex number type
	 */
	private interface Complex<T> extends Floating<Complex<T>>,
		Factory<Complex<T>>, Access<Complex<T>>, Equable<Complex<T>>
	{
		// queries:

		void real(T result);

		void imag(T result);

		void cartesian(T realResult, T imagResult);

		void magnitude(T result);

		void phase(T result);

		void polar(T magResult, T phaseResult);
	}

	private class ComplexFloat implements Complex<Float> {

		Float r;
		Float i;

		public ComplexFloat() {
			r = new Float();
			i = new Float();
		}

		public ComplexFloat(ComplexFloat other) {
			this();
			setValue(other);
		}

		public ComplexFloat(float r, float i) {
			this.r = new Float(r);
			this.i = new Float(i);
		}

		public void setZero() {
			r.setZero();
			i.setZero();
		}

		@Override
		public void setValue(Complex<Float> input) {
			input.real(r);
			input.imag(i);
		}

		@Override
		public void getValue(Complex<Float> result) {
			result.setValue(this);
		}

		@Override
		public ComplexFloat create() {
			return new ComplexFloat();
		}

		@Override
		public ComplexFloat copy() {
			return new ComplexFloat(this);
		}

		@Override
		public void real(Float result) {
			result.setValue(r);
		}

		@Override
		public void imag(Float result) {
			result.setValue(i);
		}

		@Override
		public void cartesian(Float realResult, Float imagResult) {
			real(realResult);
			imag(imagResult);
		}

		@Override
		public void magnitude(Float result) {
			result.v = (float) Math.sqrt(r.v * r.v + i.v * i.v);
		}

		@Override
		public void phase(Float result) {
			result.v = (float) Math.atan2(i.v, r.v);
			// TODO - its more complicated than this. Can grab code from OPS'
			// ComplexHelper class.
		}

		@Override
		public void polar(Float magResult, Float phaseResult) {
			magnitude(magResult);
			phase(phaseResult);
		}

	}

	// TODO - one can add more data types like UnsignedByte, etc.
	// TODO - how could Quaternions fit in here?

	// **************************************************************
	// OPS
	// **************************************************************

	// TODO - there is much duplication in method sigs of OPs. Could define them
	// more economically but this is a quick throw together example.

	// Equable OPS --------------------------------------------------

	private interface IsEqualOp<T extends Equable<T>> {

		void compute(T a, T b, Bool result);
	}

	private interface IsNotEqualOp<T extends Equable<T>> {

		void compute(T a, T b, Bool result);
	}

	// Ordered OPS --------------------------------------------------

	private interface CompareOp<T extends Ordered<T>> {

		int compute(T a, T b);
	}

	private interface IsLessOp<T extends Ordered<T>> {

		boolean compute(T a, T b, Bool result);
	}

	private interface IsLessEqualOp<T extends Ordered<T>> {

		boolean compute(T a, T b, Bool result);
	}

	private interface IsGreaterOp<T extends Ordered<T>> {

		boolean compute(T a, T b, Bool result);
	}

	private interface IsGreaterEqualOp<T extends Ordered<T>> {

		boolean compute(T a, T b, Bool result);
	}

	private interface MaxOp<T extends Ordered<T>> {

		boolean compute(T a, T b, Bool result);
	}

	private interface MinOp<T extends Ordered<T>> {

		boolean compute(T a, T b, Bool result);
	}

	// Enumerable OPS --------------------------------------------------

	private interface SuccOp<T extends Enumerable<T>> {

		void compute(T a, T result);
	}

	private interface PredOp<T extends Enumerable<T>> {

		void compute(T a, T result);
	}

	// Bounded OPS --------------------------------------------------

	private interface MinBoundOp<T extends Bounded<T>> {

		void compute(T result);
	}
	
	private interface MaxBoundOp<T extends Bounded<T>> {

		void compute(T result);
	}
	
	// Number OPS --------------------------------------------------

	private interface AddOp<T extends Number<T>> {

		void compute(T a, T b, T result);
	}
	
	private interface SubtractOp<T extends Number<T>> {

		void compute(T a, T b, T result);
	}
	
	// ? in type required for compilation of ComplexFloat examples below : HACK?

	private interface MultiplyOp<T extends Number<?>> {

		void compute(T a, T b, T result);
	}
	
	private interface DivideOp1<T extends Number<T>> {

		void compute(T a, T b, T result);
	}

	// this is less restrictive than original
	private interface PowerOp1<T extends Number<T>> {

		void compute(T a, T b, T result);
	}

	private interface NegateOp<T extends Number<T>> {

		void compute(T a, T result);
	}
	
	private interface AbsOp<T extends Number<T>> {

		void compute(T a, T result);
	}
	
	private interface SignumOp<T extends Number<T>> {

		void compute(T a, T result);
	}
	
	private interface FromIntegerOp<T extends Number<T>> {

		void compute(Integer a, T result);
	}
	
	// Integral OPS --------------------------------------------------

	private interface QuotientOp<T extends Integral<T>> {

		void compute(T a, T b, T result);
	}
	
	private interface RemainderOp<T extends Integral<T>> {

		void compute(T a, T b, T result);
	}

	private interface QuotientRemainderOp<T extends Integral<T>> {

		void compute(T a, T b, T quotResult, T remResult);
	}

	private interface DivOp<T extends Integral<T>> {

		void compute(T a, T b, T result);
	}

	private interface ModOp<T extends Integral<T>> {

		void compute(T a, T b, T result);
	}

	private interface DivModOp<T extends Integral<T>> {

		void compute(T a, T b, T divResult, T modResult);
	}

	private interface ToIntegerOp<T extends Integral<T>> {

		void compute(T a, Integer result);
	}

	private interface IsEvenOp<T extends Integral<T>> {

		void compute(T a, Bool result);
	}

	private interface IsOddOp<T extends Integral<T>> {

		void compute(T a, Bool result);
	}

	private interface GcdOp<T extends Integral<T>> {

		void compute(T a, T b, T result);
	}

	private interface LcmOp<T extends Integral<T>> {

		void compute(T a, T b, T result);
	}

	private interface FromIntegralOp<T extends Integral<T>> {

		<Z> void compute(T a, Number<Z> result);
	}

	// Real OPS --------------------------------------------------
	
	private interface ToRationalOp<T extends Real<T>> {

		void compute(T a, Rational result);
	}

	private interface RealToFracOp<T extends Real<T>> {

		<Z> void compute(T a, Fractional<Z> result);
	}

	// Ratio OPS --------------------------------------------------

	private interface Ratio<T> {

		Ratio<T> create(Integral<T> numer, Integral<T> denom);

		void numerator(T result);

		void denominator(T result);
	}

	// TODO: not an OP?
	private interface CreateRatioOp<T> {

		void compute(Integral<T> numer, Integral<T> denom, Ratio<T> result);
	}
	
	private interface ApproxRationalOp<T> {

		void compute(RealFrac<T> a, RealFrac<T> b, Rational result);
	}

	// Rational OPS --------------------------------------------------

	// Fractional OPS --------------------------------------------------

	private interface DivideOp2<T extends Fractional<T>> {

		void compute(T a, T b, T result);
	}

	private interface RecipOp<T extends Fractional<T>> {

		void compute(T a, T result);
	}

	private interface FromRationalOp<T extends Fractional<T>> {

		void compute(Rational a, T result);
	}

	private interface PowerOp2<T extends Fractional<T>> {

		<Z> void compute(T a, Integral<Z> b, T result);
	}

	// Floating OPS --------------------------------------------------

	// the constant PI

	// ? in type required for compilation of ComplexFloat examples below : HACK?

	private interface PiOp<T extends Fractional<?>> {

		void compute(T result);
	}

	// the constant E

	private interface EOp<T extends Fractional<T>> {

		void compute(T result);
	}
	
	// ? in type required for compilation of ComplexFloat examples below : HACK?

	private interface ExpOp<T extends Fractional<?>> {

		void compute(T a, T result);
	}

	private interface SqrtOp<T extends Fractional<T>> {

		void compute(T a, T result);
	}

	// ? in type required for compilation of ComplexFloat examples below : HACK?

	private interface LogOp<T extends Fractional<?>> {

		void compute(T a, T result);
	}

	// ? in type required for compilation of ComplexFloat examples below : HACK?

	private interface PowOp<T extends Fractional<?>> {

		void compute(T a, T b, T result);
	}
	
	private interface SinOp<T extends Fractional<T>> {

		void compute(T a, T result);
	}

	private interface CosOp<T extends Fractional<T>> {

		void compute(T a, T result);
	}
	
	private interface TanOp<T extends Fractional<T>> {

		void compute(T a, T result);
	}
	
	private interface ArcSinOp<T extends Fractional<T>> {

		void compute(T a, T result);
	}
	
	private interface ArcCosOp<T extends Fractional<T>> {

		void compute(T a, T result);
	}
	
	private interface ArcTanOp<T extends Fractional<T>> {

		void compute(T a, T result);
	}
	
	private interface SinhOp<T extends Fractional<T>> {

		void compute(T a, T result);
	}
	
	private interface CoshOp<T extends Fractional<T>> {

		void compute(T a, T result);
	}
	
	private interface TanhOp<T extends Fractional<T>> {

		void compute(T a, T result);
	}
	
	private interface ArcSinhOp<T extends Fractional<T>> {

		void compute(T a, T result);
	}
	
	private interface ArcCoshOp<T extends Fractional<T>> {

		void compute(T a, T result);
	}
	
	private interface ArcTanhOp<T extends Fractional<T>> {

		void compute(T a, T result);
	}

	// RealFrac OPS --------------------------------------------------

	private interface ProperFractionOp<T extends RealFrac<T>> {

		<Z> void compute(T a, Integral<Z> intResult, T fracResult);
	}

	private interface TruncateOp<T extends RealFrac<T>> {

		<Z> void compute(T a, Integral<Z> result);
	}

	private interface RoundOp<T extends RealFrac<T>> {

		<Z> void compute(T a, Integral<Z> result);
	}

	private interface CeilingOp<T extends RealFrac<T>> {

		<Z> void compute(T a, Integral<Z> result);
	}

	private interface FloorOp<T extends RealFrac<T>> {

		<Z> void floor(T a, Integral<Z> result);
	}

	// RealFloat OPS --------------------------------------------------

	private interface FloatRadixOp<T extends RealFloat<T>> {

		void compute(T a, Integer result);
	}

	private interface FloatDigitsOp<T extends RealFloat<T>> {

		void compute(T a, Int result);
	}

	private interface FloatRangeOp<T extends RealFloat<T>> {

		void compute(T a, Int minResult, Int maxResult);
	}

	private interface DecodeFloatOp<T extends RealFloat<T>> {

		void compute(T a, Integer significandResult, Int exponentResult);
	}

	private interface EncodeFloatOp<T extends RealFloat<T>> {

		void compute(Integer significand, Int exponent, T result);
	}

	private interface ExponentOp<T extends RealFloat<T>> {

		void compute(T a, Int result);
	}

	private interface SignificandOp<T extends RealFloat<T>> {

		void compute(T a, T result);
	}

	private interface ScaleFloatOp<T extends RealFloat<T>> {

		void compute(T a, Int power, T result);
	}

	private interface isNanOp<T extends RealFloat<T>> {

		boolean compute(T a, Bool result);
	}

	private interface isInfiniteOp<T extends RealFloat<T>> {

		boolean compute(T a, Bool result);
	}

	private interface isDenormalizedOp<T extends RealFloat<T>> {

		boolean compute(T a, Bool result);
	}

	private interface isNegativeZeroOp<T extends RealFloat<T>> {

		boolean compute(T a, Bool result);
	}

	private interface isIEEEOp<T extends RealFloat<T>> {

		boolean compute(T a, Bool result);
	}

	private interface Atan2Op<T extends RealFloat<T>> {

		void compute(T x, T y, T result);
	}

	// Complex OPS --------------------------------------------------

	private interface MakePolarOp<T> {

		Complex<T> compute(RealFloat<T> mag, RealFloat<T> phase);
	}

	private interface MakeCartesianOp<T> {

		Complex<T> compute(RealFloat<T> mag, RealFloat<T> phase);
	}
	
	// Passing a CisOp a t in range 0..2*pi it returns a complex value with
	// magnitude 1 and phase t.
	// TODO - maybe a constructor
	private interface CisOp<T extends RealFloat<T>> {

		void compute(T t, Complex<T> result);
	}
	
	private interface ConjugateOp<T> {

		void compute(Complex<T> a, Complex<T> result);
	}

	/**************
	 * Experiments
	 **************/

	//
	// try fleshing out some Ops for bounded primitive Ints
	//

	private class IntEqualOp implements IsEqualOp<Int> {

		@Override
		public void compute(Int a, Int b, Bool result) {
			result.setValue(a.v == b.v);
		}

	}

	private class IntNotEqualOp implements IsNotEqualOp<Int> {

		@Override
		public void compute(Int a, Int b, Bool result) {
			result.setValue(a.v != b.v);
		}

	}

	private class IntCompareOp implements CompareOp<Int> {

		@Override
		public int compute(Int a, Int b) {
			if (a.v < b.v) return -1;
			if (a.v > b.v) return 1;
			return 0;
		}

	}

	private class IntLessOp implements IsLessOp<Int> {

		@Override
		public boolean compute(Int a, Int b, Bool result) {
			result.setValue(a.v < b.v);
			return result.getValue();
		}

	}

	private class IntGreaterEqualOp implements IsGreaterEqualOp<Int> {

		@Override
		public boolean compute(Int a, Int b, Bool result) {
			result.setValue(a.v >= b.v);
			return result.getValue();
		}

	}

	private class IntSuccOp implements SuccOp<Int> {

		@Override
		public void compute(Int a, Int result) {
			result.v = a.v + 1;
		}

	}

	private class IntPredOp implements PredOp<Int> {

		@Override
		public void compute(Int a, Int result) {
			result.v = a.v - 1;
		}

	}

	private class IntMaxBoundOp implements MaxBoundOp<Int> {

		// TODO: make static
		private Int max = new Int(java.lang.Integer.MAX_VALUE);

		@Override
		public void compute(Int result) {
			result.setValue(max);
		}
	}

	private class IntMinBoundOp implements MinBoundOp<Int> {

		// TODO: make static
		private Int min = new Int(java.lang.Integer.MIN_VALUE);

		@Override
		public void compute(Int result) {
			result.setValue(min);
		}
	}

	private class IntAddOp implements AddOp<Int> {

		@Override
		public void compute(Int a, Int b, Int result) {
			result.v = a.v + b.v;
		}

	}

	private class IntMultiplyOp implements MultiplyOp<Int> {

		@Override
		public void compute(Int a, Int b, Int result) {
			result.v = a.v * b.v;
		}

	}

	private class IntNegateOp implements NegateOp<Int> {

		@Override
		public void compute(Int a, Int result) {
			result.v = -a.v;
		}

	}

	private class IntQuotientOp implements QuotientOp<Int> {

		@Override
		public void compute(Int a, Int b, Int result) {
			result.v = a.v / b.v;
		}

	}

	private class IntIsEvenOp implements IsEvenOp<Int> {

		@Override
		public void compute(Int a, Bool result) {
			result.setValue((a.v & 1) == 0);
		}

	}

	//
	// try fleshing out some Ops for unbounded Integers
	//

	private class IntegerEqualOp implements IsEqualOp<Integer> {

		@Override
		public void compute(Integer a, Integer b, Bool result) {
			result.setValue(a.v.equals(b.v));
		}

	}

	private class IntegerNotEqualOp implements IsNotEqualOp<Integer> {

		@Override
		public void compute(Integer a, Integer b, Bool result) {
			result.setValue(!a.v.equals(b.v));
		}

	}

	private class IntegerCompareOp implements CompareOp<Integer> {

		@Override
		public int compute(Integer a, Integer b) {
			return a.v.compareTo(b.v);
		}

	}

	private class IntegerLessOp implements IsLessOp<Integer> {

		@Override
		public boolean compute(Integer a, Integer b, Bool result) {
			result.setValue(a.v.compareTo(b.v) < 0);
			return result.getValue();
		}

	}

	private class IntegerGreaterEqualOp implements IsGreaterEqualOp<Integer> {

		@Override
		public boolean compute(Integer a, Integer b, Bool result) {
			result.setValue(a.v.compareTo(b.v) >= 0);
			return result.getValue();
		}

	}

	private class IntegerSuccOp implements SuccOp<Integer> {

		@Override
		public void compute(Integer a, Integer result) {
			result.v = a.v.add(BigInteger.ONE);
		}

	}

	private class IntegerPredOp implements PredOp<Integer> {

		@Override
		public void compute(Integer a, Integer result) {
			result.v = a.v.subtract(BigInteger.ONE);
		}

	}

	private class IntegerAddOp implements AddOp<Integer> {

		@Override
		public void compute(Integer a, Integer b, Integer result) {
			result.v = a.v.add(b.v);
		}

	}

	private class IntegerMultiplyOp implements MultiplyOp<Integer> {

		@Override
		public void compute(Integer a, Integer b, Integer result) {
			result.v = a.v.multiply(b.v);
		}

	}

	private class IntegerNegateOp implements NegateOp<Integer> {

		@Override
		public void compute(Integer a, Integer result) {
			result.v = a.v.negate();
		}

	}

	private class IntegerQuotientOp implements QuotientOp<Integer> {

		@Override
		public void compute(Integer a, Integer b, Integer result) {
			result.v = a.v.divide(b.v);
		}

	}

	private class IntegerIsEvenOp implements IsEvenOp<Integer> {

		@Override
		public void compute(Integer a, Bool result) {
			BigInteger x = a.v.and(BigInteger.ONE);
			result.setValue(x.compareTo(BigInteger.ZERO) == 0);
		}

	}

	//
	// now define some casting OPs
	//

	private interface CastOp<A, B> {

		void compute(A input, B result);
	}

	// TODO - in future must decide how to package classes such that this
	// operation can be defined. It uses package access from two classes.

	private class FromIntToIntegerCastOp implements CastOp<Int, Integer> {

		@Override
		public void compute(Int input, Integer result) {
			result.v = BigInteger.valueOf(input.v); // widening
		}

	}

	// TODO - in future must decide how to package classes such that this
	// operation can be defined. It uses package access from two classes.

	private class FromIntegerToIntCastOp implements CastOp<Integer, Int> {

		@Override
		public void compute(Integer input, Int result) {
			result.v = input.v.intValue(); // narrowing
		}

	}

	//
	// try fleshing out some Ops for ComplexFloat types that show we can use the
	// Floating interface with them
	//

	private class ComplexFloatLogOp implements LogOp<ComplexFloat> {
		
		private Float modulus = new Float();
		private Float argument = new Float();
		
		@Override
		public void compute(ComplexFloat a, ComplexFloat result) {
			a.polar(modulus, argument);
			result.r.v = (float) Math.log(modulus.v);
			result.i.v = getPrincipleArgument(argument.v);
		}

		private float getPrincipleArgument(float v) {
			final double TWO_PI = 2 * Math.PI;
			while (v <= -Math.PI) {
				v += TWO_PI;
			}
			while (v > Math.PI) {
				v -= TWO_PI;
			}
			return v;
		}
	}

	private class ComplexFloatExpOp implements ExpOp<ComplexFloat> {

		@Override
		public void compute(ComplexFloat a, ComplexFloat result) {
			double constant = Math.exp(a.r.v);
			result.r.v = (float) (constant * Math.cos(a.i.v));
			result.i.v = (float) (constant * Math.sin(a.i.v));
		}

	}

	private class ComplexFloatMultiplyOp implements MultiplyOp<ComplexFloat> {

		@Override
		public void compute(ComplexFloat a, ComplexFloat b, ComplexFloat result) {
			result.r.v = a.r.v * b.r.v - a.i.v * b.i.v;
			result.i.v = a.i.v * b.r.v + a.r.v * b.i.v;
		}

	}

	private class ComplexFloatPowOp implements PowOp<ComplexFloat> {

		// -- ops used for computations - TODO - make ops static

		private ComplexFloatLogOp logOp = new ComplexFloatLogOp();
		private ComplexFloatMultiplyOp mulOp = new ComplexFloatMultiplyOp();
		private ComplexFloatExpOp expOp = new ComplexFloatExpOp();

		// -- working vars --

		private ComplexFloat logA = new ComplexFloat();
		private ComplexFloat bLogA = new ComplexFloat();

		@Override
		public void compute(ComplexFloat a, ComplexFloat b, ComplexFloat result) {
			logOp.compute(a, logA);
			mulOp.compute(b, logA, bLogA);
			expOp.compute(bLogA, result);
		}

	}

	private class ComplexFloatPiOp implements PiOp<ComplexFloat> {

		@Override
		public void compute(ComplexFloat result) {
			result.r.v = (float) Math.PI;
			result.i.v = 0;
		}

	}

	// TODO - there are some lines marked HACK? above. See what ramifications are
	// of making all OPS into T extends SomeType<?>. Or is there an issue with my
	// ComplexFloat definition? (Note, I tried making ConjugateOp enforce T
	// extends Number<?> and ran into trouble trying to define one).

	//
	// try fleshing out some Ops for PreciseFloats
	//

	private class PreciseFloatPiOp implements PiOp<PreciseFloat> {

		// TODO - make static
		private final String PI_40 = "3.1415926535897932384626433832795028841971";
		private final BigDecimal SUPER_PI = new BigDecimal(PI_40);

		@Override
		public void compute(PreciseFloat result) {
			result.v = SUPER_PI;
		}

	}

	private class PreciseFloatEOp implements EOp<PreciseFloat> {

		// TODO - make static
		private final String E_40 = "2.7182818284590452353602874713526624977572";
		private final BigDecimal SUPER_E = new BigDecimal(E_40);

		@Override
		public void compute(PreciseFloat result) {
			result.v = SUPER_E;
		}

	}

	private class PreciseFloatAddOp implements AddOp<PreciseFloat> {

		@Override
		public void compute(PreciseFloat a, PreciseFloat b, PreciseFloat result) {
			result.v = a.v.add(b.v);
		}

	}

	private class PreciseFloatSubtractOp implements SubtractOp<PreciseFloat> {

		@Override
		public void compute(PreciseFloat a, PreciseFloat b, PreciseFloat result) {
			result.v = a.v.subtract(b.v);
		}

	}

	private class PreciseFloatMultiplyOp implements MultiplyOp<PreciseFloat> {

		@Override
		public void compute(PreciseFloat a, PreciseFloat b, PreciseFloat result) {
			result.v = a.v.multiply(b.v);
		}

	}

	private class PreciseFloatDivideOp implements DivideOp1<PreciseFloat> {

		@Override
		public void compute(PreciseFloat a, PreciseFloat b, PreciseFloat result) {
			result.v = a.v.divide(b.v);
		}

	}

	private class PreciseFloatPowerOp implements PowerOp1<PreciseFloat> {

		@Override
		public void compute(PreciseFloat a, PreciseFloat b, PreciseFloat result) {
			throw new UnsupportedOperationException("TODO");
		}

	}

	// more general signature than usual: rather than Integral<?> a

	private interface FactorialOp<T> {

		void compute(T a, T result);
	}

	private class PreciseFloatFactorialOp implements FactorialOp<PreciseFloat> {

		// TODO - make static
		private final PreciseFloat ONE = new PreciseFloat(BigDecimal.ONE);
		private IntSuccOp succOp = null;
		private PreciseFloatMultiplyOp multiplyOp = null;
		private IsLessOp<Int> lessOp = null; // TODO
		private AddOp<PreciseFloat> addOp = null;

		@Override
		public void compute(PreciseFloat a, PreciseFloat result) {
			Bool isLess = null; // TODO
			PreciseFloat n = new PreciseFloat(2);
			PreciseFloat accum = new PreciseFloat(1);
			Int max = new Int(a.v.intValue());
			for (Int i = new Int(2); lessOp.compute(i, max, isLess); succOp.compute(
				i, i))
			{
				multiplyOp.compute(accum, n, accum);
				addOp.compute(n, ONE, n);
			}
			result.setValue(accum);
		}

	}

	private class PreciseFloatLogOp implements LogOp<PreciseFloat> {

		@Override
		public void compute(PreciseFloat a, PreciseFloat result) {
			throw new UnsupportedOperationException("TODO");
		}

	}

	private class PreciseFloatExpOp implements ExpOp<PreciseFloat> {

		// TODO - make static
		private PreciseFloatAddOp addOp = new PreciseFloatAddOp();
		private PreciseFloatDivideOp divideOp = new PreciseFloatDivideOp();
		private PreciseFloatPowerOp powerOp = new PreciseFloatPowerOp();
		private PreciseFloatFactorialOp factOp = new PreciseFloatFactorialOp();

		PreciseFloat sum = new PreciseFloat(BigDecimal.ONE);
		PreciseFloat term = new PreciseFloat();
		PreciseFloat powTerm = new PreciseFloat();
		PreciseFloat factTerm = new PreciseFloat();
		PreciseFloat n = new PreciseFloat();

		// exp(a) = sum(a^n/n!) for n = 0 to infinity
		// Naive approach: sum for a small num of terms
		// TODO - search lit for better approximations

		@Override
		public void compute(PreciseFloat a, PreciseFloat result) {
			for (int i = 1; i < 8; i++) {
				n.v = new BigDecimal(i);
				powerOp.compute(a, n, powTerm);
				factOp.compute(n, factTerm);
				divideOp.compute(powTerm, factTerm, term);
				addOp.compute(sum, term, sum);
			}
			result.setValue(sum);
		}

	}

	private class PreciseFloatSqrtOp implements SqrtOp<PreciseFloat> {

		// TODO - make static
		private PreciseFloatMultiplyOp multiplyOp = new PreciseFloatMultiplyOp();
		private PreciseFloatLogOp logOp = null;
		private PreciseFloatExpOp expOp = null;

		private PreciseFloat log = new PreciseFloat();
		private PreciseFloat prod = new PreciseFloat();
		private PreciseFloat half = new PreciseFloat(new BigDecimal("0.5"));

		@Override
		public void compute(PreciseFloat a, PreciseFloat result) {
			// From wikipedia: sqrt(a) = e ^ (0.5 * ln(a))
			logOp.compute(a, log);
			multiplyOp.compute(half, log, prod);
			expOp.compute(prod, result);
		}

	}
}