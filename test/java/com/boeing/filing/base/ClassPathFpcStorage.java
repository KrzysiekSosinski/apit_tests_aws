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

package com.boeing.filing.base;

import com.boeing.filing.fpc.FpcResource;
import com.boeing.filing.fpc.FpcStorage;
import org.springframework.core.io.ClassPathResource;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.UUID;

final class ClassPathFpcStorage implements FpcStorage {

    @Override
    public Optional<FpcResource> getCalculatedFlightPlan(UUID transactionId) {
        ClassPathResource classPathResource = new ClassPathResource("samples/" + transactionId);
        try {
            String content = classPathResource.getContentAsString(StandardCharsets.UTF_8);
            return Optional.of(new FpcResource(content, URI.create("https://jamamakuba.dev")));
        } catch (FileNotFoundException ex) {
            return Optional.empty();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
