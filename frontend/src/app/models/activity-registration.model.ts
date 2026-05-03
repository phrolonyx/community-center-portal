export class ActivityRegistration {
  constructor(
    public id: number,
    public status: string,
    public createdAt: string,
    public confirmedAt: string | null,
    public cancelledAt: string | null,

    public childId: number,
    public childFullName: string,

    public activitySessionId: number,
    public activityName: string,
    public sessionStart: string,
    public locationName: string,

    public parentProfileId: number
  ) {}
}
