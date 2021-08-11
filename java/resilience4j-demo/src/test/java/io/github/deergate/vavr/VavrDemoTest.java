/*
 * Copyright 2021 Shang Yehua <niceshang@outlook.com>
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package io.github.deergate.vavr;

import org.junit.Test;
import io.vavr.Function0;
import io.vavr.Function1;
import io.vavr.Function2;
import io.vavr.Function4;
import io.vavr.Function5;
import io.vavr.Tuple;
import io.vavr.control.Option;

/**
 *
 * TODO VavrDemoTest说明
 *
 * @author Shang Yehua <niceshang@outlook.com>
 * @since 2021-08-10  17:04
 *
 */
public class VavrDemoTest {

    @Test
    public void testTuple() {
        var tuple = Tuple.of("java", 8);
        System.out.println(tuple._1);
        System.out.println(tuple._2);

        var that = tuple.map(s -> s.substring(2) + "vr", i -> i / 8);

        System.out.println(that);

        var other = tuple.map((s, i) -> Tuple.of(s + "ya", i / 2));
        System.out.println(other);

        var str = tuple.apply((s, i) -> s.substring(2) + "vr " + i / 8);
        System.out.println(str);
    }

    @Test
    public void funComposition() {
        Function1<Integer, Integer> plusOne = a -> a + 1;
        Function1<Integer, Integer> multiplyByTwo = a -> a * 2;

        Function1<Integer, Integer> add1AndMultiplyByTwo = plusOne.andThen(multiplyByTwo);
        System.out.println(add1AndMultiplyByTwo.apply(2));

        Function1<Integer, Integer> plus1ThenMultiplyByTwo = multiplyByTwo.compose(plusOne);
        System.out.println(plus1ThenMultiplyByTwo.apply(2));
    }

    @Test
    public void funLift() {
        Function2<Integer, Integer, Integer> divide = (a, b) -> a / b;
        Function2<Integer, Integer, Option<Integer>> safeDivide = Function2.lift(divide);
        var r1 = safeDivide.apply(2, 0);
        System.out.println(r1);

        var r2 = safeDivide.apply(4, 2);
        System.out.println(r2);

        var safeSum = Function2.lift(this::sum);
        var none = safeSum.apply(-1, 2);
        System.out.println(none);
    }

    int sum(int first, int second) {
        if (first < 0 || second < 0) {
            throw new IllegalArgumentException("Only positive integers are allowed");
        }
        return first + second;
    }

    @Test
    public void partialApplication() {
        Function2<Integer, Integer, Integer> sum = (a, b) -> a + b;
        Function1<Integer, Integer> add2 = sum.apply(2);
        System.out.println(add2.apply(8));

        Function5<Integer, Integer, Integer, Integer, Integer, Integer> sum5 =
                (a, b, c, d, e) -> a + b + c + d + e;
        Function2<Integer, Integer, Integer> add6 = sum5.apply(2, 3, 1);

        System.out.println(add6.apply(2, 2));
    }

    @Test
    public void currying() {
        Function2<Integer, Integer, Integer> sum = (a, b) -> a + b;
        Function1<Integer, Integer> add1 = sum.curried().apply(1);
        System.out.println(add1.apply(2));

        Function4<Integer, Integer, Integer, Integer, Integer> sum4 = (a, b, c, d) -> a + b + c + d;
        Function1<Integer, Function1<Integer, Function1<Integer, Integer>>> plusOne =
                sum4.curried().apply(1);
        System.out.println(plusOne.apply(2).apply(3).apply(4));
    }

    @Test
    public void memoized() {
        Function0<Double> hashCache = Function0.of(Math::random).memoized();
        System.out.println(hashCache.isMemoized());
    }

    public static void main(String[] args) {
        Test2 t=(Test2 & TaggedInterface) ()->2;
        System.out.println(t instanceof TaggedInterface);
    }
}

interface Test2 {
    Integer test();
}