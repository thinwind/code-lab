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
 * TODO Passenger说明
 *
 * @author Shang Yehua <niceshang@outlook.com>
 * @since 2021-03-31  07:41
 *
 */

// Run with: java -XX:CompileCommand='dontinline,*.passThroughImmigration' Passenger
public abstract class Passenger {
    abstract void passThroughImmigration();

    public static void main(String[] args) {
        Passenger a = new ChinesePassenger();
        Passenger b = new ForeignerPassenger();
        long current = System.currentTimeMillis();
        for (int i = 1; i <= 2_000_000_000; i++) {
            if (i % 100_000_000 == 0) {
                long temp = System.currentTimeMillis();
                System.out.println(temp - current);
                current = temp;
            }
            Passenger c = (i < 1_000_000_000) ? a : b;
            c.passThroughImmigration();
        }
    }
}

class ChinesePassenger extends Passenger {
    @Override
    void passThroughImmigration() {
    }
}

class ForeignerPassenger extends Passenger {
    @Override
    void passThroughImmigration() {
    }
}
