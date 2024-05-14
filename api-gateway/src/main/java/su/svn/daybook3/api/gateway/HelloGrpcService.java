/*
 * This file was last modified at 2024-05-14 20:56 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * HelloGrpcService.java
 * $Id$
 */

package su.svn.daybook3.api.gateway;

import io.quarkus.grpc.GrpcService;

import io.smallrye.mutiny.Uni;
import su.svn.daybook3.HelloGrpc;
import su.svn.daybook3.HelloReply;
import su.svn.daybook3.HelloRequest;

@GrpcService
public class HelloGrpcService implements HelloGrpc {

    @Override
    public Uni<HelloReply> sayHello(HelloRequest request) {
        return Uni.createFrom()
                .item("Hello " + request.getName() + "!")
                .map(msg -> HelloReply.newBuilder().setMessage(msg).build());
    }
}
