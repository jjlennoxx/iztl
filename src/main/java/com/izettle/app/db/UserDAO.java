package com.izettle.app.db;

import com.izettle.app.core.User;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

@RegisterMapper(UserMapper.class)
public interface UserDAO {

	@SqlUpdate("drop table user if exists")
	void dropUserTable();

	@SqlUpdate("create table user (id long primary key, username varchar(100), password varchar(100))")
	void createUserTable();

	@SqlUpdate("insert into user (id, username, password) values (:id, :username, :password)")
	void insert(@Bind("id") long id, @Bind("username") String username, @Bind("password") String password);

	@SqlQuery("select id from user where username = :username and password = :password")
	Long findIdByUsernameAndPassword(@Bind("username") String username, @Bind("password") String password);

	@SqlQuery("select username from user where id = :id")
	String findUsernameById(@Bind("id") long id);

	@SqlQuery("select id from user where username = :username")
	Long findIdByUsername(@Bind("username") String username);

	@SqlQuery("select * from user where username = :username")
	User findUserByUsername(@Bind("username") String username);

	@SqlQuery("select * from user where username = :username and password = :password")
	User findUserByUsernameAndPassword(@Bind("username") String username, @Bind("password") String password);
}
