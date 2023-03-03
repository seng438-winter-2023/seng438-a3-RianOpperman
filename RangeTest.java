package org.jfree.data;

import static org.junit.Assert.*;
import org.jfree.data.Range;
import org.junit.*;
import org.junit.rules.ExpectedException;

import java.security.InvalidParameterException;

public class RangeTest{
	private Range exampleRange;
	
	// Used to detect when an exception has been thrown
	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	// Sets up a Range object for use later in every test
	@Before
	public void setUp() throws Exception {
		exampleRange = new Range(-5, 5);
	}
	
	// -----------------------------------------------------------
	// getLowerBound()
	
	// Tests getting the lower bound from a Range object
	@Test
	public void getLowerBoundTest() {
		assertEquals("Retrieved lower bound incorrect",
				-5, exampleRange.getLowerBound(), .000000001d);
	}

	// -----------------------------------------------------------
	// getUpperBound()
	
	// Tests getting the upper bound from a Range object
	@Test
	public void getUpperBoundTest() {
		assertEquals("Retrieved upper bound incorrect",
				5, exampleRange.getUpperBound(), .000000001d);
	}

	// -----------------------------------------------------------
	// getLength()
	
	// 
	@Test
	public void getLengthTest() {
		assertEquals("Retrieved length is incorrect", 
				10, exampleRange.getLength(),.000000001d);
	}

	// -----------------------------------------------------------
	// getCentralValue()
	
	// 
	@Test
	public void getCentralValueTest() {
		assertEquals("Retrieved central value is incorrect", 
				0, exampleRange.getCentralValue(),.000000001d);
	}

	// -----------------------------------------------------------
	// contains()
	
	// Contains() method is tested by sending a value which is out of range
	@Test
	public void testContainsOutOfRange() {
		double testValue = -6;
		assertFalse( exampleRange.contains(testValue));
	}
	
	// Contains() method is tested by sending a value which is within the range
	@Test
	public void testContainsInRange() {
		double testValue1 = 3;
		assertTrue( exampleRange.contains(testValue1));	
	}

	// -----------------------------------------------------------
	// intersects()
	@Test
	public void testIntersectBelowSpecifiedRange() {
		assertFalse(exampleRange.intersects(6, 10));
	}
	
	@Test
	public void testIntersectAtLowervSpecifiedRange() {
		assertTrue(exampleRange.intersects(4, 10));
	}
	@Test
	public void testIntersectAboveSpecifiedRange() {
		assertFalse(exampleRange.intersects(-10, -6));
	}
	
	@Test
	public void testIntersectAtUpperSpecifiedRange() {
		assertTrue(exampleRange.intersects(-10, -4));
	}
	
	@Test
	public void testIntersectBelowGivenRange() {
		Range SpecifiedRange = new Range(6,10);
		assertFalse(exampleRange.intersects(SpecifiedRange));
	}
	
	@Test
	public void testIntersectAtLowervGivenRange() {
		Range SpecifiedRange = new Range(4,10);
		assertTrue(exampleRange.intersects(SpecifiedRange));
	}
	@Test
	public void testIntersectAboveGivenRange() {
		Range SpecifiedRange = new Range(-10,-6);
		assertFalse(exampleRange.intersects(SpecifiedRange));
	}
	
	@Test
	public void testIntersectAtUpperGivenRange() {
		Range SpecifiedRange = new Range(-10,-4);
		assertTrue(exampleRange.intersects(SpecifiedRange));
			
	}
	
	@Test 
	public void testIntersectWhenRangeIsNull() throws InvalidParameterException{
		exception.expect(InvalidParameterException.class);
		exampleRange.intersects(null);	
	}
	
	// -----------------------------------------------------------
	// constrain()
	@Test
	public void testConstraintBelowRange() {
		double testValue = -6;
		assertEquals("Return value should be -5",-5,
			exampleRange.constrain(testValue), 0.000000001d);		
	}
	
	//The Constraint() method is test when a value in the range 
	@Test
	public void testConstraintInRange() {
		double testValue = 3;
		assertEquals("Return value should be 3", 3,
			exampleRange.constrain(testValue), 0.000000001d);		
	}
	
