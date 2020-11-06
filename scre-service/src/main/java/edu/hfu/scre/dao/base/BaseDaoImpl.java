package edu.hfu.scre.dao.base;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

import org.hibernate.Criteria;
import org.hibernate.Filter;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.LockOptions;
import org.hibernate.ReplicationMode;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import org.hibernate.query.internal.NativeQueryImpl;
import org.hibernate.query.internal.QueryImpl;
import org.hibernate.transform.Transformers;
import org.hibernate.type.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.orm.hibernate5.SessionFactoryUtils;
import org.springframework.stereotype.Component;

import edu.hfu.scre.dao.review.AcademicDao;
@Component(value="baseDaoImpl")
public class BaseDaoImpl implements BaseDao {
	private static final Logger log = LoggerFactory.getLogger(BaseDaoImpl.class);
	@PersistenceContext
	private EntityManager entityManager;
	
	public Session getSession() {
		Session session = null;
		if (entityManager == null    || (session = entityManager.unwrap(Session.class)) == null) {
		    throw new NullPointerException();
		}else {
			return session;
		}
	}
	
	
	private int maxResults = 0;
	public <T> T get(Class<T> entityClass, Serializable id)
			throws Exception {		
		return get(entityClass, id, null);
	}

	public <T> T get(Class<T> entityClass, Serializable id, LockOptions lockOptions)
			throws Exception {
		return (T) getSession().get(entityClass, id, lockOptions);
	}


	public Object get(String entityName, Serializable id)
			throws Exception {
		 return getSession().get(entityName, id);
	}


	public Object get(String entityName, Serializable id, LockOptions lockOptions)
			throws Exception {
		return getSession().get(entityName, id,lockOptions);
	}

	public <T> T load(Class<T> entityClass, Serializable id)
			throws Exception {		
		return (T)getSession().load(entityClass, id);
	}

	public <T> T load(Class<T> entityClass, Serializable id, LockOptions lockOptions)
			throws Exception {	
		return (T)getSession().load(entityClass, id, lockOptions);
	}


	public Object load(String entityName, Serializable id)
			throws Exception {
		return getSession().load(entityName,id);
	}


	public Object load(String entityName, Serializable id, LockOptions lockOptions)
			throws Exception {
		return getSession().load(entityName,id,lockOptions);
	}

	public <T> List<T> loadAll(Class<T> entityClass) throws Exception {
		CriteriaBuilder build = getSession().getCriteriaBuilder();
		CriteriaQuery<T> query=  build.createQuery(entityClass);
		query.from(entityClass);
		List<T> list = getSession().createQuery(query).getResultList();
		return list;
	}


	public void load(Object entity, Serializable id) throws Exception {
		getSession().load(entity,id);		
	}


	public void refresh(Object entity) throws Exception {
		getSession().refresh(entity);
	}


	public void refresh(Object entity, LockOptions lockOptions)
			throws Exception {
		getSession().refresh(entity,lockOptions);
	}


	public boolean contains(Object entity) throws Exception {		
		return getSession().contains(entity);
	}


	public void evict(Object entity) throws Exception {
//		session.evict(obj)，会把指定的缓冲对象进行清除
//		session.clear()，把缓冲区内的全部对象清除，但不包括操作中的对象
		getSession().evict(entity);

	}


	public void initialize(Object proxy) throws Exception {
		try {
			Hibernate.initialize(proxy);
		}
		catch (HibernateException ex) {
			throw SessionFactoryUtils.convertHibernateAccessException(ex);
		}

	}


	public Filter enableFilter(String filterName) throws IllegalStateException {	
		return getSession().enableFilter(filterName);	
	}

	public <T> Serializable save(T t) throws Exception {
		log.debug("函数： save(T t);"+this.getClassContext(t));
		return getSession().save(t);
	}


