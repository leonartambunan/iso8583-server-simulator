package id.nio.iso;

import org.jpos.q2.Q2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ISOServerApplication {

	public static void main(String[] args) {
		System.out.println("Starting");
                Q2 q2 = new Q2();
                q2.start();
		SpringApplication.run(ISOServerApplication.class, args);
	}

}

