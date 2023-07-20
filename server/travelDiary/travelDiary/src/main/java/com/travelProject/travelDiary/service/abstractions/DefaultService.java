package com.travelProject.travelDiary.service.abstractions;

import com.travelProject.travelDiary.entity.User;
import com.travelProject.travelDiary.entity.abstractions.DefaultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

public abstract class DefaultService<Entity extends DefaultEntity<Id>,Repository extends JpaRepository<Entity,Id>,Manager extends DefaultServiceManager<Entity>,Id> {

    @Autowired
    private Repository repository;

    @Autowired
    private Manager manager;

    public Entity selectOne(Id id){
        Entity entity = repository.findById(id).orElse(null);
        manager.validateEntity(entity);

        return entity;
    }

    public Entity insertOne(Entity entity){
        manager.validateNotId(entity);
        Entity newEntity = repository.save(entity);

        return newEntity;
    }

    public Boolean deleteOne(Id id){
        Entity entity = repository.findById(id).orElse(null);
        manager.validateEntity(entity);

        repository.deleteById(id);
        return true;
    }

    public Entity updateOne(Entity entity){
        manager.validateEntityId(entity);

        Entity oldEntity = repository.findById(entity.getId()).orElse(null);
        manager.validateEntity(oldEntity);

        Entity newEntity = repository.save(entity);

        return newEntity;
    }

}
