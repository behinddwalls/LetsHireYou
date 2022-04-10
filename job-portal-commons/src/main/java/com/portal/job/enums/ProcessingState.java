package com.portal.job.enums;

public enum ProcessingState {
    PROCESSED("ProcessingCompleted"), NOTPROCESSED("NotProcessed"), CANCELLED("ProcessingCancelled"), PROCESSINGFAILED(
            "ProcessingFailed"), PARTIALLYPROCESSED("PartiallyProcessed"), NORESULTS("NoResults");

    private String value;

    private ProcessingState(final String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

}
