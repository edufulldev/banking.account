package account;

import org.springframework.boot.SpringApplication;

public class TestApplication {

	public static void main(String[] args) {
		SpringApplication.from(BankingRun::main).with(TestcontainersConfiguration.class).run(args);
	}

}
