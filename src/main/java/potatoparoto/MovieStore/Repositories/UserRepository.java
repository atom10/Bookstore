package potatoparoto.MovieStore.Repositories;

import potatoparoto.MovieStore.Models.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<User, Long> {
    public List<User> findByEmail(String email);
    public List<User> findByLogin(String login);
}