package jp.db.jpa.impl;


import jp.db.jpa.IJPAImpl;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.internal.NativeQueryImpl;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Map;

@Repository
public class JPAImpl implements IJPAImpl {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * 获取session
     * @return
     */
    private Session getHibernateSession() {

        Session session = entityManager.unwrap(Session.class);

        return session;
    }

    /**
     *通过SQL命令执行查询
     * @param sql
     * @return
     */
    public NativeQuery commonQuery(String sql) {
        NativeQuery query = getHibernateSession().createNativeQuery(sql);
        return query;
    }

    /**
     * 通过SQL命令执行查询，target获取接收值
    * @param sql
     * @param target
     * @return
     */
    public NativeQuery commonQueryByClass(String sql, Class target) {

        NativeQuery query = getHibernateSession().createNativeQuery(sql);
        query.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.aliasToBean(target));
        return query;
    }

    /**
     * 通过SQL命令执行增删改查
     * @param sql
     * @param param
     * @return
     */
    public int commonQueryByParam(String sql, Map<String, Object> param) {

        NativeQuery query = getHibernateSession().createNativeQuery(sql).setProperties(param);

        int executeCnt = query.executeUpdate();

        return executeCnt;
    }

    /**
     * 通过SQL返回List集合
     * @param sql
     * @param target
     * @param <T>
     * @return
     */
    public <T> T queryList(String sql, Class target) {

        NativeQuery query = getHibernateSession().createNativeQuery(sql);
        query.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.aliasToBean(target));

        return (T)query.getResultList();
    }

    /**
     * 通过SQL 和参数返回List集合
     * @param sql
     * @param target
     * @param param
     * @param <T>
     * @return
     */
    public <T> T queryListByParam(String sql, Class target, Map<String, Object> param) {

        NativeQuery query = getHibernateSession().createNativeQuery(sql).setProperties(param);
        query.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.aliasToBean(target));

        return (T)query.getResultList();
    }

    /**
     * SQL检索单条数据
     * @param sql
     * @param target
     * @param param
     * @return
     */
    public Object queryByParam(String sql, Class target, Map<String, Object> param) {

        NativeQuery query = getHibernateSession().createNativeQuery(sql).setProperties(param);
        query.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.aliasToBean(target));

        return query.getSingleResult();
    }

    @Override
    public NativeQuery queryByParam(String sql, Map<String, Object> param) {
        NativeQuery query = getHibernateSession().createNativeQuery(sql).setProperties(param);
        query.unwrap(NativeQueryImpl.class);

        return query;
    }

}
