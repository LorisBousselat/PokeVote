package pokevote;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface PokemonRepository extends CrudRepository<Pokemon, Long> {
    Pokemon findByName(String name);
    Set<Pokemon> findAllByNameOrderByScoreAsc(String name);
}
