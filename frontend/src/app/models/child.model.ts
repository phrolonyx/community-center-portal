export class Child {
  constructor(
    public id: number,
    public firstName: string,
    public lastName: string,
    public dateOfBirth: string | null,
    public age: number,
    public medicalConditions?: MedicalInfo[]
  ) {}
}

export class MedicalInfo {
  constructor(
    public id?: number,
    public condition?: string,
    public severity?: string,
    public notes?: string
  ) {}
}
