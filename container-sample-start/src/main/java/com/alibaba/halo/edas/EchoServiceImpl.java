package com.alibaba.halo.edas;

import com.alibaba.boot.hsf.annotation.HSFProvider;
import com.alibaba.fastjson.JSON;
import com.alibaba.halo.edas.mybatis.domain.HaloProjectDO;
import com.alibaba.halo.edas.mybatis.domain.HaloProjectQuery;
import com.alibaba.halo.edas.mybatis.mapper.HaloProjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;


@HSFProvider(serviceInterface = EchoService.class, serviceVersion = "1.0.0")
public class EchoServiceImpl implements EchoService {
    @Autowired
    private HaloProjectMapper haloProjectMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public String echo(String string) {
        Long id = Long.valueOf(string);
        ValueOperations<String,HaloProjectDO> operations = redisTemplate.opsForValue();
        HaloProjectDO projectDO = operations.get(string);
        if(projectDO ==null){
            HaloProjectQuery query = new HaloProjectQuery();
            query.setId(id);
            List<HaloProjectDO> haloProjectDOs = haloProjectMapper.selectByQuery(query);
            if (!CollectionUtils.isEmpty(haloProjectDOs)) {
                projectDO = haloProjectDOs.get(0);
                //put cache
                operations.set(string,projectDO,60, TimeUnit.SECONDS);
            }else {
                //put empty cache
                operations.set(string,new HaloProjectDO());
            }
        }
        if(projectDO !=null){
            return JSON.toJSONString(projectDO);
        }
        return null;
    }
}
