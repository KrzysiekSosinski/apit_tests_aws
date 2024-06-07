/*
 * The Boeing Company. Confidential & Proprietary
 * This work contains valuable confidential and proprietary information.
 * Disclosure, use or reproduction outside of The Boeing Company
 * is prohibited except as authorized in writing.  This unpublished work
 * is protected by the laws of the United States and other countries.
 * In the event of publication, the following notice shall apply:
 *
 * Copyright (c) 2024 The Boeing Company. All Rights Reserved.
 */

package com.boeing.filing.eurocontrol;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlType;
import org.junit.jupiter.api.Test;
import org.springframework.http.converter.HttpMessageConversionException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class EurocontrolXmlMapperTest {

    private final EurocontrolXmlMapper eurocontrolXmlMapper = new EurocontrolXmlMapper();

    @Test
    void readShouldReturnErrorIfClassIsNotFromEurocontrol() {
        assertThatThrownBy(() -> eurocontrolXmlMapper.read(SomeClass.class, "")).isInstanceOf(HttpMessageConversionException.class);
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType
    class SomeClass {

        private String test;

        public String getTest() {
            return test;
        }

        public void setTest(String test) {
            this.test = test;
        }
    }
}
