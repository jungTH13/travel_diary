package com.travelProject.travelDiary.service.abstractions;

import com.travelProject.travelDiary.config.exceptionCode;
import com.travelProject.travelDiary.dto.ErrorCode;
import com.travelProject.travelDiary.entity.abstractions.DefaultEntity;

public abstract class DefaultServiceManager<Entity extends DefaultEntity> {
    public void validateEntity(Entity entity){
        if(entity == null || entity.equals("")) throw new exceptionCode(ErrorCode.DISPLAY_NOT_FOUND);
    }

    public void validateNotId(Entity entity){
        if(entity.getId() != null) throw new exceptionCode(ErrorCode.DISPLAY_NOT_FOUND);
    }

    public void validateEntityId(Entity entity){
        if(entity.getId() == null) throw new exceptionCode(ErrorCode.DISPLAY_NOT_FOUND);
    }

    public void validateEntityAuth(Entity entity,String UserId){

    }
}
