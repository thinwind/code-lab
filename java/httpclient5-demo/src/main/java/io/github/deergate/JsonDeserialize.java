package io.github.deergate;

import java.io.IOException;
import java.io.Reader;

/**
 * 此接口主要用于json反序列化工具解耦
 *
 * 目前有很多的json库，为了避免依赖具体的框架
 * 引入此接口，可以根据自己的项目实际需要实现
 * 目前主流的json框架都会有类似的功能实现，此
 * 接口看似专有，实际上是非常通用的，实现的成本
 * 非常低
 *
 * @param <T> 反序列化类型，一般是BullShitData
 */
public interface JsonDeserialize<T> {

    T deserialize(Reader reader,Class<T> cls) throws IOException;

}
