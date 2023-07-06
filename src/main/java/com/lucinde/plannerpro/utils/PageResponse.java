package com.lucinde.plannerpro.utils;

import java.util.List;

public class PageResponse<T> {
        public Long count;

        public int totalPages;

        public boolean hasNext;
        public boolean hasPrevious;
        public List<T> items;

}
