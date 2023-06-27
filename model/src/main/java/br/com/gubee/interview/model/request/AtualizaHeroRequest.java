package br.com.gubee.interview.model.request;

import br.com.gubee.interview.model.enums.Race;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import static lombok.AccessLevel.PRIVATE;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = PRIVATE)
public class AtualizaHeroRequest {

    private String name;

    private Race race;

    @Min(value = 0, message = "message.powerstats.strength.min")
    @Max(value = 10, message = "message.powerstats.strength.max")
    private int strength;

    @Min(value = 0, message = "message.powerstats.agility.min")
    @Max(value = 10, message = "message.powerstats.agility.max")
    private int agility;

    @Min(value = 0, message = "message.powerstats.dexterity.min")
    @Max(value = 10, message = "message.powerstats.dexterity.max")
    private int dexterity;

    @Min(value = 0, message = "message.powerstats.intelligence.min")
    @Max(value = 10, message = "message.powerstats.intelligence.max")
    private int intelligence;
}
