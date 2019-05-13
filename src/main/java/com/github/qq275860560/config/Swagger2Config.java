package com.github.qq275860560.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;




@Configuration
@EnableSwagger2
public class Swagger2Config {

	@Bean
    public Docket createRestApi() {
		ParameterBuilder tokenPar = new ParameterBuilder();
    	List<Parameter> pars = new ArrayList<Parameter>();
    	tokenPar.name("Authorization").description("令牌token").modelRef(new ModelRef("string")).parameterType("header").required(false).defaultValue("Bearer eyJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJ1c2VybmFtZTEiLCJleHAiOjE1NTc4NTgzOTd9.aPKZg_KG_w2Suj5Fu2w9O77VIL_OHtg0tWICDD9xIvSkQY5LBAPMa7cNBw81B8wluMF7cfMJFOuHqirn9jdMZA").build();
    	pars.add(tokenPar.build());
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.regex("/api/.*"))
                .paths(PathSelectors.any())               
                .build()
                .globalOperationParameters(pars);
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("security-demo")
                .description("security-demo")
                .termsOfServiceUrl("https://github.com/qq275860560/security-demo")
                .contact(new Contact("qq275860560","https://github.com/qq275860560/security-demo","jiangyuanlin@163.com"))
                .version("1.0.0")
                .build();
    }
    //补充login logout的api文档

}
