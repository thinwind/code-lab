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

import java.util.Arrays;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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
    private final BufferedReader[] readers;
    private final String[] bufferLines;
    private final boolean[] readerEnded;
    private boolean end;
    private final LineMatcher lineMatcher;

    public MuiltiFileJoiner(String[] files, LineMatcher lineMatcher) throws FileNotFoundException {
        this.files = files;
        readers = new BufferedReader[files.length];
        bufferLines = new String[files.length];
        readerEnded = new boolean[files.length];
        end = false;
        this.lineMatcher = lineMatcher;
        for (int i = 0; i < files.length; i++) {
            readers[i] = new BufferedReader(new FileReader(files[i]));
        }
    }

    public String nextLine() throws IOException {
        String tmpLine = readers[0].readLine();
        if ( tmpLine == null) {
            closeReaderSilently(readers[0]);
            end=true;
            return null;
        }
        bufferLines[0] = tmpLine;
        
        for (int i = 1; i < readers.length; i++) {
            tmpLine = null;
            if (bufferLines[i] != null) {
                continue;
            }
            if(!readerEnded[i]){
                tmpLine = readers[i].readLine();
                if (tmpLine == null) {
                    readerEnded[i]=true;
                    closeReaderSilently(readers[i]);
                    continue;
                }
                bufferLines[i]=tmpLine;
            }
        }
        
        MatchResult result = lineMatcher.matchLines(Arrays.copyOf(bufferLines, bufferLines.length));
        boolean[] matchedDetails = result.getMatchedDetails();
        for (int i = 0; i < matchedDetails.length; i++) {
            if(matchedDetails[i]){
                bufferLines[i+1] = null;
            }
        }
        return result.getJoinedLine();
    }

    public boolean isEnd() {
        return end;
    }

    public void complete() {
        for (Reader reader : readers) {
            closeReaderSilently(reader);
        }
    }

    private void closeReaderSilently(Reader reader){
        try {
            reader.close();
        } catch (Exception e) {
            //just print the stack and do nothing
            e.printStackTrace();
        }
    }

    public String[] getFiles() {
        return files.clone();
    }
}
