/*
 * This file was last modified at 2023.01.09 23:14 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * AuthenticationContextImpl.java
 * $Id$
 */

package su.svn.daybook.services.security;

import javax.enterprise.context.RequestScoped;
import java.security.Principal;

@RequestScoped
public class AuthenticationContext {

    private volatile Principal principal;

    public Principal getPrincipal() {
        return principal;
    }

    public void setPrincipal(Principal principal) {
        this.principal = principal;
    }
}
