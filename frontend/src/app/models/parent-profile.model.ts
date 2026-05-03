export class ParentProfile {
  constructor(
    public id: number,
    public phoneNumber: string,
    public addressLine1: string,
    public city: string,
    public province: string,
    public postalCode: string,
    public emergencyName: string,
    public emergencyPhone: string,
    public emergencyRelationship: string
  ) {}
}
