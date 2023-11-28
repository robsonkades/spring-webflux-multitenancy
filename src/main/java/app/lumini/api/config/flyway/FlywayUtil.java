package app.lumini.api.config.flyway;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.configuration.FluentConfiguration;
import org.springframework.boot.autoconfigure.flyway.FlywayProperties;
import org.springframework.boot.context.properties.PropertyMapper;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.Duration;

public class FlywayUtil {

    private FlywayUtil() {
        // private constructor
    }

    public static Flyway flyway(FlywayProperties flywayProperties) {
        FluentConfiguration fluentConfiguration = new FluentConfiguration();
        configureProperties(fluentConfiguration, flywayProperties);
        return new Flyway(Flyway.configure()
                .configuration(fluentConfiguration)
                .dataSource(flywayProperties.getUrl(), flywayProperties.getUser(), flywayProperties.getPassword()));
    }

    private static void configureProperties(FluentConfiguration configuration, FlywayProperties properties) {
        PropertyMapper map = PropertyMapper.get().alwaysApplyingWhenNonNull();
        map.from(properties.isFailOnMissingLocations()).to(configuration::failOnMissingLocations);
        map.from(properties.getEncoding()).to(configuration::encoding);
        map.from(properties.getConnectRetries()).to(configuration::connectRetries);
        map.from(properties.getConnectRetriesInterval())
                .as(Duration::getSeconds)
                .as(Long::intValue)
                .to(configuration::connectRetriesInterval);
        map.from(properties.getLockRetryCount()).to(configuration::lockRetryCount);
        map.from(properties.getDefaultSchema()).to(configuration::defaultSchema);
        map.from(properties.getSchemas()).as(StringUtils::toStringArray).to(configuration::schemas);
        map.from(properties.isCreateSchemas()).to(configuration::createSchemas);
        map.from(properties.getTable()).to(configuration::table);
        map.from(properties.getTablespace()).to(configuration::tablespace);
        map.from(properties.getBaselineDescription()).to(configuration::baselineDescription);
        map.from(properties.getBaselineVersion()).to(configuration::baselineVersion);
        map.from(properties.getInstalledBy()).to(configuration::installedBy);
        map.from(properties.getPlaceholders()).to(configuration::placeholders);
        map.from(properties.getPlaceholderPrefix()).to(configuration::placeholderPrefix);
        map.from(properties.getPlaceholderSuffix()).to(configuration::placeholderSuffix);
        map.from(properties.getPlaceholderSeparator()).to(configuration::placeholderSeparator);
        map.from(properties.isPlaceholderReplacement()).to(configuration::placeholderReplacement);
        map.from(properties.getSqlMigrationPrefix()).to(configuration::sqlMigrationPrefix);
        map.from(properties.getSqlMigrationSuffixes())
                .as(StringUtils::toStringArray)
                .to(configuration::sqlMigrationSuffixes);
        map.from(properties.getSqlMigrationSeparator()).to(configuration::sqlMigrationSeparator);
        map.from(properties.getRepeatableSqlMigrationPrefix()).to(configuration::repeatableSqlMigrationPrefix);
        map.from(properties.getTarget()).to(configuration::target);
        map.from(properties.isBaselineOnMigrate()).to(configuration::baselineOnMigrate);
        map.from(properties.isCleanDisabled()).to(configuration::cleanDisabled);
        map.from(properties.isCleanOnValidationError()).to(configuration::cleanOnValidationError);
        map.from(properties.isGroup()).to(configuration::group);
        map.from(properties.isMixed()).to(configuration::mixed);
        map.from(properties.isOutOfOrder()).to(configuration::outOfOrder);
        map.from(properties.isSkipDefaultCallbacks()).to(configuration::skipDefaultCallbacks);
        map.from(properties.isSkipDefaultResolvers()).to(configuration::skipDefaultResolvers);
        map.from(properties.isValidateMigrationNaming()).to(configuration::validateMigrationNaming);
        map.from(properties.isValidateOnMigrate()).to(configuration::validateOnMigrate);
        map.from(properties.getInitSqls())
                .whenNot(CollectionUtils::isEmpty)
                .as((initSqls) -> StringUtils.collectionToDelimitedString(initSqls, "\n"))
                .to(configuration::initSql);
        map.from(properties.getScriptPlaceholderPrefix())
                .to(configuration::scriptPlaceholderPrefix);
        map.from(properties.getScriptPlaceholderSuffix())
                .to(configuration::scriptPlaceholderSuffix);
        configureExecuteInTransaction(configuration, properties, map);
        map.from(properties::getLoggers).to(configuration::loggers);
        // Flyway Teams properties
        map.from(properties.getBatch()).to(configuration::batch);
        map.from(properties.getDryRunOutput()).to(configuration::dryRunOutput);
        map.from(properties.getErrorOverrides()).to(configuration::errorOverrides);
        map.from(properties.getLicenseKey()).to(configuration::licenseKey);
    }

    private static void configureExecuteInTransaction(FluentConfiguration configuration, FlywayProperties properties,
                                                      PropertyMapper map) {
        try {
            map.from(properties.isExecuteInTransaction()).to(configuration::executeInTransaction);
        }
        catch (NoSuchMethodError ex) {
            // Flyway < 9.14
        }
    }
}
