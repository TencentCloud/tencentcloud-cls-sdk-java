package com.tencentcloud.cls.producer.response;

import com.google.common.collect.Iterables;
import com.tencentcloud.cls.producer.common.Attempt;

import java.util.List;
import java.util.Map;

/**
 * The response of the PutData API from sls server
 * @author farmerx
 *
 */
public class PutLogsResponse extends Response {
	private static final long serialVersionUID = -4660644764028977169L;

	/**
	 * Construct the response with http headers
	 * @param headers http headers
	 */
	public PutLogsResponse(Map<String, List<String>> headers) {
		super(headers);
	}

}
