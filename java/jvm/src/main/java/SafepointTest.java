/* 
 * Copyright 2021 Shang Yehua <niceshang@outlook.com>
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
/**
 *
 * TODO SafepointTest说明
 *
 * @author Shang Yehua <niceshang@outlook.com>
 * @since 2021-04-05  09:42
 *
 */

// time java SafepointTestp
// 你还可以使用如下几个选项
// -XX:+PrintGC
// -XX:+PrintGCApplicationStoppedTime 
// -XX:+PrintSafepointStatistics
// -XX:+UseCountedLoopSafepoints
public class SafepointTest {
  static double sum = 0;

  public static void foo() {
    for (int i = 0; i < 0x77777777; i++) {
      sum += Math.sqrt(i);
    }
  }

  public static void bar() {
    for (int i = 0; i < 50_000_000; i++) {
      new Object().hashCode();
    }
  }

  public static void main(String[] args) {
    // new Thread(SafepointTest::foo).start();
    new Thread(SafepointTest::bar).start();
  }
}