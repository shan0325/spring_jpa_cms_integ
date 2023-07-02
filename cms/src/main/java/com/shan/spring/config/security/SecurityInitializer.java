package com.shan.spring.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

// ApplicationRunner를 implemenets 받아와서 run 메서드를 Override 해주면, 서버 기동 시 해당 메서드를 실행한다.
@RequiredArgsConstructor
@Component
public class SecurityInitializer implements ApplicationRunner {

    private final UrlFilterInvocationMetadataSource urlFilterInvocationMetadataSource;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // data.sql 테스트 시큐리티 리소스는 초기에 설정을 못하기 때문에 여기서 기동 후 리소스 업데이트 처리
        urlFilterInvocationMetadataSource.reload();
    }
}
