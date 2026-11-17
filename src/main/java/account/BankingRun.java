package account;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BankingRun {

	public static void main(String[] args) {

		SpringApplication.run(BankingRun.class, args);
	}

}
