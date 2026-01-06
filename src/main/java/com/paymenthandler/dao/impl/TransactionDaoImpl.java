package com.paymenthandler.dao.impl;

import com.paymenthandler.dao.TransactionDao;
import com.paymenthandler.model.Transaction;
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
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Singleton
public class TransactionDaoImpl implements TransactionDao {

    private final DatabaseConnectionFactory connectionFactory;

    @Inject
    public TransactionDaoImpl(DatabaseConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    @Override
    public Transaction save(Transaction tx) {
        try (Connection conn = connectionFactory.getConnection()) {
            DSLContext ctx = DSL.using(conn, SQLDialect.H2);

            // Insert the transaction
            ctx.insertInto(DSL.table("transactions"))
                .columns(
                    DSL.field("payer_id"),
                    DSL.field("payee_id"),
                    DSL.field("amount"),
                    DSL.field("payment_method"),
                    DSL.field("fee_amount"),
                    DSL.field("total_amount")
                )
                .values(
                    tx.getPayerId(),
                    tx.getPayeeId(),
                    tx.getAmount(),
                    tx.getMethod(),
                    tx.getFeeAmount(),
                    tx.getTotalAmount()
                )
                .execute();

            // Get the last inserted ID
            Long id = ctx.select(DSL.field("id", Long.class))
                .from(DSL.table("transactions"))
                .orderBy(DSL.field("id").desc())
                .limit(1)
                .fetchOne(0, Long.class);

            // Fetch the complete record
            Record record = ctx.select(
                    DSL.field("id", Long.class),
                    DSL.field("payer_id", Long.class),
                    DSL.field("payee_id", Long.class),
                    DSL.field("amount", Double.class),
                    DSL.field("payment_method", String.class),
                    DSL.field("created_at", Timestamp.class),
                    DSL.field("fee_amount", Double.class),
                    DSL.field("total_amount", Double.class)
                )
                .from(DSL.table("transactions"))
                .where(DSL.field("id", Long.class).eq(id))
                .fetchOne();

            return new Transaction(
                record.get("id", Long.class),
                record.get("payer_id", Long.class),
                record.get("payee_id", Long.class),
                record.get("amount", Double.class),
                record.get("payment_method", String.class),
                record.get("created_at", Timestamp.class).toInstant(),
                record.get("fee_amount", Double.class),
                record.get("total_amount", Double.class)
            );

        } catch (SQLException e) {
            throw new RuntimeException("Failed to save transaction", e);
        }
    }

    @Override
    public Optional<Transaction> findById(Long id) {
        try (Connection conn = connectionFactory.getConnection()) {
            DSLContext ctx = DSL.using(conn, SQLDialect.H2);

            Record record = ctx.select(
                    DSL.field("id", Long.class),
                    DSL.field("payer_id", Long.class),
                    DSL.field("payee_id", Long.class),
                    DSL.field("amount", Double.class),
                    DSL.field("payment_method", String.class),
                    DSL.field("created_at", Timestamp.class),
                    DSL.field("fee_amount", Double.class),
                    DSL.field("total_amount", Double.class)
                )
                .from(DSL.table("transactions"))
                .where(DSL.field("id", Long.class).eq(id))
                .fetchOne();

            if (record == null) {
                return Optional.empty();
            }

            return Optional.of(new Transaction(
                record.get("id", Long.class),
                record.get("payer_id", Long.class),
                record.get("payee_id", Long.class),
                record.get("amount", Double.class),
                record.get("payment_method", String.class),
                record.get("created_at", Timestamp.class).toInstant(),
                record.get("fee_amount", Double.class),
                record.get("total_amount", Double.class)
            ));

        } catch (SQLException e) {
            throw new RuntimeException("Failed to find transaction", e);
        }
    }

    @Override
    public List<Transaction> findByUserId(Long userId) {
        try (Connection conn = connectionFactory.getConnection()) {
            DSLContext ctx = DSL.using(conn, SQLDialect.H2);

            return ctx.select(
                    DSL.field("id", Long.class),
                    DSL.field("payer_id", Long.class),
                    DSL.field("payee_id", Long.class),
                    DSL.field("amount", Double.class),
                    DSL.field("payment_method", String.class),
                    DSL.field("created_at", Timestamp.class),
                    DSL.field("fee_amount", Double.class),
                    DSL.field("total_amount", Double.class)
                )
                .from(DSL.table("transactions"))
                .where(DSL.field("payer_id", Long.class).eq(userId)
                    .or(DSL.field("payee_id", Long.class).eq(userId)))
                .fetch()
                .stream()
                .map(record -> new Transaction(
                    record.get("id", Long.class),
                    record.get("payer_id", Long.class),
                    record.get("payee_id", Long.class),
                    record.get("amount", Double.class),
                    record.get("payment_method", String.class),
                    record.get("created_at", Timestamp.class).toInstant(),
                    record.get("fee_amount", Double.class),
                    record.get("total_amount", Double.class)
                ))
                .collect(Collectors.toList());

        } catch (SQLException e) {
            throw new RuntimeException("Failed to find transactions by user", e);
        }
    }
}
