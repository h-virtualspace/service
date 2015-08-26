package com.here.virtualspace.file.service.impl;

import java.io.File;
import java.io.InputStream;
import java.util.Map;

import com.here.framework.core.file.fdfs.FastDfsClient;
import com.here.framework.log.HLogger;
import com.here.virtualspace.file.service.FileService;
/**
 * fastdfs  实现
 * @author koujp
 *
 */
public class DfsFileServiceImpl implements FileService {
	private FastDfsClient dfsClient;
	public DfsFileServiceImpl(){
	}
	@Override
	public String upload(File file, Map<String, String> metaMap)
			throws Exception {
		String absPath=file.getAbsolutePath();
		int startIndex=absPath.lastIndexOf(".");
		String extName=null;
		if(startIndex>0){
			extName=absPath.substring(startIndex);
		}
		String[] pairs=dfsClient.upload(file, extName, metaMap);
		if(null==pairs || pairs.length<2){
			return null;
		}
		return pairs[1];
	}

	@Override
	public String upload(File file, Map<String, String> metaMap,
			String extName) throws Exception {
		String[] pairs=dfsClient.upload(file, extName, metaMap);
		if(null==pairs || pairs.length<2){
			return null;
		}
		return pairs[1];
	}

	@Override
	public String upload(InputStream fileIn, long fileLength,
			String extName, Map<String, String> metaMap) throws Exception {
		String[] pairs=dfsClient.upload(fileIn, fileLength, extName, metaMap);
		try{
			fileIn.close();
		}catch(Exception e){
			HLogger.getInstance(this.getClass()).error(e);
		}
		if(null==pairs || pairs.length<2){
			return null;
		}
		return pairs[1];
	}
	@Override
	public boolean delete(String fileName) throws Exception {
		return dfsClient.delete(fileName)==0;
	}
	public FastDfsClient getDfsClient() {
		return dfsClient;
	}
	public void setDfsClient(FastDfsClient dfsClient) {
		this.dfsClient = dfsClient;
	}
	@Override
	public String getURL(String fileName) {
		String address=dfsClient.getClientConfig().getHttpAddress();
		if(!address.endsWith("/")){
			address+="/";
		}
		return address+fileName;
	}

}
