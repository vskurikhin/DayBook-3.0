package su.svn.daybook;

import io.smallrye.jwt.build.Jwt;
import org.eclipse.microprofile.jwt.Claims;

import java.util.Arrays;
import java.util.HashSet;

public class GenerateToken {
    /**
     * Generate JWT token
     */
    public static void main(String[] args) {
        System.out.println("Gradle command line arguments example");
        for (String arg : args) {
            System.out.println("Got argument [" + arg + "]");
        }
        System.out.println("System.getProperty(\"\") = " + System.getProperty("smallrye.jwt.sign.key.location"));
        String token =
                Jwt.issuer("https://svn.su/issuer")
                        .upn("jdoe@quarkus.io")
                        .groups(new HashSet<>(Arrays.asList("User", "Admin")))
                        .claim(Claims.birthdate.name(), "222-01-01")
                        .sign();
        System.out.println(token);
    }
}
