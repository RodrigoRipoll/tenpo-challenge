package ripoll.challenge.tenpoapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IRequestLogRepository extends JpaRepository<RequestLogRepository, Long> {
}