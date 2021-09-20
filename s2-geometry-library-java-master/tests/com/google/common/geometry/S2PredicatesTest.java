/*
 * Copyright 2018 Google Inc.
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

import static com.google.common.geometry.S2Predicates.sign;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.collect.ImmutableMap;
import com.google.common.geometry.S2Predicates.CompareDistance;
import com.google.common.geometry.S2Predicates.CompareDistances;
import com.google.common.geometry.S2Predicates.CompareEdgeDirections;
import com.google.common.geometry.S2Predicates.CompareEdgeDistance;
import com.google.common.geometry.S2Predicates.EdgeCircumcenterSign;
import com.google.common.geometry.S2Predicates.Excluded;
import com.google.common.geometry.S2Predicates.VoronoiSiteExclusion;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@GwtCompatible
public strictfp class S2PredicatesTest extends GeometryTestCase {
  /** Relabeled for brevity in its many, many uses below. */
  private static final double EPS = S2.DBL_EPSILON;
  /** The number of consistency checks to run. */
  private static final int NUM_CONSISTENCY_CHECKS = 5000;
  /** A label to indicate double precision was used. */
  private static final int DOUBLE = 0;
  /** A label to indicate exact precision was used. */
  private static final int EXACT = 1;
  /** A label to indicate symbolic precision was used. */
  private static final int SYMBOLIC = 2;
  /** A label to indicate long double could be useful, but double was used. */
  private static final int LDOUBLE = DOUBLE;
  /** The squared chord length of a right angle. */
  private static final double RIGHT = S1ChordAngle.RIGHT.getLength2();

  /**
   * The radian distance from Math.PI at which the sin2 triage error analysis seems to break down.
   *
   * <p>Note the threshold in C++ is 1e-14, but the error analysis isn't quite valid for Java, very
   * close to pi. Lowering this constant has no effect, however, since we're still close to pi, and
   * in any event the sin2 triage is only used for angles less than pi/2.
   */
  private static final double SIN2_ERROR_GAP = 1e-11;

  public void testSignCollinearPoints() {
    // The following points happen to be *exactly collinear* along a line that is approximately
    // tangent to the surface of the unit sphere.  In fact, C is the exact midpoint of the line
    // segment AB.  All of these points are close enough to unit length to satisfy S2.isUnitLength.
    S2Point a = p(0.7257192787703683, 0.460588256058891, 0.5110674973050485);
    S2Point b = p(0.7257192746638208, 0.4605882657381817, 0.5110674944131274);
    S2Point c = p(0.7257192767170946, 0.46058826089853633, 0.511067495859088);
    assertEquals(S2Point.sub(c, a), S2Point.sub(b, c));
    assertFalse(0 == sign(a, b, c));
    assertEquals(sign(a, b, c), sign(b, c, a));
    assertEquals(sign(a, b, c), -sign(c, b, a));

    // The points "x1" and "x2" are exactly proportional, i.e. they both lie on a common line
    // through the origin.  Both points are considered to be normalized, and in fact they both
    // satisfy (x == x.normalize()). Therefore the triangle (x1, x2, -x1) consists of three distinct
    // points that all lie on a common line through the origin.
    S2Point x1 = p(0.9999999999999999, 1.4901161193847655e-08, 0);
    S2Point x2 = p(1, 1.4901161193847656e-08, 0);
    assertEquals(x1, S2Point.normalize(x1));
    assertEquals(x2, S2Point.normalize(x2));
    assertFalse(0 == sign(x1, x2, S2Point.neg(x1)));
    assertEquals(sign(x1, x2, S2Point.neg(x1)), sign(x2, S2Point.neg(x1), x1));
    assertEquals(sign(x1, x2, S2Point.neg(x1)), -sign(S2Point.neg(x1), x2, x1));

    // Here are two more points that are distinct, exactly proportional, and that satisfy (x ==
    // x.normalize()).
    S2Point x3 = S2Point.normalize(p(1, 1, 1));
    S2Point x4 = S2Point.mul(x3, 0.9999999999999999);
    assertEquals(x3, S2Point.normalize(x3));
    assertEquals(x4, S2Point.normalize(x4));
    assertFalse(x3.equalsPoint(x4));
    assertFalse(0 == sign(x3, x4, S2Point.neg(x3)));

    // The following two points demonstrate that normalize() is not idempotent, i.e. y0.normalize()
    // != y0.normalize().normalize().  Both points satisfy S2.isNormalized(), though, and the two
    // points are exactly proportional.
    S2Point y0 = p(1, 1, 0);
    S2Point y1 = S2Point.normalize(y0);
    S2Point y2 = S2Point.normalize(y1);
    assertFalse(y1.equalsPoint(y2));
    assertEquals(y2, S2Point.normalize(y2));
    assertFalse(0 == sign(y1, y2, S2Point.neg(y1)));
    assertEquals(sign(y1, y2, S2Point.neg(y1)), sign(y2, S2Point.neg(y1), y1));
    assertEquals(sign(y1, y2, S2Point.neg(y1)), -sign(S2Point.neg(y1), y2, y1));
  }

  /**
   * This test repeatedly constructs some number of points that are on or nearly on a given great
   * circle. Then it chooses one of these points as the "origin" and sorts the other points in CCW
   * order around it. Of course, since the origin is on the same great circle as the points being
   * sorted, nearly all of these tests are degenerate. It then does various consistency checks to
   * verify that the points are indeed sorted in CCW order.
   *
   * <p>It is easier to think about what this test is doing if you imagine that the points are in
   * general position rather than on a great circle.
   */
  public void testSignStressTest() {
    // The run time of this test is *cubic* in the parameter below.
    int circleSize = 20;

    // This test is randomized, so it is beneficial to run it several times.
    for (int iter = 0; iter < 3; ++iter) {
      // The most difficult great circles are the ones in the X-Y, Y-Z, and X-Z planes, for two
      // reasons.  First, when one or more coordinates are close to zero then the perturbations can
      // be much smaller, since floating point numbers are spaced much more closely together near
      // zero.  (This tests the handling of things like underflow.)  The second reason is that most
      // of the cases of symbolicallyPerturbedSign() can only be reached when one or more input
      // point coordinates are zero.
      checkGreatCircle(p(1, 0, 0), p(0, 1, 0), circleSize);
      checkGreatCircle(p(1, 0, 0), p(0, 0, 1), circleSize);
      checkGreatCircle(p(0, -1, 0), p(0, 0, 1), circleSize);

      // This tests a great circle where at least some points have X, Y, and Z coordinates with
      // exactly the same mantissa.  One useful property of such points is that when they are scaled
      // (e.g. multiplying by 1+eps), all such points are exactly collinear with the origin.
      checkGreatCircle(p(1 << 25, 1, -8), p(-4, -(1 << 20), 1), circleSize);
    }
  }

  /**
   * Constructs approximately "n" points near the great circle through A and B, sorts them, and
   * tests whether they are sorted.
   */
  private void checkGreatCircle(S2Point a, S2Point b, int n) {
    a = S2Point.normalize(a);
    b = S2Point.normalize(b);
    List<S2Point> points = new ArrayList<>();
    points.add(a);
    points.add(b);
    while (points.size() < n) {
      addDegeneracy(points);
    }

    // Remove any accidental (0, 0, 0) points, sort, and remove duplicates.
    points =
        points.stream()
            .filter(x -> !x.equalsPoint(S2Point.ORIGIN))
            .distinct()
            .sorted()
            .collect(Collectors.toList());
    assertTrue(points.size() >= n / 2);

    sortAndTest(points, a);
    sortAndTest(points, b);
    for (S2Point origin : points) {
      sortAndTest(points, origin);
    }
  }

  /**
   * Adds zero or more (but usually one) point that is likely to trigger sign() degeneracies among
   * the given points.
   */
  private void addDegeneracy(List<S2Point> points) {
    S2Point a = points.get(random(points.size()));
    S2Point b = points.get(random(points.size()));
    int coord = rand.nextInt(3);
    switch (rand.nextInt(8)) {
      case 0:
        // Add a random point (not uniformly distributed) along the great circle AB.
        addNormalized(
            S2Point.add(
                S2Point.mul(a, rand.nextDouble() * 2 - 1),
                S2Point.mul(b, rand.nextDouble() * 2 - 1)),
            points);
        break;
      case 1:
        // Perturb one coordinate by the minimum amount possible.
        a = set(a, coord, Platform.nextAfter(a.get(coord), oneIn(2) ? 2 : -2));
        addNormalized(a, points);
        break;
      case 2:
        // Perturb one coordinate by up to 1e-15.
        a = set(a, coord, a.get(coord) + 1e-15 * uniform(-1, 1));
        addNormalized(a, points);
        break;
      case 3:
        // Scale a point just enough so that it is different while still being
        // considered normalized.
        a = S2Point.mul(a, oneIn(2) ? (1 + 2e-16) : (1 - 1e-16));
        if (S2.isUnitLength(a)) {
          points.add(a);
        }
        break;
      case 4:
        {
          // Add the intersection point of AB with X=0, Y=0, or Z=0.
          S2Point dir = set(p(0, 0, 0), coord, oneIn(2) ? 1 : -1);
          S2Point norm = S2Point.normalize(S2.robustCrossProd(a, b));
          if (norm.norm2() > 0) {
            addNormalized(S2.robustCrossProd(dir, norm), points);
          }
          break;
        }
      case 5:
        // Add two closely spaced points along the tangent at A to the great
        // circle through AB.
        addTangentPoints(a, b, points);
        break;
      case 6:
        // Add two closely spaced points along the tangent at A to the great
        // circle through A and the X-axis.
        addTangentPoints(a, p(1, 0, 0), points);
        break;
      default:
        // Add the negative of a point.
        points.add(S2Point.neg(a));
        break;
    }
  }

  /** Adds a normalized copy of 'a' to 'points'. */
  private static void addNormalized(S2Point a, List<S2Point> points) {
    points.add(S2Point.normalize(a));
  }

  /**
   * Adds two points A1 and A2 that are slightly offset from A along the tangent toward B, and such
   * that A, A1, and A2 are exactly collinear (i.e. even with infinite-precision arithmetic).
   */
  private void addTangentPoints(S2Point a, S2Point b, List<S2Point> points) {
    S2Point dir = S2Point.normalize(S2Point.crossProd(S2.robustCrossProd(a, b), a));
    if (dir.equalsPoint(S2Point.ORIGIN)) {
      return;
    }
    for (; ; ) {
      S2Point delta = S2Point.mul(dir, 1e-15 * rand.nextDouble());
      S2Point aUp = S2Point.add(a, delta);
      S2Point aDown = S2Point.sub(a, delta);
      if (!aUp.equalsPoint(a)
          && S2Point.sub(aUp, a).equalsPoint(S2Point.sub(a, aDown))
          && S2.isUnitLength(aUp)
          && S2.isUnitLength(aDown)) {
        points.add(aUp);
        points.add(aDown);
        return;
      }
    }
  }

  /** Returns a copy of the given point 'p' with 'axis' set to 'value'. */
  private static S2Point set(S2Point p, int axis, double value) {
    switch (axis) {
      case 0:
        return p(value, p.y, p.z);
      case 1:
        return p(p.x, value, p.z);
      default:
        return p(p.x, p.y, value);
    }
  }

  /**
   * Sorts the points around the given origin, and then do some consistency checks to verify that
   * they are actually sorted.
   */
  private static void sortAndTest(List<S2Point> points, S2Point origin) {
    List<S2Point> sorted = new ArrayList<>();
    sortCCW(points, origin, sorted);
    testCCW(sorted, origin);
  }

  /**
   * Given a set of points with no duplicates, first remove "origin" from "points" (if it exists)
   * and then sort the remaining points in CCW order around "origin", results going in "sorted".
   */
  private static void sortCCW(List<S2Point> points, S2Point origin, List<S2Point> sorted) {
    // Make a copy of the points with "origin" removed.
    sorted.clear();
    points.stream().filter(x -> !x.equalsPoint(origin)).forEach(sorted::add);

    // Sort the points CCW around the origin starting at (*sorted)[0].
    S2Point start = sorted.get(0);
    sorted.sort(
        (a, b) -> {
          if (S2Predicates.orderedCCW(start, b, a, origin)) {
            return 1;
          } else if (S2Predicates.orderedCCW(start, a, b, origin)) {
            return -1;
          } else {
            return 0;
          }
        });
  }

  /** Tests exhaustively whether points in "sorted" are sorted circularly CCW around "origin". */
  private static void testCCW(List<S2Point> sorted, S2Point origin) {
    int n = sorted.size();
    int totalNumCcw = 0;
    int lastNumCcw = countCCW(sorted, origin, n - 1);
    for (int start = 0; start < n; start++) {
      int numCcw = countCCW(sorted, origin, start);
      // Each iteration we increase the start index by 1, therefore the number
      // of CCW triangles should decrease by at most 1.
      assertTrue(numCcw >= lastNumCcw - 1);
      totalNumCcw += numCcw;
      lastNumCcw = numCcw;
    }
    // We have tested all triangles of the form OAB.  Exactly half of these should be CCW.
    assertEquals(n * (n - 1) / 2, totalNumCcw);
  }

  /**
   * Given a set of points sorted circularly CCW around "origin", and the index "start" of a point
   * A, returns the number of CCW triangles OAB over all sorted points B not equal to A. Also check
   * that the results of the CCW tests are consistent with the hypothesis that the points are
   * sorted.
   */
  private static int countCCW(List<S2Point> sorted, S2Point origin, int start) {
    int numCcw = 0;
    int lastSign = 1;
    int n = sorted.size();
    for (int j = 1; j < n; ++j) {
      int sign = sign(origin, sorted.get(start), sorted.get((start + j) % n));
      assertFalse(0 == sign);
      if (sign > 0) {
        numCcw++;
      }

      // Since the points are sorted around the origin, we expect to see a (possibly empty)
      // sequence of CCW triangles followed by a (possibly empty) sequence of CW triangles.
      assertFalse(sign > 0 && lastSign < 0);
      lastSign = sign;
    }
    return numCcw;
  }

  /**
   * Verifies that stableSign() is able to handle most cases where the three points are as collinear
   * as possible. (For reference, triageSign() fails virtually 100% of the time on this test.)
   *
   * <p>Note that the failure rate *decreases* as the points get closer together, and the decrease
   * is approximately linear. For example, the failure rate is 0.4% for collinear points spaced 1km
   * apart, but only 0.0004% for collinear points spaced 1 meter apart.
   */
  public void testSignStableFailureRate() {
    assertTrue(getFailureRate(1.0) < 0.01); //  1km spacing: <  1% (actual 0.4%)
    assertTrue(getFailureRate(10.0) < 0.1); // 10km spacing: < 10% (actual 4%)
  }

  /**
   * Estimates the probability that S2.stableSign() will not be able to compute the determinant sign
   * of a triangle A, B, C consisting of three points that are as collinear as possible and spaced
   * the given distance apart.
   */
  private double getFailureRate(double km) {
    int iterations = 1000;
    int failureCount = 0;
    double m = Math.tan(kmToAngle(km).radians());
    for (int iter = 0; iter < iterations; iter++) {
      Matrix3x3 matrix = getRandomFrame();
      S2Point a = matrix.getCol(0);
      S2Point x = matrix.getCol(1);
      S2Point b = S2Point.normalize(S2Point.sub(a, S2Point.mul(x, m)));
      S2Point c = S2Point.normalize(S2Point.add(a, S2Point.mul(x, m)));
      int sign = S2Predicates.Sign.stable(a, b, c);
      if (sign != 0) {
        assertEquals(S2Predicates.Sign.exact(a, b, c, true), sign);
      } else {
        failureCount++;
      }
    }
    return (double) failureCount / iterations;
  }

  /**
   * Verifies symbolicallyPerturbedSign(). The functional behavior is largely checked elsewhere, so
   * these are simple sanity checks to get code coverage.
   *
   * <p>Let M_1, M_2, ... be the sequence of submatrices whose determinant sign is tested by that
   * function. Then the i<sup>th</sup> test below is a 3x3 matrix M (with rows A, B, C) such that:
   *
   * <pre>
   * det(M) = 0
   * det(M_j) = 0 for j < i
   * det(M_i) != 0
   * </pre>
   *
   * where {@code A < B < C} in lexicographic order.
   *
   * <p>Reversing the sign of any of the "return" statements in symbolicallyPerturbedSign() causes
   * this test to fail.
   */
  public void testSignSymbolicPerturbationCodeCoverage() {
    checkSymbolicSign(1, p(-3, -1, 0), p(-2, 1, 0), p(1, -2, 0)); // det(M_1) = b0*c1 - b1*c0
    checkSymbolicSign(1, p(-6, 3, 3), p(-4, 2, -1), p(-2, 1, 4)); // det(M_2) = b2*c0 - b0*c2
    checkSymbolicSign(1, p(0, -1, -1), p(0, 1, -2), p(0, 2, 1)); // det(M_3) = b1*c2 - b2*c1

    // From this point onward, B or C must be zero, or B is proportional to C.
    checkSymbolicSign(1, p(-1, 2, 7), p(2, 1, -4), p(4, 2, -8)); // det(M_4) = c0*a1 - c1*a0
    checkSymbolicSign(1, p(-4, -2, 7), p(2, 1, -4), p(4, 2, -8)); // det(M_5) = c0
    checkSymbolicSign(1, p(0, -5, 7), p(0, -4, 8), p(0, -2, 4)); // det(M_6) = -c1
    checkSymbolicSign(1, p(-5, -2, 7), p(0, 0, -2), p(0, 0, -1)); // det(M_7) = c2*a0 - c0*a2
    checkSymbolicSign(1, p(0, -2, 7), p(0, 0, 1), p(0, 0, 2)); // det(M_8) = c2

    // From this point onward, C must be zero.
    checkSymbolicSign(1, p(-3, 1, 7), p(-1, -4, 1), p(0, 0, 0)); // det(M_9) = a0*b1 - a1*b0
    checkSymbolicSign(1, p(-6, -4, 7), p(-3, -2, 1), p(0, 0, 0)); // det(M_10) = -b0
    checkSymbolicSign(-1, p(0, -4, 7), p(0, -2, 1), p(0, 0, 0)); // det(M_11) = b1
    checkSymbolicSign(-1, p(-1, -4, 5), p(0, 0, -3), p(0, 0, 0)); // det(M_12) = a0
    checkSymbolicSign(1, p(0, -4, 5), p(0, 0, -5), p(0, 0, 0)); // det(M_13) = 1
  }

  /**
   * Given 3 points A, B, C that are exactly coplanar with the origin and where {@code A < B < C} in
   * lexicographic order, verifies that ABC is counterclockwise (if expected == 1) or clockwise (if
   * expected == -1) using expensiveSign().
   *
   * <p>This method is intended specifically for checking the cases where symbolic perturbations are
   * needed to break ties.
   */
  private static void checkSymbolicSign(int expected, S2Point a, S2Point b, S2Point c) {
    assertTrue(a.compareTo(b) < 0);
    assertTrue(a.compareTo(b) < 0);
    assertEquals(0.0, a.dotProd(S2Point.crossProd(b, c)), 0.0);

    assertEquals(expected, S2Predicates.Sign.expensive(a, b, c, true));
    assertEquals(expected, S2Predicates.Sign.expensive(b, c, a, true));
    assertEquals(expected, S2Predicates.Sign.expensive(c, a, b, true));
    assertEquals(-expected, S2Predicates.Sign.expensive(c, b, a, true));
    assertEquals(-expected, S2Predicates.Sign.expensive(b, a, c, true));
    assertEquals(-expected, S2Predicates.Sign.expensive(a, c, b, true));
  }

  /**
   * Chooses a random S2Point that is often near the intersection of one of the coodinate planes or
   * coordinate axes with the unit sphere. (It is possible to represent very small perturbations
   * near such points.)
   */
  private S2Point choosePoint() {
    S2Point p = randomPoint();
    double[] coords = {p.x, p.y, p.z};
    for (int i = 0; i < 3; ++i) {
      if (oneIn(3)) {
        coords[i] *= Math.pow(1e-50, rand.nextDouble());
      }
    }
    return new S2Point(coords[0], coords[1], coords[2]).normalize();
  }

  /** This test attempts to exercise all the code paths in all precisions. */
  public void testCompareDistancesCoverage() {
    // Test triage by sin2.
    CheckCompareDistances sin2 = CheckCompareDistances.SIN2;
    sin2.coverage(p(1, 1, 1), p(1, 1 - 1e-15, 1), p(1, 1, 1 + 2e-15), -1, DOUBLE);
    sin2.coverage(p(1, 1, 0), p(1, 1 - 1e-15, 1e-21), p(1, 1 - 1e-15, 0), 1, DOUBLE);
    sin2.coverage(p(2, 0, 0), p(2, -1, 0), p(2, 1, 1e-100), -1, EXACT);
    sin2.coverage(p(1, 0, 0), p(1, -1, 0), p(1, 1, 0), 1, SYMBOLIC);
    sin2.coverage(p(1, 0, 0), p(1, 0, 0), p(1, 0, 0), 0, SYMBOLIC);

    // Test triage by cos.
    CheckCompareDistances cos = CheckCompareDistances.COS;
    cos.coverage(p(1, 1, 1), p(1, -1, 0), p(-1, 1, 3e-15), 1, DOUBLE);
    cos.coverage(p(1, 0, 0), p(1, 1e-30, 0), p(-1, 1e-40, 0), -1, DOUBLE);
    cos.coverage(p(1, 1, 1), p(1, -1, 0), p(-1, 1, 3e-18), 1, EXACT);
    cos.coverage(p(1, 1, 1), p(1, -1, 0), p(-1, 1, 1e-100), 1, EXACT);
    cos.coverage(p(1, 1, 1), p(1, -1, 0), p(-1, 1, 0), -1, SYMBOLIC);
    cos.coverage(p(1, 1, 1), p(1, -1, 0), p(1, -1, 0), 0, SYMBOLIC);

    // Test triage by sin2 using distances greater than 90 degrees.
    CheckCompareDistances minusSin2 = CheckCompareDistances.MINUS_SIN2;
    minusSin2.coverage(p(1, 1, 0), p(-1, -1 + 1e-15, 0), p(-1, -1, 0), -1, DOUBLE);
    minusSin2.coverage(p(-1, -1, 0), p(1, 1 - 1e-15, 0), p(1, 1 - 1e-15, 1e-21), 1, DOUBLE);
    minusSin2.coverage(p(-1, -1, 0), p(2, 1, 0), p(2, 1, 1e-8), 1, EXACT);
    minusSin2.coverage(p(-1, -1, 0), p(2, 1, 0), p(2, 1, 1e-30), 1, EXACT);
    minusSin2.coverage(p(-1, -1, 0), p(2, 1, 0), p(1, 2, 0), -1, SYMBOLIC);
  }

  /**
   * This test chooses random point pairs that are nearly equidistant from a target point, and then
   * checks that the answer given by a method at one level of precision is consistent with the
   * answer given at the next higher level of precision.
   *
   * <p>The code below checks that the Cos, Sin2, and MinusSin2 methods are consistent across their
   * entire valid range of inputs, and also simulates the logic in CompareDistance that chooses
   * which method to use in order to gather statistics about how often each precision is needed.
   * (These statistics are only useful for coverage purposes, not benchmarks, since the input points
   * are chosen to be pathological worst cases.)
   */
  @GwtIncompatible("Lack of strictfp causing error bounds math to come out wrong")
  public void testCompareDistancesConsistency() {
    CheckCompareDistances.COS.consistency(p(1, 0, 0), p(0, -1, 0), p(0, 1, 0));
    PrecisionStats sin2Stats = new PrecisionStats();
    PrecisionStats cosStats = new PrecisionStats();
    PrecisionStats minusSin2Stats = new PrecisionStats();
    for (int iter = 0; iter < NUM_CONSISTENCY_CHECKS; iter++) {
      S2Point x = choosePoint();
      S2Point dir = choosePoint();
      double r = S2.M_PI_2 * Math.pow(1e-30, rand.nextDouble());
      if (oneIn(2)) {
        r = S2.M_PI_2 - r;
      }
      if (oneIn(2)) {
        r = S2.M_PI_2 + r;
      }
      S1Angle angle = S1Angle.radians(r);
      S2Point a = S2EdgeUtil.interpolateAtDistance(angle, x, dir);
      S2Point b = S2EdgeUtil.interpolateAtDistance(angle, x, dir.neg());
      int prec = CheckCompareDistances.COS.consistency(x, a, b);
      if (angle.degrees() >= 45 && angle.degrees() <= 135) {
        cosStats.tally(prec);
      }
      // The Sin2 method is only valid if both distances are less than 90 degrees, and similarly for
      // the MinusSin2 method.  (In the actual implementation these methods are only used if both
      // distances are less than 45 degrees or greater than 135 degrees respectively.)
      if (angle.radians() < S2.M_PI_2 - SIN2_ERROR_GAP) {
        prec = CheckCompareDistances.SIN2.consistency(x, a, b);
        if (angle.degrees() < 45) {
          // Don't skew the statistics by recording degenerate inputs.
          if (a.equalsPoint(b)) {
            assertEquals(SYMBOLIC, prec);
          } else {
            sin2Stats.tally(prec);
          }
        }
      } else if (angle.radians() > S2.M_PI_2 + SIN2_ERROR_GAP) {
        prec = CheckCompareDistances.MINUS_SIN2.consistency(x, a, b);
        if (angle.degrees() > 135) {
          minusSin2Stats.tally(prec);
        }
      }
    }
    System.err.println("sin2: " + sin2Stats + "\ncos: " + cosStats + "\n-sin2: " + minusSin2Stats);
  }

  private enum CheckCompareDistances {
    SIN2,
    COS,
    MINUS_SIN2;

    /** Returns the test of the given points using a specific non-general technique. */
    int test(S2Point x, S2Point a, S2Point b) {
      switch (this) {
        case COS:
          return CompareDistances.triageCos(x, a, b);
        case SIN2:
          return CompareDistances.triageSin2(x, a, b);
        case MINUS_SIN2:
          return -SIN2.test(x.neg(), a, b);
      }
      throw new IllegalArgumentException();
    }

    /** Verifies CompareDistances sign and the precision needed to determine it. */
    void coverage(S2Point x, S2Point a, S2Point b, int expectedSign, int expectedPrec) {
      // Normalize only when necessary, to allow testing points that differ a bit in magnitude.
      x = maybeNormalize(x);
      a = maybeNormalize(a);
      b = maybeNormalize(b);
      int methodSign = test(x, a, b);
      int exactSign = CompareDistances.exact(x, a, b);
      int actualSign = (exactSign != 0 ? exactSign : CompareDistances.sos(a, b));

      // Check that the sign result is expected, and is consistent with other tests.
      assertEquals(expectedSign, actualSign);
      if (exactSign != 0) {
        assertEquals(exactSign, actualSign);
      }
      if (methodSign != 0) {
        assertEquals(exactSign, methodSign);
      }

      assertEquals(expectedPrec, methodSign != 0 ? DOUBLE : exactSign != 0 ? EXACT : SYMBOLIC);

      // Make sure that the top-level function returns the expected result.
      assertEquals(expectedSign, S2Predicates.compareDistances(x, a, b));

      // Check that reversing the arguments negates the result.
      assertEquals(-expectedSign, S2Predicates.compareDistances(x, b, a));
    }

    /**
     * Checks that the result at one level of precision is consistent with the result at the next
     * higher level of Returns the minimum precision that yielded a non-zero result.
     */
    int consistency(S2Point x, S2Point a, S2Point b) {
      int dblSign = test(x, a, b);
      int exactSign = CompareDistances.exact(x, a, b);
      if (dblSign != 0) {
        assertEquals(exactSign, dblSign);
      }
      if (exactSign != 0) {
        assertEquals(exactSign, S2Predicates.compareDistances(x, a, b));
        return (dblSign == 0) ? EXACT : DOUBLE;
      } else {
        // Unlike the other methods, SOS has the precondition that the exact sign must be zero.
        int symbolicSign = CompareDistances.sos(a, b);
        assertEquals(symbolicSign, S2Predicates.compareDistances(x, a, b));
        return SYMBOLIC;
      }
    }
  }

  public void testCompareDistanceCoverage() {
    // Test triage by sin2.
    CheckCompareDistance sin2 = CheckCompareDistance.SIN2;
    sin2.check(p(1, 1, 1), p(1, 1 - 1e-15, 1), length2(1e-15), -1, DOUBLE);
    sin2.check(p(1, 1e-40, 0), p(1 + EPS, 1e-40, 0), length2(0.9 * EPS * 1e-40), 1, EXACT);
    sin2.check(p(1, 1e-40, 0), p(1 + EPS, 1e-40, 0), length2(1.1 * EPS * 1e-40), -1, EXACT);
    sin2.check(p(1, 0, 0), p(1 + EPS, 0, 0), length2(0), 0, EXACT);

    // Test triage by cos.
    CheckCompareDistance cos = CheckCompareDistance.COS;
    cos.check(p(1, 0, 0), p(1, 1e-8, 0), length2(1e-7), -1, DOUBLE);
    cos.check(p(1, 0, 0), p(-1, 1e-8, 0), length2(S2.M_PI - 1e-7), 1, DOUBLE);
    cos.check(p(1, 1, 0), p(1, -1 - 2 * EPS, 0), RIGHT, 1, DOUBLE);
    cos.check(p(1, 1, 0), p(1, -1 - EPS, 0), RIGHT, 1, EXACT);
    cos.check(p(1, 1, 0), p(1, -1, 1e-30), RIGHT, 0, EXACT);
    // The angle between these two points is exactly 60 degrees.
    cos.check(p(1, 1, 0), p(0, 1, 1), 1, 0, EXACT);
  }

  /**
   * Chooses random inputs such that the distance between points X and Y is very close to the
   * threshold distance "r", and checks that the answer given by a method at one level of precision
   * is consistent with the answer given at the next higher level of precision.
   *
   * @see #testCompareDistancesConsistency()
   */
  @GwtIncompatible("Lack of strictfp causing error bounds math to come out wrong")
  public void testCompareDistanceConsistency() {
    PrecisionStats sin2Stats = new PrecisionStats();
    PrecisionStats cosStats = new PrecisionStats();
    for (int iter = 0; iter < NUM_CONSISTENCY_CHECKS; ++iter) {
      S2Point x = choosePoint();
      S2Point dir = choosePoint();
      double r = S2.M_PI_2 * Math.pow(1e-30, rand.nextDouble());
      if (oneIn(2)) {
        r = S2.M_PI_2 - r;
      }
      if (oneIn(5)) {
        r = S2.M_PI_2 + r;
      }
      S1Angle angle = S1Angle.radians(r);
      S2Point y = S2EdgeUtil.interpolateAtDistance(angle, x, dir);
      int prec = CheckCompareDistance.COS.checkConsistency(x, y, length2(r));
      if (angle.degrees() >= 45) {
        cosStats.tally(prec);
      }
      if (angle.radians() < S2.M_PI_2 - SIN2_ERROR_GAP) {
        prec = CheckCompareDistance.SIN2.checkConsistency(x, y, length2(r));
        if (angle.degrees() < 45) {
          sin2Stats.tally(prec);
        }
      }
    }
    System.err.println("sin2:  " + sin2Stats + "\ncos:   " + cosStats);
  }

  private enum CheckCompareDistance {
    SIN2,
    COS;

    int test(S2Point x, S2Point y, double r2) {
      switch (this) {
        case COS:
          return CompareDistance.triageCos(x, y, r2);
        case SIN2:
          return CompareDistance.triageSin2(x, y, r2);
      }
      throw new IllegalArgumentException();
    }

    /**
     * Verifies that CompareDistance(x, y, r) == expected_sign, and furthermore checks that the
     * minimum required precision is "expected_prec" when the distance calculation method defined by
     * CompareDistanceWrapper is used.
     */
    void check(S2Point x, S2Point y, double r2, int expectedSign, int expectedPrec) {
      x = maybeNormalize(x);
      y = maybeNormalize(y);
      // Check that the signs are correct (if non-zero), and that the techniques are consistent.
      assertEquals(expectedSign, CompareDistance.exact(x, y, r2));
      int methodSign = test(x, y, r2);
      if (methodSign != 0) {
        assertEquals(expectedSign, methodSign);
      }
      assertEquals(expectedPrec, methodSign != 0 ? DOUBLE : EXACT);

      // Make sure that the top-level function returns the expected result.
      assertEquals(expectedSign, S2Predicates.compareDistance(x, y, r2));

      // Mathematically, if d(X, Y) < r then d(-X, Y) > (Pi - r).  Unfortunately there can be
      // rounding errors when computing the supplementary distance, so to ensure the two distances
      // are exactly supplementary we need to do the following.
      S1ChordAngle rSupp = S1ChordAngle.sub(S1ChordAngle.STRAIGHT, S1ChordAngle.fromLength2(r2));
      r2 = S1ChordAngle.sub(S1ChordAngle.STRAIGHT, rSupp).getLength2();
      assertEquals(
          -S2Predicates.compareDistance(x, y, r2),
          S2Predicates.compareDistance(x.neg(), y, rSupp.getLength2()));
    }

    /**
     * Checks that the result at one level of precision is consistent with the result at the next
     * higher level of Returns the minimum precision that yielded a non-zero result.
     */
    int checkConsistency(S2Point x, S2Point y, double r2) {
      int dblSign = test(x, y, r2);
      int exactSign = CompareDistance.exact(x, y, r2);
      assertEquals(exactSign, S2Predicates.compareDistance(x, y, r2));
      if (dblSign != 0) {
        assertEquals(exactSign, dblSign);
      }
      return (dblSign == 0) ? EXACT : DOUBLE;
    }
  }

  public void testCompareEdgeDistanceCoverage() {
    CheckCompareEdgeDistance c = new CheckCompareEdgeDistance();

    // Test triageLineSin2D.
    c.coverage(p(1, 1e-10, 1e-15), p(1, 0, 0), p(0, 1, 0), 1e-15 + EPS, -1, DOUBLE);
    c.coverage(p(1, 1, 1e-15), p(1, 0, 0), p(0, 1, 0), 1e-15 + EPS, -1, LDOUBLE);
    c.coverage(p(1, 1, 1e-40), p(1, 0, 0), p(0, 1, 0), 1e-40, -1, EXACT);
    c.coverage(p(1, 1, 0), p(1, 0, 0), p(0, 1, 0), 0, 0, EXACT);

    // Test triageLineCos2D.
    c.coverage(p(1e-15, 0, 1), p(1, 0, 0), p(0, 1, 0), S2.M_PI_2 - 1e-15 - 3 * EPS, 1, DOUBLE);
    c.coverage(p(1e-15, 0, 1), p(1, 0, 0), p(0, 1, 0), S2.M_PI_2 - 1e-15 - EPS, 1, LDOUBLE);
    c.coverage(p(1e-40, 0, 1), p(1, 0, 0), p(0, 1, 0), RIGHT, -1, EXACT);
    c.coverage(p(0, 0, 1), p(1, 0, 0), p(0, 1, 0), RIGHT, 0, EXACT);

    // Test cases where the closest point is an edge endpoint.
    c.coverage(p(1e-15, -1, 0), p(1, 0, 0), p(1, 1, 0), RIGHT, -1, DOUBLE);
    c.coverage(p(1e-18, -1, 0), p(1, 0, 0), p(1, 1, 0), RIGHT, -1, EXACT);
    c.coverage(p(1e-100, -1, 0), p(1, 0, 0), p(1, 1, 0), RIGHT, -1, EXACT);
    c.coverage(p(0, -1, 0), p(1, 0, 0), p(1, 1, 0), RIGHT, 0, EXACT);
  }

  /**
   * This test chooses random inputs such that the distance between "x" and the line (a0, a1) is
   * very close to the threshold distance "r". It then checks that the answer given by a method at
   * one level of precision is consistent with the answer given at the next higher level of See also
   * the comments in the CompareDistances consistency test.
   */
  @GwtIncompatible("Lack of strictfp causing error bounds math to come out wrong")
  public void testCompareEdgeDistanceConsistency() {
    PrecisionStats stats = new PrecisionStats();
    CheckCompareEdgeDistance c = new CheckCompareEdgeDistance();
    for (int iter = 0; iter < NUM_CONSISTENCY_CHECKS; ++iter) {
      S2Point a0 = choosePoint();
      S1Angle len = S1Angle.radians(S2.M_PI * Math.pow(1e-20, rand.nextDouble()));
      S2Point a1 = S2EdgeUtil.interpolateAtDistance(len, a0, choosePoint());
      if (oneIn(2)) {
        a1 = a1.neg();
      }
      if (a0.equalsPoint(a1.neg())) {
        // Not allowed by API.
        continue;
      }
      S2Point n = S2.robustCrossProd(a0, a1).normalize();
      double f = Math.pow(1e-20, rand.nextDouble());
      S2Point a = a0.mul(1 - f).add(a1.mul(f)).normalize();
      double r = S2.M_PI_2 * Math.pow(1e-20, rand.nextDouble());
      if (oneIn(2)) {
        r = S2.M_PI_2 - r;
      }
      S2Point x = S2EdgeUtil.interpolateAtDistance(S1Angle.radians(r), a, n);
      if (oneIn(5)) {
        // Replace "x" with a random point that is closest to an edge endpoint.
        do {
          x = choosePoint();
        } while (S2Predicates.compareEdgeDirections(a0, x, a0, a1) > 0
            && S2Predicates.compareEdgeDirections(x, a1, a0, a1) > 0);
        r = Double.min(x.angle(a0), x.angle(a1));
      }
      stats.tally(c.consistency(x, a0, a1, length2(r)));
    }
    System.err.println(stats);
  }

  private static class CheckCompareEdgeDistance {
    /**
     * Verifies that compareEdgeDistance(x, a0, a1, r) == expectedSign, and furthermore checks that
     * the minimum required precision is "expectedPrec".
     */
    void coverage(
        S2Point x, S2Point a0, S2Point a1, double r2, int expectedSign, int expectedPrec) {
      x = maybeNormalize(x);
      a0 = maybeNormalize(a0);
      a1 = maybeNormalize(a1);

      int dblSign = CompareEdgeDistance.triage(x, a0, a1, r2);
      int exactSign = CompareEdgeDistance.exact(x, a0, a1, r2);

      // Check that the signs are correct and consistent with each other.
      assertEquals(expectedSign, exactSign);
      if (dblSign != 0) {
        assertEquals(exactSign, dblSign);
      }

      assertEquals(expectedPrec, dblSign != 0 ? DOUBLE : EXACT);

      // Make sure that the top-level function returns the expected result.
      assertEquals(expectedSign, S2Predicates.compareEdgeDistance(x, a0, a1, r2));
    }

    /**
     * Checks that the result at one level of precision is consistent with the result at the next
     * higher level of Returns the minimum precision that yielded a non-zero result.
     */
    int consistency(S2Point x, S2Point a0, S2Point a1, double r2) {
      int dblSign = CompareEdgeDistance.triage(x, a0, a1, r2);
      int exactSign = CompareEdgeDistance.exact(x, a0, a1, r2);
      assertEquals(exactSign, S2Predicates.compareEdgeDistance(x, a0, a1, r2));
      if (dblSign != 0) {
        assertEquals(exactSign, dblSign);
      }
      return dblSign == 0 ? EXACT : DOUBLE;
    }
  }

  public void testCompareEdgeDirectionsCoverage() {
    CheckCompareEdgeDir x = new CheckCompareEdgeDir();
    x.coverage(p(1, 0, 0), p(1, 1, 0), p(1, -1, 0), p(1, 0, 0), 1, DOUBLE);
    x.coverage(p(1, 0, 1.5e-15), p(1, 1, 0), p(0, -1, 0), p(0, 0, 1), 1, DOUBLE);
    x.coverage(p(1, 0, 1e-18), p(1, 1, 0), p(0, -1, 0), p(0, 0, 1), 1, EXACT);
    x.coverage(p(1, 0, 1e-50), p(1, 1, 0), p(0, -1, 0), p(0, 0, 1), 1, EXACT);
    x.coverage(p(1, 0, 0), p(1, 1, 0), p(0, -1, 0), p(0, 0, 1), 0, EXACT);
  }

  /**
   * This test chooses random pairs of edges that are nearly perpendicular, then checks that the
   * answer given by a method at one level of precision is consistent with the answer given at the
   * next higher level of precision.
   *
   * <p>See also the comments in the CompareDistances test.
   */
  @GwtIncompatible("Lack of strictfp causing error bounds math to come out wrong")
  public void testCompareEdgeDirectionsConsistency() {
    CheckCompareEdgeDir x = new CheckCompareEdgeDir();
    PrecisionStats stats = new PrecisionStats();
    for (int iter = 0; iter < NUM_CONSISTENCY_CHECKS; ++iter) {
      S2Point a0 = choosePoint();
      S1Angle aLen = S1Angle.radians(S2.M_PI * Math.pow(1e-20, rand.nextDouble()));
      S2Point a1 = S2EdgeUtil.interpolateAtDistance(aLen, a0, choosePoint());
      S2Point aNorm = S2.robustCrossProd(a0, a1).normalize();
      S2Point b0 = choosePoint();
      S1Angle bLen = S1Angle.radians(S2.M_PI * Math.pow(1e-20, rand.nextDouble()));
      S2Point b1 = S2EdgeUtil.interpolateAtDistance(bLen, b0, aNorm);
      if (a0.equalsPoint(a1.neg()) || b0.equalsPoint(b1.neg())) {
        continue; // Not allowed by API.
      }
      int prec = x.consistency(a0, a1, b0, b1);
      // Don't skew the statistics by recording degenerate inputs.
      if (a0.equalsPoint(a1) || b0.equalsPoint(b1)) {
        assertEquals(EXACT, prec);
      } else {
        stats.tally(prec);
      }
    }
    System.err.println(stats);
  }

  private static class CheckCompareEdgeDir {
    /**
     * Verifies that CompareEdgeDirections(a0, a1, b0, b1) == expectedSign, and furthermore checks
     * that the minimum required precision is "expectedPrec".
     */
    void coverage(
        S2Point a0, S2Point a1, S2Point b0, S2Point b1, int expectedSign, int expectedPrec) {
      a0 = maybeNormalize(a0);
      a1 = maybeNormalize(a1);
      b0 = maybeNormalize(b0);
      b1 = maybeNormalize(b1);

      int triageSign = CompareEdgeDirections.triage(a0, a1, b0, b1);
      int exactSign = CompareEdgeDirections.exact(a0, a1, b0, b1);

      // Check that the signs are correct (if non-zero), and also that if triageSign is non-zero
      // then so is exactSign, etc.
      assertEquals(expectedSign, exactSign);
      if (triageSign != 0) {
        assertEquals(exactSign, triageSign);
      }

      assertEquals(expectedPrec, triageSign != 0 ? DOUBLE : EXACT);

      // Make sure that the top-level function returns the expected result.
      assertEquals(expectedSign, S2Predicates.compareEdgeDirections(a0, a1, b0, b1));

      // Check various identities involving swapping or negating arguments.
      assertEquals(expectedSign, S2Predicates.compareEdgeDirections(b0, b1, a0, a1));
      assertEquals(expectedSign, S2Predicates.compareEdgeDirections(a0.neg(), a1.neg(), b0, b1));
      assertEquals(expectedSign, S2Predicates.compareEdgeDirections(a0, a1, b0.neg(), b1.neg()));
      assertEquals(-expectedSign, S2Predicates.compareEdgeDirections(a1, a0, b0, b1));
      assertEquals(-expectedSign, S2Predicates.compareEdgeDirections(a0, a1, b1, b0));
      assertEquals(-expectedSign, S2Predicates.compareEdgeDirections(a0.neg(), a1, b0, b1));
      assertEquals(-expectedSign, S2Predicates.compareEdgeDirections(a0, a1.neg(), b0, b1));
      assertEquals(-expectedSign, S2Predicates.compareEdgeDirections(a0, a1, b0.neg(), b1));
      assertEquals(-expectedSign, S2Predicates.compareEdgeDirections(a0, a1, b0, b1.neg()));
    }

    /**
     * Checks that the result at one level of precision is consistent with the result at the next
     * higher level of Returns the minimum precision that yielded a non-zero result.
     */
    int consistency(S2Point a0, S2Point a1, S2Point b0, S2Point b1) {
      int triageSign = CompareEdgeDirections.triage(a0, a1, b0, b1);
      int exactSign = CompareEdgeDirections.exact(a0, a1, b0, b1);
      assertEquals(exactSign, S2Predicates.compareEdgeDirections(a0, a1, b0, b1));
      if (triageSign != 0) {
        assertEquals(exactSign, triageSign);
      }
      return (triageSign == 0) ? EXACT : DOUBLE;
    }
  }

  public void testEdgeCircumcenterSignCoverage() {
    CheckCircumcenterSign x = new CheckCircumcenterSign();
    x.coverage(p(1, 0, 0), p(1, 1, 0), p(0, 0, 1), p(1, 0, 1), p(0, 1, 1), 1, DOUBLE);
    x.coverage(p(1, 0, 0), p(1, 1, 0), p(0, 0, -1), p(1, 0, -1), p(0, 1, -1), -1, DOUBLE);
    x.coverage(
        p(1, -1, 0), p(1, 1, 0), p(1, -1e-5, 1), p(1, 1e-5, -1), p(1, 1 - 1e-5, 1e-5), -1, DOUBLE);
    x.coverage(
        p(1, -1, 0), p(1, 1, 0), p(1, -1e-5, 1), p(1, 1e-5, -1), p(1, 1 - 1e-9, 1e-5), -1, EXACT);
    x.coverage(
        p(1, -1, 0), p(1, 1, 0), p(1, -1e-5, 1), p(1, 1e-5, -1), p(1, 1 - 1e-15, 1e-5), -1, EXACT);
    x.coverage(p(1, -1, 0), p(1, 1, 0), p(1, -1e-5, 1), p(1, 1e-5, -1), p(1, 1, 1e-5), 1, SYMBOLIC);

    // This test falls back to the second symbolic perturbation:
    x.coverage(p(1, -1, 0), p(1, 1, 0), p(0, -1, 0), p(0, 0, -1), p(0, 0, 1), -1, SYMBOLIC);

    // This test falls back to the third symbolic perturbation:
    x.coverage(p(0, -1, 1), p(0, 1, 1), p(0, 1, 0), p(0, -1, 0), p(1, 0, 0), -1, SYMBOLIC);
  }

  /**
   * This test chooses random a random edge X, then chooses a random point Z on the great circle
   * through X, and finally choose three points A, B, C that are nearly equidistant from X. It then
   * checks that the answer given by a method at one level of precision is consistent with the
   * answer given at the next higher level of precision.
   */
  @GwtIncompatible("Lack of strictfp causing error bounds math to come out wrong")
  public void testEdgeCircumcenterSignConsistency() {
    PrecisionStats stats = new PrecisionStats();
    CheckCircumcenterSign x = new CheckCircumcenterSign();
    for (int iter = 0; iter < NUM_CONSISTENCY_CHECKS; iter++) {
      S2Point p = choosePoint();
      S2Point q = choosePoint();
      if (p.equalsPoint(q.neg())) {
        continue; // Not allowed by API.
      }
      double c0 = (oneIn(2) ? -1 : 1) * Math.pow(1e-20, rand.nextDouble());
      double c1 = (oneIn(2) ? -1 : 1) * Math.pow(1e-20, rand.nextDouble());
      S2Point z = p.mul(c0).add(q.mul(c1)).normalize();
      S1Angle r = S1Angle.radians(S2.M_PI * Math.pow(1e-30, rand.nextDouble()));
      S2Point a = S2EdgeUtil.interpolateAtDistance(r, z, choosePoint());
      S2Point b = S2EdgeUtil.interpolateAtDistance(r, z, choosePoint());
      S2Point c = S2EdgeUtil.interpolateAtDistance(r, z, choosePoint());
      int prec = x.consistency(p, q, a, b, c);
      // Don't skew the statistics by recording degenerate inputs.
      if (p.equalsPoint(q)) {
        // This precision would be SYMBOLIC if we handled this degeneracy.
        assertEquals(EXACT, prec);
      } else if (a.equalsPoint(b) || b.equalsPoint(c) || c.equalsPoint(a)) {
        assertEquals(SYMBOLIC, prec);
      } else {
        stats.tally(prec);
      }
    }
    System.err.println(stats);
  }

  private static class CheckCircumcenterSign {
    /**
     * Verifies that edgeCircumcenterSign(p, q, a, b, c) == expectedSign, and furthermore checks
     * that the minimum required precision is "expectedPrec".
     */
    void coverage(
        S2Point p, S2Point q, S2Point a, S2Point b, S2Point c, int expectedSign, int expectedPrec) {
      p = maybeNormalize(p);
      q = maybeNormalize(q);
      a = maybeNormalize(a);
      b = maybeNormalize(b);
      c = maybeNormalize(c);

      int abc = sign(a, b, c);
      int triageSign = EdgeCircumcenterSign.triage(p, q, a, b, c, abc);
      int exactSign = EdgeCircumcenterSign.exact(p, q, a, b, c, abc);
      int actualSign = exactSign != 0 ? exactSign : EdgeCircumcenterSign.sos(p, q, a, b, c);

      // Check that the signs are correct (if non-zero), and also that if dbl_sign
      // is non-zero then so is ld_sign, etc.
      assertEquals(expectedSign, actualSign);
      if (exactSign != 0) {
        assertEquals(exactSign, actualSign);
      }
      if (triageSign != 0) {
        assertEquals(exactSign, triageSign);
      }

      assertEquals(expectedPrec, triageSign != 0 ? DOUBLE : exactSign != 0 ? EXACT : SYMBOLIC);

      // Make sure that the top-level function returns the expected result.
      assertEquals(expectedSign, S2Predicates.edgeCircumcenterSign(p, q, a, b, c));

      // Check various identities involving swapping or negating arguments.
      assertEquals(expectedSign, S2Predicates.edgeCircumcenterSign(p, q, a, c, b));
      assertEquals(expectedSign, S2Predicates.edgeCircumcenterSign(p, q, b, a, c));
      assertEquals(expectedSign, S2Predicates.edgeCircumcenterSign(p, q, b, c, a));
      assertEquals(expectedSign, S2Predicates.edgeCircumcenterSign(p, q, c, a, b));
      assertEquals(expectedSign, S2Predicates.edgeCircumcenterSign(p, q, c, b, a));
      assertEquals(-expectedSign, S2Predicates.edgeCircumcenterSign(q, p, a, b, c));
      assertEquals(expectedSign, S2Predicates.edgeCircumcenterSign(p.neg(), q.neg(), a, b, c));
      if (actualSign == exactSign) {
        // Negating the input points may not preserve the result when symbolic
        // perturbations are used, since -X is not an exact multiple of X.
        assertEquals(
            -expectedSign, S2Predicates.edgeCircumcenterSign(p, q, a.neg(), b.neg(), c.neg()));
      }
    }

    /**
     * Checks that the result at one level of precision is consistent with the result at the next
     * higher level of Returns the minimum precision that yielded a non-zero result.
     */
    int consistency(S2Point p, S2Point q, S2Point a, S2Point b, S2Point c) {
      int abc = sign(a, b, c);
      int triageSign = EdgeCircumcenterSign.triage(p, q, a, b, c, abc);
      int exactSign = EdgeCircumcenterSign.exact(p, q, a, b, c, abc);
      if (triageSign != 0) {
        assertEquals(exactSign, triageSign);
      }
      if (exactSign != 0) {
        assertEquals(exactSign, S2Predicates.edgeCircumcenterSign(p, q, a, b, c));
        return triageSign == 0 ? EXACT : DOUBLE;
      } else {
        // Unlike the other methods, SymbolicEdgeCircumcenterSign has the precondition that the
        // exact sign must be zero.
        int symbolicSign = EdgeCircumcenterSign.sos(p, q, a, b, c);
        assertEquals(symbolicSign, S2Predicates.edgeCircumcenterSign(p, q, a, b, c));
        return SYMBOLIC;
      }
    }
  }

  public void testVoronoiSiteExclusionCoverage() {
    CheckVoronoiSiteExclusion x = new CheckVoronoiSiteExclusion();

    // Both sites are closest to edge endpoint P.
    x.coverage(
        p(1, -1e-5, 0),
        p(1, -2e-5, 0),
        p(1, 0, 0),
        p(1, 1, 0),
        length2(1e-3),
        Excluded.SECOND,
        DOUBLE);
    // Both sites are closest to edge endpoint Q.
    x.coverage(
        p(1, 1, 1e-30),
        p(1, 1, -1e-20),
        p(1, 0, 0),
        p(1, 1, 0),
        length2(1e-10),
        Excluded.SECOND,
        DOUBLE);
    // Test cases where neither site is excluded.
    x.coverage(
        p(1, -1e-10, 1e-5),
        p(1, 1e-10, -1e-5),
        p(1, -1, 0),
        p(1, 1, 0),
        length2(1e-4),
        Excluded.NEITHER,
        DOUBLE);
    x.coverage(
        p(1, -1e-10, 1e-5),
        p(1, 1e-10, -1e-5),
        p(1, -1, 0),
        p(1, 1, 0),
        length2(1e-5),
        Excluded.NEITHER,
        EXACT);
    x.coverage(
        p(1, -1e-17, 1e-5),
        p(1, 1e-17, -1e-5),
        p(1, -1, 0),
        p(1, 1, 0),
        length2(1e-4),
        Excluded.NEITHER,
        EXACT);
    x.coverage(
        p(1, -1e-20, 1e-5),
        p(1, 1e-20, -1e-5),
        p(1, -1, 0),
        p(1, 1, 0),
        length2(1e-5),
        Excluded.NEITHER,
        EXACT);

    // Test cases where the first site is excluded. Tests where the second site is excluded are
    // constructed by testVoronoiSiteExclusion).
    x.coverage(
        p(1, -1e-6, 1.0049999999e-5),
        p(1, 0, -1e-5),
        p(1, -1, 0),
        p(1, 1, 0),
        length2(1.005e-5),
        Excluded.FIRST,
        DOUBLE);
    x.coverage(
        p(1, -1.00105e-6, 1.0049999999e-5),
        p(1, 0, -1e-5),
        p(1, -1, 0),
        p(1, 1, 0),
        length2(1.005e-5),
        Excluded.FIRST,
        EXACT);
    x.coverage(
        p(1, -1e-6, 1.005e-5),
        p(1, 0, -1e-5),
        p(1, -1, 0),
        p(1, 1, 0),
        length2(1.005e-5),
        Excluded.FIRST,
        EXACT);
    x.coverage(
        p(1, -1e-31, 1.005e-30),
        p(1, 0, -1e-30),
        p(1, -1, 0),
        p(1, 1, 0),
        length2(1.005e-30),
        Excluded.FIRST,
        EXACT);
    x.coverage(
        p(1, -1e-31, 1.005e-30),
        p(1, 0, -1e-30),
        p(1, -1, 0),
        p(1, 1, 0),
        length2(1.005e-30),
        Excluded.FIRST,
        EXACT);

    // These two sites are exactly 60 degrees away from the point (1, 1, 0), which is the midpoint
    // of edge X.  This case requires symbolic perturbations to resolve correctly.  Site A is closer
    // to every point in its coverage interval except for (1, 1, 0), but site B is considered closer
    // to that point symbolically.
    x.coverage(p(0, 1, 1), p(1, 0, 1), p(0, 1, 1), p(1, 0, -1), 1, Excluded.NEITHER, EXACT);

    // This test is similar except that site A is considered closer to the equidistant point
    // (-1, 1, 0), and therefore site B is excluded.
    x.coverage(p(0, 1, 1), p(-1, 0, 1), p(0, 1, 1), p(-1, 0, -1), 1, Excluded.SECOND, EXACT);
  }

  /**
   * This test chooses random a random edge PQ, a random point X on that edge, and a random
   * threshold distance "r". It then choose two sites A and B whose distance to P is almost exactly
   * "r". This ensures that the coverage intervals for A and B will (almost) share a common
   * endpoint. It then checks that the answer given by a method at one level of precision is
   * consistent with the answer given at higher levels of precision.
   */
  @GwtIncompatible("Lack of strictfp causing error bounds math to come out wrong")
  public void testVoronoiSiteExclusionConsistency() {
    CheckVoronoiSiteExclusion v = new CheckVoronoiSiteExclusion();
    PrecisionStats stats = new PrecisionStats();
    for (int iter = 0; iter < NUM_CONSISTENCY_CHECKS; ++iter) {
      S2Point p = choosePoint();
      S2Point q = choosePoint();
      if (p.equalsPoint(q.neg())) {
        // Not allowed by API.
        continue;
      }
      double f = Math.pow(1e-20, rand.nextDouble());
      S2Point x = p.mul(1 - f).add(q.mul(f)).normalize();
      double r1 = S2.M_PI_2 * Math.pow(1e-20, rand.nextDouble());
      S2Point a = S2EdgeUtil.interpolateAtDistance(S1Angle.radians(r1), x, choosePoint());
      S2Point b = S2EdgeUtil.interpolateAtDistance(S1Angle.radians(r1), x, choosePoint());
      // Check that the other API requirements are met.
      double r2 = length2(r1);
      if (S2Predicates.compareEdgeDistance(a, p, q, r2) > 0) {
        continue;
      }
      if (S2Predicates.compareEdgeDistance(b, p, q, r2) > 0) {
        continue;
      }
      if (S2Predicates.compareDistances(p, a, b) > 0) {
        S2Point temp = a;
        a = b;
        b = temp;
      }
      if (a.equalsPoint(b)) {
        continue;
      }

      int prec = v.consistency(a, b, p, q, r2);
      // Don't skew the statistics by recording degenerate inputs.
      if (p.equalsPoint(q)) {
        assertEquals(DOUBLE, prec);
      } else {
        stats.tally(prec);
      }
    }
    System.err.println(stats);
  }

  private static class CheckVoronoiSiteExclusion {
    /**
     * Verifies that voronoiSiteExclusion(a, b, p, q, r) == expectedResult, and furthermore checks
     * that the minimum required precision is "expectedPrec".
     */
    void coverage(
        S2Point a,
        S2Point b,
        S2Point p,
        S2Point q,
        double r2,
        Excluded expectedResult,
        int expectedPrec) {
      a = maybeNormalize(a);
      b = maybeNormalize(b);
      p = maybeNormalize(p);
      q = maybeNormalize(q);

      // The internal methods (triage, exact, etc) require that site A is closer to P and site B is
      // closer to Q.  GetVoronoiSiteExclusion has special code to handle the case where this is not
      // true.  We need to duplicate that code here.  Essentially, as the API requires site A to be
      // closer than site B to P, then if site A is also closer to Q then site B must be excluded.
      if (S2Predicates.compareDistances(q, a, b) < 0) {
        assertEquals(expectedResult, Excluded.SECOND);
        // We don't know what precision was used by CompareDistances(), but we arbitrarily require
        // the test to specify it as DOUBLE.
        assertEquals(expectedPrec, DOUBLE);
      } else {
        // Check that the results are correct (if not UNCERTAIN), and also that if triageResult is
        // not UNCERTAIN then so is exactResult.
        Excluded triageResult = VoronoiSiteExclusion.triage(a, b, p, q, r2);
        Excluded exactResult = VoronoiSiteExclusion.exact(a, b, p, q, r2);
        assertEquals(expectedResult, exactResult);
        if (triageResult != Excluded.UNCERTAIN) {
          assertEquals(exactResult, triageResult);
        }
        assertEquals(expectedPrec, triageResult != Excluded.UNCERTAIN ? DOUBLE : EXACT);
      }

      // Make sure that the top-level function returns the expected result.
      assertEquals(expectedResult, S2Predicates.getVoronoiSiteExclusion(a, b, p, q, r2));

      // If site B is closer to Q, then the same site should be excluded (if any) when we swap the
      // sites and the edge direction.
      Excluded swappedResult =
          expectedResult == Excluded.FIRST
              ? Excluded.SECOND
              : expectedResult == Excluded.SECOND ? Excluded.FIRST : expectedResult;
      if (S2Predicates.compareDistances(q, b, a) < 0) {
        assertEquals(swappedResult, S2Predicates.getVoronoiSiteExclusion(b, a, q, p, r2));
      }
    }

    /**
     * Checks that the result at one level of precision is consistent with the result at the next
     * higher level of Returns the minimum precision that yielded a non-zero result.
     */
    int consistency(S2Point a, S2Point b, S2Point p, S2Point q, double r2) {
      // The internal methods require this (see TestVoronoiSiteExclusion).
      if (S2Predicates.compareDistances(q, a, b) < 0) {
        return DOUBLE;
      }

      Excluded triageResult = VoronoiSiteExclusion.triage(a, b, p, q, r2);
      Excluded exactResult = VoronoiSiteExclusion.exact(a, b, p, q, r2);
      assertEquals(exactResult, S2Predicates.getVoronoiSiteExclusion(a, b, p, q, r2));
      assertNotSame(Excluded.UNCERTAIN, exactResult);
      if (triageResult == Excluded.UNCERTAIN) {
        return EXACT;
      }
      assertEquals(exactResult, triageResult);
      return DOUBLE;
    }
  }

  /** Normalize only when necessary, to allow testing points that differ a bit in magnitude. */
  private static S2Point maybeNormalize(S2Point p) {
    return S2.isUnitLength(p) ? p : p.normalize();
  }

  private static S2Point p(double x, double y, double z) {
    return new S2Point(x, y, z);
  }

  /** Returns the squared chord distance of an angular opening of the given radians. */
  private static double length2(double radians) {
    return S1ChordAngle.fromS1Angle(S1Angle.radians(radians)).getLength2();
  }

  /** A tracker of how often each precision was used. */
  private static class PrecisionStats {
    int[] counts = new int[3];

    void tally(int precision) {
      counts[precision]++;
    }

    @Override
    public String toString() {
      return ImmutableMap.of(
              "DOUBLE", counts[0],
              "EXACT", counts[1],
              "SYMBOLIC", counts[2])
          .toString();
    }
  }
}
