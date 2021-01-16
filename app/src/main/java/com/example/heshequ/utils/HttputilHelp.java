package com.example.heshequ.utils;

import com.lidroid.xutils.HttpUtils;

public class HttputilHelp {
	 private static HttpUtils httpUtils;
      private HttputilHelp() {
      }
      public static HttpUtils getHttpUtils () {
    	  if (httpUtils == null) {
    		  httpUtils = new HttpUtils();
    	  }
    	  return httpUtils;
      }
}
