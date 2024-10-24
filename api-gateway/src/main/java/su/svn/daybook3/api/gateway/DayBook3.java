/*
 * This file was last modified at 2024-05-24 10:14 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * DayBook3.java
 * $Id$
 */

package su.svn.daybook3.api.gateway;

import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeIn;
import org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeType;
import org.eclipse.microprofile.openapi.annotations.info.Contact;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.info.License;
import org.eclipse.microprofile.openapi.annotations.security.SecurityScheme;

import jakarta.ws.rs.core.Application;

@OpenAPIDefinition(
        info = @Info(
                title = "DayBook 3 API",
                version = "3.10.0",
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
public class DayBook3 extends Application {
}