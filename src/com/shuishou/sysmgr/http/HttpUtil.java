package com.shuishou.sysmgr.http;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.SerializableEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shuishou.sysmgr.beans.Category1;
import com.shuishou.sysmgr.beans.Category2;
import com.shuishou.sysmgr.beans.Dish;
import com.shuishou.sysmgr.beans.HttpResult;
import com.shuishou.sysmgr.beans.Printer;
import com.shuishou.sysmgr.ui.MainFrame;

public class HttpUtil {

	private final static Logger logger = Logger.getLogger("HttpUtil");
	
	public static HttpClient getHttpClient(){
		HttpParams mHttpParams=new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(mHttpParams, 20*1000);
        HttpConnectionParams.setSoTimeout(mHttpParams, 20*1000);
        HttpConnectionParams.setSocketBufferSize(mHttpParams, 8*1024);
        HttpClientParams.setRedirecting(mHttpParams, true);
          
        HttpClient httpClient=new DefaultHttpClient(mHttpParams);
        return httpClient;
	}
	
    public static String getJSONObjectByGet(String uriString){
//        JSONObject resultJsonObject=null;
    	String result = null;
        if ("".equals(uriString)||uriString==null) {
            return null;
        }
        HttpClient httpClient=getHttpClient();
        StringBuilder urlStringBuilder=new StringBuilder(uriString);
        StringBuilder entityStringBuilder=new StringBuilder();
        HttpGet httpGet=new HttpGet(urlStringBuilder.toString());
        BufferedReader bufferedReader=null;
        HttpResponse httpResponse=null;
        try {
            httpResponse=httpClient.execute(httpGet); 
        } catch (Exception e) {
        	logger.error(e);
        }
        int statusCode=httpResponse.getStatusLine().getStatusCode();
        HttpEntity httpEntity=httpResponse.getEntity();
        if (httpEntity!=null) {
            try {
                bufferedReader=new BufferedReader(new InputStreamReader(httpEntity.getContent(), "UTF-8"), 8*1024);
                String line=null;
                while ((line=bufferedReader.readLine())!=null) {
                    entityStringBuilder.append(line+"\n");
                }
                if (statusCode==HttpStatus.SC_OK) {
                	return entityStringBuilder.toString();
//                	resultJsonObject=new JSONObject(entityStringBuilder.toString());
                } else {
                	logger.error("Http Error: URl : "+ uriString 
                			+ "\nhttpcode : "+ httpResponse.getStatusLine().getStatusCode()
                			+ "\nresponse message : " + entityStringBuilder.toString());
                }
                
            } catch (Exception e) {
            	logger.error(e);
            }
        }
        
        return null;
    }
    
