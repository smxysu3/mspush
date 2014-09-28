/**
 * 
 */
package com.moshi.receptionist.common.router;

import com.moshi.receptionist.common.exception.TagNotFoundException;
import com.moshi.receptionist.common.message.MessageEvent;


public interface RoutingTable {
	
	
	void routeMessage(MessageEvent event) throws TagNotFoundException;

}
