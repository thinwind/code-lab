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

import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.collection.Seq;
import io.vavr.collection.Vector;
import io.vavr.control.Option;

/**
 *
 * TODO Palindromes说明
 *
 * @author Shang Yehua <niceshang@outlook.com>
 * @since 2021-08-03  09:45
 *
 */
public class Palindromes {
    static Seq<Tuple2<Integer, Integer>> substringPalindromes(String s) {
        final int n = s.length();
        return Vector.range(0, n - 1)
                .flatMap(i -> Vector.of(Tuple.of(i, i + 1), Tuple.of(i, i + 2)))
                .flatMap(startIdx -> Vector.unfold(startIdx, currIdx -> {
                    final int i = currIdx._1;
                    final int j = currIdx._2;
                    if (0 <= i && j < n && isPalindrome(s, i, j)) {
                        final Tuple2<Integer, Integer> nextIdx = Tuple.of(i - 1, j + 1);
                        return Option.some(Tuple.of(nextIdx, currIdx));
                    } else {
                        return Option.none();
                    }
                }));
    }

    static private boolean isPalindrome(String s, int i, int j) {
        return s.substring(i, i + 1).equals(s.substring(j, j + 1));
    }
    
    public static void main(String[] args) {
        Seq<Tuple2<Integer, Integer>> result=substringPalindromes("sabbath");
        for (Tuple2<Integer,Integer> tuple2 : result) {
            System.out.println("sabbath".substring(tuple2._1, tuple2._2+1));
        }
    }
}
