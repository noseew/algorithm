/*
 * Copyright 2014 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.common.geometry;

import static com.google.common.geometry.S1ChordAngle.add;
import static com.google.common.geometry.S1ChordAngle.cos;
import static com.google.common.geometry.S1ChordAngle.sin;
import static com.google.common.geometry.S1ChordAngle.sub;
import static com.google.common.geometry.S1ChordAngle.tan;
import static com.google.common.geometry.S2.DBL_EPSILON;
import static com.google.common.geometry.S2Point.add;
import static com.google.common.geometry.S2Point.neg;
import static com.google.common.geometry.S2Point.normalize;
import static java.lang.Math.PI;
import static java.lang.Math.atan;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.tan;

import com.google.common.annotations.GwtCompatible;

/** Tests for {@link S1ChordAngle}. */
@GwtCompatible
public strictfp class S1ChordAngleTest extends GeometryTestCase {
  private static final double DOUBLE_ERROR = 1e-13;

  public void testTwoPointConstructor() {
    for (int iter = 0; iter < 100; ++iter) {
      Matrix3x3 frame = getRandomFrame();
      S2Point x = frame.getCol(0);
      S2Point y = frame.getCol(1);
      S2Point z = frame.getCol(2);
      assertEquals(S1Angle.ZERO, new S1ChordAngle(z, z).toAngle());
      assertEquals(PI, new S1ChordAngle(neg(z), z).toAngle().radians(), 1e-7);
      assertNearlyEquals(PI / 2, new S1ChordAngle(x, z).toAngle().radians());
      S2Point w = normalize(add(y, z));
      assertNearlyEquals(PI / 4, new S1ChordAngle(w, z).toAngle().radians());
    }
  }

  public void testFromLength2() {
    assertEquals(0.0, S1ChordAngle.fromLength2(0).toAngle().degrees());
    assertNearlyEquals(60.0, S1ChordAngle.fromLength2(1).toAngle().degrees());
    assertNearlyEquals(90.0, S1ChordAngle.fromLength2(2).toAngle().degrees());
    assertEquals(180.0, S1ChordAngle.fromLength2(4).toAngle().degrees());
    assertEquals(180.0, S1ChordAngle.fromLength2(5).toAngle().degrees());
  }

  public void testZero() {
    assertEquals(S1Angle.ZERO, S1ChordAngle.ZERO.toAngle());
  }

  public void testStraight() {
    assertEquals(S1Angle.degrees(180), S1ChordAngle.STRAIGHT.toAngle());
  }

  public void testRight() {
    assertEquals(90.0, S1ChordAngle.RIGHT.toAngle().degrees(), Platform.ulp(90D));
  }

  public void testInfinity() {
    assertTrue(S1ChordAngle.STRAIGHT.compareTo(S1ChordAngle.INFINITY) < 0);
    assertEquals(S1Angle.INFINITY, S1ChordAngle.INFINITY.toAngle());
  }

  public void testNegative() {
    assertTrue(S1ChordAngle.NEGATIVE.compareTo(S1ChordAngle.ZERO) < 0);
    assertTrue(S1ChordAngle.NEGATIVE.toAngle().compareTo(S1Angle.ZERO) < 0);
  }

  public void testEquals() {
    S1ChordAngle[] angles = {
      S1ChordAngle.NEGATIVE, S1ChordAngle.ZERO, S1ChordAngle.STRAIGHT, S1ChordAngle.INFINITY
    };
    for (int i = 0; i < angles.length; ++i) {
      for (int j = 0; j < angles.length; ++j) {
        assertEquals(angles[i] == angles[j], angles[i].equals(angles[j]));
      }
    }
  }

  public void testPredicates() {
    assertTrue(S1ChordAngle.ZERO.isZero());
    assertFalse(S1ChordAngle.ZERO.isNegative());
    assertFalse(S1ChordAngle.ZERO.isSpecial());
    assertFalse(S1ChordAngle.STRAIGHT.isSpecial());
    assertTrue(S1ChordAngle.NEGATIVE.isNegative());
    assertTrue(S1ChordAngle.NEGATIVE.isSpecial());
    assertTrue(S1ChordAngle.INFINITY.isInfinity());
    assertTrue(S1ChordAngle.INFINITY.isSpecial());
  }

  public void testToFromS1Angle() {
    assertEquals(0.0, S1ChordAngle.fromS1Angle(S1Angle.ZERO).toAngle().radians());
    assertEquals(4.0, S1ChordAngle.fromS1Angle(S1Angle.radians(Math.PI)).getLength2());
    assertEquals(PI, S1ChordAngle.fromS1Angle(S1Angle.radians(Math.PI)).toAngle().radians());
    assertEquals(S1Angle.INFINITY, S1ChordAngle.fromS1Angle(S1Angle.INFINITY).toAngle());
    assertEquals(
        S1Angle.INFINITY,
        S1ChordAngle.fromS1Angle(S1Angle.radians(Double.POSITIVE_INFINITY)).toAngle());
    assertTrue(S1ChordAngle.fromS1Angle(S1Angle.radians(-1)).toAngle().radians() < 0.0);
    assertNearlyEquals(1.0, S1ChordAngle.fromS1Angle(S1Angle.radians(1.0)).toAngle().radians());
  }

  public void testArithmetic() {
    S1ChordAngle zero = S1ChordAngle.ZERO;
    S1ChordAngle degree30 = S1ChordAngle.fromS1Angle(S1Angle.degrees(30));
    S1ChordAngle degree60 = S1ChordAngle.fromS1Angle(S1Angle.degrees(60));
    S1ChordAngle degree90 = S1ChordAngle.fromS1Angle(S1Angle.degrees(90));
    S1ChordAngle degree120 = S1ChordAngle.fromS1Angle(S1Angle.degrees(120));
    S1ChordAngle degree180 = S1ChordAngle.STRAIGHT;
    assertEquals(0.0, add(zero, zero).toAngle().degrees());
    assertEquals(0.0, sub(zero, zero).toAngle().degrees());
    assertEquals(0.0, sub(degree60, degree60).toAngle().degrees());
    assertEquals(0.0, sub(degree180, degree180).toAngle().degrees());
    assertEquals(0.0, sub(zero, degree60).toAngle().degrees());
    assertEquals(0.0, sub(degree30, degree90).toAngle().degrees());
    assertNearlyEquals(60.0, add(degree60, zero).toAngle().degrees());
    assertNearlyEquals(60.0, sub(degree60, zero).toAngle().degrees());
    assertNearlyEquals(60.0, add(zero, degree60).toAngle().degrees());
    assertNearlyEquals(90.0, add(degree30, degree60).toAngle().degrees());
    assertNearlyEquals(90.0, add(degree60, degree30).toAngle().degrees());
    assertNearlyEquals(60.0, sub(degree90, degree30).toAngle().degrees());
    assertNearlyEquals(30.0, sub(degree90, degree60).toAngle().degrees());
    assertEquals(180.0, add(degree180, zero).toAngle().degrees());
    assertEquals(180.0, sub(degree180, zero).toAngle().degrees());
    assertEquals(180.0, add(degree90, degree90).toAngle().degrees());
    assertEquals(180.0, add(degree120, degree90).toAngle().degrees());
    assertEquals(180.0, add(degree120, degree120).toAngle().degrees());
    assertEquals(180.0, add(degree30, degree180).toAngle().degrees());
    assertEquals(180.0, add(degree180, degree180).toAngle().degrees());
  }

  public void testTrigonometry() {
    final int iters = 20;
    for (int iter = 0; iter <= iters; ++iter) {
      double radians = PI * iter / iters;
      S1ChordAngle angle = S1ChordAngle.fromS1Angle(S1Angle.radians(radians));
      assertEquals(sin(radians), sin(angle), 1e-15);
      assertEquals(cos(radians), cos(angle), 1e-15);
      // Since the tan(x) is unbounded near Pi/4, we map the result back to an
      // angle before comparing.  (The assertion is that the result is equal to
      // the tangent of a nearby angle.)
      assertEquals(atan(tan(radians)), atan(tan(angle)), 1e-15);
    }

    // Unlike S1Angle, S1ChordAngle can represent 90 and 180 degrees exactly.
    S1ChordAngle angle90 = S1ChordAngle.fromLength2(2);
    S1ChordAngle angle180 = S1ChordAngle.fromLength2(4);
    assertEquals(1.0, sin(angle90));
    assertEquals(0.0, cos(angle90));
    assertEquals(Double.POSITIVE_INFINITY, tan(angle90));
    assertEquals(0.0, sin(angle180));
    assertEquals(-1.0, cos(angle180));
    assertEquals(-0.0, tan(angle180));
  }

  public void testHashCodeZero() {
    // Check that hashCode() and equals(...) work consistently for the ±0 edge case.
    S1ChordAngle positive0 = S1ChordAngle.fromLength2(0);
    S1ChordAngle negative0 = S1ChordAngle.fromLength2(-0.0);

    assertTrue(positive0.equals(negative0));
    assertEquals(positive0.hashCode(), negative0.hashCode());
  }

  public void testHashCodeDifferent() {
    S1ChordAngle zero = S1ChordAngle.fromLength2(0);
    S1ChordAngle nonZero = S1ChordAngle.fromLength2(1);

    assertFalse(zero.equals(nonZero));
    // ant_test uses a different (old) version of JUnit without assertNotEquals.
    assertTrue(zero.hashCode() != nonZero.hashCode());
  }

  public void testPlusError() {
    assertEquals(S1ChordAngle.NEGATIVE, S1ChordAngle.NEGATIVE.plusError(5));
    assertEquals(S1ChordAngle.INFINITY, S1ChordAngle.INFINITY.plusError(-5));
    assertEquals(S1ChordAngle.STRAIGHT, S1ChordAngle.STRAIGHT.plusError(5));
    assertEquals(S1ChordAngle.ZERO, S1ChordAngle.ZERO.plusError(-5));
    assertEquals(S1ChordAngle.fromLength2(1.25), S1ChordAngle.fromLength2(1).plusError(0.25));
    assertEquals(S1ChordAngle.fromLength2(0.75), S1ChordAngle.fromLength2(1).plusError(-0.25));
  }

  /**
   * Verifies that the error bound returned by {@link S1ChordAngle#getS2PointConstructorMaxError} is
   * large enough.
   */
  public void testGetS2PointConstructorMaxError() {
    for (int iter = 0; iter < 10000; ++iter) {
      rand.setSeed(iter);
      S2Point x = randomPoint();
      S2Point y = randomPoint();
      if (super.oneIn(10)) {
        // Occasionally test a point pair that is nearly identical or antipodal.
        S1Angle r = S1Angle.radians(1e-15 * rand.nextDouble());
        y = S2EdgeUtil.interpolateAtDistance(r, x, y);
        if (oneIn(2)) {
          y = S2Point.neg(y);
        }
      }
      S1ChordAngle dist = new S1ChordAngle(x, y);
      double error = dist.getS2PointConstructorMaxError();
      String msg = "angle=" + dist + ", iter=" + iter;
      assertTrue(msg, S2Predicates.compareDistance(x, y, dist.plusError(error).getLength2()) <= 0);
      assertTrue(msg, S2Predicates.compareDistance(x, y, dist.plusError(-error).getLength2()) >= 0);
    }
  }

  public void testS1AngleConsistency() {
    // This test checks that the error bounds in the S1ChordAngle constructors
    // are consistent with the maximum error in S1Angle(x, y).
    double maxS1AngleError = 3.25 * DBL_EPSILON;
    for (int iter = 0; iter < 10000; ++iter) {
      S2Point x = randomPoint();
      S2Point y = randomPoint();
      S1ChordAngle dist1 = S1ChordAngle.fromS1Angle(new S1Angle(x, y));
      S1ChordAngle dist2 = new S1ChordAngle(x, y);
      double maxError =
          (maxS1AngleError
              + dist1.getS1AngleConstructorMaxError()
              + dist2.getS2PointConstructorMaxError());
      assertTrue(dist1.compareTo(dist2.plusError(maxError)) <= 0);
      assertTrue(dist1.compareTo(dist2.plusError(-maxError)) >= 0);
    }
  }

  /**
   * Assert that {@code actual} is almost equal to {@code expected}, within floating point error.
   */
  private static void assertNearlyEquals(double expected, double actual) {
    assertEquals(expected, actual, DOUBLE_ERROR);
  }
}
