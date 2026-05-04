// Correspond exactement à ActivityResponseDto.java
export interface Activity {
  id: number;
  name: string;
  description: string | null;
  activityImagePath: string | null;
  sessions: SessionInfo[];
}

// Correspond à ActivityResponseDto.SessionInfo et ActivitySessionResponseDto
export interface SessionInfo {
  id: number;
  dateTimeStart: string;
  dateTimeEnd: string;
  capacity: number;
  locationName: string | null;
  hostFullName: string | null;
  minAge: number | null;
  maxAge: number | null;
}