	//The Constraint() method is test when a value above the range 
	@Test
	public void testConstraintAboveRange() {
		double testValue = 7;
		assertEquals("Return value should be 7", 7,
			exampleRange.constrain(testValue), 0.000000001d);	
	}

	// -----------------------------------------------------------
	// comnbine()
	@Test
	public void testCombineBothNull(){
		Range ret = Range.combine(null, null);
		assertEquals(ret, null);
	}

	@Test
	public void testCombineRange1Null(){
		Range ret = Range.combine(null, exampleRange);
		Range reference = new Range(-5, 5);
		assertEquals(reference, ret);
	}

	@Test
	public void testCombineRange2Null(){
		Range ret = Range.combine(exampleRange, null);
		Range reference = new Range(-5, 5);
		assertEquals(ret, reference);
	}

	// Removed tests where range1 or range2 had NaN in them

	/* Need to test if:
	 * range1 == range2
	 * range2 in range1
	 * range2 out of range1
	 * range1 besides range2
	 */	
	@Test
	public void testCombine(){
		Range range1 = new Range(-10, 10);
		Range range2 = new Range(-10, 10);
		Range ret = Range.combine(range1, range2);
		assertEquals(ret, range1);

		range2 = new Range(-5, 5);
		ret = Range.combine(range1, range2);
		assertEquals(ret, range1);

		range2 = new Range(-30, -20);
		ret = Range.combine(range1, range2);
		assertEquals(ret, new Range(-30, 10));

		range2 = new Range(10, 30);
		ret = Range.combine(range1, range2);
		assertEquals(ret, new Range(-10, 30));
	}

	// -----------------------------------------------------------
	// combineIgnoreNaN()
	@Test
	public void testCombineIgnoreNaNBothNull(){
		Range ret = Range.combineIgnoringNaN(null, null);
		assertEquals(ret, null);
	}
	
	@Test
	public void testCombineIgnoreNaNBothNaN() {
		Range range1 = new Range(Double.NaN, Double.NaN);
		Range range2 = new Range(Double.NaN, Double.NaN);
		Range ret = Range.combineIgnoringNaN(range1, range2);
		assertEquals(ret, null);
	}

	@Test
	public void testCombineIgnoreNaNRange1Null(){
		Range ret = Range.combineIgnoringNaN(null, exampleRange);
		assertEquals(ret, exampleRange);
	}

	@Test
	public void testCombineIgnoreNaNRange1NullRange2NaN(){
		Range range2 = new Range(Double.NaN, Double.NaN);
		Range ret = Range.combineIgnoringNaN(null, range2);
		assertEquals(ret, null);

		range2 = new Range(Double.NaN, 5);
		ret = Range.combineIgnoringNaN(null, range2);
		assertTrue(Double.isNaN(ret.getLowerBound()));
		assertTrue(ret.getUpperBound() == 5);

		range2 = new Range(-5, Double.NaN);
		ret = Range.combineIgnoringNaN(null, range2);
		assertTrue(Double.isNaN(ret.getUpperBound()));
		assertTrue(ret.getLowerBound() == -5);
	}

	@Test
	public void testCombineIgnoreNaNRange2Null(){
		Range ret = Range.combineIgnoringNaN(exampleRange, null);
		assertEquals(ret, exampleRange);
	}

	@Test
	public void testCombineIgnoreNaNRange2NullRange1NaN(){
		Range range1 = new Range(Double.NaN, Double.NaN);
		Range ret = Range.combineIgnoringNaN(range1, null);
		assertEquals(ret, null);

		range1 = new Range(Double.NaN, 5);
		ret = Range.combineIgnoringNaN(range1, null);
		assertTrue(Double.isNaN(ret.getLowerBound()));
		assertTrue(ret.getUpperBound() == 5);

		range1 = new Range(-5, Double.NaN);
		ret = Range.combineIgnoringNaN(range1, null);
		assertTrue(Double.isNaN(ret.getUpperBound()));
		assertTrue(ret.getLowerBound() == -5);
	}