	public <T> Serializable save(String entityName, T t)
			throws Exception {
		log.debug("函数： save(String entityName, T t);"+this.getClassContext(t));
		return getSession().save(entityName,t);
	}


	public <T> void update(T t) throws Exception {
		log.debug("函数： update(T t);"+this.getClassContext(t));
		 getSession().update(t);
	}

	public <T> void update(String entityName, T t) throws Exception {
		log.debug("函数： update(String entityName, T t);"+this.getClassContext(t));
		 getSession().update(entityName,t);
	}

	public <T> void saveOrUpdate(T t) throws Exception {
		log.debug("函数： saveOrUpdate(T t);"+this.getClassContext(t));
		this.getSession().saveOrUpdate(t);		
	}


	public <T> void saveOrUpdate(String entityName, T t)
			throws Exception {
		log.debug("函数： saveOrUpdate(String entityName, T t);"+this.getClassContext(t));
		this.getSession().saveOrUpdate(entityName, t);
		
	}


	public <T> void replicate(T t, ReplicationMode replicationMode)
			throws Exception {
		log.debug("函数： replicate(T t, ReplicationMode replicationMode);"+this.getClassContext(t));
		this.getSession().replicate(t, replicationMode);
	}
	public <T> void replicate(String entityName, T t,
			ReplicationMode replicationMode) throws Exception {
		log.debug("函数： replicate(String entityName,T t, ReplicationMode replicationMode);"+this.getClassContext(t));
		this.getSession().replicate(entityName, t, replicationMode);		
	}
	public <T> void persist(T t) throws Exception {
		log.debug("函数： persist(T t);"+this.getClassContext(t));
		this.getSession().persist(t);
	}
	public <T> void persist(String entityName, T t) throws Exception {
		log.debug("函数： persist(String entityName,T t);"+this.getClassContext(t));
		this.getSession().persist(entityName,t);
	}
	@SuppressWarnings("unchecked")
	public <T> T merge(T entity) throws Exception {
		log.debug("函数：merge(T entity);"+this.getClassContext(entity));
		return (T)this.getSession().merge(entity);
	}
	@SuppressWarnings("unchecked")
	public <T> T merge(String entityName, T entity) throws Exception {
		log.debug("函数：merge(String entityName, T entity);"+this.getClassContext(entity));
		return (T)this.getSession().merge(entityName,entity);
	}
	public <T> void delete(T entity) throws Exception {
		log.debug("函数：delete(T entity);"+this.getClassContext(entity));
		this.getSession().delete(entity);
	}
	public <T> void delete(String entityName, T entity)	
			throws Exception {
		log.debug("函数：delete(String entityName, T entity);"+this.getClassContext(entity));
		this.getSession().delete(entityName,entity);
	}
	public <T> 	void deleteAll(final Collection<T> entities) throws Exception {
		for(T entity:entities ){
			log.debug("函数：deleteAll(final Collection<T> entities);"+this.getClassContext(entity));
			this.getSession().delete(entity);
		}
	}
	public void flush() throws Exception {
		this.getSession().flush();
	}
	public void clear() throws Exception {
		this.getSession().clear();
	}
	public <T> List<T> find(String hql) throws Exception {
		return this.find(hql, (Object[]) null);
	}
	public List<Map<String,Object>> findMap(String hql) throws Exception{
		return this.findMap(hql,(Object[]) null);
	}
	public <T> List<T> find(String hql, Object value) throws Exception {
		return this.find(hql,new  Object[] {value});
	}
	public List<Map<String,Object>> findMap(String hql, Object value)
			throws Exception{
		return this.findMap(hql,new  Object[] {value});
	}
	
	@SuppressWarnings("unchecked")
	public <T> List<T> find(String hql, Object... values) throws Exception {
		Query<T> queryObject = getSession().createQuery(hql);
		if (values != null) {
			for (int i = 0; i < values.length; i++) {
				if (values[i].getClass().isArray()) {
					Object[] pas=(Object[]) values[i];
					queryObject.setParameterList(i, pas);
				}else {
					queryObject.setParameter(i, values[i]);
				}
			}
		}	
		return (List<T>)queryObject.list();
	}
	
