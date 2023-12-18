package potatoparoto.MovieStore.Repositories;

import potatoparoto.MovieStore.Models.Transaction;
import potatoparoto.MovieStore.Models.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Transactional
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    public List<Transaction> findByUser(User user);
}