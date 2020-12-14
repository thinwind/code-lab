/*
 * Copyright 2020 Shang Yehua
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
package me.shangyh.codelab.nio;

import java.lang.reflect.InvocationTargetException;
import java.nio.ByteBuffer;

/**
 *
 * TODO DirectGCTest说明
 *
 * @author Shang Yehua <niceshang@outlook.com>
 * @since 2020-11-04  15:40
 *
 */
public class DirectGCTest {
    public static void main(String[] args)
            throws InterruptedException, NoSuchMethodException, SecurityException,
            IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(Integer.MAX_VALUE);
        byte[] filling = {1};
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            byteBuffer.put(filling);
        }

        System.out.println("put ended.");
        Thread.sleep(5000);
        var cleanerMethod = byteBuffer.getClass().getMethod("cleaner");
        cleanerMethod.setAccessible(true);
        var cleaner = cleanerMethod.invoke(byteBuffer);
        var cleanMethod = cleaner.getClass().getMethod("clean");
        cleanMethod.setAccessible(true);
        cleanMethod.invoke(cleaner);
        System.out.println("clean complete");
        Thread.sleep(20000);
        System.out.println("exiting...");
    }
}
