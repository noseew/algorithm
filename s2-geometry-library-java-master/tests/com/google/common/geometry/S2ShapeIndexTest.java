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

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.collect.Lists;
import com.google.common.geometry.S2Shape.MutableEdge;
import com.google.common.geometry.S2ShapeIndex.CellRelation;
import com.google.common.geometry.S2ShapeIndex.S2ClippedShape;
import com.google.common.geometry.S2ShapeUtil.S2EdgeVectorShape;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CyclicBarrier;

/** Verifies S2ShapeIndex construction and structure. */
@GwtCompatible
public strictfp class S2ShapeIndexTest extends GeometryTestCase {
  public void testNoEdges() {
    S2ShapeIndex index = new S2ShapeIndex();
    assertTrue(index.iterator().done());
    checkIteratorMethods(index);
  }

  public void testOneEdge() {
    S2ShapeIndex index = new S2ShapeIndex();
    index.add(new S2EdgeVectorShape(S2Point.X_POS, S2Point.Y_POS));
    quadraticValidate(index);
    checkIteratorMethods(index);
  }

  /**
   * Regression test of a bug in the shrinkToFit optimization. The loop below contains almost all of
   * face 0 except for a small region in the 0/00000 subcell. That subcell is the only one that
   * contains any edges. This caused the index to be built only in that subcell. However, all the
   * other cells on that face should also have index entries, in order to indicate that they are
   * contained by the loop.
   */
  public void testShrinkToFitOptimization() {
    S2ShapeIndex index = new S2ShapeIndex();
    S2Point axis = S2Point.normalize(new S2Point(1, 0.5, 0.5));
    S2Loop loop = S2Loop.makeRegularLoop(axis, S1Angle.degrees(89), 100);
    index.add(loop);
    quadraticValidate(index);
  }

  public void testLoopsSpanningThreeFaces() {
    // Construct two loops centered around the cube vertex at the start of the Hilbert curve.
    S2Point center = S2Point.normalize(new S2Point(1, -1, -1));
    S2Polygon polygon = concentricLoopsPolygon(center, 2, TestPlatform.S2_SHAPEINDEX_NUM_EDGES);
    List<S2Loop> loops = Lists.newArrayList();
    polygon.release(loops);
    S2ShapeIndex index = new S2ShapeIndex();
    for (S2Loop loop : loops) {
      index.add(loop);
    }
    quadraticValidate(index);
    checkIteratorMethods(index);
  }

  public void testManyIdenticalEdges() {
    S2Point a = S2Point.normalize(new S2Point(0.99, 0.99, 1));
    S2Point b = S2Point.normalize(new S2Point(-0.99, -0.99, 1));
    S2ShapeIndex index = new S2ShapeIndex();
    for (int i = 0; i < TestPlatform.S2_SHAPEINDEX_NUM_EDGES; i++) {
      index.add(new S2EdgeVectorShape(a, b));
    }
    quadraticValidate(index);
    checkIteratorMethods(index);
    // Since all edges span the the diagonal of a face, no subdivision should have occurred (with
    // the default index options.)
    for (S2Iterator<S2ShapeIndex.Cell> it = index.iterator(); !it.done(); it.next()) {
      assertEquals(0, it.id().level());
    }
  }

  /**
   * This test inserts many edges into a single leaf cell, to check that subdivision stops when no
   * further subdivision is possible.
   */
  public void testManyTinyEdges() {
    // Construct two points in the same leaf cell.
    S2Point a = S2CellId.fromPoint(S2Point.X_POS).toPoint();
    S2Point b = S2Point.normalize(S2Point.add(a, new S2Point(0, 1e-12, 0)));
    S2EdgeVectorShape shape = new S2EdgeVectorShape();
    for (int i = 0; i < TestPlatform.S2_SHAPEINDEX_NUM_EDGES; i++) {
      shape.add(a, b);
    }
    S2ShapeIndex index = new S2ShapeIndex();
    index.add(shape);
    quadraticValidate(index);
    // Check that there is exactly one index cell and that it is a leaf cell.
    S2Iterator<S2ShapeIndex.Cell> it = index.iterator();
    assertFalse(it.done());
    assertTrue(it.id().isLeaf());
    it.next();
    assertTrue(it.done());
  }

  public void testHasCrossing() {
    // Coordinates are (lat,lng), which can be visualized as (y,x).
    checkHasCrossing("0:0, 0:1, 0:2, 1:2, 1:1, 1:0", false);
    checkHasCrossing("0:0, 0:1, 0:2, 1:2, 0:1, 1:0", true); // duplicate vertex
    checkHasCrossing("0:0, 0:1, 1:0, 1:1", true); // edge crossing
    checkHasCrossing("0:0, 1:1, 0:1; 0:0, 1:1, 1:0", true); // duplicate edge
    checkHasCrossing("0:0, 1:1, 0:1; 1:1, 0:0, 1:0", true); // reversed edge
    checkHasCrossing("0:0, 0:2, 2:2, 2:0; 1:1, 0:2, 3:1, 2:0", true); // vertex crossing
  }

  public void testLinearSpace() {
    // Build an index that requires FLAGS_s2shape_index_min_short_edge_fraction
    // to be non-zero in order to use a non-quadratic amount of space.

    // TODO
    // Uncomment the following line to check whether this test works properly.
    // FLAGS_s2shape_index_min_short_edge_fraction = 0;

    // Set the maximum number of "short" edges per cell to 1 so that we can
    // implement this test using a smaller index.
    S2ShapeIndex.Options options = new S2ShapeIndex.Options();
    options.setMaxEdgesPerCell(1);
    S2ShapeIndex index = new S2ShapeIndex(options);

    // The idea is to create O(n) copies of a single long edge, along with O(n)
    // clusters of (M + 1) points equally spaced along the long edge, where "M"
    // is the max_edges_per_cell() parameter.  The edges are divided such that
    // there are equal numbers of long and short edges; this maximizes the index
    // size when FLAGS_s2shape_index_min_short_edge_fraction is set to zero.
    final int kNumEdges = 100;  // Validation is quadratic
    int edgesPerCluster = options.getMaxEdgesPerCell() + 1;
    int numClusters = (kNumEdges / 2) / edgesPerCluster;

    // Create the long edges.
    S2Point a = S2Point.X_POS;
    S2Point b = S2Point.Y_POS;
    for (int i = 0; i < kNumEdges / 2; i++) {
      index.add(new S2EdgeVectorShape(a, b));
    }
    // Create the clusters of short edges.
    for (int k = 0; k < numClusters; ++k) {
      S2Point p = S2EdgeUtil.interpolate(k / (numClusters - 1.0), a, b);
      List<S2Point> points = Collections.nCopies(edgesPerCluster, p);
      index.add(S2Point.Shape.fromList(points));
    }
    quadraticValidate(index);

    // The number of index cells should not exceed the number of clusters.
    int cellCount = 0;
    for (S2Iterator<?> it = index.iterator(); !it.done(); it.next()) {
      cellCount++;
    }
    assertTrue(cellCount <= numClusters);
  }

  public void testLongIndexEntriesBound() {
    // This test demonstrates that the c2 = 366 upper bound (using default
    // parameter values) mentioned in the .cc file is achievable.

    // Set the maximum number of "short" edges per cell to 1 so that we can test
    // using a smaller index.
    S2ShapeIndex.Options options = new S2ShapeIndex.Options();
    options.setMaxEdgesPerCell(1);
    S2ShapeIndex index = new S2ShapeIndex(options);

    // This is a worst-case edge AB that touches as many cells as possible at
    // level 30 while still being considered "short" at level 29.  We create an
    // index consisting of two copies of this edge plus a full polygon.
    S2Point a = S2Projections.PROJ.faceSiTiToXyz(0, 0, (1 << 30) + 0).normalize();
    S2Point b = S2Projections.PROJ.faceSiTiToXyz(0, 0, (1 << 30) + 6).normalize();
    for (int i = 0; i < 2; ++i) {
      index.add(new S2EdgeVectorShape(a, b));
    }
    index.add(new S2Polygon(S2Loop.full()).shape());
    index.applyUpdates();

    // Count the number of index cells at each level.
    int[] counts = new int[S2CellId.MAX_LEVEL + 1];
    for (S2Iterator<?> it = index.iterator(); !it.done(); it.next()) {
      counts[it.id().level()]++;
    }
    int sum = 0;
    for (int count : counts) {
      sum += count;
    }
    assertEquals(366, sum);
  }


  @GwtIncompatible(value = "No support for threads")
  public void testConstMethodsThreadSafe() throws Exception {
    // Ensure that lazy updates are thread-safe.  In other words, make sure that nothing bad happens
    // when multiple threads call methods that cause pending updates to be applied.
    //
    // The number of readers should be large enough so that it is likely that several readers will
    // be running at once (with a multiple-core CPU.)
    final int numIterations = 100;
    final int numReaders = 8;
    final S2ShapeIndex index = new S2ShapeIndex();

    // For each iteration, update the index, schedule reader tasks, and await the barriers. Each
    // reader thread awaits the start barrier so all readers begin with maximum contention, iterates
    // the cells of the index so numReaders-1 readers should block awaiting the index construction
    // while one reader performs the update, and then awaits the end barrier so the iteration loop
    // doesn't move on until all readers are finished.
    for (int iter = 0; iter < numIterations; iter++) {
      final CyclicBarrier start = new CyclicBarrier(numReaders + 1);
      final CyclicBarrier end = new CyclicBarrier(numReaders + 1);

      // Mutate the index.
      int numVertices = 4 * skewed(10); // Up to 4K vertices
      S2Loop loop = S2Loop.makeRegularLoop(randomPoint(), kmToAngle(5), numVertices);
      index.reset();
      index.add(loop);

      // Submit readers for this iteration.
      for (int i = 0; i < numReaders; i++) {
        new Thread(
                new Runnable() {
                  @Override
                  public void run() {
                    try {
                      // Ensure that all readers start at the same time, after mutating the index.
                      start.await();
                      // The index is built on demand the first time we attempt to use it.
                      for (S2Iterator<S2ShapeIndex.Cell> it = index.iterator();
                          !it.done();
                          it.next()) {}
                      // Ensure that all threads end before any additional threads can begin.
                      end.await();
                    } catch (Exception e) {
                      throw new RuntimeException(e);
                    }
                  }
                },
                "reader-" + i)
            .start();
      }

      // Await the start and end barriers, so the readers begin after index mutation, and we don't
      // schedule more readers until after the existing readers have finished.
      start.await();
      end.await();
    }
  }

  public void testUVRegression() {
    // Regression test that verifies the FaceEdge direction is correct.
    S2Loop loop = null;
    for (int iter = 0; iter < 8; iter++) {
      int numVertices = 4 * skewed(10); // Up to 4K vertices
      loop = S2Loop.makeRegularLoop(randomPoint(), kmToAngle(5), numVertices);
    }
    S2ShapeIndex index = new S2ShapeIndex();
    index.reset();
    index.add(loop);
    quadraticValidate(index);
  }

  public void testEdgeRangeRegression() {
    // Regression test that catches error where EdgeRange was used incorrectly, when an intermediate
    // edge is not part of the second shape in a multi-shape cell.
    S2ShapeIndex index = new S2ShapeIndex();
    index.add(makeLoop("43:178, 41:162, 36:172"));
    index.add(makeLoop("40:178, 46:-173, 45:169"));
    quadraticValidate(index);
  }

  public void testReset() {
    S2ShapeIndex index = new S2ShapeIndex();
    for (int i = 0; i < 10; i++) {
      index.add(S2Loop.makeRegularLoop(randomPoint(), kmToAngle(5), 4));
      quadraticValidate(index);
      checkIteratorMethods(index);
      index.reset();
    }
  }

  public void testPolylineRegression() {
    // Regression test that catches error where a 0-edged clipped shape of a polyline was being
    // added to the index.
    S2ShapeIndex index = new S2ShapeIndex();
    index.add(makePolyline("2:1, 6:1"));
    index.add(makeLoop("0:2, 0:3, 1:3, 1:2"));
    index.add(makeLoop("3:6, 3:7, 4:7, 4:6"));
    index.add(makeLoop("3:6, 3:7, 4:7, 4:6"));

    for (S2Iterator<S2ShapeIndex.Cell> it = index.iterator(); !it.done(); it.next()) {
      S2ShapeIndex.Cell cell = it.entry();
      for (int i = 0; i < cell.numShapes(); ++i) {
        S2ShapeIndex.S2ClippedShape clipped = cell.clipped(i);
        assertTrue(clipped.shape().hasInterior() || clipped.numEdges() > 0);
      }
    }
  }

  /**
   * Verifies that every cell of the index contains the correct edges, and that no cells are missing
   * from the index. The running time of this function is quadratic in the number of edges.
   */
  private void quadraticValidate(S2ShapeIndex index) {
    // Iterate through a sequence of nonoverlapping cell ids that cover the sphere and include as a
    // subset all the cell ids used in the index.  For each cell id, verify that the expected set of
    // edges is present.

    // "minCellId" is the first S2CellId that has not been validated yet.
    S2CellId minCellId = S2CellId.begin(S2CellId.MAX_LEVEL);
    S2Iterator<S2ShapeIndex.Cell> it = index.iterator();
    while (true) {
      // Generate a list of S2CellIds ("skipped cells") that cover the gap between the last cell we
      // validated and the next cell in the index.
      S2CellUnion skipped = new S2CellUnion();
      if (!it.done()) {
        S2CellId cellid = it.id();
        assertTrue(cellid.greaterOrEquals(minCellId));
        skipped.initFromBeginEnd(minCellId, cellid.rangeMin());
        minCellId = cellid.rangeMax().next();
      } else {
        // Validate the empty cells beyond the last cell in the index.
        skipped.initFromBeginEnd(minCellId, S2CellId.end(S2CellId.MAX_LEVEL));
      }

      // Iterate through all the shapes, simultaneously validating the current index cell and all
      // the skipped cells.
      MutableEdge edge = new MutableEdge();
      // all edges in the cell
      int numEdges = 0;
      // "short" edges
      int numShortEdges = 0;
      // shapes containing cell's entry vertex
      int numContainingShapes = 0;
      for (S2Shape shape : index.shapes) {
        S2ClippedShape clipped = null;
        if (!it.done()) {
          clipped = it.entry().findClipped(shape);
        }

        // First check that containsCenter() is set correctly.
        for (int j = 0; j < skipped.size(); j++) {
          checkInterior(shape, skipped.cellId(j), false);
        }
        if (!it.done()) {
          boolean containsCenter = clipped != null && clipped.containsCenter();
          checkInterior(shape, it.id(), containsCenter);
          S2PaddedCell pcell = new S2PaddedCell(it.id(), S2ShapeIndex.CELL_PADDING);
          if (shape != null) {
            S2Point entry = pcell.getEntryVertex();
            numContainingShapes += S2ShapeUtil.containsBruteForce(shape, entry) ? 1 : 0;
          }
        }

        // Then check that the appropriate edges are present.
        for (int e = 0; e < shape.numEdges(); e++) {
          shape.getEdge(e, edge);
          for (int j = 0; j < skipped.size(); j++) {
            checkEdge(edge.a, edge.b, skipped.cellId(j), false);
          }
          if (!it.done()) {
            boolean hasEdge = clipped != null && clipped.containsEdge(e);
            checkEdge(edge.a, edge.b, it.id(), hasEdge);
            if (hasEdge) {
              numEdges++;
              double ratio = index.options().getCellSizeToLongEdgeRatio();
              int maxLevel = S2ShapeIndex.getEdgeMaxLevel(edge.a, edge.b, ratio);
              if (it.id().level() < maxLevel) {
                numShortEdges++;
              }
            }
          }
        }
      }

      // This mirrors the calculation in MutableS2ShapeIndex::MakeIndexCell().
      // It is designed to ensure that the index size is always linear in the
      // number of indexed edges.
      int maxShortEdges = Math.max(
          index.options().getMaxEdgesPerCell(),
          (int) (S2ShapeIndex.MIN_SHORT_EDGE_FRACTION * (numEdges + numContainingShapes)));
      assertTrue(numShortEdges <= maxShortEdges);
      if (it.done()) {
        break;
      }
      it.next();
    }
  }

  /**
   * Given an edge and a cell id, determines whether or not the edge should be present in that cell
   * and verify that this matches "indexHasEdge". Then verifies that "indexHasEdge" is true if and
   * only if the edge AB intersects the given cell id.
   */
  private void checkEdge(S2Point a, S2Point b, S2CellId id, boolean indexHasEdge) {
    // Expand or shrink the padding slightly to account for errors in the function we use to test
    // for intersection (intersectsRect).
    double padding = S2ShapeIndex.CELL_PADDING;
    padding += (indexHasEdge ? 1 : -1) * S2EdgeUtil.INTERSECTS_RECT_ERROR_UV_DIST;
    R2Rect bound = id.getBoundUV().expanded(padding);
    R2Vector aUv = new R2Vector();
    R2Vector bUv = new R2Vector();
    assertEquals(
        indexHasEdge,
        S2EdgeUtil.clipToPaddedFace(a, b, id.face(), padding, aUv, bUv)
            && S2EdgeUtil.intersectsRect(aUv, bUv, bound));
  }

  /**
   * Given a shape and a cell id, determines whether or not the shape contains the cell center and
   * verify that this matches "indexContainsCenter".
   */
  private void checkInterior(S2Shape shape, S2CellId id, boolean indexContainsCenter) {
    if (!shape.hasInterior()) {
      return;
    }
    S2EdgeUtil.EdgeCrosser crosser = new S2EdgeUtil.EdgeCrosser(S2.origin(), id.toPoint());
    boolean containsCenter = shape.containsOrigin();
    MutableEdge edge = new MutableEdge();
    for (int e = 0; e < shape.numEdges(); e++) {
      shape.getEdge(e, edge);
      crosser.restartAt(edge.a);
      containsCenter ^= crosser.edgeOrVertexCrossing(edge.b);
    }
    assertEquals(containsCenter, indexContainsCenter);
  }

  /** Verifies that the iterator methods behave consistently for the given index. */
  static void checkIteratorMethods(S2ShapeIndex index) {
    S2Iterator<S2ShapeIndex.Cell> it = index.iterator();
    assertTrue(it.atBegin());
    it.finish();
    assertTrue(it.done());
    List<S2CellId> ids = Lists.newArrayList();
    S2CellId minCellId = S2CellId.begin(S2CellId.MAX_LEVEL);
    for (it.restart(); !it.done(); it.next()) {
      S2CellId cellid = it.id();
      S2CellUnion skipped = new S2CellUnion();
      if (cellid.rangeMin().greaterThan(minCellId)) {
        skipped.initFromMinMax(minCellId, cellid.rangeMin().prev());
      }
      S2Iterator<S2ShapeIndex.Cell> it2 = index.iterator();
      for (int i = 0; i < skipped.size(); i++) {
        assertFalse(it2.locate(skipped.cellId(i).toPoint()));
        assertEquals(CellRelation.DISJOINT, it2.locate(skipped.cellId(i)));
        it2.restart();
        it2.seek(skipped.cellId(i));
        assertEquals(cellid, it2.id());
      }
      if (!ids.isEmpty()) {
        assertFalse(it.atBegin());
        it2.position(it);
        it2.prev();
        assertEquals(ids.get(ids.size() - 1), it2.id());
        it2.next();
        assertEquals(cellid, it2.id());
        it2.seek(ids.get(ids.size() - 1));
        assertEquals(ids.get(ids.size() - 1), it2.id());
        it2.seekForward(cellid);
        assertEquals(cellid, it2.id());
        it2.seekForward(ids.get(ids.size() - 1));
        assertEquals(cellid, it2.id());
      }
      it2.restart();
      assertEquals(cellid.toPoint(), it.center());
      assertTrue(it2.locate(it.center()));
      assertEquals(cellid, it2.id());
      it2.restart();
      assertEquals(CellRelation.INDEXED, it2.locate(cellid));
      assertEquals(cellid, it2.id());
      if (!cellid.isFace()) {
        it2.restart();
        assertEquals(CellRelation.SUBDIVIDED, it2.locate(cellid.parent()));
        assertTrue(cellid.greaterOrEquals(it2.id()));
        assertTrue(it2.id().greaterOrEquals(cellid.parent().rangeMin()));
      }
      if (!cellid.isLeaf()) {
        for (int i = 0; i < 4; i++) {
          it2.restart();
          assertEquals(CellRelation.INDEXED, it2.locate(cellid.child(i)));
          assertEquals(cellid, it2.id());
        }
      }
      ids.add(cellid);
      minCellId = cellid.rangeMax().next();
    }
  }

  /** Adds the given loops to the given index. */
  private static void addLoops(List<S2Loop> loops, S2ShapeIndex index) {
    for (S2Loop loop : loops) {
      index.add(loop);
    }
  }

  /**
   * Recursively verifies that HasCrossing returns the given // result for all possible cyclic
   * permutations of the loop vertices for the // given set of loops.
   */
  private static void checkHasCrossingPermutations(List<S2Loop> loops, int i, boolean hasCrossing) {
    if (i == loops.size()) {
      S2ShapeIndex index = new S2ShapeIndex();
      addLoops(loops, index);
      assertEquals(hasCrossing, S2ShapeUtil.findAnyCrossing(index, loops, new S2Error()));
    } else {
      S2Loop origLoop = loops.get(i);
      for (int j = 0; j < origLoop.numVertices(); j++) {
        List<S2Point> vertices = Lists.newArrayList();
        for (int k = 0; k < origLoop.numVertices(); k++) {
          vertices.add(origLoop.vertex(j + k));
        }
        S2Loop newLoop = new S2Loop(vertices);
        loops.set(i, newLoop);
        checkHasCrossingPermutations(loops, i + 1, hasCrossing);
      }
      loops.set(i, origLoop);
    }
  }

  /**
   * Given a string representation of a polygon, and a boolean indicating whether this polygon has
   * any self-intersections or loop crossings, verify that hasAnyCrossing returns the expected
   * result for all possible cyclic permutations of the loop vertices.
   */
  private static void checkHasCrossing(String polygonStr, boolean hasCrossing) {
    S2Polygon polygon = makeVerbatimPolygon(polygonStr);
    List<S2Loop> loops = Lists.newArrayList();
    polygon.release(loops);
    checkHasCrossingPermutations(loops, 0, hasCrossing);
  }
}
