package com.ecore.roles.exception;

import io.micrometer.core.lang.Nullable;

import java.util.UUID;

import static java.lang.String.format;

public class ResourceNotFoundException extends RuntimeException {

    public <T> ResourceNotFoundException(Class<T> resource, UUID id, @Nullable UUID teamId) {
        super(format("%s %s %s not found", resource.getSimpleName(), id, teamId));
    }

    public <T> ResourceNotFoundException(Class<T> resource, UUID id) {
        super(format("%s %s not found", resource.getSimpleName(), id));
    }
}
