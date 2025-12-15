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
        try (Connection conn = connectionFactory.getConnection()) {
            DSLContext ctx = DSL.using(conn, SQLDialect.H2);

            // Check if balance exists
            Integer count = ctx.selectCount()
                .from(DSL.table("balances"))
                .where(DSL.field("user_id", Long.class).eq(userId))
                .fetchOne(0, Integer.class);

            if (count != null && count > 0) {
                // Update existing balance
                ctx.update(DSL.table("balances"))
                    .set(DSL.field("amount"), amount)
                    .set(DSL.field("updated_at"), DSL.currentTimestamp())
                    .where(DSL.field("user_id", Long.class).eq(userId))
                    .execute();
            } else {
                // Insert new balance
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
}
