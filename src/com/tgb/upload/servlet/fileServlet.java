package com.tgb.upload.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSFile;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;
import com.oreilly.servlet.multipart.FilePart;
import com.oreilly.servlet.multipart.FileRenamePolicy;
import com.oreilly.servlet.multipart.MultipartParser;
import com.oreilly.servlet.multipart.Part;

@SuppressWarnings("serial")
public class fileServlet extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String method=request.getParameter("method");
		if(method.equals("upload")){
			upload(request,response);
		}else if(method.equals("delete")){
			delete(request,response);
		}else if(method.equals("showList")){
			showList(request,response);
		}else if(method.equals("downloadFile")){
			downloadFile(request,response);
		}
	
	}
	
	
	/**
	 * 文件上传
	 * @param request
	 * @param response
	 */
	private void upload(HttpServletRequest request ,HttpServletResponse response){
		try{
			Mongo mongo = new Mongo();
			DB db = mongo.getDB("test");
			GridFS gridFS= new GridFS(db,"fs");
			GridFSFile file = null;
			FileRenamePolicy rfrp = new DefaultFileRenamePolicy();
			MultipartParser mp = new MultipartParser(request, 1024 * 1024 * 1024,
					true, true, "GB18030");//“GB18030”必须和jsp编码格式相同，不然会产生中文乱码
			FilePart filePart = null;
			Part part = null;
			int pot =0;
			while ((part = mp.readNextPart()) != null) {
				if (part.isFile()) {// it's a file part
					filePart = (FilePart) part;
					filePart.setRenamePolicy(rfrp);
					String fileName = filePart.getFileName();
					InputStream in = filePart.getInputStream();
					pot = fileName.lastIndexOf(".");
					file = gridFS.createFile(in);// 创建gridfs文件
					file.put("filename", fileName);
					file.put("userId", 1);
					file.put("uploadDate", new Date());
					file.put("contentType", fileName.substring(pot));
					file.save();
				}
			}
			request.setAttribute("uploadResult", "上传成功!");
			request.getRequestDispatcher("/uploadResult.jsp").forward(request, response);
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}

	/**
	 * 文件删除
	 * @param request
	 * @param response
	 */
	private void delete(HttpServletRequest request ,HttpServletResponse response){
		try{
			String fileId = (String)request.getParameter("id");
			Mongo mongo = new Mongo();
			DB db = mongo.getDB("test");
			GridFS gridFS= new GridFS(db,"fs");
			
			ObjectId objId = new ObjectId(fileId);
			gridFS.remove(objId);
			showList(request,response);
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}

	/**
	 * 查看文件列表
	 * @param request
	 * @param response
	 */
	private void showList(HttpServletRequest request ,HttpServletResponse response){
		try{
			Mongo mongo = new Mongo();
			DB db = mongo.getDB("test");
			GridFS gridFS= new GridFS(db,"fs");
			
			
			DBObject query=new BasicDBObject("userId", 1);
			List<GridFSDBFile> gridFSDBFileList = gridFS.find(query);
				
			request.setAttribute("gridFSDBFileList", gridFSDBFileList);
			
			request.getRequestDispatcher("/fileList.jsp").forward(request, response);
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}

	/**
	 * 查看单个文件、下载文件
	 * @param request
	 * @param response
	 */
	private void downloadFile(HttpServletRequest request ,HttpServletResponse response){
		try{
			String fileId = request.getParameter("id");
			Mongo mongo = new Mongo();
			DB db = mongo.getDB("test");
			GridFS gridFS= new GridFS(db,"fs");
			ObjectId objId = new ObjectId(fileId);
			GridFSDBFile gridFSDBFile =(GridFSDBFile)gridFS.findOne(objId);
			
			if (gridFSDBFile != null) {

				OutputStream sos = response.getOutputStream();

				response.setContentType("application/octet-stream");
				// 获取原文件名
				String name = (String) gridFSDBFile.get("filename");
				String fileName = new String(name.getBytes("GBK"), "ISO8859-1");

				// 设置下载文件名
				response.addHeader("Content-Disposition", "attachment; filename=\""
						+ fileName + "\"");

				// 向客户端输出文件
				gridFSDBFile.writeTo(sos);
				sos.flush();
				sos.close();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}


}
