package com.develop.web_server;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.VendorExtension;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    private Contact contact = new Contact("Weishun Liao", "", "weishun.liao@ucdconnect.ie");


    private List<VendorExtension> vendorExtensionList = new ArrayList<>();

    ApiInfo apiInfo = new ApiInfo("RESTful web server documentation",
            "RESTful Web Server endpoints", "1.0",
            "", contact, "Apache 2.0",
            "http://www.apache.org/licenses/LICENSE-2.0",
            vendorExtensionList);


    @Bean
    public Docket apiDocket() {
        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo)
                .select().apis(RequestHandlerSelectors.basePackage("com.develop.web_server"))
                .build();
        return docket;
    }
}