	@SuppressWarnings("unchecked")
	public <T> List<T> find(String hql, Map<String,Object> params)throws Exception{
		Query<T> queryObject = getSession().createQuery(hql);
		if (null!=params){
			for(Map.Entry<String, Object> entry : params.entrySet()) 
			queryObject.setParameter(entry.getKey(), entry.getValue());
		}
		return (List<T>)queryObject.list();
		
	}
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> findMap(String hql, Object... values)
			throws Exception{
		Query<Map<String,Object>> queryObject = getSession().createQuery(hql);
		if (values != null) {
			for (int i = 0; i < values.length; i++) {
				if (values[i].getClass().isArray()) {
					Object[] pas=(Object[]) values[i];
					queryObject.setParameterList(i, pas);
				}else {
					queryObject.setParameter(i, values[i]);
				}
				
			}
		}	
		queryObject.unwrap(QueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		return (List<Map<String,Object>>)queryObject.list();
	}
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> findMap(String hql, Map<String,Object> params)
			throws Exception{
		Query<Map<String,Object>> queryObject = getSession().createQuery(hql);
		
		if (null!=params){
			for(Map.Entry<String, Object> entry : params.entrySet()) 
				queryObject.setParameter(entry.getKey(), entry.getValue());
		}	
		queryObject.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		return (List<Map<String,Object>>)queryObject.list();
	}
	public List<Object> findByNamedParam(String hql, String paramName, Object value)
			throws Exception {
		return findByNamedParam(hql, new String[] {paramName}, new Object[] {value});
	}

	@SuppressWarnings("unchecked")
	public List<Object> findByNamedParam(String hql, String[] paramNames,
			Object[] values) throws Exception {
		if (paramNames.length != values.length) {
			throw new IllegalArgumentException("Length of paramNames array must match length of values array");
		}
		Query<Object> queryObject = getSession().createQuery(hql);
		if (values != null) {
			for (int i = 0; i < values.length; i++) {
				applyNamedParameterToQuery(queryObject, paramNames[i], values[i]);
			}
		}
		return queryObject.list();
	}

	@SuppressWarnings("unchecked")
	public List<Object> findByValueBean(String hql, Object valueBean)
			throws Exception {
		Query<Object> queryObject = getSession().createQuery(hql);
		queryObject.setProperties(valueBean);
		return  (List<Object>)queryObject.list();
	}


	public List<Object> findByNamedQuery(String queryName) throws Exception {
		return findByNamedQuery(queryName, (Object[]) null);
	}


	public List<Object> findByNamedQuery(String queryName, Object value)
			throws Exception {
		return findByNamedQuery(queryName, new Object[] {value});
	}

	@SuppressWarnings("unchecked")
	public List<Object> findByNamedQuery(String queryName, Object... values)
			throws Exception {
		Query<Object> queryObject = getSession().getNamedQuery(queryName);
		if (values != null) {
			for (int i = 0; i < values.length; i++) {
				if (values[i].getClass().isArray()) {
					Object[] pas=(Object[]) values[i];
					queryObject.setParameterList(i, pas);
				}else {
					queryObject.setParameter(i, values[i]);
				}
			}
		}
		return (List<Object>)queryObject.list();
	}


	public List<Object> findByNamedQueryAndNamedParam(String queryName, String paramName,
			Object value) throws Exception {
		return findByNamedQueryAndNamedParam(queryName, new String[] {paramName}, new Object[] {value});
	}

	@SuppressWarnings("unchecked")
	
	public List<Object> findByNamedQueryAndNamedParam(String queryName, String[] paramNames,
			Object[] values) throws Exception {
		if (paramNames != null && values != null && paramNames.length != values.length) {
			throw new IllegalArgumentException("Length of paramNames array must match length of values array");
		}
		Query<Object> queryObject = getSession().getNamedQuery(queryName);
		if (values != null) {
			for (int i = 0; i < values.length; i++) {
				applyNamedParameterToQuery(queryObject, paramNames[i], values[i]);
			}
		}
		return  (List<Object>)queryObject.list();
	}

	@SuppressWarnings("unchecked")
	
	public List<Object> findByNamedQueryAndValueBean(String queryName, Object valueBean)
			throws Exception {
		Query<Object> queryObject = getSession().getNamedQuery(queryName);
		queryObject.setProperties(valueBean);
		return (List<Object>)queryObject.list();
	}

	
	public List<Object> findByCriteria(DetachedCriteria criteria)
			throws Exception {
		return findByCriteria(criteria, -1, -1);
	}

	@SuppressWarnings("unchecked")
	
	public List<Object> findByCriteria(DetachedCriteria criteria, int pageNo,
			int maxResults) throws Exception {
		Criteria executableCriteria = criteria.getExecutableCriteria(getSession());
		if (pageNo<1)
			throw new Exception("pageNo 必须大于 0");
		int firstResult=(pageNo-1)*maxResults;
		if (firstResult >= 0) {
			executableCriteria.setFirstResult(firstResult);
		}
		if (maxResults > 0) {
			executableCriteria.setMaxResults(maxResults);
		}
		return (List<Object>)executableCriteria.list();
	}
	
	public Iterator<?> iterate(String hql) throws Exception {
		return iterate(hql, (Object[]) null);
	}

	
	public Iterator<?> iterate(String hql, Object value)
			throws Exception {
		return iterate(hql, new Object[] {value});
	}

	
	public Iterator<?> iterate(String hql, Object... values)
			throws Exception {
		Query<?> queryObject = this.getSession().createQuery(hql);
		if (values != null) {
			for (int i = 0; i < values.length; i++) {
				if (values[i].getClass().isArray()) {
					Object[] pas=(Object[]) values[i];
					queryObject.setParameterList(i, pas);
				}else {
					queryObject.setParameter(i, values[i]);
				}
			}
		}
		return queryObject.list().iterator();
	}

	
	public void closeIterator(Iterator<?> it) throws Exception {
		try {
			Hibernate.close(it);
		}
		catch (HibernateException ex) {
			throw SessionFactoryUtils.convertHibernateAccessException(ex);
		}
	}

	
	//批量更新
	public int bulkUpdate(String queryString) throws Exception {
		return bulkUpdate(queryString, (Object[]) null);
	}
	public int bulkSqlUpdate(String queryString) throws Exception{
		return bulkSqlUpdate(queryString, (Object[]) null);
	}
	
	public int bulkUpdate(String queryString, Object value)
			throws Exception {
		return bulkUpdate(queryString, new Object[] {value});
	}
	public int bulkSqlUpdate(String queryString, Object value)
			throws Exception{
		return bulkSqlUpdate(queryString, new Object[] {value});
	}
	
	public int bulkUpdate(String queryString, Object... values)
			throws Exception {
		Query<?> queryObject = this.getSession().createQuery(queryString);
		String mess="函数：bulkUpdate(String queryString, Object... values);HQL语句："+queryString+"对应值：";
		if (values != null) {
			for (int i = 0; i < values.length; i++) {
				if (values[i].getClass().isArray()) {
					Object[] pas=(Object[]) values[i];
					queryObject.setParameterList(i, pas);
				}else {
					queryObject.setParameter(i, values[i]);
				}
				mess=mess+values[i] ;
			}
		}
		log.debug(mess);
		return queryObject.executeUpdate();
	}
	public int bulkSqlUpdate(String queryString, Object... values)
			throws Exception{
		NativeQuery<?> queryObject = this.getSession().createSQLQuery(queryString);
		String mess="函数：bulkUpdate(String queryString, Object... values);HQL语句："+queryString+"对应值：";
		if (values != null) {
			for (int i = 0; i < values.length; i++) {
				if (values[i].getClass().isArray()) {
					Object[] pas=(Object[]) values[i];
					queryObject.setParameterList(i, pas);
				}else {
					queryObject.setParameter(i, values[i]);
				}
				mess=mess+values[i] ;
			}
		}
		log.debug(mess);
		return queryObject.executeUpdate();
	}
	

	public int getMaxResults() {
		return maxResults;
	}

	public void setMaxResults(int maxResults) {
		this.maxResults = maxResults;
	}
	protected void applyNamedParameterToQuery(Query<?> queryObject, String paramName, Object value)
			throws HibernateException {

		if (value instanceof Collection) {
			queryObject.setParameterList(paramName, (Collection<?>) value);
		}
		else if (value instanceof Object[]) {
			queryObject.setParameterList(paramName, (Object[]) value);
		}
		else {
			queryObject.setParameter(paramName, value);
		}
	}

	
	public <T> List<T> findByPage(String hql, int pageNo, int maxResults)
			throws Exception {
		Object[] params=null;
		return findByPage(hql,params,pageNo,maxResults);
	}

	
	public <T> List<T> findByPage(String hql, Object param, int pageNo,
			int maxResults) throws Exception {
		return findByPage(hql,new Object[] {param},pageNo,maxResults);
	}

	@SuppressWarnings("unchecked")
	
	public <T> List<T> findByPage(String hql, Object[] params, int pageNo,
			int maxResults) throws Exception {
		Query<?> queryObject = getSession().createQuery(hql);
		if (pageNo<1)
			throw new Exception("pageNo 必须大于 0");
		int firstResult=(pageNo-1)*maxResults;
		if (params != null) {
			for (int i = 0; i < params.length; i++) {
				queryObject.setParameter(i, params[i]);
			}
		}		
		queryObject.setFirstResult(firstResult);  
		queryObject.setMaxResults(maxResults);  
		return  (List<T>)queryObject.list();
	}
	
	@SuppressWarnings("unchecked")
	public <T> List<T> findByPage(final String hql, final Map<String,Object> params,
			final int pageNo, final int maxResults)
			throws Exception{
		Query<?> queryObject = getSession().createQuery(hql);
		if (pageNo<1)
			throw new Exception("pageNo 必须大于 0");
		int firstResult=(pageNo-1)*maxResults;
		if (null!=params){
			for(Map.Entry<String, Object> entry : params.entrySet()) 
				queryObject.setParameter(entry.getKey(), entry.getValue());
		}	
		queryObject.setFirstResult(firstResult);  
		queryObject.setMaxResults(maxResults);  
		return  (List<T>)queryObject.list();
	}
	
	public  List<Map<String,Object>> findByPageMap(final String hql, final int pageNo,
			final int maxResults) throws Exception{
		Object[] params=null;
		return findByPageMap(hql,params,pageNo,maxResults);
	}

	public  List<Map<String,Object>> findByPageMap(final String hql, final Object param,
			final int pageNo, final int maxResults)
			throws Exception{
		return findByPageMap(hql,new Object[] {param},pageNo,maxResults);
	}

	@SuppressWarnings("unchecked")
	public  List<Map<String,Object>> findByPageMap(final String hql, final Object[] params,
			final int pageNo, final int maxResults)
			throws Exception{
		Query<?> queryObject = getSession().createQuery(hql);
		if (pageNo<1)
			throw new Exception("pageNo 必须大于 0");
		int firstResult=(pageNo-1)*maxResults;
		if (params != null) {
			for (int i = 0; i < params.length; i++) {
				queryObject.setParameter(i, params[i]);
			}
		}		
		queryObject.unwrap(QueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		queryObject.setFirstResult(firstResult);  
		queryObject.setMaxResults(maxResults);  
		return  (List<Map<String,Object>>)queryObject.list();
	}
	
	@SuppressWarnings("unchecked")
	public  List<Map<String,Object>> findByPageMap(final String hql, final Map<String,Object> params,
			final int pageNo, final int maxResults)
			throws Exception{
		Query<?> queryObject = getSession().createQuery(hql);
		if (pageNo<1)
			throw new Exception("pageNo 必须大于 0");
		int firstResult=(pageNo-1)*maxResults;
		if (null!=params){
			for(Map.Entry<String, Object> entry : params.entrySet()) 
				queryObject.setParameter(entry.getKey(), entry.getValue());
		}			
		queryObject.unwrap(QueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		queryObject.setFirstResult(firstResult);  
		queryObject.setMaxResults(maxResults);  
		return  (List<Map<String,Object>>)queryObject.list();
	}
	
	@SuppressWarnings("unchecked")
	public <T> List<T> queryBean(T t) throws Exception {
		Map<String, Object> map=beanToHql(t);
		String hql=map.get("hql").toString();
		List<Object> param=(List<Object>) map.get("param");
		log.debug(hql.toString());
		return this.find(hql.toString(), param.toArray());
	}

	/**
	 * 仅支持单个实体bean 查询 
	 */
	public <T> Map<String, Object> beanToHql(T t) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			StringBuilder hql = new StringBuilder("from "
					+ t.getClass().getName() + " o  ");
			List<Object> param = new ArrayList<Object>();
			if(null!=t){
				List<Field> fields=getFiledsInfo(t);
				int index=0;
				for(int i=0;i<fields.size();i++){
					Field field=fields.get(i);
					field.setAccessible(true);//设置可访问私有
					if (field.getType().getName().equals("java.lang.String")){
						if(null!=field.get(t)&&field.get(t).toString().length()>0&&!field.get(t).toString().equals("")){
							hql.append((index==0?" where ":" and ")+"  o."+field.getName()+" like ?"+(index++));
							param.add('%' +field.get(t).toString()+'%' );
						}
					}else {
						if(null!=field.get(t)){
							hql.append((index==0?" where ":" and ")+"  o."+field.getName()+" =?"+(index++));
							param.add(field.get(t));
						}
					}
				}
			}
			log.debug(hql.toString());
			map.put("hql", hql.toString());
			map.put("param", param);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}
		return map;
	}

	
	@SuppressWarnings("unchecked")
	public <T> List<T> queryBean(T t, int pageNo,int maxResults) throws Exception {
		Map<String, Object> map=beanToHql(t);
		String hql=map.get("hql").toString();
		List<Object> param=(List<Object>) map.get("param");		
		log.debug(hql);
		if (maxResults==0)
			return this.find(hql, param.toArray());
		return this.findByPage(hql.toString(), param.toArray(), pageNo, maxResults);
	}

	
	public int queryBeanCountByHql(String hql, Object[] params) throws Exception {
		hql=hql.trim();
		if (!hql.substring(0, 4).equalsIgnoreCase("from")){
			log.error("传入的HQL无法合成统计语句，正确 形式如下  from beanName o where ...");		
			throw new DataAccessResourceFailureException("传入的HQL无法合成统计语句，正确 形式如下  from beanName o where ...");
		}
		String newhql="select count(*) "+hql;
		List<Object> ls=this.find(newhql, params);
		int count=Integer.valueOf(ls.get(0).toString());
		return count;
	}

	
	@SuppressWarnings("unchecked")
	public <T> int queryBeanCount(T t) throws Exception {
		Map<String, Object> map=beanToHql(t);
		String hql=map.get("hql").toString();
		List<Object> param=(List<Object>) map.get("param");
		log.debug(hql.toString());		
		return queryBeanCountByHql(hql.toString(),param.toArray());
	}
	@Override
	public List<Map<String,Object>> findBySQL(String sql,final Object[] params)throws Exception{
		return findBySQL(sql,params,null);
	}
	@Override
	public List<Map<String,Object>> findBySQL(String sql) throws Exception {
		return findBySQL(sql,null,null);
	}
	@Override
	public List<Map<String,Object>> findBySQL(String sql,Map<String,Type> colNameANDdataType)throws Exception {
		return findBySQL(sql,null,colNameANDdataType);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> findBySQL(String sql,final Object[] params,Map<String,Type> colNameANDdataType)throws Exception {
		NativeQuery<Map<String,Object>> sqlQuery=getSession().createSQLQuery(sql);
		if (null!=colNameANDdataType){
			Iterator<String> keys = colNameANDdataType.keySet().iterator();
			//增加数据类型
			while(keys.hasNext()){
				String colName = keys.next();//key
				sqlQuery.addScalar(colName.substring(colName.indexOf('.')+1),colNameANDdataType.get(colName));
			}
		}
		if (params != null) {
			for (int i = 0; i < params.length; i++) {
				sqlQuery.setParameter(i, params[i]);
			}
		}	
		sqlQuery.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		return sqlQuery.list();
	}

	@Override
	public List<Map<String,Object>> findBySQL(String sql, int pageNo, int maxResults)
			throws Exception {
		return this.findBySQL(sql, null, null, pageNo, maxResults);
	}

	@Override
	public List<Map<String,Object>> findBySQL(String sql,
			Map<String, Type> colNameANDdataType, int pageNo, int maxResults)
			throws Exception {
		
		return this.findBySQL(sql, null, colNameANDdataType, pageNo, maxResults);
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String,Object>> findBySQL(String sql, Object[] params,
			Map<String, Type> colNameANDdataType, int pageNo, int maxResults)
			throws Exception {
		
		if (pageNo<1)
			throw new Exception("pageNo 必须大于 0");
		int firstResult=(pageNo-1)*maxResults;
		
		NativeQuery<Map<String,Object>> sqlQuery=getSession().createSQLQuery(sql);
		if (null!=colNameANDdataType){
			Iterator<String> keys = colNameANDdataType.keySet().iterator();
			//增加数据类型
			while(keys.hasNext()){
				String colName = keys.next();//key
				sqlQuery.addScalar(colName.substring(colName.indexOf('.')+1),colNameANDdataType.get(colName));
			}
		}
		if (params != null) {
			for (int i = 0; i < params.length; i++) {
				sqlQuery.setParameter(i, params[i]);
			}
		}	
		sqlQuery.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		sqlQuery.setFirstResult(firstResult);
		sqlQuery.setMaxResults(maxResults);
		return sqlQuery.list();
	}

	
	private <T> String getClassContext(T t){
		StringBuffer sb=new StringBuffer("操作对象："+t.getClass().getName()+"操作内容：");		
		try {
			if (null != t) {
				Field[] fields = t.getClass().getDeclaredFields();
				for (int i = 0; i < fields.length; i++) {
					fields[i].setAccessible(true);//设置可访问私有
					sb.append(fields[i].getName()+"="+fields[i].get(t)+" ");				
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}		
		return sb.toString();
	}
	
	 /**
     * 利用Java反射根据类的名称获取属性信息和父类的属性信息(目前仅支持一层父类)
     * @param className
     * @return
     * @throws ClassNotFoundException
     */

    private  <T> List<Field>  getFiledsInfo(T t) throws ClassNotFoundException {
        List<Field> list = new ArrayList<>();
        Class<?> clazz = t.getClass();
        Field[] fields = clazz.getDeclaredFields();
        list.addAll(Arrays.asList(fields));
        Class<?> superClazz = clazz.getSuperclass();
        if (superClazz != null) {
            Field[] superFields = superClazz.getDeclaredFields();
            list.addAll(Arrays.asList(superFields));
        }
        return list;
    }
    
}
