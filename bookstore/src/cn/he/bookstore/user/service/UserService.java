package cn.he.bookstore.user.service;

import cn.he.bookstore.user.dao.UserDao;
import cn.he.bookstore.user.domain.User;

/**
 * User业务逻辑层
 * @author He
 *
 */
public class UserService {

	private UserDao userDao = new UserDao();
	
	//注册
	public void regist(User user) throws UserException {
		User _user = userDao.findByUsername(user.getUsername());
		if (null != _user) {
			throw new UserException("用户名已被注册！");
		}
		_user = userDao.findByEmail(user.getEmail());
		if (null != _user) {
			throw new UserException("邮箱已被注册！");
		}
		userDao.add(user);
	}
	
	//激活
	public void active(String code) throws UserException {
		User _user = userDao.findByCode(code);
		if (null == _user) {
			throw new UserException("激活码无效！");
		}
		if (_user.isState()) {
			throw new UserException("你已经激活了，无需重复激活");
		}
		
		userDao.updateState(_user.getUid(), true);
	}

	public User login(User form) throws UserException {
		User _user = userDao.findByUsername(form.getUsername());
		if (null == _user) {
			throw new UserException("用户不存在！");
		}
		if (!form.getPassword().equals(_user.getPassword())) {
			throw new UserException("密码错误！");
		}
		if (!_user.isState()) {
			throw new UserException("账号尚未激活！");
		}
		return _user;
	}
}
