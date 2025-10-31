package com.theworkers.rolemicroservice.util;

import jakarta.persistence.criteria.Path;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GenericSpecificationBuilder<T> {

    private final Map<String, String> filters;

    public GenericSpecificationBuilder(Map<String, String> filters) {
        this.filters = filters;
    }

    public Specification<T> build() {
        if (filters == null || filters.isEmpty()) {
            return (root, query, cb) -> cb.conjunction();
        }

        List<Specification<T>> specs = new ArrayList<>();
        for (Map.Entry<String, String> entry : filters.entrySet()) {
            String field = entry.getKey();
            String value = entry.getValue();

            if (value == null || value.trim().isEmpty()) {
                continue;
            }

            specs.add((root, query, cb) -> {
                try {
                    Path<?> path = getPath(root, field);
                    if (path.getJavaType() == String.class) {
                        return cb.like((Path<String>) path, "%" + value + "%");
                    }
                    if (path.getJavaType() == Boolean.class) {
                        return cb.equal((Path<Boolean>) path, Boolean.parseBoolean(value));
                    }
                    return cb.conjunction();
                } catch (IllegalArgumentException e) {
                    throw new ValidateWebException(
                            "The field:  '" + field + "' does not exist in the entity.",
                            HttpStatus.BAD_REQUEST
                    );
                }
            });
        }
        return specs.stream().reduce(Specification::and).orElse((root, query, cb) -> cb.conjunction());
    }

    private Path<?> getPath(jakarta.persistence.criteria.Root<T> root, String field) {
        if (field.contains(".")) {
            String[] parts = field.split("\\.");
            Path<?> path = root.get(parts[0]);
            for (int i = 1; i < parts.length; i++) {
                path = path.get(parts[i]);
            }
            return path;
        }
        return root.get(field);
    }
}