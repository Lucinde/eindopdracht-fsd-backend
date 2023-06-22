package com.lucinde.plannerpro.utils;

import java.util.List;

//todo: mag dit volgens de DTO-techniek? -DTO is maar 1 taak en het hoeft niet naar de database, vandaar in deze class toegevoegd
// omdat je de list met TaskDto nodig hebt wel in deze klasse en niet als helper in de algemene helper-klasse

public class PageResponse<T> {
        public Long count;
        public List<T> tasks;
}
