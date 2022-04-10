package com.portal.job.model;

import com.portal.job.enums.PaginationAction;

/**
 * @author preetam
 *
 */
public class Pagination {

	private long totalResultCount;
	private long totalPageCount;
	private int pageSize;
	private int pageNumber;
	private boolean showNextButton;
	private boolean showPrevButton;
	private PaginationAction paginationAction;

	public long getTotalResultCount() {
		return totalResultCount;
	}

	public void setTotalResultCount(long totalResultCount) {
		this.totalResultCount = totalResultCount;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

	public boolean isShowNextButton() {
		return showNextButton;
	}

	public void setShowNextButton(boolean showNextButton) {
		this.showNextButton = showNextButton;
	}

	public boolean isShowPrevButton() {
		return showPrevButton;
	}

	public void setShowPrevButton(boolean showPrevButton) {
		this.showPrevButton = showPrevButton;
	}

	public PaginationAction getPaginationAction() {
		return paginationAction;
	}

	public void setPaginationAction(PaginationAction paginationAction) {
		this.paginationAction = paginationAction;
	}

	@Override
	public String toString() {
		return "Pagination [totalResultCount=" + totalResultCount
				+ ", totalPageCount=" + totalPageCount + ", pageSize="
				+ pageSize + ", pageNumber=" + pageNumber + ", showNextButton="
				+ showNextButton + ", showPrevButton=" + showPrevButton
				+ ", paginationAction=" + paginationAction + "]";
	}

	public long getTotalPageCount() {
		return totalPageCount;
	}

	public void setTotalPageCount(long totalPageCount) {
		this.totalPageCount = totalPageCount;
	}

}
