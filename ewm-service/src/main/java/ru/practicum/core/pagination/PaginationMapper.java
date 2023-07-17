package ru.practicum.core.pagination;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.practicum.core.exception.ValidationException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PaginationMapper {
    public static Pageable toPageable(Integer from, Integer size) {
        /*if (from == null || size == null) {
            return null;
        }*/

        int page = from / size;
        return PageRequest.of(page, size);
    }
}