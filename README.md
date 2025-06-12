Tencent CLoud CLS JAVA SDK
---

"tencent cloud cls log sdk" 是专门为cls量身打造日志上传SDK。 一切只为满足您的需求～

### USAGE

```
    <dependency>
      <groupId>com.tencentcloudapi.cls</groupId>
      <artifactId>tencentcloud-cls-sdk-java</artifactId>
      <version>1.0.15</version>
    </dependency>
```

### 为什么要使用CLS Log SDK

- 异步发送：发送日志立即返回，无须等待，支持传入callback function。
- 优雅关闭：通过调用close方法，producer会将所有其缓存的数据进行发送，防止日志丢失。
- 感知每一条日志的成功状态： 用户可以自定义CallBack方法的实现，监控每一条日志的状态
- 使用简单： 通过简单配置，就可以实现复杂的日志上传聚合、失败重试等逻辑
- 失败重试： 429、500 等服务端错误，都会进行重试， 401、403、404 默认不重试

### CLS Host

endpoint填写请参考[可用地域](https://cloud.tencent.com/document/product/614/18940#.E5.9F.9F.E5.90.8D)中 **API上传日志** Tab中的域名（也可以选择地域与网络环境类型自动生成。如：Constants.Region.BEIJING, Constants.NetworkType.Extranet）![image-20230403191435319](https://github.com/TencentCloud/tencentcloud-cls-sdk-js/blob/main/demo.png)

### 密钥信息

secretId和secretKey为云API密钥，密钥信息获取请前往[密钥获取](https://console.cloud.tencent.com/cam/capi)。并请确保密钥关联的账号具有相应的[SDK上传日志权限](https://cloud.tencent.com/document/product/614/68374#.E4.BD.BF.E7.94.A8-api-.E4.B8.8A.E4.BC.A0.E6.95.B0.E6.8D.AE)


### Demo

```
public static void main(String[] args) {
        String endpoint = "ap-guangzhou.cls.tencentcs.com";  【使用日志服务API2017的域名】
        // API密钥 secretId，必填
        String secretId = "";
        // API密钥 secretKey，必填
        String secretKey = "";
        // 日志主题ID，必填
        String topicId = "";

        final AsyncProducerConfig config = new AsyncProducerConfig(endpoint, secretId, secretKey, NetworkUtils.getLocalMachineIP());

        // 构建一个客户端实例
        final AsyncProducerClient client = new AsyncProducerClient(config);

        for (int i = 0; i < 10000; ++i) {
            List<LogItem> logItems = new ArrayList<>();
            int ts = (int) (System.currentTimeMillis() / 1000);
            LogItem logItem = new LogItem(ts);
            logItem.PushBack(new LogContent("__CONTENT__", "你好，我来自深圳|hello world"));
            logItem.PushBack(new LogContent("city", "guangzhou"));
            logItem.PushBack(new LogContent("logNo", Integer.toString(i)));
            logItem.PushBack(new LogContent("__PKG_LOGID__", (String.valueOf(System.currentTimeMillis()))));
            logItems.add(logItem);
            client.putLogs(topicId, logItems, result -> System.out.println(result.toString()));
        }
        client.close();
}
```

### 配置参数详解

| 参数                | 类型   | 描述                                                         |
| ------------------- | ------ | ------------------------------------------------------------ |
| TotalSizeInBytes    | Int64  | 实例能缓存的日志大小上限，默认为 100MB。       |
| MaxSendThreadCount  | Int64  | client能并发的最多"goroutine"的数量，默认为50 |
| MaxBlockSec         | Int    | 如果client可用空间不足，调用者在 send 方法上的最大阻塞时间，默认为 60 秒。<br/>如果超过这个时间后所需空间仍无法得到满足，send 方法会抛出TimeoutException。如果将该值设为0，当所需空间无法得到满足时，send 方法会立即抛出 TimeoutException。如果您希望 send 方法一直阻塞直到所需空间得到满足，可将该值设为负数。 |
| MaxBatchSize        | Int64  | 当一个Batch中缓存的日志大小大于等于 batchSizeThresholdInBytes 时，该 batch 将被发送，默认为 512 KB，最大可设置成 5MB。 |
| MaxBatchCount       | Int    | 当一个Batch中缓存的日志条数大于等于 batchCountThreshold 时，该 batch 将被发送，默认为 4096，最大可设置成 40960。 |
| LingerMs            | Int64  | Batch从创建到可发送的逗留时间，默认为 2 秒，最小可设置成 100 毫秒。 |
| Retries             | Int    | 如果某个Batch首次发送失败，能够对其重试的次数，默认为 10 次。<br/>如果 retries 小于等于 0，该 ProducerBatch 首次发送失败后将直接进入失败队列。 |
| MaxReservedAttempts | Int    | 每个Batch每次被尝试发送都对应着一个Attemp，此参数用来控制返回给用户的 attempt 个数，默认只保留最近的 11 次 attempt 信息。<br/>该参数越大能让您追溯更多的信息，但同时也会消耗更多的内存。 |
| BaseRetryBackoffMs  | Int64  | 首次重试的退避时间，默认为 100 毫秒。 client采样指数退避算法，第 N 次重试的计划等待时间为 baseRetryBackoffMs * 2^(N-1)。 |
| MaxRetryBackoffMs   | Int64  | 重试的最大退避时间，默认为 50 秒。                           |


### feature


 

