package com.github.theia.global.feign.client;

import com.github.theia.adapter.in.rest.dto.KaKaoInfo;
import com.github.theia.global.feign.config.KaKaoFeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import java.net.URI;

@FeignClient(name = "KaKaoClient", configuration = KaKaoFeignConfig.class)
public interface KakaoInformationClient {

    @PostMapping
    KaKaoInfo getInfo(URI baseUrl, @RequestHeader("Authorization") String accessToken);
}

