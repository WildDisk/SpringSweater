package com.example.springsweater.createDataTests

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Component

/**
 *
 *
 * @project SpringSweater
 * @author WildDisk on 09.12.2019
 */
@Component
class CreateDataForTest {
    @Autowired
    private lateinit var jdbcTemplate: JdbcTemplate

    fun createUsers() {
        println(">> Deleting users and recreate")
        jdbcTemplate.execute("""
            delete from user_role;
            delete from usr;

            insert into usr(id, username, password, is_active) values
            (1, 'admin', '${'$'}2a${'$'}08${'$'}mKEBBaUQgvJOy7xNZMzrReFVRKjxhuyz/Kwefz8anGvLZ4yXV1Bbu', true),
            (2, 'John', '${'$'}2a${'$'}08${'$'}mKEBBaUQgvJOy7xNZMzrReFVRKjxhuyz/Kwefz8anGvLZ4yXV1Bbu', true);

            insert into user_role(user_id, roles) values
            (1, 'ADMIN'), (1, 'USER'),
            (2, 'USER');
        """.trimIndent())

    }

    fun deleteUsers() {
        println(">> Deleting users")
        jdbcTemplate.execute("""
            delete from user_role;
            delete from usr;
        """.trimIndent())

    }

    fun createMessages() {
        println(">> Deleting messages and recreate")
        jdbcTemplate.execute("""
            delete from message;

            insert into message(id, text, tag, user_id)
            values (1, 'first message', 'first tag', 1),
                   (2, 'second message', 'second tag', 1),
                   (3, 'third message', 'third tag', 1),
                   (4, 'forth message', 'forth tag', 1);
                   
            alter sequence hibernate_sequence restart with 10;
        """.trimIndent())
    }

    fun deleteMessages() {
        println(">> Deleting messages")
        jdbcTemplate.execute("""
            delete from message;
        """.trimIndent())
    }
}