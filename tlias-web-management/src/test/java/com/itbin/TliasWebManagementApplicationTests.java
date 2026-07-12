package com.itbin;

import com.example.TokenParser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest//具备组件扫描的功能，但扫描的是启动类所在包及其子包
class TliasWebManagementApplicationTests {

    @Test
    void contextLoads() {
    }

    @Autowired
    private TokenParser tokenParser;

    @Test
    public void testTokenParser(){
        tokenParser.parse();
    }

}
