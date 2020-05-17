package com.laptrinhjavaweb.repository.impl;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;

import com.laptrinhjavaweb.annotation.Column;
import com.laptrinhjavaweb.annotation.Entity;
import com.laptrinhjavaweb.annotation.Id;
import com.laptrinhjavaweb.annotation.Table;
import com.laptrinhjavaweb.repository.JpaRepository;

public class SimpleJpaRepository<T, K> implements JpaRepository<T, K> {

	private Class<T> zclass;

	private Field[] fields;

	@SuppressWarnings("unchecked")
	public SimpleJpaRepository() {
		this.zclass = (Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass())
				.getActualTypeArguments()[0];
		fields = zclass.getDeclaredFields();
	}

	protected Connection getConnecttion() {
		Connection connection = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/estate32020module1part1", "root",
					"12345");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return connection;
	}

	public List<T> findAll() {
		Connection connection = this.getConnecttion();
		List<T> result = new ArrayList<>();
		PreparedStatement preStatement = null;
		ResultSet resultSet = null;
		String tableName = "";

		if (zclass.isAnnotationPresent(Table.class)) {
			Table table = zclass.getAnnotation(Table.class);
			tableName = table.name();
		}
		String sql = "Select * from " + tableName;
		try {
			preStatement = connection.prepareStatement(sql);
			resultSet = preStatement.executeQuery();
			if (zclass.isAnnotationPresent(Entity.class)) {
				while (resultSet.next()) {
					try {
						T obiject = zclass.newInstance();
						ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
						for (int i = 0; i < resultSetMetaData.getColumnCount(); i++) {
							String columName = resultSetMetaData.getColumnName(i + 1);

							Object columValue = resultSet.getObject(i + 1);
							for (Field field : fields) {
								if (field.isAnnotationPresent(Column.class)) {
									Column column = field.getAnnotation(Column.class);
									if (column.name().equals(columName) && columValue != null) {
										// set giá trị
										BeanUtils.setProperty(obiject, field.getName(), columValue);
										break;
									}
								}
							}
						}
						result.add(obiject);
					} catch (InstantiationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return result;
	}

	@Override
	public void deleteById(K k) {
		Connection connection = this.getConnecttion();
		PreparedStatement preStatement = null;
		String tableName = "";
		if (zclass.isAnnotationPresent(Table.class)) {
			Table table = zclass.getAnnotation(Table.class);
			tableName = table.name();

			String id = "";
			try {
				id = getNameId();
				String sql = "delete from" + tableName + " where  " + id + " = ?";
				preStatement = connection.prepareStatement(sql);
				preStatement.setObject(1, k);
				preStatement.executeUpdate();
			} catch (NullPointerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			throw new NullPointerException();
		}
	}

	public Field getFieldId() {
		for (int i = 0; i < fields.length; i++) {
			if (fields[i].isAnnotationPresent(Id.class)) {
				return fields[i];
			}
		}
		return null;
	}

	public String getNameId() {

		Field field = getFieldId();
		if (field == null) {
			throw new NullPointerException();
		}
		if (field.isAnnotationPresent(Column.class)) {
			Column column = field.getAnnotation(Column.class);
			return column.name();
		} else {
			return field.getName();
		}
	}

	@Override
	public void query(String sql, Object... objects) {
		Connection connection = this.getConnecttion();
		PreparedStatement preStatement = null;
		try {
			preStatement = connection.prepareStatement(sql);
			for (int i = 0; i < objects.length; i++) {
				preStatement.setObject(i + 1, objects[i]);
			}
			preStatement.executeUpdate();
		} catch (NullPointerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public List<T> queryForList(String sql, Object... objects) {
		Connection connection = this.getConnecttion();
		List<T> result = new ArrayList<>();
		PreparedStatement preStatement = null;
		ResultSet resultSet = null;
		if (zclass.isAnnotationPresent(Table.class)) {
			try {
				preStatement = connection.prepareStatement(sql);
				for (int i = 0; i < objects.length; i++) {
					preStatement.setObject(i + 1, objects[i]);
				}
				resultSet = preStatement.executeQuery();
				if (zclass.isAnnotationPresent(Entity.class)) {
					while (resultSet.next()) {
						try {
							T obiject = zclass.newInstance();
							ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
							for (int i = 0; i < resultSetMetaData.getColumnCount(); i++) {
								String columName = resultSetMetaData.getColumnName(i + 1);

								Object columValue = resultSet.getObject(i + 1);

								Field[] fields = zclass.getDeclaredFields();
								for (Field field : fields) {
									if (field.isAnnotationPresent(Column.class)) {
										Column column = field.getAnnotation(Column.class);
										if (column.name().equals(columName) && columValue != null) {
											BeanUtils.setProperty(obiject, field.getName(), columValue);
											break;
										}
									}
								}
							}
							result.add(obiject);
						} catch (InstantiationException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (InvocationTargetException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			return result;
		} else {
			throw new NullPointerException();
		}

	}

	@Override
	public void update(T t) {

		Connection connection = this.getConnecttion();
		PreparedStatement preStatement = null;
		try {
			String sql = this.getSqlupdate();
			preStatement = connection.prepareStatement(sql);
			if (zclass.isAnnotationPresent(Table.class)) {
				Object objectId = null;
				int index = 0;
				for (int i = 0; i < fields.length; i++) {
					Field fieldTmp = fields[i];
					if (fieldTmp.isAnnotationPresent(Column.class) && !fieldTmp.isAnnotationPresent(Id.class)) {
						fieldTmp.setAccessible(true);
						preStatement.setObject(i++, fieldTmp.get(t));

					} else if (fieldTmp.isAnnotationPresent(Id.class)) {
						objectId = fieldTmp.get(t);
					}
				}
				preStatement.setObject(index++, objectId);
				preStatement.executeUpdate();
			} else {
				throw new NullPointerException();
			}
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			if (preStatement != null) {
				try {
					preStatement.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}

	public String getSqlupdate() {
		StringBuilder sql = new StringBuilder("update ");
		StringBuilder conditon = new StringBuilder(" where ");
		sql.append(zclass.getAnnotation(Table.class).name());
		sql.append(" set ");
		conditon.append(this.getNameId());
		conditon.append(" = ?");
		for (int i = 0; i < fields.length; i++) {
			if (fields[i].isAnnotationPresent(Column.class) && !fields[i].isAnnotationPresent(Id.class)) {
				Column column = fields[i].getAnnotation(Column.class);
				sql.append(column.name() + " = ?,");
			}
		}
		sql.delete(sql.length() - 1, sql.length());
		sql.append(conditon);
		return sql.toString();
	}

	@Override
	public void insert(T t) {

	}

	@Override
	public void save(Object object) {
		Connection connection = this.getConnecttion();
		String sql = builSqlInsert();
		PreparedStatement preStatement = null;
		try {
			connection.setAutoCommit(false);
			preStatement = connection.prepareStatement(sql);
			Class<?> xclass = object.getClass();
			int index = 1;
			for (Field field : xclass.getDeclaredFields()) {
				field.setAccessible(true);
				preStatement.setObject(index, field.get(object));
				index++;
			}
			preStatement.executeUpdate();
			connection.commit();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (preStatement != null) {
				try {
					preStatement.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}

	private String builSqlInsert() {
		String tableName = "";
		String fieldNames = "";
		String params = "";
		if (zclass.isAnnotationPresent(Table.class)) {
			Table table = zclass.getAnnotation(Table.class);
			tableName = table.name();
		}
		fieldNames = this.getSqlFiledNames();
		params = this.getSqlParams();
		String sql = "insert into " + tableName + " (" + fieldNames.toString() + ") values(" + params.toString() + ")";
		return sql;
	}

	private String getSqlFiledNames() {

		StringBuilder fieldNames = new StringBuilder();

		for (int i = 0; i < fields.length; i++) {
			if (fields[i].isAnnotationPresent(Column.class)) {
				Column column = fields[i].getAnnotation(Column.class);
				fieldNames.append(column.name()).append(",");
			}
		}
		fieldNames.delete(fieldNames.length() - 1, fieldNames.length());
		return fieldNames.toString();
	}

	private String getSqlParams() {

		StringBuilder params = new StringBuilder();

		for (int i = 0; i < fields.length; i++) {
			if (fields[i].isAnnotationPresent(Column.class)) {
				params.append("?").append(",");
			}
		}
		params.delete(params.length() - 1, params.length());

		return params.toString();
	}

	@Override
	public T findById(K k) {
		String tableName = "";
		T obiject = null;
		if (zclass.isAnnotationPresent(Table.class)) {
			Table table = zclass.getAnnotation(Table.class);
			tableName = table.name();

			String sql = "select * from " + tableName + " where " + getNameId() + " = ?";
			Connection connection = this.getConnecttion();
			PreparedStatement preStatement = null;
			ResultSet resultSet = null;
			try {
				preStatement = connection.prepareStatement(sql);
				preStatement.setObject(1, k);
				resultSet = preStatement.executeQuery();
				if (zclass.isAnnotationPresent(Entity.class)) {

					try {
						obiject = zclass.newInstance();
						ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
						for (int i = 0; i < resultSetMetaData.getColumnCount(); i++) {
							String columName = resultSetMetaData.getColumnName(i + 1);

							Object columValue = resultSet.getObject(i + 1);

							Field[] fields = zclass.getDeclaredFields();
							for (Field field : fields) {
								if (field.isAnnotationPresent(Column.class)) {
									Column column = field.getAnnotation(Column.class);
									if (column.name().equals(columName) && columValue != null) {
										BeanUtils.setProperty(obiject, field.getName(), columValue);
										break;
									}
								}
							}
						}

					} catch (Exception e) {
						// TODO: handle exception
					}
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return obiject;
	}
}
