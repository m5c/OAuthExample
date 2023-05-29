/**
 * @Author: Maximilian Schiedermeier
 * @Date: April 2019
 */
package github.m5c.resourceserver;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * This class powers up Spring and ensures the annotated controllers are detected.
 */
@SpringBootApplication
public class ResourceServerLauncher {
    public static void main(String[] args) {

        SpringApplication.run(ResourceServerLauncher.class, args);

    }
}

