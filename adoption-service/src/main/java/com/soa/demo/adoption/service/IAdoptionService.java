package com.soa.demo.adoption.service;

import com.soa.demo.adoption.model.AdoptionRequest;
import com.soa.demo.adoption.model.User;

import java.util.List;

public interface IAdoptionService {
    public AdoptionRequest create(AdoptionRequest adoptionRequest);
    public void reactToAdoptionRequest(AdoptionRequest adoptionRequest, String reaction);
    public List<AdoptionRequest> findAll(Integer pageNumber, Integer pageSize);
    public Long countAll();
    public Long countByUser(User user);
    public AdoptionRequest findById(Long id);
    public List<AdoptionRequest> findByUserId(Long userId, Integer pageNumber, Integer pageSize);
    public boolean deleteById(Long id);
}
