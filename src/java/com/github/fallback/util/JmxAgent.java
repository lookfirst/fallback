package com.github.fallback.util;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.persistence.EntityManagerFactory;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.management.ManagementService;

import org.hibernate.SessionFactory;
import org.hibernate.ejb.HibernateEntityManagerFactory;
import org.hibernate.jmx.StatisticsService;
import org.springframework.stereotype.Service;

/**
 * This could probably be done entirely in spring xml configuration, but since
 * it really never changes and is more like 'code', I'm putting it here. It is
 * really just a simple class for configuring ehcache and hibernate to enable jmx
 * monitoring.
 *
 * http://apaker.blogspot.com/2010/06/enabling-jmx-monitoring-for-spring-jpa.html
 *
 * @author jon
 */
@Service
public class JmxAgent {

    /** */
    @Inject
    private EntityManagerFactory entityManagerFactory;

    /** */
    @Inject
    private MBeanServer mbeanServer;

    /** */
    private ObjectName on;

    /** */
    @PostConstruct
    public void start() throws Exception {

        SessionFactory sf = ((HibernateEntityManagerFactory) entityManagerFactory).getSessionFactory();

        on = new ObjectName("Hibernate:type=statistics,application=fallback");

        StatisticsService statsMBean = new StatisticsService();
        statsMBean.setSessionFactory(sf);
        statsMBean.setStatisticsEnabled(true);
        mbeanServer.registerMBean(statsMBean, on);

        CacheManager cacheMgr = CacheManager.getInstance();
        ManagementService.registerMBeans(cacheMgr, mbeanServer, true, true, true, true);
    }

    /** */
    @PreDestroy
    public void stop() throws Exception {
        mbeanServer.unregisterMBean(on);
    }
}
