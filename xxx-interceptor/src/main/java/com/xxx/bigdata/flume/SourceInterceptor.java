package com.xxx.bigdata.flume;


import com.sun.org.apache.xpath.internal.operations.Bool;
import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.interceptor.Interceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;


/**
 * 需求:
 * 读取配置文件
 * 确保环境和来源已经通过配置
 */
public class SourceInterceptor implements Interceptor {

    private final String source;
    private final String env;
    private final Boolean preservingExists;
    private final Logger logger = LoggerFactory.getLogger(SourceInterceptor.class);


    private SourceInterceptor(String source, String env, Boolean preservingExists) {
        this.source = source;
        this.env = env;
        this.preservingExists = preservingExists;
    }


    /**
     * 初始化方法
     */
    @Override
    public void initialize() {
        // do nothing
    }

    /**
     * 拦截event
     * 在 header 中增加 source 和 env 信息
     * @param event
     * @return
     */
    @Override
    public Event intercept(Event event) {
        Map<String, String> headers = event.getHeaders();
        headers.put("source", source);
        headers.put("env", env);
        return event;
    }

    @Override
    public List<Event> intercept(List<Event> events) {
        List<Event> result = new LinkedList<>();
        for (Event event : events) {
            result.add(intercept(event));
        }
        return result;
    }

    @Override
    public void close() {
        // do nothing
    }

    public static class Builder implements Interceptor.Builder {
        private String source;
        private String env;
        private Boolean preservingExists;

        @Override
        public Interceptor build() {
            return new SourceInterceptor(source, env, preservingExists);
        }

        /**
         * 从配置文件中读取数据
         * @param context
         */
        @Override
        public void configure(Context context) {
            source = context.getString(Constants.SOURCE, Constants.SOURCE_DEFAULT);
            env = context.getString(Constants.ENV, Constants.ENV_DEFAULT);
            preservingExists = context.getBoolean(Constants.PRESERVE, Constants.PRESERVE_DEFAULT);
        }
    }

    public static class Constants {
        private static final String SOURCE="source";
        private static final String SOURCE_DEFAULT = "file";

        private static final String ENV = "env";
        private static final String ENV_DEFAULT = "dev";

        private static final String PRESERVE = "preserve";
        private static final Boolean PRESERVE_DEFAULT = true;
    }
}
