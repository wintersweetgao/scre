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
package com.hljcj.acts.utils.test.acts.object.generator;

import java.lang.reflect.Type;

/**
 *
 * @author baishuo.lp
 * @version $Id: ObjectTypeHandler.java, v 0.1 2015年8月14日 下午4:49:16 baishuo.lp Exp $
 */
public interface ObjectGenerator {

    boolean isSimpleType();

    Object generateFieldObject(Class<?> clz, String fieldName, String referedCSVValue);

    String generateObjectValue(Object obj, String csvPath, boolean isSimple);

    Class<?> getItemClass(Type collectionItemType, Class<?> clz);

    void setObjectValue(Object collectionObject, Object value, String originalValue, int index);
}
