package com.lucinde.plannerpro.utils;

import java.util.List;

//todo: mag dit volgens de DTO-techniek? -DTO is maar 1 taak en het hoeft niet naar de database, vandaar in deze class toegevoegd

public class PageResponse<T> {
        public Long count;

        public int totalPages;

        public boolean hasNext;
        public boolean hasPrevious;
        public List<T> items;

}
