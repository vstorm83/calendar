/**
 * Copyright (C) 2003-2014 eXo Platform SAS.
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
package org.exoplatform.calendar.service.impl;

import org.exoplatform.calendar.service.CalendarEvent;
import org.exoplatform.calendar.service.CalendarException;
import org.exoplatform.calendar.service.CalendarType;
import org.exoplatform.calendar.service.EventHandler;
import org.exoplatform.calendar.service.Invitation;
import org.exoplatform.calendar.service.QueryCondition;
import org.exoplatform.calendar.service.storage.EventDAO;
import org.exoplatform.commons.utils.ListAccess;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;

public class EventHandlerImpl implements EventHandler {

  private static Log      log = ExoLogger.getLogger(EventHandlerImpl.class);

  protected CalendarServiceImpl calSerVice;
  protected JCRDataStorage  storage;

  public EventHandlerImpl(CalendarServiceImpl service) {
    this.calSerVice = service;
    this.storage = service.getDataStorage();
  }

  @Override
  public CalendarEvent getEventById(String eventId, CalendarType calType) {
    EventDAO dao = getSupportedEventDAOs(calType);
    if (dao != null) {
      return dao.getById(eventId, calType);
    }
    return null;
  }

  @Override
  public CalendarEvent saveEvent(CalendarEvent event, boolean isNew) {
    EventDAO dao = getSupportedEventDAOs(event.getCalendarType());
    if (dao != null) {
      return dao.save(event, isNew);
    }

    return null;
  }

  @Override
  public CalendarEvent removeEvent(String eventId, CalendarType calendarType) {
    EventDAO dao = getSupportedEventDAOs(calendarType);
    if (dao != null) {
      return dao.remove(eventId, calendarType);
    }
    return null;
  }

  @Override
  public ListAccess<CalendarEvent> findEventsByQuery(QueryCondition eventQuery) throws CalendarException {
//    return new EventList(new EventNodeListAccess(this, eventQuery));
    return null;
  }
  
  @Override
  public ListAccess<Invitation> findInvitationsByQuery(QueryCondition query) {
//    return new InvitationListAccess(this, query);
    return null;
  }

  private EventDAO getSupportedEventDAOs(CalendarType type) {
    return calSerVice.getSupportedEventDAO(type);
  }
}
