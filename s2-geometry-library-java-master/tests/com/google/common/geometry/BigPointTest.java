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

import com.google.common.annotations.GwtCompatible;
import java.math.BigDecimal;
import junit.framework.TestCase;

/** Unit tests for {@link BigPoint}. */
@GwtCompatible
public class BigPointTest extends TestCase {
  public void testCrossProd() {
    BigPoint a = createBp("1.2", "3.4", "5.6");
    BigPoint b = createBp("-7.8", "9.0", "-1.2");
    assertEquals(createBp("-54.48", "-42.24", "37.32"), a.crossProd(b));
    assertEquals(createBp("54.48", "42.24", "-37.32"), b.crossProd(a));
  }

  public void testDotProd() {
    BigPoint a = createBp("1.2", "3.4", "5.6");
    BigPoint b = createBp("-7.8", "9.0", "-1.2");
    assertEquals(new BigDecimal("14.52"), a.dotProd(b));
    assertEquals(new BigDecimal("14.52"), b.dotProd(a));
  }

  private static BigPoint createBp(String x, String y, String z) {
    return new BigPoint(new BigDecimal(x), new BigDecimal(y), new BigDecimal(z));
  }
}
