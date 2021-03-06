/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.ketal.webservice.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.codahale.metrics.annotation.Timed;
import com.github.ketal.webservice.configuration.BaseWebserviceConfig;
import com.github.ketal.webservice.exception.NotModifiedException;
import com.github.ketal.webservice.util.WSResponse;

public abstract class AppConfigResource<T extends BaseWebserviceConfig> {

    @Context Request request;
    
    protected abstract T getConfig();
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Timed
    public Response getAppConfig(@QueryParam("pretty") boolean pretty) throws NotModifiedException {
        return WSResponse.response(request, getConfig(), Status.OK);
    }
    
}
