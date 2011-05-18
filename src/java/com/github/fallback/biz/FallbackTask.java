package com.github.fallback.biz;

import java.util.TimerTask;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.github.fallback.biz.i.Fallback;

/**
 * This is the Task which runs to check what the status of all the jobs
 * are in hbase. It basically just calls back to a method in the Fallback bean
 * to keep the logic nicely organized.
 *
 * Needs to be in the prototype scope to support being restarted.
 *
 * @author jon
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class FallbackTask extends TimerTask {

    /** */
    private static final Logger log = LoggerFactory.getLogger(FallbackTask.class);

    /** */
    @Inject
    private Fallback fallback;

    /** */
    @Override
    public void run() {
        try {
            if (fallback != null) {
                fallback.checkRemoteJobStatus();
            }
        } catch (Exception e) {
            log.error("Status Task Failure:", e);
        }
    }
}
