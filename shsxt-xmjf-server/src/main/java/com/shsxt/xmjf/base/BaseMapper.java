package com.shsxt.xmjf.base;

import com.shsxt.xmjf.api.query.BaseQuery;

import java.util.List;

/**
 * Created by lp on 2018/3/1.
 */
public interface BaseMapper<T> {

    public  Integer insert(T entity);
    public Integer insertBatch(List<T> entities);

    public  T queryById(Integer id);

    public  List<T> queryByParams(BaseQuery baseQuery);

    public  Integer update(T entity);

    public  Integer delete(Integer id);

    public  Integer deleteBatch(Integer[] ids);


}
