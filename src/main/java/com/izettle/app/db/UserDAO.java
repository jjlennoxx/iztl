package com.izettle.app.db;

import com.izettle.app.core.User;
import org.skife.jdbi.v2.sqlobject.*;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

@RegisterMapper(UserMapper.class)
public interface UserDAO {

	@SqlUpdate("drop table user if exists")
	void dropUserTable();

	@SqlUpdate("create table if not exists user (id long auto_increment primary key, username varchar(100), token varchar(100))")
	void createUserTable();

	@GetGeneratedKeys
	@SqlUpdate("insert into user (username, token) values (:username, :token)")
	Long createNewUser(@Bind("username") String username, @Bind("token") String token);

	@SqlQuery("select id from user where username = :username and token = :token")
	Long findIdByUsernameAndToken(@Bind("username") String username, @Bind("token") String token);

	@SqlQuery("select username from user where id = :id")
	String findUsernameById(@Bind("id") long id);

	@SqlQuery("select id from user where username = :username")
	Long findIdByUsername(@Bind("username") String username);

	@SqlQuery("select * from user where username = :username")
	User findUserByUsername(@Bind("username") String username);

	@SqlQuery("select * from user where username = :username and token = :token")
	User findUserByUsernameAndToken(@Bind("username") String username, @Bind("token") String token);
}
