package com.tgb.upload.service;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;

public class MongoDBService {

	
	public boolean directToPath(String mogodbFilePath ) throws IOException{
		boolean flag = false ;
		
			Mongo mongo = new Mongo();
			DB db = mongo.getDB("test");
			GridFS gridFS=new GridFS(db,"fs");
			
			List<GridFSDBFile> gridFSDBFileList = new ArrayList<GridFSDBFile>();
			DBObject query=new BasicDBObject("userId", 1);
			gridFSDBFileList = gridFS.find(query);
				
			FileOutputStream out2 = null;
			OutputStream os2 = null;
			String fileName = null;
		try{
			// 循环所有文件
			if (gridFSDBFileList != null && gridFSDBFileList.size() > 0) {
				for (GridFSDBFile nb : gridFSDBFileList) {
					// 获取原文件名
					fileName = (String) nb.get("filename");
					if (StringUtils.isNotBlank(fileName)) {
						int lin = fileName.lastIndexOf(".");
						if (lin < 0) {
							fileName += nb.getContentType().lastIndexOf(".") < 0 ? "."
									+ nb.getContentType()
									: nb.getContentType();
						}
					}
					// fileName = new String(name1.getBytes("GBK"),"ISO8859-1");
					// 将mogodb中的文件写到指定文件夹
					nb.writeTo(mogodbFilePath + fileName);
					out2 = new FileOutputStream(mogodbFilePath + fileName);
					os2 = new BufferedOutputStream(out2);
					nb.writeTo(os2);
					flag=true;
				}
			}
		}catch(Exception e){
			flag=false;
		}finally{
			os2.flush();
			os2.close();
			out2.close();
		}
		return flag;
	}

	/**
	 * 删除文件夹以及其下的文件
	 * 
	 * @param sPath
	 *            被删除目录的文件路径
	 * @return 删除成功返回true
	 * @author ls
	 */
	public boolean deleteDirectory(String sPath) {
		// 如果sPath不以文件分隔符结尾，自动添加文件分隔符
		if (!sPath.endsWith(File.separator)) {
			sPath = sPath + File.separator;
		}

		File dirFile = new File(sPath);
		// 如果dirFile对应的文件不存在，或者不是一个目录，则退出
		if (!dirFile.exists() || !dirFile.isDirectory()) {
			return false;
		}

		boolean flag = true;
		// 删除文件夹下的所有文件(包括子目录)
		File[] files = dirFile.listFiles();
		for (int i = 0; i < files.length; i++) {
			// 删除子文件
			if (files[i].isFile()) {
				flag = deleteFile(files[i].getAbsolutePath());
				if (!flag)
					break;
			} // 删除子目录
			else {
				flag = deleteDirectory(files[i].getAbsolutePath());
				if (!flag)
					break;
			}
		}
		if (!flag)
			return false;

		// 删除当前目录
		if (dirFile.delete()) {
			return true;
		} else {
			return false;
		}
	}
	
	
	/**
	 * 删除单个文件
	 * 
	 * @param sPath
	 *            被删除文件的文件名
	 * @return 单个文件删除成功返回true，否则返回false
	 * @author ls
	 */
	public boolean deleteFile(String sPath) {
		boolean flag = false;
		File file = new File(sPath);
		// 是文件且不为空则进行删除
		if (file.isFile() && file.exists()) {
			file.delete();
			flag = true;
		}
		return flag;
	}
	
	/**
	 * 删除目录下的所有文件
	 * 
	 * @param sPath
	 *            被删除目录的文件路径
	 * @return 目录删除成功返回true，否则返回false
	 * @author ls
	 */
	public boolean deleteAllFile(String sPath) {
		// 如果sPath不以文件分隔符结尾，自动添加文件分隔符
		if (!sPath.endsWith(File.separator)) {
			sPath = sPath + File.separator;
		}

		File dirFile = new File(sPath);
		// 如果dirFile对应的文件不存在，或者不是一个目录，则退出
		if (!dirFile.exists() || !dirFile.isDirectory()) {
			return false;
		}

		boolean flag = true;
		// 删除文件夹下的所有文件
		File[] files = dirFile.listFiles();
		for (int i = 0; i < files.length; i++) {
			// 删除子文件
			if (files[i].isFile()) {
				flag = deleteFile(files[i].getAbsolutePath());
				if (!flag)
					break;
			}
		}
		if (!flag)
			return false;

		return flag;
	}


}