    public static String getJSONObjectByPost(String path,Map<String, String> params) {
    	return getJSONObjectByPost(path, params, "UTF-8");
    }
    public static String getJSONObjectByPost(String path,Map<String, String> paramsHashMap, String encoding) {
    	String result = null;
//        JSONObject resultJsonObject = null;
        List<NameValuePair> nameValuePairArrayList = new ArrayList<NameValuePair>();
        if (paramsHashMap != null && !paramsHashMap.isEmpty()) {
            for (Map.Entry<String, String> entry : paramsHashMap.entrySet()) {
                nameValuePairArrayList.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
        }
          
        UrlEncodedFormEntity entity = null;
        try {
            entity = new UrlEncodedFormEntity(nameValuePairArrayList, encoding);
            HttpPost httpPost = new HttpPost(path);
            httpPost.setEntity(entity);
            HttpClient httpClient = getHttpClient();
            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            if (httpEntity != null) {
                try {
                    BufferedReader bufferedReader = new BufferedReader(  
                    new InputStreamReader(httpEntity.getContent(),"UTF-8"), 8 * 1024);
                    StringBuilder entityStringBuilder = new StringBuilder();
                    String line = null;
                    while ((line = bufferedReader.readLine()) != null) {
                        entityStringBuilder.append(line + "\n");
                    }
                    if (httpResponse.getStatusLine().getStatusCode() == 200) {
//                    	resultJsonObject = new JSONObject(entityStringBuilder.toString());
                    	return entityStringBuilder.toString();
                    } else {
                    	logger.error("Http Error: URl : "+ path + "\nparam : "+ paramsHashMap 
                    			+ "\nhttpcode : "+ httpResponse.getStatusLine().getStatusCode()
                    			+ "\nresponse message : " + entityStringBuilder.toString());
                    }
                    
                } catch (Exception e) {
                    logger.error(e);
                }
            }
            
        } catch (Exception e) {
            logger.error(e);
        }
        return null;
    } 
    
    public static String getJSONObjectByPostSerialize(String path,SerializableEntity entity, String encoding) {
    	String result = null;
          
        try {
            HttpPost httpPost = new HttpPost(path);
            httpPost.setEntity(entity);
            HttpClient httpClient = getHttpClient();
            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            if (httpEntity != null) {
                try {
                    BufferedReader bufferedReader = new BufferedReader(  
                    new InputStreamReader(httpEntity.getContent(),"UTF-8"), 8 * 1024);
                    StringBuilder entityStringBuilder = new StringBuilder();
                    String line = null;
                    while ((line = bufferedReader.readLine()) != null) {
                        entityStringBuilder.append(line + "\n");
                    }
                    if (httpResponse.getStatusLine().getStatusCode() == 200) {
//                    	resultJsonObject = new JSONObject(entityStringBuilder.toString());
                    	return entityStringBuilder.toString();
                    } else {
                    	logger.error("Http Error: URl : "+ path + "\nentity : "+ entity 
                    			+ "\nhttpcode : "+ httpResponse.getStatusLine().getStatusCode()
                    			+ "\nresponse message : " + entityStringBuilder.toString());
                    }
                    
                } catch (Exception e) {
                    logger.error(e);
                }
            }
            
        } catch (Exception e) {
            logger.error(e);
        }
        return null;
    } 
    
    public static String getJSONObjectByUploadFile(String url, HashMap<String, ContentBody> params){
    	String result = null;
    	try {
        	HttpClient httpclient = getHttpClient();
            HttpPost httppost = new HttpPost(url);

            MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
            for(String s : params.keySet()){
            	entityBuilder.addPart(s, params.get(s));
            }
            HttpEntity reqEntity = entityBuilder.build();
            httppost.setEntity(reqEntity);
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity httpEntity = response.getEntity();
            if (httpEntity != null){
            	try {
                    BufferedReader bufferedReader = new BufferedReader(  
                    new InputStreamReader(httpEntity.getContent(),"UTF-8"), 8 * 1024);
                    StringBuilder entityStringBuilder = new StringBuilder();
                    String line = null;
                    while ((line = bufferedReader.readLine()) != null) {
                        entityStringBuilder.append(line + "\n");
                    }
                    if (response.getStatusLine().getStatusCode() == 200) {
//                    	resultJsonObject = new JSONObject(entityStringBuilder.toString());
                    	return entityStringBuilder.toString();
                    } else {
                    	logger.error("Http Error: URl : "+ url + "\nparams : "+ params 
                    			+ "\nhttpcode : "+ response.getStatusLine().getStatusCode()
                    			+ "\nresponse message : " + entityStringBuilder.toString());
                    }
                    
                } catch (Exception e) {
                    logger.error(e);
                }
            }
        } catch (Exception e) {
            logger.error(e);
        }
        return null;
    }
    
    /**
	 * this class just hold Category1 objects. if need dish object, please loop into the category1 objects
	 */
	public static ArrayList<Category1> loadMenu(JFrame parent, String url){
		String response = HttpUtil.getJSONObjectByGet(url);
		if (response == null){
			logger.error("get null from server for loading menu. URL = " + url);
			JOptionPane.showMessageDialog(parent, "get null from server for loading menu. URL = " + url);
			return null;
		}
		HttpResult<ArrayList<Category1>> result = new Gson().fromJson(response, new TypeToken<HttpResult<ArrayList<Category1>>>(){}.getType());
		if (!result.success){
			logger.error("return false while loading menu. URL = " + url);
			JOptionPane.showMessageDialog(parent, "return false while loading menu. URL = " + url);
			return null;
		}
		//repoint category2 and dishes to their parent
		ArrayList<Category1> c1s = result.data;
		for(Category1 c1 : c1s){
			for(Category2 c2 : c1.getCategory2s()){
				c2.setCategory1(c1);
				for(Dish dish : c2.getDishes()){
					dish.setCategory2(c2);
				}
			}
		}
		return result.data;
	}
	
	public static ArrayList<Printer> loadPrinter(JFrame parent, String url){
		String response = HttpUtil.getJSONObjectByGet(url);
		if (response == null){
			logger.error("get null from server for loading printer. URL = " + url);
			JOptionPane.showMessageDialog(parent, "get null from server for loading printer. URL = " + url);
			return null;
		}
		HttpResult<ArrayList<Printer>> result = new Gson().fromJson(response, new TypeToken<HttpResult<ArrayList<Printer>>>(){}.getType());
		if (!result.success){
			logger.error("return false while loading printer. URL = " + url);
			JOptionPane.showMessageDialog(parent, "return false while loading printer. URL = " + url);
			return null;
		}
		return result.data;
	}
}