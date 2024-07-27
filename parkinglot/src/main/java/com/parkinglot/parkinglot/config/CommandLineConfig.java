package com.parkinglot.parkinglot.config;

import com.parkinglot.parkinglot.controller.CLIController;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommandLineConfig {

    @Bean
    public CommandLineRunner commandLineRunner(CLIController cliController) {
        return args -> {
            cliController.handleCommands();
        };
    }
}
