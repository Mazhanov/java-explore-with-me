package ru.practicum.core.pagination;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PaginationMapper {
    public static Pageable toPageable(Integer from, Integer size) {

        int page = from / size;
        return PageRequest.of(page, size);
    }
}