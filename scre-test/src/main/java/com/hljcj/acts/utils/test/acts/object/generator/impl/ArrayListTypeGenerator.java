/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hljcj.acts.utils.test.acts.object.generator.impl;

import com.hljcj.acts.utils.test.acts.exception.ActsException;
import com.hljcj.acts.utils.test.acts.helper.CSVHelper;
import com.hljcj.acts.utils.test.acts.log.ActsLogUtil;
import com.hljcj.acts.utils.test.acts.object.generator.ObjectGenerator;
import com.hljcj.acts.utils.test.acts.object.manager.ObjectTypeManager;
import com.google.common.reflect.TypeToken;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.ArrayList;
import java.util.List;

/**
 * ArrayList
 * @author zhiyuan.lzy
 * @version $Id: ListTypeHandler.java, v 0.1 2014年12月16日 下午2:52:19 zhiyuan.lzy Exp $
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class ArrayListTypeGenerator implements ObjectGenerator {

    private static final Logger LOG           = LogManager.getLogger(ArrayListTypeGenerator.class);

    @Override
    public boolean isSimpleType() {
        return false;
    }

    @Override
    public Object generateFieldObject(Class<?> clz, String fieldName, String referedCSVValue) {
        return new ArrayList();
    }

    @Override
    public String generateObjectValue(Object obj, String csvPath, boolean isSimple) {
        String collectionString = "";

        if (((List) obj).size() == 0) {
            collectionString = "@element_empty@";
            return collectionString;
        }

        if (isSimple) {
            for (int i = 0; i < ((List) obj).size(); i++) {
                collectionString = collectionString + String.valueOf(((List) obj).get(i)) + ";";
            }
        } else {

            String reCsvPath = StringUtils.substringAfterLast(csvPath, "/");
            String tempCollectionString = reCsvPath + "@";
            for (int i = 0; i < ((List) obj).size(); i++) {
                try {
                    int index = CSVHelper.insertObjDataAndReturnIndex(((List) obj).get(i), csvPath);
                    collectionString = collectionString + tempCollectionString
                                       + String.valueOf(index) + ";";
                } catch (Exception e) {
                    ActsLogUtil.fail(LOG, "Cann't convert array to string!", e);
                }
            }
        }
        collectionString = collectionString.substring(0, collectionString.length() - 1);
        return collectionString;
    }

    @Override
    public Class<?> getItemClass(Type collectionItemType, Class<?> clz) {
        ParameterizedType genericType = null;
        if (collectionItemType instanceof ParameterizedType) {
            genericType = (ParameterizedType) collectionItemType;
        }

        if (null == genericType) {
            throw new ActsException("Failed to parse type!");
        }

        Type[] typeArguments = genericType.getActualTypeArguments();
        if (typeArguments.length > 1) {
            throw new ActsException("Failed to parse, typeArguments.length = "
                                    + typeArguments.length);
        }

        if (typeArguments[0] instanceof ParameterizedType) {
            ObjectTypeManager subMng = new ObjectTypeManager();
            TypeToken<?> genericTypeToken = TypeToken.of(typeArguments[0]);
            Class innCls = genericTypeToken.getRawType();
            if (subMng.isSimpleType(innCls)) {
                return genericTypeToken.getRawType();
            } else if (subMng.isCollectionType(innCls)) {
                return innCls;
            }
        } else if (typeArguments[0] instanceof TypeVariable) {
            return Object.class;
        } else if (typeArguments[0] instanceof WildcardType) {
            return Object.class;
        } else {
            return (Class<?>) typeArguments[0];
        }
        return null;
    }

    @Override
    public void setObjectValue(Object collectionObject, Object value, String originalValue,
                               int index) {
        if (collectionObject instanceof java.util.List) {
            ((List) collectionObject).add(value);
        } else {
            ActsLogUtil.fail(LOG,
                "Type of [" + collectionObject + "] is " + collectionObject.getClass()
                        + ", but List is required!");
        }
    }
}
