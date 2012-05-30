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

package org.osforce.spring4me.web.navigation.config;

public class EventConfig {
	
	private String on;
	private String to;

	public EventConfig(String on, String to) {
		this.on = on;
		this.to = to;
	}
	
	public String getOn() {
		return on;
	}

	public String getTo() {
		return to;
	}
	
	public void setOn(String on) {
		this.on = on;
	}
	
	public void setTo(String to) {
		this.to = to;
	}
	
	/**
	 * {event:view-profile | to:viewProfile}
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		sb.append("event:").append(on);
		sb.append(" | ");
		sb.append("to:").append(to);
		sb.append("}");
		return sb.toString();
	}

}
