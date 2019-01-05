package com.petmonitor;

import com.petmonitor.user.model.Role;
import com.petmonitor.user.repository.RoleDAOImpl;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

@Slf4j
public class MyServletContextListener implements ServletContextListener {

    private RoleDAOImpl roleDAO = new RoleDAOImpl();

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        roleDAO.save(Role.ADMIN);
        roleDAO.save(Role.OWNER);

    }
}
