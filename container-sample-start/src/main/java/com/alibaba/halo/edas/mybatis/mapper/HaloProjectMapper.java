package com.alibaba.halo.edas.mybatis.mapper;

import com.alibaba.halo.edas.mybatis.domain.HaloProjectDO;
import com.alibaba.halo.edas.mybatis.domain.HaloProjectQuery;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface HaloProjectMapper {

    int insert(HaloProjectDO projectDO);

    List<HaloProjectDO> selectByQuery(HaloProjectQuery query);
}
