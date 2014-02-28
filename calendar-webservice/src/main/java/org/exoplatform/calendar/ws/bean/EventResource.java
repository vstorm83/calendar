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

package org.exoplatform.calendar.ws.bean;

import static org.exoplatform.calendar.ws.CalendarRestApi.CALENDAR_URI;
import static org.exoplatform.calendar.ws.CalendarRestApi.CAL_BASE_URI;
import static org.exoplatform.calendar.ws.CalendarRestApi.CATEGORY_URI;
import static org.exoplatform.calendar.ws.CalendarRestApi.EVENT_URI;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.exoplatform.calendar.service.Attachment;
import org.exoplatform.calendar.service.CalendarEvent;
import org.exoplatform.calendar.service.Reminder;
import org.exoplatform.commons.utils.ISO8601;
import org.exoplatform.webservice.cs.bean.End;

public class EventResource<T extends Serializable> extends Resource {
  private static final long serialVersionUID = 9085055105843346382L;
  
  private String            subject;
  private String                    description;
  private Date                      from;
  private Date                      to;
  private T                    calendar;
  private String[]                  categories;
  private String                    location;
  private String                       priority;
  private RepeatResource            repeat;
  private String                    recurrenceId;
  private String                    originalEvent;
  private Reminder[]                reminder;
  private Attachment[]              attachments;
  private String[]                  participants;
  private String                    privacy;
  private String                    availability;  

  public EventResource() {}
  
  public EventResource(CalendarEvent data) {
    setId(data.getId());
    setHref(new StringBuilder(CAL_BASE_URI).append(EVENT_URI).append(data.getId()).toString());
    subject = data.getSummary();
    description = data.getDescription();
    from = data.getFromDateTime();
    to = data.getToDateTime();
    calendar = (T) new StringBuilder(CAL_BASE_URI).append(CALENDAR_URI)
                                             .append(data.getCalendarId())
                                             .toString();
    categories = new String[] { new StringBuilder(CAL_BASE_URI).append(CATEGORY_URI)
                                                              .append(data.getEventCategoryId())
                                                              .toString() };
    location = data.getLocation();
    this.priority = data.getPriority();

    End end;
    if (data.getRepeatUntilDate() != null) {
      java.util.Calendar tmp = java.util.Calendar.getInstance();
      tmp.setTime(data.getRepeatUntilDate());      
      end = new End(data.getRepeatType(), ISO8601.format(tmp));      
    } else {
      end = new End(data.getRepeatType(), String.valueOf(data.getRepeatCount()));
    }    
    
    StringBuilder repeatByMonthDay = new StringBuilder();
    if (data.getRepeatByMonthDay() != null) {
      for (long d : data.getRepeatByMonthDay()) {
        repeatByMonthDay.append(d).append(",");
      }
      if (repeatByMonthDay.length() > 0) {
        repeatByMonthDay.deleteCharAt(repeatByMonthDay.length() - 1);
      }      
    }
    
    boolean isRepeat = (data.getRepeatType() != null && !CalendarEvent.RP_NOREPEAT.equals(data.getRepeatType()));    
    repeat = new RepeatResource(isRepeat,
                                data.getRepeatType(),
                                (int)data.getRepeatInterval(),
                                StringUtils.join(data.getRepeatByDay(), ","),
                                repeatByMonthDay.toString(),
                                data.getExceptionIds(),
                                end);    
    recurrenceId = data.getOriginalReference();
    originalEvent = new StringBuffer(CAL_BASE_URI).append(EVENT_URI)
                                                  .append(data.getOriginalReference())
                                                  .toString();
    if (data.getReminders() != null)
      reminder = data.getReminders().toArray(new Reminder[] {});
    if (data.getAttachment() != null)
      attachments = data.getAttachment().toArray(new Attachment[] {});
    participants = data.getParticipant();
    privacy = data.getStatus();
    availability = data.getEventState();
  }

  public String getSubject() {
    return subject;
  }

  public void setSubject(String subject) {
    this.subject = subject;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Date getFrom() {
    return from;
  }

  public void setFrom(Date from) {
    this.from = from;
  }

  public Date getTo() {
    return to;
  }

  public void setTo(Date to) {
    this.to = to;
  }

  public T getCalendar() {
    return calendar;
  }

  public EventResource<T> setCal(T calendar) {
    this.calendar = calendar;
    return this;
  }

  public String[] getCategories() {
    return categories;
  }

  public void setCategories(String[] categories) {
    this.categories = categories;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public String getPriority() {
    return priority;
  }

  public void setPriority(String priority) {
    this.priority = priority;
  }

  public RepeatResource getRepeat() {
    return repeat;
  }

  public void setRepeat(RepeatResource repeat) {
    this.repeat = repeat;
  }

  public String getRecurrenceId() {
    return recurrenceId;
  }

  public void setRecurrenceId(String recurrenceId) {
    this.recurrenceId = recurrenceId;
  }

  public String getOriginalEvent() {
    return originalEvent;
  }

  public void setOriginalEvent(String originalEvent) {
    this.originalEvent = originalEvent;
  }

  public Reminder[] getReminder() {
    return reminder;
  }

  public void setReminder(Reminder[] reminder) {
    this.reminder = reminder;
  }

  public Attachment[] getAttachments() {
    return attachments;
  }

  public void setAttachments(Attachment[] attachments) {
    this.attachments = attachments;
  }

  public String[] getParticipants() {
    return participants;
  }

  public void setParticipants(String[] participants) {
    this.participants = participants;
  }

  public String getPrivacy() {
    return privacy;
  }

  public void setPrivacy(String privacy) {
    this.privacy = privacy;
  }

  public String getAvailability() {
    return availability;
  }

  public void setAvailability(String availability) {
    this.availability = availability;
  }  
}