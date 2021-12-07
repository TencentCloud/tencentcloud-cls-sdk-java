package com.tencentcloudapi.cls;

import com.tencentcloudapi.cls.producer.errors.ProducerException;
import org.junit.Test;

public class AsyncProducerClientTest {

    @Test
    public void testAsyncProducerClient() throws ProducerException, InterruptedException {
//        String endpoint = "ap-guangzhou.cls.tencentcs.com";
//        // API密钥 secretId，必填
//        String secretId = "";
//        // API密钥 secretKey，必填
//        String secretKey = "";
//        // 日志主题ID，必填
//        String topicId = "";
//
//        final AsyncProducerConfig config = new AsyncProducerConfig(endpoint, secretId, secretKey, NetworkUtils.getLocalMachineIP());
//
//        // 构建一个客户端实例
//        final AsyncProducerClient client = new AsyncProducerClient(config);
//
//        for (int i = 0; i < 10000; ++i) {
//            List<LogItem> logItems = new ArrayList<>();
//            int ts = (int) (System.currentTimeMillis() / 1000);
//            LogItem logItem = new LogItem(ts);
//            logItem.PushBack(new LogContent("__CONTENT__", "你好，我来自深圳|hello world"));
//            logItem.PushBack(new LogContent("city", "guangzhou"));
//            logItem.PushBack(new LogContent("logNo", Integer.toString(i)));
//            logItem.PushBack(new LogContent("__PKG_LOGID__", (String.valueOf(System.currentTimeMillis()))));
//            logItems.add(logItem);
//            client.putLogs(topicId, logItems, result -> System.out.println(result.toString()));
//        }
//        client.close();
    }
}
