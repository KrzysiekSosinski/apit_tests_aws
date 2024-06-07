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

package com.boeing.filing.security;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;

public class SecurityUtil {

    private static final String token = "eyJraWQiOiI0IiwiYWxnIjoiRVMyNTYifQ.eyJhdWQiOiJDT01NT04tSldUIiwiaWZzIjp7InNwQXR0cnMiOm51bGwsIm1haW51cG4iOm51bGwsIm1haW5vcmdhbml6YXRpb25pZCI6bnVsbCwiYXNzdXJhbmNlc2NvcmUiOm51bGwsImFzc3VyYW5jZWxldmVsIjpudWxsfSwiaXNzIjoiUGluZ0FjY2Vzc0F1dGhUb2tlbiIsImZwIjp7InN1YmplY3QiOiJ4NTA5RGV2aWNlVExTQWRhcHRlcjE6OkNOPUJvZWluZyBCYXNpYyBBc3N1cmFuY2UgU29mdHdhcmUgSXNzdWluZyBDQSBHMyxPVT1jZXJ0c2VydmVycyxPPUJvZWluZyxDPVVTOjo4NDc0Mjg4OTQyMDQzNzI5Mjk5OTk5MzY1NjE0ODQ4MzY1NTAwODM3MTk2MzYiLCJhdXRoblNvdXJjZSI6Ing1MDlEZXZpY2VUTFNBZGFwdGVyMSJ9LCJjZXJ0Ijp7InN1YnNpZGlhcnljb21wYW55SUQiOm51bGwsIklzc3VlciI6IkNOPUJvZWluZyBCYXNpYyBBc3N1cmFuY2UgU29mdHdhcmUgSXNzdWluZyBDQSBHMyxPVT1jZXJ0c2VydmVycyxPPUJvZWluZyxDPVVTIiwic3ViamVjdCI6IkNOPXRlc3RjbGllbnRjZXJ0LmZlZHAuZGlnaXRhbGF2aWF0aW9uc2VydmljZXMuY29tLE9VPXNlcnZlcnMsTz1Cb2VpbmcsQz1VUyIsInN1YnNpZGlhcnlJRCI6bnVsbCwiYm9laW5nQ29tcGFueUlEIjpudWxsLCJzZXJ2aWNlSUQiOiJGUFhfbW9ja194NTA5X2NsaWVudCIsInNlcmlhbE5vIjotMTU0MTYzODQ0NzQ0NDE1MTg1Mn0sImV4cCI6MTU4MTcwNDM2MSwiaWF0IjoxNTgxNzAzODJ9Cg==.SxQt5mLwUuJV29aHtCrji-qUxvm2amVIfOQCY3CcKExzph7WnfYP0tdHMkoEIL1I4zu2glZ0OomXs2kNHILS_w";

    public static <T> HttpEntity<T> authorized(T body) {
        return authorized(body, token, new HttpHeaders());
    }

    public static <T> HttpEntity<T> authorized(T body, String token, HttpHeaders additionalHeaders) {
        return new HttpEntity<>(body, authorizationHeader(token, additionalHeaders));
    }

    public static <T> HttpEntity<T> authorized(T body, HttpHeaders additionalHeaders) {
        return new HttpEntity<>(body, authorizationHeader(token, additionalHeaders));
    }

    private static HttpHeaders authorizationHeader(String token, HttpHeaders additionalHeaders) {
        return new HttpHeaders() {{
            add(AUTHORIZATION, "Bearer " + token);
            addAll(additionalHeaders);
        }};
    }
}
