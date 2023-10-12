package com.multipolar.bootcamp.gatewayProduct.kafka;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccessLog implements Serializable {
    private String requestMethod;
    private String requestUri;
    private Integer responseStatusCode;
    private LocalDateTime timestamp = LocalDateTime.now();
    private String content;
}
