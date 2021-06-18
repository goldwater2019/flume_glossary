# flume_glossary

## interceptor

### 自定义拦截器

#### 可以配置数据的来源和环境, 用于后期聚合时的区分
* `a1.sources.r1.interceptors.i1.source = nginx`
    * 表示收集到的日志的来源是 nginx
* `a1.sources.r1.interceptors.i1.env = dev`
    * 表示收集到的日志的环境是 dev