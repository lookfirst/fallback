package com.github.fallback.web;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.NumberUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.github.fallback.biz.i.Fallback;
import com.github.fallback.biz.i.FallbackJobStatus;
import com.github.fallback.entity.FallbackJob;

/**
 * There are primarily three urls:
 *
 * /start?clientId=234&reportFrom=&reportTo=
 * /check/#
 * /details/1,2,3,4
 * /list/STATUS
 *
 * The expected date format is: 2010-01-01 11:11:11
 * STATUS is a FallbackJobStatus
 * An unknown job id will return NOT_FOUND
 *
 * @author jon
 */
@Controller
public class FallbackController {

    /** */
    @SuppressWarnings("unused")
    private static final Logger log = LoggerFactory.getLogger(FallbackController.class);

    /** */
    private static final String FMT = "yyyy-MM-dd HH:mm:ss";

    /** */
    @Inject
    private Fallback fallback;

    /** */
    @RequestMapping(value = { "/start" })
    public ResponseEntity<String> startJob(@RequestParam Long clientId,
                                            @RequestParam @DateTimeFormat(pattern = FMT) Date reportFrom,
                                            @RequestParam @DateTimeFormat(pattern = FMT) Date reportTo) throws IOException {

        Long result = fallback.startJob(clientId, convDate(reportFrom), convDate(reportTo));

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Content-Type", "text/plain");

        if (result != null) {
            return new ResponseEntity<String>(result.toString(), responseHeaders, HttpStatus.OK);
        } else {
            return new ResponseEntity<String>(FallbackJobStatus.NOT_FOUND.toString(), responseHeaders, HttpStatus.NOT_FOUND);
        }
    }

    /** */
    @RequestMapping(value = { "/check/{jobId}" })
    public ResponseEntity<String> checkJob(@PathVariable Long jobId) throws IOException {
        FallbackJobStatus status = fallback.checkJobStatus(jobId);

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Content-Type", "text/plain");

        return new ResponseEntity<String>(status.toString(), responseHeaders, HttpStatus.OK);
    }

    /** */
    @RequestMapping(value = { "/details/{jobId}" })
    public ResponseEntity<String> jobDetails(@PathVariable String jobId) throws IOException {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Content-Type", "text/plain");
        StringBuilder response = new StringBuilder();

        if (jobId != null) {
            String[] jobIds = jobId.split(",");
            for (String id : jobIds) {
                try {
                    Long num = NumberUtils.parseNumber(id, Long.class);
                    String details = fallback.getJobDetails(num);
                    response.append(details + "\n");
                } catch (Exception ex) {
                    // ignored.
                }
            }
        } else {
            response.append(FallbackJobStatus.NOT_FOUND.toString());
        }
        return new ResponseEntity<String>(response.toString(), responseHeaders, HttpStatus.OK);
    }

    /** */
    @RequestMapping(value = { "/list/{status}" })
    public ResponseEntity<String> listStatus(@PathVariable String status) throws IOException {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Content-Type", "text/plain");
        StringBuilder response = new StringBuilder();
        boolean foundData = false;

        if (status != null) {
            List<FallbackJob> jobs = fallback.getJobsByStatus(FallbackJobStatus.valueOf(status.toUpperCase()));
            if (jobs.size() > 0) {
                for (FallbackJob job : jobs) {
                    try {
                        response.append(job.toString() + "\n");
                    } catch (Exception ex) {
                        // ignored.
                    }
                    foundData = true;
                }
            }
        }

        if (!foundData) {
            response.append(FallbackJobStatus.NOT_FOUND.toString());
        }
        return new ResponseEntity<String>(response.toString(), responseHeaders, HttpStatus.OK);
    }

    /** */
    private DateTime convDate(Date param) {
        DateTime dt = null;
        if (param != null) {
            dt = new DateTime(param.getTime());
        }
        return dt;
    }
}
