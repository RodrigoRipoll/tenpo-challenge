package ripoll.challenge.tenpoapi.repository;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "Trace")
public class RequestLog {
    @Id
    @GeneratedValue
    private Long id;
    private String method;
    private String uri;
    private int statusCode;
    @Lob
    private String responseBody;
}
