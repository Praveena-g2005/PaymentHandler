package com.paymenthandler.persistence;

import com.paymenthandler.dao.BalanceDao;
import com.paymenthandler.model.Balance;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

@ApplicationScoped
@Named("balanceDao")

public class JooqBalanceDao implements BalanceDao {

    @Inject
    private DatabaseConnectionFactory connectionFactory;

    @Override
    public Optional<Balance> getBalance(Long userId) {
        try (Connection conn = connectionFactory.getConnection()) {
            DSLContext ctx = DSL.using(conn, SQLDialect.H2);

            Record record = ctx.select(
                    DSL.field("user_id", Long.class),
                    DSL.field("amount", Double.class)
                )
                .from(DSL.table("balances"))
                .where(DSL.field("user_id", Long.class).eq(userId))
                .fetchOne();

            if (record == null) {
                return Optional.empty();
            }

            return Optional.of(new Balance(
                record.get("user_id", Long.class),
                record.get("amount", Double.class)
            ));

        } catch (SQLException e) {
            throw new RuntimeException("Failed to get balance", e);
        }
    }

    @Override
    public Balance updateBalance(Long userId, double amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Balance cannot be negative");
        }

        try (Connection conn = connectionFactory.getConnection()) {
            DSLContext ctx = DSL.using(conn, SQLDialect.H2);

            // Checking if balance exists
            Integer count = ctx.selectCount()
                .from(DSL.table("balances"))
                .where(DSL.field("user_id", Long.class).eq(userId))
                .fetchOne(0, Integer.class);

            if (count != null && count > 0) {
                // Updating existing balance
                ctx.update(DSL.table("balances"))
                    .set(DSL.field("amount"), amount)
                    .set(DSL.field("updated_at"), DSL.currentTimestamp())
                    .where(DSL.field("user_id", Long.class).eq(userId))
                    .execute();
            } else {
                ctx.insertInto(DSL.table("balances"))
                    .columns(DSL.field("user_id"), DSL.field("amount"))
                    .values(userId, amount)
                    .execute();
            }

            return new Balance(userId, amount);

        } catch (SQLException e) {
            throw new RuntimeException("Failed to update balance", e);
        }
    }

    @Override
    public Optional<String> transferBalance(Long fromUserId, Long toUserId, double amount) {
        if (amount <= 0) {
            return Optional.of("Transfer amount must be greater than 0");
        }

        Connection conn = null;
        try {
            conn = connectionFactory.getConnection();
            conn.setAutoCommit(false); 

            DSLContext ctx = DSL.using(conn, SQLDialect.H2);

            Record fromRecord = ctx.select(
                    DSL.field("user_id", Long.class),
                    DSL.field("amount", Double.class)
                )
                .from(DSL.table("balances"))
                .where(DSL.field("user_id", Long.class).eq(fromUserId))
                .fetchOne();

            if (fromRecord == null) {
                conn.rollback();
                return Optional.of("Sender has no balance record");
            }

            double fromBalance = fromRecord.get("amount", Double.class);
            System.out.println("Payer Balance: " + fromBalance);

            if (fromBalance < amount) {
                conn.rollback();
                return Optional.of("Insufficient funds");
            }

            ctx.update(DSL.table("balances"))
                .set(DSL.field("amount"), fromBalance - amount)
                .set(DSL.field("updated_at"), DSL.currentTimestamp())
                .where(DSL.field("user_id", Long.class).eq(fromUserId))
                .execute();

            Record toRecord = ctx.select(
                    DSL.field("user_id", Long.class),
                    DSL.field("amount", Double.class)
                )
                .from(DSL.table("balances"))
                .where(DSL.field("user_id", Long.class).eq(toUserId))
                .fetchOne();

            double newToBalance;
            if (toRecord != null) {
                
                double currentToBalance = toRecord.get("amount", Double.class);
                newToBalance = currentToBalance + amount;
                ctx.update(DSL.table("balances"))
                    .set(DSL.field("amount"), newToBalance)
                    .set(DSL.field("updated_at"), DSL.currentTimestamp())
                    .where(DSL.field("user_id", Long.class).eq(toUserId))
                    .execute();
            } else {
                
                newToBalance = amount;
                ctx.insertInto(DSL.table("balances"))
                    .columns(DSL.field("user_id"), DSL.field("amount"))
                    .values(toUserId, newToBalance)
                    .execute();
            }

            System.out.println("Payee Balance after transaction: " + newToBalance);

            conn.commit();
            return Optional.empty();

        } catch (Exception e) {
            if (conn != null) {
                try {
                    conn.rollback();
                    System.err.println("Transaction rolled back due to error: " + e.getMessage());
                } catch (SQLException rollbackEx) {
                    System.err.println("Error during rollback: " + rollbackEx.getMessage());
                }
            }
            throw new RuntimeException("Failed to transfer balance", e);
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true); 
                    conn.close();
                } catch (SQLException e) {
                    System.err.println("Error closing connection: " + e.getMessage());
                }
            }
        }
    }
}
