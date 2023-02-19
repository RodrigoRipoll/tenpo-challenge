package ripoll.challenge.tenpoapi.controller;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.redis.testcontainers.RedisContainer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import ripoll.challenge.tenpoapi.model.TaxCacheEntry;
import ripoll.challenge.tenpoapi.repository.IRequestLogRepository;
import ripoll.challenge.tenpoapi.repository.memoryCache.MemoryCache;
import ripoll.challenge.tenpoapi.service.AccountantService;
import ripoll.challenge.tenpoapi.service.RequestLogService;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers
public abstract class IntegrationTest {

    @Container
    static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer()
            .withUsername("rodrigo-ripoll")
            .withPassword("tenpo-pass")
            .withDatabaseName("test");

    @Container
    static RedisContainer redisContainer = new RedisContainer(DockerImageName.parse("redis:7"))
            .withExposedPorts(6379);

    static WireMockServer wireMock;
    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected AccountantService accountantService;

    @Autowired
    protected RedissonClient redissonClient;

    @Autowired
    protected IRequestLogRepository requestLogRepository;

    @Autowired
    protected MemoryCache<TaxCacheEntry> memoryTaxCache;

    @Autowired
    protected RequestLogService requestLogService;

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        redisContainer.start();
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("redisUrlConnection", () -> String.format("redis://%s:%d", redisContainer.getHost(), redisContainer.getFirstMappedPort()));
    }

    @BeforeAll
    public static void setupAll() {
        if (!postgreSQLContainer.isRunning()) postgreSQLContainer.start();
        if (!redisContainer.isRunning()) redisContainer.start();
        if (wireMock == null || !wireMock.isRunning()) {
            wireMock = new WireMockServer(8081);
            wireMock.start();
        }
    }

    @BeforeEach
    public void setup() {
        requestLogRepository.flush();
/*        wireMock.start();
        postgreSQLContainer.start();
        redisContainer.setExposedPorts(redisExposedPorts);
        redisContainer.start();*/
    }

    @AfterEach
    public void after() {
        wireMock.resetAll();
/*        postgreSQLContainer.stop();
        redisContainer.stop();*/
    }

/*    @AfterAll
    public static void tearDown() {
        postgreSQLContainer.stop();
        redisContainer.stop();
        wireMock.stop();
    }*/
}
