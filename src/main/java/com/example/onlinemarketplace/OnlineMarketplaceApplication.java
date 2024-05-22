package com.example.onlinemarketplace;

import de.kherud.llama.LlamaModel;
import de.kherud.llama.ModelParameters;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class OnlineMarketplaceApplication {

    public static void main(String[] args) {
        SpringApplication.run(OnlineMarketplaceApplication.class, args);
    }

    @Bean
    public ModelMapper modelMapper() { return new ModelMapper(); }

    @Bean
    public LlamaModel llamaModel() {
        return new LlamaModel(new ModelParameters()
                .setModelFilePath("src/main/resources/model-q4_K.gguf")
                .setNGpuLayers(43)
                .setDisableLog(true)
        );
    }
}
