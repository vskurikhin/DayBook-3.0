/*
 * This file was last modified at 2024-10-30 09:48 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * AbstractMapper.java
 * $Id$
 */

package su.svn.daybook3.converters.mappers;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;
import su.svn.daybook3.converters.buildparts.BuildPartsAnnotatedDomainFiled;
import su.svn.daybook3.converters.buildparts.BuildPartsAnnotatedModelFiled;
import su.svn.daybook3.converters.getters.GettersAnnotatedDomainFiled;
import su.svn.daybook3.converters.getters.GettersAnnotatedModelFiled;
import su.svn.daybook3.models.Identification;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.function.Supplier;

public abstract class AbstractMapper
        <K extends Comparable<? extends Serializable>, M extends Identification<K>, D extends Identification<K>> {

    public static final String BUILD_METHOD_NAME = "build";

    @Inject
    @ConfigProperty(defaultValue = "false")
    boolean logging;

    private final BuildPartsAnnotatedModelFiled<D> buildPartsDomain;
    private final BuildPartsAnnotatedDomainFiled<M> buildPartsModel;
    private final Method domainBuilderMethodBuild;
    private final Method modelBuilderMethodBuild;

    private final Object domainBuilder;
    private final Object modelBuilder;

    private final InvokerBuildPartMethod<K, M> domainInvoker;

    private final InvokerBuildPartMethod<K, D> modelInvoker;

    private final Logger log;

    protected AbstractMapper(
            @Nonnull Class<M> classAnnotatedWithDomainField,
            @Nonnull Supplier<?> modelBuildFactory,
            @Nonnull Class<D> classAnnotatedWithModelFiled,
            @Nonnull Supplier<?> domainBuildFactory,
            @Nonnull Logger log) throws NoSuchMethodException {
        var gettersDomain = new GettersAnnotatedModelFiled<>(classAnnotatedWithModelFiled);
        var gettersModel = new GettersAnnotatedDomainFiled<>(classAnnotatedWithDomainField);
        this.buildPartsModel = new BuildPartsAnnotatedDomainFiled<>(classAnnotatedWithDomainField, modelBuildFactory);
        this.buildPartsDomain = new BuildPartsAnnotatedModelFiled<>(classAnnotatedWithModelFiled, domainBuildFactory);
        this.domainBuilder = buildPartsDomain.getBuilderFactory().get();
        this.modelBuilder = buildPartsModel.getBuilderFactory().get();
        this.domainBuilderMethodBuild = domainBuilder.getClass().getDeclaredMethod(BUILD_METHOD_NAME);
        this.modelBuilderMethodBuild = modelBuilder.getClass().getDeclaredMethod(BUILD_METHOD_NAME);
        this.domainInvoker = new InvokerBuildPartMethod<>(this.domainBuilder, gettersModel, log);
        this.modelInvoker = new InvokerBuildPartMethod<>(this.modelBuilder, gettersDomain, log);
        this.log = log;
    }

    public abstract D convertToDomain(M model);

    public abstract M convertToModel(D domain);

    @Nullable
    @SuppressWarnings("unchecked")
    public D convertModelToDomain(M model) { // todo protected
        if (logging) {
            log.tracef("convertModelToDomain(%s)", model);
        }
        buildPartsDomain.forEach(entry -> domainInvoker.invokeBuilderFor(entry, model));
        try {
            return (D) domainBuilderMethodBuild.invoke(domainBuilder);
        } catch (IllegalArgumentException | ReflectiveOperationException e) {
            log.error("domain builder build method invoke ", e);
        }
        return null;
    }

    @Nullable
    @SuppressWarnings("unchecked")
    public M convertDomainToModel(D domain) { // todo protected
        if (logging) {
            log.tracef("convertDomainToModel(%s)", domain);
        }
        buildPartsModel.forEach(entry -> modelInvoker.invokeBuilderFor(entry, domain));
        try {
            return (M) modelBuilderMethodBuild.invoke(modelBuilder);
        } catch (IllegalArgumentException | ReflectiveOperationException e) {
            log.error("model builder build method invoke ", e);
        }
        return null;
    }
}
