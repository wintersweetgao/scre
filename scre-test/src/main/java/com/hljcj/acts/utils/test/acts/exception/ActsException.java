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
package com.hljcj.acts.utils.test.acts.exception;

/**
 * Test abnormal base class, exception declaration in the framework must inherit this interface
 * 
 * @author dasong.jds
 * @version $Id: ActsException.java, v 0.1 2015年4月19日 下午9:07:58 dasong.jds Exp $
 */
public class ActsException extends RuntimeException {

    private static final long serialVersionUID = 4670114874512893276L;

    /**
     * <code>ActsException</code> Constructor
     */
    public ActsException() {
        super();
    }

    /**
     * <code>ActsException</code> Constructor
     * 
     * @param message           exception description
     */
    public ActsException(String message) {
        super(message);
    }

    /**
     * <code>ActsException</code> Constructor
     * 
     * @param cause             exception reason
     */
    public ActsException(Throwable cause) {
        super(cause);
    }

    /**
     * <code>ActsException</code> Constructor
     * 
     * @param message           exception description
     * @param cause             exception reason
     */
    public ActsException(String message, Throwable cause) {
        super(message, cause);
    }

}
