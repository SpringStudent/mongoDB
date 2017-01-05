package com.tgb.upload.servlet;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;

import com.tgb.upload.service.MongoDBService;

@SuppressWarnings("serial")
public class downLoadZipServlet extends HttpServlet {

	MongoDBService mongoDBService = new MongoDBService();
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}

	
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
			// 生成的ZIP文件名为.zip
			String tmpFileName = "user1.zip";
			// zip文件保存位置
			String FilePath = "D:/batchDownloadFile/";
			
			//创建保存zip的文件夹
			File file1 = new File(FilePath);
			if (!file1.exists()) {
				file1.mkdir();
			}
			
			//下载的文件的临时保存位置
			String mogodbFilePath = "D:/mogodbFile/";
			File file2 = new File(mogodbFilePath);
			if (!file2.exists()) {
				file2.mkdir();
			}
			
			byte[] buffer = new byte[1024];
			String strZipPath = FilePath + tmpFileName;
			ZipOutputStream out = null;
			FileInputStream in = null;
		try{
			mongoDBService.directToPath(mogodbFilePath);
			
			out = new ZipOutputStream(new FileOutputStream(strZipPath));
			File dirFile = new File(mogodbFilePath);
			
			//获取该文件夹下的所有文件
			File[] file = dirFile.listFiles();
			for (int i = 0; i < file.length; i++) {
				in= new FileInputStream(file[i]);
				String fileName = file[i].getName();
				// 设置压缩文件内的字符编码，不然会变成乱码
				out.setEncoding("GBK");
				out.putNextEntry(new ZipEntry(fileName));

				int len;
				// 读入需要下载的文件的内容，打包到zip文件
				while ((len = in.read(buffer)) > 0) {
					out.write(buffer, 0, len);
				}
				in.close();
			}
				
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if (out != null) {
				out.flush();
				out.closeEntry();
				out.close();
			}
			if(in!=null){
				in.close();
			}
		}
		
		
		BufferedInputStream buff = null;
		OutputStream myout = null;
		FileInputStream fis = null;

		try {
			response.setContentType("text/html; charset=GBK");
			// 创建file对象
			File file = new File(FilePath + tmpFileName);

			// 设置response的编码方式
			response.setContentType("application/octet-stream");

			// 写明要下载的文件的大小
			response.setContentLength((int) file.length());

			// 设置附加文件名
			String filename =tmpFileName;
			byte[] bt;
			bt = filename.getBytes("UTF-8");
			filename = new String(bt, "8859_1");
			// 解决中文乱码
			response.setHeader("Content-Disposition", "attachment;filename="
					+ filename);

			// 读出文件到i/o流
			fis = new FileInputStream(file);
			buff = new BufferedInputStream(fis);

			byte[] b = new byte[1024];// 相当于我们的缓存

			long k = 0;// 该值用于计算当前实际下载了多少字节

			// 从response对象中得到输出流,准备下载
			myout = response.getOutputStream();

			// 开始循环下载
			while (k < file.length()) {

				int j = buff.read(b, 0, 1024);
				k += j;

				// 将b中的数据写到客户端的内存
				myout.write(b, 0, j);

			}

			// 将写入到客户端的内存的数据,刷新到磁盘
			myout.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (buff != null) {
					buff.close();
				}
				if (myout != null) {
					myout.close();
				}
				if (fis != null) {
					fis.close();
				}
				// 删除mogoDB文件临时保存位置所在文件夹
				//mongoDBService.deleteDirectory(mogodbFilePath);
				// 删除zip文件
				//mongoDBService.deleteAllFile(FilePath);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
		
	}
	
	
	
	




