package com.study.batch_demo.muses;

import org.springframework.batch.item.*;
import org.springframework.batch.item.file.transform.FieldSet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Arrays;
import java.util.StringJoiner;

public class MultiFileReader extends ItemStreamSupport implements
        ItemReader<FieldSet> {

    private final static String POS_KEY = "MULTI_FILE_READER_POS_KEY";

    private final String[] files;
    private final BufferedReader[] readers;
    private final String[] bufferLines;
    private final boolean[] readerEnded;
    private final LineMatcher<FieldSet> lineMatcher;
    private final String charset;
    private final FileLoader fileLoader;
    private final long[] readerPosArray;
    private final int lineBreakerLength;

    private int counter = 0;


    public MultiFileReader(String[] files, LineMatcher<FieldSet> lineMatcher,
                           String charset, int lineBreakerLength, FileLoader fileLoader) throws IOException {
        this.files = files;
        this.lineMatcher = lineMatcher;
        this.charset = charset;
        this.fileLoader = fileLoader;
        this.lineBreakerLength = lineBreakerLength;
        this.readerPosArray = new long[files.length];
        bufferLines = new String[files.length];
        readerEnded = new boolean[files.length];
        readers = new BufferedReader[files.length];
        initReaders();
    }

    private void initReaders() throws IOException {
        for (int i = 0; i < files.length; i++) {
            readers[i] = new BufferedReader(
                    new InputStreamReader(this.fileLoader.loadFile(files[i]), charset));
        }
    }

    @Override
    public FieldSet read() throws UnexpectedInputException, ParseException, NonTransientResourceException, IOException {
        //Mock for error happens
//        if(counter == 11){
//            throw new UnexpectedInputException("just for test");
//        }
        counter++;

        String tmpLine = bufferLines[0];
        boolean useDriverBuffer;
        if (tmpLine == null) {
            tmpLine = readers[0].readLine();
            if (tmpLine == null) {
                closeReaderSilently(readers[0]);
                return null;
            }
            bufferLines[0] = tmpLine;
            readerPosArray[0] += tmpLine.length() + lineBreakerLength;
            useDriverBuffer = false;
        } else {
            useDriverBuffer = true;
        }

        for (int i = 1; i < readers.length; i++) {
            if (bufferLines[i] != null) {
                continue;
            }
            if (!readerEnded[i]) {
                tmpLine = readers[i].readLine();
                if (tmpLine == null) {
                    readerEnded[i] = true;
                    closeReaderSilently(readers[i]);
                    continue;
                }
                bufferLines[i] = tmpLine;
                readerPosArray[i] += tmpLine.length() + lineBreakerLength;
            }
        }

        MatchResult<FieldSet> result = lineMatcher.matchLines(Arrays.copyOf(bufferLines, bufferLines.length));
        boolean[] matchedDetails = result.getMatchedDetails();
        if (useDriverBuffer && allSlaveNotMatched(matchedDetails)) {
            bufferLines[0] = null;
            return read();
        }
        if (!matchedDetails[0]) {
            bufferLines[0] = null;
        }
        for (int i = 1; i < matchedDetails.length; i++) {
            if (matchedDetails[i]) {
                bufferLines[i] = null;
            }
        }
        return result.getJoinedLine();
    }

    private boolean allSlaveNotMatched(boolean[] matchedDetails) {
        boolean result = true;
        for (int i = 1; i < matchedDetails.length; i++) {
            result = result && !matchedDetails[i];
        }
        return result;
    }

    public void close() throws ItemStreamException {
        for (Reader reader : readers) {
            closeReaderSilently(reader);
        }
    }

    public void open(ExecutionContext executionContext) throws ItemStreamException {
        if (executionContext.containsKey(getExecutionContextKey(POS_KEY))) {
            String posString = executionContext.getString(getExecutionContextKey(POS_KEY));
            resetReaderPosArray(posString);
            try {
                skipProccessedLines();
            } catch (IOException e) {
                throw new ItemStreamException(e);
            }
        }
    }

    private void skipProccessedLines() throws IOException {
        for (int i = 0; i < readers.length; i++) {
            readers[i].skip(readerPosArray[i]);
        }
    }

    public void update(
            ExecutionContext executionContext) throws ItemStreamException {
        executionContext.putString(getExecutionContextKey(POS_KEY), readerPosArrayToString());
    }

    private String readerPosArrayToString() {
        StringJoiner joiner = new StringJoiner(",");
        for (long i : readerPosArray) {
            joiner.add(String.valueOf(i));
        }
        return joiner.toString();
    }

    private void resetReaderPosArray(String posString) {
        String[] posStrArray = posString.split(",");
        for (int i = 0; i < posStrArray.length; i++) {
            readerPosArray[i] = Long.parseLong(posStrArray[i]);
        }
    }

    private void closeReaderSilently(Reader reader) {
        try {
            reader.close();
        } catch (Exception e) {
            //just print the stack and do nothing
            e.printStackTrace();
        }
    }
}
