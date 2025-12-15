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


@ApplicationScoped
@Named("jooqUserDao")
public class JooqUserDao implements UserDao {

    @Inject
    private DatabaseConnectionFactory connectionFactory;

    //Creating new user
     
    @Override
    public User createUser(User user) {

        try (Connection conn = connectionFactory.getConnection()) {
            DSLContext ctx = DSL.using(conn, SQLDialect.H2);

            // JOOQ type-safe INSERT
            ctx.insertInto(DSL.table("users"))
                .columns(DSL.field("username"), DSL.field("email"))
                .values(user.getName(), user.getEmail())
                .execute();
                
            Record record = ctx.select(
                    DSL.field("id", Long.class),
                    DSL.field("username", String.class),
                    DSL.field("email", String.class)
                )
                .from(DSL.table("users"))
                .where(DSL.field("email", String.class).eq(user.getEmail()))
                .fetchOne();

            if (record == null) {
                throw new RuntimeException("Failed to retrieve created user");
            }

            return new User(
                record.get("id", Long.class),
                record.get("username", String.class),
                record.get("email", String.class)
            );

        } catch (SQLException e) {
            throw new RuntimeException("Failed to create user", e);
        }
    }

     
    @Override
    public Optional<User> findById(Long id) {

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

    @Override
    public Optional<User> findByEmail(String email) {

        try (Connection conn = connectionFactory.getConnection()) {
            DSLContext ctx = DSL.using(conn, SQLDialect.H2);

            // JOOQ type-safe SELECT by email
            Record record = ctx.select(
                    DSL.field("id", Long.class),
                    DSL.field("username", String.class),
                    DSL.field("email", String.class)
                )
                .from(DSL.table("users"))
                .where(DSL.field("email", String.class).eq(email))
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
            throw new RuntimeException("Failed to find user by email", e);
        }
    }

    @Override
    public List<User> findAllUsers() {
        try (Connection conn = connectionFactory.getConnection()) {
            DSLContext ctx = DSL.using(conn, SQLDialect.H2);

            return ctx.select(
                    DSL.field("id", Long.class),
                    DSL.field("username", String.class),
                    DSL.field("email", String.class)
                )
                .from(DSL.table("users"))
                .fetch()
                .stream()  
                .map(record -> new User(  
                    record.get("id", Long.class),
                    record.get("username", String.class),
                    record.get("email", String.class)
                ))
                .collect(Collectors.toList()); 

        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch users", e);
        }
    }

    @Override
    public Optional<User> updateUser(User user) {
        try (Connection conn = connectionFactory.getConnection()) {
            DSLContext ctx = DSL.using(conn, SQLDialect.H2);

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

    @Override
    public boolean deleteUser(Long id) {
        try (Connection conn = connectionFactory.getConnection()) {
            DSLContext ctx = DSL.using(conn, SQLDialect.H2);

            int deleted = ctx.deleteFrom(DSL.table("users"))
                .where(DSL.field("id", Long.class).eq(id))
                .execute();

            return deleted > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete user", e);
        }
    }
}
