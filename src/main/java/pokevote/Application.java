package pokevote;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

@SpringBootApplication
public class Application {

    @Bean
    public CommandLineRunner cmd(PokemonService service){
        return args -> {
            var threads = new ArrayList<Thread>();
            for (int i = 0; i < 100; i++) {
                var thread=new Thread(() -> {
                    for (int j = 0; j < 100; j++) {
                        var name = ""+ ThreadLocalRandom.current().nextInt(100);
                        service.incrementScoreWithOptimisticLock("balbuzar"+name);
                    }
                });
                threads.add(thread);
                thread.start();
            }
            for(var thread : threads){
                thread.join();
            }
            System.err.println(service.totalCountVote());
        };
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Application.class, args);
    }

}
