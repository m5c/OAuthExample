/**
 * @Author: Maximilian Schiedermeier
 * @Date: April 2019
 */
package eu.kartoffelquadrat.timeservice;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * This class powers up Spring and ensures the annotated controllers are detected.
 */
@SpringBootApplication
public class TimeServiceLauncher {
    public static void main(String[] args) {

        SpringApplication.run(TimeServiceLauncher.class, args);

    }
}

