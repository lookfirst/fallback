package com.github.fallback.biz.i;

/**
 * Describes the various status's for the fallback jobs.
 *
 * @author jon
 */
public enum FallbackJobStatus {

    CREATED,
    STARTED,
    FAILED,
    COMPLETE,
    SUCCESSFUL,
    NOT_FOUND
}
