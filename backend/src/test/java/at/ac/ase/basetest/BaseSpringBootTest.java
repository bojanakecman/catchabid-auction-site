package at.ac.ase.basetest;

import at.ac.ase.AuctionApplication;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;

/**
 * Base Spring Boot Integration Test which makes it possible to
 * use dependency injection and database utils
 */

@AutoConfigureTestEntityManager
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = AuctionApplication.class)

public abstract class BaseSpringBootTest {

    @Autowired
    protected TestEntityManager testEntityManager;

    @Autowired
    protected TransactionTemplate transactionTemplate;

    /**
     * insert data from rousources folder "testdata"
     * in a separate transaction
     * @param filename in "testdata"
     */
    protected void insertTestData(String filename)
    {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("testdata/" + filename)) {

            String content = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
            tx(status -> executeSql(content));

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("couldnt load contents of file " + filename);
        }
    }

    /**
     * cleans all data in all tables
     * in separate transaction
     */
    protected void cleanDatabase() {
        tx(status -> {
            executeJpql("DELETE FROM PasswordResetToken");
            executeJpql("DELETE FROM Notification");
            executeJpql("DELETE FROM Rating");
            executeJpql("UPDATE AuctionPost SET bid_id = NULL WHERE bid_id IS NOT NULL");
            executeJpql("DELETE FROM Bid");
            executeJpql("DELETE FROM AuctionPost");
            executeJpql("DELETE FROM AuctionHouse");
            executeSql("DELETE FROM regular_user_preferences");
            executeSql("DELETE FROM auction_subscriptions");
            executeJpql("DELETE FROM RegularUser");

        });
    }

    /**
     * short callback function wrapper for better readability
     * example: tx(s -> doInTx());
     */
    protected void tx(Consumer<TransactionStatus> action) {
        transactionTemplate.executeWithoutResult(action);
    }

    /**
     * short callback function wrapper with result for better readability
     * example: var = txGetResult(s -> doInTx());
     */
    protected <T> T txGetResult(TransactionCallback<T> action) {
        return transactionTemplate.execute(action);
    }

    /**
     * execute plain sql
     * @param sql script to execute
     */
    protected void executeSql(String sql) {
        getEntityManager().createNativeQuery(sql).executeUpdate();
    }

    /**
     * execute jpql
     * @param jpql script to execute
     */
    protected void executeJpql(String jpql) {
        getEntityManager().createQuery(jpql).executeUpdate();
    }

    protected EntityManager getEntityManager() {
        return testEntityManager.getEntityManager();
    }
}
