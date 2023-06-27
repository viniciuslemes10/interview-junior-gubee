package br.com.gubee.interview.core.features.hero;

import br.com.gubee.interview.core.features.powerstats.PowerStatsService;
import br.com.gubee.interview.model.PowerStats;
import br.com.gubee.interview.model.enums.Race;
import br.com.gubee.interview.model.request.CreateHeroRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(HeroController.class)
class HeroControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private HeroService heroService;

    @MockBean
    private PowerStatsService powerStatsService;

    @BeforeEach
    public void initTest() {
        CreateHeroRequest createHeroRequest = createHeroRequest();
        PowerStats powerRequest = new PowerStats(createHeroRequest);
        UUID uuid = powerRequest.getId();
        when(heroService.create(any(), eq(uuid))).thenReturn(UUID.randomUUID());
    }

    @Test
    void createAHeroWithAllRequiredArguments() throws Exception {
        CreateHeroRequest createHeroRequest = createHeroRequest();
        PowerStats powerRequest = new PowerStats(createHeroRequest);
        UUID uuid = powerRequest.getId();
        // given
        // Convert the hero request into a string JSON format stub.
        final String body = objectMapper.writeValueAsString(createHeroRequest);

        // when
        final ResultActions resultActions = mockMvc.perform(post("/api/v1/heroes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body));

        // then
        resultActions.andExpect(status().isCreated()).andExpect(header().exists("Location"));
        when(heroService.create(any(CreateHeroRequest.class), eq(uuid))).thenReturn(UUID.randomUUID());

    }

    private CreateHeroRequest createHeroRequest() {
        return CreateHeroRequest.builder()
                .name("Batman")
                .agility(5)
                .dexterity(8)
                .strength(6)
                .intelligence(10)
                .race(Race.HUMAN)
                .build();
    }
}
