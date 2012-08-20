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
package org.apache.creadur.whisker.scan;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.TreeSet;

/**
 * Scans directories for resources, within a file system.
 */
public class FromFileSystem {

    /**
     * Base constructor.
     */
    public FromFileSystem() {
        super();
    }

    /**
     * Builds description based on given directory.
     * @param base names the base directory, not null
     * @return collected directories within the base, not null
     * @throws IOException when the scanning fails
     */
    public Collection<Directory> withBase(final String base)
            throws IOException {
        return new Builder(base).build();
    }

    /**
     * Builds a description of a file system.
     */
    private final static class Builder {
        /** Initial capacity for the backing array. */
        private static final int DEFAULT_INITIAL_CAPACITY = 64;
        /** Directory scanning base. */
        private final File base;
        /** Directories scanned. */
        private final Set<Directory> directories;
        /** Queues work not yet complete. */
        private final Queue<Work> workInProgress;
        /** Stores work done. */
        private final Collection<Work> workDone;

        /**
         * Constructs a builder with given base
         * (and default backing array).
         * @param base not null
         */
        public Builder(final String base) {
            this(base, DEFAULT_INITIAL_CAPACITY);
        }

        /**
         * Constructs a builder.
         * @param base not null
         * @param initialCapacity initial capacity for backing array
         */
        public Builder(final String base, final int initialCapacity) {
            super();
            this.base = new File(base);
            directories = new TreeSet<Directory>();
            workInProgress = new LinkedList<Work>();
            workDone = new ArrayList<Work>(initialCapacity);
        }

        /**
         * Builds directories.
         * @return not null
         * @throws IOException when scanning fails
         */
        public Collection<Directory> build() throws IOException {
            put(base).andWork().untilDone();
            return directories;
        }

        /**
         * Waiting until work done.
         */
        private void untilDone() { }

        /**
         * Adds file work to the queue.
         * @param file not null
         * @return this, not null
         */
        private Builder put(final File file) {
            return put(new Work(file));
        }

        /**
         * Queues work.
         * @param work not null
         * @return this, not null
         */
        private Builder put(final Work work) {
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
         * Notes that work has already been done.
         * @param work not null
         */
        private void alreadyDone(final Work work) {
            System.out.println("Already done " + work);
        }

        /**
         * Starts work.
         * @return this, not null
         */
        private Builder andWork() {
            while (!workInProgress.isEmpty()) {
                workDone.add(workOn(workInProgress.poll()));
            }
            return this;
        }

        /**
         * Performs work.
         * @param next not null
         * @return the work done, not null
         */
        private Work workOn(final Work next) {
            for (final String name: next.contents()) {
                put(next.whenDirectory(name));
            }
            directories.add(next.build());
            return next;
        }

        /**
         * Computes the contents of a directory.
         */
        private static final class Work {
            /** Represents base directory. */
            private static final String BASE_DIRECTORY = ".";
            /** Names the directory. */
            private final String name;
            /** The directory worked on. */
            private final File file;

            /**
             * Constructs work.
             * @param file not null
             */
            public Work(final File file) {
                this(BASE_DIRECTORY, file);
            }

            /**
             * Constructs work.
             * @param name not null
             * @param file not null
             */
            public Work(final String name, final File file) {
                if (!file.exists()) {
                    throw new IllegalArgumentException(
                            "Expected '" + file.getAbsolutePath() + "' to exist");
                }
                if (!file.isDirectory()) {
                    throw new IllegalArgumentException(
                            "Expected '" + file.getAbsolutePath() + "' to be a directory");
                }
                this.name = name;
                this.file = file;
            }

            /**
             * Gets the contents of the work directory.
             * @return not null
             */
            public String[] contents() {
                final String[] contents = file.list();
                if (contents == null) {
                    throw new IllegalArgumentException("Cannot list content of " + file);
                }
                return contents;
            }

            /**
             * Builds a directory.
             * @return not null
             */
            public Directory build() {
                final Directory result = new Directory().setName(name);
                for (final String name : contents()) {
                    if (isResource(name)) {
                        result.addResource(name);
                    }
                }
                return result;
            }

            /**
             * Is the named file a resource?
             * @param name not null
             * @return true when the named file is a resource,
             * false otherwise
             */
            private boolean isResource(final String name) {
                return !isDirectory(name);
            }

            /**
             * Is the named file a directory?
             * @param name not null
             * @return true when the named file is a directory,
             * false otherwise
             */
            private boolean isDirectory(final String name) {
                return file(name).isDirectory();
            }

            /**
             * Creates new work.
             * @param name not null
             * @return work for the named directory,
             * or null when the resource named is not a directory
             */
            public Work whenDirectory(final String name) {
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
             * Converts a name to a path relative to base.
             * @param name not null
             * @return not null
             */
            private String path(final String name) {
                final String result;
                if (isBaseDirectory()) {
                    result = name;
                } else {
                    result = this.name + "/" + name;
                }
                return result;
            }

            /**
             * This the work done in the base directory.
             * @return true when this is the base, false otherwise.
             */
            private boolean isBaseDirectory() {
                return BASE_DIRECTORY.equals(this.name);
            }

            /**
             * Creates a file.
             * @param name not null
             * @return file with given name
             */
            private File file(String name) {
                return new File(this.file, name);
            }

            /**
             * Computes some suitable hash.
             * @return a hash code
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
             * Equal when both name and file are equal.
             * @param obj possibly null
             * @return true when equal, false otherwise
             * @see java.lang.Object#equals(java.lang.Object)
             */
            @Override
            public boolean equals(final Object obj) {
                if (this == obj) {
                    return true;
                }
                if (obj == null) {
                    return false;
                }
                if (getClass() != obj.getClass()) {
                    return false;
                }
                final Work other = (Work) obj;
                if (file == null) {
                    if (other.file != null)
                        return false;
                } else if (!file.equals(other.file))
                    return false;
                if (name == null) {
                    if (other.name != null) {
                        return false;
                    }
                } else if (!name.equals(other.name)) {
                    return false;
                }
                return true;
            }

            /**
             * Something suitable for logging.
             * @return not null
             * @see java.lang.Object#toString()
             */
            @Override
            public String toString() {
                return "Work [name=" + name + ", file=" + file + "]";
            }
        }
    }
}
