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

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Pattern;

import org.exoplatform.container.component.BaseComponentPlugin;
import org.exoplatform.container.xml.InitParams;
import org.exoplatform.container.xml.PropertiesParam;
import org.exoplatform.container.xml.Property;
import org.exoplatform.management.annotations.Impact;
import org.exoplatform.management.annotations.ImpactType;
import org.exoplatform.management.annotations.Managed;
import org.exoplatform.management.annotations.ManagedDescription;
import org.exoplatform.management.annotations.ManagedName;
import org.exoplatform.management.jmx.annotations.NameTemplate;
import org.exoplatform.management.rest.annotations.RESTEndpoint;
import org.exoplatform.portal.config.UserACL;
import org.infinispan.util.concurrent.ConcurrentHashSet;

@Managed
@ManagedDescription("Rest Security Service")
@NameTemplate({@org.exoplatform.management.jmx.annotations.Property(key = "service", value = "security"), 
  @org.exoplatform.management.jmx.annotations.Property(key = "type", value = "rest") })
@RESTEndpoint(path = "restSecurityService")
public class RestSecurityServiceImpl implements RestSecurityService {

  private Map<String, Set<String>> permissions = new LinkedHashMap<String, Set<String>>();

  private Map<String, Pattern> patterns = new LinkedHashMap<String, Pattern>();

  private UserACL userACL;

  public static String NOBODY = "Nobody";

  public RestSecurityServiceImpl(UserACL userACL) {
    this.userACL = userACL;
  }

  @Managed
  @ManagedDescription("Check for permission")
  public boolean hasPermission(@ManagedDescription("rest path") @ManagedName("requestPath")  String requestPath) {    
    if (requestPath != null) {
      //Nomalize the request path
      requestPath = requestPath.trim();
      if (requestPath.length() > 1 && requestPath.charAt(requestPath.length() - 1) == '/') {
        requestPath = requestPath.substring(0, requestPath.length() - 1);
      }
      
      for (Entry<String, Pattern> entry : patterns.entrySet()) {
        if (entry.getValue().matcher(requestPath).matches()) {
          for (String permission : permissions.get(entry.getKey())) {
            //Remove nobody permission checking after upgrading to gatein 3.7.x
            permission = NOBODY.equalsIgnoreCase(permission) ? null : permission;
            
            if (userACL.hasPermission(permission)) {
              return true;
            }
          }
          
          //Request matched but user don't have permission
          return false;
        }
      }
    }

    //No permission configured for that request path
    return true;
  }

  public void addPermission(PermissionConfig config) {
    Map<String, String> perConfig = config.getConfig();
    for (String path : perConfig.keySet()) {      
      addPermission(path, perConfig.get(path));
    }
  }
  
  @Managed
  @ManagedDescription("Add permission")
  @Impact(ImpactType.WRITE)
  public void addPermission(@ManagedDescription("rest path") @ManagedName("pathRegex") String pathRegex, 
                            @ManagedDescription("permission") @ManagedName("permission") String permission) {
    pathRegex = pathRegex.trim();
    Set<String> lst = permissions.get(pathRegex);
    if (lst == null) {
      synchronized (permissions) {
        if (lst == null) {
          lst = new ConcurrentHashSet<String>();
          permissions.put(pathRegex, lst);
          patterns.put(pathRegex, Pattern.compile(pathRegex));
        }
      }
    }

    for (String str : permission.split(",")) {
      lst.add(str.trim());
    }
  }
  
  @Managed
  @ManagedDescription("Remove permission")
  @Impact(ImpactType.WRITE)
  public void removePermission(@ManagedDescription("rest path") @ManagedName("pathRegex") String pathRegex) {
    permissions.remove(pathRegex);
    patterns.remove(pathRegex);
  }
  
  @Managed
  @ManagedDescription("Get Permissions")
  public Map<String, Set<String>> getPermissions() {
    return permissions;
  }
  
  @Managed
  @ManagedDescription("Allow all")
  @Impact(ImpactType.WRITE)
  public void allowAll() {
    permissions.clear();
    patterns.clear();
  }
  
  @Managed
  @ManagedDescription("Deny all")
  @Impact(ImpactType.WRITE)
  public void denyAll() {
    permissions.clear();
    patterns.clear();
    //Should use userACL.NOBODY constant of gatein 3.7.x
    addPermission(".*", NOBODY);
  }

  public static class PermissionConfig extends BaseComponentPlugin {
    private Map<String, String> config = new HashMap<String, String>();
    
    public PermissionConfig(InitParams params) {
      if (params != null) {
        Iterator<PropertiesParam> iter = params.getPropertiesParamIterator();
        while (iter.hasNext()) {
          PropertiesParam param = iter.next();
          Iterator<Property> props = param.getPropertyIterator();
          while (props.hasNext()) {
            Property permission = props.next();
            config.put(permission.getName(), permission.getValue());
          }
        }
      }
    }
    
    public Map<String, String> getConfig() {
      return config;
    }
  }
}
