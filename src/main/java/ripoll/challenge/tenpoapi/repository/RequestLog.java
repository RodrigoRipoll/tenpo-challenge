package ripoll.challenge.tenpoapi.repository;


import com.fasterxml.jackson.databind.JsonNode;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Type;

import java.sql.Timestamp;

@Entity
@Data
@Table(name = "Request_Log")
public class RequestLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Timestamp timestamp;
    private String method;
    private String uri;
    private int statusCode;

    @Column(columnDefinition = "jsonb")
    @Type(JsonType.class)
    private JsonNode response;
}
