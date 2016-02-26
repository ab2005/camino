/*
 * Copyright (c) 2016  ab2005@gmail.com
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

package com.example.abarilov.mithrillmodule.core;

/**
 * Created by abarilov on 2/19/16.
 */

public interface Mithrill {

    public class O extends Object {}

    public interface Module {
        O controller();
        O view();
    }

    public interface Controller {
    }

    public interface View {
    }


    public interface Todo {
        O description();
        Todo description(Object o);
        O done();
        Todo done(O o);
    }

    public static class TodoImpl implements Todo {
        @Override
        public O description() {
            return null;
        }

        @Override
        public Todo description(Object o) {
            return null;
        }

        @Override
        public O done() {
            return null;
        }

        @Override
        public Todo done(O o) {
            return null;
        }
    }   
}

