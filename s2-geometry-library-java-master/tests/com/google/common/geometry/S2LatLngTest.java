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

import static junit.framework.TestCase.assertFalse;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;

/** Verifies S2LatLng. */
@GwtCompatible(emulated = true)
public strictfp class S2LatLngTest extends GeometryTestCase {

  public void testBasic() {
    S2LatLng llRad = S2LatLng.fromRadians(S2.M_PI_4, S2.M_PI_2);
    assertTrue(llRad.lat().radians() == S2.M_PI_4);
    assertTrue(llRad.lng().radians() == S2.M_PI_2);
    assertTrue(llRad.isValid());
    S2LatLng llDeg = S2LatLng.fromDegrees(45, 90);
    assertEquals(llDeg, llRad);
    assertTrue(llDeg.isValid());
    assertFalse(S2LatLng.fromDegrees(-91, 0).isValid());
    assertFalse(S2LatLng.fromDegrees(0, 181).isValid());

    S2LatLng bad = S2LatLng.fromDegrees(120, 200);
    assertFalse(bad.isValid());
    S2LatLng better = bad.normalized();
    assertTrue(better.isValid());
    assertEquals(better.lat(), S1Angle.degrees(90));
    assertDoubleNear(better.lng().radians(), S1Angle.degrees(-160).radians());

    bad = S2LatLng.fromDegrees(-100, -360);
    assertFalse(bad.isValid());
    better = bad.normalized();
    assertTrue(better.isValid());
    assertEquals(better.lat(), S1Angle.degrees(-90));
    assertDoubleNear(better.lng().radians(), 0);

    assertTrue(
        (S2LatLng.fromDegrees(10, 20).add(S2LatLng.fromDegrees(20, 30)))
            .approxEquals(S2LatLng.fromDegrees(30, 50)));
    assertTrue(
        (S2LatLng.fromDegrees(10, 20).sub(S2LatLng.fromDegrees(20, 30)))
            .approxEquals(S2LatLng.fromDegrees(-10, -10)));
    assertTrue((S2LatLng.fromDegrees(10, 20).mul(0.5)).approxEquals(S2LatLng.fromDegrees(5, 10)));
  }

  public void testConversion() {
    // Test special cases: poles, "date line"
    assertDoubleNear(
        new S2LatLng(S2LatLng.fromDegrees(90.0, 65.0).toPoint()).lat().degrees(), 90.0);
    assertEquals(
        -S2.M_PI_2, new S2LatLng(S2LatLng.fromRadians(-S2.M_PI_2, 1).toPoint()).lat().radians());
    assertDoubleNear(
        Math.abs(new S2LatLng(S2LatLng.fromDegrees(12.2, 180.0).toPoint()).lng().degrees()), 180.0);
    assertEquals(
        S2.M_PI,
        Math.abs(new S2LatLng(S2LatLng.fromRadians(0.1, -S2.M_PI).toPoint()).lng().radians()));

    // Test a bunch of random points.
    for (int i = 0; i < 100000; ++i) {
      S2Point p = randomPoint();
      assertTrue(S2.approxEquals(p, new S2LatLng(p).toPoint()));
    }

    // Test generation from E5
    S2LatLng test = S2LatLng.fromE5(123456, 98765);
    assertDoubleNear(test.lat().degrees(), 1.23456);
    assertDoubleNear(test.lng().degrees(), 0.98765);
  }

  // Equal and same sign
  private static void assertIdentical(double a, double b) {
    // Double.valueOf(0.0) != Double.valueOf(-0.0), although 0.0d == -0.0d.
    assertEquals(Double.valueOf(a), Double.valueOf(b));
  }

  public void testNegativeZeros() {
    // Equal and same sign
    assertIdentical(S2LatLng.latitude(new S2Point(1., 0., -0.)).radians(), +0.);
    assertIdentical(S2LatLng.longitude(new S2Point(1., -0., 0.)).radians(), +0.);
    assertIdentical(S2LatLng.longitude(new S2Point(-1., -0., 0.)).radians(), Math.PI);
    assertIdentical(S2LatLng.longitude(new S2Point(-0., 0., 1.)).radians(), +0.);
    assertIdentical(S2LatLng.longitude(new S2Point(-0., -0., 1.)).radians(), +0.);
  }

  public void testDistance() {
    assertEquals(
        0.0, S2LatLng.fromDegrees(90, 0).getDistance(S2LatLng.fromDegrees(90, 0)).radians());
    assertDoubleNear(
        S2LatLng.fromDegrees(-37, 25).getDistance(S2LatLng.fromDegrees(-66, -155)).degrees(),
        77,
        1e-13);
    assertDoubleNear(
        S2LatLng.fromDegrees(0, 165).getDistance(S2LatLng.fromDegrees(0, -80)).degrees(),
        115,
        1e-13);
    assertDoubleNear(
        S2LatLng.fromDegrees(47, -127).getDistance(S2LatLng.fromDegrees(-47, 53)).degrees(),
        180,
        2e-6);
  }

  @GwtIncompatible("GeometryTestCase.encodeDecode")
  public void testSerializable() throws Exception {
    S2LatLng latLong = S2LatLng.fromDegrees(-37, 25);
    assertEquals(latLong, encodeDecode(latLong));
  }
}
