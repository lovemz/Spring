package com.hust.spring.jdbc;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JDBCTest {

    private ApplicationContext ctx = null;
    private JdbcTemplate jdbcTemplate;
    private EmployeeDao employeeDao;
    private DepartmentDao departmentDao;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    {
        ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
        jdbcTemplate = (JdbcTemplate) ctx.getBean("jdbcTemplate");
        employeeDao = ctx.getBean(EmployeeDao.class);
        departmentDao = ctx.getBean(DepartmentDao.class);
        namedParameterJdbcTemplate = ctx.getBean(NamedParameterJdbcTemplate.class);
    }

    /**
     * 使用具名参数时，可以使用update(String sql, SqlParameterSource paramSource)方法进行更新操作
     * 1、SQL语句中的参数名与类的属性一致
     * 2、使用SqlParameterSource的BeanPropertySqlParameterSource实现类做参数
     */
    @Test
    public void testNamedParameterJdbcTemplate2(){
        String sql = "INSERT INTO employees(last_name, email, dept_id) VALUES(:lastName, :email, :deptId)";
        Employee employee = new Employee();
        employee.setLastName("GG");
        employee.setEmail("gg@hust.edu.cn");
        employee.setDeptId(3);

        SqlParameterSource paramSource = new BeanPropertySqlParameterSource(employee);
        namedParameterJdbcTemplate.update(sql, paramSource);
    }

    /**
     * 可以为参数起名字
     * 1、好处：若有多个参数，则不用再去对应位置，直接对应参数名，便于维护
     * 2、坏处：较为麻烦
     */
    @Test
    public void testNamedParameterJdbcTemplate(){
        String sql = "INSERT INTO employees(last_name, email, dept_id) VALUES(:ln, :email, :deptid)";
        Map<String, Object> paraMap = new HashMap<>();
        paraMap.put("ln", "FF");
        paraMap.put("email", "ff@hust.edu.cn");
        paraMap.put("deptid", 2);
        namedParameterJdbcTemplate.update(sql, paraMap);
    }

    @Test
    public void testDepartmentDao(){
        System.out.println(departmentDao.get(1));
    }


    @Test
    public void testEmployeeDao(){
        System.out.println(employeeDao.get(1));
    }

    /**
     * 获取单个列的值，或做统计查询
     * 使用queryForObject(String sql, Class<long> requiredType)
     */
    @Test
    public void testQueryForObject2(){
        String sql = "SELECT count(id) FROM employees";
        long count = jdbcTemplate.queryForObject(sql, long.class);
        System.out.println(count);
    }

    /**
     * 查找实体类的集合
     * 注意调用的不是queryForList方法
     */
    @Test
    public void testQueryForList(){
        String sql = "SELECT id, last_name lastName, email FROM employees WHERE id > ?";
        RowMapper<Employee> rowMapper = new BeanPropertyRowMapper<>(Employee.class);
        List<Employee> employees = jdbcTemplate.query(sql, rowMapper, 2);
        System.out.println(employees);

    }

    /**
     * 从数据库中获取一条记录，实际得到对应的一个对象
     * 注意不是调用queryForObject(String sql, Class<Employee> requiredType, Object... args)方法！
     * 而需要调用queryForObject(String sql, RowMapper<Employee> rowMapper, Object... args)
     * 1、其中的RowMapper指定如何去映射结果集的行，常用的实现类为BeanPropertyRowMapper
     * 2、使用SQL中列的别名完成列名和类的属性名的映射，例如:last_name lastName
     * 3、不支持级联属性，jdbcTemplate到底是一个JDBC的小工具，而不是ORM框架
     */
    @Test
    public void testQueryForObject(){
        String sql = "SELECT id, last_name lastName, email, dept_id as \"department.id\" FROM employees WHERE id = ?";
        RowMapper<Employee> rowMapper = new BeanPropertyRowMapper<>(Employee.class);
        Employee employee = jdbcTemplate.queryForObject(sql, rowMapper, 1);
        System.out.println(employee);
    }


    /**
     * 执行批量更新：批量的INSERT、UPDATE、DELETE
     * 最后一个参数是Object[]的List类型
     */
    @Test
    public void testBatchUpdate(){
        String sql = "INSERT INTO employees(last_name, email, dept_id) VALUES(?,?,?)";
        List<Object[]> batchArgs = new ArrayList<>();
        batchArgs.add(new Object[]{"AA", "aa@hust.edu.cn", 1});
        batchArgs.add(new Object[]{"BB", "bb@hust.edu.cn", 2});
        batchArgs.add(new Object[]{"CC", "cc@hust.edu.cn", 3});
        batchArgs.add(new Object[]{"DD", "dd@hust.edu.cn", 4});
        batchArgs.add(new Object[]{"EE", "ee@hust.edu.cn", 5});

        jdbcTemplate.batchUpdate(sql, batchArgs);
    }

    /**
     * 执行 UPDATE INSERT DELETE
     */
    @Test
    public void updateTest(){
        String sql = "UPDATE employees SET last_name = ? WHERE id = ?";
        jdbcTemplate.update(sql, "mzyan", 1);
    }

    @Test
    public void testDataSource() throws SQLException {
        DataSource dataSource = ctx.getBean(DataSource.class);
        System.out.println(dataSource.getConnection());
    }
}
