package cn.he.bookstore.category.service;

import java.util.ArrayList;
import java.util.List;

import cn.he.bookstore.book.dao.BookDao;
import cn.he.bookstore.book.domain.Book;
import cn.he.bookstore.category.dao.CategoryDao;
import cn.he.bookstore.category.domain.Category;

public class CategoryService {

	private CategoryDao categoryDao = new CategoryDao();
	private BookDao bookDao = new BookDao();

	public List<Category> findAll() {
		return categoryDao.findAll();
	}

	public void addCategory(Category category) {
		categoryDao.add(category);
	}

	public void remove(String cid) throws CategoryException {
		
		List<Book> books = bookDao.findByCid(cid, true);
		List<Boolean> flag = new ArrayList<Boolean>();
		for (Book b : books) {
			flag.add(b.isDel());
		}
		
		int count = books.size();
		if (count != 0) {
			if (flag.contains(false)) {
				throw new CategoryException("该目录还有图书，不能删除！");
			} else {
				for (Book b : books) {
					Category c = new Category();
					c.setCid(null);
					
					b.setCategory(c);
					bookDao.modify(b);
				}
			}
		}
		
		categoryDao.remove(cid);
	}

	public Category preModify(String cid) {
		return categoryDao.findByCid(cid);
	}

	public void modify(Category category) throws CategoryException {
		Category _category = categoryDao.findByCname(category.getCname());
		if (null != _category) {
			throw new CategoryException("分类已存在，请换另一分类！");
		}
		categoryDao.modify(category);
	}
}
