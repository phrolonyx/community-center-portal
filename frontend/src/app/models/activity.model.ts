export class Activity {
  constructor(public id: number,
              public name: string,
              public description?: string,
              public activityImagePath?: string,
              public sessions?: SessionInfo[]
              ) {}
}

export class SessionInfo {
  id!: number;
  dateTimeStart!: string;
  dateTimeEnd!: string;
  capacity!: number;
  locationName?: string;
  hostFullName?: string;
}
