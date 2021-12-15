package pokevote;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class PokemonService {
    @Autowired
    PokemonRepository pokemonRepository;

    @Autowired
    PokemonServiceWithFailure pokemonServiceWithFailure;

    @Transactional
    public void incrementScoreWithOptimisticLock(String name){
        var retry=true;
        while(retry) {
            retry=false;
            try {
                pokemonServiceWithFailure.incrementScoreWrong(name);
            } catch (org.springframework.orm.ObjectOptimisticLockingFailureException e){
                retry=true;
            }

        }
    }

    public long totalCountVote(String name) {
        return pokemonRepository.findByName(name).getScore();
    }

    public long totalCountVote() {
        AtomicLong count = new AtomicLong();
        pokemonRepository.findAll().forEach(x -> count.addAndGet(x.getScore()));
        return count.get();
    }

    @Service
    public class PokemonServiceWithFailure{
        @Autowired
        PokemonRepository pokemonRepository;

        @Transactional
        public void incrementScoreWrong(String name){
            var pokemon = pokemonRepository.findByName(name);
            if(pokemon == null)
                pokemonRepository.save(new Pokemon(name,1));
            else {
                pokemon.incrementScore();
                pokemonRepository.save(pokemon);
            }
        }
    }
}
