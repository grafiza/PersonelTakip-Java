package com.personel.PersonelTakip.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IService<T> {
    T save(T t);

    T getById(Long id);



    List<T> getAll();

    Page<T> getAll(Pageable pageable);

    T update(T t);

    void delete(Long id);
}
