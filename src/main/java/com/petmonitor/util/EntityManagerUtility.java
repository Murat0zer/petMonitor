package com.petmonitor.util;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;


public class EntityManagerUtility {

    private static EntityManagerFactory entityManagerFactory = Persistence
            .createEntityManagerFactory("PersistenceUnit");

    private EntityManagerUtility() {
    }

    public static EntityManagerFactory getEntityManagerFactory() {
        return entityManagerFactory;
    }

}
