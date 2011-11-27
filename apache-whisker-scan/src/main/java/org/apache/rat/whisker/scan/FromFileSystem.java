/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License. 
 */
package org.apache.rat.whisker.scan;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.TreeSet;



/**
 * 
 */
public class FromFileSystem {
    
    /**
     * @param base
     */
    public FromFileSystem() {
        super();
    }

    public Collection<Directory> withBase(final String base) throws IOException {
        return new Builder(base).build();
    }
    
    private static class Builder {
        /**
         * 
         */
        private static final int DEFAULT_INITIAL_CAPACITY = 64;
        
        private final File base;
        private final Set<Directory> directories;
        private final Queue<Work> workInProgress;
        private final Collection<Work> workDone;
        
        /**
         * @param base
         */
        public Builder(String base) {
            this(base, DEFAULT_INITIAL_CAPACITY);
        }

        /**
         * @param base
         */
        public Builder(String base, int initialCapacity) {
            super();
            this.base = new File(base);
            directories = new TreeSet<Directory>();
            workInProgress = new LinkedList<Work>();
            workDone = new ArrayList<Work>(initialCapacity);
        }
        
        public Collection<Directory> build() throws IOException {
            put(base).andWork().untilDone();
            return directories;
        }

        /**
         * 
         */
        private void untilDone() {
            // TODO: wait until all threads done
        }

        private Builder put(File file) {
            return put(new Work(file));
        }
        
        /**
         * @param base
         */
        private Builder put(Work work) {
            if (work != null) {
                if (workDone.contains(work)) {
                    alreadyDone(work);
                } else {
                    this.workInProgress.add(work);
                }
            }
            return this;
        }
        
        /**
         * @param work
         */
        private void alreadyDone(Work work) {
            System.out.println("Already done " + work);
        }

        private Builder andWork() {
            while (!workInProgress.isEmpty()) {
                workDone.add(workOn(workInProgress.poll()));
            }
            return this;
        }

        /**
         * @param poll
         * @return
         */
        private Work workOn(final Work next) {
            for (final String name: next.contents()) {
                put(next.whenDirectory(name));
            }
            directories.add(next.build());
            return next;
        }
        
        
        private static class Work {
            
            private static final String BASE_DIRECTORY = ".";
            private final String name;
            private final File file;
            
            /**
             * @param file
             */
            public Work(File file) {
                this(BASE_DIRECTORY, file);
            }

            /**
             * @param name
             * @param file
             */
            public Work(String name, File file) {
                if (! file.exists()) {
                    throw new IllegalArgumentException("Expected '"+ file.getAbsolutePath() + "' to exist");
                }
                if (! file.isDirectory()) {
                    throw new IllegalArgumentException("Expected '"+ file.getAbsolutePath() + "' to be a directory");
                }
                this.name = name;
                this.file = file;
            }
            
            public String[] contents() {
                final String[] contents = file.list();
                if (contents == null) {
                    throw new IllegalArgumentException("Cannot list content of " + file);
                }
                return contents;
            }

            /**
             * @return
             */
            public Directory build() {
                final Directory result = new Directory().setName(name);
                for (final String name: contents()) {
                    if (isResource(name)) {
                        result.addResource(name);
                    }
                }
                return result;
            }

            /**
             * @param name
             * @return
             */
            private boolean isResource(final String name) {
                return !isDirectory(name);
            }

            /**
             * @param name
             * @return
             */
            private boolean isDirectory(final String name) {
                return file(name).isDirectory();
            }
            
            /**
             * @param name
             * @return
             */
            public Work whenDirectory(String name) {
                final File file = file(name);
                final Work result;
                if (file.isDirectory()) {
                    result = new Work(path(name), file);
                } else {
                    result = null;
                }
                return result;
            }

            /**
             * @param name
             * @return
             */
            private String path(String name) {
                final String result;
                if (isBaseDirectory()) {
                    result = name;
                } else {
                    result = this.name + "/" + name;
                }
                return result;
            }

            /**
             * @return
             */
            private boolean isBaseDirectory() {
                return BASE_DIRECTORY.equals(this.name);
            }

            /**
             * @param name
             * @return
             */
            private File file(String name) {
                return new File(this.file, name);
            }

            /**
             * @see java.lang.Object#hashCode()
             */
            @Override
            public int hashCode() {
                final int prime = 31;
                int result = 1;
                result = prime * result
                        + ((file == null) ? 0 : file.hashCode());
                result = prime * result
                        + ((name == null) ? 0 : name.hashCode());
                return result;
            }
            
            /**
             * @see java.lang.Object#equals(java.lang.Object)
             */
            @Override
            public boolean equals(Object obj) {
                if (this == obj)
                    return true;
                if (obj == null)
                    return false;
                if (getClass() != obj.getClass())
                    return false;
                Work other = (Work) obj;
                if (file == null) {
                    if (other.file != null)
                        return false;
                } else if (!file.equals(other.file))
                    return false;
                if (name == null) {
                    if (other.name != null)
                        return false;
                } else if (!name.equals(other.name))
                    return false;
                return true;
            }
            
            /**
             * @see java.lang.Object#toString()
             */
            @Override
            public String toString() {
                return "Work [name=" + name + ", file=" + file + "]";
            }
        }
    }
}
