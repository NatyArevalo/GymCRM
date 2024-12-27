package com.gymcrm.gymcrm.Configurations;

import com.gymcrm.gymcrm.Repositories.TrainerRepository;
import com.gymcrm.gymcrm.Repositories.TrainingRepository;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Gauge;
import org.springframework.stereotype.Component;

import io.micrometer.core.instrument.MeterRegistry;

@Component
//http://localhost:8080/actuator/metrics/training_sessions_total
//http://localhost:8080/actuator/metrics/active_trainers_total
public class Metrics {
    private final Counter trainingSessionsCounter;
    private final Gauge activeTrainersGauge;
    private final TrainerRepository trainerRepository;
    private final TrainingRepository trainingRepository;

    public Metrics(MeterRegistry meterRegistry, TrainingRepository trainingRepository, TrainerRepository trainerRepository) {
        this.trainingSessionsCounter = Counter.builder("training_sessions_total")
                .description("Total number of training sessions created")
                .register(meterRegistry);

        this.activeTrainersGauge = Gauge.builder("active_trainers_total", this, Metrics::getActiveTrainersCount)
                .description("Number of active trainers")
                .register(meterRegistry);

        this.trainingRepository = trainingRepository;

        this.trainerRepository = trainerRepository;
    }

    public void incrementTrainingSessionsCounter() {
        trainingSessionsCounter.increment();
    }

    public long getTrainingSessionsCount() {
        return trainingRepository.count();
    }
    private long getActiveTrainersCount() {
        return trainerRepository.count();
    }
}