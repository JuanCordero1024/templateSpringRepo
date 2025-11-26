package com.theworkers.usermicroservice.util.specificators;

import com.theworkers.usermicroservice.model.User;
import com.theworkers.usermicroservice.model.enums.UserStatus;
import com.theworkers.usermicroservice.util.GenericSpecificationBuilder;
import org.springframework.data.jpa.domain.Specification;

import java.util.HashMap;
import java.util.Map;

public class UserSpecificationBuilder {

    private final Map<String, String> filters;

    public UserSpecificationBuilder(Map<String, String> filters) {
        this.filters = filters;
    }

    public Specification<User> build() {
        Map<String, String> remainingFilters = new HashMap<>(filters);

        // 2. Empezamos con una especificación "neutra" (siempre true)
        Specification<User> resultSpec = Specification.where(null);

        // --- CASO ESPECIAL: STATUS (Enum) ---
        if (remainingFilters.containsKey("status")) {
            String statusStr = remainingFilters.get("status");
            // Eliminamos del mapa para que el GenericBuilder no se confunda
            remainingFilters.remove("status");

            // Agregamos la regla manual convirtiendo String -> Enum
            resultSpec = resultSpec.and((root, query, cb) -> {
                try {
                    return cb.equal(root.get("status"), UserStatus.valueOf(statusStr));
                } catch (IllegalArgumentException e) {
                    return cb.disjunction();
                }
            });
        }

        if (remainingFilters.containsKey("roleId")) {
            String roleIdStr = remainingFilters.get("roleId");
            remainingFilters.remove("roleId");

            resultSpec = resultSpec.and((root, query, cb) ->
                    cb.equal(root.get("roleId"), Long.valueOf(roleIdStr))
            );
        }

        if (!remainingFilters.isEmpty()) {
            Specification<User> genericSpec = new GenericSpecificationBuilder<User>(remainingFilters).build();
            resultSpec = resultSpec.and(genericSpec);
        }

        return resultSpec;
    }
}