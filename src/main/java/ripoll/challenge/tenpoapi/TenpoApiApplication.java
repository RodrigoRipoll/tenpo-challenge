package ripoll.challenge.tenpoapi;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@SpringBootApplication
public class TenpoApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(TenpoApiApplication.class, args);
	}

	@Bean
	ApplicationRunner applicationRunner(){
		return args -> {
			System.out.println("to make call");
			//System.out.println(client.getCurrentTaxPercentage());
			//System.out.println(client.getCurrentTaxPercentage().getBody());
		};
	}

/*	@Bean
	public TaxRESTClient taxRESTClient(){
		WebClient webClient = WebClient.builder()
				//.baseUrl("tenpo-tax-api")
				.baseUrl("http://localhost:8081")
				.build();
		WebClientAdapter webClientAdapter = WebClientAdapter.forClient(webClient);
		HttpServiceProxyFactory factory = HttpServiceProxyFactory.builder(webClientAdapter)
				.build();
		return factory.createClient(TaxRESTClient.class);
	}

	interface TaxRESTClient {
		@GetExchange("/tax/payments/current")
		ResponseEntity<TaxPercentage> getCurrentTaxPercentage();
	}

	record TaxPercentage(Double tax_percentage){}*/

}
