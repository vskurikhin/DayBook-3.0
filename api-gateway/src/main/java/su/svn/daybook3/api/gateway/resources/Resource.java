/*
 * This file was last modified at 2024-05-14 21:35 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * Resource.java
 * $Id$
 */

package su.svn.daybook3.api.gateway.resources;

import io.smallrye.mutiny.Uni;
import su.svn.daybook3.api.gateway.models.Identification;

import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

import java.io.Serializable;

public interface Resource<K extends Comparable<? extends Serializable>, V extends Identification<K>> {
    Uni<Response> get(K id, @Context UriInfo uriInfo);

    Uni<Response> page(@QueryParam("page") Long page, @QueryParam("limit") Short limit);

    Uni<Response> post(V entry, @Context UriInfo uriInfo);

    Uni<Response> put(V entry, @Context UriInfo uriInfo);

    Uni<Response> delete(K id, @Context UriInfo uriInfo);
}
