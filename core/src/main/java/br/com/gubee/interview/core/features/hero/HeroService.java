package br.com.gubee.interview.core.features.hero;

import br.com.gubee.interview.model.ComparandoHero;
import br.com.gubee.interview.model.Hero;
import br.com.gubee.interview.model.request.CreateHeroRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotBlank;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class HeroService {

    private final HeroRepository heroRepository;

    @Transactional
    public UUID create(CreateHeroRequest createHeroRequest, UUID id) {

        return heroRepository.create(new Hero(createHeroRequest, id));
    }

    public ResponseEntity<Map<String, Object>> buscarHerois(@NotBlank String id) {
        return heroRepository.buscarHerois(id);
    }

    public ResponseEntity<Map<String, Object>> buscarHeroPorNome(String name) {
        return heroRepository.buscarHeroPorNome(name);
    }

    public Object atualizaHero(Hero heroAtualizado, String id) {
        return heroRepository.atualizarHeroi(heroAtualizado, id);
    }

    public ComparandoHero buscar(String name, ComparandoHero hero) {
        return heroRepository.buscar(name, hero);
    }
}


