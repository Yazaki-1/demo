package com.example.lib.core.base;

import com.example.lib.core.util.Util;
import com.example.lib.exception.exceptions.BadRequestException;
import com.example.lib.exception.exceptions.NotAcceptableException;
import com.example.lib.exception.exceptions.NotFoundException;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Optional;

/**
 * 加强 update 操作
 * @param <T>
 * @param <ID>
 */
public interface CRUDExServiceInterface<T, ID extends Serializable> extends CRUDServiceInterface<T, ID>  {

    @Override
    default T update(T model){
        if (modelGetId(model) == null) {
            throw new BadRequestException();
        }
        return getRepository().save(model);
    }

    @Override
    default T update(ID id, T model){
        if (modelGetId(model) != null && modelGetId(model) != id) {
            throw new NotAcceptableException();
        }
        modelSetId(id,model);
        return getRepository().save(model);
    }

    @Override
    default T updateOfPatch(ID id, T model){

        if (modelGetId(model) != null && modelGetId(model) != id) {
            throw new NotAcceptableException();
        }

        Optional<T> r = getRepository().findById(id);

        if (r.isEmpty()){
            throw new NotFoundException();
        }

        modelSetId(id,model);
        //支持部分更新
        String[] nullPropertyNames = Util.getNullPropertyNames(model);
        BeanUtils.copyProperties(model,r.get(), nullPropertyNames);

        return getRepository().save(r.get());
    }
}
