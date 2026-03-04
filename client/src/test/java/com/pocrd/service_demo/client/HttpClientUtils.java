package com.pocrd.service_demo.client;

import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * HTTP 客户端工具类（用于通过 Higress 网关调用服务）
 */
public class HttpClientUtils {
    
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private String baseUrl;
    
    public HttpClientUtils(String baseUrl) {
        this.baseUrl = baseUrl;
    }
    
    /**
     * 调用 GreeterServiceHttpExport 的 greet 方法
     * 
     * @param name 参数名称
     * @return 响应结果
     */
    public String greet(String name) throws Exception {
        String url = baseUrl + "/com.pocrd.service_demo.api.GreeterServiceHttpExport/greet";
        
        // 构建请求体（protobuf JSON 格式）
        String requestBody = objectMapper.writeValueAsString(
            java.util.Map.of("name", name)
        );
        
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost post = new HttpPost(url);
            post.setEntity(new StringEntity(requestBody, ContentType.APPLICATION_JSON));
            post.setHeader("Content-Type", "application/json");
            
            return httpClient.execute(post, response -> {
                int code = response.getCode();
                if (code >= 200 && code < 300) {
                    return EntityUtils.toString(response.getEntity());
                } else {
                    throw new RuntimeException("HTTP 请求失败：" + code);
                }
            });
        }
    }
    
    /**
     * 解析响应为 JSON 对象
     */
    public JsonNode parseJson(String json) throws Exception {
        return objectMapper.readTree(json);
    }
}
