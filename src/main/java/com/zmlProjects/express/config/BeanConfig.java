package com.zmlProjects.express.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.text.SimpleDateFormat;
import java.util.List;

@Configuration
@ComponentScan("com.zmlProjects")//主要是spring使用，对bean扫描
@EnableWebMvc//等同于xml配置中的<mvc:annotation-driven/>
public class BeanConfig extends WebMvcConfigurerAdapter {

    //定义跳转的文件的前后缀 ，视图模式配置
    @Bean
    public InternalResourceViewResolver internalResourceViewResolve() {
        //这里的配置我的理解是自动给后面action的方法return的字符串加上前缀和后缀，变成一个 可用的url地址 -->
        return new InternalResourceViewResolver("/", ".jsp");
    }

    //定义对ajax、json的支持
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder()
                .indentOutput(true)
                .dateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        converters.add(new MappingJackson2HttpMessageConverter(builder.build()));
    }
}

