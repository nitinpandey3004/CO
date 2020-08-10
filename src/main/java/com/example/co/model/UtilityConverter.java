package com.example.co.model;

import com.example.co.exceptions.BadRequestException;
import com.example.co.exceptions.NotFoundException;
import com.example.co.repositories.SdlcSystemRepository;
import lombok.RequiredArgsConstructor;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashMap;
import java.util.Map;

@RequiredArgsConstructor
public class UtilityConverter {

    public static Project getProject(Project oldProject, Map<String, Object> patchObject, SdlcSystemRepository sdlcSystemRepository) {

        for(String key: patchObject.keySet()) {
            callSetter(oldProject, key, patchObject.get(key), sdlcSystemRepository);
        }

        return oldProject;
    }

    private static void callSetter(Object object, String fieldName, Object value, SdlcSystemRepository sdlcSystemRepository) {
        PropertyDescriptor pd;
        try {
            pd = new PropertyDescriptor(fieldName, object.getClass());
            if(fieldName.equals("sdlcSystem")) {
                LinkedHashMap sdlcMap = (LinkedHashMap)value;
                SdlcSystem sdlcSystem = sdlcSystemRepository.findById(new Long((int)sdlcMap.get("id"))).orElseThrow(() -> new NotFoundException(SdlcSystem.class, new Long((int)sdlcMap.get("id"))));
                pd.getWriteMethod().invoke(object, sdlcSystem);
            } else {
                pd.getWriteMethod().invoke(object, value);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IntrospectionException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (ClassCastException e) {
            throw new BadRequestException(e);
        }
    }

}
