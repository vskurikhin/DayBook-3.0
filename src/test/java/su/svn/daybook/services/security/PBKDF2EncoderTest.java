package su.svn.daybook.services.security;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class PBKDF2EncoderTest {

    @Inject
    PBKDF2Encoder pbkdf2Encoder;

    @Test
    void test() {
        var encoded = pbkdf2Encoder.encode("user");
        System.out.println("encoded = " + encoded);
    }
}