package potatoparoto.MovieStore.Repositories;

import potatoparoto.MovieStore.Models.Director;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Transactional
public interface DirectorRepository extends JpaRepository<Director, Long> {
    public List<Director> findAll();
}