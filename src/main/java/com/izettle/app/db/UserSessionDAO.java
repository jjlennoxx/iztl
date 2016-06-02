package com.izettle.app.db;

import com.izettle.app.core.UserSession;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

import java.sql.Timestamp;
import java.util.List;

@RegisterMapper(UserSessionMapper.class)
public interface UserSessionDAO {

    @SqlUpdate("create table user_session (id long auto_increment primary key, user_id long, timestamp timestamp(8))")
    void createUserSessionTable();

    @GetGeneratedKeys
    @SqlUpdate("insert into user_session (user_id, timestamp) values (:userId, now())")
    Long createUserSessionForUserId(@Bind("userId") Long userId);

    @SqlQuery("select id from user_session where user_id = :userId order by id desc limit 1")
    Long findIdByUserId(@Bind("userId") Long userId);

    @SqlQuery("select id from user_session where user_id = :userId and timestamp >= :nowMinusTimeout")
    Long findIdByUserIdAndTimeout(@Bind("userId") Long userId, @Bind("nowMinusTimeout") Timestamp nowMinusTimeout);

    @SqlQuery("select * from user_session where user_id = :userId order by id desc limit :numOfTimestamps")
    List<UserSession> findLastUserTimestamps(@Bind("userId") long userId, @Bind("numOfTimestamps") int numOfTimestamps);
}
