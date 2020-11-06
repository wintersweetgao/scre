/**
 * Copyright (c) 2008-2013, http://www.snakeyaml.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hljcj.acts.utils.yaml.tokens;

import com.hljcj.acts.utils.yaml.error.Mark;

public final class TagToken extends Token {
    private final TagTuple value;

    /**
     * Constructor.
     *
     * @param value the value
     * @param startMark the start mark
     * @param endMark the end mark
     */
    public TagToken(TagTuple value, Mark startMark, Mark endMark) {
        super(startMark, endMark);
        this.value = value;
    }

    /**
     * Gets value.
     *
     * @return the value
     */
    public TagTuple getValue() {
        return this.value;
    }

    @Override
    protected String getArguments() {
        return "value=[" + value.getHandle() + ", " + value.getSuffix() + "]";
    }

    /**
     * Gets token id.
     *
     * @return the token id
     */
    @Override
    public Token.ID getTokenId() {
        return ID.Tag;
    }
}
