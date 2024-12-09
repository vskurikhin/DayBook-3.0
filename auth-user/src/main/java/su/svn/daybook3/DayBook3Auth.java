/*
 * This file was last modified at 2024-10-30 14:17 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * DayBook3Auth.java
 * $Id$
 */

package su.svn.daybook3;

import jakarta.ws.rs.core.Application;
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeIn;
import org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeType;
import org.eclipse.microprofile.openapi.annotations.info.Contact;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.info.License;
import org.eclipse.microprofile.openapi.annotations.security.SecurityScheme;

@OpenAPIDefinition(
        info = @Info(
                title = "DayBook 3 API Auth",
                version = "3.13.3",
                contact = @Contact(
                        name = "To use API Support",
                        url = "https://svn.su",
                        email = "stalker@quake.ru"),
                license = @License(
                        name = "This is free and unencumbered software released into the public domain",
                        url = "http://unlicense.org"
                ))
)
@SecurityScheme(
        securitySchemeName = "day-book",
        scheme = "Bearer",
        type = SecuritySchemeType.HTTP,
        in = SecuritySchemeIn.HEADER
)
public class DayBook3Auth extends Application {
}