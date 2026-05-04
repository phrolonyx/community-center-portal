// Correspond exactement à ActivityResponseDto.java
export interface Activity {
  id: number;
  name: string;
  description?: string;
  activityImagePath?: string;
  imageFile?: File;
  sessions?: SessionInfo[];
}

// Correspond à ActivityResponseDto.SessionInfo et ActivitySessionResponseDto
export interface SessionInfo {
  id: number;
  dateTimeStart: string;
  dateTimeEnd: string;
  capacity: number;
  locationName?: string;
  hostFullName?: string;
  minAge?: number;
  maxAge?: number;
}
