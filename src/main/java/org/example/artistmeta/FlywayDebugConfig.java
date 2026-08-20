package org.example.artistmeta;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.MigrationInfo;
import org.flywaydb.core.api.MigrationInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FlywayDebugConfig {
    private static final Logger log = LoggerFactory.getLogger(FlywayDebugConfig.class);

    @Bean
    public ApplicationRunner flywayRunner(org.springframework.beans.factory.ObjectProvider<Flyway> flywayProvider) {
        return args -> {
            Flyway flyway = flywayProvider.getIfAvailable();
            if (flyway == null) {
                log.info("Flyway bean not present; skipping debug migration runner");
                return;
            }

            try {
                log.info("Flyway bean present: {}", flyway != null);

                MigrationInfoService info = flyway.info();
                MigrationInfo current = info.current();
                log.info("Flyway current migration: {}", current == null ? "<none>" : current.getVersion() + " - " + current.getDescription());
                log.info("Pending migrations count: {}", info.pending().length);

                if (info.pending().length > 0) {
                    log.info("Applying pending Flyway migrations now (explicit)");
                    var result = flyway.migrate();
                    log.info("Flyway migrate result: {}", result);
                } else {
                    log.info("No pending Flyway migrations to apply");
                }
            } catch (Exception e) {
                log.error("Flyway debug runner encountered an exception", e);
            }
        };
    }
}
