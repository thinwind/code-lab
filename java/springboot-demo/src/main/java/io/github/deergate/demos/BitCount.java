/* 
 * Copyright 2022 Shang Yehua <niceshang@outlook.com>
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
package io.github.deergate.demos;

/**
 *
 * TODO BitCount说明
 *
 * @author Shang Yehua <niceshang@outlook.com>
 * @since 2022-07-20  15:45
 *
 */
public class BitCount {
    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            System.out.println(i+": "+Integer.toBinaryString(i)+" :"+count((byte)i));
        }
    }
    
    static int count(byte b){
        int num=0;
        while(b!=0){
            b = (byte) (b & (b-1));
            num ++;
        }
        return num;
    }
    
}
