/*
 * Copyright (C) 2003-2014 eXo Platform SAS.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
  
package org.exoplatform.calendar.ws.common;

import java.util.Map;
import java.util.Set;

public interface RestSecurityService {

  public static final String NOBODY = "Nobody";

  /**
   * Return TRUE if no permission configured for this request uri, 
   * OR user has permission configured with that uri <br/>
   * For example: if no permission is configured, any user can access any rest resource <br/>
   * But if the rest uri: /rest/calendar is configured with *:/platform/admins --> only admin group can access that resource <br/>
   * If the request uri match more than one configured uri pattern, the first one has higher priority <br/>
   * Super user has permission to access all resources. No matter how permission is configured
   */ 
  public boolean hasPermission(String requestPath);

  /**
   * Map a path regex pattern to a permission <br/>
   * This method keep order of added regex. First configured regex has higher priority <br/>
   * @param permissions contains multiple permission separated by comma. For example: 
   * manager:/platform/users,*:/platform/administrators  
   */
  public void addPermission(String pathPattern, String permissions);
  
  public void removePermission(String pathPattern);
  
  public Map<String, Set<String>> getPermissions();
  
  /**
   * Remove all restricted rule, allow all users access all resources
   */
  public void allowAll();
  
  /**
   * Deny all users (except supper user) access any resources
   */
  public void denyAll();
}
