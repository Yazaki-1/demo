package com.example.lib.core.base;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;

public abstract class BaseCrudController<ModelT,IdT extends Serializable,ServerT extends CRUDExServiceInterface<ModelT,IdT>> {

    @Autowired
    protected ServerT service;

    @GetMapping("/")
    public Object index(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", defaultValue = "id") String sort,
            @RequestParam(value = "direction", defaultValue = "DESC") String direction
    ) {
        return service.getAllOfPage(page, size, Sort.Direction.fromString(direction),sort);
    }

    @GetMapping("/{id}")
    public ModelT show(@PathVariable("id") IdT id) {
        return service.show(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ModelT save(@RequestBody ModelT t) {
        return service.store(t);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ModelT update(@PathVariable("id") IdT id, @RequestBody ModelT t) {
        return service.update(id,t);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") IdT id) {
        service.destroy(id);
    }
}