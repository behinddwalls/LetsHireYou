package com.portal.job.model;

public class CandidatesFilterCriteria {

    private String candidateStatus;
    private String candidateSortCriteria;
    private Pagination pagination;

    public String getCandidateStatus() {
        return candidateStatus;
    }

    public void setCandidateStatus(String candidateStatus) {
        this.candidateStatus = candidateStatus;
    }

    public String getCandidateSortCriteria() {
        return candidateSortCriteria;
    }

    public void setCandidateSortCriteria(String candidateSortCriteria) {
        this.candidateSortCriteria = candidateSortCriteria;
    }

    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }

    @Override
    public String toString() {
        return "CandidatesFilterCriteria [candidateStatus=" + candidateStatus
                + ", candidateSortCriteria=" + candidateSortCriteria + ", pagination=" + pagination
                + "]";
    }

}
