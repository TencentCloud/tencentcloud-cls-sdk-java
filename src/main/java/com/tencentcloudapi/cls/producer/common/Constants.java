package com.tencentcloudapi.cls.producer.common;


/**
 * @author farmerx
 */
public class Constants {
    public enum CompressType {
        NONE(""),
        LZ4(Constants.CONST_LZ4),
        GZIP(Constants.CONST_GZIP_ENCODING);

        private String strValue;

        CompressType(String strValue) {
            this.strValue = strValue;
        }

        public String toString() {
            return strValue;
        }

        public static CompressType fromString(final String compressType) {
            for (CompressType type : values()) {
                if (type.strValue.equals(compressType)) {
                    return type;
                }
            }
            throw new IllegalArgumentException("invalid CompressType: " + compressType + ", should be (" + CompressType.NONE + ", " + CompressType.GZIP + ", " + CompressType.LZ4 + ")");
        }
    }

    /**
     * 地域
     */
    public static class Region {
        public static final Region BEIJING = new Region("ap-beijing", null);
        public static final Region GUANGZHOU = new Region("ap-guangzhou", "cls-ap-guangzhou-dataflow");
        public static final Region SHANGHAI = new Region("ap-shanghai", null);
        public static final Region CHENGDU = new Region("ap-chengdu", null);
        public static final Region NANJING = new Region("ap-nanjing", null);
        public static final Region CHONGQING = new Region("ap-chongqing", null);
        public static final Region HONGKONG = new Region("ap-hongkong", null);
        public static final Region SILICONVALLEY = new Region("na-siliconvalley", null);
        public static final Region ASHBURN = new Region("na-ashburn", null);
        public static final Region SINGAPORE = new Region("ap-singapore", null);
        public static final Region BANGKOK = new Region("ap-bangkok", null);
        public static final Region FRANKFURT = new Region("eu-frankfurt", null);
        public static final Region TOKYO = new Region("ap-tokyo", null);
        public static final Region SEOUL = new Region("ap-seoul", null);
        public static final Region JAKARTA = new Region("ap-jakarta", null);
        public static final Region SAOPAULO = new Region("sa-saopaulo", null);
        public static final Region SHENZHEN_FSI = new Region("ap-shenzhen-fsi", null);
        public static final Region SHANGHAI_FSI = new Region("ap-shanghai-fsi", null);
        public static final Region BEIJING_FSI = new Region("ap-beijing-fsi", null);
        public static final Region SHANGHAI_ADC = new Region("ap-shanghai-adc", null);

        private final String endpointPrefix;
        private final String polarisService;

        public Region(String endpointPrefix, String polarisService) {
            this.endpointPrefix = endpointPrefix;
            this.polarisService = polarisService;
        }

        /**
         * 支持自定义地域
         * @param endpointPrefix
         * @return
         */
        public static Region of(String endpointPrefix, String polarisService) {
            return new Region(endpointPrefix, polarisService);
        }

        public String toString() {
            return endpointPrefix + ":" + (polarisService == null ? "" : polarisService);
        }

        public String getEndpointPrefix() {
            return endpointPrefix;
        }

        public String getPolarisService() {
            return polarisService;
        }

    }

    /**
     * 网络类型
     */
    public enum NetworkType{
        Intranet("cls.tencentyun.com"), //内网
        Extranet("cls.tencentcs.com");  //外网

        private final String endpointSuffix;

        NetworkType(String endpointPrefix) {
            this.endpointSuffix = endpointPrefix;
        }

        public String toString() {
            return endpointSuffix;
        }

        public String getEndpointSuffix() {
            return endpointSuffix;
        }
    }

    /**
     * polaris接入点namespace
     */
    public static class PolarisNamespace {
        public static final PolarisNamespace Production = new PolarisNamespace("Production");

        private final String namespace;

        public PolarisNamespace(String namespace) {
            this.namespace = namespace;
        }

        public String toString() {
            return namespace;
        }

        public String getNamespace() {
            return namespace;
        }
    }

    public static int CONST_MAX_PUT_SIZE = 1 * 1024 * 1024;
    public static final String CONST_X_SLS_COMPRESSTYPE = "x-cls-compress-type";
    public static final String CONST_LZ4 = "lz4";

    public static final String CONST_CONTENT_TYPE = "Content-Type";
    public static final String CONST_PROTO_BUF = "application/x-protobuf";
    public static final String CONST_CONTENT_LENGTH = "Content-Length";
    public static final String CONST_AUTHORIZATION = "Authorization";
    public static final String CONST_GZIP_ENCODING = "deflate";
    public static final String CONST_X_SLS_REQUESTID = "x-cls-requestid";
    public static final String CONST_HOST = "Host";
    public static final String CONST_MD5 = "MD5";
    public static final String UTF_8_ENCODING = "UTF-8";
    public static final String CONST_LOCAL_IP = "127.0.0.1";
    public static int HTTP_CONNECT_TIME_OUT = 60 * 1000;
    public static int HTTP_SEND_TIME_OUT = 60 * 1000;
    public static final String TOPIC_ID = "topic_id";
    public static final String UPLOAD_LOG_RESOURCE_URI = "/structuredlog";
    public static final int DEFAULT_TOTAL_SIZE_IN_BYTES = 100 * 1024 * 1024;
    public static final long DEFAULT_MAX_BLOCK_MS = 60 * 1000L;
    public static final int DEFAULT_SEND_THREAD_COUNT =
            Math.max(Runtime.getRuntime().availableProcessors(), 1);
    public static final int DEFAULT_BATCH_SIZE_THRESHOLD_IN_BYTES = 512 * 1024;
    public static final int MAX_BATCH_SIZE_IN_BYTES = 5 * 1024 * 1024;
    public static final int DEFAULT_BATCH_COUNT_THRESHOLD = 4096;
    public static final int MAX_BATCH_COUNT = 10000;
    public static final int DEFAULT_LINGER_MS = 2000;
    public static final int LINGER_MS_LOWER_LIMIT = 100;
    public static final int DEFAULT_RETRIES = 10;
    public static final long DEFAULT_BASE_RETRY_BACKOFF_MS = 100L;
    public static final long DEFAULT_MAX_RETRY_BACKOFF_MS = 50 * 1000L;
    public static final int DEFAULT_BUCKETS = 64;
    public static final int BUCKETS_LOWER_LIMIT = 1;
    public static final int BUCKETS_UPPER_LIMIT = 256;

    public static final String LOG_PRODUCER_PREFIX = "tencent-cloud-cls-log-producer-";
    public static final String TIMEOUT_THREAD_SUFFIX_FORMAT = "-timeout-thread-%d";
    public static final String TIMER_SEND_BATCH_TASK_SUFFIX = "-timer-send-batch";
    public static final String SUCCESS_BATCH_HANDLER_SUFFIX = "-success-batch-handler";
    public static final String FAILURE_BATCH_HANDLER_SUFFIX = "-failure-batch-handler";

}
