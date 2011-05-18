package com.github.fallback.biz.i;

import java.util.List;

import org.joda.time.DateTime;

import com.github.fallback.entity.FallbackJob;



/**
 * The main interface for the Fallback api.
 *
 * @author Jon Stevens
 */
public interface Fallback {

    /**
     * Starts the job and returns a transaction id.
     *  "clientId":"234233",
     *  "reportFrom":"2011-03-01 08:00:00",
     *  "reportTo":"2011-03-02 08:00:00",
     *
     * @return a transaction id or TODO some exception for error.
     */
    public Long startJob(Long clientId, DateTime reportFrom, DateTime reportTo);

    /**
     * Checks on the status of a job.
     * @return the job status enum
     */
    public FallbackJobStatus checkJobStatus(Long jobId);

    /**
     * This will get a list of all jobs that have a status of STARTED and then
     * query hbase
     */
    public void checkRemoteJobStatus();

    /**
     * @return FallbackJob.toString()
     */
    public String getJobDetails(Long jobId);

    /**
     * Get a list of jobs by status.
     */
    public List<FallbackJob> getJobsByStatus(FallbackJobStatus status);
}
