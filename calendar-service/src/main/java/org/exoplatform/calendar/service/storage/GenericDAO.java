/**
 * Copyright (C) 2015 eXo Platform SAS.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see<http://www.gnu.org/licenses/>.
 **/
  
package org.exoplatform.calendar.service.storage;

import org.exoplatform.calendar.service.CalendarType;

public interface GenericDAO<T, ID> {
  
  /**
   * @param id
   * @return
   */
  T getById(ID id);

  /**
   * @param id
   * @param calType
   * @return
   */
  T getById(ID id, CalendarType calType);


  /**
   * @param object
   * @param isNew
   */
  T save(T object, boolean isNew);

  /**
   * @param id
   * @param calType
   * @return 
   */
  T remove(ID id, CalendarType calType);

  /**
   * @param type
   * @return
   */
  T newInstance(CalendarType type);
}
