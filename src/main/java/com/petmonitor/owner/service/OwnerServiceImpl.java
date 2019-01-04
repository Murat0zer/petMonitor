package com.petmonitor.owner.service;

import com.petmonitor.owner.model.Owner;
import com.petmonitor.owner.repository.OwnerDAOImpl;


public class OwnerServiceImpl implements OwnerService {

    private OwnerDAOImpl ownerDao;

    public OwnerServiceImpl() {
        ownerDao = new OwnerDAOImpl();
    }


    @Override
    public Owner save(Owner owner) {
        return ownerDao.save(owner);
    }
}
