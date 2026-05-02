export class User {
  constructor(
    public id: number,
    public email: string,
    public firstName: string,
    public lastName: string,
    public roleName: string,
    public enabled: boolean,
    public pending: boolean,
    public createdAt: string,
    public lastLogin: string
  ) {}
}
