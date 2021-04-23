package com.blog.mapper;
import com.blog.bean.Type;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface TypeMapper {


    int saveType(Type type);

    Type getTypeById(Long id);

    Type getTypeByName(String name);

    List<Type> getAllType();

    List<Type> getAdminType();

    int deleteType(Long id);

    int updateType(Long id,Type type);



}
