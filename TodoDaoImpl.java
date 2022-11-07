package net.javaguides.todoapp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jdk.nashorn.internal.parser.TokenStream;
import net.javaguides.todoapp.model.Todo;
import net.javaguides.todoapp.utils.HibernateUtil;
import net.javaguides.todoapp.utils.JDBCUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.Id;
import javax.persistence.criteria.*;

/**
 * This DAO class provides CRUD database operations for the table todos in the
 * database.
 * 
 * @author Ramesh Fadatare
 *
 */

public class TodoDaoImpl implements TodoDao {

	private static final String INSERT_TODOS_SQL = "INSERT INTO todos"
			+ "  (title, username, description, target_date,  is_done) VALUES " + " (?, ?, ?, ?, ?);";

	private static final String SELECT_TODO_BY_ID = "select id,title,username,description,target_date,is_done from todos where id =?";
	private static final String SELECT_ALL_TODOS = "select * from todos";
	private static final String DELETE_TODO_BY_ID = "delete from todos where id = ?;";
	private static final String UPDATE_TODO = "update todos set title = ?, username= ?, description =?, target_date =?, is_done = ? where id = ?;";

	public TodoDaoImpl() {
	}

	@Override
	public void insertTodo(Todo todo) throws SQLException {

	}

	@Override
	public Todo selectTodo(long todoId) {
		Todo todo = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Todo> criteriaQuery = builder.createQuery(Todo.class);
			Root<Todo> root = criteriaQuery.from(Todo.class);

			criteriaQuery.select(root).where(builder.and(
					builder.equal(root.get("id"), todoId)));
			todo = session.createQuery(criteriaQuery).getSingleResult();
		}
		return todo;


	}


	@Override
	public List<Todo> selectAllTodos() {
		CriteriaBuilder builder;
		List<Todo> todos = new ArrayList<>();
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			builder = session.getCriteriaBuilder();
			CriteriaQuery<Todo> criteriaQuery = builder.createQuery(Todo.class);
			Root<Todo> root = criteriaQuery.from(Todo.class);
			criteriaQuery.select(root);
			todos = session.createQuery(criteriaQuery).getResultList();
		}

		return todos;
	}



	@Override
	public boolean deleteTodo(int id) throws SQLException {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			Transaction transaction = session.beginTransaction();
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaDelete<Todo> criteriaDelete = builder.createCriteriaDelete(Todo.class);
			Root<Todo> rootDelete = criteriaDelete.from(Todo.class);
			criteriaDelete.where(builder.equal(rootDelete.get("id"), id));
			session.createQuery(criteriaDelete).executeUpdate();
			transaction.commit();
		}
		return true;
	}

	@Override
	public int updateTodo(Todo todo) throws SQLException {
		int result = 0;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaUpdate<Todo> criteriaUpdate = builder.createCriteriaUpdate(Todo.class);
			Root<Todo> rootUpdate = criteriaUpdate.from(Todo.class);
			criteriaUpdate.set("Title", "Username, Description, Status").where(builder.equal(rootUpdate.get("Id"), todo));
			Transaction transaction = session.beginTransaction();
			result = session.createQuery(criteriaUpdate).executeUpdate();
			transaction.commit();
		}

		return result;
	}
}

