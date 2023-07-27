package org.anil.reactive;

import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.ConnectionFactoryOptions;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static io.r2dbc.spi.ConnectionFactoryOptions.*;
import static io.r2dbc.spi.ConnectionFactoryOptions.DATABASE;

@SpringBootApplication
@ConfigurationPropertiesScan
public class ReactiveApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReactiveApplication.class, args);
    }


    @Bean
    public ConnectionFactory connectionFactory(){
        ConnectionFactoryOptions options = ConnectionFactoryOptions.builder()
                .option(DRIVER, "mysql")
                .option(HOST, "localhost")
                .option(USER, "root")
                .option(PORT, 3306)  // optional, default 3306
                .option(PASSWORD, "root1234") // optional, default null, null means has no password
                .option(DATABASE, "todos").build();
        ConnectionFactory connectionFactory = ConnectionFactories.get(options);
        return connectionFactory;

    }
}


