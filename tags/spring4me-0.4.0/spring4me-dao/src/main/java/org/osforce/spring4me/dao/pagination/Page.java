/*
 * Copyright 2011-2012 the original author or authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.osforce.spring4me.dao.pagination;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author <a href="mailto:haozhonghu@hotmail.com">gavin</a>
 * @since 0.1.0
 * @create May 16, 2011 - 3:51:31 PM
 */
public class Page<T> {

	public static final Long TOTAL_COUNT_UNKNOW = -1L;
	
	protected Integer pageNo = 1;
	protected Integer pageSize = -1;
	protected Boolean autoCount = Boolean.TRUE;
	protected List<String> orderList = new ArrayList<String>();
	protected List<String> ascOrderList = new ArrayList<String>();
	protected List<String> descOrderList = new ArrayList<String>();

	protected List<T> results = new ArrayList<T>();
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

	public Boolean getAutoCount() {
		return autoCount;
	}

	public Page<T> setAutoCount(Boolean autoCount) {
		this.autoCount = autoCount;
		return this;
	}

	public List<T> getResults() {
		return results;
	}

	public Page<T> setResults(List<T> results) {
		this.results = results;
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