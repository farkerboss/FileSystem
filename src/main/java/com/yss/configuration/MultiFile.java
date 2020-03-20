package com.yss.configuration;

import javax.servlet.MultipartConfigElement;

import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MultiFile {
	@Bean  
    public MultipartConfigElement multipartConfigElement() {  
        MultipartConfigFactory factory = new MultipartConfigFactory();  
        //文件最大  
        factory.setMaxFileSize("30960KB"); //KB,MB  
        /// 设置总上传数据总大小  
        factory.setMaxRequestSize("309600KB");  
        return factory.createMultipartConfig();  
    }
}
