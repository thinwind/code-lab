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
package io.github.deergate.serializable;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Paths;

/**
 *
 * TODO TestRunLambda说明
 *
 * @author Shang Yehua <niceshang@outlook.com>
 * @since 2021-09-15  10:33
 *
 */
public class TestRunLambda {

    public static void main(String[] args)
            throws FileNotFoundException, IOException, ClassNotFoundException, InterruptedException, ExecutionException {
        Map<String, Object> data = new HashMap<>();
        data.put("k", "v");
        data.put("Miss", "zz");
        data.put("Years", 99);
        SerializableRunnable obj = new SerializableRunnable();
        System.out.println(obj);
        Runnable runnable = (Runnable & Serializable) () -> {
            System.out.println(obj);
            System.out.println(data);
        };

        ObjectOutputStream out = new ObjectOutputStream(
                new FileOutputStream(new File("/Users/shangyehua/tmp/test.dat")));
        out.writeObject(runnable);

        data.clear();
        data.put("be", "with u");


        ObjectInputStream in = new ObjectInputStream(
                new FileInputStream(new File("/Users/shangyehua/tmp/test.dat")));

        Runnable r = (Runnable) in.readObject();

        ExecutorService service = Executors.newFixedThreadPool(1);
        Future<?> futrue = service.submit(r);
        System.out.println(futrue.get());
    }
}


class SerializableRunnable implements Serializable {

    Map<String, Object> data;

    private Map<String, Object> getData() {
        return data;
    }

}
