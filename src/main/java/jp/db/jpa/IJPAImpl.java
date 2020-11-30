package jp.db.jpa;

import org.hibernate.query.NativeQuery;

import java.util.Map;

public interface IJPAImpl {

    /**
     *通过SQL命令执行查询
     * @param sql
     * @return
     */
    NativeQuery commonQuery(String sql);

    /**
     * 通过SQL命令执行查询，target获取接收值
     * @param sql
     * @param target
     * @return
     */
    NativeQuery commonQueryByClass(String sql, Class target);

    /**
     * 增删该查
     * @param sql
     * @param param
     * @return
     */
    int commonQueryByParam(String sql, Map<String, Object> param);

    /**
     * 通过SQL返回List集合
     * @param sql
     * @param target
     * @param <T>
     * @return
     */
    <T> T queryList(String sql, Class target);

    /**
     * 通过SQL 和参数返回List集合
     * @param sql
     * @param target
     * @param param
     * @param <T>
     * @return
     */
    <T> T queryListByParam(String sql, Class target, Map<String, Object> param);

    /**
     * SQL检索单条数据
     * @param sql
     * @param target
     * @param param
     * @return
     */
    Object queryByParam(String sql, Class target, Map<String, Object> param);

    /**
     * 检索单条数据
     * @param sql
     * @param param
     * @return
     */
    NativeQuery queryByParam(String sql, Map<String, Object> param);
}
