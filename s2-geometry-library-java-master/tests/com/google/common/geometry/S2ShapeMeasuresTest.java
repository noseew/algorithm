/*
 * Copyright 2019 Google Inc.
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
import com.google.common.annotations.GwtIncompatible;
import com.google.common.collect.Lists;
import com.google.common.geometry.S2ShapeMeasures.LoopOrder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@GwtCompatible
public class S2ShapeMeasuresTest extends GeometryTestCase {

  private static final double MAX_DISTANCE = 1e-6;
  // The full loop is represented as a loop with no vertices.
  private static final S2Loop FULL = makeLoop("full");
  // A degenerate loop in the shape of a "V".
  private static final S2Loop V_LOOP = makeLoop("5:1, 0:2, 5:3, 0:2");
  // The northern hemisphere, defined using two pairs of antipodal points.
  private static final S2Loop NORTH_HEMI = makeLoop("0:-180, 0:-90, 0:0, 0:90");
  // The northern hemisphere, defined using three points 120 degrees apart.
  private static final S2Loop NORTH_HEMI_3 = makeLoop("0:-180, 0:-60, 0:60");
  // The western hemisphere, defined using two pairs of antipodal points.
  private static final S2Loop WEST_HEMI = makeLoop("0:-180, -90:0, 0:0, 90:0");
  // The eastern hemisphere, defined using two pairs of antipodal points.
  private static final S2Loop EAST_HEMI = makeLoop("90:0, 0:0, -90:0, 0:-180");
  // A spiral stripe that slightly over-wraps the equator.
  private static final S2Loop CANDY_CANE =
      makeLoop("-20:150, -20:-70, 0:70, 10:-150, 10:70, -10:-70");
  // A completely degenerate triangle along the equator that sign() considers to be CCW.
  private static final S2Loop LINE_TRIANGLE = makeLoop("0:1, 0:2, 0:3");
  // A nearly-degenerate CCW chevron near the equator with very long sides (about 80 degrees). Its
  // area is less than 1e-640, which is too small to represent in double precision.
  private static final S2Loop SKINNY_CHEVRON = makeLoop("0:0, -1e-320:80, 0:1e-320, 1e-320:80");
  // A loop where the same vertex appears three times.
  private static final S2Loop THREE_LEAF_CLOVER =
      makeLoop("0:0, -3:3, 3:3, 0:0, 3:0, 3:-3, 0:0, -3:-3, -3:0");
  // A loop with groups of 3 or more vertices in a straight line.
  private static final S2Loop TESSELLATED_LOOP =
      makeLoop("10:34, 5:34, 0:34, -10:34, -10:36, -5:36, 0:36, 10:36");

  public void testLength() {
    assertEquals(S1Angle.ZERO, S2ShapeMeasures.length(makeIndex("0:0 # #").getShapes().get(0)));
    assertEquals(S1Angle.ZERO, S2ShapeMeasures.length(makePolygon("0:0, 0:1, 1:0").shape()));
  }

  public void testLengthNoPolylines() {
    assertEquals(S1Angle.ZERO, S2ShapeMeasures.length(makePolyline("")));
  }

  @GwtIncompatible("S2LaxPolylineShape")
  public void testLengthThreePolylinesInOneShape() {
    List<List<S2Point>> pointss = new ArrayList<>();
    for (String text : new String[] {"1:0", "2:0", "3:0"}) {
      pointss.add(Lists.newArrayList(makePoint("0:0"), makePoint(text)));
    }
    S2LaxPolylineShape shape = S2LaxPolylineShape.createMulti(pointss);
    assertEquals(S1Angle.degrees(6), S2ShapeMeasures.length(shape));
  }

  public void testPerimeterWrongDimension() {
    assertEquals(S1Angle.ZERO, S2ShapeMeasures.perimeter(makeIndex("0:0 # #").getShapes().get(0)));
    assertEquals(S1Angle.ZERO, S2ShapeMeasures.perimeter(makePolyline("0:0, 0:1, 1:0")));
  }

  public void testPerimeterEmptyPolygon() {
    assertEquals(S1Angle.ZERO, S2ShapeMeasures.perimeter(makePolygon("empty").shape()));
  }

  public void testPerimeterFullPolygon() {
    assertEquals(S1Angle.ZERO, S2ShapeMeasures.perimeter(makePolygon("full").shape()));
  }

  public void testPerimeterTwoLoopPolygon() {
    // To ensure that all edges are 1 degree long, we use degenerate loops.
    assertEquals(
        S1Angle.degrees(6),
        S2ShapeMeasures.perimeter(makePolygon("0:0, 1:0; 0:1, 0:2, 0:3").shape()));
  }

  public void testLoopPerimeterOctant() {
    assertEquals(
        S1Angle.radians(3 * S2.M_PI_2),
        S2ShapeMeasures.loopPerimeter(makePolygon("0:0, 0:90, 90:0").shape(), 0));
  }

  public void testLoopPerimeterMoreThanTwoPi() {
    assertEquals(
        S1Angle.radians(5 * S2.M_PI_2),
        S2ShapeMeasures.loopPerimeter(makePolygon("0:0, 0:90, 0:180, 90:0, 0:-90").shape(), 0));
  }

  public void testAreaWrongDimension() {
    assertEquals(0.0, S2ShapeMeasures.area(makeIndex("0:0 # #").getShapes().get(0)));
    assertEquals(0.0, S2ShapeMeasures.area(makePolyline("0:0, 0:1")));
  }

  public void testAreaEmptyPolygon() {
    assertEquals(0.0, S2ShapeMeasures.area(makePolygon("empty").shape()));
  }

  public void testAreaFullPolygon() {
    assertEquals(4 * S2.M_PI, S2ShapeMeasures.area(makePolygon("full").shape()));
  }

  public void testAreaTwoTinyShellsPolygon() {
    // Two small squares with sides about 10 um (micrometers) long.
    double side = S1Angle.degrees(1e-10).radians();
    S2Shape shape =
        makePolygon("0:0, 0:1e-10, 1e-10:1e-10, 1e-10:0; 0:0, 0:-1e-10, -1e-10:-1e-10, -1e-10:0")
            .shape();
    assertDoubleNear(2 * side * side, S2ShapeMeasures.area(shape), 1e-12);
  }

  public void testAreaShellAndHolePolygon() {
    S2Shape shape = makePolygon("0:0, 1:0, 0:1; 0.3:0.3, 0.3:0.6, 0.6:0.3").shape();
    assertDoubleNear(1.386e-4, S2ShapeMeasures.area(shape), 1e-7);
  }

  public void testCanonicalLoopOrderAllDegeneracies() {
    checkCanonicalLoopOrder("", new LoopOrder(0, 1));
    checkCanonicalLoopOrder("a", new LoopOrder(0, 1));
    checkCanonicalLoopOrder("aaaaa", new LoopOrder(0, 1));
    checkCanonicalLoopOrder("ba", new LoopOrder(1, 1));
    checkCanonicalLoopOrder("bab", new LoopOrder(1, 1));
    checkCanonicalLoopOrder("cbab", new LoopOrder(2, 1));
    checkCanonicalLoopOrder("bacbcab", new LoopOrder(8, -1));
  }

  public void testLoopAreaConsistentWithTurningAngle() {
    checkLoopAreaConsistentWithTurningAngle(FULL);
    checkLoopAreaConsistentWithTurningAngle(NORTH_HEMI);
    checkLoopAreaConsistentWithTurningAngle(NORTH_HEMI_3);
    checkLoopAreaConsistentWithTurningAngle(WEST_HEMI);
    checkLoopAreaConsistentWithTurningAngle(EAST_HEMI);
    checkLoopAreaConsistentWithTurningAngle(CANDY_CANE);
    checkLoopAreaConsistentWithTurningAngle(LINE_TRIANGLE);
    checkLoopAreaConsistentWithTurningAngle(SKINNY_CHEVRON);
    checkLoopAreaConsistentWithTurningAngle(THREE_LEAF_CLOVER);
    checkLoopAreaConsistentWithTurningAngle(TESSELLATED_LOOP);
  }

  public void testLoopAreaConsistentWithOrientation() {
    // Test that loopArea() returns an area near 0 for degenerate loops that contain almost no
    // points, and an area near 4*Pi for degenerate loops that contain almost all points.
    int maxVertices = 6;
    for (int i = 0; i < 50; i++) {
      int numVertices = 3 + random(maxVertices - 3 + 1);
      // Repeatedly choose N vertices that are exactly on the equator until we find some that form a
      // valid loop.
      List<S2Point> vertices = new ArrayList<>();
      do {
        vertices.clear();
        for (int j = 0; j < numVertices; j++) {
          // We limit longitude to the range [0, 90] to ensure that the loop is degenerate (as
          // opposed to following the entire equator).
          vertices.add(S2LatLng.fromRadians(0, rand.nextDouble() * S2.M_PI_2).toPoint());
        }
      } while (!new S2Loop(vertices).isValid());
      S2Loop loop = new S2Loop(vertices);
      boolean ccw = loop.isNormalized();
      // The error bound is sufficient for current tests but not guaranteed.
      assertEquals(ccw ? 0 : 4 * S2.M_PI, S2ShapeMeasures.loopArea(vertices), 1e-14);
      assertEquals(!ccw, loop.contains(S2Point.Z_POS));
    }
  }

  public void testLoopAreaAndLoopCentroid() {
    assertEquals(4 * S2.M_PI, S2ShapeMeasures.loopArea(FULL, 0));
    assertEquals(new S2Point(0, 0, 0), S2ShapeMeasures.loopCentroid(FULL, 0));

    assertEquals(2 * S2.M_PI, S2ShapeMeasures.loopArea(NORTH_HEMI, 0), 1e-14);
    assertEquals(2 * S2.M_PI, S2ShapeMeasures.loopArea(EAST_HEMI, 0), 1e-12);

    // Construct spherical caps of random height, and approximate their boundary with closely spaced
    // vertices. Then check that the area and centroid are correct.
    for (int iter = 0; iter < 50; iter++) {
      // Choose a coordinate frame for the spherical cap.
      S2Point z = randomPoint();
      S2Point x = z.crossProd(randomPoint()).normalize();
      S2Point y = z.crossProd(x).normalize();

      // Given two points at latitude phi and whose longitudes differ by dtheta, the geodesic
      // between the two points has a maximum latitude of atan(tan(phi) / cos(dtheta/2)). This can
      // be derived by positioning the two points at (-dtheta/2, phi) and (dtheta/2, phi).
      //
      // We want to position the vertices close enough together so that their maximum distance from
      // the boundary of the spherical cap is maxDist. Thus we want
      // abs(atan(tan(phi) / cos(dtheta/2)) - phi) <= maxDist.
      double maxDist = 1e-6;
      double height = 2 * rand.nextDouble();
      double phi = Math.asin(1 - height);
      double maxDtheta = 2 * Math.acos(Math.tan(Math.abs(phi)) / Math.tan(Math.abs(phi) + maxDist));
      maxDtheta = Math.min(S2.M_PI, maxDtheta); // At least 3 vertices.

      List<S2Point> vertices = new ArrayList<>();
      for (double theta = 0; theta < 2 * S2.M_PI; theta += rand.nextDouble() * maxDtheta) {
        vertices.add(
            x.mul(Math.cos(theta) * Math.cos(phi))
                .add(y.mul(Math.sin(theta) * Math.cos(phi)).add(z.mul(Math.sin(phi)))));
      }
      S2Loop loop = new S2Loop(vertices);
      double area = S2ShapeMeasures.loopArea(loop, 0);
      S2Point centroid = S2ShapeMeasures.loopCentroid(loop, 0);
      double expectedArea = 2 * S2.M_PI * height;
      assertTrue(Math.abs(area - expectedArea) <= 2 * S2.M_PI * maxDist);
      S2Point expectedCentroid = z.mul(expectedArea * (1 - 0.5 * height));
      assertTrue(centroid.sub(expectedCentroid).norm() <= 2 * maxDist);
    }
  }

  public void testTurningAngle() {
    assertEquals(-2 * S2.M_PI, S2ShapeMeasures.turningAngle(FULL, 0));
    assertEquals(2 * S2.M_PI, S2ShapeMeasures.turningAngle(V_LOOP, 0));

    // This curvature should be computed exactly.
    assertEquals(0.0, S2ShapeMeasures.turningAngle(NORTH_HEMI_3, 0));
    checkTurningAngleInvariants(NORTH_HEMI_3);

    assertEquals(0.0, S2ShapeMeasures.turningAngle(WEST_HEMI, 0), 1e-15);
    checkTurningAngleInvariants(WEST_HEMI);

    // We don't have an easy way to estimate the curvature of these loops, but we can still check
    // that the expected invariants hold.
    checkTurningAngleInvariants(CANDY_CANE);
    checkTurningAngleInvariants(THREE_LEAF_CLOVER);

    assertEquals(2 * S2.M_PI, S2ShapeMeasures.turningAngle(LINE_TRIANGLE, 0), 1e-15);
    checkTurningAngleInvariants(LINE_TRIANGLE);

    assertEquals(2 * S2.M_PI, S2ShapeMeasures.turningAngle(SKINNY_CHEVRON, 0), 1e-15);
    checkTurningAngleInvariants(SKINNY_CHEVRON);

    // Build a narrow spiral loop starting at the north pole.This is designed to test that the error
    // in turningAngle() is linear in the number of vertices even when the partial sum of the
    // curvatures gets very large. The spiral consists of two "arms" defining opposite sides of the
    // loop. This is a pathological loop that contains many long parallel edges.
    int numArmPoints = 10000; // Number of vertices in each "arm"
    double armRadius = 0.01; // Radius of spiral.
    S2Point[] spiral = new S2Point[2 * numArmPoints];
    spiral[numArmPoints] = new S2Point(0, 0, 1);
    for (int i = 0; i < numArmPoints; i++) {
      double angle = (2 * S2.M_PI / 3) * i;
      double x = Math.cos(angle);
      double y = Math.sin(angle);
      double r1 = i * armRadius / numArmPoints;
      double r2 = (i + 1.5) * armRadius / numArmPoints;
      spiral[numArmPoints - i - 1] = new S2Point(r1 * x, r1 * y, 1).normalize();
      spiral[numArmPoints + i] = new S2Point(r2 * x, r2 * y, 1).normalize();
    }

    // Check that GetCurvature() is consistent with GetArea() to within the
    // error bound of the former.  We actually use a tiny fraction of the
    // worst-case error bound, since the worst case only happens when all the
    // roundoff errors happen in the same direction and this test is not
    // designed to achieve that.  The error in GetArea() can be ignored for the
    // purposes of this test since it is generally much smaller.
    List<S2Point> loop = Lists.newArrayList(spiral);
    assertEquals(
        2 * S2.M_PI - S2ShapeMeasures.loopArea(loop),
        S2ShapeMeasures.turningAngle(loop),
        0.01 * S2.getTurningAngleMaxError(loop.size()));
  }

  public void testPruneDegeraciesAllDegeracies() {
    checkPruneDegeracies("", "");
    checkPruneDegeracies("a", "");
    checkPruneDegeracies("aaaaa", "");
    checkPruneDegeracies("ab", "");
    checkPruneDegeracies("abb", "");
    checkPruneDegeracies("aab", "");
    checkPruneDegeracies("aba", "");
    checkPruneDegeracies("abba", "");
    checkPruneDegeracies("abcb", "");
    checkPruneDegeracies("abcba", "");
    checkPruneDegeracies("abcdcdedefedcbcdcb", "");
  }

  public void testPruneDegeraciesSomeDegeracies() {
    checkPruneDegeracies("abc", "abc");
    checkPruneDegeracies("abca", "abc");
    checkPruneDegeracies("abcc", "abc");
    checkPruneDegeracies("abccaa", "abc");
    checkPruneDegeracies("aabbcc", "abc");
    checkPruneDegeracies("abcdedca", "abc");
    checkPruneDegeracies("abcbabcbcdc", "abc");
    checkPruneDegeracies("xyzabcazy", "abc");
    checkPruneDegeracies("xxyyzzaabbccaazzyyxx", "abc");
  }

  public void testCentroidPoints() {
    S2Point actual = S2ShapeMeasures.centroid(makeIndex("0:0 | 0:90 # #").getShapes().get(0));
    assertEquals(new S2Point(1, 1, 0), actual);
  }

  public void testCentroidPolyline() {
    S2Point actual = S2ShapeMeasures.centroid(makePolyline("0:0, 0:90"));
    assertTrue(S2.approxEquals(new S2Point(1, 1, 0), actual));
  }

  public void testCentroidPolygon() {
    S2Point actual = S2ShapeMeasures.centroid(makePolygon("0:0, 0:90, 90:0").shape());
    assertTrue(S2.approxEquals(new S2Point(Math.PI / 4, Math.PI / 4, Math.PI / 4), actual));
  }

  @GwtIncompatible("S2LaxPolylineShape")
  public void testPolylineLengthAndCentroid() {
    // Construct random great circles and divide them randomly into segments. Then make sure that
    // the length and centroid are correct. Note that because of the way the centroid is computed,
    // it does not matter how we split the great circle into segments.
    for (int i = 0; i < 100; i++) {
      // Choose a coordinate frame for the great circle.
      Matrix3x3 frame = getRandomFrame();
      S2Point x = frame.getCol(0);
      S2Point y = frame.getCol(1);

      List<S2Point> polyline = new ArrayList<>();
      for (double theta = 0; theta < 2 * Math.PI; theta += Math.pow(rand.nextDouble(), 10)) {
        polyline.add(x.mul(Math.cos(theta)).add(y.mul(Math.sin(theta))));
      }
      // Close the circle.
      polyline.add(polyline.get(0));

      S2Shape shape = S2LaxPolylineShape.create(polyline);
      S1Angle length = S2ShapeMeasures.polylineLength(shape, 0);
      assertTrue(Math.abs(length.radians() - 2 * Math.PI) < 2e-14);
      S2Point centroid = S2ShapeMeasures.polylineCentroid(shape, 0);
      assertTrue(centroid.norm() < 2e-14);
    }
  }

  @GwtIncompatible("S2LaxPolygonShape")
  public void testLoopCentroid() {
    assertEquals(S2Point.ORIGIN, S2ShapeMeasures.loopCentroid(S2LaxPolygonShape.FULL, 0));

    // Construct spherical caps of random height, and approximate their boundary with closely spaced
    // vertices. Then check that the centroid is correct.
    for (int i = 0; i < 50; i++) {
      // Choose a coordinate frame for the spherical cap.
      Matrix3x3 frame = getRandomFrame();
      S2Point x = frame.getCol(0);
      S2Point y = frame.getCol(1);
      S2Point z = frame.getCol(2);

      // Given two points at latitude phi and whose longitudes differ by dtheta, the geodesic
      // between the two points has a maximum latitude of atan(tan(phi) / cos(dtheta/2)). This can
      // be derived by positioning the two points at (-dtheta/2, phi) and (dtheta/2, phi).
      //
      // We want to position the vertices close enough together so that their maximum distance from
      // the boundary of the spherical cap is MAX_DISTANCE. Thus we want
      // fabs(atan(tan(phi) / cos(dtheta/2)) - phi) <= MAX_DISTANCE.
      double height = 2 * rand.nextDouble();
      double phi = Math.asin(1 - height);
      double maxDtheta =
          2 * Math.acos(Math.tan(Math.abs(phi)) / Math.tan(Math.abs(phi) + MAX_DISTANCE));
      maxDtheta = Math.min(Math.PI, maxDtheta); // At least 3 vertices.

      List<S2Point> points = new ArrayList<>();
      for (double theta = 0; theta < 2 * Math.PI; theta += rand.nextDouble() * maxDtheta) {
        points.add(
            x.mul(Math.cos(theta) * Math.cos(phi))
                .add(y.mul(Math.sin(theta) * Math.cos(phi)).add(z.mul(Math.sin(phi)))));
      }
      S2Shape shape = S2LaxPolygonShape.create(Lists.<List<S2Point>>newArrayList(points));
      S2Point centroid = S2ShapeMeasures.loopCentroid(shape, 0);
      double expectedArea = 2 * Math.PI * height;
      S2Point expectedCentroid = z.mul(expectedArea * (1 - 0.5 * height));
      assertTrue(centroid.sub(expectedCentroid).norm() < 2 * MAX_DISTANCE);
    }
  }

  private static void checkPruneDegeracies(String input, String expected) {
    S2Loop loop = testLoop(input);
    List<S2Point> vertices = S2ShapeMeasures.pruneDegeneracies(loop.vertices());
    StringBuilder actualBuilder = new StringBuilder();
    for (S2Point v : vertices) {
      actualBuilder.append((char) v.x);
    }
    assertEquals(expected, actualBuilder.toString());
  }

  private static void checkCanonicalLoopOrder(String input, LoopOrder expected) {
    assertEquals(expected, S2ShapeMeasures.canonicalLoopOrder(testLoop(input).vertices()));
  }

  private static void checkLoopAreaConsistentWithTurningAngle(S2Loop loop) {
    // Check that the area computed using GetArea() is consistent with the loop
    // curvature.  According to the Gauss-Bonnet theorem, the area of the loop
    // equals 2*Pi minus its curvature.
    double area = S2ShapeMeasures.loopArea(loop, 0);
    double gausArea = 2 * S2.M_PI - S2ShapeMeasures.turningAngle(loop, 0);
    // The error bound below is sufficient for current tests but not guaranteed.
    assertTrue(Math.abs(area - gausArea) <= 1e-14);
  }

  private static void checkTurningAngleInvariants(S2Loop input) {
    List<S2Point> loop1 = input.vertices();
    LoopOrder loopOrder1 = S2ShapeMeasures.canonicalLoopOrder(loop1);
    double expected = S2ShapeMeasures.turningAngle(loop1);
    List<S2Point> loop2 = new ArrayList<>(loop1);
    for (int i = 0; i < loop2.size(); ++i) {
      Collections.reverse(loop2);
      assertEquals(
          expected == 2 * S2.M_PI ? expected : -expected, S2ShapeMeasures.turningAngle(loop2));
      checkSameLoopOrder(loop1, loopOrder1, loop2, S2ShapeMeasures.canonicalLoopOrder(loop2));
      Collections.reverse(loop2);
      Collections.rotate(loop2, 1);
      assertEquals(expected, S2ShapeMeasures.turningAngle(loop2));
      checkSameLoopOrder(loop1, loopOrder1, loop2, S2ShapeMeasures.canonicalLoopOrder(loop2));
    }
  }

  private static void checkSameLoopOrder(
      List<S2Point> loop1, LoopOrder loopOrder1, List<S2Point> loop2, LoopOrder loopOrder2) {
    assertEquals(loop1.size(), loop2.size());
    int i1 = loopOrder1.first;
    int i2 = loopOrder2.first;
    for (int n = loop1.size(); --n >= 0; ) {
      assertEquals(loop1.get(i1 % loop1.size()), loop2.get(i2 % loop2.size()));
      i1 += loopOrder1.dir;
      i2 += loopOrder2.dir;
    }
  }

  private static S2Loop testLoop(String text) {
    List<S2Point> loop = new ArrayList<>();
    for (char c : text.toCharArray()) {
      loop.add(new S2Point(c, 0, 0));
    }
    return new S2Loop(loop);
  }
}
