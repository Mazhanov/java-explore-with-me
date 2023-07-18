package ru.practicum.core.pagination;

import lombok.experimental.UtilityClass;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@UtilityClass
public class PaginationMapper {
    public Pageable toPageable(Integer from, Integer size) {

        int page = from / size;
        return PageRequest.of(page, size);
    }
}