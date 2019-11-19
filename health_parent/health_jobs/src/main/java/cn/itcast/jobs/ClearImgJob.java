package cn.itcast.jobs;

import cn.itcast.constant.RedisConstant;
import cn.itcast.utils.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.JedisPool;

import java.util.Set;

/**
 * @Author:Administrator
 * @Date: 2019/11/19 20:03
 * 自定义job,实现垃圾图片自动清理
 */
public class ClearImgJob {
    @Autowired
    private JedisPool jedisPool;
    public void clearImg(){
        // 获取两个Redis集合的差集的集合
        Set<String> set = jedisPool.getResource().sdiff(RedisConstant.SETMEAL_PIC_RESOURCES, RedisConstant.SETMEAL_PIC_DB_RESOURCES);
        // 调用工具类中的删除方法进行删除
        if (set != null){
            for (String pic : set) {
                QiniuUtils.deleteFileFromQiniu(pic);
                // redis 中也删除
                jedisPool.getResource().srem(RedisConstant.SETMEAL_PIC_RESOURCES,pic);
            }
        }

    }
}
