package cn.he.bookstore.book.web.servlet.admin;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.ImageIcon;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import cn.he.bookstore.book.domain.Book;
import cn.he.bookstore.book.service.BookService;
import cn.he.bookstore.category.domain.Category;
import cn.he.bookstore.category.service.CategoryService;
import cn.he.utils.CommonUtils;

public class AdminUploadBookServlet extends HttpServlet {

	private BookService bookService = new BookService();
	private CategoryService categoryService = new CategoryService();
	
	/**
	 * 把表单数据封装到book对象中，
	 * 最终把book封装到数据库
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");		
		
		//设置图片上传时缓存路径以及缓存大小
		File cacheFile = new File(this.getServletContext().getRealPath("/book_img/"));
		DiskFileItemFactory diskFileItemFactory = new DiskFileItemFactory(10*1024, cacheFile);
		ServletFileUpload servletFileUpload = new ServletFileUpload(diskFileItemFactory);
		
		//设置上传图片最大值为15kb
		servletFileUpload.setFileSizeMax(15*1024);
		
		try {
			List<FileItem> fileItemList = servletFileUpload.parseRequest(request);
			Map<String, String> map = new HashMap<String, String>();
			for (FileItem fileItem : fileItemList) {
				/**
				 * 把普通表单项的内容封装到map，名称、单价、作者，
				 * 手动处理bid、image、category、del
				 */
				if (fileItem.isFormField()) {
					map.put(fileItem.getFieldName(), fileItem.getString("utf-8"));
				}
			}
			Book book = CommonUtils.getBeanFromMap(map, Book.class);
			book.setBid(CommonUtils.getUUID());
			book.setDel(false);
			Category category = CommonUtils.getBeanFromMap(map, Category.class);
			book.setCategory(category);
			
			/////////////////////////////////////////////////////////////////////
			//处理image(文件表单项)
			FileItem fileItem = fileItemList.get(1);
			String realPath = this.getServletContext().getRealPath("/book_img/");
			
			//防止某些浏览器上传时使用绝对路径的问题
			String fileName = fileItem.getName();
			int lastIndex = fileName.lastIndexOf("\\");
			if (-1 != lastIndex) {
				fileName = fileName.substring(lastIndex+1);
			}
			
			//校验上传图片的拓展名
			if (!fileName.toLowerCase().endsWith(".jpg")) {
				request.setAttribute("msg", "只能上传JPG图片！");
				request.setAttribute("categoryList", categoryService.findAll());
				request.getRequestDispatcher("/adminjsps/admin/book/add.jsp").forward(request, response);
				return;
			}
			
			//使用哈希打散文件目录（使用二级打散）
			String hexString = Integer.toHexString(fileName.hashCode());
			String middlePath = hexString.charAt(0)+"/"+hexString.charAt(1);
			File dirName = new File(realPath, middlePath);
			dirName.mkdirs();
			
			//防止文件重名
			fileName = CommonUtils.getUUID()+"_"+fileName;
			
			//保存上传的图书图片
			File file = new File(dirName, fileName);
			
			fileItem.write(file);

			//校验图片长宽
			Image image = new ImageIcon(file.getAbsolutePath()).getImage();
			if (image.getHeight(null) > 180 || image.getWidth(null) > 150) {
				deleteFile(file);
				request.setAttribute("msg", "图片大小超过150*180");
				request.setAttribute("categoryList", categoryService.findAll());
				request.getRequestDispatcher("/adminjsps/admin/book/add.jsp").forward(request, response);
				return;
			}
			
			book.setImage("book_img/" + middlePath+ "/"+ fileName);
			/////////////////////////////////////////////////////////////////////
			
			bookService.add(book);
			
			request.getRequestDispatcher("/admin/AdminBookServlet?method=findAll").forward(request, response);
			
		} catch (Exception e) {
			if (e instanceof FileUploadBase.FileSizeLimitExceededException) {
				request.setAttribute("msg", "上传图片大于15kb！");
				request.setAttribute("categoryList", categoryService.findAll());
				request.getRequestDispatcher("/adminjsps/admin/book/add.jsp").forward(request, response);
			} else {
				e.printStackTrace();
			}
		}
	}

	//删除上传超过150*180的图片以及其哈希目录
	private void deleteFile(File file) {
		
		File parent = file.getParentFile();
		file.delete();
		File[] fileArr = parent.listFiles();
		while (null == fileArr || 0 == fileArr.length) {
			parent.delete();
			parent = parent.getParentFile();
			fileArr = parent.listFiles();
		}
	}
}
