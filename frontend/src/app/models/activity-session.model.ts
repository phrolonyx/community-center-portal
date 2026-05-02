export class ActivitySession {
  constructor(
    public id: number,
    public dateTimeStart: string,
    public dateTimeEnd: string,
    public capacity: number,

    public activityId: number,
    public activityName: string,
    public locationId: number,
    public locationName: string,
    public hostId: number,
    public hostFullName: string,

    public minAge: number,
    public maxAge: number
  ) {}
}
