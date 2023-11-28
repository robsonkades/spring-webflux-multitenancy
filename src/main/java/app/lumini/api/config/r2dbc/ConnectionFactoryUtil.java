package app.lumini.api.config.r2dbc;

import io.r2dbc.pool.ConnectionPool;
import io.r2dbc.pool.ConnectionPoolConfiguration;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.ConnectionFactoryOptions;
import io.r2dbc.spi.Option;
import org.springframework.boot.autoconfigure.r2dbc.R2dbcProperties;
import org.springframework.boot.context.properties.PropertyMapper;
import org.springframework.boot.r2dbc.ConnectionFactoryBuilder;
import org.springframework.util.StringUtils;

import java.util.function.Predicate;
import java.util.function.Supplier;

public class ConnectionFactoryUtil {

    private ConnectionFactoryUtil() {
        // private constructor
    }

    public static ConnectionPool getConnectionPool(R2dbcProperties properties) {
        ConnectionFactoryOptions connectionFactoryOptions = ConnectionFactoryOptions
                .builder()
                .from(ConnectionFactoryUtil.getConnectionFactoryOptions(properties))
                .build();
        ConnectionFactory defaultConnectionFactory = ConnectionFactoryBuilder
                .withOptions(connectionFactoryOptions.mutate())
                .build();
        R2dbcProperties.Pool pool = properties.getPool();
        PropertyMapper map = PropertyMapper.get().alwaysApplyingWhenNonNull();
        ConnectionPoolConfiguration.Builder builder = ConnectionPoolConfiguration.builder(defaultConnectionFactory);
        map.from(pool.getMaxIdleTime()).to(builder::maxIdleTime);
        map.from(pool.getMaxLifeTime()).to(builder::maxLifeTime);
        map.from(pool.getMaxAcquireTime()).to(builder::maxAcquireTime);
        map.from(pool.getMaxCreateConnectionTime()).to(builder::maxCreateConnectionTime);
        map.from(pool.getInitialSize()).to(builder::initialSize);
        map.from(pool.getMaxSize()).to(builder::maxSize);
        map.from(pool.getValidationQuery()).whenHasText().to(builder::validationQuery);
        map.from(pool.getValidationDepth()).to(builder::validationDepth);
        map.from(pool.getMinIdle()).to(builder::minIdle);
        map.from(pool.getMaxValidationTime()).to(builder::maxValidationTime);
        return new ConnectionPool(builder.build());
    }

    public static ConnectionFactoryOptions getConnectionFactoryOptions(R2dbcProperties properties) {
        ConnectionFactoryOptions urlOptions = ConnectionFactoryOptions.parse(properties.getUrl());
        ConnectionFactoryOptions.Builder optionsBuilder = urlOptions.mutate();
        configureIf(optionsBuilder, urlOptions, ConnectionFactoryOptions.USER, properties::getUsername, StringUtils::hasText);
        configureIf(optionsBuilder, urlOptions, ConnectionFactoryOptions.PASSWORD, properties::getPassword, StringUtils::hasText);
        configureIf(optionsBuilder, urlOptions, ConnectionFactoryOptions.DATABASE, () -> determineDatabaseName(properties), StringUtils::hasText);
        if (properties.getProperties() != null) {
            properties.getProperties().forEach((key, value) -> optionsBuilder.option(Option.valueOf(key), value));
        }
        return optionsBuilder.build();
    }

    private static <T extends CharSequence> void configureIf(ConnectionFactoryOptions.Builder optionsBuilder,
                                                      ConnectionFactoryOptions originalOptions, Option<T> option, Supplier<T> valueSupplier,
                                                      Predicate<T> setIf) {
        if (originalOptions.hasOption(option)) {
            return;
        }
        T value = valueSupplier.get();
        if (setIf.test(value)) {
            optionsBuilder.option(option, value);
        }
    }

    private static String determineDatabaseName(R2dbcProperties properties) {
        if (properties.isGenerateUniqueName()) {
            return properties.determineUniqueName();
        }
        if (StringUtils.hasLength(properties.getName())) {
            return properties.getName();
        }
        return null;
    }
}
