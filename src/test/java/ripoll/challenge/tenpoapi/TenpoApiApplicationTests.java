package ripoll.challenge.tenpoapi;

import com.redis.testcontainers.RedisContainer;
import org.junit.jupiter.api.Test;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import ripoll.challenge.tenpoapi.repository.IRequestLogRepository;
import ripoll.challenge.tenpoapi.repository.RequestLog;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Testcontainers
@SpringBootTest
class TenpoApiApplicationTests {

	@Test
	void contextLoads() {

		RequestLog book = new RequestLog();
		book.setTimestamp(Timestamp.valueOf(LocalDateTime.now()));
		book.setUri("Testcontainers");
		book.setMethod("asd");
		book.setResponse(null);

		requestLogRepository.save(book);

		System.out.println("Context loads!");

	}

	@Autowired
	private IRequestLogRepository requestLogRepository;

	@Container
	public static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer()
			.withUsername("rodrigo-ripoll")
			.withPassword("tenpo-pass")
			.withDatabaseName("test");

	@Container
	public static RedisContainer redisContainer = new RedisContainer(DockerImageName.parse("redis:7")).withExposedPorts(6379);

	@DynamicPropertySource
	static void properties(DynamicPropertyRegistry registry) {
		redisContainer.start();
		registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
		registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
		registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
		registry.add("redis.host", () -> String.format("redis://%s:%d", redisContainer.getHost(), redisContainer.getFirstMappedPort()));
	}

	@Autowired
	private RedissonClient redissonClient;

	@Test
	void testRedisConnection() {
		RMap<String, String> map = redissonClient.getMap("test");
		map.put("key", "value");
		assertEquals("value", map.get("key"));
	}

}
