//package com.study.batch_demo.reader;
//
//import com.study.batch_demo.muses.MultiFileJoiner;
//import org.springframework.batch.item.ItemReader;
//import org.springframework.batch.item.NonTransientResourceException;
//import org.springframework.batch.item.ParseException;
//import org.springframework.batch.item.UnexpectedInputException;
//import org.springframework.batch.item.file.transform.FieldSet;
//import org.springframework.batch.item.file.transform.LineTokenizer;
//
///**
// * @author zhudan <zhudan@ebchinatech.com>
// * @since 2021/1/28 15:09
// */
//public class MultiFileItemReader implements ItemReader<FieldSet> {
//
//    final MultiFileJoiner fileJoiner;
//
//    final LineTokenizer tokenizer;
//
//    @Override
//    public FieldSet read() throws Exception, UnexpectedInputException, ParseException,
//            NonTransientResourceException {
//        String line = fileJoiner.nextLine();
//        if (line == null) {
//            return null;
//        }
//        FieldSet result = tokenizer.tokenize(line);
//        return result;
//    }
//
//    public MultiFileItemReader(MultiFileJoiner fileJoiner, LineTokenizer tokenizer) {
//        this.fileJoiner = fileJoiner;
//        this.tokenizer = tokenizer;
//    }
//}
