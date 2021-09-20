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

import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Tests for {@link S2Polyline}.
 *
 */
@GwtCompatible
public strictfp class S2PolylineTest extends GeometryTestCase {

  private static final double EPSILON = 2e-14d;

  @Override
  public void setUp() {
    super.setUp();
  }

  public void testBasic() {
    List<S2Point> vertices = Lists.newArrayList();
    S2Polyline empty = new S2Polyline(vertices);
    assertEquals(empty.getRectBound(), S2LatLngRect.empty());
  }

  public void testGetLengthCentroid() {
    // Construct random great circles and divide them randomly into segments.
    // Then make sure that the length and centroid are correct. Note that
    // because of the way the centroid is computed, it does not matter how
    // we split the great circle into segments.

    for (int i = 0; i < 100; ++i) {
      // Choose a coordinate frame for the great circle.
      S2Point x = randomPoint();
      S2Point y = S2Point.normalize(S2Point.crossProd(x, randomPoint()));

      List<S2Point> vertices = Lists.newArrayList();
      for (double theta = 0; theta < 2 * S2.M_PI; theta += Math.pow(rand.nextDouble(), 10)) {
        S2Point p = S2Point.add(S2Point.mul(x, Math.cos(theta)), S2Point.mul(y, Math.sin(theta)));
        if (vertices.isEmpty() || !p.equals(vertices.get(vertices.size() - 1))) {
          vertices.add(p);
        }
      }
      // Close the circle.
      vertices.add(vertices.get(0));
      S2Polyline line = new S2Polyline(vertices);
      S1Angle length = line.getArclengthAngle();
      assertTrue(Math.abs(length.radians() - 2 * S2.M_PI) < EPSILON);
    }
  }

  public void testMayIntersect() {
    List<S2Point> vertices = Lists.newArrayList();
    vertices.add(S2Point.normalize(new S2Point(1, -1.1, 0.8)));
    vertices.add(S2Point.normalize(new S2Point(1, -0.8, 1.1)));
    S2Polyline line = new S2Polyline(vertices);
    for (int face = 0; face < 6; ++face) {
      S2Cell cell = S2Cell.fromFace(face);
      assertEquals(line.mayIntersect(cell), (face & 1) == 0);
    }
  }

  public void testInterpolate() {
    List<S2Point> vertices = Lists.newArrayList();
    vertices.add(new S2Point(1, 0, 0));
    vertices.add(new S2Point(0, 1, 0));
    vertices.add(S2Point.normalize(new S2Point(0, 1, 1)));
    vertices.add(new S2Point(0, 0, 1));
    S2Polyline line = new S2Polyline(vertices);

    assertEquals(line.interpolate(-0.1), vertices.get(0));
    assertTrue(
        S2.approxEquals(
            line.interpolate(0.1),
            S2Point.normalize(new S2Point(1, Math.tan(0.2 * S2.M_PI / 2), 0))));
    assertTrue(S2.approxEquals(line.interpolate(0.25), S2Point.normalize(new S2Point(1, 1, 0))));

    assertEquals(line.interpolate(0.5), vertices.get(1));
    assertEquals(line.interpolate(0.75), vertices.get(2));
    assertEquals(line.interpolate(1.1), vertices.get(3));
  }

  public void testUninterpolate() {
    S2Point pointA = new S2Point(1, 0, 0);
    S2Point pointB = new S2Point(0, 1, 0);
    S2Point pointC = new S2Point(0, 0, 1);
    S2Polyline line = new S2Polyline(ImmutableList.of(pointA, pointB, pointC));

    // Test at vertices
    assertEquals(line.uninterpolate(pointA), 0d, EPSILON);
    assertEquals(line.uninterpolate(pointB), 0.5d, EPSILON);
    assertEquals(line.uninterpolate(pointC), 1d, EPSILON);

    // Test at non-vertex points on the line
    final int steps = 7; // Not a power of two, to test at fractions w/o exact value in double
    for (int i = 1; i < steps; i++) {
      double fraction = i / (double) steps;
      S2Point interpolatedPoint = line.interpolate(fraction);
      assertEquals(line.uninterpolate(interpolatedPoint), fraction, EPSILON);
    }

    // Test at a point off the line such that the unique nearest point is a vertex
    S2Point pointOffFromB = S2Point.normalize(new S2Point(-0.001, 1, -0.001));
    assertEquals(line.uninterpolate(pointOffFromB), 0.5d, EPSILON);

    // Test at a point off the line such that the unique nearest point is a not vertex
    S2Point pointOffFromMidAB = S2Point.normalize(new S2Point(1, 1, -0.001));
    assertEquals(line.uninterpolate(pointOffFromMidAB), 0.25d, EPSILON);

    // Test at a point off the line such that there are two nearest points
    S2Point pointEquidistantFromABAndBC = S2Point.normalize(new S2Point(1, 1, 1));
    double fraction = line.uninterpolate(pointEquidistantFromABAndBC);
    assertTrue(
        (0.25 - EPSILON < fraction && fraction < 0.25 + EPSILON)
            || (0.75 - EPSILON < fraction && fraction < 0.75 + EPSILON));
  }

  public void testEqualsAndHashCode() {
    List<S2Point> vertices = Lists.newArrayList();
    vertices.add(new S2Point(1, 0, 0));
    vertices.add(new S2Point(0, 1, 0));
    vertices.add(S2Point.normalize(new S2Point(0, 1, 1)));
    vertices.add(new S2Point(0, 0, 1));

    S2Polyline line1 = new S2Polyline(vertices);
    S2Polyline line2 = new S2Polyline(vertices);

    checkEqualsAndHashCodeMethods(line1, line2, true);

    List<S2Point> moreVertices = Lists.newLinkedList(vertices);
    moreVertices.remove(0);

    S2Polyline line3 = new S2Polyline(moreVertices);

    checkEqualsAndHashCodeMethods(line1, line3, false);
    checkEqualsAndHashCodeMethods(line1, null, false);
    checkEqualsAndHashCodeMethods(line1, "", false);
  }

  public void testEncodeDecode() throws IOException {
    S2Polyline polyline = makePolyline("0:0, 0:10, 10:20, 20:30");
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    polyline.encode(bos);
    assertEquals(polyline, S2Polyline.decode(new ByteArrayInputStream(bos.toByteArray())));
  }

  public void testEncodeDecodeCompressed() throws IOException {
    List<S2Point> vertices = Lists.newArrayList();
    parseVertices("0:0, 0:10, 10:20, 20:30, 30:50, 40:70", vertices);
    S2Polyline polyline = new S2Polyline(snapPointsToLevel(vertices, 20));
    ByteArrayOutputStream compact = new ByteArrayOutputStream();
    ByteArrayOutputStream lossless = new ByteArrayOutputStream();
    polyline.encodeCompact(compact);
    polyline.encode(lossless);
    assertTrue(compact.size() < lossless.size());
    assertEquals(polyline, S2Polyline.decode(new ByteArrayInputStream(compact.toByteArray())));
  }

  public void testGetSnapLevel() {
    List<S2Point> vertices = Lists.newArrayList();
    parseVertices("0:10, 10:20, 20:30", vertices);
    // Unsnapped polyline.
    S2Polyline polyline = new S2Polyline(vertices);
    assertEquals(-1, polyline.getSnapLevel());
    assertEquals(-1, polyline.getBestSnapLevel());

    // Points snapped to the same level.
    polyline = new S2Polyline(snapPointsToLevel(vertices, S2CellId.MAX_LEVEL));
    assertEquals(S2CellId.MAX_LEVEL, polyline.getSnapLevel());
    assertEquals(S2CellId.MAX_LEVEL, polyline.getBestSnapLevel());

    // Points snapped to different levels.
    polyline =
        new S2Polyline(
            ImmutableList.copyOf(
                Iterables.concat(
                    snapPointsToLevel(vertices, 10),
                    snapPointsToLevel(vertices, 11),
                    snapPointsToLevel(vertices, 12))));
    assertEquals(-1, polyline.getSnapLevel());
    assertEquals(10, polyline.getBestSnapLevel());
  }

  public void testFromSnapped() {
    S2Polyline original = makePolyline("10:10, 10:20, 10:30, 10:15, 10:40");
    S2Polyline snapped = S2Polyline.fromSnapped(original, S2CellId.MAX_LEVEL);
    assertEquals(original.numVertices(), snapped.numVertices());
    assertEquals(S2CellId.MAX_LEVEL, snapped.getSnapLevel());
    assertEquals(-1, original.getSnapLevel());
    for (int i = 0; i < original.numVertices(); ++i) {
      assertTrue(S2.approxEquals(original.vertex(i), snapped.vertex(i), 1e-9));
    }

    // Snap to a very small level and ensure that vertices are deduplicated.
    assertTrue(original.numVertices() > S2Polyline.fromSnapped(original, 2).numVertices());
  }

  public void testGetNearestEdgeIndexAndProjectToEdge() {
    List<S2Point> latLngs = Lists.newArrayList();
    latLngs.add(S2LatLng.fromDegrees(0, 0).toPoint());
    latLngs.add(S2LatLng.fromDegrees(0, 1).toPoint());
    latLngs.add(S2LatLng.fromDegrees(0, 2).toPoint());
    latLngs.add(S2LatLng.fromDegrees(1, 2).toPoint());
    S2Polyline line = new S2Polyline(latLngs);

    int edgeIndex = -1;
    S2Point testPoint = null;

    testPoint = S2LatLng.fromDegrees(0.5, -0.5).toPoint();
    edgeIndex = line.getNearestEdgeIndex(testPoint);
    assertTrue(
        S2.approxEquals(
            line.projectToEdge(testPoint, edgeIndex), S2LatLng.fromDegrees(0, 0).toPoint()));
    assertEquals(0, edgeIndex);

    testPoint = S2LatLng.fromDegrees(0.5, 0.5).toPoint();
    edgeIndex = line.getNearestEdgeIndex(testPoint);
    assertTrue(
        S2.approxEquals(
            line.projectToEdge(testPoint, edgeIndex), S2LatLng.fromDegrees(0, 0.5).toPoint()));
    assertEquals(0, edgeIndex);

    testPoint = S2LatLng.fromDegrees(0.5, 1).toPoint();
    edgeIndex = line.getNearestEdgeIndex(testPoint);
    assertTrue(
        S2.approxEquals(
            line.projectToEdge(testPoint, edgeIndex), S2LatLng.fromDegrees(0, 1).toPoint()));
    assertEquals(0, edgeIndex);

    testPoint = S2LatLng.fromDegrees(-0.5, 2.5).toPoint();
    edgeIndex = line.getNearestEdgeIndex(testPoint);
    assertTrue(
        S2.approxEquals(
            line.projectToEdge(testPoint, edgeIndex), S2LatLng.fromDegrees(0, 2).toPoint()));
    assertEquals(1, edgeIndex);

    testPoint = S2LatLng.fromDegrees(2, 2).toPoint();
    edgeIndex = line.getNearestEdgeIndex(testPoint);
    assertTrue(
        S2.approxEquals(
            line.projectToEdge(testPoint, edgeIndex), S2LatLng.fromDegrees(1, 2).toPoint()));
    assertEquals(2, edgeIndex);
  }

  public void testProject() {
    S2Point pointA = new S2Point(1, 0, 0);
    S2Point pointB = new S2Point(0, 1, 0);
    S2Point pointC = new S2Point(0, 0, 1);
    S2Polyline line = new S2Polyline(ImmutableList.of(pointA, pointB, pointC));

    S2Point pointMidAB = S2Point.normalize(new S2Point(1, 1, 0));
    S2Point pointMidBC = S2Point.normalize(new S2Point(0, 1, 1));

    // Test at query points on the line
    final int steps = 6;
    for (int i = 0; i <= steps; i++) {
      S2Point queryPoint = line.interpolate(i / (double) steps);
      assertTrue(S2.approxEquals(line.project(queryPoint), queryPoint));
    }

    // Test at a point off the line such that the unique nearest point is a vertex
    S2Point pointOffFromB = S2Point.normalize(new S2Point(-0.1, 1, -0.1));
    assertTrue(S2.approxEquals(line.project(pointOffFromB), pointB));

    // Test at a point off the line such that the unique nearest point is a not vertex
    S2Point pointOffFromMidAB = S2Point.normalize(new S2Point(1, 1, -0.1));
    assertTrue(S2.approxEquals(line.project(pointOffFromMidAB), pointMidAB));

    // Test at a point off the line such that there are two nearest points
    S2Point pointEquidistantFromABAndBC = S2Point.normalize(new S2Point(1, 1, 1));
    S2Point projectedPoint = line.project(pointEquidistantFromABAndBC);
    assertTrue(
        S2.approxEquals(projectedPoint, pointMidAB) || S2.approxEquals(projectedPoint, pointMidBC));

    // Test projecting on a degenerate polyline
    S2Polyline degenerateLine = new S2Polyline(ImmutableList.of(pointA));
    assertTrue(degenerateLine.project(pointB).equals(pointA));
  }

  public void testIntersectsEmptyPolyline() {
    S2Polyline line1 = makePolyline("1:1, 4:4");
    S2Polyline emptyPolyline = new S2Polyline(Lists.<S2Point>newArrayList());
    assertFalse(emptyPolyline.intersects(line1));
  }

  public void testIntersectsOnePointPolyline() {
    S2Polyline line1 = makePolyline("1:1, 4:4");
    S2Polyline line2 = makePolyline("1:1");
    assertFalse(line1.intersects(line2));
  }

  public void testIntersects() {
    S2Polyline line1 = makePolyline("1:1, 4:4");
    S2Polyline smallCrossing = makePolyline("1:2, 2:1");
    S2Polyline smallNonCrossing = makePolyline("1:2, 2:3");
    S2Polyline bigCrossing = makePolyline("1:2, 2:3, 4:3");
    assertTrue(line1.intersects(smallCrossing));
    assertFalse(line1.intersects(smallNonCrossing));
    assertTrue(line1.intersects(bigCrossing));
  }

  public void testIntersectsAtVertex() {
    S2Polyline line1 = makePolyline("1:1, 4:4, 4:6");
    S2Polyline line2 = makePolyline("1:1, 1:2");
    S2Polyline line3 = makePolyline("5:1, 4:4, 2:2");
    assertTrue(line1.intersects(line2));
    assertTrue(line1.intersects(line3));
  }

  public void testIntersectsVertexOnEdge() {
    S2Polyline horizontalLeftToRight = makePolyline("0:1, 0:3");
    S2Polyline verticalBottomToTop = makePolyline("-1:2, 0:2, 1:2");
    S2Polyline horizontalRightToLeft = makePolyline("0:3, 0:1");
    S2Polyline verticalTopToBottom = makePolyline("1:2, 0:2, -1:2");
    assertTrue(horizontalLeftToRight.intersects(verticalBottomToTop));
    assertTrue(horizontalLeftToRight.intersects(verticalTopToBottom));
    assertTrue(horizontalRightToLeft.intersects(verticalBottomToTop));
    assertTrue(horizontalRightToLeft.intersects(verticalTopToBottom));
  }

  public void testSubsampleVerticesTrivialInputs() {
    // No vertices.
    checkSubsample("", 1.0);
    // One vertex.
    checkSubsample("0:1", 1.0, 0);
    // Two vertices.
    checkSubsample("10:10, 11:11", 5.0, 0, 1);
    // Three points on a straight line. In theory, zero tolerance should work, but in practice there
    // are floating point errors.
    checkSubsample("-1:0, 0:0, 1:0", 1e-15, 0, 2);
    // Zero tolerance on a non-straight line.
    checkSubsample("-1:0, 0:0, 1:1", 0.0, 0, 1, 2);
    // Negative tolerance should return all vertices.
    checkSubsample("-1:0, 0:0, 1:1", -1.0, 0, 1, 2);
    // Non-zero tolerance with a straight line.
    checkSubsample("0:1, 0:2, 0:3, 0:4, 0:5", 1.0, 0, 4);

    // And finally, verify that we still do something reasonable if the client passes in an invalid
    // polyline with two or more adjacent vertices.
    checkSubsample("0:1, 0:1, 0:1, 0:2", 0.0, 0, 3);
  }

  public void testSubsampleVerticesSimpleExample() {
    String coords = "0:0, 0:1, -1:2, 0:3, 0:4, 1:4, 2:4.5, 3:4, 3.5:4, 4:4";
    checkSubsample(coords, 3.0, 0, 9);
    checkSubsample(coords, 2.0, 0, 6, 9);
    checkSubsample(coords, 0.9, 0, 2, 6, 9);
    checkSubsample(coords, 0.4, 0, 1, 2, 3, 4, 6, 9);
    checkSubsample(coords, 0, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9);
  }

  public void testSubsampleVerticesGuarantees() {
    // Check that duplicate vertices are never generated.
    checkSubsample("10:10, 12:12, 10:10", 5.0, 0);
    checkSubsample("0:0, 1:1, 0:0, 0:120, 0:130", 5.0, 0, 3, 4);

    // Check that points are not collapsed if they would create a line segment longer than 90
    // degrees, and also that the code handles original polyline segments longer than 90 degrees.
    checkSubsample(
        "90:0, 50:180, 20:180, -20:180, -50:180, -90:0, 30:0, 90:0", 5.0, 0, 2, 4, 5, 6, 7);

    // Check that the output polyline is parametrically equivalent and not just geometrically
    // equivalent, i.e. that backtracking is preserved.  The algorithm achieves this by requiring
    // that the points must be encountered in increasing order of distance along each output segment
    // except for points that are within "tolerance" of the first vertex of each segment.
    checkSubsample("10:10, 10:20, 10:30, 10:15, 10:40", 5.0, 0, 2, 3, 4);
    checkSubsample("10:10, 10:20, 10:30, 10:10, 10:30, 10:40", 5.0, 0, 2, 3, 5);
    checkSubsample("10:10, 12:12, 9:9, 10:20, 10:30", 5.0, 0, 4);
  }

  private static void checkSubsample(String coords, double toleranceDegrees, int... expected) {
    S2Polyline polyline = makePolyline(coords);
    S2Polyline simplified = polyline.subsampleVertices(S1Angle.degrees(toleranceDegrees));
    assertEquals(expected.length, simplified.numVertices());
    for (int i = 0; i < expected.length; i++) {
      assertEquals(polyline.vertex(expected[i]), simplified.vertex(i));
    }
  }

  public void testValid() {
    // A simple normalized line must be valid.
    List<S2Point> vertices = Lists.newArrayList();
    vertices.add(new S2Point(1, 0, 0));
    vertices.add(new S2Point(0, 1, 0));
    S2Polyline line = new S2Polyline(vertices);
    assertTrue(line.isValid());
  }

  public void testInvalid() {
    // A non-normalized line must be invalid.
    List<S2Point> vertices = Lists.newArrayList();
    vertices.add(new S2Point(1, 0, 0));
    vertices.add(new S2Point(0, 2, 0));
    S2Polyline line = new S2Polyline(vertices);
    assertFalse(line.isValid());

    // Lines with duplicate points must be invalid.
    List<S2Point> vertices2 = Lists.newArrayList();
    vertices2.add(new S2Point(1, 0, 0));
    vertices2.add(new S2Point(0, 1, 0));
    vertices2.add(new S2Point(0, 1, 0));
    S2Polyline line2 = new S2Polyline(vertices2);
    assertFalse(line2.isValid());
  }

  public void testNumEdges() {
    // Empty polyline has zero edges
    assertEquals(0, makePolyline("").numEdges());

    // Single vertex polyline has zero edges
    assertEquals(0, makePolyline("0:0").numEdges());

    // Multiple vertex polylines have non-zero edges
    assertEquals(1, makePolyline("0:0, 1:1").numEdges());
    assertEquals(2, makePolyline("0:0, 1:1, 2:2").numEdges());
  }

  public void testEmptyShape() {
    S2Shape shape = makePolyline("");
    assertFalse(shape.hasInterior());
    assertEquals(0, shape.numEdges());
    assertEquals(0, shape.numChains());
    assertEquals(1, shape.dimension());
  }

  public void testSingleVertexShape() {
    S2Shape shape = makePolyline("0:0");
    assertFalse(shape.hasInterior());
    assertEquals(0, shape.numEdges());
    assertEquals(0, shape.numChains());
    assertEquals(1, shape.dimension());
  }

  public void testDoubleVertexShape() {
    S2Shape shape = makePolyline("0:0, 1:1");
    assertFalse(shape.hasInterior());
    assertEquals(1, shape.numEdges());
    checkFirstNEdges(shape, "0:0|1:1");
    assertEquals(1, shape.numChains());
    checkFirstNChainStarts(shape, 0);
    checkFirstNChainLengths(shape, 1);
    checkFirstNChainEdges(shape, 0, "0:0|1:1");
    assertEquals(1, shape.dimension());
  }

  public void testTripleVertexShape() {
    S2Shape shape = makePolyline("0:0, 1:1, 2:2");
    assertFalse(shape.hasInterior());
    assertEquals(2, shape.numEdges());
    checkFirstNEdges(shape, "0:0|1:1, 1:1|2:2");
    assertEquals(1, shape.numChains());
    checkFirstNChainStarts(shape, 0);
    checkFirstNChainLengths(shape, 2);
    checkFirstNChainEdges(shape, 0, "0:0|1:1, 1:1|2:2");
    assertEquals(1, shape.dimension());
  }

  public void testS2ShapeInterface() {
    S1Angle radius = S1Angle.radians(10);
    int numVertices = 40;
    for (int iter = 0; iter < 50; ++iter) {
      // Create an S2Polyline from a regular loop, and test that its vertices can be found by
      // S2ShapeIndex.CellIterator's locate() method.
      S2Loop loop = S2Loop.makeRegularLoop(randomPoint(), radius, numVertices);
      List<S2Point> vertices = Lists.newArrayList();
      for (int i = 0; i < numVertices; ++i) {
        vertices.add(loop.vertex(i));
      }
      S2Polyline polyline = new S2Polyline(vertices);

      S2ShapeIndex index = new S2ShapeIndex();
      index.add(polyline);
      S2Iterator<S2ShapeIndex.Cell> iterator = index.iterator();
      for (int i = 0; i < numVertices; ++i) {
        assertTrue(iterator.locate(vertices.get(i)));
      }
    }
  }

  /** Verifies that contains() returns false for all cells. */
  public void testContains() {
    S2Polyline line = makePolyline("0:0, 5:5, 10:5");
    // Check a point nowhere near the line.
    assertFalse(line.contains(new S2Cell(S2LatLng.fromDegrees(-10, -10).toPoint())));
    // Check each vertex.
    assertFalse(line.contains(new S2Cell(S2LatLng.fromDegrees(0, 0).toPoint())));
    assertFalse(line.contains(new S2Cell(S2LatLng.fromDegrees(5, 5).toPoint())));
    assertFalse(line.contains(new S2Cell(S2LatLng.fromDegrees(10, 5).toPoint())));
    // And check a point exactly on the center of the last edge.
    assertFalse(line.contains(new S2Cell(S2LatLng.fromDegrees(7.5, 5).toPoint())));
  }

  /**
   * Utility for testing equals() and hashCode() results at once. Tests that lhs.equals(rhs) matches
   * expectedResult, as well as rhs.equals(lhs). Also tests that hashCode() return values are equal
   * if expectedResult is true. (hashCode() is not tested if expectedResult is false, as unequal
   * objects can have equal hashCodes.)
   *
   * @param lhs An Object for which equals() and hashCode() are to be tested.
   * @param rhs As lhs.
   * @param expectedResult True if the objects should compare equal, false if not.
   */
  private static void checkEqualsAndHashCodeMethods(
      Object lhs, Object rhs, boolean expectedResult) {
    if ((lhs == null) && (rhs == null)) {
      assertTrue("Your check is dubious...why would you expect null != null?", expectedResult);
      return;
    }

    if ((lhs == null) || (rhs == null)) {
      assertFalse(
          "Your check is dubious...why would you expect an object " + "to be equal to null?",
          expectedResult);
    }

    if (lhs != null) {
      assertEquals(expectedResult, lhs.equals(rhs));
    }
    if (rhs != null) {
      assertEquals(expectedResult, rhs.equals(lhs));
    }

    if (expectedResult) {
      String hashMessage = "hashCode() values for equal objects should be the same";
      assertTrue(hashMessage, lhs.hashCode() == rhs.hashCode());
    }
  }
}
