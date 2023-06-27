package br.com.gubee.interview.model;

import br.com.gubee.interview.model.enums.Race;

import java.util.List;
import java.util.UUID;

public class ComparandoHero {

    private UUID id;
    private String name;
    private Race race;

    private UUID idPower;
    private int strength;
    private int agility;
    private int dexterity;
    private int intelligence;

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

    public UUID getIdPower() {
        return idPower;
    }

    public void setIdPower(UUID idPower) {
        this.idPower = idPower;
    }

    public int getStrength() {
        return strength;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public int getAgility() {
        return agility;
    }

    public void setAgility(int agility) {
        this.agility = agility;
    }

    public int getDexterity() {
        return dexterity;
    }

    public void setDexterity(int dexterity) {
        this.dexterity = dexterity;
    }

    public int getIntelligence() {
        return intelligence;
    }

    public void setIntelligence(int intelligence) {
        this.intelligence = intelligence;
    }


}
