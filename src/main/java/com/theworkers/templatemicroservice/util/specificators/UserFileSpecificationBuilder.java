package com.theworkers.templatemicroservice.util.specificators;

import com.theworkers.templatemicroservice.model.UserFile;
import com.theworkers.templatemicroservice.model.enums.FileCategory;
import com.theworkers.templatemicroservice.model.enums.FileStatus;
import com.theworkers.templatemicroservice.util.GenericSpecificationBuilder;
import org.springframework.data.jpa.domain.Specification;

import java.util.HashMap;
import java.util.Map;

public class UserFileSpecificationBuilder {

    private final Map<String, String> filters;

    public UserFileSpecificationBuilder(Map<String, String> filters) {
        this.filters = filters;
    }

    public Specification<UserFile> build() {
        // 1. Copia mutable (IMPORTANTE)
        Map<String, String> remainingFilters = new HashMap<>(filters);

        Specification<UserFile> resultSpec = Specification.where(null);

        // --- FILTRO STATUS ---
        if (remainingFilters.containsKey("status")) {
            String statusStr = remainingFilters.get("status");
            remainingFilters.remove("status");

            resultSpec = resultSpec.and((root, query, cb) -> {
                try {
                    return cb.equal(root.get("status"), FileStatus.valueOf(statusStr));
                } catch (IllegalArgumentException e) {
                    return cb.disjunction();
                }
            });
        }

        // --- FILTRO FILE CATEGORY ---
        if (remainingFilters.containsKey("fileCategory")) {
            String categoryStr = remainingFilters.get("fileCategory");
            remainingFilters.remove("fileCategory");

            resultSpec = resultSpec.and((root, query, cb) -> {
                try {
                    return cb.equal(root.get("fileCategory"), FileCategory.valueOf(categoryStr));
                } catch (IllegalArgumentException e) {
                    return cb.disjunction();
                }
            });
        }

        // --- FILTRO FILE NAME (Opcional, si quieres búsqueda específica) ---
        if (remainingFilters.containsKey("fileName")) {
            String fileName = remainingFilters.get("fileName");
            remainingFilters.remove("fileName");

            resultSpec = resultSpec.and((root, query, cb) ->
                    cb.like(cb.lower(root.get("fileName")), "%" + fileName.toLowerCase() + "%")
            );
        }

        // --- GENÉRICO ---
        if (!remainingFilters.isEmpty()) {
            Specification<UserFile> genericSpec = new GenericSpecificationBuilder<UserFile>(remainingFilters).build();
            resultSpec = resultSpec.and(genericSpec);
        }

        return resultSpec;
    }
}