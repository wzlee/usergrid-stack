/*******************************************************************************
 * Copyright 2012 Apigee Corporation
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package org.usergrid.services;


import static org.junit.Assert.assertNotNull;
import org.junit.Test;
import org.usergrid.persistence.Entity;


public class UsersServiceIT extends AbstractServiceIT
{
    @Test
    public void testPermissions() throws Exception
    {
        app.createRole("manager", null, 0);
        app.createRole("member", null, 0);

        app.grantRolePermission("admin", "users:access:*");
        app.grantRolePermission("admin", "groups:access:*");

        app.put( "username", "edanuff" );
        app.put( "email", "ed@anuff.com" );

        Entity user = app.create( "user" );
        assertNotNull( user );

        app.testRequest( ServiceAction.POST, 1, "users", user.getUuid(), "roles", "admin" );
        app.testRequest( ServiceAction.POST, 1, "users", user.getUuid(), "roles", "manager" );

        app.grantUserPermission( user.getUuid(), "users:access:*" );
        app.grantUserPermission( user.getUuid(), "groups:access:*" );

        app.testDataRequest( ServiceAction.GET, "users", user.getUuid(), "rolenames" );

        app.testDataRequest( ServiceAction.GET, "users", user.getUuid(), "permissions" );

        app.testDataRequest( ServiceAction.GET, "users", user.getUuid(), "roles", "admin", "permissions" );
    }
}
