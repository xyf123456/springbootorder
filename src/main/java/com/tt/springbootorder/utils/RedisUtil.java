package com.tt.springbootorder.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName: RedisUtil
 * @Description: Redis工具类
 * @Author: Administrator
 * @CreateDate: 2019/7/30 0030 下午 8:50
 * @UpdateUser: Administrator
 * @Version: 1.0
 **/
//@Component
public final class RedisUtil {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * @ Description: 指定缓存失效时间
     * @params:  * @param key 键
     * @param time  时间（秒）
     * @return:boolean
     **/
    public boolean expire(String key, long time) {
        try {
            if (time > 0) {
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * @ Description: 根据key 获取过期时间
     * @params:  * @param key 不能为null
     * @return:long 时间(秒) 返回0代表为永久有效
     **/
    public long getExpire(String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }
    /**
     * @ Description: 判断key是否存在
     * @params:  * @param key 键
     * @return:boolean 存在 false不存在
     **/
    public boolean hasKey(String key) {
        try {
            return redisTemplate.hasKey(key);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    /**
     * @ Description: 删除缓存
     * @params:  * @param key 可以传一个值 或多个
     * @return:void
     **/
    public void del(String... key) {
        if (key != null && key.length > 0) {
            if (key.length == 1) {
                redisTemplate.delete(key[0]);
            }else {
                redisTemplate.delete(CollectionUtils.arrayToList(key));
            }
        }
    }

    /**
     * @ Description: 普通缓存获取
     * @params:  * @param key 键
     * @return:java.lang.Object 值
     **/
    public Object get(String key) {
        return key == null ? null : redisTemplate.opsForValue().get(key);
    }

    /**
     * @ Description: 普通缓存放入
     * @params:  * @param key 键
     * @param value 值
     * @return:boolean  true成功 false失败
     **/
    public boolean set(String key, Object value){

        try {
            redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * @ Description: 普通缓存放入并设置时间
     * @params:  * @param key 键
     * @param value 值
     * @param time 时间(秒) time要大于0 如果time小于等于0 将设置无限期
     * @return:boolean true成功 false 失败
     **/
    public boolean set(String key, Object value, long time) {

        try {
            if (time > 0) {
                redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
            }else {
                set(key, value);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * @ Description: 递增
     * @params:  * @param key 键
     * @param delta  要增加几(大于0)
     * @return:long
     **/
    public long incr(String key, long delta) {

        if (delta < 0) {
            throw new RuntimeException("递增因子必须大于0");
        }
        return redisTemplate.opsForValue().increment(key, delta);
    }


    /**
     * @ Description: 递减
     * @params:  * @param key 键
     * @param delta  要减少几(小于0)
     * @return:long
     **/
    public long decr(String key, long delta) {

        if (delta < 0) {
            throw new RuntimeException("递减因子必须大于0");
        }
        return redisTemplate.opsForValue().increment(key, -delta);
    }


    /**
     * @ Description: HashGet
     * @params:  * @param key 键 不能为null
     * @param item 项 不能为null
     * @return:java.lang.Object  值
     **/
    public Object hget(String key, String item) {

        return redisTemplate.opsForHash().get(key, item);
    }


    /**
     * @ Description:  获取hashKey对应的所有键值
     * @params:  * @param key 键
     * @return:java.util.Map<java.lang.Object,java.lang.Object> 对应的多个键值
     **/
    public Map<Object, Object> hmget(String key) {

        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * @ Description: HashSet
     * @params:  * @param key 键
     * @param map 对应多个键值
     * @return:boolean  true 成功 false 失败
     **/
    public boolean hmset(String key, Map<String, Object> map) {

        try {
            redisTemplate.opsForHash().putAll(key, map);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * @ Description:  HashSet 并设置时间
     * @params:  * @param key  键
     * @param map 对应多个键值
     * @param time 时间(秒)
     * @return:boolean  true成功 false失败
     **/
    public boolean hmset(String key, Map<String, Object> map, long time) {

        try {
            redisTemplate.opsForHash().putAll(key, map);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * @ Description: 向一张hash表中放入数据,如果不存在将创建
     * @params:  * @param key 键
     * @param item 项
     * @param value 值
     * @return:boolean true 成功 false失败
     **/
    public boolean hset(String key, String item, Object value) {

        try {
            redisTemplate.opsForHash().put(key, item, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean hset(String key, String item, Object value, long time) {
        try {
            redisTemplate.opsForHash().put(key, item, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * @ Description: 删除hash表中的值
     * @params:  * @param key
     * @param item
     * @return:void
     **/
    public void hdel(String key, Object... item) {

        redisTemplate.opsForHash().delete(key, item);
    }


    /**
     * @ Description: 判断hash表中是否有该项的值
     * @params:  * @param key 键 不能为null
     * @param item  项 不能为null
     * @return:boolean   true 存在 false不存在
     **/
    public boolean hHasKey(String key, String item) {

        return redisTemplate.opsForHash().hasKey(key, item);
    }

    /**
     * @ Description: hash递增 如果不存在,就会创建一个 并把新增后的值返回
     * @params:  * @param key 键
     * @param item 项
     * @param by 要增加几(大于0)
     * @return:double
     **/
    public double hincr(String key, String item, double by) {
        return redisTemplate.opsForHash().increment(key, item, by);
    }


    /**
     * @ Description: hash递减
     * @params:  * @param key 键
     * @param item 项
     * @param by 要减少记(小于0)
     * @return:double
     **/
    public double hdecr(String key, String item, double by) {

        return redisTemplate.opsForHash().increment(key, item, -by);
    }


    /**
     * @ Description:  根据key获取Set中的所有值
     * @params:  * @param key 键
     * @return:java.util.Set<java.lang.Object> 所有值
     **/
    public Set<Object> sGet(String key) {

        try {
            return redisTemplate.opsForSet().members(key);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @ Description: 根据value从一个set中查询,是否存在
     * @params:  * @param key 键
     * @param value 值
     * @return:boolean true 存在 false不存在
     **/
    public boolean sHasKey(String key, Object value) {

        try {
            return redisTemplate.opsForSet().isMember(key, value);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * @ Description: 将数据放入set缓存
     * @params:  * @param key  键
     * @param values 值 可以是多个
     * @return:long 成功个数
     **/
    public long sSet(String key, Object... values) {

        try {
            return redisTemplate.opsForSet().add(key, values);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * @ Description: 将set数据放入缓存
     * @params:  * @param key 键
     * @param time 时间(秒)
     * @param values 值 可以是多个
     * @return:long 成功个数
     **/
    public long sSetAndTime(String key, long time, Object... values) {

        try {
            Long count = redisTemplate.opsForSet().add(key, values);
            if (time > 0){
                expire(key, time);
            }
            return count;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * @ Description: 获取set缓存的长度
     * @params:  * @param key 键
     * @return:long  长度
     **/
    public long sGetSetSize(String key) {

        try {
            return redisTemplate.opsForSet().size(key);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * @ Description:  移除值为value的
     * @params:  * @param key 键
     * @param values 值 可以是多个
     * @return:long   移除的个数
     **/
    public long setRemove(String key, Object... values) {

        try {
            Long count = redisTemplate.opsForSet().remove(key, values);
            return count;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * @ Description: 获取list缓存的内容
     * @params:  * @param key 键
     * @param start 开始
     * @param end  结束 end 结束 0 到 -1代表所有值
     * @return:java.util.List<java.lang.Object>
     **/
    public List<Object> lGet(String key, long start, long end) {

        try {
            return redisTemplate.opsForList().range(key, start, end);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @ Description: 获取list缓存的长度
     * @params:  * @param key 键
     * @return:long
     **/
    public long lGetListSize(String key) {

        try {
            return redisTemplate.opsForList().size(key);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }


    /**
     * @ Description: 通过索引 获取list中的值
     * @params:  * @param key 键
     * @param index index 索引 index>=0时， 0 表头，1 第二个元素，依次类推；index<0时，-1，表尾，
               -2倒数第二个元素，依次类推
     * @return:java.lang.Object
     **/
    public Object lGetIndex(String key, long index) {

        try {
            return redisTemplate.opsForList().index(key, index);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @ Description: 将list放入缓存
     * @params:  * @param key 键
     * @param value 值
     * @return:boolean
     **/
    public boolean lSet(String key, Object value) {

        try {
            redisTemplate.opsForList().rightPush(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * @ Description: 将list放入缓存
     * @params:  * @param key 键
     * @param value 值
     * @param time 时间(秒)
     * @return:boolean
     **/
    public boolean lSet(String key, Object value, long time) {
        try {
            redisTemplate.opsForList().rightPush(key, value);
            if (time > 0)
                expire(key, time);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * @ Description: 将list放入缓存
     * @params:  * @param key 键
     * @param value  值
     * @return:boolean true 成功 false 失败
     **/
    public boolean lSet(String key, List<Object> value) {

        try {
            redisTemplate.opsForList().rightPushAll(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    /**
     * @ Description: 将list放入缓存
     * @params:  * @param key 键
     * @param value 值
     * @param time 时间(秒)
     * @return:boolean
     **/
    public boolean lSet(String key, List<Object> value, long time) {

        try {
            redisTemplate.opsForList().rightPushAll(key, value);
            if (time > 0)
                expire(key, time);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * @ Description: 根据索引修改list中的某条数据
     * @params:  * @param key 键
     * @param index 索引
     * @param value 值
     * @return:boolean
     **/
    public boolean lUpdateIndex(String key, long index, Object value) {

        try {
            redisTemplate.opsForList().set(key, index, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * @ Description:  移除N个值为value
     * @params:  * @param key 键
     * @param count 移除多少个
     * @param value 值
     * @return:long  移除的个数
     **/
    public long lRemove(String key, long count, Object value) {

        try {
            Long remove = redisTemplate.opsForList().remove(key, count, value);
            return remove;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}
