package br.com.gubee.interview.core.features.hero;

import br.com.gubee.interview.model.ComparandoHero;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ComparacaoDTO {
    private ComparandoHero hero1;
    private ComparandoHero hero2;
    private String strength;
    private String agility;
    private String dexterity;
    private String intelligence;

    // Getters e Setters

    public ComparandoHero getHero1() {
        return hero1;
    }

    public void setHero1(ComparandoHero hero1) {
        this.hero1 = hero1;
    }

    public ComparandoHero getHero2() {
        return hero2;
    }

    public void setHero2(ComparandoHero hero2) {
        this.hero2 = hero2;
    }

    public String getStrength() {
        return strength;
    }

    public void setStrength(String strength) {
        this.strength = strength;
    }

    public String getAgility() {
        return agility;
    }

    public void setAgility(String agility) {
        this.agility = agility;
    }

    public String getDexterity() {
        return dexterity;
    }

    public void setDexterity(String dexterity) {
        this.dexterity = dexterity;
    }

    public String getIntelligence() {
        return intelligence;
    }

    public void setIntelligence(String intelligence) {
        this.intelligence = intelligence;
    }

    public Map<String, Object> toJson() {
        Map<String, Object> json = new HashMap<>();

        Map<String, Object> hero1Json = new HashMap<>();
        hero1Json.put("id", hero1.getId());
        hero1Json.put("name", hero1.getName());
        hero1Json.put("race", hero1.getRace());
        hero1Json.put("strength", getComparisonResultString(hero1.getStrength(), hero2.getStrength()));
        hero1Json.put("agility", getComparisonResultString(hero1.getAgility(), hero2.getAgility()));
        hero1Json.put("dexterity", getComparisonResultString(hero1.getDexterity(), hero2.getDexterity()));
        hero1Json.put("intelligence", getComparisonResultString(hero1.getIntelligence(), hero2.getIntelligence()));

        Map<String, Object> hero2Json = new HashMap<>();
        hero2Json.put("id", hero2.getId());
        hero2Json.put("name", hero2.getName());
        hero2Json.put("race", hero2.getRace());
        hero2Json.put("strength", getComparisonResultString(hero2.getStrength(), hero1.getStrength()));
        hero2Json.put("agility", getComparisonResultString(hero2.getAgility(), hero1.getAgility()));
        hero2Json.put("dexterity", getComparisonResultString(hero2.getDexterity(), hero1.getDexterity()));
        hero2Json.put("intelligence", getComparisonResultString(hero2.getIntelligence(), hero1.getIntelligence()));

        json.put("hero1", hero1Json);
        json.put("hero2", hero2Json);

        return json;
    }



    public List<String> compararAtributos(ComparandoHero hero1, ComparandoHero hero2) {
        List<String> resultados = new ArrayList<>();

        resultados.add(getComparisonResultString(hero1.getStrength(), hero2.getStrength()));

        resultados.add(getComparisonResultString(hero1.getAgility(), hero2.getAgility()));

        resultados.add(getComparisonResultString(hero1.getIntelligence(), hero2.getIntelligence()));

        resultados.add(getComparisonResultString(hero1.getDexterity(), hero2.getDexterity()));

        return resultados;
    }

    private String getComparisonResultString(int heroAttribute, int heroAtributeTwo) {
        if (heroAttribute > heroAtributeTwo) {
            return "MAIOR";
        } else if (heroAttribute < heroAtributeTwo) {
            return "MENOR";
        } else {
            return "IGUAL";
        }
    }

}
