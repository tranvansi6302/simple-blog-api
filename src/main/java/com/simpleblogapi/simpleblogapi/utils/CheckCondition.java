package com.simpleblogapi.simpleblogapi.utils;

import com.simpleblogapi.simpleblogapi.enums.PostStatus;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.beans.PropertyDescriptor;
import java.util.HashSet;
import java.util.Set;

public class CheckCondition {
    public static boolean isValidPostStatus(String status) {
        for (PostStatus value : PostStatus.values()) {
            if (value.name().equals(status)) {
                return true;
            }
        }
        return false;
    }

    public static String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<>();
        for (PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) emptyNames.add(pd.getName());
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

}