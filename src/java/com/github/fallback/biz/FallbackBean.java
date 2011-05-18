package com.github.fallback.biz;

import java.util.Date;
import java.util.List;
import java.util.Timer;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.fallback.biz.i.Fallback;
import com.github.fallback.biz.i.FallbackJobStatus;
import com.github.fallback.dao.FallbackJobDao;
import com.github.fallback.entity.FallbackJob;

/**
 * This is the primary bean for managing the example tracking api
 *
 * @author Jon Stevens
 */
@Service
@ManagedResource
public class FallbackBean implements Fallback {

    /** */
    private static final Logger log = LoggerFactory.getLogger(FallbackBean.class);

    /** */
    @Inject
    private FallbackJobDao jobDao;

    /** The SpringBean. */
    @Inject
    private ApplicationContext appCtx;

    /** */
    private int secondsBetweenRuns = 5 * 1000;

    /** */
    private Timer fallbackTaskTimer = new Timer("FallbackTask", false);
    private FallbackTask statusTask = null;

    /** */
    @PostConstruct
    public void start() {
        if (log.isDebugEnabled()) {
            log.debug("Called FallbackBean start(): ");
        }

        // gets a new bean each time
        this.statusTask = this.appCtx.getBean(FallbackTask.class);
        this.fallbackTaskTimer.scheduleAtFixedRate(statusTask, new Date(), this.secondsBetweenRuns);
    }

    /** */
    @PreDestroy
    public void stop() {
        if (log.isDebugEnabled()) {
            log.debug("Called FallbackBean stop(): ");
        }
        this.statusTask.cancel();
    }

    /** */
    @ManagedOperation
    public void setSecondsBetweenRuns(int secondsBetweenRuns) {
        this.secondsBetweenRuns = secondsBetweenRuns * 1000;
        stop();
        start();
    }

    /*
     * (non-Javadoc)
     * @see com.github.fallback.biz.i.Fallback#startJob(java.lang.Long, org.joda.time.DateTime, org.joda.time.DateTime)
     */
    @Override
    @Transactional
    public Long startJob(Long clientId, DateTime reportFrom, DateTime reportTo) {

        FallbackJob job = jobDao.create(clientId, reportFrom, reportTo);

        startRemoteJob(job);

        return job.getId();
    }

    /*
     * (non-Javadoc)
     * @see com.github.fallback.biz.i.Fallback#checkJobStatus(java.lang.Long)
     */
    @Override
    public FallbackJobStatus checkJobStatus(Long jobId) {
        FallbackJob job = this.jobDao.find(jobId);
        if (job == null) {
            return FallbackJobStatus.NOT_FOUND;
        }
        return job.getStatus();
    }

    /*
     * (non-Javadoc)
     * @see com.github.fallback.biz.i.Fallback#getJobDetails(java.lang.Long)
     */
    @Override
    public String getJobDetails(Long jobId) {
        FallbackJob job = this.jobDao.find(jobId);
        if (job == null) {
            return FallbackJobStatus.NOT_FOUND.toString();
        }
        return job.toString();
    }

    /*
     * (non-Javadoc)
     * @see com.github.fallback.biz.i.Fallback#checkRemoteJobStatus()
     */
    @Override
    @Transactional
    public void checkRemoteJobStatus() {
        List<FallbackJob> jobs = this.jobDao.getStartedJobs();
        if (log.isDebugEnabled()) {
            log.debug("Found " + jobs.size() + " started jobs.");
        }
        for (FallbackJob job : jobs) {
            log.debug(job.toString());
        }
    }

    /*
     * (non-Javadoc)
     * @see com.github.fallback.biz.i.Fallback#getJobsByStatus(com.github.fallback.biz.i.FallbackJobStatus)
     */
    @Override
    public List<FallbackJob> getJobsByStatus(FallbackJobStatus status) {
        return this.jobDao.findSome("status", status);
    }

    /**
     * Fires off event to some remote service to start a query based on the from/to dates.
     * This should be wrapped in a transaction already.
     */
    private void startRemoteJob(FallbackJob job) {
        try {
            // do the work to start the job

            job.setStatus(FallbackJobStatus.STARTED);

        } catch (Exception e) {
            log.error("Job Failed", e);
            job.setStatus(FallbackJobStatus.FAILED);
            job.setMessage(e.getMessage());
        }
    }
}
