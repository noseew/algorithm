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

import static com.google.common.geometry.S2Projections.PROJ;

import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.Lists;
import com.google.common.geometry.S2Projections.FaceSiTi;

/** Verifies S2 static methods. */
@GwtCompatible
public strictfp class S2Test extends GeometryTestCase {
  // Test helper methods for testing the traversal order.
  private static int swapAxes(int ij) {
    return ((ij >> 1) & 1) + ((ij & 1) << 1);
  }

  private static int invertBits(int ij) {
    return ij ^ 3;
  }

  public void testTraversalOrder() {
    for (int r = 0; r < 4; ++r) {
      for (int i = 0; i < 4; ++i) {
        // Check consistency with respect to swapping axes.
        assertEquals(S2.ijToPos(r, i), S2.ijToPos(r ^ S2.SWAP_MASK, swapAxes(i)));
        assertEquals(S2.posToIJ(r, i), swapAxes(S2.posToIJ(r ^ S2.SWAP_MASK, i)));

        // Check consistency with respect to reversing axis directions.
        assertEquals(S2.ijToPos(r, i), S2.ijToPos(r ^ S2.INVERT_MASK, invertBits(i)));
        assertEquals(S2.posToIJ(r, i), invertBits(S2.posToIJ(r ^ S2.INVERT_MASK, i)));

        // Check that the two tables are inverses of each other.
        assertEquals(S2.ijToPos(r, S2.posToIJ(r, i)), i);
        assertEquals(S2.posToIJ(r, S2.ijToPos(r, i)), i);
      }
    }
  }

  public void testSTUV() {
    // Check boundary conditions.
    for (double x = 0; x <= 1; x++) {
      assertDoubleNear(PROJ.stToUV(x), 2 * x - 1);
      assertDoubleNear(PROJ.uvToST(2 * x - 1), x);
    }
    // Check that UVtoST and STtoUV are inverses.
    for (double x = 0; x <= 1; x += 0.0001) {
      assertDoubleNear(PROJ.uvToST(PROJ.stToUV(x)), x);
      assertDoubleNear(PROJ.stToUV(PROJ.uvToST(2 * x - 1)), 2 * x - 1);
    }
  }

  public void testFaceUVtoXYZ() {
    // Check that each face appears exactly once.
    S2Point sum = new S2Point();
    for (int face = 0; face < 6; ++face) {
      S2Point center = S2Projections.faceUvToXyz(face, 0, 0);
      assertEquals(S2Projections.getNorm(face), center);
      assertEquals(1.0, Math.abs(center.get(center.largestAbsComponent())));
      sum = S2Point.add(sum, S2Point.fabs(center));
    }
    assertEquals(sum, new S2Point(2, 2, 2));

    // Check that each face has a right-handed coordinate system.
    for (int face = 0; face < 6; ++face) {
      assertEquals(
          1.0,
          S2Point.crossProd(S2Projections.getUAxis(face), S2Projections.getVAxis(face))
              .dotProd(S2Projections.faceUvToXyz(face, 0, 0)));
    }

    // Check that the Hilbert curves on each face combine to form a
    // continuous curve over the entire cube.
    for (int face = 0; face < 6; ++face) {
      // The Hilbert curve on each face starts at (-1,-1) and terminates
      // at either (1,-1) (if axes not swapped) or (-1,1) (if swapped).
      int sign = ((face & S2.SWAP_MASK) != 0) ? -1 : 1;
      assertEquals(
          S2Projections.faceUvToXyz(face, sign, -sign),
          S2Projections.faceUvToXyz((face + 1) % 6, -1, -1));
    }
  }

  public void testFaceXyzToUvw() {
    for (int face = 0; face < 6; ++face) {
      assertEquals(new S2Point(0, 0, 0), S2Projections.faceXyzToUvw(face, new S2Point(0, 0, 0)));
      assertEquals(
          new S2Point(1, 0, 0), S2Projections.faceXyzToUvw(face, S2Projections.getUAxis(face)));
      assertEquals(
          new S2Point(-1, 0, 0),
          S2Projections.faceXyzToUvw(face, S2Point.neg(S2Projections.getUAxis(face))));
      assertEquals(
          new S2Point(0, 1, 0), S2Projections.faceXyzToUvw(face, S2Projections.getVAxis(face)));
      assertEquals(
          new S2Point(0, -1, 0),
          S2Projections.faceXyzToUvw(face, S2Point.neg(S2Projections.getVAxis(face))));
      assertEquals(
          new S2Point(0, 0, 1), S2Projections.faceXyzToUvw(face, S2Projections.getNorm(face)));
      assertEquals(
          new S2Point(0, 0, -1),
          S2Projections.faceXyzToUvw(face, S2Point.neg(S2Projections.getNorm(face))));
    }
  }

  public void testXYZToFaceSiTi() {
    // Check the conversion of random cells to center points and back.
    for (int level = 0; level <= S2CellId.MAX_LEVEL; level++) {
      for (int i = 0; i < 1000; ++i) {
        S2CellId id = getRandomCellId(level);
        S2Point p = id.toPoint();
        FaceSiTi result = PROJ.xyzToFaceSiTi(p);
        int resultLevel = PROJ.levelIfCenter(result, p);
        assertEquals(level, resultLevel);
        S2CellId actualId =
            S2CellId.fromFaceIJ(result.face, (int) (result.si / 2), (int) (result.ti / 2))
                .parent(level);
        assertEquals(id, actualId);

        // Now test a point near the cell center but not equal to it.
        S2Point pMoved = S2Point.add(id.toPoint(), new S2Point(1e-13, 1e-13, 1e-13));
        FaceSiTi moved = PROJ.xyzToFaceSiTi(pMoved);
        assertEquals(-1, PROJ.levelIfCenter(moved, pMoved));
        assertEquals(result.face, moved.face);
        assertEquals(result.si, moved.si);
        assertEquals(result.ti, moved.ti);

        // Finally, test some random (si,ti) values that may be at different
        // levels, or not at a valid level at all (for example, si == 0).
        int faceRandom = random(S2CellId.NUM_FACES);
        long siRandom;
        long tiRandom;
        long mask = 0xFFFFFFFFL << (S2CellId.MAX_LEVEL - level);
        do {
          siRandom = rand.nextInt() & mask;
          tiRandom = rand.nextInt() & mask;
        } while (siRandom > S2Projections.MAX_SITI || tiRandom > S2Projections.MAX_SITI);
        S2Point pRandom = PROJ.faceSiTiToXyz(faceRandom, siRandom, tiRandom);
        FaceSiTi actual = PROJ.xyzToFaceSiTi(pRandom);
        int actualLevel = PROJ.levelIfCenter(actual, pRandom);
        if (actual.face != faceRandom) {
          // The chosen point is on the edge of a top-level face cell.
          assertEquals(-1, actualLevel);
          assertTrue(
              actual.si == 0
                  || actual.si == S2Projections.MAX_SITI
                  || actual.ti == 0
                  || actual.ti == S2Projections.MAX_SITI);
        } else {
          assertEquals(siRandom, actual.si);
          assertEquals(tiRandom, actual.ti);
          if (actualLevel >= 0) {
            assertEquals(
                pRandom,
                S2CellId.fromFaceIJ(actual.face, (int) (actual.si / 2), (int) (actual.ti / 2))
                    .parent(actualLevel)
                    .toPoint());
          }
        }
      }
    }
  }

  public void testUVNorms() {
    // Check that GetUNorm and GetVNorm compute right-handed normals for
    // an edge in the increasing U or V direction.
    for (int face = 0; face < 6; ++face) {
      for (double x = -1; x <= 1; x += 1 / 1024.) {
        assertDoubleNear(
            S2Point.crossProd(
                    S2Projections.faceUvToXyz(face, x, -1), S2Projections.faceUvToXyz(face, x, 1))
                .angle(S2Projections.getUNorm(face, x)),
            0);
        assertDoubleNear(
            S2Point.crossProd(
                    S2Projections.faceUvToXyz(face, -1, x), S2Projections.faceUvToXyz(face, 1, x))
                .angle(S2Projections.getVNorm(face, x)),
            0);
      }
    }
  }

  public void testUVAxes() {
    // Check that axes are consistent with FaceUVtoXYZ.
    for (int face = 0; face < 6; ++face) {
      assertEquals(
          S2Projections.getUAxis(face),
          S2Point.sub(
              S2Projections.faceUvToXyz(face, 1, 0), S2Projections.faceUvToXyz(face, 0, 0)));
      assertEquals(
          S2Projections.getVAxis(face),
          S2Point.sub(
              S2Projections.faceUvToXyz(face, 0, 1), S2Projections.faceUvToXyz(face, 0, 0)));
    }
  }

  public void testUVWAxis() {
    for (int face = 0; face < 6; ++face) {
      // Check that axes are consistent with faceUvToXyz.
      assertEquals(
          S2Projections.getUAxis(face),
          S2Point.sub(
              S2Projections.faceUvToXyz(face, 1, 0), S2Projections.faceUvToXyz(face, 0, 0)));
      assertEquals(
          S2Projections.getVAxis(face),
          S2Point.sub(
              S2Projections.faceUvToXyz(face, 0, 1), S2Projections.faceUvToXyz(face, 0, 0)));
      assertEquals(S2Projections.getNorm(face), S2Projections.faceUvToXyz(face, 0, 0));

      // Check that every face coordinate frame is right-handed.
      assertEquals(
          1D,
          S2Point.scalarTripleProduct(
              S2Projections.getUAxis(face),
              S2Projections.getVAxis(face),
              S2Projections.getNorm(face)));

      // Check that GetUVWAxis is consistent with getUAxis, getVAxis, getNorm.
      assertEquals(S2Projections.getUAxis(face), S2Projections.getUVWAxis(face, 0));
      assertEquals(S2Projections.getVAxis(face), S2Projections.getUVWAxis(face, 1));
      assertEquals(S2Projections.getNorm(face), S2Projections.getUVWAxis(face, 2));
    }
  }

  public void testUVWFace() {
    // Check that GetUVWFace is consistent with GetUVWAxis.
    for (int face = 0; face < 6; ++face) {
      for (int axis = 0; axis < 3; ++axis) {
        assertEquals(
            S2Projections.getUVWFace(face, axis, 0),
            S2Projections.xyzToFace(S2Point.neg(S2Projections.getUVWAxis(face, axis))));
        assertEquals(
            S2Projections.getUVWFace(face, axis, 1),
            S2Projections.xyzToFace(S2Projections.getUVWAxis(face, axis)));
      }
    }
  }

  public void testAngleArea() {
    S2Point pz = new S2Point(0, 0, 1);
    S2Point p000 = new S2Point(1, 0, 0);
    S2Point p045 = new S2Point(1, 1, 0);
    S2Point p090 = new S2Point(0, 1, 0);
    S2Point p180 = new S2Point(-1, 0, 0);

    assertDoubleNear(S2.angle(p000, pz, p045), S2.M_PI_4);
    assertEquals(S2.turnAngle(p000, pz, p045), -3 * S2.M_PI_4, 0);

    assertDoubleNear(S2.angle(p045, pz, p180), 3 * S2.M_PI_4);
    assertDoubleNear(S2.angle(p000, pz, p180), S2.M_PI);

    assertDoubleNear(S2.angle(pz, p000, pz), 0);
    assertDoubleNear(S2.angle(pz, p000, p045), S2.M_PI_2);

    assertDoubleNear(S2.area(p000, p090, pz), S2.M_PI_2);
    assertDoubleNear(S2.area(p045, pz, p180), 3 * S2.M_PI_4);

    // Make sure that area() has good *relative* accuracy even for
    // very small areas.
    final double eps = 1e-10;
    S2Point pepsx = new S2Point(eps, 0, 1);
    S2Point pepsy = new S2Point(0, eps, 1);
    double expected1 = 0.5 * eps * eps;
    assertDoubleNear(S2.area(pepsx, pepsy, pz), expected1, 1e-14 * expected1);

    // Make sure that it can handle degenerate triangles.
    S2Point pr = S2Point.normalize(new S2Point(0.257, -0.5723, 0.112));
    S2Point pq = S2Point.normalize(new S2Point(-0.747, 0.401, 0.2235));
    assertEquals(0.0, S2.area(pr, pr, pr), 0.0);
    assertDoubleNear(S2.area(pr, pq, pr), 0);
    assertEquals(0.0, S2.area(p000, p045, p090), 0.0);

    double maxGirard = 0;
    for (int i = 0; i < 10000; ++i) {
      S2Point p0 = randomPoint();
      S2Point d1 = randomPoint();
      S2Point d2 = randomPoint();
      S2Point p1 = S2Point.add(p0, S2Point.mul(d1, 1e-15));
      S2Point p2 = S2Point.add(p0, S2Point.mul(d2, 1e-15));
      // The actual displacement can be as much as 1.2e-15 due to roundoff.
      // This yields a maximum triangle area of about 0.7e-30.
      assertTrue(S2.area(p0, p1, p2) < 0.7e-30);
      maxGirard = Math.max(maxGirard, S2.girardArea(p0, p1, p2));
    }
    // This check only passes if girardArea() uses robustCrossProd().
    assertTrue("" + maxGirard, maxGirard <= 1e-14);

    // Try a very long and skinny triangle.
    S2Point p045eps = new S2Point(1, 1, eps);
    double expected2 = 5.8578643762690495119753e-11; // Mathematica.
    assertDoubleNear(S2.area(p000, p045eps, p090), expected2, 1e-9 * expected2);

    // Triangles with near-180 degree edges that sum to a quarter-sphere.
    final double eps2 = 1e-14;
    S2Point p000eps2 = new S2Point(1, 0.1 * eps2, eps2);
    double quarterArea1 =
        S2.area(p000eps2, p000, p045)
            + S2.area(p000eps2, p045, p180)
            + S2.area(p000eps2, p180, pz)
            + S2.area(p000eps2, pz, p000);
    assertDoubleNear(quarterArea1, S2.M_PI);

    // Four other triangles that sum to a quarter-sphere.
    S2Point p045eps2 = new S2Point(1, 1, eps2);
    double quarterArea2 =
        S2.area(p045eps2, p000, p045)
            + S2.area(p045eps2, p045, p180)
            + S2.area(p045eps2, p180, pz)
            + S2.area(p045eps2, pz, p000);
    assertDoubleNear(quarterArea2, S2.M_PI);

    // Compute the area of a hemisphere using four triangles with one near-180
    // degree edge and one near-degenerate edge.
    for (int i = 0; i < 100; ++i) {
      double lng = 2 * S2.M_PI * rand.nextDouble();
      S2Point p0 = S2LatLng.fromRadians(1e-20, lng).normalized().toPoint();
      S2Point p1 = S2LatLng.fromRadians(0, lng).normalized().toPoint();
      double p2lng = lng + rand.nextDouble();
      S2Point p2 = S2LatLng.fromRadians(0, p2lng).normalized().toPoint();
      S2Point p3 = S2LatLng.fromRadians(0, lng + S2.M_PI).normalized().toPoint();
      S2Point p4 = S2LatLng.fromRadians(0, lng + 5.0).normalized().toPoint();
      double area =
          (S2.area(p0, p1, p2) + S2.area(p0, p2, p3) + S2.area(p0, p3, p4) + S2.area(p0, p4, p1));
      assertEquals(area, 2 * S2.M_PI, 2e-15);
    }
  }

  public void testCCW() {
    S2Point a = new S2Point(0.72571927877036835, 0.46058825605889098, 0.51106749730504852);
    S2Point b = new S2Point(0.7257192746638208, 0.46058826573818168, 0.51106749441312738);
    S2Point c = new S2Point(0.72571927671709457, 0.46058826089853633, 0.51106749585908795);
    assertTrue(S2Predicates.sign(a, b, c) != 0);
  }

  public void testFrames() {
    S2Point z = S2Point.normalize(new S2Point(0.2, 0.5, -3.3));
    Matrix3x3 m = S2.getFrame(z);
    assertTrue(S2.approxEquals(m.getCol(2), z));
    assertTrue(S2.isUnitLength(m.getCol(0)));
    assertTrue(S2.isUnitLength(m.getCol(1)));
    assertEquals(det(m), 1, 1e-15);

    assertTrue(S2.approxEquals(S2.toFrame(m, m.getCol(0)), S2Point.X_POS));
    assertTrue(S2.approxEquals(S2.toFrame(m, m.getCol(1)), S2Point.Y_POS));
    assertTrue(S2.approxEquals(S2.toFrame(m, m.getCol(2)), S2Point.Z_POS));

    assertTrue(S2.approxEquals(S2.fromFrame(m, S2Point.X_POS), m.getCol(0)));
    assertTrue(S2.approxEquals(S2.fromFrame(m, S2Point.Y_POS), m.getCol(1)));
    assertTrue(S2.approxEquals(S2.fromFrame(m, S2Point.Z_POS), m.getCol(2)));
  }

  /**
   * Returns the determinant of a 3x3 matrix. This is not located on Matrix3x3 since, despite the
   * name, that class is more general than 3x3 matrices.
   */
  private static double det(Matrix3x3 m) {
    assert m.cols() == 3 && m.rows() == 3;
    return m.get(0, 0) * (m.get(1, 1) * m.get(2, 2) - m.get(1, 2) * m.get(2, 1))
        - m.get(0, 1) * (m.get(1, 0) * m.get(2, 2) - m.get(1, 2) * m.get(2, 0))
        + m.get(0, 2) * (m.get(1, 0) * m.get(2, 1) - m.get(1, 1) * m.get(2, 0));
  }

  // Note: obviously, I could have defined a bundle of metrics like this in the
  // S2 class itself rather than just for testing. However, it's not clear that
  // this is useful other than for testing purposes, and I find
  // S2.kMinWidth.GetMaxLevel(width) to be slightly more readable than
  // than S2.kWidth.min().GetMaxLevel(width). Also, there is no fundamental
  // reason that we need to analyze the minimum, maximum, and average values of
  // every metric; it would be perfectly reasonable to just define one of these.

  class MetricBundle {
    public MetricBundle(S2.Metric min, S2.Metric max, S2.Metric avg) {
      this.min_ = min;
      this.max_ = max;
      this.avg_ = avg;
    }

    S2.Metric min_;
    S2.Metric max_;
    S2.Metric avg_;
  }

  public void testMinMaxAvg(MetricBundle bundle) {
    assertTrue(bundle.min_.deriv() < bundle.avg_.deriv());
    assertTrue(bundle.avg_.deriv() < bundle.max_.deriv());
  }

  public void testLessOrEqual(MetricBundle a, MetricBundle b) {
    assertTrue(a.min_.deriv() <= b.min_.deriv());
    assertTrue(a.max_.deriv() <= b.max_.deriv());
    assertTrue(a.avg_.deriv() <= b.avg_.deriv());
  }

  public void testMetrics() {
    MetricBundle angleSpan =
        new MetricBundle(PROJ.minAngleSpan, PROJ.maxAngleSpan, PROJ.avgAngleSpan);
    MetricBundle width = new MetricBundle(PROJ.minWidth, PROJ.maxWidth, PROJ.avgWidth);
    MetricBundle edge = new MetricBundle(PROJ.minEdge, PROJ.maxEdge, PROJ.avgEdge);
    MetricBundle diag = new MetricBundle(PROJ.minDiag, PROJ.maxDiag, PROJ.avgDiag);
    MetricBundle area = new MetricBundle(PROJ.minArea, PROJ.maxArea, PROJ.avgArea);

    // First, check that min <= avg <= max for each metric.
    testMinMaxAvg(angleSpan);
    testMinMaxAvg(width);
    testMinMaxAvg(edge);
    testMinMaxAvg(diag);
    testMinMaxAvg(area);

    // Check that the maximum aspect ratio of an individual cell is consistent
    // with the global minimums and maximums.
    assertTrue(PROJ.maxEdgeAspect >= 1.0);
    assertTrue(PROJ.maxEdgeAspect <= PROJ.maxEdge.deriv() / PROJ.minEdge.deriv());
    assertTrue(PROJ.maxDiagAspect >= 1);
    assertTrue(PROJ.maxDiagAspect <= PROJ.maxDiag.deriv() / PROJ.minDiag.deriv());

    // Check various conditions that are provable mathematically.
    testLessOrEqual(width, angleSpan);
    testLessOrEqual(width, edge);
    testLessOrEqual(edge, diag);

    assertTrue(PROJ.minArea.deriv() >= PROJ.minWidth.deriv() * PROJ.minEdge.deriv() - 1e-15);
    assertTrue(PROJ.maxArea.deriv() <= PROJ.maxWidth.deriv() * PROJ.maxEdge.deriv() + 1e-15);

    // GetMinLevelForLength() and friends have built-in assertions, we just need to call these
    // functions to test them.
    //
    // We don't actually check that the metrics are correct here, e.g. that getMinWidth(10) is a
    // lower bound on the width of cells at level 10. It is easier to check these properties in
    // S2CellTest, since S2Cell has methods to compute the cell vertices, etc.

    for (int level = -2; level <= S2CellId.MAX_LEVEL + 3; ++level) {
      double dWidth = Math.scalb(PROJ.minWidth.deriv(), -level);
      // Check lengths.
      if (level >= S2CellId.MAX_LEVEL + 3) {
        dWidth = 0;
      }

      // Check boundary cases (exactly equal to a threshold value).
      int expectedLevel = Math.max(0, Math.min(S2CellId.MAX_LEVEL, level));
      assertEquals(expectedLevel, PROJ.minWidth.getMinLevel(dWidth));
      assertEquals(expectedLevel, PROJ.minWidth.getMaxLevel(dWidth));
      assertEquals(expectedLevel, PROJ.minWidth.getClosestLevel(dWidth));

      // Also check non-boundary cases.
      assertEquals(expectedLevel, PROJ.minWidth.getMinLevel(1.2 * dWidth));
      assertEquals(expectedLevel, PROJ.minWidth.getMaxLevel(0.8 * dWidth));
      assertEquals(expectedLevel, PROJ.minWidth.getClosestLevel(1.2 * dWidth));
      assertEquals(expectedLevel, PROJ.minWidth.getClosestLevel(0.8 * dWidth));

      // Check areas.
      double dArea = PROJ.minArea.deriv() * Math.pow(4, -level);
      if (level <= -3) {
        dArea = 0;
      }
      assertEquals(expectedLevel, PROJ.minArea.getMinLevel(dArea));
      assertEquals(expectedLevel, PROJ.minArea.getMaxLevel(dArea));
      assertEquals(expectedLevel, PROJ.minArea.getClosestLevel(dArea));
      assertEquals(expectedLevel, PROJ.minArea.getMinLevel(1.2 * dArea));
      assertEquals(expectedLevel, PROJ.minArea.getMaxLevel(0.8 * dArea));
      assertEquals(expectedLevel, PROJ.minArea.getClosestLevel(1.2 * dArea));
      assertEquals(expectedLevel, PROJ.minArea.getClosestLevel(0.8 * dArea));
    }
  }

  public void testTrueCentroidForSmallTriangle() {
    S2Polygon smallPoly =
        new S2Polygon(
            Lists.newArrayList(
                new S2Loop(
                    Lists.newArrayList(
                        S2LatLng.fromE7(0x2094588f, 0xfc9edbe6).toPoint(),
                        S2LatLng.fromE7(0x209456c4, 0xfc9ee491).toPoint(),
                        S2LatLng.fromE7(0x20945e7f, 0xfc9ee954).toPoint()))));
    // We shouldn't have to normalize, but the centroid is scaled by the area, and this triangle is
    // SO small that the magnitude is insufficient to get good precision in the contains() test, so
    // we actually should normalize in this case.
    assertTrue(smallPoly.contains(S2Point.normalize(smallPoly.getCentroid())));
  }

  public void testTrueCentroidSemiEquator() {
    // Test the centroid of polyline ABC that follows the equator and consists of two 90 degree
    // edges (i.e., C = -A). The centroid (multiplied by length) should point toward B and have a
    // norm of 2.0. (The centroid itself has a norm of 2/Pi, and the total edge length is Pi).
    S2Point a = new S2Point(0, -1, 0);
    S2Point b = new S2Point(1, 0, 0);
    S2Point c = new S2Point(0, 1, 0);
    S2Point centroid = S2.trueCentroid(a, b).add(S2.trueCentroid(b, c));
    assertTrue(S2.approxEquals(b, centroid.normalize()));
    assertDoubleNear(2.0, centroid.norm());
  }

  public void testGreateCircles() {
    // Construct random great circles and divide them randomly into segments. Then make sure that
    // the centroid is approximately at the center of the sphere. Note that because of the way the
    // centroid is computed, it does not matter how we split the great circle into segments.
    //
    // Note that this is a direct test of the properties that the centroid should have, rather than
    // a test that it matches a particular formula.
    for (int i = 0; i < 100; i++) {
      Matrix3x3 frame = getRandomFrame();
      S2Point x = frame.getCol(0);
      S2Point y = frame.getCol(1);
      S2Point.Builder builder = new S2Point.Builder();

      S2Point v0 = x;
      for (double theta = 0; theta < 2 * Math.PI; theta += Math.pow(rand.nextDouble(), 10)) {
        S2Point v1 = x.mul(Math.cos(theta)).add(y.mul(Math.sin(theta)));
        builder.add(S2.trueCentroid(v0, v1));
        v0 = v1;
      }
      // Close the circle.
      builder.add(S2.trueCentroid(v0, x));
      assertTrue(builder.build().norm() < 2e-14);
    }
  }
}
