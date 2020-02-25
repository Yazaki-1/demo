package com.example.lib.core.base;

import com.example.lib.core.util.CRUDUtlis;
import com.example.lib.exception.exceptions.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.io.Serializable;
import java.util.Optional;

public interface CRUDServiceInterface<T, ID extends Serializable> {

    @NotNull
    public JpaRepository<T,ID> getRepository();

    default T show(ID id){
        Optional<T> r = getRepository().findById(id);
        if (r.isEmpty()){
            throw new NotFoundException();
        }
        return r.get();
    }

    default void destroy(ID id){
        boolean b = getRepository().existsById(id);
        if (b == false){
            throw new NotFoundException();
        }
        getRepository().deleteById(id);
    }

    default T store(T model){
        modelSetId(null,model);
        return getRepository().saveAndFlush(model);
    }

    default boolean existsById(ID id){
        return getRepository().existsById(id);
    }

    ///////////////////   update

    T update(T model);

    T update(ID id, T model);

    T updateOfPatch(ID id, T model);

    /////////////////////   list
    default List<T> getAll(){
        return getRepository().findAll();
    }

    default Page<T> getAllOfPage(int page){
        return getAllOfPage(page, defaultSize());
    }

    default Page<T> getAllOfPage(int page, int size){
        return getAllOfPage(page, size, defaultSort());
    }

    default Page<T> getAllOfPage(int page, int size, String... sort){
        return getAllOfPage(page, size,defaultDirection(), sort);
    }

    default Page<T> getAllOfPage(int page, int size, Sort.Direction direction, String... sort){
        return getAllOfPage(page, size, Sort.by(direction , sort));
    }

    default Page<T> getAllOfPage(int page, int size, Sort sort){
        Pageable pageable = PageRequest.of(page,size,sort);
        return getAllOfPage(pageable);
    }

    default Page<T> getAllOfPage(Pageable pageable){
        return getRepository().findAll(pageable);
    }


    default int defaultSize(){
        return 10;
    }
    default String[] defaultSort(){
        return new String[]{"id"};
    }
    default Sort.Direction defaultDirection(){
        return Sort.Direction.DESC;
    }

    //////////////////////////////////////   id  opertion

    default ID modelGetId(T model) {
        return CRUDUtlis.getId(model);
    }

    default void modelSetId(ID id, T model) {
        CRUDUtlis.setId(id,model);
    }
}

