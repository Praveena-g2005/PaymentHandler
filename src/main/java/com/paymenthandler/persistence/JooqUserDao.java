package com.paymenthandler.persistence;

import com.paymenthandler.dao.UserDao;
import com.paymenthandler.model.User;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * JOOQ-based UserDao implementation
 * Demonstrates:
 * - JOOQ for type-safe SQL queries
 * - Try-with-resources for connection management
 * - CDI @Named qualifier
 */
@ApplicationScoped
@Named("jooqUserDao")
public class JooqUserDao implements UserDao {

    @Inject
    private DatabaseConnectionFactory connectionFactory;

    /**
     * Create a new user
     * Demonstrates: JOOQ INSERT with RETURNING clause
     */
    @Override
    public User createUser(User user) {
        // Try-with-resources: Connection auto-closes
        try (Connection conn = connectionFactory.getConnection()) {
            DSLContext ctx = DSL.using(conn, SQLDialect.H2);

            // JOOQ type-safe INSERT
            Record record = ctx.insertInto(DSL.table("users"))
                .columns(DSL.field("username"), DSL.field("email"))
                .values(user.getName(), user.getEmail())
                .returningResult(
                    DSL.field("id", Long.class),
                    DSL.field("username", String.class),
                    DSL.field("email", String.class)
                )
                .fetchOne();

            return new User(
                record.get("id", Long.class),
                record.get("username", String.class),
                record.get("email", String.class)
            );

        } catch (SQLException e) {
            throw new RuntimeException("Failed to create user", e);
        }
    }

    /**
     * Find user by ID
     * Demonstrates: JOOQ SELECT with WHERE clause + Optional
     */
    @Override
    public Optional<User> findById(Long id) {
        // Try-with-resources for automatic connection cleanup
        try (Connection conn = connectionFactory.getConnection()) {
            DSLContext ctx = DSL.using(conn, SQLDialect.H2);

            // JOOQ type-safe SELECT
            Record record = ctx.select(
                    DSL.field("id", Long.class),
                    DSL.field("username", String.class),
                    DSL.field("email", String.class)
                )
                .from(DSL.table("users"))
                .where(DSL.field("id", Long.class).eq(id))
                .fetchOne();

            if (record == null) {
                return Optional.empty();
            }

            return Optional.of(new User(
                record.get("id", Long.class),
                record.get("username", String.class),
                record.get("email", String.class)
            ));

        } catch (SQLException e) {
            throw new RuntimeException("Failed to find user", e);
        }
    }

    /**
     * Find all users
     * Demonstrates: JOOQ SELECT ALL + Stream API
     */
    @Override
    public List<User> findAllUsers() {
        try (Connection conn = connectionFactory.getConnection()) {
            DSLContext ctx = DSL.using(conn, SQLDialect.H2);

            // JOOQ SELECT all users
            return ctx.select(
                    DSL.field("id", Long.class),
                    DSL.field("username", String.class),
                    DSL.field("email", String.class)
                )
                .from(DSL.table("users"))
                .fetch()
                .stream()  // Stream API
                .map(record -> new User(  // Lambda + map
                    record.get("id", Long.class),
                    record.get("username", String.class),
                    record.get("email", String.class)
                ))
                .collect(Collectors.toList());  // Collectors

        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch users", e);
        }
    }

    /**
     * Update user
     * Demonstrates: JOOQ UPDATE
     */
    @Override
    public Optional<User> updateUser(User user) {
        try (Connection conn = connectionFactory.getConnection()) {
            DSLContext ctx = DSL.using(conn, SQLDialect.H2);

            // JOOQ type-safe UPDATE
            int updated = ctx.update(DSL.table("users"))
                .set(DSL.field("username"), user.getName())
                .set(DSL.field("email"), user.getEmail())
                .where(DSL.field("id", Long.class).eq(user.getId()))
                .execute();

            return updated > 0 ? Optional.of(user) : Optional.empty();

        } catch (SQLException e) {
            throw new RuntimeException("Failed to update user", e);
        }
    }

    /**
     * Delete user
     * Demonstrates: JOOQ DELETE
     */
    @Override
    public boolean deleteUser(Long id) {
        try (Connection conn = connectionFactory.getConnection()) {
            DSLContext ctx = DSL.using(conn, SQLDialect.H2);

            // JOOQ type-safe DELETE
            int deleted = ctx.deleteFrom(DSL.table("users"))
                .where(DSL.field("id", Long.class).eq(id))
                .execute();

            return deleted > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete user", e);
        }
    }
}
