package com.cuit.common.common;

import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadLocalRandom;

@Component
public class CustomIdGenerator implements IdentifierGenerator {
    @Override
    public Long nextId(Object entity) {
        // 生成 16 位的随机数字 ID
        Long id = ThreadLocalRandom.current().nextLong(1, 10000000000000000L);
        return id;
    }
}
