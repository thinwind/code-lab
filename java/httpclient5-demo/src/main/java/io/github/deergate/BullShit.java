package io.github.deergate;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.ThreadLocalRandom;


public class BullShit {

    BullShitData data;

    String[] famous;

    String[] bosh;

    String[] after;

    String[] before;

    private final JsonDeserialize<BullShitData> deserializer;

    public BullShit(JsonDeserialize<BullShitData> deserializer) throws IOException {
        this.deserializer = deserializer;
        init();
    }


    public void init() throws IOException {
        InputStream inputStream = BullShit.class.getResourceAsStream("/bullshit/data.json");
        InputStreamReader reader = new InputStreamReader(inputStream, "UTF-8");
        data = deserializer.deserialize(reader, BullShitData.class);
        inputStream.close();
        famous = data.famous;
        bosh = data.bosh;
        after = data.after;
        before = data.before;
    }

    /**
     * 生成关于title的狗屁不通文章
     *
     * @param title  标题
     * @param length 正文长度
     * @return content 正文
     */
    public String generator(String title, int length) {
        StringBuilder builder = new StringBuilder();
        while (builder.length() < length) {
            //获取100以内的随机整数
            ThreadLocalRandom random = ThreadLocalRandom.current();
            int rand = random.nextInt(100);
            if (rand < 10) {
                builder.append("\n");
            } else if (rand < 20) {
                String content = famous[random.nextInt(famous.length)];
                content = content.replace("a", before[random.nextInt(before.length)])
                        .replace("b", after[random.nextInt(after.length)]);
                builder.append(content);
            } else {
                String content = bosh[random.nextInt(bosh.length)];
                content = content.replace("x", title);
                builder.append(content);
            }
        }
        return builder.toString();
    }
}
