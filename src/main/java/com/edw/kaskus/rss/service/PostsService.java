package com.edw.kaskus.rss.service;

import com.edw.kaskus.rss.bean.Posts;
import com.edw.kaskus.rss.config.MyBatisSqlSessionFactory;
import com.edw.kaskus.rss.mapper.PostsMapper;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

/**
 *
 * @author edwin < edwinkun at gmail dot com >
 */
public class PostsService {

    private Logger logger = Logger.getLogger(this.getClass());

    public void insert(Posts posts) {
        SqlSession sqlSession = null;
        try {
            sqlSession = MyBatisSqlSessionFactory.getSqlSessionFactory().openSession(true);
            PostsMapper mapper = sqlSession.getMapper(PostsMapper.class);
            mapper.insert(posts);
        } catch (Exception e) {
            logger.error(e, e);
        } finally {
            if (sqlSession != null) {
                sqlSession.close();
            }
        }
    }

}
