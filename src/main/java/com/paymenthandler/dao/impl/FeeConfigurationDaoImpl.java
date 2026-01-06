package com.paymenthandler.dao.impl;

import com.paymenthandler.dao.FeeConfigurationDao;
import com.paymenthandler.enums.FeeType;
import com.paymenthandler.model.FeeConfiguration;
import com.paymenthandler.persistence.DatabaseConnectionFactory;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Singleton
public class FeeConfigurationDaoImpl implements FeeConfigurationDao {

    private final DatabaseConnectionFactory connectionFactory;

    @Inject
    public FeeConfigurationDaoImpl(DatabaseConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    @Override
    public FeeConfiguration save(FeeConfiguration config) {
        try (Connection conn = connectionFactory.getConnection()) {
            DSLContext ctx = DSL.using(conn, SQLDialect.H2);

            ctx.insertInto(DSL.table("fee_configurations"))
                .columns(
                    DSL.field("payment_method"),
                    DSL.field("fee_type"),
                    DSL.field("fee_value")
                )
                .values(
                    config.getPaymentMethod(),
                    config.getFeeType().name(),
                    config.getFeeValue()
                )
                .execute();

            return findByPaymentMethod(config.getPaymentMethod())
                .orElseThrow(() -> new RuntimeException("Failed to retrieve saved fee configuration"));

        } catch (SQLException e) {
            throw new RuntimeException("Failed to save fee configuration", e);
        }
    }

    @Override
    public FeeConfiguration update(FeeConfiguration config) {
        try (Connection conn = connectionFactory.getConnection()) {
            DSLContext ctx = DSL.using(conn, SQLDialect.H2);

            int updated = ctx.update(DSL.table("fee_configurations"))
                .set(DSL.field("fee_type"), config.getFeeType().name())
                .set(DSL.field("fee_value"), config.getFeeValue())
                .set(DSL.field("updated_at"), Timestamp.from(Instant.now()))
                .where(DSL.field("payment_method", String.class).eq(config.getPaymentMethod()))
                .execute();

            if (updated == 0) {
                throw new RuntimeException("Fee configuration not found for: " + config.getPaymentMethod());
            }

            return findByPaymentMethod(config.getPaymentMethod())
                .orElseThrow(() -> new RuntimeException("Failed to retrieve updated fee configuration"));

        } catch (SQLException e) {
            throw new RuntimeException("Failed to update fee configuration", e);
        }
    }

    @Override
    public Optional<FeeConfiguration> findByPaymentMethod(String paymentMethod) {
        try (Connection conn = connectionFactory.getConnection()) {
            DSLContext ctx = DSL.using(conn, SQLDialect.H2);

            Record record = ctx.select(
                    DSL.field("id", Long.class),
                    DSL.field("payment_method", String.class),
                    DSL.field("fee_type", String.class),
                    DSL.field("fee_value", Double.class),
                    DSL.field("created_at", Timestamp.class),
                    DSL.field("updated_at", Timestamp.class)
                )
                .from(DSL.table("fee_configurations"))
                .where(DSL.field("payment_method", String.class).eq(paymentMethod))
                .fetchOne();

            if (record == null) {
                return Optional.empty();
            }

            return Optional.of(mapRecordToFeeConfiguration(record));

        } catch (SQLException e) {
            throw new RuntimeException("Failed to find fee configuration", e);
        }
    }

    @Override
    public List<FeeConfiguration> findAll() {
        try (Connection conn = connectionFactory.getConnection()) {
            DSLContext ctx = DSL.using(conn, SQLDialect.H2);

            return ctx.select(
                    DSL.field("id", Long.class),
                    DSL.field("payment_method", String.class),
                    DSL.field("fee_type", String.class),
                    DSL.field("fee_value", Double.class),
                    DSL.field("created_at", Timestamp.class),
                    DSL.field("updated_at", Timestamp.class)
                )
                .from(DSL.table("fee_configurations"))
                .fetch()
                .stream()
                .map(this::mapRecordToFeeConfiguration)
                .collect(Collectors.toList());

        } catch (SQLException e) {
            throw new RuntimeException("Failed to retrieve fee configurations", e);
        }
    }

    @Override
    public boolean deleteByPaymentMethod(String paymentMethod) {
        try (Connection conn = connectionFactory.getConnection()) {
            DSLContext ctx = DSL.using(conn, SQLDialect.H2);

            int deleted = ctx.deleteFrom(DSL.table("fee_configurations"))
                .where(DSL.field("payment_method", String.class).eq(paymentMethod))
                .execute();

            return deleted > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete fee configuration", e);
        }
    }

    private FeeConfiguration mapRecordToFeeConfiguration(Record record) {
        return FeeConfiguration.builder()
            .id(record.get("id", Long.class))
            .paymentMethod(record.get("payment_method", String.class))
            .feeType(FeeType.fromString(record.get("fee_type", String.class)))
            .feeValue(record.get("fee_value", Double.class))
            .createdAt(record.get("created_at", Timestamp.class).toInstant())
            .updatedAt(record.get("updated_at", Timestamp.class).toInstant())
            .build();
    }
}
