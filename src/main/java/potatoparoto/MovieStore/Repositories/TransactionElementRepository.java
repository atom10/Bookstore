package potatoparoto.MovieStore.Repositories;

import potatoparoto.MovieStore.Models.TransactionElement;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public interface TransactionElementRepository extends JpaRepository<TransactionElement, Long> {
}