/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.flume.serialization;
@SuppressWarnings("all")
@org.apache.avro.specific.AvroGenerated
public class TransferStateFileMetaNew extends org.apache.avro.specific.SpecificRecordBase implements org.apache.avro.specific.SpecificRecord {
    public static final org.apache.avro.Schema SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"TransferStateFileMetaNew\",\"namespace\":\"org.apache.flume.serialization\",\"fields\":[{\"name\":\"offset\",\"type\":\"long\"}]}");
    @Deprecated public long offset;

    /**
     * Default constructor.
     */
    public TransferStateFileMetaNew() {}

    /**
     * All-args constructor.
     */
    public TransferStateFileMetaNew(Long offset) {
        this.offset = offset;
    }

    public org.apache.avro.Schema getSchema() { return SCHEMA$; }
    // Used by DatumWriter.  Applications should not call.
    public Object get(int field$) {
        switch (field$) {
            case 0: return offset;
            default: throw new org.apache.avro.AvroRuntimeException("Bad index");
        }
    }
    // Used by DatumReader.  Applications should not call.
    @SuppressWarnings(value="unchecked")
    public void put(int field$, Object value$) {
        switch (field$) {
            case 0: offset = (Long)value$; break;
            default: throw new org.apache.avro.AvroRuntimeException("Bad index");
        }
    }

    /**
     * Gets the value of the 'offset' field.
     */
    public Long getOffset() {
        return offset;
    }

    /**
     * Sets the value of the 'offset' field.
     * @param value the value to set.
     */
    public void setOffset(Long value) {
        this.offset = value;
    }

    /** Creates a new TransferStateFileMetaNew RecordBuilder */
    public static Builder newBuilder() {
        return new Builder();
    }

    /** Creates a new TransferStateFileMetaNew RecordBuilder by copying an existing Builder */
    public static Builder newBuilder(Builder other) {
        return new Builder(other);
    }

    /** Creates a new TransferStateFileMetaNew RecordBuilder by copying an existing TransferStateFileMetaNew instance */
    public static Builder newBuilder(TransferStateFileMetaNew other) {
        return new Builder(other);
    }

    /**
     * RecordBuilder for TransferStateFileMetaNew instances.
     */
    public static class Builder extends org.apache.avro.specific.SpecificRecordBuilderBase<TransferStateFileMetaNew>
            implements org.apache.avro.data.RecordBuilder<TransferStateFileMetaNew> {

        private long offset;

        /** Creates a new Builder */
        private Builder() {
            super(TransferStateFileMetaNew.SCHEMA$);
        }

        /** Creates a Builder by copying an existing Builder */
        private Builder(Builder other) {
            super(other);
        }

        /** Creates a Builder by copying an existing TransferStateFileMetaNew instance */
        private Builder(TransferStateFileMetaNew other) {
            super(TransferStateFileMetaNew.SCHEMA$);
            if (isValidValue(fields()[0], other.offset)) {
                this.offset = data().deepCopy(fields()[0].schema(), other.offset);
                fieldSetFlags()[0] = true;
            }
        }

        /** Gets the value of the 'offset' field */
        public Long getOffset() {
            return offset;
        }

        /** Sets the value of the 'offset' field */
        public Builder setOffset(long value) {
            validate(fields()[0], value);
            this.offset = value;
            fieldSetFlags()[0] = true;
            return this;
        }

        /** Checks whether the 'offset' field has been set */
        public boolean hasOffset() {
            return fieldSetFlags()[0];
        }

        /** Clears the value of the 'offset' field */
        public Builder clearOffset() {
            fieldSetFlags()[0] = false;
            return this;
        }

        @Override
        public TransferStateFileMetaNew build() {
            try {
                TransferStateFileMetaNew record = new TransferStateFileMetaNew();
                record.offset = fieldSetFlags()[0] ? this.offset : (Long) defaultValue(fields()[0]);
                return record;
            } catch (Exception e) {
                throw new org.apache.avro.AvroRuntimeException(e);
            }
        }
    }
}