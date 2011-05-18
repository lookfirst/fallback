package com.github.fallback.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.Table;

import com.github.fallback.biz.i.FallbackJobStatus;

/**
 * Describes a Job
 *
 * @author jon
 */
@Entity(name = "job")
@Table(appliesTo = "job", indexes = { @Index(name = "status", columnNames = { "status" }) })
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class FallbackJob {

    /** */
    @Id
    @GeneratedValue
    private Long id;

    /** */
    @Column(nullable = false)
    private Long clientId;

    /** */
    @Column(nullable = false)
    private Date createdDate;

    /** */
    @Column(nullable = false)
    private Date reportFrom;

    /** */
    @Column(nullable = false)
    private Date reportTo;

    /** */
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private FallbackJobStatus status;

    /** */
    @Column(nullable = true, length = 2048)
    private String message;

    /** */
    public FallbackJob() {
    }

    /**
     * Main constructor that should be used to create a new job.
     */
    public FallbackJob(Long clientId, Date reportFrom, Date reportTo) {
        this.clientId = clientId;
        this.reportFrom = reportFrom;
        this.reportTo = reportTo;
        this.status = FallbackJobStatus.CREATED;
        this.createdDate = new Date();
    }

    /** */
    public Long getId() {
        return id;
    }

    /** */
    public Long getClientId() {
        return clientId;
    }

    /** */
    public Date getReportFrom() {
        return reportFrom;
    }

    /** */
    public Date getReportTo() {
        return reportTo;
    }

    /** */
    public FallbackJobStatus getStatus() {
        return status;
    }

    /** */
    public void setStatus(FallbackJobStatus status) {
        this.status = status;
    }

    /** */
    public Date getCreatedDate() {
        return createdDate;
    }

    /**
     * A place to detail why an error occurred.
     */
    public String getMessage() {
        return message;
    }

    /**
     * A place to detail why an error occurred.
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /** */
    @Override
    public String toString() {
        return "FallbackJob [id=" + id + ", clientId=" + clientId + ", createdDate=" + createdDate + ", reportFrom=" + reportFrom + ", reportTo=" + reportTo + ", status=" + status + ", message=" + message + "]";
    }
}
