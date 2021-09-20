/*
 * Copyright 2006 Google Inc.
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
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.geometry.S2Shape.MutableEdge;
import com.google.common.io.BaseEncoding;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Tests for {@link S2Polygon}.
 *
 */
@GwtCompatible
public strictfp class S2PolygonTest extends GeometryTestCase {
  private static Logger logger = Logger.getLogger(S2PolygonTest.class.getName());

  /**
   * The error margin used when comparing the actual and expected results of constructive geometry
   * operations. The intersections in the expected data were computed in lat-lng space, while the
   * actual intersections are computed using geodesics. The error due to this depends on the length
   * and direction of the line segment being intersected, and how close the intersection is to the
   * endpoints of the segment. The worst case is for a line segment between two points at the same
   * latitude, where the intersection point is in the middle of the segment. In this case the error
   * is approximately {@code (p * t^2) / 8}, where {@code p} is the absolute latitude in radians,
   * {@code t} is the longitude difference in radians, and both {@code p} and {@code t} are small.
   * The test cases all have small latitude and longitude differences. If {@code p} and {@code t}
   * are converted to degrees, the following error bound is valid as long as {@code (p * t^2 <
   * 150)}.
   */
  private static final double OPERATIONS_MAX_ERROR = 1e-4;

  // A set of nested loops around the point 0:0 (lat:lng).
  // Every vertex of NEAR0 is a vertex of NEAR1.
  private static final String NEAR_POINT = "0:0";
  private static final String NEAR0 = "-1:0, 0:1, 1:0, 0:-1;";
  private static final String NEAR1 = "-1:-1, -1:0, -1:1, 0:1, 1:1, 1:0, 1:-1, 0:-1;";
  private static final String NEAR2 = "-1:-2, -2:5, 5:-2;";
  private static final String NEAR3 = "-2:-2, -3:6, 6:-3;";
  private static final String NEAR_HEMI = "0:-90, -90:0, 0:90, 90:0;";

  // A set of nested loops around the point 0:180 (lat:lng).
  // Every vertex of FAR0 and FAR2 belongs to FAR1, and all
  // the loops except FAR2 are non-convex.
  private static final String FAR0 = "0:179, 1:180, 0:-179, 2:-180;";
  private static final String FAR1 =
      "0:179, -1:179, 1:180, -1:-179, 0:-179, 3:-178, 2:-180, 3:178;";
  private static final String FAR2 = "3:-178, 3:178, -1:179, -1:-179;";
  private static final String FAR3 = "-3:-178, 4:-177, 4:177, -3:178, -2:179;";
  private static final String FAR_HEMI = "0:-90, 60:90, -60:90;";

  // A set of nested loops around the point -90:0 (lat:lng).
  private static final String SOUTH_POINT = "-89.9999:0.001";
  private static final String SOUTH0A = "-90:0, -89.99:0.01, -89.99:0;";
  private static final String SOUTH0B = "-90:0, -89.99:0.03, -89.99:0.02;";
  private static final String SOUTH0C = "-90:0, -89.99:0.05, -89.99:0.04;";
  private static final String SOUTH1 = "-90:0, -89.9:0.1, -89.9:-0.1;";
  private static final String SOUTH2 = "-90:0, -89.8:0.2, -89.8:-0.2;";
  private static final String SOUTH_HEMI = "0:-180, 0:60, 0:-60;";

  // Two different loops that surround all the NEAR and FAR loops except
  // for the hemispheres.
  private static final String NEAR_FAR1 =
      "-1:-9, -9:-9, -9:9, 9:9, 9:-9, 1:-9, " + "1:-175, 9:-175, 9:175, -9:175, -9:-175, -1:-175;";
  private static final String NEAR_FAR2 =
      "-2:15, -2:170, -8:-175, 8:-175, 2:170, 2:15, 8:-4, -8:-4;";

  // Loops that result from intersection of other loops.
  private static final String FAR_SOUTH_H = "0:-180, 0:90, -60:90, 0:-90;";

  // Rectangles that form a cross, with only shared vertices, no crossing edges.
  // Optional holes outside the intersecting region.
  private static final String CROSS1 = "-2:1, -1:1, 1:1, 2:1, 2:-1, 1:-1, -1:-1, -2:-1;";
  private static final String CROSS1_SIDE_HOLE = "-1.5:0.5, -1.2:0.5, -1.2:-0.5, -1.5:-0.5;";
  private static final String CROSS2 = "1:-2, 1:-1, 1:1, 1:2, -1:2, -1:1, -1:-1, -1:-2;";
  private static final String CROSS2_SIDE_HOLE = "0.5:-1.5, 0.5:-1.2, -0.5:-1.2, -0.5:-1.5;";
  private static final String CROSS_CENTER_HOLE = "-0.5:0.5, 0.5:0.5, 0.5:-0.5, -0.5:-0.5;";

  // Two rectangles that intersect, but no edges cross and there's always
  // local containment (rather than crossing) at each shared vertex.
  // In this ugly ASCII art, 1 is A+B, 2 is B+C:
  //     +---+---+---+
  //     | A | B | C |
  //     +---+---+---+
  private static final String OVERLAP1 = "0:1, 1:1, 2:1, 2:0, 1:0, 0:0;";
  private static final String OVERLAP1_SIDE_HOLE = "0.2:0.8, 0.8:0.8, 0.8:0.2, 0.2:0.2;";
  private static final String OVERLAP2 = "1:1, 2:1, 3:1, 3:0, 2:0, 1:0;";
  private static final String OVERLAP2_SIDE_HOLE = "2.2:0.8, 2.8:0.8, 2.8:0.2, 2.2:0.2;";
  private static final String OVERLAP_CENTER_HOLE = "1.2:0.8, 1.8:0.8, 1.8:0.2, 1.2:0.2;";

  // The intersection of polygon B on A should be ~.25 while the intersection of A on B should
  // be ~.5.
  private static final String OVERLAP_A = "-10:10, 0:10, 0:-10, -10:-10, -10:0";
  private static final String OVERLAP_B = "-10:0, 10:0, 10:-5, -10:-5";

  // Two rectangles that are "adjacent", but rather than having common edges,
  // those edges are slightly off. A third rectangle that is not adjacent to
  // either of the first two.
  private static final String ADJACENT0 = "0:1, 1:1, 2:1, 2:0, 1:0, 0:0;";
  private static final String ADJACENT1 = "0:2, 1:2, 2:2, 2:1.01, 1:0.99, 0:1.01;";
  private static final String UN_ADJACENT = "10:10, 11:10, 12:10, 12:9, 11:9, 10:9;";

  // Shapes used to test comparison functions for polygons.
  private static final String RECTANGLE1 = "0:1, 1:1, 2:1, 2:0, 1:0, 0:0;";
  private static final String RECTANGLE2 = "5:1, 6:1, 7:1, 7:0, 6:0, 5:0;";
  private static final String TRIANGLE = "15:0, 17:0, 16:2;";
  private static final String TRIANGLE_ROT = "17:0, 16:2, 15:0;";

  private final S2Polygon near0 = makeVerbatimPolygon(NEAR0);
  private final S2Polygon near10 = makeVerbatimPolygon(NEAR0 + NEAR1);
  private final S2Polygon near30 = makeVerbatimPolygon(NEAR3 + NEAR0);
  private final S2Polygon near32 = makeVerbatimPolygon(NEAR2 + NEAR3);
  private final S2Polygon near3210 = makeVerbatimPolygon(NEAR0 + NEAR2 + NEAR3 + NEAR1);
  private final S2Polygon nearH3210 =
      makeVerbatimPolygon(NEAR0 + NEAR2 + NEAR3 + NEAR_HEMI + NEAR1);

  private final S2Polygon far10 = makeVerbatimPolygon(FAR0 + FAR1);
  private final S2Polygon far21 = makeVerbatimPolygon(FAR2 + FAR1);
  private final S2Polygon far321 = makeVerbatimPolygon(FAR2 + FAR3 + FAR1);
  private final S2Polygon farH20 = makeVerbatimPolygon(FAR2 + FAR_HEMI + FAR0);
  private final S2Polygon farH3210 = makeVerbatimPolygon(FAR2 + FAR_HEMI + FAR0 + FAR1 + FAR3);

  private final S2Polygon south0ab = makeVerbatimPolygon(SOUTH0A + SOUTH0B);
  private final S2Polygon south2 = makeVerbatimPolygon(SOUTH2);
  private final S2Polygon south210b = makeVerbatimPolygon(SOUTH2 + SOUTH0B + SOUTH1);
  private final S2Polygon southH21 = makeVerbatimPolygon(SOUTH2 + SOUTH_HEMI + SOUTH1);
  private final S2Polygon southH20abc =
      makeVerbatimPolygon(SOUTH2 + SOUTH0B + SOUTH_HEMI + SOUTH0A + SOUTH0C);

  private final S2Polygon nf1n10f2s10abc =
      makeVerbatimPolygon(SOUTH0C + FAR2 + NEAR1 + NEAR_FAR1 + NEAR0 + SOUTH1 + SOUTH0B + SOUTH0A);

  private final S2Polygon nf2n2f210s210ab =
      makeVerbatimPolygon(
          FAR2 + SOUTH0A + FAR1 + SOUTH1 + FAR0 + SOUTH0B + NEAR_FAR2 + SOUTH2 + NEAR2);

  private final S2Polygon f32n0 = makeVerbatimPolygon(FAR2 + NEAR0 + FAR3);
  private final S2Polygon n32s0b = makeVerbatimPolygon(NEAR3 + SOUTH0B + NEAR2);

  private final S2Polygon adj0 = makeVerbatimPolygon(ADJACENT0);
  private final S2Polygon adj1 = makeVerbatimPolygon(ADJACENT1);
  private final S2Polygon unAdj = makeVerbatimPolygon(UN_ADJACENT);

  private final S2Polygon farH = makeVerbatimPolygon(FAR_HEMI);
  private final S2Polygon southH = makeVerbatimPolygon(SOUTH_HEMI);
  private final S2Polygon farHSouthH = makeVerbatimPolygon(FAR_SOUTH_H);

  private final S2Polygon cross1 = makeVerbatimPolygon(CROSS1);
  private final S2Polygon cross1SideHole = makeVerbatimPolygon(CROSS1 + CROSS1_SIDE_HOLE);
  private final S2Polygon cross1CenterHole = makeVerbatimPolygon(CROSS1 + CROSS_CENTER_HOLE);
  private final S2Polygon cross2 = makeVerbatimPolygon(CROSS2);
  private final S2Polygon cross2SideHole = makeVerbatimPolygon(CROSS2 + CROSS2_SIDE_HOLE);
  private final S2Polygon cross2CenterHole = makeVerbatimPolygon(CROSS2 + CROSS_CENTER_HOLE);

  private final S2Polygon overlap1 = makeVerbatimPolygon(OVERLAP1);
  private final S2Polygon overlap1SideHole = makeVerbatimPolygon(OVERLAP1 + OVERLAP1_SIDE_HOLE);
  private final S2Polygon overlap2 = makeVerbatimPolygon(OVERLAP2);
  private final S2Polygon overlap2SideHole = makeVerbatimPolygon(OVERLAP2 + OVERLAP2_SIDE_HOLE);
  private final S2Polygon overlap1CenterHole = makeVerbatimPolygon(OVERLAP1 + OVERLAP_CENTER_HOLE);
  private final S2Polygon overlap2CenterHole = makeVerbatimPolygon(OVERLAP2 + OVERLAP_CENTER_HOLE);

  private final S2Polygon overlapA = makeVerbatimPolygon(OVERLAP_A);
  private final S2Polygon overlapB = makeVerbatimPolygon(OVERLAP_B);

  private final S2Polygon empty = new S2Polygon();
  private final S2Polygon full = new S2Polygon(S2Loop.full());

  private static final int VALIDITY_ITERS = 100;

  private static void checkContains(String aText, String bText) {
    S2Polygon a = makeVerbatimPolygon(aText);
    S2Polygon b = makeVerbatimPolygon(bText);
    assertTrue(a.contains(b));
    assertTrue(a.approxContains(b, S1Angle.radians(1e-15)));
  }

  private static void checkContainsPoint(String aText, String bText) {
    S2Polygon a = makePolygon(aText);
    assertTrue(a.contains(makePoint(bText)));
  }

  private static byte[] encode(S2Polygon polygon) throws IOException {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    polygon.encode(outputStream);
    return outputStream.toByteArray();
  }

  private static S2Polygon decode(byte[] encoded) throws IOException {
    return S2Polygon.decode(new ByteArrayInputStream(encoded));
  }

  private static void encodeDecode(S2Polygon polygon) throws IOException {
    byte[] encoded = encode(polygon);
    S2Polygon decodedPolygon = decode(encoded);
    assertEquals(polygon, decodedPolygon);
  }

  // Make sure we've set things up correctly.
  public void testInit() {
    checkContains(NEAR1, NEAR0);
    checkContains(NEAR2, NEAR1);
    checkContains(NEAR3, NEAR2);
    checkContains(NEAR_HEMI, NEAR3);
    checkContains(FAR1, FAR0);
    checkContains(FAR2, FAR1);
    checkContains(FAR3, FAR2);
    checkContains(FAR_HEMI, FAR3);
    checkContains(SOUTH1, SOUTH0A);
    checkContains(SOUTH1, SOUTH0B);
    checkContains(SOUTH1, SOUTH0C);
    checkContains(SOUTH_HEMI, SOUTH2);
    checkContains(NEAR_FAR1, NEAR3);
    checkContains(NEAR_FAR1, FAR3);
    checkContains(NEAR_FAR2, NEAR3);
    checkContains(NEAR_FAR2, FAR3);

    checkContainsPoint(NEAR0, NEAR_POINT);
    checkContainsPoint(NEAR1, NEAR_POINT);
    checkContainsPoint(NEAR2, NEAR_POINT);
    checkContainsPoint(NEAR3, NEAR_POINT);
    checkContainsPoint(NEAR_HEMI, NEAR_POINT);
    checkContainsPoint(SOUTH0A, SOUTH_POINT);
    checkContainsPoint(SOUTH1, SOUTH_POINT);
    checkContainsPoint(SOUTH2, SOUTH_POINT);
    checkContainsPoint(SOUTH_HEMI, SOUTH_POINT);
  }

  public void testOverlapFraction() {
    double bOverlapA = S2Polygon.getOverlapFraction(overlapA, overlapB);
    double aOverlapB = S2Polygon.getOverlapFraction(overlapB, overlapA);
    assertEquals(0.25, bOverlapA, 0.01);
    assertEquals(0.5, aOverlapB, 0.01);
  }

  public void testOriginNearPole() {
    // S2Polygon operations are more efficient if S2.origin() is near a pole.
    // (Loops that contain a pole tend to have very loose bounding boxes because
    // they span the full longitude range.  S2Polygon canonicalizes all loops so
    // that they don't contain S2.origin(), thus by placing S2.origin() near a
    // pole we minimize the number of canonical loops which contain that pole.)
    assertTrue(S2LatLng.latitude(S2.origin()).degrees() >= 80);
  }

  private static class TestCase {
    final String a;
    final String b;
    final String a_and_b;
    final String a_or_b;
    final String a_minus_b;

    TestCase(String a, String b, String a_and_b, String a_or_b, String a_minus_b) {
      this.a = a;
      this.b = b;
      this.a_and_b = a_and_b;
      this.a_or_b = a_or_b;
      this.a_minus_b = a_minus_b;
    }
  }

  private static TestCase[] testCases = {
    // Two triangles that share an edge.
    new TestCase("4:2, 3:1, 3:3;", "3:1, 2:2, 3:3;", "", "4:2, 3:1, 2:2, 3:3;", "4:2, 3:1, 3:3;"),
    // Two vertical bars and a horizontal bar connecting them.
    new TestCase(
        "0:0, 0:2, 3:2, 3:0;   0:3, 0:5, 3:5, 3:3;",
        "1:1, 1:4, 2:4, 2:1;",
        "1:1, 1:2, 2:2, 2:1;   1:3, 1:4, 2:4, 2:3;",
        "0:0, 0:2, 1:2, 1:3, 0:3, 0:5, 3:5, 3:3, 2:3, 2:2, 3:2, 3:0;",
        "0:0, 0:2, 1:2, 1:1, 2:1, 2:2, 3:2, 3:0;   " + "0:3, 0:5, 3:5, 3:3, 2:3, 2:4, 1:4, 1:3;"),
    // Two vertical bars and two horizontal bars centered around S2.origin().
    new TestCase(
        "1:88, 1:93, 2:93, 2:88;   -1:88, -1:93, 0:93, 0:88;",
        "-2:89, -2:90, 3:90, 3:89;   -2:91, -2:92, 3:92, 3:91;",
        "1:89, 1:90, 2:90, 2:89;   1:91, 1:92, 2:92, 2:91;   "
            + "-1:89, -1:90, 0:90, 0:89;   -1:91, -1:92, 0:92, 0:91;",
        "-1:88, -1:89, -2:89, -2:90, -1:90, -1:91, -2:91, -2:92, -1:92,"
            + "-1:93, 0:93, 0:92, 1:92, 1:93, 2:93, 2:92, 3:92, 3:91, 2:91,"
            + "2:90, 3:90, 3:89, 2:89, 2:88, 1:88, 1:89, 0:89, 0:88;   "
            + "0:90, 0:91, 1:91, 1:90;",
        "1:88, 1:89, 2:89, 2:88;   1:90, 1:91, 2:91, 2:90;   "
            + "1:92, 1:93, 2:93, 2:92;   -1:88, -1:89, 0:89, 0:88;   "
            + "-1:90, -1:91, 0:91, 0:90;   -1:92, -1:93, 0:93, 0:92;"),
    // Two interlocking square doughnuts centered around -S2.origin().
    new TestCase(
        "-1:-93, -1:-89, 3:-89, 3:-93;   0:-92, 0:-90, 2:-90, 2:-92;",
        "-3:-91, -3:-87, 1:-87, 1:-91;   -2:-90, -2:-88, 0:-88, 0:-90;",
        "-1:-91, -1:-90, 0:-90, 0:-91;   0:-90, 0:-89, 1:-89, 1:-90;",
        "-1:-93, -1:-91, -3:-91, -3:-87, 1:-87, 1:-89, 3:-89, 3:-93;   "
            + "0:-92, 0:-91, 1:-91, 1:-90, 2:-90, 2:-92;   "
            + "-2:-90, -2:-88, 0:-88, 0:-89, -1:-89, -1:-90;",
        "-1:-93, -1:-91, 0:-91, 0:-92, 2:-92, 2:-90, 1:-90, 1:-89, 3:-89,"
            + "3:-93;   -1:-90, -1:-89, 0:-89, 0:-90;"),
    // An incredibly thin triangle intersecting a square, such that the two
    // intersection points of the triangle with the square are identical.
    // This results in a degenerate loop that needs to be handled correctly.
    new TestCase(
        "10:44, 10:46, 12:46, 12:44;",
        "11:45, 89:45.00000000000001, 90:45;",
        "", // Empty intersection!
        // Original square with extra vertex, and triangle disappears (due to
        // default vertex_merge_radius of
        // S2EdgeUtil.DEFAULT_INTERSECTION_TOLERANCE).
        "10:44, 10:46, 12:46, 12:45, 12:44;",
        "10:44, 10:46, 12:46, 12:45, 12:44;")
  };

  public void testIsValidUnitLength() {
    for (int iter = 0; iter < VALIDITY_ITERS; ++iter) {
      List<List<S2Point>> loops = getConcentricLoops(1 + random(6), 3);
      List<S2Point> loop = loops.get(random(loops.size()));
      int index = random(loop.size());
      S2Point p = loop.get(index);
      switch (random(3)) {
        case 0:
          p = new S2Point(0, 0, 0);
          break;
        case 1:
          p = S2Point.mul(p, 1e-30 * Math.pow(1e60, rand.nextDouble()));
          break;
        default:
          p = new S2Point(Double.NaN, Double.NaN, Double.NaN);
          break;
      }
      loop.set(index, p);
      checkInvalid(loops, "unit length");
    }
  }

  public void testIsValidVertexCount() {
    for (int iter = 0; iter < VALIDITY_ITERS; ++iter) {
      List<List<S2Point>> loops = Lists.newArrayList();
      List<S2Point> loopPoints = Lists.newArrayList();
      if (oneIn(2)) {
        loopPoints.add(randomPoint());
        loopPoints.add(randomPoint());
      }
      loops.add(loopPoints);
      checkInvalid(loops, "at least 3 vertices");
    }
  }

  public void testIsValidDuplicateVertex() {
    for (int iter = 0; iter < VALIDITY_ITERS; ++iter) {
      List<List<S2Point>> loops = getConcentricLoops(1, 3);
      List<S2Point> loop = loops.get(0);
      int n = loop.size();
      int i = random(n);
      int j = random(n - 1);
      loop.set(i, loop.get(j + (j >= i ? 1 : 0)));
      checkInvalid(loops, "duplicate vertex");
    }
  }

  public void testIsValidSelfIntersection() {
    for (int iter = 0; iter < VALIDITY_ITERS; ++iter) {
      // Use multiple loops so that we can test both holes and shells. We need at least 5 vertices
      // so that the modified edges don't intersect any nested loops.
      List<List<S2Point>> loops = getConcentricLoops(1 + random(6), 5);
      List<S2Point> loop = loops.get(random(loops.size()));
      int n = loop.size();
      int i = random(n);
      S2Point temp = loop.get(i);
      loop.set(i, loop.get((i + 1) % n));
      loop.set((i + 1) % n, temp);
      checkInvalid(loops, "crosses edge");
    }
  }

  public void testIsValidEmptyLoop() {
    for (int iter = 0; iter < VALIDITY_ITERS; ++iter) {
      List<List<S2Point>> loops = getConcentricLoops(random(5), 3);
      List<S2Point> emptyLoop = Lists.newArrayList();
      loops.add(emptyLoop);
      checkInvalid(loops, "Non-empty, non-full loops must have at least 3 vertices");
    }
  }

  public void testIsValidFullLoop() {
    S2Loop fullLoop = S2Loop.full();
    List<S2Point> fullLoopPoints = Lists.newArrayList();
    for (int i = 0; i < fullLoop.numVertices(); ++i) {
      fullLoopPoints.add(fullLoop.vertex(i));
    }
    for (int iter = 0; iter < VALIDITY_ITERS; ++iter) {
      // This is only an error if there is at least one other loop.
      List<List<S2Point>> loops = getConcentricLoops(1 + random(5), 3);
      loops.add(fullLoopPoints);
      checkInvalid(loops, "full loop appears in non-full polygon");
    }
  }

  public void testIsValidLoopsCrossing() {
    for (int iter = 0; iter < VALIDITY_ITERS; ++iter) {
      List<List<S2Point>> loops = getConcentricLoops(2, 4);
      // Both loops have the same number of vertices, and vertices at the same index position are
      // collinear with the center point, so we can create a crossing by simply exchanging two
      // vertices at the same index position.
      int n = loops.get(0).size();
      int i = random(n);
      S2Point temp = loops.get(0).get(i);
      loops.get(0).set(i, loops.get(1).get(i));
      loops.get(1).set(i, temp);
      if (oneIn(2)) {
        // By copying the two adjacent vertices from one loop to the other, we can ensure that the
        // crossings happen at vertices rather than edges.
        loops.get(0).set((i + 1) % n, loops.get(1).get((i + 1) % n));
        loops.get(0).set((i + n - 1) % n, loops.get(1).get((i + n - 1) % n));
      }
      checkInvalid(loops, "crosses loop");
    }
  }

  public void testIsValidDuplicateEdge() {
    for (int iter = 0; iter < VALIDITY_ITERS; ++iter) {
      List<List<S2Point>> loops = getConcentricLoops(2, 4);
      int n = loops.get(0).size();
      List<S2Point> loopA = loops.get(0);
      List<S2Point> loopB = loops.get(1);
      if (oneIn(2)) {
        // Create a shared edge (same direction in both loops).
        int i = random(n);
        loopA.set(i, loopB.get(i));
        loopA.set((i + 1) % n, loopB.get((i + 1) % n));
      } else {
        // Create a reversed edge (opposite direction in either loop) by cutting loop 0 into two
        // halves along one of its diagonals and replacing both loops with the result.
        int split = 2 + random(n - 3);
        loopB.clear();
        loopB.add(loopA.get(0));
        for (int s = split; s < n; ++s) {
          loopB.add(loopA.get(s));
        }
      }
      checkInvalid(loops, "has duplicate");
    }
  }

  public void testIsValidLoopDepthNegative() {
    for (int iter = 0; iter < VALIDITY_ITERS; ++iter) {
      S2Polygon poly = new S2Polygon(loops(getConcentricLoops(1 + random(4), 3 /*min_vertices*/)));
      int i = random(poly.numLoops());
      if (i == 0 || oneIn(3)) {
        poly.loop(i).setDepth(-1);
      } else {
        poly.loop(i).setDepth(poly.loop(i - 1).depth() + 2);
      }
      checkInvalid(poly, "invalid loop depth");
    }
  }

  public void testIsValidLoopNestingInvalid() {
    for (int iter = 0; iter < VALIDITY_ITERS; ++iter) {
      S2Polygon poly = new S2Polygon(loops(getConcentricLoops(2 + random(4), 3 /*min_vertices*/)));
      poly.loop(random(poly.numLoops())).invert();
      checkInvalid(poly, "Invalid nesting");
    }
  }

  /**
   * Checks that the S2Loop / S2Polygon constructors, and {@code isValid()} methods don't crash when
   * they receive arbitrary invalid input. (We don't test large inputs; it is assumed that the
   * client enforces their own size limits before even attempting to construct geometric objects.)
   */
  public void testIsValidFuzz() {
    for (int iter = 0; iter < VALIDITY_ITERS; ++iter) {
      int numLoops = 1 + random(10);
      List<List<S2Point>> loops = Lists.newArrayList();
      for (int i = 0; i < numLoops; ++i) {
        int numVertices = random(10);
        List<S2Point> loop = Lists.newArrayList();
        while (loop.size() < numVertices) {
          // Since the number of vertices is random, we automatically test empty loops, full loops,
          // and invalid vertex counts. Also, since most vertices are random, we automatically get
          // self-intersections and loop crossings. That leaves zero and NaN vertices, duplicate
          // vertices, and duplicate edges to be created explicitly.
          if (oneIn(10)) {
            // Zero vertex.
            loop.add(new S2Point(0, 0, 0));
          } else if (oneIn(10)) {
            // NaN vertex.
            loop.add(new S2Point(Double.NaN, Double.NaN, Double.NaN));
          } else if (oneIn(10) && !loop.isEmpty()) {
            // Duplicate vertex.
            loop.add(loop.get(random(loop.size())));
          } else if (oneIn(10) && loop.size() + 2 <= numVertices && loops.size() > 0) {
            // Try to copy an edge from a random loop.
            List<S2Point> other = loops.get(random(loops.size()));
            int n = other.size();
            if (n >= 2) {
              int k0 = random(n);
              int k1 = (k0 + 1) % n;
              if (oneIn(2)) {
                // Copy the reverse of this edge.
                int temp = k0;
                k0 = k1;
                k1 = temp;
              }
              loop.add(other.get(k0));
              loop.add(other.get(k1));
            }
          } else {
            // Add a random non-unit-length point.
            S2Point p = randomPoint();
            loop.add(S2Point.mul(p, 1e-30 * Math.pow(1e60, rand.nextDouble())));
          }
        }
        loops.add(loop);
      }
      // We could get any error message.
      checkInvalid(loops, "");
    }
  }

  private static void checkInvalid(List<List<S2Point>> invalidLoops, String snippet) {
    checkInvalid(new S2Polygon(loops(invalidLoops)), snippet);
  }

  private static void checkInvalid(S2Polygon polygon, String snippet) {
    S2Error error = new S2Error();
    assertTrue(polygon.findValidationError(error));
    assertTrue(
        "Actual Error: " + error.text() + ",\n but expected substring: " + snippet,
        error.text().indexOf(snippet) != -1);
  }

  private static List<S2Loop> loops(List<List<S2Point>> vertices) {
    List<S2Loop> loops = Lists.newArrayList();
    for (int i = 0; i < vertices.size(); ++i) {
      loops.add(new S2Loop(vertices.get(i)));
    }
    Collections.shuffle(loops);
    return loops;
  }

  public void testEncodeDecode() throws IOException {
    // Empty polygon.
    encodeDecode(empty);

    // Full polygon
    encodeDecode(full);

    for (TestCase testCase : testCases) {
      encodeDecode(makePolygon(testCase.a, S2CellId.MAX_LEVEL));
      encodeDecode(makePolygon(testCase.b, S2CellId.MAX_LEVEL));
      encodeDecode(makePolygon(testCase.a_and_b, S2CellId.MAX_LEVEL));
      encodeDecode(makePolygon(testCase.a_or_b, S2CellId.MAX_LEVEL));
      encodeDecode(makePolygon(testCase.a_minus_b, S2CellId.MAX_LEVEL));
    }
  }

  public void testEncodingSize_EmptyPolygon() throws IOException {
    byte[] encoded = encode(new S2Polygon());

    // 1 byte for version, 1 for the level, 1 for the length.
    assertEquals(3, encoded.length);
  }

  public void testEncodingSize_SnappedPolygon() throws IOException {
    S2Polygon snapped = makePolygon("0:0, 0:2, 2:0; 0:0, 0:-2, -2:-2, -2:0", S2CellId.MAX_LEVEL);
    byte[] encoded = encode(snapped);

    // 2 loops, one with 3 vertices, one with 4.
    // Polygon:
    //   1 byte for version
    //   1 byte for level
    //   1 byte for num_loops
    // Loops:
    //   5 bytes overhead
    //   8 bytes per vertex
    assertEquals(1 + 1 + 1 + 2 * 5 + 7 * 8, encoded.length);
  }

  public void testOperations() {
    S2Polygon farSouth = new S2Polygon();
    farSouth.initToIntersection(farH, southH);
    checkEqual(farSouth, farHSouthH, 1e-15);

    for (int testNumber = 0; testNumber < testCases.length; testNumber++) {
      TestCase test = testCases[testNumber];
      logger.info("Polygon operation test case " + testNumber);
      S2Polygon a = makeVerbatimPolygon(test.a);
      S2Polygon b = makeVerbatimPolygon(test.b);
      S2Polygon expectedAAndB = makeVerbatimPolygon(test.a_and_b);
      S2Polygon expectedAOrB = makeVerbatimPolygon(test.a_or_b);
      S2Polygon expectedAMinusB = makeVerbatimPolygon(test.a_minus_b);

      S2Polygon aAndB = new S2Polygon();
      S2Polygon aOrB = new S2Polygon();
      S2Polygon aMinusB = new S2Polygon();
      aAndB.initToIntersection(a, b);
      checkEqual(aAndB, expectedAAndB, OPERATIONS_MAX_ERROR);
      aOrB.initToUnion(a, b);
      checkEqual(aOrB, expectedAOrB, OPERATIONS_MAX_ERROR);
      checkUnion(a, b);
      aMinusB.initToDifference(a, b);
      checkEqual(aMinusB, expectedAMinusB, OPERATIONS_MAX_ERROR);
    }
  }

  private void polylineIntersectionSharedEdgeTest(S2Polygon p, int startVertex, int direction) {
    logger.info(
        "Polyline intersection shared edge test start=" + startVertex + " direction=" + direction);
    List<S2Point> points = Lists.newArrayList();
    points.add(p.loop(0).vertex(startVertex));
    points.add(p.loop(0).vertex(startVertex + direction));
    S2Polyline polyline = new S2Polyline(points);
    List<S2Polyline> polylines;
    if (direction < 0) {
      polylines = p.intersectWithPolyline(polyline);
      assertEquals(0, polylines.size());
      polylines = p.subtractFromPolyline(polyline);
      assertEquals(1, polylines.size());
      assertEquals(2, polylines.get(0).numVertices());
      assertEquals(points.get(0), polylines.get(0).vertex(0));
      assertEquals(points.get(1), polylines.get(0).vertex(1));
    } else {
      polylines = p.intersectWithPolyline(polyline);
      assertEquals(1, polylines.size());
      assertEquals(2, polylines.get(0).numVertices());
      assertEquals(points.get(0), polylines.get(0).vertex(0));
      assertEquals(points.get(1), polylines.get(0).vertex(1));
      polylines = p.subtractFromPolyline(polyline);
      assertEquals(0, polylines.size());
    }
  }

  /**
   * This tests polyline-polyline intersections. It covers the same edge cases as {@code
   * testOperations} and also adds some extra tests for shared edges.
   */
  public void testPolylineIntersection() {
    for (int v = 0; v < 3; ++v) {
      polylineIntersectionSharedEdgeTest(cross1, v, 1);
      polylineIntersectionSharedEdgeTest(cross1, v + 1, -1);
      polylineIntersectionSharedEdgeTest(cross1SideHole, v, 1);
      polylineIntersectionSharedEdgeTest(cross1SideHole, v + 1, -1);
    }

    // This duplicates some of the tests in testOperations by
    // converting the outline of polygon A to a polyline then intersecting
    // it with the polygon B. It then converts B to a polyline and intersects
    // it with A. It then feeds all of the results into a polygon builder and
    // tests that the output is equal to doing an intersection between A and B.

    for (int testNumber = 0; testNumber < testCases.length; testNumber++) {
      TestCase test = testCases[testNumber];
      logger.info("Polyline intersection test case " + testNumber);
      S2Polygon a = makeVerbatimPolygon(test.a);
      S2Polygon b = makeVerbatimPolygon(test.b);
      S2Polygon expectedAAndB = makeVerbatimPolygon(test.a_and_b);

      List<S2Point> points = Lists.newArrayList();
      List<S2Polyline> polylines = Lists.newArrayList();
      for (int ab = 0; ab < 2; ab++) {
        S2Polygon tmp = (ab == 1) ? a : b;
        S2Polygon tmp2 = (ab == 1) ? b : a;
        for (int l = 0; l < tmp.numLoops(); l++) {
          points.clear();
          if (tmp.loop(l).isHole()) {
            for (int v = tmp.loop(l).numVertices(); v >= 0; v--) {
              points.add(tmp.loop(l).vertex(v));
            }
          } else {
            for (int v = 0; v <= tmp.loop(l).numVertices(); v++) {
              points.add(tmp.loop(l).vertex(v));
            }
          }
          polylines.addAll(tmp2.intersectWithPolyline(new S2Polyline(points)));
        }
      }

      S2PolygonBuilder builder = new S2PolygonBuilder(S2PolygonBuilder.Options.DIRECTED_XOR);
      for (int i = 0; i < polylines.size(); i++) {
        for (int j = 0; j < polylines.get(i).numVertices() - 1; j++) {
          builder.addEdge(polylines.get(i).vertex(j), polylines.get(i).vertex(j + 1));
          logger.info(
              " ... Adding edge: "
                  + polylines.get(i).vertex(j)
                  + " - "
                  + polylines.get(i).vertex(j + 1));
        }
      }

      S2Polygon a_and_b = new S2Polygon();
      assertTrue(builder.assemblePolygon(a_and_b, null));
      checkEqual(a_and_b, expectedAAndB, OPERATIONS_MAX_ERROR);
    }
  }

  private static void checkEqual(S2Polygon a, S2Polygon b) {
    checkEqual(a, b, 0);
  }

  private static void checkEqual(S2Polygon a, S2Polygon b, final double maxError) {
    if (a.boundaryApproxEquals(b, maxError)) {
      return;
    }

    S2PolygonBuilder builder = new S2PolygonBuilder(S2PolygonBuilder.Options.DIRECTED_XOR);
    builder.addPolygon(a);
    S2Polygon a2 = new S2Polygon();
    assertTrue(builder.assemblePolygon(a2, null));
    builder.addPolygon(b);
    S2Polygon b2 = new S2Polygon();
    assertTrue(builder.assemblePolygon(b2, null));
    assertTrue(a2.boundaryApproxEquals(b2, maxError));
  }

  private static void checkComplementary(S2Polygon a, S2Polygon b) {
    S2Polygon b1 = new S2Polygon();
    b1.initToComplement(b);
    checkEqual(a, b1);
  }

  public void testApproxContains() {
    // Get a random S2Cell as a polygon.
    S2CellId id = S2CellId.fromLatLng(S2LatLng.fromE6(69852241, 6751108));
    S2Cell cell = new S2Cell(id.parent(10));
    S2Polygon cellAsPolygon = new S2Polygon(cell);

    // We want to roughly bisect the polygon, so we make a rectangle that is the
    // top half of the current polygon's bounding rectangle.
    S2LatLngRect bounds = cellAsPolygon.getRectBound();
    S2LatLngRect upperHalf =
        new S2LatLngRect(new R1Interval(bounds.lat().getCenter(), bounds.lat().hi()), bounds.lng());

    // Turn the S2LatLngRect into an S2Polygon
    List<S2Point> points = Lists.newArrayList();
    for (int i = 0; i < 4; i++) {
      points.add(upperHalf.getVertex(i).toPoint());
    }
    List<S2Loop> loops = Lists.newArrayList();
    loops.add(new S2Loop(points));
    S2Polygon upperHalfPolygon = new S2Polygon(loops);

    // Get the intersection. There is no guarantee that the intersection will be
    // contained by A or B.
    S2Polygon intersection = new S2Polygon();
    intersection.initToIntersection(cellAsPolygon, upperHalfPolygon);
    assertFalse(cellAsPolygon.contains(intersection));
    assertTrue(
        cellAsPolygon.approxContains(intersection, S2EdgeUtil.DEFAULT_INTERSECTION_TOLERANCE));
  }

  public void tryUnion(S2Polygon a, S2Polygon b) {
    S2Polygon union1 = new S2Polygon();
    union1.initToUnion(a, b);

    S2Polygon union2 = S2Polygon.union(Arrays.asList(new S2Polygon(a), new S2Polygon(b)));

    checkEqual(union1, union2);
  }

  /**
   * Given a pair of polygons where A contains B, check that various identities involving union,
   * intersection, and difference operations hold true.
   */
  private static void checkOneNestedPair(S2Polygon a, S2Polygon b) {
    assertTrue(a.contains(b));
    assertEquals(!b.isEmpty(), a.intersects(b));
    assertEquals(!b.isEmpty(), b.intersects(a));

    S2Polygon c = new S2Polygon();
    c.initToUnion(a, b);
    checkEqual(c, a);

    S2Polygon d = new S2Polygon();
    d.initToIntersection(a, b);
    checkEqual(d, b);

    S2Polygon e = new S2Polygon();
    e.initToDifference(b, a);
    assertTrue(e.isEmpty());
  }

  /**
   * Given a pair of disjoint polygons A and B, check that various identities involving union,
   * intersection, and difference operations hold true.
   */
  private static void checkOneDisjointPair(S2Polygon a, S2Polygon b) {
    assertFalse(a.intersects(b));
    assertFalse(b.intersects(a));
    assertEquals(b.isEmpty(), a.contains(b));
    assertEquals(a.isEmpty(), b.contains(a));

    S2Polygon ab = new S2Polygon();
    S2Polygon c = new S2Polygon();
    S2Polygon d = new S2Polygon();
    S2Polygon e = new S2Polygon();
    S2Polygon f = new S2Polygon();
    S2PolygonBuilder builder = new S2PolygonBuilder(S2PolygonBuilder.Options.DIRECTED_XOR);
    builder.addPolygon(a);
    builder.addPolygon(b);
    assertTrue(builder.assemblePolygon(ab, null));

    c.initToUnion(a, b);
    checkEqual(c, ab);

    d.initToIntersection(a, b);
    assertTrue(d.isEmpty());

    e.initToDifference(a, b);
    checkEqual(e, a);

    f.initToDifference(b, a);
    checkEqual(f, b);
  }

  /**
   * Given polygons A and B whose union covers the sphere, check that various identities involving
   * union, intersection, and difference hold true.
   */
  private static void checkOneCoveringPair(S2Polygon a, S2Polygon b) {
    assertEquals(a.isFull(), a.contains(b));
    assertEquals(b.isFull(), b.contains(a));
    S2Polygon c = new S2Polygon();
    c.initToUnion(a, b);
    assertTrue(c.isFull());
  }

  /**
   * Given polygons A and B such that both A and its complement intersect both B and its complement,
   * check that various identities involving union, intersection, and difference hold true.
   */
  private static void checkOneOverlappingPair(S2Polygon a, S2Polygon b) {
    assertFalse(a.contains(b));
    assertFalse(b.contains(a));
    assertTrue(a.intersects(b));

    S2Polygon c = new S2Polygon();
    c.initToUnion(a, b);
    assertFalse(c.isFull());

    S2Polygon d = new S2Polygon();
    d.initToIntersection(a, b);
    assertFalse(d.isEmpty());

    S2Polygon e = new S2Polygon();
    e.initToDifference(b, a);
    assertFalse(e.isEmpty());
  }

  /**
   * Given a pair of polygons where A contains B, test various identities involving A, B, and their
   * complements.
   */
  private static void checkNestedPair(S2Polygon a, S2Polygon b) {
    S2Polygon a1 = new S2Polygon();
    S2Polygon b1 = new S2Polygon();
    a1.initToComplement(a);
    b1.initToComplement(b);
    checkOneNestedPair(a, b);
    checkOneNestedPair(b1, a1);
    checkOneDisjointPair(a1, b);
    checkOneCoveringPair(a, b1);
  }

  /**
   * Given a pair of disjoint polygons A and B, test various identities involving A, B, and their
   * complements.
   */
  private static void checkDisjointPair(S2Polygon a, S2Polygon b) {
    S2Polygon a1 = new S2Polygon();
    a1.initToComplement(a);
    S2Polygon b1 = new S2Polygon();
    b1.initToComplement(b);
    checkOneDisjointPair(a, b);
    checkOneCoveringPair(a1, b1);
    checkOneNestedPair(a1, b);
    checkOneNestedPair(b1, a);
  }

  /**
   * Given polygons A and B such that both A and its complement intersect both B and its complement,
   * test various identities involving these four polygons.
   */
  private static void checkOverlappingPair(S2Polygon a, S2Polygon b) {
    S2Polygon a1 = new S2Polygon();
    a1.initToComplement(a);
    S2Polygon b1 = new S2Polygon();
    b1.initToComplement(b);
    checkOneOverlappingPair(a, b);
    checkOneOverlappingPair(a1, b1);
    checkOneOverlappingPair(a1, b);
    checkOneOverlappingPair(a, b1);
  }

  /** "a1" is the complement of "a", and "b1" is the complement of "b". */
  private static void checkOneComplementPair(S2Polygon a, S2Polygon a1, S2Polygon b, S2Polygon b1) {
    // Check DeMorgan's Law and that subtraction is the same as intersection
    // with the complement.  This function is called multiple times in order to
    // test the various combinations of complements.
    S2Polygon aOrB = new S2Polygon();
    S2Polygon aAndB1 = new S2Polygon();
    S2Polygon aMinusB = new S2Polygon();
    aAndB1.initToIntersection(a, b1);
    aOrB.initToUnion(a1, b);
    aMinusB.initToDifference(a, b);
    checkComplementary(aOrB, aAndB1);
    checkEqual(aMinusB, aAndB1);
  }

  /** Test identities that should hold for any pair of polygons A, B and their complements. */
  private static void checkComplements(S2Polygon a, S2Polygon b) {
    S2Polygon a1 = new S2Polygon();
    a1.initToComplement(a);
    S2Polygon b1 = new S2Polygon();
    b1.initToComplement(b);
    checkOneComplementPair(a, a1, b, b1);
    checkOneComplementPair(a1, a, b, b1);
    checkOneComplementPair(a, a1, b1, b);
    checkOneComplementPair(a1, a, b1, b);
  }

  private static void checkUnion(S2Polygon a, S2Polygon b) {
    S2Polygon c = new S2Polygon();
    c.initToUnion(a, b);
    List<S2Polygon> polygons = Lists.newArrayList();
    polygons.add(new S2Polygon(a));
    polygons.add(new S2Polygon(b));
    S2Polygon cUnion = S2Polygon.union(polygons);
    checkEqual(c, cUnion);
  }

  private static void checkRelationImpl(
      S2Polygon a, S2Polygon b, boolean contains, boolean contained, boolean intersects) {
    assertEquals(contains, a.contains(b));
    assertEquals(contained, b.contains(a));
    assertEquals(intersects, a.intersects(b));
    if (contains) {
      checkNestedPair(a, b);
    }
    if (contained) {
      checkNestedPair(b, a);
    }
    if (!intersects) {
      checkDisjointPair(a, b);
    }
    if (intersects && !(contains | contained)) {
      checkOverlappingPair(a, b);
    }
    checkUnion(a, b);
    checkComplements(a, b);
  }

  private static void checkRelation(
      S2Polygon a, S2Polygon b, boolean contains, boolean contained, boolean intersects) {
    try {
      checkRelationImpl(a, b, contains, contained, intersects);
    } catch (AssertionError e) {
      System.err.println("args " + a + ", " + b);
      throw e;
    }
  }

  public void testRelations() {
    checkRelation(near10, empty, true, false, false);
    checkRelation(near10, near10, true, true, true);
    checkRelation(full, near10, true, false, true);
    checkRelation(near10, near30, false, true, true);
    checkRelation(near10, near32, false, false, false);
    checkRelation(near10, near3210, false, true, true);
    checkRelation(near10, nearH3210, false, false, false);
    checkRelation(near30, near32, true, false, true);
    checkRelation(near30, near3210, true, false, true);
    checkRelation(near30, nearH3210, false, false, true);
    checkRelation(near32, near3210, false, true, true);
    checkRelation(near32, nearH3210, false, false, false);
    checkRelation(near3210, nearH3210, false, false, false);

    checkRelation(far10, far21, false, false, false);
    checkRelation(far10, far321, false, true, true);
    checkRelation(far10, farH20, false, false, false);
    checkRelation(far10, farH3210, false, false, false);
    checkRelation(far21, far321, false, false, false);
    checkRelation(far21, farH20, false, false, false);
    checkRelation(far21, farH3210, false, true, true);
    checkRelation(far321, farH20, false, false, true);
    checkRelation(far321, farH3210, false, false, true);
    checkRelation(farH20, farH3210, false, false, true);

    checkRelation(south0ab, south2, false, true, true);
    checkRelation(south0ab, south210b, false, false, true);
    checkRelation(south0ab, southH21, false, true, true);
    checkRelation(south0ab, southH20abc, false, true, true);
    checkRelation(south2, south210b, true, false, true);
    checkRelation(south2, southH21, false, false, true);
    checkRelation(south2, southH20abc, false, false, true);
    checkRelation(south210b, southH21, false, false, true);
    checkRelation(south210b, southH20abc, false, false, true);
    checkRelation(southH21, southH20abc, true, false, true);

    checkRelation(nf1n10f2s10abc, nf2n2f210s210ab, false, false, true);
    checkRelation(nf1n10f2s10abc, near32, true, false, true);
    checkRelation(nf1n10f2s10abc, far21, false, false, false);
    checkRelation(nf1n10f2s10abc, south0ab, false, false, false);
    checkRelation(nf1n10f2s10abc, f32n0, true, false, true);

    checkRelation(nf2n2f210s210ab, near10, false, false, false);
    checkRelation(nf2n2f210s210ab, far10, true, false, true);
    checkRelation(nf2n2f210s210ab, south210b, true, false, true);
    checkRelation(nf2n2f210s210ab, south0ab, true, false, true);
    checkRelation(nf2n2f210s210ab, n32s0b, true, false, true);

    checkRelation(cross1, cross2, false, false, true);
    checkRelation(cross1SideHole, cross2, false, false, true);
    checkRelation(cross1CenterHole, cross2, false, false, true);
    checkRelation(cross1, cross2SideHole, false, false, true);
    checkRelation(cross1, cross2CenterHole, false, false, true);
    checkRelation(cross1SideHole, cross2SideHole, false, false, true);
    checkRelation(cross1CenterHole, cross2SideHole, false, false, true);
    checkRelation(cross1SideHole, cross2CenterHole, false, false, true);
    checkRelation(cross1CenterHole, cross2CenterHole, false, false, true);

    // These cases, when either polygon has a hole, test a different code path
    // from the other cases.
    checkRelation(overlap1, overlap2, false, false, true);
    checkRelation(overlap1SideHole, overlap2, false, false, true);
    checkRelation(overlap1CenterHole, overlap2, false, false, true);
    checkRelation(overlap1, overlap2SideHole, false, false, true);
    checkRelation(overlap1, overlap2CenterHole, false, false, true);
    checkRelation(overlap1SideHole, overlap2SideHole, false, false, true);
    checkRelation(overlap1CenterHole, overlap2SideHole, false, false, true);
    checkRelation(overlap1SideHole, overlap2CenterHole, false, false, true);
    checkRelation(overlap1CenterHole, overlap2CenterHole, false, false, true);
  }

  public void testUnionSloppySuccess() {
    S2Polygon union = S2Polygon.unionSloppy(Arrays.asList(adj0, adj1), S1Angle.degrees(0.1));

    assertEquals(1, union.numLoops());
    if (union.numLoops() != 1) {
      return;
    }
    S2Loop s2Loop = union.loop(0);
    assertEquals(8, s2Loop.numVertices());
    if (s2Loop.numVertices() != 8) {
      return;
    }
    S2Loop expected = makeLoop("2:0, 1:0, 0:0, 0:1, 0:2, 1:2, 2:2, 2:1");
    double maxError = S1Angle.degrees(0.01000001).radians();
    assertTrue(expected.boundaryApproxEquals(s2Loop, maxError));
  }

  public void testUnionSloppyFailure() {
    // The polygons are sufficiently far apart that this angle will not
    // bring them together:
    S2Polygon union = S2Polygon.unionSloppy(Arrays.asList(adj0, unAdj), S1Angle.degrees(0.1));

    assertEquals(2, union.numLoops());
  }

  public void testCompareTo() {
    // Polygons with same loops, but in different order:
    S2Polygon p1 = makePolygon(RECTANGLE1 + RECTANGLE2);
    S2Polygon p2 = makePolygon(RECTANGLE2 + RECTANGLE1);
    assertEquals(0, p1.compareTo(p2));

    // Polygons with same loops, but in different order and containing a
    // different number of points.
    S2Polygon p3 = makePolygon(RECTANGLE1 + TRIANGLE);
    S2Polygon p4 = makePolygon(TRIANGLE + RECTANGLE1);
    assertEquals(0, p3.compareTo(p4));

    // Polygons with same logical loop (but loop is reordered).
    S2Polygon p5 = makePolygon(TRIANGLE);
    S2Polygon p6 = makePolygon(TRIANGLE_ROT);
    assertEquals(0, p5.compareTo(p6));

    // Polygons with a differing number of loops
    S2Polygon p7 = makePolygon(RECTANGLE1 + RECTANGLE2);
    S2Polygon p8 = makePolygon(TRIANGLE);
    assertTrue(0 > p8.compareTo(p7));
    assertTrue(0 < p7.compareTo(p8));

    // Polygons with a differing number of loops (one a subset of the other)
    S2Polygon p9 = makePolygon(RECTANGLE1 + RECTANGLE2 + TRIANGLE);
    S2Polygon p10 = makePolygon(RECTANGLE1 + RECTANGLE2);
    assertTrue(0 < p9.compareTo(p10));
    assertTrue(0 > p10.compareTo(p9));
  }

  public void testGetDistance() {
    // Error margin since we're doing numerical computations
    double epsilon = 1e-15;

    // A rectangle with (lat,lng) vertices (3,1), (3,-1), (-3,-1) and (-3,1)
    String inner = "3:1, 3:-1, -3:-1, -3:1;";
    // A larger rectangle with (lat,lng) vertices (4,2), (4,-2), (-4,-2) and
    // (-4,s)
    String outer = "4:2, 4:-2, -4:-2, -4:2;";

    S2Polygon rect = makePolygon(inner);
    S2Polygon shell = makePolygon(inner + outer);

    // All of the vertices of a polygon should be distance 0
    for (int i = 0; i < shell.numLoops(); i++) {
      for (int j = 0; j < shell.loop(i).numVertices(); j++) {
        assertEquals(0d, shell.getDistance(shell.loop(i).vertex(j)).radians(), epsilon);
      }
    }

    // A non-vertex point on an edge should be distance 0
    assertEquals(
        0d,
        rect.getDistance(
                S2Point.normalize(S2Point.add(rect.loop(0).vertex(0), rect.loop(0).vertex(1))))
            .radians(),
        epsilon);

    S2Point origin = S2LatLng.fromDegrees(0, 0).toPoint();
    // rect contains the origin
    assertEquals(0d, rect.getDistance(origin).radians(), epsilon);

    // shell does NOT contain the origin, since it has a hole. The shortest
    // distance is to (1,0) or (-1,0), and should be 1 degree
    assertEquals(1d, shell.getDistance(origin).degrees(), epsilon);
  }

  public void testMultipleInit() {
    S2Polygon polygon = makePolygon("0:0, 0:2, 2:0");
    assertEquals(1, polygon.numLoops());
    assertEquals(3, polygon.getNumVertices());
    S2LatLngRect bound1 = polygon.getRectBound();

    List<S2Loop> loops = Lists.newArrayList();
    loops.add(makeLoop("10:0, -10:-20, -10:20"));
    loops.add(makeLoop("40:30, 20:10, 20:50"));
    polygon.initNested(loops);
    assertTrue(polygon.isValid());
    assertTrue(loops.isEmpty());
    assertEquals(2, polygon.numLoops());
    assertEquals(6, polygon.getNumVertices());
    assertTrue(!bound1.equals(polygon.getRectBound()));
  }

  public void testProject() {
    S2Polygon polygon = makeVerbatimPolygon(NEAR0 + NEAR2);
    double epsilon = 1e-15;
    S2Point point;
    S2Point projected;

    // The point inside the polygon should be projected into itself.
    point = makePoint("1.1:0");
    projected = polygon.project(point);
    assertTrue(point.aequal(projected, epsilon));

    // The point is on the outside of the polygon.
    point = makePoint("5.1:-2");
    projected = polygon.project(point);
    assertTrue(makePoint("5:-2").aequal(projected, epsilon));

    // The point is inside the hole in the polygon. Note the expected value
    // is based on a plane, so it's not that accurate; thus, tolerance is
    // reduced to 1e-6.
    point = makePoint("-0.49:-0.49");
    projected = polygon.project(point);
    assertTrue(makePoint("-0.5:-0.5").aequal(projected, 1e-6));

    point = makePoint("0:-3");
    projected = polygon.project(point);
    assertTrue(makePoint("0:-2").aequal(projected, epsilon));
  }

  public void testProjectMatchesDistance() {
    S2Polygon polygon = makePolygon(NEAR0 + NEAR2);
    double epsilon = 1e-15;
    S2Point point;
    S2Point projected;

    // In the hole
    point = makePoint("-0.49:-0.49");
    projected = polygon.project(point);
    assertEquals(polygon.getDistance(point).radians(), point.angle(projected), epsilon);

    // Outside the polygon
    point = makePoint("10:15");
    projected = polygon.project(point);
    assertEquals(polygon.getDistance(point).radians(), point.angle(projected), epsilon);
  }

  public void testFastInit() {
    S2LatLngRect bound = null;
    Map<S2Loop, List<S2Loop>> nestedLoops = Maps.newHashMap();
    nestedLoops.put(null, Lists.<S2Loop>newArrayList());

    List<S2Point> vertices = Lists.newArrayList();

    bound = parseVertices("-2:-2, -3:6, 6:-3", vertices);
    S2Loop loop1 = S2Loop.newLoopWithTrustedDetails(vertices, false, bound);
    nestedLoops.get(null).add(loop1);
    nestedLoops.put(loop1, Lists.<S2Loop>newArrayList());

    vertices = Lists.newArrayList();
    bound = parseVertices("-1:-2, -2:5, 5:-2", vertices);
    S2Loop loop2 = S2Loop.newLoopWithTrustedDetails(vertices, false, bound);
    nestedLoops.get(loop1).add(loop2);
    nestedLoops.put(loop2, Lists.<S2Loop>newArrayList());

    vertices = Lists.newArrayList();
    bound = parseVertices("-1:0, 0:1, 1:0, 0:-1", vertices);
    S2Loop loop3 = S2Loop.newLoopWithTrustedDetails(vertices, false, bound);
    nestedLoops.get(loop2).add(loop3);
    nestedLoops.put(loop3, Lists.<S2Loop>newArrayList());

    S2Polygon polygon = new S2Polygon();
    polygon.initWithNestedLoops(nestedLoops);

    assertEquals(0, polygon.compareTo(makePolygon(NEAR0 + NEAR2 + NEAR3)));

    assertTrue(polygon.isValid());
    assertEquals(0, polygon.loop(0).depth());
    assertEquals(1, polygon.loop(1).depth());
    assertEquals(2, polygon.loop(2).depth());
    assertDoubleNear(0.003821967440517272, polygon.getArea());
  }

  public void testInitToSnappedWithSnapLevel() {
    S2Polygon polygon = makePolygon("0:0, 0:2, 2:0; 0:0, 0:-2, -2:-2, -2:0");

    for (int level = 0; level <= S2CellId.MAX_LEVEL; level++) {
      S2Polygon snappedPolygon = new S2Polygon();
      snappedPolygon.initToSnapped(polygon, level);
      assertTrue(snappedPolygon.isValid());
      double cellAngle = PROJ.maxDiag.getValue(level);
      S1Angle mergeRadius = S1Angle.radians(cellAngle);
      assertTrue(
          "snapped polygon should approx contain original polygon for"
              + "\nsnap level = "
              + level
              + ", mergeRadius = "
              + mergeRadius
              + "\noriginal polygon: "
              + polygon
              + "\nsnapped polygon: "
              + snappedPolygon,
          snappedPolygon.approxContains(polygon, mergeRadius));
    }
  }

  public void testGetBestSnapLevel() {
    // Unsnapped polygon.
    assertEquals(-1, makePolygon("10:10, 30:20, 20:30").getBestSnapLevel());
    // Polygons snapped at particular levels.
    assertEquals(20, makePolygon("10:10, 30:20, 20:30", 20).getBestSnapLevel());
    assertEquals(
        S2CellId.MAX_LEVEL,
        makePolygon("10:10, 30:20, 20:30", S2CellId.MAX_LEVEL).getBestSnapLevel());
    // Mixed snap levels for different loops.
    List<S2Loop> loops = Lists.newArrayList();
    loops.add(makeLoop("10:10, 30:20, 20:30", 10));
    loops.add(makeLoop("60:60, 80:70, 70:80", 12));
    assertEquals(10, new S2Polygon(loops).getBestSnapLevel());
  }

  public void testInitToSimplifiedInCellPointsOnCellBoundaryKept() {
    S2CellId cellId = S2CellId.fromToken("89c25c");
    S2Cell cell = new S2Cell(cellId);
    S2Polygon loop = new S2Polygon(makeCellLoop(cell, "0.1:0, 0.2:0, 0.2:0.5"));
    S1Angle tolerance = S1Angle.radians(loop.loop(0).vertex(0).angle(loop.loop(0).vertex(1)) * 1.1);
    S2Polygon simplifiedLoop = new S2Polygon();
    simplifiedLoop.initToSimplified(loop, tolerance, false);
    assertTrue(simplifiedLoop.isEmpty());
    S2Polygon simplifiedLoopInCell = new S2Polygon();
    simplifiedLoopInCell.initToSimplifiedInCell(loop, cell, tolerance);
    assertTrue(loop.boundaryApproxEquals(simplifiedLoopInCell, tolerance.radians()));
    assertEquals(3, simplifiedLoopInCell.getNumVertices());
    assertEquals(-1, simplifiedLoop.getSnapLevel());
  }

  public void testInitToSimplifiedInCellPointsInsideCellSimplified() {
    S2CellId cellId = S2CellId.fromToken("89c25c");
    S2Cell cell = new S2Cell(cellId);
    S2Polygon loop = new S2Polygon(makeCellLoop(cell, "0.1:0, 0.2:0, 0.2:0.5, 0.2:0.8"));
    S1Angle tolerance = S1Angle.radians(loop.loop(0).vertex(0).angle(loop.loop(0).vertex(1)) * 1.1);
    S2Polygon simplifiedLoop = new S2Polygon();
    simplifiedLoop.initToSimplifiedInCell(loop, cell, tolerance);
    assertTrue(loop.boundaryNear(simplifiedLoop, 1e-15));
    assertEquals(3, simplifiedLoop.getNumVertices());
    assertEquals(-1, simplifiedLoop.getSnapLevel());
  }

  public void testGetLoops() {
    S2Loop loopA = makeLoop("0:30, 0:34, 0:36, 0:39, 0:41, 0:44, 30:44, 30:30");
    S2Loop loopB = makeLoop("0:30, -30:30, -30:44, 0:44, 0:41, 0:39, 0:36, 0:34");
    List<S2Loop> expected = Arrays.asList(loopA, loopB);
    List<S2Loop> actual = new S2Polygon(new ArrayList<>(expected)).getLoops();
    assertEquals(2, actual.size());
    assertTrue(actual.contains(loopA));
    assertTrue(actual.contains(loopB));
  }

  /**
   * Creates a loop from a comma separated list of u:v coordinates relative to a cell. The loop
   * "0:0, 1:0, 1:1, 0:1" is counter-clockwise.
   */
  private static S2Polygon makeCellLoop(S2Cell cell, String str) {
    S2Point[] vertices = {
      cell.getVertex(0), cell.getVertex(1), cell.getVertex(2), cell.getVertex(3)
    };
    List<S2Point> loopVertices = Lists.newArrayList();
    for (String p : Splitter.on(",").split(str)) {
      Iterator<String> coords = Splitter.on(":").trimResults().split(p).iterator();
      double u = Double.parseDouble(coords.next());
      double v = Double.parseDouble(coords.next());
      assertFalse(coords.hasNext());
      loopVertices.add(
          S2Point.normalize(
              interp(v, interp(u, vertices[0], vertices[1]), interp(u, vertices[2], vertices[3]))));
    }
    return new S2Polygon(new S2Loop(loopVertices));
  }

  private static S2Point interp(double t, S2Point a, S2Point b) {
    return S2Point.add(S2Point.mul(a, 1 - t), S2Point.mul(b, t));
  }

  /**
   * Verifies that clipBoundary can succeed with duplicate adjacent vertices. Although such a case
   * means the polygon is invalid, it is common to fix invalidity issues by doing a self-
   * intersection to node crossings and drop duplicates.
   */
  public void testDuplicatePointClipping() {
    S2Polygon p = makePolygon("0:0, 0:0, 0:4, 4:4, 4:0");
    S2Polygon fixed = new S2Polygon();
    fixed.initToIntersection(p, p);
    assertEquals(makePolygon("0:0, 0:4, 4:4, 4:0"), fixed);
  }

  // Create 'numLoops' nested regular loops around a common center point. All loops have the same
  // number of vertices (at least 'minVertices'). Furthermore, the vertices at the same index
  // position are collinear with the common center point of all the loops. The loop radii decrease
  // exponentially in order to prevent accidental loop crossings when one of the loops is
  // modified.
  private List<List<S2Point>> getConcentricLoops(int numLoops, int minVertices) {
    List<List<S2Point>> loops = Lists.newArrayList();
    // Radii decrease exponentially.
    assert (numLoops <= 10);
    S2Point center = randomPoint();
    int numVertices = minVertices + random(10);
    for (int i = 0; i < numLoops; ++i) {
      S1Angle radius = S1Angle.degrees(80 * Math.pow(0.1, i));
      S2Loop loop = S2Loop.makeRegularLoop(center, radius, numVertices);
      List<S2Point> loopPoints = Lists.newArrayList();
      for (int j = 0; j < loop.numVertices(); ++j) {
        loopPoints.add(loop.vertex(j));
      }
      loops.add(loopPoints);
    }
    return loops;
  }

  public void testSplitting() {
    // It takes too long to test all the polygons in debug mode, so we just pick out some of the
    // more interesting ones.
    splitAndAssemble(near10);
    splitAndAssemble(nearH3210);
    splitAndAssemble(south0ab);
    splitAndAssemble(south210b);
    splitAndAssemble(nf1n10f2s10abc);
    splitAndAssemble(nf2n2f210s210ab);
    splitAndAssemble(farHSouthH);

    // TODO(eengle): Fix the remaining tests. This was confirmed to be a problem before the
    // S2ShapeIndex was used.
    // splitAndAssemble(southH);
    // splitAndAssemble(farH);
    // splitAndAssemble(southH20abc);
    // splitAndAssemble(farH3210);
  }

  private void splitAndAssemble(S2Polygon polygon) {
    S2PolygonBuilder builder = new S2PolygonBuilder(S2PolygonBuilder.Options.DIRECTED_XOR);
    S2Polygon expected = new S2Polygon();
    builder.addPolygon(polygon);
    assertTrue(builder.assemblePolygon(expected, null));

    for (int iter = 0; iter < 10; ++iter) {
      // Compute the minimum level such that the polygon's bounding cap is guaranteed to be cut.
      double diameter = 2 * polygon.getCapBound().angle().radians();
      int minLevel = PROJ.maxDiag.getMinLevel(diameter);

      // TODO(user): Choose a level that will have up to 256 cells in the covering.
      int level = minLevel + random(4);
      S2RegionCoverer coverer =
          S2RegionCoverer.builder()
              .setMinLevel(minLevel)
              .setMaxLevel(level)
              .setMaxCells(500)
              .build();

      ArrayList<S2CellId> cells = Lists.newArrayList();
      coverer.getCovering(polygon, cells);
      S2CellUnion covering = new S2CellUnion();
      covering.initFromCellIds(cells);
      checkCovering(polygon, covering, false);
      checkCoveringIsConservative(polygon, cells);
      logger.info(cells.size() + " cells in covering");
      List<S2Polygon> pieces = Lists.newArrayList();
      for (int i = 0; i < cells.size(); ++i) {
        S2Cell cell = new S2Cell(cells.get(i));
        S2Polygon window = new S2Polygon(cell);
        S2Polygon piece = new S2Polygon();
        piece.initToIntersection(polygon, window);
        pieces.add(piece);
        logger.info(
            "\nPiece " + i + ":\n Window: " + window.toString() + "\n Piece: " + piece.toString());
      }

      // Now we repeatedly remove two random pieces, compute their union, and insert the result as a
      // new piece until only one piece is left.
      //
      // We don't use S2Polygon.union() because it joins the pieces in a mostly
      // deterministic order. We don't just randomly shuffle the pieces and repeatedly join the
      // last two pieces because this always joins a single original piece to the current union
      // rather than doing the unions according to some random tree structure.
      while (pieces.size() > 1) {
        S2Polygon a = choosePiece(pieces);
        S2Polygon b = choosePiece(pieces);
        S2Polygon c = new S2Polygon();
        c.initToUnion(a, b);
        pieces.add(c);
        logger.info(
            "\nJoining piece a: "
                + a.toString()
                + "\n With piece b: "
                + b.toString()
                + "\n To get piece c: "
                + c.toString()
                + " at iteration "
                + iter);
      }
      S2Polygon result = pieces.get(0);

      // The moment of truth!
      assertTrue(
          "\nActual:\n" + result.toString() + "\nExpected:\n" + expected.toString(),
          expected.boundaryNear(result, 1e-15));
    }
  }

  /** Removes a random polygon from {@code pieces} and returns it. */
  private S2Polygon choosePiece(List<S2Polygon> pieces) {
    int i = random(pieces.size());
    S2Polygon result = pieces.get(i);
    pieces.remove(i);
    return result;
  }

  /**
   * Checks that contains(S2Cell) and mayIntersect(S2Cell) are implemented conservatively, by
   * comparing against the contains/intersect result with the 'cell polygon' defined by the four
   * cell vertices. Please note that the cell polygon is *not* an exact representation of the
   * S2Cell: cell vertices are rounded from their true mathematical positions, which leads to tiny
   * cracks and overlaps between the cell polygons at different cell levels. That is why
   * contains(S2Cell) and mayIntersect(S2Cell) cannot be implemented by simply converting the cell
   * to an S2Polygon. But it is still useful to do this sanity check. In particular:
   *
   * <ul>
   *   <li>If contains(cell) is true, the polygon must contain the cell polygon.
   *   <li>If the polygon intersects the cell polygon, then mayIntersect(cell) must return true.
   * </ul>
   */
  private static void checkCoveringIsConservative(S2Polygon polygon, List<S2CellId> cells) {
    for (int i = 0; i < cells.size(); ++i) {
      S2Cell cell = new S2Cell(cells.get(i));
      S2Polygon cellPoly = new S2Polygon(cell);
      if (polygon.contains(cell)) {
        assertTrue(polygon.contains(cellPoly));
      }
      if (polygon.intersects(cellPoly)) {
        assertTrue(polygon.mayIntersect(cell));
      }
    }
  }

  /** Checks that the index is properly deserialized. */
  @GwtIncompatible("Object serialization")
  public void testIndexDeserialization() throws IOException, ClassNotFoundException {
    S2Point center = S2Point.X_POS;
    S1Angle angle = S1Angle.radians(10);
    int numVertices = 10;
    S2Polygon polygon = new S2Polygon(S2Loop.makeRegularLoop(center, angle, numVertices));
    // Initialize the index.
    polygon.index.iterator();
    ByteArrayOutputStream output = new ByteArrayOutputStream();
    java.io.ObjectOutputStream out = new java.io.ObjectOutputStream(output);

    // Serialize the object.
    out.writeObject(polygon);
    out.close();

    // Deserialize the object.
    S2Polygon copy = new S2Polygon();
    ByteArrayInputStream input = new ByteArrayInputStream(output.toByteArray());
    java.io.ObjectInputStream in = new java.io.ObjectInputStream(input);
    copy = (S2Polygon) in.readObject();
    in.close();

    assertTrue(copy.index != null);
    assertTrue(copy.index.iterator().locate(S2Point.X_POS));
    assertTrue(copy.index.shapes.size() == 1);
    assertTrue(copy.getNumVertices() == numVertices);
    assertTrue(copy.contains(S2Point.X_POS));
  }

  public void testEmptyPolygonShape() {
    S2Polygon.Shape shape = empty.shape();
    assertEquals(empty, shape.polygon());
    assertTrue(shape.hasInterior());
    assertFalse(shape.containsOrigin());
    assertEquals(0, shape.numEdges());
    assertEquals(0, shape.numChains());
    assertEquals(2, shape.dimension());
  }

  public void testFullPolygonShape() {
    S2Polygon.Shape shape = full.shape();
    assertEquals(full, shape.polygon());
    assertTrue(shape.hasInterior());
    assertTrue(shape.containsOrigin());
    assertEquals(0, shape.numEdges());
    assertEquals(1, shape.numChains());
    checkFirstNChainStarts(shape, 0);
    checkFirstNChainLengths(shape, 0);
    assertEquals(2, shape.dimension());
  }

  public void testOneLoopPolygonShape() {
    S2Polygon.Shape shape = near0.shape();
    assertEquals(near0, shape.polygon());
    assertTrue(shape.hasInterior());
    assertFalse(shape.containsOrigin());
    assertEquals(4, shape.numEdges());
    checkGetEdge(near0, shape);
    assertEquals(1, shape.numChains());
    checkFirstNChainStarts(shape, 0);
    checkFirstNChainLengths(shape, 4);
    assertEquals(2, shape.dimension());
  }

  public void testSeveralLoopPolygonShape() {
    S2Polygon.Shape shape = near3210.shape();
    assertEquals(near3210, shape.polygon());
    assertTrue(shape.hasInterior());
    assertFalse(shape.containsOrigin());
    assertEquals(18, shape.numEdges());
    checkGetEdge(near3210, shape);
    assertEquals(4, shape.numChains());
    checkFirstNChainStarts(shape, 0, 3, 6, 14);
    checkFirstNChainLengths(shape, 3, 3, 8, 4);
    assertEquals(2, shape.dimension());
  }

  public void testManyLoopPolygonShape() {
    int numLoops = 100;
    int numVerticesPerLoop = 6;
    S2Polygon polygon = concentricLoopsPolygon(S2Point.X_POS, numLoops, numVerticesPerLoop);
    S2Polygon.Shape shape = polygon.shape();
    assertEquals(polygon, shape.polygon());
    assertTrue(shape.hasInterior());
    assertFalse(shape.containsOrigin());
    assertEquals(600, shape.numEdges());
    checkGetEdge(polygon, shape);
    assertEquals(100, shape.numChains());
    checkFirstNChainStarts(shape, 0, 6, 12, 18, 24);
    checkFirstNChainLengths(shape, 6, 6, 6, 6, 6);
    assertEquals(2, shape.dimension());
  }

  private static void checkGetEdge(S2Polygon polygon, S2Shape shape) {
    MutableEdge edge = new MutableEdge();
    for (int e = 0, i = 0; i < polygon.numLoops(); ++i) {
      S2Loop loop = polygon.loop(i);
      for (int j = 0; j < loop.numVertices(); ++j, ++e) {
        if (!loop.isEmptyOrFull()) {
          shape.getEdge(e, edge);
          assertEquals(loop.orientedVertex(j), edge.a);
          assertEquals(loop.orientedVertex(j + 1), edge.b);
          shape.getChainEdge(i, j, edge);
          assertEquals(loop.orientedVertex(j), edge.a);
          assertEquals(loop.orientedVertex(j + 1), edge.b);
        }
      }
    }
  }

  public void testInitRecursion() {
    String loop = "-18.84:-40.96, -18.93:-40.96, -18.93:-40.86, -18.84:-40.86";
    assertFalse(makePolygon(loop + "; " + loop).isValid());
  }

  /** Verifies a bug in S2ShapeIndex has been fixed. */
  public void testPointInBigLoop() {
    S2LatLng center = S2LatLng.fromRadians(0.3, 2);
    S2Loop loop = S2Loop.makeRegularLoop(center.toPoint(), S1Angle.degrees(80), 10);
    assertTrue(new S2Polygon(loop).mayIntersect(new S2Cell(S2CellId.fromLatLng(center))));
  }

  /** Verifies S2Polygon.getCentroid() can handle polygons with empty loops. */
  public void testEmptyLoopCentroid() {
    S2Polygon polygon = new S2Polygon(S2Loop.empty());
    assertEquals(polygon.getCentroid(), S2Point.ORIGIN);
  }

  public void testEncodeDecodeLossless() throws Exception {
    S2Polygon polygon = makePolygon("1:2, 3:4, 5:6; 7:8, 9:10, 11:12");

    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    LittleEndianOutput encoder = new LittleEndianOutput(baos);
    encoder.writeByte((byte) 1);

    // These two bytes are ignored. Respectively encoding that owns loop is false and there are
    // no holes.
    encoder.writeByte((byte) 0);
    encoder.writeByte((byte) 0);

    encoder.writeInt(2);
    polygon.loop(0).encode(encoder);
    polygon.loop(1).encode(encoder);
    polygon.getRectBound().encode(encoder);

    ByteArrayInputStream in = new ByteArrayInputStream(baos.toByteArray());
    S2Polygon decodedPolygon = S2Polygon.decode(in);
    assertEquals(polygon, decodedPolygon);
    assertEquals(0, in.available());
  }

  public void testDecodeEncodeSingleLoopLossless() throws IOException {
    String encodedBytesHexString =
        "010100010000000108000000D44A8442C3F9EF3F7EDA2AB341DC913F27DCF7C958DEA1BFB4"
            + "825F3C81FDEF3F27DCF7C958DE913F1EDD892B0BDF91BFB4825F3C81FDEF3F27DCF7C958DE"
            + "913F1EDD892B0BDF913FD44A8442C3F9EF3F7EDA2AB341DC913F27DCF7C958DEA13FD44A84"
            + "42C3F9EF3F7EDA2AB341DC91BF27DCF7C958DEA13FB4825F3C81FDEF3F27DCF7C958DE91BF"
            + "1EDD892B0BDF913FB4825F3C81FDEF3F27DCF7C958DE91BF1EDD892B0BDF91BFD44A8442C3"
            + "F9EF3F7EDA2AB341DC91BF27DCF7C958DEA1BF0000000000013EFC10E8F8DFA1BF3EFC10E8"
            + "F8DFA13F389D52A246DF91BF389D52A246DF913F013EFC10E8F8DFA1BF3EFC10E8F8DFA13F"
            + "389D52A246DF91BF389D52A246DF913F";
    ByteArrayInputStream bais =
        new ByteArrayInputStream(BaseEncoding.base16().decode(encodedBytesHexString));
    S2Polygon decodedPolygon = S2Polygon.decode(bais);
    assertEquals(1, decodedPolygon.numLoops());
    assertEquals(
        makeLoop("-2:1, -1:1, 1:1, 2:1, 2:-1, 1:-1, -1:-1, -2:-1"), decodedPolygon.loop(0));
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    decodedPolygon.encode(bos);
    assertEquals(encodedBytesHexString, BaseEncoding.base16().encode(bos.toByteArray()));
  }

  public void testEncodeDecodeEmptyLossless() throws IOException {
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    new S2Polygon().encode(bos);
    assertEquals("041E00", BaseEncoding.base16().encode(bos.toByteArray()));
  }

  public void testEncodeDecodeCorruptLossless() {
    try {
      S2Polygon.decode(new ByteArrayInputStream(BaseEncoding.base16().decode("0101FFFFFF")));
      fail();
    } catch (IOException e) {
      assertEquals("EOF", e.getMessage());
    }
  }

  public void testEncodeDecodeTwoPolygons() throws IOException {
    S2Polygon p1 = makePolygon("10:10,10:0,0:0");
    S2Polygon p2 = makePolygon("0:0,1:0,1:1,0:1");
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    p1.encode(out);
    p2.encode(out);

    byte[] data = out.toByteArray();
    ByteArrayInputStream in = new ByteArrayInputStream(data);
    S2Polygon p1Decoded = S2Polygon.decode(in);
    S2Polygon p2Decoded = S2Polygon.decode(in);
    assertEquals(p1, p1Decoded);
    assertEquals(p2, p2Decoded);
    assertEquals(0, in.available());
  }

  public void testDecodeEncodeTwoLoopsLossless() throws IOException {
    String encodedBytesHexString =
        "010101020000000108000000D44A8442C3F9EF3F7EDA2AB341DC913F27DCF7C958DEA1BFB4"
            + "825F3C81FDEF3F27DCF7C958DE913F1EDD892B0BDF91BFB4825F3C81FDEF3F27DCF7C958DE"
            + "913F1EDD892B0BDF913FD44A8442C3F9EF3F7EDA2AB341DC913F27DCF7C958DEA13FD44A84"
            + "42C3F9EF3F7EDA2AB341DC91BF27DCF7C958DEA13FB4825F3C81FDEF3F27DCF7C958DE91BF"
            + "1EDD892B0BDF913FB4825F3C81FDEF3F27DCF7C958DE91BF1EDD892B0BDF91BFD44A8442C3"
            + "F9EF3F7EDA2AB341DC91BF27DCF7C958DEA1BF0000000000013EFC10E8F8DFA1BF3EFC10E8"
            + "F8DFA13F389D52A246DF91BF389D52A246DF913F0104000000C5D7FA4B60FFEF3F1EDD892B"
            + "0BDF813F214C95C437DF81BFC5D7FA4B60FFEF3F1EDD892B0BDF813F214C95C437DF813FC5"
            + "D7FA4B60FFEF3F1EDD892B0BDF81BF214C95C437DF813FC5D7FA4B60FFEF3F1EDD892B0BDF"
            + "81BF214C95C437DF81BF000100000001900C5E3B73DF81BF900C5E3B73DF813F399D52A246"
            + "DF81BF399D52A246DF813F013EFC10E8F8DFA1BF3EFC10E8F8DFA13F389D52A246DF91BF38"
            + "9D52A246DF913F";
    ByteArrayInputStream bais =
        new ByteArrayInputStream(BaseEncoding.base16().decode(encodedBytesHexString));
    S2Polygon decodedPolygon = S2Polygon.decode(bais);
    assertEquals(2, decodedPolygon.numLoops());
    assertEquals(
        makeLoop("-2:1, -1:1, 1:1, 2:1, 2:-1, 1:-1, -1:-1, -2:-1"), decodedPolygon.loop(0));
    assertEquals(makeLoop("-0.5:0.5, 0.5:0.5, 0.5:-0.5, -0.5:-0.5"), decodedPolygon.loop(1));
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    decodedPolygon.encode(bos);
    assertEquals(encodedBytesHexString, BaseEncoding.base16().encode(bos.toByteArray()));
  }
}
