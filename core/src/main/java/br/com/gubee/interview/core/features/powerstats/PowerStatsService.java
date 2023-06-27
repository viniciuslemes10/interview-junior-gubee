package br.com.gubee.interview.core.features.powerstats;

import br.com.gubee.interview.model.PowerStats;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PowerStatsService {

    private final PowerStatsRepository powerStatsRepository;

    @Transactional
    public UUID create(PowerStats powerStats) {
        return powerStatsRepository.create(powerStats);
    }

    public String buscaPower(String id) {

        return powerStatsRepository.buscarPorId(id);
    }

    public Object atualizaPower(PowerStats powerAtualizado, String id) {
        return powerStatsRepository.atualizaPower(powerAtualizado, id);
    }

    public ResponseEntity<Object> excluirPower(String power) {
        return powerStatsRepository.excluirPower(power);
    }
}
