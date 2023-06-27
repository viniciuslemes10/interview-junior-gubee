package br.com.gubee.interview.model;

import br.com.gubee.interview.model.enums.Race;
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
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = PRIVATE)
public class Hero {

    private UUID id;
    private String name;
    private Race race;
    private UUID powerStatsId;
    private Instant createdAt;
    private Instant updatedAt;
    private boolean enabled;

    public Hero(CreateHeroRequest createHeroRequest, UUID powerStatsId) {
        this.enabled = true;
        this.name = createHeroRequest.getName();
        this.race = createHeroRequest.getRace();
        this.powerStatsId = powerStatsId;
    }

    public Hero(AtualizaHeroRequest atualiza) {
        if (atualiza.getName() != null && !atualiza.getName().isEmpty()) {
            this.name = atualiza.getName();
        } else {
            System.out.println("Vazio");
        }

        if (atualiza.getRace() == null || atualiza.getRace().name().isEmpty() || atualiza.getRace().name().isBlank()) {

            System.out.println("Veio Null");

        } else {
            this.race = atualiza.getRace();
        }
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Race getRace() {
        return race;
    }

    public void setRace(Race race) {
        this.race = race;
    }

    public UUID getPowerStatsId() {
        return powerStatsId;
    }

    public void setPowerStatsId(UUID powerStatsId) {
        this.powerStatsId = powerStatsId;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
