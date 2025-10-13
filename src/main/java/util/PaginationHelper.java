package util;

import org.springframework.data.domain.Page;

public class PaginationHelper {
    public static <T> PaginatedResponse<T> paginate(Page<T> pageData) {
        return new PaginatedResponse<>(
                pageData.getContent(),
                pageData.getNumber(),
                pageData.getSize(),
                pageData.getTotalElements(),
                pageData.getTotalPages(),
                pageData.isLast()
        );
    }
}

