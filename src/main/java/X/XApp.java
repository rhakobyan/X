package X;

import X.storage.StorageProperties;
import X.storage.StorageService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import javax.servlet.http.HttpSessionListener;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class XApp {

    public static void main(String[] args) {
        SpringApplication.run(XApp.class, args);
    }

    @Bean
    CommandLineRunner init(StorageService storageService) {
        return (args) -> {
            storageService.init();
        };
    }

    @Bean
    public HttpSessionListener httpSessionListener(){
        //SessionListener should implement javax.servlet.http.HttpSessionListener
        return new SessionListener();
    }
}
