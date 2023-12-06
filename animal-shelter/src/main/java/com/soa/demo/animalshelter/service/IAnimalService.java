package com.soa.demo.animalshelter.service;

import com.soa.demo.animalshelter.model.Animal;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;

public interface IAnimalService {
    public Animal create(Animal animal) throws DataIntegrityViolationException;
    public List<Animal> findAll(Integer pageNumber, Integer pageSize);
    public Long countAll();
    public Animal findById(Long id);
    public void update(Animal animal);
    public boolean deleteById(Long id);
    public void uploadImage(Long id, byte[] image);
}
