package com.example.heshequ.utils;

import com.lidroid.xutils.HttpUtils;

public class HttpUtilHelp {
	 private static HttpUtils httpUtils;
      private HttpUtilHelp() {
      }
      public static HttpUtils getHttpUtils () {
    	  if (httpUtils == null) {
    		  httpUtils = new HttpUtils();
    	  }
    	  return httpUtils;
      }
}
