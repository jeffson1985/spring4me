package org.sarons.spring4me.jdbc.pagination;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Gavin
 * @since 0.5.0
 * @create 2012-7-30 - 下午9:55:09
 * @param <T>
 */
public class Page<T> {

	public static final Long TOTAL_COUNT_UNKNOW = -1L;
	
	protected Integer pageNo = 1;
	protected Integer pageSize = -1;
	protected Boolean autoCount = Boolean.TRUE;
	protected List<String> orderList = new ArrayList<String>();
	protected List<String> ascOrderList = new ArrayList<String>();
	protected List<String> descOrderList = new ArrayList<String>();

	protected List<T> result = new ArrayList<T>();
	protected Long totalCount = -1L;

	public Page() {
	}

	public Page(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getPageNo() {
		return pageNo;
	}

	public Page<T> setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
		if (pageNo < 1) {
			this.pageNo = 1;
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
		return ((pageNo - 1) * pageSize) + 1;
	}

	public Boolean isAutoCount() {
		return autoCount;
	}

	public Page<T> setAutoCount(Boolean autoCount) {
		this.autoCount = autoCount;
		return this;
	}

	public List<T> getResult() {
		return result;
	}

	public Page<T> setResult(List<T> results) {
		this.result = results;
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

		Long count = totalCount / pageSize;
		if (totalCount % pageSize > 0) {
			count++;
		}
		return count;
	}

	public Boolean hasNext() {
		return (pageNo + 1 <= getTotalPages());
	}

	public Integer getNextPage() {
		if (hasNext()) {
			return pageNo + 1;
		} else {
			return pageNo;
		}
	}

	public Boolean hasPre() {
		return (pageNo - 1 >= 1);
	}

	public Integer getPrePage() {
		if (hasPre()) {
			return pageNo - 1;
		} else {
			return pageNo;
		}
	}
	
	//
	public List<String> getOrderList() {
		return orderList;
	}
	
	public List<String> getAscOrderList() {
		return ascOrderList;
	}
	
	public List<String> getDescOrderList() {
		return descOrderList;
	}
	
	public Page<T> asc(String column) {
		orderList.add(column + " ASC");
		ascOrderList.add(column);
		return this;
	}
	
	public Page<T> desc(String column) {
		orderList.add(column + " DESC");
		descOrderList.add(column);
		return this;
	}
	
}