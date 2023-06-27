package br.com.gubee.interview.model;

import br.com.gubee.interview.model.request.AtualizaHeroRequest;
import br.com.gubee.interview.model.request.CreateHeroRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

import static lombok.AccessLevel.PRIVATE;

@Data
@NoArgsConstructor(access = PRIVATE)
@AllArgsConstructor
@Builder
public class PowerStats {

    private UUID id;
    private int strength;
    private int agility;
    private int dexterity;
    private int intelligence;
    private Instant createdAt;
    private Instant updatedAt;

    public PowerStats(CreateHeroRequest createHeroRequest) {
        this.id = UUID.randomUUID();
        this.strength = createHeroRequest.getStrength();
        this.agility = createHeroRequest.getAgility();
        this.dexterity = createHeroRequest.getDexterity();
        this.intelligence = createHeroRequest.getIntelligence();
    }

    public PowerStats(AtualizaHeroRequest atualiza) {
        if(atualiza.getStrength() != 0) {
            this.strength = atualiza.getStrength();

        }
        if(atualiza.getAgility() != 0) {
            this.agility = atualiza.getAgility();
        }
        if(atualiza.getDexterity() != 0) {
            this.dexterity = atualiza.getDexterity();
        }
        if(atualiza.getIntelligence() != 0) {
            this.intelligence = atualiza.getIntelligence();
        }
    }

    public PowerStats(ComparandoHero hero) {
        this.strength = hero.getStrength();
        this.agility = hero.getAgility();
        this.dexterity = hero.getDexterity();
        this.intelligence = hero.getIntelligence();
    }

}
