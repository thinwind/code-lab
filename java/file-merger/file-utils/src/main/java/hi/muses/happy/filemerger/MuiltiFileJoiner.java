/*
 * Copyright 2021 Shang Yehua
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
package hi.muses.happy.filemerger;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;

/**
 *
 * 多文件合并成一个文件
 *
 * @author Shang Yehua <niceshang@outlook.com>
 * @since 2021-00-04  20:51
 *
 */
public class MuiltiFileJoiner {
    private final String[] files;
    private final Reader[] readers;
    private final int bufferSize;
    private final String[] bufferLines;
    private boolean end;

    public MuiltiFileJoiner(String[] files,int bufferSize) throws FileNotFoundException {
        this.files = files;
        this.bufferSize = bufferSize;
        readers = new Reader[files.length];
        bufferLines = new String[files.length];
        for (int i = 0; i < files.length; i++) {
            readers[i] = new BufferedReader(new FileReader(files[i]));
        }
    }
    
    public boolean
    
    public void complete() {
        for (Reader reader : readers) {
            try {
                reader.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

}
