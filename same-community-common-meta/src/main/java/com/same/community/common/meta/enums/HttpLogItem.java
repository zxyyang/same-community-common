package com.same.community.common.meta.enums;

/**
 * all related info for a http call
 *
 * <p>
 * Copyright (C) 2015 Shanghai Shuhe Co., Ltd. All rights reserved.
 * <p>
 * No parts of this file may be reproduced or transmitted in any form or by any means,
 * electronic, mechanical, photocopying, recording, or otherwise, without prior written
 * permission of Shanghai Shuhe Co., Ltd.
 *
 * @author machao
 * @date 2018-10-30 上午11:51
 */


public enum HttpLogItem {
    userId,
    requestUri, requestMethod, requestApi, requestContentType, requestId,
    requestParameters,
    requestHeaders,
    requestBody,
    requestTime, responseStatus, responseContentType, responseBody, responseInterval, remoteAddr, userAgent,
    connectTimeout, socketTimeout, exceptionMessage,
    traceId, spanId

}
