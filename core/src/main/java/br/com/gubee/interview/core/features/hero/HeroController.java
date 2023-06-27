package br.com.gubee.interview.core.features.hero;

import br.com.gubee.interview.core.features.powerstats.PowerStatsService;
import br.com.gubee.interview.model.ComparandoHero;
import br.com.gubee.interview.model.Hero;
import br.com.gubee.interview.model.PowerStats;
import br.com.gubee.interview.model.request.AtualizaHeroRequest;
import br.com.gubee.interview.model.request.CreateHeroRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static java.lang.String.format;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.ResponseEntity.created;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/heroes", produces = APPLICATION_JSON_VALUE)
public class HeroController {

    private final HeroService heroService;

    private final PowerStatsService powerStatsService;

    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> create(@Validated
                                       @RequestBody CreateHeroRequest createHeroRequest) {
        System.out.println(createHeroRequest);

        PowerStats powerStats = new PowerStats(createHeroRequest);

        final UUID idPower = powerStatsService.create(powerStats);

        final UUID id = heroService.create(createHeroRequest, idPower);

        return created(URI.create(format("/api/v1/heroes/%s", id))).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> listHero(@PathVariable @NotBlank String id) {
        return heroService.buscarHerois(id);

    }

    @GetMapping("/name/{name}")
    public ResponseEntity<Map<String, Object>> bucaHeroName(@PathVariable @NotBlank String name) {
        return heroService.buscarHeroPorNome(name);
    }

    @PutMapping("/atualizaHero/{id}")
    public ResponseEntity<Object> editarHero(@RequestBody @Valid AtualizaHeroRequest atualizaHero, @PathVariable @NotBlank String id) {
        Hero heroAtualizado = new Hero(atualizaHero);

        PowerStats powerAtualizado = new PowerStats(atualizaHero);

        ResponseEntity<Map<String, Object>> hero = heroService.buscarHerois(id);

        var heroA = heroService.atualizaHero(heroAtualizado, id);

        String power = String.valueOf(powerStatsService.buscaPower(id));
        System.out.println(id);

        var powerA = powerStatsService.atualizaPower(powerAtualizado, power);

        if (id.length() != 36) {
            System.out.println("Entrei Neste");
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(power);
    }

    @DeleteMapping("/excluirHero/{id}")
    public ResponseEntity<Object> excluirHero(@PathVariable @NotBlank String id) {

        String power = powerStatsService.buscaPower(id);

        boolean hero = Boolean.parseBoolean(String.valueOf(heroService.buscarHerois(id)));

        String powerDelete = String.valueOf(powerStatsService.excluirPower(id));

        System.out.println(power);

        if (id.length() != 36 || power == null) {
            System.out.println("Entrei Neste");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } else {
            return ResponseEntity.ok().build();
        }

    }

    @GetMapping("/compare/{name}/{nametwo}")
    public ResponseEntity<Map<String, Object>> compararHerois(
            @PathVariable(value = "name") @NotBlank String name,
            @PathVariable(value = "nametwo") @NotBlank String nameTwo) {

        ComparandoHero hero1 = new ComparandoHero();
        ComparandoHero heroCompare1 = heroService.buscar(name, hero1);

        ComparandoHero hero2 = new ComparandoHero();
        ComparandoHero heroCompare2 = heroService.buscar(nameTwo, hero2);

        if (heroCompare1 == null || heroCompare2 == null) {
            System.out.println("Entrei Neste");
            return ResponseEntity.notFound().build();
        }

        ComparacaoDTO comparacaoDTO = new ComparacaoDTO();
        List<String> resultados = comparacaoDTO.compararAtributos(heroCompare1, heroCompare2);
        comparacaoDTO.setHero1(heroCompare1);
        comparacaoDTO.setHero2(heroCompare2);
        comparacaoDTO.setStrength(resultados.get(0));
        comparacaoDTO.setAgility(resultados.get(1));
        comparacaoDTO.setDexterity(resultados.get(2));
        comparacaoDTO.setIntelligence(resultados.get(3));

        Map<String, Object> json = comparacaoDTO.toJson();

        return ResponseEntity.ok(json);
    }





}
