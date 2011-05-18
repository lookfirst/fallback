package com.github.fallback.dao;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.EntityManagerFactory;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.github.fallback.biz.FallbackBean;
import com.github.fallback.biz.i.FallbackJobStatus;
import com.github.fallback.entity.FallbackJob;
import com.github.fallback.util.BaseDAO;

/**
 * Manages data access to jobs. Assumes the dao is wrapped in a transaction already.
 *
 * @author jon
 */
@Repository
public class FallbackJobDao extends BaseDAO<Long, FallbackJob> {

    /** */
    private static final Logger log = LoggerFactory.getLogger(FallbackBean.class);

    /** */
    @Inject
    EntityManagerFactory entityManagerFactory;

    /** */
    @PostConstruct
    public void init() {
        super.setEntityManagerFactory(entityManagerFactory);
    }

    /**
     * Creates a new job and returns the newly created object.
     */
    public FallbackJob create(Long clientId, DateTime reportFrom, DateTime reportTo) {
        FallbackJob job = new FallbackJob(clientId, reportFrom.toDate(), reportTo.toDate());
        this.persist(job);
        this.flush(job);
        if (log.isDebugEnabled()) {
            log.debug("Created: " + job);
        }
        return job;
    }

    /**
     * Sets the status of the job to STARTED
     */
    public void setJobStarted(FallbackJob job) {
        job.setStatus(FallbackJobStatus.STARTED);
    }

    /**
     * Sets the status of the job to FAILED
     */
    public void setJobFailed(FallbackJob job) {
        job.setStatus(FallbackJobStatus.FAILED);
    }

    /**
     * Sets the status of the job to COMPLETE
     */
    public void setJobComplete(FallbackJob job) {
        job.setStatus(FallbackJobStatus.COMPLETE);
    }

    /**
     * Sets the status of the job to SUCCESSFUL
     */
    public void setJobSuccessful(FallbackJob job) {
        job.setStatus(FallbackJobStatus.SUCCESSFUL);
    }

    /**
     * Get a list of jobs with status STARTED
     */
    public List<FallbackJob> getStartedJobs() {
        return this.findSome("status", FallbackJobStatus.STARTED);
    }

    /**
     * Get a list of jobs with status FAILED
     */
    public List<FallbackJob> getFailedJobs() {
        return this.findSome("status", FallbackJobStatus.FAILED);
    }

    /**
     * Get a list of jobs with status COMPLETE
     */
    public List<FallbackJob> getCompleteJobs() {
        return this.findSome("status", FallbackJobStatus.COMPLETE);
    }

    /**
     * Get a list of jobs with status SUCCESSFUL
     */
    public List<FallbackJob> getSuccessfulJobs() {
        return this.findSome("status", FallbackJobStatus.SUCCESSFUL);
    }
}