	/* Need to test if:
	 * range1 == range2
	 * range2 in range1
	 * range2 out of range1
	 * range1 besides range2
	 */
	@Test
	public void testCombineIgnoreNaN(){
		Range range2 = new Range(-5, 5);
		Range ret = Range.combineIgnoringNaN(exampleRange, range2);
		assertEquals(ret, exampleRange);

		range2 = new Range(-3, 3);
		ret = Range.combineIgnoringNaN(exampleRange, range2);
		assertEquals(ret, exampleRange);

		range2 = new Range(8, 10);
		ret = Range.combineIgnoringNaN(exampleRange, range2);
		assertEquals(ret, new Range(-5, 10));

		range2 = new Range(5, 20);
		ret = Range.combineIgnoringNaN(exampleRange, range2);
		assertEquals(ret, new Range(-5, 20));
	}

	// -----------------------------------------------------------
	// expandToInclude()
	@Test
	public void testExpandToIncludeNullRange(){
		Range ret = Range.expandToInclude(null, Double.NaN);
		assertTrue(Double.isNaN(ret.getLowerBound()));
		assertTrue(Double.isNaN(ret.getUpperBound()));

		ret = Range.expandToInclude(null, 5);
		assertEquals(ret, new Range(5, 5));
	}

	@Test
	public void testExpandToIncludeValLow(){
		Range ret = Range.expandToInclude(exampleRange, -10);
		assertEquals(ret, new Range(-10, 5));
	}

	@Test
	public void testExpandToIncludeValNaN(){
		Range ret = Range.expandToInclude(exampleRange, Double.NaN);
		assertTrue(Double.isNaN(ret.getLowerBound()));
		assertTrue(ret.getUpperBound() == 5);
	}

	@Test
	public void testExpandToIncludeValHigh(){
		Range ret = Range.expandToInclude(exampleRange, 200);
		assertEquals(ret, new Range(-5, 200));
	}
	
	@Test
	public void testExpandToInclude(){
		Range ret = Range.expandToInclude(exampleRange, 0);
		assertEquals(ret, new Range(-5, 5));
	}

	// -----------------------------------------------------------
	// expand()
	@Test
	public void testExpandRangeNull() throws IllegalArgumentException{
		exception.expect(IllegalArgumentException.class);
		Range ret = Range.expand(null, 0.5, 0.5);
	}

	@Test
	public void testExpandRangeNaN(){
		Range ret = Range.expand(new Range(Double.NaN, Double.NaN), 0.5, 0.5);
		// Should throw error????
	}

	@Test
	public void testExpandNegLowerNegUpper(){
		Range ret = Range.expand(exampleRange, -0.25, -0.25);
		assertEquals(ret, new Range(-2.5, 2.5));
	}

	@Test
	public void testExpandNegLower(){
		Range ret = Range.expand(exampleRange, -0.25, 0.5);
		assertEquals(ret, new Range(-2.5, 10));
	}

	@Test
	public void testExpandNegUpper(){
		Range ret = Range.expand(exampleRange, 0.5, -0.25);
		assertEquals(ret, new Range(-10, 2.5));
	}


	@Test
	public void testExpandNaNLower(){
		Range ret = Range.expand(exampleRange, Double.NaN, 0.5);
		// throws error????
	}

	@Test
	public void testExpandNaNUpper(){
		Range ret = Range.expand(exampleRange, 0.5, Double.NaN);
		// throws error????
	}

	@Test
	public void testExpandLowerGreaterThanUpper(){
		Range ret = Range.expand(exampleRange, -0.25, -1);
		assertEquals(ret, new Range(-5, -2.5));
		// Is this meant to stop upper from crossing lower, or is it meant to
		// flip upper and lower so upper = lower and lower = upper?
	}
	
	@Test
	public void testExpand(){
		Range ret = Range.expand(exampleRange, 0.5, 0.5);
		assertEquals(-10, 10);
	}

	// -----------------------------------------------------------
	// shift()
	@Test
	public void testShiftRangeNull() throws IllegalArgumentException{
		exception.expect(IllegalArgumentException.class);
		Range ret = Range.shift(null, 5);
	}

