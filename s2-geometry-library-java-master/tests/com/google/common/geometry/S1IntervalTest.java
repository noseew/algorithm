/*
 * Copyright 2005 Google Inc.
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

import com.google.common.annotations.GwtCompatible;

/** Tests for {@link S1Interval}. */
@GwtCompatible
public strictfp class S1IntervalTest extends GeometryTestCase {
  // Create some standard intervals to use in the tests.  These include the
  // empty and full intervals, intervals containing a single point, and
  // intervals spanning one or more "quadrants" which are numbered as follows:
  //    quad1 == [0, Pi/2]
  //    quad2 == [Pi/2, Pi]
  //    quad3 == [-Pi, -Pi/2]
  //    quad4 == [-Pi/2, 0]
  S1Interval empty = S1Interval.empty();
  S1Interval full = S1Interval.full();
  // Single-point intervals:
  S1Interval zero = new S1Interval(0, 0);
  S1Interval pi2 = new S1Interval(S2.M_PI_2, S2.M_PI_2);
  S1Interval pi = new S1Interval(S2.M_PI, S2.M_PI);
  S1Interval mipi = new S1Interval(-S2.M_PI, -S2.M_PI); // Same as "pi" after normalization.
  S1Interval mipi2 = new S1Interval(-S2.M_PI_2, -S2.M_PI_2);
  // Single quadrants:
  S1Interval quad1 = new S1Interval(0, S2.M_PI_2);
  S1Interval quad2 = new S1Interval(S2.M_PI_2, -S2.M_PI);
  S1Interval quad3 = new S1Interval(S2.M_PI, -S2.M_PI_2);
  S1Interval quad4 = new S1Interval(-S2.M_PI_2, 0);
  // Quadrant pairs:
  S1Interval quad12 = new S1Interval(0, -S2.M_PI);
  S1Interval quad23 = new S1Interval(S2.M_PI_2, -S2.M_PI_2);
  S1Interval quad34 = new S1Interval(-S2.M_PI, 0);
  S1Interval quad41 = new S1Interval(-S2.M_PI_2, S2.M_PI_2);
  // Quadrant triples:
  S1Interval quad123 = new S1Interval(0, -S2.M_PI_2);
  S1Interval quad234 = new S1Interval(S2.M_PI_2, 0);
  S1Interval quad341 = new S1Interval(S2.M_PI, S2.M_PI_2);
  S1Interval quad412 = new S1Interval(-S2.M_PI_2, -S2.M_PI);
  // Small intervals around the midpoints between quadrants, such that
  // the center of each interval is offset slightly CCW from the midpoint.
  S1Interval mid12 = new S1Interval(S2.M_PI_2 - 0.01, S2.M_PI_2 + 0.02);
  S1Interval mid23 = new S1Interval(S2.M_PI - 0.01, -S2.M_PI + 0.02);
  S1Interval mid34 = new S1Interval(-S2.M_PI_2 - 0.01, -S2.M_PI_2 + 0.02);
  S1Interval mid41 = new S1Interval(-0.01, 0.02);

  public void testConstructorsAndAccessors() {
    // Spot-check the constructors and accessors.
    assertEquals(0d, quad12.lo());
    assertEquals(S2.M_PI, quad12.hi());
    assertEquals(S2.M_PI, quad34.get(0));
    assertEquals(0d, quad34.get(1));
    assertEquals(S2.M_PI, pi.lo());
    assertEquals(S2.M_PI, pi.hi());

    // Check that [-Pi, -Pi] is normalized to [Pi, Pi].
    assertEquals(S2.M_PI, mipi.lo());
    assertEquals(S2.M_PI, mipi.hi());
    assertEquals(S2.M_PI_2, quad23.lo());
    assertEquals(-S2.M_PI_2, quad23.hi());

    // Check that the default S1Interval is identical to Empty().
    S1Interval defaultEmpty = new S1Interval();
    assertTrue(defaultEmpty.isValid());
    assertTrue(defaultEmpty.isEmpty());
    assertEquals(empty.lo(), defaultEmpty.lo());
    assertEquals(empty.hi(), defaultEmpty.hi());
  }

  public void testSimplePredicates() {
    // is_valid(), is_empty(), is_full(), is_inverted()
    assertTrue(zero.isValid() && !zero.isEmpty() && !zero.isFull());
    assertTrue(empty.isValid() && empty.isEmpty() && !empty.isFull());
    assertTrue(empty.isInverted());
    assertTrue(full.isValid() && !full.isEmpty() && full.isFull());
    assertTrue(!quad12.isEmpty() && !quad12.isFull() && !quad12.isInverted());
    assertTrue(!quad23.isEmpty() && !quad23.isFull() && quad23.isInverted());
    assertTrue(pi.isValid() && !pi.isEmpty() && !pi.isInverted());
    assertTrue(mipi.isValid() && !mipi.isEmpty() && !mipi.isInverted());
  }

  public void testGetCenter() {
    assertEquals(S2.M_PI_2, quad12.getCenter());
    assertEquals(3.0 - S2.M_PI, new S1Interval(3.1, 2.9).getCenter());
    assertEquals(S2.M_PI - 3.0, new S1Interval(-2.9, -3.1).getCenter());
    assertEquals(S2.M_PI, new S1Interval(2.1, -2.1).getCenter());
    assertEquals(S2.M_PI, pi.getCenter());
    assertEquals(S2.M_PI, mipi.getCenter());
    assertEquals(S2.M_PI, Math.abs(quad23.getCenter()));
    assertEquals(0.75 * S2.M_PI, quad123.getCenter());
  }

  public void testGetLength() {
    assertEquals(S2.M_PI, quad12.getLength());
    assertEquals(0d, pi.getLength());
    assertEquals(0d, mipi.getLength());
    assertEquals(1.5 * S2.M_PI, quad123.getLength());
    assertEquals(S2.M_PI, Math.abs(quad23.getLength()));
    assertEquals(2 * S2.M_PI, full.getLength());
    assertTrue(empty.getLength() < 0);
  }

  public void testComplement() {
    assertTrue(empty.complement().isFull());
    assertTrue(full.complement().isEmpty());
    assertTrue(pi.complement().isFull());
    assertTrue(mipi.complement().isFull());
    assertTrue(zero.complement().isFull());
    assertTrue(quad12.complement().approxEquals(quad34));
    assertTrue(quad34.complement().approxEquals(quad12));
    assertTrue(quad123.complement().approxEquals(quad4));
  }

  public void testContains() {
    // Contains(double), InteriorContains(double)
    assertTrue(!empty.contains(0) && !empty.contains(S2.M_PI) && !empty.contains(-S2.M_PI));
    assertTrue(!empty.interiorContains(S2.M_PI) && !empty.interiorContains(-S2.M_PI));
    assertTrue(full.contains(0) && full.contains(S2.M_PI) && full.contains(-S2.M_PI));
    assertTrue(full.interiorContains(S2.M_PI) && full.interiorContains(-S2.M_PI));
    assertTrue(quad12.contains(0) && quad12.contains(S2.M_PI) && quad12.contains(-S2.M_PI));
    assertTrue(quad12.interiorContains(S2.M_PI_2) && !quad12.interiorContains(0));
    assertTrue(!quad12.interiorContains(S2.M_PI) && !quad12.interiorContains(-S2.M_PI));
    assertTrue(quad23.contains(S2.M_PI_2) && quad23.contains(-S2.M_PI_2));
    assertTrue(quad23.contains(S2.M_PI) && quad23.contains(-S2.M_PI));
    assertTrue(!quad23.contains(0));
    assertTrue(!quad23.interiorContains(S2.M_PI_2) && !quad23.interiorContains(-S2.M_PI_2));
    assertTrue(quad23.interiorContains(S2.M_PI) && quad23.interiorContains(-S2.M_PI));
    assertTrue(!quad23.interiorContains(0));
    assertTrue(pi.contains(S2.M_PI) && pi.contains(-S2.M_PI) && !pi.contains(0));
    assertTrue(!pi.interiorContains(S2.M_PI) && !pi.interiorContains(-S2.M_PI));
    assertTrue(mipi.contains(S2.M_PI) && mipi.contains(-S2.M_PI) && !mipi.contains(0));
    assertTrue(!mipi.interiorContains(S2.M_PI) && !mipi.interiorContains(-S2.M_PI));
    assertTrue(zero.contains(0) && !zero.interiorContains(0));
  }

  private void testIntervalOps(
      S1Interval x,
      S1Interval y,
      String expectedRelation,
      S1Interval expectedUnion,
      S1Interval expectedIntersection) {
    // Test all of the interval operations on the given pair of intervals.
    // "expectedRelation" is a sequence of "T" and "F" characters corresponding
    // to the expected results of Contains(), InteriorContains(), Intersects(),
    // and InteriorIntersects() respectively.
    assertEquals(x.contains(y), expectedRelation.charAt(0) == 'T');
    assertEquals(x.interiorContains(y), expectedRelation.charAt(1) == 'T');
    assertEquals(x.intersects(y), expectedRelation.charAt(2) == 'T');
    assertEquals(x.interiorIntersects(y), expectedRelation.charAt(3) == 'T');

    // bounds() returns a const reference to a member variable, so we need to
    // make a copy when invoking it on a temporary object.
    assertEquals(expectedUnion, x.union(y));
    assertEquals(expectedIntersection, x.intersection(y));

    assertEquals(x.contains(y), x.union(y).equals(x));
    assertEquals(x.intersects(y), !x.intersection(y).isEmpty());

    if (y.lo() == y.hi()) {
      S1Interval r = x;
      r = r.addPoint(y.lo());
      assertEquals(expectedUnion, r);
    }
  }

  public void testIntervalOps() {
    // Contains(S1Interval), InteriorContains(S1Interval),
    // Intersects(), InteriorIntersects(), Union(), Intersection()
    testIntervalOps(empty, empty, "TTFF", empty, empty);
    testIntervalOps(empty, full, "FFFF", full, empty);
    testIntervalOps(empty, zero, "FFFF", zero, empty);
    testIntervalOps(empty, pi, "FFFF", pi, empty);
    testIntervalOps(empty, mipi, "FFFF", mipi, empty);

    testIntervalOps(full, empty, "TTFF", full, empty);
    testIntervalOps(full, full, "TTTT", full, full);
    testIntervalOps(full, zero, "TTTT", full, zero);
    testIntervalOps(full, pi, "TTTT", full, pi);
    testIntervalOps(full, mipi, "TTTT", full, mipi);
    testIntervalOps(full, quad12, "TTTT", full, quad12);
    testIntervalOps(full, quad23, "TTTT", full, quad23);

    testIntervalOps(zero, empty, "TTFF", zero, empty);
    testIntervalOps(zero, full, "FFTF", full, zero);
    testIntervalOps(zero, zero, "TFTF", zero, zero);
    testIntervalOps(zero, pi, "FFFF", new S1Interval(0, S2.M_PI), empty);
    testIntervalOps(zero, pi2, "FFFF", quad1, empty);
    testIntervalOps(zero, mipi, "FFFF", quad12, empty);
    testIntervalOps(zero, mipi2, "FFFF", quad4, empty);
    testIntervalOps(zero, quad12, "FFTF", quad12, zero);
    testIntervalOps(zero, quad23, "FFFF", quad123, empty);

    testIntervalOps(pi2, empty, "TTFF", pi2, empty);
    testIntervalOps(pi2, full, "FFTF", full, pi2);
    testIntervalOps(pi2, zero, "FFFF", quad1, empty);
    testIntervalOps(pi2, pi, "FFFF", new S1Interval(S2.M_PI_2, S2.M_PI), empty);
    testIntervalOps(pi2, pi2, "TFTF", pi2, pi2);
    testIntervalOps(pi2, mipi, "FFFF", quad2, empty);
    testIntervalOps(pi2, mipi2, "FFFF", quad23, empty);
    testIntervalOps(pi2, quad12, "FFTF", quad12, pi2);
    testIntervalOps(pi2, quad23, "FFTF", quad23, pi2);

    testIntervalOps(pi, empty, "TTFF", pi, empty);
    testIntervalOps(pi, full, "FFTF", full, pi);
    testIntervalOps(pi, zero, "FFFF", new S1Interval(S2.M_PI, 0), empty);
    testIntervalOps(pi, pi, "TFTF", pi, pi);
    testIntervalOps(pi, pi2, "FFFF", new S1Interval(S2.M_PI_2, S2.M_PI), empty);
    testIntervalOps(pi, mipi, "TFTF", pi, pi);
    testIntervalOps(pi, mipi2, "FFFF", quad3, empty);
    testIntervalOps(pi, quad12, "FFTF", new S1Interval(0, S2.M_PI), pi);
    testIntervalOps(pi, quad23, "FFTF", quad23, pi);

    testIntervalOps(mipi, empty, "TTFF", mipi, empty);
    testIntervalOps(mipi, full, "FFTF", full, mipi);
    testIntervalOps(mipi, zero, "FFFF", quad34, empty);
    testIntervalOps(mipi, pi, "TFTF", mipi, mipi);
    testIntervalOps(mipi, pi2, "FFFF", quad2, empty);
    testIntervalOps(mipi, mipi, "TFTF", mipi, mipi);
    testIntervalOps(mipi, mipi2, "FFFF", new S1Interval(-S2.M_PI, -S2.M_PI_2), empty);
    testIntervalOps(mipi, quad12, "FFTF", quad12, mipi);
    testIntervalOps(mipi, quad23, "FFTF", quad23, mipi);

    testIntervalOps(quad12, empty, "TTFF", quad12, empty);
    testIntervalOps(quad12, full, "FFTT", full, quad12);
    testIntervalOps(quad12, zero, "TFTF", quad12, zero);
    testIntervalOps(quad12, pi, "TFTF", quad12, pi);
    testIntervalOps(quad12, mipi, "TFTF", quad12, mipi);
    testIntervalOps(quad12, quad12, "TFTT", quad12, quad12);
    testIntervalOps(quad12, quad23, "FFTT", quad123, quad2);
    testIntervalOps(quad12, quad34, "FFTF", full, quad12);

    testIntervalOps(quad23, empty, "TTFF", quad23, empty);
    testIntervalOps(quad23, full, "FFTT", full, quad23);
    testIntervalOps(quad23, zero, "FFFF", quad234, empty);
    testIntervalOps(quad23, pi, "TTTT", quad23, pi);
    testIntervalOps(quad23, mipi, "TTTT", quad23, mipi);
    testIntervalOps(quad23, quad12, "FFTT", quad123, quad2);
    testIntervalOps(quad23, quad23, "TFTT", quad23, quad23);
    testIntervalOps(quad23, quad34, "FFTT", quad234, new S1Interval(-S2.M_PI, -S2.M_PI_2));

    testIntervalOps(quad1, quad23, "FFTF", quad123, new S1Interval(S2.M_PI_2, S2.M_PI_2));
    testIntervalOps(quad2, quad3, "FFTF", quad23, mipi);
    testIntervalOps(quad3, quad2, "FFTF", quad23, pi);
    testIntervalOps(quad2, pi, "TFTF", quad2, pi);
    testIntervalOps(quad2, mipi, "TFTF", quad2, mipi);
    testIntervalOps(quad3, pi, "TFTF", quad3, pi);
    testIntervalOps(quad3, mipi, "TFTF", quad3, mipi);

    testIntervalOps(quad12, mid12, "TTTT", quad12, mid12);
    testIntervalOps(mid12, quad12, "FFTT", quad12, mid12);

    S1Interval quad12eps = new S1Interval(quad12.lo(), mid23.hi());
    S1Interval quad2hi = new S1Interval(mid23.lo(), quad12.hi());
    testIntervalOps(quad12, mid23, "FFTT", quad12eps, quad2hi);
    testIntervalOps(mid23, quad12, "FFTT", quad12eps, quad2hi);

    // This test checks that the union of two disjoint intervals is the smallest
    // interval that contains both of them.  Note that the center of "mid34"
    // slightly CCW of -Pi/2 so that there is no ambiguity about the result.
    S1Interval quad412eps = new S1Interval(mid34.lo(), quad12.hi());
    testIntervalOps(quad12, mid34, "FFFF", quad412eps, empty);
    testIntervalOps(mid34, quad12, "FFFF", quad412eps, empty);

    S1Interval quadeps12 = new S1Interval(mid41.lo(), quad12.hi());
    S1Interval quad1lo = new S1Interval(quad12.lo(), mid41.hi());
    testIntervalOps(quad12, mid41, "FFTT", quadeps12, quad1lo);
    testIntervalOps(mid41, quad12, "FFTT", quadeps12, quad1lo);

    S1Interval quad2lo = new S1Interval(quad23.lo(), mid12.hi());
    S1Interval quad3hi = new S1Interval(mid34.lo(), quad23.hi());
    S1Interval quadeps23 = new S1Interval(mid12.lo(), quad23.hi());
    S1Interval quad23eps = new S1Interval(quad23.lo(), mid34.hi());
    S1Interval quadeps123 = new S1Interval(mid41.lo(), quad23.hi());
    testIntervalOps(quad23, mid12, "FFTT", quadeps23, quad2lo);
    testIntervalOps(mid12, quad23, "FFTT", quadeps23, quad2lo);
    testIntervalOps(quad23, mid23, "TTTT", quad23, mid23);
    testIntervalOps(mid23, quad23, "FFTT", quad23, mid23);
    testIntervalOps(quad23, mid34, "FFTT", quad23eps, quad3hi);
    testIntervalOps(mid34, quad23, "FFTT", quad23eps, quad3hi);
    testIntervalOps(quad23, mid41, "FFFF", quadeps123, empty);
    testIntervalOps(mid41, quad23, "FFFF", quadeps123, empty);
  }

  public void testAddPoint() {
    S1Interval r = empty;
    r = r.addPoint(0);
    assertEquals(r, zero);
    r = empty;
    r = r.addPoint(S2.M_PI);
    assertEquals(r, pi);
    r = empty;
    r = r.addPoint(-S2.M_PI);
    assertEquals(r, mipi);
    r = empty;
    r = r.addPoint(S2.M_PI);
    r = r.addPoint(-S2.M_PI);
    assertEquals(r, pi);
    r = empty;
    r = r.addPoint(-S2.M_PI);
    r = r.addPoint(S2.M_PI);
    assertEquals(r, mipi);
    r = empty;
    r = r.addPoint(mid12.lo());
    r = r.addPoint(mid12.hi());
    assertEquals(r, mid12);
    r = empty;
    r = r.addPoint(mid23.lo());
    r = r.addPoint(mid23.hi());
    assertEquals(r, mid23);
    r = quad1;
    r = r.addPoint(-0.9 * S2.M_PI);
    r = r.addPoint(-S2.M_PI_2);
    assertEquals(r, quad123);
    r = full;
    r = r.addPoint(0);
    assertTrue(r.isFull());
    r = full;
    r = r.addPoint(S2.M_PI);
    assertTrue(r.isFull());
    r = full;
    r = r.addPoint(-S2.M_PI);
    assertTrue(r.isFull());
  }

  public void testClampPoint() {
    S1Interval r = new S1Interval(-S2.M_PI, -S2.M_PI);
    assertEquals(S2.M_PI, r.clampPoint(-S2.M_PI));
    assertEquals(S2.M_PI, r.clampPoint(0));
    r = new S1Interval(0, S2.M_PI);
    assertEquals(0.1, r.clampPoint(0.1));
    assertEquals(0d, r.clampPoint(-S2.M_PI_2 + 1e-15));
    assertEquals(S2.M_PI, r.clampPoint(-S2.M_PI_2 - 1e-15));
    r = new S1Interval(S2.M_PI - 0.1, -S2.M_PI + 0.1);
    assertEquals(S2.M_PI, r.clampPoint(S2.M_PI));
    assertEquals(S2.M_PI - 0.1, r.clampPoint(1e-15));
    assertEquals(-S2.M_PI + 0.1, r.clampPoint(-1e-15));
    assertEquals(0d, S1Interval.full().clampPoint(0));
    assertEquals(S2.M_PI, S1Interval.full().clampPoint(S2.M_PI));
    assertEquals(S2.M_PI, S1Interval.full().clampPoint(-S2.M_PI));
  }

  public void testFromPointPair() {
    assertEquals(S1Interval.fromPointPair(-S2.M_PI, S2.M_PI), pi);
    assertEquals(S1Interval.fromPointPair(S2.M_PI, -S2.M_PI), pi);
    assertEquals(S1Interval.fromPointPair(mid34.hi(), mid34.lo()), mid34);
    assertEquals(S1Interval.fromPointPair(mid23.lo(), mid23.hi()), mid23);
  }

  public void testExpanded() {
    assertEquals(empty.expanded(1), empty);
    assertEquals(full.expanded(1), full);
    assertEquals(zero.expanded(1), new S1Interval(-1, 1));
    assertEquals(mipi.expanded(0.01), new S1Interval(S2.M_PI - 0.01, -S2.M_PI + 0.01));
    assertEquals(pi.expanded(27), full);
    assertEquals(pi.expanded(S2.M_PI_2), quad23);
    assertEquals(pi2.expanded(S2.M_PI_2), quad12);
    assertEquals(mipi2.expanded(S2.M_PI_2), quad34);

    assertEquals(empty.expanded(-1), empty);
    assertEquals(full.expanded(-1), full);
    assertEquals(quad123.expanded(-27), empty);
    assertEquals(quad234.expanded(-27), empty);
    assertEquals(quad123.expanded(-S2.M_PI_2), quad2);
    assertEquals(quad341.expanded(-S2.M_PI_2), quad4);
    assertEquals(quad412.expanded(-S2.M_PI_2), quad1);
  }

  public void testApproxEquals() {
    // Choose two values kLo and kHi such that it's okay to shift an endpoint by
    // kLo (i.e., the resulting interval is equivalent) but not by kHi.
    final double kLo = 4 * S2.DBL_EPSILON; // < maxError default
    final double kHi = 6 * S2.DBL_EPSILON; // > maxError default

    // Empty intervals.
    assertTrue(empty.approxEquals(empty));
    assertTrue(zero.approxEquals(empty) && empty.approxEquals(zero));
    assertTrue(pi.approxEquals(empty) && empty.approxEquals(pi));
    assertTrue(mipi.approxEquals(empty) && empty.approxEquals(mipi));
    assertFalse(empty.approxEquals(full));
    assertTrue(empty.approxEquals(new S1Interval(1, 1 + 2 * kLo)));
    assertFalse(empty.approxEquals(new S1Interval(1, 1 + 2 * kHi)));
    assertTrue(new S1Interval(S2.M_PI - kLo, -S2.M_PI + kLo).approxEquals(empty));

    // Full intervals.
    assertTrue(full.approxEquals(full));
    assertFalse(full.approxEquals(empty));
    assertFalse(full.approxEquals(zero));
    assertFalse(full.approxEquals(pi));
    assertTrue(full.approxEquals(new S1Interval(kLo, -kLo)));
    assertFalse(full.approxEquals(new S1Interval(2 * kHi, 0)));
    assertTrue(new S1Interval(-S2.M_PI + kLo, S2.M_PI - kLo).approxEquals(full));
    assertFalse(new S1Interval(-S2.M_PI, S2.M_PI - 2 * kHi).approxEquals(full));

    // Singleton intervals.
    assertTrue(pi.approxEquals(pi) && mipi.approxEquals(pi));
    assertTrue(pi.approxEquals(new S1Interval(S2.M_PI - kLo, S2.M_PI - kLo)));
    assertFalse(pi.approxEquals(new S1Interval(S2.M_PI - kHi, S2.M_PI - kHi)));
    assertTrue(pi.approxEquals(new S1Interval(S2.M_PI - kLo, -S2.M_PI + kLo)));
    assertFalse(pi.approxEquals(new S1Interval(S2.M_PI - kHi, -S2.M_PI)));
    assertFalse(zero.approxEquals(pi));
    assertTrue(pi.union(mid12).union(zero).approxEquals(quad12));
    assertTrue(quad2.intersection(quad3).approxEquals(pi));
    assertTrue(quad3.intersection(quad2).approxEquals(pi));

    // Intervals whose corresponding endpoints are nearly the same but where the
    // endpoints are in opposite order (i.e., inverted intervals).
    assertFalse(new S1Interval(0, kLo).approxEquals(new S1Interval(kLo, 0)));
    assertFalse(
        new S1Interval(S2.M_PI - 0.5 * kLo, -S2.M_PI + 0.5 * kLo)
            .approxEquals(new S1Interval(-S2.M_PI + 0.5 * kLo, S2.M_PI - 0.5 * kLo)));

    // Other intervals.
    assertTrue(new S1Interval(1 - kLo, 2 + kLo).approxEquals(new S1Interval(1, 2)));
    assertTrue(new S1Interval(1 + kLo, 2 - kLo).approxEquals(new S1Interval(1, 2)));
    assertTrue(new S1Interval(2 - kLo, 1 + kLo).approxEquals(new S1Interval(2, 1)));
    assertTrue(new S1Interval(2 + kLo, 1 - kLo).approxEquals(new S1Interval(2, 1)));
    assertFalse(new S1Interval(1 - kHi, 2 + kLo).approxEquals(new S1Interval(1, 2)));
    assertFalse(new S1Interval(1 + kHi, 2 - kLo).approxEquals(new S1Interval(1, 2)));
    assertFalse(new S1Interval(2 - kHi, 1 + kLo).approxEquals(new S1Interval(2, 1)));
    assertFalse(new S1Interval(2 + kHi, 1 - kLo).approxEquals(new S1Interval(2, 1)));
    assertFalse(new S1Interval(1 - kLo, 2 + kHi).approxEquals(new S1Interval(1, 2)));
    assertFalse(new S1Interval(1 + kLo, 2 - kHi).approxEquals(new S1Interval(1, 2)));
    assertFalse(new S1Interval(2 - kLo, 1 + kHi).approxEquals(new S1Interval(2, 1)));
    assertFalse(new S1Interval(2 + kLo, 1 - kHi).approxEquals(new S1Interval(2, 1)));
  }

  public void testGetDirectedHausdorffDistance() {
    assertEquals(0.0, empty.getDirectedHausdorffDistance(empty));
    assertEquals(0.0, empty.getDirectedHausdorffDistance(mid12));
    assertEquals(S2.M_PI, mid12.getDirectedHausdorffDistance(empty));

    assertEquals(0.0, quad12.getDirectedHausdorffDistance(quad123));
    S1Interval in = new S1Interval(3.0, -3.0); // an interval whose complement center is 0.
    assertEquals(3.0, new S1Interval(-0.1, 0.2).getDirectedHausdorffDistance(in));
    assertEquals(3.0 - 0.1, new S1Interval(0.1, 0.2).getDirectedHausdorffDistance(in));
    assertEquals(3.0 - 0.1, new S1Interval(-0.2, -0.1).getDirectedHausdorffDistance(in));
  }
}
