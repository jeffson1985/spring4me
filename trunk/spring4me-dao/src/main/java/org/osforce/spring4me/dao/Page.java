package org.osforce.spring4me.dao;

import java.util.ArrayList;
import java.util.List;

public class Page<T> {

	protected Integer pageNO = 1;
	protected Integer pageSize = -1;
	protected Boolean autoCount = Boolean.TRUE;

	protected List<T> result = new ArrayList<T>();
	protected Long totalCount = -1L;

	public Page() {
	}

	public Page(int pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getPageNO() {
		return pageNO;
	}

	public Page<T> setPageNO(Integer pageNO) {
		this.pageNO = pageNO;
		if (pageNO < 1) {
			this.pageNO = 1;
		}
		return this;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public Page<T> setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
		return this;
	}

	public Integer getFirst() {
		return ((pageNO - 1) * pageSize) + 1;
	}

	public Boolean getAutoCount() {
		return autoCount;
	}

	public Page<T> setAutoCount(Boolean autoCount) {
		this.autoCount = autoCount;
		return this;
	}

	public List<T> getResult() {
		return result;
	}

	public Page<T> setResult(List<T> result) {
		this.result = result;
		return this;
	}

	public Long getTotalCount() {
		return totalCount;
	}

	public Page<T> setTotalCount(Long totalCount) {
		this.totalCount = totalCount;
		return this;
	}

	public Long getTotalPages() {
		if (totalCount < 0) {
			return -1L;
		}

		long count = totalCount / pageSize;
		if (totalCount % pageSize > 0) {
			count++;
		}
		return count;
	}

	public Boolean getHasNext() {
		return (pageNO + 1 <= getTotalPages());
	}

	public Integer getNextPage() {
		if (getHasNext()) {
			return pageNO + 1;
		} else {
			return pageNO;
		}
	}

	public Boolean getHasPre() {
		return (pageNO - 1 >= 1);
	}

	public Integer getPrePage() {
		if (getHasPre()) {
			return pageNO - 1;
		} else {
			return pageNO;
		}
	}
}