	@Test
	public void testShiftRangeNaN(){
		Range ret = Range.shift(new Range(Double.NaN, Double.NaN), 5);
		// throws error????

		ret = Range.shift(new Range(Double.NaN, 5), 5);
		// throws error????

		ret = Range.shift(new Range(-5, Double.NaN), 5);
		// throws error????
	}

	@Test
	public void testShiftDeltaNeg(){
		Range ret = Range.shift(exampleRange, -5);
		assertEquals(ret, new Range(-10, 0));
	}

	@Test
	public void testShiftDeltaPos(){
		Range ret = Range.shift(exampleRange, 5);
		assertEquals(ret, new Range(0, 10));
	}
	
	@Test
	public void testShift(){
		Range ret = Range.shift(new Range(0, 10), 10);
		assertEquals(ret, new Range(10, 20));
	}

	@Test
	public void testShiftZeroCrossingRangeNull() throws IllegalArgumentException{
		exception.expect(IllegalArgumentException.class);
		Range ret = Range.shift(null, -10, true);
	}
	
	@Test
	public void testShiftZeroCrossingRangeNaN(){
		Range ret = Range.shift(new Range(Double.NaN, Double.NaN), 5, true);
		// throws error????

		ret = Range.shift(new Range(Double.NaN, 5), 5, true);
		// throws error????

		ret = Range.shift(new Range(-5, Double.NaN), 5, true);
		// throws error????
	}

	@Test
	public void testShiftZeroCrossingDeltaNeg(){
		Range ret = Range.shift(exampleRange, -10, true);
		assertEquals(ret, new Range(-15, -5));
	}

	@Test
	public void testShiftZeroCrossingDeltaPos(){
		Range ret = Range.shift(exampleRange, 10, true);
		assertEquals(ret, new Range(5, 15));
	}

	// -----------------------------------------------------------
	// scale()
	@Test
	public void testScaleRangeNull() throws IllegalArgumentException{
		exception.expect(IllegalArgumentException.class);
		Range ret = Range.scale(null, 2);
	}

	@Test
	public void testScaleRangeNaN(){
		Range ret = Range.scale(new Range(Double.NaN, Double.NaN), 5);

		ret = Range.scale(new Range(Double.NaN, 5), 5);
		
		ret = Range.scale(new Range(-5, Double.NaN), 5);
	}

	@Test
	public void testScaleFactorLessThan0() throws IllegalArgumentException{
		exception.expect(IllegalArgumentException.class);
		Range ret = Range.scale(exampleRange, -1);
	}
	
	@Test
	public void testScale(){
		Range ret = Range.scale(exampleRange, 2);
		assertEquals(ret, new Range(-10, 10));
	}

	// -----------------------------------------------------------
	// isNaNRange()
	@Test
	public void testIsNaNRange(){
		assertFalse(exampleRange.isNaNRange());

		Range testRange = new Range(Double.NaN, Double.NaN);
		assertTrue(testRange.isNaNRange());

		testRange = new Range(Double.NaN, 5);
		assertFalse(testRange.isNaNRange());

		testRange = new Range(-5, Double.NaN);
		assertFalse(testRange.isNaNRange());
	}
	
	// -----------------------------------------------------------
	// Misc tests
	@Test
	public void testConstructorException() throws IllegalArgumentException {
		exception.expect(IllegalArgumentException.class);
		Range test = new Range(5, -1);
	}
	
	// Trust me its the correct hash
	@Test
	public void testHash() {
		assertEquals(exampleRange.hashCode(), 39321600);
	}
	
	@Test
	public void testEquals() {
		Double a = 5.0;
		assertFalse(exampleRange.equals(a));
		
		Range testRange = new Range(-5, 5);
		assertTrue(exampleRange.equals(testRange));
		
		testRange = new Range(-4, 5);
		assertFalse(exampleRange.equals(testRange));
		
		testRange = new Range(-5, 4);
		assertFalse(exampleRange.equals(testRange));
	}
	
	
	@After
	public void tearDown() throws Exception {exampleRange = null;}
}